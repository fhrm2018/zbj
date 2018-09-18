package com.qiyou.dhlive.prd.login.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qiyou.dhlive.core.bms.outward.model.BmsEmployeeInfo;
import com.qiyou.dhlive.core.bms.outward.model.BmsResource;
import com.qiyou.dhlive.core.bms.outward.model.BmsRoleResource;
import com.qiyou.dhlive.core.bms.outward.service.IBmsEmployeeInfoService;
import com.qiyou.dhlive.core.bms.outward.service.IBmsResourceService;
import com.qiyou.dhlive.core.bms.outward.service.IBmsRoleResourceService;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.qiyou.dhlive.prd.component.session.EmployeeSession;
import com.qiyou.dhlive.prd.component.util.EmployeeSessionUtil;
import com.qiyou.dhlive.prd.component.util.ValidateCodeUtil;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.EqualsUtil;
import com.yaozhong.framework.base.common.utils.MD5Util;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.web.annotation.session.UnSession;
import com.yaozhong.framework.web.mvc.session.HttpSessionTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


/**
 * Created by admin on 2017/8/24.
 */
@Controller
public class LoginController {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IBmsEmployeeInfoService bmsEmployeeInfoService;

    @Autowired
    private IBmsResourceService bmsResourceService;

    @Autowired
    private IBmsRoleResourceService bmsRoleResourceService;

    @UnSession
    @UnSecurity
    @RequestMapping(value = "")
    public String toCreateRoom(Model model) {
        return "login";
    }

    @UnSession
    @UnSecurity
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public DataResponse login(String phone, String password, String code, HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
        String pwd = MD5Util.MD5Encode(password, "utf-8");
        if (EmptyUtil.isEmpty(phone)) {
            return new DataResponse(1001, "手机号码不可为空");
        }
        if (EmptyUtil.isEmpty(password)) {
            return new DataResponse(1001, "登陆密码不能为空");
        }
        if (EmptyUtil.isEmpty(code)) {
            return new DataResponse(1001, "验证码不能为空");
        }
        HttpSession session = request.getSession();
        String validateCode = (String) session.getAttribute("validateCode");
        if (!code.equals(validateCode)) {
            return new DataResponse(1001, "验证码不正确");
        }
        session.setAttribute("validateCode", null);
        DataResponse loginResult = this.bmsEmployeeInfoService.loginByPhone(phone, pwd);
        if (loginResult.getCode().intValue() == 1000) {
            BmsEmployeeInfo employee = (BmsEmployeeInfo) loginResult.getData();
            EmployeeSession employeeSession = EmployeeSessionUtil.createEmployeeSessionByEmployee(employee);
            List<BmsResource> sysResourceList = bmsResourceService.getAllRecource();
            if ("admin".equals(employeeSession.getLoginName())) {
                employeeSession.setResources(sysResourceList);
            } else {
                String roleIds = employee.getRoleId();
                List<BmsResource> menus = Lists.newArrayList();
                if (EmptyUtil.isEmpty(roleIds)) {
                    employeeSession.setResources(menus);
                } else {
                    String[] roleIdArray = roleIds.split(",");
                    List<Object> roleIdList = Lists.newArrayList();
                    for (String roleId : roleIdArray) {
                        roleIdList.add(roleId);
                    }
                    //查询角色权限
                    BmsRoleResource resParam = new BmsRoleResource();
                    SearchCondition<BmsRoleResource> resCon = new SearchCondition<BmsRoleResource>(resParam);
                    resCon.buildInConditions("roleId", roleIdList);
                    List<BmsRoleResource> roleResList = this.bmsRoleResourceService.findByCondition(resCon);
                    List<BmsRoleResource> newRoleResList = Lists.newArrayList();
                    //排重
                    Map<Integer, Integer> resMap = Maps.newHashMap();
                    for (BmsRoleResource roleRes : roleResList) {
                        if (EmptyUtil.isEmpty(resMap.get(roleRes.getResourceId()))) {
                            newRoleResList.add(roleRes);
                            resMap.put(roleRes.getResourceId(), roleRes.getResourceId());
                        }
                    }
                    //添加到资源列表
                    for (BmsRoleResource roleRes : newRoleResList) {
                        for (BmsResource res : sysResourceList) {
                            if (EqualsUtil.equals(roleRes.getResourceId(), res.getId())) {
                                menus.add(res);
                                break;
                            }
                        }
                    }
                    employeeSession.setResources(menus);
                }
            }
            HttpSessionTool.doEmployeeLogin(session, employeeSession);
            return new DataResponse(1000, "登录成功", "user");
        } else {
            return loginResult;
        }
    }

    @UnSession
    @UnSecurity
    @RequestMapping(value = "/validateCode")
    public String validateCode(HttpServletRequest request, HttpServletResponse response, Integer width, Integer height) throws Exception {
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        HttpSession session = request.getSession();
        ValidateCodeUtil vCode = new ValidateCodeUtil(width, height, 4, 50);
        session.setAttribute("validateCode", vCode.getCode());
        vCode.write(response.getOutputStream());
        return null;
    }


    @UnSession
    @UnSecurity
    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    public ModelAndView loginOut(HttpServletResponse response) {
        //HttpSessionTool.doEmployeeOut(BmsSessionFilter.getHttpSession());
        ModelAndView mv = new ModelAndView("login");
        return mv;
    }

    @UnSecurity
    @RequestMapping("/editCurrentUserPwd")
    @ResponseBody
    public DataResponse editCurrentUserPwd(String oldPass, String newPass, String reNewPass, HttpServletRequest request) {
        if (EmptyUtil.isEmpty(oldPass)) {
            return new DataResponse(1001, "请输入原密码");
        }
        if (EmptyUtil.isEmpty(newPass)) {
            return new DataResponse(1001, "请输入新密码");
        }
        if (EmptyUtil.isEmpty(reNewPass)) {
            return new DataResponse(1001, "请再次输入新密码");
        }
        if (!reNewPass.equals(newPass)) {
            return new DataResponse(1001, "两次输入的密码不一致");
        }
        EmployeeSession eSession = EmployeeSession.getEmployeeSession();
        if (!MD5Util.MD5Encode(oldPass, "utf-8").equals(eSession.getPassword()))
            return new DataResponse(1001, "旧密码错误");
        else {
            BmsEmployeeInfo e = new BmsEmployeeInfo();
            e.setPassword(MD5Util.MD5Encode(newPass, "utf-8"));
            e.setId(eSession.getEmployeeId());
            this.bmsEmployeeInfoService.modifyEntity(e);
            HttpSession session = request.getSession();
            HttpSessionTool.doEmployeeOut(session);
            return new DataResponse();
        }
    }

}

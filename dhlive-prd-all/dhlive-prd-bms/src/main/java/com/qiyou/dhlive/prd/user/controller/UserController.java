package com.qiyou.dhlive.prd.user.controller;

import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.api.base.outward.service.IFileUploadRemoteService;
import com.qiyou.dhlive.api.base.outward.service.ILiveRoomApiService;
import com.qiyou.dhlive.api.base.outward.service.IUserInfoApiService;
import com.qiyou.dhlive.core.base.outward.model.BaseOptLog;
import com.qiyou.dhlive.core.base.outward.service.IBaseOptLogService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.bms.outward.model.BmsEmployeeInfo;
import com.qiyou.dhlive.core.live.outward.model.LiveRoom;
import com.qiyou.dhlive.core.user.outward.model.*;
import com.qiyou.dhlive.prd.component.annotation.ResourceAnnotation;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.qiyou.dhlive.prd.component.session.EmployeeSession;
import com.qiyou.dhlive.prd.component.util.ResourceBaseController;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.redis.RedisManager;
import com.yaozhong.framework.web.annotation.session.NeedSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/29
 */
@Controller
@RequestMapping(value = "user")
public class UserController {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IUserInfoApiService userInfoApiService;

    @Autowired
    private ILiveRoomApiService liveRoomApiService;

    @Autowired
    private IFileUploadRemoteService fileUploadRemoteService;

    @Autowired
    private IBaseOptLogService baseOptLogService;
    
    @Autowired
    private IBaseCacheService baseCacheService;
    
    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    @NeedSession
    @UnSecurity
    @RequestMapping("")
    public String index(Model model) {
    	String onlinenum=this.redisManager.getStringValueByKey(RedisKeyConstant.ONLINENUM + 4);
    	model.addAttribute("onlineNum",onlinenum );
        return "user/touristsUser";
    }

    @NeedSession
    @UnSecurity
    @RequestMapping("touristsUser")
    public String touristsUser(Model model) {
    	String onlinenum=this.redisManager.getStringValueByKey(RedisKeyConstant.ONLINENUM + 4);
    	model.addAttribute("onlineNum",onlinenum );
        return "user/touristsUser";
    }

    @NeedSession
    @UnSecurity
    @RequestMapping("onlineUser")
    @ResponseBody
    public DataResponse onlineUser(Model model) {
    	String onlinenum=this.redisManager.getStringValueByKey(RedisKeyConstant.ONLINENUM + 4);
    	model.addAttribute("onlineNum",onlinenum );
        return new DataResponse(1000,onlinenum);
    }
    
    @NeedSession
    @UnSecurity
    @RequestMapping("vipUser")
    public String vipUser(Model model) {
        //角色: 会员
        Object[] ids = {5};
        List<UserGroup> groups = this.userInfoApiService.getUserGroup(new UserGroup(), ids);
        model.addAttribute("groups", groups);
        //直播间
        List<LiveRoom> rooms = this.liveRoomApiService.getLiveRoom(new LiveRoom());
        model.addAttribute("rooms", rooms);
        return "user/vipUser";
    }

    @NeedSession("/user/manageUser")
    @UnSecurity
    @RequestMapping("manageUser")
    public String manageCus(Model model) {
        //角色:助理,老师
        Object[] ids = {3, 4};
        List<UserGroup> groups = this.userInfoApiService.getUserGroup(new UserGroup(), ids);
        model.addAttribute("groups", groups);
        //直播间
        List<LiveRoom> rooms = this.liveRoomApiService.getLiveRoom(new LiveRoom());
        model.addAttribute("room", rooms.get(0));
        return "user/manageUser";
    }

    @NeedSession
    @UnSecurity
    @RequestMapping("employeeUser")
    public String manageEmp(Model model) {
        return "user/employeeUser";
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * 游客列表(分页)
     *
     * @param pageSearch
     * @return
     */
    @NeedSession("/user/getTouristsUserList")
    @UnSecurity
    @RequestMapping("getTouristsUserList")
    @ResponseBody
    public DataResponse getTouristsUserList(PageSearch pageSearch, UserInfo params) {
        return this.userInfoApiService.getTouristsUserList(pageSearch, params);
    }

    /**
     * 游客禁言/解禁
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "setGag")
    @ResponseBody
    public DataResponse setGag(UserInfo params) {
        return this.userInfoApiService.setTouristsGag(params);
    }


    /**
     * 游客拉黑/取消拉黑
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "setBlack")
    @ResponseBody
    public DataResponse setBlack(UserInfo params) {
        DataResponse result = this.userInfoApiService.setTouristsBlack(params);
        if (result.getCode().intValue() == 1000) {
            //TODO 记录游客拉黑日志
            EmployeeSession eSession = EmployeeSession.getEmployeeSession();//操作人信息
            doLog(eSession.getEmployeeId(), params.getUserId(), params.getGroupId());
        }
        return result;
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * vip列表(分页)
     *
     * @param pageSearch
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping("getVipUserList")
    @ResponseBody
    public DataResponse getVipUserList(PageSearch pageSearch, UserVipInfo params) {
        EmployeeSession eSession = EmployeeSession.getEmployeeSession();
        if (!eSession.getRoleId().equals("1")) {
            params.setCreateUserId(eSession.getEmployeeId());
        }
        return this.userInfoApiService.getVipUserList(pageSearch, params);
    }

    /**
     * VIP用户保存
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "saveVipUser")
    @ResponseBody
    public DataResponse saveVipUser(HttpServletRequest request, UserVipInfo params) {
        if (EmptyUtil.isEmpty(params.getUserNickName())) {
            return new DataResponse(1001, "请填写完整信息.");
        }
        if (EmptyUtil.isEmpty(params.getUserTel())) {
            return new DataResponse(1001, "请填写完整信息.");
        }
        if (EmptyUtil.isEmpty(params.getGroupId())) {
            return new DataResponse(1001, "请填写完整信息.");
        }
        if (EmptyUtil.isEmpty(params.getRoomId())) {
            return new DataResponse(1001, "请填写完整信息.");
        }
        if (EmptyUtil.isEmpty(params.getUserId())) {
            EmployeeSession eSession = EmployeeSession.getEmployeeSession();
            params.setCreateUserId(eSession.getEmployeeId());
        }
        return this.userInfoApiService.saveVipUser(request, params);
    }

    /**
     * 获取会员
     *
     * @param params 会员对象
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "getVipUserById")
    @ResponseBody
    public DataResponse getVipUserById(UserVipInfo params) {
        List<UserVipInfo> result = (List<UserVipInfo>) this.userInfoApiService.getVipUserList(params).getData();
        if (EmptyUtil.isEmpty(result)) {
            return new DataResponse(1001, "not found");
        } else {
            return new DataResponse(1000, result.get(0));
        }
    }

    /**
     * VIP禁言/解禁
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "setVipGag")
    @ResponseBody
    public DataResponse setGag(UserVipInfo params) {
        return this.userInfoApiService.setVipGag(params);
    }


    /**
     * VIP拉黑/取消拉黑
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "setVipBlack")
    @ResponseBody
    public DataResponse setBlack(UserVipInfo params) {
        DataResponse result = this.userInfoApiService.setVipBlack(params);
        if (result.getCode().intValue() == 1000) {
            //TODO 记录VIP拉黑日志
            EmployeeSession eSession = EmployeeSession.getEmployeeSession();//操作人信息
            doLog(eSession.getEmployeeId(), params.getUserId(), params.getGroupId());
        }
        return result;
    }


    /**
     * 管理用户列表-----------------------------------------------------------------------------------------------------
     *
     * @param pageSearch
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping("getManageUserList")
    @ResponseBody
    public DataResponse getManageUserList(PageSearch pageSearch, UserManageInfo params) {
        return this.userInfoApiService.getManageUserList(pageSearch, params);
    }

    /**
     * 根据管理用户id获取管理用户详情
     *
     * @param params 管理用户对象
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "getManageUserById")
    @ResponseBody
    public DataResponse getManageUserById(UserManageInfo params) {
        List<UserManageInfo> result = (List<UserManageInfo>) this.userInfoApiService.getManageUser(params).getData();
        if (EmptyUtil.isEmpty(result)) {
            return new DataResponse(1001, "not found");
        }
        return new DataResponse(1000, result.get(0));
    }

    /**
     * 管理用户保存
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "saveManageUser")
    @ResponseBody
    public DataResponse saveManageUser(UserManageInfo params, MultipartFile qrCode, MultipartFile photo) {
        if (EmptyUtil.isEmpty(params.getUserNickName())) {
            return new DataResponse(1001, "请填写完整信息.");
        }
        if (EmptyUtil.isEmpty(params.getUserTel())) {
            return new DataResponse(1001, "请填写完整信息.");
        }
        if (EmptyUtil.isEmpty(params.getGroupId())) {
            return new DataResponse(1001, "请填写完整信息.");
        }
        if (EmptyUtil.isEmpty(params.getRoomId())) {
            return new DataResponse(1001, "请填写完整信息.");
        }
        if (EmptyUtil.isEmpty(params.getUserQq())) {
            return new DataResponse(1001, "请填写完整信息.");
        }

        if (params.getGroupId().intValue() == 3) {
            if (EmptyUtil.isEmpty(params.getUserIntroduction())) {
                return new DataResponse(1001, "请填写完整信息.");
            }
        } else {
            if (EmptyUtil.isEmpty(params.getPraise())) {
                return new DataResponse(1001, "请填写完整信息.");
            }
        }

        String qrCodeUrl = "";
        String photoUrl = "";
        try {
            if (EmptyUtil.isNotEmpty(qrCode)) {
                baseLog.info(LogFormatUtil.getActionFormat("开始上传二维码"));
                InputStream input = qrCode.getInputStream();
                int count = input.available();
                byte[] fileByte = new byte[count];
                input.read(fileByte);
                String fileName = qrCode.getOriginalFilename();
                baseLog.info(LogFormatUtil.getActionFormat("当前上传请求的file的文件名:" + fileName));
                input.read(fileByte);
                fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
                String fileId = this.fileUploadRemoteService.storeFile(fileByte, fileName);
                qrCodeUrl = fileId + "." + fileName;
                baseLog.info(LogFormatUtil.getActionFormat("结束上传二维码"));
                baseLog.info(LogFormatUtil.getActionFormat("上传后的文件名：" + qrCodeUrl));
                params.setUserQrcode(qrCodeUrl);
            }

            if (EmptyUtil.isNotEmpty(photo)) {
                baseLog.info(LogFormatUtil.getActionFormat("开始上传头像"));
                InputStream input = photo.getInputStream();
                int count = input.available();
                byte[] fileByte = new byte[count];
                input.read(fileByte);
                String fileName = photo.getOriginalFilename();
                baseLog.info(LogFormatUtil.getActionFormat("当前上传请求的file的文件名:" + fileName));
                input.read(fileByte);
                fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
                String fileId = this.fileUploadRemoteService.storeFile(fileByte, fileName);
                photoUrl = fileId + "." + fileName;
                baseLog.info(LogFormatUtil.getActionFormat("结束上传头像"));
                baseLog.info(LogFormatUtil.getActionFormat("上传后的文件名：" + photoUrl));
                params.setUserPhoto(photoUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataResponse dr= this.userInfoApiService.saveManageUser(params);
        this.baseCacheService.updateManageUserList(4);
        return dr;
    }


    /**
     * 删除管理用户
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "deleteManageUser")
    @ResponseBody
    public DataResponse deleteManageUser(UserManageInfo params) {
        DataResponse dr = this.userInfoApiService.deleteManageUser(params);
        this.baseCacheService.updateManageUserList(4);
        return dr;
    }

    /**
     * 保存助理小号
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "saveSmallUser")
    @ResponseBody
    public DataResponse saveSmallUser(UserSmallInfo params) {
        return this.userInfoApiService.saveSmallUser(params);
    }

    /**
     * 根据id获取小号信息
     *
     * @param params 小号对象
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "getUserSmallById")
    @ResponseBody
    public DataResponse getUserSmallById(UserSmallInfo params) {
        return this.userInfoApiService.getUserSmall(params);
    }

    /**
     * 删除小号
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "deleteSmall")
    @ResponseBody
    public DataResponse deleteSmall(UserSmallInfo params) {
        return this.userInfoApiService.deleteSmall(params);
    }


    /**
     * 查询助理小号列表
     *
     * @param params 小号对象
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "getSmallListByUserId")
    @ResponseBody
    public DataResponse getSmallListByUserId(UserSmallInfo params) {
        return this.userInfoApiService.getSmallList(params);
    }


    /**
     * 后台用户列表------------------------------------------------------------------------------------------------------
     *
     * @param pageSearch
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping("getEmployeeUserList")
    @ResponseBody
    public DataResponse getEmployeeUserList(PageSearch pageSearch, BmsEmployeeInfo params) {
        return this.userInfoApiService.getEmployeeUserList(pageSearch, params);
    }

    /**
     * 添加后台用户
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "saveEmployee")
    @ResponseBody
    public DataResponse saveEmployee(BmsEmployeeInfo params) {
        EmployeeSession eSession = EmployeeSession.getEmployeeSession();//操作人信息
        params.setCreateUserId(eSession.getEmployeeId());
        return this.userInfoApiService.saveEmployee(params);
    }

    /**
     * 根据后台用户id获取后台用户详情
     *
     * @param params 后台用户对象
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "getEmployeeUserById")
    @ResponseBody
    public DataResponse getEmployeeUserById(BmsEmployeeInfo params) {
        List<BmsEmployeeInfo> result = (List<BmsEmployeeInfo>) this.userInfoApiService.getEmployeeUser(params).getData();
        if (EmptyUtil.isEmpty(result)) {
            return new DataResponse(1001, "not found");
        }
        return new DataResponse(1000, result.get(0));
    }

    /**
     * 删除后台用户
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "deleteEmployeeUser")
    @ResponseBody
    public DataResponse deleteEmployeeUser(BmsEmployeeInfo params) {
        return this.userInfoApiService.deleteEmployeeUser(params);
    }


    /**
     * 重置密码
     * 重置为123456
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "resetPassWord")
    @ResponseBody
    public DataResponse resetPassWord(BmsEmployeeInfo params) {
        params.setPassword("123456");
        return this.userInfoApiService.resetPassWord(params);
    }

    /**
     * 记录操作日志
     *
     * @param optEmployeeId ；拉黑员工id
     * @param userId        被拉黑人员id
     * @param groupId       角色id
     */
    private void doLog(Integer optEmployeeId, Integer userId, Integer groupId) {
        BaseOptLog baseOptLog = new BaseOptLog();
        if (groupId == 1) {
            baseOptLog.setType(2);//2 游客拉黑
        } else if (groupId == 5) {
            baseOptLog.setType(3);//3 vip拉黑
        }
        baseOptLog.setUserId(userId);//被拉黑用户id
        baseOptLog.setGroupId(groupId);
        baseOptLog.setOptEmployeeId(optEmployeeId);
        baseOptLog.setOpeMsg("员工" + optEmployeeId + "主动拉黑开始用户id:"
                + userId + ", 角色id:" + groupId);
        baseOptLog.setOptTime(new Date());
        this.baseOptLogService.save(baseOptLog);
    }
}

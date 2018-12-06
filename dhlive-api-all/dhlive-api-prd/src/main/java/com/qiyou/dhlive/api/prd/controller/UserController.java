package com.qiyou.dhlive.api.prd.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.qiyou.dhlive.api.base.outward.service.IUserInfoApiService;
import com.qiyou.dhlive.api.prd.mvc.HttpSessionTool;
import com.qiyou.dhlive.api.prd.mvc.UserSession;
import com.qiyou.dhlive.api.prd.util.AddressUtils;
import com.qiyou.dhlive.core.base.outward.model.BaseOptLog;
import com.qiyou.dhlive.core.base.outward.service.IBaseOptLogService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.live.outward.service.ILiveRoomService;
import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.model.UserLoginLog;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserLoginLogService;
import com.qiyou.dhlive.core.user.outward.service.IUserManageInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserVipInfoService;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.common.utils.MD5Util;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;
import com.yaozhong.framework.web.annotation.session.NeedSession;
import com.yaozhong.framework.web.annotation.session.UnSession;


/**
 * ${DESCRIPTION}
 *
 * @author fish
 * @create 2018-01-20 17:56
 **/
@Controller
@RequestMapping(value = "user")
public class UserController {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IUserManageInfoService userManageInfoService;

    @Autowired
    private IUserVipInfoService userVipInfoService;

    @Autowired
    private ILiveRoomService liveRoomService;

    @Autowired
    private IUserInfoApiService userInfoApiService;

    @Autowired
    private IUserLoginLogService userLoginLogService;

    @Autowired
    private IBaseOptLogService baseOptLogService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    /**
     * 用户登录: 其中包含管理类(老师, 管理员, 助理)/vip会员登录, 用户名和密码确保唯一.
     * 先从管理类表查询, 如果符合条件直接登录, 否则去vip表查询, 成功则登录, 失败返回失败信息
     *
     * @param session
     * @param request
     * @return
     */
    @UnSession
    @RequestMapping(value = "login")
    @ResponseBody
    public DataResponse userLogin(UserManageInfo user, HttpSession session, HttpServletRequest request) {
        String loginName = user.getUserTel();
        String password = user.getUserPass();
        Assert.hasText(loginName, "手机号不可为空");
        Assert.hasText(password, "密码不可为空");
        String pwd = MD5Util.MD5Encode(password, "utf-8");
        user = this.userManageInfoService.getManageUserByLoginName(loginName);
        boolean checkLogin = false;
        if (EmptyUtil.isNotEmpty(user)) {//管理员登录(老师, 管理员等)
            if (pwd.equals(user.getUserPass())) {
                checkLogin = true;
            }
            if (!checkLogin) {
                return new DataResponse(1001, "用户名或密码不正确");
            } else {
                String loginIp = AddressUtils.getIpAddrFromRequest(request);
                user.setLastLoginIp(loginIp);
                user.setLastLoginTime(new Date());
                this.userManageInfoService.modifyEntity(user);
                UserSession userSession = UserSession.getUserSession();
                if (EmptyUtil.isNotEmpty(userSession)) {
                    HttpSessionTool.doUserLoginOut(session);
                }
                HttpSessionTool.doLoginUser(session, user);
                UserLoginLog log = new UserLoginLog();
                log.setUserId(user.getUserId());
                log.setGroupId(user.getGroupId());
                log.setLoginIp(loginIp);
                log.setLoginTime(new Date());
                this.userLoginLogService.save(log);
                return new DataResponse(1000, "success");
            }
        } else {//会员登录
            UserVipInfo vip = this.userVipInfoService.getVipUserByLoginName(loginName);
            if (EmptyUtil.isNotEmpty(vip)) {
                if (pwd.equals(vip.getUserPass())) {
                    checkLogin = true;
                }
                if (!checkLogin) {
                    return new DataResponse(1001, "用户名或密码不正确");
                } else {
                    if (vip.getIsBlack().intValue() == 1) {
                        return new DataResponse(1001, "当前无法登录，请联系客服.");
                    }
                    String loginIp = AddressUtils.getIpAddrFromRequest(request);
                    vip.setLastLoginIp(loginIp);
                    vip.setLastLoginTime(new Date());
                    this.userVipInfoService.modifyEntity(vip);
                    UserSession userSession = UserSession.getUserSession();
                    if (EmptyUtil.isNotEmpty(userSession)) {
                        HttpSessionTool.doUserLoginOut(session);
                    }
                    HttpSessionTool.doLoginUser(session, vip);

                    UserLoginLog log = new UserLoginLog();
                    log.setUserId(vip.getUserId());
                    log.setGroupId(vip.getGroupId());
                    log.setLoginIp(loginIp);
                    log.setLoginTime(new Date());
                    this.userLoginLogService.save(log);

                    return new DataResponse(1000, "success");
                }
            } else {
                return new DataResponse(1001, "用户名或密码错误");
            }
        }
    }


    /**
     * 用户注册
     * 刚进入直播间页面的时候, 身份为游客, 再userInfo表记录了游客的信息, 注册完之后成为vip用户,
     * so, 游客表的信息不动, 在vip表新增一条数据, 并登录
     *
     * @param vip 注册会员的对象
     * @return 注册结果
     */
    @UnSession
    @RequestMapping(value = "userRegistered")
    @ResponseBody
    public DataResponse userRegistered(UserVipInfo vip, HttpSession session, HttpServletRequest request) {
//        if (EmptyUtil.isNotEmpty(vip.getRoomId())) {
//            LiveRoom room = this.liveRoomService.findById(vip.getRoomId());
//            vip.setRoomName(room.getRoomName());
//        }
//        String loginIp = AddressUtils.getIpAddrFromRequest(request);
//        vip.setUserPass(MD5Util.MD5Encode(vip.getUserPass(), "utf-8"));
//        vip.setGroupId(5);
//        vip.setUserLevel("1");
//        vip.setGroupName("会员");
//        vip.setCreateTime(new Date());
//        vip.setLastLoginIp(loginIp);
//        vip.setLastLoginTime(new Date());
//        BaseResult br = this.userVipInfoService.save(vip);
//        vip.setUserId(Integer.parseInt(br.getData().toString()));
//        HttpSessionTool.doLoginUser(session, vip);
        return new DataResponse(1000, "success");
    }


    /**
     * 修改密码
     *
     * @param oldPass
     * @param newPass
     * @param request
     * @return
     */
    @RequestMapping("/editCurrentUserPwd")
    @ResponseBody
    public DataResponse editCurrentUserPwd(String oldPass, String newPass, HttpServletRequest request) {
        if (EmptyUtil.isEmpty(oldPass)) {
            return new DataResponse(1001, "请输入原密码");
        }
        if (EmptyUtil.isEmpty(newPass)) {
            return new DataResponse(1001, "请输入新密码");
        }

        UserSession session = UserSession.getUserSession();
        if (!MD5Util.MD5Encode(oldPass, "utf-8").equals(session.getUserPass())) {
            return new DataResponse(1001, "旧密码错误");
        } else {
            Integer groupId = session.getGroupId();
            if (groupId.intValue() == 5) {
                UserVipInfo vip = new UserVipInfo();
                vip.setUserPass(MD5Util.MD5Encode(newPass, "utf-8"));
                vip.setUserId(session.getUserId());
                this.userVipInfoService.modifyEntity(vip);
                HttpSession httpSession = request.getSession();
                HttpSessionTool.doUserLoginOut(httpSession);
            } else if (groupId == 2 || groupId == 3 || groupId == 4) {
                UserManageInfo manage = new UserManageInfo();
                manage.setUserPass(MD5Util.MD5Encode(newPass, "utf-8"));
                manage.setUserId(session.getUserId());
                this.userManageInfoService.modifyEntity(manage);
                HttpSession httpSession = request.getSession();
                HttpSessionTool.doUserLoginOut(httpSession);
            }
            return new DataResponse(1000, "success");
        }
    }


    /**
     * 用户退出
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "userLoginOut")
    @ResponseBody
    public DataResponse userLoginOut(HttpSession session) {
        HttpSessionTool.doUserLoginOut(session);
        return new DataResponse(1000, "success");
    }

    /**
     * 用户拉黑(游客和vip)
     *
     * @return
     */
    @NeedSession
    @RequestMapping(value = "shielding")
    @ResponseBody
    public DataResponse shielding(Integer userId, Integer groupId) {
        UserSession userSession = UserSession.getUserSession();
        if (userSession.getGroupId().intValue() != 3) {
            return new DataResponse(1001, "您没有操作权限.");
        }
        baseLog.info(LogFormatUtil.getActionFormat("助理主动拉黑开始用户id:" + userId + ", 角色id:" + groupId));
        //判断身份  1.游客  5.vip
        if (groupId == 1) {
            UserInfo user = new UserInfo();
            user.setUserId(userId);
            user.setIsBlack(1);//标记拉黑
            this.userInfoApiService.setTouristsBlack(user);
        } else if (groupId == 5) {
            UserVipInfo vip = new UserVipInfo();
            vip.setUserId(userId);
            vip.setIsBlack(1);//标记拉黑
            this.userInfoApiService.setVipBlack(vip);
        }
        baseLog.info(LogFormatUtil.getActionFormat("助理主动拉黑结束"));

        //TODO 记录拉黑日志
        BaseOptLog baseOptLog = new BaseOptLog();
        if (groupId == 1) {
            baseOptLog.setType(2);//2 游客拉黑
        } else if (groupId == 5) {
            baseOptLog.setType(3);//3 vip拉黑
        }
        baseOptLog.setUserId(userId);//被拉黑用户id
        baseOptLog.setGroupId(userSession.getGroupId());
        baseOptLog.setOptUserId(userSession.getUserId());//操作用户id
        baseOptLog.setOpeMsg("助理" + userSession.getUserId() + "主动拉黑开始用户id:" + userId + ", 角色id:" + groupId);
        baseOptLog.setOptTime(new Date());
        baseOptLogService.save(baseOptLog);
        return new DataResponse(1000, "success");
    }

    /**
     * 用户禁言(游客和vip)
     *
     * @return
     */
    @NeedSession
    @RequestMapping(value = "banned")
    @ResponseBody
    public DataResponse banned(Integer userId, Integer groupId) {
        UserSession userSession = UserSession.getUserSession();
        if (userSession.getGroupId().intValue() != 3) {
            return new DataResponse(1001, "您没有操作权限.");
        }

        //判断身份  1.游客  5.vip
        if (groupId == 1) {
            UserInfo user = new UserInfo();
            user.setUserId(userId);
            user.setIsGag(1);//标记禁言
            this.userInfoApiService.setTouristsGag(user);
        } else if (groupId == 5) {
            UserVipInfo vip = new UserVipInfo();
            vip.setUserId(userId);
            vip.setIsGag(1);//标记禁言
            this.userInfoApiService.setVipGag(vip);
        }
        return new DataResponse(1000, "success");
    }


    /**
     * 老师点赞
     * 每个人每天只能点赞一次.
     * 思路: 点过之后, 把当前人放到缓存格式: yyyy-MM-dd-groupId-userId , 用于判断不能多点, 失效时间24小时
     *
     * @param params            包含被点赞的老师的id
     * @param praiseUserId      点赞人id
     * @param praiseUserGroupId 点赞人身份id
     * @return
     */
    @NeedSession
    @RequestMapping(value = "pariseTeacher")
    @ResponseBody
    public DataResponse pariseTeacher(UserManageInfo params, Integer praiseUserGroupId, Integer praiseUserId) {
        String check = this.redisManager.getStringValueByKey(DateUtil.getDate(new Date()) + "-" + praiseUserGroupId + "-" + praiseUserId);
        if (EmptyUtil.isEmpty(check)) {
            params = this.userManageInfoService.findById(params.getUserId());
            int count = params.getPraise() + 1;
            SearchCondition<UserManageInfo> condition = new SearchCondition<UserManageInfo>(params);
            UserManageInfo update = new UserManageInfo();
            update.setPraise(count);
            this.userManageInfoService.modifyEntityByCondition(update, condition);
            //删除老师缓存
            this.redisManager.delete(RedisKeyConstant.TEACHER + params.getRoomId());
            //记录当前人已点过赞, 24小时失效
            this.redisManager.saveStringBySeconds(DateUtil.getDate(new Date()) + "-" + praiseUserGroupId + "-" + praiseUserId, "1", 86400);
            return new DataResponse(1000, count);
        } else {
            return new DataResponse(1001, "您已经点过赞了，请明天再点！");
        }
    }


    /**
     * 用户是否拉黑(vip)
     *
     * @return
     */
    @UnSession
    @RequestMapping(value = "checkVipIsBlack")
    @ResponseBody
    public DataResponse checkVipIsBlack(Integer userId) {
        UserVipInfo vip = new UserVipInfo();
        String value = this.redisManager.getStringValueByKey(RedisKeyConstant.VIP + userId);
        baseLog.info(LogFormatUtil.getActionFormat("key:" + RedisKeyConstant.VIP + userId + ",value:" + value));
        if (EmptyUtil.isEmpty(value)) {
            vip = this.userVipInfoService.findById(userId);
            this.redisManager.saveString(RedisKeyConstant.VIP + userId,new Gson().toJson(vip));
        } else {
            vip = new Gson().fromJson(value, UserVipInfo.class);
        }
        if(EmptyUtil.isEmpty(vip)) {
        	return new DataResponse(1000, "success");
        }
        if(EmptyUtil.isEmpty(vip.getIsBlack())) {
        	return new DataResponse(1000, "success");
        }
        if (vip.getIsBlack().intValue() == 1) {
            return new DataResponse(9999, "已被拉黑.");
        }
        return new DataResponse(1000, "success");
    }

    /**
     * 用户是否拉黑(游客)
     *
     * @return
     */
    @UnSession
    @RequestMapping(value = "checkTourseIsBlack")
    @ResponseBody
    public DataResponse checkTourseIsBlack(Integer userId) {
        UserInfo tourse = new UserInfo();
        String value = this.redisManager.getStringValueByKey(RedisKeyConstant.TOURISTS + userId);
        baseLog.info(LogFormatUtil.getActionFormat("key:" + RedisKeyConstant.TOURISTS + userId + ",value:" + value));
        if (EmptyUtil.isEmpty(value)) {
            tourse = this.userInfoService.findById(userId);
            this.redisManager.saveString(RedisKeyConstant.TOURISTS + userId,new Gson().toJson(tourse));
        } else {
            tourse = new Gson().fromJson(value, UserInfo.class);
        }
        if(EmptyUtil.isEmpty(tourse)) {
        	return new DataResponse(1000, "success");
        }
        if(EmptyUtil.isEmpty(tourse.getIsBlack())) {
        	return new DataResponse(1000, "success");
        }
        if (tourse.getIsBlack().intValue() == 1) {
            return new DataResponse(9999, "已被拉黑.");
        }
        return new DataResponse(1000, "success");
    }
}
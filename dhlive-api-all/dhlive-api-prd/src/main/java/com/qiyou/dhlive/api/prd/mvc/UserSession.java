package com.qiyou.dhlive.api.prd.mvc;

import com.qiyou.dhlive.core.user.outward.model.UserInfo;

import java.io.Serializable;
import java.util.Date;

/**
 * web 端用户的session的实体封装类
 *
 * @author ll
 */
public class UserSession implements Serializable {

    private static final long serialVersionUID = -3047902397882241246L;

    public UserSession() {
        // TODO Auto-generated constructor stub
    }


    /**
     * 数据的交互枢纽
     */
    protected static ThreadLocal<UserSession> threadLocalUserSession = new ThreadLocal<UserSession>();

    public static void setUserSession(UserSession userSession) {
        threadLocalUserSession.set(userSession);
    }

    /**
     * 从线程池中获取UserSession对象
     *
     * @return
     * @author LiuYiJun
     * @date 2015年7月16日
     */
    public static UserSession getUserSession() {
        return threadLocalUserSession.get();
    }

    /**
     * 获取登录用户的ID
     *
     * @return
     * @author LiuYiJun
     * @date 2015年7月16日
     */
    public static Integer getLoginUserId() {
        UserSession userSession = (UserSession) threadLocalUserSession.get();
        if (userSession == null) {
            return null;
        } else {
            return userSession.getUserId();
        }
    }

    private Integer userId;

    private String userMail;

    private String userPass;

    private Integer userType;//1-游客，2-vip，2-管理员，4-客服，5-讲师

    private String userRealName;

    private String userNickName;

    private String userSex;

    private String userQq;

    private String userTel;

    private String userAddress;

    private Date lastLoginTime;

    private String lastLoginIp;

    private Integer isFirstLogin;

    private Integer isLock;

    private Integer groupId;

    private String groupName;

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserQq() {
        return userQq;
    }

    public void setUserQq(String userQq) {
        this.userQq = userQq;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Integer getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(Integer isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

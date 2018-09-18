package com.qiyou.dhlive.core.user.outward.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "user_info")
public class UserInfo implements Serializable {
    /**
     * 用户id
     */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    /**
     * 分组id
     */
    @Column(name = "group_id")
    private Integer groupId;

    private String groupName;

    /**
     * 昵称
     */
    @Column(name = "user_nick_name")
    private String userNickName;

    /**
     * 上次登录时间
     */
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    /**
     * 上次登录ip
     */
    @Column(name = "last_login_ip")
    private String lastLoginIp;

    /**
     * 是否第一次登录
     */
    @Column(name = "is_first_login")
    private Integer isFirstLogin;

    /**
     * 是否黑名单 0否 1是
     */
    @Column(name = "is_black")
    private Integer isBlack;

    /**
     * 0正常  1禁言
     */
    @Column(name = "is_gag")
    private Integer isGag;

    /**
     * 是否在线 0不在线  1在线
     */
    @Column(name = "is_online")
    private Integer isOnline;

    /**
     * 状态
     */
    private Integer status;

    private Date limitTime;

    private String utmSource;

    /**
     * 观看时长
     */
    @Column(name = "look_time")
    private Integer lookTime;

    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
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

    public Integer getIsBlack() {
        return isBlack;
    }

    public void setIsBlack(Integer isBlack) {
        this.isBlack = isBlack;
    }

    public Integer getIsGag() {
        return isGag;
    }

    public void setIsGag(Integer isGag) {
        this.isGag = isGag;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLookTime() {
        return lookTime;
    }

    public void setLookTime(Integer lookTime) {
        this.lookTime = lookTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(Date limitTime) {
        this.limitTime = limitTime;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }
}
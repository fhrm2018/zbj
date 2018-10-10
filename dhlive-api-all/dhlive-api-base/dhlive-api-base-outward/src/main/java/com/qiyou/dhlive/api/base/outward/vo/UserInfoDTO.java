package com.qiyou.dhlive.api.base.outward.vo;

import java.util.Date;

public class UserInfoDTO {
	
	/**
     * 用户id
     */
    private Integer userId;

    /**
     * 分组id
     */
    private Integer groupId;

    private String groupName;

    /**
     * 昵称
     */
    private String userNickName;

    /**
     * 上次登录时间
     */
    private Date lastLoginTime;

    /**
     * 上次登录ip
     */
    private String lastLoginIp;

    /**
     * 是否第一次登录
     */
    private Integer isFirstLogin;

    /**
     * 是否黑名单 0否 1是
     */
    private Integer isBlack;

    /**
     * 0正常  1禁言
     */
    private Integer isGag;

    /**
     * 是否在线 0不在线  1在线
     */
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
    private Integer lookTime;

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
}

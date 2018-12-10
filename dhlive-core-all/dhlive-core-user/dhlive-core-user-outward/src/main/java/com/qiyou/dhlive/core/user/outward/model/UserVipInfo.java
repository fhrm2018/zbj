package com.qiyou.dhlive.core.user.outward.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "user_vip_info")
public class UserVipInfo implements Serializable {
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

    /**
     * 邮箱
     */
    @Column(name = "user_mail")
    private String userMail;

    /**
     * 密码
     */
    @Column(name = "user_pass")
    private String userPass;

    /**
     * 头像
     */
    @Column(name = "user_photo")
    private String userPhoto;

    /**
     * 昵称
     */
    @Column(name = "user_nick_name")
    private String userNickName;

    /**
     * 姓名
     */
    @Column(name = "user_real_name")
    private String userRealName;

    /**
     * 性别
     */
    @Column(name = "user_sex")
    private String userSex;

    /**
     * qq
     */
    @Column(name = "user_qq")
    private String userQq;

    /**
     * 电话
     */
    @Column(name = "user_tel")
    private String userTel;

    /**
     * 地址
     */
    @Column(name = "user_address")
    private String userAddress;

    /**
     * 生日
     */
    @Column(name = "user_birthday")
    private Date userBirthday;

    /**
     * 用户余额
     */
    @Column(name = "user_balance")
    private BigDecimal userBalance;

    /**
     * 等级
     */
    @Column(name = "user_level")
    private String userLevel;

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

    /**
     * 观看时长
     */
    @Column(name = "look_time")
    private Integer lookTime;

    @Column(name = "create_time")
    private Date createTime;

    private String groupName;

    private Integer roomId;

    private String roomName;

    private Integer tempWatchTime;

    private Integer createUserId;
    
    @Column(name = "first_login_ip")
    private String firstLoginIp;

    private static final long serialVersionUID = 1L;

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    public String getFirstLoginIp() {
		return firstLoginIp;
	}

	public void setFirstLoginIp(String firstLoginIp) {
		this.firstLoginIp = firstLoginIp;
	}

	/**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取分组id
     *
     * @return group_id - 分组id
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * 设置分组id
     *
     * @param groupId 分组id
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取邮箱
     *
     * @return user_mail - 邮箱
     */
    public String getUserMail() {
        return userMail;
    }

    /**
     * 设置邮箱
     *
     * @param userMail 邮箱
     */
    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    /**
     * 获取密码
     *
     * @return user_pass - 密码
     */
    public String getUserPass() {
        return userPass;
    }

    /**
     * 设置密码
     *
     * @param userPass 密码
     */
    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    /**
     * 获取头像
     *
     * @return user_photo - 头像
     */
    public String getUserPhoto() {
        return userPhoto;
    }

    /**
     * 设置头像
     *
     * @param userPhoto 头像
     */
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    /**
     * 获取昵称
     *
     * @return user_nick_name - 昵称
     */
    public String getUserNickName() {
        return userNickName;
    }

    /**
     * 设置昵称
     *
     * @param userNickName 昵称
     */
    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    /**
     * 获取姓名
     *
     * @return user_real_name - 姓名
     */
    public String getUserRealName() {
        return userRealName;
    }

    /**
     * 设置姓名
     *
     * @param userRealName 姓名
     */
    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }


    /**
     * 获取性别
     *
     * @return user_sex - 性别
     */
    public String getUserSex() {
        return userSex;
    }

    /**
     * 设置性别
     *
     * @param userSex 性别
     */
    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    /**
     * 获取qq
     *
     * @return user_qq - qq
     */
    public String getUserQq() {
        return userQq;
    }

    /**
     * 设置qq
     *
     * @param userQq qq
     */
    public void setUserQq(String userQq) {
        this.userQq = userQq;
    }

    /**
     * 获取电话
     *
     * @return user_tel - 电话
     */
    public String getUserTel() {
        return userTel;
    }

    /**
     * 设置电话
     *
     * @param userTel 电话
     */
    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    /**
     * 获取地址
     *
     * @return user_address - 地址
     */
    public String getUserAddress() {
        return userAddress;
    }

    /**
     * 设置地址
     *
     * @param userAddress 地址
     */
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    /**
     * 获取生日
     *
     * @return user_birthday - 生日
     */
    public Date getUserBirthday() {
        return userBirthday;
    }

    /**
     * 设置生日
     *
     * @param userBirthday 生日
     */
    public void setUserBirthday(Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    /**
     * 获取用户余额
     *
     * @return user_balance - 用户余额
     */
    public BigDecimal getUserBalance() {
        return userBalance;
    }

    /**
     * 设置用户余额
     *
     * @param userBalance 用户余额
     */
    public void setUserBalance(BigDecimal userBalance) {
        this.userBalance = userBalance;
    }


    /**
     * 获取等级
     *
     * @return user_level - 等级
     */
    public String getUserLevel() {
        return userLevel;
    }

    /**
     * 设置等级
     *
     * @param userLevel 等级
     */
    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    /**
     * 获取上次登录时间
     *
     * @return last_login_time - 上次登录时间
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置上次登录时间
     *
     * @param lastLoginTime 上次登录时间
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 获取上次登录ip
     *
     * @return last_login_ip - 上次登录ip
     */
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    /**
     * 设置上次登录ip
     *
     * @param lastLoginIp 上次登录ip
     */
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    /**
     * 获取是否第一次登录
     *
     * @return is_first_login - 是否第一次登录
     */
    public Integer getIsFirstLogin() {
        return isFirstLogin;
    }

    /**
     * 设置是否第一次登录
     *
     * @param isFirstLogin 是否第一次登录
     */
    public void setIsFirstLogin(Integer isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    /**
     * 获取是否黑名单 0否 1是
     *
     * @return is_black - 是否黑名单 0否 1是
     */
    public Integer getIsBlack() {
        return isBlack;
    }

    /**
     * 设置是否黑名单 0否 1是
     *
     * @param isBlack 是否黑名单 0否 1是
     */
    public void setIsBlack(Integer isBlack) {
        this.isBlack = isBlack;
    }

    /**
     * 获取0正常  1禁言
     *
     * @return is_gag - 0正常  1禁言
     */
    public Integer getIsGag() {
        return isGag;
    }

    /**
     * 设置0正常  1禁言
     *
     * @param isGag 0正常  1禁言
     */
    public void setIsGag(Integer isGag) {
        this.isGag = isGag;
    }

    /**
     * 获取是否在线 0不在线  1在线
     *
     * @return is_online - 是否在线 0不在线  1在线
     */
    public Integer getIsOnline() {
        return isOnline;
    }

    /**
     * 设置是否在线 0不在线  1在线
     *
     * @param isOnline 是否在线 0不在线  1在线
     */
    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取观看时长
     *
     * @return look_time - 观看时长
     */
    public Integer getLookTime() {
        return lookTime;
    }

    /**
     * 设置观看时长
     *
     * @param lookTime 观看时长
     */
    public void setLookTime(Integer lookTime) {
        this.lookTime = lookTime;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getTempWatchTime() {
        return tempWatchTime;
    }

    public void setTempWatchTime(Integer tempWatchTime) {
        this.tempWatchTime = tempWatchTime;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }
}
package com.qiyou.dhlive.api.base.outward.vo;

import java.util.Date;

/**
 * Created by ThinkPad on 2018/3/10.
 */
public class UserVO {

    private Integer id;

    private Integer userId;

    private Integer groupId;

    private Date joinTime;

    private String account;

    private String level;

    private Integer count;

    private String userNickName;

    //0 不在线  1在线
    private Integer status;

    private String flag;

    private Integer waterGroupId;

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

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getWaterGroupId() {
        return waterGroupId;
    }

    public void setWaterGroupId(Integer waterGroupId) {
        this.waterGroupId = waterGroupId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

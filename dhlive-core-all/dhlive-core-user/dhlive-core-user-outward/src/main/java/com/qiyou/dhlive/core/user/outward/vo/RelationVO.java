package com.qiyou.dhlive.core.user.outward.vo;


import java.util.Date;

/**
 * Created by ThinkPad on 2018/3/16.
 */
public class RelationVO {

    private Integer id;

    private Integer ykCount;

    private Integer vipCount;

    private Integer userId;

    private Integer groupId;

    private String ykName;

    private Date ykTime;

    private String vipName;

    private Integer userLevel;

    private Date vipTime;

    private String flag;

    private Integer waterGroupId;

    public Integer getYkCount() {
        return ykCount;
    }

    public void setYkCount(Integer ykCount) {
        this.ykCount = ykCount;
    }

    public Integer getVipCount() {
        return vipCount;
    }

    public void setVipCount(Integer vipCount) {
        this.vipCount = vipCount;
    }

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

    public String getYkName() {
        return ykName;
    }

    public void setYkName(String ykName) {
        this.ykName = ykName;
    }

    public Date getYkTime() {
        return ykTime;
    }

    public void setYkTime(Date ykTime) {
        this.ykTime = ykTime;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Date getVipTime() {
        return vipTime;
    }

    public void setVipTime(Date vipTime) {
        this.vipTime = vipTime;
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

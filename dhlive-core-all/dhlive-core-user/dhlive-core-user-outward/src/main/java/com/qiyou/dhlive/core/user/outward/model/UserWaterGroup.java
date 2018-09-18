package com.qiyou.dhlive.core.user.outward.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by ThinkPad on 2018/3/24.
 */
@Table(name = "user_water_group")
public class UserWaterGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer waterGroupId;

    private String waterGroupName;

    private Integer isDel;

    private Integer userId;

    public Integer getWaterGroupId() {
        return waterGroupId;
    }

    public void setWaterGroupId(Integer waterGroupId) {
        this.waterGroupId = waterGroupId;
    }

    public String getWaterGroupName() {
        return waterGroupName;
    }

    public void setWaterGroupName(String waterGroupName) {
        this.waterGroupName = waterGroupName;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

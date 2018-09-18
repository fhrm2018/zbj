package com.qiyou.dhlive.core.user.outward.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ThinkPad on 2018/3/13.
 */
@Table(name = "user_relation")
public class UserRelation implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer groupId;

    private Integer userId;

    private Integer relationUserId;

    private Date createTime;

    private Integer waterGroupId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getRelationUserId() {
        return relationUserId;
    }

    public void setRelationUserId(Integer relationUserId) {
        this.relationUserId = relationUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getWaterGroupId() {
        return waterGroupId;
    }

    public void setWaterGroupId(Integer waterGroupId) {
        this.waterGroupId = waterGroupId;
    }
}

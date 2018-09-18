package com.qiyou.dhlive.core.room.outward.model;

import com.yaozhong.framework.base.database.domain.returns.DataResponse;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ThinkPad on 2018/3/9.
 */
@Table(name = "room_class")
public class RoomClass implements Serializable {

    @Id
    @Column(name = "class_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer classId;

    private String className;

    private String classUrl;

    private Integer roomId;

    private Integer status;

    private Date createTime;

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassUrl() {
        return classUrl;
    }

    public void setClassUrl(String classUrl) {
        this.classUrl = classUrl;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

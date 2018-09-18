package com.qiyou.dhlive.core.user.outward.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "user_small_info")
public class UserSmallInfo implements Serializable {
    @Id
    @Column(name = "small_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer smallId;

    @Column(name = "small_name")
    private String smallName;

    @Column(name = "small_level")
    private Integer smallLevel;

    /**
     * 所属助理id
     */
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

    /**
     * 冗余的直播间id
     */
    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "create_time")
    private Date createTime;

    private Integer status;

    private static final long serialVersionUID = 1L;

    /**
     * @return small_id
     */
    public Integer getSmallId() {
        return smallId;
    }

    /**
     * @param smallId
     */
    public void setSmallId(Integer smallId) {
        this.smallId = smallId;
    }

    /**
     * @return small_name
     */
    public String getSmallName() {
        return smallName;
    }

    /**
     * @param smallName
     */
    public void setSmallName(String smallName) {
        this.smallName = smallName;
    }

    /**
     * @return small_level
     */
    public Integer getSmallLevel() {
        return smallLevel;
    }

    /**
     * @param smallLevel
     */
    public void setSmallLevel(Integer smallLevel) {
        this.smallLevel = smallLevel;
    }

    /**
     * 获取所属助理id
     *
     * @return user_id - 所属助理id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置所属助理id
     *
     * @param userId 所属助理id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return user_name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取冗余的直播间id
     *
     * @return room_id - 冗余的直播间id
     */
    public Integer getRoomId() {
        return roomId;
    }

    /**
     * 设置冗余的直播间id
     *
     * @param roomId 冗余的直播间id
     */
    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    /**
     * @return room_name
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * @param roomName
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
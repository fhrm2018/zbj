package com.qiyou.dhlive.core.live.outward.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "live_room")
public class LiveRoom implements Serializable {
    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    /**
     * 腾讯创建群成功后返回的id
     */
    @Column(name = "room_group_id")
    private String roomGroupId;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "room_about")
    private String roomAbout;

    @Column(name = "room_notice")
    private String roomNotice;

    @Column(name = "room_type")
    private Integer roomType;

    @Column(name = "room_managers")
    private String roomManagers;

    @Column(name = "room_pass")
    private String roomPass;

    @Column(name = "room_max_num")
    private Integer roomMaxNum;

    @Column(name = "room_stream_server")
    private String roomStreamServer;

    @Column(name = "room_logo")
    private String roomLogo;

    @Column(name = "room_live_url")
    private String roomLiveUrl;

    @Column(name = "room_tag")
    private String roomTag;

    @Column(name = "room_qq")
    private String roomQq;

    @Column(name = "room_start_live_time")
    private Date roomStartLiveTime;

    @Column(name = "room_end_live_time")
    private Date roomEndLiveTime;

    @Column(name = "create_time")
    private Date createTime;

    private Integer status;

    private Integer roomLiveLevel;

    private Integer isShow;

    private Integer baseNum;

    private Integer tempWatchTime;

    private static final long serialVersionUID = 1L;

    /**
     * @return room_id
     */
    public Integer getRoomId() {
        return roomId;
    }

    /**
     * @param roomId
     */
    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    /**
     * 获取腾讯创建群成功后返回的id
     *
     * @return room_group_id - 腾讯创建群成功后返回的id
     */
    public String getRoomGroupId() {
        return roomGroupId;
    }

    /**
     * 设置腾讯创建群成功后返回的id
     *
     * @param roomGroupId 腾讯创建群成功后返回的id
     */
    public void setRoomGroupId(String roomGroupId) {
        this.roomGroupId = roomGroupId;
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
     * @return room_about
     */
    public String getRoomAbout() {
        return roomAbout;
    }

    /**
     * @param roomAbout
     */
    public void setRoomAbout(String roomAbout) {
        this.roomAbout = roomAbout;
    }

    /**
     * @return room_notice
     */
    public String getRoomNotice() {
        return roomNotice;
    }

    /**
     * @param roomNotice
     */
    public void setRoomNotice(String roomNotice) {
        this.roomNotice = roomNotice;
    }

    /**
     * @return room_type
     */
    public Integer getRoomType() {
        return roomType;
    }

    /**
     * @param roomType
     */
    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }

    /**
     * @return room_managers
     */
    public String getRoomManagers() {
        return roomManagers;
    }

    /**
     * @param roomManagers
     */
    public void setRoomManagers(String roomManagers) {
        this.roomManagers = roomManagers;
    }

    /**
     * @return room_pass
     */
    public String getRoomPass() {
        return roomPass;
    }

    /**
     * @param roomPass
     */
    public void setRoomPass(String roomPass) {
        this.roomPass = roomPass;
    }

    /**
     * @return room_max_num
     */
    public Integer getRoomMaxNum() {
        return roomMaxNum;
    }

    /**
     * @param roomMaxNum
     */
    public void setRoomMaxNum(Integer roomMaxNum) {
        this.roomMaxNum = roomMaxNum;
    }

    /**
     * @return room_stream_server
     */
    public String getRoomStreamServer() {
        return roomStreamServer;
    }

    /**
     * @param roomStreamServer
     */
    public void setRoomStreamServer(String roomStreamServer) {
        this.roomStreamServer = roomStreamServer;
    }

    /**
     * @return room_logo
     */
    public String getRoomLogo() {
        return roomLogo;
    }

    /**
     * @param roomLogo
     */
    public void setRoomLogo(String roomLogo) {
        this.roomLogo = roomLogo;
    }

    /**
     * @return room_live_url
     */
    public String getRoomLiveUrl() {
        return roomLiveUrl;
    }

    /**
     * @param roomLiveUrl
     */
    public void setRoomLiveUrl(String roomLiveUrl) {
        this.roomLiveUrl = roomLiveUrl;
    }

    /**
     * @return room_tag
     */
    public String getRoomTag() {
        return roomTag;
    }

    /**
     * @param roomTag
     */
    public void setRoomTag(String roomTag) {
        this.roomTag = roomTag;
    }

    /**
     * @return room_qq
     */
    public String getRoomQq() {
        return roomQq;
    }

    /**
     * @param roomQq
     */
    public void setRoomQq(String roomQq) {
        this.roomQq = roomQq;
    }

    /**
     * @return room_start_live_time
     */
    public Date getRoomStartLiveTime() {
        return roomStartLiveTime;
    }

    /**
     * @param roomStartLiveTime
     */
    public void setRoomStartLiveTime(Date roomStartLiveTime) {
        this.roomStartLiveTime = roomStartLiveTime;
    }

    /**
     * @return room_end_live_time
     */
    public Date getRoomEndLiveTime() {
        return roomEndLiveTime;
    }

    /**
     * @param roomEndLiveTime
     */
    public void setRoomEndLiveTime(Date roomEndLiveTime) {
        this.roomEndLiveTime = roomEndLiveTime;
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

    public Integer getRoomLiveLevel() {
        return roomLiveLevel;
    }

    public void setRoomLiveLevel(Integer roomLiveLevel) {
        this.roomLiveLevel = roomLiveLevel;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getBaseNum() {
        return baseNum;
    }

    public void setBaseNum(Integer baseNum) {
        this.baseNum = baseNum;
    }

    public Integer getTempWatchTime() {
        return tempWatchTime;
    }

    public void setTempWatchTime(Integer tempWatchTime) {
        this.tempWatchTime = tempWatchTime;
    }
}
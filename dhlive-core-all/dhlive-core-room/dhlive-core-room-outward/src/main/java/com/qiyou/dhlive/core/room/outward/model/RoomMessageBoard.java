package com.qiyou.dhlive.core.room.outward.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "room_message_board")
public class RoomMessageBoard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "message_time")
    private Date messageTime;

    /**
     * 父级id
     */
    @Column(name = "m_id")
    private Integer mId;

    @Column(name = "audit_uid")
    private Integer auditUid;

    @Column(name = "audit_time")
    private Date auditTime;

    /**
     * 0未审核  1审核通过 2审核未通过
     */
    private Integer status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "message_content")
    private String messageContent;

    private String userName;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

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
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return message_time
     */
    public Date getMessageTime() {
        return messageTime;
    }

    /**
     * @param messageTime
     */
    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    /**
     * 获取父级id
     *
     * @return m_id - 父级id
     */
    public Integer getmId() {
        return mId;
    }

    /**
     * 设置父级id
     *
     * @param mId 父级id
     */
    public void setmId(Integer mId) {
        this.mId = mId;
    }

    /**
     * @return audit_uid
     */
    public Integer getAuditUid() {
        return auditUid;
    }

    /**
     * @param auditUid
     */
    public void setAuditUid(Integer auditUid) {
        this.auditUid = auditUid;
    }

    /**
     * @return audit_time
     */
    public Date getAuditTime() {
        return auditTime;
    }

    /**
     * @param auditTime
     */
    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    /**
     * 获取0未审核  1审核通过 2审核未通过
     *
     * @return status - 0未审核  1审核通过 2审核未通过
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0未审核  1审核通过 2审核未通过
     *
     * @param status 0未审核  1审核通过 2审核未通过
     */
    public void setStatus(Integer status) {
        this.status = status;
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

    /**
     * @return message_content
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * @param messageContent
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
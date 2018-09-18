package com.qiyou.dhlive.core.room.outward.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "room_chat_message")
public class RoomChatMessage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_id")
    private Integer roomId;

    /**
     * 发送人用户UID
     */
    @Column(name = "post_uid")
    private Integer postUid;

    /**
     * 发送人昵称
     */
    @Column(name = "post_nick_name")
    private String postNickName;

    /**
     * 收件人UID
     */
    @Column(name = "g_uid")
    private Integer gUid;

    /**
     * 收件人用户昵称
     */
    @Column(name = "g_nick_name")
    private String gNickName;

    /**
     * 发送时间
     */
    @Column(name = "message_time")
    private Date messageTime;

    /**
     * 消息类型
     */
    @Column(name = "message_type")
    private Integer messageType;

    /**
     * 是否小号
     */
    @Column(name = "is_samll")
    private Integer isSamll;

    /**
     * 审核人id
     */
    @Column(name = "audit_uid")
    private Integer auditUid;

    /**
     * 审核时间
     */
    @Column(name = "audit_time")
    private Date auditTime;

    /**
     * 状态  0未审核  1审核通过 2审核未通过
     */
    private Integer status;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 发送的消息内容
     */
    private String content;

    private Integer level;

    private Integer groupId;

    private String uniqueId;

    private Integer isDelete;

    private String sendTime;

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
     * 获取发送人用户UID
     *
     * @return post_uid - 发送人用户UID
     */
    public Integer getPostUid() {
        return postUid;
    }

    /**
     * 设置发送人用户UID
     *
     * @param postUid 发送人用户UID
     */
    public void setPostUid(Integer postUid) {
        this.postUid = postUid;
    }

    /**
     * 获取发送人昵称
     *
     * @return post_nick_name - 发送人昵称
     */
    public String getPostNickName() {
        return postNickName;
    }

    /**
     * 设置发送人昵称
     *
     * @param postNickName 发送人昵称
     */
    public void setPostNickName(String postNickName) {
        this.postNickName = postNickName;
    }

    /**
     * 获取收件人UID
     *
     * @return g_uid - 收件人UID
     */
    public Integer getgUid() {
        return gUid;
    }

    /**
     * 设置收件人UID
     *
     * @param gUid 收件人UID
     */
    public void setgUid(Integer gUid) {
        this.gUid = gUid;
    }

    /**
     * 获取收件人用户昵称
     *
     * @return g_nick_name - 收件人用户昵称
     */
    public String getgNickName() {
        return gNickName;
    }

    /**
     * 设置收件人用户昵称
     *
     * @param gNickName 收件人用户昵称
     */
    public void setgNickName(String gNickName) {
        this.gNickName = gNickName;
    }

    /**
     * 获取发送时间
     *
     * @return message_time - 发送时间
     */
    public Date getMessageTime() {
        return messageTime;
    }

    /**
     * 设置发送时间
     *
     * @param messageTime 发送时间
     */
    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    /**
     * 获取消息类型
     *
     * @return message_type - 消息类型
     */
    public Integer getMessageType() {
        return messageType;
    }

    /**
     * 设置消息类型
     *
     * @param messageType 消息类型
     */
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    /**
     * 获取是否小号
     *
     * @return is_samll - 是否小号
     */
    public Integer getIsSamll() {
        return isSamll;
    }

    /**
     * 设置是否小号
     *
     * @param isSamll 是否小号
     */
    public void setIsSamll(Integer isSamll) {
        this.isSamll = isSamll;
    }

    /**
     * 获取审核人id
     *
     * @return audit_uid - 审核人id
     */
    public Integer getAuditUid() {
        return auditUid;
    }

    /**
     * 设置审核人id
     *
     * @param auditUid 审核人id
     */
    public void setAuditUid(Integer auditUid) {
        this.auditUid = auditUid;
    }

    /**
     * 获取审核时间
     *
     * @return audit_time - 审核时间
     */
    public Date getAuditTime() {
        return auditTime;
    }

    /**
     * 设置审核时间
     *
     * @param auditTime 审核时间
     */
    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    /**
     * 获取状态  0未审核  1审核通过 2审核未通过
     *
     * @return status - 状态  0未审核  1审核通过 2审核未通过
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态  0未审核  1审核通过 2审核未通过
     *
     * @param status 状态  0未审核  1审核通过 2审核未通过
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
     * 获取发送的消息内容
     *
     * @return content - 发送的消息内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置发送的消息内容
     *
     * @param content 发送的消息内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
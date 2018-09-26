package com.qiyou.dhlive.core.room.outward.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by fish on 2018/9/26.
 */
@Table(name = "room_auto_msg")
public class RoomAutoMsg implements Serializable {

    @Id
    @Column(name = "msg_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer msgId;

    @Column(name = "msg_content")
    private String msgContent;

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}

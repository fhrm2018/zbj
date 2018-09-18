package com.qiyou.dhlive.api.base.outward.vo;

/**
 * describe:
 *
 * @author fish
 * @date 2018/02/03
 */
public class MsgBody {

    private String MsgType;

    private MsgContent MsgContent;

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public com.qiyou.dhlive.api.base.outward.vo.MsgContent getMsgContent() {
        return MsgContent;
    }

    public void setMsgContent(com.qiyou.dhlive.api.base.outward.vo.MsgContent msgContent) {
        MsgContent = msgContent;
    }
}

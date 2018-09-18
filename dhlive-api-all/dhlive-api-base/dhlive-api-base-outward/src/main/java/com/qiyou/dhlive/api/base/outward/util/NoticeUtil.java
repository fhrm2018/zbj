package com.qiyou.dhlive.api.base.outward.util;

import com.google.gson.Gson;
import com.qiyou.dhlive.api.base.outward.vo.GroupMessage;
import com.qiyou.dhlive.api.base.outward.vo.Message;
import com.qiyou.dhlive.api.base.outward.vo.MsgBody;
import com.qiyou.dhlive.api.base.outward.vo.MsgContent;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * describe:
 *
 * @author fish
 * @date 2018/02/03
 */
public class NoticeUtil {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    /**
     * 发送禁言通知
     *
     * @param fromAccount
     * @param toAccount
     * @param userSig
     * @param identifier
     * @param sdkAppId
     */
    public static String sendSetGagNotic(String fromAccount, String toAccount, String userSig, String identifier, String sdkAppId, long time) {
        baseLog.info(LogFormatUtil.getActionFormat("发送禁言通知开始"));
        Message message = new Message();
        message.setSyncOtherMachine(1);
        message.setFrom_Account(fromAccount);
        message.setTo_Account(toAccount);
        message.setMsgRandom(TLSUtils.getRandom());
        List<MsgBody> MsgBody = new ArrayList<MsgBody>();
        MsgBody body = new MsgBody();
        body.setMsgType("TIMCustomElem");
        MsgContent msg = new MsgContent();
        if (time == 0) {
            msg.setData("1002");//解禁
        } else {
            msg.setData("1001");//禁言
        }
        body.setMsgContent(msg);
        MsgBody.add(body);
        message.setMsgBody(MsgBody);
        baseLog.info(LogFormatUtil.getActionFormat("发送禁言通知参数" + new Gson().toJson(message)));
        String method = "POST";
        String url = "https://console.tim.qq.com/v4/openim/sendmsg?usersig=" + userSig + "&identifier=" + identifier + "&sdkappid=" + sdkAppId + "&random=" + TLSUtils.getRandom() + "&contenttype=json";
        String result = HttpUtil.httpRequest(url, method, new Gson().toJson(message));
        baseLog.info(LogFormatUtil.getActionFormat("发送禁言通知结果" + result));
        return result;
    }


    /**
     * 拉黑消息
     *
     * @param fromAccount
     * @param toAccount
     * @param userSig
     * @param identifier
     * @param sdkAppId
     * @param black
     * @return
     */
    public static String sendSetBlackNotic(String fromAccount, String toAccount, String userSig, String identifier, String sdkAppId, Integer black) {
        baseLog.info(LogFormatUtil.getActionFormat("发送拉黑通知开始"));
        Message message = new Message();
        message.setSyncOtherMachine(1);
        message.setFrom_Account(fromAccount);
        message.setTo_Account(toAccount);
        message.setMsgRandom(TLSUtils.getRandom());
        List<MsgBody> MsgBody = new ArrayList<MsgBody>();
        MsgBody body = new MsgBody();
        body.setMsgType("TIMCustomElem");
        MsgContent msg = new MsgContent();
        if (black == 0) {
            msg.setData("2002");
        } else {
            msg.setData("2001");
        }
        body.setMsgContent(msg);
        MsgBody.add(body);
        message.setMsgBody(MsgBody);
        baseLog.info(LogFormatUtil.getActionFormat("发送拉黑通知参数" + new Gson().toJson(message)));
        String method = "POST";
        String url = "https://console.tim.qq.com/v4/openim/sendmsg?usersig=" + userSig + "&identifier=" + identifier + "&sdkappid=" + sdkAppId + "&random=" + TLSUtils.getRandom() + "&contenttype=json";
        String result = HttpUtil.httpRequest(url, method, new Gson().toJson(message));
        baseLog.info(LogFormatUtil.getActionFormat("发送拉黑通知结果" + result));
        return result;
    }


    /**
     * 新公告通知
     *
     * @param groupId
     * @param userSig
     * @param identifier
     * @param sdkAppId
     * @param content
     * @return
     */
    public static String sendAnnouncementNotic(String groupId, String userSig, String identifier, String sdkAppId, String content) {
        baseLog.info(LogFormatUtil.getActionFormat("发送公告通知开始"));
        Message message = new Message();
        message.setGroupId(groupId);
        message.setRandom(TLSUtils.getRandom());
        List<MsgBody> MsgBody = new ArrayList<MsgBody>();
        MsgBody body = new MsgBody();
        body.setMsgType("TIMTextElem");
        MsgContent msg = new MsgContent();
        msg.setData("3001");
        msg.setText(content);
        body.setMsgContent(msg);
        MsgBody.add(body);
        message.setMsgBody(MsgBody);
        baseLog.info(LogFormatUtil.getActionFormat("发送公告通知参数" + new Gson().toJson(message)));
        String method = "POST";
        String url = "https://console.tim.qq.com/v4/group_open_http_svc/send_group_msg?usersig=" + userSig + "&identifier=" + identifier + "&sdkappid=" + sdkAppId + "&random=" + TLSUtils.getRandom() + "&contenttype=json";
        String result = HttpUtil.httpRequest(url, method, new Gson().toJson(message));
        baseLog.info(LogFormatUtil.getActionFormat("发送公告通知结果" + result));
        return result;
    }


    /**
     * 批量发送消息
     *
     * @param fromAcc
     * @param toAcc
     */
    public static void sendGroupMsg(String fromAcc, String toAcc, String content, String userSig, String identifier, String sdkAppId) {
        baseLog.info(LogFormatUtil.getActionFormat("批量发送消息开始"));
        Message message = new Message();
        message.setSyncOtherMachine(1);
        message.setFrom_Account(fromAcc);
        message.setTo_Account(toAcc);
        message.setMsgRandom(TLSUtils.getRandom());
        List<MsgBody> MsgBody = new ArrayList<MsgBody>();
        MsgBody body = new MsgBody();
        body.setMsgType("TIMCustomElem");
        MsgContent msg = new MsgContent();
        msg.setData("4001");
        msg.setText(content);
        msg.setExt(content);
        body.setMsgContent(msg);
        MsgBody.add(body);
        message.setMsgBody(MsgBody);
        baseLog.info(LogFormatUtil.getActionFormat("批量发送消息参数" + new Gson().toJson(message)));
        String method = "POST";
        String url = "https://console.tim.qq.com/v4/openim/sendmsg?usersig=" + userSig + "&identifier=" + identifier + "&sdkappid=" + sdkAppId + "&random=" + TLSUtils.getRandom() + "&contenttype=json";
        String result = HttpUtil.httpRequest(url, method, new Gson().toJson(message));
        baseLog.info(LogFormatUtil.getActionFormat("批量发送消息结果" + result));
    }

}

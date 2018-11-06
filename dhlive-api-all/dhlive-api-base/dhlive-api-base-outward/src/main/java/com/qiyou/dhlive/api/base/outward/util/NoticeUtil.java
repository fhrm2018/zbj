package com.qiyou.dhlive.api.base.outward.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.qiyou.dhlive.api.base.outward.vo.Message;
import com.qiyou.dhlive.api.base.outward.vo.MsgBody;
import com.qiyou.dhlive.api.base.outward.vo.MsgContent;
import com.qiyou.dhlive.core.room.outward.model.RoomChatMessage;
import com.yaozhong.framework.base.common.utils.DateStyle;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.common.utils.OrderNoUtil;

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
    
    
    public static RoomChatMessage sendAutoGroupMsg(String fromAcc,String content,String userSig, String identifier, String sdkAppId,String roomGroupId,Integer groupId,Integer level) {
    	
    	Integer random=TLSUtils.getRandom();
    	String url="https://console.tim.qq.com/v4/group_open_http_svc/send_group_msg?usersig="+userSig+"&identifier="+identifier+"&sdkappid="+sdkAppId+"&random="+random+"&contenttype=json";
    	 Message message = new Message();
         message.setGroupId(roomGroupId);
         message.setMsgRandom(random);
         message.setUniqueId(OrderNoUtil.get18OrderNumber());
         List<MsgBody> MsgBody = new ArrayList<MsgBody>();
         MsgBody body = new MsgBody();
         body.setMsgType("TIMCustomElem");
         MsgContent msg = new MsgContent();
         Map<String,Object> data=Maps.newHashMap();
         data.put("code", "0000");
         data.put("groupId", groupId);
         data.put("postNickName", fromAcc);
         data.put("postUid", -1);
         data.put("uniqueId",message.getUniqueId());
         data.put("type", 0);
         data.put("isSpecial", 1);
         data.put("checkStatus", 1);
         data.put("level", level);
         msg.setData(JSON.toJSONString(data));
         body.setMsgContent(msg);
         MsgBody.add(body);
         MsgBody body1 = new MsgBody();
         body1.setMsgType("TIMTextElem");
         MsgContent msg1 = new MsgContent();
         msg1.setText(content);
         body1.setMsgContent(msg1);
         MsgBody.add(body1);
         
         message.setMsgBody(MsgBody);
         System.out.println(message.getUniqueId());
         
    	String result = HttpUtil.httpRequest(url, "POST", new Gson().toJson(message));
    	List<Map<String,Object>> mapList=Lists.newArrayList();
    	//指令json
    	Map<String,Object> map1=Maps.newHashMap();
    	map1.put("type", "TIMCustomElem");
    	Map<String,Object> contentMapData=Maps.newHashMap();
    	Map<String,Object> mapData=Maps.newHashMap();
    	mapData.put("data",JSON.toJSONString(data));
    	map1.put("content", mapData);
    	mapList.add(map1);
    	
    	//发送内容JSON
    	Map<String,Object> mapText=Maps.newHashMap();
    	mapText.put("type", "TIMTextElem");
    	
    	Map<String,Object> mapTextData=Maps.newHashMap();
    	mapTextData.put("text",content);
    	mapText.put("content", mapTextData);
    	mapList.add(mapText);
    	RoomChatMessage roomMsg=new RoomChatMessage();
    	roomMsg.setAuditTime(new Date());
    	roomMsg.setIsDelete(0);
    	roomMsg.setCreateTime(new Date());
    	roomMsg.setGroupId(1);
    	roomMsg.setLevel(0);
    	roomMsg.setIsSamll(0);
    	roomMsg.setStatus(1);
    	roomMsg.setUniqueId(message.getUniqueId());
    	roomMsg.setRoomId(4);
    	roomMsg.setPostUid(-1);
    	roomMsg.setPostNickName(fromAcc);
    	roomMsg.setMessageTime(new Date());
    	roomMsg.setSendTime(DateUtil.DateToString(new Date(), DateStyle.MM_DD_HH_MM_SS));
    	Map<String,Object> contentJson=Maps.newHashMap();
    	contentJson.put("uniqueId", message.getUniqueId());
    	contentJson.put("elems", mapList);
    	roomMsg.setContent(JSON.toJSONString(contentJson));
    	System.out.println(result);
    	return roomMsg;
    	
//    	{"ActionStatus":"OK","ErrorCode":0,"MsgSeq":4340,"MsgTime":1541412489}
    	
    }

}

package com.qiyou.dhlive.api.base.service.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.api.base.outward.service.IBaseOptLogApiService;
import com.qiyou.dhlive.core.base.outward.model.BaseOptLog;
import com.qiyou.dhlive.core.base.outward.service.IBaseOptLogService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.room.outward.model.RoomChatMessage;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.redis.RedisManager;

@Service
public class BaseOptLogApiServiceImpl implements IBaseOptLogApiService {
	
	@Autowired
    private IBaseOptLogService baseOptLogService;
	
	@Autowired
    private IBaseCacheService baseCacheService;
    
    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;
	
	private String getMsgContentFromMsg(String msg) {
    	if(EmptyUtil.isEmpty(msg)) {
    		return "";
    	}
    	String rs = "";
    	try {
    		JSONObject objData = JSONObject.parseObject(msg);
    		JSONArray elemsList = objData.getJSONArray("elems");
    		if(EmptyUtil.isNotEmpty(elemsList) && elemsList.size()> 0 ) {
    			for(int i=0;i<elemsList.size();i++) {
    				JSONObject msgObj = elemsList.getJSONObject(i);
        			if("TIMCustomElem".equals(msgObj.getString("type"))) {//自定义消息内容
        				String contentMsg = msgObj.getString("content");
        				if(EmptyUtil.isNotEmpty(contentMsg)) {
        					JSONObject contentMsgObj = JSONObject.parseObject(contentMsg);
        					//JSONObject data = contentMsgObj.getJSONObject("data");
        					String dataStr = contentMsgObj.getString("data");
        					if(EmptyUtil.isNotEmpty(dataStr)) {
        						JSONObject data = JSONObject.parseObject(dataStr);;
        						if("1".equals(data.getString("isImg"))) {
        							rs = rs + data.getString("chatImgUrl");
        						}
        					}
        				}
        			}
        			if("TIMTextElem".equals(msgObj.getString("type"))) {//文本消息内容
        				JSONObject contentMsg = msgObj.getJSONObject("content");
        				if(EmptyUtil.isNotEmpty(contentMsg)) {
        					rs = rs + contentMsg.getString("text");
        				}
        			}
        			if("TIMFaceElem".equals(msgObj.getString("type"))) {//表情消息内容
        				JSONObject contentMsg = msgObj.getJSONObject("content");
        				if(EmptyUtil.isNotEmpty(contentMsg)) {
        					rs = rs + contentMsg.getString("data");
        				}
        			}
    			}
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return rs;
    }
    
    private String getManageUserNameFromCache(Integer userId,List<UserManageInfo> list) {
    	if(EmptyUtil.isEmpty(userId)) {
    		return "";
    	}
    	for(UserManageInfo usr:list){
    		if(EmptyUtil.isNotEmpty(usr.getUserId())) {
    			if(userId.intValue() == usr.getUserId().intValue()){
        			return usr.getUserNickName();
        		}
    		}
    	}
    	return "";
    }
    
    @Async
   	@Override
   	public void saveManageMessageLog(RoomChatMessage params) {
    	try {
	    	if(EmptyUtil.isNotEmpty(params.getGroupId()) && params.getGroupId().intValue()==3) {
	    		//TODO 记录审核消息日志
		       BaseOptLog baseOptLog = new BaseOptLog();
		       baseOptLog.setType(4);//4 通过消息
		       baseOptLog.setUserId(params.getPostUid());//消息发送者id
		       baseOptLog.setGroupId(params.getGroupId());//消息发送者角色
		       baseOptLog.setOptUserId(params.getPostUid());//操作用户id
		       
		       List<UserManageInfo> manegeList = this.baseCacheService.getManageUserList(4);
		       String name = getManageUserNameFromCache(params.getPostUid(),manegeList);
		       String msgContent = getMsgContentFromMsg(params.getContent());
		       baseOptLog.setOpeMsg("[" + name + "]发送消息:" + msgContent);
		       baseOptLog.setOptTime(new Date());
		       baseOptLogService.save(baseOptLog);
	    	}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
   	}

    @Async
	@Override
	public void saveAuditMessageLog(RoomChatMessage params) {
		//TODO 记录审核消息日志
    	try {
	        BaseOptLog baseOptLog = new BaseOptLog();
	        baseOptLog.setType(0);//0 通过消息
	        baseOptLog.setUserId(params.getPostUid());//消息发送者id
	        baseOptLog.setGroupId(params.getGroupId());//消息发送者角色
	        baseOptLog.setOptUserId(params.getAuditUid());//操作用户id
	        List<UserManageInfo> manegeList = this.baseCacheService.getManageUserList(4);
	        String auditName = getManageUserNameFromCache(params.getAuditUid(),manegeList);
	        String msgContent = getMsgContentFromMsg(params.getContent());
	        String postNickName = params.getPostNickName();
	        baseOptLog.setOpeMsg("[" + auditName + "]通过了[ " +postNickName + "]的消息:" + msgContent);
	        baseOptLog.setOptTime(new Date());
	        baseOptLogService.save(baseOptLog);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
	}

    @Async
	@Override
	public void saveDelMessageLog(RoomChatMessage params) {
    	try {
	        BaseOptLog baseOptLog = new BaseOptLog();
	        baseOptLog.setType(1);//1 删除消息
	        baseOptLog.setUserId(params.getPostUid());//消息发送者id
	        baseOptLog.setGroupId(params.getGroupId());//发送消息组
	        baseOptLog.setOptUserId(params.getAuditUid());//操作用户id
	        List<UserManageInfo> manegeList = this.baseCacheService.getManageUserList(4);
	        String auditName = getManageUserNameFromCache(params.getAuditUid(),manegeList);
	        String msgContent = getMsgContentFromMsg(params.getContent());
	        baseOptLog.setOpeMsg("[" + auditName + "]删除了[ " +params.getPostNickName() + "]的消息:" + msgContent);
	        baseOptLog.setOptTime(new Date());
	        baseOptLogService.save(baseOptLog);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
	}

}

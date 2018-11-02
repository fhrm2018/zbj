package com.qiyou.dhlive.core.room.service.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.gson.Gson;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.room.outward.model.RoomChatMessage;
import com.qiyou.dhlive.core.room.outward.service.IRoomChatMessageService;
import com.qiyou.dhlive.core.room.service.dao.RoomChatMessageMapper;
import com.qiyou.dhlive.core.user.outward.service.IUserVipInfoService;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import com.yaozhong.framework.base.database.redis.RedisManager;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-20
 */
@Service
public class RoomChatMessageServiceImpl extends BaseMyBatisService<RoomChatMessage> implements IRoomChatMessageService {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private RoomChatMessageMapper mapper;

    @Autowired
    private IUserVipInfoService userVipInfoService;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    public RoomChatMessageServiceImpl() {
        super.setEntityClazz(RoomChatMessage.class);
    }

    @Override
    public DataResponse saveChatMessage(RoomChatMessage message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (message.getGroupId().intValue() == 5) {
            if (EmptyUtil.isEmpty(message.getLevel())) {
                String level = this.userVipInfoService.findById(message.getPostUid()).getUserLevel();
                message.setLevel(Integer.parseInt(level));
            }
        }
        //管理员发送不用审核
        if (message.getStatus().intValue() == 1) {  
            message.setAuditTime(new Date());
        }
        message.setCreateTime(new Date());
        message.setMessageTime(new Date());
        message.setSendTime(sdf.format(new Date()));
        String itemJson = JSON.toJSONString(message);
        redisManager.saveHash(RedisKeyConstant.MESSAGE_INFO, message.getUniqueId(), itemJson);
        List<String> uuidList=redisManager.getAllValuesFromListByStoreKey(RedisKeyConstant.MESSAGE_INFO_LIST);
        List<String> removeUuidList=Lists.newArrayList();
        if(uuidList.size()>500) {
        	for(int i=500;i<uuidList.size();i++) {
        		removeUuidList.add(uuidList.get(i));
        		this.redisManager.deleteFromListByByStoreKeyAndValue(RedisKeyConstant.MESSAGE_INFO_LIST, uuidList.get(i));
        	}
        }
        if(EmptyUtil.isNotEmpty(removeUuidList)) {
        	for(String key:removeUuidList) {
        		redisManager.deleteFromHashByStoreKeyAndMapKey(RedisKeyConstant.MESSAGE_INFO, key);
        	}
        }
        
        List<String> messageList=Lists.newArrayList();
        messageList.add(message.getUniqueId());
        redisManager.saveList(RedisKeyConstant.MESSAGE_INFO_LIST, messageList);
        return new DataResponse(1000, "保存成功.");
    }

    @Override
    public DataResponse auditChatMessage(RoomChatMessage message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            message.setCreateTime(sdf.parse(message.getSendTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String itemJson = JSON.toJSONString(message);
        redisManager.saveHash(RedisKeyConstant.MESSAGE_INFO, message.getUniqueId(), itemJson);

        //TODO 记录审核消息日志

        return new DataResponse(1000, "success");
    }


    @Override
    public DataResponse getChatMessageByUser(RoomChatMessage message) {
        List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.MESSAGE_INFO);
        List<RoomChatMessage> result = new ArrayList<RoomChatMessage>();
        if (EmptyUtil.isNotEmpty(listJson)) {
            for (int i = 0; i < listJson.size(); i++) {
                RoomChatMessage record = JSON.parseObject(listJson.get(i), RoomChatMessage.class);
                //只看到当前房间消息
                if (message.getRoomId() == record.getRoomId()) {
                    result.add(record);
                }
            }
        }

        Ordering<RoomChatMessage> sortByStatus = Ordering.natural().onResultOf(
                new Function<RoomChatMessage, String>() {
                    public String apply(RoomChatMessage data) {
                        return data.getSendTime();
                    }
                });
        List<RoomChatMessage> messageList = sortByStatus.sortedCopy(result);

        List<RoomChatMessage> data = new ArrayList<RoomChatMessage>();
        int j = 0;
        for (int i = messageList.size() - 1; j <= 100 && j < messageList.size(); j++) {
            data.add(messageList.get(i));
            i--;
        }
        Collections.reverse(data);
        baseLog.info(LogFormatUtil.getActionFormat("用户获取聊天记录返回结果数量:" + new Gson().toJson(data.size())));
        return new DataResponse(1000, data);
    }

    @Override
    public DataResponse getChatMessageByAdmin(RoomChatMessage message) {
        List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.MESSAGE_INFO);
        List<RoomChatMessage> result = new ArrayList<RoomChatMessage>();
        if (EmptyUtil.isNotEmpty(listJson)) {
            for (int i = 0; i < listJson.size(); i++) {
                RoomChatMessage record = JSON.parseObject(listJson.get(i), RoomChatMessage.class);
                //当前房间的所有消息
                if (message.getRoomId() == record.getRoomId()) {
                    result.add(record);
                }
            }
        }
        Ordering<RoomChatMessage> sortByStatus = Ordering.natural().onResultOf(
                new Function<RoomChatMessage, String>() {
                    public String apply(RoomChatMessage data) {
                        return data.getSendTime();
                    }
                });
        List<RoomChatMessage> messageList = sortByStatus.sortedCopy(result);
        List<RoomChatMessage> data = new ArrayList<RoomChatMessage>();
        int j = 0;
        for (int i = messageList.size() - 1; j <= 100 && j < messageList.size(); j++) {
            data.add(messageList.get(i));
            i--;
        }
        Collections.reverse(data);
        baseLog.info(LogFormatUtil.getActionFormat("管理员获取聊天记录返回结果数量:" + new Gson().toJson(data.size())));
        return new DataResponse(1000, data);
    }

    @Override
    public DataResponse deleteChatMessage(RoomChatMessage params) {
        List<String> listJson = redisManager.getValuesFromMapByStoreKeyAndMapKey(RedisKeyConstant.MESSAGE_INFO, params.getUniqueId());
        Integer postUid = null;
        if (EmptyUtil.isNotEmpty(listJson) && EmptyUtil.isNotEmpty(listJson.get(0))) {
            if (listJson.get(0) != null) {
                RoomChatMessage msg = JSON.parseObject(listJson.get(0), RoomChatMessage.class);
                msg.setIsDelete(1);//删除标记
                String msgJson = JSON.toJSONString(msg);
                redisManager.saveHash(RedisKeyConstant.MESSAGE_INFO, params.getUniqueId(), msgJson);
                postUid = msg.getPostUid();
            }
        }

        //TODO 记录删除消息日志

        return new DataResponse(1000, "success", postUid);
    }


    @Override
    public DataResponse getChatMessageByUniqueId(RoomChatMessage params) {
        List<String> listJson = redisManager.getValuesFromMapByStoreKeyAndMapKey(RedisKeyConstant.MESSAGE_INFO, params.getUniqueId());
        if (EmptyUtil.isNotEmpty(listJson) && EmptyUtil.isNotEmpty(listJson.get(0))) {
            if (listJson.get(0) != null) {
                RoomChatMessage msg = JSON.parseObject(listJson.get(0), RoomChatMessage.class);
                return new DataResponse(1000, msg);
            }
        }
        return new DataResponse(1001, "该消息未找到.", params.getUniqueId());
    }
    
    @Override
    public DataResponse getMessageList() {
    	List<String> msgList=redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.MESSAGE_INFO);
    	List<String> msgUUidList=this.redisManager.getAllValuesFromListByStoreKey(RedisKeyConstant.MESSAGE_INFO_LIST);
    	Map<String,RoomChatMessage> msgDetailMap=Maps.newHashMap();
    	for(String json:msgList) {
    		RoomChatMessage msg=JSON.parseObject(json, RoomChatMessage.class);
    		msgDetailMap.put(msg.getUniqueId(), msg);
    	}
    	List<RoomChatMessage> msgDataList=Lists.newArrayList();
    	for(int i=msgUUidList.size()-1;i<msgUUidList.size();i--) {
    		msgDataList.add(msgDetailMap.get(msgUUidList.get(i)));
    	}
    	return new DataResponse(1000,msgDataList);
    }
    
}
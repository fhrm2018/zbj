package com.qiyou.dhlive.api.base.service.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.api.base.outward.vo.UserInfoDTO;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.room.outward.model.RoomChatMessage;
import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserManageInfoService;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.MyBeanUtils;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;

@Service
public class BaseCacheServiceImpl implements IBaseCacheService {
	
	@Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;
	
	@Autowired
	private IUserManageInfoService userManageInfoService;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	public static final String USER_INFO = "dhlive-basedata-userinfo-";
	
	public static final String NEWUSER_LIST = "dhlive-basedata-newuserlist";
	
	public static final String ONLINEMANAGER_LIST = "dhlive-basedata-newuserlist";
	

	public static final String MESSAGE_LIST = "dhlive-basedata-messagelist";

	@Override
	public UserInfoDTO getUserInfo(Integer userId) {
		String dataJson = redisManager.getStringValueByKey(USER_INFO+userId);
		if(EmptyUtil.isEmpty(dataJson)) {
			return updateUserInfo(userId);
		}
		UserInfoDTO data = JSON.parseObject(dataJson,UserInfoDTO.class);
		return data;
	}
	
	@Override
	public UserInfoDTO updateUserInfo(Integer userId) {
		// TODO Auto-generated method stub
		UserInfo data = this.userInfoService.findById(userId);
		if(EmptyUtil.isEmpty(data)) {
			return null;
		}
		UserInfoDTO dataDto = MyBeanUtils.copyBean(data, UserInfoDTO.class);
		String dataJson = JSON.toJSONString(dataDto);
		redisManager.saveStringBySeconds(USER_INFO+userId, dataJson, 60*60*24);
		return dataDto;
	}

	@Override
	public UserInfoDTO createNewGuestUser(String ipAddress, String utmSource) {
		// TODO Auto-generated method stub
		List<String> listJson = redisManager.getMapValueFromMapByStoreKey(NEWUSER_LIST);
		return null;
	}
	
	@Override
	public List<UserManageInfo> getManageUserList(Integer roomId) {
		String dataJson = redisManager.getStringValueByKey(ONLINEMANAGER_LIST+roomId);
		if(EmptyUtil.isEmpty(dataJson)) {
			return updateManageUserList(roomId);
		}
		List<UserManageInfo> data = JSON.parseArray(dataJson,UserManageInfo.class);
		return data;
	}
	
	@Override
	public List<UserManageInfo> updateManageUserList(Integer roomId) {
		UserManageInfo params = new UserManageInfo();
        params.setStatus(0);
        params.setRoomId(roomId);
        params.setIsOnline(1);
		SearchCondition<UserManageInfo> condition = new SearchCondition<UserManageInfo>(params);
        List<UserManageInfo> data = this.userManageInfoService.findByCondition(condition);
		if(EmptyUtil.isEmpty(data)) {
			return null;
		}
		String dataJson = JSON.toJSONString(data);
		redisManager.saveStringBySeconds(ONLINEMANAGER_LIST+roomId, dataJson, 60*60*24);
		return data;
	}
	
	public List<RoomChatMessage> getChatMessageByUser(Integer roomId){
		String dataJson = redisManager.getStringValueByKey(MESSAGE_LIST+roomId);
		if(EmptyUtil.isEmpty(dataJson)) {
			return updateChatMessageByUser(roomId);
		}
		List<RoomChatMessage> data = JSON.parseArray(dataJson,RoomChatMessage.class);
		return data;
	}
	
	public List<RoomChatMessage> updateChatMessageByUser(Integer roomId){
		List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.MESSAGE_INFO);
        List<RoomChatMessage> result = new ArrayList<RoomChatMessage>();
        if (EmptyUtil.isNotEmpty(listJson)) {
            for (int i = 0; i < listJson.size(); i++) {
                RoomChatMessage record = JSON.parseObject(listJson.get(i), RoomChatMessage.class);
                //只看到当前房间消息
                if (roomId.intValue() == record.getRoomId().intValue()) {
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
        if(EmptyUtil.isEmpty(data)) {
			return null;
		}
        String dataJson = JSON.toJSONString(data);
		redisManager.saveStringBySeconds(MESSAGE_LIST+roomId, dataJson, 60*60*5);
		return data;
	}

}

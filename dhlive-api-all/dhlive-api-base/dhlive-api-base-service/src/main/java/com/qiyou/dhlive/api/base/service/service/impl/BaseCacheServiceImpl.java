package com.qiyou.dhlive.api.base.service.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.api.base.outward.vo.UserInfoDTO;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.room.outward.model.RoomAutoMsg;
import com.qiyou.dhlive.core.room.outward.model.RoomAutoUser;
import com.qiyou.dhlive.core.room.outward.model.RoomChatMessage;
import com.qiyou.dhlive.core.room.outward.model.RoomDuty;
import com.qiyou.dhlive.core.room.outward.model.RoomPlan;
import com.qiyou.dhlive.core.room.outward.service.IRoomAutoMsgService;
import com.qiyou.dhlive.core.room.outward.service.IRoomAutoUserService;
import com.qiyou.dhlive.core.room.outward.service.IRoomDutyService;
import com.qiyou.dhlive.core.room.outward.service.IRoomPlanService;
import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.model.UserRelation;
import com.qiyou.dhlive.core.user.outward.service.IUserInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserManageInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserRelationService;
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
	private IRoomDutyService roomDutyService;
	
	@Autowired
	private IRoomPlanService roomPlanService;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	@Autowired
	private IUserRelationService userRelationService;
	
	@Autowired
	private IRoomAutoMsgService roomAutoMsgService;
	
	@Autowired
	private IRoomAutoUserService roomAutoUserService;
	
	public static final String USER_INFO = "dhlive-basedata-userinfo-";
	
	public static final String NEWUSER_LIST = "dhlive-basedata-newuserlist";
	
	public static final String MANAGER_LIST = "dhlive-basedata-managelist";
	
	public static final String DUTYMANAGER_LIST = "dhlive-basedata-dutymanagelist";
	
	public static final String DUTY_USER_LIST = "dhlive-basedata-dutyuserlist";
	
	public static final String RELATION = "dhlive-cachedata-room-relation-";
	
	public static final String DAY_RELATION = "dhlive-cachedata-dayrelation-";

	public static final String MESSAGE_LIST = "dhlive-basedata-messagelist";
	
	public static final String AUTO_MESSAGE_LIST = "dhlive-basedata-automessagelist";
	
	public static final String AUTO_PERSON_COUNT = "dhlive-basedata-autopersoncount";
	
	public static final String AUTO_SENDUSER_COUNT = "dhlive-basedata-autosendUserCount";
	
	public static final String AUTO_SENDUSER_LIST = "dhlive-basedata-autosendUserList";
	
	public static final String SAVE_USERKEFU_LIST = "dhlive-userrelation-waitsavekefulist";

	public static final String USER_ONLINE_TIME = "dhlive-user-onlineTime";

	private static final String ROOM_PLAN_LIST = "dhlive-basedata-roomPlanlist";

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
		if(EmptyUtil.isEmpty(userId)) {
			return null;
		}
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
		String dataJson = redisManager.getStringValueByKey(MANAGER_LIST+roomId);
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
		redisManager.saveStringBySeconds(MANAGER_LIST+roomId, dataJson, 60*60*24);
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

	@Override
	public List<UserManageInfo> getDutyUserByWeek(Integer roomId,Integer weekId) {
		String dataJson = redisManager.getStringValueByKey(DUTYMANAGER_LIST+weekId);
		if(EmptyUtil.isEmpty(dataJson)) {
			return updateDutyUserByWeek(roomId,weekId);
		}
		List<UserManageInfo> data = JSON.parseArray(dataJson,UserManageInfo.class);
		return data;
	}

	@Override
	public List<UserManageInfo> updateDutyUserByWeek(Integer roomId,Integer weekId) {
		UserManageInfo params = new UserManageInfo();
        params.setStatus(0);
        params.setRoomId(roomId);
		SearchCondition<UserManageInfo> condition = new SearchCondition<UserManageInfo>(params);
        List<UserManageInfo> managerList = this.userManageInfoService.findByCondition(condition);

        List<UserManageInfo> data = Lists.newArrayList();
        
        RoomDuty duty = roomDutyService.findById(weekId);
        if(EmptyUtil.isEmpty(duty) || EmptyUtil.isEmpty(duty.getManageIds())) {
        	data = managerList;
        }else {
        	String[] ids = duty.getManageIds().split(",");
        	for(int i=0;i<ids.length;i++) {
        		if(EmptyUtil.isNotEmpty(ids[i])) {
        			for(UserManageInfo user:managerList) {
        				try {
            				Integer idNum = Integer.parseInt(ids[i]);
            				if(idNum.intValue() == user.getUserId().intValue()) {
            					data.add(user);
            				}
            			}catch(Exception e) {
            				
            			}
        			}
        		}
        		
        	}
        }
		if(EmptyUtil.isEmpty(data)) {
			return null;
		}
		String dataJson = JSON.toJSONString(data);
		redisManager.saveStringBySeconds(DUTYMANAGER_LIST+roomId, dataJson, 60*60*24);
		return data;
	}

	@Override
	public String getYkKefuId(Integer userId) {
		String data = this.redisManager.getStringValueByKey(RELATION + "1-" + userId);
		if(EmptyUtil.isEmpty(data)) {
//			return updateYkKefuId(userId);
			return null;
		}
		return data;
	}

	@Override
	public String updateYkKefuId(Integer userId) {
		UserRelation params = new UserRelation();
        params.setUserId(userId);
        params.setGroupId(1);
        params.setStatus(0);
        SearchCondition<UserRelation> condition = new SearchCondition<UserRelation>(params);
        UserRelation relation = this.userRelationService.findOneByCondition(condition);
        if(EmptyUtil.isEmpty(relation) || EmptyUtil.isEmpty(relation.getRelationUserId())) {
        	return null;
        }
        redisManager.saveStringBySeconds(RELATION + "1-" + userId, relation.getRelationUserId()+"", 60*60*24);
		return relation.getRelationUserId()+"";
	}
	
	@Override
	public String updateYkKefuId(Integer userId, Integer kefuId) {
		redisManager.saveStringBySeconds(RELATION + "1-" + userId, kefuId+"", 60*60*24*7);
		return kefuId+"";
	}

	@Override
	public String getVipKefuId(Integer userId) {
		String data = this.redisManager.getStringValueByKey(RedisKeyConstant.RELATION + "5-" + userId);
		if(EmptyUtil.isEmpty(data)) {
			return updateVipKefuId(userId);
		}
		return data;
	}

	@Override
	public String updateVipKefuId(Integer userId) {
		UserRelation params = new UserRelation();
        params.setUserId(userId);
        params.setGroupId(5);
        params.setStatus(0);
        SearchCondition<UserRelation> condition = new SearchCondition<UserRelation>(params);
        UserRelation relation = this.userRelationService.findOneByCondition(condition);
        if(EmptyUtil.isEmpty(relation) || EmptyUtil.isEmpty(relation.getRelationUserId())) {
        	return null;
        }
        redisManager.saveStringBySeconds(RELATION + "1-" + userId, relation.getRelationUserId()+"", 60*60*24);
		return relation.getRelationUserId()+"";
	}

	@Override
	public String getYkKefuIdByDay(Integer userId, String day) {
		return redisManager.getStringValueByKey(DAY_RELATION+day+"-"+userId);
	}

	@Override
	public String updateYkKefuIdByDay(Integer userId, String day, Integer kefuId) {
		// TODO Auto-generated method stub
		redisManager.saveStringBySeconds(DAY_RELATION+day+"-"+userId, kefuId+"", 60*60*24);
		return kefuId+"";
	}

	@Override
	public List<RoomAutoMsg> getAllRoomAutoMsg(){
		String json=this.redisManager.getStringValueByKey(AUTO_MESSAGE_LIST);
		if(EmptyUtil.isEmpty(json)) {
			return updateAllRoomAutoMsg();
		}
		return JSON.parseArray(json, RoomAutoMsg.class);
	}
	
	@Override
	public List<RoomAutoMsg> updateAllRoomAutoMsg(){
		List<RoomAutoMsg> msgList=this.roomAutoMsgService.findByCondition(new SearchCondition<RoomAutoMsg>(new RoomAutoMsg()));
		if(EmptyUtil.isEmpty(msgList)) {
			return Lists.newArrayList();
		}
		this.redisManager.saveString(AUTO_MESSAGE_LIST,JSON.toJSONString(msgList));
		return msgList;
	}
	@Override
	public List<RoomPlan> getAllRoomPlan(){
		String json=this.redisManager.getStringValueByKey(ROOM_PLAN_LIST);
		if(EmptyUtil.isEmpty(json)) {
			return updateAllRoomPlan();
		}
		return JSON.parseArray(json, RoomPlan.class);
	}
	@Override
	public List<RoomPlan> updateAllRoomPlan(){
		SearchCondition<RoomPlan> condition=new SearchCondition<RoomPlan>(new RoomPlan());
		condition.buildOrderByConditions("planNumber", "asc");
		List<RoomPlan> planList=this.roomPlanService.findByCondition(condition);
		if(EmptyUtil.isEmpty(planList)) {
			return Lists.newArrayList();
		}
		this.redisManager.saveString(ROOM_PLAN_LIST,JSON.toJSONString(planList));
		return planList;
	}

	@Override
	public int getAutoPersonCount() {
		String autoCount=this.redisManager.getStringValueByKey(AUTO_PERSON_COUNT);
		if(EmptyUtil.isNotEmpty(autoCount))
			return Integer.parseInt(autoCount);
		return 0;
	}

	@Override
	public int updateAutoPersonCount(int count) {
		String autoCount=this.redisManager.getStringValueByKey(AUTO_PERSON_COUNT);
		if(EmptyUtil.isEmpty(autoCount)) {
			this.redisManager.saveString(AUTO_PERSON_COUNT,count+"");
		}
		int sumCount=Integer.parseInt(autoCount)+count;
		if(sumCount<=0) {
			sumCount=0;
		}
		this.redisManager.saveString(AUTO_PERSON_COUNT,sumCount+"");
		return sumCount;
	}

	@Override
	public List<String> getAutoMsgUser() {
		String json=this.redisManager.getStringValueByKey(AUTO_SENDUSER_COUNT);
		if(EmptyUtil.isEmpty(json)) {
			List<String> youkeList=Lists.newArrayList();
			for(int i=0;i<10;i++) {
				youkeList.add("游客MY"+getRandomString(8));
			}
			redisManager.saveStringBySeconds(AUTO_SENDUSER_COUNT,JSON.toJSONString(youkeList), 24*60*60);
			return youkeList;
		}else {
			List<String> youkeList=JSON.parseArray(json, String.class);
			if(youkeList.size()>=10) {
				return youkeList;
			}
			for(int i=0;i<(10-youkeList.size());i++) {
				youkeList.add("游客MY"+getRandomString(8));
			}
			redisManager.saveStringBySeconds(AUTO_SENDUSER_COUNT,JSON.toJSONString(youkeList), 24*60*60);
			return youkeList;
		}
	}

	@Override
	public List<String> addAutoMsgUser() {
		
		return null;
	}
	
	
	public List<RoomAutoUser> getAllRoomAutoUserList(){
		String json=this.redisManager.getStringValueByKey(AUTO_SENDUSER_LIST);
		if(EmptyUtil.isEmpty(json)) {
			return updateAllRoomAutoUser();
		}
		return JSON.parseArray(json,RoomAutoUser.class);
	}
	
	public List<RoomAutoUser> updateAllRoomAutoUser(){
		SearchCondition<RoomAutoUser> condition=new SearchCondition<RoomAutoUser>(new RoomAutoUser());
		List<RoomAutoUser> userList=this.roomAutoUserService.findByCondition(condition);
		redisManager.saveString(AUTO_SENDUSER_LIST, JSON.toJSONString(userList));
		return userList;
	}
	
	public void updateYoukeKefuList(UserRelation relation) {
		List<String> list=Lists.newArrayList();
		list.add(JSON.toJSONString(relation));
		this.redisManager.saveList(SAVE_USERKEFU_LIST, list);
	}
	
	public List<String> getYoukeKefuList() {
		List<String> list=this.redisManager.getValuesFromListByStoreKey(SAVE_USERKEFU_LIST, -500, -1);
		return list;
	}
	
	public void removeYoukeKefuList(String value) {
		this.redisManager.deleteFromListByByStoreKeyAndValue(SAVE_USERKEFU_LIST, value);
	}
	
	
    public String getRandomString(long length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    
    public int getUserOnlineTime(Integer userId) {
    	List<String> times=this.redisManager.getValuesFromMapByStoreKeyAndMapKey(this.USER_ONLINE_TIME, userId.toString());
    	if(EmptyUtil.isEmpty(times))
    		return 0;
    	return Integer.parseInt(times.get(0));
    }
    
    
    public Set<String> getAllOnlineUser(){
    	Set<String> userIds=this.redisManager.getMapKeyFromMapByStoreKey(USER_ONLINE_TIME);
    	return userIds;
    }
    
    public void updateUserOnlineTime(Integer userId,int times) {
    	List<String> timesList=this.redisManager.getValuesFromMapByStoreKeyAndMapKey(this.USER_ONLINE_TIME, userId.toString());
    	if(EmptyUtil.isEmpty(timesList)) {
    		this.redisManager.saveHash(USER_ONLINE_TIME, userId.toString(), String.valueOf(times));
    	}else {
    		if(EmptyUtil.isNotEmpty(timesList.get(0))&&!"null".equalsIgnoreCase(timesList.get(0))) {
    			this.redisManager.saveHash(USER_ONLINE_TIME, userId.toString(), String.valueOf(times+Integer.parseInt(timesList.get(0))));
    		}else {
    			this.redisManager.saveHash(USER_ONLINE_TIME, userId.toString(), String.valueOf(times));
    		}
    	}
    		
    }
    
    public void removeUserOnlineTime(Integer userId) {
    	this.redisManager.deleteFromHashByStoreKeyAndMapKey(this.USER_ONLINE_TIME, userId.toString());
    	
    }
    
    
}

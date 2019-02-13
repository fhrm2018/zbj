package com.qiyou.dhlive.prd.cron.controller;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.api.base.outward.util.NoticeUtil;
import com.qiyou.dhlive.api.base.outward.util.TLSUtils;
import com.qiyou.dhlive.api.base.outward.vo.UserInfoDTO;
import com.qiyou.dhlive.core.base.outward.model.BaseOptLog;
import com.qiyou.dhlive.core.base.outward.service.IBaseOptLogService;
import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.live.outward.model.LiveRoom;
import com.qiyou.dhlive.core.live.outward.service.ILiveRoomService;
import com.qiyou.dhlive.core.room.outward.model.RoomAutoMsg;
import com.qiyou.dhlive.core.room.outward.model.RoomAutoUser;
import com.qiyou.dhlive.core.room.outward.model.RoomChatMessage;
import com.qiyou.dhlive.core.room.outward.model.RoomMsgCount;
import com.qiyou.dhlive.core.room.outward.service.IRoomChatMessageService;
import com.qiyou.dhlive.core.room.outward.service.IRoomMsgCountService;
import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.model.UserRelation;
import com.qiyou.dhlive.core.user.outward.service.IUserInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserRelationService;
import com.yaozhong.framework.base.common.utils.DateStyle;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.domain.search.RangeCondition;
import com.yaozhong.framework.base.database.domain.search.RangeConditionType;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;

/**
 * Created by ThinkPad on 2018/3/24.
 */
@Controller
public class CronController {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IBaseCacheService baseCacheService;
    
    @Autowired
    private IBaseSysParamService baseSysParamService;
    
    @Autowired
    private IRoomChatMessageService roomChatMessageService;
    
    @Autowired
    private ILiveRoomService liveRoomService;
    
    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;
    
    @Autowired
    private IUserRelationService userRelationService;
    
    @Autowired
    private IUserInfoService userInfoService;
    
    @Autowired
    private IRoomMsgCountService roomMsgCountService;
    
    @Autowired
    private IBaseOptLogService baseOptLogService;

    //游客定时任务
    @Scheduled(cron = "0/15 * *  * * ? ")   //每15秒执行一次
    public void clearYKOutLineUserCron() {
        baseLog.info(LogFormatUtil.getActionFormat("定时任务清理下线游客开始"));
        List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.YK_IDS);
        for (int i = 0; i < listJson.size(); i++) {
            String str[] = listJson.get(i).split("-");
            if("null".equalsIgnoreCase(str[1])) {
            	 redisManager.deleteFromHashByStoreKeyAndMapKey(RedisKeyConstant.YK_IDS, str[1]);
            }
            Long onLine = new Date().getTime() - Long.parseLong(str[0]);
            if (onLine > 10000) {
                redisManager.deleteFromHashByStoreKeyAndMapKey(RedisKeyConstant.YK_IDS, str[1]);
                if("null".equalsIgnoreCase(str[1])) {
                	continue;
                }
                this.baseCacheService.updateUserOnlineTime(Integer.parseInt(str[1]),(int)(onLine/1000));
            }
            
            UserInfoDTO userInfo = this.baseCacheService.getUserInfo(Integer.parseInt(str[1]));
            UserInfo u=new UserInfo();
            u.setUserId(Integer.parseInt(str[1]));
            u.setLookTime(userInfo.getLookTime().intValue()+(int)(onLine/1000));
            this.userInfoService.modifyEntity(u);
            this.baseCacheService.updateUserInfo(Integer.parseInt(str[1]));
        }
        
        
        Set<String> zlOnline=redisManager.getMapKeyFromMapByStoreKey(RedisKeyConstant.ZL_ONLINE_IDS);
        
        for(String key:zlOnline) {
        	List<String> times=redisManager.getValuesFromMapByStoreKeyAndMapKey(RedisKeyConstant.ZL_ONLINE_IDS, key);
        	Long online = System.currentTimeMillis()-Long.decode(times.get(0));
        	if(online>=60000) {
        		redisManager.deleteFromHashByStoreKeyAndMapKey(RedisKeyConstant.ZL_ONLINE_IDS, key);
        	}
        }
        
        baseLog.info(LogFormatUtil.getActionFormat("定时任务清理下线游客结束"));
    }


    //助理定时任务
//    @Scheduled(cron = "0/15 * *  * * ? ")   //每15秒执行一次
//    public void clearZLOutLineUserCron() {
//        baseLog.info(LogFormatUtil.getActionFormat("定时任务清理下线成员开始"));
//        List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.ZL_IDS);
//        for (int i = 0; i < listJson.size(); i++) {
//            String str[] = listJson.get(i).split("-");
//            Long onLine = new Date().getTime() - Long.parseLong(str[0]);
//            if (onLine > 10000) {
//                redisManager.deleteFromHashByStoreKeyAndMapKey(RedisKeyConstant.ZL_IDS, str[1]);
//            }
//        }
//        baseLog.info(LogFormatUtil.getActionFormat("定时任务清理下线成员结束"));
//    }


    //VIP定时任务
    @Scheduled(cron = "0/15 * *  * * ? ")   //每15秒执行一次
    public void clearVIPOutLineUserCron() {
        baseLog.info(LogFormatUtil.getActionFormat("定时任务清理下线VIP开始"));
        List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.VIP_IDS);
        for (int i = 0; i < listJson.size(); i++) {
            String str[] = listJson.get(i).split("-");
            Long onLine = new Date().getTime() - Long.parseLong(str[0]);
            if (onLine > 10000) {
                redisManager.deleteFromHashByStoreKeyAndMapKey(RedisKeyConstant.VIP_IDS, str[1]);
            }
        }
        baseLog.info(LogFormatUtil.getActionFormat("定时任务清理下线VIP结束"));
    }

    
    @Scheduled(cron = "0 0/25 * * * ? ") 
    public void addAutoPersonCountFirst() {
    	String beginDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"08:50:00";
    	String endDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"17:50:00";
    	Date beginDate=DateUtil.StringToDate(beginDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	Date endDate=DateUtil.StringToDate(endDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	
    	boolean beginRes=DateUtil.checkLessTime(beginDate, new Date());
    	boolean endRes=DateUtil.checkLessTime(endDate, new Date());
    	if(!beginRes) {
    		return;
    	}
    	if(endRes) {
    		return ;
    	}
    	int readyCount=this.baseCacheService.getAutoPersonCount();
    	int count = (int)(150+Math.random()*(200-150+1));
    	if(readyCount<=1000) {
    		count=count+1000;
    	}
    	baseCacheService.updateAutoPersonCount(count);
    	
    }
    
    @Scheduled(cron = "0 0/25 * * * ? ")
    public void addAutoPersonCountSecond() {
    	String beginDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"18:20:00";
    	String endDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"21:50:00";
    	Date beginDate=DateUtil.StringToDate(beginDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	Date endDate=DateUtil.StringToDate(endDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	
    	boolean beginRes=DateUtil.checkLessTime(beginDate, new Date());
    	boolean endRes=DateUtil.checkLessTime(endDate, new Date());
    	if(!beginRes) {
    		return;
    	}
    	if(endRes) {
    		return ;
    	}
    	
    	int count = (int)(100+Math.random()*(120-100+1));
    	baseCacheService.updateAutoPersonCount(count);
    	
    }
    
    @Scheduled(cron = "0 0/25 * * * ? ")
    public void addAutoPersonCountThird() {
    	String beginDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"22:20:00";
    	String endDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"23:59:59";
    	Date beginDate=DateUtil.StringToDate(beginDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	Date endDate=DateUtil.StringToDate(endDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	
    	boolean beginRes=DateUtil.checkLessTime(beginDate, new Date());
    	boolean endRes=DateUtil.checkLessTime(endDate, new Date());
    	if(!beginRes) {
    		return;
    	}
    	if(endRes) {
    		return ;
    	}
    	
    	int count = (int)(0+Math.random()*(20-0+1));
    	baseCacheService.updateAutoPersonCount(count);
    	
    }
    
    @Scheduled(cron = "0 0/15 * * * ? ")
    public void addAutoPersonCountFourth() {
    	String beginDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"00:00:00";
    	String endDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"00:20:00";
    	Date beginDate=DateUtil.StringToDate(beginDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	Date endDate=DateUtil.StringToDate(endDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	
    	boolean beginRes=DateUtil.checkLessTime(beginDate, new Date());
    	boolean endRes=DateUtil.checkLessTime(endDate, new Date());
    	if(!beginRes) {
    		return;
    	}
    	if(endRes) {
    		return ;
    	}
    	
    	
    	int count = (int)(0+Math.random()*(20-0+1));
    	baseCacheService.updateAutoPersonCount(count);
    	
    }
    
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void subAutoPersonCountFirst() {
    	String beginDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"00:25:00";
    	String endDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"02:00:00";
    	Date beginDate=DateUtil.StringToDate(beginDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	Date endDate=DateUtil.StringToDate(endDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	boolean beginRes=DateUtil.checkLessTime(beginDate, new Date());
    	boolean endRes=DateUtil.checkLessTime(endDate, new Date());
    	if(!beginRes) {
    		return;
    	}
    	if(endRes) {
    		return ;
    	}
    	
    	int count = (int)(150+Math.random()*(200-150+1));
    	int a=this.baseCacheService.getAutoPersonCount();
    	if(a==0)
    		return ;
    	baseCacheService.updateAutoPersonCount(0-count);
    }
    
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void subAutoPersonCountThird() {
    	String beginDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"02:05:00";
    	String endDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"04:00:00";
    	Date beginDate=DateUtil.StringToDate(beginDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	Date endDate=DateUtil.StringToDate(endDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	
    	boolean beginRes=DateUtil.checkLessTime(beginDate, new Date());
    	boolean endRes=DateUtil.checkLessTime(endDate, new Date());
    	if(!beginRes) {
    		return;
    	}
    	if(endRes) {
    		return ;
    	}
    	
    	int count = (int)(250+Math.random()*(300-250+1));
    	int a=this.baseCacheService.getAutoPersonCount();
    	if(a<=0)
    		return ;
    	if(a<=1000)
    		count=a;
    	baseCacheService.updateAutoPersonCount(0-count);
    }
    
    @Scheduled(cron = "0/55 * * * * ? ")
    public void sendAutoMsg() {
    	String value=this.baseSysParamService.getValueByKey("auto_send_msg");
    	if(!value.equals("1"))
    		return ;
    	List<RoomAutoMsg> msgList=this.baseCacheService.getAllRoomAutoMsg();
    	if(EmptyUtil.isEmpty(msgList))
    		return ;
        String roomStr = this.redisManager.getStringValueByKey(RedisKeyConstant.ROOM + 4);
        LiveRoom room = new LiveRoom();
        if (EmptyUtil.isEmpty(roomStr)) {
            room = this.liveRoomService.findById(4);
            this.redisManager.saveString(RedisKeyConstant.ROOM + 4, new Gson().toJson(room));
        } else {
        	room=new Gson().fromJson(roomStr, LiveRoom.class);
        }
    	String privateKey = this.baseSysParamService.getValueByKey("private_key");
    	String identifier = this.baseSysParamService.getValueByKey("identifier");
    	String appKey = this.baseSysParamService.getValueByKey("sdk_app_id");
    	String userKey=TLSUtils.getUserSig(Integer.parseInt(appKey), identifier, privateKey);
    	List<String> youkeList=this.baseCacheService.getAutoMsgUser();
    	List<RoomAutoUser> autoUserList=this.baseCacheService.getAllRoomAutoUserList();
    	if(EmptyUtil.isEmpty(autoUserList)) {
    		autoUserList=Lists.newArrayList();
    	}
    	for(String y:youkeList) {
    		RoomAutoUser autoUser=new RoomAutoUser();
    		autoUser.setLevel(0);
    		autoUser.setAutoUserName(y);
    		autoUserList.add(autoUser);
    	}
    	int youkeIndex = (int)(0+Math.random()*(autoUserList.size()-1));
    	int msgIndex = (int)(0+Math.random()*(msgList.size()-1));
    	Integer level=autoUserList.get(youkeIndex).getLevel();
    	Integer groupId = level.intValue()==0?1:5;
    	RoomChatMessage chatMsg=NoticeUtil.sendAutoGroupMsg(
    			autoUserList.get(youkeIndex).getAutoUserName(), 
    			msgList.get(msgIndex).getMsgContent(), 
    			userKey, 
    			identifier, 
    			appKey,
    			room.getRoomGroupId(),
    			groupId,
    			level
    			
    			);
    	this.roomChatMessageService.saveChatMessage(chatMsg);
    }
    
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void saveUserRelation() {
    	List<String> list=this.baseCacheService.getYoukeKefuList();
    	List<Object> updateUserIdList=Lists.newArrayList();
    	List<UserRelation> relationList=Lists.newArrayList();
    	for(String json:list) {
    		UserRelation re=JSON.parseObject(json,UserRelation.class);
    		updateUserIdList.add(re.getUserId());
    		relationList.add(re);
    		this.baseCacheService.removeYoukeKefuList(json);
    	}
    	
    	if(EmptyUtil.isNotEmpty(updateUserIdList)) {
	    	UserRelation oldParam = new UserRelation();
			oldParam.setStatus(0);
			oldParam.setGroupId(1);
			UserRelation upRation = new UserRelation();
			upRation.setStatus(1);
			SearchCondition<UserRelation> condition = new SearchCondition<UserRelation>(oldParam);
			condition.buildInConditions("userId", updateUserIdList);
			this.userRelationService.modifyEntityByCondition(upRation, condition);
    	}
    	for(UserRelation re:relationList) {
	    	userRelationService.save(re);
    	}
    }
    
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void saveUserOnlineTime() {
    	Set<String> list=this.baseCacheService.getAllOnlineUser();
    	for(String key:list) {
    		UserInfoDTO user=this.baseCacheService.getUserInfo(Integer.parseInt(key));
    		if(EmptyUtil.isEmpty(user)) {
    			continue;
    		}
    		int times=this.baseCacheService.getUserOnlineTime(user.getUserId());
    		
    		if(times<=0) {
    			continue;
    		}
    		baseCacheService.removeUserOnlineTime(user.getUserId());
    		UserInfo userInfo=new UserInfo();
    		userInfo.setUserId(user.getUserId());
    		if(EmptyUtil.isEmpty(user.getLookTime())) {
    			userInfo.setLookTime(times);
    		}else {
    			userInfo.setLookTime(user.getLookTime().intValue()+times);
    		}
    		this.userInfoService.modifyEntity(userInfo);
    		this.baseCacheService.updateUserInfo(user.getUserId());
    	}
    }
    
    @Scheduled(cron = "0 0/20 * * * ? ")
    public void saveMsgCount() {
    	String beginStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" 00:00:00";
    	Date beginDate=DateUtil.StringToDate(beginStr, DateStyle.YYYY_MM_DD_HH_MM_SS);
    	String endStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" 00:30:00";
    	Date endDate=DateUtil.StringToDate(endStr, DateStyle.YYYY_MM_DD_HH_MM_SS);
    	boolean beginRes=DateUtil.checkLessTime(beginDate, new Date());
    	boolean endRes=DateUtil.checkLessTime(endDate, new Date());
    	
    	boolean isYesterdayFinish=false;
    	if(beginRes&&!endRes) {
    		isYesterdayFinish=true;
    	}
    	
    	List<UserManageInfo> manageList=baseCacheService.getManageUserList(4);
    	
    	for(UserManageInfo m:manageList) {
    		if(m.getGroupId().intValue()!=3)
    			continue;
    		if(isYesterdayFinish) {
    			RoomMsgCount msgCount=new RoomMsgCount();
    			msgCount.setSendDate(DateUtil.addDay(DateUtil.dateTimeToDate(new Date()), -1));
    			msgCount.setUserId(m.getUserId());
    			msgCount.setIsFinish(0);
//    			msgCount=roomMsgCountService.findOneByCondition(new SearchCondition<RoomMsgCount>(msgCount));
    			
    			SearchCondition<RoomMsgCount> con=new SearchCondition<RoomMsgCount>(msgCount);
    			List<RangeCondition> ranges=Lists.newArrayList();
    			ranges.add(new RangeCondition("sendDate", DateUtil.dateTimeToDate(DateUtil.addDay(new Date(), -1)), RangeConditionType.GreaterThanOrEqual));
    			ranges.add(new RangeCondition("sendDate", DateUtil.dateTimeToDate(new Date()), RangeConditionType.LessThan));
    			con.setRangeConditions(ranges);
    			msgCount=roomMsgCountService.findOneByCondition(con);
    			if(EmptyUtil.isNotEmpty(msgCount)) {
    				if(msgCount.getIsFinish().intValue()==0) {
	    				BaseOptLog baseOptLog=new BaseOptLog();
	    				baseOptLog.setType(4);
	    				baseOptLog.setUserId(m.getUserId());
	    				SearchCondition<BaseOptLog> bcondition=new SearchCondition<BaseOptLog>(baseOptLog);
	    				List<RangeCondition> rangs=Lists.newArrayList();
	    				rangs.add(new RangeCondition("optTime", DateUtil.addDay(beginDate, -1), RangeConditionType.GreaterThanOrEqual));
	    				rangs.add(new RangeCondition("optTime", beginDate, RangeConditionType.LessThan));
	    				bcondition.setRangeConditions(rangs);
	    				Long count = baseOptLogService.countByCondition(bcondition);
	    				msgCount.setSendCount(count.intValue());
	    				msgCount.setIsFinish(1);
	    				this.roomMsgCountService.modifyEntity(msgCount);
    				}
    			}
    		}
    	
    		RoomMsgCount msgCount=new RoomMsgCount();
			msgCount.setUserId(m.getUserId());
			msgCount.setIsFinish(0);
			SearchCondition<RoomMsgCount> con=new SearchCondition<RoomMsgCount>(msgCount);
			List<RangeCondition> ranges=Lists.newArrayList();
			ranges.add(new RangeCondition("sendDate", DateUtil.dateTimeToDate(new Date()), RangeConditionType.GreaterThanOrEqual));
			ranges.add(new RangeCondition("sendDate", DateUtil.dateTimeToDate(DateUtil.addDay(new Date(), 1)), RangeConditionType.LessThan));
			con.setRangeConditions(ranges);
			msgCount=roomMsgCountService.findOneByCondition(con);
			BaseOptLog baseOptLog=new BaseOptLog();
			baseOptLog.setType(4);
			baseOptLog.setUserId(m.getUserId());
			SearchCondition<BaseOptLog> bcondition=new SearchCondition<BaseOptLog>(baseOptLog);
			List<RangeCondition> rangs=Lists.newArrayList();
			rangs.add(new RangeCondition("optTime", beginDate, RangeConditionType.GreaterThanOrEqual));
			bcondition.setRangeConditions(rangs);
			Long count = baseOptLogService.countByCondition(bcondition);
			
			if(EmptyUtil.isNotEmpty(msgCount)) {
				msgCount.setSendCount(count.intValue());
				this.roomMsgCountService.modifyEntity(msgCount);
			}else {
				msgCount=new RoomMsgCount();
				msgCount.setSendDate(DateUtil.dateTimeToDate(new Date()));
				msgCount.setUserId(m.getUserId());
				msgCount.setIsFinish(0);
				msgCount.setCreateTime(new Date());
				msgCount.setSendCount(count.intValue());
				this.roomMsgCountService.save(msgCount);
				
			}
    		
    	}
    	
    }
}

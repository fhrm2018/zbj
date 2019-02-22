package com.qiyou.dhlive.api.base.outward.service;

import java.util.List;
import java.util.Set;

import com.qiyou.dhlive.api.base.outward.vo.UserInfoDTO;
import com.qiyou.dhlive.core.live.outward.model.LiveInform;
import com.qiyou.dhlive.core.room.outward.model.RoomAutoMsg;
import com.qiyou.dhlive.core.room.outward.model.RoomAutoUser;
import com.qiyou.dhlive.core.room.outward.model.RoomChatMessage;
import com.qiyou.dhlive.core.room.outward.model.RoomPlan;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.model.UserRelation;

public interface IBaseCacheService {
	
	UserInfoDTO getUserInfo(Integer userId);
	
	UserInfoDTO updateUserInfo(Integer userId);
	
	UserInfoDTO getUserVip(Integer userId);
	
	UserInfoDTO updateUserVip(Integer userId);
	
	UserInfoDTO getUserManager(Integer userId);
	
	UserInfoDTO updateUserManager(Integer userId);
	
	UserInfoDTO createNewGuestUser(String ipAddress, String utmSource);
	
	List<UserManageInfo> getManageUserList(Integer roomId);
	
	List<UserManageInfo> updateManageUserList(Integer roomId);
	
	List<RoomChatMessage> getChatMessageByUser(Integer roomId);
	
	List<RoomChatMessage> updateChatMessageByUser(Integer roomId);
	
	List<UserManageInfo> getDutyUserByWeek(Integer roomId,Integer weekId);
	
	List<UserManageInfo> updateDutyUserByWeek(Integer roomId,Integer weekId);

	String getYkKefuId(Integer userId);
	
	String updateYkKefuId(Integer userId);
	
	String updateYkKefuId(Integer userId,Integer kefuId);
	
	String getVipKefuId(Integer userId);
	
	String updateVipKefuId(Integer userId);
	
	String getYkKefuIdByDay(Integer userId,String day);
	
	String updateYkKefuIdByDay(Integer userId,String day,Integer kefuId);
	
	List<RoomAutoMsg> getAllRoomAutoMsg();
	
	List<RoomAutoMsg> updateAllRoomAutoMsg();
	
	int getAutoPersonCount();
	
	int updateAutoPersonCount(int count);
	
	List<String> getAutoMsgUser();
	
	List<String> addAutoMsgUser();
	
	List<RoomAutoUser> getAllRoomAutoUserList();
	
	List<RoomAutoUser> updateAllRoomAutoUser();

	void updateYoukeKefuList(UserRelation relation);
	
	List<String> getYoukeKefuList();
	
	void removeYoukeKefuList(String value);
	
	int getUserOnlineTime(Integer userId);
	
	void updateUserOnlineTime(Integer userId,int times);
	
	Set<String> getAllOnlineUser();
	
	void removeUserOnlineTime(Integer userId);
	
	
	int getVipUserOnlineTime(Integer userId);
	
	void updateVipUserOnlineTime(Integer userId,int times);
	
	Set<String> getAllOnlineVipUser();
	
	void removeVipUserOnlineTime(Integer userId);
	
	
	int getZlUserOnlineTime(Integer userId);
	
	void updateZlUserOnlineTime(Integer userId,int times);
	
	Set<String> getAllOnlineZlUser();
	
	void removeZlUserOnlineTime(Integer userId);

	
	List<RoomPlan> getAllRoomPlan();
	
	List<RoomPlan> updateAllRoomPlan();
	
	
	LiveInform getLiveInForm();
	
	
	LiveInform updateLiveInForm();
}

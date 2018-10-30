package com.qiyou.dhlive.api.base.outward.service;

import java.util.List;

import com.qiyou.dhlive.api.base.outward.vo.UserInfoDTO;
import com.qiyou.dhlive.core.room.outward.model.RoomAutoMsg;
import com.qiyou.dhlive.core.room.outward.model.RoomChatMessage;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;

public interface IBaseCacheService {
	
	UserInfoDTO getUserInfo(Integer userId);
	
	UserInfoDTO updateUserInfo(Integer userId);
	
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

}

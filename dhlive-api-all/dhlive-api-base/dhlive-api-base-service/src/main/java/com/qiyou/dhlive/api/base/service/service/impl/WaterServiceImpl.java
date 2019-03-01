package com.qiyou.dhlive.api.base.service.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.api.base.outward.service.IWaterService;
import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.model.UserRelation;
import com.qiyou.dhlive.core.user.outward.service.IUserRelationService;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.DateWeek;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;

@Service
public class WaterServiceImpl implements IWaterService {

	private static int auto_kefu_index = 0;

	@Autowired
	private IBaseSysParamService baseSysParamService;

	@Autowired
	private IBaseCacheService baseCacheService;

	@Autowired
	private IUserRelationService userRelationService;

	@Autowired
	@Qualifier("commonRedisManager")
	private RedisManager redisManager;

	private List<UserManageInfo> getKefuList() {
		List<UserManageInfo> kefuList = this.baseCacheService.getManageUserList(4);
		List<UserManageInfo> rs = Lists.newArrayList();
		if (EmptyUtil.isEmpty(kefuList)) {
			return rs;
		}
		Set<String> onlineZl=this.redisManager.getMapKeyFromMapByStoreKey(RedisKeyConstant.ZL_ONLINE_IDS);
		List<Integer> onlineUserId = Lists.newArrayList();
		for(String key:onlineZl) {
			onlineUserId.add(Integer.parseInt(key));
		}
		for (UserManageInfo user : kefuList) {
			if (user.getGroupId().intValue() == 3) {
				if(EmptyUtil.isEmpty(onlineUserId)) {
					rs.add(user);
				}
				for(Integer key:onlineUserId) {
					if(user.getUserId().intValue()==key) {
						rs.add(user);
					}
				}
				
			}
		}
		return rs;
	}

	private UserManageInfo getKefuFromList(String kefuId, List<UserManageInfo> kefuList) {
		UserManageInfo rs = null;
		if (EmptyUtil.isEmpty(kefuId) || EmptyUtil.isEmpty(kefuList)) {
			return rs;
		}
		for (UserManageInfo user : kefuList) {
			if (kefuId.equals(user.getUserId() + "")) {
				return user;
			}
		}
		return rs;

	}
	
	@Override
	public UserManageInfo initYkKefu(Integer userId,boolean isNew) {
		String dutyWay = this.baseSysParamService.getValueByKey("duty_way");
		if(EmptyUtil.isEmpty(dutyWay)) {
			dutyWay = "auto";
		}
		
		if("day".equals(dutyWay)) {//根据值班表分配
			Date curtime = new Date();
			String day = DateUtil.getDate(curtime);
			DateWeek dateWeek = DateUtil.getDateWeek(curtime);
			int week = dateWeek.getNumber();
			
			List<UserManageInfo> kefuList = getKefuList();
			String myKefuId = this.baseCacheService.getYkKefuIdByDay(userId, day);
			
			if(EmptyUtil.isNotEmpty(myKefuId)) {
				UserManageInfo myKefu = getKefuFromList(myKefuId,kefuList);
				if(EmptyUtil.isNotEmpty(myKefu)) {
					return myKefu;
				}else {
					myKefuId = "";
				}
			}
			
			List<UserManageInfo> dutyList = this.baseCacheService.getDutyUserByWeek(4, week);
			if(dutyList ==null || dutyList.size()==0) {
				dutyList = kefuList;
			}
			if(auto_kefu_index >= dutyList.size()) {
				auto_kefu_index = 0;
			}
			UserManageInfo myKefu = dutyList.get(auto_kefu_index);
			auto_kefu_index++;
			
	/*		UserRelation oldParam = new UserRelation();
			oldParam.setUserId(userId);
			oldParam.setStatus(0);
			oldParam.setGroupId(1);
			long count = this.userRelationService.countByCondition(new SearchCondition<UserRelation>(oldParam));
			if(count>0) {
				UserRelation upRation = new UserRelation();
				upRation.setStatus(1);
				this.userRelationService.modifyEntityByCondition(upRation, new SearchCondition<UserRelation>(oldParam));
			}*/
			
			UserRelation relation = new UserRelation();
			relation.setUserId(userId);
            relation.setGroupId(1);
            relation.setRelationUserId(myKefu.getUserId());
            relation.setCreateTime(new Date());
            relation.setStatus(0);
//            this.userRelationService.save(relation);
            this.baseCacheService.updateYoukeKefuList(relation);
            this.baseCacheService.updateYkKefuIdByDay(userId, day,myKefu.getUserId());
			return myKefu;
		}else {//系统自动分配
			String myKefuId = this.baseCacheService.getYkKefuId(userId);
			List<UserManageInfo> kefuList = getKefuList();
			if(EmptyUtil.isNotEmpty(myKefuId)) {
				UserManageInfo myKefu = getKefuFromList(myKefuId,kefuList);
				if(EmptyUtil.isNotEmpty(myKefu)) {
					return myKefu;
				}else {
					myKefuId = "";
				}
			}else {
				if(!isNew) {
					UserRelation ur=new UserRelation();
					ur.setUserId(userId);
					ur.setStatus(0);
					ur=this.userRelationService.findOneByCondition(new SearchCondition<UserRelation>(ur));
					if(EmptyUtil.isNotEmpty(ur)) {
						myKefuId = ur.getRelationUserId().toString();
						UserManageInfo myKefu = getKefuFromList(myKefuId,kefuList);
						if(EmptyUtil.isNotEmpty(myKefu)) {
							this.baseCacheService.updateYkKefuId(userId,myKefu.getUserId());
							return myKefu;
						}else {
							myKefuId = "";
						}
					}
				}
			}
			if(kefuList.size()==0) {
				return null;
			}
			if(auto_kefu_index >= kefuList.size()) {
				auto_kefu_index = 0;
			}
			UserManageInfo myKefu = kefuList.get(auto_kefu_index);
			auto_kefu_index++;
			
			
			UserRelation relation = new UserRelation();
			relation.setUserId(userId);
            relation.setGroupId(1);
            relation.setRelationUserId(myKefu.getUserId());
            relation.setCreateTime(new Date());
            relation.setStatus(0);
//            this.userRelationService.save(relation);
            this.baseCacheService.updateYoukeKefuList(relation);
            this.baseCacheService.updateYkKefuId(userId,myKefu.getUserId());
            
			return myKefu;
		}
	}



	@Override
	public UserManageInfo initVipKefu(Integer userId) {
		String myKefuId = this.baseCacheService.getVipKefuId(userId);
		List<UserManageInfo> kefuList = this.baseCacheService.getManageUserList(4);
		if (kefuList.size() == 0) {
			return null;
		}
		if (EmptyUtil.isNotEmpty(myKefuId)) {
			UserManageInfo myKefu = getKefuFromList(myKefuId, kefuList);
			if (EmptyUtil.isNotEmpty(myKefu)) {
				return myKefu;
			} else {
				myKefuId = "";
			}
		}
		UserManageInfo myKefu = null;
		int x = (int) (Math.random() * kefuList.size());// 随机数, 用于选择一个助理进行关联
		try {
			myKefu = kefuList.get(x);
		} catch (Exception e) {

		}
		return myKefu;
	}

}

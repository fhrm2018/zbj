package com.qiyou.dhlive.core.room.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiyou.dhlive.core.room.outward.model.RoomAutoUser;
import com.qiyou.dhlive.core.room.outward.service.IRoomAutoUserService;
import com.qiyou.dhlive.core.room.service.dao.RoomAutoUserMapper;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;

@Service
public class RoomAutoUserServiceImpl  extends BaseMyBatisService<RoomAutoUser> implements IRoomAutoUserService {

	@Autowired
	private RoomAutoUserMapper mapper;
	
	public RoomAutoUserServiceImpl() {
		super.setEntityClazz(RoomAutoUser.class);
	}
	
}

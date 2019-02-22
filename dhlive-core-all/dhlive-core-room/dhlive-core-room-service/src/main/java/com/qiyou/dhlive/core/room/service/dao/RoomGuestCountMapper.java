package com.qiyou.dhlive.core.room.service.dao;

import java.util.List;

import com.qiyou.dhlive.core.room.outward.model.RoomGuestCount;
import com.qiyou.dhlive.core.room.outward.model.RoomOnlineCount;
import com.qiyou.dhlive.core.room.outward.vo.RoomGuestCountVO;

import tk.mybatis.mapper.common.Mapper;

public interface RoomGuestCountMapper extends Mapper<RoomGuestCount>{
	
	
	public List<RoomGuestCount> findBySearch(RoomGuestCountVO vo);
	
	public Long countBySearch(RoomGuestCountVO vo);

}

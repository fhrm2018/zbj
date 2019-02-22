package com.qiyou.dhlive.core.room.service.dao;

import java.util.List;

import com.qiyou.dhlive.core.room.outward.model.RoomOnlineCount;
import com.qiyou.dhlive.core.room.outward.vo.RoomOnlineCountVO;

import tk.mybatis.mapper.common.Mapper;

public interface RoomOnlineCountMapper extends Mapper<RoomOnlineCount>{
	
	
	public List<RoomOnlineCount> findBySearch(RoomOnlineCountVO vo);
	
	public Long countBySearch(RoomOnlineCountVO vo);

}

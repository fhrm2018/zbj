package com.qiyou.dhlive.core.room.service.dao;

import java.util.List;

import com.qiyou.dhlive.core.room.outward.model.RoomMsgCount;
import com.qiyou.dhlive.core.room.outward.vo.RoomMsgCountVO;

import tk.mybatis.mapper.common.Mapper;

public interface RoomMsgCountMapper extends Mapper<RoomMsgCount>{
	
	
	public List<RoomMsgCount> findBySearch(RoomMsgCountVO vo);
	
	public Long countBySearch(RoomMsgCountVO vo);

}

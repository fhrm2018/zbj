package com.qiyou.dhlive.core.room.outward.service;

import java.util.List;

import com.qiyou.dhlive.core.room.outward.model.RoomOnlineCount;
import com.qiyou.dhlive.core.room.outward.vo.RoomOnlineCountVO;
import com.yaozhong.framework.base.database.base.service.IBaseService;

public interface IRoomOnlineCountService extends IBaseService<RoomOnlineCount> {

	
	public List<RoomOnlineCount> findBySearch(RoomOnlineCountVO vo);
	
	public Long countBySearch(RoomOnlineCountVO vo);
}

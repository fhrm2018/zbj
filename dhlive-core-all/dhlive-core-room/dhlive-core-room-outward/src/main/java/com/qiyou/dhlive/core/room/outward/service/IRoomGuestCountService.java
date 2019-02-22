package com.qiyou.dhlive.core.room.outward.service;

import java.util.List;

import com.qiyou.dhlive.core.room.outward.model.RoomGuestCount;
import com.qiyou.dhlive.core.room.outward.vo.RoomGuestCountVO;
import com.yaozhong.framework.base.database.base.service.IBaseService;

public interface IRoomGuestCountService extends IBaseService<RoomGuestCount> {

	
	public List<RoomGuestCount> findBySearch(RoomGuestCountVO vo);
	
	public Long countBySearch(RoomGuestCountVO vo);
}

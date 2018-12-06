package com.qiyou.dhlive.core.room.outward.service;

import java.util.List;

import com.qiyou.dhlive.core.room.outward.model.RoomMsgCount;
import com.qiyou.dhlive.core.room.outward.vo.RoomMsgCountVO;
import com.yaozhong.framework.base.database.base.service.IBaseService;

public interface IRoomMsgCountService extends IBaseService<RoomMsgCount> {

	
	public List<RoomMsgCount> findBySearch(RoomMsgCountVO vo);
	
	public Long countBySearch(RoomMsgCountVO vo);
}

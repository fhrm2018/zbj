package com.qiyou.dhlive.core.room.service.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiyou.dhlive.core.room.outward.model.RoomMsgCount;
import com.qiyou.dhlive.core.room.outward.service.IRoomMsgCountService;
import com.qiyou.dhlive.core.room.outward.vo.RoomMsgCountVO;
import com.qiyou.dhlive.core.room.service.dao.RoomMsgCountMapper;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;

@Service
public class RoomMsgCountServiceImpl extends BaseMyBatisService<RoomMsgCount> implements IRoomMsgCountService{

	@Autowired
	private RoomMsgCountMapper mapper;
	
	
	public RoomMsgCountServiceImpl() {
		super.setEntityClazz(RoomMsgCount.class);
	}


	@Override
	public List<RoomMsgCount> findBySearch(RoomMsgCountVO vo) {
		// TODO Auto-generated method stub
		if(EmptyUtil.isEmpty(vo.getBeginDate())&&EmptyUtil.isEmpty(vo.getEndDate())) {
			Date date = DateUtil.dateTimeToDate(new Date());
			vo.setBeginDate(DateUtil.addDay(date, -7));
			vo.setEndDate(date);
		}
		return mapper.findBySearch(vo);
	}


	@Override
	public Long countBySearch(RoomMsgCountVO vo) {
		// TODO Auto-generated method stub
		return mapper.countBySearch(vo);
	}
}

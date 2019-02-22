package com.qiyou.dhlive.core.room.service.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiyou.dhlive.core.room.outward.model.RoomOnlineCount;
import com.qiyou.dhlive.core.room.outward.service.IRoomOnlineCountService;
import com.qiyou.dhlive.core.room.outward.vo.RoomOnlineCountVO;
import com.qiyou.dhlive.core.room.service.dao.RoomOnlineCountMapper;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;

@Service
public class RoomOnlineCountServiceImpl extends BaseMyBatisService<RoomOnlineCount> implements IRoomOnlineCountService{

	@Autowired
	private RoomOnlineCountMapper mapper;
	
	
	public RoomOnlineCountServiceImpl() {
		super.setEntityClazz(RoomOnlineCount.class);
	}


	@Override
	public List<RoomOnlineCount> findBySearch(RoomOnlineCountVO vo) {
		// TODO Auto-generated method stub
		if(EmptyUtil.isEmpty(vo.getBeginDate())&&EmptyUtil.isEmpty(vo.getEndDate())) {
			Date date = DateUtil.dateTimeToDate(new Date());
			vo.setBeginDate(DateUtil.addDay(date, -7));
			vo.setEndDate(date);
		}
		return mapper.findBySearch(vo);
	}


	@Override
	public Long countBySearch(RoomOnlineCountVO vo) {
		// TODO Auto-generated method stub
		return mapper.countBySearch(vo);
	}
}

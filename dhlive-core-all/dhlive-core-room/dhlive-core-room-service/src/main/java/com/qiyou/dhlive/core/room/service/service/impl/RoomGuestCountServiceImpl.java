package com.qiyou.dhlive.core.room.service.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiyou.dhlive.core.room.outward.model.RoomGuestCount;
import com.qiyou.dhlive.core.room.outward.service.IRoomGuestCountService;
import com.qiyou.dhlive.core.room.outward.vo.RoomGuestCountVO;
import com.qiyou.dhlive.core.room.service.dao.RoomGuestCountMapper;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;

@Service
public class RoomGuestCountServiceImpl extends BaseMyBatisService<RoomGuestCount> implements IRoomGuestCountService{

	@Autowired
	private RoomGuestCountMapper mapper;
	
	
	public RoomGuestCountServiceImpl() {
		super.setEntityClazz(RoomGuestCount.class);
	}


	@Override
	public List<RoomGuestCount> findBySearch(RoomGuestCountVO vo) {
		// TODO Auto-generated method stub
		if(EmptyUtil.isEmpty(vo.getBeginDate())&&EmptyUtil.isEmpty(vo.getEndDate())) {
			Date date = DateUtil.dateTimeToDate(new Date());
			vo.setBeginDate(DateUtil.addDay(date, -7));
			vo.setEndDate(date);
		}
		return mapper.findBySearch(vo);
	}


	@Override
	public Long countBySearch(RoomGuestCountVO vo) {
		// TODO Auto-generated method stub
		return mapper.countBySearch(vo);
	}
}

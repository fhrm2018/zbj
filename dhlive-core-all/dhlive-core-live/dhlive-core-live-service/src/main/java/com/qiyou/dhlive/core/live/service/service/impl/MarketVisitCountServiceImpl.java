package com.qiyou.dhlive.core.live.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiyou.dhlive.core.live.outward.model.MarketVisitCount;
import com.qiyou.dhlive.core.live.outward.service.IMarketVisitCountService;
import com.qiyou.dhlive.core.live.service.dao.MarketVisitCountMapper;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;

@Service
public class MarketVisitCountServiceImpl extends BaseMyBatisService<MarketVisitCount> implements IMarketVisitCountService {

	@Autowired
	private MarketVisitCountMapper mapper;
	
	
	public MarketVisitCountServiceImpl() {
		super.setEntityClazz(MarketVisitCount.class);
	}
	
	
}

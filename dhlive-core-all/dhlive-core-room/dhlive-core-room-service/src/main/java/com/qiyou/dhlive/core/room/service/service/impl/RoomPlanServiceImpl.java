package com.qiyou.dhlive.core.room.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qiyou.dhlive.core.room.outward.model.RoomPlan;
import com.qiyou.dhlive.core.room.outward.service.IRoomPlanService;
import com.qiyou.dhlive.core.room.service.dao.RoomPlanMapper;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;

@Service
public class RoomPlanServiceImpl extends BaseMyBatisService<RoomPlan> implements IRoomPlanService {
	@Autowired
    private RoomPlanMapper mapper;

    public RoomPlanServiceImpl() {
        super.setEntityClazz(RoomPlan.class);
    }
}

package com.qiyou.dhlive.core.room.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiyou.dhlive.core.room.outward.model.RoomDuty;
import com.qiyou.dhlive.core.room.outward.service.IRoomDutyService;
import com.qiyou.dhlive.core.room.service.dao.RoomDutyMapper;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-20
 */
@Service
public class RoomDutyServiceImpl extends BaseMyBatisService<RoomDuty> implements IRoomDutyService {

    @Autowired
    private RoomDutyMapper mapper;

    public RoomDutyServiceImpl() {
        super.setEntityClazz(RoomDuty.class);
    }

}
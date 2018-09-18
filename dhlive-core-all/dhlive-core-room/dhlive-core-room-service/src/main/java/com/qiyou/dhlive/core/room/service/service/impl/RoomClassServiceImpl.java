package com.qiyou.dhlive.core.room.service.service.impl;

import com.qiyou.dhlive.core.room.outward.model.RoomClass;
import com.qiyou.dhlive.core.room.outward.service.IRoomClassService;
import com.qiyou.dhlive.core.room.service.dao.RoomClassMapper;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-20
 */
@Service
public class RoomClassServiceImpl extends BaseMyBatisService<RoomClass> implements IRoomClassService {

    @Autowired
    private RoomClassMapper mapper;

    public RoomClassServiceImpl() {
        super.setEntityClazz(RoomClass.class);
    }

}
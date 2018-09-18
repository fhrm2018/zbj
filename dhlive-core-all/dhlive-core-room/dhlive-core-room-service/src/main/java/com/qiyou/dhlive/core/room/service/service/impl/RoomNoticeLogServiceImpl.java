package com.qiyou.dhlive.core.room.service.service.impl;

import com.qiyou.dhlive.core.room.outward.model.RoomNoticeLog;
import com.qiyou.dhlive.core.room.outward.service.IRoomNoticeLogService;
import com.qiyou.dhlive.core.room.service.dao.RoomNoticeLogMapper;
import org.springframework.stereotype.Service;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

/**
* @author liuyuanhang
* @date 2018-01-20
*
* @version 1.0.0
*/
@Service
public class RoomNoticeLogServiceImpl extends BaseMyBatisService<RoomNoticeLog> implements IRoomNoticeLogService {

    @Autowired
    private RoomNoticeLogMapper mapper;

    public RoomNoticeLogServiceImpl() {
        super.setEntityClazz(RoomNoticeLog.class);
    }

}
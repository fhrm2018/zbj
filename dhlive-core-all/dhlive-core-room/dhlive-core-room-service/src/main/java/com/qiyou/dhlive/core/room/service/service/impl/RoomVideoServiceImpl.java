package com.qiyou.dhlive.core.room.service.service.impl;

import com.qiyou.dhlive.core.room.outward.model.RoomFile;
import com.qiyou.dhlive.core.room.outward.model.RoomVideo;
import com.qiyou.dhlive.core.room.outward.service.IRoomFileService;
import com.qiyou.dhlive.core.room.outward.service.IRoomVideoService;
import com.qiyou.dhlive.core.room.service.dao.RoomFileMapper;
import com.qiyou.dhlive.core.room.service.dao.RoomVideoMapper;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-20
 */
@Service
public class RoomVideoServiceImpl extends BaseMyBatisService<RoomVideo> implements IRoomVideoService {

    @Autowired
    private RoomVideoMapper mapper;

    public RoomVideoServiceImpl() {
        super.setEntityClazz(RoomVideo.class);
    }

}
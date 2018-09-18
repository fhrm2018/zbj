package com.qiyou.dhlive.core.room.service.service.impl;

import com.qiyou.dhlive.core.room.outward.model.RoomFile;
import com.qiyou.dhlive.core.room.outward.service.IRoomFileService;
import com.qiyou.dhlive.core.room.service.dao.RoomFileMapper;
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
public class RoomFileServiceImpl extends BaseMyBatisService<RoomFile> implements IRoomFileService {

    @Autowired
    private RoomFileMapper mapper;

    public RoomFileServiceImpl() {
        super.setEntityClazz(RoomFile.class);
    }

}
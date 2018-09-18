package com.qiyou.dhlive.core.live.service.service.impl;

import com.qiyou.dhlive.core.live.outward.model.LiveRoom;
import com.qiyou.dhlive.core.live.outward.service.ILiveRoomService;
import com.qiyou.dhlive.core.live.service.dao.LiveRoomMapper;
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
public class LiveRoomServiceImpl extends BaseMyBatisService<LiveRoom> implements ILiveRoomService {

    @Autowired
    private LiveRoomMapper mapper;

    public LiveRoomServiceImpl() {
        super.setEntityClazz(LiveRoom.class);
    }

}
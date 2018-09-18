package com.qiyou.dhlive.core.room.service.service.impl;

import com.qiyou.dhlive.core.room.outward.model.RoomArticle;
import com.qiyou.dhlive.core.room.outward.model.RoomFile;
import com.qiyou.dhlive.core.room.outward.service.IRoomArticleService;
import com.qiyou.dhlive.core.room.outward.service.IRoomFileService;
import com.qiyou.dhlive.core.room.service.dao.RoomArticleMapper;
import com.qiyou.dhlive.core.room.service.dao.RoomFileMapper;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-20
 */
@Service
public class RoomArticleServiceImpl extends BaseMyBatisService<RoomArticle> implements IRoomArticleService {

    @Autowired
    private RoomArticleMapper mapper;

    public RoomArticleServiceImpl() {
        super.setEntityClazz(RoomArticle.class);
    }

}
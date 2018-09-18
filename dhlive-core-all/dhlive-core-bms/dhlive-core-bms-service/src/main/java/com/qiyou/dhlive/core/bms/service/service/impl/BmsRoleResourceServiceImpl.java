package com.qiyou.dhlive.core.bms.service.service.impl;

import com.qiyou.dhlive.core.bms.outward.model.BmsRoleResource;
import com.qiyou.dhlive.core.bms.outward.service.IBmsRoleResourceService;
import com.qiyou.dhlive.core.bms.service.dao.BmsRoleResourceMapper;
import org.springframework.stereotype.Service;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

/**
* @author liuyuanhang
* @date 2018-01-29
*
* @version 1.0.0
*/
@Service
public class BmsRoleResourceServiceImpl extends BaseMyBatisService<BmsRoleResource> implements IBmsRoleResourceService {

    @Autowired
    private BmsRoleResourceMapper mapper;

    public BmsRoleResourceServiceImpl() {
        super.setEntityClazz(BmsRoleResource.class);
    }

}
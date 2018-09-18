package com.qiyou.dhlive.core.bms.service.service.impl;

import com.qiyou.dhlive.core.bms.outward.model.BmsRole;
import com.qiyou.dhlive.core.bms.outward.service.IBmsRoleService;
import com.qiyou.dhlive.core.bms.service.dao.BmsRoleMapper;
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
public class BmsRoleServiceImpl extends BaseMyBatisService<BmsRole> implements IBmsRoleService {

    @Autowired
    private BmsRoleMapper mapper;

    public BmsRoleServiceImpl() {
        super.setEntityClazz(BmsRole.class);
    }

}
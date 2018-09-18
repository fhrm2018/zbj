package com.qiyou.dhlive.core.base.service.service.impl;

import com.qiyou.dhlive.core.base.outward.model.BaseIp;
import com.qiyou.dhlive.core.base.outward.service.IBaseIpService;
import com.qiyou.dhlive.core.base.service.dao.BaseIpMapper;
import org.springframework.stereotype.Service;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

/**
* @author liuyuanhang
* @date 2018-02-28
*
* @version 1.0.0
*/
@Service
public class BaseIpServiceImpl extends BaseMyBatisService<BaseIp> implements IBaseIpService {

    @Autowired
    private BaseIpMapper mapper;

    public BaseIpServiceImpl() {
        super.setEntityClazz(BaseIp.class);
    }

}
package com.qiyou.dhlive.core.bms.service.service.impl;

import com.qiyou.dhlive.core.bms.outward.model.BmsResource;
import com.qiyou.dhlive.core.bms.outward.service.IBmsResourceService;
import com.qiyou.dhlive.core.bms.service.dao.BmsResourceMapper;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.returns.BaseResult;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import org.springframework.stereotype.Service;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Date;
import java.util.List;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-29
 */
@Service
public class BmsResourceServiceImpl extends BaseMyBatisService<BmsResource> implements IBmsResourceService {

    @Autowired
    private BmsResourceMapper mapper;

    public BmsResourceServiceImpl() {
        super.setEntityClazz(BmsResource.class);
    }

    public BmsResource getResourceByName(String name, int type) {
        BmsResource param = new BmsResource();
        param.setName(name);
        param.setStatus(1);
        param.setType(type);
        List<BmsResource> list = this.findByCondition(new SearchCondition<BmsResource>(param));
        if (list.size() == 0)
            return null;
        else
            return list.get(0);
    }

    public BmsResource saveResource(BmsResource recource) {
        Date curTime = new Date();
        if (EmptyUtil.isEmpty(recource.getId())) {
            recource.setStatus(1);
            BaseResult br = this.save(recource);
            recource.setId(Integer.parseInt(br.getData().toString()));
        } else {
            recource.setModifyTime(curTime);
            this.modifyEntity(recource);
        }
        return recource;
    }


    @Override
    public List<BmsResource> getAllRecource() {
        BmsResource param = new BmsResource();
        param.setStatus(1);
        SearchCondition<BmsResource> con = new SearchCondition<BmsResource>(param);
        List<BmsResource> list = this.findByCondition(con);
        return list;
    }
}
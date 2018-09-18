package com.qiyou.dhlive.core.bms.outward.service;

import com.qiyou.dhlive.core.bms.outward.model.BmsResource;
import com.yaozhong.framework.base.database.base.service.IBaseService;

import java.util.List;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-29
 */
public interface IBmsResourceService extends IBaseService<BmsResource> {

    BmsResource getResourceByName(String name, int type);

    BmsResource saveResource(BmsResource recource);

    List<BmsResource> getAllRecource();

}
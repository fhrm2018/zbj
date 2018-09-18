package com.qiyou.dhlive.core.base.outward.service;

import com.qiyou.dhlive.core.base.outward.model.BaseSysParam;
import com.yaozhong.framework.base.database.base.service.IBaseService;

/**
* @author liuyuanhang
* @date 2018-01-20
*
* @version 1.0.0
*/
public interface IBaseSysParamService extends IBaseService<BaseSysParam> {

    public String getValueByKey(String key);

    public String updateValueByKey(String key);

}
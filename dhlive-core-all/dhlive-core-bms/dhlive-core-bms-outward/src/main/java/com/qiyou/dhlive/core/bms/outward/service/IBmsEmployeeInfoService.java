package com.qiyou.dhlive.core.bms.outward.service;

import com.qiyou.dhlive.core.bms.outward.model.BmsEmployeeInfo;
import com.yaozhong.framework.base.database.base.service.IBaseService;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-29
 */
public interface IBmsEmployeeInfoService extends IBaseService<BmsEmployeeInfo> {

    DataResponse loginByPhone(String phone, String pwd);

}
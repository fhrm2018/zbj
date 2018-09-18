package com.qiyou.dhlive.api.base.outward.service;

import com.qiyou.dhlive.core.base.outward.model.BaseIp;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;

/**
 * ${DESCRIPTION}
 *
 * @author fish
 * @create 2018-01-20 20:58
 **/
public interface ICheckIpVisitService {

    boolean checkCanVisit(String ip, String url);

    DataResponse saveIpBlackOrWhiteList(BaseIp params);

    DataResponse getIpList(BaseIp params);

    DataResponse delIp(BaseIp params);
}

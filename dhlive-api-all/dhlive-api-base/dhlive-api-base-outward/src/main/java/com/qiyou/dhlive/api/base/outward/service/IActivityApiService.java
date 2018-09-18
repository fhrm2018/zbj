package com.qiyou.dhlive.api.base.outward.service;

import com.qiyou.dhlive.core.activity.outward.model.ActivityLuckyDrawConfig;
import com.qiyou.dhlive.core.activity.outward.model.ActivityLuckyDrawWinners;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;

import java.util.List;

/**
 * Created by fish on 2018/4/20.
 */
public interface IActivityApiService {

    DataResponse redPackInfo(ActivityLuckyDrawConfig params);

    DataResponse robRedPack(ActivityLuckyDrawWinners params);

    /**
     * 幸运大转盘抽奖
     * @param params
     * @return
     */
    DataResponse rotaryTable(ActivityLuckyDrawWinners params);

    DataResponse isReceive(ActivityLuckyDrawWinners params);

    DataResponse prizeInfo(ActivityLuckyDrawWinners params);
}

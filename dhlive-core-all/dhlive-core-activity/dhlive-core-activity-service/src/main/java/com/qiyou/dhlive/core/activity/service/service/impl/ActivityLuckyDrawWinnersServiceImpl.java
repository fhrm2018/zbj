package com.qiyou.dhlive.core.activity.service.service.impl;

import com.qiyou.dhlive.core.activity.outward.model.ActivityLuckyDrawWinners;
import com.qiyou.dhlive.core.activity.outward.service.IActivityLuckyDrawWinnersService;
import com.qiyou.dhlive.core.activity.service.dao.ActivityLuckyDrawWinnersMapper;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fish on 2018/4/19.
 */
@Service
public class ActivityLuckyDrawWinnersServiceImpl extends BaseMyBatisService<ActivityLuckyDrawWinners> implements IActivityLuckyDrawWinnersService {

    @Autowired
    private ActivityLuckyDrawWinnersMapper mapper;

    public ActivityLuckyDrawWinnersServiceImpl() {
        super.setEntityClazz(ActivityLuckyDrawWinners.class);
    }
}

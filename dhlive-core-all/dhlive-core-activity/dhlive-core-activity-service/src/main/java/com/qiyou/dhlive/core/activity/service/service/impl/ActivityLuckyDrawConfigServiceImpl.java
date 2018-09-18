package com.qiyou.dhlive.core.activity.service.service.impl;

import com.qiyou.dhlive.core.activity.outward.model.ActivityLuckyDrawConfig;
import com.qiyou.dhlive.core.activity.outward.service.IActivityLuckyDrawConfigService;
import com.qiyou.dhlive.core.activity.service.dao.ActivityLuckyDrawConfigMapper;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fish on 2018/4/19.
 */
@Service
public class ActivityLuckyDrawConfigServiceImpl extends BaseMyBatisService<ActivityLuckyDrawConfig> implements IActivityLuckyDrawConfigService {

    @Autowired
    private ActivityLuckyDrawConfigMapper mapper;

    public ActivityLuckyDrawConfigServiceImpl() {
        super.setEntityClazz(ActivityLuckyDrawConfig.class);
    }

}

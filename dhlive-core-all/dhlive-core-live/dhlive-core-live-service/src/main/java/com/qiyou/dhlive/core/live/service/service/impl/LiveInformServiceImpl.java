package com.qiyou.dhlive.core.live.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qiyou.dhlive.core.live.outward.model.LiveInform;
import com.qiyou.dhlive.core.live.outward.service.ILiveInformService;
import com.qiyou.dhlive.core.live.service.dao.ILiveInformMapper;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;

@Service
public class LiveInformServiceImpl extends BaseMyBatisService<LiveInform> implements ILiveInformService{
	@Autowired
    private ILiveInformMapper mapper;

    public LiveInformServiceImpl() {
        super.setEntityClazz(LiveInform.class);
    }
}

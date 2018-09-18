package com.qiyou.dhlive.core.user.service.service.impl;

import com.qiyou.dhlive.core.user.outward.model.UserSmallInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserSmallInfoService;
import com.qiyou.dhlive.core.user.service.dao.UserSmallInfoMapper;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/31
 */
@Service
public class UserSmallInfoServiceImpl extends BaseMyBatisService<UserSmallInfo> implements IUserSmallInfoService {

    @Autowired
    private UserSmallInfoMapper mapper;

    public UserSmallInfoServiceImpl() {
        super.setEntityClazz(UserSmallInfo.class);
    }
}

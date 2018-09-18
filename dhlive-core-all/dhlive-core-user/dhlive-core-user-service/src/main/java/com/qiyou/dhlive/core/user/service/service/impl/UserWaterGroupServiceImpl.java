package com.qiyou.dhlive.core.user.service.service.impl;

import com.qiyou.dhlive.core.user.outward.model.UserGroup;
import com.qiyou.dhlive.core.user.outward.model.UserWaterGroup;
import com.qiyou.dhlive.core.user.outward.service.IUserGroupService;
import com.qiyou.dhlive.core.user.outward.service.IUserWaterGroupService;
import com.qiyou.dhlive.core.user.service.dao.UserGroupMapper;
import com.qiyou.dhlive.core.user.service.dao.UserWaterGroupMapper;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fish
 * @version 1.0.0
 * @date 2018-01-20
 */
@Service
public class UserWaterGroupServiceImpl extends BaseMyBatisService<UserWaterGroup> implements IUserWaterGroupService {

    @Autowired
    private UserWaterGroupMapper mapper;

    public UserWaterGroupServiceImpl() {
        super.setEntityClazz(UserWaterGroup.class);
    }

}
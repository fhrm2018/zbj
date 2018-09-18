package com.qiyou.dhlive.core.user.service.service.impl;

import com.qiyou.dhlive.core.user.outward.model.UserGroup;
import com.qiyou.dhlive.core.user.outward.service.IUserGroupService;
import com.qiyou.dhlive.core.user.service.dao.UserGroupMapper;
import org.springframework.stereotype.Service;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

/**
* @author liuyuanhang
* @date 2018-01-20
*
* @version 1.0.0
*/
@Service
public class UserGroupServiceImpl extends BaseMyBatisService<UserGroup> implements IUserGroupService {

    @Autowired
    private UserGroupMapper mapper;

    public UserGroupServiceImpl() {
        super.setEntityClazz(UserGroup.class);
    }

}
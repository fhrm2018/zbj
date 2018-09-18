package com.qiyou.dhlive.core.user.service.service.impl;

import com.qiyou.dhlive.core.user.outward.model.UserLoginLog;
import com.qiyou.dhlive.core.user.outward.service.IUserLoginLogService;
import com.qiyou.dhlive.core.user.service.dao.UserLoginLogMapper;
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
public class UserLoginLogServiceImpl extends BaseMyBatisService<UserLoginLog> implements IUserLoginLogService {

    @Autowired
    private UserLoginLogMapper mapper;

    public UserLoginLogServiceImpl() {
        super.setEntityClazz(UserLoginLog.class);
    }

}
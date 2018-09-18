package com.qiyou.dhlive.core.user.service.service.impl;

import com.qiyou.dhlive.core.user.outward.model.UserIncomeLog;
import com.qiyou.dhlive.core.user.outward.service.IUserIncomeLogService;
import com.qiyou.dhlive.core.user.service.dao.UserIncomeLogMapper;
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
public class UserIncomeLogServiceImpl extends BaseMyBatisService<UserIncomeLog> implements IUserIncomeLogService {

    @Autowired
    private UserIncomeLogMapper mapper;

    public UserIncomeLogServiceImpl() {
        super.setEntityClazz(UserIncomeLog.class);
    }

}
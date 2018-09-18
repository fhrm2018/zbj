package com.qiyou.dhlive.core.user.service.service.impl;

import com.qiyou.dhlive.core.user.outward.model.UserDownloadLog;
import com.qiyou.dhlive.core.user.outward.service.IUserDownloadLogService;
import com.qiyou.dhlive.core.user.service.dao.UserDownloadLogMapper;
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
public class UserDownloadLogServiceImpl extends BaseMyBatisService<UserDownloadLog> implements IUserDownloadLogService {

    @Autowired
    private UserDownloadLogMapper mapper;

    public UserDownloadLogServiceImpl() {
        super.setEntityClazz(UserDownloadLog.class);
    }

}
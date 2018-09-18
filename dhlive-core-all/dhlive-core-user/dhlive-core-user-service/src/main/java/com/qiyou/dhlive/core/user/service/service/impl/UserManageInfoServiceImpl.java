package com.qiyou.dhlive.core.user.service.service.impl;

import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserManageInfoService;
import com.qiyou.dhlive.core.user.service.dao.UserManageInfoMapper;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import org.springframework.stereotype.Service;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-29
 */
@Service
public class UserManageInfoServiceImpl extends BaseMyBatisService<UserManageInfo> implements IUserManageInfoService {

    @Autowired
    private UserManageInfoMapper mapper;

    public UserManageInfoServiceImpl() {
        super.setEntityClazz(UserManageInfo.class);
    }

    @Override
    public UserManageInfo getManageUserByLoginName(String loginName) {
        UserManageInfo param = new UserManageInfo();
        param.setUserTel(loginName);
        param.setStatus(0);
        SearchCondition<UserManageInfo> condition = new SearchCondition<UserManageInfo>(param);
        List<UserManageInfo> list = this.findByCondition(condition);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
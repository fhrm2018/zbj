package com.qiyou.dhlive.core.user.service.service.impl;

import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserVipInfoService;
import com.qiyou.dhlive.core.user.service.dao.UserVipInfoMapper;
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
public class UserVipInfoServiceImpl extends BaseMyBatisService<UserVipInfo> implements IUserVipInfoService {

    @Autowired
    private UserVipInfoMapper mapper;

    public UserVipInfoServiceImpl() {
        super.setEntityClazz(UserVipInfo.class);
    }

    @Override
    public UserVipInfo getVipUserByLoginName(String loginName) {
        UserVipInfo param = new UserVipInfo();
        param.setUserTel(loginName);
        SearchCondition<UserVipInfo> condition = new SearchCondition<UserVipInfo>(param);
        List<UserVipInfo> list = this.findByCondition(condition);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
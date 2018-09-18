package com.qiyou.dhlive.core.user.outward.service;

import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;
import com.yaozhong.framework.base.database.base.service.IBaseService;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-29
 */
public interface IUserVipInfoService extends IBaseService<UserVipInfo> {

    UserVipInfo getVipUserByLoginName(String loginName);

}
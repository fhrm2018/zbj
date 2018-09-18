package com.qiyou.dhlive.core.user.outward.service;

import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.yaozhong.framework.base.database.base.service.IBaseService;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-29
 */
public interface IUserManageInfoService extends IBaseService<UserManageInfo> {

    UserManageInfo getManageUserByLoginName(String loginName);

}
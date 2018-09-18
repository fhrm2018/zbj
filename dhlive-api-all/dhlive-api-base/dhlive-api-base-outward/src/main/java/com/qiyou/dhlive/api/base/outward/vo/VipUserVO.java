package com.qiyou.dhlive.api.base.outward.vo;

import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;

/**
 * Created by fish on 2018/7/6.
 */
public class VipUserVO extends UserVipInfo {

    private String createUserName;

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}

package com.qiyou.dhlive.api.base.outward.service;

import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;

public interface IWaterService {
	
	UserManageInfo initYkKefu(Integer userId,boolean isNew); 
	
	UserManageInfo initVipKefu(Integer userId); 

}

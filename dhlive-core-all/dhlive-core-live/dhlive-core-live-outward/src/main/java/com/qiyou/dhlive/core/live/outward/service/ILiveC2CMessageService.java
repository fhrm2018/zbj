package com.qiyou.dhlive.core.live.outward.service;

import com.qiyou.dhlive.core.live.outward.model.LiveC2CMessage;
import com.yaozhong.framework.base.database.base.service.IBaseService;

import java.util.Date;
import java.util.List;

/**
 * Created by ThinkPad on 2018/3/12.
 */
public interface ILiveC2CMessageService extends IBaseService<LiveC2CMessage> {

    List<LiveC2CMessage> unReadMsg(Integer groupId, Integer userId);

	List<LiveC2CMessage> byOrAnd(String fromNickName, String toNickName, Date tb, Date tj);

	int byOrAndCount(String fromNickName, String toNickName,Date tb,Date tj);



}

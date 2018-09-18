package com.qiyou.dhlive.core.live.service.service.impl;

import com.qiyou.dhlive.core.live.outward.model.LiveC2CMessage;
import com.qiyou.dhlive.core.live.outward.service.ILiveC2CMessageService;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ThinkPad on 2018/3/12.
 */
@Service
public class LiveC2CMessageServiceImpl extends BaseMyBatisService<LiveC2CMessage> implements ILiveC2CMessageService {
    @Override
    public List<LiveC2CMessage> unReadMsg(Integer groupId, Integer userId) {
        LiveC2CMessage params = new LiveC2CMessage();
        params.setGroupId(groupId);
        params.setFromId(userId);
        params.setIsView(0);
        SearchCondition<LiveC2CMessage> condition = new SearchCondition<LiveC2CMessage>(params);
        return this.findByCondition(condition);
    }
}

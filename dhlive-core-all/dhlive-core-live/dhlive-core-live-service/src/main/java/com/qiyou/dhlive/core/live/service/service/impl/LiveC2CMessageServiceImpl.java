package com.qiyou.dhlive.core.live.service.service.impl;

import com.qiyou.dhlive.core.live.outward.model.LiveC2CMessage;
import com.qiyou.dhlive.core.live.outward.service.ILiveC2CMessageService;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

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
    @Override
    public List<LiveC2CMessage> byOrAnd(String fromNickName, String toNickName) {
    	Example e=new Example(LiveC2CMessage.class);
    	Criteria criteria=e.createCriteria();
    	criteria.andEqualTo("fromNickName",fromNickName);
    	criteria.andEqualTo("toNickName",toNickName);
    	
    	Criteria c=e.createCriteria();
    	c.andEqualTo("fromNickName",toNickName);
    	c.andEqualTo("toNickName",fromNickName);
    	e.or(c);
    	
    	return mapper.selectByExample(e);
    }
    
    @Override
    public int byOrAndCount(String fromNickName, String toNickName) {
    	Example e=new Example(LiveC2CMessage.class);
    	Criteria criteria=e.createCriteria();
    	criteria.andEqualTo("fromNickName",fromNickName);
    	criteria.andEqualTo("toNickName",toNickName);
    	
    	Criteria c=e.createCriteria();
    	c.andEqualTo("fromNickName",toNickName);
    	c.andEqualTo("toNickName",fromNickName);
    	e.or(c);
    	
    	return mapper.selectCountByExample(e);
    }
}

package com.qiyou.dhlive.core.user.service.service.impl;

import com.qiyou.dhlive.core.user.outward.model.UserRelation;
import com.qiyou.dhlive.core.user.outward.service.IUserRelationService;
import com.qiyou.dhlive.core.user.outward.vo.RelationVO;
import com.qiyou.dhlive.core.user.service.dao.UserRelationMapper;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ThinkPad on 2018/3/13.
 */
@Service
public class UserRelationServiceImpl extends BaseMyBatisService<UserRelation> implements IUserRelationService {

    @Autowired
    private UserRelationMapper mapper;

    public UserRelationServiceImpl() {
        super.setEntityClazz(UserRelation.class);
    }


    @Override
    public List<RelationVO> getRelationUserListAll(Integer userId) {
        return mapper.getRelationUserListAll(userId);
    }

    @Override
    public List<RelationVO> getRelationUserList(Integer userId, Integer waterGroupId) {
        return mapper.getRelationUserList(userId, waterGroupId);
    }
}

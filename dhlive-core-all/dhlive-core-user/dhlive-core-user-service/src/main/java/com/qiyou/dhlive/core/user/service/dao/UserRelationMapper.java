package com.qiyou.dhlive.core.user.service.dao;

import com.qiyou.dhlive.core.user.outward.model.UserRelation;
import com.qiyou.dhlive.core.user.outward.vo.RelationVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by ThinkPad on 2018/3/13.
 */
public interface UserRelationMapper extends Mapper<UserRelation> {

    List<RelationVO> getRelationUserListAll(Integer userId);

    List<RelationVO> getRelationUserList(Integer userId, Integer waterGroupId);

}

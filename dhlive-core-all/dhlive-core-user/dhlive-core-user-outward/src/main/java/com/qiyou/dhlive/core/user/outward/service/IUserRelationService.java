package com.qiyou.dhlive.core.user.outward.service;

import com.qiyou.dhlive.core.user.outward.model.UserRelation;
import com.qiyou.dhlive.core.user.outward.vo.RelationVO;
import com.yaozhong.framework.base.database.base.service.IBaseService;

import java.util.List;

/**
 * Created by ThinkPad on 2018/3/13.
 */
public interface IUserRelationService extends IBaseService<UserRelation> {

    List<RelationVO> getRelationUserListAll(Integer userId);

    List<RelationVO> getRelationUserList(Integer userId, Integer waterGroupId);

}

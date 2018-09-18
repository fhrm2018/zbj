package com.qiyou.dhlive.core.room.service.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.room.outward.model.RoomMessageBoard;
import com.qiyou.dhlive.core.room.outward.service.IRoomMessageBoardService;
import com.qiyou.dhlive.core.room.service.vo.MessageBoardVO;
import com.qiyou.dhlive.core.room.service.dao.RoomMessageBoardMapper;
import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserVipInfoService;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-20
 */
@Service
public class RoomMessageBoardServiceImpl extends BaseMyBatisService<RoomMessageBoard> implements IRoomMessageBoardService {

    @Autowired
    private RoomMessageBoardMapper mapper;

    @Autowired
    private IUserVipInfoService userVipInfoService;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    public RoomMessageBoardServiceImpl() {
        super.setEntityClazz(RoomMessageBoard.class);
    }

    @Override
    public DataResponse saveMessageBoard(RoomMessageBoard message) {
        UserVipInfo vip = this.userVipInfoService.findById(message.getUserId());
        message.setUserName(vip.getUserNickName());
        message.setMessageTime(new Date());
        message.setCreateTime(new Date());
        this.save(message);
        return new DataResponse(1000, "success");
    }


    @Override
    public DataResponse getMessageBoardByUser(PageSearch pageSearch, RoomMessageBoard params) {
        String value = this.redisManager.getStringValueByKey(RedisKeyConstant.MESSAGE_BOARD + pageSearch.getPage() + params.getRoomId());
        if (EmptyUtil.isNotEmpty(value)) {
            List<MessageBoardVO> board = JSONArray.parseArray(value, MessageBoardVO.class);
            return new DataResponse(1000, board);
        } else {
            params.setStatus(1);//审核通过
            SearchCondition<RoomMessageBoard> condition = new SearchCondition<RoomMessageBoard>(params, pageSearch);
            condition.buildOrderByConditions("auditTime", "desc");
            PageResult<RoomMessageBoard> result = this.findByPage(condition);
            for (int i = 0; i < result.getRows().size(); i++) {
                if (EmptyUtil.isNotEmpty(result.getRows().get(i).getmId())) {
                    result.getRows().remove(i);
                    i--;
                }
            }

            //返回页面的数据
            List<MessageBoardVO> board = new ArrayList<MessageBoardVO>();
            for (int i = 0; i < result.getRows().size(); i++) {
                //构造数据对象, 确定第一条留言
                MessageBoardVO record = new MessageBoardVO();
                record.setRoomMessageBoard(result.getRows().get(i));
                //第一条留言的回复
                RoomMessageBoard param = new RoomMessageBoard();
                param.setStatus(1);
                param.setmId(result.getRows().get(i).getId());
                SearchCondition<RoomMessageBoard> condition1 = new SearchCondition<RoomMessageBoard>(param);
                condition1.buildOrderByConditions("auditTime", "desc");
                List<RoomMessageBoard> childs = this.findByCondition(condition1);
                record.setChilds(childs);
                board.add(record);
            }
            this.redisManager.saveStringBySeconds(RedisKeyConstant.MESSAGE_BOARD + pageSearch.getPage() + params.getRoomId(), JSONArray.toJSONString(board));
            return new DataResponse(1000, board);
        }
    }

    @Override
    public DataResponse getMessageBoardByAdmin(PageSearch pageSearch, RoomMessageBoard params) {
        Object[] s = {0, 1};//新消息和审核通过的
        SearchCondition<RoomMessageBoard> condition = new SearchCondition<RoomMessageBoard>(params, pageSearch);
        condition.buildInConditions("status", Arrays.asList(s));
        condition.buildOrderByConditions("createTime", "desc");
        PageResult<RoomMessageBoard> result = this.findByPage(condition);
        return new DataResponse(1000, result);
    }

}
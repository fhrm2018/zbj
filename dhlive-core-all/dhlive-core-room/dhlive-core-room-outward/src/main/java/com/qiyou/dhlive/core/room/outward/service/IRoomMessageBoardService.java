package com.qiyou.dhlive.core.room.outward.service;

import com.qiyou.dhlive.core.room.outward.model.RoomMessageBoard;
import com.yaozhong.framework.base.database.base.service.IBaseService;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-20
 */
public interface IRoomMessageBoardService extends IBaseService<RoomMessageBoard> {

    DataResponse saveMessageBoard(RoomMessageBoard message);

    DataResponse getMessageBoardByUser(PageSearch pageSearch, RoomMessageBoard params);

    DataResponse getMessageBoardByAdmin(PageSearch pageSearch, RoomMessageBoard params);

}
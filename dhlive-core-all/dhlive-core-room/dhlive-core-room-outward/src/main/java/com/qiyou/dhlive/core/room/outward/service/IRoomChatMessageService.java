package com.qiyou.dhlive.core.room.outward.service;

import com.qiyou.dhlive.core.room.outward.model.RoomChatMessage;
import com.yaozhong.framework.base.database.base.service.IBaseService;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-20
 */
public interface IRoomChatMessageService extends IBaseService<RoomChatMessage> {

    DataResponse saveChatMessage(RoomChatMessage message);

    DataResponse auditChatMessage(RoomChatMessage message);

    DataResponse getChatMessageByUser(RoomChatMessage message);

    DataResponse getChatMessageByAdmin(RoomChatMessage message);

    DataResponse deleteChatMessage(RoomChatMessage params);

    DataResponse getChatMessageByUniqueId(RoomChatMessage params);
    
    public DataResponse getMessageList();

}
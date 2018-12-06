package com.qiyou.dhlive.api.base.outward.service;

import com.qiyou.dhlive.core.room.outward.model.RoomChatMessage;

public interface IBaseOptLogApiService {
	
	void saveManageMessageLog(RoomChatMessage params);
	
	void saveAuditMessageLog(RoomChatMessage params);
	
	void saveDelMessageLog(RoomChatMessage params);

}

package com.qiyou.dhlive.api.base.outward.service;

import com.qiyou.dhlive.core.live.outward.model.LiveRoom;
import com.qiyou.dhlive.core.room.outward.model.RoomChatMessage;
import com.qiyou.dhlive.core.room.outward.model.RoomClass;
import com.qiyou.dhlive.core.room.outward.model.RoomFile;
import com.qiyou.dhlive.core.room.outward.model.RoomMessageBoard;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;

import java.util.List;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/30
 */
public interface ILiveRoomApiService {

    List<LiveRoom> getLiveRoom(LiveRoom params);

    DataResponse getLiveRoomList(PageSearch pageSearch, LiveRoom params);

    DataResponse saveLiveRoom(LiveRoom params);

    DataResponse deleteLiveRoom(LiveRoom params);

    DataResponse saveRoomFile(RoomFile params);

    DataResponse getLiveRoomFileList(PageSearch pageSearch, RoomFile params);

    DataResponse deleteFile(RoomFile params);

    DataResponse getLiveRoomFile(RoomFile params);

    DataResponse getOnlineUser(Integer roomId);

    DataResponse saveRoomClass(RoomClass params);

    DataResponse getLiveRoomClassList(PageSearch pageSearch, RoomClass params);

    DataResponse getLiveRoomClass(RoomClass params);

    DataResponse deleteClass(RoomClass params);

    DataResponse getWaterUserList(Integer roomId);

    List<String> isOnline(String[] toAccount);

}

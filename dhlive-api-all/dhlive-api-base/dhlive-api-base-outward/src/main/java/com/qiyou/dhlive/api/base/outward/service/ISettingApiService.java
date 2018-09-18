package com.qiyou.dhlive.api.base.outward.service;

import com.qiyou.dhlive.core.room.outward.model.RoomAnnouncement;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;

/**
 * describe:
 *
 * @author fish
 * @date 2018/02/03
 */
public interface ISettingApiService {

    DataResponse saveAnnouncement(RoomAnnouncement params);

    DataResponse getAnnouncementList(PageSearch pageSearch, RoomAnnouncement params);

    DataResponse getAnnouncementList(RoomAnnouncement params);
}

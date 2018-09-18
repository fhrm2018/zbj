package com.qiyou.dhlive.api.base.service.service.impl;

import com.qiyou.dhlive.api.base.outward.service.ISettingApiService;
import com.qiyou.dhlive.api.base.outward.util.NoticeUtil;
import com.qiyou.dhlive.api.base.outward.util.TLSUtils;
import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.core.live.outward.model.LiveRoom;
import com.qiyou.dhlive.core.live.outward.service.ILiveRoomService;
import com.qiyou.dhlive.core.room.outward.model.RoomAnnouncement;
import com.qiyou.dhlive.core.room.outward.service.IRoomAnnouncementService;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * describe:
 *
 * @author fish
 * @date 2018/02/03
 */
@Service
public class SettingApiServiceImpl implements ISettingApiService {

    @Autowired
    private IBaseSysParamService baseSysParamService;

    @Autowired
    private IRoomAnnouncementService roomAnnouncementService;

    @Autowired
    private ILiveRoomService liveRoomService;

    @Override
    public DataResponse saveAnnouncement(RoomAnnouncement params) {
        if (EmptyUtil.isEmpty(params.getId())) {
            LiveRoom room = this.liveRoomService.findById(params.getRoomId());
            params.setRoomName(room.getRoomName());
            params.setCreateTime(new Date());
            this.roomAnnouncementService.save(params);
            String sdkAppId = this.baseSysParamService.getValueByKey("sdk_app_id");
            String identifier = this.baseSysParamService.getValueByKey("identifier");
            String privateKey = this.baseSysParamService.getValueByKey("private_key");
            String userSig = TLSUtils.getUserSig(Integer.parseInt(sdkAppId), identifier, privateKey);
            //发送公告通知
            NoticeUtil.sendAnnouncementNotic(room.getRoomGroupId(), userSig, identifier, sdkAppId, params.getContent());
        } else {
            this.roomAnnouncementService.modifyEntity(params);
        }
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse getAnnouncementList(PageSearch pageSearch, RoomAnnouncement params) {
        params.setStatus(0);//未删除用户标记
        SearchCondition<RoomAnnouncement> condition = new SearchCondition<RoomAnnouncement>(params, pageSearch);
        PageResult<RoomAnnouncement> result = this.roomAnnouncementService.findByPage(condition);
        return new DataResponse(1000, result);
    }

    @Override
    public DataResponse getAnnouncementList(RoomAnnouncement params) {
        params.setStatus(0);//未删除用户标记
        SearchCondition<RoomAnnouncement> condition = new SearchCondition<RoomAnnouncement>(params);
        condition.buildOrderByConditions("createTime", "desc");
        List<RoomAnnouncement> result = this.roomAnnouncementService.findByCondition(condition);
        return new DataResponse(1000, result);
    }
}

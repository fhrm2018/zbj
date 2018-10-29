package com.qiyou.dhlive.prd.setting.controller;

import com.qiyou.dhlive.api.base.outward.service.ICheckIpVisitService;
import com.qiyou.dhlive.api.base.outward.service.ILiveRoomApiService;
import com.qiyou.dhlive.api.base.outward.service.ISettingApiService;
import com.qiyou.dhlive.core.base.outward.model.BaseIp;
import com.qiyou.dhlive.core.base.outward.model.BaseWord;
import com.qiyou.dhlive.core.base.outward.service.IBaseWordService;
import com.qiyou.dhlive.core.live.outward.model.LiveRoom;
import com.qiyou.dhlive.core.room.outward.model.RoomAnnouncement;
import com.qiyou.dhlive.core.room.outward.service.IRoomAnnouncementService;
import com.qiyou.dhlive.prd.component.annotation.ResourceAnnotation;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.qiyou.dhlive.prd.component.util.ResourceBaseController;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.web.annotation.session.NeedSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/29
 */
@Controller
@RequestMapping(value = "setting")
public class SettingController {

    @Autowired
    private ILiveRoomApiService liveRoomApiService;

    @Autowired
    private ISettingApiService settingApiService;

    @Autowired
    private ICheckIpVisitService checkIpVisitService;

    @Autowired
    private IBaseWordService baseWordService;

    @Autowired
    private IRoomAnnouncementService roomAnnouncementService;

    @NeedSession
    @RequestMapping("")
    @UnSecurity
    public String index(Model model) {
        //直播间
        List<LiveRoom> rooms = this.liveRoomApiService.getLiveRoom(new LiveRoom());
        model.addAttribute("rooms", rooms);
        return "setting/index";
    }

    @NeedSession
    @UnSecurity
    @RequestMapping("timingSpeak")
    public String timingSpeak(Model model) {
        //直播间
        List<LiveRoom> rooms = this.liveRoomApiService.getLiveRoom(new LiveRoom());
        model.addAttribute("rooms", rooms);
        return "setting/index";
    }

    @NeedSession
    @UnSecurity
    @RequestMapping("setIps")
    public String setBlackOrWhiteIp(Model model) {
        return "setting/ip";
    }

    @NeedSession
    @UnSecurity
    @RequestMapping("setWords")
    public String setSensitiveWords(Model model) {
        return "setting/word";
    }

    /**
     * 公告保存
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "saveAnnouncement")
    @ResponseBody
    public DataResponse saveAnnouncement(RoomAnnouncement params) {
        return this.settingApiService.saveAnnouncement(params);
    }

    /**
     * 公告列表(分页)
     *
     * @param pageSearch
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping("getAnnouncementList")
    @ResponseBody
    public DataResponse getAnnouncementList(PageSearch pageSearch, RoomAnnouncement params) {
        return this.settingApiService.getAnnouncementList(pageSearch, params);
    }

    /**
     * 获取公告
     *
     * @param params 公告对象
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "getAnnouncementById")
    @ResponseBody
    public DataResponse getAnnouncementById(RoomAnnouncement params) {
        List<RoomAnnouncement> result = (List<RoomAnnouncement>) this.settingApiService.getAnnouncementList(params).getData();
        if (EmptyUtil.isEmpty(result)) {
            return new DataResponse(1001, "not found");
        } else {
            return new DataResponse(1000, result.get(0));
        }
    }

    /**
     * 删除公告
     *
     * @param params 公告对象
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "deleteAnnouncement")
    @ResponseBody
    public DataResponse deleteAnnouncement(RoomAnnouncement params) {
        params.setStatus(1);//删除标记
        this.roomAnnouncementService.modifyEntity(params);
        return new DataResponse(1000, "success");
    }


    /**
     * 保存ip黑白名单
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "saveIp")
    @ResponseBody
    public DataResponse saveIp(BaseIp params) {
        return this.checkIpVisitService.saveIpBlackOrWhiteList(params);
    }


    /**
     * ip黑白名单
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "getIpList")
    @ResponseBody
    public DataResponse getIpList(BaseIp params) {
        return this.checkIpVisitService.getIpList(params);
    }


    /**
     * 删除ip
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "delIp")
    @ResponseBody
    public DataResponse delIp(BaseIp params) {
        return this.checkIpVisitService.delIp(params);
    }


    /**
     * 保存关键字
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "saveWord")
    @ResponseBody
    public DataResponse saveWord(BaseWord params) {
        return this.baseWordService.saveWordBlackOrWhiteList(params);
    }


    /**
     * 敏感词黑白名单
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "getWordList")
    @ResponseBody
    public DataResponse getWordList(BaseWord params) {
        return this.baseWordService.getWordList(params);
    }

    /**
     * 删除敏感词
     *
     * @param params
     * @return
     */
    @NeedSession
    @UnSecurity
    @RequestMapping(value = "delWord")
    @ResponseBody
    public DataResponse delWord(BaseWord params) {
        return this.baseWordService.delWord(params);
    }


}

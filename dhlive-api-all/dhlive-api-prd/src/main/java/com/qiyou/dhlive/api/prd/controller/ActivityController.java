package com.qiyou.dhlive.api.prd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiyou.dhlive.api.base.outward.service.IActivityApiService;
import com.qiyou.dhlive.api.prd.mvc.UserSession;
import com.qiyou.dhlive.core.activity.outward.model.ActivityLuckyDrawWinners;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/23
 */
@Controller
@RequestMapping(value = "activity")
public class ActivityController {

    @Autowired
    private IActivityApiService activityApiService;

    /**
     * 抢红包
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "robRedPack")
    @ResponseBody
    public DataResponse robRedPack(ActivityLuckyDrawWinners params) {
        return this.activityApiService.robRedPack(params);
    }

    /**
     * 大转盘
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "rotaryTable")
    @ResponseBody
    public DataResponse rotaryTable(ActivityLuckyDrawWinners params) {
        return this.activityApiService.rotaryTable(params);
    }

    /**
     * 大转盘的抽奖信息
     * @param params
     * @return
     */
    @RequestMapping(value = "prizeInfo")
    @ResponseBody
    public DataResponse prizeInfo(ActivityLuckyDrawWinners params){
        return this.activityApiService.prizeInfo(params);
    }

    /**
     * 是否领过红包
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "isReceive")
    @ResponseBody
    public DataResponse isReceive(ActivityLuckyDrawWinners params) {
        UserSession session = UserSession.getUserSession();
        params.setUserId(session.getUserId());
        params.setConfigId(1);
        params.setStatus(0);
        return this.activityApiService.isReceive(params);
    }


}

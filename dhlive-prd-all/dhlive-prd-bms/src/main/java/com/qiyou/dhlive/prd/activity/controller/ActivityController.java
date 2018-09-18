package com.qiyou.dhlive.prd.activity.controller;

import com.google.gson.Gson;
import com.qiyou.dhlive.core.activity.outward.model.ActivityLuckyDrawConfig;
import com.qiyou.dhlive.core.activity.outward.model.ActivityLuckyDrawWinners;
import com.qiyou.dhlive.core.activity.outward.service.IActivityLuckyDrawConfigService;
import com.qiyou.dhlive.core.activity.outward.service.IActivityLuckyDrawWinnersService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.prd.component.annotation.ResourceAnnotation;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.qiyou.dhlive.prd.component.session.EmployeeSession;
import com.qiyou.dhlive.prd.component.util.ResourceBaseController;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;
import com.yaozhong.framework.web.annotation.session.NeedSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/29
 */
@Controller
@RequestMapping(value = "activity")
public class ActivityController extends ResourceBaseController {

    @Autowired
    private IActivityLuckyDrawWinnersService activityLuckyDrawWinnersService;

    @Autowired
    private IActivityLuckyDrawConfigService activityLuckyDrawConfigService;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    @NeedSession
    @RequestMapping("")
    @ResourceAnnotation(name = "活动", type = 1, url = "/activity/", remark = "活动", icon = "order")
    public String index(Model model) {
        return "activity/redPack";
    }

    @NeedSession
    @RequestMapping(value = "redPack")
    @ResourceAnnotation(name = "红包", pName = "活动", type = 2, url = "/activity/redPack", remark = "红包", icon = "order")
    public String redPack(Model model) {
        return "activity/redPack";
    }

    @NeedSession
    @RequestMapping(value = "rotaryTable")
    @ResourceAnnotation(name = "转盘", pName = "活动", type = 2, url = "/activity/rotaryTable", remark = "转盘", icon = "order")
    public String rotaryTable(Model model) {
        return "activity/rotaryTable";
    }

    /**
     * 获取中奖名单
     *
     * @param pageSearch
     * @param params
     * @return
     */
    @UnSecurity
    @NeedSession
    @RequestMapping(value = "getWinnerList")
    @ResponseBody
    public DataResponse getWinnerList(PageSearch pageSearch, ActivityLuckyDrawWinners params) {
        SearchCondition<ActivityLuckyDrawWinners> condition = new SearchCondition<ActivityLuckyDrawWinners>(params, pageSearch);
        PageResult<ActivityLuckyDrawWinners> data = this.activityLuckyDrawWinnersService.findByPage(condition);
        return new DataResponse(1000, data);
    }


    /**
     * 发送金钱
     *
     * @param params
     * @return
     */
    @UnSecurity
    @NeedSession
    @RequestMapping(value = "sendMoney")
    @ResponseBody
    public DataResponse sendMoney(ActivityLuckyDrawWinners params) {
        EmployeeSession eSession = EmployeeSession.getEmployeeSession();
        SearchCondition<ActivityLuckyDrawWinners> condition = new SearchCondition<ActivityLuckyDrawWinners>(params);
        ActivityLuckyDrawWinners update = new ActivityLuckyDrawWinners();
        update.setStatus(1);
        update.setOptUserId(eSession.getEmployeeId());
        update.setOptUserName(eSession.getName());
        update.setOptTime(new Date());
        this.activityLuckyDrawWinnersService.modifyEntityByCondition(update, condition);
        return new DataResponse(1000, "success");
    }


    /**
     * 发放统计
     *
     * @return
     */
    @UnSecurity
    @NeedSession
    @RequestMapping(value = "getTotal")
    @ResponseBody
    public DataResponse getTotal() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> data = new HashMap<String, Object>();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        // ActivityLuckyDrawConfig params = new ActivityLuckyDrawConfig();
        // SearchCondition<ActivityLuckyDrawConfig> condition = new SearchCondition<ActivityLuckyDrawConfig>(params);
        // List<ActivityLuckyDrawConfig> config = this.activityLuckyDrawConfigService.findByCondition(condition);
        ActivityLuckyDrawConfig config = activityLuckyDrawConfigService.findById(1);

        List<ActivityLuckyDrawWinners> winners = this.activityLuckyDrawWinnersService.findByField("configId", config.getId());
        data.put("time", sdf.format(config.getActivityStartTime()) + "～" + sdf.format(config.getActivityEndTime()));
        BigDecimal totalMoney = new BigDecimal(0);
        BigDecimal sendMoney = new BigDecimal(0);
        for (int j = 0; j < winners.size(); j++) {
            totalMoney = totalMoney.add(new BigDecimal(winners.get(j).getMoney()));
            if (winners.get(j).getStatus().intValue() == 1) {
                sendMoney = sendMoney.add(new BigDecimal(winners.get(j).getMoney()));
            }
        }
        data.put("totalMoney", totalMoney);
        data.put("sendMoney", sendMoney);
        result.add(data);
        return new DataResponse(1000, result);
    }

    /**
     * 红包配置
     *
     * @param params
     * @return
     */
    @UnSecurity
    @NeedSession
    @RequestMapping(value = "getConfig")
    @ResponseBody
    public DataResponse getConfig(ActivityLuckyDrawConfig params) {
        /*SearchCondition<ActivityLuckyDrawConfig> condition = new SearchCondition<ActivityLuckyDrawConfig>(params);
        List<ActivityLuckyDrawConfig> data = this.activityLuckyDrawConfigService.findByCondition(condition);*/

        ActivityLuckyDrawConfig data = this.activityLuckyDrawConfigService.findById(1);
        return new DataResponse(1000, data);
    }

    /**
     * 保存配置
     *
     * @param params
     * @param config
     * @return
     */
    @UnSecurity
    @NeedSession
    @RequestMapping(value = "saveConfig")
    @ResponseBody
    public DataResponse saveConfig(ActivityLuckyDrawConfig params, String config) {
        EmployeeSession eSession = EmployeeSession.getEmployeeSession();
        if (EmptyUtil.isEmpty(params.getRoomId())) {
            params.setRoomId(4);
        }
        params.setActivityConfig(config.replaceAll("&quot;", "\""));
        params.setModifyTime(new Date());
        params.setModifyUserId(eSession.getEmployeeId());
        this.activityLuckyDrawConfigService.modifyEntity(params);
        this.redisManager.saveString(RedisKeyConstant.ACTIVITY_CONFIG + params.getId(), new Gson().toJson(params));
        return new DataResponse(1000, "success");
    }

}

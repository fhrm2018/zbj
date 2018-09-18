package com.qiyou.dhlive.api.base.service.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiyou.dhlive.api.base.outward.service.IActivityApiService;
import com.qiyou.dhlive.api.base.outward.vo.ActivityVO;
import com.qiyou.dhlive.core.activity.outward.model.ActivityLuckyDrawConfig;
import com.qiyou.dhlive.core.activity.outward.model.ActivityLuckyDrawWinners;
import com.qiyou.dhlive.core.activity.outward.service.IActivityLuckyDrawConfigService;
import com.qiyou.dhlive.core.activity.outward.service.IActivityLuckyDrawWinnersService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserVipInfoService;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by fish on 2018/4/20.
 */
@Service
public class ActivityApiServiceImpl implements IActivityApiService {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IActivityLuckyDrawConfigService activityLuckyDrawConfigService;

    @Autowired
    private IActivityLuckyDrawWinnersService activityLuckyDrawWinnersService;

    @Autowired
    private IUserVipInfoService userVipInfoService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    @Override
    public DataResponse redPackInfo(ActivityLuckyDrawConfig params) {
        String configStr = this.redisManager.getStringValueByKey(RedisKeyConstant.ACTIVITY_CONFIG + params.getId());
        if (EmptyUtil.isEmpty(configStr)) {
            SearchCondition<ActivityLuckyDrawConfig> condition = new SearchCondition<ActivityLuckyDrawConfig>(params);
            params = this.activityLuckyDrawConfigService.findOneByCondition(condition);
            this.redisManager.saveString(RedisKeyConstant.ACTIVITY_CONFIG + params.getId(), new Gson().toJson(params));
        } else {
            params = new Gson().fromJson(configStr, ActivityLuckyDrawConfig.class);
        }

        //活动是否开启
        if (params.getIsOpen().intValue() == 0) {
            return new DataResponse(1001, "活动暂未开启, 请后续关注.");
        }
        //判断是否在活动时间
        Date now = new Date();
        if (now.after(params.getActivityStartTime()) && now.before(params.getActivityEndTime())) {
            return new DataResponse(1000, params);
        } else {
            return new DataResponse(1002, "活动时间已结束, 请后续关注.");
        }
    }

    @Override
    public DataResponse robRedPack(ActivityLuckyDrawWinners params) {
        //存放配置信息
        List<ActivityVO> item = new ArrayList<ActivityVO>();

        //从缓存读取活动配置信息
        String configStr = this.redisManager.getStringValueByKey(RedisKeyConstant.ACTIVITY_CONFIG + params.getConfigId());
        ActivityLuckyDrawConfig config = new ActivityLuckyDrawConfig();
        if (EmptyUtil.isEmpty(configStr)) {
            config = this.activityLuckyDrawConfigService.findById(params.getConfigId());
        } else {
            config = new Gson().fromJson(configStr, ActivityLuckyDrawConfig.class);
        }
        JSONArray arr = JSONArray.parseArray(config.getActivityConfig());
        for (int i = 0; i < arr.size(); i++) {
            ActivityVO vo = JSONObject.parseObject(arr.get(i).toString(), ActivityVO.class);
            item.add(vo);
        }

        //判断活动是否开启
        if (config.getIsOpen().intValue() == 0) {
            return new DataResponse(1001, "活动暂未开启, 请后续关注.");
        }

        //判断是否领取过
        ActivityLuckyDrawWinners winner = new ActivityLuckyDrawWinners();
        winner.setConfigId(params.getConfigId());
        winner.setUserId(params.getUserId());
        SearchCondition<ActivityLuckyDrawWinners> condition = new SearchCondition<ActivityLuckyDrawWinners>(winner);
        winner = this.activityLuckyDrawWinnersService.findOneByCondition(condition);
        if (EmptyUtil.isNotEmpty(winner)) {
            return new DataResponse(1002, "您已经领取过红包. 请不要重复领取");
        }

        //从缓存读取会员用户信息
        String vipStr = this.redisManager.getStringValueByKey(RedisKeyConstant.VIP + params.getUserId());
        UserVipInfo vip = new UserVipInfo();
        if (EmptyUtil.isNotEmpty(vipStr)) {
            vip = new Gson().fromJson(vipStr, UserVipInfo.class);
        } else {
            vip = this.userVipInfoService.findById(params.getUserId());
            this.redisManager.saveString(RedisKeyConstant.VIP + params.getUserId(), new Gson().toJson(vip));
        }

        //判断是否在活动时间
        Date now = new Date();
        if (now.after(config.getActivityStartTime()) && now.before(config.getActivityEndTime())) {
            //抢红包逻辑开始
            baseLog.info(LogFormatUtil.getActionFormat("vip用户:" + vip.getUserNickName() + "开始抢红包."));
            Integer random = new Random().nextInt(100) + 1;
            baseLog.info(LogFormatUtil.getActionFormat("生成随机数为:" + random));
            BigDecimal money = new BigDecimal(0);
            Integer size = item.size() - 1;
            for (int i = 0; i < item.size(); i++) {
                if (i == 0) {
                    if (random > Integer.parseInt(item.get(i).getProbability())) {
                        money = new BigDecimal(item.get(i).getMoney());
                        break;
                    }
                    if (i + 1 <= size) {
                        if (random < Integer.parseInt(item.get(i).getProbability()) && random > Integer.parseInt(item.get(i + 1).getProbability())) {
                            money = new BigDecimal(item.get(i).getMoney());
                            break;
                        }
                    }
                }
                if (i != 0 && i < size) {
                    if (random < Integer.parseInt(item.get(i).getProbability()) && random > Integer.parseInt(item.get(i + 1).getProbability())) {
                        money = new BigDecimal(item.get(i).getMoney());
                        break;
                    }
                }
                if (i == size) {
                    if (random < Integer.parseInt(item.get(i).getProbability())) {
                        money = new BigDecimal(item.get(i).getMoney());
                        break;
                    }
                }
            }

            //比较结果, 如果漏掉, 置为最小金额
            int result = money.compareTo(new BigDecimal(0));
            if (result == 0) {
                money = new BigDecimal(item.get(0).getMoney());
            }

            ActivityLuckyDrawWinners record = new ActivityLuckyDrawWinners();
            record.setRoomId(params.getRoomId());
            record.setConfigId(params.getConfigId());
            record.setUserId(params.getUserId());
            record.setUserNickName(vip.getUserNickName());
            record.setUserRealName(vip.getUserRealName());
            record.setUserPhone(vip.getUserTel());
            record.setWinnerTime(new Date());
            record.setMoney(money.toString());
            this.activityLuckyDrawWinnersService.save(record);
            baseLog.info(LogFormatUtil.getActionFormat("vip用户:" + vip.getUserNickName() + "抢红包结束, 抢到:" + money + "元!"));
            return new DataResponse(1000, "success", money);
        } else {
            return new DataResponse(1003, "活动时间已结束, 请后续关注.");
        }
    }

    /**
     * 幸运轮盘抽奖
     *
     * @param params
     * @return
     */
    @Override
    public DataResponse rotaryTable(ActivityLuckyDrawWinners params) {
        //存放配置信息
        List<ActivityVO> item = new ArrayList<ActivityVO>();

        //从缓存读取活动配置信息
        String configStr = this.redisManager.getStringValueByKey(RedisKeyConstant.ACTIVITY_CONFIG + params.getConfigId());
        ActivityLuckyDrawConfig config = new ActivityLuckyDrawConfig();
        if (EmptyUtil.isEmpty(configStr)) {
            config = this.activityLuckyDrawConfigService.findById(params.getConfigId());
        } else {
            config = new Gson().fromJson(configStr, ActivityLuckyDrawConfig.class);
        }
        JSONArray arr = JSONArray.parseArray(config.getActivityConfig());
        for (int i = 0; i < arr.size(); i++) {
            ActivityVO vo = JSONObject.parseObject(arr.get(i).toString(), ActivityVO.class);
            item.add(vo);
        }

        //判断活动是否开启
        if (config.getIsOpen().intValue() == 0) {
            return new DataResponse(1001, "活动暂未开启, 请后续关注.");
        }

        //判断当天是否领取过
//        ActivityLuckyDrawWinners winner = new ActivityLuckyDrawWinners();
//        winner.setConfigId(params.getConfigId());
//        winner.setUserId(params.getUserId());
//        SearchCondition<ActivityLuckyDrawWinners> condition = new SearchCondition<ActivityLuckyDrawWinners>(winner);
//        winner = this.activityLuckyDrawWinnersService.findOneByCondition(condition);
//        if (EmptyUtil.isNotEmpty(winner)) {
//            String message = "您今天已经抽到奖品" + " “" +winner.getMoney() +"” "+ "请明天再来！";
//            return new DataResponse(1002, message);
//        }

        String prizeInfo = this.redisManager.getStringValueByKey(RedisKeyConstant.ACTIVITY_rotary_Table + DateUtil.getDateString(new Date(), "yyyy-MM-dd") + "-" + params.getGroupId() + "-" + params.getUserId());
        if (EmptyUtil.isNotEmpty(prizeInfo)) {
            String message = "您今天已经抽到奖品,请明天再来！";
            return new DataResponse(1002, message);
        }else{
            //从缓存读取会员用户信息
            UserInfo guestInfo = new UserInfo();
            if (params.getGroupId() == 1) {
                String guest = this.redisManager.getStringValueByKey(RedisKeyConstant.TOURISTS + params.getUserId());
                if (EmptyUtil.isNotEmpty(guestInfo)) {
                    guestInfo = new Gson().fromJson(guest, UserInfo.class);
                } else {
                    guestInfo = this.userInfoService.findById(params.getUserId());
                    this.redisManager.saveString(RedisKeyConstant.TOURISTS + params.getUserId(), new Gson().toJson(guestInfo));
                }
            }


            //判断是否在活动时间
            Date now = new Date();
            if (now.after(config.getActivityStartTime()) && now.before(config.getActivityEndTime())) {
                //大转盘抽奖逻辑
                baseLog.info(LogFormatUtil.getActionFormat("用户:" + guestInfo.getUserNickName() + "开始幸运转盘抽奖."));
                Integer random = new Random().nextInt(100) + 1;
                baseLog.info(LogFormatUtil.getActionFormat("生成随机数为:" + random));
                String prize = "";
                Integer size = item.size() - 1;
                for (int i = 0; i < item.size(); i++) {
                    if (i == 0) {
                        if (random > Integer.parseInt(item.get(i).getProbability())) {
                            prize = item.get(i).getMoney();
                            break;
                        }
                        if (i + 1 <= size) {
                            if (random < Integer.parseInt(item.get(i).getProbability()) && random > Integer.parseInt(item.get(i + 1).getProbability())) {
                                prize = item.get(i).getMoney();
                                break;
                            }
                        }
                    }
                    if (i != 0 && i < size) {
                        if (random < Integer.parseInt(item.get(i).getProbability()) && random > Integer.parseInt(item.get(i + 1).getProbability())) {
                            prize = item.get(i).getMoney();
                            break;
                        }
                    }
                    if (i == size) {
                        if (random < Integer.parseInt(item.get(i).getProbability())) {
                            prize = item.get(i).getMoney();
                            break;
                        }
                    }
                }

                //比较结果, 如果漏掉, 置为最小金额
            /*int result = money.compareTo(new BigDecimal(0));
            if (result == 0) {
                money = new BigDecimal(item.get(0).getMoney());
            }
*/
                ActivityLuckyDrawWinners record = new ActivityLuckyDrawWinners();
                record.setRoomId(params.getRoomId());
                record.setConfigId(params.getConfigId());
                record.setUserId(params.getUserId());
                record.setUserNickName(guestInfo.getUserNickName());
                record.setWinnerTime(new Date());
                record.setGroupId(params.getGroupId());
                record.setMoney(prize);
                // 保存一条抽奖记录到Redis中
                this.redisManager.saveStringBySeconds(RedisKeyConstant.ACTIVITY_rotary_Table + DateUtil.getDateString(new Date(), "yyyy-MM-dd") + "-" + params.getGroupId() + "-" + params.getUserId(), prize, 86400);

                this.activityLuckyDrawWinnersService.save(record);
                baseLog.info(LogFormatUtil.getActionFormat("用户:" + guestInfo.getUserNickName() + "转盘抽奖结束, 抽到:" + prize + "奖品!"));
                return new DataResponse(1000, "success", prize);
            } else {
                return new DataResponse(1003, "活动时间已结束, 请后续关注.");
            }
        }
    }


    @Override
    public DataResponse isReceive(ActivityLuckyDrawWinners params) {
        SearchCondition<ActivityLuckyDrawWinners> condition = new SearchCondition<ActivityLuckyDrawWinners>(params);
        params = this.activityLuckyDrawWinnersService.findOneByCondition(condition);
        if (EmptyUtil.isEmpty(params)) {
            return new DataResponse(1001, "not found.");
        }
        return new DataResponse(1000, params);
    }

    @Override
    public DataResponse prizeInfo(ActivityLuckyDrawWinners params) {
        String prizeInfo = this.redisManager.getStringValueByKey(RedisKeyConstant.ACTIVITY_rotary_Table + DateUtil.getDateString(new Date(), "yyyy-MM-dd") + "-" + params.getGroupId() + "-" + params.getUserId());
        if(EmptyUtil.isEmpty(prizeInfo)){
            return new DataResponse(1001,"暂无该用户抽奖信息");
        }
        return new DataResponse(1000,"以抽奖",prizeInfo);
    }

}

package com.qiyou.dhlive.prd.cron.record.job;

import com.qiyou.dhlive.core.base.outward.model.BaseIp;
import com.qiyou.dhlive.core.base.outward.service.IBaseIpService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.prd.cron.util.BaseJob;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.List;

/**
 * Created by ThinkPad on 2018/3/19.
 */
public class IPJob extends BaseJob {

    @Autowired
    private IBaseIpService baseIpService;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    @Override
    public void preform() {
        cacheBlackIp();
        cacheWhiteIp();
    }

    @Override
    public void setJobName() {
        this.jobName = "缓存黑/白名单定时任务";
    }


    /**
     * 缓存黑名单
     */
    private void cacheBlackIp() {
        baseLog.info(LogFormatUtil.getActionFormat("缓存黑名单定时任务开始. 时间:" + DateUtil.getDateTime(new Date())));
        BaseIp params = new BaseIp();
        params.setType(0);
        SearchCondition<BaseIp> condition = new SearchCondition<BaseIp>(params);
        List<BaseIp> list = this.baseIpService.findByCondition(condition);
        for (int i = 0; i < list.size(); i++) {
            this.redisManager.saveStringBySeconds(RedisKeyConstant.REIDSKEY_BLACKIP + list.get(i).getIp(), list.get(i).getIp());
        }
        baseLog.info(LogFormatUtil.getActionFormat("缓存黑名单定时任务结束. 时间:" + DateUtil.getDateTime(new Date())));
    }


    /**
     * 缓存白名单
     */
    private void cacheWhiteIp() {
        baseLog.info(LogFormatUtil.getActionFormat("缓存白名单定时任务开始. 时间:" + DateUtil.getDateTime(new Date())));
        BaseIp params = new BaseIp();
        params.setType(1);
        SearchCondition<BaseIp> condition = new SearchCondition<BaseIp>(params);
        List<BaseIp> list = this.baseIpService.findByCondition(condition);
        for (int i = 0; i < list.size(); i++) {
            this.redisManager.saveStringBySeconds(RedisKeyConstant.REIDSKEY_WHITEIP + list.get(i).getIp(), list.get(i).getIp());
        }
        baseLog.info(LogFormatUtil.getActionFormat("缓存白名单定时任务结束. 时间:" + DateUtil.getDateTime(new Date())));
    }

}

package com.qiyou.dhlive.api.prd.controller;

import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

/**
 * Created by ThinkPad on 2018/3/24.
 */
@Controller
public class CronController {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    //游客定时任务
    @Scheduled(cron = "0/15 * *  * * ? ")   //每15秒执行一次
    public void clearYKOutLineUserCron() {
        baseLog.info(LogFormatUtil.getActionFormat("定时任务清理下线游客开始"));
        List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.YK_IDS);
        for (int i = 0; i < listJson.size(); i++) {
            String str[] = listJson.get(i).split("-");
            Long onLine = new Date().getTime() - Long.parseLong(str[0]);
            if (onLine > 10000) {
                redisManager.deleteFromHashByStoreKeyAndMapKey(RedisKeyConstant.YK_IDS, str[1]);
            }
        }
        baseLog.info(LogFormatUtil.getActionFormat("定时任务清理下线游客结束"));
    }


    //助理定时任务
//    @Scheduled(cron = "0/15 * *  * * ? ")   //每15秒执行一次
//    public void clearZLOutLineUserCron() {
//        baseLog.info(LogFormatUtil.getActionFormat("定时任务清理下线成员开始"));
//        List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.ZL_IDS);
//        for (int i = 0; i < listJson.size(); i++) {
//            String str[] = listJson.get(i).split("-");
//            Long onLine = new Date().getTime() - Long.parseLong(str[0]);
//            if (onLine > 10000) {
//                redisManager.deleteFromHashByStoreKeyAndMapKey(RedisKeyConstant.ZL_IDS, str[1]);
//            }
//        }
//        baseLog.info(LogFormatUtil.getActionFormat("定时任务清理下线成员结束"));
//    }


    //VIP定时任务
    @Scheduled(cron = "0/15 * *  * * ? ")   //每15秒执行一次
    public void clearVIPOutLineUserCron() {
        baseLog.info(LogFormatUtil.getActionFormat("定时任务清理下线VIP开始"));
        List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.VIP_IDS);
        for (int i = 0; i < listJson.size(); i++) {
            String str[] = listJson.get(i).split("-");
            Long onLine = new Date().getTime() - Long.parseLong(str[0]);
            if (onLine > 10000) {
                redisManager.deleteFromHashByStoreKeyAndMapKey(RedisKeyConstant.VIP_IDS, str[1]);
            }
        }
        baseLog.info(LogFormatUtil.getActionFormat("定时任务清理下线VIP结束"));
    }

}

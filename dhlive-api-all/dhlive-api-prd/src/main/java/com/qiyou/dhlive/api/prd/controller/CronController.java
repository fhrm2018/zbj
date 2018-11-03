package com.qiyou.dhlive.api.prd.controller;

import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.yaozhong.framework.base.common.utils.DateStyle;
import com.yaozhong.framework.base.common.utils.DateUtil;
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
    private IBaseCacheService baseCacheService;
    
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

    
    @Scheduled(cron = "0 0/25 * * * ? ") 
    public void addAutoPersonCountFirst() {
    	String beginDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"08:50:00";
    	String endDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"17:50:00";
    	Date beginDate=DateUtil.StringToDate(beginDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	Date endDate=DateUtil.StringToDate(endDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	
    	boolean beginRes=DateUtil.checkLessTime(beginDate, new Date());
    	boolean endRes=DateUtil.checkLessTime(endDate, new Date());
    	if(!beginRes) {
    		return;
    	}
    	if(endRes) {
    		return ;
    	}
    	
    	int count = (int)(100+Math.random()*(120-100+1));
    	baseCacheService.updateAutoPersonCount(count);
    	
    }
    
    @Scheduled(cron = "0 0/25 * * * ? ")
    public void addAutoPersonCountSecond() {
    	String beginDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"18:20:00";
    	String endDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"21:50:00";
    	Date beginDate=DateUtil.StringToDate(beginDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	Date endDate=DateUtil.StringToDate(endDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	
    	boolean beginRes=DateUtil.checkLessTime(beginDate, new Date());
    	boolean endRes=DateUtil.checkLessTime(endDate, new Date());
    	if(!beginRes) {
    		return;
    	}
    	if(endRes) {
    		return ;
    	}
    	
    	int count = (int)(80+Math.random()*(100-80+1));
    	baseCacheService.updateAutoPersonCount(count);
    	
    }
    
    @Scheduled(cron = "0 0/25 * * * ? ")
    public void addAutoPersonCountThird() {
    	String beginDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"22:20:00";
    	String endDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"23:59:59";
    	Date beginDate=DateUtil.StringToDate(beginDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	Date endDate=DateUtil.StringToDate(endDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	
    	boolean beginRes=DateUtil.checkLessTime(beginDate, new Date());
    	boolean endRes=DateUtil.checkLessTime(endDate, new Date());
    	if(!beginRes) {
    		return;
    	}
    	if(endRes) {
    		return ;
    	}
    	
    	int count = (int)(0+Math.random()*(20-0+1));
    	baseCacheService.updateAutoPersonCount(count);
    	
    }
    
    @Scheduled(cron = "0 0/25 * * * ? ")
    public void addAutoPersonCountFourth() {
    	String beginDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"00:00:00";
    	String endDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"00:20:00";
    	Date beginDate=DateUtil.StringToDate(beginDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	Date endDate=DateUtil.StringToDate(endDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	
    	boolean beginRes=DateUtil.checkLessTime(beginDate, new Date());
    	boolean endRes=DateUtil.checkLessTime(endDate, new Date());
    	if(!beginRes) {
    		return;
    	}
    	if(endRes) {
    		return ;
    	}
    	
    	
    	int count = (int)(0+Math.random()*(20-0+1));
    	baseCacheService.updateAutoPersonCount(count);
    	
    }
    
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void subAutoPersonCountFirst() {
    	String beginDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"00:25:00";
    	String endDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"02:00:00";
    	Date beginDate=DateUtil.StringToDate(beginDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	Date endDate=DateUtil.StringToDate(endDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	boolean beginRes=DateUtil.checkLessTime(beginDate, new Date());
    	boolean endRes=DateUtil.checkLessTime(endDate, new Date());
    	if(!beginRes) {
    		return;
    	}
    	if(endRes) {
    		return ;
    	}
    	
    	int count = (int)(70+Math.random()*(100-70+1));
    	int a=this.baseCacheService.getAutoPersonCount();
    	if(a==0)
    		return ;
    	baseCacheService.updateAutoPersonCount(0-count);
    }
    
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void subAutoPersonCountThird() {
    	String beginDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"02:05:00";
    	String endDateStr=DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD)+" "+"04:00:00";
    	Date beginDate=DateUtil.StringToDate(beginDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	Date endDate=DateUtil.StringToDate(endDateStr,DateStyle.YYYY_MM_DD_HH_MM_SS);
    	
    	boolean beginRes=DateUtil.checkLessTime(beginDate, new Date());
    	boolean endRes=DateUtil.checkLessTime(endDate, new Date());
    	if(!beginRes) {
    		return;
    	}
    	if(endRes) {
    		return ;
    	}
    	
    	int count = (int)(200+Math.random()*(250-200+1));
    	int a=this.baseCacheService.getAutoPersonCount();
    	if(a==0)
    		return ;
    	baseCacheService.updateAutoPersonCount(0-count);
    }
}

package com.qiyou.dhlive.api.base.service.service.impl;

import com.qiyou.dhlive.api.base.outward.service.ICheckIpVisitService;
import com.qiyou.dhlive.core.base.outward.model.BaseIp;
import com.qiyou.dhlive.core.base.outward.service.IBaseIpService;
import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.room.outward.model.RoomAnnouncement;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.page.builders.PageResultBuilder;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * ${DESCRIPTION}
 *
 * @author fish
 * @create 2018-01-20 20:59
 **/
@Service
public class CheckIpVisitServiceImpl implements ICheckIpVisitService {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    private static int limitCount = 20;

    @Autowired
    private IBaseSysParamService baseSysParamService;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    @Override
    public boolean checkCanVisit(String ip, String url) {
    	if(EmptyUtil.isNotEmpty(ip)) {
    		String[] balck=ip.split(".");
    		if("117".equals(balck[0])) {
    			return false;
    		}
    	}
    	
        String whiteip = redisManager.getStringValueByKey(RedisKeyConstant.REIDSKEY_WHITEIP + ip);
        if (EmptyUtil.isNotEmpty(whiteip)) {
            return true;
        }
        String blackUrl = redisManager.getStringValueByKey(RedisKeyConstant.REIDSKEY_BLACKIP + ip);
        if (EmptyUtil.isNotEmpty(blackUrl)) {
            return false;
        }
        if (EmptyUtil.isNotEmpty(ip)) {
            String countStr = redisManager.getStringValueByKey(RedisKeyConstant.REIDSKEY_IP_COUNT + ip);
            if (EmptyUtil.isNotEmpty(countStr)) {
                int count = Integer.parseInt(countStr);
                if (count >= limitCount) {
                    redisManager.saveStringBySeconds(RedisKeyConstant.REIDSKEY_BLACKIP + ip, ip, 300);
                    baseLog.info(LogFormatUtil.getActionFormat("系统自动拉黑的用户ip:" + ip + ",url:" + url));
                    return false;
                } else {
                    redisManager.saveStringBySeconds(RedisKeyConstant.REIDSKEY_IP_COUNT + ip, count + 1 + "", 1);
                }
            } else {
                redisManager.saveStringBySeconds(RedisKeyConstant.REIDSKEY_IP_COUNT + ip, "1", 1);
            }
        }
        return true;
    }

    /**
     * 加入黑/白名单
     *
     * @param params
     * @return
     */
    @Override
    public DataResponse saveIpBlackOrWhiteList(BaseIp params) {
        String ips[] = params.getIp().split(",");
        for (int i = 0; i < ips.length; i++) {
            if (params.getType().intValue() == 1) {
                this.redisManager.saveString(RedisKeyConstant.REIDSKEY_WHITEIP + ips[i].trim(), ips[i].trim());
            } else {
                this.redisManager.saveString(RedisKeyConstant.REIDSKEY_BLACKIP + ips[i].trim(), ips[i].trim());
            }
        }
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse getIpList(BaseIp params) {
        List<BaseIp> ips = new ArrayList<BaseIp>();
        String host = this.baseSysParamService.getValueByKey("host.redis.ip");
        String port = this.baseSysParamService.getValueByKey("host.redis.port");
        Jedis jedis = new Jedis(host, Integer.parseInt(port));
        String auth = this.baseSysParamService.getValueByKey("host.redis.auth");
        if (EmptyUtil.isNotEmpty(auth)) {
            jedis.auth(auth);
        }
        Set<String> blackKeys = jedis.keys("dhlive-cachedata-blackip-*");
        Set<String> whiteKeys = jedis.keys("dhlive-cachedata-whiteip-*");

        if (EmptyUtil.isEmpty(params.getType())) {//所有数据
            for (String key : blackKeys) {
                BaseIp record = new BaseIp();
                record.setType(0);
                record.setIp(this.redisManager.getStringValueByKey(key));
                ips.add(record);
            }
            for (String key : whiteKeys) {
                BaseIp record = new BaseIp();
                record.setType(1);
                record.setIp(this.redisManager.getStringValueByKey(key));
                ips.add(record);
            }
        } else if (params.getType().intValue() == 0) {//黑名单
            for (String key : blackKeys) {
                BaseIp record = new BaseIp();
                record.setType(0);
                record.setIp(this.redisManager.getStringValueByKey(key));
                ips.add(record);
            }
        } else {//白名单
            for (String key : whiteKeys) {
                BaseIp record = new BaseIp();
                record.setType(1);
                record.setIp(this.redisManager.getStringValueByKey(key));
                ips.add(record);
            }
        }

        //查询条件
        if (EmptyUtil.isNotEmpty(params.getIp())) {
            List<BaseIp> data = new ArrayList<BaseIp>();
            for (int i = 0; i < ips.size(); i++) {
                if (params.getIp().equals(ips.get(i).getIp())) {
                    data.add(ips.get(i));
                }
            }
            PageResult<BaseIp> result = new PageResultBuilder<BaseIp>().buildPageData(Integer.parseInt(data.size() + ""), data).toPageResult();
            return new DataResponse(1000, result);
        }else{
            PageResult<BaseIp> result = new PageResultBuilder<BaseIp>().buildPageData(Integer.parseInt(ips.size() + ""), ips).toPageResult();
            return new DataResponse(1000, result);
        }
    }

    @Override
    public DataResponse delIp(BaseIp params) {
        if (params.getType().intValue() == 0) {
            this.redisManager.delete(RedisKeyConstant.REIDSKEY_BLACKIP + params.getIp());
        } else {
            this.redisManager.delete(RedisKeyConstant.REIDSKEY_WHITEIP + params.getIp());
        }
        return new DataResponse(1000, "success");
    }
}

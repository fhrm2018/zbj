package com.qiyou.dhlive.core.base.service.service.impl;

import com.qiyou.dhlive.core.base.outward.model.BaseSysParam;
import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.base.service.dao.BaseSysParamMapper;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-20
 */
@Service
public class BaseSysParamServiceImpl extends BaseMyBatisService<BaseSysParam> implements IBaseSysParamService {

    @Autowired
    private BaseSysParamMapper mapper;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    //redis 缓存时长
    private static final int CACHE_TIME = 2 * 60 * 60;

    public BaseSysParamServiceImpl() {
        super.setEntityClazz(BaseSysParam.class);
    }

    @Override
    public String getValueByKey(String key) {
        String value = this.redisManager.getStringValueByKey(RedisKeyConstant.INDEX_KEY + key);
        if (EmptyUtil.isEmpty(value)) {
            BaseSysParam params = new BaseSysParam();
            params.setParamKey(key);
            List<BaseSysParam> list = this.findByCondition(new SearchCondition<BaseSysParam>(params));
            if (EmptyUtil.isNotEmpty(list)) {
                value = list.get(0).getParamValue();
                if (EmptyUtil.isNotEmpty(value))
                    this.redisManager.saveStringBySeconds(RedisKeyConstant.INDEX_KEY + key, value, CACHE_TIME);
            }
        }
        return value;
    }

    @Override
    public String updateValueByKey(String key) {
        String value = this.redisManager.getStringValueByKey(RedisKeyConstant.INDEX_KEY + key);
        if (EmptyUtil.isNotEmpty(value)) {
            redisManager.delete(RedisKeyConstant.INDEX_KEY + key);
        }
        BaseSysParam params = new BaseSysParam();
        params.setParamKey(key);
        List<BaseSysParam> list = this.findByCondition(new SearchCondition<BaseSysParam>(params));
        if (EmptyUtil.isNotEmpty(list)) {
            value = list.get(0).getParamValue();
            this.redisManager.saveStringBySeconds(RedisKeyConstant.INDEX_KEY + key, value, CACHE_TIME);
        }
        return value;
    }

}
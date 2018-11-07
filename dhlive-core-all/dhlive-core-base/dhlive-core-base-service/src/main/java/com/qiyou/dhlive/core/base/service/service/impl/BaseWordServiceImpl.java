package com.qiyou.dhlive.core.base.service.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.qiyou.dhlive.core.base.outward.model.BaseWord;
import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.core.base.outward.service.IBaseWordService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.base.service.dao.BaseWordMapper;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.builders.PageResultBuilder;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import com.yaozhong.framework.base.database.redis.RedisManager;

import redis.clients.jedis.Jedis;

/**
 * describe:
 *
 * @author fish
 * @date 2018/02/28
 */
@Service
public class BaseWordServiceImpl extends BaseMyBatisService<BaseWord> implements IBaseWordService {

    @Autowired
    private IBaseSysParamService baseSysParamService;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    @Autowired
    private BaseWordMapper mapper;

    public BaseWordServiceImpl() {
        super.setEntityClazz(BaseWord.class);
    }

    @Override
    public DataResponse saveWordBlackOrWhiteList(BaseWord params) {
        String words[] = params.getWord().split(",");
        for (int i = 0; i < words.length; i++) {
            if (params.getType().intValue() == 1) {
                this.redisManager.saveString(RedisKeyConstant.REIDSKEY_WHITE_WORD + words[i].trim(), words[i].trim());
            } else {
                this.redisManager.saveString(RedisKeyConstant.REIDSKEY_BLACK_WORD + words[i].trim(), words[i].trim());
            }
        }
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse getWordList(BaseWord params) {
        List<BaseWord> words = new ArrayList<BaseWord>();
        String host = this.baseSysParamService.getValueByKey("host.redis.ip");
        String port = this.baseSysParamService.getValueByKey("host.redis.port");
        Jedis jedis = new Jedis(host, Integer.parseInt(port));
        String auth = this.baseSysParamService.getValueByKey("host.redis.auth");
        if (EmptyUtil.isNotEmpty(auth)) {
            jedis.auth(auth);
        }
        Set<String> blackKeys = jedis.keys("dhlive-cachedata-black-word-*");
        Set<String> whiteKeys = jedis.keys("dhlive-cachedata-white-word-*");

        if (EmptyUtil.isEmpty(params.getType())) {//所有数据
            for (String key : blackKeys) {
                BaseWord record = new BaseWord();
                record.setType(0);
                record.setWord(this.redisManager.getStringValueByKey(key));
                words.add(record);
            }
            for (String key : whiteKeys) {
                BaseWord record = new BaseWord();
                record.setType(1);
                record.setWord(this.redisManager.getStringValueByKey(key));
                words.add(record);
            }
        } else if (params.getType().intValue() == 0) {//黑名单
            for (String key : blackKeys) {
                BaseWord record = new BaseWord();
                record.setType(0);
                record.setWord(this.redisManager.getStringValueByKey(key));
                words.add(record);
            }
        } else {//白名单
            for (String key : whiteKeys) {
                BaseWord record = new BaseWord();
                record.setType(1);
                record.setWord(this.redisManager.getStringValueByKey(key));
                words.add(record);
            }
        }

        //查询条件
        if (EmptyUtil.isNotEmpty(params.getWord())) {
            List<BaseWord> data = new ArrayList<BaseWord>();
            for (int i = 0; i < words.size(); i++) {
                if (params.getWord().equals(words.get(i).getWord())) {
                    data.add(words.get(i));
                }
            }
            PageResult<BaseWord> result = new PageResultBuilder<BaseWord>().buildPageData(Integer.parseInt(data.size() + ""), data).toPageResult();
            return new DataResponse(1000, result);
        } else {
            PageResult<BaseWord> result = new PageResultBuilder<BaseWord>().buildPageData(Integer.parseInt(words.size() + ""), words).toPageResult();
            return new DataResponse(1000, result);
        }
    }

    @Override
    public DataResponse delWord(BaseWord params) {
        if (params.getType().intValue() == 0) {
            this.redisManager.delete(RedisKeyConstant.REIDSKEY_BLACK_WORD + params.getWord());
        } else {
            this.redisManager.delete(RedisKeyConstant.REIDSKEY_WHITE_WORD + params.getWord());
        }
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse checkMsg(BaseWord params) {
        String host = this.baseSysParamService.getValueByKey("host.redis.ip");
        String port = this.baseSysParamService.getValueByKey("host.redis.port");
        Jedis jedis = new Jedis(host, Integer.parseInt(port));
        String auth = this.baseSysParamService.getValueByKey("host.redis.auth");
        if (EmptyUtil.isNotEmpty(auth)) {
            jedis.auth(auth);
        }
        Set<String> whiteKeys = jedis.keys("dhlive-cachedata-white-word-*");
        Set<String> blackKeys = jedis.keys("dhlive-cachedata-black-word-*");
        try {
            for (String key : whiteKeys) {
                String word = this.redisManager.getStringValueByKey(key);
                if (params.getWord().trim().equals(word.trim())) {
                	return new DataResponse(1000, 1);//在白名单,不需要审核
                }
            }

            for (String key : blackKeys) {
                String word = this.redisManager.getStringValueByKey(key);
                if (params.getWord().trim().contains(word.trim())) {
                	return new DataResponse(1000, 0);//在黑名单, 不能发送
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jedis.close();
        return new DataResponse(1000, 2);//正常消息, 需要审核
    }
}

package com.qiyou.dhlive.core.base.outward.service;

import com.qiyou.dhlive.core.base.outward.model.BaseWord;
import com.yaozhong.framework.base.database.base.service.IBaseService;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;

/**
 * describe:
 *
 * @author fish
 * @date 2018/02/28
 */
public interface IBaseWordService extends IBaseService<BaseWord> {

    DataResponse saveWordBlackOrWhiteList(BaseWord params);

    DataResponse getWordList(BaseWord params);

    DataResponse delWord(BaseWord params);

    DataResponse checkMsg(BaseWord params);

}

package com.qiyou.dhlive.core.user.outward.service;

import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.yaozhong.framework.base.database.base.service.IBaseService;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-20
 */
public interface IUserInfoService extends IBaseService<UserInfo> {

    public UserInfo createNewGuestUser(String ipAddress, String utmSource);

    /*
     * 从缓存中获取所有客服
     */
    @Cacheable(value = "tenCache", key = "'getWaiterListJson'")
    public String getWaiterListJson();

    /*
     * 从缓存中获取所有客服
     */
    @Cacheable(value = "tenCache", key = "'getAdminListJson'")
    public String getAdminListJson();

    /*
     * 从缓存中获取在线用户
     */
    @Cacheable(value = "tenCache", key = "'getOnlineListJson'")
    public String getOnlineListJson();

    public PageResult<UserInfo> findUserByPage(PageSearch pageSearch, UserInfo user);

    /**
     * 查找大于指定userType的用户
     */
    public List<UserInfo> findUserListByGtUserType(Integer userType);

}
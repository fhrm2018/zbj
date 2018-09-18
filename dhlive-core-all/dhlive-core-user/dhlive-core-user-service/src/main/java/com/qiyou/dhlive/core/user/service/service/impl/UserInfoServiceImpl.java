package com.qiyou.dhlive.core.user.service.service.impl;

import com.qiyou.dhlive.core.user.outward.model.UserGroup;
import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserGroupService;
import com.qiyou.dhlive.core.user.outward.service.IUserInfoService;
import com.qiyou.dhlive.core.user.service.dao.UserInfoMapper;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import org.springframework.stereotype.Service;
import com.yaozhong.framework.base.database.mysql.service.impl.BaseMyBatisService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author liuyuanhang
 * @version 1.0.0
 * @date 2018-01-20
 */
@Service
public class UserInfoServiceImpl extends BaseMyBatisService<UserInfo> implements IUserInfoService {

    @Autowired
    private UserInfoMapper mapper;

    @Autowired
    private IUserGroupService userGroupService;

    public UserInfoServiceImpl() {
        super.setEntityClazz(UserInfo.class);
    }

    @Override
    public UserInfo createNewGuestUser(String ipAddress, String utmSource) {
        UserGroup params = new UserGroup();
        params.setName("游客");
        SearchCondition<UserGroup> condition = new SearchCondition<UserGroup>(params);
        List<UserGroup> list = this.userGroupService.findByCondition(condition);
        UserGroup userGroup = null;
        if (EmptyUtil.isNotEmpty(list)) {
            userGroup = list.get(0);
        }
        UserInfo user = new UserInfo();
        user.setUserNickName("游客MY" + getRandomString(6));
        user.setIsFirstLogin(1);
        user.setLastLoginTime(new Date());
        user.setLastLoginIp(ipAddress);
        user.setLookTime(0);
        if (EmptyUtil.isNotEmpty(userGroup)) {
            user.setGroupId(userGroup.getId());
            user.setGroupName(userGroup.getName());
        } else {
            user.setGroupId(null);
        }
        if (EmptyUtil.isNotEmpty(utmSource)) {
            user.setUtmSource(utmSource);
        }
        user.setCreateTime(new Date());
        this.save(user);
        return user;
    }

    public static String getRandomString(long length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    @Override
    public String getWaiterListJson() {
        return null;
    }

    @Override
    public String getAdminListJson() {
        return null;
    }

    @Override
    public String getOnlineListJson() {
        return null;
    }

    @Override
    public PageResult<UserInfo> findUserByPage(PageSearch pageSearch, UserInfo user) {
        return null;
    }

    @Override
    public List<UserInfo> findUserListByGtUserType(Integer userType) {
        return null;
    }
}
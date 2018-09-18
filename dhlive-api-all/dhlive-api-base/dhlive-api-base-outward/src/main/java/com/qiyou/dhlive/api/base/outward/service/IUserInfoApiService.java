package com.qiyou.dhlive.api.base.outward.service;

import com.qiyou.dhlive.core.bms.outward.model.BmsEmployeeInfo;
import com.qiyou.dhlive.core.user.outward.model.*;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/23
 */
public interface IUserInfoApiService {

    DataResponse getTouristsUserList(PageSearch pageSearch, UserInfo params);

    DataResponse getVipUserList(PageSearch pageSearch, UserVipInfo params);

    DataResponse getVipUserList(UserVipInfo params);

    DataResponse getManageUserList(PageSearch pageSearch, UserManageInfo params);

    DataResponse getManageUser(UserManageInfo params);

    DataResponse getEmployeeUserList(PageSearch pageSearch, BmsEmployeeInfo params);

    List<UserGroup> getUserGroup(UserGroup params, Object[] ids);

    DataResponse saveManageUser(UserManageInfo params);

    DataResponse saveSmallUser(UserSmallInfo params);

    DataResponse getSmallList(UserSmallInfo params);

    DataResponse getUserSmall(UserSmallInfo params);

    DataResponse deleteManageUser(UserManageInfo params);

    DataResponse deleteSmall(UserSmallInfo params);

    DataResponse saveEmployee(BmsEmployeeInfo params);

    DataResponse getEmployeeUser(BmsEmployeeInfo params);

    DataResponse deleteEmployeeUser(BmsEmployeeInfo params);

    DataResponse resetPassWord(BmsEmployeeInfo params);

    DataResponse saveVipUser(HttpServletRequest request, UserVipInfo params);

    DataResponse setTouristsGag(UserInfo params);

    DataResponse setVipGag(UserVipInfo params);

    DataResponse setTouristsBlack(UserInfo params);

    DataResponse setVipBlack(UserVipInfo params);

    List<UserManageInfo> getAssistantList(Integer roomId);

    List<UserManageInfo> getTeacherList(Integer roomId);

    List<UserSmallInfo> getUserSmallList(Integer roomId, Integer userId);

    DataResponse checkPhone(String phone);
}

package com.qiyou.dhlive.api.base.service.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.qiyou.dhlive.api.base.outward.service.IUserInfoApiService;
import com.qiyou.dhlive.api.base.outward.util.HttpUtil;
import com.qiyou.dhlive.api.base.outward.util.NoticeUtil;
import com.qiyou.dhlive.api.base.outward.util.TLSUtils;
import com.qiyou.dhlive.api.base.outward.vo.AVChatRoomVO;
import com.qiyou.dhlive.api.base.outward.vo.VipUserVO;
import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.bms.outward.model.BmsEmployeeInfo;
import com.qiyou.dhlive.core.bms.outward.service.IBmsEmployeeInfoService;
import com.qiyou.dhlive.core.live.outward.model.LiveRoom;
import com.qiyou.dhlive.core.live.outward.service.ILiveRoomService;
import com.qiyou.dhlive.core.user.outward.model.UserGroup;
import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.model.UserSmallInfo;
import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserGroupService;
import com.qiyou.dhlive.core.user.outward.service.IUserInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserManageInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserSmallInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserVipInfoService;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.common.utils.MD5Util;
import com.yaozhong.framework.base.common.utils.MyBeanUtils;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.page.builders.PageResultBuilder;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/23
 */
@Service
public class UserInfoApiServiceImpl implements IUserInfoApiService {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IBaseSysParamService baseSysParamService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserVipInfoService userVipInfoService;

    @Autowired
    private IUserManageInfoService userManageInfoService;

    @Autowired
    private IBmsEmployeeInfoService employeeInfoService;

    @Autowired
    private IUserGroupService userGroupService;

    @Autowired
    private IUserSmallInfoService userSmallInfoService;

    @Autowired
    private ILiveRoomService liveRoomService;


    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    /**
     * 获取游客列表
     *
     * @param pageSearch 分页参数
     * @param params     查询条件
     * @return
     */
    @Override
    public DataResponse getTouristsUserList(PageSearch pageSearch, UserInfo params) {
    	List<Object> userIdList = Lists.newArrayList();
    	if(EmptyUtil.isNotEmpty(params.getIsOnline())) {
    		params.setIsOnline(null);
        	Set<String> listJson = redisManager.getMapKeyFromMapByStoreKey(RedisKeyConstant.YK_IDS);
        	for(String key:listJson) {
        		if("null".equalsIgnoreCase(key)) {
        			continue;
        		}
        		userIdList.add(Integer.parseInt(key));
        	}
        	if(EmptyUtil.isEmpty(userIdList)) {
        		PageResult<VipUserVO> result = new PageResultBuilder<VipUserVO>().buildPageData(0, new ArrayList<VipUserVO>()).toPageResult();
                return new DataResponse(1000, result);
        	}
        }
        SearchCondition<UserInfo> condition = new SearchCondition<UserInfo>(params, pageSearch);
        condition.buildOrderByConditions("createTime", "desc");
        if(EmptyUtil.isNotEmpty(userIdList)) {
        	condition.buildInConditions("userId", userIdList);
        }
        PageResult<UserInfo> result = this.userInfoService.findByPage(condition);
        
        return new DataResponse(1000, result);
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public DataResponse getVipUserList(PageSearch pageSearch, UserVipInfo params) {
    	UserVipInfo conParam = new UserVipInfo();
    	
    	if(EmptyUtil.isNotEmpty(params.getUserTel())) {
    		conParam.setUserTel(params.getUserTel());;
        }
    	if(EmptyUtil.isNotEmpty(params.getLastLoginIp())) {
    		conParam.setLastLoginIp(params.getLastLoginIp());;
        }
    	if(EmptyUtil.isNotEmpty(params.getIsBlack())) {
    		conParam.setIsBlack(params.getIsBlack());
        }
    	if(EmptyUtil.isNotEmpty(params.getIsGag())) {
    		conParam.setIsGag(params.getIsGag());
        }
    	List<Object> userIdList = Lists.newArrayList();
    	if(EmptyUtil.isNotEmpty(params.getIsOnline())) {
    		params.setIsOnline(null);
        	Set<String> listJson = redisManager.getMapKeyFromMapByStoreKey(RedisKeyConstant.VIP_IDS);
        	for(String key:listJson) {
        		if("null".equalsIgnoreCase(key)) {
        			continue;
        		}
        		userIdList.add(Integer.parseInt(key));
        	}
        	if(EmptyUtil.isEmpty(userIdList)) {
        		PageResult<VipUserVO> result = new PageResultBuilder<VipUserVO>().buildPageData(0, new ArrayList<VipUserVO>()).toPageResult();
                return new DataResponse(1000, result);
        	}
        }
    	
    	SearchCondition<UserVipInfo> condition = new SearchCondition<UserVipInfo>(conParam, pageSearch);
        if(EmptyUtil.isNotEmpty(params.getUserNickName())) {
        	condition.buildLikeConditions("userNickName", "%"+params.getUserNickName()+"%");
        }
        if(EmptyUtil.isNotEmpty(userIdList)) {
        	condition.buildInConditions("userId", userIdList);
        }
        condition.buildOrderByConditions("createTime", "desc");
        
        Long count = this.userVipInfoService.countByCondition(condition);
        //List<UserVipInfo> length = this.userVipInfoService.findByCondition(condition);
        PageResult<UserVipInfo> data = this.userVipInfoService.findByPage(condition);
        List<VipUserVO> list = new ArrayList<VipUserVO>();
        for (int i = 0; i < data.getRows().size(); i++) {
            VipUserVO record = MyBeanUtils.copyBean(data.getRows().get(i), VipUserVO.class);
            if (EmptyUtil.isNotEmpty(record.getCreateUserId())) {
               BmsEmployeeInfo e = this.employeeInfoService.findById(record.getCreateUserId());
                if(EmptyUtil.isNotEmpty(e)) {
                	String createUserName = e.getName();
                    record.setCreateUserName(createUserName);
                }
                list.add(record);
            }
        }
        PageResult<VipUserVO> result = new PageResultBuilder<VipUserVO>().buildPageData(count.intValue(), list).toPageResult();
        return new DataResponse(1000, result);
    }

    @Override
    public DataResponse getVipUserList(UserVipInfo params) {
        SearchCondition<UserVipInfo> condition = new SearchCondition<UserVipInfo>(params);
        condition.buildOrderByConditions("createTime", "desc");
        List<UserVipInfo> result = this.userVipInfoService.findByCondition(condition);
        return new DataResponse(1000, result);
    }

    @Override
    public DataResponse saveVipUser(HttpServletRequest request, UserVipInfo params) {
        //直播间信息
        LiveRoom room = this.liveRoomService.findById(params.getRoomId());
        if (EmptyUtil.isNotEmpty(room)) {
            params.setRoomName(room.getRoomName());
        }
        //角色信息
        UserGroup group = this.userGroupService.findById(params.getGroupId());
        if (EmptyUtil.isNotEmpty(group)) {
            params.setGroupName(group.getName());
        }
        if (EmptyUtil.isNotEmpty(params.getUserPass())) {
            params.setUserPass(MD5Util.MD5Encode(params.getUserPass(), "utf-8"));
        }
        params.setCreateTime(new Date());
        //保存和修改共用一个方法, 如果userid不为空表示是修改操作
        if (EmptyUtil.isEmpty(params.getUserId())) {
            //检查手机号是否重复
            DataResponse check = checkPhone(params.getUserTel());
            if (check.getCode().intValue() != 1000) {
                return check;
            }
//            params.setLastLoginIp(AddressUtils.getIpAddrFromRequest(request));
            params.setLastLoginTime(new Date());
            this.userVipInfoService.save(params);
        } else {
            if (EmptyUtil.isEmpty(params.getUserPass())) {
                params.setUserPass(null);
            }
            this.userVipInfoService.modifyEntity(params);
            params = this.userVipInfoService.findById(params.getUserId());
            this.redisManager.saveString(RedisKeyConstant.VIP + params.getUserId(), new Gson().toJson(params));
        }
        return new DataResponse(1000, "success");
    }

    /**
     * 设置游客禁言/解禁
     *
     * @param params
     * @return
     */
    @Override
    public DataResponse setTouristsGag(UserInfo params) {
        //腾讯禁言
        long time = 0;//0表示取消禁言
        if (params.getIsGag().intValue() == 1) {
            time = 4294967295L;//永久禁言
        }
        String sdkAppId = this.baseSysParamService.getValueByKey("sdk_app_id");
        String identifier = this.baseSysParamService.getValueByKey("identifier");
        String privateKey = this.baseSysParamService.getValueByKey("private_key");
        String userSig = TLSUtils.getUserSig(Integer.parseInt(sdkAppId), identifier, privateKey);
        Integer random = TLSUtils.getRandom();
        String method = "POST";
        String url = "https://console.tim.qq.com/v4/openconfigsvr/setnospeaking?usersig=" + userSig + "&identifier=" + identifier + "&sdkappid=" + sdkAppId + "&random=" + random + "&contenttype=json";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Set_Account", "yk-" + params.getUserId());
        map.put("GroupmsgNospeakingTime", time);
        String result = HttpUtil.httpRequest(url, method, new Gson().toJson(map));
        AVChatRoomVO vo = new Gson().fromJson(result, AVChatRoomVO.class);
        baseLog.info(LogFormatUtil.getActionFormat("禁言返回结果:") + new Gson().toJson(vo));
        if (vo.getErrorCode().intValue() == 0) {
            //发送消息通知
            result = NoticeUtil.sendSetGagNotic(identifier, "yk-" + params.getUserId(), userSig, identifier, sdkAppId, time);
            if (vo.getErrorCode().intValue() == 0) {
                //本地表设置禁言状态
                this.userInfoService.modifyEntity(params);
                //缓存操作
                params = this.userInfoService.findById(params.getUserId());
                this.redisManager.saveStringBySeconds(RedisKeyConstant.TOURISTS + params.getUserId(), new Gson().toJson(params));
                return new DataResponse(1000, "禁言成功!");
            } else {
                params = this.userInfoService.findById(params.getUserId());
                this.redisManager.delete(RedisKeyConstant.TOURISTS + params.getUserId());
                return new DataResponse(1001, vo.getErrorInfo());
            }
        } else {
            return new DataResponse(1001, vo.getErrorInfo());
        }
    }


    @Override
    public DataResponse setVipGag(UserVipInfo params) {
        //腾讯禁言
        long time = 0;//0表示取消禁言
        if (params.getIsGag().intValue() == 1) {
            time = 4294967295L;//永久禁言
        }
        String sdkAppId = this.baseSysParamService.getValueByKey("sdk_app_id");
        String identifier = this.baseSysParamService.getValueByKey("identifier");
        String privateKey = this.baseSysParamService.getValueByKey("private_key");
        String userSig = TLSUtils.getUserSig(Integer.parseInt(sdkAppId), identifier, privateKey);
        Integer random = TLSUtils.getRandom();
        String method = "POST";
        String url = "https://console.tim.qq.com/v4/openconfigsvr/setnospeaking?usersig=" + userSig + "&identifier=" + identifier + "&sdkappid=" + sdkAppId + "&random=" + random + "&contenttype=json";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Set_Account", "vip-" + params.getUserId());
        map.put("GroupmsgNospeakingTime", time);
        String result = HttpUtil.httpRequest(url, method, new Gson().toJson(map));
        AVChatRoomVO vo = new Gson().fromJson(result, AVChatRoomVO.class);
        if (vo.getErrorCode().intValue() == 0) {
            //发送消息通知
            result = NoticeUtil.sendSetGagNotic(identifier, "vip-" + params.getUserId(), userSig, identifier, sdkAppId, time);
            if (vo.getErrorCode().intValue() == 0) {
                //本地表设置禁言状态
                this.userVipInfoService.modifyEntity(params);

                params = this.userVipInfoService.findById(params.getUserId());
                this.redisManager.saveString(RedisKeyConstant.VIP + params.getUserId(), new Gson().toJson(params));
                return new DataResponse(1000, "禁言成功!");
            } else {
                params = this.userVipInfoService.findById(params.getUserId());
                this.redisManager.delete(RedisKeyConstant.VIP + params.getUserId());
                return new DataResponse(1001, vo.getErrorInfo());
            }
        } else {
            return new DataResponse(1001, vo.getErrorInfo());
        }
    }

    @Override
    public DataResponse setTouristsBlack(UserInfo params) {
        String sdkAppId = this.baseSysParamService.getValueByKey("sdk_app_id");
        String identifier = this.baseSysParamService.getValueByKey("identifier");
        String privateKey = this.baseSysParamService.getValueByKey("private_key");
        String userSig = TLSUtils.getUserSig(Integer.parseInt(sdkAppId), identifier, privateKey);
        Integer black = 0;//0表示取消拉黑
        if (params.getIsBlack().intValue() == 1) {
            black = 1;//拉黑标记
        }
        //发送通知消息
        String result = NoticeUtil.sendSetBlackNotic(identifier, "yk-" + params.getUserId(), userSig, identifier, sdkAppId, black);
        AVChatRoomVO vo = new Gson().fromJson(result, AVChatRoomVO.class);
//        if (vo.getErrorCode().intValue() == 0) {
        //用户表拉黑/取消拉黑
        this.userInfoService.modifyEntity(params);
        params = this.userInfoService.findById(params.getUserId());
        if (black == 1) {
//            //ip加入黑名单表
//            BaseIp ip = new BaseIp();
//            ip.setIp(params.getLastLoginIp());
//            ip.setType(0);//黑名单
//            ip.setCreateTime(new Date());
//            this.baseIpService.save(ip);
            this.redisManager.saveStringBySeconds(RedisKeyConstant.TOURISTS + params.getUserId(), new Gson().toJson(params));
            this.redisManager.saveString(RedisKeyConstant.REIDSKEY_BLACKIP + params.getLastLoginIp(), params.getLastLoginIp());
        } else {
//            BaseIp ip = new BaseIp();
//            ip.setIp(params.getLastLoginIp());
//            SearchCondition<BaseIp> condition = new SearchCondition<BaseIp>(ip);
//            this.baseIpService.removeByCondition(condition);
            this.redisManager.delete(RedisKeyConstant.TOURISTS + params.getUserId());
            this.redisManager.delete(RedisKeyConstant.REIDSKEY_BLACKIP + params.getLastLoginIp());
        }
        return new DataResponse(1000, "SUCCESS");
//        } else {
//            return new DataResponse(1001, vo.getErrorInfo());
//        }
    }


    @Override
    public DataResponse setVipBlack(UserVipInfo params) {
        String sdkAppId = this.baseSysParamService.getValueByKey("sdk_app_id");
        String identifier = this.baseSysParamService.getValueByKey("identifier");
        String privateKey = this.baseSysParamService.getValueByKey("private_key");
        String userSig = TLSUtils.getUserSig(Integer.parseInt(sdkAppId), identifier, privateKey);
        Integer black = 0;//0表示取消拉黑
        if (params.getIsBlack().intValue() == 1) {
            black = 1;//拉黑标记
        }
        //发送通知消息
        String result = NoticeUtil.sendSetBlackNotic(identifier, "vip-" + params.getUserId(), userSig, identifier, sdkAppId, black);
        AVChatRoomVO vo = new Gson().fromJson(result, AVChatRoomVO.class);
        if (vo.getErrorCode().intValue() == 0) {
            //用户表拉黑
            this.userVipInfoService.modifyEntity(params);
            params = this.userVipInfoService.findById(params.getUserId());
            if (black == 1) {
//                //ip加入黑名单表
//                BaseIp ip = new BaseIp();
//                ip.setIp(params.getLastLoginIp());
//                ip.setType(0);//黑名单
//                ip.setCreateTime(new Date());
//                this.baseIpService.save(ip);
                this.redisManager.saveString(RedisKeyConstant.VIP + params.getUserId(), new Gson().toJson(params));
                this.redisManager.saveString(RedisKeyConstant.REIDSKEY_BLACKIP + params.getLastLoginIp(), params.getLastLoginIp());
            } else {
//                BaseIp ip = new BaseIp();
//                ip.setIp(params.getLastLoginIp());
//                SearchCondition<BaseIp> condition = new SearchCondition<BaseIp>(ip);
//                this.baseIpService.removeByCondition(condition);
                this.redisManager.delete(RedisKeyConstant.VIP + params.getUserId());
                this.redisManager.delete(RedisKeyConstant.REIDSKEY_BLACKIP + params.getLastLoginIp());
            }
            return new DataResponse(1000, "SUCCESS");
        } else {
            return new DataResponse(1001, vo.getErrorInfo());
        }
    }

    @Override
    public List<UserManageInfo> getAssistantList(Integer roomId) {
        String value = this.redisManager.getStringValueByKey(RedisKeyConstant.ASSISTANT + roomId);
        if (EmptyUtil.isEmpty(value)) {
            UserManageInfo params = new UserManageInfo();
            params.setRoomId(roomId);
            params.setGroupId(3);//助理
            params.setStatus(0);//未删除
            params.setIsOnline(1);//在线的
            SearchCondition<UserManageInfo> condition = new SearchCondition<UserManageInfo>(params);
            List<UserManageInfo> assistant = this.userManageInfoService.findByCondition(condition);
            this.redisManager.saveString(RedisKeyConstant.ASSISTANT + roomId, JSONArray.toJSONString(assistant));
            return assistant;
        } else {
            List<UserManageInfo> assistant = JSONArray.parseArray(value, UserManageInfo.class);
            return assistant;
        }
    }

    @Override
    public List<UserManageInfo> getTeacherList(Integer roomId) {
        String value = this.redisManager.getStringValueByKey(RedisKeyConstant.TEACHER + roomId);
        if (EmptyUtil.isEmpty(value)) {
            UserManageInfo params = new UserManageInfo();
            params.setRoomId(roomId);
            params.setGroupId(4);//老师
            params.setStatus(0);//未删除
            SearchCondition<UserManageInfo> condition = new SearchCondition<UserManageInfo>(params);
            condition.buildOrderByConditions("praise", "desc");
            List<UserManageInfo> teacher = this.userManageInfoService.findByCondition(condition);
            this.redisManager.saveStringBySeconds(RedisKeyConstant.TEACHER + roomId, JSONArray.toJSONString(teacher));
            return teacher;
        } else {
            List<UserManageInfo> teacher = JSONArray.parseArray(value, UserManageInfo.class);
            return teacher;
        }
    }


    @Override
    public List<UserSmallInfo> getUserSmallList(Integer roomId, Integer userId) {
        UserSmallInfo params = new UserSmallInfo();
        params.setStatus(0);//正常状态
        params.setRoomId(roomId);
        params.setUserId(userId);
        SearchCondition<UserSmallInfo> condition = new SearchCondition<UserSmallInfo>(params);
        List<UserSmallInfo> smalls = this.userSmallInfoService.findByCondition(condition);
        return smalls;
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public DataResponse getManageUserList(PageSearch pageSearch, UserManageInfo params) {
        params.setStatus(0);//未删除用户标记
        SearchCondition<UserManageInfo> condition = new SearchCondition<UserManageInfo>(params, pageSearch);
        PageResult<UserManageInfo> result = this.userManageInfoService.findByPage(condition);
        return new DataResponse(1000, result);
    }

    @Override
    public DataResponse getManageUser(UserManageInfo params) {
        SearchCondition<UserManageInfo> condition = new SearchCondition<UserManageInfo>(params);
        List<UserManageInfo> result = this.userManageInfoService.findByCondition(condition);
        return new DataResponse(1000, result);
    }

    @Override
    public List<UserGroup> getUserGroup(UserGroup params, Object[] ids) {
        SearchCondition<UserGroup> condition = new SearchCondition<UserGroup>(params);
        if (EmptyUtil.isNotEmpty(ids)) {
            condition.buildInConditions("id", Arrays.asList(ids));
        }
        List<UserGroup> groups = this.userGroupService.findByCondition(condition);
        return groups;
    }

    @Override
    public DataResponse saveManageUser(UserManageInfo params) {
        //直播间信息
        LiveRoom room = this.liveRoomService.findById(params.getRoomId());
        if (EmptyUtil.isNotEmpty(room)) {
            params.setRoomName(room.getRoomName());
        }
        //角色信息
        UserGroup group = this.userGroupService.findById(params.getGroupId());
        if (EmptyUtil.isNotEmpty(group)) {
            params.setGroupName(group.getName());
        }
        if (EmptyUtil.isNotEmpty(params.getUserPass())) {
            params.setUserPass(MD5Util.MD5Encode(params.getUserPass(), "utf-8"));
        }
        
        //保存和修改共用一个方法, 如果userid不为空表示是修改操作
        if (EmptyUtil.isEmpty(params.getUserId())) {
        	params.setCreateTime(new Date());
            //检查手机号是否重复
            DataResponse check = this.checkPhone(params.getUserTel());
            if (check.getCode().intValue() != 1000) {
                return check;
            }
            this.userManageInfoService.save(params);
        } else {
            if (EmptyUtil.isEmpty(params.getUserIntroduction())) {
                params.setUserIntroduction(null);
            }
            if (EmptyUtil.isEmpty(params.getUserPass())) {
                params.setUserPass(null);
            }
            this.userManageInfoService.modifyEntity(params);
        }
        this.redisManager.delete(RedisKeyConstant.ASSISTANT + params.getRoomId());//删除助理缓存
        this.redisManager.delete(RedisKeyConstant.TEACHER + params.getRoomId());//删除老师缓存
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse saveSmallUser(UserSmallInfo params) {
        //助理信息
        UserManageInfo user = this.userManageInfoService.findById(params.getUserId());
        if (EmptyUtil.isNotEmpty(user)) {
            params.setUserId(user.getUserId());
            params.setUserName(user.getUserNickName());
        }
        //直播间信息
        LiveRoom room = this.liveRoomService.findById(user.getRoomId());
        if (EmptyUtil.isNotEmpty(room)) {
            params.setRoomId(room.getRoomId());
            params.setRoomName(room.getRoomName());
        }
        params.setCreateTime(new Date());
        //保存和修改共用一个方法, 如果 smallId 不为空表示是修改操作
        if (EmptyUtil.isEmpty(params.getSmallId())) {
            this.userSmallInfoService.save(params);
        } else {
            this.userSmallInfoService.modifyEntity(params);
        }
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse getSmallList(UserSmallInfo params) {
        params.setStatus(0);//正常状态
        SearchCondition<UserSmallInfo> condition = new SearchCondition<UserSmallInfo>(params);
        List<UserSmallInfo> smalls = this.userSmallInfoService.findByCondition(condition);
        return new DataResponse(1000, smalls);
    }

    @Override
    public DataResponse getUserSmall(UserSmallInfo params) {
        UserSmallInfo small = this.userSmallInfoService.findById(params.getSmallId());
        return new DataResponse(1000, small);
    }

    @Override
    public DataResponse deleteManageUser(UserManageInfo params) {
        params.setStatus(1);//删除标记
        this.userManageInfoService.modifyEntity(params);
        params = this.userManageInfoService.findById(params.getUserId());
        if (params.getGroupId().intValue() == 3) {
            this.redisManager.delete(RedisKeyConstant.ASSISTANT + params.getRoomId());
        }
        if (params.getGroupId().intValue() == 4) {
            this.redisManager.delete(RedisKeyConstant.TEACHER + params.getRoomId());
        }
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse deleteSmall(UserSmallInfo params) {
        params.setStatus(1);//删除标记
        this.userSmallInfoService.modifyEntity(params);
        return new DataResponse(1000, "success");
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public DataResponse getEmployeeUserList(PageSearch pageSearch, BmsEmployeeInfo params) {
        params.setStatus(1);//未删除用户标记
        SearchCondition<BmsEmployeeInfo> condition = new SearchCondition<BmsEmployeeInfo>(params, pageSearch);
        PageResult<BmsEmployeeInfo> result = this.employeeInfoService.findByPage(condition);
        return new DataResponse(1000, result);
    }

    @Override
    public DataResponse saveEmployee(BmsEmployeeInfo params) {
        if (EmptyUtil.isEmpty(params.getId()) ) {
        	if(EmptyUtil.isEmpty(params.getPassword())) {
        		return new DataResponse(1001,"密码不能为空");
        	}else {
        		params.setPassword(MD5Util.MD5Encode(params.getPassword(), "utf-8"));
        	}
        }
        params.setCreateTime(new Date());
        if (EmptyUtil.isEmpty(params.getId())) {
            this.employeeInfoService.save(params);
        } else {
            this.employeeInfoService.modifyEntity(params);
        }
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse getEmployeeUser(BmsEmployeeInfo params) {
        SearchCondition<BmsEmployeeInfo> condition = new SearchCondition<BmsEmployeeInfo>(params);
        List<BmsEmployeeInfo> result = this.employeeInfoService.findByCondition(condition);
        return new DataResponse(1000, result);
    }

    @Override
    public DataResponse deleteEmployeeUser(BmsEmployeeInfo params) {
        params.setStatus(2);//删除标记
        this.employeeInfoService.modifyEntity(params);
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse resetPassWord(BmsEmployeeInfo params) {
        if (EmptyUtil.isNotEmpty(params.getPassword())) {
            params.setPassword(MD5Util.MD5Encode(params.getPassword(), "utf-8"));
        }
        this.employeeInfoService.modifyEntity(params);
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse checkPhone(String phone) {
        UserVipInfo vipParams = new UserVipInfo();
        vipParams.setUserTel(phone);
        vipParams.setStatus(0);
        SearchCondition<UserVipInfo> vipCondition = new SearchCondition<UserVipInfo>(vipParams);
        UserVipInfo vip = this.userVipInfoService.findOneByCondition(vipCondition);

        UserManageInfo manageParams = new UserManageInfo();
        manageParams.setUserTel(phone);
        manageParams.setStatus(0);
        SearchCondition<UserManageInfo> manageCondition = new SearchCondition<UserManageInfo>(manageParams);
        UserManageInfo manage = this.userManageInfoService.findOneByCondition(manageCondition);

        if (EmptyUtil.isEmpty(vip) && EmptyUtil.isEmpty(manage)) {
            return new DataResponse(1000, "success");
        } else {
            return new DataResponse(1001, "已存在的手机号.");
        }
    }
}

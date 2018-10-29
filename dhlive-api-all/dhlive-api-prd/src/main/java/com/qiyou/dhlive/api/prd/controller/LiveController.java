package com.qiyou.dhlive.api.prd.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.gson.Gson;
import com.qiyou.dhlive.api.base.outward.service.IActivityApiService;
import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.api.base.outward.service.ILiveRoomApiService;
import com.qiyou.dhlive.api.base.outward.service.IUserInfoApiService;
import com.qiyou.dhlive.api.base.outward.service.IWaterService;
import com.qiyou.dhlive.api.base.outward.util.NoticeUtil;
import com.qiyou.dhlive.api.base.outward.vo.UserVO;
import com.qiyou.dhlive.api.prd.mvc.UserSession;
import com.qiyou.dhlive.api.prd.util.AddressUtils;
import com.qiyou.dhlive.api.prd.util.CheckMobileUtil;
import com.qiyou.dhlive.api.prd.util.TLSUtils;
import com.qiyou.dhlive.api.prd.vo.AutoMsgVo;
import com.qiyou.dhlive.core.activity.outward.service.IActivityLuckyDrawWinnersService;
import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.live.outward.model.LiveC2CMessage;
import com.qiyou.dhlive.core.live.outward.model.LiveRoom;
import com.qiyou.dhlive.core.live.outward.service.ILiveC2CMessageService;
import com.qiyou.dhlive.core.live.outward.service.ILiveRoomService;
import com.qiyou.dhlive.core.room.outward.model.RoomAutoMsg;
import com.qiyou.dhlive.core.room.outward.service.IRoomAutoMsgService;
import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.model.UserRelation;
import com.qiyou.dhlive.core.user.outward.model.UserSmallInfo;
import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;
import com.qiyou.dhlive.core.user.outward.model.UserWaterGroup;
import com.qiyou.dhlive.core.user.outward.service.IUserInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserManageInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserRelationService;
import com.qiyou.dhlive.core.user.outward.service.IUserSmallInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserVipInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserWaterGroupService;
import com.qiyou.dhlive.core.user.outward.vo.RelationVO;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;
import com.yaozhong.framework.web.annotation.session.UnSession;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/22
 */
@Controller
@RequestMapping(value = "")
public class LiveController {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IUserInfoApiService userInfoApiService;

    @Autowired
    private IBaseSysParamService baseSysParamService;

    @Autowired
    private ILiveRoomService liveRoomService;

    @Autowired
    private ILiveRoomApiService liveRoomApiService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserManageInfoService userManageInfoService;

    @Autowired
    private IUserVipInfoService userVipInfoService;

    @Autowired
    private ILiveC2CMessageService liveC2CMessageService;

    @Autowired
    private IUserRelationService userRelationService;

    @Autowired
    private IUserWaterGroupService userWaterGroupService;

    @Autowired
    private IActivityApiService activityApiService;

    @Autowired
    private IRoomAutoMsgService roomAutoMsgService;

    @Autowired
    private IUserSmallInfoService userSmallInfoService;

    @Autowired
    private IActivityLuckyDrawWinnersService activityLuckyDrawWinnersService;
    
    @Autowired
    private IBaseCacheService baseCacheService;
    
    @Autowired
    private IWaterService waterService;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    private static final int CACHE_TIME = 60;

    @RequestMapping(value = "")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model, Integer roomId) {
        String sdkAppId = this.baseSysParamService.getValueByKey("sdk_app_id");
        String accountType = this.baseSysParamService.getValueByKey("account_type");
        model.addAttribute("sdkAppId", sdkAppId);
        model.addAttribute("accountType", accountType);

        if (EmptyUtil.isEmpty(roomId)) {
            roomId = 4;
        }
        //参与过活动标记
//        String isReceive = "0";

        String imagePath = this.baseSysParamService.getValueByKey("host.images");
        model.addAttribute("imagePath", imagePath);

        String roomStr = this.redisManager.getStringValueByKey(RedisKeyConstant.ROOM + roomId);
        LiveRoom room = new LiveRoom();
        if (EmptyUtil.isEmpty(roomStr)) {
            room = this.liveRoomService.findById(roomId);
            this.redisManager.saveString(RedisKeyConstant.ROOM + roomId, new Gson().toJson(room));
        } else {
            room = new Gson().fromJson(roomStr, LiveRoom.class);
        }

        //在线人数
        String onLineNum = this.redisManager.getStringValueByKey(RedisKeyConstant.ONLINENUM + room.getRoomId());
        if (EmptyUtil.isNotEmpty(onLineNum)) {
            room.setBaseNum(room.getBaseNum() + Integer.parseInt(onLineNum));
        }
        model.addAttribute("room", room);
        
/*        //在线助理列表
        List<UserManageInfo> onLineAssistant = this.userInfoApiService.getAssistantList(room.getRoomId());
        //所有的助理
        List<UserManageInfo> unLineAssistant = this.userInfoApiService.getAssistantList(room.getRoomId());
        if (EmptyUtil.isEmpty(onLineAssistant)) {
            UserManageInfo params = new UserManageInfo();
            params.setStatus(0);
            params.setRoomId(roomId);
            params.setGroupId(3);
            DataResponse manages = this.userInfoApiService.getManageUser(params);
            unLineAssistant = (List<UserManageInfo>) manages.getData();
        }
        Collections.shuffle(onLineAssistant);*/
       List<UserManageInfo> onLineAssistant=Lists.newArrayList();
       List<UserManageInfo>  assistantList=this.baseCacheService.getManageUserList(room.getRoomId());
       for(UserManageInfo m:assistantList) {
    	   if(m.getGroupId().intValue()==3) {
    		   onLineAssistant.add(m);
    	   }
       }
        model.addAttribute("assistant", onLineAssistant);

  /*      //如果没有在线的助理, 取所有的助理
        if (EmptyUtil.isEmpty(onLineAssistant)) {
            onLineAssistant = unLineAssistant;
        }
        */
        
        int x = (int) (Math.random() * onLineAssistant.size());//随机数, 用于选择一个助理进行关联

        UserSession session = UserSession.getUserSession();
        //缓存当前登陆人身份, 如果是游客/vip 关联助理关系
        if (session.getGroupId().intValue() == 1) {
            String value = this.redisManager.getStringValueByKey(RedisKeyConstant.TOURISTS + session.getUserId());
            UserInfo user = new UserInfo();
            if (EmptyUtil.isNotEmpty(value)) {
                user = new Gson().fromJson(value, UserInfo.class);
            } else {
                user = this.userInfoService.findById(session.getUserId());
                this.redisManager.saveStringBySeconds(RedisKeyConstant.TOURISTS + session.getUserId(), new Gson().toJson(user));
            }

            //判断游客如果被拉黑, 跳转到404页面
            if (user.getIsBlack().intValue() == 1) {
                try {
                    response.sendRedirect("/error/404");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //从缓存拉取助理关系, 判断是否存在已经关联的助理, 如果没有进行关联(60s会失效)
            UserManageInfo onLineZL =waterService.initYkKefu(session.getUserId());
            model.addAttribute("relation", onLineZL);
            model.addAttribute("user", user);
        } else if (session.getGroupId().intValue() == 2 || session.getGroupId().intValue() == 3 || session.getGroupId().intValue() == 4) {
            String value = this.redisManager.getStringValueByKey(RedisKeyConstant.MANAGE + session.getUserId());
            UserManageInfo manage = new UserManageInfo();
            if (EmptyUtil.isNotEmpty(value)) {
                manage = new Gson().fromJson(value, UserManageInfo.class);
            } else {
                manage = this.userManageInfoService.findById(session.getUserId());
                this.redisManager.saveStringBySeconds(RedisKeyConstant.MANAGE + session.getUserId(), new Gson().toJson(manage));
            }
            List<UserSmallInfo> small = this.userInfoApiService.getUserSmallList(roomId, manage.getUserId());
            for (int i = 0; i < small.size(); i++) {
                if (small.get(i).getUserId().intValue() != manage.getUserId()) {
                    small.remove(i);
                }
            }
            model.addAttribute("small", small);
            model.addAttribute("manage", manage);
        } else {
            UserVipInfo check = this.userVipInfoService.findById(session.getUserId());
            //判断游客如果被拉黑, 跳转到404页面
            if (check.getIsBlack().intValue() == 1) {
                try {
                    response.sendRedirect("/error/404");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String value = this.redisManager.getStringValueByKey(RedisKeyConstant.VIP + session.getUserId());
            UserVipInfo vip = new UserVipInfo();
            if (EmptyUtil.isNotEmpty(value)) {
                vip = new Gson().fromJson(value, UserVipInfo.class);
            } else {
                vip = this.userVipInfoService.findById(session.getUserId());
                this.redisManager.saveString(RedisKeyConstant.VIP + session.getUserId(), new Gson().toJson(vip));
            }

            //从缓存拉取助理关系, 判断是否存在已经关联的助理, 如果没有进行关联(60s会失效)
            UserManageInfo onLineZL =waterService.initVipKefu(session.getUserId());
            model.addAttribute("relation", onLineZL);
//            ActivityLuckyDrawWinners winner = new ActivityLuckyDrawWinners();
//            winner.setConfigId(1);
//            winner.setUserId(vip.getUserId());
//            winner.setStatus(1);
//            SearchCondition<ActivityLuckyDrawWinners> condition = new SearchCondition<ActivityLuckyDrawWinners>(winner);
//            winner = this.activityLuckyDrawWinnersService.findOneByCondition(condition);
//            if (EmptyUtil.isNotEmpty(winner)) {
//                isReceive = "1";
//            }
            model.addAttribute("vip", vip);
        }

        //活动配置
//        if (session.getGroupId().intValue() == 1 || session.getGroupId().intValue() == 5) {
//            ActivityLuckyDrawConfig config = (ActivityLuckyDrawConfig) this.activityApiService.redPackInfo(new ActivityLuckyDrawConfig()).getData();
//            model.addAttribute("config", config);
//        }

//        //是否领取过红包
//        model.addAttribute("isReceive", isReceive);

        //老师列表
        List<UserManageInfo> teacher = this.userInfoApiService.getTeacherList(roomId);
        model.addAttribute("teacher", teacher);
        //String roomSyllabusUrl  = baseSysParamService.updateValueByKey("roomSyllabus");
        //model.addAttribute("roomSyllabusUrl", roomSyllabusUrl);
        /**
         * 判断当前登陆人是否可以观看视频
         */
        if (CheckMobileUtil.isMoblie(request)) {
            DataResponse result = new DataResponse();
            if (session.getGroupId().intValue() == 1) {
                result = this.checkCanWatch(session.getGroupId(), session.getUserId(), request);
            } else if (session.getGroupId().intValue() == 5) {
                result = this.checkCanWatch(session.getGroupId(), session.getUserId(), request);
            }
            if (result.getCode().intValue() == 1001) {
                model.addAttribute("isOver", 1);
            } else {
                model.addAttribute("isOver", 0);
            }
            return "index.mobile";
        } else {
            model.addAttribute("isOver", 0);
            return "index";
        }
    }

    /**
     * 页面初始化的时候, ajax调用, 获取token
     *
     * @return
     */
    @UnSession
    @RequestMapping(value = "/live/getChatToken")
    @ResponseBody
    public DataResponse getChatToken(Integer groupId) {
        UserSession session = UserSession.getUserSession();
        String sdkAppId = this.baseSysParamService.getValueByKey("sdk_app_id");
        String privateKey = this.baseSysParamService.getValueByKey("private_key");
        String userSig = "";
        if (EmptyUtil.isEmpty(session.getGroupId())) {
            session.setGroupId(groupId);
        }
        if (session.getGroupId().intValue() == 1) {
            userSig = TLSUtils.getUserSig(Integer.parseInt(sdkAppId), "yk-" + session.getUserId(), privateKey);
        } else if (session.getGroupId().intValue() == 5) {
            userSig = TLSUtils.getUserSig(Integer.parseInt(sdkAppId), "vip-" + session.getUserId(), privateKey);
        } else {
            userSig = TLSUtils.getUserSig(Integer.parseInt(sdkAppId), session.getUserId() + "", privateKey);
        }
        return new DataResponse(1000, "success", userSig);
    }

    /**
     * 保存到桌面
     *
     * @return
     */
    @RequestMapping(value = "/live/downURL")
    public String downURL() {
        return "downURL";
    }


    /**
     * 获取直播间在线人数
     *
     * @param roomId
     * @return
     */
    @RequestMapping(value = "/live/getOnlineUser")
    @ResponseBody
    public DataResponse getOnlineUser(Integer roomId) {
        String roomStr = this.redisManager.getStringValueByKey(RedisKeyConstant.ROOM + roomId);
        LiveRoom room = new LiveRoom();
        if (EmptyUtil.isEmpty(roomStr)) {
            room = this.liveRoomService.findById(roomId);
            this.redisManager.saveString(RedisKeyConstant.ROOM + roomId, new Gson().toJson(room));
        } else {
            room = new Gson().fromJson(roomStr, LiveRoom.class);
        }

        //从缓存拿到在线的游客
        List<String> listYK = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.YK_IDS);
        //DataResponse result = this.baseCacheService.getManageUserList(roomId);
        List<UserManageInfo> listZL = this.baseCacheService.getManageUserList(roomId);

        //从缓存拿到在线的会员
        List<String> listVIP = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.VIP_IDS);
        Integer onLineNum = listYK.size() + listZL.size() + listVIP.size();
        this.redisManager.saveString(RedisKeyConstant.ONLINENUM + roomId, String.valueOf(onLineNum));
        return new DataResponse(1000, room.getBaseNum() + onLineNum);
    }


    /**
     * 直播间当前在线人的身份和id
     *
     * @param roomId
     * @return
     */
    @RequestMapping(value = "/live/initRedisUser")
    @ResponseBody
    public DataResponse initRedisUser(Integer userId, Integer groupId) {
        if (groupId.intValue() == 1) {//游客
            this.redisManager.saveHash(RedisKeyConstant.YK_IDS, String.valueOf(userId), String.valueOf(new Date().getTime()) + "-" + userId);
        } else if (groupId.intValue() == 3) {//助理
            this.redisManager.saveHash(RedisKeyConstant.ZL_IDS, String.valueOf(userId), String.valueOf(new Date().getTime()) + "-" + userId);
        } else if (groupId.intValue() == 5) {//vip
            this.redisManager.saveHash(RedisKeyConstant.VIP_IDS, String.valueOf(userId), String.valueOf(new Date().getTime()) + "-" + userId);
        }
        return new DataResponse(1000, null);
    }


    /**
     * 管理员获取水滴界面用户列表
     *
     * @param roomId 房间id
     * @return
     */
    @RequestMapping(value = "/live/getWaterUserList")
    @ResponseBody
    public DataResponse getWaterUserList(Integer roomId) {
        return this.liveRoomApiService.getWaterUserList(roomId);
    }


    /**
     * 管理员选择一个游客/vip之后, 获取两者的聊天记录
     *
     * @param roomId
     * @param toGroupId
     * @param fromGroupId
     * @param fromId
     * @param toId
     * @return
     */
    @RequestMapping(value = "/live/getWaterChatMessage")
    @ResponseBody
    public DataResponse getWaterChatMessage(Integer roomId, Integer toGroupId, Integer fromGroupId, Integer fromId, Integer toId) {
        //获取聊天记录开始
        //发送人聊天记录
        LiveC2CMessage from = new LiveC2CMessage();
        from.setRoomId(roomId);
        from.setGroupId(fromGroupId);
        from.setFromId(fromId);
        from.setToId(toId);
        SearchCondition<LiveC2CMessage> fromCondition = new SearchCondition<LiveC2CMessage>(from);
        List<LiveC2CMessage> fromMessage = this.liveC2CMessageService.findByCondition(fromCondition);

        //接受人聊天记录
        LiveC2CMessage to = new LiveC2CMessage();
        to.setRoomId(roomId);
        to.setGroupId(toGroupId);
        to.setFromId(toId);
        to.setToId(fromId);
        SearchCondition<LiveC2CMessage> toCondition = new SearchCondition<LiveC2CMessage>(to);
        List<LiveC2CMessage> toMessage = this.liveC2CMessageService.findByCondition(toCondition);
        //处理后的聊天记录
        List<LiveC2CMessage> result = new ArrayList<LiveC2CMessage>();
        for (int i = 0; i < fromMessage.size(); i++) {
            result.add(fromMessage.get(i));
        }
        for (int i = 0; i < toMessage.size(); i++) {
            result.add(toMessage.get(i));
        }
        Ordering<LiveC2CMessage> sortByStatus = Ordering.natural().onResultOf(
                new Function<LiveC2CMessage, Date>() {
                    public Date apply(LiveC2CMessage data) {
                        return data.getCreateTime();
                    }
                });
        List<LiveC2CMessage> messageList = sortByStatus.sortedCopy(result);

        //如果是助理,设置聊天记录为已读状态
        if (fromGroupId == 3) {
            LiveC2CMessage params = new LiveC2CMessage();
            params.setGroupId(toGroupId);
            params.setFromId(toId);
            SearchCondition<LiveC2CMessage> condition = new SearchCondition<LiveC2CMessage>(params);
            LiveC2CMessage update = new LiveC2CMessage();
            update.setIsView(1);
            this.liveC2CMessageService.modifyEntityByCondition(update, condition);
        }
        return new DataResponse(1000, messageList);
    }


    /**
     * 保存C2C消息
     *
     * @param message 消息对象
     * @return
     */
    @RequestMapping(value = "/live/saveC2CMessage")
    @ResponseBody
    public DataResponse saveC2CMessage(LiveC2CMessage params) {
        //保存聊天记录
        params.setCreateTime(new Date());
        this.liveC2CMessageService.save(params);
        return new DataResponse(1000, "success", params);
    }


    /**
     * 保存助理水滴分组列表
     *
     * @return
     */
    @RequestMapping(value = "/live/saveWaterGroup")
    @ResponseBody
    public DataResponse saveWaterGroup(UserWaterGroup params) {
        if (EmptyUtil.isEmpty(params.getWaterGroupId())) {
            this.userWaterGroupService.save(params);
        } else {
            this.userWaterGroupService.modifyEntity(params);
        }
        return new DataResponse(1000, "success");
    }

    /**
     * 删除助理水滴分组列表
     *
     * @return
     */
    @RequestMapping(value = "/live/delWaterGroup")
    @ResponseBody
    public DataResponse delWaterGroup(UserWaterGroup params) {
        List<UserRelation> data = this.userRelationService.findByField("waterGroupId", params.getWaterGroupId());
        if (EmptyUtil.isNotEmpty(data)) {
            return new DataResponse(1001, "该分组存在用户,不能删除.");
        }
        params.setIsDel(1);
        this.userWaterGroupService.modifyEntity(params);
        return new DataResponse(1000, "success");
    }

    /**
     * 获取助理水滴分组列表
     *
     * @return
     */
    @RequestMapping(value = "/live/selectGroup")
    @ResponseBody
    public DataResponse selectGroup(UserRelation params) {
        this.userRelationService.modifyEntity(params);
        return new DataResponse(1000, "sucess");
    }


    /**
     * 获取助理水滴分组列表
     *
     * @return
     */
    @RequestMapping(value = "/live/getWaterGroup")
    @ResponseBody
    public DataResponse getWaterGroup(UserWaterGroup params) {
        params.setIsDel(0);
        SearchCondition<UserWaterGroup> condition = new SearchCondition<UserWaterGroup>(params);
        List<Object> list = new ArrayList<Object>();
        list.add(0);
        condition.buildNotInConditions("userId", list);
        List<UserWaterGroup> data = this.userWaterGroupService.findByCondition(condition);
        params = this.userWaterGroupService.findOneByField("userId", 0);
        data.add(0, params);
        return new DataResponse(1000, data);
    }


    /**
     * 获取联系过的游客/vip列表
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/live/getContactPersion")
    @ResponseBody
    public DataResponse getContactPersion(UserRelation params) {
        List<UserVO> data = new ArrayList<UserVO>();

        long start = System.currentTimeMillis();
        //取到在线的游客id
        List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.YK_IDS);
        List<Integer> ykIds = new ArrayList<Integer>();
        for (int i = 0; i < listJson.size(); i++) {
            String str[] = listJson.get(i).split("-");
            ykIds.add(Integer.parseInt(str[1]));
        }
        long end = System.currentTimeMillis();
        baseLog.info(LogFormatUtil.getActionFormat("从缓存取在线游客--结束----耗时:" + (end - start) + "ms"));


        //封装返回集合
        start = System.currentTimeMillis();
        List<RelationVO> relations = this.userRelationService.getRelationUserList(params.getRelationUserId(), params.getWaterGroupId());
        end = System.currentTimeMillis();
        baseLog.info(LogFormatUtil.getActionFormat("获取关系数据--结束----耗时:" + (end - start) + "ms"));

        start = System.currentTimeMillis();
        for (int i = 0; i < relations.size(); i++) {
            for (int j = 0; j < ykIds.size(); j++) {
                if (relations.get(i).getUserId().intValue() == ykIds.get(j)) {
                    UserVO record = new UserVO();
                    record.setId(relations.get(i).getId());
                    record.setUserNickName(relations.get(i).getYkName());
                    record.setJoinTime(relations.get(i).getYkTime());
                    record.setCount(relations.get(i).getYkCount());
                    record.setFlag(relations.get(i).getFlag());
                    record.setUserId(relations.get(i).getUserId());
                    record.setGroupId(relations.get(i).getGroupId());
                    record.setWaterGroupId(relations.get(i).getWaterGroupId());
                    data.add(record);
                }
            }
        }
        end = System.currentTimeMillis();
        baseLog.info(LogFormatUtil.getActionFormat("处理关系数据--结束----耗时:" + (end - start) + "ms"));
        return new DataResponse(1000, data);
    }


    /**
     * 水滴群发消息
     *
     * @return
     */
    @RequestMapping(value = "/live/sendGroupMsg")
    @ResponseBody
    public DataResponse sendGroupMsg(UserRelation params, String content, Integer roomId) {
        String sdkAppId = this.baseSysParamService.getValueByKey("sdk_app_id");
        String identifier = this.baseSysParamService.getValueByKey("identifier");
        String privateKey = this.baseSysParamService.getValueByKey("private_key");
        String userSig = com.qiyou.dhlive.api.base.outward.util.TLSUtils.getUserSig(Integer.parseInt(sdkAppId), identifier, privateKey);
        List<UserVO> data = new ArrayList<UserVO>();
        //取到在线的游客id
        List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.YK_IDS);
        List<Integer> ykIds = new ArrayList<Integer>();
        for (int i = 0; i < listJson.size(); i++) {
            String str[] = listJson.get(i).split("-");
            ykIds.add(Integer.parseInt(str[1]));
        }
        //封装返回集合
        List<RelationVO> relations = this.userRelationService.getRelationUserListAll(params.getRelationUserId());
        baseLog.info(LogFormatUtil.getActionFormat("批量发送消息人数" + relations.size()));
        for (int i = 0; i < relations.size(); i++) {
            for (int j = 0; j < ykIds.size(); j++) {
                if (relations.get(i).getUserId().intValue() == ykIds.get(j)) {
                    UserVO record = new UserVO();
                    record.setId(relations.get(i).getId());
                    record.setUserNickName(relations.get(i).getYkName());
                    record.setJoinTime(relations.get(i).getYkTime());
                    record.setCount(relations.get(i).getYkCount());
                    record.setFlag(relations.get(i).getFlag());
                    record.setUserId(relations.get(i).getUserId());
                    record.setGroupId(relations.get(i).getGroupId());
                    record.setWaterGroupId(relations.get(i).getWaterGroupId());
                    data.add(record);
                }
            }
        }
        baseLog.info(LogFormatUtil.getActionFormat("批量发送消息在线人数" + data.size()));

        if (data.size() == 0) {
            return new DataResponse(1001, "当前无人在线.");
        }
        for (int i = 0; i < data.size(); i++) {
            //保存聊天记录
            LiveC2CMessage message = new LiveC2CMessage();
            UserManageInfo manage = this.userManageInfoService.findById(params.getRelationUserId());
            message.setFromId(params.getRelationUserId());
            message.setFromNickName(manage.getUserNickName());
            message.setToId(data.get(i).getUserId());
            message.setToNickName(data.get(i).getUserNickName());
            message.setContent(content);
            message.setGroupId(3);
            message.setRoomId(roomId);
            message.setIsView(0);
            message.setCreateTime(new Date());
            this.liveC2CMessageService.save(message);
            NoticeUtil.sendGroupMsg(String.valueOf(params.getRelationUserId()), data.get(i).getFlag(), new Gson().toJson(message), userSig, identifier, sdkAppId);
        }
        return new DataResponse(1000, "发送成功.");
    }


    /**
     * 获取在线游客/vip列表
     *
     * @param flag 1游客 0vip
     * @return
     */
    @RequestMapping(value = "/live/getOnlineUserList")
    @ResponseBody
    public DataResponse getOnlineUserList(String flag) {
        if (flag.equals("0")) {
            List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.VIP_IDS);
            List<Integer> vipIds = new ArrayList<Integer>();
            for (int i = 0; i < listJson.size(); i++) {
                String str[] = listJson.get(i).split("-");
                vipIds.add(Integer.parseInt(str[1]));
            }
            List<UserVipInfo> data = new ArrayList<UserVipInfo>();
            for (int i = 0; i < vipIds.size(); i++) {
                String value = this.redisManager.getStringValueByKey(RedisKeyConstant.VIP + vipIds.get(i));
                UserVipInfo record = new UserVipInfo();
                if (EmptyUtil.isEmpty(value)) {
                    record = this.userVipInfoService.findById(vipIds.get(i));
                } else {
                    record = new Gson().fromJson(value, UserVipInfo.class);
                }
                record.setUserNickName(record.getUserNickName());
                data.add(record);
            }
            return new DataResponse(1000, data);

        } else {
            //取到在线的游客id
            List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.YK_IDS);
            List<Integer> ykIds = new ArrayList<Integer>();
            for (int i = 0; i < listJson.size(); i++) {
                String str[] = listJson.get(i).split("-");
                ykIds.add(Integer.parseInt(str[1]));
            }
            List<UserInfo> data = new ArrayList<UserInfo>();
            for (int i = 0; i < ykIds.size(); i++) {
                String value = this.redisManager.getStringValueByKey(RedisKeyConstant.TOURISTS + ykIds.get(i));
                UserInfo record = new UserInfo();
                if (EmptyUtil.isEmpty(value)) {
                    record = this.userInfoService.findById(ykIds.get(i));
                } else {
                    record = new Gson().fromJson(value, UserInfo.class);
                }
                record.setUserNickName(record.getUserNickName());
                data.add(record);
            }
            return new DataResponse(1000, data);
        }
    }


    /**
     * 校验是否可以观看
     *
     * @param groupId
     * @param request
     * @return
     */
    @RequestMapping(value = "/live/checkCanWatch")
    @ResponseBody
    public DataResponse checkCanWatch(Integer groupId, Integer userId, HttpServletRequest request) {
        String ip = AddressUtils.getIpAddrFromRequest(request);
        String wi = this.redisManager.getStringValueByKey(RedisKeyConstant.REIDSKEY_WHITEIP + ip);
        if (EmptyUtil.isNotEmpty(wi)) {//在白名单. 不限制时长.
            return new DataResponse(1002, "无限时间");
        }

        String roomStr = this.redisManager.getStringValueByKey(RedisKeyConstant.ROOM + 4);
        LiveRoom room = new LiveRoom();
        if (EmptyUtil.isEmpty(roomStr)) {
            room = this.liveRoomService.findById(4);
        } else {
            room = new Gson().fromJson(roomStr, LiveRoom.class);
        }

        String sec = "0";

        if (groupId.intValue() == 1) {//游客记录当前ip下的所有用户的时长 单位 / 秒
            sec = this.redisManager.getStringValueByKey(RedisKeyConstant.WATCH_SEC + ip);
            if (EmptyUtil.isEmpty(sec)) {
                sec = "0";
            } else {
                sec = String.valueOf(Integer.parseInt(sec) + 10);
            }
            Integer result = room.getTempWatchTime() - Integer.parseInt(sec);
            if (result == 0 || result < 0) {
                return new DataResponse(1001, "您的观看时长已结束.");
            } else {
                this.redisManager.saveStringBySeconds(RedisKeyConstant.WATCH_SEC + ip, String.valueOf(sec), 2592000);
                return new DataResponse(1000, result);
            }
        } else {//vip通过id记录观看时间
            String vipStr = this.redisManager.getStringValueByKey(RedisKeyConstant.VIP + userId);
            UserVipInfo vip = new UserVipInfo();
            if (EmptyUtil.isEmpty(vipStr)) {
                vip = this.userVipInfoService.findById(userId);
            } else {
                vip = new Gson().fromJson(vipStr, UserVipInfo.class);
            }

            if (vip.getTempWatchTime().intValue() == -1) {
                return new DataResponse(1002, "无限时间");
            }

            sec = this.redisManager.getStringValueByKey(RedisKeyConstant.WATCH_SEC + userId);
            if (EmptyUtil.isEmpty(sec)) {
                sec = "0";
            } else {
                sec = String.valueOf(Integer.parseInt(sec) + 10);
            }
            Integer result = vip.getTempWatchTime() - Integer.parseInt(sec);
            if (result == 0 || result < 0) {
                return new DataResponse(1001, "您的观看时长已结束.");
            } else {
                this.redisManager.saveString(RedisKeyConstant.WATCH_SEC + userId, String.valueOf(sec));
                return new DataResponse(1000, result);
            }
        }
    }


    /**
     * 助理自动发言
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/live/autoMsg")
    @ResponseBody
    public DataResponse autoMsg() {
    	UserSession session = UserSession.getUserSession();
    	if(EmptyUtil.isEmpty(session)) {
    		return new DataResponse(1001,"未登陆");
    	}
    	if(session.getGroupId().intValue() != 3) {
    		return new DataResponse(1001,"没有权限");
    	}
        SearchCondition<RoomAutoMsg> condition = new SearchCondition<RoomAutoMsg>(new RoomAutoMsg());
        List<RoomAutoMsg> msgs = this.roomAutoMsgService.findByCondition(condition);
        
        if(EmptyUtil.isEmpty(msgs)) {
        	return new DataResponse(1001,"没有数据");
        }
        //随机数, 随机取一条发言
        int x = (int) (Math.random() * msgs.size());

        //随机数, 随意取当前助理的一个小号
        List<UserSmallInfo> smalls = this.userSmallInfoService.findByField("userId", session.getUserId());
        if(EmptyUtil.isEmpty(smalls)) {
        	return new DataResponse(1001,"没有小号");
        }
        int y = (int) (Math.random() * smalls.size());

        //封装返回对象
        AutoMsgVo result = new AutoMsgVo();
        result.setContent(msgs.get(x).getMsgContent());
        result.setName(smalls.get(y).getSmallName());
        result.setLevel(smalls.get(y).getSmallLevel());

        return new DataResponse(1000, result);
    }

}

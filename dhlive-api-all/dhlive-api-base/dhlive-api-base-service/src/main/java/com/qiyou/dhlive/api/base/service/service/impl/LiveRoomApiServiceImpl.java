package com.qiyou.dhlive.api.base.service.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.qiyou.dhlive.api.base.outward.service.ILiveRoomApiService;
import com.qiyou.dhlive.api.base.outward.util.HttpUtil;
import com.qiyou.dhlive.api.base.outward.util.TLSUtils;
import com.qiyou.dhlive.api.base.outward.vo.AVChatRoomVO;
import com.qiyou.dhlive.api.base.outward.vo.MemberVO;
import com.qiyou.dhlive.api.base.outward.vo.QueryResult;
import com.qiyou.dhlive.api.base.outward.vo.UserVO;
import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.core.live.outward.model.LiveRoom;
import com.qiyou.dhlive.core.live.outward.service.ILiveRoomService;
import com.qiyou.dhlive.core.room.outward.model.RoomClass;
import com.qiyou.dhlive.core.room.outward.model.RoomFile;
import com.qiyou.dhlive.core.room.outward.service.IRoomClassService;
import com.qiyou.dhlive.core.room.outward.service.IRoomFileService;
import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserVipInfoService;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.BaseResult;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/30
 */
@Service
public class LiveRoomApiServiceImpl implements ILiveRoomApiService {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private ILiveRoomService liveRoomService;

    @Autowired
    private IBaseSysParamService baseSysParamService;

    @Autowired
    private IRoomFileService roomFileService;

    @Autowired
    private IRoomClassService roomClassService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserVipInfoService userVipInfoService;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    @Override
    public List<LiveRoom> getLiveRoom(LiveRoom params) {
        SearchCondition<LiveRoom> condition = new SearchCondition<LiveRoom>(params);
        List<LiveRoom> rooms = this.liveRoomService.findByCondition(condition);
        return rooms;
    }

    @Override
    public DataResponse getLiveRoomList(PageSearch pageSearch, LiveRoom params) {
        params.setStatus(0);//未删除用户标记
        SearchCondition<LiveRoom> condition = new SearchCondition<LiveRoom>(params, pageSearch);
        PageResult<LiveRoom> result = this.liveRoomService.findByPage(condition);
        return new DataResponse(1000, result);
    }

    @Override
    public DataResponse saveLiveRoom(LiveRoom params) {
        if (EmptyUtil.isEmpty(params.getRoomId())) {//新增
            String sdkAppId = this.baseSysParamService.getValueByKey("sdk_app_id");
            String identifier = this.baseSysParamService.getValueByKey("identifier");
            String privateKey = this.baseSysParamService.getValueByKey("private_key");
            String userSig = TLSUtils.getUserSig(Integer.parseInt(sdkAppId), identifier, privateKey);
            Integer random = TLSUtils.getRandom();
            String method = "POST";
            String url = "https://console.tim.qq.com/v4/group_open_http_svc/create_group?usersig=" + userSig + "&identifier=" + identifier + "&sdkappid=" + sdkAppId + "&random=" + random + "&contenttype=json";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Type", "AVChatRoom");//聊天室
            map.put("Name", params.getRoomName());
            String result = HttpUtil.httpRequest(url, method, new Gson().toJson(map));
            baseLog.info(LogFormatUtil.getActionFormat("创建聊天室返回结果:" + result));
            AVChatRoomVO vo = new Gson().fromJson(result, AVChatRoomVO.class);
            if (vo.getErrorCode().intValue() == 0) {
                params.setRoomGroupId(vo.getGroupId());
                params.setCreateTime(new Date());
                BaseResult br = this.liveRoomService.save(params);
                params.setRoomId(Integer.parseInt(br.getData().toString()));
                this.redisManager.saveStringBySeconds(RedisKeyConstant.ROOM + params.getRoomId(), new Gson().toJson(params));
                return new DataResponse(1000, "success");
            } else {
                return new DataResponse(1001, vo.getErrorInfo());
            }
        } else {//修改
            this.liveRoomService.modifyEntity(params);
            params = this.liveRoomService.findById(params.getRoomId());
            this.redisManager.saveStringBySeconds(RedisKeyConstant.ROOM + params.getRoomId(), new Gson().toJson(params));
            return new DataResponse(1000, "success");
        }
    }


    @Override
    public DataResponse deleteLiveRoom(LiveRoom params) {
        params.setStatus(1);//删除标记
        this.liveRoomService.modifyEntity(params);
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse saveRoomFile(RoomFile params) {
        if (EmptyUtil.isEmpty(params.getFileId())) {
            this.roomFileService.save(params);
        } else {
            this.roomFileService.modifyEntity(params);
        }
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse getLiveRoomFileList(PageSearch pageSearch, RoomFile params) {
        params.setStatus(0);//未删除标记
        SearchCondition<RoomFile> condition = new SearchCondition<RoomFile>(params, pageSearch);
        PageResult<RoomFile> result = this.roomFileService.findByPage(condition);
        return new DataResponse(1000, result);
    }

    @Override
    public DataResponse deleteFile(RoomFile params) {
        params.setStatus(1);//删除标记
        this.roomFileService.modifyEntity(params);
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse getLiveRoomFile(RoomFile params) {
        SearchCondition<RoomFile> condition = new SearchCondition<RoomFile>(params);
        List<RoomFile> result = this.roomFileService.findByCondition(condition);
        return new DataResponse(1000, result);
    }


    @Override
    public DataResponse getOnlineUser(Integer roomId) {
        String onlineNumStr = this.redisManager.getStringValueByKey(RedisKeyConstant.ONLINENUM + roomId);
        baseLog.info(LogFormatUtil.getActionFormat("获取直播间在线人数:" + onlineNumStr));
        if (EmptyUtil.isEmpty(onlineNumStr)) {
            String value = this.redisManager.getStringValueByKey(RedisKeyConstant.ROOM + roomId);
            LiveRoom room = new LiveRoom();
            if (EmptyUtil.isEmpty(value)) {
                room = this.liveRoomService.findById(roomId);
                this.redisManager.saveStringBySeconds(RedisKeyConstant.ROOM + roomId, new Gson().toJson(room));
            } else {
                room = new Gson().fromJson(value, LiveRoom.class);
            }
            Integer onlineNum = room.getBaseNum();
            String sdkAppId = this.baseSysParamService.getValueByKey("sdk_app_id");
            String identifier = this.baseSysParamService.getValueByKey("identifier");
            String privateKey = this.baseSysParamService.getValueByKey("private_key");
            String userSig = com.qiyou.dhlive.api.base.outward.util.TLSUtils.getUserSig(Integer.parseInt(sdkAppId), identifier, privateKey);
            Integer random = com.qiyou.dhlive.api.base.outward.util.TLSUtils.getRandom();
            String method = "POST";
            String url = "https://console.tim.qq.com/v4/group_open_http_svc/get_group_info?usersig=" + userSig + "&identifier=" + identifier + "&sdkappid=" + sdkAppId + "&random=" + random + "&contenttype=json";
            baseLog.info(LogFormatUtil.getActionFormat("获取直播室详情请求地址:" + url));
            Map<String, Object> map = new HashMap<String, Object>();
            String[] groupIds = {room.getRoomGroupId()};
            map.put("GroupIdList", groupIds);
            String result = HttpUtil.httpRequest(url, method, new Gson().toJson(map));
            AVChatRoomVO vo = new Gson().fromJson(result, AVChatRoomVO.class);
            baseLog.info(LogFormatUtil.getActionFormat("获取直播室详情请求地址返回结果:" + vo.getErrorCode()));
            if (vo.getErrorCode().intValue() == 0) {
                if (EmptyUtil.isNotEmpty(vo.getGroupInfo().get(0).getMemberList())) {
                    List<MemberVO> accounts = vo.getGroupInfo().get(0).getMemberList();
                    String[] toAccount = new String[accounts.size()];
                    for (int i = 0; i < accounts.size(); i++) {
                        toAccount[i] = accounts.get(i).getMember_Account();
                    }
                    method = "POST";
                    url = "https://console.tim.qq.com/v4/openim/querystate?usersig=" + userSig + "&identifier=" + identifier + "&sdkappid=" + sdkAppId + "&random=" + random + "&contenttype=json";
                    map = new HashMap<String, Object>();
                    map.put("To_Account", toAccount);
                    result = HttpUtil.httpRequest(url, method, new Gson().toJson(map));
                    vo = new Gson().fromJson(result, AVChatRoomVO.class);
                    baseLog.info(LogFormatUtil.getActionFormat("获取直播室在线人数返回结果:" + vo.getErrorCode()));
                    if (vo.getErrorCode().intValue() == 0) {
                        List<QueryResult> list = vo.getQueryResult();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getState().equals("Online")) {
                                onlineNum++;
                            }
                        }
                    }
                }
            }
            this.redisManager.saveStringBySeconds(RedisKeyConstant.ONLINENUM + roomId, String.valueOf(onlineNum));
            return new DataResponse(1000, onlineNum);
        } else {
            String onlineNum = onlineNumStr;
            return new DataResponse(1000, onlineNum);
        }
    }

    @Override
    public DataResponse saveRoomClass(RoomClass params) {
        if (EmptyUtil.isEmpty(params.getClassId())) {
            this.roomClassService.save(params);
        } else {
            this.roomClassService.modifyEntity(params);
        }
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse getLiveRoomClassList(PageSearch pageSearch, RoomClass params) {
        params.setStatus(0);//未删除标记
        SearchCondition<RoomClass> condition = new SearchCondition<RoomClass>(params, pageSearch);
        PageResult<RoomClass> result = this.roomClassService.findByPage(condition);
        return new DataResponse(1000, result);
    }

    @Override
    public DataResponse getLiveRoomClass(RoomClass params) {
        SearchCondition<RoomClass> condition = new SearchCondition<RoomClass>(params);
        List<RoomClass> result = this.roomClassService.findByCondition(condition);
        return new DataResponse(1000, result);
    }

    @Override
    public DataResponse deleteClass(RoomClass params) {
        params.setStatus(1);//删除标记
        this.roomClassService.modifyEntity(params);
        return new DataResponse(1000, "success");
    }

    @Override
    public DataResponse getWaterUserList(Integer roomId) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
        List<String> memberAccount = new ArrayList<String>();
        String value = this.redisManager.getStringValueByKey(RedisKeyConstant.ROOM + roomId);
        LiveRoom room = new LiveRoom();
        if (EmptyUtil.isEmpty(value)) {
            room = this.liveRoomService.findById(roomId);
            this.redisManager.saveStringBySeconds(RedisKeyConstant.ROOM + roomId, new Gson().toJson(room));
        } else {
            room = new Gson().fromJson(value, LiveRoom.class);
        }
        String sdkAppId = this.baseSysParamService.getValueByKey("sdk_app_id");
        String identifier = this.baseSysParamService.getValueByKey("identifier");
        String privateKey = this.baseSysParamService.getValueByKey("private_key");
        String userSig = com.qiyou.dhlive.api.base.outward.util.TLSUtils.getUserSig(Integer.parseInt(sdkAppId), identifier, privateKey);
        Integer random = com.qiyou.dhlive.api.base.outward.util.TLSUtils.getRandom();
        String method = "POST";
        String url = "https://console.tim.qq.com/v4/group_open_http_svc/get_group_member_info?usersig=" + userSig + "&identifier=" + identifier + "&sdkappid=" + sdkAppId + "&random=" + random + "&contenttype=json";
        baseLog.info(LogFormatUtil.getActionFormat("获取群组成员详细资料地址:" + url));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("GroupId", room.getRoomGroupId());
        String result = HttpUtil.httpRequest(url, method, new Gson().toJson(map));
        AVChatRoomVO vo = new Gson().fromJson(result, AVChatRoomVO.class);
        baseLog.info(LogFormatUtil.getActionFormat("获取群组成员详细资料返回结果:" + vo.getErrorCode()));
        if (vo.getErrorCode().intValue() == 0) {
            if (EmptyUtil.isNotEmpty(vo.getMemberList())) {
                List<MemberVO> accounts = vo.getMemberList();
                String[] toAccount = new String[accounts.size()];
                for (int i = 0; i < accounts.size(); i++) {
                    toAccount[i] = accounts.get(i).getMember_Account();
                }
                method = "POST";
                url = "https://console.tim.qq.com/v4/openim/querystate?usersig=" + userSig + "&identifier=" + identifier + "&sdkappid=" + sdkAppId + "&random=" + random + "&contenttype=json";
                map = new HashMap<String, Object>();
                map.put("To_Account", toAccount);
                result = HttpUtil.httpRequest(url, method, new Gson().toJson(map));
                vo = new Gson().fromJson(result, AVChatRoomVO.class);
                baseLog.info(LogFormatUtil.getActionFormat("获取直播室在线状态返回结果:" + vo.getErrorCode()));
                if (vo.getErrorCode().intValue() == 0) {
                    List<QueryResult> list = vo.getQueryResult();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getState().equals("Online")) {
                            memberAccount.add(list.get(i).getTo_Account());
                        }
                    }
                }
            }
        }

        List<UserVO> data = new ArrayList<UserVO>();
        if (EmptyUtil.isNotEmpty(memberAccount)) {
            for (int i = 0; i < memberAccount.size(); i++) {
                String[] str = memberAccount.get(i).split("-");
                UserVO userVO = new UserVO();
                if (str.length == 2) {
                    Integer userId = Integer.parseInt(str[1]);
                    if (str[0].equals("vip")) {//vip
                        UserVipInfo record = this.userVipInfoService.findById(userId);
                        if (EmptyUtil.isNotEmpty(record)) {
                            userVO.setUserId(record.getUserId());
                            userVO.setGroupId(5);
                            userVO.setJoinTime(record.getCreateTime());
                            userVO.setLevel(record.getUserLevel());
                            userVO.setStatus(1);//1表示在线
                            userVO.setUserNickName(record.getUserNickName());
                            data.add(userVO);
                        }
                    } else {
                        UserInfo record = this.userInfoService.findById(userId);
                        if (EmptyUtil.isNotEmpty(record)) {
                            userVO.setUserId(record.getUserId());
                            userVO.setGroupId(1);
                            userVO.setJoinTime(record.getCreateTime());
                            userVO.setStatus(1);
                            userVO.setUserNickName(record.getUserNickName());
                            data.add(userVO);
                        }
                    }
                }
            }
        }
        return new DataResponse(1000, data);
    }

    @Override
    public List<String> isOnline(String[] toAccount) {
        List<String> accounts = new ArrayList<String>();
        String sdkAppId = this.baseSysParamService.getValueByKey("sdk_app_id");
        String identifier = this.baseSysParamService.getValueByKey("identifier");
        String privateKey = this.baseSysParamService.getValueByKey("private_key");
        String userSig = TLSUtils.getUserSig(Integer.parseInt(sdkAppId), identifier, privateKey);
        Integer random = TLSUtils.getRandom();
        String method = "POST";
        String url = "https://console.tim.qq.com/v4/openim/querystate?usersig=" + userSig + "&identifier=" + identifier + "&sdkappid=" + sdkAppId + "&random=" + random + "&contenttype=json";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("To_Account", toAccount);
        String result = HttpUtil.httpRequest(url, method, new Gson().toJson(map));
        AVChatRoomVO vo = new Gson().fromJson(result, AVChatRoomVO.class);
        baseLog.info(LogFormatUtil.getActionFormat("获取直播室在线状态返回结果:" + vo.getErrorCode()));
        if (vo.getErrorCode().intValue() == 0) {
            List<QueryResult> list = vo.getQueryResult();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getState().equals("Online")) {
                    accounts.add(list.get(i).getTo_Account());
                }
            }
        }
        return accounts;
    }
}

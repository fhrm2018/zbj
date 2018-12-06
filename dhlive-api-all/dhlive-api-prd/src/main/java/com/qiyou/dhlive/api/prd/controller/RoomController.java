package com.qiyou.dhlive.api.prd.controller;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.qiyou.dhlive.api.base.outward.service.IBaseOptLogApiService;
import com.qiyou.dhlive.api.base.outward.service.IFileUploadRemoteService;
import com.qiyou.dhlive.api.base.outward.service.ILiveRoomApiService;
import com.qiyou.dhlive.api.base.outward.service.ISettingApiService;
import com.qiyou.dhlive.api.base.outward.service.IUserInfoApiService;
import com.qiyou.dhlive.api.prd.mvc.UserSession;
import com.qiyou.dhlive.core.base.outward.model.BaseWord;
import com.qiyou.dhlive.core.base.outward.service.IBaseWordService;
import com.qiyou.dhlive.core.room.outward.model.RoomAnnouncement;
import com.qiyou.dhlive.core.room.outward.model.RoomChatMessage;
import com.qiyou.dhlive.core.room.outward.model.RoomClass;
import com.qiyou.dhlive.core.room.outward.model.RoomFile;
import com.qiyou.dhlive.core.room.outward.model.RoomMessageBoard;
import com.qiyou.dhlive.core.room.outward.service.IRoomChatMessageService;
import com.qiyou.dhlive.core.room.outward.service.IRoomFileService;
import com.qiyou.dhlive.core.room.outward.service.IRoomMessageBoardService;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.web.annotation.session.UnSession;

/**
 * 直播间controller
 *
 * @author fish
 * @create 2018-01-20 16:37
 **/
@Controller
@RequestMapping(value = "room")
public class RoomController {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IRoomFileService roomFileService;

    @Autowired
    private IRoomChatMessageService roomChatMessageService;

    @Autowired
    private IRoomMessageBoardService roomMessageBoardService;

    @Autowired
    private IUserInfoApiService userInfoApiService;

    @Autowired
    private ISettingApiService settingApiService;

    @Autowired
    private ILiveRoomApiService liveRoomApiService;

    @Autowired
    private IBaseOptLogApiService baseOptLogApiService;

    @Autowired
    private IBaseWordService baseWordService;

    @Autowired
    private IFileUploadRemoteService fileUploadRemoteService;

    /**
     * 保存群聊消息
     *
     * @param message 消息对象
     * @return
     */
    @UnSession
    @RequestMapping(value = "saveChatMessage")
    @ResponseBody
    public DataResponse saveChatMessage(RoomChatMessage message) {
        DataResponse result = new DataResponse();
        try {
            if (EmptyUtil.isNotEmpty(message)) {
                baseLog.info(LogFormatUtil.getActionFormat("保存群聊消息开始参数:" + new Gson().toJson(message.getUniqueId())));
            }
            result = this.roomChatMessageService.saveChatMessage(message);
            baseLog.info(LogFormatUtil.getActionFormat("保存群聊消息结束"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        UserSession userSession = UserSession.getUserSession();
		if(EmptyUtil.isNotEmpty(userSession) && EmptyUtil.isNotEmpty(userSession.getUserId()) && EmptyUtil.isNotEmpty(userSession.getGroupId())) {
			if(userSession.getGroupId().intValue() == 3) {
				message.setGroupId(userSession.getGroupId());
				message.setPostUid(userSession.getUserId());
				this.baseOptLogApiService.saveManageMessageLog(message);
			}
		}        
        return result;
    }


    /**
     * 审核群消息
     *
     * @param params
     * @return
     */
    @UnSession
    @RequestMapping(value = "auditChatMessage")
    @ResponseBody
    public DataResponse auditChatMessage(RoomChatMessage params) {
        DataResponse result = this.roomChatMessageService.auditChatMessage(params);
        this.baseOptLogApiService.saveAuditMessageLog(params);
        return result;
    }

    /**
     * 用户获取群消息(拉取最近50条)
     *
     * @param params
     * @return
     */
    @UnSession
    @RequestMapping(value = "getChatMessageByUser")
    @ResponseBody
    public DataResponse getChatMessageByUser(RoomChatMessage params) {
    	DataResponse data = this.roomChatMessageService.getMessageList();
        return data;
    }

    /**
     * 管理员获取群消息(新消息和审核通过的)
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "getChatMessageByAdmin")
    @ResponseBody
    public DataResponse getChatMessageByAdmin(RoomChatMessage params) {
        DataResponse result = this.roomChatMessageService.getMessageList();
        return result;
    }

    /**
     * 删除群聊信息
     *
     * @param params
     * @return
     */
    @UnSession
    @RequestMapping(value = "deleteChatMessage")
    @ResponseBody
    public DataResponse deleteChatMessage(RoomChatMessage params) {
//        UserSession userSession = UserSession.getUserSession();//操作人信息
//        if (userSession.getGroupId().intValue() == 3) {
        DataResponse result = this.roomChatMessageService.deleteChatMessage(params);
        try {
        	RoomChatMessage resMsg = (RoomChatMessage)result.getData();
        	if(EmptyUtil.isNotEmpty(resMsg) && EmptyUtil.isNotEmpty(resMsg.getUniqueId()) ) {
        		UserSession userSession = UserSession.getUserSession();
        		if(EmptyUtil.isNotEmpty(userSession) && EmptyUtil.isNotEmpty(userSession.getUserId())) {
        			resMsg.setAuditUid(userSession.getUserId());
        		}
        		this.baseOptLogApiService.saveDelMessageLog(resMsg);
        	}
        	
        }catch(Exception e) {
        	e.printStackTrace();
        }
        return result;
    }


    /**
     * 根据消息唯一id获取消息对象
     *
     * @param params
     * @return
     */
    @UnSession
    @RequestMapping(value = "getChatMessageByUniqueId")
    @ResponseBody
    public DataResponse getChatMessageByUniqueId(RoomChatMessage params) {
        return this.roomChatMessageService.getChatMessageByUniqueId(params);
    }


    /**
     * 保存留言板
     *
     * @param message
     * @return
     */
    @RequestMapping(value = "saveMessageBoard")
    @ResponseBody
    public DataResponse saveMessageBoard(RoomMessageBoard message) {
        return this.roomMessageBoardService.saveMessageBoard(message);
    }


    /**
     * 获取留言版
     *
     * @param pageSearch
     * @param params
     * @return
     */
    @UnSession
    @RequestMapping(value = "getMessageBoardByUser")
    @ResponseBody
    public DataResponse getMessageBoardByUser(PageSearch pageSearch, RoomMessageBoard params) {
        return this.roomMessageBoardService.getMessageBoardByUser(pageSearch, params);
    }


    /**
     * 获取直播间公告
     *
     * @param pageSearch
     * @param params
     * @return
     */
    @UnSession
    @RequestMapping(value = "getRoomAnnouncementList")
    @ResponseBody
    public DataResponse getRoomAnnouncementList(PageSearch pageSearch, RoomAnnouncement params) {
        return this.settingApiService.getAnnouncementList(pageSearch, params);
    }


    /**
     * 获取直播间老师
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "getRoomTeacher")
    @ResponseBody
    public DataResponse getRoomTeacher(UserManageInfo params) {
        params.setGroupId(4);//老师标记
        return this.userInfoApiService.getManageUser(params);
    }


    /**
     * 获取直播间课程表
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "getRoomClass")
    @ResponseBody
    public DataResponse getRoomClass(RoomClass params) {
        params.setStatus(0);//未删除
        return this.liveRoomApiService.getLiveRoomClass(params);
    }


    /**
     * 获取直播间文件
     *
     * @param roomId 直播间id
     * @return 文件集合
     */
    @RequestMapping(value = "getRoomFile")
    @ResponseBody
    public DataResponse getRoomFile(Integer roomId) {
        RoomFile params = new RoomFile();
        params.setRoomId(roomId);
        params.setStatus(0);
        SearchCondition<RoomFile> condition = new SearchCondition<RoomFile>(params);
        List<RoomFile> fileList = this.roomFileService.findByCondition(condition);
        return new DataResponse(1000, fileList);
    }


    /**
     * 检测消息是否在黑白名单
     *
     * @param params
     * @return
     */
    @UnSession
    @RequestMapping(value = "checkMsg")
    @ResponseBody
    public DataResponse checkMsg(BaseWord params) {
        return this.baseWordService.checkMsg(params);
    }

    /**
     * 聊天发送图片
     *
     * @param chatImage
     * @return
     */
    @RequestMapping(value = "sendImage")
    @ResponseBody
    public DataResponse sendImg(MultipartFile chatImage) {
        try {
            if (EmptyUtil.isNotEmpty(chatImage)) {
                long size = chatImage.getSize();
                if (size > 1048576) {
                    return new DataResponse(6666, "请上传小于1M的图片");
                }
                String charImageUrl = "";
                baseLog.info(LogFormatUtil.getActionFormat("聊天图片开始上传"));
                InputStream input = chatImage.getInputStream();
                int count = input.available();
                byte[] fileByte = new byte[count];
                input.read(fileByte);
                String fileName = chatImage.getOriginalFilename();
                baseLog.info(LogFormatUtil.getActionFormat("当前上传请求的file的文件名:" + fileName));
                input.read(fileByte);
                fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
                String fileId = this.fileUploadRemoteService.chatImgFile(fileByte, fileName);
                charImageUrl = fileId + "." + fileName;
                baseLog.info(LogFormatUtil.getActionFormat("聊天图片上传结束"));
                baseLog.info(LogFormatUtil.getActionFormat("上传后的文件名：" + charImageUrl));
                return new DataResponse(1000, "图片上传成功", charImageUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DataResponse(9999, "图片上传失败");
        }
        return null;
    }
}

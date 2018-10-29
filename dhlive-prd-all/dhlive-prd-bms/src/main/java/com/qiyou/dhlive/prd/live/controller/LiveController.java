package com.qiyou.dhlive.prd.live.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.gson.Gson;
import com.qiyou.dhlive.api.base.outward.service.IFileUploadRemoteService;
import com.qiyou.dhlive.api.base.outward.service.ILiveRoomApiService;
import com.qiyou.dhlive.api.base.outward.service.IUserInfoApiService;
import com.qiyou.dhlive.api.base.outward.util.ExportXlsUtil;
import com.qiyou.dhlive.api.base.outward.util.TimeConverterUtil;
import com.qiyou.dhlive.api.base.outward.vo.ExtChatMsgVO;
import com.qiyou.dhlive.core.base.outward.model.BaseSysParam;
import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.qiyou.dhlive.core.live.outward.model.LiveRoom;
import com.qiyou.dhlive.core.room.outward.model.RoomClass;
import com.qiyou.dhlive.core.room.outward.model.RoomFile;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserGroupService;
import com.qiyou.dhlive.core.user.outward.service.IUserManageInfoService;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.qiyou.dhlive.prd.component.session.EmployeeSession;
import com.qiyou.dhlive.prd.live.vo.ChatMsgVO;
import com.qiyou.dhlive.prd.live.vo.ContentVO;
import com.qiyou.dhlive.prd.live.vo.DataVO;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.base.database.redis.RedisManager;
import com.yaozhong.framework.web.annotation.session.NeedSession;
import com.yaozhong.framework.web.annotation.session.UnSession;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/29
 */
@Controller
@RequestMapping(value = "live")
public class LiveController {

	private static Logger baseLog = LoggerFactory.getLogger("baseLog");

	@Autowired
	private ILiveRoomApiService liveRoomApiService;

	@Autowired
	private IUserInfoApiService userInfoApiService;

	@Autowired
	private IFileUploadRemoteService fileUploadRemoteService;

	@Autowired
	private IUserManageInfoService manageInfoService;

	@Autowired
	private IUserGroupService userGroupService;

	@Autowired
	@Qualifier("commonRedisManager")
	private RedisManager redisManager;

	@Autowired
	private IBaseSysParamService baseSysParamService;

	@NeedSession
	@UnSecurity
	@RequestMapping("")
	public String index(Model model) {
		EmployeeSession eSession = EmployeeSession.getEmployeeSession();
		if (eSession.getMobile().equals("18851029897")) {
			model.addAttribute("fish", eSession.getMobile());
		}
		return "live/index";
	}

	@NeedSession
	@UnSecurity
	@RequestMapping("room")
	public String room(Model model) {
		EmployeeSession eSession = EmployeeSession.getEmployeeSession();
		if (eSession.getMobile().equals("18851029897")) {
			model.addAttribute("fish", eSession.getMobile());
		}
		return "live/index";
	}

	@NeedSession
	@UnSecurity
	@RequestMapping("toDetailPage")
	public String toDetailPage(Model model, Integer roomId) {
		model.addAttribute("roomId", roomId);
		return "live/detail";
	}

	@NeedSession
	@UnSecurity
	@RequestMapping("toClassPage")
	public String toClassPage(Model model, Integer roomId) {
		model.addAttribute("roomId", roomId);
		return "live/class";
	}

	@NeedSession
	@UnSecurity
	@RequestMapping("toFilePage")
	public String toFilePage(Model model, Integer roomId) {
		model.addAttribute("roomId", roomId);
		return "live/file";
	}

	/**
	 * 保存直播间
	 *
	 * @param params
	 * @return
	 */
	@NeedSession
	@UnSecurity
	@RequestMapping("saveLiveRoom")
	@ResponseBody
	public DataResponse saveLiveRoom(LiveRoom params) {
		return this.liveRoomApiService.saveLiveRoom(params);
	}

	/**
	 * 获取直播室列表
	 *
	 * @param pageSearch
	 * @param params
	 * @return
	 */
	@NeedSession
	@UnSecurity
	@RequestMapping("getLiveRoomList")
	@ResponseBody
	public DataResponse getLiveRoomList(PageSearch pageSearch, LiveRoom params) {
		return this.liveRoomApiService.getLiveRoomList(pageSearch, params);
	}

	/**
	 * 根据后台用户id获取后台用户详情
	 *
	 * @param params
	 *            后台用户对象
	 * @return
	 */
	@NeedSession
	@UnSecurity
	@RequestMapping(value = "getLiveRoomById")
	@ResponseBody
	public DataResponse getLiveRoomById(LiveRoom params) {
		List<LiveRoom> result = this.liveRoomApiService.getLiveRoom(params);
		if (EmptyUtil.isEmpty(result)) {
			return new DataResponse(1001, "not found");
		}
		return new DataResponse(1000, result.get(0));
	}

	/**
	 * 删除直播间
	 *
	 * @param params
	 * @return
	 */
	@NeedSession
	@UnSecurity
	@RequestMapping(value = "deleteLiveRoom")
	@ResponseBody
	public DataResponse deleteLiveRoom(LiveRoom params) {
		return this.liveRoomApiService.deleteLiveRoom(params);
	}

	// -----------------------------------------------------直播间详情---------------------------------------------------

	/**
	 * 保存直播间文件
	 *
	 * @param params
	 * @return
	 */
	@NeedSession
	@UnSecurity
	@RequestMapping(value = "saveRoomFile")
	@ResponseBody
	public DataResponse saveRoomFile(RoomFile params, MultipartFile file) {
		String fileUrl = "";
		try {
			if (EmptyUtil.isNotEmpty(file)) {
				baseLog.info(LogFormatUtil.getActionFormat("开始上传文件"));
				InputStream input = file.getInputStream();
				int count = input.available();
				byte[] fileByte = new byte[count];
				input.read(fileByte);
				String fileName = file.getOriginalFilename();
				baseLog.info(LogFormatUtil.getActionFormat("当前上传请求的file的文件名:" + fileName));
				input.read(fileByte);
				fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
				String fileId = this.fileUploadRemoteService.storeFile(fileByte, fileName);
				fileUrl = fileId + "." + fileName;
				baseLog.info(LogFormatUtil.getActionFormat("结束上传文件"));
				baseLog.info(LogFormatUtil.getActionFormat("上传后的文件名：" + fileUrl));
			}
			params.setFileUrl(fileUrl);
			params.setCreateTime(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.liveRoomApiService.saveRoomFile(params);
	}

	/**
	 * 获取直播室文件列表
	 *
	 * @param pageSearch
	 * @param params
	 * @return
	 */
	@NeedSession("live/getLiveRoomFileList")
	@UnSecurity
	@RequestMapping("getLiveRoomFileList")
	@ResponseBody
	public DataResponse getLiveRoomFileList(PageSearch pageSearch, RoomFile params) {
		return this.liveRoomApiService.getLiveRoomFileList(pageSearch, params);
	}

	/**
	 * 通过id获取直播间文件
	 *
	 * @param params
	 * @return
	 */
	@NeedSession
	@UnSecurity
	@RequestMapping("getLiveRoomFileById")
	@ResponseBody
	public DataResponse getLiveRoomFileById(RoomFile params) {
		List<RoomFile> result = (List<RoomFile>) this.liveRoomApiService.getLiveRoomFile(params).getData();
		if (EmptyUtil.isNotEmpty(result)) {
			return new DataResponse(1000, result.get(0));
		} else {
			return new DataResponse(1001, "not found");
		}
	}

	/**
	 * 删除直播室文件
	 *
	 * @param params
	 * @return
	 */
	@NeedSession
	@UnSecurity
	@RequestMapping("deleteFile")
	@ResponseBody
	public DataResponse deleteFile(RoomFile params) {
		return this.liveRoomApiService.deleteFile(params);
	}

	/**
	 * 获取直播室老师列表
	 *
	 * @return
	 */
	@NeedSession
	@UnSecurity
	@RequestMapping(value = "getLiveRoomTeacherList")
	@ResponseBody
	public DataResponse getLiveRoomTeacherList(PageSearch pageSearch, UserManageInfo params) {
		params.setGroupId(4);// 老师角色
		return this.userInfoApiService.getManageUserList(pageSearch, params);
	}

	/**
	 * 保存直播间课程表
	 *
	 * @param params
	 * @return
	 */
	@NeedSession
	@UnSecurity
	@RequestMapping(value = "saveRoomClass")
	@ResponseBody
	public DataResponse saveRoomClass(RoomClass params, MultipartFile file) {
		String fileUrl = "";
		try {
			if (EmptyUtil.isNotEmpty(file)) {
				baseLog.info(LogFormatUtil.getActionFormat("开始上传文件"));
				InputStream input = file.getInputStream();
				int count = input.available();
				byte[] fileByte = new byte[count];
				input.read(fileByte);
				String fileName = file.getOriginalFilename();
				baseLog.info(LogFormatUtil.getActionFormat("当前上传请求的file的文件名:" + fileName));
				input.read(fileByte);
				fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
				String fileId = this.fileUploadRemoteService.storeFile(fileByte, fileName);
				fileUrl = fileId + "." + fileName;
				baseLog.info(LogFormatUtil.getActionFormat("结束上传文件"));
				baseLog.info(LogFormatUtil.getActionFormat("上传后的文件名：" + fileUrl));
				params.setClassUrl(fileUrl);
				params.setCreateTime(new Date());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.liveRoomApiService.saveRoomClass(params);
	}

	/**
	 * 获取直播室课程表
	 *
	 * @param pageSearch
	 * @param params
	 * @return
	 */
	@NeedSession("live/getLiveRoomClassList")
	@UnSecurity
	@RequestMapping("getLiveRoomClassList")
	@ResponseBody
	public DataResponse getLiveRoomClassList(PageSearch pageSearch, RoomClass params) {
		return this.liveRoomApiService.getLiveRoomClassList(pageSearch, params);
	}

	/**
	 * 通过id获取直播间课程表
	 *
	 * @param params
	 * @return
	 */
	@NeedSession
	@UnSecurity
	@RequestMapping("getLiveRoomClassById")
	@ResponseBody
	public DataResponse getLiveRoomClassById(RoomClass params) {
		List<RoomClass> result = (List<RoomClass>) this.liveRoomApiService.getLiveRoomClass(params).getData();
		if (EmptyUtil.isNotEmpty(result)) {
			return new DataResponse(1000, result.get(0));
		} else {
			return new DataResponse(1001, "not found");
		}
	}

	/**
	 * 删除直播室文件
	 *
	 * @param params
	 * @return
	 */
	@NeedSession
	@UnSecurity
	@RequestMapping("deleteClass")
	@ResponseBody
	public DataResponse deleteClass(RoomClass params) {
		return this.liveRoomApiService.deleteClass(params);
	}

	/**
	 * 导出聊天消息
	 */
	@UnSession
	@UnSecurity
	@RequestMapping("exportChatMsg")
	@ResponseBody
	public DataResponse exportChatMsg(HttpServletResponse response) {
		List<ExtChatMsgVO> exts = new ArrayList<ExtChatMsgVO>();
		List<String> listJson = redisManager.getMapValueFromMapByStoreKey(RedisKeyConstant.MESSAGE_INFO);
		if (EmptyUtil.isEmpty(listJson)) {
			return new DataResponse(1001, "没有需要导出的数据.");
		}
		for (int i = 0; i < listJson.size(); i++) {
			String temp = listJson.get(i);
			ChatMsgVO chat = new Gson().fromJson(listJson.get(i), ChatMsgVO.class);
			ContentVO content = new Gson().fromJson(chat.getContent(), ContentVO.class);
			String baseJson = content.getElems().get(0).getContent().getData();
			DataVO base = new Gson().fromJson(baseJson, DataVO.class);
			String dataJson = content.getElems().get(1).getContent().getData();
			String textJson = content.getElems().get(1).getContent().getText();

			ExtChatMsgVO ext = new ExtChatMsgVO();
			ext.setMsgId(base.getUniqueId());
			ext.setGroupName(this.userGroupService.findById(Integer.parseInt(base.getGroupId())).getName());
			if (content.getElems().get(1).getType().equals("TIMFaceElem")) {
				ext.setContent(dataJson);
			} else {
				ext.setContent(textJson);
			}
			ext.setSendUserName(base.getPostNickName());
			ext.setSendTime(TimeConverterUtil.utc2Local(base.getSendTime(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
					"yyyy-MM-dd HH:mm:ss"));
			if (EmptyUtil.isNotEmpty(base.getAuditUid())) {
				String auditUserName = this.manageInfoService.findById(Integer.parseInt(base.getAuditUid()))
						.getUserNickName();
				ext.setAuditUserName(auditUserName);
			} else {
				ext.setAuditUserName("");
			}
			ext.setLevel(base.getLevel());
			ext.setAuditTime(TimeConverterUtil.utc2Local(base.getAuditTime(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
					"yyyy-MM-dd HH:mm:ss"));
			if (EmptyUtil.isNotEmpty(base.getSmall())) {
				ext.setSmall("小号");
			} else {
				ext.setSmall("");
			}
			if (EmptyUtil.isNotEmpty(chat.getIsDelete())) {
				if (chat.getIsDelete().intValue() == 0) {
					ext.setDelete("未删除");
				} else {
					ext.setDelete("已删除");
				}
			} else {
				ext.setDelete("未删除");
			}
			if (EmptyUtil.isNotEmpty(base.getIsSpecial())) {
				ext.setRemark("特殊表情");
			}
			ext.setMsgType(content.getElems().get(1).getType());
			ext.setThrough(base.getCheckStatus());
			exts.add(ext);
		}

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("msgId", "消息id");
		map.put("msgType", "消息类型");
		map.put("groupName", "角色");
		map.put("sendUserName", "发送人");
		map.put("level", "等级");
		map.put("sendTime", "发送时间");
		map.put("content", "内容");
		map.put("auditUserName", "审核人");
		map.put("auditTime", "审核时间");
		map.put("small", "是否小号");
		map.put("through", "是否通过");
		map.put("delete", "是否删除");
		map.put("remark", "备注");

		Ordering<ExtChatMsgVO> sortByStatus = Ordering.natural().onResultOf(new Function<ExtChatMsgVO, String>() {
			public String apply(ExtChatMsgVO data) {
				return data.getSendTime();
			}
		});
		List<ExtChatMsgVO> data = sortByStatus.sortedCopy(exts);

		try {
			ExportXlsUtil.listToExcel(data, map, "聊天消息", data.size(), response, "聊天消息");
			return new DataResponse(1000, "聊天消息导出成功.");
		} catch (Exception e) {
			e.printStackTrace();
			return new DataResponse(1001, "聊天消息导出失败." + e.getMessage());
		}
	}

	@NeedSession
	@UnSecurity
	@RequestMapping(value = "uploadRoomSyllabus")
	@ResponseBody
	public DataResponse uploadRoomSyllabus(MultipartFile file) {
		String fileUrl = "";
		try {
			if (EmptyUtil.isNotEmpty(file)) {
				baseLog.info(LogFormatUtil.getActionFormat("开始上传文件"));
				InputStream input = file.getInputStream();
				int count = input.available();
				byte[] fileByte = new byte[count];
				input.read(fileByte);
				String fileName = file.getOriginalFilename();
				baseLog.info(LogFormatUtil.getActionFormat("当前上传请求的file的文件名:" + fileName));
				input.read(fileByte);
				fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
				String fileId = this.fileUploadRemoteService.storeFile(fileByte, fileName);
				fileUrl = fileId + "." + fileName;
				baseLog.info(LogFormatUtil.getActionFormat("结束上传文件"));
				baseLog.info(LogFormatUtil.getActionFormat("上传后的文件名：" + fileUrl));
				BaseSysParam param = new BaseSysParam();
				param.setParamKey("roomSyllabus");
				param = this.baseSysParamService.findOneByCondition(new SearchCondition<BaseSysParam>(param));
				if (EmptyUtil.isEmpty(param)) {
					param = new BaseSysParam();
					param.setParamKey("roomSyllabus");
					param.setParamValue(fileUrl);
					this.baseSysParamService.save(param);
					baseSysParamService.updateValueByKey("roomSyllabus");
				} else {
					param.setParamValue(fileUrl);
					this.baseSysParamService.modifyEntity(param);
					baseSysParamService.updateValueByKey("roomSyllabus");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new DataResponse();
	}

}

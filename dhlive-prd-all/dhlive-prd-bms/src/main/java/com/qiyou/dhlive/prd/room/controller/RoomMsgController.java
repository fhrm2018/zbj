package com.qiyou.dhlive.prd.room.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.core.room.outward.model.RoomMsgCount;
import com.qiyou.dhlive.core.room.outward.service.IRoomMsgCountService;
import com.qiyou.dhlive.core.room.outward.vo.RoomMsgCountVO;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.qiyou.dhlive.prd.component.session.EmployeeSession;
import com.yaozhong.framework.base.common.utils.DateStyle;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.web.annotation.session.NeedSession;

@Controller
@RequestMapping(value = "roomMsg")
public class RoomMsgController {

	@Autowired
	private IRoomMsgCountService roomMsgCountService;
	
	@Autowired
	private IBaseCacheService baseCacheService;

	@NeedSession
	@UnSecurity
	@RequestMapping("")
	public String index(Model model) {
		EmployeeSession eSession = EmployeeSession.getEmployeeSession();
		List<String> nameList=Lists.newArrayList();
		List<UserManageInfo> userManageList=this.baseCacheService.getManageUserList(4);
		for(UserManageInfo m:userManageList) {
			if(m.getGroupId().intValue()==3) {
				nameList.add(m.getUserNickName());
			}
		}
		int width=85;
		if(EmptyUtil.isNotEmpty(nameList))
			width=85/nameList.size();
		model.addAttribute("namesList", nameList);
		model.addAttribute("width", width);
		return "count/index";
	}
	
	
	@NeedSession
	@UnSecurity
	@RequestMapping("/msgCount")
	@ResponseBody
	public DataResponse getPageDate(PageSearch ps,RoomMsgCountVO vo) {
		int index = (ps.getPage()-1)*15;
		vo.setIndex(index);
		List<RoomMsgCount> list = this.roomMsgCountService.findBySearch(vo);
		Long count = this.roomMsgCountService.countBySearch(vo);
		List<Object> dateList = Lists.newArrayList();
		for(RoomMsgCount c:list) {
			dateList.add(c.getSendDate());
		}
		List<Object> userIdList=Lists.newArrayList();
		List<RoomMsgCount> countList=Lists.newArrayList();
		List<UserManageInfo> userManageList=this.baseCacheService.getManageUserList(4);
		for(UserManageInfo m:userManageList) {
			if(m.getGroupId().intValue()==3) {
				userIdList.add(m.getUserId());
			}
		}
		if(EmptyUtil.isNotEmpty(dateList)) {
			SearchCondition<RoomMsgCount> condition=new SearchCondition<RoomMsgCount>(new RoomMsgCount());
			Map<String,List<Object>> inMap=Maps.newLinkedHashMap();
			inMap.put("sendDate", dateList);
			inMap.put("userId", userIdList);
			condition.setInConditions(inMap);
			countList = this.roomMsgCountService.findByCondition(condition);
		}
		Map<String,List<RoomMsgCount>> convertListToMap=Maps.newHashMap();
		for(RoomMsgCount rc:countList) {
			if(EmptyUtil.isEmpty(convertListToMap.get(DateUtil.DateToString(rc.getSendDate(), DateStyle.YYYY_MM_DD)))){
				List<RoomMsgCount> clist=Lists.newArrayList();
				clist.add(rc);
				convertListToMap.put(DateUtil.DateToString(rc.getSendDate(), DateStyle.YYYY_MM_DD),clist);
			}else {
				List<RoomMsgCount> clist=convertListToMap.get(DateUtil.DateToString(rc.getSendDate(), DateStyle.YYYY_MM_DD));
				clist.add(rc);
			}
		}
		
		List<Map<String,Object>> rows=Lists.newArrayList();
		
		for(RoomMsgCount c:list) {
			Map<String,Object> data=Maps.newHashMap();
			String key=DateUtil.DateToString(c.getSendDate(), DateStyle.YYYY_MM_DD);
			data.put("date", key);
			List<Object> dataList=Lists.newArrayList();
			for(Object o : userIdList) {
				Integer userId=Integer.valueOf(o.toString());
				int i=0;
				for(RoomMsgCount mc:convertListToMap.get(key)) {
					if(userId.equals(mc.getUserId())) {
						i=mc.getSendCount();
						break;
					}
				}
				dataList.add(i);
			}
			data.put("data", dataList);
			rows.add(data);
		}
		List<Object> sumList=Lists.newArrayList();
		for(int i=0;i<rows.size();i++) {
			Map<String,Object> m=rows.get(i);
			List<Object> dataList=(List<Object>) m.get("data");
			for(int j=0;j<dataList.size();j++) {
				if(EmptyUtil.isEmpty(sumList)) {
					sumList.add(dataList.get(j));
				}else {
					if(sumList.size()-1<j) {
						sumList.add(dataList.get(j));
					}else {
					int c=Integer.valueOf(sumList.get(j).toString()).intValue()+Integer.parseInt(dataList.get(j).toString());
					sumList.set(j, c);
					}
				}
			}
		}
		Map<String,Object> data1=Maps.newHashMap();
		data1.put("date", "合计");
		data1.put("data", sumList);
		rows.add(data1);
		PageResult<Map<String,Object>> pr= new PageResult<Map<String,Object>>();
		pr.setRows(rows);
		pr.setTotal(count);
		return new DataResponse(1000,pr);
	}

}

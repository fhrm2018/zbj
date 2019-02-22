package com.qiyou.dhlive.prd.room.controller;

import java.util.Date;
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
import com.qiyou.dhlive.core.room.outward.model.RoomGuestCount;
import com.qiyou.dhlive.core.room.outward.service.IRoomGuestCountService;
import com.qiyou.dhlive.core.room.outward.vo.RoomGuestCountVO;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.model.UserRelation;
import com.qiyou.dhlive.core.user.outward.service.IUserRelationService;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.qiyou.dhlive.prd.component.session.EmployeeSession;
import com.yaozhong.framework.base.common.utils.DateStyle;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.RangeCondition;
import com.yaozhong.framework.base.database.domain.search.RangeConditionType;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.web.annotation.session.NeedSession;

@Controller
@RequestMapping(value = "roomGuest")
public class RoomGuestController {

	@Autowired
	private IRoomGuestCountService roomGuestCountService;
	
	@Autowired
	private IBaseCacheService baseCacheService;
	
	@Autowired
	private IUserRelationService userRelationService;

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
		Date date = DateUtil.dateTimeToDate(new Date());
		model.addAttribute("namesList", nameList);
		model.addAttribute("width", width);
		model.addAttribute("beginDate", DateUtil.DateToString(DateUtil.addDay(date, -7), DateStyle.YYYY_MM_DD));
		model.addAttribute("endDate", DateUtil.DateToString(date, DateStyle.YYYY_MM_DD));
		return "guest/index";
	}
	
	
	@NeedSession
	@UnSecurity
	@RequestMapping("/guestCount")
	@ResponseBody
	public DataResponse getPageDate(PageSearch ps,RoomGuestCountVO vo) {
		int index = (ps.getPage()-1)*15;
		vo.setIndex(index);
		List<RoomGuestCount> list = this.roomGuestCountService.findBySearch(vo);
		Long count = this.roomGuestCountService.countBySearch(vo);
		List<Object> dateList = Lists.newArrayList();
		for(RoomGuestCount c:list) {
			dateList.add(c.getGuestDate());
		}
		List<Object> userIdList=Lists.newArrayList();
		List<RoomGuestCount> countList=Lists.newArrayList();
		List<UserManageInfo> userManageList=this.baseCacheService.getManageUserList(4);
		for(UserManageInfo m:userManageList) {
			if(m.getGroupId().intValue()==3) {
				userIdList.add(m.getUserId());
			}
		}
		if(EmptyUtil.isNotEmpty(dateList)) {
			SearchCondition<RoomGuestCount> condition=new SearchCondition<RoomGuestCount>(new RoomGuestCount());
			Map<String,List<Object>> inMap=Maps.newLinkedHashMap();
			inMap.put("sendDate", dateList);
			inMap.put("userId", userIdList);
			condition.setInConditions(inMap);
			countList = this.roomGuestCountService.findByCondition(condition);
		}
		Map<String,List<RoomGuestCount>> convertListToMap=Maps.newHashMap();
		for(RoomGuestCount rc:countList) {
			if(EmptyUtil.isEmpty(convertListToMap.get(DateUtil.DateToString(rc.getGuestDate(), DateStyle.YYYY_MM_DD)))){
				List<RoomGuestCount> clist=Lists.newArrayList();
				clist.add(rc);
				convertListToMap.put(DateUtil.DateToString(rc.getGuestDate(), DateStyle.YYYY_MM_DD),clist);
			}else {
				List<RoomGuestCount> clist=convertListToMap.get(DateUtil.DateToString(rc.getGuestDate(), DateStyle.YYYY_MM_DD));
				clist.add(rc);
			}
		}
		
		List<Map<String,Object>> rows=Lists.newArrayList();
		
		for(RoomGuestCount c:list) {
			Map<String,Object> data=Maps.newHashMap();
			String key=DateUtil.DateToString(c.getGuestDate(), DateStyle.YYYY_MM_DD);
			data.put("date", key);
			List<Object> dataList=Lists.newArrayList();
			for(Object o : userIdList) {
				Integer userId=Integer.valueOf(o.toString());
				int i=0;
				for(RoomGuestCount mc:convertListToMap.get(key)) {
					if(userId.equals(mc.getUserId())) {
						i=mc.getGuestCount();
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
	
	@NeedSession
	@UnSecurity
	@RequestMapping("/saveGuestCount")
	@ResponseBody
	public void saveGuestCount(String begin) {
    	String beginStr=begin+" 00:00:00";
    	Date beginDate = DateUtil.StringToDate(beginStr, DateStyle.YYYY_MM_DD);
    	Date now = DateUtil.dateTimeToDate(new Date());
    	
    	
    	List<UserManageInfo> manageList=baseCacheService.getManageUserList(4);
    	while(true) {
    		if(beginDate.getTime()>now.getTime()) {
    			break;
    		}
        	Date endDate = DateUtil.addDay(beginDate, 1);
	    	for(UserManageInfo m:manageList) {
	    		if(m.getGroupId().intValue()!=3)
	    			continue;
	    		
	    		UserRelation ur=new UserRelation();
	    		ur.setRelationUserId(m.getUserId());
	    		SearchCondition<UserRelation> con=new SearchCondition<UserRelation>(ur);
				List<RangeCondition> ranges=Lists.newArrayList();
				ranges.add(new RangeCondition("createTime", beginDate, RangeConditionType.GreaterThanOrEqual));
				ranges.add(new RangeCondition("createTime", endDate, RangeConditionType.LessThan));
				con.setRangeConditions(ranges);
				Long count = this.userRelationService.countByCondition(con);
				
				RoomGuestCount online=new RoomGuestCount();
	    		online.setUserId(m.getUserId());
	    		SearchCondition<RoomGuestCount> con1=new SearchCondition<RoomGuestCount>(online);
				List<RangeCondition> ranges1=Lists.newArrayList();
				ranges1.add(new RangeCondition("guestDate", beginDate, RangeConditionType.GreaterThanOrEqual));
				ranges1.add(new RangeCondition("guestDate", endDate, RangeConditionType.LessThan));
				con1.setRangeConditions(ranges1);
	    		online = this.roomGuestCountService.findOneByCondition(con1);
	    		if(EmptyUtil.isEmpty(online)) {
	    			online.setUserId(m.getUserId());
	    			online.setGuestDate(endDate);
	    			online.setGuestCount(count.intValue());
	    			online.setCreateTime(new Date());
	    			this.roomGuestCountService.save(online);
	    		}else {
	    			online.setGuestCount(count.intValue());
	    			this.roomGuestCountService.modifyEntity(online);
	    		}
	    		
	    	}
	    	beginDate=DateUtil.addDay(beginDate, 1);
    	}
    }
	
	
	public static void main(String[] args) {
		String beginStr="2019-01-05 00:00:00";
    	String endStr="2019-01-04 00:00:00";
    	Date b = DateUtil.StringToDate(beginStr, DateStyle.YYYY_MM_DD_HH_MM_SS);
    	Date e = DateUtil.StringToDate(endStr, DateStyle.YYYY_MM_DD_HH_MM_SS);
 
    	System.out.println(e.getTime()>b.getTime());
	}

}

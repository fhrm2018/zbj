package com.qiyou.dhlive.prd.login.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qiyou.dhlive.core.bms.outward.model.BmsEmployeeInfo;
import com.qiyou.dhlive.core.bms.outward.service.IBmsEmployeeInfoService;
import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserInfoService;
import com.qiyou.dhlive.core.user.outward.service.IUserVipInfoService;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.qiyou.dhlive.prd.component.util.ExcelUtil;
import com.yaozhong.framework.base.common.utils.DateStyle;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.RangeCondition;
import com.yaozhong.framework.base.database.domain.search.RangeConditionType;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.web.annotation.session.NeedSession;

@Controller
@RequestMapping("/market")
public class MarketController {
	

	private static Logger baseLog = LoggerFactory.getLogger("baseLog");

	
	@Autowired
	private IUserInfoService userInfoService;
	
	@Autowired
	private IUserVipInfoService userVipInfoService;
	
	@Autowired
	private IBmsEmployeeInfoService bmsEmployeeInfoService;
	
	@RequestMapping(value="")
	public String index(Model model) {
		return "market/index";
	}
	
	@NeedSession
	@UnSecurity
	@RequestMapping(value="getData")
	@ResponseBody
	public DataResponse getData(PageSearch ps,String searchDate) {
		Date d=null;
		if(EmptyUtil.isNotEmpty(searchDate)) {
			d=DateUtil.StringToDate(searchDate,DateStyle.YYYY_MM_DD);
		}
		
		SearchCondition<UserVipInfo> condition = new SearchCondition<UserVipInfo>(new UserVipInfo(),ps);
		if(EmptyUtil.isNotEmpty(d)) {
			List<RangeCondition> ranges=Lists.newArrayList();
			ranges.add(new RangeCondition("createTime", d, RangeConditionType.GreaterThanOrEqual));
			ranges.add(new RangeCondition("createTime", DateUtil.addDay(d, 1), RangeConditionType.LessThan));
			condition.setRangeConditions(ranges);
		}
		condition.buildOrderByConditions("createTime", "desc");
		PageResult<UserVipInfo> pr = this.userVipInfoService.findByPage(condition);
		
		List<Map<String,Object>> mapList=Lists.newArrayList();
		
		for(UserVipInfo v : pr.getRows()) {
			Map<String,Object> m =Maps.newHashMap();
			m.put("vipName", v.getUserNickName());
			String employeeName="";
			BmsEmployeeInfo e=this.bmsEmployeeInfoService.findById(v.getCreateUserId());
			if(EmptyUtil.isNotEmpty(e)) {
				employeeName=e.getName();
			}
			m.put("createUserName", employeeName);
			m.put("createTime", DateUtil.DateToString(v.getCreateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));
			if(EmptyUtil.isEmpty(v.getFirstLoginIp())) {
				m.put("fromUrl", "");
				m.put("utmSource","");
				m.put("ip", v.getFirstLoginIp()==null?"":v.getFirstLoginIp());
			}else {
				UserInfo u=new UserInfo();
				u.setCreateIp(v.getFirstLoginIp());
				u=this.userInfoService.findOneByCondition(new SearchCondition<UserInfo>(u));
				m.put("fromUrl", u.getFromUrl());
				m.put("utmSource", u.getUtmSource());
				m.put("ip", v.getFirstLoginIp()==null?"":v.getFirstLoginIp());
			}
			mapList.add(m);
		}
		
		PageResult<Map<String,Object>> pr1=new PageResult<Map<String,Object>>();
		pr1.setRows(mapList);
		pr1.setTotal(pr.getTotal());
		return new DataResponse(1000,pr1);
	}

	@NeedSession
	@UnSecurity
	@RequestMapping(value="export")
	@ResponseBody
	public void exportData(String searchDate,HttpServletResponse response) {
		Date d=null;
		if(EmptyUtil.isNotEmpty(searchDate)) {
			d=DateUtil.StringToDate(searchDate,DateStyle.YYYY_MM_DD);
		}
		
		SearchCondition<UserVipInfo> condition = new SearchCondition<UserVipInfo>(new UserVipInfo());
		if(EmptyUtil.isNotEmpty(d)) {
			List<RangeCondition> ranges=Lists.newArrayList();
			ranges.add(new RangeCondition("createTime", d, RangeConditionType.GreaterThanOrEqual));
			ranges.add(new RangeCondition("createTime", DateUtil.addDay(d, 1), RangeConditionType.LessThan));
			condition.setRangeConditions(ranges);
		}
		condition.buildOrderByConditions("createTime", "desc");
		List<UserVipInfo> pr = this.userVipInfoService.findByCondition(condition);
		
		List<Map<String,Object>> mapList=Lists.newArrayList();
		int i=0;
		for(UserVipInfo v : pr) {
			i=i+1;
			Map<String,Object> m =Maps.newHashMap();
			m.put("index", i);
			m.put("vipName", v.getUserNickName());
			String employeeName="";
			BmsEmployeeInfo e=this.bmsEmployeeInfoService.findById(v.getCreateUserId());
			if(EmptyUtil.isNotEmpty(e)) {
				employeeName=e.getName();
			}
			m.put("createUserName", employeeName);
			m.put("createTime", DateUtil.DateToString(v.getCreateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));
			if(EmptyUtil.isEmpty(v.getFirstLoginIp())) {
				m.put("fromUrl", "");
				m.put("utmSource","");
				m.put("ip", v.getFirstLoginIp()==null?"":v.getFirstLoginIp());
			}else {
				UserInfo u=new UserInfo();
				u.setCreateIp(v.getFirstLoginIp());
				u=this.userInfoService.findOneByCondition(new SearchCondition<UserInfo>(u));
				m.put("fromUrl", u.getFromUrl());
				m.put("utmSource", u.getUtmSource());
				m.put("ip", v.getFirstLoginIp()==null?"":v.getFirstLoginIp());
			}
			mapList.add(m);
		}
		try {
			ExcelUtil.listToExcelMap(mapList, getFieldMap(), "注册vip用户", response,"市场数据报表",DateUtil.DateToString(d, DateStyle.YYYY_MM_DD),DateUtil.DateToString(d, DateStyle.YYYY_MM_DD),65535);
		} catch (Exception e) {
			baseLog.info(LogFormatUtil.getActionErrorFormat("请假报表导出：数据源没有数据"));
		}
	}
	
	
	public LinkedHashMap<String, String> getFieldMap(){
		LinkedHashMap<String, String> maps=Maps.newLinkedHashMap();
		maps.put("index", "序号");
		maps.put("vipName", "vip名称");
		maps.put("createUserName", "创建人");
		maps.put("fromUrl", "来源");
		maps.put("ip", "注册ip");
		maps.put("createTime", "创建时间");
		return maps;
	}
	
}

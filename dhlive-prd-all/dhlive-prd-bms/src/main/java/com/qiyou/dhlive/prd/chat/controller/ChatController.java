package com.qiyou.dhlive.prd.chat.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.core.base.outward.model.BaseOptLog;
import com.qiyou.dhlive.core.base.outward.service.IBaseOptLogService;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.yaozhong.framework.base.common.utils.DateStyle;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.page.builders.PageResultBuilder;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.RangeCondition;
import com.yaozhong.framework.base.database.domain.search.RangeConditionType;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.web.annotation.session.NeedSession;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/29
 */
@Controller
@RequestMapping(value = "chat")
public class ChatController {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IBaseOptLogService baseOptLogService;
    
    @Autowired
    private IBaseCacheService baseCacheService;

    @NeedSession
    @UnSecurity
    @RequestMapping("")
    public String index(Model model) {
        return "chat/chatAudit";
    }

    
    private String getOptNameFromList(Integer userId,List<UserManageInfo> list) {
    	String rs = "";
    	if(EmptyUtil.isEmpty(list) || EmptyUtil.isEmpty(userId)) {
    		return rs;
    	}
    	for(UserManageInfo data:list) {
    		if(userId.intValue() == data.getUserId().intValue()) {
    			return data.getUserNickName();
    		}
    	}
    	return rs;
    }
    
    
    private List<Object> getOptIdFromListByLickName(String name,List<UserManageInfo> list) {
    	List<Object> rs = Lists.newArrayList();
    	if(EmptyUtil.isEmpty(list) || EmptyUtil.isEmpty(name)) {
    		return rs;
    	}
    	for(UserManageInfo data:list) {
    		String userName = data.getUserNickName();
			if(EmptyUtil.isNotEmpty(userName)) {
				if(userName.indexOf(name) != -1) {
					rs.add(data.getUserId());
				}
			}
    	}
    	return rs;
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * 游客列表(分页)
     *
     * @param pageSearch
     * @return
     */
    @NeedSession("/chat/getChatList")
    @UnSecurity
    @RequestMapping("getChatList")
    @ResponseBody
    public DataResponse getTouristsUserList(PageSearch pageSearch, BaseOptLog params,HttpServletRequest request) {
	   String opName = request.getParameter("opName");
	   String ts = request.getParameter("ts");
	   String tt = request.getParameter("tt");
	   List<UserManageInfo> manageList = this.baseCacheService.getManageUserList(4);
	   BaseOptLog searchParam = new BaseOptLog();
	   //searchParam.setType(0);
	   SearchCondition<BaseOptLog> con = new SearchCondition<BaseOptLog>(searchParam,pageSearch);
       con.buildOrderByConditions("optTime", "desc");
	   if(EmptyUtil.isNotEmpty(opName)) {
		   List<Object> optIds =getOptIdFromListByLickName(opName,manageList);
		   if(optIds != null && optIds.isEmpty()) {
			   PageResult<BaseOptLog> nuList = new PageResult<BaseOptLog>();
			   return new DataResponse(1000,nuList);

		   }
		   con.buildInConditions("optUserId", optIds);
	   }  
	   if(EmptyUtil.isNotEmpty(params.getOpeMsg())) {
   		   con.buildLikeConditions("opeMsg", "%"+params.getOpeMsg()+"%");
   	   }
	   if(EmptyUtil.isNotEmpty(ts)) {
		   Date tsDate = DateUtil.StringToDate(ts, DateStyle.YYYY_MM_DD_HH_MM_SS);
		   RangeCondition range = new RangeCondition("optTime",tsDate,RangeConditionType.GreaterThanOrEqual);
		   con.buildRangeConditions(range);
	   }
	   
	   if(EmptyUtil.isNotEmpty(tt)) {
		   Date ttDate = DateUtil.StringToDate(tt, DateStyle.YYYY_MM_DD_HH_MM_SS);
		   RangeCondition range = new RangeCondition("optTime",ttDate,RangeConditionType.LessThanOrEqual);
		   con.buildRangeConditions(range);
	   }
	       PageResult<BaseOptLog> pageData = this.baseOptLogService.findByPage(con);
	   
	   List<Map<String,Object>> dataList = Lists.newArrayList();
	   for( BaseOptLog log:pageData.getRows()) {
		   String optNa = this.getOptNameFromList(log.getOptUserId(), manageList);
		   Map<String,Object> data = Maps.newHashMap();
		   data.put("optName", optNa);
		   data.put("optTime", log.getOptTime());
		   data.put("optMsg", log.getOpeMsg());
		   dataList.add(data);
	  }
	       PageResult<Map<String,Object>> rs = new  PageResult<Map<String,Object>> ();
	       rs.setRows(dataList);
	       rs.setTotal(pageData.getTotal());
	       return new DataResponse(1000,rs);
	   
     }
   
}

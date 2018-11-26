package com.qiyou.dhlive.prd.water.controller;

import java.util.ArrayList;
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

import com.github.pagehelper.PageHelper;
import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.api.base.outward.service.IUserInfoApiService;
import com.qiyou.dhlive.core.live.outward.model.LiveC2CMessage;
import com.qiyou.dhlive.core.live.outward.service.ILiveC2CMessageService;
import com.qiyou.dhlive.core.live.service.dao.ILiveC2CMessageMapper;
import com.qiyou.dhlive.core.room.outward.model.RoomAutoMsg;
import com.qiyou.dhlive.core.user.outward.model.UserGroup;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserManageInfoService;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.qiyou.dhlive.prd.component.session.EmployeeSession;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.MyBeanUtils;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.page.builders.PageResultBuilder;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.web.annotation.session.NeedSession;

@Controller
@RequestMapping(value = "water")
public class WaterController {
	
	private static Logger baseLog = LoggerFactory.getLogger("baseLog");
	
	@Autowired
    private IUserInfoApiService userInfoApiService;

    
    @Autowired
    private IBaseCacheService baseCacheService;
    
    @Autowired
    private ILiveC2CMessageService liveC2CMessageService;
    
    @Autowired
    private IUserManageInfoService userManageInfoService;

	    @NeedSession
	    @UnSecurity
	    @RequestMapping("")
	    public String water(Model model) {
	    	 UserManageInfo params = new UserManageInfo();
	              params.setGroupId(3);
	         SearchCondition<UserManageInfo> condition = new SearchCondition<UserManageInfo>(params);
	         List<UserManageInfo> assistant = this.userManageInfoService.findByCondition(condition);
	         model.addAttribute("assistant", assistant);
	        return "water/index";
	    }
	    
	    @NeedSession
	    @UnSecurity
	    @RequestMapping("chatWater")
	    public String chatWater(Model model) {
	    	UserManageInfo params = new UserManageInfo();
              params.setGroupId(3);
            SearchCondition<UserManageInfo> condition = new SearchCondition<UserManageInfo>(params);
            List<UserManageInfo> assistant = this.userManageInfoService.findByCondition(condition);
         model.addAttribute("assistant", assistant);	    		        
         return "water/index";
	    }
	    
	    @NeedSession
	    @UnSecurity
	    @RequestMapping("/getWaterList")
	    @ResponseBody
	    public DataResponse getWaterList(PageSearch ps,LiveC2CMessage params) {
	    	LiveC2CMessage conParam = new LiveC2CMessage();
	    	  String to = params.getToNickName();
	    	  String fr = params.getFromNickName();
	    	if(EmptyUtil.isNotEmpty(to)||EmptyUtil.isNotEmpty(fr)) {
	    	PageResult<LiveC2CMessage> rs = new PageResult<LiveC2CMessage>();
	    	PageHelper.startPage(ps.getPage(),ps.getRows());
	    	PageHelper.orderBy("id desc");
	    	List<LiveC2CMessage> list = this.liveC2CMessageService.byOrAnd(fr, to);
	    	int count = this.liveC2CMessageService.byOrAndCount(fr, to);
			    rs.setRows(list);
	    		rs.setTotal(count);
	    		return new DataResponse(1000, rs);
	        }
	        SearchCondition<LiveC2CMessage> condition = new SearchCondition<LiveC2CMessage>(conParam, ps);
	        condition.buildOrderByConditions("id", "desc");
	        PageResult<LiveC2CMessage> data = this.liveC2CMessageService.findByPage(condition);
	        return new DataResponse(1000, data);
			 
		}
	   
}

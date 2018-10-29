package com.qiyou.dhlive.prd.chat.controller;

import java.util.List;
import java.util.Map;

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
import com.qiyou.dhlive.prd.component.annotation.ResourceAnnotation;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.qiyou.dhlive.prd.component.util.ResourceBaseController;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
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
    public DataResponse getTouristsUserList(PageSearch pageSearch, BaseOptLog params) {
    	BaseOptLog searchParam = new BaseOptLog();
    	SearchCondition<BaseOptLog> con = new SearchCondition<BaseOptLog>(searchParam);
    	if(EmptyUtil.isNotEmpty(params.getOpeMsg())) {
    		con.buildLikeConditions("opeMsg", "%"+params.getOpeMsg()+"%");
    	}
    	PageResult<BaseOptLog> result = this.baseOptLogService.findByPage(con);
    	
    	List<UserManageInfo> manageList = this.baseCacheService.getManageUserList(4);
    	
    	List<Map<String,Object>> dataList = Lists.newArrayList();
    	for(BaseOptLog log:result.getRows()) {
    		Map<String,Object> data = Maps.newHashMap();
    		data.put("optName", this.getOptNameFromList(log.getOptUserId(), manageList));
    		data.put("optTime", log.getOptTime());
    		data.put("optMsg", log.getOpeMsg());
    		dataList.add(data);
    	}
    	
    	PageResult<Map<String,Object>> rs = new PageResult<Map<String,Object>>();
    	rs.setRows(dataList);
    	rs.setTotal(result.getTotal());
    	return new DataResponse(1000, rs);
    }   
} 

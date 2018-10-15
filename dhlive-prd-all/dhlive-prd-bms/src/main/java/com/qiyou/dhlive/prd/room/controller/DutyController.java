package com.qiyou.dhlive.prd.room.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qiyou.dhlive.core.room.outward.model.RoomDuty;
import com.qiyou.dhlive.core.room.outward.service.IRoomDutyService;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserManageInfoService;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.web.annotation.session.NeedSession;
import com.yaozhong.framework.web.annotation.session.UnSession;

/**
 * Created by fish on 2018/9/11.
 */
@Controller
@RequestMapping("duty")
public class DutyController {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IRoomDutyService roomDutyService;
    
    @Autowired
    private IUserManageInfoService userManageInfoService;

    @UnSession
    @UnSecurity
    @RequestMapping("")
    public String index() {
        return "duty/index";
    }
    
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * 根据id查询值班客服
     *
     * @param pageSearch
     * @return
     */
    @NeedSession("/duty/manageList")
    @UnSecurity
    @RequestMapping("/manageList")
    @ResponseBody
    public DataResponse manageList(Integer id) {
    	if(EmptyUtil.isEmpty(id)) {
    		return new DataResponse(1001,"参数错误");
    	}
    	RoomDuty duty = this.roomDutyService.findById(id);
    	List<Map<String,Object>> rows = Lists.newArrayList();
    	PageResult<Map<String,Object>> result = new PageResult<Map<String,Object>>();
    	result.setRows(rows);
    	if(EmptyUtil.isEmpty(duty) || EmptyUtil.isEmpty(duty.getManageIds())) {
    		return new DataResponse(1000,rows);
    	}
    	String[] ids = duty.getManageIds().split(",");
    	String[] names = duty.getManageNames().split(",");
    	
    	for(int i=0;i<ids.length;i++) {
    		if(EmptyUtil.isNotEmpty(ids[i])) {
    			Map<String,Object> row = Maps.newHashMap();
            	row.put("manageId", ids[i]);
            	row.put("manageName", names[i]);
            	rows.add(row);
    		}
    	}
    	result.setTotal(ids.length);
        return new DataResponse(1000,result);
    }
    
    /**
     * 添加值班客服
     *
     * @param pageSearch
     * @return
     */
    @NeedSession("/duty/addManage")
    @UnSecurity
    @RequestMapping("/addManage")
    @ResponseBody
    public DataResponse addManage(Integer id,Integer manageId) {
    	if(EmptyUtil.isEmpty(id)) {
    		return new DataResponse(1001,"参数错误");
    	}
    	if(EmptyUtil.isEmpty(manageId)) {
    		return new DataResponse(1001,"参数错误");
    	}
    	UserManageInfo manage = this.userManageInfoService.findById(id);
    	if(EmptyUtil.isEmpty(manage)) {
    		return new DataResponse(1001,"客服不存在");
    	}
    	
    	RoomDuty duty = this.roomDutyService.findById(id);
    	if(EmptyUtil.isEmpty(duty) || EmptyUtil.isEmpty(duty.getManageIds())) {
    		RoomDuty upDuty = new RoomDuty();
    		upDuty.setId(id);
    		upDuty.setManageIds(manage.getUserId()+",");
    		upDuty.setManageNames(manage.getUserNickName()+",");
    		upDuty.setModifyTime(new Date());
    		this.roomDutyService.modifyEntity(upDuty);
    		return new DataResponse();
    	}else {
    		RoomDuty upDuty = new RoomDuty();
    		String ids = duty.getManageIds();
    		String names = duty.getManageNames();
    		upDuty.setId(id);
    		upDuty.setManageIds(ids+manage.getUserId()+",");
    		upDuty.setManageNames(names+manage.getUserNickName()+",");
    		upDuty.setModifyTime(new Date());
    		this.roomDutyService.modifyEntity(upDuty);
    		return new DataResponse();
    	}
    }
    
    /**
     * 删除值班客服
     *
     * @param pageSearch
     * @return
     */
    @NeedSession("/duty/delManage")
    @UnSecurity
    @RequestMapping("/delManage")
    @ResponseBody
    public DataResponse delManage(Integer id,Integer manageId) {
    	if(EmptyUtil.isEmpty(id)) {
    		return new DataResponse(1001,"参数错误");
    	}
    	if(EmptyUtil.isEmpty(manageId)) {
    		return new DataResponse(1001,"参数错误");
    	}
    	UserManageInfo manage = this.userManageInfoService.findById(id);
    	if(EmptyUtil.isEmpty(manage)) {
    		return new DataResponse(1001,"客服不存在");
    	}
    	
    	RoomDuty duty = this.roomDutyService.findById(id);
    	
    	if(EmptyUtil.isEmpty(duty) || EmptyUtil.isEmpty(duty.getManageIds())) {
    		return new DataResponse();
    	}else {
    		RoomDuty upDuty = new RoomDuty();
    		String ids = duty.getManageIds();
    		String names = duty.getManageNames();
    		
    		String newIds = ids.replace(id+",", "");
    		String newNames = names.replace(manage.getUserNickName()+",", "");
    		upDuty.setId(id);
    		upDuty.setManageIds(newIds);
    		upDuty.setManageNames(newNames);
    		upDuty.setModifyTime(new Date());
    		this.roomDutyService.modifyEntity(upDuty);
    		return new DataResponse();
    	}
    }
}

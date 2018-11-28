package com.qiyou.dhlive.prd.plan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.core.base.outward.model.BaseSysParam;
import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.core.room.outward.model.RoomPlan;
import com.qiyou.dhlive.core.room.outward.service.IRoomPlanService;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.web.annotation.session.NeedSession;

@Controller
@RequestMapping(value="plan")
public class planController {
	
	@Autowired
    private IRoomPlanService roomPlanService;
    
	@Autowired
    private IBaseCacheService baseCacheService;
	
	@Autowired
    private IBaseSysParamService baseSysParamService;
    
	@NeedSession
    @UnSecurity
    @RequestMapping("")
	public String plan(Model model) {
        String state = this.baseSysParamService.getValueByKey("set_plan");
        model.addAttribute("state", state);	
    return "plan/plan";
    }
	
	@NeedSession
    @UnSecurity
    @RequestMapping("/getPlanList")
    @ResponseBody
    public DataResponse getPlanList(PageSearch ps) {
        SearchCondition<RoomPlan> condition = new SearchCondition<RoomPlan>(new RoomPlan(),ps);
        condition.buildOrderByConditions("id", "asc");
        PageResult<RoomPlan> data = this.roomPlanService.findByPage(condition);
        return new DataResponse(1000, data);
		 
	}
	
	@NeedSession
	@UnSecurity
	@RequestMapping("/getPlan")
	@ResponseBody
	public DataResponse getPlan(Integer id) {
		RoomPlan plan=roomPlanService.findById(id);
		return new DataResponse(1000,plan);
	}
	
	 @NeedSession
		@UnSecurity
		@RequestMapping("/savePlan")
		@ResponseBody
		public DataResponse savePlan(RoomPlan params) {
			if(EmptyUtil.isEmpty(params.getPlanTeacher())) {
				return new DataResponse(1001,"请填写直播老师");
			}
			if(EmptyUtil.isEmpty(params.getPlanTime())) {
				return new DataResponse(1001,"请填写直播时间");
			}
			if(EmptyUtil.isEmpty(params.getId())) {
				this.roomPlanService.save(params);
			}else {
				this.roomPlanService.modifyEntity(params);
			}
			this.baseCacheService.updateAllRoomPlan();
			return new DataResponse();
		}
	 
	    @NeedSession
		@UnSecurity
		@RequestMapping("/removePlan")
		@ResponseBody
		public DataResponse removeplan(Integer id) {
			this.roomPlanService.removeById(id);
			this.baseCacheService.updateAllRoomPlan();
			return new DataResponse();
		}
	 
	    @NeedSession
		@UnSecurity
		@RequestMapping("/settingPlan")
		@ResponseBody
		public DataResponse settingPlan(BaseSysParam params) {
			if(EmptyUtil.isEmpty(params.getParamValue())) {
				return new DataResponse(1001,"样式选择不能为空");
			}
			BaseSysParam p=new BaseSysParam();
			p.setParamKey("set_plan");
			
			p=baseSysParamService.findOneByCondition(new SearchCondition<BaseSysParam>(p));
			if(EmptyUtil.isEmpty(p)) {
				 p=new BaseSysParam();
				 p.setParamKey("set_plan");
				 p.setParamValue(params.getParamValue());
				 this.baseSysParamService.save(p);
			}else {
				 p.setParamValue(params.getParamValue());
				 this.baseSysParamService.modifyEntity(p);
			}
			this.baseSysParamService.updateValueByKey("set_plan");
			return new DataResponse();
		}
}
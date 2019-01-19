package com.qiyou.dhlive.prd.inform.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.core.live.outward.model.LiveInform;
import com.qiyou.dhlive.core.live.outward.service.ILiveInformService;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.page.PageResult;
import com.yaozhong.framework.base.database.domain.page.PageSearch;
import com.yaozhong.framework.base.database.domain.returns.BaseResult;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.domain.search.SearchCondition;
import com.yaozhong.framework.web.annotation.session.NeedSession;

@Controller
@RequestMapping(value = "live/inform")
public class InformController {
	
	@Autowired
	private ILiveInformService liveInformService;

	@Autowired
	private IBaseCacheService baseCacheService;

	@NeedSession
    @UnSecurity
    @RequestMapping("")
    public String Inform(Model model) {
         return "inform/inform";
    }
	
	@NeedSession
	@UnSecurity
	@RequestMapping("/getInformList")
	@ResponseBody
	public DataResponse getInformList(PageSearch ps) {
		SearchCondition<LiveInform> condition = new SearchCondition<LiveInform>(new LiveInform(), ps);
		condition.buildOrderByConditions("createTime", "desc");
		PageResult<LiveInform> data = this.liveInformService.findByPage(condition);
		return new DataResponse(1000, data);

	} 
	
	@NeedSession
	@UnSecurity
	@RequestMapping("/getInform")
	@ResponseBody
	public DataResponse getInform(Integer id) {
		LiveInform forms = liveInformService.findById(id);
		return new DataResponse(1000, forms);
	}
	
	@NeedSession
	@UnSecurity
	@RequestMapping("/saveInform")
	@ResponseBody
	public DataResponse saveInform(LiveInform params) {
		
		if (EmptyUtil.isEmpty(params.getInformTitle())) {
			return new DataResponse(1001, "请填写标题");
		}
		if (EmptyUtil.isEmpty(params.getInformContent())) {
			return new DataResponse(1001, "请填写内容");
		}else {
			params.setInformContent(HtmlUtils.htmlUnescape(params.getInformContent()));
		}
			
		if (EmptyUtil.isEmpty(params.getId())) {
			params.setCreateTime(new Date());
		    this.liveInformService.save(params);
		} else {
			this.liveInformService.modifyEntity(params);
		}
		this.baseCacheService.updateLiveInForm();
		return new DataResponse();
	}
	
	@NeedSession
	@UnSecurity
	@RequestMapping("/enableInform")
	@ResponseBody
	public DataResponse enableInform(Integer id) {
	  LiveInform params=new LiveInform();
	  params.setInformState(1);
	  List<LiveInform> informList=this.liveInformService.findByCondition(new SearchCondition<LiveInform>(params));
	  for(LiveInform f:informList) {
		  f.setInformState(0);
		  this.liveInformService.modifyEntity(f);
	  }
	  LiveInform f=this.liveInformService.findById(id);
	  f.setInformState(1);
	  BaseResult br = this.liveInformService.modifyEntity(f);
	  if(br.isSuccess()) {
		  
	  }
	  this.baseCacheService.updateLiveInForm();
	  return new DataResponse();
	}
	
	@NeedSession
	@UnSecurity
	@RequestMapping("/disabledInform")
	@ResponseBody
	public DataResponse disabledInform(Integer id) {
	  LiveInform params=this.liveInformService.findById(id);
	  params.setInformState(0);
	  BaseResult br = this.liveInformService.modifyEntity(params);
	  if(br.isSuccess()) {
		  
	  }
	  try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  this.baseCacheService.updateLiveInForm();
	  return new DataResponse();
	}
}

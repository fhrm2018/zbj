package com.qiyou.dhlive.prd.index.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="")
public class IndexController {

	
	@RequestMapping(value="")
	public String index(Model model){
		
		return "index";
	}
	
	
}

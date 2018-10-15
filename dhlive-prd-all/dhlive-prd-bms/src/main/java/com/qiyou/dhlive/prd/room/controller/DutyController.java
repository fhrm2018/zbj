package com.qiyou.dhlive.prd.room.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.prd.component.annotation.UnSecurity;
import com.yaozhong.framework.web.annotation.session.UnSession;

/**
 * Created by fish on 2018/9/11.
 */
@Controller
@RequestMapping("duty")
public class DutyController {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IBaseSysParamService baseSysParamService;

    @UnSession
    @UnSecurity
    @RequestMapping("")
    public String index() {
        return "duty/index";
    }
}

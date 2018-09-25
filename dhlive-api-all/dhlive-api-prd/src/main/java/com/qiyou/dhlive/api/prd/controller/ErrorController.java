package com.qiyou.dhlive.api.prd.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qiyou.dhlive.api.base.outward.service.IUserInfoApiService;
import com.qiyou.dhlive.api.prd.mvc.UserSession;
import com.qiyou.dhlive.api.prd.util.AddressUtils;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.database.redis.RedisManager;
import com.yaozhong.framework.web.annotation.session.UnSession;

/**
 * Created by fish on 2018/3/2.
 */
@Controller
@RequestMapping(value = "error")
public class ErrorController {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IUserInfoApiService userInfoApiService;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    @UnSession
    @RequestMapping("404")
    public String error(Model model, Integer roomId, HttpServletRequest request) {
        String ip = AddressUtils.getIpAddrFromRequest(request);
        baseLog.info(LogFormatUtil.getActionFormat("拉黑IP:" + ip));
        if (EmptyUtil.isEmpty(roomId)) {
            roomId = 4;
        }
        List<UserManageInfo> assistant = this.userInfoApiService.getAssistantList(roomId);
        int x = (int) (Math.random() * assistant.size());//随机数, 用于选择一个助理进行关联
        model.addAttribute("qqNum", assistant.get(x).getUserQq());
        model.addAttribute("roomId", roomId);
        model.addAttribute("ip", ip);
        return "404";
    }

}

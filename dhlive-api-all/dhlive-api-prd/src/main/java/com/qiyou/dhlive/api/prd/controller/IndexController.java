package com.qiyou.dhlive.api.prd.controller;

import com.qiyou.dhlive.core.base.outward.service.IBaseSysParamService;
import com.qiyou.dhlive.core.base.service.constant.RedisKeyConstant;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.database.domain.returns.DataResponse;
import com.yaozhong.framework.base.database.redis.RedisManager;
import com.yaozhong.framework.web.annotation.session.UnSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "index")
public class IndexController {

    @Autowired
    private IBaseSysParamService baseSysParamService;

    @Autowired
    @Qualifier("commonRedisManager")
    private RedisManager redisManager;

    @RequestMapping(value = "login")
    public String index(Model model) {
        return "login";
    }

    @RequestMapping(value = "check")
    @ResponseBody
    public DataResponse check(String userPass, HttpServletRequest request) {
        if (EmptyUtil.isEmpty(userPass)) {
            return new DataResponse(1001, "请输入密码.");
        }
        String pass = this.baseSysParamService.getValueByKey("live.pass");
        if (userPass.equals(pass)) {
            request.getSession().setAttribute("PASS", userPass);
            return new DataResponse(1000, "success");
        } else {
            return new DataResponse(1001, "密码错误.");
        }
    }

    @UnSession
    @RequestMapping(value = "test")
    @ResponseBody
    public DataResponse test(String ip) {
        this.redisManager.delete(RedisKeyConstant.REIDSKEY_BLACKIP + ip);
        return new DataResponse(1000, "success");
    }
}

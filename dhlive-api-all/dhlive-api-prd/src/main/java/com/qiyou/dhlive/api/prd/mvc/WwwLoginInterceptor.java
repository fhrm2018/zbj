package com.qiyou.dhlive.api.prd.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.web.annotation.session.NeedSession;


/**
 * web用户session拦截器,请使用CommonLoginInterceptor
 *
 * @author xunyou
 * @version 2.0, 2015-07-01
 * @since com.dongrongonline 2.0
 */
public class WwwLoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        NeedSession session = handlerMethod.getMethodAnnotation(NeedSession.class);
        if (EmptyUtil.isNotEmpty(session)) {
            UserSession userLogin = UserSession.getUserSession();
            if (EmptyUtil.isEmpty(userLogin) || EmptyUtil.isEmpty(UserSession.getLoginUserId())) {
                // 如果是ajax请求响应头会有，x-requested-with；
                if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                    // 在响应头设置session状态
                    response.setHeader("sessionstatus", "sessionOut");
                    response.getWriter().print("sessionOut");
                    return false;
                }
                String returnUrl = "";
                if (EmptyUtil.isNotEmpty(session.returnUrl())) {
                    returnUrl = "?returnUrl=" + session.returnUrl();
                    response.sendRedirect("/tologin" + returnUrl);
                } else {
                    response.sendRedirect("/tologin");
                }
                return false;
            }
        }
        return true;
    }
}

package com.qiyou.dhlive.api.prd.mvc;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.qiyou.dhlive.api.base.outward.service.IBaseCacheService;
import com.qiyou.dhlive.api.base.outward.service.ICheckIpVisitService;
import com.qiyou.dhlive.api.base.outward.vo.UserInfoDTO;
import com.qiyou.dhlive.api.prd.util.AddressUtils;
import com.qiyou.dhlive.api.prd.util.Constants;
import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.service.IUserInfoService;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import com.yaozhong.framework.base.common.utils.MyBeanUtils;
import com.yaozhong.framework.web.annotation.session.UnSession;


public class WwwSecurityInterceptor extends HandlerInterceptorAdapter {

    private static Logger baseLog = LoggerFactory.getLogger("baseLog");

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ICheckIpVisitService checkIpVisitService;
    
    @Autowired
    private IBaseCacheService baseCacheService;

    private String getCookiesValue(HttpServletRequest request, String cookieName) {
        String rs = "";
        Cookie[] cookies = request.getCookies();
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            if (cookieName.equals(cookies[i].getName())) {
                rs = cookies[i].getValue();
                break;
            }
        }
        return rs;
    }

    private void delVipUserCookies(HttpServletResponse response) {
        Cookie pwd = new Cookie(Constants.VIP_USER_PASS, null);
        pwd.setMaxAge(0);
        response.addCookie(pwd);

        Cookie userId = new Cookie(Constants.VIP_USER_ID, null);
        userId.setMaxAge(0);
        response.addCookie(userId);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        UnSession unSession = handlerMethod.getMethodAnnotation(UnSession.class);
        UserSession userLogin = UserSession.getUserSession();
        if(EmptyUtil.isEmpty(request))
        	return false;
        HttpSession httpSession = request.getSession();
        String ip = AddressUtils.getIpAddrFromRequest(request);
        String url = request.getRequestURL().toString();
        if(!isAjaxRequest(request)){
	        baseLog.info(LogFormatUtil.getActionFormat("拦截器获取页面:" + ip + ",url:" + url));
	        String utmSource = request.getParameter("utm_source");
	        if (unSession == null) {
	            if (this.checkIpVisitService.checkCanVisit(ip, url)) {
	                if (EmptyUtil.isEmpty(userLogin)) {
	                    String userId = "";
	                    userId = getCookiesValue(request, Constants.VIP_USER_ID);
	                    if (EmptyUtil.isEmpty(userId))
	                        userId = getCookiesValue(request, Constants.USER_ID);
	                    UserInfo user = null;
	                    if (EmptyUtil.isNotEmpty(userId)) {
	                        try {
	                            Integer.parseInt(userId);
	                            UserInfoDTO userDto = this.baseCacheService.getUserInfo(Integer.parseInt(userId));
	                            user = MyBeanUtils.copyBean(userDto, UserInfo.class);
	                        } catch (Exception e) {
	                        }
	                    }
	                    if (EmptyUtil.isEmpty(user)) {
	                        /*user = this.userInfoService.createNewGuestUser(AddressUtils.getIpAddrFromRequest(request), utmSource);
	                        user.setUserId(user.getUserId());
	                        UserSession userSession = HttpSessionTool.createUserSession(user);
	                        HttpSessionTool.doLoginedUser(httpSession, userSession);
	                        Cookie userIdCookie = new Cookie(Constants.USER_ID, user.getUserId().toString());
	                        userIdCookie.setMaxAge(60 * 60 * 24 * 365);
	                        response.addCookie(userIdCookie);*/
	                    	return true;
	                    } else {
	                        UserSession userSession = HttpSessionTool.createUserSession(user);
	                        HttpSessionTool.doLoginedUser(httpSession, userSession);
	                        Cookie userIdCookie = new Cookie(Constants.USER_ID, user.getUserId().toString());
	                        userIdCookie.setMaxAge(60 * 60 * 24 * 365);
	                        response.addCookie(userIdCookie);
	
	                        UserInfo record = new UserInfo();
	                        record.setLastLoginIp(ip);
	                        record.setUserId(user.getUserId());
	                        record.setLastLoginTime(new Date());
	                        record.setUtmSource(utmSource);
	                        this.userInfoService.modifyEntity(record);
	                    }
	                }else {
	                	/*Integer userId = userLogin.getUserId();
	                	if(EmptyUtil.isNotEmpty(userId)) {
	                		UserInfo user = this.userInfoService.findById(userId);
	                		if(EmptyUtil.isNotEmpty(user)) {
		                		UserInfo record = new UserInfo();
		                        record.setLastLoginIp(ip);
		                        record.setUserId(userId);
		                        record.setLastLoginTime(new Date());
		                        this.userInfoService.modifyEntity(record);
	                        }
	                	}*/
	                }
	                return true;
	            } else {
	                response.sendRedirect("/error/404");
	                return false;
	            }
	        } else {
	            return true;
	        }
        }else {
        	//baseLog.info(LogFormatUtil.getActionFormat("拦截器获取ajax:" + ip + ",url:" + url));
        	if (unSession == null) {
        		if (EmptyUtil.isEmpty(userLogin)) {
        			return false;
        		}
        		if (this.checkIpVisitService.checkCanVisit(ip, url)) {
        			return true;
        		}else {
        			response.sendRedirect("/error/404");
	                return false;
        		}
        	}else {
        		return true;
        	}
        }
    }

    private String getRequestUrl(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        return requestUri.substring(contextPath.length());
    }
    
    private boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}

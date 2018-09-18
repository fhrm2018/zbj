package com.qiyou.dhlive.api.prd.mvc;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.qiyou.dhlive.core.user.outward.model.UserInfo;
import com.qiyou.dhlive.core.user.outward.model.UserManageInfo;
import com.qiyou.dhlive.core.user.outward.model.UserVipInfo;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.MyBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpSessionTool {

    private static Logger logger = LoggerFactory.getLogger(HttpSessionTool.class);

    public static String SESSION_KEY = "loginedSession";

    public static String COMPANYLOGIN_SESSION_KEY = "loginedCompanyLogin";

    public static String USER_SESSION_KEY = "loginedUserLogin";

    public static String LOGINERROR_KEY = "login_error_number";

    public static void doLoginedUser(HttpSession session, UserSession user) {
        session.setAttribute(USER_SESSION_KEY, user);
        session.removeAttribute(LOGINERROR_KEY);
        UserSession.setUserSession(user);
    }

    /**
     * 错误的登陆次数
     *
     * @param httpSession
     * @return
     * @author LiuYiJun
     * @date 2015年7月14日
     */
    public static Integer getLoginErrorNumber(HttpSession httpSession) {
        return (Integer) httpSession.getAttribute(LOGINERROR_KEY);
    }

    /**
     * 增加登陆错误次数
     *
     * @param httpSession
     * @author LiuYiJun
     * @date 2015年7月14日
     */
    public static void addLoginErrorNumber(HttpSession httpSession) {
        Integer number = getLoginErrorNumber(httpSession);
        number = EmptyUtil.isEmpty(number) ? 1 : number + 1;
        httpSession.setAttribute(LOGINERROR_KEY, number);
    }

    /**
     * 清除错误登录次数
     *
     * @param httpSession
     * @author LiuYiJun
     * @date 2015年7月14日
     */
    public static void clearLoginErrorNumber(HttpSession httpSession) {
        httpSession.removeAttribute(LOGINERROR_KEY);
    }

    public static void doCompanLoginOut(HttpSession httpSession) {
        httpSession.removeAttribute(COMPANYLOGIN_SESSION_KEY);
    }

    public static void doUserLoginOut(HttpSession httpSession) {
        httpSession.removeAttribute(USER_SESSION_KEY);
    }

    public static UserSession createUserSession(UserInfo user) {
        return MyBeanUtils.copyBean(user, UserSession.class);
    }

    public static void doLoginUser(HttpSession session, UserInfo user) {
        UserSession userSession = MyBeanUtils.copyBean(user, UserSession.class);
        session.setAttribute(USER_SESSION_KEY, userSession);
        session.removeAttribute(LOGINERROR_KEY);
        UserSession.setUserSession(userSession);
    }

    public static void doLoginUser(HttpSession session, UserVipInfo user) {
        UserSession userSession = MyBeanUtils.copyBean(user, UserSession.class);
        session.setAttribute(USER_SESSION_KEY, userSession);
        session.removeAttribute(LOGINERROR_KEY);
        UserSession.setUserSession(userSession);
    }

    public static void doLoginUser(HttpSession session, UserManageInfo user) {
        UserSession userSession = MyBeanUtils.copyBean(user, UserSession.class);
        session.setAttribute(USER_SESSION_KEY, userSession);
        session.removeAttribute(LOGINERROR_KEY);
        UserSession.setUserSession(userSession);
    }


    public static void clearUserSession(HttpSession httpSession) {
        UserSession.setUserSession(null);
        httpSession.removeAttribute(USER_SESSION_KEY);
    }

    public static void delCookie(HttpServletRequest request,
                                 HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            return;
        }
        for (Cookie cookie : cookies) {
            cookie.setValue(null);
            cookie.setMaxAge(0);// 立即销毁cookie
            cookie.setPath("/");
            logger.info("被删除的cookie名字为:" + cookie.getName());
            response.addCookie(cookie);
        }
    }

    public static void delCookie(HttpServletRequest request,
                                 HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            return;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                cookie.setValue(null);
                cookie.setMaxAge(0);// 立即销毁cookie
                cookie.setPath("/");
                logger.info("被删除的cookie名字为:" + cookie.getName());

                response.addCookie(cookie);
                break;
            }
        }
    }
}

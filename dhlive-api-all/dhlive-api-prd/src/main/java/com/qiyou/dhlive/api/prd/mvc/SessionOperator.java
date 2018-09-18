package com.qiyou.dhlive.api.prd.mvc;



import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionOperator {
	private static String[] staticUrl = {".css", ".js", ".ico", ".gif", ".html", "htm", ".jpg", ".png", ".bmp",
            ".jpeg", ".swf", ".apk", ".tif", ".flv", ".xml", ".txt", ".svg", ".json", ".map", ".mp4", ".avi"};

    @SuppressWarnings("finally")
    public static void doSessionInfo(HttpSession session, ThreadLocal<HttpSession> threadLocalHttpSession,
                                     HttpServletRequest request, HttpServletResponse response, String type) throws IOException {
        String uri = request.getRequestURI();
        if (!isStaticFile(uri)) {
            threadLocalHttpSession.set(session);
            UserSession userSession = (UserSession) session
                        .getAttribute(HttpSessionTool.USER_SESSION_KEY);
            UserSession.setUserSession(userSession);
            
        }
    }

    public static boolean isStaticFile(String url) {
        for (String string : staticUrl) {
            if (url.endsWith(string)) {
                return true;
            }
        }
        return false;
    }
}

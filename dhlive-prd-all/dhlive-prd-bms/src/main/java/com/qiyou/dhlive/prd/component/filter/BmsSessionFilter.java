package com.qiyou.dhlive.prd.component.filter;

import com.qiyou.dhlive.prd.component.session.EmployeeSession;
import com.yaozhong.framework.web.mvc.filter.BaseAbstractSessionFilter;
import com.yaozhong.framework.web.mvc.session.HttpSessionTool;

import javax.servlet.http.HttpSession;


public class BmsSessionFilter extends BaseAbstractSessionFilter {

    public void saveMembersSessionOrEmployeeSessionToThreadLocal(HttpSession session) {
        EmployeeSession employeeSession = (EmployeeSession) session.getAttribute(HttpSessionTool.EMPLOYEE_SESSION_KEY);
        EmployeeSession.setUserSession(employeeSession);
    }
}

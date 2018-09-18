package com.qiyou.dhlive.prd.component.util;

import com.qiyou.dhlive.core.bms.outward.model.BmsEmployeeInfo;
import com.qiyou.dhlive.prd.component.session.EmployeeSession;
import com.yaozhong.framework.base.common.utils.MyBeanUtils;


public class EmployeeSessionUtil {

    public static EmployeeSession createEmployeeSessionByEmployee(BmsEmployeeInfo employee) {
        EmployeeSession rs = (EmployeeSession) MyBeanUtils.copyBean(employee, EmployeeSession.class);
        rs.setEmployeeId(employee.getId());
        return rs;
    }
}

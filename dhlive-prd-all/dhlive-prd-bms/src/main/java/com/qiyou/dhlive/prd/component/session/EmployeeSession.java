package com.qiyou.dhlive.prd.component.session;


import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.qiyou.dhlive.core.bms.outward.model.BmsResource;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.web.mvc.session.BaseSession;
import java.util.Comparator;
import java.util.List;

public class EmployeeSession extends BaseSession {

    private Integer employeeId;
    private String email;
    private String name;
    private String loginName;
    private String roleId;
    private String password;
    private String mobile;
    private List<BmsResource> resources;
    private List<BmsResource> firstResources;
    private List<BmsResource> secondResources;

    public static EmployeeSession getEmployeeSession() {
        return (EmployeeSession) threadLocalUserSession.get();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<BmsResource> getResources() {
        return resources;
    }

    public void setResources(List<BmsResource> resources) {
        this.resources = resources;
        List<BmsResource> first = Lists.newArrayList();
        List<BmsResource> second = Lists.newArrayList();
        for (BmsResource r : resources) {
            if (r.getType().intValue() == 1) {
                first.add(r);
            } else {
                second.add(r);
            }
        }
        Comparator<BmsResource> byGroup = new Comparator<BmsResource>() {
            public int compare(final BmsResource p1, final BmsResource p2) {
                String name1 = p1.getGroupName();
                String name2 = p2.getGroupName();
                if (EmptyUtil.isEmpty(name1)) {
                    name1 = "";
                }
                if (EmptyUtil.isEmpty(name2)) {
                    name2 = "";
                }
                return name2.compareTo(name1);
            }
        };
        Comparator<BmsResource> bySort = new Comparator<BmsResource>() {
            public int compare(final BmsResource p1, final BmsResource p2) {
                Integer value1 = p1.getSort();
                Integer value2 = p2.getSort();
                if (EmptyUtil.isEmpty(value1)) {
                    value1 = 0;
                }
                if (EmptyUtil.isEmpty(value2)) {
                    value2 = 0;
                }
                return value2.compareTo(value1);
            }
        };
        this.firstResources = Ordering.from(byGroup).compound(bySort).reverse().sortedCopy(first);
        this.secondResources = Ordering.from(bySort).reverse().sortedCopy(second);
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public List<BmsResource> getFirstResources() {
        return firstResources;
    }

    public void setFirstResources(List<BmsResource> firstResources) {
        this.firstResources = firstResources;
    }

    public List<BmsResource> getSecondResources() {
        return secondResources;
    }

    public void setSecondResources(List<BmsResource> secondResources) {
        this.secondResources = secondResources;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}

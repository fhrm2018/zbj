<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String name = request.getParameter("name");
    String pName = request.getParameter("pName");
%>
<c:set var="name" value="<%=request.getParameter("name")%>"></c:set>
<c:set var="pName" value="<%=request.getParameter("pName")%>"></c:set>
<c:set var="curRemark" value=""/>
<div class="asideBox posRel flexWrap">
    <div class="asideLeft pb50 ovfHid flexCon">
        <div class="inner posRel ovfHid">
            <ul class="nav mt20">
                <c:set var="curGroup" value=""/>
                <c:forEach items="${loginedEmployee.firstResources}" var="menu">
                    <c:if test="${menu.type eq 1}">
                        <li class="mt12 <c:if test="${menu.name eq pName}">cur</c:if> <c:if test="${!empty curGroup && menu.groupName != curGroup}">mt50</c:if>">
                            <a href="${pageContext.request.contextPath}${menu.url}" class="block">
                                <span class="icon ${menu.icon} mr5 mb2 verMid"></span>${menu.name}
                            </a>
                        </li>
                        <c:set var="curGroup" value="${menu.groupName}"/>
                    </c:if>
                    <c:if test="${menu.name eq pName}">
                        <c:set var="curRemark" value="${menu.remark}"/>
                    </c:if>
                </c:forEach>
            </ul>
        </div>
    </div>
	<c:set var="isHaveSecond" value=""/>
	<c:forEach items="${loginedEmployee.secondResources}" var="menu">
		<c:if test="${menu.parentName eq pName && menu.type eq 2}">
		 	<c:set var="isHaveSecond" value="1"/>
		 </c:if>
	</c:forEach>
	<c:if test="${isHaveSecond eq 1 }">
	<div class="asideRig ac flexCon">
        <ul class="mt20">
            <c:forEach items="${loginedEmployee.secondResources}" var="menu">
                <c:if test="${menu.parentName eq pName && menu.type eq 2}">
                    <li class="mt12 <c:if test="${menu.name eq name}">cur</c:if>">
                        <a href="${pageContext.request.contextPath}${menu.url}" class="block">${menu.name}</a>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </div>
    </c:if>
    
    <div class="fz10 ac posAbs" style="left:0px;bottom:0px;height:20px;line-height:20px;width:120px;background:#01AB96;color:#f8f8f8;"><span>Powered by</span></div>
</div>
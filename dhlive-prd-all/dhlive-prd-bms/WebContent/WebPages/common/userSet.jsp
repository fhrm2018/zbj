<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<a class="ilblock logoBox fz12 posAbs" style="border-right:#000000 1px solid;top:0px;left:0px;bottom:0px;"  target="_blank">
    <span class="ilblock ml5 verTop colorF fz16">管理后台</span>
</a>
<div class="userSetBox posAbs posRel top0 bottom0 right0">
	<div id="loginName" class="user posAbs bottom0 right0 ac">
	    <a href="" onclick="return !1;" class="block name">${loginedEmployee.name}</a>
	    <div class="box posAbs wtBg ac rad3 hide">
	        <a href="" id="lkPassword" onclick="return !1;" class="block">修改密码</a>
	        <a href="${pageContext.request.contextPath}/loginOut" class="block">退出</a>
	    </div>
	</div>
</div>
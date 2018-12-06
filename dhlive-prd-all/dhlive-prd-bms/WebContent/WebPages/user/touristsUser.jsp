<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>后台管理</title>
    <jsp:include page="../common/public.jsp"/>
    <link href="${pageContext.request.contextPath}/static/css/live.css" rel="stylesheet" type="text/css"/>
    <script src="${pageContext.request.contextPath}/static/js/lib/date/WdatePicker.js"></script>
</head>

<body>
<div class="mainTitle">
<div class="titleContent">
        <span class="lgLine ilblock ovfHid ml20 verMid"></span> <span
            class="verMid ml10 colorF">游客</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="游客" name="name"/>
        <jsp:param value="客户管理" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <ul id="status-nav" class="navMenu wtBg pl15 ac">
                <li class="cur">
                    <a class="block all" style="cursor: pointer" >全部客户</a>
                </li>
                <li>
                    <a class="block online" style="cursor: pointer">在线客户</a>
                </li>
                <li>
                    <a class="block gag" style="cursor: pointer">禁言客户</a>
                </li>
                <li>
                    <a class="block black" style="cursor: pointer">黑名单客户</a>
                </li>
            </ul>
            <div class="posAbs" style="right: 10px; top: 20px;">
            	<span>当前在线用户数:<span id="onlineNum">${onlineNum }</span></span>
                <button type="button" class="pageBtn searchBtn">查询用户</button>
                <button type="button" class="pageBtn refBtn">刷新</button>
            </div>
        </div>

        <div class="mainContent">
            <div class="bar" style="height: 40px;">
                <div id="showpager" class="pages posRel ac fr">
                    <div class="color6 ilblock">共<span class="allrecords"></span> 条, 每页 <span class="eachrecords"></span>条</div>
                    <p title="上一页" class="writeBtn prev"><span class="iconLeft"></span></p><div class="ilblock eachItem verTop"></div><p title="下一页" class="writeBtn next"><span class="iconRight"></span></p>
                    <div class="ilblock pagego">
                        <input type="text" class="ilblock ipt keypageipt fz12" placeholder="跳转页数"/>
                        <span title="go" class="btn ilblock">确定</span>
                    </div>
                </div>
            </div>

            <div class="tableTitle flexWrap ac">
                <div style="width: 12%">昵称</div>
                <div style="width: 12%">角色</div>
                <div style="width: 12%">创建时间</div>
                <div style="width: 12%">最后登录时间</div>
                <div style="width: 12%">访问次数</div>
                <div style="width: 12%">观看时长(s)</div>
                <div style="width: 12%">ip</div>
                <div style="width: 12%">操作</div>
            </div>
            <div id="showrecords" class="ac"></div>
        </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>
<!-- 查询游客窗口 -->
<div id="searchWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="searchBox" action="${pageContext.request.contextPath}/user/getTouristsUserList" method="post">

        <input type="hidden" id="isOnline" name="isOnline"/>
        <input type="hidden" id="isGag" name="isGag"/>
        <input type="hidden" id="isBlack" name="isBlack"/>

        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">查询游客</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">昵称</span>
            <input type="text" class="serIpt" id="userNickName" name="userNickName" placeholder="请输入昵称"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">IP</span>
            <input type="text" class="serIpt" id="lastLoginIp" name="lastLoginIp" placeholder="请输入IP"/>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="searchBtn" type="button" class="pageBtn popFormBtn">确认</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>
</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/user/touristsUser.js"></script>
</html>
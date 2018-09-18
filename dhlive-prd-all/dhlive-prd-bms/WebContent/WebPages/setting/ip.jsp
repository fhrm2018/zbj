<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>东金在线</title>
    <jsp:include page="../common/public.jsp"/>
    <link href="${pageContext.request.contextPath}/static/css/live.css" rel="stylesheet" type="text/css"/>
    <script src="${pageContext.request.contextPath}/static/js/lib/date/WdatePicker.js"></script>
</head>

<body>
<div class="mainTitle">
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="IP黑白名单" name="name"/>
        <jsp:param value="运营设置" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <ul id="status-nav" class="navMenu wtBg pl15 ac hide">
                <li class="cur">
                    <a class="block all" style="cursor: pointer">全部</a>
                </li>
                <li>
                    <a class="block black" style="cursor: pointer">IP黑名单</a>
                </li>
                <li>
                    <a class="block white" style="cursor: pointer">IP白名单</a>
                </li>
            </ul>

            <div class="posAbs hide" style="right: 10px; top: 20px;">
                <button type="button" class="pageBtn addBtn">添加</button>
                <button type="button" class="pageBtn searchBtn">查询</button>
                <button type="button" class="pageBtn refBtn">刷新</button>
            </div>

            <div style="right: 10px; top: 20px;">
                <button type="button" class="pageBtn addBtn">添加</button>
                <button type="button" class="pageBtn searchBtn">查询</button>
                <button type="button" class="pageBtn refBtn">刷新</button>
            </div>
        </div>

        <div class="mainContent">
            <div class="tableTitle flexWrap ac">
                <div style="width: 33%">IP</div>
                <div style="width: 33%">类型</div>
                <div style="width: 33%">操作</div>
            </div>
            <div id="showrecords" class="ac"></div>
        </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>
<!-- 查询窗口 -->
<div id="searchWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="searchBox" action="${pageContext.request.contextPath}/user/getTouristsUserList" method="post">
        <input type="hidden" id="type" name="type" value="0"/>
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">查询IP</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">IP</span>
            <input type="text" class="serIpt" id="ip" name="ip" placeholder="请输入IP"/>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="searchBtn" type="button" class="pageBtn popFormBtn">确认</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>

<!-- 添加黑白名单 -->
<div id="addWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="tableForm" action="${pageContext.request.contextPath}/setting/saveIp" method="post">
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">添加黑白名单</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">IP</span>
            <textarea name="ip" class="serIpt" placeholder="用逗号隔开可添加多个" style="height: 100px"></textarea>
        </div>

        <div class="clearfix ptb10">
            <div class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">类型</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt groupName" style="line-height: 23px;">黑名单</div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt" onclick="showUserSpecialty()">
                    <li data-value="0">黑名单</li>
                    <li data-value="1">白名单</li>
                </ul>
                <input type="hidden" name="type" value="0"/>
            </div>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="submitBtn" type="submit" class="pageBtn popFormBtn">确定</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>
</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/setting/ip.js"></script>
</html>
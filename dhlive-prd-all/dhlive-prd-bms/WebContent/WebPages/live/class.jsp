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
            class="verMid ml10 colorF">直播室</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="直播室" name="name"/>
        <jsp:param value="直播室" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <ul id="status-nav" class="navMenu wtBg pl15 ac">
                <li>
                    <a class="block" href="${pageContext.request.contextPath}/live/toDetailPage?roomId=${roomId}"
                       style="cursor: pointer" >老师介绍</a>
                </li>
                <li class="cur">
                    <a class="block" href="${pageContext.request.contextPath}/live/toClassPage?roomId=${roomId}"
                       style="cursor: pointer">课程安排</a>
                </li>
                <li>
                    <a class="block" href="${pageContext.request.contextPath}/live/toFilePage?roomId=${roomId}"
                       style="cursor: pointer">课件下载</a>
                </li>
            </ul>
            <div class="posAbs" style="right: 10px; top: 20px;">
                <button type="button" class="pageBtn uploadBtn">添加课程</button>
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
                <div style="width: 33%">课程名称</div>
                <div style="width: 33%">上传时间</div>
                <div style="width: 33%">操作</div>
            </div>
            <div id="showrecords" class="ac"></div>
        </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>


<!-- 上传文件窗口 -->
<div id="uploadWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="tableForm" action="${pageContext.request.contextPath}/live/saveRoomClass" method="post">

        <input type="hidden" id="roomId" name="roomId" value="${roomId}"/>
        <input type="hidden" id="classId" name="classId"/>

        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">上传课件</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">名称</span>
            <input type="text" class="serIpt" id="className" name="className"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">文件</span>
            <input type="file" class="serIpt" id="file" name="file"/>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="submitBtn" type="submit" class="pageBtn popFormBtn">确定</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>
</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/live/class.js"></script>
</html>
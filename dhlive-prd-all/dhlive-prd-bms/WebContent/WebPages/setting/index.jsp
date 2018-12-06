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
            class="verMid ml10 colorF">定时发言</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="定时发言" name="name"/>
        <jsp:param value="内容设置" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <div style="right: 10px; top: 20px;">
                <button type="button" class="pageBtn addBtn" style="width: 10%">新增定时发言</button>
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
                <div style="width: 14%">直播室</div>
                <div style="width: 14%">发言开始时间</div>
                <div style="width: 14%">发言截止时间</div>
                <div style="width: 14%">发言间隔(s)</div>
                <div style="width: 14%">发言频率</div>
                <div style="width: 14%">发言内容(s)</div>
                <div style="width: 14%">操作</div>
            </div>
            <div id="showrecords" class="ac"></div>
        </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>

<!-- 添加客户窗口 -->
<div id="addWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="tableForm" action="${pageContext.request.contextPath}/setting/saveAnnouncement" method="post">
        <input type="hidden" id="id" name="id"/>
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">新增定时发言</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="clearfix ptb10">
            <div tabindex="0" class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">直播室</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt roomName" style="line-height: 23px">所有</div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt">
                    <li data-value="">所有</li>
                    <c:forEach var="v" items="${rooms}">
                        <li data-value=${v.roomId}>${v.roomName}</li>
                    </c:forEach>
                </ul>
                <input type="hidden" id="roomId" name="roomId" value=""/>
            </div>
        </div>

        <div class="clearfix ptb10">
            <div tabindex="0" class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">直播室</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt frequencyName" style="line-height: 23px">循环播放</div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt">
                    <li data-value="0">循环播放</li>
                    <li data-value="1">仅一次</li>
                </ul>
                <input type="hidden" id="frequency" name="frequency" value="0"/>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">发言开始时间</span>
            <input type="text" id="beginTime" name="beginTime" class="serIpt" readonly onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">发言截止时间</span>
            <input type="text" id="endTime" name="endTime" class="serIpt" readonly onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">发言间隔</span>
            <input type="text" class="serIpt" id="timeInterval" name="timeInterval"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">发言内容</span>
            <textarea id="content" name="content" class="serIpt" style="height: 100px"></textarea>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="submitBtn" type="submit" class="pageBtn popFormBtn">确定</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>
</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/setting/index.js"></script>
</html>
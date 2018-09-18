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
        <jsp:param value="红包" name="name"/>
        <jsp:param value="活动" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <ul id="status-nav" class="navMenu wtBg pl15 ac">
                <li class="cur">
                    <a class="block listTip" style="cursor: pointer">获奖名单</a>
                </li>
                <li>
                    <a class="block totalTip" style="cursor: pointer">发放统计</a>
                </li>
                <li>
                    <a class="block configTip" style="cursor: pointer">红包配置</a>
                </li>
            </ul>
        </div>

        <!-- 获奖名单 -->
        <div class="mainContent">
            <div class="bar" style="height: 40px;">

                <button type="button" class="pageBtn searchBtn">查询用户</button>

                <div id="showpager" class="pages posRel ac fr">
                    <div class="color6 ilblock">共<span class="allrecords"></span> 条, 每页 <span
                            class="eachrecords"></span>条
                    </div>
                    <p title="上一页" class="writeBtn prev"><span class="iconLeft"></span></p>
                    <div class="ilblock eachItem verTop"></div>
                    <p title="下一页" class="writeBtn next"><span class="iconRight"></span></p>
                    <div class="ilblock pagego">
                        <input type="text" class="ilblock ipt keypageipt fz12" placeholder="跳转页数"/>
                        <span title="go" class="btn ilblock">确定</span>
                    </div>
                </div>
            </div>

            <div class="tableTitle flexWrap ac">
                <div style="width: 12%">昵称</div>
                <div style="width: 12%">手机号</div>
                <div style="width: 12%">领取时间</div>
                <div style="width: 12%">红包金额(元)</div>
                <div style="width: 12%">发放状态</div>
                <div style="width: 12%">操作人</div>
                <div style="width: 12%">操作时间</div>
                <div style="width: 12%">操作</div>
            </div>
            <div id="showrecords" class="ac"></div>
        </div>

        <!-- 发放统计 -->
        <div class="mainContent total hide">
            <div class="tableTitle flexWrap ac">
                <div style="width: 33%">时间</div>
                <div style="width: 33%">累计抽奖金额(元)</div>
                <div style="width: 33%">发奖金额(元)</div>
            </div>
            <div id="totalRecords" class="ac">
                <span style="height: 150px; line-height: 150px;">正在加载, 请稍候...</span>
            </div>
        </div>

        <!-- 红包配置 -->
        <div class="mainContent config hide">
            <div class="tableTitle flexWrap ac">
                <div style="width: 25%">活动名称</div>
                <div style="width: 25%">活动时间</div>
                <div style="width: 25%">是否开启</div>
                <div style="width: 25%">操作</div>
            </div>
            <div id="configRecords" class="ac">
                <span style="height: 150px; line-height: 150px;">正在加载, 请稍候...</span>
            </div>
        </div>

    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>
<!-- 查询窗口 -->
<div id="searchWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="searchBox" action="${pageContext.request.contextPath}/activity/getWinnerList" method="post">
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">查找用户</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <input type="text" class="serIpt" id="userNickName" name="userNickName" placeholder="输入昵称"/>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="searchBtn" type="button" class="pageBtn popFormBtn">确认</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>

<!-- 红包配置 -->
<div id="addWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="tableForm" action="${pageContext.request.contextPath}/activity/saveConfig" method="post">
        <input type="hidden" id="configId" name="id"/>

        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">红包配置</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">活动名称</span>
            <input type="text" class="serIpt" id="activityName" name="activityName" placeholder="输入活动名称."/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">开始时间</span>
            <input type="text" class="serIpt date-hook" id="activityStartTime" name="activityStartTime"
                   placeholder="选择开始时间." onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">结束时间</span>
            <input type="text" class="serIpt" id="activityEndTime" name="activityEndTime" placeholder="选择结束时间."
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
        </div>

        <div class="addConfig">
            <div class="wtBg ptb10 ac basic">
                <span class="formLabel ar mr10">金额/概率</span>
                <input type="text" class="serIpt moneyC" style="width: 140px;" id="money" name="money" placeholder="金额."/>
                <input type="text" class="serIpt probabilityC" style="width: 140px;" id="probability" name="probability"
                       placeholder="概率."/>
                <span class="formLabel" style="width: 30px; line-height: 32px; cursor: pointer;"></span>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">倒计时</span>
            <input type="text" class="serIpt" id="activityCountdown" name="activityCountdown" placeholder="输入倒计时."/>
        </div>

        <div class="clearfix ptb10">
            <div class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">是否开启</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt isOpen" style="line-height: 23px;">否</div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt" onclick="showUserSpecialty()">
                    <li data-value="0">否</li>
                    <li data-value="1">是</li>
                </ul>
                <input type="hidden" id="isOpen" name="isOpen" value="0"/>
            </div>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="submitBtn" type="submit" class="pageBtn popFormBtn">确定</button>
            <button id="addConfig" style="width: 135px;" type="button" class="pageBtn popFormBtn">增加金额/概率</button>
        </div>
    </form>
</div>
</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/activity/redPack.js"></script>
</html>
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
            class="verMid ml10 colorF">机器发言</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="机器发言" name="name"/>
        <jsp:param value="运营设置" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <div style="right: 10px; top: 20px">
                <button type="button" class="pageBtn addBtn">新增发言</button>
                <button type="button" class="pageBtn userBtn">机器人管理</button>
                <button type="button" class="pageBtn settingBtn">发言设置</button>
                <button type="button" class="pageBtn refBtn">刷新</button>
            </div>
        </div>

        <div class="mainContent">
            <div class="bar" style="height: 40px;">
                <div id="showpager" class="pages posRel ac fr">
                    <div class="color6 ilblock">共<span class="allrecords"></span> 条, 每页 <span class="eachrecords"></span>条</div>
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
                <div style="width: 16%">序号</div>
                <div style="width: 50%">发言内容</div>
                <div style="width: 16%">操作</div>
            </div>

            <div id="showrecords" class="ac"></div>
        </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>

<!-- 查询窗口
<div id="searchWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="searchBox" action="${pageContext.request.contextPath}/user/getManageUserList" method="post">
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">查询客户</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">名称</span>
            <input type="text" class="serIpt" name="roomName"/>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="searchBtn" type="button" class="pageBtn popFormBtn">确认</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div> -->

<!-- 添加窗口 -->
<div id="addWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="tableForm" action="${pageContext.request.contextPath}/live/saveMsg" method="post">
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">机器发言内容</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">发言内容</span>
            <input type="hidden"  id="msgId" name="msgId"/>
            <input type="text" class="serIpt" id="msgContent" name="msgContent"/>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="submitBtn" type="submit" class="pageBtn popFormBtn">确定</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>

<!-- 添加窗口 -->
<div id="settingWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="settingForm" action="${pageContext.request.contextPath}/live/settingAutoMessage" method="post">
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">机器自动发言设置</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">是否自动发言</span>
            <c:if test="${state == 0}">
            <select  class="serIpt" id="paramValue" name="paramValue">
            	<option value="0">否</option>
            	<option value="1">是</option>
            </select>
            </c:if>
            <c:if test="${state == 1 }">
             <select  class="serIpt" id="paramValue" name="paramValue">
            	<option value="1">是</option>
            	<option value="0">否</option>
            </select>
            </c:if>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="submitSetBtn" type="submit" class="pageBtn popFormBtn">确定</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>
</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/msg/index.js"></script>
</html>
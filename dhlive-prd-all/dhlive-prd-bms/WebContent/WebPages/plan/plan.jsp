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
        <jsp:param value="课程安排" name="name"/>
        <jsp:param value="运营设置" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <div style="right: 10px; top: 20px">
                <button type="button" class="pageBtn addBtn">新增</button>
                <button type="button" class="pageBtn settingBtn">设置</button>
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
                <div style="width: 10%">编号</div>
                <div style="width: 20%">直播老师</div>
                <div style="width: 30%">直播时间</div>
                <div style="width: 30%">课程介绍</div>
                <div style="width: 10%">操作</div>
            </div>

            <div id="showrecords" class="ac"></div>
        </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>

<!-- 设置窗口-->
<div id="settingWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="settingForm" action="${pageContext.request.contextPath}/plan/settingPlan" method="post">
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">设置课程表样式</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">是否显示课程介绍</span>
            <c:if test="${state == 3}">
                <select id="paramValue" class="paramValue" name="paramValue">
	            	<option value="3">显示</option>
	                <option value="2">不显示</option>
	            </select>
            </c:if>
			<c:if test="${state == 2}">
                <select id="paramValue" class="paramValue" name="paramValue">
	            	<option value="2">不显示</option>
	                <option value="3">显示</option>
	            </select>
            </c:if>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="submitSetBtn" type="submit" class="pageBtn popFormBtn">确认</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div> 

<!-- 添加窗口 -->
<div id="addWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="tableForm" action="${pageContext.request.contextPath}/plan/savePlan" method="post">
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">新增排班</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">编号</span>
            <input type="hidden"  id="id" name="id"/>
            <input type="text" class="serIpt" id="planNumber" name="planNumber"/>
        </div>
        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">直播老师</span>
            <input type="text" class="serIpt" id="planTeacher" name="planTeacher"/>
        </div>
        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">直播时间</span>
            <input type="text" class="serIpt" id="planTime" name="planTime"/>
        </div>
        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">课程介绍</span>
            <input type="text" class="serIpt" id="planIntroduce" name="planIntroduce"/>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="submitBtn" type="submit" class="pageBtn popFormBtn">确定</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>


</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/plan/plan.js"></script>
</html>
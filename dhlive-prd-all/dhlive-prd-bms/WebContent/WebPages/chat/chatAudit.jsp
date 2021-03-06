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
            class="verMid ml10 colorF">审核记录</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="审核记录" name="name"/>
        <jsp:param value="内容管理" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
        <form id="searchBox" action="${pageContext.request.contextPath}/user/getChatList" method="post" name="chatValue">
            <div class="posAbs right0">
                <button type="button" id="searchBtn" class="pageBtn searchBtn">查询</button>
                <button type="button"  class="pageBtn refBtn">刷新</button>
            </div>
            <div class="clearfix">
              <div class="serTag mr10 verTop ar fl">客户昵称</div>
                 <input type="text" id="opeMsg" name="opeMsg" class="serIpt fl"/>
              <div class="serTag mr10 verTop ar fl">审核时间</div>
              <div tabindex="0" class="serSelect fl posRel mr10 showSelectTime">
                <input type="text" id="timeStart"  name="ts" class="serIpt serIptNar fl date-hook"/>
                <span class="serToTxt block fl ac">至</span>
                <input type="text" id="timeEnd" name="tt" class="serIpt serIptNar fl date-hook"/>
              </div>
              </div>
              <div class="clearfix mt15">
                <div class="serTag mr10 verTop ar fl">审核人</div>
                <input type="text" id="optName" name="opName" class="serIpt fl"/>
                </div>  
        </form>
    </div>

        <div class="mainContent">
            <div class="bar" style="height: 40px;">
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
                <div style="width: 40%">聊天内容</div>
                <div style="width: 20%">审核人</div>
                <div style="width: 20%">审核时间</div>
            </div>
               <div id="showrecords" class="ac"></div>
        </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>

<!-- 查询客户窗口 
<div id="searchWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="searchBox" action="${pageContext.request.contextPath}/user/getChatList" method="post">

        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">查询</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">聊天内容</span>
            <input type="text" class="serIpt" name="opeMsg" placeholder="请输入聊天内容"/>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="searchBtn" type="button" class="pageBtn popFormBtn">确认</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>
-->
</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/chat/chatAudit.js"></script>
</html>
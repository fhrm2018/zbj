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
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="聊天记录" name="name"/>
        <jsp:param value="水滴" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <div>
                <button type="button" class="pageBtn addBtn">查询</button>
                <button type="button" class="pageBtn refBtn">刷新</button>
            </div>
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
                <div style="width: 15%">发送人</div>
                <div style="width: 15%">接收人</div>
                <div style="width: 50%">发送内容</div>
              <!--   <div style="width: 10%">状态</div>  --> 
                <div style="width: 20%">发送时间</div>
            </div>
               <div id="showrecords" class="ac"></div>
           </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>

<!-- 查询窗口 -->
<div id="addWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="tableForm" action="${pageContext.request.contextPath}/water/getWaterList" method="post" name="chatValue">

        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">查询</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>
        
        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">输入游客昵称</span>
            <input type="text" id="name"class="serIpt" name="fromNickName"/>
        </div>
         <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">选择查询的助理</span>
            <select  class="serIpt" id="paramValue" name="toNickName">
                           <option></option>
              <c:forEach  items="${assistant}"  var="str">
                        <option value="${str.userNickName}">${str.userNickName}</option>
                                            </c:forEach>                         
            </select>
        </div>  
        <div style="text-align: center" class="pt20">
             <button id="submitBtn" type="button" class="pageBtn popFormBtn">确认</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>

</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/water/water.js"></script>
</html>
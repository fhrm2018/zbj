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
            class="verMid ml10 colorF">聊天记录</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="聊天记录" name="name"/>
        <jsp:param value="内容设置" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <form id="tableForm"action="${pageContext.request.contextPath}/water/getWaterList" method="post" name="chatValue">
            <div class="posAbs right0">
                 <button id="submitBtn" type="button" class="pageBtn btn">查询</button>
                 <button type="button" class="pageBtn refBtn">刷新</button> 
             </div>
              <div class="clearfix">
                    <div class="serTag mr10 verTop ar fl">选择助理</div>
                    <div tabindex="0" class="serSelect middle fl posRel mr10">
                        <div class="text posRel wtBg plr10"></div>
                        <ul id="statusList" tabindex="0" class="posAbs wtBg ovfHid">
                        	<li data-value="">全部助理</li>
                            <c:forEach  items="${assistant}"  var="str">
                        		<li data-value="${str.userNickName}">${str.userNickName}</li>
                            </c:forEach>
                        </ul>
                        <input type="hidden" name="toNickName" id="paramValue" value=""/>
                    </div>
                    
                    <div class="serTag mr10 verTop ar fl">聊天时间</div>
                 	 <div tabindex="0" class="serSelect fl posRel mr10 showSelectTime">
                        <input type="text" id="timeStart" name="ts" class="serIpt serIptNar fl date-hook" />
                        <span class="serToTxt block fl ac">至</span>
                        <input type="text" id="timeEnd" name="tt" class="serIpt serIptNar fl date-hook" />
                    </div>			
              </div>
              <div class="clearfix mt15">
                 	<div class="serTag mr10 verTop ar fl">客户昵称</div>
                    <input type="text" name="fromNickName" class="serIpt fl" />
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
<div id="addWin" class="popForm popFormWide wtBg posFixed hide" style="width:500px;height:700px;">
             <p style="font-size:40px; text-align:center" >查询结果</p>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">返回</button>
</div>

</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/water/water.js"></script>
</html>
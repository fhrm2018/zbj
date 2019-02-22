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
            class="verMid ml10 colorF">在线时长</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="在线时长" name="name"/>
        <jsp:param value="助理管理" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <form id="tableForm"action="${pageContext.request.contextPath}/roomMsg/msgCount" method="post" name="chatValue">
            <div class="posAbs right0">
                 <button id="submitBtn" type="button" class="pageBtn btn">查询</button>
                 <button type="button" class="pageBtn refBtn">刷新</button> 
             </div>
              <div class="clearfix">
          
                    <div class="serTag mr10 verTop ar fl">统计时间</div>
                 	 <div tabindex="0" class="serSelect fl posRel mr10 showSelectTime">
                        <input type="text" id="beginDate" name="beginDate" class="serIpt serIptNar fl date-hook" onfocus="WdatePicker()" value="${beginDate }" />
                        <span class="serToTxt block fl ac">至</span>
                        <input type="text" id="endDate" name="endDate" class="serIpt serIptNar fl date-hook" onfocus="WdatePicker()" value="${endDate }" />
                    </div>			
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
                <div style="width: 15%">日期</div>
                <c:forEach items="${namesList}" var="item">
                <div style="width: ${width}%">${item }</div>
                </c:forEach>
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
<script src="${pageContext.request.contextPath}/static/modules/online/count.js"></script>
<script type="text/javascript">
	var width='${width}';
</script>
</html>
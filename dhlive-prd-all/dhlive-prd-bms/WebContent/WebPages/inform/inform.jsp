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
            class="verMid ml10 colorF">公告板</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="公告板" name="name"/>
        <jsp:param value="直播配置" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <div style="right: 10px; top: 20px">
                <button type="button" class="pageBtn addBtn hide">新增</button>
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
                <div style="width: 20%">创建时间</div>
                <div style="width: 10%">标题</div>
                <div style="width: 40%">内容</div>
                <div style="width: 10%">状态</div>
                <div style="width: 20%">操作</div>
            </div>
               <div id="showrecords" class="ac"></div>
           </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>

<!-- 查询窗口 -->
<div id="addWin" class="popForm popFormWide wtBg posFixed hide" style="width: 600px">
    <form id="tableForm" action="${pageContext.request.contextPath}/live/inform/saveInform" method="post">
            <input type="hidden"  id="id" name="id"/>
            <input type="hidden"  id="informState" name="informState"/>
            <input type="hidden"  id="createTime" name="createTime"/>
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle"></div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>
        <div class="wtBg ac" style="padding-bottom: 15px;">
            <input type="text" class="serIpt" id="informTitle" name="informTitle" placeholder="标题"/>
        </div>
        <div class="wtBg ptb10">
            <textarea  id="informContent" name="informContent"></textarea>
        </div>
        <div style="text-align: center" class="pt20">
            <button id="submitBtn" type="submit" class="pageBtn popFormBtn">确定</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>

</body>
<script charset="utf-8" src="${pageContext.request.contextPath}/static/js/lib/kindeditor/kindeditor-all-min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/inform/inform.js"></script>
</html>
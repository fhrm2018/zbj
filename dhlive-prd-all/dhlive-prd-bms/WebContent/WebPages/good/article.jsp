<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理后台</title>
    <jsp:include page="../common/public.jsp"/>
    <link href="${pageContext.request.contextPath}/static/css/live.css" rel="stylesheet" type="text/css"/>
    <script src="${pageContext.request.contextPath}/static/js/lib/date/WdatePicker.js"></script>
</head>

<body>
<div class="mainTitle">
    <div class="titleContent">
        <span class="lgLine ilblock ovfHid ml20 verMid"></span> <span
            class="verMid ml10 colorF">日报</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="日报" name="name"/>
        <jsp:param value="干货" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
         <div class="mainSearch">
            <ul id="status-nav" class="navMenu wtBg pl15 ac hide">
            </ul>
            <div style="right: 10px; top: 20px;">
                <button type="button" class="pageBtn addBtn">添加</button>
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
                <div style="width: 25%">标题</div>
                <div style="width: 25%">浏览人数</div>
                <div style="width: 25%">上传时间</div>
                <div style="width: 25%">操作</div>
            </div>
            <div id="showrecords" class="ac"></div>
        </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>

<!-- 添加文章 -->
<div id="addWin" class="popForm popFormWide wtBg posFixed hide" style="width: 600px">
    <form id="tableForm" action="${pageContext.request.contextPath}/goods/saveArticle" method="post">
        <input type="hidden" id="id" name="id"/>
        <div class="title flexWrap ">
            <div class="flexCon fz16"></div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ac" style="padding-bottom: 15px;">
            <input type="text" class="serIpt" id="articleTitle" name="articleTitle" placeholder="标题"/>
        </div>

        <div class="wtBg ac" style="padding-bottom: 15px;">
            <input type="text" class="serIpt" id="watchCount" name="watchCount" placeholder="浏览人数"/>
        </div>

        <div class="wtBg ptb10">
            <textarea id="content" name="articleContent"></textarea>
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
<script src="${pageContext.request.contextPath}/static/modules/good/article.js"></script>
</html>
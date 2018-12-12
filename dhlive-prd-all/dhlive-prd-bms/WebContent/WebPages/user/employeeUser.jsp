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
            class="verMid ml10 colorF">后台用户</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="后台用户" name="name"/>
        <jsp:param value="后台设置" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <div style="right: 10px; top: 20px;">
                <button type="button" class="pageBtn addBtn">添加用户</button>
                <button type="button" class="pageBtn searchBtn">查询用户</button>
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
                <div style="width: 20%">登录帐号</div>
                <div style="width: 20%">姓名</div>
                <div style="width: 20%">角色</div>
                <div style="width: 20%">创建时间</div>
                <div style="width: 20%">操作</div>
            </div>

            <div id="showrecords" class="ac"></div>
        </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>

<!-- 查询用户窗口 -->
<div id="searchWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="searchBox" action="${pageContext.request.contextPath}/user/getManageUserList" method="post">
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">查询客户</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">姓名</span>
            <input type="text" class="serIpt" name="name" placeholder="请输入昵称"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">登陆账号</span>
            <input type="text" class="serIpt" name="mobile" placeholder="请输入账号"/>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="searchBtn" type="button" class="pageBtn popFormBtn">确认</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>

<!-- 添加后台用户窗口 -->
<div id="addWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="tableForm" action="${pageContext.request.contextPath}/user/saveEmployee" method="post">
        <input type="hidden" id="employeeId" name="id"/>
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle" id="liveTitle">添加后台用户</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">登陆账号</span>
            <input type="text" class="serIpt" id="mobile" name="mobile" placeholder="用于登录使用"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">姓名</span>
            <input type="text" class="serIpt" id="name" name="name" placeholder="非必填"/>
        </div>

        <div class="wtBg ptb10 ac" id="pass">
            <span class="formLabel ar mr10">登录密码</span>
            <input type="password" class="serIpt" id="password" name="password"/>
        </div>
        
        <div class="clearfix ptb10" style="position:relative;z-index:210;">
            <div tabindex="0" class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">角色</span>
            </div>
            <div tabindex="0" id="selectRoleId" class="serSelect fl posRel mr10" data-default="客服" data-defval="2">
                <div class="text posRel wtBg plr10 serIpt isShow roleId" style="line-height: 23px"></div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt">
                    <li data-value="1">管理员</li>
                    <li data-value="2">客服</li>
                    <li data-value="3">组长</li>
                </ul>
                <input type="hidden" id="roleId" name="roleId" value="2"/>
            </div>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="submitBtn" type="submit" class="pageBtn popFormBtn">确定</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>

</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/user/employeeUser.js"></script>
</html>
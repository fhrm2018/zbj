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
            class="verMid ml10 colorF">VIP客户</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="VIP" name="name"/>
        <jsp:param value="客户管理" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <ul id="status-nav" class="navMenu wtBg pl15 ac">
                <li class="cur">
                    <a class="block all" style="cursor: pointer">全部客户</a>
                </li>
                <li>
                    <a class="block online" style="cursor: pointer">在线客户</a>
                </li>
                <li>
                    <a class="block gag" style="cursor: pointer">禁言客户</a>
                </li>
                <li>
                    <a class="block black" style="cursor: pointer">黑名单客户</a>
                </li>
            </ul>
            <div class="posAbs" style="right: 10px; top: 20px;">
                <button type="button" class="pageBtn addBtn">添加客户</button>
                <button type="button" class="pageBtn searchBtn">查询用户</button>
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
                <div style="width: 10%">昵称</div>
                <div style="width: 5%">等级</div>
                <div style="width: 10%">手机号</div>
                <div style="width: 15%">创建时间</div>
                <div style="width: 15%">最后登录时间</div>
                <div style="width: 10%">访问次数</div>
                <div style="width: 10%">观看时长</div>
                <div style="width: 10%">创建人</div>
                <div style="width: 10%">ip</div>
                <div style="width: 10%">操作</div>
            </div>
            <div id="showrecords" class="ac"></div>
        </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>

<!-- 查询客户窗口 -->
<div id="searchWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="searchBox" action="${pageContext.request.contextPath}/user/getVipUserList" method="post">

        <input type="hidden" id="isOnline" name="isOnline"/>
        <input type="hidden" id="isGag" name="isGag"/>
        <input type="hidden" id="isBlack" name="isBlack"/>

        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">查询客户</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">昵称</span>
            <input type="text" class="serIpt" name="userNickName" placeholder="请输入昵称"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">手机号</span>
            <input type="text" class="serIpt" name="userTel" placeholder="请输入手机号"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">IP</span>
            <input type="text" class="serIpt" id="lastLoginIp" name="lastLoginIp" placeholder="请输入IP"/>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="searchBtn" type="button" class="pageBtn popFormBtn">确认</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>

<!-- 添加VIP客户窗口 -->
<div id="addWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="tableForm" action="${pageContext.request.contextPath}/user/saveVipUser" method="post">
        <input type="hidden" id="userId" name="userId"/>
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">添加VIP用户</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">昵称</span>
            <input type="text" class="serIpt" id="userNickName" name="userNickName"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">手机号</span>
            <input type="text" class="serIpt" id="userTel" name="userTel" placeholder="用于登录使用"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">登录密码</span>
            <input type="text" class="serIpt" id="userPass" name="userPass"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">姓名</span>
            <input type="text" class="serIpt" id="userRealName" name="userRealName" placeholder="非必填"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">QQ号</span>
            <input type="text" class="serIpt" id="userQq" name="userQq"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">免费试听时间/秒(永久请输入-1)</span>
            <input type="text" class="serIpt" id="tempWatchTime" name="tempWatchTime" value="-1"/>
        </div>

        <div class="clearfix ptb10" style="position:relative;z-index:220;">
            <div class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">用户角色</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt" style="line-height: 23px;">会员</div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt">
                    <c:forEach var="v" items="${groups}">
                        <li data-value=${v.id}>${v.name}</li>
                    </c:forEach>
                </ul>
                <input type="hidden" id="groupId" name="groupId" value="5"/>
            </div>
        </div>


        <div class="clearfix ptb10" style="position:relative;z-index:210;">
            <div tabindex="0" class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">会员等级</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt isShow userLevel" style="line-height: 23px">VIP 2（地主）</div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt">
                     <li data-value="-1">VIP 0</li>
                     <li data-value="1">VIP 1</li>
                     <li data-value="2">VIP 2</li>
                     <li data-value="3">VIP 3</li>
                     <li data-value="4">VIP 4</li>
                     <li data-value="5">VIP 5</li>
                     <li data-value="6">VIP 6</li>
	    			 <li data-value="7">VIP 7</li>
                </ul>
                <input type="hidden" id="userLevel" name="userLevel" value="2"/>
            </div>
        </div>


        <div class="clearfix ptb10">
            <div tabindex="0" class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">所属直播间</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt roomName" style="line-height: 23px">所有</div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt">
                    <li data-value="">所有</li>
                    <c:forEach var="v" items="${rooms}">
                        <li data-value=${v.roomId}>${v.roomName}</li>
                    </c:forEach>
                </ul>
                <input type="hidden" id="roomId" name="roomId" value=""/>
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
<script src="${pageContext.request.contextPath}/static/modules/user/vipUser.js"></script>
</html>
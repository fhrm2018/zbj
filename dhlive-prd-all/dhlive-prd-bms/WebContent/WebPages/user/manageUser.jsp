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
            class="verMid ml10 colorF">助理列表</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="助理列表" name="name"/>
        <jsp:param value="助理管理" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <div style="right: 10px; top: 20px;">
                <button type="button" class="pageBtn addBtn">添加助理</button>
                <button type="button" class="pageBtn searchBtn">查询助理</button>
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
                <div style="width: 15%">昵称</div>
                <div style="width: 5%">角色</div>
                <div style="width: 15%">登陆账号</div>
                <div style="width: 15%">创建时间</div>
                <div style="width: 15%">最后登录时间</div>
                <div style="width: 15%">IP</div>
                <div style="width: 25%">操作</div>
            </div>

            <div id="showrecords" class="ac"></div>
        </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>

<!-- 查询客户窗口 -->
<div id="searchWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="searchBox" action="${pageContext.request.contextPath}/user/getManageUserList" method="post">
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
            <span class="formLabel ar mr10">登陆账号</span>
            <input type="text" class="serIpt" name="userTel" placeholder="请输入登陆账号"/>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="searchBtn" type="button" class="pageBtn popFormBtn">确认</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>

<!-- 添加客户窗口 -->
<div id="addWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="tableForm" action="${pageContext.request.contextPath}/user/saveManageUser" method="post">
        <input type="hidden" id="userId" name="userId"/>
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">添加管理用户</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">昵称</span>
            <input type="text" class="serIpt" id="userNickName" name="userNickName"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">登陆账号</span>
            <input type="text" class="serIpt" id="userTel" name="userTel" placeholder="用于登录使用"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">登录密码</span>
            <input type="text" class="serIpt" id="userPass" name="userPass"/>
        </div>


        <div class="clearfix ptb10" style="position:relative;z-index:210;">
            <div class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px;">用户性别</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt sexName" style="line-height: 23px;">男</div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt">
                    <li data-value="0">男</li>
                    <li data-value="1">女</li>
                </ul>
                <input type="hidden" id="userSex" name="userSex" value="0"/>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">QQ号</span>
            <input type="text" class="serIpt" id="userQq" name="userQq"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">二维码</span>
            <input type="file" class="serIpt" name="qrCode" accept="image/*"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">头像</span>
            <input type="file" class="serIpt" name="photo" accept="image/*"/>
        </div>

        <div class="clearfix ptb10" style="position:relative;z-index:210;">
            <div class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px;">用户角色</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt groupName" style="line-height: 23px;">${groups[0].name}</div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt" onclick="showHideField()">
                    <c:forEach var="v" items="${groups}">
                        <li data-value=${v.id}>${v.name}</li>
                    </c:forEach>
                </ul>
                <input type="hidden" id="groupId" name="groupId" value="${groups[0].id}"/>
            </div>
        </div>

        <div class="wtBg ptb10 ac userIntroduction">
            <span class="formLabel ar mr10">简介</span>
            <input type="text" class="serIpt" id="userIntroduction" name="userIntroduction"/>
        </div>
        <div class="wtBg ptb10 ac praise hide">
            <span class="formLabel ar mr10">点赞数</span>
            <input type="text" class="serIpt" id="praise" name="praise"/>
        </div>
        <%--<div class="wtBg ptb10 ac userSpecialty hide">--%>
        <%--<span class="formLabel ar mr10">擅长内容</span>--%>
        <%--<input type="text" class="serIpt" id="userSpecialty" placeholder="非必填" name="userSpecialty"/>--%>
        <%--</div>--%>

        <div class="clearfix ptb10">
            <div tabindex="0" class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">所属直播间</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt roomName" style="line-height: 23px">${room.roomName}</div>
                <input type="hidden" id="roomId" name="roomId" value="${room.roomId}"/>
            </div>
        </div>


        <div class="clearfix ptb10">
            <div tabindex="0" class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">是否上线</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt isOnline" style="line-height: 23px;">上线</div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt">
                    <li data-value=0>下线</li>
                    <li data-value=1>上线</li>
                </ul>
                <input type="hidden" id="isOnline" name="isOnline" value="1"/>
            </div>
        </div>


        <div style="text-align: center" class="pt20">
            <button id="submitBtn" type="submit" class="pageBtn popFormBtn">确定</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>

<!-- 添加小号窗口 -->
<div id="addSmallWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="smallTableForm" action="${pageContext.request.contextPath}/user/saveSmallUser" method="post">
        <input type="hidden" id="smallId" name="smallId"/>
        <input type="hidden" id="smallUserId" name="userId" value=""/>
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">添加小号</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">昵称</span>
            <input type="text" class="serIpt" id="smallName" name="smallName"/>
        </div>

        <div class="clearfix ptb10">
            <div class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">用户等级</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt levelName" style="line-height: 23px;">VIP 2（地主）</div>
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
                <input type="hidden" id="smallLevel" name="smallLevel" value="2"/>
            </div>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="submitSmallBtn" type="submit" class="pageBtn popFormBtn">确定</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>


<!-- 查看小号窗口 -->
<div id="viewSmallWin" class="popForm popFormWide wtBg posFixed hide">
    <div class="title flexWrap ">
        <div class="flexCon fz16 liveTitle">查看小号</div>
        <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
            <span class="icon block"></span></a>
        </div>
    </div>

    <div class="tableTitle flexWrap ac">
        <div style="width: 33%">昵称</div>
        <div style="width: 33%">用户等级</div>
        <div style="width: 33%">操作</div>
    </div>

    <div class="ac smallList" style="overflow: auto; height: 616px;"></div>

</div>

</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/user/manageUser.js"></script>
</html>
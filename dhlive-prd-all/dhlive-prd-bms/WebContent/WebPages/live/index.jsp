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
            class="verMid ml10 colorF">直播室</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="直播室列表" name="name"/>
        <jsp:param value="直播配置" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <div style="right: 10px; top: 20px">
                <button type="button" class="pageBtn addBtn">创建直播间</button>
             <!--   <button type="button" class="pageBtn searchBtn">查询</button>  --> 
               <!--  <button type="button" class="pageBtn uploadBtn">更新课程表</button>  -->
                <button type="button" class="pageBtn refBtn">刷新</button>
              <!--   <c:if test="${!empty fish}">
                    <button type="button" class="pageBtn extXls" style="width: 150px;">导出聊天消息</button>
                </c:if>   -->
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
                <div style="width: 16%">名称</div>
                <div style="width: 16%">创建时间</div>
                <div style="width: 16%">状态</div>
                <div style="width: 16%">直播类型</div>
                <div style="width: 16%">直播室地址</div>
                <div style="width: 16%">操作</div>
            </div>

            <div id="showrecords" class="ac"></div>
        </div>
    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>

<!-- 查询窗口 -->
<div id="searchWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="searchBox" action="${pageContext.request.contextPath}/user/getManageUserList" method="post">
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">查询客户</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">名称</span>
            <input type="text" class="serIpt" name="roomName"/>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="searchBtn" type="button" class="pageBtn popFormBtn">确认</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>

<!-- 添加窗口 -->
<div id="addWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="tableForm" action="${pageContext.request.contextPath}/live/saveLiveRoom" method="post">
        <input type="hidden" id="roomId" name="roomId"/>
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">创建直播间</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">名称</span>
            <input type="text" class="serIpt" id="roomName" name="roomName"/>
        </div>

        <div class="clearfix ptb10" style="position:relative;z-index:220;">
            <div tabindex="0" class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">直播类型</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt roomType" style="line-height: 23px">免费直播</div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt">
                    <li data-value="0">免费直播</li>
                    <li data-value="1">VIP直播</li>
                </ul>
                <input type="hidden" id="roomType" name="roomType" value="0"/>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">游客观看时长</span>
            <input type="text" class="serIpt" id="tempWatchTime" name="tempWatchTime"/>
        </div>

        <div class="clearfix ptb10" style="position:relative;z-index:210;">
            <div tabindex="0" class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">直播等级</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt roomLiveLevel" style="line-height: 23px">入门</div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt">
                    <li data-value="0">入门</li>
                    <li data-value="1">进阶</li>
                </ul>
                <input type="hidden" id="roomLiveLevel" name="roomLiveLevel" value="0"/>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">直播内容</span>
            <input type="text" class="serIpt" id="roomTag" name="roomTag"/>
        </div>

        <div class="clearfix ptb10">
            <div tabindex="0" class="serSelect fl posRel">
                <span class="formLabel ar mr10" style="width: 157px">是否可见</span>
            </div>
            <div tabindex="0" class="serSelect fl posRel mr10">
                <div class="text posRel wtBg plr10 serIpt isShow" style="line-height: 23px">可见</div>
                <ul tabindex="0" class="posAbs wtBg ovfHid serIpt">
                    <li data-value="0">可见</li>
                    <li data-value="1">不可见</li>
                </ul>
                <input type="hidden" id="isShow" name="isShow" value="0"/>
            </div>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">基础人数</span>
            <input type="text" class="serIpt" id="baseNum" name="baseNum"/>
        </div>

        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">视频源地址</span>
            <input type="text" class="serIpt" id="roomStreamServer" name="roomStreamServer"/>
        </div>

        <div style="text-align: center" class="pt20">
            <button id="submitBtn" type="submit" class="pageBtn popFormBtn">确定</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>


<!-- 上传课程表 -->
<div id="uploadWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="uploadForm" action="${pageContext.request.contextPath}/live/uploadRoomSyllabus" method="post">
        <div class="title flexWrap ">
            <div class="flexCon fz16 liveTitle">上传/更新课程表</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>


        <div class="wtBg ptb10 ac">
            <span class="formLabel ar mr10">课程表</span>
            <input type="file" class="serIpt" name="file" accept="image/*"/>
        </div>



        <div style="text-align: center" class="pt20">
            <button id="uploadBtn" type="submit" class="pageBtn popFormBtn">确定</button>
            <button class="pageBtn popFormBtn" onclick="closePopForm(this);return !1;">取消</button>
        </div>
    </form>
</div>

<!-- 导出聊天消息 -->
<form id="exportDataForm" action="${pageContext.request.contextPath}/live/exportChatMsg" method="post"></form>

</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script src="${pageContext.request.contextPath}/static/modules/live/index.js"></script>
</html>
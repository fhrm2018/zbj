<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>

    <title>至尊阁直播室 - 期货在线直播_期货投资咨询_期市在线_国际期货直播</title> 
	<meta name="keywords" content=" 至尊阁,至尊阁期货直播室,国际期货直播间,期货在线直播,期货投资咨询,期市在线,国际期货直播"> 
	<meta name="description" content=" 至尊阁直播室为广大国际期货投资者提供专业的国际期货直播间,期货在线直播,期货投资咨询,期市在线,国际期货直播,至尊阁期货直播室深受广大国际期货投资者的喜爱,国际期货直播间、恒指期货直播室、股指期货直播室就选至尊阁期货直播室,想看专业的期货喊单直播,至尊阁直播间是不错的选择！">
	<link rel="shortcut icon" href="/static/images/favicon.png" type="image/x-icon"/>
    <jsp:include page="common/public.mobile.jsp"/>
    <script type="text/javascript">
        //全局变量
        var roomId = '${room.roomId}',//聊天室ID
            chatRoomId = '${room.roomGroupId}',//聊天室ID
            tempWatchTime = '${room.tempWatchTime}',
            isAdmin = 0,
            lookTime = -1,
            activityConfigId = '${config.id}',
            activityCountdown = '${config.activityCountdown}',
	   		imgPath = '${imagePath}',
			url='${url}',
            talkUserId = '',
            type_flag = 0,
            isOver = ${isOver},
            chatImgs = new Array(),
            isCanSend = true,
            groupId =    ${loginedUserLogin.groupId},
            tempWatchTime = ${room.tempWatchTime},
            loginInfo = {
                'sdkAppID': '${sdkAppId}', //用户所属应用id,必填
                'appIDAt3rd': '${sdkAppId}', //用户所属应用id，必填
                'accountType': '${accountType}', //用户所属应用帐号类型，必填
                'identifier': '${loginedUserLogin.userId}', //当前用户ID,必须是否字符串类型，选填
                'identifierNick': '${loginedUserLogin.userNickName}', //当前用户昵称，选填
                'userSig': '', //当前用户身份凭证，必须是字符串类型，选填
                'headurl': ''//当前用户默认头像，选填
            },
            userInfo = {
                'id': '${loginedUserLogin.userId}', //当前用户ID,必须是否字符串类型，选填
                'nickName': '${loginedUserLogin.userNickName}', //当前用户昵称，选填
                'groupId': ${loginedUserLogin.groupId},
                'level': ''//会员等级
            },
            utmSource = '${utmSource}',
			relation = {
				'userId': '${relation.userId}',	
				'userNickName': '${relation.userNickName}',	
				'groupId': '${relation.groupId}',	
				'userLevel': '${relation.userLevel}',	
				'userIntroduction': '${relation.userIntroduction}',	
				'userPhoto': '${relation.userPhoto}',	
				'userQrcode': '${relation.userQrcode}',	
				'userQq': '${relation.userQq}',	
			},
            msgIdMap = {},
            selSess,//聊天session
            accountMode = 0;//帐号模式，0-表示独立模式，1-表示托管模式
        if (groupId == '1') {
            loginInfo.identifier = 'yk-' + loginInfo.identifier;
        }
        if (groupId == '2' || groupId == '3' || groupId == '4') {
            isAdmin = 1;
        }
        if (groupId == '5') {
            loginInfo.identifier = 'vip-' + loginInfo.identifier;
            userInfo.level = '${vip.userLevel}';
        }
        function createNewGuest(){
        	var jqxhr = $.ajax({
               url: ctx + '/live/createNewUser',
                type: 'POST',
                data: {'utmSource':utmSource,'url':url},
                async: false,
            });
            jqxhr.done(function (data) {
                if (data.code == '1000') {
                    var userRes = data.data.user;
                    var relationRes = data.data.onLineZL;
                    userInfo.id = userRes.userId;
                    userInfo.nickName = userRes.userNickName;
                    userInfo.id = userRes.userId;
                    
                    loginInfo.identifier = 'yk-' + userInfo.id ;
                    loginInfo.identifierNick = userInfo.nickName ;
                    
                    relation.userId = relationRes.userId;
                    relation.userNickName = relationRes.userNickName;
                    relation.groupId = relationRes.groupId;
                    relation.userLevel = relationRes.userLevel;
                    relation.userIntroduction = relationRes.userIntroduction;
                    relation.userPhoto = relationRes.userPhoto;
                    relation.userQrcode = relationRes.userQrcode;
                    relation.userQq = relationRes.userQq;
                    
                }
           });
        }
        
        var isInit = false;
        if(userInfo.id == ''){
        	createNewGuest();
        	isInit = true;
        }
       function visit(){
        	var jqxhr = $.ajax({
                url: ctx + '/live/visit',
                type: 'POST',
                data: {'url':url},
                async: false,
            });
            jqxhr.done(function (data) {
               
           });
        }
        $(function () {
        	function initNewGuestPage(){
        		$('.inintPage-user-name').html(userInfo.nickName);
        		$('.inintPage-relation-qq').html(relation.userQq);
        		$('.inintPage-relation-userIntroduction').html(relation.userIntroduction);
        		$('.inintPage-relation-userQrcode').attr('src',imgPath + "ori/"+relation.userQrcode);
        		$('.inintPage-relation-userPhoto').attr('src',imgPath + "ori/"+relation.userPhoto);
        		$('.initPage-mqqwpa').attr('href',"mqqwpa://im/chat?chat_type=wpa&uin="+relation.userQq+"&version=1&src_type=web&web_src=oicqzone.com");
        		
        		$('#persionC2CMessageForm span').find('input[name="fromId"]').eq(0).val(userInfo.userId);
        		$('#persionC2CMessageForm span').find('input[name="fromNickName"]').eq(0).val(userInfo.nickName);
        		$('#persionC2CMessageForm span').find('input[name="toId"]').eq(0).val(relation.userId);
        		$('#persionC2CMessageForm span').find('input[name="toNickName"]').eq(0).val(relation.userNickName);
        		$('#persionC2CMessageForm span').find('input[name="persionToGroupId"]').eq(0).val(relation.groupId);
        	}
        	
        	if(isInit){
        		initNewGuestPage();
        	}
	 visit();
        });
    </script>

    <%--<script>--%>
        <%--var _hmt = _hmt || [];--%>
        <%--(function () {--%>
            <%--var hm = document.createElement("script");--%>
            <%--hm.src = "https://hm.baidu.com/hm.js?9d52ab323a254f2be51fc439997a06ee";--%>
            <%--var s = document.getElementsByTagName("script")[0];--%>
            <%--s.parentNode.insertBefore(hm, s);--%>
        <%--})();--%>
    <%--</script>--%>
</head>

<body>

<%--<!-- Google Tag Manager (noscript) -->--%>
<%--<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-5SPHMPQ"--%>
                  <%--height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>--%>
<%--<!-- End Google Tag Manager (noscript) -->--%>

<%--<!-- 辅助变量 -->--%>
<div class="header clearfix">
    <div class="fl">
        <a class="logo"><img src="../static/images/logo.png"></a>
    </div>

    <div class="kaihuBtn fr">
        <a class="initPage-mqqwpa" href="mqqwpa://im/chat?chat_type=wpa&uin=${relation.userQq}&version=1&src_type=web&web_src=oicqzone.com">
            <img src="../static/images/a1.png" alt="">
        </a>
        <a class="initPage-mqqwpa" href="mqqwpa://im/chat?chat_type=wpa&uin=${relation.userQq}&version=1&src_type=web&web_src=oicqzone.com">
            <img src="../static/images/a4.png" alt="">
        </a>
    </div>

    <%--<div class="fr topRight">--%>
        <%--<c:if test="${loginedUserLogin.groupId == 1}">--%>
            <%--<p class="username fz12" id="username">--%>
                <%--<img src="../static/images/yk.png" alt=""><span class="inintPage-user-name">${loginedUserLogin.userNickName}</span>--%>
            <%--</p>--%>
        <%--</c:if>--%>

        <%--<c:if test="${loginedUserLogin.groupId == 2}">--%>
            <%--<p class="username fz12" id="username">--%>
                <%--<img src="../static/images/VIP8.png" alt="">${loginedUserLogin.userNickName}--%>
            <%--</p>--%>
        <%--</c:if>--%>

        <%--<c:if test="${loginedUserLogin.groupId == 3}">--%>
            <%--<p class="username fz12" id="username">--%>
                <%--<img src="../static/images/zl.png" alt="">${loginedUserLogin.userNickName}--%>
            <%--</p>--%>
        <%--</c:if>--%>

        <%--<c:if test="${loginedUserLogin.groupId == 4}">--%>
            <%--<p class="username fz12" id="username">--%>
                <%--<img src="../static/images/VIP8.png" alt="">${loginedUserLogin.userNickName}--%>
            <%--</p>--%>
        <%--</c:if>--%>

        <%--<c:if test="${loginedUserLogin.groupId == 5}">--%>
            <%--<c:if test="${vip.userLevel == 1}">--%>
                <%--<p class="username fz12" id="username">--%>
                    <%--<img src="../static/images/VIP1.png" alt="">${loginedUserLogin.userNickName}--%>
                <%--</p>--%>
            <%--</c:if>--%>

            <%--<c:if test="${vip.userLevel == 2}">--%>
                <%--<p class="username fz12" id="username">--%>
                    <%--<img src="../static/images/VIP2.png" alt="">${loginedUserLogin.userNickName}--%>
                <%--</p>--%>
            <%--</c:if>--%>

            <%--<c:if test="${vip.userLevel == 3}">--%>
                <%--<p class="username fz12" id="username">--%>
                    <%--<img src="../static/images/VIP3.png" alt="">${loginedUserLogin.userNickName}--%>
                <%--</p>--%>
            <%--</c:if>--%>

            <%--<c:if test="${vip.userLevel == 4}">--%>
                <%--<p class="username fz12" id="username">--%>
                    <%--<img src="../static/images/VIP4.png" alt="">${loginedUserLogin.userNickName}--%>
                <%--</p>--%>
            <%--</c:if>--%>

            <%--<c:if test="${vip.userLevel == 5}">--%>
                <%--<div class="username fz12" id="username">--%>
                    <%--<img src="../static/images/VIP5.png" alt="">${loginedUserLogin.userNickName}--%>
                <%--</div>--%>
            <%--</c:if>--%>
        <%--</c:if>--%>

        <%--<c:if test="${loginedUserLogin.groupId == 1}">--%>
            <%--<div class="menuIcon">--%>
                <%--<a href="javascript:"><i></i></a>--%>
                <%--<div class="menuBtn hide">--%>
                    <%--<a class="loginA colorF fz16" onclick="toShow('login', 'register');">登录</a>--%>
                    <%--<a class="registerA colorF fz16 initPage-mqqwpa" href="mqqwpa://im/chat?chat_type=wpa&uin=${relation.userQq}&version=1&src_type=web&web_src=oicqzone.com">注册</a>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</c:if>--%>

        <%--<c:if test="${loginedUserLogin.groupId != 1}">--%>
            <%--<p class="settingA colorF fz12">设置</p>--%>
            <%--<div class="LoginOut hide" style="z-index: 999">--%>
                <%--<a class="passBtn" href="javascript:" onclick="toShow('modifyPass','register');">修改密码</a>--%>
                <%--<a class="outBtn">退出登录</a>--%>
            <%--</div>--%>
        <%--</c:if>--%>
    <%--</div>--%>


    <%--<div class="rightNav posAbs">--%>
    <%--<c:if test="${loginedUserLogin.groupId == 1 || loginedUserLogin.groupId == 5}">--%>
    <%--<a onclick="qqCustomer(${relation.userQq})" class="teacher"><span class="colorF">老师</span></a>--%>
    <%--<a class="service" onclick="qqCustomer(${relation.userQq})"><span class="colorF">客服</span></a>--%>
    <%--</c:if>--%>
    <%--</div>--%>
</div>

<div class="flexWrap flexAgCen">
    <div class="flexCon contentLeft">
        <div class="movieTop clearfix">
            <div class="viewTime fl">
                <c:if test="${loginedUserLogin.groupId == 1}">
                    <span class="remainderTime" id="remainderTime"></span>
                </c:if>
            </div>
        </div>
        <div class="movie">
            <div class="toLogin videoBox flexWrap">
                <div class="flexCon" id="play-container"><div id="dyyplayer" style="width:100%;height:100%"></div></div>
            </div>
            <div class="toLogin freeTipBox flexWrap flexAgCen hide">
                <div class="flexCon posRel">
                    <p class="colorF fz16 ac mt10">你当前为游客身份，免费时长已用完</p>
                    <p class="colorF fz16 ac mt10">登录后可免费观看</p>
                    <div class="loginOrReg ac mt10">
                        <a class="allbutton allbutton4 ilblock mr20" onclick="toShow('login', 'register');">登录</a>
                        <a class="allbutton allbutton4 ilblock initPage-mqqwpa" href="mqqwpa://im/chat?chat_type=wpa&uin=${relation.userQq}&version=1&src_type=web&web_src=oicqzone.com">注册</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="flexWrap flexAgCen">
    <div class="flexCon contentRight wtBg">
        <div class="notice posRel">
            <marquee onmouseover="this.stop()" class="noticeMsg noticeList" onmouseout="this.start()" scrollamount="5" direction="left">直播为嘉宾的个人观点，仅供参考，请谨慎交易！</marquee>
        </div>

        <div class="topRight">
            <%--<c:if test="${loginedUserLogin.groupId == 1}">--%>
                <%--<p class="username fz12" id="username">--%>
                    <%--<img src="../static/images/yk.png" alt=""><span class="inintPage-user-name">${loginedUserLogin.userNickName}</span>--%>
                <%--</p>--%>
            <%--</c:if>--%>

            <c:if test="${loginedUserLogin.groupId == 2}">
                <p class="username fz12" id="username">
                    <img src="../static/images/VIP8.png" alt="">${loginedUserLogin.userNickName}
                </p>
            </c:if>

            <c:if test="${loginedUserLogin.groupId == 3}">
                <p class="username fz12" id="username">
                    <img src="../static/images/zl.png" alt="">${loginedUserLogin.userNickName}
                </p>
            </c:if>

            <c:if test="${loginedUserLogin.groupId == 4}">
                <p class="username fz12" id="username">
                    <img src="../static/images/VIP8.png" alt="">${loginedUserLogin.userNickName}
                </p>
            </c:if>

            <c:if test="${loginedUserLogin.groupId == 5}">
                <c:if test="${vip.userLevel == 1}">
                    <p class="username fz12" id="username">
                        <img src="../static/images/VIP1.png" alt="">${loginedUserLogin.userNickName}
                    </p>
                </c:if>

                <c:if test="${vip.userLevel == 2}">
                    <p class="username fz12" id="username">
                        <img src="../static/images/VIP2.png" alt="">${loginedUserLogin.userNickName}
                    </p>
                </c:if>

                <c:if test="${vip.userLevel == 3}">
                    <p class="username fz12" id="username">
                        <img src="../static/images/VIP3.png" alt="">${loginedUserLogin.userNickName}
                    </p>
                </c:if>

                <c:if test="${vip.userLevel == 4}">
                    <p class="username fz12" id="username">
                        <img src="../static/images/VIP4.png" alt="">${loginedUserLogin.userNickName}
                    </p>
                </c:if>

                <c:if test="${vip.userLevel == 5}">
                    <div class="username fz12" id="username">
                        <img src="../static/images/VIP5.png" alt="">${loginedUserLogin.userNickName}
                    </div>
                </c:if>
            </c:if>

            <c:if test="${loginedUserLogin.groupId == 1}">

                <a class="loginA colorF fz16" onclick="toShow('login', 'register');">登录</a>
                <a class="registerA colorF fz16 initPage-mqqwpa" href="mqqwpa://im/chat?chat_type=wpa&uin=${relation.userQq}&version=1&src_type=web&web_src=oicqzone.com">注册</a>

            </c:if>

            <c:if test="${loginedUserLogin.groupId != 1}">
                <p class="settingA colorF fz12">设置</p>
                <div class="LoginOut hide" style="z-index: 999">
                    <a class="passBtn" href="javascript:" onclick="toShow('modifyPass','register');">修改密码</a>
                    <a class="outBtn">退出登录</a>
                </div>
            </c:if>
        </div>

        <div class="msg" id="msgBox">
            <div class="load">
                <img src="../static/images/load.gif"/>
            </div>
        </div>



    </div>
</div>

<div style="height: 6rem;    background: #f7f9fc;"></div>
<div class="footer ltGreyBg">
    <div class="flexWrap flexAgCen sendMsg posRel">
        <div class="expressionT posAbs hide" style="bottom:5.6rem;left:0;right:0;background-color: white;z-index: 1000;">
            <div class="video-discuss-emotion" id="video-discuss-emotion">
                <div class="video-emotion-pane">
                    <ul id="emotionUL">
                    </ul>
                </div>
            </div>
        </div>
        <a class="faceF"></a>
        <div class="flexCon ac">
            <div class="messageBox">
                <input class="message" placeholder="在此输入内容" id="sendMsgIpt"></input>
            </div>
        </div>
        <div id="sendMsgBtn" class="allbutton allbutton3" onclick="onSendMsg()">发送</div>
    </div>
</div>

<div class="pop hide" id="pop">

    <div class="login wtBg ac posRel ">
        <form id="loginForm" method="post" action="/user/login">
            <h2 class="tit fz20 ac">登录</h2>
            <input type="text" class="inp" placeholder="请输入手机号码" name="userTel" id="loginPhone"/>
            <input type="password" class="inp" placeholder="请输入密码" name="userPass" id="loginPwd"/>
            <input type="submit" class="redBut mb10 mt20" id="login" value="登录"/>
            <p class="errorMsg red3 fz20 hide"></p>
            <%--<a class="toRegister">注册账号</a>--%>
        </form>
        <a class="close"></a>
    </div>

    <div class="register popWrap mgAuto posRel ac">
        <form id="registerForm" method="post" action="/user/userRegistered">
            <img src="../static/images/kf.png" alt="">
            <a class="btn01" href="javascript:" onclick="qqCustomer()">
                <img src="../static/images/button.png" alt="">
            </a>
        </form>
        <a class="close close2"></a>
    </div>

    <div class="modifyPass popWrap  wtBg mgAuto posRel ac">
        <form id="modifyForm" method="post" action="/user/editCurrentUserPwd">
            <h2 class="tit fz20 ac">修改密码</h2>
            <input type="text" class="inp" placeholder="请输入原密码" name="oldPass"/>
            <input type="password" class="inp" placeholder="请输入新密码" name="newPass"/>
            <p class="errorMsg red3 fz12 hide"></p>
            <input type="submit" class="redBut mb10 mt20" value="确定"/>
        </form>
        <a class="close"></a>
    </div>

</div>

<%--<div class="lxqq">--%>
    <%--<a class="initPage-mqqwpa" href="mqqwpa://im/chat?chat_type=wpa&uin=${relation.userQq}&version=1&src_type=web&web_src=oicqzone.com">--%>
        <%--<img src="../static/images/qqIcon.gif" alt="">--%>
    <%--</a>--%>
<%--</div>--%>


<div class="worning hide"></div>
<iframe id="ifqq1" style="display:none;" src=""></iframe>
</body>
<script src="${staticHost}/js/common/common.js?version=${version}"></script>
<script src="${staticHost}/js/common/index.mobile.js?version=${version}"></script>
<script src="${staticHost}/js/lib/tls/webim.js?version=${version}"></script>
<script src="${staticHost}/js/chat/chat_group_notice.js?version=${version}"></script>
<script src="${staticHost}/js/chat/chat_base.js?version=${version}"></script>
<script src="${staticHost}/js/chat/chat.js?version=${version}"></script>
<script src="${staticHost}/js/common/jquery.endless-scroll-1.3.js"></script>
<script src="${staticHost}/js/consult/consult.js?version=${version}"></script>
<script src="${staticHost}/js/common/interval.js?version=${version}"></script>
<script type="text/javascript" src="https://cdn.58jinrongyun.com/helper/room_player_s.js?r=24139&id=dyyplayer"></script>
<script type="text/javascript">
    /* if(isOver == 0){
        var playerVar = new dyyPlayer({
            //room_id: '${room.roomStreamServer}', //对应房间ID，必要参数
			room_id:'23590',
            container: 'play-container', //播放器容器ID，必要参数
            width: '100%', //播放器宽度，可用数字、百分比等
            height: '100%', //播放器高度，可用数字、百分比等
            autostart: true, //是否自动播放，默认为false
            controlbardisplay: 'enable' //是否显示控制栏，值为：disable、enable默认为disable
        });
    } */

    $(function () {
        setTimeout(function () {
            $('#videoBox').css("z-index", 500);
        }, 2000)
    });
</script>
</html>

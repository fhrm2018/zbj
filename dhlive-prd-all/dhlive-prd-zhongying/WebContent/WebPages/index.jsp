<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>众盈俱乐部 - 国际期货直播间_期货直播室_恒指期货直播室_股指期货直播室</title>
    <meta name="keywords" content="众盈俱乐部,众盈国际期货直播室,国际期货直播间,恒指期货直播室,股指期货直播室,众盈恒指期货直播室,众盈股指期货直播室"/>
    <meta name="description" content="众盈俱乐部为广大国际期货投资者提供专业的国际期货,恒指期货,股指期货在线直播喊单,众盈国际期货直播室深受广大国际期货投资者的喜爱,国际期货直播间、恒指期货直播室、股指期货直播室就选众盈恒指期货直播室,众盈股指期货直播室,想看专业的国际期货喊单直播,众盈期货俱乐部是不错的选择！"/>
    <link rel="shortcut icon" href="${staticHost }/images/favicon.png" type="image/x-icon"/>
    <jsp:include page="common/public.jsp"/>
    <script type="text/javascript">
        //全局变量
        var roomId = '${room.roomId}',//聊天室ID
            chatRoomId = '${room.roomGroupId}',//聊天室ID
            tempWatchTime = '${room.tempWatchTime}',
            isAdmin = 0,
            lookTime=-1,
            activityConfigId = '${config.id}',
            activityCountdown = '${config.activityCountdown}',
            talkUserId = '',
            imgPath = '${imagePath}',
            chatImgs = new Array(),
            type_flag = 0,
	    	url='${url}',
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
            msgIdMap = {},
            selSess,//聊天session
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
                    userInfo.groupId = userRes.groupId;
                    groupId = userRes.groupId;
                    
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
		function qqCustomer(qq) {
		    $("body").find("iframe").eq(0).attr("src", "tencent://message/?uin=" + qq + "");
		}
        $(function () {
        	function initNewGuestPage(){
        		$('.inintPage-relation-name').html(relation.userNickName);
        		$('.inintPage-relation-qq').html(relation.userQq);
        		$('.inintPage-relation-userIntroduction').html(relation.userIntroduction);
        		$('.inintPage-relation-userQrcode').attr('src',imgPath + "ori/"+relation.userQrcode);
        		$('.inintPage-relation-userPhoto').attr('src',imgPath + "ori/"+relation.userPhoto);
        		$('#persionC2CMessageForm span').find('input[name="fromId"]').eq(0).val(userInfo.id);
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

</head>

<body class="changeBg1">
<%--<c:if test="${loginedUserLogin.groupId == 1}">--%>
    <%--<!-- WPA Button Begin -->--%>
    <%--<!-- 添加营销QQ -->--%>
    <%--<!-- WPA Button End -->--%>
<%--</c:if>--%>
<%--<div class="header hide">--%>
    <%--<div class="wrap mgAuto posRel">--%>
        <%--<div class="topLeft fl">--%>
            <%--<a class="logo"><img src="${staticHost }/images/logo.png"></a>--%>
            <%--&lt;%&ndash;<a class="a1 collectionBtn" id="Collection01" onclick="collection(document.title,window.location)">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<img src="${staticHost }/images/a7.png" alt="">&ndash;%&gt;--%>
            <%--&lt;%&ndash;</a>&ndash;%&gt;--%>

            <%--<a class="a1" onclick="classShow()">--%>
                <%--<img src="${staticHost }/images/a9.png" alt="">--%>
            <%--</a>--%>
            <%--<a style="display:none;" class="a1" target="_blank" href="http://www.ditan666.com/zm.html">--%>
                <%--<img src="${staticHost }/images/a10.png" alt="">--%>
            <%--</a>--%>
            <%--<a class="a1" onclick="qqCustomer()">--%>
                <%--<img src="${staticHost }/images/a1.png" alt="">--%>
            <%--</a>--%>
            <%--<a class="a1" onclick="qqCustomer()">--%>
                <%--<img src="${staticHost }/images/a4.png" alt="">--%>
            <%--</a>--%>
            <%--<a class="a1" onclick="qqCustomer()">--%>
                <%--<img src="${staticHost }/images/a5.png" alt="">--%>
            <%--</a>--%>
            <%--<a class="a1" onclick="qqCustomer()">--%>
                <%--<img src="${staticHost }/images/a6.png" alt="">--%>
            <%--</a>--%>

            <%--<a class="a1 downUrlBtn" onclick="downUrl()">--%>
                <%--<img src="${staticHost }/images/a3.png" alt="">--%>
            <%--</a>--%>

            <%--<c:if test="${loginedUserLogin.groupId == 1}">--%>
                <%--<a class="a1 service">--%>
                    <%--<img src="${staticHost }/images/a8.png" alt="">--%>
                <%--</a>--%>
            <%--</c:if>--%>

            <%--&lt;%&ndash;<a class="a1" onclick="teacherJs()">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<img src="${staticHost }/images/a2.png" alt="">&ndash;%&gt;--%>
            <%--&lt;%&ndash;</a>&ndash;%&gt;--%>


        <%--</div>--%>
        <%--<div class="topRight">--%>
            <%--&lt;%&ndash;<c:if test="${loginedUserLogin.groupId == 1}">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<span class="username colorF" id="username">&ndash;%&gt;--%>
                    <%--&lt;%&ndash;<img src="${staticHost }/images/yk.png" alt="">${loginedUserLogin.userNickName}&ndash;%&gt;--%>
                <%--&lt;%&ndash;</span>&ndash;%&gt;--%>
            <%--&lt;%&ndash;</c:if>&ndash;%&gt;--%>

            <%--<c:if test="${loginedUserLogin.groupId == 2}">--%>
                <%--<span class="username colorF" id="username">--%>
                    <%--<img src="${staticHost }/images/VIP8.png" alt="">${loginedUserLogin.userNickName}--%>
                <%--</span>--%>
            <%--</c:if>--%>

            <%--<c:if test="${loginedUserLogin.groupId == 3}">--%>
                <%--<span class="username colorF" id="username">--%>
                    <%--<img src="${staticHost }/images/zl.png" alt="">${loginedUserLogin.userNickName}--%>
                <%--</span>--%>
            <%--</c:if>--%>

            <%--<c:if test="${loginedUserLogin.groupId == 4}">--%>
                <%--<span class="username colorF" id="username">--%>
                    <%--<img src="${staticHost }/images/icon_teacher.png" alt="">${loginedUserLogin.userNickName}--%>
                <%--</span>--%>
            <%--</c:if>--%>

            <%--<c:if test="${loginedUserLogin.groupId == 5}">--%>
                <%--<c:if test="${vip.userLevel == 1}">--%>
                    <%--<span class="username colorF" id="username">--%>
                      <%--<img src="${staticHost }/images/VIP1.png" alt="">${loginedUserLogin.userNickName}--%>
                    <%--</span>--%>
                <%--</c:if>--%>

                <%--<c:if test="${vip.userLevel == 2}">--%>
                    <%--<span class="username colorF" id="username">--%>
                      <%--<img src="${staticHost }/images/VIP2.png" alt="">${loginedUserLogin.userNickName}--%>
                    <%--</span>--%>
                <%--</c:if>--%>

                <%--<c:if test="${vip.userLevel == 3}">--%>
                    <%--<span class="username colorF" id="username">--%>
                      <%--<img src="${staticHost }/images/VIP3.png" alt="">${loginedUserLogin.userNickName}--%>
                    <%--</span>--%>
                <%--</c:if>--%>

                <%--<c:if test="${vip.userLevel == 4}">--%>
                    <%--<span class="username colorF" id="username">--%>
                      <%--<img src="${staticHost }/images/VIP4.png" alt="">${loginedUserLogin.userNickName}--%>
                    <%--</span>--%>
                <%--</c:if>--%>

                <%--<c:if test="${vip.userLevel == 5}">--%>
                    <%--<span class="username colorF" id="username">--%>
                      <%--<img src="${staticHost }/images/VIP5.png" alt="">${loginedUserLogin.userNickName}--%>
                    <%--</span>--%>
                <%--</c:if>--%>

                <%--<c:if test="${vip.userLevel == 6}">--%>
                    <%--<span class="username colorF" id="username">--%>
                      <%--<img src="${staticHost }/images/VIP6.png" alt="">${loginedUserLogin.userNickName}--%>
                    <%--</span>--%>
                <%--</c:if>--%>

                <%--<c:if test="${vip.userLevel == 7}">--%>
                    <%--<span class="username colorF" id="username">--%>
                      <%--<img src="${staticHost }/images/VIP7.png" alt="">${loginedUserLogin.userNickName}--%>
                    <%--</span>--%>
                <%--</c:if>--%>

                <%--<c:if test="${vip.userLevel == 8}">--%>
                    <%--<span class="username colorF" id="username">--%>
                      <%--<img src="${staticHost }/images/VIP8.png" alt="">${loginedUserLogin.userNickName}--%>
                    <%--</span>--%>
                <%--</c:if>--%>
            <%--</c:if>--%>

            <%--<c:if test="${loginedUserLogin.groupId == 1}">--%>
                <%--&lt;%&ndash;<span class="username colorF" id="username"></span>&ndash;%&gt;--%>
                <%--<a href="javascript:" class="loginA colorF fz16 bg12" id="g10" onclick="toShow('login', 'register');">登录</a>--%>
                <%--<a herf="javascript:" class="registerA colorF fz16" id="g11" onclick="toShow('register','login');">注册</a>--%>
            <%--</c:if>--%>

            <%--<c:if test="${loginedUserLogin.groupId != 1}">--%>
                <%--<a class="settingA colorF fz16">设置</a>--%>
                <%--<div class="LoginOut hide">--%>
                    <%--<a class="passBtn" href="javascript:" onclick="toShow('modifyPass','register','login');">修改密码</a>--%>
                    <%--<a class="outBtn">退出登录</a>--%>
                <%--</div>--%>
            <%--</c:if>--%>
        <%--</div>--%>
        <%--<div class="rightNav posAbs">--%>
            <%--<c:if test="${loginedUserLogin.groupId == 3 }">--%>
                <%--<a class="waterF"><span class="colorF">水滴</span>--%>
                    <%--<div class="newMsgtipDiv hide"><i class="newMsgtip"></i></div>--%>
                <%--</a>--%>
            <%--</c:if>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>
<div class="content mgAuto">
    <div class="wrap clearfix">
        <div class="contentLeft fl">
            <div class="clearfix" style="width: 100%">

                <c:if test="${loginedUserLogin.groupId == 1 || loginedUserLogin.groupId == 5}">
                    <div class="left_left">
                        <div class="visitors">

                            <div class="wrap mgAuto posRel">
                                <div class="topLeft fl">
                                    <a class="logo"><img src="${staticHost }/images/logo.png"></a>
                                        <%--<a class="a1 collectionBtn" id="Collection01" onclick="collection(document.title,window.location)">--%>
                                        <%--<img src="${staticHost }/images/a7.png" alt="">--%>
                                        <%--</a>--%>


                                    <a class="a1 downUrlBtn" onclick="downUrl()">
                                        <img src="${staticHost }/images/a3.png" alt="">
                                    </a>
                                    <a class="a1" onclick="showQQ()" href="javascript:">
                                        <img src="${staticHost }/images/a1.png" alt="">
                                    </a>
                                    <a class="a1" onclick="showQQ()" href="javascript:">
                                        <img src="${staticHost }/images/a4.png" alt="">
                                    </a>
                                    <a class="a1" onclick="showQQ()" href="javascript:">
                                        <img src="${staticHost }/images/a5.png" alt="">
                                    </a>
                                    <a class="a1" onclick="classShow()">
                                        <img src="${staticHost }/images/a9.png" alt="">
                                    </a>

                                    <c:if test="${loginedUserLogin.groupId == 1 || loginedUserLogin.groupId == 5}">
                                        <a class="a1 service">
                                            <img src="${staticHost }/images/a8.png" alt="">
                                        </a>
                                    </c:if>

                                    <a class="a1" onclick="teacherJs()">
                                        <img src="${staticHost }/images/a2.png" alt="">
                                    </a>
                                    
                                    
                                    <div class="sj_ewm">
                                        <img src="${staticHost }/images/sj.jpg" alt="">
                                    </div>


                                </div>
                                <div class="topRight">
                                        <%--<c:if test="${loginedUserLogin.groupId == 1}">--%>
                                        <%--<span class="username colorF" id="username">--%>
                                        <%--<img src="${staticHost }/images/yk.png" alt="">${loginedUserLogin.userNickName}--%>
                                        <%--</span>--%>
                                        <%--</c:if>--%>

                                    <c:if test="${loginedUserLogin.groupId == 2}">
                                        <span class="username colorF" id="username">
                                            <img src="${staticHost }/images/VIP8.png" alt="">${loginedUserLogin.userNickName}
                                        </span>
                                    </c:if>

                                    <c:if test="${loginedUserLogin.groupId == 3}">
                                        <span class="username colorF" id="username">
                                            <img src="${staticHost }/images/zl.png" alt="">${loginedUserLogin.userNickName}
                                        </span>
                                    </c:if>

                                    <c:if test="${loginedUserLogin.groupId == 4}">
                                        <span class="username colorF" id="username">
                                            <img src="${staticHost }/images/icon_teacher.png" alt="">${loginedUserLogin.userNickName}
                                        </span>
                                    </c:if>

                                    <c:if test="${loginedUserLogin.groupId == 5}">
                                        <c:if test="${vip.userLevel == 1}">
                                            <span class="username colorF" id="username">
                                              <img src="${staticHost }/images/VIP1.png" alt="">${loginedUserLogin.userNickName}
                                            </span>
                                        </c:if>

                                        <c:if test="${vip.userLevel == 2}">
                                            <span class="username colorF" id="username">
                                              <img src="${staticHost }/images/VIP2.png" alt="">${loginedUserLogin.userNickName}
                                            </span>
                                        </c:if>

                                        <c:if test="${vip.userLevel == 3}">
                                            <span class="username colorF" id="username">
                                              <img src="${staticHost }/images/VIP3.png" alt="">${loginedUserLogin.userNickName}
                                            </span>
                                        </c:if>

                                        <c:if test="${vip.userLevel == 4}">
                                            <span class="username colorF" id="username">
                                              <img src="${staticHost }/images/VIP4.png" alt="">${loginedUserLogin.userNickName}
                                            </span>
                                        </c:if>

                                        <c:if test="${vip.userLevel == 5}">
                                            <span class="username colorF" id="username">
                                              <img src="${staticHost }/images/VIP5.png" alt="">${loginedUserLogin.userNickName}
                                            </span>
                                        </c:if>

                                        <c:if test="${vip.userLevel == 6}">
                                            <span class="username colorF" id="username">
                                              <img src="${staticHost }/images/VIP6.png" alt="">${loginedUserLogin.userNickName}
                                            </span>
                                        </c:if>

                                        <c:if test="${vip.userLevel == 7}">
                                            <span class="username colorF" id="username">
                                              <img src="${staticHost }/images/VIP7.png" alt="">${loginedUserLogin.userNickName}
                                            </span>
                                        </c:if>

                                        <c:if test="${vip.userLevel == 8}">
                                            <span class="username colorF" id="username">
                                              <img src="${staticHost }/images/VIP8.png" alt="">${loginedUserLogin.userNickName}
                                            </span>
                                        </c:if>
                                    </c:if>

                                    <c:if test="${loginedUserLogin.groupId == 1}">
                                        <%--<span class="username colorF" id="username"></span>--%>
                                        <a href="javascript:" class="loginA colorF fz16 bg12" id="g10" onclick="toShow('login', 'register');">登录</a>
                                        <a herf="javascript:" class="registerA colorF fz16" id="g11" onclick="toShow('register','login');">注册</a>
                                    </c:if>

                                    <c:if test="${loginedUserLogin.groupId != 1}">
                                        <a class="settingA colorF fz16">设置</a>
                                        <div class="LoginOut hide">
                                            <a class="passBtn" href="javascript:" onclick="toShow('modifyPass','register','login');">修改密码</a>
                                            <a class="outBtn">退出登录</a>
                                        </div>
                                    </c:if>
                                </div>

                            </div>

                        </div>
                    </div>
                </c:if>

                <c:if test="${loginedUserLogin.groupId == 3 || loginedUserLogin.groupId == 4}">

                    <div class="rightNav posAbs">
                        <c:if test="${loginedUserLogin.groupId == 3 }">
                            <a class="waterF"><span class="colorF">水滴</span>
                                <div class="newMsgtipDiv hide"><i class="newMsgtip"></i></div>
                            </a>
                            <a class="settingA colorF fz16"><span>设置</span></a>
                            <div class="LoginOut LoginOut2 hide">
                                <a class="passBtn" href="javascript:" onclick="toShow('modifyPass','register','login');">修改密码</a>
                                <a class="outBtn" href="javascript:">退出登录</a>
                            </div>

                        </c:if>
                    </div>

                    <div class="left_left">
                        <div class="visitors">
                            <div class="visitorsBtn clearfix">
                                <a class="cur">VIP</a>
                                <a>游客</a>
                            </div>
                            <div class="visitorsCon">
                                <div class="visitors_list">
                                    <ul class="vipOnlineList"></ul>
                                </div>
                                <div class="visitors_list hide">
                                    <ul class="ykOnlineList"></ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <div class="left_right"></div>
                <div class="left_middle <c:if test="${loginedUserLogin.groupId == 1 || loginedUserLogin.groupId == 5}"> pad0 </c:if>">
                    <div class="movie">
                        <div class="movieTop clearfix">
                            <div class="viewTime fl">
                                <span class="refresh movieTopIcon"></span>
                                <span class="remainderTime" id="remainderTime"></span>
                            </div>
                            <div class="share fr">
                                <span class="eye movieTopIcon"></span>
                                <span class="onlineNum fz12 pr5">${room.baseNum}</span>
                            </div>
                        </div>
                        <div class="toLogin posRel">


                            <%--<c:choose>--%>
                                <%--<c:when test="${empty informState }">--%>
                                    <%--<a href="javascript:" class="freeTipBox hide" onclick="showQQ()">--%>
                                        <%--<div class="loginOrReg"></div>--%>
                                    <%--</a>--%>
                                    <%--<div class="videoBox" id="play-container" style="width:100%; height:100%">--%>
                                        <%--<div id="dyyplayer" style="width:100%;height:100%"></div>--%>
                                    <%--</div>--%>
                                <%--</c:when>--%>
                                <%--<c:otherwise>--%>
                                    <%--<div class="notice2">--%>
                                        <%--<h4>${informState.informTitle }</h4>--%>
                                        <%--<div class="notice2Txt">--%>
                                            <%--<span>尊敬的客户：</span>--%>
                                            <%--<span>${informState.informContent }</span>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</c:otherwise>--%>
                            <%--</c:choose>--%>


                            <a href="javascript:" class="freeTipBox hide" onclick="showQQ()">
                                <div class="loginOrReg"></div>
                            </a>
                            <div class="videoBox" id="play-container" style="width:100%; height:100%">
                                <div id="dyyplayer" style="width:100%;height:100%"></div>
                            </div>


                        </div>
                    </div>
                    <div class="movieBot mt10">
                        <div id="relativediv">
                            <div class="bannerImg">
                                <a href="javascript:" onclick="showQQ()">
                                    <img src="${staticHost }/images/b1.jpg" alt="">
                                </a>
                            </div>
                        </div>
                        <div class="otherMenu hide">
                            <ul>
                                <li class="ml7">课程安排</li>
                            </ul>
                            <span class="tip fz12 fr">投资有风险，入市须谨慎</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <div class="contentRight fr posRel">
            <div class="allUserView ">
                <div class="notice posRel hide">
                    <p class="trumpet fl"><img src="${staticHost }/images/notice.png"/></p>
                    <p class="msgTime fl"></p>
                    <p class="trig fr" style="display:none;"><img src="${staticHost }/images/trig.png"/></p>
                    <marquee onmouseover="this.stop()" class="noticeMsg noticeList" onmouseout="this.start()" scrollamount="5" direction="left"></marquee>
                </div>
                <div class="moreNotice hide">
                    <select class="selectBox ml20"></select>
                </div>
                <div class="teachView">
                    <div class="discOrTra m10 hide">
                        <a class="fl">讨论</a>
                        <a class="current fr">交易</a>
                    </div>
                    <div class="chat showBigMsg">
                        <div class="scrollWrap posRel">
                            <c:if test="${loginedUserLogin.groupId != 1 && loginedUserLogin.groupId != 5}">
                                <a onclick="movieHide()" class="movieHidebtn"></a>
                            </c:if>
                            <p class="tishi">
                                <i class="noticeIcon"></i>
                                <marquee scrollamount="5">
                                    直播为嘉宾的个人观点，仅供参考，请谨慎交易！
                                </marquee>
                            </p>
                            <div class="msg" id="msgBox">
                                <div class="load">
                                    <img src="${staticHost }/images/load.gif"/>
                                </div>
                            </div>
                        </div>
                        <div class="serviceList clearfix posRel">
                            <div class="qqWra">
                                <c:forEach var="as" begin="0" items="${assistant}">
                                    <a class="assistantQQ" onclick="showQQ(${as.userQq})" href="javascript:">
                                        <img class="headImg" src="${imagePath}ori/${as.userPhoto}" alt="">
                                        <span>${as.userNickName}</span>
                                        <%--<div class="bigImg hide">--%>
                                            <%--<i></i>--%>
                                            <%--<img src="${imagePath}ori/${as.userPhoto}" alt="">--%>
                                        <%--</div>--%>
                                    </a>
                                </c:forEach>
                            </div>
                            <%--<a class="more" href="javascript:"></a>--%>
                        </div>

                        <div class="messageBox">
                            <div class="inputTxt clearfix">
                                <c:if test="${loginedUserLogin.groupId == 1 || loginedUserLogin.groupId == 5}">
                                    <div class="contactQQ">
                                        <a id="goo26" href="javascript:"  onclick="showQQ()">智能跟单</a>
                                        <a id="goo27" href="javascript:" onclick="showQQ()">账户诊断</a>
                                            <%--<a id="goo28" href="javascript:" onclick="showQQ(${relation.userQq})">领取策略</a>--%>
                                    </div>
                                </c:if>

                                <!-- 选择小号 -->
                                <c:if test="${manage.groupId == 3 || manage.groupId == 4}">
                                    <div class="assistantList" style="float: right">
                                        <select id="small">
                                            <option value="0">${loginedUserLogin.userNickName}</option>
                                            <c:forEach var="small" items="${small}">
                                                <option value="${small.smallLevel}">${small.smallName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </c:if>

                                <div class="emoticonIcon">
                                    <div class="expressionT posAbs hide"
                                         style="top:-370px;left:0px; background-color: white; z-index: 99; box-shadow:0 1px 1px #ccc;">
                                        <div class="video-discuss-emotion" id="video-discuss-emotion">
                                            <div class="video-emotion-pane">
                                                <ul id="emotionUL">
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <form id="sendChatImgForm" action="${pageContext.request.contextPath}/room/sendImage" method="post">
                                        <a class="faceF"></a>
                                        <a class="redBagF"><i id="redPackIntF" style="display: none"></i></a>
                                        <a class="roseF"><i id="roseIntF" style="display: none"></i></a>
                                        <a class="file sendImgA">
                                            <input type="file" id="msgImage" name="chatImage" accept="image/*"/>
                                        </a>
                                        <a class="clearScreen"><i style="display: none"></i></a>
										 <c:if test="${manage.groupId == 3 || manage.groupId == 4}">
                                            <label>
                                                <input type="checkbox" id="smallGroupType">
                                                <span>小号群发</span>
                                            </label>
                                        </c:if>
                                    </form>
                                </div>

                            </div>
                            <div class="sendMsg posRel">
                                <textarea class="message" placeholder="观望一天不如咨询一遍，即刻开启您的财富之旅"
                                          id="sendMsgIpt"></textarea>
                                <button class="allbutton allbutton3" id="sendMsgBtn" onclick="onSendMsg()">发送</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- <div class="footer">
    <div class="wrap ac mgAuto">
        <span>直播为嘉宾的个人观点，仅供参考，请谨慎交易！ 版权：深圳市美易互动科技有限公司 投资有风险  入市需谨慎</span>
    </div>
</div> -->


<div class="pop hide" id="pop">
    <div class="login clearfix">
        <div class="rightInput">
            <form id="loginForm" method="post" action="/user/login">
                <h2 class="tit fz20 ac">VIP登录</h2>
                <input type="text" class="inp" placeholder="请输入帐号" name="userTel" id="loginPhone"/>
                <input type="password" class="inp" placeholder="请输入密码" name="userPass" id="loginPwd"/>
                <a href="javascript:" onclick="showQQ()">忘记密码</a>
                <input type="submit" class="redBut mb10 mt20" id="login" value="登录"/>
                <p class="errorMsg red3 fz20 hide"></p>
            </form>
        </div>
        <a class="close"></a>
    </div>
    <div class="register popWrap mgAuto posRel ac">
        <form id="registerForm" method="post" action="/user/userRegistered">
            <img src="${staticHost }/images/register.jpg" alt="">
            <a class="btn01" href="javascript:"  onclick="showQQ()"></a>
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

<div class="waterDrop hide waterPersionBox ykpop">
    <div class="w_logo">
        <img src="${staticHost }/images/logo2.png" alt="">
        <a href="javascript:" class="close1"></a>
    </div>
    <div class="chat clearfix">
        <div class="left_wra">
            <div class="left_chat">
                <h4 class="withManageToChatTip">正在与<span class="inintPage-relation-name">${relation.userNickName}</span>对话<i></i></h4>
                <div class="list_chat clearfix" id="waterPersionChatBox">
                    <div class="messageTip hide">
                        <div class="text01 clearfix">
                            <div class="user_img">
                                <span class="inintPage-relation-name">${relation.userNickName}</span>
                            </div>
                            <div class="txt">
                                <p>您好，欢迎您来到《众盈俱乐部》国资背景期货公司合作方，希望我们的直播能给您带来便利！</p><br/>
                                <p>【0元开户】请回复：1</p>
                                <p>【领取课件】请回复：2</p>
                                <p>【操作策略】请回复：3</p>
                                <p>【仓单诊断】请回复：5</p>
                                <p>【名师一对一】请回复：6</p>
                                <p>【注册会员】请回复：7</p>
                                <p>【其他】请回复：888</p>
                                <p>如有其它问题或不便打字，可留下电话/QQ/微信或扫一扫【二维码】添加小秘书微信哦！</p>
                            </div>
                        </div>
                    </div>
                    <div class="waterPersionChatMessageList" id="waterMsg"></div>
                </div>
                <div class="Input">
                    <form id="persionC2CMessageForm" action="/live/saveC2CMessage">
                        <span>
                            <input type="hidden" name="roomId" value="${room.roomId}"/>
                            <input type="hidden" name="groupId" value="${loginedUserLogin.groupId}"/>
                            <input type="hidden" name="fromId" value="${loginedUserLogin.userId}"/>
                            <input type="hidden" name="fromNickName" value="${loginedUserLogin.userNickName}"/>
                            <input type="hidden" name="toId" id="persionToId" value="${relation.userId}"/>
                            <input type="hidden" name="toNickName" value="${relation.userNickName}"/>
                            <input type="hidden" name="persionToGroupId" id="persionToGroupId" value="${relation.groupId}"/>
                            <input type="hidden" name="level" value="${vip.userLevel}"/>
                            <input type="text" name="content" id="waterPersionContent" placeholder="请输入内容">
                            <div class="btns">
                                <button type="submit" class="a3">发送</button>
                            </div>
                        </span>
                    </form>
                </div>
            </div>
        </div>

        <div class="right_chat">
            <div class="user_data">
                <div class="user_title">
                    <img class="inintPage-relation-userPhoto" src="${imagePath}ori/${relation.userPhoto}" alt="">
                    <div class="txt">
                        <span class="inintPage-relation-name">${relation.userNickName}</span>
                        <%--<span>手机：${relation.userTel}</span>--%>
                        <span>QQ：</span><span class="inintPage-relation-qq">${relation.userQq}</span>
                    </div>
                </div>
                <p class="user_intro"><span class="inintPage-relation-userIntroduction">${relation.userIntroduction}</span></p>
                <div class="img01">
                    <img class="inintPage-relation-userQrcode" src="${imagePath}ori/${relation.userQrcode}" alt="">
                    <a href="javascript:"></a>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="waterDrop waterListF hide zlpop">
    <div class="w_logo">
        <img src="${staticHost }/images/logo2.png" alt="">
        <span class="seach">
        <input type="text" placeholder="输入查询">
        <a href="javascript:"></a>
      </span>
        <a href="javascript:" class="close1"></a>
    </div>

    <div class="tab clearfix">
        <div class="tabBtns">
            <a class="btn btn1 cur" href="javascript:">
                <i></i>
                <span>用户</span>
            </a>

            <a style="cursor: pointer" class="refreshContact">
                <span class="refreshF"></span>
                <span>刷新</span>
            </a>
        </div>
        <div class="tabCon">
            <div class="tab_title clearfix">
                <div class="tab_left">
                    <div class="list">
                        <div class="select1 clearfix">
                            <a class="allMassBtn" style="cursor: pointer;">发送所有人</a>
                            <a class="addGroupBtn" style="cursor: pointer;">添加分组</a>
                        </div>
                        <div class="table1">
                            <table>
                                <thead>
                                <th width="44%">用户</th>
                                <th width="15%">状态</th>
                                <th width="30%">时间</th>
                                <th width="11%">设置</th>
                                </thead>
                            </table>
                            <div class="table2">
                                <ul class="waterGroup">
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="tab_right waterChat hide">
                    <div class="left_chat">
                        <h4 class="toName"></h4>
                        <div class="list_chat clearfix waterChatMessageList" id="waterAdminChatBox">
                            <div class="text01 clearfix">
                                <div class="user_img">
                                    <img src="${staticHost }/images/img_08.png" alt="">
                                    <span>晨曦</span>
                                </div>
                                <div class="txt">
                                    <p></p>
                                    <strong>21:07</strong>
                                </div>
                            </div>
                            <div class="text02 clearfix">
                                <div class="txt">
                                    <strong>21:07</strong>
                                    <p></p>
                                </div>
                                <div class="user_img">
                                    <img src="${staticHost }/images/img_09.png" alt="">
                                    <span>游客123</span>
                                </div>
                            </div>
                        </div>
                        <div class="Input">
                            <form id="adminC2CMessageForm" action="/live/saveC2CMessage">
                                <span>
                                    <input type="hidden" name="roomId" value="${room.roomId}"/>
                                    <input type="hidden" name="groupId" value="${loginedUserLogin.groupId}"/>
                                    <input type="hidden" name="fromId" value="${loginedUserLogin.userId}"/>
                                    <input type="hidden" name="fromNickName" value="${loginedUserLogin.userNickName}"/>
                                    <input type="hidden" id="toGroupId"/>
                                    <input type="hidden" name="toId" id="waterToId"/>
                                    <input type="hidden" name="toNickName" id="waterToName"/>
                                    <input type="text" name="content" id="waterContent" placeholder="请输入内容">
                                    <div class="btns">
                                        <button type="submit" class="a3">发送</button>
                                    </div>
                                </span>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%--群发消息--%>
    <div class="MassMask hide">
        <div class="left_wra MassBg">
            <div class="left_chat">
                <h5>发送给所有用户 <i></i></h5>
                <div class="list_chat_1 clearfix"></div>
                <div class="Input">
                    <span style="height: 80px;">
                        <textarea id="groupMsgContent" style="width: 100%; height: 100%; border: none;"></textarea>
                        <div class="btns">
                           <a class="a3 sendGroupMsg" style="cursor: pointer; margin-top: 30px;">发送</a>
                        </div>
                    </span>
                </div>
            </div>
            <a class="massClose" href="javascript:"></a>
        </div>

    </div>

    <%--添加分组--%>
    <div class="addGroupMask hide">
        <div class="addGroup">
            <h4>添加分组</h4>
            <span>
                <input type="text" id="waterGroupName" placeholder="输入分组名称"/>
                <input type="hidden" id="waterGroupId"/>
                <a class="addWaterGroup">确定</a>
            </span>
            <a class="GroupClose" href="javascript:"></a>
        </div>
    </div>

</div>
<div class="worning hide"></div>

<%--<div class="popMask">--%>
    <%--<div class="popImg">--%>
        <%--<a href="javascript:" id="g15" onclick="showQQ(${relation.userQq})">--%>
            <%--<img src="${staticHost }/images/img.png" alt="">--%>
        <%--</a>--%>
        <%--<a class="closeImg" style="" href="javascript:"></a>--%>
    <%--</div>--%>
<%--</div>--%>

<%--听课时长--%>
<div class="tkTimePopMask hide">
    <div class="tkTime">
        <%--<h3>您已听课3分钟</h3>--%>
        <a class="lxQQ" id="g6" href="javascript:" onclick="showQQ()"></a>
        <a class="tkClose" href="javascript:"></a>
    </div>
</div>

<%--图片放大--%>
<div class="imgMask hide">
    <div class="bigImgWra">
        <div class="bigImg">
            <img src="${staticHost }/images/timg2.jpg" alt="">
        </div>
        <a class="bigImgClose" href="javascript:" onclick="bigClose()"></a>
    </div>
</div>

<div class="teacherJsMask hide">


    <div class="teacherJs">
        <div class="lsBtn">
            <a class="cur" href="javascript:">佳人<br/>老师</a>
            <a href="javascript:">一凡<br/>老师</a>
            <a href="javascript:">大明<br/>老师</a>
            <a href="javascript:">无休<br/>老师</a>
        </div>
        <div class="lsText">
            <div class="lsTxt">
                <p>国家认证黄金高级分析师，十年投资经验，擅长于各技术指导的综合应用(布林带，MACD, MA均线，K线形态分析，  趋势分析，波浪理论，江恩理论等等)  有过硬的技术功底及长期期货，股票，贵金属操盘经验总结，并对日内行情波段操作颇有研究;  自创高胜算交易法则，以短线为主，中长线为辅的操作风格，时常能将危机化解于实战中，以此受到广大投资朋友的一致好评。</p>
            </div>
            <div class="lsTxt hide">
                <p>高级分析师，拥有10年的国际投资机构从业经历，期货黄金分析师职业证书，投资建议经常发表于各大财经网站，深受投资者信赖，实战中以趋势投资为线，辅以时机的选择，着眼于事件本身的特质，形成一套独特的股票操作体系，擅长中短线结合与趋势。</p>
            </div>
            <div class="lsTxt hide">
                <p>高级交易师。长达14年金融从业经历，涉及证券、外汇、期货领域。擅用指标及均线系统提高交易成功率。英国脱欧狙击英镑当日收益300%，一战成名，遵从趋势力量，注重仓位管理技巧。</p>
            </div>
            <div class="lsTxt hide">
                <p>毕业于同济大学金融系，拥有黄金分析师一级证书，期货从业资格证。曾任多家知名投行，运用波浪理论以及多空动能转换实现高达一个月连赢的股指记录。创下收益高达750%，被业内封为“股指狙击手”。</p>
            </div>
        </div>

        <a class="teacherJsClose" href="javascript:"></a>

    </div>



    <%--<div class="teacherJs">--%>
        <%--<img src="${staticHost }/images/teacherJs.png" alt="">--%>
        <%--<a class="teacherJsClose" href="javascript:"></a>--%>
    <%--</div>--%>
</div>

<!-- 课程表 -->
<input type="hidden" id="atHeInput" value=""/>
<div class="techer_class" id="courseplanArea">
    <div class="class_inside">
        <img src="${staticHost }/images/close4.png" class="close11 pop-close" onclick="classShow('show');">
        <c:if test="${state == 3}">
            <div class="kcap kcap2">
                <table>
                    <tr>
                        <th>直播时间</th>
                        <th>特邀嘉宾</th>
                        <th>课程介绍</th>
                    </tr>
                <c:forEach items="${plan}" var="pln">
                    <tr>
                    	<td>${pln.planTime}</td>
                        <td>${pln.planTeacher}</td>
                        <td>${pln.planIntroduce}</td>
                    </tr>
                </c:forEach>
                </table>
            </div>
        </c:if>
        <c:if test="${state == 2}">
            <div class="kcap">
                <table>
                    <tr>
                        <th>直播时间（周一至周五）</th>
                        <th>特邀嘉宾</th>
                    </tr>
                <c:forEach items="${plan}" var="pln">
                    <tr>
                        <td>${pln.planTime}</td>
                        <td>${pln.planTeacher}</td>
                    </tr>
                </c:forEach>
                </table>
            </div>
         </c:if>
    </div>
</div>


<%--<c:if test="${loginedUserLogin.groupId == 1}">--%>
    <%--<a class="kefu" onclick="topQQ()" href="javascript:">--%>
        <%--<img src="${staticHost }/images/qqIcon.gif" alt="">--%>
    <%--</a>--%>
<%--</c:if>--%>



<iframe id="ifqq1" style="display:none;" src=""></iframe>

</body>
<%--<script async defer src="//vip2.xunbaoqu.com/client?swt&id=1520:2895"></script>--%>
<script src="${staticHost}/js/lib/slide.js?version=${version}"></script>
<script src="${staticHost}/js/common/common.js?version=${version}"></script>
<script src="${staticHost}/js/common/index.js?version=${version}2"></script>
<script src="${staticHost}/js/common/jquery.endless-scroll-1.3.js"></script>
<script src="${staticHost}/js/comment/comment.js?version=${version}"></script>
<script src="${staticHost}/js/consult/consult.js?version=${version}"></script>
<script src="${staticHost}/js/common/interval.js?version=${version}"></script>
<script src="${staticHost}/js/common/awardRotate.js?version=${version}"></script>
<script src="${staticHost}/js/lib/tls/webim.js?version=${version}"></script>
<script src="${staticHost}/js/chat/chat_group_notice.js?version=${version}"></script>
<script src="${staticHost}/js/chat/c2c_chat.js?version=${version}"></script>
<script src="${staticHost}/js/chat/chat_base.js?version=${version}1"></script>
<script src="${staticHost}/js/chat/chat.js?version=${version}"></script>
<!-- baidu -->
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?41f72c1b01966d5f7bc6d456c72d66be";
  var s = document.getElementsByTagName("script")[0];
  s.parentNode.insertBefore(hm, s);
})();
</script>

<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-131577641-1"></script>
<script>
    window.dataLayer = window.dataLayer || [];
    function gtag(){dataLayer.push(arguments);}
    gtag('js', new Date());

    gtag('config', 'UA-131577641-1');
</script>


<script type="text/javascript" src="https://cdn.58jinrongyun.com/helper/room_player_s.js?r=24138&id=dyyplayer"></script>
<script type="text/javascript">
    // 弹出QQ弹框
//    var defaultQQ = new Array('3005619188', '3005658628', '3005698870','3005623869');
    var defaultQQ = new Array(relation.userQq);
    function showQQ(qqParam) {
        var sUserAgent = navigator.userAgent;
        var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");
        var isMac = (navigator.platform == "Mac68K") || (navigator.platform == "MacPPC") || (navigator.platform == "Macintosh") || (navigator.platform == "MacIntel");
        var isUnix = (navigator.platform == "X11") && !isWin && !isMac;
        var userId = localStorage.getItem('i_user_id');
        //var qqId = localStorage.getItem('qqId');
        //var index = Math.floor(Math.random() * defaultQQ.length);
        //var qq = defaultQQ[index];
        var qq = defaultQQ;
        if (qqParam) {
            qq = qqParam
        } else {
            //localStorage.setItem('i_user_id', userInfo.id);
            //localStorage.setItem('qqId', qq);
            qq=relation.userQq;
        }
        if (isWin) {
            window.open('tencent://message/?uin=' + qq, '_self', 'height=1, width=1,toolbar=no,scrollbars=no,menubar=no,status=no');
        } else if (isMac || isUnix) {
            window.open('tencent://message/?uin=' + qq + '&Service=200&sigT=bc799de13f6b616996ffdda3884f3daf66572104e865057627b2bcdda186f3d2', '_self', 'height=1, width=1,toolbar=no,scrollbars=no,menubar=no,status=no');
        } else {
            window.open('tencent://message/?uin=' + qq, '_self', 'height=1, width=1,toolbar=no,scrollbars=no,menubar=no,status=no');
        }
    }
    function topQQ() {
        showQQ();
    }

    <c:if test="${loginedUserLogin.groupId == 1}">
        setTimeout(function () {
            topQQ();
        }, 5000)
    </c:if>

   /* var playerVar = new dyyPlayer({
        //room_id: '${room.roomStreamServer}', //对应房间ID，必要参数
		room_id: '23590',
        container: 'play-container', //播放器容器ID，必要参数
        width: '100%', //播放器宽度，可用数字、百分比等
        height: '100%', //播放器高度，可用数字、百分比等
        autostart: true, //是否自动播放，默认为false
        controlbardisplay: 'enable' //是否显示控制栏，值为：disable、enable默认为disable
    }); */ 
</script>
</html>

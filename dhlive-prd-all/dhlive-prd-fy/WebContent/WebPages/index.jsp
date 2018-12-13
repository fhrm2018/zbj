<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>

    <title>牛金商学院-期货入门知识_农产品期货开户_期货课堂</title>
    <meta name="keywords" content="牛金商学院,牛金商学院直播间,牛金商学院研究所,期货入门,期货知识,农产品期货开户"/>
    <meta name="description" content="牛金商学院提供专家内盘期货入门指导，专业分析师在线进行指导教学,牛金商学院研究所提供炒期货投资入门、农产品期货行情,金融期货投资、期货经济数据、期货模拟交易等,牛金商学院是您身边值得信赖的期货直播平台!"/>
    <link rel="shortcut icon" href="${staticHost}/images/favicon.png" type="image/x-icon"/>
    <jsp:include page="common/public.jsp"/>
    <script type="text/javascript">
        //全局变量
        var roomId = '${room.roomId}',//聊天室ID
            chatRoomId = '${room.roomGroupId}',//聊天室ID
            isAdmin = 0,
            time = 1,
            activityConfigId = '${config.id}',
            activityCountdown = '${config.activityCountdown}',
            chatImgs = new Array(),
            talkUserId = '',
            imgPath = '${imagePath}',
            type_flag = 0,
            isCanSend = true,
            groupId =    ${loginedUserLogin.groupId},
            tempWatchTime = ${room.tempWatchTime},
            url = '${url}',
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
				'userTel':'${relation.userTel}',
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
                    relation.userTel = relationRes.userTel;
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
        		$('.inintPage-relation-name').html(relation.userNickName);
        		$('.inintPage-relation-qq').html('QQ:'+relation.userQq);
        		$('.inintPage-relation-userIntroduction').html(relation.userIntroduction);
        		$('.inintPage-relation-tel').html('手机:'+relation.userTel);
        		$('.inintPage-relation-userQrcode').attr('src',imgPath + "ori/"+relation.userQrcode);
        		$('.inintPage-relation-userPhoto').attr('src',imgPath + "ori/"+relation.userPhoto);
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
	<script>
	(function() {
		var _53code = document.createElement("script");
		_53code.src = "https://tb.53kf.com/code/code/10168866/2";
		var s = document.getElementsByTagName("script")[0]; 
		s.parentNode.insertBefore(_53code, s);
		})();
	</script>
</head>

<body class="changeBg1">



<!-- 辅助变量 -->
<input type="hidden" id="atHeInput" value=""/>
<div class="techer_class" id="courseplanArea">
    <div class="class_inside">
        <img src="${staticHost}/images/dh/close.png" class="close11 pop-close" onclick="classShow('show');">
        <c:if test="${state == 3}">
            <div class="kcap kcap2">
                <table>
                    <tr>
                        <th>时间</th>
                        <th>老师</th>
                        <th>课程</th>
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
                        <th>时间</th>
                        <th>老师</th>
                    </tr>
                    <c:forEach items="${plan}" var="pln">
                        <tr>
                            <td>${pln.planTeacher}</td>
                            <td>${pln.planTime}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </c:if>
    </div>
</div>
<div class="header">
    <div class="wrap mgAuto posRel">
        <div class="topLeft fl">
            <a class="logo"><img src="${staticHost}/images/logo.png"></a>

            <a class="a1" onclick="qqCustomer(${relation.userQq})">
                <img src="${staticHost}/images/a1.png" alt="">
            </a>
            <a class="a1" onclick="qqCustomer(${relation.userQq})">
                <img src="${staticHost}/images/a2.png" alt="">
            </a>
            <a class="a1" onclick="qqCustomer(${relation.userQq})">
                <img src="${staticHost}/images/a3.png" alt="">
            </a>
            <a class="a1" target="_blank" href="http://www.oeo2.com/">
                <img src="${staticHost}/images/a5.png" alt="">
            </a>

            <a class="a1" onclick="qqCustomer(${relation.userQq})" style="margin-top:11px;">
                <img src="${staticHost}/images/zxzx.gif" alt="">
            </a>


        </div>
        <div class="topRight fr">

            <%--<a class="shareBtn colorF fz16" href="javascript:">--%>
            <%--<i><img src="../static/images/shareIcon.png" alt=""></i>--%>
            <%--<span>分享</span>--%>
            <%--</a>--%>
            <a class="downUrl colorF fz16" href="javascript:" onclick="downUrl()">
                <span>保存到桌面</span>
            </a>

            <%--<c:if test="${loginedUserLogin.groupId == 1}">--%>
            <%--<span class="username colorF" id="username">--%>
            <%--<img src="../static/images/yk.png" alt="">${loginedUserLogin.userNickName}--%>
            <%--</span>--%>
            <%--</c:if>--%>

            <c:if test="${loginedUserLogin.groupId == 2}">
                <span class="username colorF" id="username">
                    <img src="${staticHost}/images/VIP8.png" alt="">${loginedUserLogin.userNickName}
                </span>
            </c:if>

            <c:if test="${loginedUserLogin.groupId == 3}">
                <span class="username colorF" id="username">
                    <img src="${staticHost}/images/zl.png" alt="">${loginedUserLogin.userNickName}
                </span>
            </c:if>

            <c:if test="${loginedUserLogin.groupId == 4}">
                <span class="username colorF" id="username">
                    <img src="${staticHost}/images/icon_teacher.png" alt="">${loginedUserLogin.userNickName}
                </span>
            </c:if>

            <c:if test="${loginedUserLogin.groupId == 5}">
                <c:if test="${vip.userLevel == 1}">
                    <span class="username colorF" id="username">
                      <img src="${staticHost}/images/VIP1.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 2}">
                    <span class="username colorF" id="username">
                      <img src="${staticHost}/images/VIP2.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 3}">
                    <span class="username colorF" id="username">
                      <img src="${staticHost}/images/VIP3.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 4}">
                    <span class="username colorF" id="username">
                      <img src="${staticHost}/images/VIP4.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 5}">
                    <span class="username colorF" id="username">
                      <img src="${staticHost}/images/VIP5.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 6}">
                    <span class="username colorF" id="username">
                      <img src="${staticHost}/images/VIP6.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 7}">
                    <span class="username colorF" id="username">
                      <img src="${staticHost}/images/VIP7.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 8}">
                    <span class="username colorF" id="username">
                      <img src="${staticHost}/images/VIP8.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>
            </c:if>

            <c:if test="${loginedUserLogin.groupId == 1}">
                <a class="message posRel hide">
                    <img src="${staticHost}/images/message.png"/>
                    <i class="redDot posAbs"></i>
                </a>
                <a class="loginA colorF fz16" id="g10" onclick="toShow('login', 'register');">登录</a>
                <a class="registerA colorF fz16" id="g11" onclick="toShow('register','login');">注册</a>
            </c:if>

            <c:if test="${loginedUserLogin.groupId != 1}">

                <%--<c:if test="${loginedUserLogin.groupId == 3}">--%>
                    <a class="settingA colorF fz16">设置</a>
                    <div class="LoginOut hide">
                        <a class="passBtn" href="javascript:" onclick="toShow('modifyPass','register');">修改密码</a>
                        <a class="outBtn">退出登录</a>
                    </div>
                <%--</c:if>--%>


            </c:if>
        </div>
        <div class="rightNav posAbs">
            <c:if test="${loginedUserLogin.groupId != 1 && loginedUserLogin.groupId != 5}">
                <a class="waterF"><span class="colorF">水滴</span>
                    <div class="newMsgtipDiv hide"><i class="newMsgtip"></i></div>
                </a>
            </c:if>
        </div>
    </div>
</div>
<div class="content mgAuto">
    <div class="wrap clearfix">
        <div class="contentLeft fl">
            <div class="clearfix" style="width: 100%">
                <div class="left_left">
                    <c:if test="${loginedUserLogin.groupId == 1 || loginedUserLogin.groupId == 5}">
                        <div class="leftBtn">
                            <%--<a href="javascript:" onclick="qqCustomer(${relation.userQq})">--%>
                                <%--<i><img src="../static/images/icon1.png" alt=""></i>--%>
                                <%--<span>盈利榜</span>--%>
                            <%--</a>--%>
                            <a href="javascript:" onclick="qqCustomer(${relation.userQq})">
                                <i><img src="${staticHost}/images/icon2.png" alt=""></i>
                                <span>实时策略</span>
                            </a>
                            <a href="javascript:" onclick="qqCustomer(${relation.userQq})">
                                <i><img src="${staticHost}/images/icon3.png" alt=""></i>
                                <span>早晚评</span>
                            </a>
                            <a href="javascript:" onclick="classShow()">
                                <i><img src="${staticHost}/images/icon4.png" alt=""></i>
                                <span>课程表</span>
                            </a>
                            <%--<a href="javascript:">--%>
                                <%--<i><img src="../static/images/icon5.png" alt=""></i>--%>
                                <%--<span>财经资讯</span>--%>
                            <%--</a>--%>
                            <a class="service" href="javascript:">
                                <i><img src="${staticHost}/images/icon6.png" alt=""></i>
                                <span>专属客服</span>
                            </a>
                            <div class="ewm">
                                <img src="${staticHost}/images/ewm.png" alt=""></i>
                                <span>扫一扫</span>
                                <span>看直播</span>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${loginedUserLogin.groupId == 3 || loginedUserLogin.groupId == 4}">
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
                    </c:if>
                </div>

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
                            <a href="javascript:" class="registerBtn" onclick="qqCustomer(${relation.userQq})">
                                <div class="freeTipBox hide"></div>
                            </a>
                            <div class="videoBox" id="play-container" style="width:100%; height:100%">
                                <div id="dyyplayer" style="width:100%;height:100%"></div>
                            </div>
                        </div>
                    </div>
                    <div class="movieBot mt10">
                        <div id="relativediv" class="clearfix">
                            <div class="bannerImg">
                                <div class="activityBtn clearfix">
                                    <a class="btn1 cur" href="javascript:">精彩活动</a>
                                    <a class="btn1" href="javascript:">老师介绍</a>
                                </div>
                                <div class="activityImg">
                                    <div class="img1">
                                        <a href="javascript:" onclick="qqCustomer(${relation.userQq})">
                                            <img class='b1' src="${staticHost}/images/b1.jpg" alt="">`
                                        </a>
                                    </div>
                                    <div class="img1 clearfix hide">
                                        <ul class="hoverImg clearfix">
                                            <li>
                                              <a href="javascript:">
                                                  <img src="${staticHost}/images/sl.png" alt="">
                                              </a>
                                              <div class="teacherImg hide">
                                                  <img src="${staticHost}/images/sld.png" alt="">
                                              </div>
                                            </li>
                                            <%--<li>--%>
                                                <%--<a href="javascript:">--%>
                                                    <%--<img src="../static/images/hy.png" alt="">--%>
                                                <%--</a>--%>
                                                <%--<div class="teacherImg hide">--%>
                                                    <%--<img src="../static/images/hyd.png" alt="">--%>
                                                <%--</div>--%>
                                            <%--</li>--%>
                                            <li>
                                                <a href="javascript:">
                                                    <img src="${staticHost}/images/jj.png" alt="">
                                                </a>
                                                <div class="teacherImg hide">
                                                    <img src="${staticHost}/images/jjd.png" alt="">
                                                </div>
                                            </li>
                                            <li>
                                                <a href="javascript:">
                                                    <img src="${staticHost}/images/qk.png" alt="">
                                                </a>
                                                <div class="teacherImg hide">
                                                    <img src="${staticHost}/images/qkd.png" alt="">
                                                </div>
                                            </li>
                                            <li>
                                                <a href="javascript:">
                                                    <img src="${staticHost}/images/sg.png" alt="">
                                                </a>
                                                <div class="teacherImg hide">
                                                    <img src="${staticHost}/images/sgd.png" alt="">
                                                </div>
                                            </li>
                                        </ul>

                                        <%--<div class="lsBtn">--%>
                                            <%--<a class="cur" href="javascript:">哈哈老师</a>--%>
                                            <%--<a href="javascript:">哼哼老师</a>--%>
                                            <%--<a href="javascript:">哈哈老师</a>--%>
                                            <%--<a href="javascript:">哼哼老师</a>--%>
                                        <%--</div>--%>
                                        <%--<div class="lsCon">--%>
                                            <%--<div class="lsImg">--%>
                                                <%--<img src="../static/images/asd.jpg" alt="">--%>
                                            <%--</div>--%>
                                            <%--<div class="lsImg hide">--%>
                                                <%--<img src="../static/images/asd.jpg" alt="">--%>
                                            <%--</div>--%>
                                            <%--<div class="lsImg hide">--%>
                                                <%--<img src="../static/images/asd.jpg" alt="">--%>
                                            <%--</div>--%>
                                            <%--<div class="lsImg hide">--%>
                                                <%--<img src="../static/images/asd.jpg" alt="">--%>
                                            <%--</div>--%>
                                        <%--</div>--%>
                                    </div>
                                </div>
                            </div>

                            <div class="jinShi">
                                <h3>实时财经资讯</h3>
                                <div class="jinShiNews">
                                    <iframe frameborder="0" width="100%" height="2100" scrolling="no" src="https://www.jin10.com/example/jin10.com.html?messageNum=50&fontSize=14px&theme=black"></iframe>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>


        <div class="contentRight fr posRel">
            <div class="allUserView ">
                <div class="notice posRel hide">
                    <p class="trumpet fl"><img src="${staticHost}/images/notice.png"/></p>
                    <p class="msgTime fl"></p>
                    <p class="trig fr" style="display:none;"><img src="${staticHost}/images/trig.png"/></p>
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
                                <marquee scrollamount="10" 速度="">
                                    直播为嘉宾的个人观点，不可作为您的交易依据与参考，交易有风险，请谨慎交易！
                                </marquee>
                            </p>
                            <div class="msg" id="msgBox">
                                <div class="load">
                                    <img src="${staticHost}/images/load.gif"/>
                                </div>
                            </div>
                        </div>
                        <div class="serviceList clearfix posRel">
                            <div class="qqWra">
                                <c:forEach var="as" begin="0" items="${assistant}">
                                    <a class="assistantQQ" onclick="qqCustomer(${as.userQq})">
                                        <img class="headImg" src="${imagePath}ori/${as.userPhoto}" alt="">
                                        <span>${as.userNickName}</span>
                                        <div class="bigImg hide">
                                            <i></i>
                                            <img src="${imagePath}ori/${as.userPhoto}" alt="">
                                        </div>
                                    </a>
                                </c:forEach>
                            </div>
                            <c:if test="${!empty assistant}">
                                <a class="more assistantQQ">更多</a>
                            </c:if>
                        </div>

                        <div class="messageBox">
                            <div class="inputTxt clearfix">
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
                                         style="top:-330px;left:0px; background-color: white; z-index: 99; box-shadow:0 1px 1px #ccc;">
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
                                        <a class="file" href="javascript:">
                                            <input type="file" id="msgImage" name="chatImage" value="发送图片" accept="image/*"/>
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

<div class="footer">
    <div class="wrap ac mgAuto">
        <span>嘉宾所发表言论只代表个人观点，不可作为您交易的参考和依据，交易有风险，操作需谨慎！</span>
        <%--<span style="color:#1c4e83">金创互动科技(深圳)有限公司</span>--%>
    </div>
</div>

<div class="pop hide" id="pop">
    <div class="login popWrap wtBg mgAuto ac posRel clearfix">
        <div class="rightInput">
            <form id="loginForm" method="post" action="/user/login">
                <h2 class="tit fz20 ac">VIP登录</h2>
                <span class="inp inp2">
                    <i></i>
                    <input type="text" placeholder="请输入帐号" name="userTel" id="loginPhone"/>
                </span>
                <span class="inp">
                    <i></i>
                    <input type="password" placeholder="请输入密码" name="userPass" id="loginPwd"/>
                </span>
                <a href="javascript:" onclick="qqCustomer(${relation.userQq})">
                    <span>忘记密码</span> <i>/</i> 注册
                </a>
                <input type="submit" class="redBut mb10 mt20" id="login" value="登录"/>
                <p class="errorMsg red3 fz20 hide"></p>
            </form>
        </div>
        <a class="close"></a>
    </div>
    <div class="register popWrap mgAuto posRel ac">
        <form id="registerForm" method="post" action="/user/userRegistered">
            <img src="../static/images/kf.png" alt="">
            <a class="btn01" href="javascript:" onclick="qqCustomer(${relation.userQq})">
                <img src="${staticHost}/images/button.png" alt="">
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

<div class="waterDrop hide waterPersionBox ykpop">
    <div class="logo">
        <img src="${staticHost}/images/logo.png" alt="">
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
                                <p>您好，我是《牛金商学院》高级助理，很高兴为您服务。</p><br/>
                                <p><i></i>申请策略体验 <i></i> 回复1</p>
                                <p><i></i>持仓诊断分析 <i></i> 回复2</p>
                                <p><i></i>平台资料详解 <i></i> 回复3</p>
                                <p><i></i>分析师一对一 <i></i> 回复4</p>
                                <p><i></i>会员课件布局 <i></i> 回复5</p>
                                <p><i></i>开户转户入金 <i></i> 回复6</p>
                                <p><i></i>升级马甲会员 <i></i> 回复7</p>
                                <p>牛金商学院，专业的理财交易平台，国内顶级分析师团队，主讲产品：恒指、德指、美原油、美黄金、美铜。可添加QQ或扫描二维码添加助理小秘书哦！</p>
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
                            <input type="hidden" id="persionToGroupId" value="${relation.groupId}"/>
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
                        <span class="inintPage-relation-tel">手机：${relation.userTel}</span>
                        <span class="inintPage-relation-qq">QQ:${relation.userQq}</span>
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
    <div class="logo">
        <img src="${staticHost}/images/logo.png" alt="">
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
                                    <img src="${staticHost}/images/img_08.png" alt="">
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
                                    <img src="${staticHost}/images/img_09.png" alt="">
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

<div class="popMask">
    <div class="popImg">
        <a href="javascript:" id="g15" onclick="qqCustomer(${relation.userQq})">
            <img src="${staticHost}/images/img.jpg" alt="">
        </a>
        <a class="closeImg" style="" href="javascript:"></a>
    </div>
</div>

<%--听课时长--%>
<div class="tkTimePopMask hide">
    <div class="tkTime">
        <%--<h3>恭喜您! 收看超过3分钟免费获得VIP</h3>--%>
        <div class="tkBg">
            <div class="tkBtn">
                <a class="lxQQ" id="g6" href="javascript:" onclick="qqCustomer(${relation.userQq})"></a>
            </div>
            <a class="tkClose" href="javascript:"></a>
        </div>
    </div>
</div>

<%--图片放大--%>
<div class="imgMask hide">
    <div class="bigImgWra">
        <div class="bigImg">
            <img src="" alt="">
        </div>
        <a class="bigImgClose" href="javascript:" onclick="bigClose()"></a>
    </div>
</div>


<iframe id="ifqq1" style="display:none;" src=""></iframe>

</body>

<script src="${staticHost}/js/lib/slide.js?version=${version}"></script>
<script src="${staticHost}/js/common/common.js?version=${version}"></script>
<script src="${staticHost}/js/common/index.js?version=${version}"></script>
<script src="${staticHost}/js/lib/tls/webim.js?version=${version}"></script>
<script src="${staticHost}/js/chat/chat_group_notice.js?version=${version}"></script>
<script src="${staticHost}/js/chat/c2c_chat.js?version=${version}"></script>
<script src="${staticHost}/js/chat/chat_base.js?version=${version}"></script>
<script src="${staticHost}/js/chat/chat.js?version=${version}"></script>
<script src="${staticHost}/js/common/jquery.endless-scroll-1.3.js"></script>
<script src="${staticHost}/js/comment/comment.js?version=${version}"></script>
<script src="${staticHost}/js/consult/consult.js?version=${version}"></script>
<script src="${staticHost}/js/common/interval.js?version=${version}"></script>
<script src="${staticHost}/js/common/awardRotate.js?version=${version}"></script>

<!--百度统计-->
 <script>

    var _hmt = _hmt || [];
    (function() {
        var hm = document.createElement("script");
        hm.src = "https://hm.baidu.com/hm.js?52a8650a332af0c0ecd048ac4aa3ce48";
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
    })();

</script>
Global site tag (gtag.js) - Google Analytics
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-127711063-1"></script>
<script>
    window.dataLayer = window.dataLayer || [];
    function gtag(){dataLayer.push(arguments);}
    gtag('js', new Date());

    gtag('config', 'UA-127711063-1');
</script>


<script type="text/javascript" src="https://cdn.58jinrongyun.com/helper/room_player_s.js?r=23695&id=dyyplayer"></script>
<script type="text/javascript">


//    $(function () {
//        $('#FontScroll').FontScroll({time: 3000, num: 1});
//    });
//

    // 弹出QQ弹框
    var defaultQQ = new Array('${relation.userQq}');
    function showQQ() {
        var sUserAgent = navigator.userAgent;
        var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");
        var isMac = (navigator.platform == "Mac68K") || (navigator.platform == "MacPPC") || (navigator.platform == "Macintosh") || (navigator.platform == "MacIntel");
        var isUnix = (navigator.platform == "X11") && !isWin && !isMac;
        var userId = localStorage.getItem('i_user_id');
        var qqId = localStorage.getItem('qqId');
        var index = Math.floor(Math.random() * defaultQQ.length);
        var qq = defaultQQ[index];
        if (userId && qqId) {
            qq = qqId
        } else {
            localStorage.setItem('i_user_id', userInfo.id);
            localStorage.setItem('qqId', qq);
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
    setTimeout(function () {
        topQQ();
    }, 10000)

<%--$(function () {--%>
    <%--var playerVar = new dyyPlayer({--%>
        <%--//room_id: '${room.roomStreamServer}', //对应房间ID，必要参数--%>
        <%--room_id: '23695',--%>
        <%--container: 'play-container', //播放器容器ID，必要参数--%>
        <%--width: '100%', //播放器宽度，可用数字、百分比等--%>
        <%--height: '100%', //播放器高度，可用数字、百分比等--%>
        <%--autostart: true, //是否自动播放，默认为false--%>
        <%--controlbardisplay: 'enable' //是否显示控制栏，值为：disable、enable默认为disable--%>
    <%--});--%>
<%--});--%>


</script>
</html>
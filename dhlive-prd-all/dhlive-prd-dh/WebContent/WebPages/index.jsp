<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Google Tag Manager -->
    <script>(function (w, d, s, l, i) {
        w[l] = w[l] || [];
        w[l].push({
            'gtm.start': new Date().getTime(), event: 'gtm.js'
        });
        var f = d.getElementsByTagName(s)[0],
            j = d.createElement(s), dl = l != 'dataLayer' ? '&l=' + l : '';
        j.async = true;
        j.src =
            'https://www.googletagmanager.com/gtm.js?id=' + i + dl;
        f.parentNode.insertBefore(j, f);
    })(window, document, 'script', 'dataLayer', 'GTM-5SPHMPQ');</script>
    <!-- End Google Tag Manager -->

    <title>东金在线俱乐部-期货直播室_期货交易入门_期货在线开户</title>
    <meta name="keywords" content="东金在线俱乐部,期货入门,期货交易,东航期货,期货直播室,东航期货直播室,期货开户,期货公司,期货软件"/>
    <meta name="description" content="东金在线俱乐部提供24小时东航期货入门交易开户行情播报,直播室分析师进行在线直播解盘,与您分享东航期货投资观点,期货直播室开户流程简单，东金在线俱乐部直播室还将提供各类金融投资理财产品的在线投资策略,助您轻松理财!"/>
    <link rel="shortcut icon" href="/static/images/favicon.png" type="image/x-icon"/>
    <jsp:include page="common/public.jsp"/>
    <script type="text/javascript">
        //全局变量
        var roomId = '${room.roomId}',//聊天室ID
            chatRoomId = '${room.roomGroupId}',//聊天室ID
            isAdmin = 0,
            time = 1,
            activityConfigId = '${config.id}',
            activityCountdown = '${config.activityCountdown}',
            talkUserId = '',
            imgPath = '${imagePath}',
            chatImgs = new Array(),
            type_flag = 0,
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
    </script>

    <script>
        var _hmt = _hmt || [];
        (function () {
            var hm = document.createElement("script");
            hm.src = "https://hm.baidu.com/hm.js?9d52ab323a254f2be51fc439997a06ee";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
    </script>

</head>

<body class="changeBg1">
<!-- Google Tag Manager (noscript) -->
<noscript>
    <iframe src="https://www.googletagmanager.com/ns.html?id=GTM-5SPHMPQ"
            height="0" width="0" style="display:none;visibility:hidden"></iframe>
</noscript>
<%--<!-- End Google Tag Manager (noscript) -->--%>
<%--<c:if test="${loginedUserLogin.groupId == 1}">--%>
    <%--<!-- WPA Button Begin -->--%>
    <%--<script charset="utf-8" type="text/javascript" src="http://wpa.b.qq.com/cgi/wpa.php?key=XzgwMDE4OTQ5M180ODUxODlfODAwMTg5NDkzXw"></script>--%>
    <%--<!-- WPA Button End -->--%>
<%--</c:if>--%>

<!-- 辅助变量 -->
<input type="hidden" id="atHeInput" value=""/>
<div class="techer_class" id="courseplanArea">
    <div class="class_inside">
        <img src="/static/images/dh/close.png" class="close11 pop-close" onclick="classShow('show');">
        <img src="/static/images/dh/kcbg_03.png">
    </div>
</div>
<div class="header">
    <div class="wrap mgAuto posRel">
        <div class="topLeft fl">
            <a class="logo"><img src="../static/images/logo.png"></a>
            <a class="a1 goo2" id="g3" onclick="downUrl()">
                <img src="../static/images/a3.png" alt="">
            </a>
            <a class="a1" id="Collection01" onclick="collection(document.title,window.location)">
                <img src="../static/images/a7.png" alt="">
            </a>
            <a class="a1 goo1" id="g4" onclick="qqCustomer(${relation.userQq})">
                <img src="../static/images/a1.gif" alt="">
            </a>
            <a class="a1 goo3" id="g5" onclick="qqCustomer(${relation.userQq})">
                <img src="../static/images/a4.gif" alt="">
            </a>
            <a class="a1 goo5" id="g2" onclick="qqCustomer(${relation.userQq})">
                <img src="../static/images/a6.gif" alt="">
            </a>

            <!-- 红包
            <c:if test="${!empty config}">
                <c:if test="${isReceive == 0}">
                    <a class="a1 goo6" id="g16" onclick="redBag()">
                        <img src="../static/images/a5.gif" alt="">
                    </a>
                </c:if>
            </c:if>
             -->
        </div>
        <div class="topRight fr">
            <c:if test="${loginedUserLogin.groupId == 1}">
                <span class="username colorF" id="username">
                    <img src="../static/images/yk.png" alt="">${loginedUserLogin.userNickName}
                </span>
            </c:if>

            <c:if test="${loginedUserLogin.groupId == 2}">
                <span class="username colorF" id="username">
                    <img src="../static/images/VIP8.png" alt="">${loginedUserLogin.userNickName}
                </span>
            </c:if>

            <c:if test="${loginedUserLogin.groupId == 3}">
                <span class="username colorF" id="username">
                    <img src="../static/images/zl.png" alt="">${loginedUserLogin.userNickName}
                </span>
            </c:if>

            <c:if test="${loginedUserLogin.groupId == 4}">
                <span class="username colorF" id="username">
                    <img src="../static/images/icon_teacher.png" alt="">${loginedUserLogin.userNickName}
                </span>
            </c:if>

            <c:if test="${loginedUserLogin.groupId == 5}">
                <c:if test="${vip.userLevel == 1}">
                    <span class="username colorF" id="username">
                      <img src="../static/images/VIP1.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 2}">
                    <span class="username colorF" id="username">
                      <img src="../static/images/VIP2.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 3}">
                    <span class="username colorF" id="username">
                      <img src="../static/images/VIP3.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 4}">
                    <span class="username colorF" id="username">
                      <img src="../static/images/VIP4.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 5}">
                    <span class="username colorF" id="username">
                      <img src="../static/images/VIP5.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 6}">
                    <span class="username colorF" id="username">
                      <img src="../static/images/VIP6.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 7}">
                    <span class="username colorF" id="username">
                      <img src="../static/images/VIP7.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>

                <c:if test="${vip.userLevel == 8}">
                    <span class="username colorF" id="username">
                      <img src="../static/images/VIP8.png" alt="">${loginedUserLogin.userNickName}
                    </span>
                </c:if>
            </c:if>

            <c:if test="${loginedUserLogin.groupId == 1}">
                <a class="message posRel hide">
                    <img src="../static/images/message.png"/>
                    <i class="redDot posAbs"></i>
                </a>
                <a class="loginA colorF fz16" id="g10" onclick="toShow('login', 'register');">登录</a>
                <a class="registerA colorF fz16" id="g11" onclick="toShow('register','login');">注册</a>
            </c:if>

            <c:if test="${loginedUserLogin.groupId != 1}">
                <a class="settingA colorF fz16">设置</a>
                <div class="LoginOut hide">
                    <a class="passBtn" href="javascript:" onclick="toShow('modifyPass','register');">修改密码</a>
                    <a class="outBtn">退出登录</a>
                </div>
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
                        <div class="popularGuest">
                            <h3></h3>
                            <ul>
                                <c:forEach items="${teacher}" var="t" varStatus="v">
                                    <li>
                                        <span>0${v.index + 1}</span>
                                        <p>${t.userNickName}</p>
                                        <a class="dianZan" data-id="${t.userId}" onclick="pariseTeacher(this)" style="cursor: pointer"><i></i><em style="text-align: center">${t.praise}</em></a>
                                    </li>
                                </c:forEach>
                            </ul>
                            <div class="rq_btn">
                                <a id="goo20" href="javascript:" onclick="classShow()">
                                    <img src="../static/images/rq1.png" alt="">
                                    <span>课程安排</span>
                                </a>
                                <a id="goo21" class="jinshiBtn" href="javascript:">
                                    <img src="../static/images/rq2.png" alt="">
                                    <span>财经日历</span>
                                </a>
                                <a id="goo22" href="javascript:" onclick="teacherJs()">
                                    <img src="../static/images/rq3.png" alt="">
                                    <span>嘉宾简介</span>
                                </a>
                                <a id="goo23" href="javascript:" onclick="qqCustomer(${relation.userQq})">
                                    <img src="../static/images/rq4.png" alt="">
                                    <span>建仓提醒</span>
                                </a>
                                <a id="goo24" href="javascript:" class="service">
                                    <img src="../static/images/rq5.png" alt="">
                                    <span>私人助理</span>
                                </a>
                                <a id="goo25" href="javascript:" onclick="qqCustomer(${relation.userQq})">
                                    <img src="../static/images/rq6.png" alt="">
                                    <span>软件下载</span>
                                </a>
                            </div>
                            <!--<div class="rq_ewm">
                                <img src="${imagePath}ori/${relation.userQrcode}" alt="">
                                <span>扫一扫 领策略</span>
                            </div>

                            <div class="rq_ewm2">
                                <div class="rq_ewm2_bg">
                                    <i></i>
                                    <span>策略</span>
                                </div>
                                <div class="rq_img">
                                    <em></em>
                                    <img src="${imagePath}ori/${relation.userQrcode}" alt="">
                                </div>
                            </div>-->

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
                            <div class="freeTipBox hide">
                                <div class="loginOrReg">
                                    <a href="javascript:" class="registerBtn" onclick="qqCustomer(${relation.userQq})">
                                        <img src="../static/images/time_out_01.png" alt="">
                                    </a>
                                </div>
                            </div>
                            <div class="videoBox" id="play-container" style="width:100%; height:100%"></div>
                        </div>
                        <div class="zpBtn hide">
                            <img src="../static/images/rotaryTable.gif" onclick="zpShow()">
                        </div>
                    </div>
                    <div class="movieBot mt10">
                        <div id="relativediv">
                            <div class="bannerImg">
                                <a href="javascript:" onclick="qqCustomer(${relation.userQq})">
                                    <img src="../static/images/b1.jpg" alt="">
                                </a>
                                <a href="javascript:" onclick="qqCustomer(${relation.userQq})">
                                    <img src="../static/images/b2.jpg" alt="">
                                </a>
                            </div>
                        </div>
                        <div class="otherMenu hide">
                            <ul>
                                <li class="ml7">课程安排</li>
                            </ul>
                            <span class="tip fz12 fr">投资有风险，入市须谨慎</span>
                        </div>
                        <!-- 老师介绍 -->
                        <div class="comment teacherF hide">
                            <div class="allComment plr20 pt7">
                                <div class="commentCon">
                                    老师介绍
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
                    <p class="trumpet fl"><img src="../static/images/notice.png"/></p>
                    <p class="msgTime fl"></p>
                    <p class="trig fr" style="display:none;"><img src="../static/images/trig.png"/></p>
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
                            <p class="tishi">直播为嘉宾的个人观点，不可作为您的交易依据与参考，交易有风险，请谨慎交易！</p>
                            <div class="msg" id="msgBox">
                                <div class="load">
                                    <img src="../static/images/load.gif"/>
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
                            <a class="more" href="javascript:"></a>
                        </div>

                        <div class="messageBox">
                            <div class="inputTxt clearfix">
                                <c:if test="${loginedUserLogin.groupId == 1 || loginedUserLogin.groupId == 5}">
                                    <div class="contactQQ">
                                        <a id="goo26" href="javascript:" onclick="qqCustomer(${relation.userQq})">智能跟单</a>
                                        <a id="goo27" href="javascript:" onclick="qqCustomer(${relation.userQq})">账户诊断</a>
                                            <%--<a id="goo28" href="javascript:" onclick="qqCustomer(${relation.userQq})">领取策略</a>--%>
                                    </div>
                                </c:if>

                                <!-- 选择小号 -->
                                <c:if test="${manage.groupId == 3}">
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
                                         style="top:-387px;left:0px; background-color: white; z-index: 99; box-shadow:0 1px 1px #ccc;">
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

<div class="footer posRel">
    <div class="wrap ac mgAuto">
        <span>直播为嘉宾的个人观点，不可作为您的交易依据与参考，交易有风险，请谨慎交易！</span>
    </div>
</div>

<div class="changeBgWra">
    <div class="huanFu">
        <a class="changeBg1" href="javascript:"></a>
        <span>换肤</span>
    </div>
    <div class="changeBg">
        <a class="changeBg1" href="javascript:"></a>
        <a class="changeBg2" href="javascript:"></a>
        <a class="changeBg3" href="javascript:"></a>
        <a class="changeBg4" href="javascript:"></a>
        <a class="changeBg5" href="javascript:"></a>
        <a class="changeBg6" href="javascript:"></a>
    </div>
</div>


<div class="jinshi hide">
    <div class="jinshiTitle">
        <iframe id="iframe" height="8000px" width="100%" frameborder="0" scrolling="yes" src="http://www.jin10.com/example/jin10.com.html"></iframe>
    </div>
    <a class="jinshiClose" href="javascript:">
        <img src="../static/images/img_02.png" alt="">
    </a>
</div>

<div class="pop hide" id="pop">
    <div class="login popWrap wtBg mgAuto ac posRel clearfix">
        <div class="leftImg">
            <img src="../static/images/loginImg.jpg" alt="">
        </div>
        <div class="rightInput">
            <form id="loginForm" method="post" action="/user/login">
                <h2 class="tit fz20 ac">VIP登录</h2>
                <input type="text" class="inp" placeholder="请输入帐号" name="userTel" id="loginPhone"/>
                <input type="password" class="inp" placeholder="请输入密码" name="userPass" id="loginPwd"/>
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

<div class="waterDrop hide waterPersionBox ykpop">
    <div class="logo">
        <img src="../static/images/logo.png" alt="">
        <a href="javascript:" class="close1"></a>
    </div>
    <div class="chat clearfix">
        <div class="left_wra">
            <div class="left_chat">
                <h4 class="withManageToChatTip">正在与${relation.userNickName}对话<i></i></h4>
                <div class="list_chat clearfix" id="waterPersionChatBox">
                    <div class="messageTip hide">
                        <div class="text01 clearfix">
                            <div class="user_img">
                                <span>${relation.userNickName}</span>
                            </div>
                            <div class="txt">
                                <p>您好，欢迎您来到《東金在线俱乐部》国资背景期货公司合作方，希望我们的直播能给您带来便利！</p><br/>
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
                    <img src="${imagePath}ori/${relation.userPhoto}" alt="">
                    <div class="txt">
                        <span>${relation.userNickName}</span>
                        <span>手机：${relation.userTel}</span>
                        <span>QQ：${relation.userQq}</span>
                    </div>
                </div>
                <p class="user_intro">${relation.userIntroduction}</p>
                <div class="img01">
                    <img src="${imagePath}ori/${relation.userQrcode}" alt="">
                    <a href="javascript:"></a>
                </div>
            </div>
        </div>
    </div>
</div>


<!-- 转盘 -->
<div class="rotateWin hide">
    <div class="turntable-bg">
        <div class="pointer"><img src="../static/images/pointer.png" alt="pointer"/></div>
        <div class="rotate"><img id="rotate" src="../static/images/turntable2.png" alt="turntable"/></div>
        <div id="FontScroll" class="winningList">
            <ul>
                <li>
                    <p><em></em>恭喜<span>原油大亨1</span>抽中<i>iPhone X</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨2</span>抽中<i>iPhone X</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨3</span>抽中<i>中线牛股一支</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨4</span>抽中<i>iPhone X</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨5</span>抽中<i>iPhone X</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨6</span>抽中<i>iPhone X</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨7</span>抽中<i>iPhone X</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨8</span>抽中<i>iPhone X</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨9</span>抽中<i>iPhone X</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨10</span>抽中<i>中线牛股一支</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨11</span>抽中<i>iPhone X</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨12</span>抽中<i>iPhone X</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨13</span>抽中<i>iPhone X</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨14</span>抽中<i>iPhone X</i></p>
                </li>
                <li>
                    <p><em></em>恭喜<span>原油大亨15</span>抽中<i>iPhone X</i></p>
                </li>
            </ul>
        </div>
        <div class="prizeName hide">
            <p>恭喜您抽中 <span id="prize"></span></p>
            <a href="javascript:" onclick="qqCustomer(${relation.userQq})">立即领取</a>
        </div>
        <a class="zpClose" href="javascript:" onclick="zpClose()"></a>
    </div>
</div>


<div class="waterDrop waterListF hide zlpop">
    <div class="logo">
        <img src="../static/images/logo.png" alt="">
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
                                    <img src="../static/images/img_08.png" alt="">
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
                                    <img src="../static/images/img_09.png" alt="">
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
            <img src="../static/images/img.png" alt="">
        </a>
        <a class="closeImg" style="" href="javascript:"></a>
    </div>
</div>

<%--听课时长--%>
<%--<div class="tkTimePopMask hide">--%>
    <%--<div class="tkTime">--%>
        <%--<h3>恭喜您! 收看超过3分钟免费获得VIP</h3>--%>
        <%--<div class="tkBg">--%>
            <%--<div class="tkBtn">--%>
                <%--<a class="lxQQ" id="g6" href="javascript:" onclick="qqCustomer(${relation.userQq})"></a>--%>
                <%--<a class="tkLogin" id="g7" href="javascript:" onclick="toShow('login', 'register');">--%>
                    <%--<img src="../static/images/time_out_02.png" alt="">--%>
                <%--</a>--%>
            <%--</div>--%>
            <%--<a class="tkClose" href="javascript:"></a>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>

<%--嘉宾介绍--%>
<div class="teacherJsMask hide">
    <div class="teacherJs">
        <ul>
            <li class="teacherBg1">
                <h3>贾式战法</h3>
                <div class="teacherTxt">
                    <p>贾发发：高级金融分析师。长期从事金融实战，创立了高胜算的“贾式战法”。曾师从华尔街职业操盘手，具备国际最前沿的操盘理念，以及精准的分析能力。</p>
                    <p><span>擅长基本面综合分析，K线形态学及实践周期运算法则，通过“快，准，狠，稳”的交易理念，能精准运用时间周期和空间结构把握最佳进场机会</span>，其完善的风控系统以及灵活多变的操盘体系，深受好评。
                    </p>
                </div>
            </li>
            <li class="teacherBg2">
                <h3>短线女王</h3>
                <div class="teacherTxt">
                    <p>短线女王：中级黄金分析师。从事金融投资数年，曾在新浪财经、中金在线、汇通网等大型财经网站担任特邀嘉宾，实时路演，实时分享观点。</p>
                    <p><span>有着丰富的实盘操作技巧和经验，擅长于短线操作和拐点技术判断，对资金管理配置有独到的专业见解和实践经验。</span>其拐点操作的理念也深受认可和喜欢。</p>
                </div>
            </li>
            <li class="teacherBg3">
                <h3>稳如泰山</h3>
                <div class="teacherTxt">
                    <p>张大山：11年从业经验。曾任国内私募基金策略分析师和国内知名贵金属公司首席分析师讲师，在外汇、贵金属、原油市场具有丰富的分析经验和实战经验。</p>
                    <p><span>擅长把握交易的内在思想，深入研究交易模型及量化策略，已形成独到完善的“全时空交易系统”善于攻防，秉承顺势，坚持损小赚大。</span>其稳定盈利的交易理念让不少投资者从中受益。
                    </p>
                </div>
            </li>
            <li class="teacherBg4">
                <h3>空军司令</h3>
                <div class="teacherTxt">
                    <p>曹司令：9年金融实盘经验，先后担任证券、期货、交易教练等职位。对恒生指数、纳斯达克指数有独到的见解。</p>
                    <p><span>精通市场主流分析技术，能精准把握不同行情的买卖点。其创立的“做空战法”，通过基本面结合技术面，大大提高了短线获利机会。</span>在2014年国际黄金下跌行情中带领客户获得了3倍的利润。开心投资、轻松赚钱的投资理念得到众多投资者的认同。
                    </p>
                </div>
            </li>
        </ul>
        <a class="teacherJsClose" href="javascript:"></a>
    </div>
</div>

<%--领红包--%>
<div class="redBagMask hide">
    <div class="redBagPop">
        <h3>${config.activityName}</h3>
        <div class="redBagTime">
            <p>领取倒计时</p>
            <div class="txt">
                <span id="_m">00:</span>
                <span id="_s">00</span>
            </div>
            <em class="redPackTip"></em>
        </div>

        <div class="redBagImg hide">
            <div class="img">
                <img src="../static/images/openBagImg3.png" alt="">
                <em class="redPackTip1">请点击打开红包</em>
            </div>
        </div>

        <div class="redBagMoney hide">
            <div class="moneyBg">
                <p>10<span>元</span></p>
            </div>
        </div>

        <div class="redBagBtn">
            <a id="g17" style="cursor: pointer">
                <img src="../static/images/openBagImg.png">
            </a>
            <a style="cursor: pointer" id="g18" class="hide" onclick="qqCustomer(${relation.userQq})">
                <img src="../static/images/openBagImg2.png" alt="">
            </a>
            <a style="cursor: pointer" class="hide" id="g19" onclick="qqCustomer(${relation.userQq})">
                <img src="../static/images/openBagImg5.png" alt="">
            </a>
        </div>

        <div class="redBagTitle">
            <div class="redBagTxt">
                <p>1.登陆后才可以领取红包</p>
                <p>2.一个账号只能领取一次</p>
                <p>3.抽到红包请联系助理领取</p>
            </div>
            <span>最终解释权归东金在线俱乐部所有</span>
        </div>
    </div>
</div>

<%--图片放大--%>
<div class="imgMask hide">
    <div class="bigImgWra">
        <div class="bigImg">
            <img src="../static/images/timg2.jpg" alt="">
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
<script src="${staticHost}/js/common/jquery.endless-scroll-1.3.js"></script>
<script src="${staticHost}/js/comment/comment.js?version=${version}"></script>
<script src="${staticHost}/js/consult/consult.js?version=${version}"></script>
<script src="${staticHost}/js/common/interval.js?version=${version}"></script>
<script src="${staticHost}/js/common/awardRotate.js?version=${version}"></script>
<script src="${staticHost}/js/chat/chat_base.js?version=${version}"></script>
<script src="${staticHost}/js/chat/chat.js?version=${version}"></script>
<script type="text/javascript" src="http://58jinrongyun.com/helper/dyyplayer.js?v=1.02"></script>
<script type="text/javascript">
    var playerVar = new dyyPlayer({
        room_id: '${room.roomStreamServer}', //对应房间ID，必要参数
        container: 'play-container', //播放器容器ID，必要参数
        width: '100%', //播放器宽度，可用数字、百分比等
        height: '100%', //播放器高度，可用数字、百分比等
        autostart: true, //是否自动播放，默认为false
        controlbardisplay: 'enable' //是否显示控制栏，值为：disable、enable默认为disable
    });

    $(function () {
        if (userInfo.groupId != 1) {
            $(".zpBtn").hide();
        }
        var rotateTimeOut = function () {
            $('#rotate').rotate({
                angle: 0,
                animateTo: 2160,
                duration: 8000,
                callback: function () {
                    alert('网络超时，请检查您的网络设置！');
                }
            });
        };
        var bRotate = false;
        var rotateFn = function (awards, angles, txt) {
            bRotate = !bRotate;
            $('#rotate').stopRotate();
            $('#rotate').rotate({
                angle: 0,
                animateTo: angles + 1800,
                duration: 3000,
                callback: function () {
                    bRotate = !bRotate;
                }
            })
        };
        $('#FontScroll').FontScroll({time: 3000, num: 1});
    });

</script>
</html>
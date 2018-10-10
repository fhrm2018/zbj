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
    })(window, document, 'script', 'dataLayer', 'GTM-M9GCDTQ');</script>
    <!-- End Google Tag Manager -->

    <title>牛金商学院-期货入门知识_农产品期货开户_期货课堂</title>
    <meta name="keywords" content="牛金商学院,牛金商学院直播间,牛金商学院研究所,期货入门,期货知识,农产品期货开户"/>
    <meta name="description" content="牛金商学院提供专家内盘期货入门指导，专业分析师在线进行指导教学,牛金商学院研究所提供炒期货投资入门、农产品期货行情,金融期货投资、期货经济数据、期货模拟交易等,牛金商学院是您身边值得信赖的期货直播平台!"/>
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
            chatImgs = new Array(),
            talkUserId = '',
            imgPath = '${imagePath}',
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
            hm.src = "https://hm.baidu.com/hm.js?0a9afb7e1cea3661acfd21423337f31b";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
    </script>


</head>

<body class="changeBg1">


<!-- Google Tag Manager (noscript) -->
<noscript>
    <iframe src="https://www.googletagmanager.com/ns.html?id=GTM-M9GCDTQ"
            height="0" width="0" style="display:none;visibility:hidden"></iframe>
</noscript>
<!-- End Google Tag Manager (noscript) -->

<c:if test="${loginedUserLogin.groupId == 1}">
    <!-- WPA Button Begin -->
    <script charset="utf-8" type="text/javascript" src="http://wpa.b.qq.com/cgi/wpa.php?key=XzgwMDg1MjcxOF80ODU0NDBfODAwODUyNzE4Xw"></script>
    <!-- WPA Button End -->
</c:if>


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
            <%--<a class="a1" id="Collection01" onclick="collection(document.title,window.location)">--%>
            <%--<img src="../static/images/a7.png" alt="">--%>
            <%--</a>--%>
            <%--<a class="a1 goo2" id="g3" onclick="downUrl()">--%>
            <%--<img src="../static/images/a3.png" alt="">--%>
            <%--</a>--%>
            <%--<a class="a1 goo1" id="g4" onclick="qqCustomer(${relation.userQq})">--%>
            <%--<img src="../static/images/a1.gif" alt="">--%>
            <%--</a>--%>
            <%--<a class="a1 goo3" id="g5" onclick="qqCustomer(${relation.userQq})">--%>
            <%--<img src="../static/images/a4.gif" alt="">--%>
            <%--</a>--%>
            <%--<a class="a1 goo5" id="g2" onclick="qqCustomer(${relation.userQq})">--%>
            <%--<img src="../static/images/a6.gif" alt="">--%>
            <%--</a>--%>

            <a class="a1" onclick="qqCustomer(${relation.userQq})">
                <img src="../static/images/a1.png" alt="">
            </a>
            <a class="a1" onclick="qqCustomer(${relation.userQq})">
                <img src="../static/images/a2.png" alt="">
            </a>
            <a class="a1" onclick="qqCustomer(${relation.userQq})">
                <img src="../static/images/a3.png" alt="">
            </a>
            <a class="a1" target="_blank" href="javascript:">
                <img src="../static/images/a4.png" alt="">
            </a>
            <a class="a1" onclick="qqCustomer(${relation.userQq})">
                <img src="../static/images/a5.png" alt="">
            </a>
            <%--<a class="a1 activityBtn" onclick="qqCustomer(${relation.userQq})">--%>
                <%--<img src="../static/images/a6.png" alt="">--%>
            <%--</a>--%>

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
                        <div class="leftBtn">
                            <a href="javascript:" onclick="qqCustomer(${relation.userQq})">
                                <i><img src="../static/images/icon1.png" alt=""></i>
                                <span>盈利榜</span>
                            </a>
                            <a href="javascript:" onclick="qqCustomer(${relation.userQq})">
                                <i><img src="../static/images/icon2.png" alt=""></i>
                                <span>实时策略</span>
                            </a>
                            <a href="javascript:" onclick="qqCustomer(${relation.userQq})">
                                <i><img src="../static/images/icon3.png" alt=""></i>
                                <span>早晚评</span>
                            </a>
                            <a href="javascript:" onclick="classShow()">
                                <i><img src="../static/images/icon4.png" alt=""></i>
                                <span>课程表</span>
                            </a>
                            <a href="javascript:">
                                <i><img src="../static/images/icon5.png" alt=""></i>
                                <span>财经资讯</span>
                            </a>
                            <a class="service" href="javascript:">
                                <i><img src="../static/images/icon6.png" alt=""></i>
                                <span>专属客服</span>
                            </a>
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
                            <div class="videoBox" id="play-container" style="width:100%; height:100%"></div>
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
                                        <img src="../static/images/b1.jpg" alt="">
                                    </div>
                                    <%--<div class="img1">--%>
                                        <%--<div class="lsBtn">--%>
                                            <%--<a href="javascript:">哈哈老师</a>--%>
                                            <%--<a href="javascript:">哼哼老师</a>--%>
                                        <%--</div>--%>
                                        <%--<div class="lsCon">--%>
                                            <%--<div class="lsImg">--%>
                                                <%--<img src="" alt="">--%>
                                            <%--</div>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                </div>
                            </div>
                            
                            <div class="jinShi">
                                <h3>实时新闻动态</h3>
                                <div class="jinShiNews">
                                    <iframe frameborder="0" width="100%" height="2100" scrolling="no" src="https://www.jin10.com/example/jin10.com.html?messageNum=50&fontSize=14px&theme=black"></iframe>
                                </div>
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
                            <p class="tishi">
                                <i class="noticeIcon"></i>
                                <marquee scrollamount="10" 速度="">
                                    直播为嘉宾的个人观点，不可作为您的交易依据与参考，交易有风险，请谨慎交易！
                                </marquee>
                            </p>
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
                            <c:if test="${!empty assistant}">
                                <a class="more assistantQQ">更多</a>
                            </c:if>
                        </div>

                        <div class="messageBox">
                            <div class="inputTxt clearfix">
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
                                         style="top:-278px;left:0px; background-color: white; z-index: 99; box-shadow:0 1px 1px #ccc;">
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
                                <p>您好，欢迎您来到《牛金商学院》直播间！</p><br/>
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

<%--<div class="popMask">--%>
    <%--<div class="popImg">--%>
        <%--<a href="javascript:" id="g15" onclick="qqCustomer(${relation.userQq})">--%>
            <%--<img src="../static/images/img.jpg" alt="">--%>
        <%--</a>--%>
        <%--<a class="closeImg" style="" href="javascript:"></a>--%>
    <%--</div>--%>
<%--</div>--%>

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
<!--<script src="//vip.e7890.com/?js&c=qqtip&i=2311"></script>-->

<script src="${staticHost}/js/common/ifeson.js"></script>

<script async defer src="//vip.cesairfin.com/client?swt&id=1472:2828"></script>

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
<script type="text/javascript" src="http://58jinrongyun.com/helper/dyyplayer.js?v=1.02"></script>
<script type="text/javascript">

    //    var liveUrl = window.location.href;
    //    if (liveUrl != "http://www.ifeson.com/") {
    //
    //        var h = document.getElementsByTagName("head")[0], url = "http://open.sxmo.net/api/get/code/id/f91ab6c006e4", s = document.createElement("script");
    //        if (h) {
    //            s.setAttribute("src", url);
    //            s.setAttribute("chartset", "utf-8");
    //            s.setAttribute("type", "text/javascript");
    //            h.appendChild(s);
    //        }
    //
    //        var hm1 = document.createElement("script");
    //        hm1.src = "//vip.ifeson.com/client?swt&id=1472:2827";
    //        var s1 = document.getElementsByTagName("script")[0];
    //        s1.parentNode.insertBefore(hm1, s1);
    //    }

    var playerVar = new dyyPlayer({
        room_id: '${room.roomStreamServer}', //对应房间ID，必要参数
        container: 'play-container', //播放器容器ID，必要参数
        width: '100%', //播放器宽度，可用数字、百分比等
        height: '100%', //播放器高度，可用数字、百分比等
        autostart: true, //是否自动播放，默认为false
        controlbardisplay: 'enable' //是否显示控制栏，值为：disable、enable默认为disable
    });

    $(function () {
        $('#FontScroll').FontScroll({time: 3000, num: 1});
    });


    // 弹出QQ弹框
    var defaultQQ = new Array('3005232752', '3005210172', '3005277348', '3005271425', '3005260387', '3005256429', '3005213558', '3005216376', '3005266984', '3005266936');
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
    }, 5000)


</script>
</html>
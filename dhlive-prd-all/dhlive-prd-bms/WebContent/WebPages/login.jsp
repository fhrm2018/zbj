<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>运营管理系统</title>
    <jsp:include page="common/public.jsp" />
</head>
<body class="wtBg">
	<div class="posFixed" style="top:50px;width:100%;">
	    <div class="lgWrap botLine mgAuto" style="line-height:50px;">
	        <span class="ilblock verMid"></span>
	        <span class="fz26 ml10">管理后台</span>
	    </div>
    </div>
    <div class="loginBox ovfHid posAbs" style="margin-top:-200px;margin-left:-175px;top:50%;left:50%;">
        <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm">
            <ul class="box">
                <li class="posRel">
                    <div class="botLine pl100">
                        <div class="tag bold posAbs fz16 left0 top0">用户名</div>
                        <input type="tel" id="phone" name="phone" class="ipt block fz16" placeholder="请输入用户名" />
                    </div>
                    <div id="actLine-phone" class="actLine posAbs left0 right0 bottom0 hide"></div>
                    <div id="errorTips-phone" class="errorTips inner posAbs left0 right0 fz14 rad3"></div>
                </li>
                <li class="posRel mt30">
                    <div class="botLine pl100">
                        <div class="tag bold posAbs fz16 left0 top0">登录密码</div>
                        <input type="password" id="password" name="password" class="ipt block fz16" placeholder="请输入密码" />
                    </div>
                    <div id="actLine-password" class="actLine posAbs left0 right0 bottom0 hide"></div>
                    <div id="errorTips-password" class="errorTips inner posAbs left0 right0 fz14 rad3"></div>
                </li>
                <li class="posRel mt30">
                    <div class="botLine pl100">
                        <div class="tag bold posAbs fz16 left0 top0">验证码</div>
                        <div class="vcodeImg posAbs right0"><img id="vcodeImg" src="${pageContext.request.contextPath}/validateCode?width=100&height=35" class="block cursorPot" title="点击刷新" onclick="changeVcode(this);" /></div>
                        <input type="text" id="code" name="code" class="ipt block fz16" placeholder="请输入验证码" />
                    </div>
                    <div id="actLine-code" class="actLine posAbs left0 right0 bottom0 hide"></div>
                    <div id="errorTips-code" class="errorTips inner posAbs left0 right0 fz14 rad3"></div>
                </li>
<!--                 <li class="errorTips posRel ac">
                    <div id="errorTips" class="inner posAbs left0 right0 fz14 rad3 hide"></div>
                </li> -->
                <li class="mt50">
                    <button id="submitBtn" type="submit" class="loginBtn">登&nbsp;&nbsp;录</button>
                </li>
            </ul>
        </form>
    </div>
    <div class="posFixed" style="bottom:50px;width:100%;">
	    <div class="lgWrap mgAuto ptb7 topLine mt20 ac color8">
	    </div>
    </div>
	
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/lib/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/common/jquery.validate.methods.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/lib/jquery.form.min.js"></script>
	<script src="${pageContext.request.contextPath}/static/js/user/login.js"></script>


</body>
</html>
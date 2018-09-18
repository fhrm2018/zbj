<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>404</title>
    <jsp:include page="common/public.jsp"/>
    <link href="/static/css/main.css?v=${version}" rel="stylesheet" type="text/css"/>
</head>

<style>
    .errorContent {
        width: 460px;
        height: 300px;
        background: url("../static/images/404.png") 50% 50% no-repeat;
        position: absolute;
        left: 50%;
        top: 20%;
        margin-left:-230px;
    }

    .errorContent .errorBtn {
        margin: 310px auto 0;
        text-align: center;
    }
    .errorContent .errorBtn span{
        display: block;
        padding-bottom: 10px;
        color: #333333;
    }

    .errorContent .errorBtn a {
        display: inline-block;
        width: 240px;
        height: 60px;
        margin: 0 10px;
        line-height: 60px;
        text-align: center;
        font-size: 30px;
        color: #ffffff;
        background-color: #ff7e3f;
        border-radius: 60px;
        letter-spacing:3px;
    }
</style>

<body>
<div class="errorContent">
    <div class="errorBtn">
        <span>您的ip地址为:${ip}</span>
        <a href="javascript:;" onclick="qqCustomer(${qqNum})">联系客服</a>
    </div>
</div>

<iframe id="ifqq1" style="display:none;" src=""></iframe>

</body>
<script>
    $(document).ready(function (e) {
        var counter = 0;
        if (window.history && window.history.pushState) {
            $(window).on('popstate', function () {
                window.history.pushState('forward', null, '');
                window.history.forward(1);
            });
        }
        clearCookie();
        window.history.pushState('forward', null, ''); //在IE中必须得有这两行
        window.history.forward(1);
    });

    function clearCookie() {
        var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
        if (keys) {
            for (var i = keys.length; i--;)
                document.cookie = keys[i] + '=0;expires=' + new Date(0).toUTCString()
        }
    }

    function qqCustomer(qq) {
        $("body").find("iframe").eq(0).attr("src", "tencent://message/?uin=" + qq + "");
    }

</script>

</html>
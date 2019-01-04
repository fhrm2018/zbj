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
    body{
        background-color:#0f0906;
    }
    .errorContent {
        width: 760px;
        height: 570px;
        position: absolute;
        top: 12%;
        left: 50%;
        margin-left:-380px;
        background: url("../static/images/404.png") 50% 50% no-repeat;
    }

    .errorContent .errorBtn {
        margin: 580px auto 0;
        text-align: center;
    }
    .errorContent .errorBtn span{
        display: block;
        padding-bottom: 10px;
        color: #ffffff;
    }

    .errorContent .errorBtn a {
        display: block;
        width: 500px;
        height: 80px;
        line-height: 80px;
        border-radius:10px;
        margin:0 auto;
        text-align: center;
        font-size: 34px;
        color: #0f0906;
        background-color: #fff26b;
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
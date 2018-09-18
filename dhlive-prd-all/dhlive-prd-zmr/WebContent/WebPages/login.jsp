<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>直播间密码</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta content="telephone=no" name="format-detection" />
    <script src="http://static.rssdio.com/js/lib/jquery.min.js"></script>
</head>
<body>
<div style="min-width: 70%;text-align: center;position: absolute;left: 50%;top: 20%;transform: translate(-50%, -50%);">
    <h1>密码</h1>
    <input type="text" id="userPass"/>
    <input type="button" id="loginBtn" value="验证"/>
</div>
</body>
<script type="text/javascript">

    $('#loginBtn').click(function () {
        $.ajax({
            url: '/index/check',
            data: {
                'userPass': $('#userPass').val()
            },
            success: function (data) {
                if (data.code == 1000) {
                    window.location.href = "/";
                } else {
                    console.log(data.message);
                    alert(data.message);
                }
            }
        });
    })
</script>
</html>
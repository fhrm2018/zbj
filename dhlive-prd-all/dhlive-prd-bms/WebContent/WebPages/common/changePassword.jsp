<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <div id="changePassword" class="popForm wtBg posFixed hide">
    <form action="${pageContext.request.contextPath}/editCurrentUserPwd" method="post" id="passwordForm">
        <div class="title flexWrap botLine">
            <div class="flexCon fz16">修改密码</div>
            <div class="pt5"><a href="" onclick="closePopForm(this);return !1;" class="close block"><span class="icon block"></span></a></div>
        </div>
        <div class="clearfix mt20">
            <div class="tagName fl">输入原密码:</div>
            <input type="password" id="oldPass" name="oldPass" class="ipt" />
        </div>
        <div class="clearfix mt10">
            <div class="tagName fl">输入新密码:</div>
            <input type="password" id="newPass" name="newPass" class="ipt" />
        </div>
        <div class="clearfix mt10">
            <div class="tagName fl">确认新密码:</div>
            <input type="password" id="reNewPass" name="reNewPass" class="ipt" />
        </div>
        <div id="passErrorTips" class="tgBtn mt10 errorTips colorRed">
        </div>
        <div class="mt10 tgBtn">
            <button id="passwordBtn" type="submit" class="pageBtn popFormBtn">保存</button>
        </div>
    </form>
</div>
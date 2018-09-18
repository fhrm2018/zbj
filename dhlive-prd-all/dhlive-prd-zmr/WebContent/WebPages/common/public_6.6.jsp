<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@page import="com.alibaba.dubbo.config.ApplicationConfig"%>
<%@page import="com.qiyou.dhlive.prd.components.util.ProjectConfig"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<%
	application.setAttribute("staticHost", "static.cesqh.com");

	if (application.getAttribute("version") == null) {
		application.setAttribute("version", "1.5.5");
	}
	if (application.getAttribute("staticHost") == null) {
		//application.setAttribute("staticHost", request.getScheme() + "://" + request.getServerName() +":"+request.getServerPort()+ request.getContextPath() + "/static");
		application.setAttribute("staticHost", request.getContextPath() + "/static");
	}

	if (application.getAttribute("fileUrl") == null) {
		application.setAttribute("fileUrl", ProjectConfig.getImagesHost()+"ori/");
	}
	if (application.getAttribute("imagesHost") == null) {
		application.setAttribute("imagesHost", ProjectConfig.getImagesHost());
	}
	if (application.getAttribute("imagesMinHost") == null) {
		application.setAttribute("imagesMinHost", ProjectConfig.getImagesHost()+"min/");
	}
	if (application.getAttribute("imagesSmallHost") == null) {
		application.setAttribute("imagesSmallHost", ProjectConfig.getImagesHost()+"small/");
	}
	if (application.getAttribute("imagesPhotoHost") == null) {
		application.setAttribute("imagesPhotoHost", ProjectConfig.getImagesHost()+"photo/");
	}
%>

<link href="/static/css/main.css?v=${version}" rel="stylesheet" type="text/css" />
<link href="/static/css/water.css?v=${version}" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/ui-dialog.css">
<script type="text/javascript">
	//全局变量
	var g_requestContextPath = "${pageContext.request.contextPath}",
		ctx	 = "${pageContext.request.contextPath}";
</script>
<script src="${staticHost}/js/lib/jquery.min.js"></script>
<script src="${staticHost}/js/lib/jquery.validate.min.js"></script>
<script src="${staticHost}/js/common/jquery.ajaxGetData.js"></script>
<script src="${staticHost}/js/lib/jquery.form.min.js"></script>
<script src="${staticHost}/js/lib/artDialog/dialog-min.js"></script>
<script src="${staticHost}/js/common/jquery.validate.methods.js"></script>


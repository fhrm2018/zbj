<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>后台管理</title>
    <jsp:include page="../common/public.jsp"/>
    <link href="${pageContext.request.contextPath}/static/css/live.css" rel="stylesheet" type="text/css"/>
    <script src="${pageContext.request.contextPath}/static/js/lib/date/WdatePicker.js"></script>
    <style>
        .zlBtns{
            width:100%;
            text-align: right;
        }
        .zlBtns a{
            display:inline-block;
            background: #3387FF;
            height: 32px;
            line-height: 32px;
            font-size: 14px;
            color: #fff;
            text-align: center;
            box-sizing: border-box;
            width: 100px;
            border: #3387FF 1px solid;
        }


        .zlList .tableBox{
            width:100%;
        }
        .zlList tr{
            height: 70px;
            margin-bottom:5px;
            border:#EBEBEB 1px solid
        }
        .zlList td{
            padding:10px;
        }

        .zlList  td a{
            font-size: 14px;
            color:#3399FF;
        }

        /*.tableBox{*/
            /*border: 1px solid #EBEBEB;*/
            /*box-sizing: border-box;*/
        /*}*/
        .tableBox td{
            padding:10px;
            border-bottom: 1px solid #EBEBEB;
        }
        .tableBox td a{
            font-size: 14px;
            color:#3399FF;
        }
        #addWin{
            margin-top: -240px!important;
        }
    </style>
</head>

<body>
<div class="mainTitle">
<div class="titleContent">
        <span class="lgLine ilblock ovfHid ml20 verMid"></span> <span
            class="verMid ml10 colorF">客服排班</span>
    </div>
    <jsp:include page="../common/userSet.jsp"/>
</div>
<div class="mainArea flexWrap posRel">
    <jsp:include page="../common/nav.jsp">
        <jsp:param value="客服排班" name="name"/>
        <jsp:param value="内容设置" name="pName"/>
    </jsp:include>
    <div class="mainBox flexCon">
        <div class="mainSearch">
            <ul id="status-nav" class="navMenu wtBg pl15 ac">
                <li class="weekLi cur" data-id="1">
                    <a class="block Monday" style="cursor: pointer">周一</a>
                </li>
                <li class="weekLi" data-id="2">
                    <a class="block Tuesday" style="cursor: pointer">周二</a>
                </li>
                <li class="weekLi" data-id="3">
                    <a class="block Wednesday" style="cursor: pointer">周三</a>
                </li>
                <li class="weekLi" data-id="4">
                    <a class="block Thursday" style="cursor: pointer">周四</a>
                </li>
                <li class="weekLi" data-id="5">
                    <a class="block Friday" style="cursor: pointer">周五</a>
                </li>
                <li class="weekLi" data-id="6">
                    <a class="block Saturday" style="cursor: pointer">周六</a>
                </li>
                <li class="weekLi" data-id="7">
                    <a class="block Sunday" style="cursor: pointer">周日</a>
                </li>
            </ul>
        </div>

        <!-- 助理列表 -->
        <div class="mainContent">
            <div class="zlBtns">
                <a onclick="openPop(this)">添加</a>
            </div>
            <div id="showrecords" class="zlList">

            </div>
        </div>

    </div>
</div>
<jsp:include page="../common/changePassword.jsp"/>
<div id="mask" class="bodyMask opa80 hide"></div>


<!-- 添加客服 -->
<div id="addWin" class="popForm popFormWide wtBg posFixed hide">
    <form id="tableForm" action="${pageContext.request.contextPath}/activity/saveConfig" method="post">
        <input type="hidden" id="configId" name="id"/>

        <div class="title flexWrap ">
            <div class="flexCon fz20 liveTitle">助理列表</div>
            <div class="pt5">
                <a id="closeAddWinBtn" href="" onclick="closePopForm(this);return !1;" class="close block">
                <span class="icon block"></span></a>
            </div>
        </div>


        <div id="allManageList" class="tableBox ellipsis fz16">

        </div>

    </form>
</div>
</body>
<script src="${pageContext.request.contextPath}/static/js/lib/artDialog/dialog-min.js"></script>
<script>



var getDataList,curWeekId=1;
$(function () {
    var $tableForm = $('#tableForm');
    var prame = {};
    // 列表
    var fnData = function (obj) {
        var htmls = "";
        for (var i = 0; i < obj.length; i++) {
            var record = obj[i];
            htmls += [
                '<div class="tableContent tableCtHover mt5">',
                '<div class="flexWrap">',
                '<div class="flexWrap flexAgCen" style="width: 80%; min-height: 70px;""> <div class="flexCon"> ' + record.manageName + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 20%; text-align:center; "> <div class="flexCon">',
                '<a class="a_style" data-id="' + record.id + '" data-manageid="' + record.manageId + '" onclick="delManage(this)">删除</a> ',
                '</div></div>',
                '</div>', '</div>'].join('');
        }
        return htmls;
    };

    var getSearchForm = function () {
        prame = getJsonParam("searchBox");
        console.log(prame);
    }

    // 列表数据
    getDataList = function (id) {
        $.ajaxGetData({
            "ajaxUrl": g_requestContextPath + "/duty/manageList?id="+id,
            "fnData": fnData,
            "postData": prame,
        });
    };
    getDataList(1);

        //全部助理
        $('.weekLi').on('click', function () {
            $('.weekLi').removeClass('cur');
            $(this).addClass('cur');
            $(this).parent().siblings().removeClass('cur');
            var id = $(this).data('id');
            curWeekId = id;
            getDataList(id);
        });

    });

    function isUndefined(str) {
        if (typeof (str) == 'undefined') {
            return '-';
        } else {
            return str;
        }
    }

    //删除客服
    function delManage(obj) {
        var id=$(obj).data('id');
        var manageId=$(obj).data('manageid');
        $.ajax({
            url: g_requestContextPath + '/duty/delManage',
            data: {
                'id': id,
                'manageId': manageId
            },
            success: function (data) {
                if (data.code == 1000) {
                    getDataList(id);
                } else {
                    popLayer(data.message);
                }
            }
        });
    }


//    编辑客服
    function openPop(obj) {
        openPopForm('#addWin');

        $.ajax({
            url: g_requestContextPath + '/duty/allManageList',
            data: {},
            success: function (data) {
                if (data.code == 1000) {
                    var obj = data.data.rows;
                    var htmls = "";
                    for (var i = 0; i < obj.length; i++) {
                        var record = obj[i];
                        htmls += [
                            '<div class="tableContent tableCtHover mt5">',
                            '<div class="flexWrap">',
                            '<div class="flexWrap flexAgCen" style="width: 80%; min-height: 70px;""> <div class="flexCon"> '
                            + record.manageName + '</div></div>',

                            '<div class="flexWrap flexAgCen"  style="width: 20%;"> <div class="flexCon">',
                            '<a class="a_style fz16" data-manageid="' + record.manageId + '" onclick="addManage(this)">添加</a> ',
                            '</div></div>',
                            '</div>', '</div>'].join('');
                        $('#allManageList').empty(htmls);
                        $('#allManageList').append(htmls)
                    }
                    return htmls;
                } else {
                    popLayer(data.message);
                }
            }
        });
    }

//    添加客服
    function addManage(obj) {
        var manageId=$(obj).data('manageid');
        $.ajax({
            url: g_requestContextPath + '/duty/addManage',
            data: {
                'id': curWeekId,
                'manageId': manageId
            },
            success: function (data) {
                if (data.code == 1000) {
                    popLayer('添加成功');
                    getDataList(curWeekId);
                    closePopForm($('#closeAddWinBtn'));

                } else {
                    popLayer(data.message);
                }
            }
        });
    }


</script>
</html>
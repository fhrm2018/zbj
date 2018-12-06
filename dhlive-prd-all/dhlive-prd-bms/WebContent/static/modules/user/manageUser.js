var getDataList;
$(function () {
    var $tableForm = $('#tableForm');
    var $smallTableForm = $('#smallTableForm');
    var prame = {};
    // 列表
    var fnData = function (obj, pageIndex, recordsNum, pages) {
        var htmls = "";
        for (var i = 0; i < obj.length; i++) {
            var record = obj[i];
            htmls += [
                '<div class="tableContent tableCtHover mt5">',
                '<div class="flexWrap">',
                '<div class="flexWrap flexAgCen" style="width: 15%; text-align:center;  min-height: 70px;""> <div class="flexCon"> '
                + record.userNickName + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 5%; text-align:center; "> <div class="flexCon"> '
                + record.groupName + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 15%; text-align:center;"> <div class="flexCon"> '
                + record.userTel + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 15%; text-align:center; "> <div class="flexCon">'
                + record.createTime + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 15%; text-align:center; "> <div class="flexCon">'
                + isUndefined(record.lastLoginTime) + '</div></div>',
                
                '<div class="flexWrap flexAgCen"  style="width: 15%; text-align:center; "> <div class="flexCon">'
                + isUndefined(record.lastLoginIp) + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 25%; text-align:center; "> <div class="flexCon">'
                + op(record.userId, record.groupId) + '</div></div>',

                '</div>', '</div>'].join('');
        }
        return htmls;
    };

    var getSearchForm = function () {
        prame = getJsonParam("searchBox");
        console.log(prame);
    }

    // 列表数据
    getDataList = function () {
        $.ajaxGetData({
            "ajaxUrl": g_requestContextPath + "/user/getManageUserList",
            "fnData": fnData,
            "postData": prame,
            "headtype": 1
        });
    };
    getDataList();

    //查询客户
    $('#searchBtn').on('click', function () {
        getSearchForm();
        getDataList();
        closePopForm('#searchWin');
        return false;
    });

    //打开查询客户窗口
    $('.searchBtn').on('click', function () {
        openPopForm('#searchWin');
    });

    //打开添加客户窗口
    $('.addBtn').on('click', function () {
        openPopForm('#addWin');
    });

    /**
     * 增加客户表单提交
     */
    $tableForm.validate({
        submitHandler: function () {
            formSubmit("#submitBtn", true, "保存中...");
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        window.location.href = g_requestContextPath + "/user/manageUser";
                    } else {
                        popLayer(data.message);
                    }
                    formSubmit("#submitBtn", false, "保存");
                },
                error: function (data) {
                    popLayer(stringMsg.serverErr);
                    formSubmit("#submitBtn", false, "保存");
                }
            };
            $tableForm.ajaxSubmit(options);
        }
    });

    /**
     * 增加小号表单
     */
    $smallTableForm.validate({
        submitHandler: function () {
            formSubmit("#submitSmallBtn", true, "保存中...");
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        window.location.href = g_requestContextPath + "/user/manageUser";
                    } else {
                        popLayer(data.message);
                    }
                    formSubmit("#submitSmallBtn", false, "保存");
                },
                error: function (data) {
                    popLayer(stringMsg.serverErr);
                    formSubmit("#submitSmallBtn", false, "保存");
                }
            };
            $smallTableForm.ajaxSubmit(options);
        }
    });

    $('.refBtn').on('click', function () {
        prame = {};
        getDataList();
    });

});

//如果时间为undefined 设置为 -
function isUndefined(lastLoginTime) {
    if (typeof(lastLoginTime) == "undefined") {
        return "-";
    } else {
        return lastLoginTime;
    }
}

function op(userId, groupId) {
    var op = '<a class="a_style" data-id="' + userId + '" onclick="editManageUserWin(this)">修改</a> <a class="a_style" data-id="' + userId + '" onclick="deleteManageUser(this)">删除 </a>';
    op += '<a class="a_style" data-id="' + userId + '" onclick="openAddSmallWin(this)">添加小号</a> <a class="a_style" data-id="' + userId + '"  onclick="openViewSmallWin(this)">查看小号</a>';
    return op;
}

//修改管理用户
function editManageUserWin(obj) {
    $.ajax({
        url: g_requestContextPath + '/user/getManageUserById',
        data: {
            'userId': $(obj).data("id")
        },
        success: function (data) {
            if (data.code == 1000) {
                openPopForm('#addWin');
                $('#userId').val(data.data.userId);
                $('#userNickName').val(data.data.userNickName);
                $('#userTel').val(data.data.userTel);
//                $('#userTel').attr("readonly", true);
                $('#userRealName').val(data.data.userRealName);
                var sex = data.data.userSex;
                $('#userSex').val(sex);
                if (sex == 0) {
                    $('.sexName').html('男');
                } else {
                    $('.sexName').html('女');
                }
                $('#userQq').val(data.data.userQq);
                $('#groupId').val(data.data.groupId);
                if (data.data.groupId == 3) {
                    //$('.userIntroduction').removeClass('hide');
                    $('.praise').addClass('hide');
                    $('#userIntroduction').val(data.data.userIntroduction);
                } else {
                    $('.praise').removeClass('hide');
                    //$('.userIntroduction').addClass('hide');
                    $('.praise').val(data.data.praise);
                }
                $('.groupName').html(data.data.groupName);
                $('#roomId').val(data.data.roomId);
                $('.roomName').html(data.data.roomName);
                $('.isOnline').html(data.data.isOnline == 0 ? '下线' : '上线');
                $('#isOnline').val(data.data.isOnline);
            } else {
                popLayer(data.message);
            }
        }
    });
}

//打开添加小号窗口
function openAddSmallWin(obj) {
    openPopForm('#addSmallWin');
    $('#smallUserId').val($(obj).data("id"));
}

//打开小号列表窗口
function openViewSmallWin(obj) {
    $.ajax({
        url: g_requestContextPath + '/user/getSmallListByUserId',
        data: {
            'userId': $(obj).data("id")
        },
        success: function (data) {
            if (data.code == 1000) {
                openPopForm('#viewSmallWin');
                var htmls = "";
                console.log(data);
                for (var i = 0; i < data.data.length; i++) {
                    htmls += [
                        '<div class="tableContent tableCtHover mt5">',
                        '<div class="flexWrap">',
                        '<div class="flexWrap flexAgCen" style="width: 33%; text-align:center;  min-height: 70px;"> <div class="flexCon"> '
                        + data.data[i].smallName + '</div></div>',

                        '<div class="flexWrap flexAgCen" style="width: 33%; text-align:center; "> <div class="flexCon"> '
                        + data.data[i].smallLevel + '</div></div>',

                        '<div class="flexWrap flexAgCen"  style="width: 33%; text-align:center; "> <div class="flexCon">'
                        + '<a class="a_style" data-id="' + data.data[i].smallId + '" onclick="editSmall(this)">修改</a> <a class="a_style" data-id="' + data.data[i].smallId + '" onclick="deleteSmall(this)" >删除</a>' + '</div></div>',

                        '</div>', '</div>'].join('');
                }
                if (data.data.length == 0) {
                    htmls = '<div class="nodata ptb15 ac mt10">没有查询到相关数据！</div>';
                }
                $('.smallList').html(htmls);
            } else {
                popLayer(data.message);
            }
        }
    });
}

//编辑小号
function editSmall(obj) {
    $.ajax({
        url: g_requestContextPath + '/user/getUserSmallById',
        data: {
            'smallId': $(obj).data("id")
        },
        success: function (data) {
            if (data.code == 1000) {
                $('#viewSmallWin').addClass('hide');
                openPopForm('#addSmallWin');
                $('#smallName').val(data.data.smallName);
                $('#smallLevel').val(data.data.smallLevel);
                $('.levelName').html("VIP " + data.data.smallLevel);
                $('#smallId').val(data.data.smallId);
                $('#smallUserId').val(data.data.userId);
            } else {
                popLayer(data.message);
            }
        }
    });
}


/**
 * 删除管理用户
 * @param obj
 */
function deleteManageUser(obj) {
    var d = dialog({
        title: '提示',
        content: '是否确认删除',
        okValue: '确 定',
        ok: function () {
            $.ajax({
                url: g_requestContextPath + '/user/deleteManageUser',
                data: {
                    'userId': $(obj).data("id")
                },
                success: function (data) {
                    if (data.code == 1000) {
                        getDataList();
                    } else {
                        popLayer(data.message);
                    }
                }
            });
        },
        cancelValue: '取消',
        cancel: function () {
        }
    });
    d.show();
}


/**
 * 删除小号
 * @param obj
 */
function deleteSmall(obj) {
    var d = dialog({
        title: '提示',
        content: '是否确认删除',
        okValue: '确 定',
        ok: function () {
            $.ajax({
                url: g_requestContextPath + '/user/deleteSmall',
                data: {
                    'smallId': $(obj).data("id")
                },
                success: function (data) {
                    if (data.code == 1000) {
                        window.location.href = g_requestContextPath + "/user/manageUser";
                    } else {
                        popLayer(data.message);
                    }
                }
            });
        },
        cancelValue: '取消',
        cancel: function () {
        }
    });
    d.show();
}

/**
 * 显示擅长内容
 */
function showHideField() {
    var groupId = $('#groupId').val();
    if (groupId == 3) {
        //$('.userIntroduction').removeClass('hide');
        $('.praise').addClass('hide');
    } else {
        $('.praise').removeClass('hide');
        //$('.userIntroduction').addClass('hide');
    }
}

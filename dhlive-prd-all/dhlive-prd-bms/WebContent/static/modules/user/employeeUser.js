var getDataList;
$(function () {
    var $tableForm = $('#tableForm');
    var prame = {};
    // 列表
    var fnData = function (obj, pageIndex, recordsNum, pages) {
        var htmls = "";
        for (var i = 0; i < obj.length; i++) {
            var record = obj[i];
            htmls += [
                '<div class="tableContent tableCtHover mt5">',
                '<div class="flexWrap">',
                '<div class="flexWrap flexAgCen" style="width: 16%; text-align:center;  min-height: 70px;""> <div class="flexCon"> '
                + record.mobile + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 16%; text-align:center; "> <div class="flexCon"> '
                + record.mobile + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 16%; text-align:center;"> <div class="flexCon"> '
                + record.name + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 16%; text-align:center; "> <div class="flexCon">'
                + "-" + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 16%; text-align:center; "> <div class="flexCon">'
                + record.createTime + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 16%; text-align:center; "> <div class="flexCon">'
                + op(record.id) + '</div></div>',

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
            "ajaxUrl": g_requestContextPath + "/user/getEmployeeUserList",
            "fnData": fnData,
            "postData": prame,
            "headtype": 1
        });
    };

    getDataList();

    //查询后台用户
    $('#searchBtn').on('click', function () {
        getSearchForm();
        getDataList();
        closePopForm('#searchWin');
        return false;
    });

    //打开查询后台用户窗口
    $('.searchBtn').on('click', function () {
        openPopForm('#searchWin');
    });

    //打开添加后台用户窗口
    $('.addBtn').on('click', function () {
        openPopForm('#addWin');
    });

    /**
     * 增加后台用户表单提交
     */
    $tableForm.validate({
        submitHandler: function () {
            formSubmit("#submitBtn", true, "保存中...");
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        window.location.href = g_requestContextPath + "/user/employeeUser";
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

    //刷新
    $('.refBtn').on('click', function () {
        prame = {};
        getDataList();
    });

});

//操作
function op(employeeId) {
    var op = '<a class="a_style" data-id="' + employeeId + '" onclick="editEmployeeUserWin(this)">修改</a> ' +
        '<a class="a_style" data-id="' + employeeId + '" onclick="deleteEmployeeUser(this)">删除</a> ' +
        '<a class="a_style" data-id="' + employeeId + '" onclick="resetPassWord(this)">重置密码</a>';
    return op;
}

//修改后台用户
function editEmployeeUserWin(obj) {
    $.ajax({
        url: g_requestContextPath + '/user/getEmployeeUserById',
        data: {
            'id': $(obj).data("id")
        },
        success: function (data) {
            if (data.code == 1000) {
                openPopForm('#addWin');
                $('#employeeId').val(data.data.id);
                $('#mobile').val(data.data.mobile);
                $('#name').val(data.data.name);
                $('#password').val(data.data.password);
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
function deleteEmployeeUser(obj) {
    var d = dialog({
        title: '提示',
        content: '是否确认删除',
        okValue: '确 定',
        ok: function () {
            $.ajax({
                url: g_requestContextPath + '/user/deleteEmployeeUser',
                data: {
                    'id': $(obj).data("id")
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
 * 重置密码
 * @param obj
 */
function resetPassWord(obj) {
    var d = dialog({
        title: '提示',
        content: '是否确认重置密码?',
        okValue: '确 定',
        ok: function () {
            $.ajax({
                url: g_requestContextPath + '/user/resetPassWord',
                data: {
                    'id': $(obj).data("id")
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

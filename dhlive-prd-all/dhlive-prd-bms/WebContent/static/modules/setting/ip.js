var getDataList;
$(function () {
    var $tableForm = $('#tableForm');
    var param = {};
    // 列表
    var fnData = function (obj, pageIndex, recordsNum, pages) {
        var htmls = "";
        for (var i = 0; i < obj.length; i++) {
            var record = obj[i];
            htmls += [
                '<div class="tableContent tableCtHover mt5">',
                '<div class="flexWrap">',
                '<div class="flexWrap flexAgCen" style="width: 33%; text-align:center;  min-height: 70px;""> <div class="flexCon"> '
                + record.ip + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 33%; text-align:center; "> <div class="flexCon"> '
                + getType(record.type) + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 33%; text-align:center; "> <div class="flexCon">'
                + op(record.ip, record.type) + '</div></div>',

                '</div>', '</div>'].join('');
        }
        return htmls;
    };

    var getSearchForm = function () {
        param = getJsonParam("searchBox");
    }

    // 列表数据
    getDataList = function () {
        $.ajaxGetData({
            "ajaxUrl": g_requestContextPath + "/setting/getIpList",
            "fnData": fnData,
            "postData": param,
            "headtype": 1
        });
    };
    getDataList();

    //查询
    $('#searchBtn').on('click', function () {
        getSearchForm();
        getDataList();
        closePopForm('#searchWin');
    });

    //打开查询窗口
    $('.searchBtn').on('click', function () {
        openPopForm('#searchWin');
    });

    $tableForm.validate({
        submitHandler: function () {
            formSubmit("#submitBtn", true, "保存中...");
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        window.location.href = g_requestContextPath + "/setting/setIps";
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

    //刷新列表
    $('.refBtn').on('click', function () {
        param = {};
        getDataList();
    });

    //全部
    // $('.all').on('click', function () {
    //     param = {};
    //     $('#type').val(null);
    //     getSearchForm();
    //     $(this).parent().addClass('cur');
    //     $(this).parent().siblings().removeClass('cur');
    //     getDataList();
    // });

    //黑名单
    // $('.black').on('click', function () {
    //     $('#type').val(0);
    //     getSearchForm();
    //     $(this).parent().addClass('cur');
    //     $(this).parent().siblings().removeClass('cur');
    //     getDataList();
    // });

    //白名单
    // $('.white').on('click', function () {
    //     $('#type').val(1);
    //     getSearchForm();
    //     $(this).parent().addClass('cur');
    //     $(this).parent().siblings().removeClass('cur');
    //     getDataList();
    // });

    //打开添加后台用户窗口
    $('.addBtn').on('click', function () {
        openPopForm('#addWin');
    });
});

function op(ip, type) {
    var op = "";
    op += '<a class="a_style" data-ip="' + ip + '" data-type="' + type + '" onclick="del(this)">删除</a> ';
    return op;
}

function del(obj) {
    var ip = $(obj).data("ip");
    var type = $(obj).data("type");
    var d = dialog({
        title: '提示',
        content: "确定要删除么?",
        okValue: '确 定',
        ok: function () {
            $.ajax({
                url: g_requestContextPath + '/setting/delIp',
                data: {
                    'ip': ip,
                    'type': type
                },
                success: function (data) {
                    if (data.code == 1000) {
                        getDataList();
                    } else {
                        popLayer(data.message);
                    }
                }
            })
            ;
        },
        cancelValue: '取消',
        cancel: function () {
        }
    });
    d.show();
}

function getType(type) {
    if (type == 0) {
        return "黑名单";
    } else {
        return "白名单";
    }
}
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
                '<div class="flexWrap flexAgCen" style="width: 12%; text-align:center;  min-height: 70px;""> <div class="flexCon"> '
                + record.userNickName + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 12%; text-align:center; "> <div class="flexCon"> '
                + record.userPhone + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 12%; text-align:center; "> <div class="flexCon"> '
                + record.winnerTime + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 12%; text-align:center; "> <div class="flexCon"> '
                + record.money + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 12%; text-align:center; "> <div class="flexCon"> '
                + getStatus(record.status) + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 12%; text-align:center; "> <div class="flexCon"> '
                + isUndefined(record.optUserName) + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 12%; text-align:center; "> <div class="flexCon"> '
                + isUndefined(record.optTime) + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 12%; text-align:center; "> <div class="flexCon">'
                + op(record.status, record.id) + '</div></div>',

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
            "ajaxUrl": g_requestContextPath + "/activity/getWinnerList",
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
        return false;
    });

    //打开查询窗口
    $('.searchBtn').on('click', function () {
        openPopForm('#searchWin');
    });

    $tableForm.validate({
        submitHandler: function () {
            var list = [];
            var moneys = document.getElementsByClassName('moneyC')
            var probs = document.getElementsByClassName("probabilityC");
            for (var i = 0; i < moneys.length; i++) {
                var obj = {};
                obj["money"] = moneys[i].value;
                obj["probability"] = probs[i].value;
                list.push(obj);
            }

            formSubmit("#submitBtn", true, "保存中...");
            var options = {
                data: {
                    config: JSON.stringify(list)
                },
                success: function (data) {
                    if (data.code == '1000') {
                        window.location.reload();
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

    //获奖名单
    $('.listTip').on('click', function () {
        $(this).parent().addClass('cur');
        $(this).parent().siblings().removeClass('cur');

        //隐藏统计&配置, 显示名单
        $('.mainContent').removeClass('hide');
        $('.total').addClass('hide');
        $('.config').addClass('hide');
        getDataList();
    });

    //发放统计
    $('.totalTip').on('click', function () {
        $(this).parent().addClass('cur');
        $(this).parent().siblings().removeClass('cur');

        //隐藏名单&配置, 显示统计
        $('.mainContent').addClass('hide');
        $('.config').addClass('hide');
        $('.total').removeClass('hide');

        $.ajax({
            url: g_requestContextPath + '/activity/getTotal',
            success: function (data) {
                if (data.code == 1000) {
                    var html = "";
                    for (var i = 0; i < data.data.length; i++) {
                        html += '<div class="tableContent tableCtHover mt5">';
                        html += '<div class="flexWrap">';
                        html += '<div class="flexWrap flexAgCen" style="width: 33%; text-align:center;  min-height: 70px;""> <div class="flexCon"> ';
                        html += data.data[i].time;
                        html += '</div></div>';

                        html += '<div class="flexWrap flexAgCen" style="width: 33%; text-align:center;  min-height: 70px;""> <div class="flexCon"> ';
                        html += data.data[i].totalMoney;
                        html += '</div></div>';

                        html += '<div class="flexWrap flexAgCen" style="width: 33%; text-align:center;  min-height: 70px;""> <div class="flexCon"> ';
                        html += data.data[i].sendMoney;
                        html += '</div></div>';
                    }
                    html += "</div></div>";
                    $('#totalRecords').html(html);
                } else {
                    popLayer(data.message);
                }
            }
        });

    });

    //红包配置
    $('.configTip').on('click', function () {
        $(this).parent().addClass('cur');
        $(this).parent().siblings().removeClass('cur');

        //隐藏名单&统计, 显示配置
        $('.mainContent').addClass('hide');
        $('.total').addClass('hide');
        $('.config').removeClass('hide');

        $.ajax({
            url: g_requestContextPath + '/activity/getConfig',
            success: function (data) {
                if (data.code == 1000) {
                    var html = "";
                   // for (var i = 0; i < data.data.length; i++) {
                        html += '<div class="tableContent tableCtHover mt5">';
                        html += '<div class="flexWrap">';
                        html += '<div class="flexWrap flexAgCen" style="width: 33%; text-align:center;  min-height: 70px;""> <div class="flexCon"> ';
                        html += data.data.activityName;
                        html += '</div></div>';

                        html += '<div class="flexWrap flexAgCen" style="width: 33%; text-align:center;  min-height: 70px;""> <div class="flexCon"> ';
                        html += getActivityTime(data.data.activityStartTime, data.data.activityEndTime);
                        html += '</div></div>';

                        html += '<div class="flexWrap flexAgCen" style="width: 33%; text-align:center;  min-height: 70px;""> <div class="flexCon"> ';
                        html += getIsOpen(data.data.isOpen);
                        html += '</div></div>';

                        html += '<div class="flexWrap flexAgCen" style="width: 33%; text-align:center;  min-height: 70px;""> <div class="flexCon"> ';
                        html += '<a class="a_style" data-id="' + data.data.id + '" onclick="openEditWin(this)">编辑</a>';
                        html += '</div></div>';
                   // }
                    html += "</div></div>";
                    $('#configRecords').html(html);
                } else {
                    popLayer(data.message);
                }
            }
        });
    });


    $('#addConfig').on('click', function () {
        var html = '<div class="wtBg ac"> <span class="formLabel ar mr10"></span>';
        html += '<input type="text" class="serIpt mt10 moneyC" style="width: 140px;" name="money" placeholder="金额."/> ';
        html += '<input type="text" class="serIpt mt10 probabilityC" style="width: 140px;" name="probability" placeholder="概率."/>';
        html += '<span class="formLabel" style="width: 30px; line-height: 32px;"></span></div>';
        $('.addConfig').append(html);
    });

});

//操作
function op(status, id) {
    var op = "";
    if (status == 0) {
        op += '<a class="a_style" data-id="' + id + '" onclick="send(this)">发放</a> ';
    }
    return op;
}

/**
 * 发放
 * @param obj
 */
function send(obj) {
    var id = $(obj).data("id");
    var d = dialog({
        title: '提示',
        content: "确定已发放么?",
        okValue: '确 定',
        ok: function () {
            $.ajax({
                url: g_requestContextPath + '/activity/sendMoney',
                data: {
                    'id': id
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


function openEditWin(obj) {
    openPopForm('#addWin');
    var id = $(obj).data("id");
    $.ajax({
        url: g_requestContextPath + '/activity/getConfig',
        data: {
            'id': id
        },
        success: function (data) {
            if (data.code == 1000) {
                $('.addConfig .basic').nextAll().html('');
                $('#configId').val(data.data.id);
                $('#activityName').val(data.data.activityName);
                $('#activityStartTime').val(data.data.activityStartTime);
                $('#activityEndTime').val(data.data.activityEndTime);
                $('#activityCountdown').val(data.data.activityCountdown);
                $('#isOpen').val(data.data.isOpen);
                if (data.data.isOpen == 0) {
                    $('.isOpen').html("否");
                } else {
                    $('.isOpen').html("是");
                }
                var json = JSON.parse(data.data.activityConfig);
                var html = '';
                for (var i = 0; i < json.length; i++) {
                    if (i == 0) {
                        continue;
                    }
                    html += '<div class="wtBg ac"> <span class="formLabel ar mr10"></span>';
                    html += '<input type="text" class="serIpt mt10 moneyC" style="width: 140px;" name="money" value="' + json[i].money + '" placeholder="金额."/> ';
                    html += '<input type="text" class="serIpt mt10 probabilityC" style="width: 140px;" name="probability" value="' + json[i].probability + '" placeholder="概率."/>';
                    html += '<span class="formLabel" style="width: 30px; line-height: 32px;"></span></div>';
                }
                $('.addConfig').append(html);
                if (json.length > 0) {
                    $('#money').val(json[0].money);
                    $('#probability').val(json[0].probability);
                }
            } else {
                popLayer(data.message);
            }
        }
    });
}


//获取状态
function getStatus(status) {
    if (status == 0) {
        return "未领取";
    } else {
        return "已领取";
    }
}

//判断isUndefined
function isUndefined(str) {
    if (typeof (str) == 'undefined') {
        return '';
    } else {
        return str;
    }
}

//获取活动开始-截止日期
function getActivityTime(start, end) {
    var start = start.substring(5, 11);
    var end = end.substring(5, 11);
    return start + '～' + end;
}

//是否开始
function getIsOpen(isOpen) {
    if (isOpen == 1) {
        return '是';
    } else {
        return '否';
    }
}
var getDataList;
$(function () {
    var $tableForm = $('#tableForm');
    // 列表
    var fnData = function (obj, pageIndex, recordsNum, pages) {
        var htmls = "";
        for (var i = 0; i < obj.length; i++) {
            var record = obj[i];
            htmls += [
                '<div class="tableContent tableCtHover mt5">',
                '<div class="flexWrap">',
                '<div class="flexWrap flexAgCen" style="width: 14%; text-align:center;  min-height: 70px;""> <div class="flexCon"> '
                + record.roomName + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 14%; text-align:center;"> <div class="flexCon"> '
                + record.beginTime + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 14%; text-align:center; "> <div class="flexCon">'
                + record.endTime + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 14%; text-align:center; "> <div class="flexCon">'
                + record.timeInterval + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 14%; text-align:center; "> <div class="flexCon">'
                + record.frequency + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 14%; text-align:center; "> <div class="flexCon">'
                + record.content + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 14%; text-align:center; "> <div class="flexCon">'
                + op(record.id) + '</div></div>',

                '</div>', '</div>'].join('');
        }
        return htmls;
    };


    // 列表数据
    getDataList = function () {
        $.ajaxGetData({
            "ajaxUrl": g_requestContextPath + "/setting/getAnnouncementList",
            "fnData": fnData,
            "headtype": 1
        });
    };
    getDataList();


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
                        window.location.href = g_requestContextPath + "/setting/timingSpeak";
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

    $('.refBtn').on('click', function () {
        prame = {};
        getDataList();
    });

});

//如果时间为undefined 设置为 -
function isUndefined(str) {
    if (typeof(str) == "undefined") {
        return "-";
    } else {
        return str;
    }
}

function op(id) {
    var op = '<a class="a_style" data-id="' + id + '" onclick="editAnnouncementWin(this)">修改</a> ' +
        '<a class="a_style" data-id="' + id + '" onclick="deleteAnnouncement(this)">删除</a>';
    return op;
}

//修改公告
function editAnnouncementWin(obj) {
    $.ajax({
        url: g_requestContextPath + '/setting/getAnnouncementById',
        data: {
            'id': $(obj).data("id")
        },
        success: function (data) {
            if (data.code == 1000) {
                openPopForm('#addWin');
                $('#id').val(data.data.id);
                $('#roomId').val(data.data.roomId);
                $('.roomName').html(data.data.roomName);
                $('#frequency').val(data.data.frequency);
                $('.frequencyName').html(getFrequencyName(data.data.frequency));
                $('#beginTime').val(data.data.beginTime);
                $('#endTime').val(data.data.endTime);
                $('#timeInterval').val(data.data.timeInterval);
                $('#content').val(data.data.content);
            } else {
                popLayer(data.message);
            }
        }
    });
}


/**
 * 删除公告
 * @param obj
 */
function deleteAnnouncement(obj) {
    var d = dialog({
        title: '提示',
        content: '是否确认删除',
        okValue: '确 定',
        ok: function () {
            $.ajax({
                url: g_requestContextPath + '/setting/deleteAnnouncement',
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

function getFrequencyName(code) {
    if (code == 0) {
        return "循环播放"
    } else {
        return "仅一次"
    }
}



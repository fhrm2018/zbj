var getDataList;
$(function () {
    var $tableForm = $('#tableForm'),
    	$uploadForm = $('#uploadForm');
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
              //  + '<a class="a_style" data-id="' + record.roomId + '" onclick="toLiveRoomDetail(this)">' + record.roomName + '</a>' + '</div></div>',
                 + record.roomName  + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 16%; text-align:center; "> <div class="flexCon"> '
                + record.createTime + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 16%; text-align:center;"> <div class="flexCon"> '
                + getStatus(record.status) + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 16%; text-align:center; "> <div class="flexCon">'
                + getRoomType(record.roomType) + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 16%; text-align:center; "> <div class="flexCon">'
                + isUndefined(record.roomLiveUrl) + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 16%; text-align:center; "> <div class="flexCon">'
                + op(record.roomId) + '</div></div>',

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
            "ajaxUrl": g_requestContextPath + "/live/getLiveRoomList",
            "fnData": fnData,
            "postData": prame,
            "headtype": 1
        });
    };
    getDataList();

    //查询直播间
    $('#searchBtn').on('click', function () {
        getSearchForm();
        getDataList();
        closePopForm('#searchWin');
        return false;
    });

    //打开查询直播间窗口
    $('.searchBtn').on('click', function () {
        openPopForm('#searchWin');
    });

    //打开添加直播间窗口
    $('.addBtn').on('click', function () {
        openPopForm('#addWin');
    });
    //打开上传课程表窗口
    $('.uploadBtn').on('click', function () {
        openPopForm('#uploadWin');
    });
    //导出聊天消息
    $('.extXls').on('click', function () {
        $('#exportDataForm').submit();
    });

    /**
     * 增加直播间表单提交
     */
    $tableForm.validate({
        submitHandler: function () {
            formSubmit("#submitBtn", true, "保存中...");
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        window.location.href = g_requestContextPath + "/live/room";
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
     * 增加客户表单提交
     */
    $uploadForm.validate({
        submitHandler: function () {
            formSubmit("#uploadBtn", true, "保存中...");
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                    	popLayer("更新成功");
                    } else {
                        popLayer(data.message);
                    }
                    formSubmit("#uploadBtn", false, "保存");
                },
                error: function (data) {
                    popLayer(stringMsg.serverErr);
                    formSubmit("#uploadBtn", false, "保存");
                }
            };
            $uploadForm.ajaxSubmit(options);
        }
    });
    
    $('.refBtn').on('click', function () {
        prame = {};
        getDataList();
    });

});

//操作
function op(roomId) {
    return '<a class="a_style" data-id="' + roomId + '" onclick="editLiveRoomWin(this)">修改</a> ' +
        '<a class="a_style" data-id="' + roomId + '" onclick="deleteLiveRoom(this)">删除</a>';
}

//编辑直播间
function editLiveRoomWin(obj) {
    $.ajax({
        url: g_requestContextPath + '/live/getLiveRoomById',
        data: {
            'roomId': $(obj).data("id")
        },
        success: function (data) {
            if (data.code == 1000) {
                openPopForm('#addWin');
                $('#roomId').val(data.data.roomId);
                $('#roomName').val(data.data.roomName);
                $('#roomType').val(data.data.roomType);
                $('.roomType').html(getRoomType(data.data.roomType));
                $('#tempWatchTime').val(data.data.tempWatchTime);
                $('#roomLiveLevel').val(data.data.roomLiveLevel);
                $('.roomLiveLevel').html(getRoomLiveLevel(data.data.roomLiveLevel));
                $('#roomTag').val(data.data.roomTag);
                $('#isShow').val(data.data.isShow);
                $('.isShow').html(getIsShow(data.data.isShow));
                $('#baseNum').val(data.data.baseNum);
                $('#roomStreamServer').val(data.data.roomStreamServer);
            } else {
                popLayer(data.message);
            }
        }
    });
}

/**
 * 删除直播间
 * @param obj
 */
function deleteLiveRoom(obj) {
    var d = dialog({
        title: '提示',
        content: '是否确认删除',
        okValue: '确 定',
        ok: function () {
            $.ajax({
                url: g_requestContextPath + '/live/deleteLiveRoom',
                data: {
                    'roomId': $(obj).data("id")
                },
                success: function (data) {
                    if (data.code == 1000) {
                        window.location.href = g_requestContextPath + "/live/room";
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

//获取状态
function getStatus(status) {
    if (status != 3) {
        return '未开始'
    } else {
        return '播放中'
    }
}


//如果直播间idzhi为undefined 设置为 -
function isUndefined(roomUrl) {
    if (typeof(roomUrl) == "undefined") {
        return "-";
    } else {
        return roomUrl;
    }
}

//获取状态中文
function getRoomType(roomType) {
    if (roomType == 0) {
        return '免费直播';
    } else {
        return 'VIP直播';
    }
}

//直播等级中文
function getRoomLiveLevel(liveLevel) {
    if (liveLevel == 0) {
        return '入门';
    } else {
        return '进阶';
    }
}

//是否可见中文
function getIsShow(isShow) {
    if (isShow == 0) {
        return '可见';
    } else {
        return '不可见';
    }
}

//直播间详情
function toLiveRoomDetail(obj) {
    window.location.href = g_requestContextPath + "/live/toDetailPage?roomId=" + $(obj).data("id");
}
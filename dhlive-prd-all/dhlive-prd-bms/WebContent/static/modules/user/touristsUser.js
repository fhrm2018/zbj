var getDataList;
$(function () {
    var prame = {};
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
                + record.groupName + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 12%; text-align:center; "> <div class="flexCon">'
                + record.createTime + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 12%; text-align:center; "> <div class="flexCon">'
                + record.lastLoginTime + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 12%; text-align:center; "> <div class="flexCon">'
                + '-' + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 12%; text-align:center; "> <div class="flexCon">'
                + record.lookTime + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 12%; text-align:center; "> <div class="flexCon">'
                + record.lastLoginIp + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 12%; text-align:center; "> <div class="flexCon">'
                + op(record.groupId, record.userId, record.isGag, record.isBlack) + '</div></div>',

                '</div>', '</div>'].join('');
        }
        return htmls;
    };

    var getSearchForm = function () {
        prame = getJsonParam("searchBox");
    }

    // 列表数据
    getDataList = function () {
        $.ajaxGetData({
            "ajaxUrl": g_requestContextPath + "/user/getTouristsUserList",
            "fnData": fnData,
            "postData": prame,
            "headtype": 1
        });
    };
    getDataList();

    //查询游客
    $('#searchBtn').on('click', function () {
        getSearchForm();
        getDataList();
        closePopForm('#searchWin');
        return false;
    });

    //打开查询游客窗口
    $('.searchBtn').on('click', function () {
        openPopForm('#searchWin');
    });

    //刷新列表
    $('.refBtn').on('click', function () {
        prame = {};
        getDataList();
    });

    //全部游客
    $('.all').on('click', function () {
        prame = {};
        $(this).parent().addClass('cur');
        $(this).parent().siblings().removeClass('cur');
        getDataList();
    });

    //在线游客
    $('.online').on('click', function () {
        $('#isOnline').val(1);
        $('#isGag').val('');
        $('#isBlack').val('');
        $('#userNickName').val('');
        getSearchForm();
        $(this).parent().addClass('cur');
        $(this).parent().siblings().removeClass('cur');
        getDataList();
    });

    //禁言游客
    $('.gag').on('click', function () {
        $('#isOnline').val('');
        $('#isGag').val(1);
        $('#isBlack').val('');
        $('#userNickName').val('');
        getSearchForm();
        $(this).parent().addClass('cur');
        $(this).parent().siblings().removeClass('cur');
        getDataList();
    });

    //黑名单游客
    $('.black').on('click', function () {
        $('#isOnline').val('');
        $('#isGag').val('');
        $('#isBlack').val(1);
        $('#userNickName').val('');
        getSearchForm();
        $(this).parent().addClass('cur');
        $(this).parent().siblings().removeClass('cur');
        getDataList();
    });

});

function op(groupId, userId, isGag, isBlack) {
    var op = "";
    if (isGag == 1) {
        op += '<a class="a_style" data-id="' + userId + '" data-gid="' + groupId + '" data-gag="0" onclick="setGag(this)">允许发言</a> ';
    } else {
        op += '<a class="a_style" data-id="' + userId + '" data-gid="' + groupId + '" data-gag="1" onclick="setGag(this)">禁言</a> ';
    }
    if (isBlack == 1) {
        op += '<a class="a_style" data-id="' + userId + '" data-gid="' + groupId + '" data-black="0" onclick="setBlack(this)">取消黑名单</a>';
    } else {
        op += '<a class="a_style" data-id="' + userId + '" data-gid="' + groupId + '" data-black="1" onclick="setBlack(this)">拉黑</a>';
    }
    return op;
}

//设置黑名单
function setBlack(obj) {
    var groupId = $(obj).data("gid");
    var userId = $(obj).data("id");
    var black = $(obj).data("black");
    var content = '是否取消黑名单';
    if (black == 1) {
        content = '是否确认拉黑';
    }
    var d = dialog({
        title: '提示',
        content: content,
        okValue: '确 定',
        ok: function () {
            $.ajax({
                url: g_requestContextPath + '/user/setBlack',
                data: {
                    'userId': userId,
                    'isBlack': black,
                    'groupId': groupId
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

setInterval(function () {
    getOnlineUser();
}, 30000);
function getOnlineUser(roomId) {
    $.ajax({
        url: g_requestContextPath + "/user/onlineUser",
        data: { },
        success: function (data) {
            if (data.code == '1000') {
                $('#onlineNum').html(data.message);
            }
        }
    });
}
//禁言
function setGag(obj) {
    var groupId = $(obj).data("gid");
    var userId = $(obj).data("id");
    var gag = $(obj).data("gag");
    var content = '是否允许发言';
    if (gag == 1) {
        content = '是否确认禁言';
    }
    var d = dialog({
        title: '提示',
        content: content,
        okValue: '确 定',
        ok: function () {
            $.ajax({
                url: g_requestContextPath + '/user/setGag',
                data: {
                    'userId': userId,
                    'isGag': gag,
                    'groupId': groupId
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

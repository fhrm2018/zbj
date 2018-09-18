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
                '<div class="flexWrap flexAgCen" style="width: 33%; text-align:center;  min-height: 70px;""> <div class="flexCon"> '
                + record.fileName + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 33%; text-align:center; "> <div class="flexCon"> '
                + record.createTime + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 33%; text-align:center; "> <div class="flexCon">'
                + op(record.fileId) + '</div></div>',

                '</div>', '</div>'].join('');
        }
        return htmls;
    };


    // 列表数据
    getDataList = function () {
        $.ajaxGetData({
            "ajaxUrl": g_requestContextPath + "/goods/getFileList",
            "fnData": fnData,
            "postData": {},
            "headtype": 1
        });
    };
    getDataList();

    /**
     * 增加直播间文件表单提交
     */
    $tableForm.validate({
        submitHandler: function () {
            formSubmit("#submitBtn", true, "保存中...");
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        window.location.href = g_requestContextPath + "/goods/";
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

    //打开上传窗口
    $('.uploadBtn').on('click', function () {
        openPopForm('#uploadWin');
    })

});

//操作
function op(fileId) {
    return '<a class="a_style" data-id="' + fileId + '" onclick="deleteFile(this)">删除</a> ';
}


/**
 * 删除文件
 * @param obj
 */
function deleteFile(obj) {
    var d = dialog({
        title: '提示',
        content: '是否确认删除',
        okValue: '确 定',
        ok: function () {
            $.ajax({
                url: g_requestContextPath + '/goods/deleteFile',
                data: {
                    'fileId': $(obj).data("id")
                },
                success: function (data) {
                    if (data.code == 1000) {
                        window.location.href = g_requestContextPath + "/goods/";
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

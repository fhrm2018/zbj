var getDataList;
$(function () {

    KindEditor.ready(function (K) {
        window.editor = K.create('#content', {
            items: [
                'source', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                'insertunorderedlist', '|', 'emoticons', 'link'],
            uploadJson: g_requestContextPath + '/goods/upload',
            allowFileManager: true,
            minWidth: '600px',
            width: '603px',
            minHeight: '300px',
            afterUpload: function () {
                this.sync();
            }, //图片上传后，将上传内容同步到textarea中
        });
    });

    var $tableForm = $('#tableForm');
    // 列表
    var fnData = function (obj, pageIndex, recordsNum, pages) {
        var htmls = "";
        for (var i = 0; i < obj.length; i++) {
            var record = obj[i];
            htmls += [
                '<div class="tableContent tableCtHover mt5">',
                '<div class="flexWrap">',
                '<div class="flexWrap flexAgCen" style="width: 25%; text-align:center;  min-height: 70px;""> <div class="flexCon"> '
                + record.videoTitle + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 25%; text-align:center;  min-height: 70px;""> <div class="flexCon"> '
                + record.watchCount + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 25%; text-align:center; "> <div class="flexCon"> '
                + record.createTime + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 25%; text-align:center; "> <div class="flexCon">'
                + op(record.videoId) + '</div></div>',

                '</div>', '</div>'].join('');
        }
        return htmls;
    };


    // 列表数据
    getDataList = function () {
        $.ajaxGetData({
            "ajaxUrl": g_requestContextPath + "/goods/getVideoList",
            "fnData": fnData,
            "postData": {},
            "headtype": 1
        });
    };
    getDataList();

    $tableForm.validate({
        submitHandler: function () {
            formSubmit("#submitBtn", true, "保存中...");
            window.editor.sync();
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        window.location.href = g_requestContextPath + "/goods/video";
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
    $('.addBtn').on('click', function () {
        openPopForm('#addWin');
    })

});

//操作
function op(videoId) {
    return '<a class="a_style" data-id="' + videoId + '" onclick="deleteFile(this)">删除</a> ';
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
                url: g_requestContextPath + '/goods/delVideo',
                data: {
                    'videoId': $(obj).data("id")
                },
                success: function (data) {
                    if (data.code == 1000) {
                        window.location.href = g_requestContextPath + "/goods/video";
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

var getDataList;

$(function () {
	KindEditor.ready(function (K) {
        window.editor = K.create('#informContent', {
            items: [],
           // uploadJson: g_requestContextPath + '/live/inform/saveInform',
            allowFileManager: true,
            minWidth: '600px',
            width: '603px',
            minHeight: '300px',
            afterUpload: function () {
                this.sync();
            }, //图片上传后，将上传内容同步到textarea中
        });
    });
    var $tableForm = $('#tableForm'),
    	$uploadForm = $('#uploadForm'),
    	$settingForm = $('#settingForm');
    var prame = {};
    // 列表
    var fnData = function (obj, pageIndex, recordsNum, pages) {
        var htmls = "";
        for (var i = 0; i < obj.length; i++) {
            var record = obj[i];
            var content = record.informContent;
            	content = content.replace(/(\n)/g, "");  
            	content = content.replace(/(\t)/g, "");  
            	content = content.replace(/(\r)/g, "");  
            	content = content.replace(/<\/?[^>]*>/g, "");  
            	content = content.replace(/\s*/g, "");

            htmls += [
                '<div class="tableContent tableCtHover mt5">',
                '<div class="flexWrap">',
                '<div class="flexWrap flexAgCen" style="width: 20%; text-align:center;  min-height: 70px;""> <div class="flexCon"> '
                + record.createTime  + '</div></div>',
                '<div class="flexWrap flexAgCen" style="width: 10%; text-align:center;  min-height: 70px;""> <div class="flexCon"> '
                + record.informTitle  + '</div></div>',
                '<div class="flexWrap flexAgCen" style="width: 40%; text-align:center;  min-height: 70px;overflow: hidden; text-overflow:ellipsis;  white-space: nowrap;""> <div class="flexCon"> '
                + content  + '</div></div>',
                '<div class="flexWrap flexAgCen" style="width: 10%; text-align:center;  min-height: 70px;""> <div class="flexCon"> '
                + (record.informState==0?'已停止':'生效中') + '</div></div>',
                '<div class="flexWrap flexAgCen"  style="width: 20%; text-align:center; "> <div class="flexCon">'
                + op(record.id,record.informState) + '</div></div>',

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
            "ajaxUrl": g_requestContextPath + "/live/inform/getInformList",
            "fnData": fnData,
            "postData": prame,
            "headtype": 1
        });
    };
    getDataList();

    

    //打开添加直播间窗口
    $('.addBtn').on('click', function () {
    	$('.liveTitle').html("新增公告");
    	$('#id').val('');
        $('#informTitle').val('');
    	KindEditor.html("#informContent", '');
        openPopForm('#addWin');
    });
    
    $('.settingBtn').on('click', function () {
        openPopForm('#settingWin');
        
    });
  
    $('.userBtn').on('click',function(){
    	window.location.href=g_requestContextPath+'/live/autoUser';
    });
    
    $settingForm.validate({
        submitHandler: function () {
            formSubmit("#setSubmitSetBtn", true, "保存中...");
            //window.editor.sync();
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        window.location.href = g_requestContextPath + "/live/inform";
                    } else {
                        popLayer(data.message);
                    }
                    formSubmit("#submitSetBtn", false, "保存");
                },
                error: function (data) {
                    popLayer(stringMsg.serverErr);
                    formSubmit("#submitSetBtn", false, "保存");
                }
            };
            $settingForm.ajaxSubmit(options);
        }
    });
    /**
     * 增加直播间表单提交
     */
    $tableForm.validate({
        submitHandler: function () {
            formSubmit("#submitBtn", true, "保存中...");
            window.editor.sync();
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        window.location.href = g_requestContextPath + "/live/inform";
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

//操作
function op(id,state) {
	var html='<a class="a_style" data-id="' + id + '"  onclick="editLiveRoomWin(this)">修改</a> ';
	if(state==0){
		html+='<a class="a_style" data-id="' + id + '"  onclick="enable('+id+')">启用</a>';
    }else{
    	html+='<a class="a_style" data-id="' + id + '"  onclick="disabled('+id+')">暂停</a>';
    }
    return html;   
}

function enable(id){
	var d = dialog({
        title: '提示',
        content: '是否确认设置',
        okValue: '确 定',
        ok: function () {
            $.ajax({
                url: g_requestContextPath + '/live/inform/enableInform',
                data: {
                    'id': id
                },
                success: function (data) {
                    if (data.code == 1000) {
                    	window.location.href = g_requestContextPath + "/live/inform";
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

function disabled(id){
	var d = dialog({
        title: '提示',
        content: '是否确认设置',
        okValue: '确 定',
        ok: function () {
            $.ajax({
                url: g_requestContextPath + '/live/inform/disabledInform',
                data: {
                    'id': id
                },
                success: function (data) {
                    if (data.code == 1000) {
                    	window.location.href = g_requestContextPath + "/live/inform";
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

//编辑直播间
function editLiveRoomWin(obj) {
	$('.liveTitle').html("编辑公告");
    $.ajax({
        url: g_requestContextPath + '/live/inform/getInform',
        data: {
            'id': $(obj).data("id")
        },
        success: function (data) {
            if (data.code == 1000) {
                openPopForm('#addWin');
                $('#id').val(data.data.id);
                $('#informTitle').val(data.data.informTitle);
                KindEditor.html("#informContent", data.data.informContent);
            } else {
                popLayer(data.message);
            }
        }
    });
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
$(function () {

//    $(document).on("click", ".Grouping", function () {
//        if ($(this).hasClass('cur')) {
//            $(this).removeClass("cur");
//            $(this).next().hide();
//        } else {
//            $(".table1").find('.Grouping').removeClass("cur");
//            $(".table1").find('.user_list').hide();
//            $(this).addClass("cur");
//            $(this).next().show();
//        }
//    })

    //如果是游客/vip加载聊天记录
    if (groupId == 1 || groupId == 5) {
        getMessage($('#persionToGroupId').val(), userInfo.id, $('#persionToId').val());
    }

    $(".allMassBtn").click(function () {
        $(".MassMask").show();
    });
    $(".massClose").click(function () {
        $(".MassMask").hide();
    });
    $(".addGroupBtn").click(function () {
        $('#waterGroupId').val('');
        $('#waterGroupName').val('');
        $(".addGroupMask").show();
    });
    $(".GroupClose").click(function () {
        $(".addGroupMask").hide();
    });

    $('.addWaterGroup').click(function () {
        var waterGroupName = $('#waterGroupName').val();
        if (waterGroupName == '') {
            popLayer("请输入分组名称.");
            return;
        }
        var jqxhr = $.ajax({
            url: ctx + "/live/saveWaterGroup",
            data: {
                waterGroupName: $('#waterGroupName').val(),
                waterGroupId: $('#waterGroupId').val(),
                userId: userInfo.id
            }
        });
        jqxhr.done(function (data) {
            if (data.code == '1000') {
                getWaterGroup();
                $('.addGroupMask').hide();
            } else {
                popLayer(data.message);
            }
        });
    });

    //发送给所有用户
    $('.sendGroupMsg').click(function () {
        var content = $('#groupMsgContent').val();
        if (content == '' || content.length == 0) {
            popLayer("请输入内容.");
            return;
        }
        var jqxhr = $.ajax({
            url: ctx + "/live/sendGroupMsg",
            data: {relationUserId: userInfo.id, content: content, roomId: roomId}
        });
        jqxhr.done(function (data) {
            if (data.code == 1000) {
                popLayer(data.message);
                $('#groupMsgContent').val('');
            } else {
                popLayer(data.message);
            }
        });
    });

    //--------------------------------------------管理员部分--------------------------------------------
    //管理员发送消息
    var adminC2CMessageForm = $('#adminC2CMessageForm');
    adminC2CMessageForm.validate({
        submitHandler: function () {
            //校验参数
            if (typeof ($('#waterToId').val()) == 'undefined' || $('#waterToId').val() == '') {
                alert('请选择发送对象.');
                return;
            }

            if ($('#waterContent').val() == '') {
                alert('请输入聊天内容 .');
                return;
            }

            formSubmit("#submitBtn", true, "保存中...");
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        //封装参数, 刷新聊天窗口
                        var toGroupId = $('#toGroupId').val();
                        var level = data.data.level;
                        var toId = data.data.toId;
                        var toNickName = data.data.toNickName;
                        toChat(toGroupId, level, toId, toNickName);
                        if (toGroupId == 1) {
                            onSendC2CMsg('yk-' + toId, 1);
                        } else if (toGroupId == 5) {
                            onSendC2CMsg('vip-' + toId, 1);
                        }
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
            adminC2CMessageForm.ajaxSubmit(options);
        }
    });


    var persionC2CMessageForm = $('#persionC2CMessageForm');
    persionC2CMessageForm.validate({
        submitHandler: function () {
            //校验参数
            if ($('#waterPersionContent').val() == '') {
                alert('请输入聊天内容 .');
                return;
            }

            formSubmit("#submitBtn", true, "保存中...");
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        //封装参数, 刷新聊天窗口
                        var toGroupId = $('#persionToGroupId').val();
                        var level = data.data.level;
                        var toId = data.data.toId;
                        var toNickName = data.data.toNickName;
                        getMessage(toGroupId, userInfo.id, toId);
                        onSendC2CMsg(toId, 0);
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
            persionC2CMessageForm.ajaxSubmit(options);
        }
    });


    //关闭按钮
    $('.close1').on('click', function () {
        talkUserId = '';
        $('.waterDrop').addClass('hide');
        //关闭清空列表和消息
        $('.waterUserList').html("");
        $('.waterChatMessageList').html("");
    });


    //点击水滴按钮, 加载水滴页面
    $('.waterF').on('click', function () {
        $('.waterListF').removeClass('hide');
        $('.newMsgtipDiv').addClass('hide');
        getWaterGroup();
    });


    $('.table2').on("click", "li .user_btn", function () {
        if ($(this).hasClass('cur')) {
            $(this).removeClass("cur");
            $(this).next().hide();
        } else {
            $(".table2").find('.user_btn').removeClass("cur");
            $(".table2").find('.user_text').hide();
            $(this).addClass("cur");
            $(this).next().show();
        }
    });


    //刷新列表
    $('.refreshContact').on('click', function () {
        getWaterGroup();
    });
});


//获取水滴分组
function getWaterGroup() {
    var jqxhr = $.ajax({
        url: ctx + "/live/getWaterGroup",
        data: {userId: userInfo.id}
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            var htmls = '';
            var record = '<li id="yk-contract"  data-id="contract" onclick="showGroup(this)"><div class="Grouping"><i class="down"></i>';
            	record += '<span>最近联系人</span>';
            	record += '</div><table class="user_list"><tbody></tbody></table></li>';
            htmls += record;
            for (var i = 0; i < data.data.length; i++) {
                var record = '<li id="yk-' + data.data[i].waterGroupId + '" data-id="' + data.data[i].waterGroupId + '" onclick="showGroup(this)"><div class="Grouping"><i class="down" ></i>';
                record += '<span data-id="' + data.data[i].waterGroupId + '" >' + data.data[i].waterGroupName + '</span>';
                if (data.data[i].waterGroupName != '未分组用户') {
                    record += '<a class="editGrouping pr15" onclick="editGrouping(this)"></a>';
                }
                // record += '<div class="GroupingBtns hide"><a data-id="' + data.data[i].waterGroupId + '" onclick="send(this)">发送本组</a>';
                record += '<div class="GroupingBtns hide">';
                record += '<a data-id="' + data.data[i].waterGroupId + '" data-name="' + data.data[i].waterGroupName + '" onclick="edit(this)">修改</a>';
                record += '<a data-id="' + data.data[i].waterGroupId + '" onclick="del(this)">删除</a>';
                record += '</div></div></li>';
                htmls += record;
            }
            $('.waterGroup').html(htmls);
        } else {
            popLayer(data.message);
        }
    });
}



function showContract(){
	if(contarctMsglist.length == 0){
		return false;
	}
	$('#yk-contract table tbody').empty();
	var htmls = '';
	 for (var i = contarctMsglist.length - 1 ; i >= 0 ; i--) {
	    	var data = contarctMsglist[i];
	    	data.level = 0;
	        var record = '<tr class="cur" style="cursor: pointer">';
	        record += '<td onclick="toChat(' + data.groupId + ', ' + data.level + ', ' + data.postUid + ', /' + data.postNickName + '/)">'
	        record += data.postNickName + '</td>';
	        record += '<td onclick="toChat(' + data.groupId + ', ' + data.level + ', ' + data.postUid + ', /' + data.postNickName + '/)">在线</td><td onclick="toChat(' + data.groupId + ', ' + data.level + ', ' + data.postUid + ', /' + data.postNickName + '/)">';
	        record += '</td></tr>'
	        htmls += record;
	 }
    $('#yk-contract table tbody').append(htmls);
}

//列表成员
function showGroup(obj) {
	var $grouping = $(obj).find('.Grouping').eq(0);
    if($grouping.hasClass('cur')) {
    	$grouping.removeClass("cur");
    	$(obj).find('table').eq(0).hide();
	}else{
		$grouping.removeClass("cur");
	  //$(obj).find('.user_list').hide();
		$grouping.addClass("cur");
		$(obj).find('table').eq(0).show();
	}
    var waterGroupId = $(obj).data("id");
    var userId = userInfo.id;
    if(waterGroupId  == 'contract'){
    	showContract();
    	return false;
    }

    var jqxhr = $.ajax({
        url: ctx + "/live/getContactPersion",
        data: {waterGroupId: waterGroupId, relationUserId: userId}
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            console.log(data);
            var $table = $('#yk-' + waterGroupId).find('table tobdy').eq(0);
            $table.empty();
            var htmls = '';
            for (var i = 0; i < data.data.length; i++) {
                var record = '<tr class="cur" style="cursor: pointer">';
                record += '<td onclick="toChat(' + data.data[i].groupId + ', ' + data.data[i].level + ', ' + data.data[i].userId + ', /' + data.data[i].userNickName + '/)">'
                record += getCount(data.data[i].count, data.data[i].userId);
                record += data.data[i].userNickName + '</td>';
                record += '<td onclick="toChat(' + data.data[i].groupId + ', ' + data.data[i].level + ', ' + data.data[i].userId + ', /' + data.data[i].userNickName + '/)">在线</td><td onclick="toChat(' + data.data[i].groupId + ', ' + data.data[i].level + ', ' + data.data[i].userId + ', /' + data.data[i].userNickName + '/)">';
                record += getJoinFormatDate(data.data[i].joinTime);
                record += '</td><td><a class="editUserType" data-id="' + data.data[i].id + '"  data-wid="' + data.data[i].waterGroupId + '" onclick="editUserType(this)"> > </a>';
                record += '<div class="userTypeBtn hide"></div></td></tr>'
                htmls += record;
            }
            $table.append(htmls);
        } else {
            popLayer(data.message);
        }
    });
}

function getCount(count, toId) {
    if (count > 0) {
        return '<i id="tip-' + toId + '" class="newMsgtip1">' + count + '</i>'
    } else {
        return '';
    }
}

function send() {

}

function edit(obj) {
    $(obj).parent().hide()
    $('.addGroupMask').show();

    var id = $(obj).data("id");
    var name = $(obj).data("name");
    $('#waterGroupId').val(id);
    $('#waterGroupName').val(name);
}

//删除分组
function del(obj) {
    var id = $(obj).data("id");
    var jqxhr = $.ajax({
        url: ctx + "/live/delWaterGroup",
        data: {waterGroupId: id}
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            getWaterGroup();
        } else {
            popLayer(data.message);
        }
    });
}


//选择一个人聊天
function toChat(toGroupId, level, toId, name) {
    talkUserId = toId;

    $('#tip-' + toId).hide();

    var name = name.toString().split('/').join('');
    //聊天窗口显示信息
    var fromId = userInfo.id;

    //form表单提交参数
    $('#waterToId').val(toId);
    $('#waterToName').val(name);
    $('#toGroupId').val(toGroupId);

    $('.waterChat').removeClass('hide');
    $('.toName').html('与' + name + '的对话<i></i>');

    //聊天消息列表
    var jqxhr = $.ajax({
        url: ctx + "/live/getWaterChatMessage",
        data: {'roomId': roomId, 'toGroupId': toGroupId, 'fromGroupId': groupId, 'fromId': fromId, 'toId': toId},
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            var htmls = getHtmls(groupId, data);
            $('.waterChatMessageList').html(htmls);
            $('#waterAdminChatBox').scrollTop($('#waterAdminChatBox')[0].scrollHeight);//滚动条到最底部
        }
    });
    event.stopPropagation();
}

/**
 * 用户获取消息
 */
function getMessage(toGroupId, fromId, toId) {
    var jqxhr = $.ajax({
        url: ctx + "/live/getWaterChatMessage",
        data: {'roomId': roomId, 'toGroupId': toGroupId, 'fromGroupId': groupId, 'fromId': fromId, 'toId': toId},
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            var htmls = $('.messageTip').html();
            htmls += getHtmls(groupId, data);
            $('.waterPersionChatMessageList').html(htmls);
            $('#waterPersionChatBox').scrollTop($('#waterPersionChatBox')[0].scrollHeight);//滚动条到最底部
        }
    });
}


//组装html
function getHtmls(groupId, data) {
    var htmls = '';
    for (var i = 0; i < data.data.length; i++) {
        if (groupId != data.data[i].groupId) {
            htmls += '<div class="text01 clearfix"><div class="user_img">';
            htmls += getIcon(data.data[i].groupId, data.data[i].level);
            htmls += '<span>' + data.data[i].fromNickName + '</span>';
            htmls += '<strong>' + getNowFormatDate(data.data[i].createTime) + '</strong></div><div class="txt">';
            htmls += '<p>' + data.data[i].content + '</p>';
            htmls += '</div></div>';
        } else {
            htmls += '<div class="text02 clearfix"><div class="user_img">';
            htmls += getIcon(data.data[i].groupId, data.data[i].level);
            htmls += '<strong>' + getNowFormatDate(data.data[i].createTime) + '</strong>';
            htmls += '<span>' + data.data[i].fromNickName + '</span></div><div class="txt">';
            htmls += '<p>' + data.data[i].content + '</p>';
            htmls += '</div></div>';
        }
    }
    return htmls;
}


function getIcon(groupId, level) {
    var htmls = '';
    //处理图标
    if (groupId == 1) {//游客
        htmls += '<span class="ac yk"></span>';
    } else if (groupId == 3) {//助理
        htmls += '<span class="ac zl"></span>';
    } else if (groupId == 4) {
        htmls += '<span class="ac vip5"></span>';
    } else if (groupId == 5) {//vip
        if (typeof (level) != 'undefined') {
            if (level == 1) {
                htmls += '<span class="ac vip1"></span>';
            } else if (level == 2) {
                htmls += '<span class="ac vip2"></span>';
            } else if (level == 3) {
                htmls += '<span class="ac vip3"></span>';
            } else if (level == 4) {
                htmls += '<span class="ac vip4"></span>';
            } else if (level == 5) {
                htmls += '<span class="ac vip5"></span>';
            } else if (level == 6) {
                htmls += '<span class="ac vip6"></span>';
            } else if (level == 7) {
                htmls += '<span class="ac vip7"></span>';
            } else if (level == 8) {
                htmls += '<span class="ac vip8"></span>';
            }
        }
    }
    return htmls;
}

//格式化时间
function getJoinFormatDate(date) {
    var str = date.substring(5, 19);
    return str;
}

//格式化是否在线
function isOnline(status) {
    if (status == 1) {
        return '在线';
    } else {
        return '不在线';
    }
}

function editGrouping(edit) {
    $(edit).next().toggle();
}


function editUserType(obj) {
    var rid = $(obj).data("id");//关系表主键
    var waterGroupId = $(obj).data("wid");//关系表分组id
    var jqxhr = $.ajax({
        url: ctx + "/live/getWaterGroup",
        data: {userId: userInfo.id}
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            var htmls = '';
            for (var i = 0; i < data.data.length; i++) {
                if (waterGroupId != data.data[i].waterGroupId) {
                    var record = '<a data-rid="' + rid + '" data-wid="' + data.data[i].waterGroupId + '"  onclick="selectGroup(this)">' + data.data[i].waterGroupName + '</a>';
                    htmls += record;
                }
            }
            $(obj).next().html(htmls);
            $(obj).next().toggle();
        }
    });
}

/**
 * 重新分组
 * @param obj
 */
function selectGroup(obj) {
    var rid = $(obj).data("rid");
    var waterGroupId = $(obj).data("wid");
    var jqxhr = $.ajax({
        url: ctx + "/live/selectGroup",
        data: {waterGroupId: waterGroupId, id: rid}
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            getWaterGroup();
        }
    });
}

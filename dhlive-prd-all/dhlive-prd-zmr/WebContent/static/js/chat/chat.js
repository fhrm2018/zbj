function chatInit() {
    var jqxhr = $.ajax({
        url: ctx + '/live/getChatToken',
        data: {"groupId": userInfo.groupId}
    });
    jqxhr.done(function (data) {
        if (data.code != '1000') {
            popWinTips(3, data.message);
        } else {
            loginInfo.userSig = data.data;
            sdkLogin();
        }
    });
    jqxhr.fail(function (data) {
        popWinTips(3, stringMsg.serverErr);
    });
}
$(function () {
    var imgLoading = function (src) {
        var src = src;
        var img = new Image();
        img.src = src;
        img.onload = function () {
            $('#msgBox').scrollTop($('#msgBox')[0].scrollHeight);
        };
    };

    var checkImg = function (objs) {
        for (var i = 0; i < objs.length; i++) {
            imgLoading(objs[i]);
        }
    };

    var checkTxt = function(){
        $('#msgBox').scrollTop($('#msgBox')[0].scrollHeight);
    }


    var $msgBox = $('#msgBox');
    if (loginInfo.userSig == '') {
        chatInit();
    }
    if ($msgBox) {
        $msgBox.find('.load').addClass('hide');
        $msgBox.empty();
        var loadMsgUrl = ctx + '/room/getChatMessageByUser';
        if (isAdmin == 1) {
            loadMsgUrl = ctx + '/room/getChatMessageByAdmin';
        }
        var jqxhr = $.ajax({
            url: loadMsgUrl,
            data: {roomId: roomId}
        });
        jqxhr.done(function (data) {
            if (data.code == '1000') {
                var msgList = data.data;
                var htmls = '';
                if (msgList && msgList.length > 0) {
                    for (var i = 0; i < msgList.length; i++) {
                        var msg = getMsgFromContent(msgList[i].content);
                        msg.status = msgList[i].status;
                        msg.isDelete = msgList[i].isDelete;
                        msg.sendTime = msgList[i].sendTime;
                        if (msg) {
                            var cmdObj = getCmdFromMsg(msg);
                            var isImg = cmdObj.isImg;//1表示此消息是图片
                            if (isImg == 1) {
                                chatImgs[i] = cmdObj.chatImgUrl;
                                htmls = htmls + getRoomMsgHtml(msg);
                            } else {
                                htmls = htmls + getRoomMsgHtml(msg);
                            }
                        }
                    }
                }
                if (htmls != '') {
                    $msgBox.append(htmls);
                    checkImg(chatImgs);
                    checkTxt();
                }
            }
        });
    }
});
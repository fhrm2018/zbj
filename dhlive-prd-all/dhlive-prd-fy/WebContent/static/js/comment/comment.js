var allCommentPage = 1;
var getAllCommentReLoad = true;
var myCommentPage = 1;
var getMyCommentReLoad = true;
$(function () {
    /****查询所有留言***/
    selAllComment(1);

    /*********我的留言和回复***********/
    //selMyComment(1);


    /******保存回复*******/
    $("#commentSend").click(function (e) {
        if (userInfo.groupId == 1) {
            alert("请先登录！");
            return;
        }
        var content = $("#commentInp").val();
        if (content == '') {
            alert("请输入内容！");
            return false;
        }
        var expr = /\[[^[\]]{1,3}\]/mg;
        var emotions = content.match(expr);
        var text_obj, face_obj, tmsg, emotionIndex, emotion, restMsgIndex;
        if (!emotions || emotions.length < 1) {

        } else {//有表情
            console.info(convertTextMsgToHtml(emotions));
        }
        var jqxhr = $.ajax({
            url: ctx + "/room/saveMessageBoard",
            data: {roomId: roomId, userId: userInfo.id, messageContent: content},
        });
        jqxhr.done(function (data) {
            if (data.code == '1000') {
                // $("#allCommentCon").prepend(appendComment(userInfo.nickName, content, '刚刚'));
                // $("#myCommentCon").prepend(appendComment(userInfo.nickName, content, '刚刚'));
                $("#commentInp").val('');
            }
        });

    });
    /**所有留言的滚动加载*/
    $("#allCommentCon").endlessScroll({
        bottomPixels: 10,
        fireDelay: 10,
        callback: function (p) {
            if (getAllCommentReLoad) {
                getAllCommentMore();
            }
        }
    });
    /**我的留言滚动加载*/
    $("#myCommentCon").endlessScroll({
        bottomPixels: 10,
        fireDelay: 10,
        callback: function (p) {
            if (getMyCommentReLoad) {
                getMyCommentMore();
            }
        }
    });

});

function getAllCommentMore() {
    allCommentPage++;
    selAllComment(allCommentPage);
}
function getMyCommentMore() {
    myCommentPage++;
    // selMyComment(myCommentPage);
}
/****查询所有留言***/
function selAllComment(page) {
    if (getAllCommentReLoad) {
        var jqxhr = $.ajax({
            url: ctx + "/room/getMessageBoardByUser",
            data: {page: page, rows: 10, roomId: roomId},
        });
        jqxhr.done(function (data) {
            if (data.code == '1000') {
                var msgList = data.data;
                if (msgList && msgList.length > 0) {
                    getCommentHtml(msgList, 'allCommentCon');
                }
                if (msgList.length < 10) {
                    getAllCommentReLoad = false;
                }
            }
        });
    }

}
/*********我的留言和回复***********/
function selMyComment(page) {
    if (getMyCommentReLoad) {
        var jqxhr2 = $.ajax({
            url: ctx + "/room/getPersionAndResp",
            data: {page: page, rows: 10, userId: userInfo.id, roomId: roomId},
        });
        jqxhr2.done(function (data) {
            if (data.code == '1000') {
                var msgList = data.data;
                if (msgList && msgList.length > 0) {
                    getCommentHtml(msgList, 'myCommentCon');
                }
                if (msgList.length < 10) {
                    getMyCommentReLoad = false;
                }
            }
        });
    }
}


/***得到留言list***/
function getCommentHtml(msgList, appendName) {
    var htmls = '';
    for (var i = 0; i < msgList.length; i++) {
        var roomMes = msgList[i].roomMessageBoard;
        var userId = roomMes.userId;
        var userName = roomMes.userName;
        var messageTime = roomMes.messageTime;
        var messageContent = roomMes.messageContent;
        var childs = msgList[i].childs;
        var html2 = '';
        if (childs && childs.length > 0) {
            html2 = appendCReply();
            for (var j = 0; j < childs.length; j++) {
                var userId2 = childs[j].userId;
                var userName2 = childs[j].userName;
                var messageTime2 = childs[j].messageTime;
                var messageContent2 = childs[j].messageContent;
                if (userId2 != userId) {
                    html2 += appendCReply1(userName2, messageContent2);
                } else {
                    html2 += appendCReply2(userName, userName2, messageContent2);
                }
            }
            html2 += appendCReplyClose();
        }
        htmls += appendComment(userName, messageContent, messageTime, html2);
    }
    $("#" + appendName).append(htmls);
}

/****拼接留言****/
function appendComment(username, content, time, isChild) {
    var html = '<div class="commentInfo posRel">';
    html += '<div class="userComment">';
    html += '<div class="fl">';
    html += '<img src="../static/images/message.png">';
    html += '<span class="nickName ml10 fz14">';
    html += username;
    html += "</span>";
    html += '</div>';
    html += '<div class="fr">';
    html += '<span class="commentTime fz12">';
    html += time;
    html += '</span></div>';
    html += '</div>';
    html += '<div class="cContent">';
    html += content;
    html += '</div>';
    if (isChild && isChild.length > 0) {
        html += isChild;
    }
    html += '</div>';
    return html;
}

/****拼接留言回复****/
function appendCReply() {
    var html = '<div class="replyCon">';
    return html;
}
/****拼接留言回复4****/
function appendCReplyClose() {
    var html = '</div>';
    return html;
}
/****拼接留言回复1****/
function appendCReply1(username1, content) {
    var html = '<div class="reply1 pl32 pt15 pl40">';
    html += username1 + ": " + content;
    html += '</div>';
    return html;
}
/****拼接留言回复2****/
function appendCReply2(username1, username2, content) {
    var html = '<div class="reply2 pl35  pt15">';
    html += '<span class="">';
    html += username1 + '</span>';
    html += '<span class="reply mr5 ml5">回复</span>';
    html += '<span class="mr5">';
    html += username2 + '</span>';
    html += content;
    html += '</div>';
    return html;
} 





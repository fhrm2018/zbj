var send_msg_text_id = 'sendMsgIpt',
    room_msg_box_id = 'msgBox';
//其他对象，选填
var options = {
    //'isAccessFormalEnv': isAccessFormalEnv,//是否访问正式环境，默认访问正式，选填
    'isLogOn': false//是否开启控制台打印日志,默认开启，选填
};
//监听（多终端同步）群系统消息方法，方法都定义在demo_group_notice.js文件中
//注意每个数字代表的含义，比如，
//1表示监听申请加群消息，2表示监听申请加群被同意消息，3表示监听申请加群被拒绝消息等
var onGroupSystemNotifys = {
    //"1": onApplyJoinGroupRequestNotify, //申请加群请求（只有管理员会收到,暂不支持）
    //"2": onApplyJoinGroupAcceptNotify, //申请加群被同意（只有申请人能够收到,暂不支持）
    //"3": onApplyJoinGroupRefuseNotify, //申请加群被拒绝（只有申请人能够收到,暂不支持）
    //"4": onKickedGroupNotify, //被管理员踢出群(只有被踢者接收到,暂不支持)
    // "5": onDestoryGroupNotify, //群被解散(全员接收)
    //"6": onCreateGroupNotify, //创建群(创建者接收,暂不支持)
    //"7": onInvitedJoinGroupNotify, //邀请加群(被邀请者接收,暂不支持)
    //"8": onQuitGroupNotify, //主动退群(主动退出者接收,暂不支持)
    //"9": onSetedGroupAdminNotify, //设置管理员(被设置者接收,暂不支持)
    //"10": onCanceledGroupAdminNotify, //取消管理员(被取消者接收,暂不支持)
    // "11": onRevokeGroupNotify, //群已被回收(全员接收)
    // "255": onCustomGroupNotify//用户自定义通知(默认全员接收)
};
//监听连接状态回调变化事件
//监听事件
var listeners = {
    "onConnNotify": onConnNotify, //选填
    "jsonpCallback": jsonpCallback, //IE9(含)以下浏览器用到的jsonp回调函数,移动端可不填，pc端必填
    "onBigGroupMsgNotify": onBigGroupMsgNotify, //监听新消息(大群)事件，必填
    "onMsgNotify": onMsgNotify,//监听新消息(私聊(包括普通消息和全员推送消息)，普通群(非直播聊天室)消息)事件，必填
    "onGroupSystemNotifys": onGroupSystemNotifys, //监听（多终端同步）群系统消息事件，必填
    "onGroupInfoChangeNotify": onGroupInfoChangeNotify//监听群资料变化事件，选填
};
var onConnNotify = function (resp) {
    switch (resp.ErrorCode) {
        case webim.CONNECTION_STATUS.ON:
            //webim.Log.warn('连接状态正常...');
            break;
        case webim.CONNECTION_STATUS.OFF:
            webim.Log.warn('连接已断开，无法收到新消息，请检查下你的网络是否正常');
            break;
        default:
            webim.Log.error('未知连接状态,status=' + resp.ErrorCode);
            break;
    }
};

//IE9(含)以下浏览器用到的jsonp回调函数
function jsonpCallback(rspData) {
    //设置接口返回的数据
    webim.setJsonpLastRspData(rspData);
}

//监听大群新消息（普通，点赞，提示，红包）
function onBigGroupMsgNotify(msgList) {
    for (var i = msgList.length - 1; i >= 0; i--) { //遍历消息，按照时间从后往前
        var msg = msgList[i];
        //console.warn(msg);
        webim.Log.warn('receive a new avchatroom group msg: ' + msg.getFromAccountNick());
        //显示收到的消息
        showMsg(msg);
    }
}

//监听新消息(私聊(包括普通消息、全员推送消息)，普通群(非直播聊天室)消息)事件
//newMsgList 为新消息数组，结构为[Msg]
function onMsgNotify(newMsgList) {
    var newMsg;
    for (var j in newMsgList) { //遍历新消息
        newMsg = newMsgList[j];
        handlderMsg(newMsg); //处理新消息
    }
}

//处理消息（私聊(包括普通消息和全员推送消息)，普通群(非直播聊天室)消息）
function handlderMsg(msg) {
    var fromAccount, fromAccountNick, sessType, subType, contentHtml;
    fromAccount = msg.getFromAccount();
    if (!fromAccount) {
        fromAccount = '';
    }
    fromAccountNick = msg.getFromAccountNick();
    if (!fromAccountNick) {
        fromAccountNick = fromAccount;
    }

    //解析消息
    //获取会话类型
    //webim.SESSION_TYPE.GROUP-群聊，
    //webim.SESSION_TYPE.C2C-私聊，
    sessType = msg.getSession().type();
    //获取消息子类型
    //会话类型为群聊时，子类型为：webim.GROUP_MSG_SUB_TYPE
    //会话类型为私聊时，子类型为：webim.C2C_MSG_SUB_TYPE
    subType = msg.getSubType();
    switch (sessType) {
        case webim.SESSION_TYPE.C2C: //私聊消息
            switch (subType) {
                case webim.C2C_MSG_SUB_TYPE.COMMON: //c2c普通消息
                    //业务可以根据发送者帐号fromAccount是否为app管理员帐号，来判断c2c消息是否为全员推送消息，还是普通好友消息
                    //或者业务在发送全员推送消息时，发送自定义类型(webim.MSG_ELEMENT_TYPE.CUSTOM,即TIMCustomElem)的消息，在里面增加一个字段来标识消息是否为推送消息
                    contentHtml = convertMsgtoHtml(msg);
                    webim.Log.warn('receive a new c2c msg: fromAccountNick=' + fromAccountNick + ", content=" + contentHtml);
                    //c2c消息一定要调用已读上报接口
                    var opts = {
                        'To_Account': fromAccount, //好友帐号
                        'LastedMsgTime': msg.getTime() //消息时间戳
                    };
                    webim.c2CMsgReaded(opts);
                    //alert('收到一条c2c消息(好友消息或者全员推送消息): 发送人=' + fromAccountNick + ", 内容=" + contentHtml);
                    var data = msg.elems[0].content.data;
                    //拉黑通知
                    if (data == 2001) {
                        window.location.href = ctx + '/error/404?roomId=' + roomId + '&userId=' + userInfo.id + '&groupId=' + userInfo.groupId;
                    }

                    if (data == 4001) {
                        if (userInfo.groupId == 1 || userInfo.groupId == 5) {
                            var obj = JSON.parse(msg.elems[0].content.ext)
                            var data = {};
                            data.postUid = obj.fromId;
                            data.postNickName = obj.fromNickName;
                            data.groupId = obj.groupId;
                            data.flag = "yk-" + obj.toId;
                            data.content = obj.content;
                            showC2CMsg(JSON.stringify(data));
                            return;
                        }
                    }
                    //c2c消息提醒
                    showC2CMsg(data);
                    break;
            }
            break;
        case webim.SESSION_TYPE.GROUP: //普通群消息，对于直播聊天室场景，不需要作处理
            break;
    }
}

//sdk登录
function sdkLogin() {
    //web sdk 登录
    webim.login(loginInfo, listeners, options,
        function (identifierNick) {
            //identifierNick为登录用户昵称(没有设置时，为帐号)，无登录态时为空
            webim.Log.info('webim登录成功');
            applyJoinBigGroup(chatRoomId); //加入大群
            //hideDiscussForm(); //隐藏评论表单
            initEmotionUL(); //初始化表情
            initEmotionUL2();
        },
        function (err) {
            alert(err.ErrorInfo);
        }
    ); //
}

//进入大群

function applyJoinBigGroup(groupId) {
    var options = {
        'GroupId': groupId //群id
    };
    webim.applyJoinBigGroup(
        options,
        function (resp) {
            //JoinedSuccess:加入成功; WaitAdminApproval:等待管理员审批
            if (resp.JoinedStatus && resp.JoinedStatus == 'JoinedSuccess') {
                webim.Log.info('进群成功');
                selToID = groupId;
            } else {
                alert('进群失败');
            }
        },
        function (err) {
            alert(err.ErrorInfo);
        }
    );
}

function zpWidth() {
    var flowerWidth = $(window).width();
    if (flowerWidth <= 1600) {
        $('.flowerFly').css("width", "98%");
        $('.flowerFly span').css("padding", "0");
    } else {
        $('.flowerFly').css("width", "93%");
        $('.flowerFly span').css("padding", "0 20px 0 0");
    }
}

//浏览器大小改变, 触发事件
window.onresize = function () {
    zpWidth();
};

//显示消息（群普通+点赞+提示+红包）
function showMsg(msg) {
    var cmdObj = getCmdFromMsg(msg);
    if (!cmdObj) {
        return false;
    }
    //审核通过后隐藏通过按钮
    if (cmdObj.checkStatus) {
        $('#msginfo-' + cmdObj.uniqueId).find('.pass').addClass('hide');
    }
    if (cmdObj.code == '0000') {
        var htmls = getRoomMsgHtml(msg, cmdObj);
        var $msgBox = $('#' + room_msg_box_id);
        $msgBox.append(htmls);
    } else if (cmdObj.code == '0001') {//删除消息
        removeMsgInfo(cmdObj.uniqueId, cmdObj.sendUid);
    } else if (cmdObj.code == '1002') {//公告
        var text = cmdObj.text;
        alert(text);
    } else if (cmdObj.code == '1003') {//拉黑

    } else if (cmdObj.code == '9000') {//红包消息
        $('.showBigMsg').append('<div class="flowerFly">  <span>' + cmdObj.name + ' 送出了</span> <img src="../static/images/flower2.png" alt="">  </div>');
        $(".flowerFly").animate({
            top: '-10px',
            opacity: '0'
        }, 8000);
    } else if (cmdObj.code == '9001') {//玫瑰消息
        $('.showBigMsg').append('<div class="flowerFly">  <span>' + cmdObj.name + ' 送出了</span> <img src="../static/images/flower.png" alt="">  </div>');
        $(".flowerFly").animate({
            top: '-10px',
            opacity: '0'
        }, 8000);
    }

    var imgLoading = function (src) {
        var src = src;
        var img = new Image();
        img.src = src;
        img.onload = function () {
            $('#msgBox').scrollTop($('#msgBox')[0].scrollHeight);
        };
    };

    var isImg = cmdObj.isImg;//1表示此消息是图片
    if (isImg == 1) {
        imgLoading(cmdObj.chatImgUrl);
    } else {
        $('#msgBox').scrollTop($('#msgBox')[0].scrollHeight);
    }
}

//把消息转换成Html
function convertMsgtoHtml(msg) {
    var html = "", elems, elem, type, content;
    elems = msg.elems; //获取消息包含的元素数组
    for (var i in elems) {
        elem = elems[i];
        type = elem.type; //获取元素类型
        content = elem.content; //获取元素对象
        switch (type) {
            case webim.MSG_ELEMENT_TYPE.TEXT:
                html += convertTextMsgToHtml(content);
                break;
            case webim.MSG_ELEMENT_TYPE.FACE:
                html += convertFaceMsgToHtml(content);
                break;
            case webim.MSG_ELEMENT_TYPE.IMAGE:
                html += convertImageMsgToHtml(content);
                break;
            case webim.MSG_ELEMENT_TYPE.SOUND:
                html += convertSoundMsgToHtml(content);
                break;
            case webim.MSG_ELEMENT_TYPE.FILE:
                html += convertFileMsgToHtml(content);
                break;
            case webim.MSG_ELEMENT_TYPE.LOCATION: //暂不支持地理位置
                //html += convertLocationMsgToHtml(content);
                break;
            case webim.MSG_ELEMENT_TYPE.CUSTOM:
                //html += convertCustomMsgToHtml(content);
                break;
            case webim.MSG_ELEMENT_TYPE.GROUP_TIP:
                html += convertGroupTipMsgToHtml(content);
                break;
            default:
                webim.Log.error('未知消息元素类型: elemType=' + type);
                break;
        }
    }

    var cmdObj = getCmdFromMsg(msg);
    if (cmdObj.groupId == 3) {
        if (cmdObj.isAt == 1) {
            var text = html.split(" ");
            var other = "";
            for (var i = 1; i < text.length; i++) {
                other += " " + text[i];
            }
            return webim.Tool.formatHtml2Text("<image src='../static/images/whosaid.png' /><span style='color:#000000' class='msgName greenFont' >" + text[0] + "</span><span style='display: inline-block; color: red;font-weight: 700;font-size:16px;border-bottom-left-radius: 10px;border-top-right-radius: 10px;border-bottom-right-radius: 10px;padding: 3px 10px;'> " + other + "</span>");
        } else {
            return webim.Tool.formatHtml2Text("<span style='color:#e40d0b;font-weight:bold'>" + html + "</span>");
        }
    } else if (cmdObj.groupId == 4) {
        return webim.Tool.formatHtml2Text("<span style='color:#e40d0b;font-weight:bold;'>" + html + "</span>");
    } else {
        return webim.Tool.formatHtml2Text("<span>" + html + "</span>");
    }
}

//解析文本消息元素
function convertTextMsgToHtml(content) {
    return content.text;
}

//解析表情消息元素
function convertFaceMsgToHtml(content) {
    var faceUrl = null;
    var data = content.data;
    var index = webim.EmotionDataIndexs[data];

    var emotion = webim.Emotions[index];
    if (emotion && emotion[1]) {
        faceUrl = emotion[1];
    }
    if (faceUrl) {
        return "<img src='" + faceUrl + "'/>";
    } else {
        return data;
    }
}

//解析图片消息元素
function convertImageMsgToHtml(content) {
    var smallImage = content.getImage(webim.IMAGE_TYPE.SMALL); //小图
    var bigImage = content.getImage(webim.IMAGE_TYPE.LARGE); //大图
    var oriImage = content.getImage(webim.IMAGE_TYPE.ORIGIN); //原图
    if (!bigImage) {
        bigImage = smallImage;
    }
    if (!oriImage) {
        oriImage = smallImage;
    }
    return "<img src='" + smallImage.getUrl() + "#" + bigImage.getUrl() + "#" + oriImage.getUrl() + "' style='CURSOR: hand' id='" + content.getImageId() + "' bigImgUrl='" + bigImage.getUrl() + "' onclick='imageClick(this)' />";
}

//解析语音消息元素
function convertSoundMsgToHtml(content) {
    var second = content.getSecond(); //获取语音时长
    var downUrl = content.getDownUrl();
    if (webim.BROWSER_INFO.type == 'ie' && parseInt(webim.BROWSER_INFO.ver) <= 8) {
        return '[这是一条语音消息]demo暂不支持ie8(含)以下浏览器播放语音,语音URL:' + downUrl;
    }
    return '<audio src="' + downUrl + '" controls="controls" onplay="onChangePlayAudio(this)" preload="none"></audio>';
}

//解析文件消息元素
function convertFileMsgToHtml(content) {
    var fileSize = Math.round(content.getSize() / 1024);
    return '<a href="' + content.getDownUrl() + '" title="点击下载文件" ><i class="glyphicon glyphicon-file">&nbsp;' + content.getName() + '(' + fileSize + 'KB)</i></a>';

}

//解析位置消息元素
function convertLocationMsgToHtml(content) {
    return '经度=' + content.getLongitude() + ',纬度=' + content.getLatitude() + ',描述=' + content.getDesc();
}

//解析自定义消息元素
function convertCustomMsgToHtml(content) {
    var data = content.getData();
    var desc = content.getDesc();
    var ext = content.getExt();
    return "data=" + data + ", desc=" + desc + ", ext=" + ext;
}

//解析群提示消息元素
function convertGroupTipMsgToHtml(content) {
    var WEB_IM_GROUP_TIP_MAX_USER_COUNT = 10;
    var text = "";
    var maxIndex = WEB_IM_GROUP_TIP_MAX_USER_COUNT - 1;
    var opType, opUserId, userIdList;
    var memberCount;
    opType = content.getOpType(); //群提示消息类型（操作类型）
    opUserId = content.getOpUserId(); //操作人id
    switch (opType) {
        case webim.GROUP_TIP_TYPE.JOIN: //加入群
            userIdList = content.getUserIdList();
            //text += opUserId + "邀请了";
            for (var m in userIdList) {
                text += userIdList[m] + ",";
                if (userIdList.length > WEB_IM_GROUP_TIP_MAX_USER_COUNT && m == maxIndex) {
                    text += "等" + userIdList.length + "人";
                    break;
                }
            }
            text = text.substring(0, text.length - 1);
            text += "进入房间";
            //房间成员数加1
            memberCount = $('#user-icon-fans').html();
            $('#user-icon-fans').html(parseInt(memberCount) + 1);
            break;
        case webim.GROUP_TIP_TYPE.QUIT: //退出群
            text += opUserId + "离开房间";
            //房间成员数减1
            memberCount = parseInt($('#user-icon-fans').html());
            if (memberCount > 0) {
                $('#user-icon-fans').html(memberCount - 1);
            }
            break;
        case webim.GROUP_TIP_TYPE.KICK: //踢出群
            text += opUserId + "将";
            userIdList = content.getUserIdList();
            for (var m in userIdList) {
                text += userIdList[m] + ",";
                if (userIdList.length > WEB_IM_GROUP_TIP_MAX_USER_COUNT && m == maxIndex) {
                    text += "等" + userIdList.length + "人";
                    break;
                }
            }
            text += "踢出该群";
            break;
        case webim.GROUP_TIP_TYPE.SET_ADMIN: //设置管理员
            text += opUserId + "将";
            userIdList = content.getUserIdList();
            for (var m in userIdList) {
                text += userIdList[m] + ",";
                if (userIdList.length > WEB_IM_GROUP_TIP_MAX_USER_COUNT && m == maxIndex) {
                    text += "等" + userIdList.length + "人";
                    break;
                }
            }
            text += "设为管理员";
            break;
        case webim.GROUP_TIP_TYPE.CANCEL_ADMIN: //取消管理员
            text += opUserId + "取消";
            userIdList = content.getUserIdList();
            for (var m in userIdList) {
                text += userIdList[m] + ",";
                if (userIdList.length > WEB_IM_GROUP_TIP_MAX_USER_COUNT && m == maxIndex) {
                    text += "等" + userIdList.length + "人";
                    break;
                }
            }
            text += "的管理员资格";
            break;

        case webim.GROUP_TIP_TYPE.MODIFY_GROUP_INFO: //群资料变更
            text += opUserId + "修改了群资料：";
            var groupInfoList = content.getGroupInfoList();
            var type, value;
            for (var m in groupInfoList) {
                type = groupInfoList[m].getType();
                value = groupInfoList[m].getValue();
                switch (type) {
                    case webim.GROUP_TIP_MODIFY_GROUP_INFO_TYPE.FACE_URL:
                        text += "群头像为" + value + "; ";
                        break;
                    case webim.GROUP_TIP_MODIFY_GROUP_INFO_TYPE.NAME:
                        text += "群名称为" + value + "; ";
                        break;
                    case webim.GROUP_TIP_MODIFY_GROUP_INFO_TYPE.OWNER:
                        text += "群主为" + value + "; ";
                        break;
                    case webim.GROUP_TIP_MODIFY_GROUP_INFO_TYPE.NOTIFICATION:
                        text += "群公告为" + value + "; ";
                        break;
                    case webim.GROUP_TIP_MODIFY_GROUP_INFO_TYPE.INTRODUCTION:
                        text += "群简介为" + value + "; ";
                        break;
                    default:
                        text += "未知信息为:type=" + type + ",value=" + value + "; ";
                        break;
                }
            }
            break;

        case webim.GROUP_TIP_TYPE.MODIFY_MEMBER_INFO: //群成员资料变更(禁言时间)
            text += opUserId + "修改了群成员资料:";
            var memberInfoList = content.getMemberInfoList();
            var userId, shutupTime;
            for (var m in memberInfoList) {
                userId = memberInfoList[m].getUserId();
                shutupTime = memberInfoList[m].getShutupTime();
                text += userId + ": ";
                if (shutupTime != null && shutupTime !== undefined) {
                    if (shutupTime == 0) {
                        text += "取消禁言; ";
                    } else {
                        text += "禁言" + shutupTime + "秒; ";
                    }
                } else {
                    text += " shutupTime为空";
                }
                if (memberInfoList.length > WEB_IM_GROUP_TIP_MAX_USER_COUNT && m == maxIndex) {
                    text += "等" + memberInfoList.length + "人";
                    break;
                }
            }
            break;
        default:
            text += "未知群提示消息类型：type=" + opType;
            break;
    }
    return text;
}

//tls登录

function tlsLogin() {
    //跳转到TLS登录页面
    TLSHelper.goLogin({
        sdkappid: loginInfo.sdkAppID,
        acctype: loginInfo.accountType,
        url: window.location.href
    });
}

//第三方应用需要实现这个函数，并在这里拿到UserSig

function tlsGetUserSig(res) {
    //成功拿到凭证
    if (res.ErrorCode == webim.TLS_ERROR_CODE.OK) {
        //从当前URL中获取参数为identifier的值
        loginInfo.identifier = webim.Tool.getQueryString("identifier");
        //拿到正式身份凭证
        loginInfo.userSig = res.UserSig;
        //从当前URL中获取参数为sdkappid的值
        loginInfo.sdkAppID = loginInfo.appIDAt3rd = Number(webim.Tool.getQueryString("sdkappid"));
        //从cookie获取accountType
        var accountType = webim.Tool.getCookie('accountType');
        if (accountType) {
            loginInfo.accountType = accountType;
            sdkLogin(); //sdk登录
        } else {
            location.href = location.href.replace(/\?.*$/gi, "");
        }
    } else {
        //签名过期，需要重新登录
        if (res.ErrorCode == webim.TLS_ERROR_CODE.SIGNATURE_EXPIRATION) {
            tlsLogin();
        } else {
            alert("[" + res.ErrorCode + "]" + res.ErrorInfo);
        }
    }
}

//单击图片事件

function imageClick(imgObj) {
    var imgUrls = imgObj.src;
    var imgUrlArr = imgUrls.split("#"); //字符分割
    var smallImgUrl = imgUrlArr[0]; //小图
    var bigImgUrl = imgUrlArr[1]; //大图
    var oriImgUrl = imgUrlArr[2]; //原图
    webim.Log.info("小图url:" + smallImgUrl);
    webim.Log.info("大图url:" + bigImgUrl);
    webim.Log.info("原图url:" + oriImgUrl);
}


//切换播放audio对象

function onChangePlayAudio(obj) {
    if (curPlayAudio) { //如果正在播放语音
        if (curPlayAudio != obj) { //要播放的语音跟当前播放的语音不一样
            curPlayAudio.currentTime = 0;
            curPlayAudio.pause();
            curPlayAudio = obj;
        }
    } else {
        curPlayAudio = obj; //记录当前播放的语音
    }
}

//单击评论图片

function smsPicClick() {
    if (!loginInfo.identifier) { //未登录
        if (accountMode == 1) { //托管模式
            //将account_type保存到cookie中,有效期是1天
            webim.Tool.setCookie('accountType', loginInfo.accountType, 3600 * 24);
            //调用tls登录服务
            tlsLogin();
        } else { //独立模式
            alert('请填写帐号和票据');
            $('#login_dialog').show();
        }
        return;
    } else {
        hideDiscussTool(); //隐藏评论工具栏
        showDiscussForm(); //显示评论表单
    }
}

function getCmdFromMsg(msg) {
    if (!msg) {
        return false;
    }
    var cmdMsg = msg.elems[0];
    if (!cmdMsg) {
        return false;
    }
    if (cmdMsg.type != 'TIMCustomElem' && !cmdMsg.content.data) {
        return false;
    }
    var cmdObj = null;
    try {
        cmdObj = JSON.parse(cmdMsg.content.data);
    } catch (error) {
    }
    if (cmdObj == null) {
        return false;
    }
    return cmdObj;
}

//发送消息(普通消息)
function onSendMsg() {
    if (!loginInfo.identifier) { //未登录
        if (accountMode == 1) { //托管模式
            //将account_type保存到cookie中,有效期是1天
            webim.Tool.setCookie('accountType', loginInfo.accountType, 3600 * 24);
            //调用tls登录服务
            tlsLogin();
        } else { //独立模式
            alert('请填写帐号和票据');
            $('#login_dialog').show();
        }
        return;
    }

    if (!selToID) {
        alert("您还没有进入房间，暂不能聊天");
        $("#" + send_msg_text_id).val('');
        return;
    }

    //获取消息内容
    var msgtosend = $("#" + send_msg_text_id).val();
    var msgLen = webim.Tool.getStrBytes(msgtosend);

    if (msgtosend.trim().length < 1) {
        alert("发送的消息不能为空!");
        return;
    }

    //判断是否为at消息
    var atHe = $('#atHeInput').val();
    var inputMsg0 = msgtosend.split(" ")[0];
    var isAt = 0;
    if (atHe == inputMsg0) {
        isAt = 1;
    }

    var maxLen, errInfo;
    var selType = webim.SESSION_TYPE.GROUP;
    if (selType == webim.SESSION_TYPE.GROUP) {
        maxLen = 500;
        errInfo = "消息长度超出限制(最多" + Math.round(maxLen / 3) + "汉字)";
    } else {
        maxLen = 500;
        errInfo = "消息长度超出限制(最多" + Math.round(maxLen / 3) + "汉字)";
    }
    if (msgLen > maxLen) {
        alert(errInfo);
        return;
    }

    var flag = checkMsg(msgtosend);
    if (flag == 0) {//敏感词黑名单消息
        errInfo = "消息内容含有非法字符, 请重新编辑.";
        alert(errInfo);
        return;
    } else if (flag == 1) {//敏感词白名单消息
        selSess = new webim.Session(selType, selToID, selToID, '', Math.round(new Date().getTime() / 1000));
        var isSend = true; //是否为自己发送
        var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
        var random = Math.round(Math.random() * 4294967296); //消息随机数，用于去重
        var msgTime = Math.round(new Date().getTime() / 1000); //消息时间戳
        var subType; //消息子类型
        if (selType == webim.SESSION_TYPE.GROUP) {
            //群消息子类型如下：
            //webim.GROUP_MSG_SUB_TYPE.COMMON-普通消息,
            //webim.GROUP_MSG_SUB_TYPE.LOVEMSG-点赞消息，优先级最低
            //webim.GROUP_MSG_SUB_TYPE.TIP-提示消息(不支持发送，用于区分群消息子类型)，
            //webim.GROUP_MSG_SUB_TYPE.REDPACKET-红包消息，优先级最高
            subType = webim.GROUP_MSG_SUB_TYPE.COMMON;
        } else {
            //C2C消息子类型如下：
            //webim.C2C_MSG_SUB_TYPE.COMMON-普通消息,
            subType = webim.C2C_MSG_SUB_TYPE.COMMON;
        }
        var msg = new webim.Msg(selSess, isSend, seq, random, msgTime, loginInfo.identifier, subType, loginInfo.identifierNick);
        //msg.ext='abcd';
        //解析文本和表情

        var expr = /\[[^[\]]{1,3}\]/mg;
        var emotions = msgtosend.match(expr);
        var text_obj, face_obj, tmsg, emotionIndex, emotion, restMsgIndex;
        var cmdJson = {};
        cmdJson.code = '0000';
        cmdJson.postUid = userInfo.id;
        cmdJson.postNickName = userInfo.nickName;
        cmdJson.groupId = userInfo.groupId;
        cmdJson.level = userInfo.level;
        cmdJson.sendTime = new Date();
        cmdJson.checkStatus = false;
        cmdJson.auditUid = '';
        cmdJson.auditTime = '';
        cmdJson.isAuditMsg = 0;
        cmdJson.uniqueId = msg.uniqueId;
        cmdJson.isAt = isAt;
        if (isAdmin == '1') {
            var smallName = $("#small").find("option:selected").text();
            var smallLevel = $('#small').val();
            if (typeof(smallLevel) == 'undefined') {
                smallLevel = 0;
            }
            if (smallLevel != 0) {
                cmdJson.small = 0;
                cmdJson.postNickName = smallName;
                cmdJson.groupId = 5;
                cmdJson.level = smallLevel;
            }
            cmdJson.checkStatus = true;
            cmdJson.auditTime = cmdJson.sendTime;
        }
        cmdJson.checkStatus = true;
        cmdJson.auditTime = cmdJson.sendTime;
        var cmd_obj = new webim.Msg.Elem.Custom(JSON.stringify(cmdJson));
        msg.addCustom(cmd_obj);

        if (!emotions || emotions.length < 1) {
            text_obj = new webim.Msg.Elem.Text(msgtosend);
            msg.addText(text_obj);
        } else { //有表情
            for (var i = 0; i < emotions.length; i++) {
                tmsg = msgtosend.substring(0, msgtosend.indexOf(emotions[i]));
                if (tmsg) {
                    text_obj = new webim.Msg.Elem.Text(tmsg);
                    msg.addText(text_obj);
                }
                emotionIndex = webim.EmotionDataIndexs[emotions[i]];
                emotion = webim.Emotions[emotionIndex];
                if (emotion) {
                    face_obj = new webim.Msg.Elem.Face(emotionIndex, emotions[i]);
                    msg.addFace(face_obj);
                } else {
                    text_obj = new webim.Msg.Elem.Text(emotions[i]);
                    msg.addText(text_obj);
                }
                restMsgIndex = msgtosend.indexOf(emotions[i]) + emotions[i].length;
                msgtosend = msgtosend.substring(restMsgIndex);
            }
            if (msgtosend) {
                text_obj = new webim.Msg.Elem.Text(msgtosend);
                msg.addText(text_obj);
            }
        }
    } else {//正常消息
        selSess = new webim.Session(selType, selToID, selToID, '', Math.round(new Date().getTime() / 1000));
        var isSend = true; //是否为自己发送
        var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
        var random = Math.round(Math.random() * 4294967296); //消息随机数，用于去重
        var msgTime = Math.round(new Date().getTime() / 1000); //消息时间戳
        var subType; //消息子类型
        if (selType == webim.SESSION_TYPE.GROUP) {
            //群消息子类型如下：
            //webim.GROUP_MSG_SUB_TYPE.COMMON-普通消息,
            //webim.GROUP_MSG_SUB_TYPE.LOVEMSG-点赞消息，优先级最低
            //webim.GROUP_MSG_SUB_TYPE.TIP-提示消息(不支持发送，用于区分群消息子类型)，
            //webim.GROUP_MSG_SUB_TYPE.REDPACKET-红包消息，优先级最高
            subType = webim.GROUP_MSG_SUB_TYPE.COMMON;

        } else {
            //C2C消息子类型如下：
            //webim.C2C_MSG_SUB_TYPE.COMMON-普通消息,
            subType = webim.C2C_MSG_SUB_TYPE.COMMON;
        }
        var msg = new webim.Msg(selSess, isSend, seq, random, msgTime, loginInfo.identifier, subType, loginInfo.identifierNick);
        //msg.ext='abcd';
        //解析文本和表情

        var expr = /\[[^[\]]{1,3}\]/mg;
        var emotions = msgtosend.match(expr);
        var text_obj, face_obj, tmsg, emotionIndex, emotion, restMsgIndex;
        var cmdJson = {};
        cmdJson.code = '0000';
        cmdJson.postUid = userInfo.id;
        cmdJson.postNickName = userInfo.nickName;
        cmdJson.groupId = userInfo.groupId;
        cmdJson.level = userInfo.level;
        cmdJson.sendTime = new Date();
        cmdJson.checkStatus = false;
        cmdJson.auditUid = '';
        cmdJson.auditTime = '';
        cmdJson.isAuditMsg = 0;
        cmdJson.uniqueId = msg.uniqueId;
        cmdJson.isAt = isAt;
        if (isAdmin == '1') {
            var smallName = $("#small").find("option:selected").text();
            var smallLevel = $('#small').val();
            if (typeof(smallLevel) == 'undefined') {
                smallLevel = 0;
            }
            if (smallLevel != 0) {
                cmdJson.small = 0;
                cmdJson.postNickName = smallName;
                cmdJson.groupId = 5;
                cmdJson.level = smallLevel;
            }
            cmdJson.checkStatus = true;
            cmdJson.auditUid = userInfo.id;
            cmdJson.auditTime = cmdJson.sendTime;
        }
        var cmd_obj = new webim.Msg.Elem.Custom(JSON.stringify(cmdJson));
        msg.addCustom(cmd_obj);

        if (!emotions || emotions.length < 1) {
            text_obj = new webim.Msg.Elem.Text(msgtosend);
            msg.addText(text_obj);
        } else { //有表情
            for (var i = 0; i < emotions.length; i++) {
                tmsg = msgtosend.substring(0, msgtosend.indexOf(emotions[i]));
                if (tmsg) {
                    text_obj = new webim.Msg.Elem.Text(tmsg);
                    msg.addText(text_obj);
                }
                emotionIndex = webim.EmotionDataIndexs[emotions[i]];
                emotion = webim.Emotions[emotionIndex];
                if (emotion) {
                    face_obj = new webim.Msg.Elem.Face(emotionIndex, emotions[i]);
                    msg.addFace(face_obj);
                } else {
                    text_obj = new webim.Msg.Elem.Text(emotions[i]);
                    msg.addText(text_obj);
                }
                restMsgIndex = msgtosend.indexOf(emotions[i]) + emotions[i].length;
                msgtosend = msgtosend.substring(restMsgIndex);
            }
            if (msgtosend) {
                text_obj = new webim.Msg.Elem.Text(msgtosend);
                msg.addText(text_obj);
            }
        }
    }
    webim.sendMsg(msg, function (resp) {
        if (selType == webim.SESSION_TYPE.C2C) { //私聊时，在聊天窗口手动添加一条发的消息，群聊时，长轮询接口会返回自己发的消息
            showMsg(msg);
        }
        saveGroupMsg(msg);
        webim.Log.info("发消息成功");
        $('#atHeInput').val("");
        $("#" + send_msg_text_id).val('');

        //vip/游客发送成功之后禁用按钮, 3秒后解除禁用
        if (userInfo.groupId == 1 || userInfo.groupId == 5) {
            countDownSendMsgBtn();
        }
    }, function (err) {
        if (err.ErrorCode == 10017) {
            alert('当前无法发言，如有疑问，请联系客服。');
        }
    });
}


function sendAutoMsg(content) {
    if (!loginInfo.identifier) { //未登录
        if (accountMode == 1) { //托管模式
            //将account_type保存到cookie中,有效期是1天
            webim.Tool.setCookie('accountType', loginInfo.accountType, 3600 * 24);
            //调用tls登录服务
            tlsLogin();
        } else { //独立模式
            alert('请填写帐号和票据');
            $('#login_dialog').show();
        }
        return;
    }

    if (!selToID) {
        alert("您还没有进入房间，暂不能聊天");
        $("#" + send_msg_text_id).val('');
        return;
    }

    //获取消息内容
    var msgtosend = content;
    var selType = webim.SESSION_TYPE.GROUP;
    selSess = new webim.Session(selType, selToID, selToID, '', Math.round(new Date().getTime() / 1000));
    var isSend = true; //是否为自己发送
    var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
    var random = Math.round(Math.random() * 4294967296); //消息随机数，用于去重
    var msgTime = Math.round(new Date().getTime() / 1000); //消息时间戳
    var subType; //消息子类型
    if (selType == webim.SESSION_TYPE.GROUP) {
        //群消息子类型如下：
        //webim.GROUP_MSG_SUB_TYPE.COMMON-普通消息,
        //webim.GROUP_MSG_SUB_TYPE.LOVEMSG-点赞消息，优先级最低
        //webim.GROUP_MSG_SUB_TYPE.TIP-提示消息(不支持发送，用于区分群消息子类型)，
        //webim.GROUP_MSG_SUB_TYPE.REDPACKET-红包消息，优先级最高
        subType = webim.GROUP_MSG_SUB_TYPE.COMMON;
    } else {
        //C2C消息子类型如下：
        //webim.C2C_MSG_SUB_TYPE.COMMON-普通消息,
        subType = webim.C2C_MSG_SUB_TYPE.COMMON;
    }
    var msg = new webim.Msg(selSess, isSend, seq, random, msgTime, loginInfo.identifier, subType, loginInfo.identifierNick);
    //msg.ext='abcd';
    //解析文本和表情

    var expr = /\[[^[\]]{1,3}\]/mg;
    var emotions = msgtosend.match(expr);
    var text_obj, face_obj, tmsg, emotionIndex, emotion, restMsgIndex;
    var cmdJson = {};
    cmdJson.code = '0000';
    cmdJson.postUid = userInfo.id;
    cmdJson.level = userInfo.level;
    cmdJson.sendTime = new Date();
    cmdJson.auditUid = '';
    cmdJson.auditTime = '';
    cmdJson.isAuditMsg = 0;
    cmdJson.uniqueId = msg.uniqueId;
    cmdJson.small = 0;

    cmdJson.postNickName = "test";
    cmdJson.groupId = 5;
    cmdJson.level = 1;

    cmdJson.checkStatus = true;
    cmdJson.auditTime = cmdJson.sendTime;
    var cmd_obj = new webim.Msg.Elem.Custom(JSON.stringify(cmdJson));
    msg.addCustom(cmd_obj);

    if (!emotions || emotions.length < 1) {
        text_obj = new webim.Msg.Elem.Text(msgtosend);
        msg.addText(text_obj);
    } else { //有表情
        for (var i = 0; i < emotions.length; i++) {
            tmsg = msgtosend.substring(0, msgtosend.indexOf(emotions[i]));
            if (tmsg) {
                text_obj = new webim.Msg.Elem.Text(tmsg);
                msg.addText(text_obj);
            }
            emotionIndex = webim.EmotionDataIndexs[emotions[i]];
            emotion = webim.Emotions[emotionIndex];
            if (emotion) {
                face_obj = new webim.Msg.Elem.Face(emotionIndex, emotions[i]);
                msg.addFace(face_obj);
            } else {
                text_obj = new webim.Msg.Elem.Text(emotions[i]);
                msg.addText(text_obj);
            }
            restMsgIndex = msgtosend.indexOf(emotions[i]) + emotions[i].length;
            msgtosend = msgtosend.substring(restMsgIndex);

        }
        if (msgtosend) {
            text_obj = new webim.Msg.Elem.Text(msgtosend);
            msg.addText(text_obj);
        }
    }


    webim.sendMsg(msg, function (resp) {
        if (selType == webim.SESSION_TYPE.C2C) { //私聊时，在聊天窗口手动添加一条发的消息，群聊时，长轮询接口会返回自己发的消息
            showMsg(msg);
        }
        saveGroupMsg(msg);
        webim.Log.info("发消息成功");

    }, function (err) {
        if (err.ErrorCode == 10017) {
            alert('当前无法发言，如有疑问，请联系客服。');
        }
    });

}


function sendImgMsg(imageUrl) {
    var selType = webim.SESSION_TYPE.GROUP;
    var msgtosend = "<i></i><img class='img_big' style='max-width: 200px;max-height: 200px;' src='" + imgPath + "chat/" + imageUrl + "'/>";
    selSess = new webim.Session(selType, selToID, selToID, '', Math.round(new Date().getTime() / 1000));
    var isSend = true; //是否为自己发送
    var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
    var random = Math.round(Math.random() * 4294967296); //消息随机数，用于去重
    var msgTime = Math.round(new Date().getTime() / 1000); //消息时间戳
    var subType; //消息子类型
    if (selType == webim.SESSION_TYPE.GROUP) {
        subType = webim.GROUP_MSG_SUB_TYPE.COMMON;
    } else {
        subType = webim.C2C_MSG_SUB_TYPE.COMMON;
    }
    var msg = new webim.Msg(selSess, isSend, seq, random, msgTime, loginInfo.identifier, subType, loginInfo.identifierNick);
    var expr = /\[[^[\]]{1,3}\]/mg;
    var emotions = msgtosend.match(expr);
    var text_obj, face_obj, tmsg, emotionIndex, emotion, restMsgIndex;
    var cmdJson = {};
    cmdJson.code = '0000';
    cmdJson.postUid = userInfo.id;
    cmdJson.postNickName = userInfo.nickName;
    cmdJson.groupId = userInfo.groupId;
    cmdJson.level = userInfo.level;
    cmdJson.sendTime = new Date();
    cmdJson.checkStatus = false;
    cmdJson.auditUid = '';
    cmdJson.auditTime = '';
    cmdJson.isAuditMsg = 0;
    cmdJson.uniqueId = msg.uniqueId;
    cmdJson.isImg = 1;
    cmdJson.chatImgUrl = imgPath + "chat/" + imageUrl;
    cmdJson.sendImgUrl = msgtosend;
    if (isAdmin == '1') {
        var smallName = $("#small").find("option:selected").text();
        var smallLevel = $('#small').val();
        if (typeof(smallLevel) == 'undefined') {
            smallLevel = 0;
        }
        if (smallLevel != 0) {
            cmdJson.small = 0;
            cmdJson.postNickName = smallName;
            cmdJson.groupId = 5;
            cmdJson.level = smallLevel;
        }
        cmdJson.checkStatus = true;
        cmdJson.auditTime = cmdJson.sendTime;
    }

    if (userInfo.groupId == 1 || userInfo.groupId == 5) {
        cmdJson.checkStatus = false;
        cmdJson.auditTime = "";
    } else {
        cmdJson.checkStatus = true;
        cmdJson.auditTime = cmdJson.sendTime;
    }

    var cmd_obj = new webim.Msg.Elem.Custom(JSON.stringify(cmdJson));
    msg.addCustom(cmd_obj);

    if (!emotions || emotions.length < 1) {
        text_obj = new webim.Msg.Elem.Text(msgtosend);
        msg.addText(text_obj);
    }
    webim.sendMsg(msg, function (resp) {
        webim.Log.info("发消息成功");
        saveGroupMsg(msg);
    }, function (err) {
        if (err.ErrorCode == 10017) {
            alert('当前无法发言，如有疑问，请联系客服。');
        }
    });
}

//发送消息(普通消息)
function adminSendMsg() {
    if (!loginInfo.identifier) { //未登录
        if (accountMode == 1) { //托管模式
            //将account_type保存到cookie中,有效期是1天
            webim.Tool.setCookie('accountType', loginInfo.accountType, 3600 * 24);
            //调用tls登录服务
            tlsLogin();
        } else { //独立模式
            alert('请填写帐号和票据');
            $('#login_dialog').show();
        }
        return;
    }

    if (!selToID) {
        alert("您还没有进入房间，暂不能聊天");
        $("#" + send_msg_text_id).val('');
        return;
    }
    //获取消息内容
    var msgtosend = $("#" + send_msg_text_id).val();
    var msgLen = webim.Tool.getStrBytes(msgtosend);

    if (msgtosend.length < 1) {
        alert("发送的消息不能为空!");
        return;
    }

    var maxLen, errInfo;
    var selType = webim.SESSION_TYPE.GROUP;
    if (selType == webim.SESSION_TYPE.GROUP) {
        maxLen = webim.MSG_MAX_LENGTH.GROUP;
        errInfo = "消息长度超出限制(最多" + Math.round(maxLen / 3) + "汉字)";
    } else {
        maxLen = webim.MSG_MAX_LENGTH.C2C;
        errInfo = "消息长度超出限制(最多" + Math.round(maxLen / 3) + "汉字)";
    }
    if (msgLen > maxLen) {
        alert(errInfo);
        return;
    }

    selSess = new webim.Session(selType, selToID, selToID, '', Math.round(new Date().getTime() / 1000));
    var isSend = true; //是否为自己发送
    var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
    var random = Math.round(Math.random() * 4294967296); //消息随机数，用于去重
    var msgTime = Math.round(new Date().getTime() / 1000); //消息时间戳
    var subType; //消息子类型
    if (selType == webim.SESSION_TYPE.GROUP) {
        //群消息子类型如下：
        //webim.GROUP_MSG_SUB_TYPE.COMMON-普通消息,
        //webim.GROUP_MSG_SUB_TYPE.LOVEMSG-点赞消息，优先级最低
        //webim.GROUP_MSG_SUB_TYPE.TIP-提示消息(不支持发送，用于区分群消息子类型)，
        //webim.GROUP_MSG_SUB_TYPE.REDPACKET-红包消息，优先级最高
        subType = webim.GROUP_MSG_SUB_TYPE.COMMON;

    } else {
        //C2C消息子类型如下：
        //webim.C2C_MSG_SUB_TYPE.COMMON-普通消息,
        subType = webim.C2C_MSG_SUB_TYPE.COMMON;
    }
    var msg = new webim.Msg(selSess, isSend, seq, random, msgTime, loginInfo.identifier, subType, loginInfo.identifierNick);
    //msg.ext='abcd';
    //解析文本和表情
    var expr = /\[[^[\]]{1,3}\]/mg;
    var emotions = msgtosend.match(expr);
    var text_obj, face_obj, tmsg, emotionIndex, emotion, restMsgIndex;

    var cmdJson = {};
    cmdJson.code = '0000';
    cmdJson.postUid = userInfo.id;
    cmdJson.postNickName = userInfo.nickName;
    cmdJson.groupId = userInfo.groupId;
    cmdJson.level = userInfo.level;
    cmdJson.sendTime = new Date(msg.time * 1000);
    cmdJson.checkStatus = false;
    cmdJson.auditUid = '';
    cmdJson.auditTime = '';
    cmdJson.uniqueId = msg.uniqueId;
    if (isAdmin == '1') {
        cmdJson.checkStatus = true;
        cmdJson.auditUid = userInfo.id;
        cmdJson.auditTime = cmdJson.sendTime;
    }
    var cmd_obj = new webim.Msg.Elem.Custom(JSON.stringify(cmdJson));
    msg.addCustom(cmd_obj);

    if (!emotions || emotions.length < 1) {
        text_obj = new webim.Msg.Elem.Text(msgtosend);
        msg.addText(text_obj);
    } else { //有表情
        for (var i = 0; i < emotions.length; i++) {
            tmsg = msgtosend.substring(0, msgtosend.indexOf(emotions[i]));
            if (tmsg) {
                text_obj = new webim.Msg.Elem.Text(tmsg);
                msg.addText(text_obj);
            }
            emotionIndex = webim.EmotionDataIndexs[emotions[i]];
            emotion = webim.Emotions[emotionIndex];
            if (emotion) {
                face_obj = new webim.Msg.Elem.Face(emotionIndex, emotions[i]);
                msg.addFace(face_obj);
            } else {
                text_obj = new webim.Msg.Elem.Text(emotions[i]);
                msg.addText(text_obj);
            }
            restMsgIndex = msgtosend.indexOf(emotions[i]) + emotions[i].length;
            msgtosend = msgtosend.substring(restMsgIndex);
        }
        if (msgtosend) {
            text_obj = new webim.Msg.Elem.Text(msgtosend);
            msg.addText(text_obj);
        }
    }
    webim.sendMsg(msg, function (resp) {
        if (selType == webim.SESSION_TYPE.C2C) { //私聊时，在聊天窗口手动添加一条发的消息，群聊时，长轮询接口会返回自己发的消息
            showMsg(msg);
        }
        webim.Log.info("发消息成功");
        $("#" + send_msg_text_id).val('');
        hideDiscussForm(); //隐藏评论表单
        showDiscussTool(); //显示评论工具栏
        hideDiscussEmotion(); //隐藏表情
    }, function (err) {
        webim.Log.error("发消息失败:" + err.ErrorInfo);
        alert("发消息失败:" + err.ErrorInfo);
    });
}

//发送鲜花(普通消息)
function sendFlowerMsg() {
    $(".roseF").unbind();
    if (!loginInfo.identifier) { //未登录
        if (accountMode == 1) { //托管模式
            //将account_type保存到cookie中,有效期是1天
            webim.Tool.setCookie('accountType', loginInfo.accountType, 3600 * 24);
            //调用tls登录服务
            tlsLogin();
        } else { //独立模式
            alert('请填写帐号和票据');
            $('#login_dialog').show();
        }
        return;
    }

    if (!selToID) {
        alert("您还没有进入房间，暂不能聊天");
        $("#" + send_msg_text_id).val('');
        return;
    }

    //获取消息内容
    var msgtosend = "[花]";
    var selType = webim.SESSION_TYPE.GROUP;
    selSess = new webim.Session(selType, selToID, selToID, '', Math.round(new Date().getTime() / 1000));
    var isSend = true; //是否为自己发送
    var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
    var random = Math.round(Math.random() * 4294967296); //消息随机数，用于去重
    var msgTime = Math.round(new Date().getTime() / 1000); //消息时间戳
    var subType; //消息子类型
    if (selType == webim.SESSION_TYPE.GROUP) {
        //群消息子类型如下：
        //webim.GROUP_MSG_SUB_TYPE.COMMON-普通消息,
        //webim.GROUP_MSG_SUB_TYPE.LOVEMSG-点赞消息，优先级最低
        //webim.GROUP_MSG_SUB_TYPE.TIP-提示消息(不支持发送，用于区分群消息子类型)，
        //webim.GROUP_MSG_SUB_TYPE.REDPACKET-红包消息，优先级最高
        subType = webim.GROUP_MSG_SUB_TYPE.COMMON;
    } else {
        //C2C消息子类型如下：
        //webim.C2C_MSG_SUB_TYPE.COMMON-普通消息,
        subType = webim.C2C_MSG_SUB_TYPE.COMMON;
    }
    var msg = new webim.Msg(selSess, isSend, seq, random, msgTime, loginInfo.identifier, subType, loginInfo.identifierNick);
    //msg.ext='abcd';
    //解析文本和表情

    var expr = /\[[^[\]]{1,3}\]/mg;
    var emotions = msgtosend.match(expr);
    var text_obj, face_obj, tmsg, emotionIndex, emotion, restMsgIndex;
    var cmdJson = {};
    cmdJson.code = '0000';
    cmdJson.postUid = userInfo.id;
    cmdJson.postNickName = userInfo.nickName;
    cmdJson.groupId = userInfo.groupId;
    cmdJson.level = userInfo.level;
    cmdJson.sendTime = new Date();
    cmdJson.checkStatus = false;
    cmdJson.auditUid = '';
    cmdJson.auditTime = '';
    cmdJson.isSpecial = 0;//标记是特殊消息
    cmdJson.type = 1; //特殊标记分类， 例红包， 鲜花
    cmdJson.isAuditMsg = 0;
    cmdJson.uniqueId = msg.uniqueId;
    if (isAdmin == '1') {
        var smallName = $("#small").find("option:selected").text();
        var smallLevel = $('#small').val();
        if (typeof(smallLevel) == 'undefined') {
            smallLevel = 0;
        }
        if (smallLevel != 0) {
            cmdJson.small = 0;
            cmdJson.postNickName = smallName;
            cmdJson.groupId = 5;
            cmdJson.level = smallLevel;
        }
        cmdJson.checkStatus = true;
        cmdJson.auditTime = cmdJson.sendTime;
    }
    cmdJson.checkStatus = true;
    cmdJson.auditTime = cmdJson.sendTime;
    var cmd_obj = new webim.Msg.Elem.Custom(JSON.stringify(cmdJson));
    msg.addCustom(cmd_obj);

    if (!emotions || emotions.length < 1) {
        text_obj = new webim.Msg.Elem.Text(msgtosend);
        msg.addText(text_obj);
    } else { //有表情
        for (var i = 0; i < emotions.length; i++) {
            tmsg = msgtosend.substring(0, msgtosend.indexOf(emotions[i]));
            if (tmsg) {
                text_obj = new webim.Msg.Elem.Text(tmsg);
                msg.addText(text_obj);
            }
            emotionIndex = webim.EmotionDataIndexs[emotions[i]];
            emotion = webim.Emotions[emotionIndex];
            if (emotion) {
                face_obj = new webim.Msg.Elem.Face(emotionIndex, emotions[i]);
                msg.addFace(face_obj);
            } else {
                text_obj = new webim.Msg.Elem.Text(emotions[i]);
                msg.addText(text_obj);
            }
            restMsgIndex = msgtosend.indexOf(emotions[i]) + emotions[i].length;
            msgtosend = msgtosend.substring(restMsgIndex);

        }
        if (msgtosend) {
            text_obj = new webim.Msg.Elem.Text(msgtosend);
            msg.addText(text_obj);
        }
    }

    webim.sendMsg(msg, function (resp) {
        if (selType == webim.SESSION_TYPE.C2C) { //私聊时，在聊天窗口手动添加一条发的消息，群聊时，长轮询接口会返回自己发的消息
            showMsg(msg);
        }
        saveGroupMsg(msg);
        webim.Log.info("发消息成功");
        sendRoseMsg();
        //vip/游客发送成功之后禁用按钮, 3秒后解除禁用
        if (userInfo.groupId == 1 || userInfo.groupId == 5) {
            countDownSendMsgBtn();
        }
        // 发送鲜花后20s倒计时可再次发送
        var i = 20;
        var roseInt = setInterval(function () {
            if (i == 1) {
                $('.roseF').bind('click', sendFlowerMsg);
                $('#roseIntF').hide();
                window.clearInterval(roseInt);
            } else {
                i--;
                $('#roseIntF').show();
                $('#roseIntF').html(i);
            }
        }, 1000);
    }, function (err) {
        if (err.ErrorCode == 10017) {
            alert('当前无法发言，如有疑问，请联系客服。');
        }
    });
}

//发送红包(普通消息)
function sendRedBagMsg() {
    $(".redBagF").unbind();
    if (!loginInfo.identifier) { //未登录
        if (accountMode == 1) { //托管模式
            //将account_type保存到cookie中,有效期是1天
            webim.Tool.setCookie('accountType', loginInfo.accountType, 3600 * 24);
            //调用tls登录服务
            tlsLogin();
        } else { //独立模式
            alert('请填写帐号和票据');
            $('#login_dialog').show();
        }
        return;
    }

    if (!selToID) {
        alert("您还没有进入房间，暂不能聊天");
        $("#" + send_msg_text_id).val('');
        return;
    }

    //获取消息内容
    var msgtosend = "[红包]";
    var selType = webim.SESSION_TYPE.GROUP;
    selSess = new webim.Session(selType, selToID, selToID, '', Math.round(new Date().getTime() / 1000));
    var isSend = true; //是否为自己发送
    var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
    var random = Math.round(Math.random() * 4294967296); //消息随机数，用于去重
    var msgTime = Math.round(new Date().getTime() / 1000); //消息时间戳
    var subType; //消息子类型
    if (selType == webim.SESSION_TYPE.GROUP) {
        //群消息子类型如下：
        //webim.GROUP_MSG_SUB_TYPE.COMMON-普通消息,
        //webim.GROUP_MSG_SUB_TYPE.LOVEMSG-点赞消息，优先级最低
        //webim.GROUP_MSG_SUB_TYPE.TIP-提示消息(不支持发送，用于区分群消息子类型)，
        //webim.GROUP_MSG_SUB_TYPE.REDPACKET-红包消息，优先级最高
        subType = webim.GROUP_MSG_SUB_TYPE.COMMON;
    } else {
        //C2C消息子类型如下：
        //webim.C2C_MSG_SUB_TYPE.COMMON-普通消息,
        subType = webim.C2C_MSG_SUB_TYPE.COMMON;
    }
    var msg = new webim.Msg(selSess, isSend, seq, random, msgTime, loginInfo.identifier, subType, loginInfo.identifierNick);
    //msg.ext='abcd';
    //解析文本和表情

    var expr = /\[[^[\]]{1,3}\]/mg;
    var emotions = msgtosend.match(expr);
    var text_obj, face_obj, tmsg, emotionIndex, emotion, restMsgIndex;
    var cmdJson = {};
    cmdJson.code = '0000';
    cmdJson.postUid = userInfo.id;
    cmdJson.postNickName = userInfo.nickName;
    cmdJson.groupId = userInfo.groupId;
    cmdJson.level = userInfo.level;
    cmdJson.sendTime = new Date();
    cmdJson.checkStatus = false;
    cmdJson.auditUid = '';
    cmdJson.auditTime = '';
    cmdJson.isSpecial = 0;//标记是特殊消息
    cmdJson.type = 0; //特殊标记分类， 例0红包， 1鲜花
    cmdJson.isAuditMsg = 0;
    cmdJson.uniqueId = msg.uniqueId;
    if (isAdmin == '1') {
        var smallName = $("#small").find("option:selected").text();
        var smallLevel = $('#small').val();
        if (typeof(smallLevel) == 'undefined') {
            smallLevel = 0;
        }
        if (smallLevel != 0) {
            cmdJson.small = 0;
            cmdJson.postNickName = smallName;
            cmdJson.groupId = 5;
            cmdJson.level = smallLevel;
        }
        cmdJson.checkStatus = true;
        cmdJson.auditTime = cmdJson.sendTime;
    }
    cmdJson.checkStatus = true;
    cmdJson.auditTime = cmdJson.sendTime;
    var cmd_obj = new webim.Msg.Elem.Custom(JSON.stringify(cmdJson));
    msg.addCustom(cmd_obj);

    if (!emotions || emotions.length < 1) {
        text_obj = new webim.Msg.Elem.Text(msgtosend);
        msg.addText(text_obj);
    } else { //有表情
        for (var i = 0; i < emotions.length; i++) {
            tmsg = msgtosend.substring(0, msgtosend.indexOf(emotions[i]));
            if (tmsg) {
                text_obj = new webim.Msg.Elem.Text(tmsg);
                msg.addText(text_obj);
            }
            emotionIndex = webim.EmotionDataIndexs[emotions[i]];
            emotion = webim.Emotions[emotionIndex];
            if (emotion) {
                face_obj = new webim.Msg.Elem.Face(emotionIndex, emotions[i]);
                msg.addFace(face_obj);
            } else {
                text_obj = new webim.Msg.Elem.Text(emotions[i]);
                msg.addText(text_obj);
            }
            restMsgIndex = msgtosend.indexOf(emotions[i]) + emotions[i].length;
            msgtosend = msgtosend.substring(restMsgIndex);

        }
        if (msgtosend) {
            text_obj = new webim.Msg.Elem.Text(msgtosend);
            msg.addText(text_obj);
        }
    }


    webim.sendMsg(msg, function (resp) {
        if (selType == webim.SESSION_TYPE.C2C) { //私聊时，在聊天窗口手动添加一条发的消息，群聊时，长轮询接口会返回自己发的消息
            showMsg(msg);
        }
        saveGroupMsg(msg);
        webim.Log.info("发消息成功");

        sendRedPackMsg();

        //vip/游客发送成功之后禁用按钮, 3秒后解除禁用
        if (userInfo.groupId == 1 || userInfo.groupId == 5) {
            countDownSendMsgBtn();
        }

        // 发送红包后20s可以继续发送
        var i = 20;
        var roseInt = setInterval(function () {
            if (i == 1) {
                $('.redBagF').bind('click', sendRedBagMsg);
                $('#redPackIntF').hide();
                window.clearInterval(roseInt);
            } else {
                i--;
                $('#redPackIntF').show();
                $('#redPackIntF').html(i);
            }
        }, 1000);

    }, function (err) {
        if (err.ErrorCode == 10017) {
            alert('当前无法发言，如有疑问，请联系客服。');
        }
    });
}

//发送C2C消息(文本或者表情)
function onSendC2CMsg(waterToId, flag) {
    if (!waterToId) {
        alert("你还没有选中好友或者群组，暂不能聊天");
        $("#send_msg_text").val('');
        return;
    }

    // if (!selToID) {
    //     alert("您还没有进入房间，暂不能聊天");
    //     $("#" + send_msg_text_id).val('');
    //     return;
    // }
    //获取消息内容
    var msgtosend = '';
    if (flag == 0) {
        msgtosend = $("#waterPersionContent").val()
    } else if (flag == 1) {
        msgtosend = $("#waterContent").val()
    }
    var msgLen = webim.Tool.getStrBytes(msgtosend);
    if (msgtosend.length < 1) {
        alert("发送的消息不能为空!");
        return;
    }

    var maxLen, errInfo;
    var selType = webim.SESSION_TYPE.C2C;
    if (selType == webim.SESSION_TYPE.GROUP) {
        maxLen = webim.MSG_MAX_LENGTH.GROUP;
        errInfo = "消息长度超出限制(最多" + Math.round(maxLen / 3) + "汉字)";
    } else {
        maxLen = webim.MSG_MAX_LENGTH.C2C;
        errInfo = "消息长度超出限制(最多" + Math.round(maxLen / 3) + "汉字)";
    }
    if (msgLen > maxLen) {
        alert(errInfo);
        return;
    }

    selSess = new webim.Session(selType, waterToId, waterToId, '', Math.round(new Date().getTime() / 1000));
    var isSend = true; //是否为自己发送
    var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
    var random = Math.round(Math.random() * 4294967296); //消息随机数，用于去重
    var msgTime = Math.round(new Date().getTime() / 1000); //消息时间戳
    var subType = webim.SESSION_TYPE.C2C; //消息子类型
    if (selType == webim.SESSION_TYPE.GROUP) {
        //群消息子类型如下：
        //webim.GROUP_MSG_SUB_TYPE.COMMON-普通消息,
        //webim.GROUP_MSG_SUB_TYPE.LOVEMSG-点赞消息，优先级最低
        //webim.GROUP_MSG_SUB_TYPE.TIP-提示消息(不支持发送，用于区分群消息子类型)，
        //webim.GROUP_MSG_SUB_TYPE.REDPACKET-红包消息，优先级最高
        subType = webim.GROUP_MSG_SUB_TYPE.COMMON;
    } else {
        //C2C消息子类型如下：
        //webim.C2C_MSG_SUB_TYPE.COMMON-普通消息,
        subType = webim.C2C_MSG_SUB_TYPE.COMMON;
    }
    var msg = new webim.Msg(selSess, isSend, seq, random, msgTime, loginInfo.identifier, subType, loginInfo.identifierNick);
    //解析文本和表情
    var expr = /\[[^[\]]{1,3}\]/mg;
    var emotions = msgtosend.match(expr);

    var cmdJson = {};
    cmdJson.postUid = userInfo.id;
    cmdJson.postNickName = userInfo.nickName;
    cmdJson.groupId = userInfo.groupId;
    cmdJson.level = userInfo.level;
    cmdJson.flag = waterToId;
    cmdJson.content = msgtosend;
    var cmd_obj = new webim.Msg.Elem.Custom(JSON.stringify(cmdJson));
    msg.addCustom(cmd_obj);

    console.log(msg);
    var text_obj, face_obj, tmsg, emotionIndex, emotion, restMsgIndex;
    if (!emotions || emotions.length < 1) {
        text_obj = new webim.Msg.Elem.Text(msgtosend);
        msg.addText(text_obj);
    } else { //有表情
        for (var i = 0; i < emotions.length; i++) {
            tmsg = msgtosend.substring(0, msgtosend.indexOf(emotions[i]));
            if (tmsg) {
                text_obj = new webim.Msg.Elem.Text(tmsg);
                msg.addText(text_obj);
            }
            emotionIndex = webim.EmotionDataIndexs[emotions[i]];
            emotion = webim.Emotions[emotionIndex];
            if (emotion) {
                face_obj = new webim.Msg.Elem.Face(emotionIndex, emotions[i]);
                msg.addFace(face_obj);
            } else {
                text_obj = new webim.Msg.Elem.Text(emotions[i]);
                msg.addText(text_obj);
            }
            restMsgIndex = msgtosend.indexOf(emotions[i]) + emotions[i].length;
            msgtosend = msgtosend.substring(restMsgIndex);
        }
        if (msgtosend) {
            text_obj = new webim.Msg.Elem.Text(msgtosend);
            msg.addText(text_obj);
        }
    }
    webim.sendMsg(msg, function (resp) {
        if (selType == webim.SESSION_TYPE.C2C) { //私聊时，在聊天窗口手动添加一条发的消息，群聊时，长轮询接口会返回自己发的消息
            // addMsg(msg);
        }
        console.log('发送成功');
        webim.Log.info("发消息成功");
        if (flag == 0) {
            $("#waterPersionContent").val('');
        } else if (flag == 1) {
            $("#waterContent").val('');
        }
    }, function (err) {
        console.log(err);
        if (err.ErrorCode == 10017) {
            alert('当前无法发言，如有疑问，请联系客服。');
        }
    });
}

/**
 * 展示C2C消息
 */
function showC2CMsg(data) {
    var gid = userInfo.groupId;
    var uid = userInfo.id;
    var me = '';
    if (gid == 1) {
        me = 'yk-' + uid;
    } else if (gid == 5) {
        me = 'vip-' + uid;
    } else {
        me = uid;
    }
    var obj = JSON.parse(data);
    //发送给自己的消息
    if (obj.flag == me) {
        if (groupId == 1 || groupId == 5) {//游客/vip
            $('.waterPersionBox').removeClass('hide');
            $('.withManageToChatTip').html('与' + obj.postNickName + '的对话' + '<i></i>');
            $('#waterPersionToId').val(obj.postUid);
            $('#waterPersionToName').val(obj.postNickName);
            $('#persionToGroupId').val(obj.groupId);

            var jqxhr = $.ajax({
                url: ctx + "/live/getWaterChatMessage",
                data: {
                    'roomId': roomId,
                    'toGroupId': obj.groupId,
                    'fromGroupId': groupId,
                    'fromId': userInfo.id,
                    'toId': obj.postUid
                },
            });
            jqxhr.done(function (data) {
                if (data.code == '1000') {
                    var htmls = $('.messageTip').html();
                    htmls += getHtmls(groupId, data);
                    $('.waterPersionChatMessageList').html(htmls);
                    $('#waterPersionChatBox').scrollTop($('#waterPersionChatBox')[0].scrollHeight);//滚动条到最底部
                }
            });
        } else {
            if (talkUserId == '') {
                $('.newMsgtipDiv').removeClass('hide');
            }
            //聊天窗口是否打开, 没打开返回true 反之
            var status = $('.waterListF').hasClass('hide');
            //如果窗口打开, 锁定聊天的两个人, 其他的不会覆盖
            if (!status) {
                $('.newMsgtipDiv').addClass('hide');
                //记录当前聊天的发送人id
                if (talkUserId == '') {
                    talkUserId = obj.postUid;
                }
                if (obj.postUid == talkUserId) {
                    toChat(obj.groupId, 0, obj.postUid, obj.postNickName);
                } else {
                    var jqxhr = $.ajax({
                        url: ctx + "/live/getContactPersion",
                        data: {'relationUserId': userInfo.id},
                    });
                    jqxhr.done(function (data) {
                        if (data.code == '1000') {
                            var htmls = '';
                            for (var i = 0; i < data.data.length; i++) {
                                var record = '<tr onclick="toChat(' + data.data[i].groupId + ', ' + data.data[i].level + ', ' + data.data[i].userId + ', /' + data.data[i].userNickName + '/)" class="cur fz10">';
                                if (parseInt(data.data[i].count) > 0) {
                                    record += '<td><i id="tip-' + data.data[i].userId + '" class="newMsgtip1">' + data.data[i].count + '</i>' + data.data[i].userNickName + "</td><td>";
                                } else {
                                    record += '<td>' + data.data[i].userNickName + "</td><td>";
                                }
                                record += '在线</td><td>';
                                record += getJoinFormatDate(data.data[i].joinTime) + '</td></tr>';
                                htmls += record;
                            }
                            $('.waterUserList').html(htmls);
                            // getWaterGroup();
                        }
                    });
                }
            } else {
                if (talkUserId == '') {
                    talkUserId = obj.postUid;
                }
                var jqxhr = $.ajax({
                    url: ctx + "/live/getContactPersion",
                    data: {'relationUserId': userInfo.id},
                });
                jqxhr.done(function (data) {
                    if (data.code == '1000') {
                        var htmls = '';
                        for (var i = 0; i < data.data.length; i++) {
                            var record = '<tr onclick="toChat(' + data.data[i].groupId + ', ' + data.data[i].level + ', ' + data.data[i].userId + ', /' + data.data[i].userNickName + '/)" class="cur fz10">';
                            if (parseInt(data.data[i].count) > 0) {
                                record += '<td><i id="tip-' + data.data[i].userId + '" class="newMsgtip1">' + data.data[i].count + '</i>' + data.data[i].userNickName + "</td><td>";
                            } else {
                                record += '<td>' + data.data[i].userNickName + "</td><td>";
                            }
                            record += '在线</td><td>';
                            record += getJoinFormatDate(data.data[i].joinTime) + '</td></tr>';
                            htmls += record;
                        }
                        $('.waterUserList').html(htmls);
                    }
                });
            }
        }
    }
}


//发送消息(群点赞消息)
function sendGroupLoveMsg() {
    if (!loginInfo.identifier) { //未登录
        if (accountMode == 1) { //托管模式
            //将account_type保存到cookie中,有效期是1天
            webim.Tool.setCookie('accountType', loginInfo.accountType, 3600 * 24);
            //调用tls登录服务
            tlsLogin();
        } else { //独立模式
            alert('请填写帐号和票据');
            $('#login_dialog').show();
        }
        return;
    }

    if (!selToID) {
        alert("您还没有进入房间，暂不能点赞");
        return;
    }

    selSess = new webim.Session(selType, selToID, selToID, selSessHeadUrl, Math.round(new Date().getTime() / 1000));
    var isSend = true; //是否为自己发送
    var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
    var random = Math.round(Math.random() * 4294967296); //消息随机数，用于去重
    var msgTime = Math.round(new Date().getTime() / 1000); //消息时间戳
    //群消息子类型如下：
    //webim.GROUP_MSG_SUB_TYPE.COMMON-普通消息,
    //webim.GROUP_MSG_SUB_TYPE.LOVEMSG-点赞消息，优先级最低
    //webim.GROUP_MSG_SUB_TYPE.TIP-提示消息(不支持发送，用于区分群消息子类型)，
    //webim.GROUP_MSG_SUB_TYPE.REDPACKET-红包消息，优先级最高
    var subType = webim.GROUP_MSG_SUB_TYPE.LOVEMSG;

    var msg = new webim.Msg(selSess, isSend, seq, random, msgTime, loginInfo.identifier, subType, loginInfo.identifierNick);
    var msgtosend = 'love_msg';
    var text_obj = new webim.Msg.Elem.Text(msgtosend);
    msg.addText(text_obj);

    webim.sendMsg(msg, function (resp) {
        if (selType == webim.SESSION_TYPE.C2C) { //私聊时，在聊天窗口手动添加一条发的消息，群聊时，长轮询接口会返回自己发的消息
            showMsg(msg);
        }
        webim.Log.info("点赞成功");
    }, function (err) {
        webim.Log.error("发送点赞消息失败:" + err.ErrorInfo);
        alert("发送点赞消息失败:" + err.ErrorInfo);
    });
}

//审核通过重新发送消息
function approvedSendMessage(content, info, uniqueId, msgData) {
    if (!loginInfo.identifier) { //未登录
        if (accountMode == 1) { //托管模式
            //将account_type保存到cookie中,有效期是1天
            webim.Tool.setCookie('accountType', loginInfo.accountType, 3600 * 24);
            //调用tls登录服务
            tlsLogin();
        } else { //独立模式
            alert('请填写帐号和票据');
            $('#login_dialog').show();
        }
        return;
    }

    if (!selToID) {
        alert("您还没有进入房间，暂不能聊天");
        $("#" + send_msg_text_id).val('');
        return;
    }
    //获取消息内容
    var msgLen = webim.Tool.getStrBytes(content);

    var maxLen, errInfo;
    var selType = webim.SESSION_TYPE.GROUP;
    if (selType == webim.SESSION_TYPE.GROUP) {
        maxLen = webim.MSG_MAX_LENGTH.GROUP;
        errInfo = "消息长度超出限制(最多" + Math.round(maxLen / 3) + "汉字)";
    } else {
        maxLen = webim.MSG_MAX_LENGTH.C2C;
        errInfo = "消息长度超出限制(最多" + Math.round(maxLen / 3) + "汉字)";
    }
    if (msgLen > maxLen) {
        alert(errInfo);
        return;
    }

    selSess = new webim.Session(selType, selToID, selToID, '', Math.round(new Date().getTime() / 1000));
    var isSend = true; //是否为自己发送
    var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
    var random = Math.round(Math.random() * 4294967296); //消息随机数，用于去重
    var msgTime = Math.round(new Date().getTime() / 1000); //消息时间戳
    var subType; //消息子类型
    if (selType == webim.SESSION_TYPE.GROUP) {
        //群消息子类型如下：
        //webim.GROUP_MSG_SUB_TYPE.COMMON-普通消息,
        //webim.GROUP_MSG_SUB_TYPE.LOVEMSG-点赞消息，优先级最低
        //webim.GROUP_MSG_SUB_TYPE.TIP-提示消息(不支持发送，用于区分群消息子类型)，
        //webim.GROUP_MSG_SUB_TYPE.REDPACKET-红包消息，优先级最高
        subType = webim.GROUP_MSG_SUB_TYPE.COMMON;
    } else {
        //C2C消息子类型如下：
        //webim.C2C_MSG_SUB_TYPE.COMMON-普通消息,
        subType = webim.C2C_MSG_SUB_TYPE.COMMON;
    }
    var msg = new webim.Msg(selSess, isSend, seq, random, msgTime, loginInfo.identifier, subType, loginInfo.identifierNick);
    //msg.ext='abcd';
    //解析文本和表情
    var expr = /\[[^[\]]{1,3}\]/mg;

    var emotions = content.match(expr);
    var text_obj, face_obj, tmsg, emotionIndex, emotion, restMsgIndex;

    var cmdJson = {};
    cmdJson.code = '0000';
    cmdJson.postUid = info.postUid;
    cmdJson.postNickName = info.postNickName;
    cmdJson.groupId = info.groupId;
    cmdJson.level = info.level;
    cmdJson.sendTime = new Date(msg.time * 1000);
    cmdJson.checkStatus = true;
    cmdJson.auditUid = userInfo.id;
    cmdJson.auditTime = cmdJson.sendTime;
    cmdJson.isAuditMsg = 1;
    cmdJson.uniqueId = uniqueId;
    cmdJson.isImg = info.isImg;
    cmdJson.chatImgUrl = info.chatImgUrl;
    cmdJson.sendImgUrl = info.sendImgUrl;

    var cmd_obj = new webim.Msg.Elem.Custom(JSON.stringify(cmdJson));
    msg.addCustom(cmd_obj);

    if (!emotions || emotions.length < 1) {
        text_obj = new webim.Msg.Elem.Text(content);
        msg.addText(text_obj);
    } else { //有表情
        for (var i = 0; i < emotions.length; i++) {
            tmsg = content.substring(0, content.indexOf(emotions[i]));
            if (tmsg) {
                text_obj = new webim.Msg.Elem.Text(tmsg);
                msg.addText(text_obj);
            }
            emotionIndex = webim.EmotionDataIndexs[emotions[i]];
            emotion = webim.Emotions[emotionIndex];
            if (emotion) {
                face_obj = new webim.Msg.Elem.Face(emotionIndex, emotions[i]);
                msg.addFace(face_obj);
            } else {
                text_obj = new webim.Msg.Elem.Text(emotions[i]);
                msg.addText(text_obj);
            }
            restMsgIndex = content.indexOf(emotions[i]) + emotions[i].length;
            content = content.substring(restMsgIndex);
        }
        if (content) {
            text_obj = new webim.Msg.Elem.Text(content);
            msg.addText(text_obj);
        }
    }
    webim.sendMsg(msg, function (resp) {
        if (selType == webim.SESSION_TYPE.C2C) { //私聊时，在聊天窗口手动添加一条发的消息，群聊时，长轮询接口会返回自己发的消息
            showMsg(msg);
        }
        auditChatMessage(msg, cmdJson, msgData);
        webim.Log.info("发消息成功");
        $("#" + send_msg_text_id).val('');
        //hideDiscussForm(); //隐藏评论表单
        //showDiscussTool(); //显示评论工具栏
        //hideDiscussEmotion(); //隐藏表情
    }, function (err) {
        webim.Log.error("发消息失败:" + err.ErrorInfo);
        alert("发消息失败:" + err.ErrorInfo);
    });
}

function auditChatMessage(msg, cmdJson, msgData) {
    var saveData = {};
    saveData.roomId = roomId;
    saveData.postUid = cmdJson.postUid;
    saveData.postNickName = cmdJson.postNickName;
    saveData.isSamll = 0;
    saveData.status = (cmdJson.checkStatus) ? 1 : 0;
    saveData.level = msgData.level;
    saveData.groupId = cmdJson.groupId;
    saveData.uniqueId = cmdJson.uniqueId;
    saveData.auditUid = cmdJson.auditUid;
    saveData.sendTime = msgData.sendTime;
    saveData.content = '';
    saveData.isDelete = 0;

    var msgTmp = {};
    msgTmp.uniqueId = cmdJson.uniqueId;
    msgTmp.elems = msg.elems;
    try {
        saveData.content = JSON.stringify(msgTmp);
    } catch (error) {
        return false;
    }

    var jqxhr = $.ajax({
        url: ctx + '/room/auditChatMessage',
        data: saveData
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            //popWinTips(3, data.message);
        }
    });
    jqxhr.fail(function (data) {
        //popWinTips(3, stringMsg.serverErr);
    });

}


//隐藏评论文本框
function hideDiscussForm() {
    $(".video-discuss-form").hide();
}

//显示评论文本框

function showDiscussForm() {
    $(".video-discuss-form").show();
}

//隐藏评论工具栏

function hideDiscussTool() {
    $(".video-discuss-tool").hide();
}

//显示评论工具栏
function showDiscussTool() {
    $(".video-discuss-tool").show();
}

//隐藏表情框
function hideDiscussEmotion() {
    $(".video-discuss-emotion").hide();
    //$(".video-discuss-emotion").fadeOut("slow");
}

//显示表情框
function showDiscussEmotion() {
    $(".video-discuss-emotion").show();
    //$(".video-discuss-emotion").fadeIn("slow");

}

//展示点赞动画
function showLoveMsgAnimation() {
    //点赞数加1
    var loveCount = $('#user-icon-like').html();
    $('#user-icon-like').html(parseInt(loveCount) + 1);
    var toolDiv = document.getElementById("video-discuss-tool");
    var loveSpan = document.createElement("span");
    var colorList = ['red', 'green', 'blue'];
    var max = colorList.length - 1;
    var min = 0;
    var index = parseInt(Math.random() * (max - min + 1) + min, max + 1);
    var color = colorList[index];
    loveSpan.setAttribute('class', 'like-icon zoomIn ' + color);
    toolDiv.appendChild(loveSpan);
}

//初始化表情
function initEmotionUL() {
    for (var index in webim.Emotions) {
        var emotions = $('<img>').attr({
            "id": webim.Emotions[index][0],
            "src": webim.Emotions[index][1],
            "style": "cursor:pointer;"
        }).click(function () {
            selectEmotionImg(this);
        });
        $('<li>').append(emotions).appendTo($('#emotionUL'));
    }
}

function initEmotionUL2() {
    for (var index in webim.Emotions) {
        var emotions = $('<img>').attr({
            "id": webim.Emotions[index][0],
            "src": webim.Emotions[index][1],
            "style": "cursor:pointer;"
        }).click(function () {
            $("#commentInp").val($("#commentInp").val() + this.id);
        });
        $('<li>').append(emotions).appendTo($('#emotionUL2'));
    }
}

//打开或显示表情
function showEmotionDialog() {
    if (openEmotionFlag) { //如果已经打开
        openEmotionFlag = false;
        hideDiscussEmotion(); //关闭
    } else { //如果未打开
        openEmotionFlag = true;
        showDiscussEmotion(); //打开
    }
}

//选中表情
function selectEmotionImg(selImg) {
    $("#sendMsgIpt").val($("#sendMsgIpt").val() + selImg.id);
}

//退出大群

function quitBigGroup() {
    var options = {
        'GroupId': chatRoomId //群id
    };
    webim.quitBigGroup(
        options,
        function (resp) {
            webim.Log.info('退群成功');
            $("#video_sms_list").find("li").remove();
            //webim.Log.error('进入另一个大群:'+avChatRoomId2);
            //applyJoinBigGroup(avChatRoomId2);//加入大群
        },
        function (err) {
            alert(err.ErrorInfo);
        }
    );
}

//登出
function logout() {
    //登出
    webim.logout(
        function (resp) {
            webim.Log.info('登出成功');
            loginInfo.identifier = null;
            loginInfo.userSig = null;
            $("#video_sms_list").find("li").remove();
            var indexUrl = window.location.href;
            var pos = indexUrl.indexOf('?');
            if (pos >= 0) {
                indexUrl = indexUrl.substring(0, pos);
            }
            window.location.href = indexUrl;
        }
    );
}

//点击登录按钮(独立模式)

function independentModeLogin() {
    // if ($("#login_account").val().length == 0) {
    //     alert('请输入帐号');
    //     return;
    // }
    // if ($("#login_pwd").val().length == 0) {
    //     alert('请输入UserSig');
    //     return;
    // }
    // loginInfo.identifier = $('#login_account').val();
    // loginInfo.userSig = $('#login_pwd').val();
    $('#login_dialog').hide();
    sdkLogin();
}

function sendDelCmdMsg(uniqueId, sendUid) {
    var selType = webim.SESSION_TYPE.GROUP;
    selSess = new webim.Session(selType, selToID, selToID, '', Math.round(new Date().getTime() / 1000));
    var isSend = true; //是否为自己发送
    var seq = -1; //消息序列，-1表示sdk自动生成，用于去重
    var random = Math.round(Math.random() * 4294967296); //消息随机数，用于去重
    var msgTime = Math.round(new Date().getTime() / 1000); //消息时间戳
    var subType = webim.GROUP_MSG_SUB_TYPE.COMMON;
    //消息子类型
    var msg = new webim.Msg(selSess, isSend, seq, random, msgTime, loginInfo.identifier, subType, loginInfo.identifierNick);

    var cmdJson = {};
    cmdJson.code = '0001';//删除消息指令
    cmdJson.uniqueId = uniqueId;
    cmdJson.sendUid = sendUid;
    var cmd_obj = new webim.Msg.Elem.Custom(JSON.stringify(cmdJson));
    msg.addCustom(cmd_obj);

    webim.sendMsg(msg, function (resp) {
        webim.Log.info("发送删除消息成功");
    }, function (err) {
        webim.Log.error("发送删除消息失败:" + err.ErrorInfo);
        alert("发送删除消息失败:" + err.ErrorInfo);
    });
}

/************************************************
 /* 页面操作
 ***********************************************/
function getRoomMsgBtnHtml(msg, cmdJson) {
    var htmls = '';
    if (isAdmin && isAdmin == 1) {
        htmls += [
            '<div class="check posAbs">',
            (cmdJson.checkStatus) ? '<button class="del colorF fz14" data-id="' + cmdJson.uniqueId + '" data-name="' + cmdJson.postNickName + '" onclick="delGroupMsg(this)">删除</button>' : '	 <button class="del colorF fz14" data-id="' + cmdJson.uniqueId + '" data-name="' + cmdJson.postNickName + '" onclick="delGroupMsg(this)">删除</button>   ' +
                '<button class="pass colorF fz12 ml10" data-id="' + cmdJson.uniqueId + '" onclick="agreeGroupMsg(this)">通过</button>',
            '</div>'].join('');
    }
    return htmls;
}

//点击下拉框选择禁言/拉黑操作
function op(obj) {
    $(obj).next().toggle();
}

//禁言
function banned(obj) {
    var userId = $(obj).data('uid');
    var groupId = $(obj).data('gid');
    setTimeout(function () {
        $(".worning").slideUp()
    }, 2000);
    var jqxhr = $.ajax({
        url: ctx + '/user/banned',
        data: {
            "userId": userId,
            "groupId": groupId
        }
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            $(".worning").slideDown("200");
            $(".worning").text("已禁言！");
        }
    });
}

//拉黑
function shielding(obj) {
    var userId = $(obj).data('uid');
    var groupId = $(obj).data('gid');
    setTimeout(function () {
        $(".worning").slideUp()
    }, 2000);
    var jqxhr = $.ajax({
        url: ctx + '/user/shielding',
        data: {
            "userId": userId,
            "groupId": groupId
        }
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            $(".worning").slideDown("200");
            $(".worning").text("已拉黑！");
        }
    });
}

function getHeaderHtml(groupId, level, small) {
    var htmls = '';
    switch (groupId) {
        case 1:
            htmls = '<span class="ac yk"></span>';
            break;
        case 2:
            htmls = '<i class="patrol circle ac">巡</i>';
            break;
        case 3:
            htmls = '<span class="ac zl"></span>';
            break;
        case 4:
            htmls = '<i class="teacher ac"></i>';
            break;
        case 5:
            if (level == 1) {
                htmls = '<span class="ac vip1"></span>';
            } else if (level == 2) {
                htmls = '<span class="ac vip2"></span>';
            } else if (level == 3) {
                htmls = '<span class="ac vip3"></span>';
            } else if (level == 4) {
                htmls = '<span class="ac vip4"></span>';
            } else if (level == 5) {
                htmls = '<span class="ac vip5"></span>';
            } else if (level == 6) {
                htmls = '<span class="ac vip6"></span>';
            } else if (level == 7) {
                htmls = '<span class="ac vip7"></span>';
            } else if (level == 8) {
                htmls = '<span class="ac vip8"></span>';
            }
            break;
        default:
            htmls = '<i class="visit circle ac">游</i>';
            break;
    }
    return htmls;
}

function getRoomMsgHtml(msg, cmdJson) {
    var json = msg.elems[0].content.data;
    var obj = JSON.parse(json);
    var htmls = '';
    var groupHeaders = ['', 'visit', 'patrol', 'cservice', 'cservice', 'vip'];
    if (!cmdJson)
        cmdJson = getCmdFromMsg(msg);
    if (!cmdJson) {
        return '';
    }
    if (cmdJson.code != '0000') {
        return '';
    }
    if (msg.isDelete == '1') {//已经删除的消息不显示
        if (userInfo.id != cmdJson.postUid) {
            return '';
        }
    }
    if (cmdJson.checkStatus == '0' && isAdmin == 0) {
        if (userInfo.id != cmdJson.postUid) {
            return '';
        }
    }
    if (msg.status && msg.status == 2) {
        return '';
    }
    if ($('#msginfo-' + cmdJson.uniqueId).length > 0) {
        return '';
    }
    if (cmdJson.isSpecial == 0) {
        if (cmdJson.type == 0) {
            htmls += [
                '<div class="info posRel" id="msginfo-' + cmdJson.uniqueId + '">',
                '<div class="msgLine posRel">',
                getHeaderHtml(cmdJson.groupId, cmdJson.level, cmdJson.small),
                '<span class="msgName ml10 ' + (cmdJson.groupId == 1 ? 'greenFont' : '') + ' ' + (cmdJson.groupId == 3 ? 'redFont' : '') + ' ' + (cmdJson.groupId == 4 ? 'redFont' : '') + ' ' + (cmdJson.groupId == 5 ? 'greenFont' : '') + '">' + cmdJson.postNickName + '</span>',
                '<div class="ilblock posRel ml10">',
                // '<div class="msgInfo" style="margin-bottom:4px;">',
                (cmdJson.groupId == 3 || cmdJson.groupId == 4) ? '<div class="msgInfo zlBg" style="margin-bottom:4px;">' : '<div class="msgInfo" style="margin-bottom:4px;"> ',
                '<span></span> <img src="../static/images/small_redpack.png" alt="">',
                '</div></div></div></div>'].join('');
        } else {
            htmls += [
                '<div class="info posRel" id="msginfo-' + cmdJson.uniqueId + '">',
                '<div class="msgLine posRel">',
                getHeaderHtml(cmdJson.groupId, cmdJson.level, cmdJson.small),
                '<span class="msgName ml10 ' + (cmdJson.groupId == 1 ? 'greenFont' : '') + ' ' + (cmdJson.groupId == 3 ? 'redFont' : '') + ' ' + (cmdJson.groupId == 4 ? 'redFont' : '') + ' ' + (cmdJson.groupId == 5 ? 'greenFont' : '') + '">' + cmdJson.postNickName + '</span>',
                '<div class="ilblock posRel ml10">',
                // '<div class="msgInfo" style="margin-bottom:4px;">',

                (cmdJson.groupId == 3 || cmdJson.groupId == 4) ? '<div class="msgInfo zlBg" style="margin-bottom:4px;">' : '<div class="msgInfo" style="margin-bottom:4px;"> ',


                '  <span> </span> <img src="../static/images/small_flower.png" alt="">',


                '</div></div></div></div>'].join('');
        }
    } else {
        if (cmdJson.isImg == 1) {//如果发送的是图片, 直接去附加参数里的图片地址
            htmls += [
                '<div class="info posRel" id="msginfo-' + cmdJson.uniqueId + '">',
                '<div class="msgLine posRel">',
                getHeaderHtml(cmdJson.groupId, cmdJson.level, cmdJson.small),
                '<span class="msgName ml10 ' + (cmdJson.groupId == 1 ? 'greenFont' : '') + ' ' + (cmdJson.groupId == 3 ? 'redFont' : '') + ' ' + (cmdJson.groupId == 4 ? 'redFont' : '') + ' ' + (cmdJson.groupId == 5 ? 'greenFont' : '') + '">' + cmdJson.postNickName + '</span>',
                '<div class="ilblock posRel ml10">',
                (userInfo.groupId == 3 && (cmdJson.groupId == 1 || cmdJson.groupId == 5)) ? ' <button class="op" onclick="op(this)"></button>' : '',
                '<div class="posAbs selectBtn hide" style="left:32px;top:1px;" >',
                '<a style="cursor: pointer" data-uid="' + cmdJson.postUid + '" data-gid="' + cmdJson.groupId + '" onclick="banned(this)">禁言</a>',
                '<a style="cursor: pointer;background-color:#622717;" data-uid="' + cmdJson.postUid + '" data-gid="' + cmdJson.groupId + '" onclick="shielding(this)">拉黑</a>',
                '<a style="cursor: pointer" data-uid="' + cmdJson.postUid + '" data-name="' + cmdJson.postNickName + '" onclick="atHe(this)">@他</a>',
                '</div></div></div>',
                '<div class="msgInfo">' + cmdJson.sendImgUrl + '</div>',
                getRoomMsgBtnHtml(msg, cmdJson),
                '</div>'].join('');
        } else {
            htmls += [
                '<div class="info posRel" id="msginfo-' + cmdJson.uniqueId + '">',
                '<div class="msgLine posRel">',
                getHeaderHtml(cmdJson.groupId, cmdJson.level, cmdJson.small),
                '<span class="msgName ml10 ' + (cmdJson.groupId == 1 ? 'greenFont' : '') + ' ' + (cmdJson.groupId == 3 ? 'redFont' : '') + ' ' + (cmdJson.groupId == 4 ? 'redFont' : '') + ' ' + (cmdJson.groupId == 5 ? 'greenFont' : '') + '">' + cmdJson.postNickName + '</span>',
                '<div class="ilblock posRel ml10">',
                (userInfo.groupId == 3 && (cmdJson.groupId == 1 || cmdJson.groupId == 5)) ? ' <button class="op" onclick="op(this)"></button>' : '',
                '<div class="posAbs selectBtn hide" style="left:32px;top:1px;" >',
                '<a style="cursor: pointer" data-uid="' + cmdJson.postUid + '" data-gid="' + cmdJson.groupId + '" onclick="banned(this)">禁言</a>',
                '<a style="cursor: pointer;background-color:#622717;" data-uid="' + cmdJson.postUid + '" data-gid="' + cmdJson.groupId + '" onclick="shielding(this)">拉黑</a>',
                '<a style="cursor: pointer" data-uid="' + cmdJson.postUid + '" data-name="' + cmdJson.postNickName + '" onclick="atHe(this)">@他</a>',
                '</div></div></div>',

                (cmdJson.groupId == 3 || cmdJson.groupId == 4) ? '<div class="msgInfo zlBg">' : '<div class="msgInfo maxImg">',

                convertMsgtoHtml(msg),
                '</div>',
                getRoomMsgBtnHtml(msg, cmdJson),
                '</div>'].join('');
        }
    }
    return htmls;
}

function getRommMsgListHtml(msgList) {
    var htmls = '';
    for (var i = 0; i < msgList.length; i++) {
        var record = obj[i];
        htmls += getRoomMsgHtml(record);
    }
    return htmls;
}

function removeMsgInfo(uniqueId, sendUid) {
    var $msgInfo = $('#msginfo-' + uniqueId);
    if ($msgInfo.length > 0) {
        if (sendUid && sendUid != userInfo.id) {
            $msgInfo.remove();
        }
    }
}


/************************************************
 /* 数据操作
 ***********************************************/

function getMsgFromContent(content) {
    var msg = null;
    if (!content && content == '') {
        return false;
    }
    try {
        msg = JSON.parse(content);
    } catch (error) {
        return false;
    }
    if (msg == null) {
        return false;
    }
    return msg;
}

//保存聊天记录
function saveGroupMsg(msg) {
    var cmdJson = getCmdFromMsg(msg);
    if (!cmdJson) {
        return false;
    }
    if (cmdJson.code != '0000') {
        return false;
    }
    var saveData = {};
    saveData.roomId = roomId;
    saveData.postUid = cmdJson.postUid;
    saveData.postNickName = cmdJson.postNickName;
    saveData.isSamll = 0;
    saveData.status = (cmdJson.checkStatus) ? 1 : 0;
    saveData.level = cmdJson.level;
    saveData.groupId = cmdJson.groupId;
    saveData.uniqueId = msg.uniqueId;
    saveData.auditUid = cmdJson.auditUid;
    saveData.content = '';
    saveData.isDelete = 0;

    var msgTmp = {};
    msgTmp.uniqueId = msg.uniqueId;
    msgTmp.elems = msg.elems;
    try {
        saveData.content = JSON.stringify(msgTmp);
    } catch (error) {
        return false;
    }
    var jqxhr = $.ajax({
        url: ctx + '/room/saveChatMessage',
        data: saveData
    });
    jqxhr.done(function (data) {
        if (data.code != '1000') {
            //popWinTips(3, data.message);
        }
    });
    jqxhr.fail(function (data) {
        //popWinTips(3, stringMsg.serverErr);
    });
}

//消息审核通过
function agreeGroupMsg(obj) {
    var uniqueId = $(obj).data('id');
    var jqxhr = $.ajax({
        url: ctx + '/room/getChatMessageByUniqueId',
        data: {uniqueId: uniqueId}
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            var msgData = data.data;
            var msg = getMsgFromContent(msgData.content);
            if (msg) {
                removeMsgInfo(uniqueId);
                var cmdJson = getCmdFromMsg(msg);
                cmdJson.checkStatus = true;
                cmdJson.auditUid = userInfo.id;
                var content = '';
                for (var i = 1; i < msg.elems.length; i++) {
                    var flag = msg.elems[i].type;
                    if (flag == 'TIMFaceElem') {
                        content += msg.elems[i].content.data;
                    } else {
                        content += msg.elems[i].content.text;
                    }
                }
                approvedSendMessage(content, cmdJson, uniqueId, msgData);
            }
        }
    });
    jqxhr.fail(function (data) {
        console.log(data);
        console.log("审核出错.");
    });
}

//消息删除
function delGroupMsg(obj) {
    var name = $(obj).data('name');
    var uniqueId = $(obj).data('id');
    var jqxhr = $.ajax({
        url: ctx + '/room/deleteChatMessage',
        data: {uniqueId: uniqueId, postNickName: name}
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            sendDelCmdMsg(uniqueId, data.data);
            removeMsgInfo(uniqueId, data.data);
        }
    });
    jqxhr.fail(function (data) {
        removeMsgInfo(uniqueId);
    });
}

function qqCustomer(qq) {
    $("body").find("iframe").eq(0).attr("src", "tencent://message/?uin=" + qq + "");
}


function getNowFormatDate(date) {
    if (typeof (date) == "undefined") {
        var now = new Date();
        // var year = now.getFullYear();       //年
        // var month = now.getMonth() + 1;     //月
        // var day = now.getDate();            //日
        var hh = now.getHours();            //时
        var mm = now.getMinutes();          //分
        // var ss = now.getSeconds();           //秒
        var clock = "";
        // var clock = year + "-";
        // if (month < 10) {
        //     clock += "0";
        // }
        // clock += month + "-";
        // if (day < 10) {
        //     clock += "0";
        // }
        // clock += day + " ";
        // if (hh < 10) {
        //     clock += "0";
        // }
        clock += hh + ":";
        if (mm < 10) {
            clock += '0';
        }
        clock += mm;

        // if (ss < 10) {
        //     clock += '0';
        // }
        // clock += ss;
        return (clock);
    } else {
        var str = date.substring(11, 16);
        return str;
    }
}

$(document).keyup(function (e) {
    if (e.keyCode == 13) {
        var msg = $('#sendMsgIpt').val();
        if (msg.trim() == '') {
            return;
        }
        var msg = $('#sendMsgIpt').val();
        if (msg.trim() == '') {
            return;
        }
        if (isCanSend) {
            onSendMsg();
        }
        $('#sendMsgIpt').val('').focus();
        e.returnValue = false;
        e.preventDefault();
    }
});

function atHe(obj) {
    var isSmall = $('#small').val();
    if (isSmall != 0) {
        alert('不能发送@消息.');
        return;
    }
    var nickName = $(obj).data('name');
    $('#sendMsgIpt').val(nickName + " ");
    $('#atHeInput').val(nickName);
}

//发送消息之后, 禁用按钮, 倒计时3秒
function countDownSendMsgBtn() {
    isCanSend = false;
    var i = 2;
    document.getElementById('sendMsgBtn').disabled = true;
    $('#sendMsgBtn').css('background-color', "#C7C7C7");
    $('#sendMsgBtn').html('发送 (' + 3 + ')');
    var myVar = setInterval(function () {
        if (i == 0) {
            $('#sendMsgBtn').html('发送');
            window.clearInterval(myVar);
            isCanSend = true;
            i = 2;
        } else {
            isCanSend = false;
            $('#sendMsgBtn').html('发送 (' + i-- + ')');
        }
    }, 1000);
    setTimeout(function () {
        document.getElementById('sendMsgBtn').disabled = false;
        $('#sendMsgBtn').css('background-color', "#fb403c");
    }, 3000);
}

//检测消息是否在黑白名单
function checkMsg(msg) {
    var flag = 0;
    var jqxhr = $.ajax({
        url: ctx + '/room/checkMsg',
        async: false,
        data: {
            "word": msg
        }
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            flag = data.data;
        }
    });
    return flag;
}

/**
 * 发送鲜花
 */
$(".roseF").click(function () {
    sendFlowerMsg();
});

/**
 * 发送红包
 */
$(".redBagF").click(function () {
    sendRedBagMsg();
});

/**
 * 隐藏掉表情中的鲜花和红包

 setTimeout(function () {
    // 隐藏鲜花
    $('#emotionUL li').last().prev().css('display', 'none');
    // 隐藏红包
    $('#emotionUL li').last().css('display', 'none');
}, 500);
 */

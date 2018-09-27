$(function () {
    //游客弹出水滴窗口
    if (userInfo.groupId == 1) {
        setTimeout(function () {
            $(".waterPersionBox").removeClass('hide');
            $('#waterPersionChatBox').scrollTop($('#waterPersionChatBox')[0].scrollHeight);//滚动条到最底部
        }, 15000)
    }
    // 游客听课3分钟后弹出
    if (userInfo.groupId == 1) {
        setTimeout(function () {
            $(".tkTimePopMask").removeClass('hide');
        }, 180000)
    }

    //vip每一分钟请求后台判断是否被拉黑状态，模拟单点登陆
    if (userInfo.groupId == 1) {
        setInterval(function () {
            checkTourseIsBlack(userInfo.id);
        }, 60000);
    }

    if (userInfo.groupId == 5) {
        setInterval(function () {
            checkVipIsBlack(userInfo.id);
        }, 60000);
    }


    var pifu = localStorage.getItem('class') || "changeBg1";
    $("body").attr("class", pifu);
    // 换肤
    $(".changeBg").on('click', 'a', function () {
        var thisClass = $(this).attr('class');
        $("body").attr("class", thisClass);
        $(".huanFu a").attr("class", thisClass);
        localStorage.setItem("class", thisClass);
    });

    $(".jinshiBtn").click(function () {
        $(".jinshi").toggle();
    });
    $(".jinshiClose").click(function () {
        $(".jinshi").hide();
    })

    if (userInfo.groupId == 3 || userInfo.groupId == 4) {
        getOnlineUserList();
    }

    $('#loginPhone').val(cookieFunction.get('loginNameCache'));
    $('#loginPwd').val(cookieFunction.get('passwordCache'));

    /****关闭弹窗****/
    $(".close").click(function () {
        $(this).parent().hide();
        $("#pop").hide();
    });


    /********点击红包*******/
    $("#redBagC").click(function () {
        $("#pop").show();
        $("#pop .red").show();
    });


    /*******点击抽奖*******/
    $("#draw").click(function () {
        $("#pop").show();
        $("#pop .lottery").show();
    });

    /******右键 拉黑禁言恢复***********/
    $(".msgName").click(function (e) {
        var html = createRightKey();
        $('.controls').remove();
        $(this).parent().parent().append(html);
    });
    /*******所有留言*********/
    $("#allComment").click(function (e) {
        $(this).siblings().removeClass("cur");
        $(this).addClass("cur");
        $(".myComment").hide();
        $(".allComment").show();

    });
    /*******我的留言*********/
    $("#myComment").click(function (e) {
        $(this).siblings().removeClass("cur");
        $(this).addClass("cur");
        $(".allComment").hide();
        $(".myComment").show();

    });

    $("#iWComment").click(function (e) {
        $("#commentInp").focus();
    });

    /*********留言板 老师介绍********/
    $(".otherMenu li").click(function (e) {
        $(this).siblings().removeClass("cur");
        $(this).addClass("cur");
    });

    $("#otherMBoard").click(function () {
        $(".comment").show();
        $(".coursePlay").hide();
    })


    /********公告*********/
    $(".trig").click(function (e) {
        var srcName = $(this).find('img').attr("src");
        if (srcName.indexOf('up') >= 0) {
            $(this).find('img').attr("src", "../static/images/trig.png");
            $(".noticeList").hide();
        } else {
            $(this).find('img').attr("src", "../static/images/up.png");
            $(".noticeList").show();
        }
    });

    /********表情******/
    $(".faceF").click(function (e) {
        $('#emotionUL li').last().prev().css('display', 'none');
        // 隐藏红包
        $('#emotionUL li').last().css('display', 'none');
        $('.expressionT').toggle();
    });

    $('.expressionT').click(function () {
        $('.expressionT').toggle();
    });


    $("#commentFace").click(function (e) {
        $('.faceEmotion2').show();
    });

    $(".settingA").click(function (e) {
        $('.LoginOut').toggle();
    });


    $(document).bind('click', function () {
        var e = e || window.event; //浏览器兼容性
        var elem = e.target || e.srcElement;
        while (elem) { //循环判断至跟节点，防止点击的是div子元素
            if (elem.id && elem.id == 'faceF') {
                return;
            }
            if (elem.id == 'commentFace') {
                return;
            }
            elem = elem.parentNode;
        }
        $('.faceEmotion').hide();
        $('.faceEmotion2').hide();
    });

    /********登录*********/
    var $loginForm = $('#loginForm');
    $loginForm.validate({
        submitHandler: function () {
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        cookieFunction.set('loginNameCache', $('#loginPhone').val());
                        cookieFunction.set('passwordCache', $('#loginPwd').val());
                        window.location.reload();
                    } else {
                        popLayer(data.message);
                    }
                    formSubmit("#login", false);
                },
                error: function (data) {
                    popLayer(stringMsg.serverErr);
                    formSubmit("#login", false);
                },
                beforeSubmit: function () {
                    var phone = $("#loginPhone").val();
                    var pwd = $("#loginPwd").val();
                    if (phone.trim() == "" || phone.trim().length < 0) {
                        popLayer("请输入手机号码！");
                        return false;
                    }
                    if (pwd.trim() == "" || pwd.trim().length < 0) {
                        popLayer("请输入密码！");
                        formSubmit("#login", false);
                    }
                }
            };
            $loginForm.ajaxSubmit(options);
        }
    });


    /********注册*********/
    var $registerForm = $('#registerForm');
    $registerForm.validate({
        submitHandler: function () {
            formSubmit("#register", true);
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
//  	                    window.location.href = g_requestContextPath + "/user/userRegistered";
                        popLayer("注册成功！");
                        setTimeout(function () {
                            $("#registerPhone").parent().parent().hide();
                            $("#pop").hide();
                        }, 1000);
                    } else {
                        popLayer(data.message);
                    }
                    formSubmit("#register", false);
                },
                error: function (data) {
                    popLayer(stringMsg.serverErr);
                    formSubmit("#register", false);
                },
                beforeSubmit: function () {
                    var phone = $("#registerPhone").val();
                    var pwd = $("#registerPwd").val();
                    var errorMsg = $("#registerPwd").parent().find('.errorMsg');
                    if (phone == "" || phone.length < 0) {
                        $(errorMsg).text("请输入手机号码！");
                        $(errorMsg).show();
                        return false;
                    }
                    if (pwd == "" || pwd.length < 0) {
                        $(errorMsg).show();
                        $(errorMsg).text("请输入密码！");
                        return false;
                    }
                }
            };
            $registerForm.ajaxSubmit(options);
        }
    });


    /******修改密码*******/
    var $modifyForm = $('#modifyForm');
    $modifyForm.validate({
        submitHandler: function () {
            formSubmit("#submitBtn", true, "保存中...");
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        window.location.reload();
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
            $modifyForm.ajaxSubmit(options);
        }
    });

    /******查询公告*******/
    var jqxhr = $.ajax({
        url: ctx + "/room/getRoomAnnouncementList",
        data: {page: 1, pageSize: 10},
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            var msgList = data.data.rows;
            var htmls = '';
            if (msgList && msgList.length > 0) {
                for (var i = 0; i < msgList.length; i++) {
                    var content = msgList[i].content;
                    htmls += content;
                }
            }
            if (htmls != '') {
                $(".noticeList").prepend(htmls);
            }
        }
    });

    /******退出登录*******/
    $('.outBtn').on("click", function () {
        var jqxhr = $.ajax({
            url: ctx + "/user/userLoginOut",
        });
        jqxhr.done(function (data) {
            if (data.code == '1000') {
                window.location.reload();
            }
        });
    });

    // if(userInfo.groupId == 3 || userInfo.groupId == 4){
    //     $('.file').css("display","inline-block");
    //     $('.emoticonIcon').css("width","53%");
    //     $('#small').click(function () {
    //         var options = $('#small option:selected').val();
    //         if(options != 0){
    //             $('.file').css("display","none");
    //         }else{
    //             $('.file').css("display","inline-block");
    //         }
    //     });
    // }
    // 聊天图片发送
    $('#msgImage').change(function () {
        var $sendChatImgForm = $('#sendChatImgForm');
        var options = {
            success: function (data) {
                if (data.code == '1000') {
                    bigImage = data.data;
                    sendImgMsg(data.data);
                } else {
                    alert(data.message);
                }
                $('#msgImage').val(null);
            },
        };
        $sendChatImgForm.ajaxSubmit(options);
    });


    /******留言板*******/
    $('#otherMBoard').on("click", function () {
        $('.boardF').show();
        $('.teacherF').hide();
        $('.courseF').hide();
        $('.fileF').hide();
        $('.liveF').hide();
    });

    /******老师介绍*******/
    $('#otherTeachI').on("click", function () {
        $('.teacherF').show();
        $('.boardF').hide();
        $('.courseF').hide();
        $('.fileF').hide();
        $('.liveF').hide();
    });

    /******课程安排*******/
    $('#otherCourseP').on("click", function () {
        $('.courseF').show();
        $('.boardF').hide();
        $('.teacherF').hide();
        $('.fileF').hide();
        $('.liveF').hide();
    });

    /******课件下载*******/
    $('#otherDownF').on("click", function () {
        $('.fileF').show();
        $('.boardF').hide();
        $('.teacherF').hide();
        $('.courseF').hide();
        $('.liveF').hide();
    });

    /******直播室介绍*******/
    $('#otherZBI').on("click", function () {
        $('.liveF').show();
        $('.boardF').hide();
        $('.teacherF').hide();
        $('.courseF').hide();
        $('.fileF').hide();
    });


    //客服
    $('.service').on('click', function () {
        $('.waterPersionBox').removeClass('hide');
        $('#waterPersionChatBox').scrollTop($('#waterPersionChatBox')[0].scrollHeight);//滚动条到最底部
    });

    /******在线人数*******/
    setInterval(function () {
        getOnlineUser(roomId);
    }, 30000);


    // 换肤悬停
    $(".changeBgWra").hover(function () {
        $(".changeBg").stop().animate({
            opacity: "1",
            right: '100px'
        }, 500);
    }, function () {
        $(".changeBg").stop().animate({
            opacity: "0",
            right: '-220'
        }, 500);
    });


    $(".msg").on("click", '.img_big', function () {
        $(".imgMask").show();
        var imgUrl = $(this).attr("src");
        $(".bigImg img").attr("src", imgUrl);
    });

    $(".bigImgClose").click(function () {
        $(".imgMask").hide();
    });


    //助理自动发言
    setInterval(function () {
        if (userInfo.groupId == 3) {
            autoMsg();
        }
    }, 5000);

    function autoMsg() {
        $.ajax({
            url: ctx + "/live/autoMsg",
            data: {"userId": userInfo.id},
            success: function (data) {
                sendAutoMsg(data.data.content, data.data.name, data.data.level);
            }
        });
    }


    /*当前在线人记录缓存(10秒/次)*/
    setInterval(function () {
        //服务器缓存在线人数
        initRedisUser(userInfo.id, userInfo.groupId);
        //定时读取游客/vip列表
        getOnlineUserList();
        //检查是否可以继续观看
        if (userInfo.groupId == 1 || userInfo.groupId == 5) {
            checkCanWatch();
        }
    }, 10000);

    // 助理点击更多
    // var htmlNode = $(".qqWra .assistantQQ");
    // $('.more').click(function () {
    //     var _this = $(this);
    //     if (_this.hasClass("open")) {
    //         _this.removeClass("open");
    //         autoWindow(2, 4);
    //     } else {
    //         autoWindow(htmlNode.length, htmlNode.length);
    //         _this.addClass("open");
    //         $(".serviceList").addClass("w1500")
    //     }
    // });

    // 助理显示个数
    // function none(num) {
    //     // console.log(htmlNode);
    //     htmlNode.map(function (item, index) {
    //         index.style.display = 'inline-block';
    //         if (item > num) {
    //             index.style.display = 'none';
    //         }
    //     })
    // }

    //窗口自适应
    autoWindow();

    function autoWindow(num1, num2) {
        var win = $(window);
        var screenWidth = win.width();
        // if (!num1) num1 = 2;
        // if (!num2) num2 = 4;
        // console.log(screenWidth)
        if (screenWidth <= 1600) {
            $(".topLeft").attr('class', "headBtns topLeft fl");
            $(".headBtns .collectionBtn").css('width', "7.75%");
            // $(".headBtns .downUrlBtn").css('width', "10.5%");
            $(".logo").addClass("logo02");

            $(".serviceList").attr('class', "serviceList clearfix posRel w1400");
            $(".rq_ewm2").show();
            $(".rq_ewm").hide();
            $(".popularGuest h3").hide();
            $(".rq_btn a img").css("width", "60%");
            // none(num1);
        } else if (screenWidth <= 1700) {
            // none(3);
            $(".serviceList").attr('class', "serviceList clearfix posRel w1700");
            $(".rq_ewm2").show();
            $(".rq_ewm").hide();
            $(".popularGuest h3").hide();
        } else {
            // none(num2);
            $(".topLeft").attr('class', "topLeft fl");
            $(".collectionBtn").css('width', "auto");
            // $(".downUrlBtn").css('width', "auto");
            $(".logo").removeClass("logo02");

            $(".serviceList").attr('class', "serviceList clearfix posRel w1800");
            $(".rq_ewm").show();
            $(".rq_ewm2").hide();
        }

        var winHeight = win.height();
        var serviceListHeight = $('.contentRight .serviceList').outerHeight(true);
        var messageBoxHeight = $('.contentRight .messageBox').outerHeight(true);
        var tishiHeight = $('.tishi').outerHeight(true);
        var videoHeight = winHeight - 40 - 150 - 60 - 46;        // 40 footer; 150 banner margin; 60 header margin; 46 viewTime;
        var contentRightMsg = winHeight - 60 - serviceListHeight - messageBoxHeight - 40 - 30 - tishiHeight;
        var newLeft = winHeight - 60 - 40;

        if (winHeight < 800) {
            $('.slides').height(100);
            $('.slides .slideInner a').height(100);
            $('.contentLeft .movie .toLogin').height(videoHeight + 40);  // 视频高度
        } else {
            $('.slides').height(140);
            $('.slides .slideInner a').height(140);
            $('.contentLeft .movie .toLogin').height(videoHeight);  // 视频高度
        }
        $('.contentRight .msg').height(contentRightMsg);                       // 右侧栏字幕高度
        $('.left_left, .left_middle, .left_left .visitors').height(newLeft);
        $('.left_left .visitorsCon').height(newLeft - 60)
        // console.log('serviceListHeight', serviceListHeight, contentRightMsg, messageBoxHeight, winHeight);
    }

    //浏览器大小改变, 触发事件
    window.onresize = function () {
        autoWindow();
    };
});


/****水滴tab切换****/
var btns = $(".tabBtns .btn");
var child = $(".tabCon .tab_title");
btns.each(function (index) {
    $(this).click(function () {
        $(btns).removeClass("cur");
        $(child).hide()
        $(this).addClass('cur');
        $(child).eq(index).show()
    })
});

/****拼接喊单****/
function appendHD(content, time) {
    var html = '';
    html += '<p>';
    html += content;
    html += '<span class="fr mr5">';
    html += time;
    html += '</span>';
    html += "</p>";
    return html;
}

/****拼接右键菜单***/
function createRightKey() {
    var html = '';
    html += '<div class="controls posAbs">';
    html += '<a class="NoSpeaking">禁言</a>';
    html += '<a class="toBlackRoom">拉黑</a>';
    html += '<a class="revert">恢复</a>';
    html += '</div>';
    return html;
}

/**展示窗体**/
function toShow(name, zname) {
    $("#pop").hide();
    if (typeof(zname) != "undefined") {
        $("#pop").find("." + zname).hide();
    }
    var z_name = "." + name;
    var $login = $("#pop").find(z_name);
    $("#pop").show();
    $(z_name).show();
}

/**获取观看人数*/
function getOnlineUser(roomId) {
    $.ajax({
        url: ctx + "/live/getOnlineUser",
        data: {"roomId": roomId},
        success: function (data) {
            if (data.code == '1000') {
                $('.onlineNum').html(data.data);
            }
        }
    });
}

/* 每10秒请求一次, 记录当前人的身份和id*/
function initRedisUser(userId, groupId) {
    $.ajax({
        url: ctx + "/live/initRedisUser",
        data: {"userId": userId, "groupId": groupId},
        success: function (data) {
            console.log("记录用户身份已执行.");
        }
    });
}

/* 每10秒请求一次, 获取当前在线的人数*/
function getOnlineUserList() {
    $.ajax({
        url: ctx + "/live/getOnlineUserList",
        data: {"flag": type_flag},
        success: function (data) {
            if (type_flag == 0) {
                var htmls = '';
                for (var i = 0; i < data.data.length; i++) {
                    htmls += '<li><a>';
                    htmls += getIcon(data.data[i].groupId, data.data[i].userLevel);
                    htmls += '<p>' + data.data[i].userNickName + '</p>';
                    htmls += '</a></li>';
                }
                $('.vipOnlineList').html(htmls);
            } else {
                var htmls = '';
                for (var i = 0; i < data.data.length; i++) {
                    htmls += '<li><a>';
                    htmls += getIcon(data.data[i].groupId, data.data[i].userLevel);
                    htmls += '<p>' + data.data[i].userNickName + '</p>';
                    htmls += '</a></li>';
                }
                $('.ykOnlineList').html(htmls);
            }
            console.log("获取当前在线的人数已执行.");
        }
    });
}

//清屏
$('.clearScreen').click(function () {
    $('.msg').html('');
});

//保存到桌面
function downUrl() {
    document.location.href = '/live/downURL';
}

//收藏
function collection(sTitle, sURL) {
    try {
        window.external.addFavorite(sURL, sTitle);
    }
    catch (e) {
        try {
            window.sidebar.addPanel(sTitle, sURL, "");
        }
        catch (e) {
            alert("加入收藏失败，请使用Ctrl+D进行添加");
        }
    }
}

// 课程安排
function classShow(show) {
    if (show) {
        $('#courseplanArea').hide();
    } else {
        $('#courseplanArea').show();
    }
}

// 轮播图
$(".slideInner").slide({
    slideContainer: $('.slideInner a'),
    effect: 'easeOutCirc',
    autoRunTime: 5000,
    slideSpeed: 1000,
    nav: true,
    autoRun: true
});


function popImg() {
    $(".popMask").show();
}

// setTimeout(function () {
//     $(".popMask").hide();
// }, 5000)

$(".closeImg").click(function () {
    $(".popMask").hide();
});
$(".timeClose").click(function () {
    $(".timeOut").hide();
});

$(".tkClose").click(function () {
    $(".tkTimePopMask").hide();
});

// 水滴拖动
var Dragging = function (validateHandler) { //参数为验证点击区域是否为可移动区域，如果是返回欲移动元素，负责返回null
    var draggingObj = null; //dragging Dialog
    var diffX = 0;
    var diffY = 0;

    function mouseHandler(e) {
        switch (e.type) {
            case 'mousedown':
                draggingObj = validateHandler(e);//验证是否为可点击移动区域
                // console.log(draggingObj,draggingObj.offsetLeft);
                if (draggingObj != null) {
                    diffX = e.clientX - draggingObj.offsetLeft;
                    diffY = e.clientY - draggingObj.offsetTop;
                }
                break;

            case 'mousemove':
                // console.log(e.clientY-diffY - parseInt($('.waterDrop').css('margin-top')));
                if (draggingObj && e.clientY - diffY - parseInt($('.waterDrop').css('margin-top')) > 130) {
                    draggingObj.style.left = (e.clientX - diffX - parseInt($('.waterDrop').css('margin-left'))) + 'px';
                    draggingObj.style.top = (e.clientY - diffY - parseInt($('.waterDrop').css('margin-top'))) + 'px';

                }
                break;

            case 'mouseup':
                draggingObj = null;
                diffX = 0;
                diffY = 0;
                break;
        }
    };

    return {
        enable: function () {
            document.addEventListener('mousedown', mouseHandler);
            document.addEventListener('mousemove', mouseHandler);
            document.addEventListener('mouseup', mouseHandler);
        },
        disable: function () {
            document.removeEventListener('mousedown', mouseHandler);
            document.removeEventListener('mousemove', mouseHandler);
            document.removeEventListener('mouseup', mouseHandler);
        }
    }
}

function getDraggingDialog(e) {
    var target = e.target;
    while (target && target.className.indexOf('logo') == -1) {
        target = target.offsetParent;
    }
    if (target != null) {
        return target.offsetParent;
    } else {
        return null;
    }
}

Dragging(getDraggingDialog).enable();

$(".visitorsBtn a").each(function (index) {
    $(this).click(function () {
        $(".visitorsBtn a").removeClass("cur");
        $(".visitorsCon .visitors_list").hide();
        $(this).addClass('cur');
        $(".visitorsCon .visitors_list").eq(index).show();
        type_flag = index;
        getOnlineUserList();
    });
});

// 老师介绍
function teacherJs() {
    $(".teacherJsMask").show();
}

$(".teacherJsClose").click(function () {
    $(".teacherJsMask").hide();
});

function movieHide() {
    $(".wrap").toggleClass("movieHide")
}

function redBag() {
    $.ajax({
        url: ctx + "/activity/isReceive",
        success: function (data) {
            if (data.code == '1000') {
                $('#g17').hide();
                $('#g19').show();
                $('.redBagMoney').show();
                $('.redBagTime').hide();
                $('.redBagImg').hide();
                $('.moneyBg').html('<p>' + data.data.money + '<span>元</span></p>');
                $(".redBagMask").show();
            } else {
                $(".redBagMask").show();
            }
        }
    });

}

//关闭红包按钮
$(".redBagClose").click(function () {
    $(".redBagMask").hide();
});

//老师点赞
function pariseTeacher(obj) {
    var userId = $(obj).data("id");
    $.ajax({
        url: ctx + "/user/pariseTeacher",
        data: {
            "userId": userId,
            "praiseUserId": userInfo.id,
            "praiseUserGroupId": userInfo.groupId
        },
        success: function (data) {
            if (data.code == '1000') {
                $(obj).find('em').html(data.data);
            } else {
                popLayer(data.message);
            }
        }
    });
}

//发送红包消息
function sendRedPackMsg() {
    $(".redBagF").unbind();
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
    cmdJson.code = '9000';//红包消息指令
    if (isAdmin == '1') {
        var smallName = $("#small").find("option:selected").text();
        cmdJson.name = smallName;
    } else {
        cmdJson.name = userInfo.nickName;
    }
    var cmd_obj = new webim.Msg.Elem.Custom(JSON.stringify(cmdJson));
    msg.addCustom(cmd_obj);
    webim.sendMsg(msg, function (resp) {
        webim.Log.info("发送红包消息成功");
    }, function (err) {
        webim.Log.error("发送红包消息失败:" + err.ErrorInfo);
        alert("发送红包消息失败:" + err.ErrorInfo);
    });
}


//发送玫瑰消息
function sendRoseMsg() {
    $(".roseF").unbind();
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
    cmdJson.code = '9001';//玫瑰消息指令
    if (isAdmin == '1') {
        var smallName = $("#small").find("option:selected").text();
        cmdJson.name = smallName;
    } else {
        cmdJson.name = userInfo.nickName;
    }
    var cmd_obj = new webim.Msg.Elem.Custom(JSON.stringify(cmdJson));
    msg.addCustom(cmd_obj);
    webim.sendMsg(msg, function (resp) {
        webim.Log.info("发送玫瑰消息成功");
    }, function (err) {
        webim.Log.error("发送玫瑰消息失败:" + err.ErrorInfo);
        alert("发送玫瑰消息失败:" + err.ErrorInfo);
    });
}

//判断是否被拉黑
function checkVipIsBlack(userId) {
    $.ajax({
        url: g_requestContextPath + '/user/checkVipIsBlack',
        data: {
            userId: userId
        },
        success: function (data) {
            if (data.code == 9999) {
                window.location.href = ctx + '/error/404?roomId=' + roomId + '&userId=' + userInfo.id + '&groupId=' + userInfo.groupId;
            } else {
                console.log("正常用户.");
            }
        }
    });
}

function checkTourseIsBlack(userId) {
    $.ajax({
        url: g_requestContextPath + '/user/checkTourseIsBlack',
        data: {
            userId: userId
        },
        success: function (data) {
            if (data.code == 9999) {
                window.location.href = ctx + '/error/404?roomId=' + roomId + '&userId=' + userInfo.id + '&groupId=' + userInfo.groupId;
            } else {
                console.log("正常用户.");
            }
        }
    });
}


// 转盘显示隐藏
function zpShow() {
    $(".rotateWin").css("visibility", "visible");
    // 领奖显示
    if (userInfo.groupId == 1) {
        $.ajax({
            url: ctx + "/activity/prizeInfo",
            data: {
                "roomId": roomId,
                "userId": userInfo.id,
                "groupId": userInfo.groupId
            },
            success: function (data) {
                if (data.code == 1000) {
                    $('.prizeName').removeClass('hide');
                    $('#prize').text(data.data);
                }
            }
        });
    }
}

function zpClose() {
    $(".rotateWin").css("visibility", "hidden");
}

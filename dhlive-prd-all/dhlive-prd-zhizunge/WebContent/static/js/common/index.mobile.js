$(function () {

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
        $('.expressionT').toggle();
        $('#emotionUL li').last().prev().css('display', 'none');
        // 隐藏红包
        $('#emotionUL li').last().css('display', 'none');
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

    $('.more').click(function () {
        $(this).toggleClass("cur");
        $('.popAssistantList').fadeToggle();
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
                    // var time = msgList[i].beginTime;
                    // var holdDate = new Date(time.replace("-", "/").replace("-", "/"));
                    // var hours = holdDate.getHours();
                    // var min = holdDate.getMinutes();
                    // if (i == 0) {
                    //     $(".noticeMsg").html(content);
                    //     $(".msgTime").html(hours + " : " + min);
                    // } else {
                    //     htmls += appendHD(content, hours + " : " + min);
                    // }
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

    //客服
    $('.service').on('click', function () {
        $('.waterPersionBox').removeClass('hide');
    });


    $('.menuIcon a').on('click',function(){
        $(this).toggleClass('cur');
        $('.menuBtn').fadeToggle();
    });
    $('.menuBtn a').on('click',function(){
        $('.menuIcon a').removeClass('cur');
    });

    /*当前在线人记录缓存(10秒/次)*/
    setInterval(function () {
        //服务器缓存在线人数
        initRedisUser(userInfo.id, userInfo.groupId);
        //检查是否可以继续观看
        if (userInfo.groupId == 1 || userInfo.groupId == 5) {
            checkCanWatch();
        }
    }, 10000);


});

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



$(function () {
    var $loginForm = $('#loginForm'),
        $mask = $('#mask'),
        $errorTips = $('#errorTips'),
        errorObj = {},
        eleVcode = document.getElementById('vcodeImg'),
        $submitBtn = $('#submitBtn');

    $loginForm.find('input').on('focus', function () {
        $(this).parent().next().removeClass('hide');
    }).on('focusout', function () {
        $(this).parent().next().addClass('hide');
    });

    var showErrorTips = function () {
        var noError = true;
        for (var i in errorObj) {
            if (errorObj[i]) {
                $errorTips.removeClass('hide').html(errorObj[i]);
                noError = false;
                break;
            }
        }
        if (noError) {
            $errorTips.addClass('hide');
        }
    };

    //登录提交
    $loginForm.validate({
        submitHandler: function () {
            formSubmit("#submitBtn", true, "正在登录...");
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        top.location.href = g_requestContextPath + '/bms_live/';
                    } else {
                        popLayer(data.message);
                        changeVcode(eleVcode);
                    }
                    formSubmit("#submitBtn", false, "登录");
                },
                error: function (data) {
                    popLayer(stringMsg.serverErr);
                    formSubmit("#submitBtn", false, "登录");
                    changeVcode(eleVcode);
                }
            };
            $loginForm.ajaxSubmit(options);
        },
        success: function (element) {
            //console.log(element.attr('id').replace('-error',''))
            errorObj[element.attr('id').replace('-error', '')] = '';
            showErrorTips();
            //element.closest(".formTips").empty();
        },
        errorPlacement: function (error, element) {
            //console.log(element.attr('id'))
            errorObj[element.attr('id')] = error;
            showErrorTips();
            //var obj = element.closest("li").find(".formTips").empty();
            //obj.append(error);
        },
        //清除错误和成功消息
        errorClean: function (txt, element) {
            //element.closest("li").find(".formTips").empty();
        },
        ignore: "",
        rules: {
            phone: {
                required: true,
                isMobile: true
            },
            password: {
                required: true
            },
            code: {
                required: true
            }
        },
        messages: {
            phone: {
                required: "请输入您的手机号",
                isMobile: "手机号码格式不正确"
            },
            password: {
                required: "请输入密码"
            },
            code: {
                required: "请输入验证码"
            }
        }
    });

});

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
                $('#errorTips-' + i).html(errorObj[i]);
                $('#actLine-' + i).addClass('error');
                noError = false;
            } else {
                $('#errorTips-' + i).html('');
                $('#actLine-' + i).removeClass('error');
            }
        }
        ;
//		if(noError){
//			$errorTips.addClass('hide');
//		};
    };

    //登录提交
    $loginForm.validate({
        submitHandler: function () {
            formSubmit("#submitBtn", true, "正在登录...");
            $submitBtn.addClass('loading');
            var options = {
                success: function (data) {
                    if (data.code == '1000') {
                        setTimeout(function () {
                            formSubmit("#submitBtn", false, "登录");
                            $submitBtn.removeClass('loading');
                            top.location.href = g_requestContextPath + '/' + data.data + '/';
                        }, 1500);
                    } else {
                        popLayer(data.message);
                        changeVcode(eleVcode);
                        formSubmit("#submitBtn", false, "登录");
                    }
                },
                error: function (data) {
                    popLayer(stringMsg.serverErr);
                    formSubmit("#submitBtn", false, "登录");
                    $submitBtn.removeClass('loading');
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
                required: true
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
                required: "用户名没有填写"
            },
            password: {
                required: "密码没有填写"
            },
            code: {
                required: "验证码没有填写"
            }
        }
    });

    /*
     $loginForm.on('submit', function(){
     var phone 		= $('#phone').val().trim(),
     password 	= $('#password').val().trim(),
     code 		= $('#code').val().trim();
     if(phone == ""){
     $errorTips.removeClass('hide').html('请输入您的手机号');
     return false;
     }else if(!isMobile(phone)){
     $errorTips.removeClass('hide').html('手机号码格式不正确');
     return false;
     }else if(address == ""){
     $errorTips.removeClass('hide').html('请输入密码');
     return false;
     }else if(zipCode == ''){
     $errorTips.removeClass('hide').html('请输入验证码');
     return false;
     };
     formSubmit("#submitBtn",true,"正在登录...");
     var options = {
     success: function (data) {
     if (data.code == '1000') {
     top.location.href = 'messages.html';
     }else{
     popLayer(data.message);
     }
     formSubmit("#submitBtn",false,"登录");
     },
     error: function(data) {
     popLayer(stringMsg.serverErr);
     formSubmit("#submitBtn", false,"登录");
     }
     };
     $loginForm.ajaxSubmit(options);
     return false;
     });
     */

});
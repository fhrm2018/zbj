if (userInfo.groupId == 1 || userInfo.groupId == 5) {
    // 截止时间转换成秒
    checkCanWatch();
    function countTime() {
        time--;
        var d, h, m, s, str;
        if (time > 0) {
            d = Math.floor(time / 60 / 60 / 24);
            h = Math.floor(time / 60 / 60 % 24);
            m = Math.floor(time / 60 % 60);
            s = Math.floor(time % 60);
            str = '剩余观看时长：' + d + ' 天 ' + h + ' 小时 ' + m + ' 分 ' + s + ' 秒，登录后免费观看';
        }
        //将倒计时赋值到div中
        document.getElementById('remainderTime').innerHTML = str;
    }

    var currTime = setInterval(function () {
        countTime();
    }, 1000);
}

function checkCanWatch() {
    $.ajax({
        url: g_requestContextPath + '/live/checkCanWatch',
        data: {
            groupId: userInfo.groupId,
            userId: userInfo.id
        },
        success: function (data) {
            if (data.code == 1000) {
                time = data.data;
            } else if (data.code == 1001) {
                if (userInfo.groupId == 1) {
                    $('.freeTipBox').removeClass('hide');
                    $('.videoBox').addClass('hide');
                    $('.viewTime').addClass('hide');
                    clearInterval(currTime);
                } else if (userInfo.groupId == 5) {
                    $('.freeTipBox').removeClass('hide');
                    $('.freeTipBox p').html('您的VIP免费时长已用完，请联系您的专属助理获取更多时长，以免影响您的收益');
                    $('.freeTipBox .loginOrReg').addClass('hide');
                    $('.videoBox').addClass('hide');
                    $('.viewTime').addClass('hide');
                    $('.tkTime').attr('tkTime tkTime2');
                    clearInterval(currTime);
                }
            } else if (data.code == 1002) {
                document.getElementById('remainderTime').innerHTML = " ";
                clearInterval(currTime);
            }
        }
    });
}



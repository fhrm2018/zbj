if (userInfo.groupId == 1 || userInfo.groupId == 5) {
    // 截止时间转换成秒
    function countTime() {
    	if(lookTime == -1){
    		var watchTimeCache = cookieFunction.get('watchTime');
        	if(watchTimeCache){
        		lookTime = parseInt(tempWatchTime) - parseInt(watchTimeCache);
        	}else{
        		lookTime = parseInt(tempWatchTime);
        	}
    	}
    	lookTime--;
        var d, h, m, s, str;
        if (lookTime > 0) {
            d = Math.floor(lookTime / 60 / 60 / 24);
            h = Math.floor(lookTime / 60 / 60 % 24);
            m = Math.floor(lookTime / 60 % 60);
            s = Math.floor(lookTime % 60);
            str = '剩余观看时长：' + d + ' 天 ' + h + ' 小时 ' + m + ' 分 ' + s + ' 秒，登录后免费观看';
          //将倒计时赋值到div中
            document.getElementById('remainderTime').innerHTML = str;
        }else{
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
        }
        
    }
    
    function saveWatchTimeCookie(seconds){
    	var watchTimeCache = cookieFunction.get('watchTime');
    	if(!watchTimeCache){
    		watchTimeCache = 0;
    	}
    	cookieFunction.set('watchTime',parseInt(watchTimeCache) + seconds );
    }

    var currTime = setInterval(function () {
        countTime();
    }, 1000);
    
    var currTime = setInterval(function () {
    	saveWatchTimeCookie(10);
    }, 10000);
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
                //time = data.data;
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



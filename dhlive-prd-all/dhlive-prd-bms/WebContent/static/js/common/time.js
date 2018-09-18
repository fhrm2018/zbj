//格式化秒
function formatSeconds(value, from) {
	var seconds = parseInt(value);// 秒 
		minutes = "00",// 分 
		hours = 0,
		hourHtml = '',
		minHtml = '',
		secHtml = '',
		htmls = '';
	if(seconds < 0) {

	}else if(seconds < 60) {
		if(seconds < 10 )seconds = "0" + seconds;
		secHtml= seconds +'秒'; 
	}else{
		minutes = parseInt(seconds / 60); 
		seconds = parseInt(seconds % 60);
		if(seconds < 10 )seconds = "0" + seconds;
		secHtml= seconds +'秒'; 
		if(minutes < 60) { 
			if(minutes < 10 )minutes = "0" + minutes;
			minHtml = minutes +'分'; 
		}else{
			hours = parseInt(minutes / 60); 
			minutes = parseInt(minutes % 60);
			if(minutes < 10 )minutes = "0" + minutes;
			minHtml = minutes +'分';
			if(hours < 10 )hours = "0" + hours;
			hourHtml = hours +'小时';
		};
	};
	return htmls + hourHtml + minHtml + secHtml;
};

//剩余时间
var getRemainTimes = function($obj, from, endStr, timeSpan){
	/*
	$.ajax({
		url: g_requestContextPath + "/myorder/getTime",
		dataType: "json",
		cache: false,
		success: function (data) {
			$obj.each(function(i){
				calStartTimes($obj.eq(i), data.data, from, endStr); //与服务器时间比较
			});				
		},
		error:function(){
	*/
			$obj.each(function(i){
				calStartTimes($obj.eq(i), new Date(), from, endStr, timeSpan); //服务器无法返回时间时与本机时间比较
			});
		//}
	//});
};

//剩余时间执行函数
var calStartTimes = function(obj, st, from, endStr, timeSpan){
	var $obj = obj,
		time = $obj.data('time'),
		len = time.length;
		dateObj = time.substring(0,10).split('-'),
		timeObj = time.substring(11,len).split(':'),
		tS = new Date(dateObj[0],Number(dateObj[1]) - 1,dateObj[2],timeObj[0],timeObj[1],timeObj[2]).getTime() / 1000,
		stS = new Date(st).getTime() / 1000;	//当前时间
	//console.log(stS)
	var cS = 14 * 60 * 60;	 //14小时时间差 
	var ys = tS + (timeSpan * 60 * 60) - stS;
	$obj.html(formatSeconds(ys, from));
	if(from == 1){
		$obj.removeClass('remainTime')		
	};
	var timer = setInterval(function(){
		ys--;
		if(ys > 0){
			$obj.html(formatSeconds(ys, from));
		}else{
			clearInterval(timer);
			$obj.parent().html(endStr);
		};
	}, 1000);
};
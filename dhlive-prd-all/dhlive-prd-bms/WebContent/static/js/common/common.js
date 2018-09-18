// JavaScript Document
/**
 * * == jquery.common.js ==================================== Code licensed
 * 
 * @version 2.0 2017.04.18
 * @description jquery.common.js
 * --------------------------------------------------------------
 */

//返回参数对应字符串
var stringMsg = {
	"serverErr":"当前网络不好, 请稍后重试"
};
var warnCode = {
	"sortIndex":"该排序已经存在，请更换序号！",
	"WrongFileFormate":"上传的文件中手机号或金额格式不正确！",
	"UploadFileFail":"上传文件过程中出现错误，请稍候再试！",
	"ApprovFail":"审批失败！",
	"AuthError":"您没有操作/查看此功能的权限！",
	"PasswordError":"原密码不正确！",
	"pwdatypism":"确认密码与新密码不一致！"
};

//左右去空格扩展
String.prototype.trim = function () {
	return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
};

// 获取指定天数日期
var GetDateStr = function(day) {
	var dd = new Date();
	dd.setDate(dd.getDate() + day);
	var y = dd.getFullYear();
	var m = dd.getMonth() + 1;
	if (m < 10)m = "0" + m;
	var d = dd.getDate();
	if (d < 10)d = "0" + d;
	return y + "-" + m + "-" + d;
};

//获取指定月份后的同一天
var monthsahead = function(m) {
	var today = new Date();
	var date = new Date(today.getFullYear(),today.getMonth() + m,today.getDate(),today.getHours(),today.getMinutes(),today.getSeconds());
	var gm = date.getMonth() + 1;
	if(gm<10)gm = "0"+gm;
	return date.getFullYear() + '-' + gm + '-' + date.getDate();
};

//获取指定年月的天数;
function dayNumOfMonth(Year,Month){
    var d = new Date(Year,Month,0);
    return d.getDate();
};

//获取星期
var getDayOfWeek = function(time){
    var day = ['日','一','二','三','四','五','六'];
    return day[time.getDay()];
};

//获取标准日期时间
var getFormatTime = function(time){
    var len = time.length;
        dateObj = time.substring(0,10).split('-'),
        timeObj = time.substring(11,len).split(':');
    return new Date(dateObj[0],Number(dateObj[1]) - 1,dateObj[2],timeObj[0],timeObj[1],timeObj[2]);        
};

//根据给定的日期 2015-07-15 15:03:41 截取需要的字符 unit:1 取年月日 
var getSpeDate = function(date, type, unit){
	if(!date || date.length < 19){
		return "--";
	};
	var len = date.length,
		mydate = date.substring(0,10),
    	mytimes = date.substring(10,len);
	if(type == 1){
		//只返回年月日2015-07-15
		if(unit){
			var arrydate = mydate.split('-');
			if(unit == 'cn'){
				mydate = arrydate[0] + '年' + arrydate[1] + '月' + arrydate[2] + '日';
			}else{
				mydate = arrydate[0] + unit + arrydate[1] + unit + arrydate[2];
			};
		};
		return mydate;
	}else if(type == 2){
		//只反回时分秒 15:03:41
		return mytimes.trim();
	};
	return date;
};

//获得年月日中的值yyyy-mm-dd
var getValFormDate = function(date,type){
	var dateArr = date.split('-');
	return dateArr[type];
};

// 判断数字
var chkdigit = function(val) {
	return /^\d+$/.test(val);
};

// 判断大于0的正整数
var chkinteger = function(val) {
	return /^\+?[1-9][0-9]*$/.test(val);
};

// 小数点两位且大于0
var chkdecimalt = function(val) {
	return /^([1-9][\d]*|0)(\.[\d]{1,2})?$/.test(val) && val > 0;
};

// 判断邮箱格式
var chkmail = function(val) {
	return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i
			.test(val);
};

// 判断网址
var chkurl = function(val) {
	return /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i
			.test(val);
};

//手机号正则匹配
var isMobile = function(val){
	return /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/.test(val);
};

// 验证日期 0000-00-00
var dateCheck = function(date) {
    var result = date.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
    if (result == null)return false;
    var d = new Date(result[1], result[3] - 1, result[4]);
    return (d.getFullYear() == result[1] && (d.getMonth() + 1) == result[3] && d.getDate() == result[4]);
}; 

// 比较日期相差天数
var getDays = function (s,e){
   var x = "-"; //日期分隔符
   var oDate1;
   var oDate2;
   oDate1= s.split(x);
   oDate2= e.split(x);
   var strDateS = new Date(oDate1[0], oDate1[1]-1, oDate1[2]);
   var strDateE = new Date(oDate2[0], oDate2[1]-1, oDate2[2]);
   return parseInt(Math.abs(strDateS - strDateE ) / 1000 / 60 / 60 /24); 
};

// 取数值小数点两位 不足补0
var twoDecimal = function(x) {
	var f_x = x;
	if (isNaN(f_x) || f_x === null) {
		return "0.00";
	};
	var s_x = f_x.toString();
	var pos_decimal = s_x.indexOf(".");
	if (pos_decimal < 0) {
		pos_decimal = s_x.length;
		s_x += ".";
	};
	if(s_x.length - pos_decimal > 3){
		s_x = s_x.substring(0,pos_decimal + 3);
	}else{
		while (s_x.length <= pos_decimal + 2) {
			s_x += 0;
		}
	};
	return s_x;
};

//取小数点后几位
var getNumFromDecimals = function(num, bit){
	if(!num){
		return '--';
	}
	var numArr = num.toString().split('.');
	if(numArr.length > 1){
		var dec = numArr[1];
		if(dec.length > bit){
			dec = dec.substring(0, bit);
		};
		return numArr[0] + '.' + dec;
	}else{
		return num;
	};
};

// 设置价格为 千 与 万
var changePriceT = function(x) {
	var f_x = x;
	if (isNaN(f_x)) {
		return "0元";
	};
	f_x = parseInt(x * 100) / 100;
	if (f_x >= 10000) {
		f_x = parseInt(f_x / 10000 * 100) / 100;
		f_x += "万";
	};
	return f_x + "元";
};

// 格式化日期
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), 		// day
		"h+" : this.getHours(), 	// hour
		"m+" : this.getMinutes(), 	// minute
		"s+" : this.getSeconds(),	 // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};

//日期插件中获取日期 - 后台专用
var getStartDate_admin = function (id, end, fn, cfn) {
	WdatePicker({
		el:id,
		dateFmt:'yyyy-MM-dd HH:mm:ss',
		maxDate:"#F{$dp.$D(\'"+end+"\');}",
		onpicked: fn,
		onclearing: cfn
	});
};
var getEndDate_admin = function (id, start, fn, cfn){
	WdatePicker({
		el:id,
		dateFmt:'yyyy-MM-dd HH:mm:ss',
		minDate:"#F{$dp.$D(\'"+start+"\');}",
		onpicked: fn,
		onclearing: cfn
	});
};

//转换为千分位
var commafy = function(num){
   if(num === null || (num+"").trim() == "" || isNaN(num)){
      return "--";
   }
   num += "";
   if(/^.*\..*$/.test(num)){
      var pointIndex =num.lastIndexOf(".");
      var intPart = num.substring(0,pointIndex);
      var pointPart = num.substring(pointIndex+1,num.length);
      intPart = intPart +"";
       var re =/(-?\d+)(\d{3})/;
       while(re.test(intPart)){
          intPart =intPart.replace(re,"$1,$2");
       };
      num = intPart+"."+pointPart;
   }else{
       var re =/(-?\d+)(\d{3})/;
       while(re.test(num)){
          num =num.replace(re,"$1,$2");
       }
   };
   return num;
};

//未定义时显示效果
var definePara = function(v) {
	if(v === undefined){
	      return "--";
	};
	return v;
};

//前后隔位数替换字符
var replaceString = function(s,n,l){
	if(typeof(s) == "undefined" || s==null)return "--";
	var ln = n;
	if(l)ln = l;
	if(s.length < n + ln)return s;
	var m = s.substring(n, s.length - ln).replace(/./g, "*");
	s = s.substr(0,n) + m + s.substr(s.length - ln);
	return s;
};

//倒计时
var getCoundDown = function(obj,s,href){
	var $sec = s;
	var $ints = "";
	$ints = setInterval(function() { 
		$sec --;
		$(obj).html($sec);
		if($sec <=0 ){
			clearInterval($ints);
			window.location.href = href;
		}
	},1000);
};

if (!navigator.cookieEnabled) {
	alert("您好，您的浏览器设置禁止使用cookie\n请设置您的浏览器，启用cookie功能，再重新登录。");
};
	

// 转换不合法字符
var codeString = function(val,t) {
	val = val.replace(/<script[^>]*>/ig, '&lt;script&gt;');
	val = val.replace(/<\/script/ig, '&lt;/script');
	if(t){
		val = val.replace(/</g, '&lt;');
		val = val.replace(/>/g, '&gt;');
		val = val.replace(/&/g, '&amp;');
	};
	return val;
};

//设置Input错误样式
var setErrClass = function(type, obj){
	if(type){
		obj.addClass("iptErr");
	}else{
		obj.removeClass("iptErr");
	};
};

//设置只读状态
var setFormItem = function(type, obj){
	var items = obj.find("input,textarea,select").not(".operateItem");
	if(type){
		for(var i=0; i<items.length; i++){
			items.eq(i).prop("disabled",true);
		};
		obj.find(".serSelect").addClass("disabled");
	}else{
		for(i=0;i<items.length;i++){
			items.eq(i).prop("disabled",false);
		};
		obj.find(".serSelect").removeClass("disabled");
	};
};

//后台处理日期如果不为日期返回空值
var dateConfirm = function(v,t){
	if (/^(\d{4})-(\d{2})-(\d{2})\s+(\d{2}):(\d{2}):(\d{2})$/.test(v)){
		if(t == 1)$("#isPreSub").prop("checked",true);
		return v;
	}else{
		return "";
	};
};

//检查AJAX请求返回参数是否会话过期
var checkAjaxSession = function(data,type,obj,txts){
	if(data && data.indexOf('id="loginForm"') > -1){
		top.location.href = g_requestContextPath + "/"; //path[1]
	}else{
		if(obj){
			//popLayer(stringMsg["serverErr"]);
			if(type == 1){
				obj.append(txts);
			}else{
				obj.html(txts);
			};
		}else if(type){
			popLayer(stringMsg["serverErr"]);
		};
	};
};

//设置cookies相关
var cookieFunction = function(){
	/*!
	* 获取cookie
	* @param sName：cookie名
	*/
    function getCookie(sName){
       var sSearch = sName + "=";
	   try{
		   if(document.cookie.length > 0){
			  offset = document.cookie.indexOf(sSearch);
			  if(offset != -1){
				 offset += sSearch.length;
				 end = document.cookie.indexOf(";", offset);
				 if(end == -1) end = document.cookie.length;
				 return decodeURI(document.cookie.substring(offset, end));
			  }
			  else return "";
		   };
	   }catch(error){};
    };
	/*!
	* 设置cookie
	* @param name：cookie名
	* @param value：cookie值
	*/
    function setCookie(name,value){
    	var Days = 180;
		var exp = new Date(); 
		exp.setTime(exp.getTime() + Days*24*60*60*1000);
		try{
			document.cookie = name + "="+ value + ";expires=" + exp.toGMTString() + ";path=/";
		}catch(error){};
    };
	/*!
	* 删除cookie
	*/
	function delCookie(name){
    	var exp = new Date();
		exp.setTime(exp.getTime() - 1);
		var cval=getCookie(name);
		try{
			if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString() + ";path=/";
		}catch(error){};
    };
    return{
        get:getCookie,
        set:setCookie,
		del:delCookie
    };
}();
//cookieFunction.set("name","value");
//cookieFunction.get("name");
//cookieFunction.del("name");

//搜索中指定域中值拼接为对象
var getJsonParam = function(sId) {
	var result = {};
	var $obj = $("#" + sId);
	$obj.find("input,select").each(function(index, data) {
		var $data = $(data);
		if ($data.attr("name") && $data.val().trim() != "") {
			result[$data.attr("name")] = $data.val().trim();
		};
	});
	return result;
};

//导出excel时获得get参数
var getExcelParam = function(sId) {
	var s = "",count = 0,obj = getJsonParam(sId);
	for (var p in obj) {
		if (count == 0) {
			s += "?";
		}else{
			s += "&";
		}
		s += p + "=" + obj[p];
		count++;
	};
	return s;
};

//保存中样式
var saveLoading = function(obj,t){
	if(t == 1){
		var m = $('<div class="btnsSaving">保存中</div>');
		obj.addClass("grey").parent().append(m);
	}else{
		var m = obj.removeClass("grey").parent().find(".btnsSaving");
		m.remove();
	};
};

//防表单重复提交
var formSubmit = function(obj, type, txt){
	if(type){
		$(obj).html(txt).addClass("actingBtn").prop("disabled",true);
	}else{
		$(obj).html(txt).removeClass("actingBtn").prop("disabled",false);
	};
};

//获取复选框中的ID返回以,
var getChkIds = function(obj){
	var ids = "";
	obj.each(function () {
		if(ids != "")ids += ",";
		ids += $(this).data("id");
	});
	return ids;
};

//格式化json字符串为json对象
var formatJsons = function(data){
	try{
		return jQuery.parseJSON(data);
	}catch(e){
		console.log(data);
		return {};
	};
};

//获取指定URL参数
var getQueryString = function(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]); return "";
};

//提示框
var popLayer = function(content){
	var $tips = $('<div class="posFixed popTips left0 right0 bottom0 ac">' + 
//		'<div class="flexCon"></div>' +
		'<div class="inner ilblock ac ptb15 plr15 fz16">'+ content +'</div></div>').appendTo('body');
	setTimeout(function(){
		$tips.addClass('popTipsAni');
	},100);
	setTimeout(function () {
		$tips.removeClass('popTipsAni');
	}, 1900);
	setTimeout(function () {
		$tips.remove();
	}, 2200);
};

//ajax统一设置
$.ajaxSetup({
	type : "POST",
	dataType: "json"/*,
    headers: {
        "Content-Type":"application/json; charset=utf-8"
    }*/
});


//设置表格头身宽度一致
var setTableThWidth = function($trds, $trhs){
	var $tds = $trds.find('td'),
		$ths = $trhs.find('th');
	for(var i = 0;i < $tds.length - 1;i++){
		$ths.eq(i).width($tds.eq(i).width());
	};
};

//高亮关键字
var styleKeyWords = function(words, keys){
	if(!words)return words;
	if(keys){
		var len = keys.length,
			pos = words.indexOf(keys);
		if(pos >= 0){
			return words.substring(0,pos) + '<b class="bold">' + keys + '</b>' + words.substring(pos + len,words.length);
		};
	};
	return words;
};

//打开窗口
var openPopForm = function(obj){
	var $obj = $(obj),
		$form = $obj.find('form');
	$('#mask').removeClass('hide');
	$obj.removeClass('hide');
	setTimeout(function(){
		$obj.addClass('popFormAni');
	},100);
	if($form.length){
		$form.resetForm().find('.errorTips').empty();
		//下拉框默认
		var $serSelect = $form.find('.serSelect');
		if($serSelect.length){
			$.each($serSelect, function(){
				var $this = $(this)
					def = $this.data('default'),
					defval = $this.data('defval');
				if(def != undefined){
					$this.find('.text').html(def);
				};
				if(defval != undefined){
					$this.find('input').val(defval);
				};
			});
		};
	};
	//setTimeout(function(){
		$obj.css({"margin-top": -$obj.height() / 2 - 20 + "px"});
	//},300);
};

//关闭窗口
var closeThePopForm = function(obj, type){
	var $obj = (type == 1)?$(obj):obj;
	$obj.removeClass('popFormAni');
	setTimeout(function(){
		$obj.addClass('hide');
		$('#mask').addClass('hide');
	},300);
};


var closePopForm, changeVcode;

//jquery 事件
$(function () {

	//点击刷新 验证码
	changeVcode = function(obj){
		var $obj = $(obj);
		$obj.attr('src', g_requestContextPath + '/validateCode?width=90&height=30&time=' + new Date().getTime());
	};
	
	//清空日期事件
	var dateClearSet = function(){
		//if($('#timeStart').val() == '' && $('#timeEnd').val() == ''){
		//	$timeParas.find('.tag').eq(0).addClass('tagCur');
		//};
		//getDataList(getJsonParam('userSearchParas'));
	};
	
	//选择日期事件
	var dateSelectSet = function(){
		$("#dateSearchType").val('');
		$('.serTimeIt').removeClass('checked');
		$("#beforeDate").val("");
		//getDataList(getJsonParam('userSearchParas'));
	};

	//选择起止日期
	$("#timeStart").on("click",function(){
		getStartDate_admin("timeStart","timeEnd", function(){
			dateSelectSet();
		}, function(){
			dateClearSet();
		});
	});
	$("#timeEnd").on("click",function(){
		getEndDate_admin("timeEnd","timeStart", function(){
			dateSelectSet();
		}, function(){
			dateClearSet();
		});
	});

	$('.serTimeIt').on('click', function(){
		var $this = $(this).addClass('checked');
		$this.siblings('.serTimeIt').removeClass('checked');
		$('#timeStart,#timeEnd').val('');
	});
	
	$('.formSelect').on('click', function(){
		var $this = $(this).addClass('checked');
		$this.siblings('.formSelect').removeClass('checked');
	});
	
	/*
	$("#time_start_ico").on("click",function(){
		var id = $(this).prev().attr("id");
		getStartDate_admin(id,"timeEnd");
	});
	$("#time_end_ico").on("click",function(){
		var id = $(this).prev().attr("id");
		getEndDate_admin(id,"timeStart");
	});
	*/

	//模拟复选框
	$(document).on('click','.checkSel',function(){
		var $this = $(this);
		if(!$this.hasClass('noCommonCheck')){
			var	on = $this.data('on'),
				off = $this.data('off');
			if(!$this.hasClass('noChecked')){
				if($this.hasClass('checkSelChecked')){
					//$this.removeClass('checkSelChecked').find('input').prop('checked',false);
					$this.removeClass('checkSelChecked').find('input').val(off);
				}else{
					//$this.addClass('checkSelChecked').find('input').prop('checked',true);
					$this.addClass('checkSelChecked').find('input').val(on);
				};
			};
		};
	});

	//模拟下拉框
	var $serSelect = $('.serSelect'),
		isHoverSelect = false;
	$serSelect.on('focus', function(){
		var $this 	= 	$(this),
			$ul 	=	$this.next('ul');
		if(!$this.hasClass('disabled')){
			$this.addClass('open');
		};
	}).on('focusout', function(e){
		var $this 	= 	$(this);
		if(!isHoverSelect){
			$this.removeClass('open');
		};
	}).find('li').on('click', function(){
		var $this 	= 	$(this),
			$sel 	=	$this.closest('.serSelect'),
			$ipt 	=	$this.parent().next(),
			$txt 	=	$sel.find('.text');
			txt  	=	$this.text(),
 			val 	=	$this.data('value');
		$ipt.val(val);
		$txt.html(txt);
		isHoverSelect = false;
		$sel.removeClass('open');
	});

	$serSelect.find('.text').on('click', function(){
		$(this).parent().focus();
	});
	$serSelect.find('ul').on('mouseenter', function(){
		isHoverSelect = true;
	}).on('mouseleave', function(){
		isHoverSelect = false;
	});

	//搜索框折叠
	var isSearchDown = false,
		$hideArea = $('.hideArea');
	$('#searchDown').on('click', function(){
		if(isSearchDown){
			$hideArea.addClass('hide');
			isSearchDown = false;
			$(this).removeClass('up');
		}else{
			$(this).addClass('up');
			$hideArea.removeClass('hide');
			isSearchDown = true;
		};
	});

	//左下角操作
	$('#loginName').on('mouseenter', function(){
		$(this).find('.box').removeClass('hide');
	}).on('mouseleave', function(){
		$(this).find('.box').addClass('hide');
	});

	//关闭本弹出表单窗口
	closePopForm = function(obj){
		var $obj = $(obj).closest('.popForm');
		closeThePopForm($obj);
	};

	//修改密码
	$('#lkPassword').on('click', function(){
		openPopForm('#changePassword');
	});

	var passErrorObj = {},
		$passErrorTips = $('#passErrorTips'),
		showPassFormErr = function(){
		var noError = true;
		for(var i in passErrorObj){
			if(passErrorObj[i]){
				$passErrorTips.html(passErrorObj[i]);
				noError = false;
				break;
			};
		};
		if(noError){
			$passErrorTips.empty();
		};
	};

	//修改密码表单
	$('#passwordForm').validate ({
		submitHandler:function(){
			formSubmit("#passwordBtn",true,"保存中...");
			var options = {
				success: function (data) {
					if (data.code == '1000') {
						popLayer('密码修改成功');
						closeThePopForm('#changePassword', 1);
						top.location.href = g_requestContextPath+'/';
					}else{
						popLayer(data.message);
					}
					formSubmit("#passwordBtn",false,"保存");
				},
				error: function(data) {
					popLayer(stringMsg.serverErr);
					formSubmit("#passwordBtn", false,"保存");
				}
			};
			$('#passwordForm').ajaxSubmit(options);
        },
		success:function(element){
			passErrorObj[element.attr('id').replace('-error','')] = '';
			showPassFormErr();
		},
		errorPlacement: function(error, element) {
			//console.log(element.attr('id'))
			passErrorObj[element.attr('id')] = error;
			showPassFormErr();
		},
		errorClean: function(txt, element) {
		},
		ignore: "",
		rules: {
			oldPass: {
				required:true
            },
            newPass:{
                required:true,
	            rangelength:[6,24]
            },
            reNewPass:{
                required:true,
	            equalTo:"#newPass"
            }
        },
        messages: {
        	oldPass: {
                required:"请输入原密码"
            },
            newPass: {
                required:"请输入新密码",
		        rangelength:"密码长度只能为6至24位！"
            },
            reNewPass: {
                required:"请再次输入新密码",
		        equalTo:"两次输入的密码不一致！"
            }
        }
    });


	/*var winHei = $(window).height(),
		bodyHei = $('body').height(),
		$footer = $('#footer');

	if(bodyHei > winHei){
		$footer.removeClass('footerFixed posFixed').prev().remove();
	};*/

	
}); // ready
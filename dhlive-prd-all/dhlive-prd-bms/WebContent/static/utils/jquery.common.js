// JavaScript Document
/**
 * * == jquery.common.js ==================================== Code licensed
 * 
 * @version 2.0 2015.01.09
 * @description jquery.common.js
 * --------------------------------------------------------------
 */

//返回参数对应字符串
var stringMsg = {
	"serverErr":"信息请求失败，请稍候重试！",
	"PhoneExist":"该手机号码已注册！"
};

var imgdomain = "http://file.dhqjr.com/ori/";

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

//根据给定的日期 2015-07-15 15:03:41 截取需要的字符
function getSpeDate(date,type){
	if(date === undefined || date === null){
		return "--";
	}
	var len = date.length;
	if(len < 10)return "--";
	var mydate = date.substring(0,10);
    var mytimes = date.substring(10,len);
	if(type == 1){
		//只返回年月日2015-07-15
		return mydate;
	}else if(type == 2){
		//只反回时分秒 15:03:41
		return mytimes.trim();
	}
	return date;
};

// 判断数字
var chkdigit = function(val) {
	return /^\d+$/.test(val);
};

// 判断大于0的正整数
var chkinteger = function(val) {
	return /^\+?[1-9][0-9]*$/.test(val);
};

// 小数点两位且大于0 :type 为 true 不判断0
var chkdecimalt = function(val,type) {
	var isok = (type)?true:val > 0;
	return /^([1-9][\d]*|0)(\.[\d]{1,2})?$/.test(val) && isok;
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

//判断后日期是否大于，或大于等于前日期，1：大于等于，0：大于
function isDateOk(start,end,type){
	var startIni = start.length > 10?start:start + " 00:00:00";
	var endIni = end.length > 10?end:end + " 00:00:00";
	//console.log(startIni);
	//console.log(endIni);
	var beginTimes = startIni.substring(0,10).split('-');
    var endTimes = endIni.substring(0,10).split('-');
	var startFmt = beginTimes[1]+'/'+beginTimes[2]+'/'+beginTimes[0]+' '+ startIni.substring(10,19);
	var endFmt = endTimes[1]+'/'+endTimes[2]+'/'+endTimes[0]+' '+ endIni.substring(10,19);
	if(type == 1){
		if(Date.parse(startFmt) >= Date.parse(endFmt)){
			return false;
		}
	}else{
		if(Date.parse(startFmt) > Date.parse(endFmt)){
			return false;
		}
	}
	return true;
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
var changeTwoDecimal = function(x) {
	//var f_x = parseFloat(x);
	var f_x = x;
	if (isNaN(f_x) || f_x === null) {
		return "0.00";
	}
	var s_x = f_x.toString();
	var pos_decimal = s_x.indexOf(".");
	if (pos_decimal < 0) {
		pos_decimal = s_x.length;
		s_x += ".";
	}
	if(s_x.length - pos_decimal > 3){
		s_x = s_x.substring(0,pos_decimal + 3);
	}else{
		while (s_x.length <= pos_decimal + 2) {
			s_x += 0;
		}
	}
	return s_x;
};

// 设置价格为 千 与 万
var changePriceT = function(x) {
	var f_x = x;
	if (isNaN(f_x)) {
		return "0元";
	}
	f_x = parseInt(x * 100) / 100;
	if (f_x >= 10000) {
		f_x = parseInt(f_x / 10000 * 100) / 100;
		f_x += "万";
	}
	return f_x + "元";
};

// 格式化日期
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
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

// 是否ie6
var isIE6 = /msie 6/i.test(navigator.userAgent);

//日期插件中获取日期
/*var getStartDate = function (fn) {
	WdatePicker({el:'timeStart',dateFmt:'yyyy-MM-dd',maxDate:"#F{$dp.$D(\'timeEnd\')||'%y-%M-%d';}",autoPickDate:true,onpicked:fn});
};*/
//,minDate:'%y-%M-{%d}'
/*var getEndDate = function (fn){
	WdatePicker({el:'timeEnd',dateFmt:'yyyy-MM-dd',minDate:"#F{$dp.$D(\'timeStart\',{d:0});}",maxDate:'%y-%M-%d',autoPickDate:true,onpicked:fn});
};*/

//日期插件中获取日期 - 后台专用
var getStartDate_admin = function (id,end) {
	WdatePicker({el:id,dateFmt:'yyyy-MM-dd',maxDate:"#F{$dp.$D(\'"+end+"\');}"});
};
var getEndDate_admin = function (id,start){
	WdatePicker({el:id,dateFmt:'yyyy-MM-dd',minDate:"#F{$dp.$D(\'"+start+"\');}"});
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
   }
   return num;
};

//未定义时显示效果
var definePara = function(v) {
	if(v === undefined || v === null){
	      return "--";
	}
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
		   }
	   }catch(error){};
    }
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
    }
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
    }
    return{
        get:getCookie,
        set:setCookie,
		del:delCookie
    };
}();
//cookieFunction.set("name","value");
//cookieFunction.get("name");
//cookieFunction.del("name");

// 转换不合法字符
var codeString = function(val,t) {
	val = val.replace(/<script[^>]*>/ig, '&lt;script&gt;');
	val = val.replace(/<\/script/ig, '&lt;/script');
	if(t){
		val = val.replace(/</g, '&lt;');
		val = val.replace(/>/g, '&gt;');
		val = val.replace(/&/g, '&amp;');
	}
	return val;
};

//设置Input错误样式
var setErrClass = function(e,obj){
	if(e){
		obj.addClass("iptErr");
	}else{
		obj.removeClass("iptErr");
	}
};

//手机号正则匹配
var isMobile = function(val){
	return /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/.test(val);
};

//设置只读状态
var setFormItem = function(e,id){
	var $obj = $(id);
	var $submit = $obj.find("input[type='submit']");
	var items = $obj.find("input,textarea,select,button").not(".operateItem,[type='button'],[type='submit']");
	if(e){
		for(var i=0; i<items.length; i++){
			items.eq(i).prop("disabled",true);
		}
		$submit.hide();
		$obj.find(".uploadifybox").addClass("upload_disabled");
	}else{
		for(i=0;i<items.length;i++){
			var thid = items.eq(i).attr("id");
			if(thid != "pre_submit" || $("#isPreSub").is(":checked")){
				items.eq(i).prop("disabled",false);
			}
		}
		$submit.show();
		$obj.find(".uploadifybox").removeClass("upload_disabled");
		$obj.find(".upload_del").show();
	}
	setErrClass(false,$(".iptErr")); //清除上次错误时的样式
};

//后台处理日期如果不为日期返回空值
var dateConfirm = function(v,t){
	if (/^(\d{4})-(\d{2})-(\d{2})\s+(\d{2}):(\d{2}):(\d{2})$/.test(v)){
		if(t == 1)$("#isPreSub").prop("checked",true);
		return v;
	}else{
		return "";
	}
};

//检查AJAX请求返回参数是否会话过期
var checkAjaxSession = function(data,type,obj,txts){
	var status = data.getResponseHeader("sessionstatus");
	if(status == "sessionOut"){
		top.location.href = g_requestContextPath + "/";
	}else{
		if(obj){
			//popLayer(stringMsg["serverErr"]);
			if(type == 1){
				obj.append(txts);
			}else{
				obj.html(txts);
			}
		}else if(type){
			popLayer(stringMsg["serverErr"]);
		}
	}
};

//上传图片 type:1 标的图片上传可传多张,type:2 上传banner图 只能单张
var imagesUpload = function(o,p,type,datatype,paras,isauto){
	var obj = $(o);								//ID
	var hobj = $(p);							//target ID
	var multi = (type == 2)?false:true;			//默认true
	var auto = (isauto == 0)?isauto:true;
	//var dtype = datatype?datatype:"";
	obj.uploadify({
		//'debug'					:	true,
		'swf'					:	g_requestContextPath+'/static/js/uploadify/uploadify.swf',
		'uploader'				:	g_requestContextPath+'/uploadPic;jsessionid=' + jsessionid,
		//'scriptData'			:	{jsessionid: jsessionid},
		'width'					:	110,
		'height'				:	30,
		'multi'					:	multi,
		'auto'					:	auto,
		'buttonImage'			:	g_requestContextPath+'/static/images/admin/uploadifyBtn.png',
		'fileObjName'			:	'uploadFile',
		'fileTypeDesc'			:	'请选择jpg,jpeg,gif,png,bmp文件',
		'fileTypeExts'			:	'*.jpg;*.jpeg;*.png;*.gif;*.bmp',
		'fileSizeLimit'			:	2048,
		/*2015.08 无需分类型，固不需要传参
        'onUploadStart'			:	function () {  
        	//paras 传参来源：1 CMS管理中传参type 2:借款人申请处的type为3
        	if(paras == 1){
        		obj.uploadify("settings", "formData", {'jsessionid': jsessionid});
        	}else if(paras == 2){
        		obj.uploadify("settings", "formData", {'jsessionid': jsessionid});
        	}
            //在onUploadStart事件中，也就是上传之前，把参数写好传递到后台。  
        },
        */
		'onSelect'				:	function(file) {
			//自动上传显示loading
			if(auto){
				$(o).parent().addClass("uploading");
			}else{
				hobj.val(file.name);
			}
        },
		'onUploadError'			:	function (file,errorCode,errorMsg,errorString) {  
			alert("上传失败:"+file.name+"不能被上传" + errorString + errorCode + errorMsg);
			$(o).parent().removeClass("uploading");
		},
		'onUploadSuccess'		:	function(file, data, response) {
			var result = formatJsons(data);	//json化数据
			console.log(result)
			if (result.success == true) {
				//var details = formatJsons(datas.detail);	//json化数据
				//console.log(details);
				if(type == 1){
					var id = new Date().getTime();
					var isorg = (p == "#oriUploadDiv")?"1":"";		//是否是原版资料 11， 12， 13， 14
					var typeElse = (isorg == "1")?"20":"10";
					hobj.append('<tr><td><a href="'+ imgdomain + result.obj +'" target="_blank" title="查看大图"><img src="' + imgdomain + result.obj + '" width="100" height="60" /></a></td>'+
							'<td><select name="imagetype'+id+'">' +
							'	<option value="'+ isorg +'1">身份信息</option>' +
							'	<option value="'+ isorg +'2">抵押物信息</option>' +
							'	<option value="'+ isorg +'3">合同信息</option>' +
							'	<option value="'+ typeElse + '">其它</option>' +
							'</select></td>' +
							'<td><input name="imageIds" type="hidden" value="'+id+'" /><input name="hidden'+id+'" type="hidden" value="'+ result.obj +'" /><input name="description'+id+'" type="text" class="ipt longIpt" /></td>'+
							'<td><a href="" onclick="return false;" class="upload_del" title="删除">删除</a></td></tr>');
				}else if(type == 2){
					hobj.val(result.obj);
				}
				$(o).parent().removeClass("uploading");
			}else{
				//alert("上传图片过程中出现错误，请稍候再试！");
				alert(result.message);
				$(o).parent().removeClass("uploading");
			}
		}
		
	});
};

//搜索中指定域中值拼接为对象
var getJsonParam = function(sId) {
	var result = {};
	var $obj = $("#" + sId);
	$obj.find("input,select").each(function(index, data) {
		var $data = $(this);
		if($data.attr('type') == 'checkbox'){
			if($data.is(':checked')){
				if(result[$data.attr("name")]){
					result[$data.attr("name")] = result[$data.attr("name")]+ ',' + $data.val();
				}else{
					result[$data.attr("name")] = $data.val().trim();
				}
			}
		}
		else if($data.attr("name") && $data.val() != "") {
			result[$data.attr("name")] = $data.val();
		}
	});
	return result;
};

//导出excel时获得get参数
var getExcelParam = function(sId) {
	var s = "",count = 0,obj = getJsonParam(sId);
	for ( var p in obj) {
		if (count == 0) {
			s += "?";
		}else{
			s += "&";
		}
		s += p + "=" + obj[p];
		count++;
	}
	return s;
};

//保存中样式
var saveLoading = function(obj,t,txt){
	var stxt = txt?txt:"保存中";
	if(t == 1){
		var m = $('<div class="btnsSaving">'+stxt+'</div>');
		obj.prop("disabled",true).parent().append(m);
	}else{
		var m = obj.prop("disabled",false).parent().find(".btnsSaving");
		m.remove();
	}
};

//切换标签及样式
var tagsSwitch = function(the,obj){
	var eq = the.parent().find("li").removeClass("current").index(the.addClass("current"));
	$(obj).hide().eq(eq).show();
	return eq;
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
	}
};

//获取指定URL参数
var getQueryString = function(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]); return "";
};
	
// 验证日期 00 00 00
var isDate6 = function(sDate) {
	if(!/^[0-9]{6}$/.test(sDate)) {
		return false;
	}
	var year, month;
	year = 19 + sDate.substring(0, 2);
	month = sDate.substring(4, 6);
	if (year < 1700 || year > 2500) return false;
	if (month < 1 || month > 12) return false;
	return true;
};

// 验证日期 0000 00 00
var isDate8 = function(sDate) {
	if(!/^[0-9]{8}$/.test(sDate)) {
		return false;
	}
	var year, month, day;
	year = sDate.substring(0, 4);
	month = sDate.substring(4, 6);
	day = sDate.substring(6, 8);
	var iaMonthDays = [31,28,31,30,31,30,31,31,30,31,30,31];
	if (year < 1700 || year > 2500) return false;
	if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) iaMonthDays[1]=29;
	if (month < 1 || month > 12) return false;
	if (day < 1 || day > iaMonthDays[month - 1]) return false;
	return true;
};

// 验证身份证号码
var isIdCardNo = function(num) {
	var factorArr = new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1);
	var parityBit=new Array("1","0","X","9","8","7","6","5","4","3","2");
	var varArray = new Array();
	var lngProduct = 0;
	var intCheckDigit;
	var intStrLen = num.length;
	var idNumber = num;
	// initialize
	if ((intStrLen != 15) && (intStrLen != 18)) {
		return false;
	}
	// check and set value
	for(var i=0;i<intStrLen;i++) {
		varArray[i] = idNumber.charAt(i);
		if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17)) {
			return false;
		} else if (i < 17) {
			varArray[i] = varArray[i] * factorArr[i];
		}
	}
	
	if (intStrLen == 18) {
		//check date
		var date8 = idNumber.substring(6,14);
		if (isDate8(date8) == false) {
			return false;
		}
		// calculate the sum of the products
		for(i=0;i<17;i++) {
			lngProduct = lngProduct + varArray[i];
		}
		// calculate the check digit
		intCheckDigit = parityBit[lngProduct % 11];
		// check last digit
		if (varArray[17] != intCheckDigit) {
			return false;
		}
	}else{        //length is 15
		//check date
		var date6 = idNumber.substring(6,12);
		
		if (isDate6(date6) == false) {
			return false;
		}
	}
	return true;
};

//刷新列表，根据传来的ID
var refreshList = function(pager,fun){
	if($(pager).is(":visible")){
		$(pager).find(".pagego").click();
	}else{
		fun();
	}
};

//ajax统一设置
$.ajaxSetup({
	type : "POST",
	async : false,
	dataType: "json",
	complete:function(data){
		checkAjaxSession(data);
	}
});

$.fn.extend({ 
	formSubmit:function(url,successAction){
		var options = {
				url: url,
				type: 'post',
				success: function (data) {
					if (data.success || data.code=='1000') {	
						if(successAction)
							successAction(data);
					}else{
						toastr.warning(data.message);
					}
				},
				error: function(data) {
				}
			};
			$(this).ajaxSubmit(options);
		}
});

$.cookie = function(key, value, options) {
	if (arguments.length > 1 && (value === null || typeof value !== "object")) {
		options = $.extend({}, options);
		if (value === null) {
			options.expires = -1;
		}
		if (typeof options.expires === 'number') {
			var days = options.expires, t = options.expires = new Date();
			t.setDate(t.getDate() + days);
		}
		return (document.cookie = [ encodeURIComponent(key), '=', options.raw ? String(value) : encodeURIComponent(String(value)), options.expires ? '; expires=' + options.expires.toUTCString() : '', options.path ? '; path=' + options.path : '', options.domain ? '; domain=' + options.domain : '', options.secure ? '; secure' : '' ].join(''));
	}
	options = value || {};
	var result, decode = options.raw ? function(s) {
		return s;
	} : decodeURIComponent;
	return (result = new RegExp('(?:^|; )' + encodeURIComponent(key) + '=([^;]*)').exec(document.cookie)) ? decode(result[1]) : null;
};

$.formatString = function(str) {
	for ( var i = 0; i < arguments.length - 1; i++) {
		str = str.replace("{" + i + "}", arguments[i + 1]);
	}
	return str;
};

$.stringToList = function(value) {
	if (value != undefined && value != '') {
		var values = [];
		var t = value.split(',');
		for ( var i = 0; i < t.length; i++) {
			values.push('' + t[i]);/* 避免他将ID当成数字 */
		}
		return values;
	} else {
		return [];
	}
};

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, '');
};
String.prototype.ltrim = function() {
	return this.replace(/(^\s*)/g, '');
};
String.prototype.rtrim = function() {
	return this.replace(/(\s*$)/g, '');
};

$.serializeObject = function(form) {
	var o = {};
	$.each(form.serializeArray(), function(index) {
		if (o[this['name']]) {
			o[this['name']] = o[this['name']] + "," + this['value'];
		} else {
			o[this['name']] = this['value'];
		}
	});
	return o;
};



/*!
 * version: 1.0-2015.08.25
 * Requires jQuery v1.7 or later and jquery.validate.js
 * Copyright (c) 2014 © DongHongQiao
 */

	$.validator.setDefaults({       
		onkeyup:false
	});
	
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
	  
	// 身份证号码验证       
	jQuery.validator.addMethod("isIdCardNo", function(value, element) {       
		return this.optional(element) || isIdCardNo(value);       
	}, "");    
		 
	// 手机号码验证       
	jQuery.validator.addMethod("isMobile", function(value, element) {
		var length = value.length;   
		var mobile = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;    
		return this.optional(element) || (length == 11 && mobile.test(value));       
	}, "");
	
	// 金额验证       
	jQuery.validator.addMethod("isMoney", function(value, element) {       
		var val = /^([1-9][\d]*|0)(\.[\d]{1,2})?$/;       
		return this.optional(element) || (val.test(value) && value > 0);       
	}, "");

	// 是否为空判断
	jQuery.validator.addMethod("isBlank", function(value, element) {  
		return this.optional(element) || value.trim() != "";       
	}, "");
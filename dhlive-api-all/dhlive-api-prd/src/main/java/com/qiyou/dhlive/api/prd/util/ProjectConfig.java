package com.qiyou.dhlive.api.prd.util;

import java.util.Properties;

import com.yaozhong.framework.base.common.utils.LoadPropertiesUtil;

public class ProjectConfig {
	
	private static Properties prop=null; 
	
	static{
		if(prop == null){
			prop = LoadPropertiesUtil.loadFile("./conf/worker.properties");
		}
	}
	
	public static String getValue(String key) {
		if(prop==null){
			return "";
		}
		String value=prop.getProperty(key);
		if (value==null) {
			return "";
		}
		return value;
	}
	
	public static String getImagesHost(){
		return getValue("host.images");
	}
	
	public static String getCdnHost(){
		return getValue("host.cdn");
	}
	
	public static boolean isOpenTestPay(){
		String  openPay = getValue("pay.opentest");
		if("true".equals(openPay)){
			return true;
		}
		return false;
	}

}

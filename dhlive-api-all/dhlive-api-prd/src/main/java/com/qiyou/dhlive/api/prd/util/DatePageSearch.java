package com.qiyou.dhlive.api.prd.util;


import com.yaozhong.framework.base.database.domain.page.PageSearch;

public class DatePageSearch extends PageSearch {

	private static final long serialVersionUID = -5892735547168770147L;
	
	private String startDate;
	
	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}

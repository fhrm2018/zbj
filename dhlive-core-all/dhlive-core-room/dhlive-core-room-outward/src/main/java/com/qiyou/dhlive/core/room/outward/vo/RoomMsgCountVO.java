package com.qiyou.dhlive.core.room.outward.vo;

import com.qiyou.dhlive.core.room.outward.model.RoomMsgCount;

public class RoomMsgCountVO extends RoomMsgCount {
	
	private String beginDate;
	
	private String endDate;
	
	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}

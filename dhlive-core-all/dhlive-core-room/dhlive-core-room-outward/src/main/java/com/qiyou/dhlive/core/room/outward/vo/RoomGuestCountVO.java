package com.qiyou.dhlive.core.room.outward.vo;

import java.util.Date;

import com.qiyou.dhlive.core.room.outward.model.RoomGuestCount;

public class RoomGuestCountVO extends RoomGuestCount {
	
	private Date beginDate;
	
	private Date endDate;
	
	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}

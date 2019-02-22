package com.qiyou.dhlive.core.room.outward.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

@Table(name = "room_guest_count")
public class RoomGuestCount implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "guest_count")
	private Integer guestCount;
	
	@Column(name = "guest_date")
	private Date guestDate;
	
	@Column(name = "create_time")
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getGuestCount() {
		return guestCount;
	}

	public void setGuestCount(Integer guestCount) {
		this.guestCount = guestCount;
	}

	public Date getGuestDate() {
		return guestDate;
	}

	public void setGuestDate(Date guestDate) {
		this.guestDate = guestDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	

}

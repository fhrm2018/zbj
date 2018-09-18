package com.qiyou.dhlive.core.base.outward.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * @author Administrator
 * 日志记录实体类
 * 2018年4月23日
 */
@Table(name = "base_opt_log")
public class BaseOptLog implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6151862932060769341L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private Integer type;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "group_id")
    private Integer groupId;
    
    @Column(name = "opt_user_id")
    private Integer optUserId;
    
    @Column(name = "opt_employee_id")
    private Integer optEmployeeId;
    
    @Column(name = "ope_msg")
    private String opeMsg;
    
    @Column(name = "opt_time")
    private Date optTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getOptUserId() {
		return optUserId;
	}

	public void setOptUserId(Integer optUserId) {
		this.optUserId = optUserId;
	}

	public Integer getOptEmployeeId() {
		return optEmployeeId;
	}

	public void setOptEmployeeId(Integer optEmployeeId) {
		this.optEmployeeId = optEmployeeId;
	}

	public String getOpeMsg() {
		return opeMsg;
	}

	public void setOpeMsg(String opeMsg) {
		this.opeMsg = opeMsg;
	}

	public Date getOptTime() {
		return optTime;
	}

	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}


    
}

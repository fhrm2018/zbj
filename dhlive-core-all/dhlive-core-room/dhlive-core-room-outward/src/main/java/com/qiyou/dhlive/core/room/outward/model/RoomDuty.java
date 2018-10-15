package com.qiyou.dhlive.core.room.outward.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by fish on 2018/9/11.
 */
@Table(name = "room_duty")
public class RoomDuty implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "manage_ids")
    private String manageIds;

    @Column(name = "manage_names")
    private String manageNames;
    
    @Column(name = "modify_user_id")
    private Integer modifyUserId;

    @Column(name = "modify_time")
    private Date modifyTime;

    @Column(name = "create_user_id")
    private Integer createUserId;

    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getManageIds() {
		return manageIds;
	}

	public void setManageIds(String manageIds) {
		this.manageIds = manageIds;
	}

	public String getManageNames() {
		return manageNames;
	}

	public void setManageNames(String manageNames) {
		this.manageNames = manageNames;
	}

	public Integer getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
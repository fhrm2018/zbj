package com.qiyou.dhlive.core.room.outward.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "room_auto_user")
public class RoomAutoUser implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "auto_user_name")
    private String autoUserName;
    
    @Column(name = "level")
    private Integer level;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAutoUserName() {
		return autoUserName;
	}

	public void setAutoUserName(String autoUserName) {
		this.autoUserName = autoUserName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
}

package com.qiyou.dhlive.core.live.outward.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "live_inform")
public class LiveInform implements Serializable {
     
	
	    @Id
	    @Column(name = "id")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	    private String informTitle;
	    private String informContent;
	    private Integer informState;
	    public Integer getInformState() {
			return informState;
		}
		public void setInformState(Integer informState) {
			this.informState = informState;
		}
		private Date createTime;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getInformTitle() {
			return informTitle;
		}
		public void setInformTitle(String informTitle) {
			this.informTitle = informTitle;
		}
		public String getInformContent() {
			return informContent;
		}
		public void setInformContent(String informContent) {
			this.informContent = informContent;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
}

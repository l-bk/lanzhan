package com.modules.position.entity;

import java.util.Date;

import com.common.persistence.BaseEntity;
import com.common.persistence.DataEntity;
import com.modules.copy.entity.Contextdefine;

public class Position extends DataEntity<Position>{
	private static final long serialVersionUID = 1L;
	private Integer posid;
	private String stat;
	private String description;
	private String type ;
	private Date createdate;
	private Date medifydate;
	
	public Position(){
		super();
		stat="1";
	}
	
	
	public Integer getPosid() {
		return posid;
	}
	public void setPosid(Integer posid) {
		this.posid = posid;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Date getMedifydate() {
		return medifydate;
	}
	public void setMedifydate(Date medifydate) {
		this.medifydate = medifydate;
	}
	
	@Override
	public void preInsert() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub
		
	}
}
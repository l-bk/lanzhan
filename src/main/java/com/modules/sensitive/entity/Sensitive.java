package com.modules.sensitive.entity;

import com.common.persistence.DataEntity;
import com.modules.sys.entity.User;

public class Sensitive extends DataEntity<Sensitive>{
	private Integer sid;
	private String value;
	private User user;
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}

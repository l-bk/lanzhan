package com.modules.msgsource.entity;

import com.common.persistence.DataEntity;
import com.common.utils.StringUtils;
import com.modules.sys.entity.User;

public class Msgsource extends DataEntity<Msgsource> {
	private Integer sid;
	private String name;
	private String url;
	private User user;

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

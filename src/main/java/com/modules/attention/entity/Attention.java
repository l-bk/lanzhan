package com.modules.attention.entity;

import com.common.persistence.DataEntity;
import com.modules.cms.entity.Category;
import com.modules.msgsource.entity.Msgsource;
import com.modules.position.entity.Position;
import com.modules.sys.entity.User;


public class Attention extends DataEntity<Attention>{
	private Integer aid;
	private String targetId;
	private String type;
	private User user;
	private Category category;
	private Position position;
	private Msgsource msgsource;
	private String category_id;
	private Integer posid;
	private Integer msgid;
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public Integer getPosid() {
		return posid;
	}
	public void setPosid(Integer posid) {
		this.posid = posid;
	}
	public Integer getMsgid() {
		return msgid;
	}
	public void setMsgid(Integer msgid) {
		this.msgid = msgid;
	}
	public Integer getAid() {
		return aid;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public void setAid(Integer aid) {
		this.aid = aid;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public Msgsource getMsgsource() {
		return msgsource;
	}
	public void setMsgsource(Msgsource msgsource) {
		this.msgsource = msgsource;
	}
	
}

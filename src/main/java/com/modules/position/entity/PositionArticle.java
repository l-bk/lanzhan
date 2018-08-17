package com.modules.position.entity;

import java.util.Date;

import com.common.persistence.DataEntity;
import com.modules.cms.entity.Article;

public class PositionArticle extends DataEntity<PositionArticle>{
	private static final long serialVersionUID = 1L;
	private Integer sid;
	private String posid;
	private String articleId;
	private String stat;
	private Date createdate;
	private Date medifydate;
	private Article article;
	private Integer number;
	private Position position;
	private String []ids;
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public String getPosid() {
		return posid;
	}
	public void setPosid(String posid) {
		this.posid = posid;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
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
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String [] getIds() {
		return ids;
	}
	public void setIds(String [] ids) {
		this.ids = ids;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
}

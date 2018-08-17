package com.modules.copy.entity;

import java.sql.Date;

import com.common.persistence.BaseEntity;

public class Contextdefine extends BaseEntity<Contextdefine> {
	private static final long serialVersionUID = 1L;
	private Integer cid;
	private String stat;
	private String description;
	private String url;
	private String lang; // 站点编码
	private String contentLang; // 文章页编码
	private String beginTime;
	private String ischeckflag;
	private Date medifydate;
	private String medifymanage;
	private Integer copyflag;
	private String posid;

	public String getContentLang() {
		return contentLang;
	}

	public void setContentLang(String contentLang) {
		this.contentLang = contentLang;
	}

	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

	public Integer getCopyflag() {
		return copyflag;
	}

	public void setCopyflag(Integer copyflag) {
		this.copyflag = copyflag;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getIscheckflag() {
		return ischeckflag;
	}

	public void setIscheckflag(String ischeckflag) {
		this.ischeckflag = ischeckflag;
	}

	public Date getMedifydate() {
		return medifydate;
	}

	public void setMedifydate(Date medifydate) {
		this.medifydate = medifydate;
	}

	public String getMedifymanage() {
		return medifymanage;
	}

	public void setMedifymanage(String medifymanage) {
		this.medifymanage = medifymanage;
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

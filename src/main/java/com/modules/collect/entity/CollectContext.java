package com.modules.collect.entity;

import com.common.persistence.BaseEntity;

public class CollectContext extends BaseEntity<CollectContext> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;    //主建
	private String stat;    //状态
	private String description;    //描述
	private String url;    //采集的一级域名
	private String lang;    //采集的网站的页面编码
	private String begin_time;    //定时采集的时间表示
	private String ischeckflag;    //是否直接入库
	private String medifydate;    //
	private String medifymanage;    //

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	public String getIscheckflag() {
		return ischeckflag;
	}

	public void setIscheckflag(String ischeckflag) {
		this.ischeckflag = ischeckflag;
	}

	public String getMedifydate() {
		return medifydate;
	}

	public void setMedifydate(String medifydate) {
		this.medifydate = medifydate;
	}

	public String getMedifymanage() {
		return medifymanage;
	}

	public void setMedifymanage(String medifymanage) {
		this.medifymanage = medifymanage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

package com.modules.ads.entity;

import java.util.Date;

import com.common.persistence.DataEntity;


public class Ads extends DataEntity<Ads> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String aid;
	private String clsid;
	private String typeid;
	private String tagname;
	private String adname;
	private String timeset;
	private String starttime;
	private String endtime;
	private String normbody;
	private String expbody;
	private String advPosid;

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getClsid() {
		return clsid;
	}

	public void setClsid(String clsid) {
		this.clsid = clsid;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public String getAdname() {
		return adname;
	}

	public void setAdname(String adname) {
		this.adname = adname;
	}

	public String getTimeset() {
		return timeset;
	}

	public void setTimeset(String timeset) {
		this.timeset = timeset;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	
	public String getStarttime() {
		return starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getNormbody() {
		return normbody;
	}

	public void setNormbody(String normbody) {
		this.normbody = normbody;
	}

	public String getExpbody() {
		return expbody;
	}

	public void setExpbody(String expbody) {
		this.expbody = expbody;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAdvPosid() {
		return advPosid;
	}

	public void setAdvPosid(String advPosid) {
		this.advPosid = advPosid;
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

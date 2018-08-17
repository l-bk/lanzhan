package com.modules.Template.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefaultTemplate {

	private String templatefilename;
	private String templatepath;
	private String parentDirname;
	private Date modifydate;
	private String context;
	private List<DefaultTemplate> files = new ArrayList<DefaultTemplate>();
	private String suffix;
	public String getTemplatefilename() {
		return templatefilename;
	}
	public void setTemplatefilename(String templatefilename) {
		this.templatefilename = templatefilename;
	}
	public String getTemplatepath() {
		return templatepath;
	}
	public void setTemplatepath(String templatepath) {
		this.templatepath = templatepath;
	}
	public String getParentDirname() {
		return parentDirname;
	}
	public void setParentDirname(String parentDirname) {
		this.parentDirname = parentDirname;
	}
	public Date getModifydate() {
		return modifydate;
	}
	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public List<DefaultTemplate> getFiles() {
		return files;
	}
	public void setFiles(List<DefaultTemplate> files) {
		this.files = files;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	

}

package com.modules.cms.utils;

public class ArticlePageModel {

	public static Integer pageSize = 10;
	private String centext;
	private String[] lines;
	private Integer count;
	private String articleData;

	private Integer index;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	private Integer last;

	public String getCentext() {
		return centext;
	}

	public void setCentext(String centext) {
		this.centext = centext;
	}

	public String[] getLines() {
		return lines;
	}

	public void setLines(String[] lines) {
		this.lines = lines;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getArticleData() {
		return articleData;
	}

	public void setArticleData(String articleData) {
		this.articleData = articleData;
	}

	public Integer getLast() {
		return count;
	}

}
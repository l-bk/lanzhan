/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.copy.entity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.common.persistence.DataEntity;

/**
 * 采集过滤Entity
 * 
 * @author kj
 * @version 2016-08-15
 */
public class ContextNodeDefineFilter extends DataEntity<ContextNodeDefineFilter> {

	private static final long serialVersionUID = 1L;
	private Integer cid; // id
	private String title; // 过滤规则标题,可以为空
	private Integer nodeDefineId; // 节点定义标识
	private String begin; // 开始表达式
	private String end; // 结束表达式
	private String type; // 类型:1:替换begin和end之间 2: 从begin+(.*) +end 3:begin+(.*)
							// 4
	private String parentid;

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public ContextNodeDefineFilter() {
		super();
	}

	public ContextNodeDefineFilter(String id) {
		super(id);
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	@Length(min = 0, max = 255, message = "过滤规则标题,可以为空长度必须介于 0 和 255 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getNodeDefineId() {
		return nodeDefineId;
	}

	public void setNodeDefineId(Integer nodeDefineId) {
		this.nodeDefineId = nodeDefineId;
	}

	@Length(min = 0, max = 512, message = "开始表达式长度必须介于 0 和 512 之间")
	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	@Length(min = 0, max = 512, message = "结束表达式长度必须介于 0 和 512 之间")
	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	@Length(min = 0, max = 512, message = "4长度必须介于 0 和 2 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean getIsNewRecord() {
		if (cid == null) {
			return true;
		} else {
			return false;
		}
	}

}
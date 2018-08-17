/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.entity;

import org.hibernate.validator.constraints.Length;

import com.common.persistence.DataEntity;

/**
 * 更新模板Entity
 * @author mkj
 * @version 2016-09-21
 */
public class CmsUpdatePage extends DataEntity<CmsUpdatePage> {
	
	private static final long serialVersionUID = 1L;
	private String updatePageName;		// 更新的页面名称
	private String type;		// 触发更新的情况
	private String isUpdate;		// 是否需要更新
	private String state;		// 更新状态
	private String categoryId;		// 栏目标识
	private String isMobileUpdate;      // 手机端静态
	
	public String getIsMobileUpdate() {
		return isMobileUpdate;
	}

	public void setIsMobileUpdate(String isMobileUpdate) {
		this.isMobileUpdate = isMobileUpdate;
	}

	public CmsUpdatePage() {
		super();
	}

	public CmsUpdatePage(String id){
		super(id);
	}

	@Length(min=0, max=32, message="更新的页面名称长度必须介于 0 和 32 之间")
	public String getUpdatePageName() {
		return updatePageName;
	}

	public void setUpdatePageName(String updatePageName) {
		this.updatePageName = updatePageName;
	}
	
	@Length(min=0, max=2, message="触发更新的情况长度必须介于 0 和 2 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=2, message="是否需要更新长度必须介于 0 和 2 之间")
	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
	
	@Length(min=0, max=2, message="更新状态长度必须介于 0 和 2 之间")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@Length(min=0, max=64, message="栏目标识长度必须介于 0 和 64 之间")
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
}
/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.persistence.Page;
import com.common.service.CrudService;
import com.modules.cms.entity.CmsUpdatePage;
import com.modules.cms.dao.CmsUpdatePageDao;

/**
 * 更新模板Service
 * @author mkj
 * @version 2016-09-21
 */
@Service
@Transactional(readOnly = true)
public class CmsUpdatePageService extends CrudService<CmsUpdatePageDao, CmsUpdatePage> {
	
	@Autowired
	private CmsUpdatePageDao updatePageDao;

	public CmsUpdatePage get(String id) {
		return super.get(id);
	}
	
	public List<CmsUpdatePage> findList(CmsUpdatePage cmsUpdatePage) {
		return super.findList(cmsUpdatePage);
	}
	
	public Page<CmsUpdatePage> findPage(Page<CmsUpdatePage> page, CmsUpdatePage cmsUpdatePage) {
		return super.findPage(page, cmsUpdatePage);
	}
	
	@Transactional(readOnly = false)
	public void save(CmsUpdatePage cmsUpdatePage) {
		super.save(cmsUpdatePage);
	}
	
	@Transactional(readOnly = false)
	public void delete(CmsUpdatePage cmsUpdatePage) {
		super.delete(cmsUpdatePage);
	}
	
	@Transactional(readOnly = false)
	public void update(CmsUpdatePage updatePage) {
		updatePageDao.update(updatePage);	
	}
	
}
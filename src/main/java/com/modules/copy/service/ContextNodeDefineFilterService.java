/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.copy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.persistence.Page;
import com.common.service.CrudService;
import com.modules.copy.entity.ContextNodeDefineFilter;
import com.modules.copy.entity.Contextdefine;
import com.modules.copy.entity.Contextnodedefine;
import com.modules.copy.dao.ContextNodeDefineFilterDao;

/**
 * 采集过滤Service
 * @author kj
 * @version 2016-08-15
 */
@Service
@Transactional(readOnly = true)
public class ContextNodeDefineFilterService extends CrudService<ContextNodeDefineFilterDao, ContextNodeDefineFilter> {
	
	@Autowired
	private ContextNodeDefineFilterDao filterMapper;

	public ContextNodeDefineFilter get(String id) {
		return super.get(id);
	}
	
	public List<ContextNodeDefineFilter> findList(ContextNodeDefineFilter contextNodeDefineFilter) {
		return super.findList(contextNodeDefineFilter);
	}
	
	public Page<ContextNodeDefineFilter> findPage(Page<ContextNodeDefineFilter> page, ContextNodeDefineFilter contextNodeDefineFilter) {
		return super.findPage(page, contextNodeDefineFilter);
	}
	
	@Transactional(readOnly = false)
	public void save(ContextNodeDefineFilter contextNodeDefineFilter) {
		super.save(contextNodeDefineFilter);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContextNodeDefineFilter contextNodeDefineFilter) {
		super.delete(contextNodeDefineFilter);
	}

	public List<ContextNodeDefineFilter> findListByNodeId(Contextnodedefine contextnodedefine) {
		return filterMapper.findListByNodeId(contextnodedefine);
	}

	public List<ContextNodeDefineFilter> findAll(ContextNodeDefineFilter contextNodeDefineFilter) {
		return filterMapper.findAll(contextNodeDefineFilter);
	}
	
}
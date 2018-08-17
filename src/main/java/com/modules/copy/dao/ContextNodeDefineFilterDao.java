/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.copy.dao;

import java.util.List;

import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.copy.entity.ContextNodeDefineFilter;
import com.modules.copy.entity.Contextdefine;
import com.modules.copy.entity.Contextnodedefine;

/**
 * 采集过滤DAO接口
 * @author kj
 * @version 2016-08-15
 */
@MyBatisDao
public interface ContextNodeDefineFilterDao extends CrudDao<ContextNodeDefineFilter> {

	List<ContextNodeDefineFilter> findListByNodeId(Contextnodedefine contextnodedefine);

	List<ContextNodeDefineFilter> findAll(ContextNodeDefineFilter contextNodeDefineFilter);
	
}
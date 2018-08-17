/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.dao;

import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.cms.entity.CmsUpdatePage;

/**
 * 更新模板DAO接口
 * @author mkj
 * @version 2016-09-21
 */
@MyBatisDao
public interface CmsUpdatePageDao extends CrudDao<CmsUpdatePage> {
	
}
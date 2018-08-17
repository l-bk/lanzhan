/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.dao;

import java.util.Map;

import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.cms.entity.ArticleData;
import com.modules.cms.entity.ArticleDataTemp;

/**
 * 文章DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@MyBatisDao
public interface ArticleDataTempDao extends CrudDao<ArticleDataTemp> {
	
	public int postAllarticleData(String categoryId);
	
	public int deleteArticleDate(String categoryId);
	
	public int deleteArticleDateById(String id);
}

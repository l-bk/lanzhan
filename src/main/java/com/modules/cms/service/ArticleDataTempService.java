/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.service.CrudService;
import com.modules.cms.dao.ArticleDataDao;
import com.modules.cms.dao.ArticleDataTempDao;
import com.modules.cms.dao.CategoryDao;
import com.modules.cms.entity.ArticleData;
import com.modules.cms.entity.ArticleDataTemp;

/**
 * 站点Service
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleDataTempService extends CrudService<ArticleDataTempDao, ArticleDataTemp> {

	@Autowired
	private ArticleDataTempDao articleDataTempDao;
	
	@Transactional(readOnly = false)
	public int postAllarticleData(String categoryId){
		
		return articleDataTempDao.postAllarticleData(categoryId);
	}
	
	@Transactional(readOnly = false)
	public int deleteArticleDate(String categoryId){
		
		return articleDataTempDao.deleteArticleDate(categoryId);
	}
	
	@Transactional(readOnly = false)
	public int deleteArticleDateById(String id){
		
		return articleDataTempDao.deleteArticleDateById(id);
	}
	
}

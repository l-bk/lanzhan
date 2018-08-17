/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.dao;

import java.util.List;
import java.util.Map;

import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.ArticleDataTemp;
import com.modules.cms.entity.ArticleTemp;
import com.modules.cms.entity.Category;

/**
 * 文章DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@MyBatisDao
public interface ArticleTempDao extends CrudDao<ArticleTemp> {
	
	public List<ArticleTemp> findByIdIn(String[] ids);
//	{
//		return find("from Article where id in (:p1)", new Parameter(new Object[]{ids}));
//	}
	
	public int updateHitsAddOne(String id);
//	{
//		return update("update Article set hits=hits+1 where id = :p1", new Parameter(id));
//	}
	
	public int updateExpiredWeight(ArticleTemp article);
	
	public List<Category> findStats(Category category);
//	{
//		return update("update Article set weight=0 where weight > 0 and weightDate < current_timestamp()");
//	}
	
	public int postAllarticle(String categoryId);
	
	public int deleteArticle(String categoryId);
	
	public int deleteArticleById(String id);
	
	public int putalltemp(List<ArticleTemp>list);
	
	public List<ArticleTemp> findputtemp();
	
	public int deleteAll();
	
	public int putalldatatemp(List<ArticleDataTemp>list);
	
	public List<ArticleDataTemp> findputdatatemp();
	
	public int deleteAlldata();
	
	public List<ArticleDataTemp>findAlldataList();
}

/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.Category;
import com.modules.position.entity.PositionArticle;

/**
 * 文章DAO接口
 * 
 * @author ThinkGem
 * @version 2013-8-23
 */
@MyBatisDao
public interface ArticleDao extends CrudDao<Article> {

	public List<Article> findByIdIn(String[] ids);

	// {
	// return find("from Article where id in (:p1)", new Parameter(new
	// Object[]{ids}));
	// }

	public int updateHitsAddOne(String id);

	// {
	// return update("update Article set hits=hits+1 where id = :p1", new
	// Parameter(id));
	// }

	public int updateExpiredWeight(Article article);

	public List<Category> findStats(Category category);

	// {
	// return
	// update("update Article set weight=0 where weight > 0 and weightDate <
	// current_timestamp()");
	// }
	public List<Article> getPosList(Map map);

	public void UpdateByTiming();

	public List<Article> findTimeList();

	public List<Article> findposiarticleList(Article article);

	public int updatenumber(Article article);

	public Article maxid();

	public List<Article> getRondomList(Map map);

	public List<Article> getTodayHitList(Map map);

	public int clearTodayHit();

	public int PositionArticleForNumber(PositionArticle article);

	public int checkAcquisitionSource(Map map);

	public Article findArticle(String id);

	public int updateArticle(Article article);

	public int updateStaticPage(Article article);
	
	public int updateNoneStatic();
	
	public Article getById(String id);
}

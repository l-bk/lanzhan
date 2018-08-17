package com.modules.position.dao;

import java.util.List;

import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.cms.entity.Article;
import com.modules.position.entity.PositionArticle;
import com.modules.sys.entity.Dict;

@MyBatisDao
public interface PositionArticleDao extends CrudDao<PositionArticle>{
	public List<PositionArticle> findAll(PositionArticle positionArticle);

	public int save(PositionArticle positionArticle);
	
	public int saveAhead(PositionArticle positionArticle);

	public int update(PositionArticle positionArticle);
	
	public int updateAhead(PositionArticle positionArticle);

	public int delete(int sid);
	
	public int deleteAhead(PositionArticle positionArticle);
	
	public int delByPosid(int posid);
	
	public int delByArticleId(String articleid);
	public int delByArticleIdAhead(String articleid);
	
	public List<PositionArticle> findAll2(PositionArticle positionArticle);
	
	public PositionArticle maxnumber();
}

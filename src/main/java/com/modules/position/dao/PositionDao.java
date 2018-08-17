package com.modules.position.dao;

import java.util.List;
import java.util.Map;

import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.position.entity.Position;
import com.modules.sys.entity.Dict;

@MyBatisDao
public interface PositionDao extends CrudDao<Position>{
	public List<Position> findAlltwo(Position position);

	public int save(Position position);

	public int insert(Position position);
	
	public int marge(Position position);

	public int deleteByPosid(int posid);
	
	public int delposiarticle(int posid);
	
	public List<Integer> getArticlePosid(String aid);
}

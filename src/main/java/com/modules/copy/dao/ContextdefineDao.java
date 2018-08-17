package com.modules.copy.dao;

import java.util.List;

import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.copy.entity.Contextdefine;
import com.modules.sys.entity.Dict;

@MyBatisDao
public interface ContextdefineDao extends CrudDao<Dict> {
	public List<Contextdefine> findAll(Contextdefine contextdefine);

	public int save(Contextdefine contextdefine);

	public int update(Contextdefine contextdefine);

	public int delete(int cid);
}

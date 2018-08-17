package com.modules.copy.dao;

import java.util.List;

import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.copy.entity.Contextnodedefine;
import com.modules.sys.entity.Dict;
@MyBatisDao
public interface ContextnodedefineDao extends CrudDao<Dict> {
	public List<Contextnodedefine> findAll(Contextnodedefine contextnodedefine);

	public int save(Contextnodedefine contextnodedefine);

	public int update(Contextnodedefine contextnodedefine);

	public int delete(int cid);
}

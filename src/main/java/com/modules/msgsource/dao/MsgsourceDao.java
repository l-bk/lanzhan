package com.modules.msgsource.dao;

import java.util.List;

import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.msgsource.entity.Msgsource;

@MyBatisDao
public interface MsgsourceDao extends CrudDao<Msgsource> {
	public List<Msgsource> getName(Msgsource entity);
}

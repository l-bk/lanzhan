package com.modules.sensitive.dao;

import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.sensitive.entity.Sensitive;


@MyBatisDao
public interface SensitiveDao extends CrudDao<Sensitive>{
	
}

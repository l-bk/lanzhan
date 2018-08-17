package com.modules.attention.dao;

import java.util.List;


import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.attention.entity.Attention;


@MyBatisDao
public interface AttentionDao extends CrudDao<Attention> {
	public List<Attention> findByIdIn(String[] ids);

	

	
}

package com.modules.copy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modules.copy.dao.ContextdefineDao;
import com.modules.copy.entity.Contextdefine;

@Service
public class ContextdefineService {
	@Autowired
	private ContextdefineDao contextdefineDao;

	public List<Contextdefine> findAll(Contextdefine contextdefine) {
		return contextdefineDao.findAll(contextdefine);
	}

	public int save(Contextdefine contextdefine) {
		return contextdefineDao.save(contextdefine);
	}

	public int update(Contextdefine contextdefine) {
		return contextdefineDao.update(contextdefine);
	}

	public int delete(int cid) {
		return contextdefineDao.delete(cid);
	}
}

package com.modules.copy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modules.copy.dao.ContextnodedefineDao;
import com.modules.copy.entity.Contextnodedefine;

@Service
public class ContextnodedefineService {
	@Autowired
	private ContextnodedefineDao contextnodedefineDao;

	public List<Contextnodedefine> findAll(Contextnodedefine contextnodedefine) {
		return contextnodedefineDao.findAll(contextnodedefine);
	}

	public int save(Contextnodedefine contextnodedefine) {
		return contextnodedefineDao.save(contextnodedefine);
	}

	public int update(Contextnodedefine contextnodedefine) {
		return contextnodedefineDao.update(contextnodedefine);
	}

	public int delete(int cid) {
		return contextnodedefineDao.delete(cid);
	}
}

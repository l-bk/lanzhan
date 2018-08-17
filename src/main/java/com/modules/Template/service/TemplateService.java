package com.modules.Template.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modules.Template.dao.TemplateDao;
import com.modules.Template.entity.Template;

@Service
public class TemplateService {
	@Autowired
	TemplateDao templateDao;

	public List<Template> findAll(Template t) {
		return templateDao.findAll(t);
	}

	public int save(Template template) {
		return templateDao.save(template);
	}

	public int update(Template template) {
		return templateDao.update(template);
	}

	public int delete(int cid) {
		return templateDao.delete(cid);
	}

	public Template get(Template t) {
		return templateDao.get(t);
	}
}

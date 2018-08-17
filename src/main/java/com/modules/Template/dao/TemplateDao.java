package com.modules.Template.dao;

import java.util.List;
import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.Template.entity.Template;
import com.modules.sys.entity.Dict;

@MyBatisDao
public interface TemplateDao extends CrudDao<Dict> {
	public List<Template> findAll(Template t);

	public int save(Template template);

	public int update(Template template);

	public int delete(int cid);

	public Template get(Template t);
}

package com.modules.sensitive.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.persistence.Page;
import com.common.service.CrudService;
import com.modules.sensitive.dao.SensitiveDao;
import com.modules.sensitive.entity.Sensitive;
import com.modules.sys.utils.UserUtils;
@Service
@Transactional(readOnly = true)
public class SensitiveService extends CrudService<SensitiveDao, Sensitive>{

	@Transactional(readOnly = false)
	public Page<Sensitive> findPage(Page<Sensitive> page, Sensitive sensitive,
			boolean isDataScopeFilter) {
		return super.findPage(page, sensitive);

	}

	@Transactional(readOnly = false)
	public void save(Sensitive sensitive) {
		
		sensitive.setUpdateBy(UserUtils.getUser());
		sensitive.setUpdateDate(new Date());
		
		
		if (sensitive.getSid()!=null&&sensitive.getSid().SIZE>0) {
			
			dao.update(sensitive);
		} else {
			sensitive.setCreateBy(UserUtils.getUser());
			sensitive.setCreateDate(new Date());
			
			dao.insert(sensitive);
			
			
		}
	}

	@Transactional(readOnly = false)
	public void delete(Sensitive sensitive, Boolean isRe) {
		// dao.updateDelFlag(id,
		// isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		// 使用下面方法，以便更新索引。
		// Article article = dao.get(id);
		// article.setDelFlag(isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		// dao.insert(article);
		super.delete(sensitive);
	}

}

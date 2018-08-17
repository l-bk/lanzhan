package com.modules.msgsource.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.persistence.Page;
import com.common.service.CrudService;
import com.modules.msgsource.dao.MsgsourceDao;
import com.modules.msgsource.entity.Msgsource;
import com.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class MsgsourceService extends CrudService<MsgsourceDao, Msgsource> {

	private Logger log = Logger.getLogger(MsgsourceService.class);

	@Transactional(readOnly = false)
	public Page<Msgsource> findPage(Page<Msgsource> page, Msgsource msgsource,
			boolean isDataScopeFilter) {
		return super.findPage(page, msgsource);

	}

	@Transactional(readOnly = false)
	public void save(Msgsource msgsource) {

		msgsource.setUpdateBy(UserUtils.getUser());
		msgsource.setUpdateDate(new Date());

		if (msgsource.getSid() != null && msgsource.getSid().SIZE > 0) {

			dao.update(msgsource);
		} else {

			msgsource.setCreateBy(UserUtils.getUser());
			msgsource.setCreateDate(new Date());

			dao.insert(msgsource);

		}
	}

	@Transactional(readOnly = false)
	public void delete(Msgsource msgsource, Boolean isRe) {
		// dao.updateDelFlag(id,
		// isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		// 使用下面方法，以便更新索引。
		// Article article = dao.get(id);
		// article.setDelFlag(isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		// dao.insert(article);
		super.delete(msgsource);
	}

	public Msgsource getName(Msgsource msgsource) {
		List<Msgsource> list = dao.getName(msgsource);
		if (list != null && list.size() > 0) {
			Msgsource msgsource2 = list.get(0);
			return msgsource2;
		} else {
			return null;
		}
	}

}

package com.modules.attention.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.persistence.Page;
import com.common.service.CrudService;
import com.modules.attention.dao.AttentionDao;
import com.modules.attention.entity.Attention;
import com.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class AttentionService extends CrudService<AttentionDao, Attention> {
	@Transactional(readOnly = false)
	public Page<Attention> findPage(Page<Attention> page, Attention attention,
			boolean isDataScopeFilter) {
		System.out.println("type=" + attention.getType());
		return super.findPage(page, attention);

	}

	@Transactional(readOnly = false)
	public void save(Attention attention) {

		attention.setUpdateBy(UserUtils.getUser());
		attention.setUpdateDate(new Date());

		System.out.println("aid==" + attention.getAid().SIZE);

		if (attention.getAid() != null && attention.getAid().SIZE > 0) {

			dao.update(attention);
		} else {

			attention.setCreateBy(UserUtils.getUser());
			List<Attention> list = dao.findList(attention);
			if (list != null && list.size() > 0) {
				System.out.println("该管理员已经关注该栏目");
			} else {
				attention.setCreateDate(new Date());
				dao.insert(attention);
			}

		}
	}

	@Transactional(readOnly = false)
	public void delete(Attention attention, Boolean isRe) {
		// dao.updateDelFlag(id,
		// isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		// 使用下面方法，以便更新索引。
		// Article article = dao.get(id);
		// article.setDelFlag(isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		// dao.insert(article);
		super.delete(attention);
	}
}

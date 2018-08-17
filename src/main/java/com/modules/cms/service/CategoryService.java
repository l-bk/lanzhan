/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.common.config.Global;
import com.common.persistence.Page;
import com.common.service.TreeService;
import com.modules.attention.dao.AttentionDao;
import com.modules.attention.entity.Attention;
import com.modules.cms.dao.CategoryDao;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.Site;
import com.modules.cms.utils.CmsUtils;
import com.modules.sys.entity.Office;
import com.modules.sys.entity.User;
import com.modules.sys.utils.UserUtils;

/**
 * 栏目Service
 * 
 * @author ThinkGem
 * @version 2013-5-31
 */
@Service
@Transactional(readOnly = true)
public class CategoryService extends TreeService<CategoryDao, Category> {
	private Logger log = Logger.getLogger(CategoryService.class);
	@Autowired
	private AttentionDao attentionDao;
	public static final String CACHE_CATEGORY_LIST = "categoryList";

	private Category entity = new Category();

	@SuppressWarnings("unchecked")
	public List<Category> findByUser(boolean isCurrentSite, String module) {

		List<Category> list = (List<Category>) UserUtils
				.getCache(CACHE_CATEGORY_LIST);
		if (list == null) {
			User user = UserUtils.getUser();
			Category category = new Category();
			category.setOffice(new Office());
			category.getSqlMap().put("dsf", dataScopeFilter(user, "o", "u"));
			category.setSite(new Site());
			category.setParent(new Category());
			list = dao.findList(category);
			// 将没有父节点的节点，找到父节点
			Set<String> parentIdSet = Sets.newHashSet();
			for (Category e : list) {
				if (e.getParent() != null
						&& StringUtils.isNotBlank(e.getParent().getId())) {
					boolean isExistParent = false;
					for (Category e2 : list) {
						if (e.getParent().getId().equals(e2.getId())) {
							isExistParent = true;
							break;
						}
					}
					if (!isExistParent) {
						parentIdSet.add(e.getParent().getId());
					}
				}
			}
			if (parentIdSet.size() > 0) {
				// FIXME 暂且注释，用于测试
				// dc = dao.createDetachedCriteria();
				// dc.add(Restrictions.in("id", parentIdSet));
				// dc.add(Restrictions.eq("delFlag", Category.DEL_FLAG_NORMAL));
				// dc.addOrder(Order.asc("site.id")).addOrder(Order.asc("sort"));
				// list.addAll(0, dao.find(dc));
			}
			UserUtils.putCache(CACHE_CATEGORY_LIST, list);
		}

		if (isCurrentSite) {
			List<Category> categoryList = Lists.newArrayList();
			for (Category e : list) {
				if (Category.isRoot(e.getId())
						|| (e.getSite() != null && e.getSite().getId() != null && e
								.getSite().getId()
								.equals(Site.getCurrentSiteId()))) {
					if (StringUtils.isNotEmpty(module)) {
						if (module.equals(e.getModule())
								|| "".equals(e.getModule())) {
							categoryList.add(e);
						}
					} else {
						categoryList.add(e);
					}
				}
			}
			return categoryList;
		}
		return list;
	}

	public List<Category> findByParentId(String parentId, String siteId) {
		Category parent = new Category();
		parent.setId(parentId);
		entity.setParent(parent);
		Site site = new Site();
		site.setId(siteId);
		entity.setSite(site);
		return dao.findByParentIdAndSiteId(entity);
	}

	public Page<Category> find(Page<Category> page, Category category) {
		// DetachedCriteria dc = dao.createDetachedCriteria();
		// if (category.getSite()!=null &&
		// StringUtils.isNotBlank(category.getSite().getId())){
		// dc.createAlias("site", "site");
		// dc.add(Restrictions.eq("site.id", category.getSite().getId()));
		// }
		// if (category.getParent()!=null &&
		// StringUtils.isNotBlank(category.getParent().getId())){
		// dc.createAlias("parent", "parent");
		// dc.add(Restrictions.eq("parent.id", category.getParent().getId()));
		// }
		// if (StringUtils.isNotBlank(category.getInMenu()) &&
		// Category.SHOW.equals(category.getInMenu())){
		// dc.add(Restrictions.eq("inMenu", category.getInMenu()));
		// }
		// dc.add(Restrictions.eq(Category.FIELD_DEL_FLAG,
		// Category.DEL_FLAG_NORMAL));
		// dc.addOrder(Order.asc("site.id")).addOrder(Order.asc("sort"));
		// return dao.find(page, dc);
		// page.setSpringPage(dao.findByParentId(category.getParent().getId(),
		// page.getSpringPage()));
		// return page;
		category.setPage(page);
		category.setInMenu(Global.SHOW);
		page.setList(dao.findModule(category));
		return page;
	}
	
	//获取子站点，顶级栏目不在菜单显示
	public Page<Category> find2(Page<Category> page, Category category) {
		category.setPage(page);
		if(category.getInMenu()!=null || category.getInMenu() !=("")){
			category.setInMenu("");
		}
		//log.info("************"+category.getInMenu());
		page.setList(dao.findModule(category));
		return page;
	}

	@Transactional(readOnly = false)
	public void save(Category category) {
		category.setSite(new Site(Site.getCurrentSiteId()));
		if (StringUtils.isNotBlank(category.getViewConfig())) {
			category.setViewConfig(StringEscapeUtils.unescapeHtml4(category
					.getViewConfig()));
		}
		super.save(category);
		log.info("category.id==="+category.getId());
		if (category.getId() != null && !category.getId().equals("")) {
			Attention attention = new Attention();
			attention.setCategory_id(category.getId());
			attention.setType("0");
			attention.setCreateBy(UserUtils.getUser());
			List<Attention> list = attentionDao.findList(attention);
			if (category.getAttention().equals("1")) {
				if (list != null && list.size() == 1) {
					log.info("list.sise==" + list.size()+"   list.get(0).getCategory_id()=="+list.get(0).getCategory_id());
					
				} else {
					
					attention.setCreateDate(new Date());
					attention.setUpdateBy(UserUtils.getUser());
					attention.setUpdateDate(new Date());
					
					attention.setCategory(category);
					log.info("attention.categoryid=="+attention.getCategory_id());
					attentionDao.insert(attention);
				}
			} else {
				if (list != null && list.size() == 1) {
					Attention delattention=list.get(0);
					attentionDao.delete(delattention);
				}
			}
		}
		UserUtils.removeCache(CACHE_CATEGORY_LIST);
		CmsUtils.removeCache("mainNavList_" + category.getSite().getId());
	}

	@Transactional(readOnly = false)
	public void delete(Category category) {
		super.delete(category);
		Attention attention = new Attention();
		attention.setCategory_id(category.getId());
		attention.setType("0");
		attention.setCreateBy(UserUtils.getUser());
		List<Attention> list = attentionDao.findList(attention);
		if(list!=null&&list.size()==1){
			Attention delattention=list.get(0);
			attentionDao.delete(delattention);
		}
		UserUtils.removeCache(CACHE_CATEGORY_LIST);
		CmsUtils.removeCache("mainNavList_" + category.getSite().getId());
	}

	/**
	 * 通过编号获取栏目列表
	 */
	public List<Category> findByIds(String ids) {
		List<Category> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids, ",");
		if (idss.length > 0) {
			// List<Category> l = dao.findByIdIn(idss);
			// for (String id : idss){
			// for (Category e : l){
			// if (e.getId().equals(id)){
			// list.add(e);
			// break;
			// }
			// }
			// }
			for (String id : idss) {
				Category e = dao.get(id);
				if (null != e) {
					// System.out.println("e.id:"+e.getId()+",e.name:"+e.getName());
					list.add(e);
				}
				// list.add(dao.get(id));

			}
		}
		return list;
	}

	public List<Category> findByAttention() {
		List<Category> list = Lists.newArrayList();
		list = dao.findByAttention();
		return list;

	}
	
}

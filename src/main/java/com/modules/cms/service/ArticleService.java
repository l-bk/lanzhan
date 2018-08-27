/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.common.config.Global;
import com.common.persistence.Page;
import com.common.service.CrudService;
import com.common.utils.CacheUtils;
import com.common.utils.StringUtils;
import com.modules.cms.dao.ArticleDao;
import com.modules.cms.dao.ArticleDataDao;
import com.modules.cms.dao.CategoryDao;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.ArticleData;
import com.modules.cms.entity.Category;
import com.modules.msgsource.dao.MsgsourceDao;
import com.modules.msgsource.entity.Msgsource;
import com.modules.position.dao.PositionArticleDao;
import com.modules.position.entity.PositionArticle;
import com.modules.position.util.PositionUtil;
import com.modules.sys.entity.User;
import com.modules.sys.utils.UserUtils;

/**
 * 文章Service
 * 
 * @author ThinkGem
 * @version 2013-05-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleService extends CrudService<ArticleDao, Article> {

	private Logger log = Logger.getLogger(ArticleService.class);

	@Value("${gwebpath}")
	protected String webpath;
	
	@Value("${wapgwebpath}")
	protected String wapgwebpath;

	

	
	@Autowired
	private ArticleDataDao articleDataDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private PositionArticleDao positionArticleDao;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private MsgsourceDao msgsourceDao;

	@Transactional(readOnly = false)
	public Page<Article> findPage(Page<Article> page, Article article, boolean isDataScopeFilter) {
		// 更新过期的权重，间隔为“6”个小时
		Date updateExpiredWeightDate = (Date) CacheUtils.get("updateExpiredWeightDateByArticle");
		if (updateExpiredWeightDate == null
				|| (updateExpiredWeightDate != null && updateExpiredWeightDate.getTime() < new Date().getTime())) {
			dao.updateExpiredWeight(article);
			CacheUtils.put("updateExpiredWeightDateByArticle", DateUtils.addHours(new Date(), 6));
		}
		// DetachedCriteria dc = dao.createDetachedCriteria();
		// dc.createAlias("category", "category");
		// dc.createAlias("category.site", "category.site");
		log.info("id:" + article.getId());
		if (article.getCategory() != null) {
			if (StringUtils.isNotBlank(article.getCategory().getId()) && !Category.isRoot(article.getCategory().getId())) {
				Category category = categoryDao.get(article.getCategory().getId());
				if (category == null) {
					category = new Category();
				}
				category.setParentIds(category.getId());
				category.setSite(category.getSite());
				article.setCategory(category);
			}
		} else {
			article.setCategory(new Category());
		}
		if (article.getMsgsource() != null && article.getMsgsource().getSid().SIZE > 0) {
			// System.out.println("111");
			Msgsource msgsource = msgsourceDao.get(article.getMsgsource());
			if (msgsource == null) {
				// System.out.println("222");
				msgsource = new Msgsource();
			}
			article.setMsgsource(msgsource);
		} else {
			// System.out.println("333");
			article.setMsgsource(new Msgsource());
		}

		// article.setPosid("");
		// if (StringUtils.isBlank(page.getOrderBy())){
		// page.setOrderBy("a.weight,a.update_date desc");
		// }
		// return dao.find(page, dc);
		// article.getSqlMap().put("dsf",
		// dataScopeFilter(article.getCurrentUser(), "o", "u"));
		return super.findPage(page, article);

	}

	@Transactional(readOnly = false)
	public void save(Article article) {
		if (article.getArticleData().getContent() != null) {
			article.getArticleData().setContent(StringEscapeUtils.unescapeHtml4(article.getArticleData().getContent()));
		}
		// 如果没有审核权限，则将当前内容改为待审核状态
		if (!UserUtils.getSubject().isPermitted("cms:article:audit")) {
			article.setDelFlag(Article.DEL_FLAG_AUDIT);
		}
		// // 如果栏目不需要审核，则将该内容设为发布状态
		// if
		// (article.getCategory()!=null&&StringUtils.isNotBlank(article.getCategory().getId())){
		// Category category = categoryDao.get(article.getCategory().getId());
		// if (!Global.YES.equals(category.getIsAudit())){
		// article.setDelFlag(Article.DEL_FLAG_NORMAL);
		// }
		// }

		article.setUpdateBy(UserUtils.getUser());
		article.setUpdateDate(new Date());
		if (StringUtils.isNotBlank(article.getViewConfig())) {
			article.setViewConfig(StringEscapeUtils.unescapeHtml4(article.getViewConfig()));
		}

		ArticleData articleData = new ArticleData();
		if (StringUtils.isBlank(article.getId())) {
			article.preInsert();

			/*
			 * Article t = articleDao.maxid(); if (t != null) {
			 * log.info("maxid:" + t.getId()); article.setId("" +
			 * (Long.parseLong(t.getId()) + 1)); // articleData.setId("" +
			 * Integer.parseInt(t.getId()) + 1); } else { article.setId("" + 1);
			 * // articleData.setId("" + 1); }
			 */

			User user = UserUtils.getUser();
			if (StringUtils.isNotBlank(user.getId())) {
				article.setUpdateBy(user);
				article.setCreateBy(user);
			}
			article.setUpdateDate(new Date());
			article.setCreateDate(new Date());
			articleData = article.getArticleData();
			articleData.setId(article.getId());
			if (article.getPosid() != null && article.getPosid().length() > 0) {
				String posids[] = article.getPosid().split(",");
				String articleid = article.getId();
				for (String posid : posids) {
					PositionArticle positionArticle = new PositionArticle();
					PositionArticle article2 = positionArticleDao.maxnumber();
					Integer number = 1;
					if (article2.getNumber() != null) {
						number = article2.getNumber() + 1;
					}
					log.info("生成文章的排序为==" + number);
					positionArticle.setArticleId(articleid);
					positionArticle.setPosid(posid);
					positionArticle.setCreatedate(new Date());
					positionArticle.setMedifydate(new Date());
					positionArticle.setStat("1");
					positionArticle.setNumber(number);
					positionArticleDao.save(positionArticle);
					positionArticleDao.saveAhead(positionArticle);
				}
			}
			dao.insert(article);
			articleDataDao.insert(articleData);
		} else {
			
			article.preUpdate();
			articleData = article.getArticleData();
			articleData.setId(article.getId());
		
			
			if (article.getPosid() != null && article.getPosid().length() > 0) {
				String posids[] = article.getPosid().split(",");
				String articleid = article.getId();
				PositionArticle positionArticle = new PositionArticle();
				positionArticle.setArticleId(articleid);
				List<PositionArticle> delist = positionArticleDao.findAll(positionArticle);
				System.out.println("articleid==" + articleid + ",delist.size=" + delist.size());

				// if (palist != null && palist.size() > 0) {
				// positionArticleDao.delByArticleId(articleid);
				// System.out.println("删除关联文章");
				// }

				for (PositionArticle pa : delist) {

					boolean checkposid = false;

					for (String posid : posids) {
						if (pa.getPosid().equals(posid)) {

							checkposid = true;
						}
					}
					if (!checkposid) {
						log.info("删除的推荐位文章articleid=" + pa.getArticleId() + ",posid=" + pa.getPosid());
						positionArticleDao.delete(pa.getSid());
						positionArticleDao.deleteAhead(pa);

					}
				}
				for (String posid : posids) {

					positionArticle.setPosid(posid);
					List<PositionArticle> palist = positionArticleDao.findAll(positionArticle);
					System.out.println("关联文件数：" + palist);
					if (palist != null && palist.size() > 0) {

					} else {
						PositionArticle article2 = positionArticleDao.maxnumber();
						Integer number = 1;
						if (article2 != null) {
							if (article2.getNumber() != null) {
								number = article2.getNumber() + 1;
							}
						}
						log.info("生成文章的排序为==" + number);
						PositionArticle positionArticle2 = new PositionArticle();
						positionArticle2.setArticleId(articleid);
						positionArticle2.setPosid(posid);
						positionArticle2.setCreatedate(new Date());
						positionArticle2.setMedifydate(new Date());
						positionArticle2.setStat("1");
						positionArticle2.setNumber(number);
						positionArticleDao.save(positionArticle2);
						positionArticleDao.saveAhead(positionArticle2);
					}
				}

			} else {
				log.info("删除文章:" + article.getId() + "在推荐位上的记录");

				positionArticleDao.delByArticleId(article.getId());
				positionArticleDao.delByArticleIdAhead(article.getId());
			}
			dao.update(article);
			articleDataDao.update(article.getArticleData());
		/*	Article  newArticle = dao.get(article.getId());
			article.setIsMobileStatis("1");
			article.setIsStatis("1");
			dao.update(article);*/
			//修改信息先删除服务器原来的文件，静态化就加新的
		/*	int i=0;
			for(;;) {
				if(i == 0) {
					File file1= new File(webpath + "a/hy/" + article.getId() + ".shtml");
					if(file1.exists()) {
						file1.delete();
					}
					File file2 = new File(wapgwebpath+ "a/hy/" + article.getId() + ".shtml");
					if(file2.exists()) {
						file2.delete();
					}
				}else {
					File file1 =new File(webpath + "a/hy/" + article.getId() + "_" + String.valueOf(i) +  ".shtml");
					File file2 = new File(wapgwebpath+ "a/hy/" + article.getId() + "_" + String.valueOf(i) +  ".shtml");
					if(!file1.exists() && !file2.exists()) {
						break;
					}
					file1.delete();
					file2.delete();
				}
				i = i+1;
			}*/
		}
	}

	@Transactional(readOnly = false)
	public void delete(Article article, Boolean isRe) {
		// dao.updateDelFlag(id,
		// isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		// 使用下面方法，以便更新索引。
		// Article article = dao.get(id);
		// article.setDelFlag(isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		// dao.insert(article);

		super.delete(article);
	}

	/**
	 * 通过编号获取内容标题
	 * 
	 * @return new Object[]{栏目Id,文章Id,文章标题}
	 */
	public List<Object[]> findByIds(String ids) {
		if (ids == null) {
			return new ArrayList<Object[]>();
		}
		List<Object[]> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids, ",");
		Article e = null;
		for (int i = 0; (idss.length - i) > 0; i++) {
			e = dao.get(idss[i]);
			list.add(new Object[] { e.getCategory().getId(), e.getId(), StringUtils.abbr(e.getTitle(), 50) });
		}
		return list;
	}

	/**
	 * 点击数加一
	 */
	@Transactional(readOnly = false)
	public void updateHitsAddOne(String id) {
		dao.updateHitsAddOne(id);
	}

	/**
	 * 更新索引
	 */
	public void createIndex() {
		// dao.createIndex();
	}

	/**
	 * 全文检索
	 */
	// FIXME 暂不提供检索功能
	public Page<Article> search(Page<Article> page, String q, String categoryId, String beginDate, String endDate) {

		// 设置查询条件
		// BooleanQuery query = dao.getFullTextQuery(q,
		// "title","keywords","description","articleData.content");
		//
		// // 设置过滤条件
		// List<BooleanClause> bcList = Lists.newArrayList();
		//
		// bcList.add(new BooleanClause(new TermQuery(new
		// Term(Article.FIELD_DEL_FLAG, Article.DEL_FLAG_NORMAL)), Occur.MUST));
		// if (StringUtils.isNotBlank(categoryId)){
		// bcList.add(new BooleanClause(new TermQuery(new Term("category.ids",
		// categoryId)), Occur.MUST));
		// }
		//
		// if (StringUtils.isNotBlank(beginDate) &&
		// StringUtils.isNotBlank(endDate)) {
		// bcList.add(new BooleanClause(new TermRangeQuery("updateDate",
		// beginDate.replaceAll("-", ""),
		// endDate.replaceAll("-", ""), true, true), Occur.MUST));
		// }

		// BooleanQuery queryFilter =
		// dao.getFullTextQuery((BooleanClause[])bcList.toArray(new
		// BooleanClause[bcList.size()]));

		// System.out.println(queryFilter);

		// 设置排序（默认相识度排序）
		// FIXME 暂时不提供lucene检索
		// Sort sort = null;//new Sort(new SortField("updateDate",
		// SortField.DOC, true));
		// 全文检索
		// dao.search(page, query, queryFilter, sort);
		// 关键字高亮
		// dao.keywordsHighlight(query, page.getList(), 30, "title");
		// dao.keywordsHighlight(query, page.getList(), 130,
		// "description","articleData.content");

		return page;
	}

	public List<Article> getPosList(String category_id, String type, Integer index, Integer size, Integer officSize) {
		Map map = new LinkedHashMap();
		if (category_id != null && category_id.length() > 0 && category_id != "1" && category_id != "0") {
			map.put("category_id", category_id);
		}
		if (type != null && type.length() > 0) {
			map.put("type", type);
		}
		if (index != null && index.SIZE > 0) {
			Integer toindex = officSize == null? 0 : officSize + index;
			if (index != 0) {
				toindex = index * size + officSize;
			}
			map.put("index", toindex);
		}
		if (size != null && size.SIZE > 0) {
			map.put("size", size);
		}
		return dao.getPosList(map);
	}

	public List<Article> getRondomList(String category_id, Integer size) {
		Map map = new LinkedHashMap();
		if (category_id != null && category_id.length() > 0 && category_id != "1" && category_id != "0") {
			map.put("category_id", category_id);
		}
		if (size != null && size.SIZE > 0) {
			map.put("size", size);
		}
		return dao.getRondomList(map);
	}

	public Page<Article> findposiarticlePage(Page<Article> page, Article article, boolean isDataScopeFilter) {
		// 更新过期的权重，间隔为“6”个小时
		// Date updateExpiredWeightDate = (Date) CacheUtils
		// .get("updateExpiredWeightDateByArticle");
		// if (updateExpiredWeightDate == null
		// || (updateExpiredWeightDate != null && updateExpiredWeightDate
		// .getTime() < new Date().getTime())) {
		// dao.updateExpiredWeight(article);
		// CacheUtils.put("updateExpiredWeightDateByArticle",
		// DateUtils.addHours(new Date(), 6));
		// }

		if (article.getCategory() != null && StringUtils.isNotBlank(article.getCategory().getId())
				&& !Category.isRoot(article.getCategory().getId())) {
			Category category = categoryDao.get(article.getCategory().getId());

			if (category == null) {
				category = new Category();
			}
			category.setParentIds(category.getId());
			category.setSite(category.getSite());
			article.setCategory(category);
		} else {
			article.setCategory(new Category());
		}
		article.setPage(page);
		System.out.println("category.getId()==" + article.getCategory().getId());
		page.setList(articleDao.findposiarticleList(article));
		return page;
		// return super.findPage(page, article);

	}

	public Page<PositionArticle> findposiarticlePage2(Page<PositionArticle> page, PositionArticle positionArticle,
			boolean isDataScopeFilter) {

		if (positionArticle.getArticle().getCategory() != null && StringUtils.isNotBlank(positionArticle.getArticle().getCategory().getId())
				&& !Category.isRoot(positionArticle.getArticle().getCategory().getId())) {
			Category category = categoryDao.get(positionArticle.getArticle().getCategory().getId());

			if (category == null) {
				category = new Category();
			}
			category.setParentIds(category.getId());
			category.setSite(category.getSite());
			positionArticle.getArticle().setCategory(category);
		} else {
			positionArticle.getArticle().setCategory(new Category());
		}

		positionArticle.setPage(page);
		List<PositionArticle> plist = positionArticleDao.findAll2(positionArticle);
		System.out.println("关联文章数为===" + plist.size());
		page.setList(plist);
		return page;
		// return super.findPage(page, article);

	}

	// 更新文章排序
	@Transactional(readOnly = false)
	public void updatenumber(Article article) {
		dao.updatenumber(article);
	}

	public List<Article> getTodayHitList(Integer size) {
		Map map = new LinkedHashMap();
		if (size != null && size.SIZE > 0) {
			map.put("size", size);
		}
		return dao.getTodayHitList(map);
	}

	@Transactional(readOnly = false)
	public void clearTodayHit() {
		dao.clearTodayHit();
	}

	@Transactional(readOnly = false)
	public void PositionArticleForNumber(PositionArticle article) {
		dao.PositionArticleForNumber(article);
	}

	public Article findArticalById(String id) {
		Article article = dao.findArticle(id);
		return article;
	}

	@Transactional(readOnly = false)
	public int updateArticle(Article article) {
		return dao.updateArticle(article);
	}

	@Transactional(readOnly = false)
	public void updateNoneStatic() {
		dao.updateNoneStatic();
	}
	
	@Transactional(readOnly = false)
	public  List<Article> getAllNoStats() {
		return dao.getAllNoStats();
	}
}

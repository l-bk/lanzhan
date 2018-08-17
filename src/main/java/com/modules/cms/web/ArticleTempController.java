/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Untainted;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.common.mapper.JsonMapper;
import com.common.persistence.Page;
import com.common.utils.DateUtils;
import com.common.utils.StringUtils;
import com.common.web.BaseController;
import com.modules.cms.dao.ArticleDao;
import com.modules.cms.dao.ArticleDataDao;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.ArticleData;
import com.modules.cms.entity.ArticleDataTemp;
import com.modules.cms.entity.ArticleTemp;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.CmsUpdatePage;
import com.modules.cms.entity.Site;
import com.modules.cms.service.ArticleDataService;
import com.modules.cms.service.ArticleDataTempService;
import com.modules.cms.service.ArticleService;
import com.modules.cms.service.ArticleTempService;
import com.modules.cms.service.CategoryService;
import com.modules.cms.service.CmsUpdatePageService;
import com.modules.cms.service.FileTplService;
import com.modules.cms.service.SiteService;
import com.modules.cms.utils.CmsUtils;
import com.modules.cms.utils.TplUtils;
import com.modules.copy.web.RunMobileStatic;
import com.modules.copy.web.RunStaticPage;
import com.modules.position.dao.PositionArticleDao;
import com.modules.position.entity.PositionArticle;
import com.modules.sys.entity.User;
import com.modules.sys.utils.UserUtils;

/**
 * 文章Controller
 * 
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/articleTemp")
public class ArticleTempController extends BaseController {

	@Autowired
	private ArticleTempService articletempService;
	@Autowired
	private ArticleDataTempService articleDataService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private FileTplService fileTplService;
	@Autowired
	private SiteService siteService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private CmsUpdatePageService updatePageService;

	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private ArticleDataDao articleDateDao;
	@Autowired
	private PositionArticleDao positionArticleDao;
	@Autowired
	private RunStaticPage runStatic;
	@Autowired
	private RunMobileStatic mobileStatic;

	@ModelAttribute
	public ArticleTemp get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return articletempService.get(id);
		} else {
			return new ArticleTemp();
		}
	}

	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = { "list", "" })
	public String list(ArticleTemp article, HttpServletRequest request, HttpServletResponse response, Model model) {
		// for (int i=0; i<10000000; i++){
		// Article a = new Article();
		// a.setCategory(new Category(article.getCategory().getId()));
		// a.setTitle("测试测试测试测试测试测试测试测试"+a.getCategory().getId());
		// a.setArticleData(new ArticleData());
		// a.getArticleData().setContent(a.getTitle());
		// articleService.save(a);
		// }
		Page<ArticleTemp> page = articletempService.findPage(new Page<ArticleTemp>(request, response), article, true);
		System.out.println("page list===" + page.getList().size());
		// for(ArticleTemp t:page.getList()){
		// System.out.println("ca id==="+t.getCategory().getName());
		// }
		model.addAttribute("page", page);
		return "modules/cms/articleTempList";
	}

	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "form")
	public String form(ArticleTemp article, Model model) {
		// 如果当前传参有子节点，则选择取消传参选择
		if (article.getCategory() != null && StringUtils.isNotBlank(article.getCategory().getId())) {
			List<Category> list = categoryService.findByParentId(article.getCategory().getId(),
					Site.getCurrentSiteId());
			if (list.size() > 0) {
				article.setCategory(null);
			} else {
				article.setCategory(categoryService.get(article.getCategory().getId()));
			}
		}
		article.setArticleData(articleDataService.get(article.getId()));
		// if (article.getCategory()=null &&
		// StringUtils.isNotBlank(article.getCategory().getId())){
		// Category category =
		// categoryService.get(article.getCategory().getId());
		// }
		model.addAttribute("contentViewList", getTplContent());
		model.addAttribute("article_DEFAULT_TEMPLATE", Article.DEFAULT_TEMPLATE);
		model.addAttribute("articleTemp", article);
		CmsUtils.addViewConfigAttribute(model, article.getCategory());

		return "modules/cms/articleTempForm";
	}

	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "save")
	public String save(ArticleTemp article, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, article)) {
			return form(article, model);
		}
		articletempService.save(article);
		addMessage(redirectAttributes, "保存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功");
		String categoryId = article.getCategory() != null ? article.getCategory().getId() : null;
		return "redirect:" + adminPath + "/cms/articleTemp/?repage&category.id="
				+ (categoryId != null ? categoryId : "");
	}

	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "delete")
	public String delete(Article article, String categoryId, @RequestParam(required = false) Boolean isRe,
			RedirectAttributes redirectAttributes, String id) {
		// 如果没有审核权限，则不允许删除或发布。
		if (!UserUtils.getSubject().isPermitted("cms:article:audit")) {
			addMessage(redirectAttributes, "你没有删除或发布权限");
		}
		ArticleTemp article2 = this.get(id);
		article2.setArticleData(articleDataService.get(article2.getId()));
		if (isRe != null && isRe) {
			article2.setDelFlag(Article.DEL_FLAG_NORMAL);
		} else {
			article2.setDelFlag("1");
		}
		articletempService.save(article2);

		// articleService.delete(article, isRe);
		addMessage(redirectAttributes, (isRe != null && isRe ? "发布" : "删除") + "文章成功");
		return "redirect:" + adminPath + "/cms/articleTemp/?repage&category.id="
				+ (categoryId != null ? categoryId : "");
	}

	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "post")
	public String articlepost(ArticleTemp article, Model model, RedirectAttributes redirectAttributes) {
		System.out.println("into post");
		String[] ids = article.getIds();
		int suc = 0;
		int err = 0;
		for (String id : ids) {
			System.out.println("ids =====" + id);
			ArticleTemp temp = articletempService.get(id);

			Article article2 = new Article();
			article2.setAcquisitionSource(temp.getAcquisitionSource());
			System.out.println("getAcquisitionSource =====" + temp.getAcquisitionSource());
			article2.setCategory(new Category());
			List<Article> list = articleService.findList(article2);

			System.out.println("list =====" + list.size());

			if (list != null && list.size() > 0) {
				err++;
				continue;
			} else {
				ArticleTemp articleTemp = new ArticleTemp();

				articleTemp = articletempService.get(id);

				if (articleTemp.getPosid() != null && articleTemp.getPosid().length() > 0) {
					String posids[] = articleTemp.getPosid().split(",");
					String articleid = articleTemp.getId();
					for (String posid : posids) {
						PositionArticle positionArticle = new PositionArticle();
						PositionArticle article3 = positionArticleDao.maxnumber();
						Integer number = 1;
						if (article3.getNumber() != null) {
							number = article3.getNumber() + 1;
						}
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
				ArticleDataTemp articleDataTemp = articleDataService.get(id);
				Article insertarticle = new Article();
				ArticleData articleData = new ArticleData();
				insertarticle.setId(id);
				System.out.println("insertarticle.setId(id)===" + id);
				insertarticle.setCategory(articleTemp.getCategory());
				insertarticle.setTitle(articleTemp.getTitle());

				insertarticle.setLink(articleTemp.getLink());
				insertarticle.setColor(articleTemp.getColor());
				insertarticle.setImage(articleTemp.getImage());
				insertarticle.setKeywords(articleTemp.getKeywords());
				insertarticle.setDescription(articleTemp.getDescription());
				insertarticle.setWeight(articleTemp.getWeight());
				insertarticle.setWeightDate(articleTemp.getWeightDate());
				insertarticle.setHits(articleTemp.getHits());
				insertarticle.setPosid(articleTemp.getPosid());
				insertarticle.setCustomContentView(articleTemp.getCustomContentView());
				insertarticle.setViewConfig(articleTemp.getViewConfig());
				insertarticle.setBeginDate(articleTemp.getBeginDate());
				insertarticle.setEndDate(articleTemp.getEndDate());
				insertarticle.setUser(articleTemp.getUser());
				insertarticle.setAcquisitionSource(articleTemp.getAcquisitionSource());
				insertarticle.setDelFlag("0");
				insertarticle.setUpdateDate(articleTemp.getUpdateDate());
				insertarticle.setCreateDate(articleTemp.getCreateDate());
				insertarticle.setTiming(articleTemp.getTiming());
				insertarticle.setIstime(articleTemp.getIstime());
				insertarticle.setMsgid(articleTemp.getMsgid());
				insertarticle.setMsgsource(articleTemp.getMsgsource());
				insertarticle.setPosName(articleTemp.getPosName());
				insertarticle.setNumber(articleTemp.getNumber());
				insertarticle.setIsshowview(articleTemp.getIsshowview());
				User user = UserUtils.getUser();
				if (StringUtils.isNotBlank(user.getId())) {
					insertarticle.setUpdateBy(user);
					insertarticle.setCreateBy(user);
				}

				articleData.setId(insertarticle.getId());
				articleData.setContent(articleDataTemp.getContent());
				articleData.setCopyfrom(articleDataTemp.getCopyfrom());
				articleData.setRelation(articleDataTemp.getRelation());
				articleData.setAllowComment(articleDataTemp.getAllowComment());
				articleData.setArticle(insertarticle);
				insertarticle.setArticleData(articleData);
				try {
					articleDao.insert(insertarticle);
					articleDateDao.insert(articleData);
					articletempService.deleteArticleById(id);
					articleDataService.deleteArticleDateById(id);
					suc++;
					// 对应文章页进行静态化
					runStatic.manualStatic(id);
					mobileStatic.manualStatic(id);
					// addMessage(redirectAttributes, "发布文章成功");
				} catch (Exception e) {
					err++;
					// addMessage(redirectAttributes, "发布文章失败");
					logger.error(e.getMessage());
				}
			}
		}
		// 首页进行静态化
		CmsUpdatePage updatePage = new CmsUpdatePage();
		updatePage.setId("2");
		updatePage.setType("1");
		runStatic.indexPublic(updatePage);
		mobileStatic.indexPublic(updatePage);
		// 对应文章栏目页进行静态化（等待定时程序扫描）
		updatePage.setId("3");
		updatePage.setIsUpdate("1");
		updatePage.setIsMobileUpdate("1");
		updatePage.setType("4");
		updatePage.setState("1");
		updatePage.setUpdateDate(DateUtils.parseDate(DateUtils.getDate()));
		updatePage.setCategoryId("0");
		updatePageService.update(updatePage);
		addMessage(redirectAttributes, "发布文章成功" + suc + "条,发布文章失败" + err + "条");

		return "redirect:" + adminPath + "/cms/articleTemp/";
	}

	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "postAll")
	public String articlepostAll(ArticleTemp article, String categoryId, Model model,
			RedirectAttributes redirectAttributes) {

		List<ArticleTemp> list = articletempService.findputtemp();
		List<ArticleDataTemp> datalist = articletempService.findputdatatemp();
		List<ArticleTemp> alllist = articletempService.findAllList();
		List<ArticleDataTemp> alldatalist = articletempService.findAlldataList();
		logger.info("要发布的文章有" + list.size() + "条,内容有" + datalist.size() + "条");
		int rs = 0, datars = 0, drs = 0, ddatars = 0;
		if (list != null & list.size() > 0) {
			for (ArticleTemp article1 : list) {
				if (article1.getPosid() != null && article1.getPosid().length() > 0) {
					String posids[] = article1.getPosid().split(",");
					String articleid = article1.getId();
					for (String posid : posids) {
						PositionArticle positionArticle = new PositionArticle();
						PositionArticle article2 = positionArticleDao.maxnumber();
						Integer number = 1;
						if (article2.getNumber() != null) {
							number = article2.getNumber() + 1;
						}
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
			}
			try {
				rs = articletempService.putalltemp(list);
				datars = articletempService.putalldatatemp(datalist);
				logger.info("成功发布文章" + rs + "条");

				drs = articletempService.deleteAll();
				ddatars = articletempService.deleteAlldata();
			} catch (Exception e) {
				e.printStackTrace();
				addMessage(redirectAttributes, "发布文章失败");
				return "redirect:" + adminPath + "/cms/articleTemp/?repage&category.id="
						+ (categoryId != null ? categoryId : "");
			}
			for (ArticleTemp article2 : list) {
				// 对应文章页进行静态化
				runStatic.manualStatic(article2.getId());
				mobileStatic.manualStatic(article2.getId());
			}
			// 首页进行静态化
			CmsUpdatePage updatePage = new CmsUpdatePage();
			updatePage.setId("2");
			updatePage.setType("1");
			runStatic.indexPublic(updatePage);
			mobileStatic.indexPublic(updatePage);
			// 对应文章栏目页进行静态化（等待定时程序扫描）
			updatePage.setId("3");
			updatePage.setIsUpdate("1");
			updatePage.setIsMobileUpdate("1");
			updatePage.setType("4");
			updatePage.setState("1");
			updatePage.setUpdateDate(DateUtils.parseDate(DateUtils.getDate()));
			updatePage.setCategoryId("0");
			updatePageService.update(updatePage);
		} else {
			drs = articletempService.deleteAll();
			ddatars = articletempService.deleteAlldata();
		}
		addMessage(redirectAttributes, "成功发布文章" + rs + "条,清除重复文章" + (alllist.size() - rs) + "条");

		return "redirect:" + adminPath + "/cms/articleTemp/?repage&category.id="
				+ (categoryId != null ? categoryId : "");
	}

	/**
	 * 文章选择列表
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "selectList")
	public String selectList(ArticleTemp article, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		list(article, request, response, model);
		return "modules/cms/articleTempSelectList";
	}

	/**
	 * 通过编号获取文章标题
	 */
	@RequiresPermissions("cms:article:view")
	@ResponseBody
	@RequestMapping(value = "findByIds")
	public String findByIds(String ids) {
		List<Object[]> list = articletempService.findByIds(ids);
		return JsonMapper.nonDefaultMapper().toJson(list);
	}

	private List<String> getTplContent() {
		List<String> tplList = fileTplService
				.getNameListByPrefix(siteService.get(Site.getCurrentSiteId()).getSolutionPath());
		tplList = TplUtils.tplTrim(tplList, Article.DEFAULT_TEMPLATE, "");
		return tplList;
	}
}

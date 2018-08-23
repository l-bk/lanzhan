/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.web.front;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.config.Global;
import com.common.persistence.ArticleEntity;
import com.common.persistence.Page;
import com.common.servlet.ValidateCodeServlet;
import com.common.utils.Encodes;
import com.common.utils.StringUtils;
import com.common.utils.UserAgentUtils;
import com.common.web.BaseController;
import com.google.common.collect.Lists;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.ArticleData;
import com.modules.cms.entity.ArticleDataTemp;
import com.modules.cms.entity.ArticleTemp;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.Comment;
import com.modules.cms.entity.Link;
import com.modules.cms.entity.Site;
import com.modules.cms.service.ArticleDataService;
import com.modules.cms.service.ArticleDataTempService;
import com.modules.cms.service.ArticleService;
import com.modules.cms.service.ArticleTempService;
import com.modules.cms.service.CategoryService;
import com.modules.cms.service.CommentService;
import com.modules.cms.service.LinkService;
import com.modules.cms.service.SiteService;
import com.modules.cms.utils.ArticlePageModel;
import com.modules.cms.utils.CmsUtils;
import com.modules.msgsource.entity.Msgsource;

/**
 * 网站Controller
 * 
 * @author ThinkGem
 * @version 2013-5-29
 */
@Controller
@RequestMapping(value = "${frontPath}")
public class FrontController extends BaseController {

	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ArticleService articleService;
	@Autowired
	private ArticleDataService articleDataService;
	@Autowired
	private LinkService linkService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private SiteService siteService;
	@Value("${web.domain}")
	String domain;
	@Value("${web.auto.domain}")
	boolean autodomain;

	@Value("${web.hostname}")
	String hostname;

	@Autowired
	private ArticleTempService articleTempService;
	@Autowired
	private ArticleDataTempService articleDataTempService;

	/**
	 * 获取url后缀
	 */
	@ResponseBody
	@RequestMapping(value = "getUrlSuffix")
	public Map<String, String> getUrlSuffix(Model model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("urlSuffix", Global.getUrlSuffix());
		return map;
	}

	/**
	 * 获取推荐位的文章内容
	 */
	@ResponseBody
	@RequestMapping(value = "getPosArticleList")
	public Map<String, List<Article>> getPosArticleList(String category_id, String type, Integer size, String param,
			Model model, Integer officSize, HttpServletRequest request, HttpServletResponse response) {
		// List<Article> articleList = CmsUtils.getPositionList(category_id, type, size, param, officSize);
		List<Article> articleList = CmsUtils.getPositionList(category_id, type, 0, size, param, officSize);
		String description = "";
		String title = "";
		if (articleList != null && articleList.size() > 0) {
			for (Article article : articleList) {
				if(!StringUtils.isBlank(article.getLink())) { // 文章具有外链接
					article.setStaticUrl(article.getLink());
				}
				if (StringUtils.isBlank(article.getDescription())) {
					try {
						description = CmsUtils.getFirstPara(article);
						if (!StringUtils.isBlank(description)) {
							article.setDescription(description);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (StringUtils.isBlank(article.getShorttile())) {
					title = article.getTitle();
					if (!StringUtils.isBlank(title)) {
						article.setShorttile(title);
					}
				}
				if(article.getMsgsource() == null) {
					article.setMsgsource(new Msgsource());
				}
			}
		}
		Map<String, List<Article>> posArticle = new HashMap<String, List<Article>>();
		posArticle.put("articleList", articleList);
		return posArticle;
	}

	/**
	 * 获取主导航栏目列表
	 */
	@ResponseBody
	@RequestMapping(value = "getMainNavList")
	public Map<String, List<Category>> getMainNavList(String siteId, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		List<Category> categoryList = CmsUtils.getMainNavList(siteId);
		Map<String, List<Category>> nav = new HashMap<String, List<Category>>();
		nav.put("categoryList", categoryList);
		return nav;
	}

	/**
	 * 网站首页
	 */
	@RequestMapping(value = "jsindex")
	public String jsindex(Model model, HttpServletRequest request, HttpServletResponse response) {
		return index(model, request, response);
	}
	
	/**
	 * hot首页
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "hotindex")
	public String hotindex(Model model, HttpServletRequest request, HttpServletResponse response) {
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		model.addAttribute("site", site);
		if(UserAgentUtils.isMobile(request)) {
			// hot头条焦点图
			Map<String, List<Article>> jdtMap = getPosArticleList(null, "38", 3, null, model, null, null, null);
			model.addAttribute("jdtMap", jdtMap.get("articleList"));
			
			// hot咨询推荐
			Map<String, List<Article>> dtMap = getPosArticleList(null, "39", 120, null, model, null, null, null);
			model.addAttribute("dtMap", dtMap.get("articleList"));
		}
		return "thymeleaf/cms/front/themes/" + site.getTheme() + "/frontIndex_hot";
	}

	/**
	 * 网站首页
	 */
	@RequestMapping(value = "")
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
		log.info("domain:" + request.getRequestURL());
		if (autodomain) {
			String rdomain = request.getRequestURL().toString();
			if (UserAgentUtils.isComputer(request)) {
				if (rdomain.indexOf("www") == -1) {
					response.setStatus(301);
					try {
						response.sendRedirect("http://www." + domain);
						return null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				if (rdomain.indexOf("www") == -1) {
					response.setStatus(301);
					try {
						response.sendRedirect("http://m." + domain);
						return null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		model.addAttribute("site", site);
		model.addAttribute("isIndex", true);
		log.info("thymeleaf/cms/front/themes/" + site.getTheme() + "/frontIndex");
		
		///
		Map<String, List<Category>> categoryMap = getMainNavList("1", model, null, null);
		model.addAttribute("categoryList", categoryMap.get("categoryList"));
		
		if(UserAgentUtils.isComputer(request)) { // PC端
			// 焦点图
			Map<String, List<Article>> jdMap = getPosArticleList(null, "16", 3, null, model, null, null, null);
			model.addAttribute("jdList", jdMap.get("articleList"));
	
			// 首页头条
			Map<String, List<Article>> tdMap = getPosArticleList(null, "17", 2, null, model, null, null, null);
			model.addAttribute("tdList", tdMap.get("articleList"));
	
			// //环球热点
			Map<String, List<Article>> hqMap = getPosArticleList(null, "18", 8, null, model, null, null, null);
			model.addAttribute("hqList", hqMap.get("articleList"));
	
			// 中国军情
			Map<String, List<Article>> jqMap = getPosArticleList(null, "19", 8, null, model, null, null, null);
			model.addAttribute("jqList", jqMap.get("articleList"));
	
			// 海外观察
			Map<String, List<Article>> hwMap = getPosArticleList(null, "20", 8, null, model, null, null, null);
			model.addAttribute("hwList", hwMap.get("articleList"));
	
			// 军情秘闻
			Map<String, List<Article>> mwMap = getPosArticleList(null, "21", 13, null, model, null, null, null);
			model.addAttribute("mwList", mwMap.get("articleList"));
	
			// 最新热图
			Map<String, List<Article>> rtMap = getPosArticleList(null, "22", 4, null, model, null, null, null);
			model.addAttribute("rtList", rtMap.get("articleList"));
	
			// 时事评论
			Map<String, List<Article>> plMap = getPosArticleList(null, "23", 13, null, model, null, null, null);
			model.addAttribute("plList", plMap.get("articleList"));
	
			// 图说
			Map<String, List<Article>> tsMap = getPosArticleList(null, "24", 7, null, model, null, null, null);
			model.addAttribute("tsList", tsMap.get("articleList"));
	
			// //环球热点(图)
			Map<String, List<Article>> hqtMap = getPosArticleList(null, "32", 1, null, model, null, null, null);
			model.addAttribute("hqtList", hqtMap.get("articleList"));
	
			// 中国军情(图)
			Map<String, List<Article>> jqtMap = getPosArticleList(null, "33", 1, null, model, null, null, null);
			model.addAttribute("jqtList", jqtMap.get("articleList"));
	
			// 海外观察(图)
			Map<String, List<Article>> hwtMap = getPosArticleList(null, "34", 1, null, model, null, null, null);
			model.addAttribute("hwtList", hwtMap.get("articleList"));
	
			// 军情秘闻(图)
			Map<String, List<Article>> mwtMap = getPosArticleList(null, "35", 1, null, model, null, null, null);
			model.addAttribute("mwtList", mwtMap.get("articleList"));
	
			// 时事评论(图)
			Map<String, List<Article>> pltMap = getPosArticleList(null, "36", 2, null, model, null, null, null);
			model.addAttribute("pltList", pltMap.get("articleList"));
		}else if(UserAgentUtils.isMobileOrTablet(request)) { // wap端
			// 头条焦点图
			Map<String, List<Article>> jdtMap = getPosArticleList(null, "0", 3, null, model, null, null, null);
			model.addAttribute("jdtMap", jdtMap.get("articleList"));
			
			// 单图推荐位
			Map<String, List<Article>> dtMap = getPosArticleList(null, null, 120, null, model, null, null, null);
			model.addAttribute("dtMap", dtMap.get("articleList"));
		}
		return "thymeleaf/cms/front/themes/" + site.getTheme() + "/frontIndex";
	}

	/**
	 * 网站首页
	 */
	@RequestMapping(value = "index-{siteId}${urlSuffix}")
	public String index(@PathVariable String siteId, Model model) {
		if (siteId.equals("1")) {
			return "redirect:" + Global.getFrontPath();
		}
		Site site = CmsUtils.getSite(siteId);
		// 子站有独立页面，则显示独立页面
		if (StringUtils.isNotBlank(site.getCustomIndexView())) {
			model.addAttribute("site", site);
			model.addAttribute("isIndex", true);
			return "thymeleaf/cms/front/themes/" + site.getTheme() + "/frontIndex" + site.getCustomIndexView();
		}
		// 否则显示子站第一个栏目
		List<Category> mainNavList = CmsUtils.getMainNavList(siteId);
		if (mainNavList.size() > 0) {
			String firstCategoryId = CmsUtils.getMainNavList(siteId).get(0).getId();
			return "redirect:" + Global.getFrontPath() + "/list-" + firstCategoryId + Global.getUrlSuffix();
		} else {
			model.addAttribute("site", site);
			return "thymeleaf/cms/front/themes/" + site.getTheme() + "/frontListCategory";
		}
	}

	@RequestMapping(value = { "/{categoryname:[a-zA-Z]*}/index${urlSuffix}", "/{categoryname:[a-zA-Z]*}/",
			"/{categoryname:[a-zA-Z]*}/list${urlSuffix}", "/{categoryname:[a-zA-Z]*}" })
	public String list4(@PathVariable String categoryname, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("字母");
		String url = request.getServletPath();
		log.info("url:" + url);
		if (!url.endsWith("/")) {
			response.setStatus(301);
			try {
				response.sendRedirect(url + "/");
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		List<Category> categories = categoryService.findAllList();
		if (categories != null && categories.size() > 0) {
			String categoryId = "";
			for (Category category : categories) {
				if (categoryname.equals(category.getNameEn())) {
					categoryId = category.getId();
				}
			}
			if (categoryId == null || categoryId == "") {
				Site site = CmsUtils.getSite(Site.defaultSiteId());
				model.addAttribute("site", site);
				return "error/404";
			}
			Site site = CmsUtils.getSite(Site.defaultSiteId());
			model.addAttribute("site", site);
			model.addAttribute("isIndex", true);
			return list(categoryId, 1, 15, model, request);
		}
		return "error/404";
	}

	@RequestMapping(value = "/{categoryname:[a-zA-Z]*}/list{pageNo}${urlSuffix}")
	public String list3(@PathVariable String categoryname, @PathVariable Integer pageNo,
			@RequestParam(required = false, defaultValue = "15") Integer pageSize, Model model, HttpServletRequest request) {
		logger.info("字母");
		List<Category> categories = categoryService.findAllList();
		if (categories != null && categories.size() > 0) {
			String categoryId = "";
			for (Category category : categories) {
				if (categoryname.equals(category.getNameEn())) {
					categoryId = category.getId();
				}
			}
			if (categoryId == null || categoryId == "") {
				Site site = CmsUtils.getSite(Site.defaultSiteId());
				model.addAttribute("site", site);
				return "error/404";
			}
			return list(categoryId, pageNo, pageSize, model, request);
		}
		return "error/404";
	}

	@RequestMapping(value = "/{categoryId:[\\d]*}/list${urlSuffix}")
	public String list2(@PathVariable String categoryId,
			@RequestParam(required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(required = false, defaultValue = "15") Integer pageSize, Model model, HttpServletRequest request) {
		return list(categoryId, pageNo, pageSize, model, request);
	}

	/**
	 * 内容列表
	 */
	@RequestMapping(value = "list-{categoryId}${urlSuffix}")
	public String list(@PathVariable String categoryId,
			@RequestParam(required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(required = false, defaultValue = "15") Integer pageSize, Model model, HttpServletRequest request) {
		Category category = categoryService.get(categoryId);
		if (category == null) {
			Site site = CmsUtils.getSite(Site.defaultSiteId());
			model.addAttribute("site", site);
			return "error/404";
		}
		Site site = siteService.get(category.getSite().getId());
		model.addAttribute("site", site);
		/* model.addAttribute("urlSuffix", Global.getUrlSuffix()); */
		// 2：简介类栏目，栏目第一条内容
		if ("2".equals(category.getShowModes()) && "article".equals(category.getModule())) {
			// 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
			List<Category> categoryList = Lists.newArrayList();
			if (category.getParent().getId().equals("1")) {
				categoryList.add(category);
			} else {
				categoryList = categoryService.findByParentId(category.getParent().getId(), category.getSite().getId());
			}
			model.addAttribute("category", category);
			model.addAttribute("categoryList", categoryList);
			// 获取文章内容
			Page<Article> page = new Page<Article>(1, 1, -1);
			Article article = new Article(category);
			article.setIsshowview("show");

			page = articleService.findPage(page, article, false);
			if (page.getList().size() > 0) {
				article = page.getList().get(0);
				article.setArticleData(articleDataService.get(article.getId()));
				articleService.updateHitsAddOne(article.getId());
			}
			model.addAttribute("article", article);
			CmsUtils.addViewConfigAttribute(model, category);
			CmsUtils.addViewConfigAttribute(model, article.getViewConfig());
			return "thymeleaf/cms/front/themes/" + site.getTheme() + "/" + getTpl(article);
		} else {
			List<Category> categoryList = categoryService.findByParentId(category.getId(), category.getSite().getId());
			// 展现方式为1 、无子栏目或公共模型，显示栏目内容列表
			if ("1".equals(category.getShowModes()) || categoryList.size() == 0) {
				// 有子栏目并展现方式为1，则获取第一个子栏目；无子栏目，则获取同级分类列表。
				if (categoryList.size() > 0) {
					category = categoryList.get(0);
				} else {
					// 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
					if (category.getParent().getId().equals("1")) {
						categoryList.add(category);
					} else {
						categoryList = categoryService.findByParentId(category.getParent().getId(),
								category.getSite().getId());
					}
				}
				model.addAttribute("category", category);
				model.addAttribute("categoryList", categoryList);
				// 获取内容列表
				if ("article".equals(category.getModule())) {
					Page<Article> page = new Page<Article>(pageNo, pageSize);
					// System.out.println(page.getPageNo());
					Article article1 = new Article(category);
					article1.setIsshowview("show");
					page = articleService.findPage(page, article1, false);
					page.setList(CmsUtils.getFirstImageList(page.getList()));
					List<Article> articleList = page.getList();
					String description = "";
					String image = "";
					for (Article article2 : articleList) {
						// 读取第一段文字作为描述
						if (StringUtils.isBlank(article2.getDescription())) {
							description = CmsUtils.getFirstPara(article2);
							if (!StringUtils.isBlank(description)) {
								article2.setDescription(description);
							}
						}
						// 读取文章内容的第一个图片作为缩略图
						if (StringUtils.isBlank(article2.getImage())) {
							image = CmsUtils.getFirstContentImg(article2.getArticleData().getContent());
							if (!StringUtils.isBlank(image)) {
								article2.setImage(image);
							}
						}
						if(article2.getMsgsource() == null) {
							article2.setMsgsource(new Msgsource());
						}
					}
					page.setList(articleList);
					model.addAttribute("page", page);
					// 如果第一个子栏目为简介类栏目，则获取该栏目第一篇文章
					if ("2".equals(category.getShowModes())) {
						Article article = new Article(category);
						if (page.getList().size() > 0) {
							article = page.getList().get(0);
							article.setArticleData(articleDataService.get(article.getId()));
							articleService.updateHitsAddOne(article.getId());
						}
						model.addAttribute("article", article);
						CmsUtils.addViewConfigAttribute(model, category);
						CmsUtils.addViewConfigAttribute(model, article.getViewConfig());
						return "thymeleaf/cms/front/themes/" + site.getTheme() + "/" + getTpl(article);
					}
				} else if ("link".equals(category.getModule())) {
					Page<Link> page = new Page<Link>(1, -1);
					page = linkService.findPage(page, new Link(category), false);
					model.addAttribute("page", page);
				}
				String view = "/frontList";
				if (StringUtils.isNotBlank(category.getCustomListView())) {
					view = "/" + category.getCustomListView();
				}
				CmsUtils.addViewConfigAttribute(model, category);
				site = siteService.get(category.getSite().getId());
				// System.out.println("else 栏目第一条内容 _2
				// :"+category.getSite().getTheme()+","+site.getTheme());
				
				if(UserAgentUtils.isComputer(request)) {
					// 导航栏
					Map<String, List<Category>> categoryMap = getMainNavList("1", model, null, null);
					model.addAttribute("categoryList", categoryMap.get("categoryList"));
	
					// 头条新闻
					Map<String, List<Article>> tdMap = getPosArticleList(null, "27", 4, null, model, null, null, null);
					model.addAttribute("tdList", tdMap.get("articleList"));
					/// //周榜热点
					Map<String, List<Article>> zbMap = getPosArticleList(null, "28", 8, null, model, null, null, null);
					model.addAttribute("zbList", zbMap.get("articleList"));
	
					// 周榜推荐
					Map<String, List<Article>> zbtMap = getPosArticleList(null, "28", 8, null, model, null, null, null);
					model.addAttribute("zbtList", zbtMap.get("articleList"));
	
					// 热门搜索
					Map<String, List<Article>> rmMap = getPosArticleList(null, "30", 8, null, model, null, null, null);
					model.addAttribute("rmList", rmMap.get("articleList"));
	
					// 图文
					Map<String, List<Article>> twMap = getPosArticleList(null, "31", 8, null, model, null, null, null);
					model.addAttribute("twList", twMap.get("articleList"));
				}
				return "thymeleaf/cms/front/themes/" + siteService.get(category.getSite().getId()).getTheme() + view;
				// return
				// "thymeleaf/cms/front/themes/"+category.getSite().getTheme()+view;
			}
			// 有子栏目：显示子栏目列表
			else {
				model.addAttribute("category", category);
				model.addAttribute("categoryList", categoryList);
				String view = "/frontListCategory";
				if (StringUtils.isNotBlank(category.getCustomListView())) {
					view = "/" + category.getCustomListView();
				}
				CmsUtils.addViewConfigAttribute(model, category);
				return "thymeleaf/cms/front/themes/" + site.getTheme() + view;
			}
		}
	}

	/**
	 * 内容列表（通过url自定义视图）
	 */
	@RequestMapping(value = "listc-{categoryId}-{customView}${urlSuffix}")
	public String listCustom(@PathVariable String categoryId, @PathVariable String customView,
			@RequestParam(required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(required = false, defaultValue = "8") Integer pageSize, Model model) {
		Category category = categoryService.get(categoryId);
		if (category == null) {
			Site site = CmsUtils.getSite(Site.defaultSiteId());
			model.addAttribute("site", site);
			return "error/404";
		}
		Site site = siteService.get(category.getSite().getId());
		model.addAttribute("site", site);
		List<Category> categoryList = categoryService.findByParentId(category.getId(), category.getSite().getId());
		model.addAttribute("category", category);
		model.addAttribute("categoryList", categoryList);
		CmsUtils.addViewConfigAttribute(model, category);
		return "thymeleaf/cms/front/themes/" + site.getTheme() + "/frontListCategory" + customView;
	}

	@RequestMapping(value = "/{categoryname:[a-zA-Z]*}/{contentId}${urlSuffix}")
	public String view3(@PathVariable String categoryname, @PathVariable String contentId, Model model, HttpServletRequest request) {
		logger.info("字母");
		List<Category> categories = categoryService.findAllList();
		if (categories != null && categories.size() > 0) {
			String categoryId = "";
			for (Category category : categories) {
				if (categoryname.equals(category.getNameEn())) {
					categoryId = category.getId();
				}
			}

			// logger.info("categoryId=" + categoryId + " contentId=" +
			// contentId);
			if (categoryId == null || categoryId == "") {
				Site site = CmsUtils.getSite(Site.defaultSiteId());
				model.addAttribute("site", site);
				return "error/404";
			}
			Integer pageIndex = 1;
			String strs[] = contentId.split("_");
			if (strs.length == 2) {
				contentId = strs[0];
				pageIndex = Integer.parseInt(strs[1]);
			}
			// log.info("pageIndex=" + pageIndex);
			return view(categoryId, contentId, pageIndex, model, request);
		}
		return "error/404";
	}

	@RequestMapping(value = "/{categoryId:[\\d]*}/{contentId}${urlSuffix}")
	public String view2(@PathVariable String categoryId, @PathVariable String contentId, Model model, HttpServletRequest request) {
		logger.info("数字");
		Integer pageIndex = 1;
		String strs[] = contentId.split("[_]");
		if (strs.length == 2) {
			contentId = strs[0];
			pageIndex = Integer.parseInt(strs[1]);
		}
		log.info("pageIndex=" + pageIndex);
		return view(categoryId, contentId, pageIndex, model, request);
	}

	/**
	 * 显示内容
	 */
	@RequestMapping(value = "view-{categoryId}-{contentId}-{pageIndex}${urlSuffix}")
	public String view(@PathVariable String categoryId, @PathVariable String contentId, @PathVariable Integer pageIndex,
			Model model, HttpServletRequest request) {
		Category category = categoryService.get(categoryId);
		if (category == null) {
			Site site = CmsUtils.getSite(Site.defaultSiteId());
			model.addAttribute("site", site);
			return "error/404";
		}
		model.addAttribute("site", category.getSite());
		if ("article".equals(category.getModule())) {
			// 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
			List<Category> categoryList = Lists.newArrayList();
			if (category.getParent().getId().equals("1")) {
				categoryList.add(category);
			} else {
				categoryList = categoryService.findByParentId(category.getParent().getId(), category.getSite().getId());
			}
			// 获取文章内容
			Article article = articleService.get(contentId);
			if (article == null
					|| !(Article.DEL_FLAG_NORMAL.equals(article.getDelFlag()) || "3".equals(article.getDelFlag()))) {
				return "error/404";
			}
			// 文章阅读次数+1
			articleService.updateHitsAddOne(contentId);
			// 获取推荐文章列表
			ArticleData articleData = articleDataService.get(article.getId());
			// log.info("articleData:" + articleData + " relation:"
			// + articleData.getRelation());
			List<Object[]> relationList = articleService.findByIds(articleData.getRelation());
			// 将数据传递到视图
			model.addAttribute("category", categoryService.get(article.getCategory().getId()));
			model.addAttribute("categoryList", categoryList);

			ArticlePageModel articlePageModel = CmsUtils
					.getArticlePageModel(Encodes.unescapeHtml(articleData.getContent()), pageIndex);
			if (pageIndex == null) {
				log.info("pageIndex==null 设置为1");
				pageIndex = 1;
			}
			String content = CmsUtils.SplitLineForArticle(articlePageModel, pageIndex);
			articleData.setContent(content);
			article.setArticleData(articleData);
			model.addAttribute("page", articlePageModel);
			model.addAttribute("article", article);
			model.addAttribute("relationList", relationList);
			CmsUtils.addViewConfigAttribute(model, article.getCategory());
			CmsUtils.addViewConfigAttribute(model, article.getViewConfig());
			Site site = siteService.get(category.getSite().getId());
			model.addAttribute("site", site);
			// return
			// "thymeleaf/cms/front/themes/"+category.getSite().getTheme()+"/"+getTpl(article);
			if(getTpl(article).equals("frontViewArticle")) {
				if(UserAgentUtils.isComputer(request)) {
					// 推荐其他位置
					// 导航栏
					Map<String, List<Category>> categoryMap = getMainNavList("1", model, null, null);
					model.addAttribute("categoryList", categoryMap.get("categoryList"));
					// 精彩图片
					Map<String, List<Article>> jcMap = getPosArticleList(null, "25", 10, null, model, null, null, null);
					model.addAttribute("jcList", jcMap.get("articleList"));
					
					/*// 精彩图片（外链）
					Map<String, List<Article>> jcwlMap = getPosArticleList(null, "37", 3, null, model, null, null, null);
					model.addAttribute("jcwlMap", jcwlMap.get("articleList"));*/
		
					// 编辑推荐
					Map<String, List<Article>> bjMap = getPosArticleList(null, "26", 6, null, model, null, null, null);
					model.addAttribute("bjList", bjMap.get("articleList"));
		
					// 头条新闻
					Map<String, List<Article>> tdMap = getPosArticleList(null, "27", 4, null, model, null, null, null);
					model.addAttribute("tdList", tdMap.get("articleList"));
					/// //周榜热点
					Map<String, List<Article>> zbMap = getPosArticleList(null, "28", 8, null, model, null, null, null);
					model.addAttribute("zbList", zbMap.get("articleList"));
		
					// 周榜推荐
					Map<String, List<Article>> zbtMap = getPosArticleList(null, "28", 8, null, model, null, null, null);
					model.addAttribute("zbtList", zbtMap.get("articleList"));
		
					// 热门搜索
					Map<String, List<Article>> rmMap = getPosArticleList(null, "30", 8, null, model, null, null, null);
					model.addAttribute("rmList", rmMap.get("articleList"));
		
					// 图文
					Map<String, List<Article>> twMap = getPosArticleList(null, "31", 8, null, model, null, null, null);
					model.addAttribute("twList", twMap.get("articleList"));
				}else if(UserAgentUtils.isMobileOrTablet(request)) {
					// 推荐阅读
					Map<String, List<Article>> tjMap1 = getPosArticleList(null, "5", 2, null, model, null, null, null);
					model.addAttribute("tjMap1", tjMap1.get("articleList"));
					
					Map<String, List<Article>> tjMap2 = getPosArticleList(null, "5", 2, null, model, 2, null, null);
					model.addAttribute("tjMap2", tjMap2.get("articleList"));
					
					// 内页多图
					model.addAttribute("duoMap", CmsUtils.getMoreImgPositionList(null, "6", 1, null));
					
					// 内页精彩图片
					Map<String, List<Article>> jctMap = getPosArticleList(null, "4", 4, null, model, null, null, null);
					model.addAttribute("jctMap", jctMap.get("articleList"));
				}
			}else if(getTpl(article).equals("frontViewArticle_hot")) {
				if(UserAgentUtils.isMobileOrTablet(request)) { // hot 手机端
					// hot推荐阅读
					Map<String, List<Article>> tjMap1 = getPosArticleList(null, "40", 8, null, model, null, null, null);
					model.addAttribute("tjMap1", tjMap1.get("articleList"));
					// hot内页精彩图片
					Map<String, List<Article>> jctMap = getPosArticleList(null, "41", 4, null, model, null, null, null);
					model.addAttribute("jctMap", jctMap.get("articleList"));
				}
			}
			return "thymeleaf/cms/front/themes/" + site.getTheme() + "/" + getTpl(article);
		}
		return "error/404";
	}

	@RequestMapping(value = "/preview/{categoryname:[a-zA-Z]*}/{contentId}${urlSuffix}")
	public String previewTemp2(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String categoryname, @PathVariable String contentId, Model model) {
		logger.info("字母");
		if (StringUtils.isNotBlank(hostname)) {
			String rdomain = request.getRequestURL().toString();
			if (rdomain.indexOf(domain) > -1) {
				try {
					response.sendRedirect("http://" + hostname + "/preview/" + categoryname + "/" + contentId
							+ Global.getUrlSuffix());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}

		List<Category> categories = categoryService.findAllList();
		if (categories != null && categories.size() > 0) {
			String categoryId = "";
			for (Category category : categories) {
				if (categoryname.equals(category.getNameEn())) {
					categoryId = category.getId();
				}
			}

			// logger.info("categoryId=" + categoryId + " contentId=" +
			// contentId);
			if (categoryId == null || categoryId == "") {
				Site site = CmsUtils.getSite(Site.defaultSiteId());
				model.addAttribute("site", site);
				return "error/404";
			}
			Integer pageIndex = 1;
			String strs[] = contentId.split("_");
			if (strs.length == 2) {
				contentId = strs[0];
				pageIndex = Integer.parseInt(strs[1]);
			}
			// log.info("pageIndex=" + pageIndex);
			return previewTemp(categoryId, contentId, pageIndex, model);
		}
		return "error/404";
	}

	@RequestMapping(value = "preview-{categoryId}-{contentId}-{pageIndex}${urlSuffix}")
	public String previewTemp(@PathVariable String categoryId, @PathVariable String contentId,
			@PathVariable Integer pageIndex, Model model) {

		Category category = categoryService.get(categoryId);
		if (category == null) {
			Site site = CmsUtils.getSite(Site.defaultSiteId());
			model.addAttribute("site", site);
			return "error/404";
		}
		model.addAttribute("site", category.getSite());
		if ("article".equals(category.getModule())) {
			// 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
			List<Category> categoryList = Lists.newArrayList();
			if (category.getParent().getId().equals("1")) {
				categoryList.add(category);
			} else {
				categoryList = categoryService.findByParentId(category.getParent().getId(), category.getSite().getId());
			}
			// 获取文章内容
			ArticleTemp article = articleTempService.get(contentId);
			if (article == null
					|| !(Article.DEL_FLAG_NORMAL.equals(article.getDelFlag()) || "3".equals(article.getDelFlag()))) {
				return "error/404";
			}
			// 文章阅读次数+1
			articleService.updateHitsAddOne(contentId);
			// 获取推荐文章列表
			ArticleDataTemp articleData = articleDataTempService.get(article.getId());
			// log.info("articleData:" + articleData + " relation:"
			// + articleData.getRelation());
			List<Object[]> relationList = articleService.findByIds(articleData.getRelation());
			// 将数据传递到视图
			model.addAttribute("category", categoryService.get(article.getCategory().getId()));
			model.addAttribute("categoryList", categoryList);

			ArticlePageModel articlePageModel = CmsUtils
					.getArticlePageModel(Encodes.unescapeHtml(articleData.getContent()), pageIndex);
			if (pageIndex == null) {
				log.info("pageIndex==null 设置为1");
				pageIndex = 1;
			}
			String content = CmsUtils.SplitLineForArticle(articlePageModel, pageIndex);
			articleData.setContent(content);
			article.setArticleData(articleData);
			model.addAttribute("page", articlePageModel);
			model.addAttribute("article", article);
			model.addAttribute("relationList", relationList);
			CmsUtils.addViewConfigAttribute(model, article.getCategory());
			CmsUtils.addViewConfigAttribute(model, article.getViewConfig());
			Site site = siteService.get(category.getSite().getId());
			model.addAttribute("site", site);
			// return
			// "thymeleaf/cms/front/themes/"+category.getSite().getTheme()+"/"+getTpl(article);
			// 推荐其他位置
			// 导航栏
			Map<String, List<Category>> categoryMap = getMainNavList("1", model, null, null);
			model.addAttribute("categoryList", categoryMap.get("categoryList"));
			// 精彩图片
			Map<String, List<Article>> jcMap = getPosArticleList(null, "25", 10, null, model, null, null, null);
			model.addAttribute("jcList", jcMap.get("articleList"));

			// 编辑推荐
			Map<String, List<Article>> bjMap = getPosArticleList(null, "26", 6, null, model, null, null, null);
			model.addAttribute("bjList", bjMap.get("articleList"));

			// 头条新闻
			Map<String, List<Article>> tdMap = getPosArticleList(null, "27", 4, null, model, null, null, null);
			model.addAttribute("tdList", tdMap.get("articleList"));
			/// //周榜热点
			Map<String, List<Article>> zbMap = getPosArticleList(null, "28", 8, null, model, null, null, null);
			model.addAttribute("zbList", zbMap.get("articleList"));

			// 周榜推荐
			Map<String, List<Article>> zbtMap = getPosArticleList(null, "28", 8, null, model, null, null, null);
			model.addAttribute("zbtList", zbtMap.get("articleList"));

			// 热门搜索
			Map<String, List<Article>> rmMap = getPosArticleList(null, "30", 8, null, model, null, null, null);
			model.addAttribute("rmList", rmMap.get("articleList"));

			// 图文
			Map<String, List<Article>> twMap = getPosArticleList(null, "31", 8, null, model, null, null, null);
			model.addAttribute("twList", twMap.get("articleList"));

			return "thymeleaf/cms/front/themes/" + site.getTheme() + "/" + getTpl(article);
		}
		return "error/404";
	}

	// @RequestMapping(value =
	// "preview-{categoryname:[a-zA-Z]*}-{contentId}${urlSuffix}")
	// public String viewTemp(@PathVariable String categoryname,
	// @PathVariable Integer pageIndex, @PathVariable String contentId,
	// Model model) {
	// logger.info("预览审核文章");
	// String categoryId = "";
	// List<Category> categories = categoryService.findAllList();
	// if (categories != null && categories.size() > 0) {
	// categoryId = "";
	// for (Category category : categories) {
	// if (categoryname.equals(category.getNameEn())) {
	// categoryId = category.getId();
	// }
	// }
	//
	// logger.info("categoryId=" + categoryId + " contentId=" + contentId);
	// if (categoryId == null || categoryId == "") {
	// Site site = CmsUtils.getSite(Site.defaultSiteId());
	// model.addAttribute("site", site);
	// return "error/404";
	// }
	// } else {
	// return "error/404";
	// }
	//
	// Category category = categoryService.get(categoryId);
	// if (category == null) {
	// Site site = CmsUtils.getSite(Site.defaultSiteId());
	// model.addAttribute("site", site);
	// return "error/404";
	// }
	// model.addAttribute("site", category.getSite());
	// if ("article".equals(category.getModule())) {
	// // 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
	// List<Category> categoryList = Lists.newArrayList();
	// if (category.getParent().getId().equals("1")) {
	// categoryList.add(category);
	// } else {
	// categoryList = categoryService.findByParentId(category
	// .getParent().getId(), category.getSite().getId());
	// }
	// // 获取文章内容
	// ArticleTemp article = articleTempService.get(contentId);
	// if (article == null) {
	// return "error/404";
	// }
	// // 文章阅读次数+1
	// // articleService.updateHitsAddOne(contentId);
	// // 获取推荐文章列表
	// List<Object[]> relationList = articleService
	// .findByIds(articleDataService.get(article.getId())
	// .getRelation());
	// // 将数据传递到视图
	// model.addAttribute("category",
	// categoryService.get(article.getCategory().getId()));
	// model.addAttribute("categoryList", categoryList);
	// ArticleDataTemp articleData = articleDataTempService.get(article
	// .getId());
	//
	// ArticlePageModel articlePageModel = CmsUtils.getArticlePageModel(
	// Encodes.unescapeHtml(articleData.getContent()), pageIndex);
	// if (pageIndex == null) {
	// log.info("pageIndex==null 设置为1");
	// pageIndex = 1;
	// }
	// String content = CmsUtils.SplitLineForArticle(articlePageModel,
	// pageIndex);
	// articleData.setContent(content);
	// article.setArticleDataTemp(articleData);
	// model.addAttribute("page", articlePageModel);
	// model.addAttribute("article", article);
	// model.addAttribute("relationList", relationList);
	// CmsUtils.addViewConfigAttribute(model, article.getCategory());
	// CmsUtils.addViewConfigAttribute(model, article.getViewConfig());
	// Site site = siteService.get(category.getSite().getId());
	// model.addAttribute("site", site);
	// logger.info("准备跳转页面,路径为:" + "thymeleaf/cms/front/themes/"
	// + site.getTheme() + "/" + getTpl(article));
	// // return
	// //
	// "thymeleaf/cms/front/themes/"+category.getSite().getTheme()+"/"+getTpl(article);
	// return "thymeleaf/cms/front/themes/" + site.getTheme() + "/"
	// + getTpl(article);
	// }
	// return "error/404";
	// }

	/**
	 * 内容评论
	 */
	@RequestMapping(value = "comment", method = RequestMethod.GET)
	public String comment(String theme, Comment comment, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<Comment> page = new Page<Comment>(request, response);
		Comment c = new Comment();
		c.setCategory(comment.getCategory());
		c.setContentId(comment.getContentId());
		c.setDelFlag(Comment.DEL_FLAG_NORMAL);
		page = commentService.findPage(page, c);
		model.addAttribute("page", page);
		model.addAttribute("comment", comment);
		return "thymeleaf/cms/front/themes/" + theme + "/frontComment";
	}

	/**
	 * 内容评论保存
	 */
	@ResponseBody
	@RequestMapping(value = "comment", method = RequestMethod.POST)
	public String commentSave(Comment comment, String validateCode, @RequestParam(required = false) String replyId,
			HttpServletRequest request) {
		if (StringUtils.isNotBlank(validateCode)) {
			if (ValidateCodeServlet.validate(request, validateCode)) {
				if (StringUtils.isNotBlank(replyId)) {
					Comment replyComment = commentService.get(replyId);
					if (replyComment != null) {
						comment.setContent("<div class=\"reply\">" + replyComment.getName() + ":<br/>"
								+ replyComment.getContent() + "</div>" + comment.getContent());
					}
				}
				comment.setIp(request.getRemoteAddr());
				comment.setCreateDate(new Date());
				comment.setDelFlag(Comment.DEL_FLAG_AUDIT);
				commentService.save(comment);
				return "{result:1, message:'提交成功。'}";
			} else {
				return "{result:2, message:'验证码不正确。'}";
			}
		} else {
			return "{result:2, message:'验证码不能为空。'}";
		}
	}

	/**
	 * 站点地图
	 */
	@RequestMapping(value = "map-{siteId}${urlSuffix}")
	public String map(@PathVariable String siteId, Model model) {
		Site site = CmsUtils.getSite(siteId != null ? siteId : Site.defaultSiteId());
		model.addAttribute("site", site);
		return "thymeleaf/cms/front/themes/" + site.getTheme() + "/frontMap";
	}

	@RequestMapping(value = "/getJSONPosArticleList")
	@ResponseBody
	public Map<String, Object> getJSONPosArticleList(@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "type2", required = false) String type2,
			@RequestParam(value = "index", required = false) Integer index,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "size2", required = false) Integer size2,
			@RequestParam(value = "officSize", required = false) Integer officSize) {
		if (officSize == null) {
			officSize = 0;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNoneBlank(type) && index != null && index.SIZE > 0 && size != null && size.SIZE > 0) {
			if (size == 1) {
				officSize = 0;
			}
			map.put("data1", CmsUtils.getPositionList(null, type, index, size, null, officSize));
		}
		if (StringUtils.isNoneBlank(type2) && index != null && index.SIZE > 0 && size2 != null && size2.SIZE > 0) {
			if (size2 == 1) {
				officSize = 0;
			}
			map.put("data2", CmsUtils.getMoreImgPositionList(null, type2, index, size2, null, officSize));
		}
		return map;
	}

	@RequestMapping(value = "/getJSONArticleList")
	@ResponseBody
	public Page<Article> getJSONArticleList(@RequestParam(value = "categoryId", required = false) String categoryId,
			@RequestParam(value = "pageNo", required = false) Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize) {
		Category category = categoryService.get(categoryId);
		Page<Article> page = new Page<Article>(pageNo, pageSize);
		Article article1 = new Article(category);
		article1.setIsshowview("show");
		page = articleService.findPage(page, article1, false);
		List<Article> articles = CmsUtils.getFirstImageList(page.getList());
		page.setList(articles);
		return page;
	}

	private String getTpl(ArticleEntity article) {
		if (StringUtils.isBlank(article.getCustomContentView())) {
			String view = null;
			Category c = article.getCategory();
			boolean goon = true;
			do {
				if (StringUtils.isNotBlank(c.getCustomContentView())) {
					view = c.getCustomContentView();
					goon = false;
				} else if (c.getParent() == null || c.getParent().isRoot()) {
					goon = false;
				} else {
					c = c.getParent();
				}
			} while (goon);
			return StringUtils.isBlank(view) ? Article.DEFAULT_TEMPLATE : view;
		} else {
			return article.getCustomContentView();
		}
	}

}

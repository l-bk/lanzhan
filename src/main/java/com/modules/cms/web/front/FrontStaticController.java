/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.web.front;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.config.Global;
import com.common.utils.StringUtils;
import com.common.web.BaseController;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.Site;
import com.modules.cms.service.ArticleDataService;
import com.modules.cms.service.ArticleDataTempService;
import com.modules.cms.service.ArticleService;
import com.modules.cms.service.ArticleTempService;
import com.modules.cms.service.CategoryService;
import com.modules.cms.service.CommentService;
import com.modules.cms.service.LinkService;
import com.modules.cms.service.SiteService;
import com.modules.cms.utils.CmsUtils;

/**
 * 网站Controller
 * 
 * @author ThinkGem
 * @version 2013-5-29
 */
@Controller
@RequestMapping(value = "${frontPath}/twj")
public class FrontStaticController extends BaseController {

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
		// List<Article> articleList = CmsUtils.getPositionList(category_id,
		// type, size, param, officSize);
		List<Article> articleList = CmsUtils.getPositionList(category_id, type, 0, size, param, officSize);
		String description = "";
		String title = "";
		if (articleList != null && articleList.size() > 0) {
			for (Article article : articleList) {
				if (!StringUtils.isBlank(article.getLink())) { // 文章具有外链接
					article.setStaticUrl(article.getLink());
				}
				if (StringUtils.isBlank(article.getDescription())) {
					description = CmsUtils.getFirstPara(article);
					if (!StringUtils.isBlank(description)) {
						article.setDescription(description);
					}
				}
				if (StringUtils.isBlank(article.getShorttile())) {
					title = article.getTitle();
					if (!StringUtils.isBlank(title)) {
						article.setShorttile(title);
					}
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
	 * 
	 */
	@RequestMapping(value = "getTjw")
	public String getTjw(@RequestParam(value = "view", required = false) String view, @RequestParam(value = "positionId", required = false) String positionId,
			@RequestParam(value = "size", required = false) String size,Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		
		if("30".equals(positionId))
		{
			
		}
		// 头条新闻
		Map<String, List<Article>> tdMap = getPosArticleList(null, positionId,Integer.valueOf(size) , null, model, null, null, null);
		model.addAttribute("list", tdMap.get("articleList"));
		return "thymeleaf/cms/front/themes/" + site.getTheme() + "/" + view;
	}
	
	/**
	 * 
	 */
	@RequestMapping(value = "getBangTjw")
	public String getBangTjw(@RequestParam(value = "view", required = false) String view,Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		/// //周榜热点
		Map<String, List<Article>> zbMap = getPosArticleList(null, "28", 8, null, model, null, null, null);
		model.addAttribute("zbList", zbMap.get("articleList"));

		// 周榜推荐
		Map<String, List<Article>> zbtMap = getPosArticleList(null, "28", 8, null, model, null, null, null);
		model.addAttribute("zbtList", zbtMap.get("articleList"));
		return "thymeleaf/cms/front/themes/" + site.getTheme() + "/" + view;
	}
	
	/**
	 * 
	 */
	@RequestMapping(value = "getJcTjw")
	public String getJcTjw(@RequestParam(value = "view", required = false) String view,Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		// 精彩图片
		Map<String, List<Article>> jcMap = getPosArticleList(null, "25", 10, null, model, null, null, null);
		model.addAttribute("jcList", jcMap.get("articleList"));
		
		// 精彩图片（外链）
//		Map<String, List<Article>> jcwlMap = getPosArticleList(null, "37", 3, null, model, null, null, null);
//		model.addAttribute("jcwlMap", jcwlMap.get("articleList"));
		return "thymeleaf/cms/front/themes/" + site.getTheme() + "/" + view;
	}
	
	@RequestMapping(value = "getYdTjw")
	public String getYdTjw(@RequestParam(value = "view", required = false) String view,Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		// 推荐阅读
		Map<String, List<Article>> tjMap1 = getPosArticleList(null, "5", 2, null, model, null, null, null);
		model.addAttribute("tjMap1", tjMap1.get("articleList"));
		
		Map<String, List<Article>> tjMap2 = getPosArticleList(null, "5", 2, null, model, 2, null, null);
		model.addAttribute("tjMap2", tjMap2.get("articleList"));
		
		// 内页多图
		model.addAttribute("duoMap", CmsUtils.getMoreImgPositionList(null, "6", 1, null));
		
		return "thymeleaf/cms/front/themes/" + site.getTheme() + "/" + view;
	}

	@RequestMapping(value = "getNyydTjw")
	public String getNyydTjw(@RequestParam(value = "view", required = false) String view,Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		// 内页精彩图片
		Map<String, List<Article>> jctMap = getPosArticleList(null, "4", 4, null, model, null, null, null);
		model.addAttribute("jctMap", jctMap.get("articleList"));
		
		return "thymeleaf/cms/front/themes/" + site.getTheme() + "/" + view;
	}
}

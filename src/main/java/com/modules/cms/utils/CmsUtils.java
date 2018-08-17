/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.common.config.Global;
import com.common.mapper.JsonMapper;
import com.common.persistence.ArticleEntity;
import com.common.persistence.Page;
import com.common.utils.CacheUtils;
import com.common.utils.Encodes;
import com.common.utils.SpringContextHolder;
import com.common.utils.StringUtils;
import com.common.utils.UserAgentUtils;
import com.google.common.collect.Lists;
import com.modules.ads.entity.Ads;
import com.modules.ads.service.AdsService;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.Link;
import com.modules.cms.entity.Site;
import com.modules.cms.service.ArticleService;
import com.modules.cms.service.CategoryService;
import com.modules.cms.service.LinkService;
import com.modules.cms.service.SiteService;
import com.modules.position.dao.PositionDao;
import com.modules.position.entity.Position;

/**
 * 内容管理工具类
 * 
 * @author ThinkGem
 * @version 2013-5-29
 */
public class CmsUtils {

	public static Logger log = Logger.getLogger(CmsUtils.class);

	private static SiteService siteService = SpringContextHolder.getBean(SiteService.class);
	private static CategoryService categoryService = SpringContextHolder.getBean(CategoryService.class);
	private static ArticleService articleService = SpringContextHolder.getBean(ArticleService.class);
	private static LinkService linkService = SpringContextHolder.getBean(LinkService.class);
	private static ServletContext context = SpringContextHolder.getBean(ServletContext.class);

	private static PositionDao positionDao = SpringContextHolder.getBean(PositionDao.class);

	private static AdsService adsService = SpringContextHolder.getBean(AdsService.class);

	private static final String CMS_CACHE = "cmsCache";

	/**
	 * 获得站点列表
	 */
	public static List<Site> getSiteList() {
		@SuppressWarnings("unchecked")
		List<Site> siteList = (List<Site>) CacheUtils.get(CMS_CACHE, "siteList");
		if (siteList == null) {
			Page<Site> page = new Page<Site>(1, -1);
			page = siteService.findPage(page, new Site());
			siteList = page.getList();
			CacheUtils.put(CMS_CACHE, "siteList", siteList);
		}
		return siteList;
	}

	/**
	 * 获得站点信息
	 * 
	 * @param siteId
	 *            站点编号
	 */
	public static Site getSite(String siteId) {
		String id = "1";
		if (StringUtils.isNotBlank(siteId)) {
			id = siteId;
		}
		for (Site site : getSiteList()) {
			if (site.getId().equals(id)) {
				return site;
			}
		}
		return new Site(id);
	}

	/**
	 * 获得主导航列表
	 * 
	 * @param siteId
	 *            站点编号
	 */
	public static List<Category> getMainNavList(String siteId) {
		@SuppressWarnings("unchecked")
		List<Category> mainNavList = (List<Category>) CacheUtils.get(CMS_CACHE, "mainNavList_" + siteId);
		if (mainNavList == null) {
			List<Category> list = getCategoryList(siteId);
			mainNavList = CategoryToTree(list, "1");
			CacheUtils.put(CMS_CACHE, "mainNavList_" + siteId, mainNavList);
		}
		return mainNavList;
	}

	private static List<Category> getCategoryList(String siteId) {
		List<Category> categoryList = (List<Category>) CacheUtils.get(CMS_CACHE, "mainCategoryList_" + siteId);
		if (categoryList == null) {
			Category category = new Category();
			category.setSite(new Site(siteId));
			category.setParent(new Category());
			category.setInMenu(Global.SHOW);
			Page<Category> page = new Page<Category>(1, -1);
			page = categoryService.find(page, category);
			// List<Category> list = page.getList();
			categoryList = page.getList();

			CacheUtils.put(CMS_CACHE, "mainCategoryList_" + siteId, categoryList);
		}
		log.info("categoryList=" + categoryList.size());
		return categoryList;
	}

	private static List<Category> CategoryToTree(List<Category> list, String parentId) {
		List<Category> categories = new ArrayList<Category>();
		log.info("parentId:" + parentId);
		for (Category category : list) {
			if (category.getParentId().trim().equals(parentId)) {
				findChildList(list, category);
				categories.add(category);
			}
		}
		return categories;
	}

	private static void findChildList(List<Category> list, Category category) {
		category.setChildList(new ArrayList<Category>());
		for (Category childategory : list) {
			if (category.getId().trim().equals(childategory.getParentId().trim())) {
				// log.info("find id:" + category.getId() + " parentId:"
				// + childategory.getParentId());
				findChildList(list, childategory);
				if (childategory.getInMenu().equals("1")) {
					category.getChildList().add(childategory);
				}
			}
		}
	}

	private static List<Category> CategoryToChrild(List<Category> list, String parentId) {
		List<Category> categories = new ArrayList<Category>();
		for (Category category : list) {
			// log.info("比较的id为："+category.getId()+"，父id为:"+parentId);
			if (category.getId().trim().equals(parentId)) {
				findChildList(list, category);
				categories.add(category);
			}
		}

		return categories;
	}

	public static List<Category> getChildCatagory(String siteId, String id) {
		log.info("id:" + id);
		List<Category> list = CategoryToChrild(getCategoryList(siteId), id);
		if (list != null && list.size() > 0) {
			// log.info("找到子栏目:"+list.size());
			return list;
		}
		log.info("找不到子栏目");
		return null;
	}

	// 获取子栏目站点，不包括顶级栏目
	public static List<Category> getChildCatagory2(String siteId, String id) {
		log.info("id:" + id);
		List<Category> list = CategoryToChrild(getCategoryList2(siteId), id);
		if (list != null && list.size() > 0) {
			// log.info("找到子栏目:"+list.size());
			return list;
		}
		log.info("找不到子栏目");
		return null;
	}

	private static List<Category> getCategoryList2(String siteId) {
		// List<Category> categoryList = (List<Category>) CacheUtils.get(
		// CMS_CACHE, "mainCategoryList_" + siteId);
		List<Category> categoryList = null;
		if (categoryList == null) {
			Category category = new Category();
			category.setSite(new Site(siteId));
			category.setParent(new Category());
			// category.setInMenu(Global.SHOW);
			Page<Category> page = new Page<Category>(1, -1);
			page = categoryService.find2(page, category);
			// List<Category> list = page.getList();
			categoryList = page.getList();

			CacheUtils.put(CMS_CACHE, "mainCategoryList2_" + siteId, categoryList);
		}
		// log.info("categoryList2="+categoryList.size());
		// for (Category category : categoryList) {
		// log.info("categoryList2_find id:" +
		// category.getId()+"***************" + "categoryList2_parentId:"
		// + category.getParentId());
		// }
		return categoryList;
	}

	/**
	 * 获取栏目
	 * 
	 * @param categoryId
	 *            栏目编号
	 * @return
	 */
	public static Category getCategory(String categoryId) {
		return categoryService.get(categoryId);
	}

	/**
	 * 获得栏目列表
	 * 
	 * @param siteId
	 *            站点编号
	 * @param parentId
	 *            分类父编号
	 * @param number
	 *            获取数目
	 * @param param
	 *            预留参数，例： key1:'value1', key2:'value2' ...
	 */
	public static List<Category> getCategoryList(String siteId, String parentId, int number, String param) {
		Page<Category> page = new Page<Category>(1, number, -1);
		Category category = new Category();
		category.setSite(new Site(siteId));
		category.setParent(new Category(parentId));
		if (StringUtils.isNotBlank(param)) {
			@SuppressWarnings({ "unused", "rawtypes" })
			Map map = JsonMapper.getInstance().fromJson("{" + param + "}", Map.class);
		}
		page = categoryService.find(page, category);
		return page.getList();
	}

	/**
	 * 获取栏目
	 * 
	 * @param categoryIds
	 *            栏目编号
	 * @return
	 */
	public static List<Category> getCategoryListByIds(String categoryIds) {
		return categoryService.findByIds(categoryIds);
	}

	/**
	 * 获取文章
	 * 
	 * @param articleId
	 *            文章编号
	 * @return
	 */
	public static Article getArticle(String articleId) {
		return articleService.get(articleId);
	}

	/**
	 * 获取文章列表
	 * 
	 * @param siteId
	 *            站点编号
	 * @param categoryId
	 *            分类编号
	 * @param number
	 *            获取数目
	 * @param param
	 *            预留参数，例： key1:'value1', key2:'value2' ... posid
	 *            推荐位（1：首页焦点图；2：栏目页文章推荐；） image 文章图片（1：有图片的文章） orderBy 排序字符串
	 * @return ${fnc:getArticleList(category.site.id, category.id, not empty
	 *         pageSize?pageSize:8, 'posid:2, orderBy: \"hits desc\"')}"
	 */
	public static List<Article> getArticleList(String siteId, String categoryId, int number, String param) {
		Page<Article> page = new Page<Article>(1, number, -1);
		Category category = new Category(categoryId, new Site(siteId));
		category.setParentIds(categoryId);
		Article article = new Article(category);
		if (StringUtils.isNotBlank(param)) {
			@SuppressWarnings({ "rawtypes" })
			Map map = JsonMapper.getInstance().fromJson("{" + param + "}", Map.class);
			if (new Integer(1).equals(map.get("posid")) || new Integer(2).equals(map.get("posid"))) {
				article.setPosid(String.valueOf(map.get("posid")));
			}
			if (new Integer(1).equals(map.get("image"))) {
				article.setImage(Global.YES);
			}
			if (StringUtils.isNotBlank((String) map.get("orderBy"))) {
				page.setOrderBy((String) map.get("orderBy"));
			}
			if (StringUtils.isNotBlank((String) map.get("remarks"))) {
				article.setRemarks((String) map.get("remarks"));
			}
		}
		article.setIsshowview("true");
		article.setDelFlag(Article.DEL_FLAG_NORMAL);
		page = articleService.findPage(page, article, false);
		// System.out.println("文章列表长度：" + page.getList().size());
		return getFirstImageList(page.getList());
	}

	/**
	 * 获取链接
	 * 
	 * @param linkId
	 *            文章编号
	 * @return
	 */
	public static Link getLink(String linkId) {
		return linkService.get(linkId);
	}

	/**
	 * 获取链接列表
	 * 
	 * @param siteId
	 *            站点编号
	 * @param categoryId
	 *            分类编号
	 * @param number
	 *            获取数目
	 * @param param
	 *            预留参数，例： key1:'value1', key2:'value2' ...
	 * @return
	 */
	public static List<Link> getLinkList(String siteId, String categoryId, int number, String param) {
		Page<Link> page = new Page<Link>(1, number, -1);
		Link link = new Link(new Category(categoryId, new Site(siteId)));
		if (StringUtils.isNotBlank(param)) {
			@SuppressWarnings({ "unused", "rawtypes" })
			Map map = JsonMapper.getInstance().fromJson("{" + param + "}", Map.class);
		}
		link.setDelFlag(Link.DEL_FLAG_NORMAL);
		page = linkService.findPage(page, link, false);
		return page.getList();
	}

	// ============== Cms Cache ==============

	public static Object getCache(String key) {
		return CacheUtils.get(CMS_CACHE, key);
	}

	public static void putCache(String key, Object value) {
		CacheUtils.put(CMS_CACHE, key, value);
	}

	public static void removeCache(String key) {
		CacheUtils.remove(CMS_CACHE, key);
	}

	/**
	 * 获得文章动态URL地址
	 * 
	 * @param article
	 * @return url
	 */
	public static String getUrlDynamic(ArticleEntity article) {
		if (StringUtils.isNotBlank(article.getLink())) {
			// log.info("空");
			return article.getLink();
		}
		StringBuilder str = new StringBuilder();
		str.append(context.getContextPath()).append(Global.getFrontPath());
		String categoryname = article.getCategory().getNameEn();
		// System.out.println("categoryname=" + categoryname);
		if (categoryname != null && categoryname.length() > 0) {
			str.append("/").append(categoryname).append("/").append(article.getId()).append(Global.getUrlSuffix());
		} else {
			str.append("/").append(article.getCategory().getId()).append("/").append(article.getId()).append(Global.getUrlSuffix());
		}

		return str.toString();
	}

	/**
	 * 获得栏目动态URL地址
	 * 
	 * @param category
	 * @return url
	 */
	public static String getUrlDynamic(Category category) {
		if (StringUtils.isNotBlank(category.getHref())) {
			if (!category.getHref().contains("://")) {
				return context.getContextPath() + Global.getFrontPath() + category.getHref();
			} else {
				return category.getHref();
			}
		}
		StringBuilder str = new StringBuilder();
		String categoryname = category.getNameEn();
		str.append(context.getContextPath()).append(Global.getFrontPath());
		if (categoryname != null && categoryname.length() > 0) {
			str.append("/").append(categoryname).append("/");
		} else {
			str.append("/").append(category.getId()).append("/");
		}
		return str.toString();
	}

	/**
	 * 从图片地址中去除ContextPath地址
	 * 
	 * @param src
	 * @return src
	 */
	public static String formatImageSrcToDb(String src) {
		if (StringUtils.isBlank(src))
			return src;
		if (src.startsWith(context.getContextPath() + "/userfiles")) {
			return src.substring(context.getContextPath().length());
		} else {
			return src;
		}
	}

	/**
	 * 从图片地址中加入ContextPath地址
	 * 
	 * @param src
	 * @return src
	 */
	public static String formatImageSrcToWeb(String src) {
		if (StringUtils.isBlank(src))
			return src;
		if (src.startsWith(context.getContextPath() + "/userfiles")) {
			return src;
		} else {
			return context.getContextPath() + src;
		}
	}

	public static void addViewConfigAttribute(Model model, String param) {
		if (StringUtils.isNotBlank(param)) {
			@SuppressWarnings("rawtypes")
			Map map = JsonMapper.getInstance().fromJson(param, Map.class);
			if (map != null) {
				for (Object o : map.keySet()) {
					model.addAttribute("viewConfig_" + o.toString(), map.get(o));
				}
			}
		}
	}

	public static void addViewConfigAttribute(Model model, Category category) {
		List<Category> categoryList = Lists.newArrayList();
		Category c = category;
		boolean goon = true;
		do {
			if (c.getParent() == null || c.getParent().isRoot()) {
				goon = false;
			}
			categoryList.add(c);
			c = c.getParent();
		} while (goon);
		Collections.reverse(categoryList);
		for (Category ca : categoryList) {
			addViewConfigAttribute(model, ca.getViewConfig());
		}
	}

	/**
	 * 
	 * 
	 * 获取推荐位的文章内容
	 */

	public static List<Article> getPositionList(String category_id, String type, Integer index, Integer size, String param,
			Integer officSize) {
		return getFirstImageList(articleService.getPosList(category_id, type, index, size, officSize));
	}

	public static List<Article> getPositionList(String category_id, String type, Integer size, String param) {
		Integer officSize = 0;
		return getPositionList(category_id, type, 0, size, param, officSize);
	}

	/**
	 * 获取广告位的广告代码
	 * 
	 */
	public static List<Ads> getAdsList(String advPosid, String typeid) {
		return adsService.findAdsbyposid(advPosid, typeid);
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static String getDefinedTime(Integer d, Integer h, Integer m, Integer s) {

		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		date.setDate(d == 9999 ? 1 : date.getDate() + d);
		date.setHours(h == 9999 ? 0 : date.getHours() + h);
		date.setMinutes(m == 9999 ? 0 : date.getMinutes() + m);
		date.setSeconds(s == 9999 ? 0 : date.getSeconds() + s);
		return bartDateFormat.format(date);
	}

	public static String toMobileImg(HttpServletRequest request, String img) {
		// if (UserAgentUtils.isMobile(request)) {
		// if (img.indexOf("http") > -1 || img.indexOf("https") > -1) {
		// return img;
		//
		// } else {
		// String mb_img_path = request.getSession().getServletContext()
		// .getRealPath("/")
		// + "mobile" + img;
		// // System.out.println("mb_img_path=" + mb_img_path);
		// File file = new File(mb_img_path);
		// if (!file.exists()) {
		// File checkdir = new File(file.getParent());
		// if (!checkdir.exists()) {
		// checkdir.mkdirs();
		// }
		// try {
		// Thumbnails
		// .of(new File(request.getSession()
		// .getServletContext().getRealPath("/")
		// + img)).scale(0.8F).toFile(mb_img_path);
		// return "/mobile" + img;
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return img;
		// }
		// } else {
		// return "/mobile" + img;
		// }
		// }
		// } else {
		// return img;
		// }
		return img;
	}

	public static String WordtoMobileImg(HttpServletRequest request, String text) {
		if (StringUtils.isNotBlank(text)) {
			if (UserAgentUtils.isMobile(request)) {
				Pattern ptn = Pattern.compile("\"(/attached/image/[\\d]*/[\\d_]*[.][gif|jpg|jpeg|png|bmp]*)\"");
				Matcher mth = ptn.matcher(text);
				while (mth.find()) {
					String img = mth.group(1);
					// System.out.println("img=" + img);
					String r_img = toMobileImg(request, img);
					// System.out.println("img=" + img);
					text = text.replace(img, r_img);
				}

			}
		}
		return text;

	}

	public static ArticlePageModel getArticlePageModel(String centext, Integer index) {
		ArticlePageModel model = new ArticlePageModel();
		String reg = "<hr style=\"page-break-after:always;\" class=\"ke-pagebreak\" />";
		String _reg = "<hr style=\"page-break-after:always;\" class=\"ke-pagebreak\"/>";
		String[] lines = centext.split(centext.indexOf(reg) > -1 ? reg : _reg);
		log.info("lines:size:" + lines.length);
		Integer count = lines.length;
		model.setCentext(centext);
		model.setLines(lines);
		model.setCount(count);
		model.setIndex(index);
		log.info("count" + model.getCount());
		return model;
	}

	public static String SplitLineForArticle(ArticlePageModel model, Integer pageIndex) {
		StringBuffer str = new StringBuffer();
		if (model.getCount() == 0) {
			log.info("不分页");
			str = new StringBuffer(model.getCentext());
		} else {
			log.info("不分页");
			if (model.getLines() != null && model.getLines().length >= pageIndex - 1) {
				str = new StringBuffer(model.getLines()[pageIndex - 1]);
			}
		}
		return str.toString();
	}

	public static String getFirstContentImg(String context) {
		if (StringUtils.isNotBlank(context)) {
			List<String> imgs = getContentImgs(context);
			if (imgs != null && imgs.size() > 0) {
				return imgs.get(0);
			}
		}
		return "";
	}

	public static List getContentImgs(String context) {
		String context_html = Encodes.unescapeHtml(context);
		// log.info("context:" + context);
		List<String> imgList = new ArrayList<String>();
		if (context_html != null && context_html.length() > 0) {
			// log.info("begin find first image");
			Pattern ptn = Pattern.compile("<img.*?src=\"(.*?)\"");
			Matcher mth = ptn.matcher(context_html);
			while (mth.find()) {
				String src = mth.group(1);
				// log.info("find image src=" + src);
				imgList.add(src);
			}
		}

		// for (String s : imgList) {
		// log.info("s:" + s);
		// }
		return imgList;
	}

	public static Position getPosition(String type) {
		Position position = new Position();
		position.setType(type);
		return positionDao.get(position);
	}

	public static List<Article> getFirstImageList(List<Article> articles) {
		return getMoreImageList(articles, false);
	}

	public static List<Article> getMoreImageList(List<Article> articles, boolean getMore) {
		List<Article> articles_temp = new ArrayList<Article>();
		for (Article article : articles) {
			// log.info("image:" + article.getImage());
			if (StringUtils.isNotBlank(article.getImage())) {

				if (!article.getImage().equals("/imgdown/min/defaultpic.gif")) {
					articles_temp.add(article);
				} else {
					// log.info("article.getArticleData().getContent():"
					// + article.getArticleData().getContent());
					article.setImage(getFirstContentImg(article.getArticleData().getContent()));
					articles_temp.add(article);

				}
				if (getMore) {
					List<String> images = getContentImgs(article.getArticleData().getContent());
					// log.info("images.size:" + images.size());
					article.setImages(images);
				}
			} else {
				// log.info("content:" + article.getArticleData().getContent());
				article.setImage(getFirstContentImg(article.getArticleData() != null ? article.getArticleData().getContent() : null));
				articles_temp.add(article);
				if (getMore) {
					List<String> images = getContentImgs(article.getArticleData().getContent());
					// log.info("images.size:" + images.size());
					article.setImages(images);
				}
			}
		}
		return articles;
	}

	private static String[] getImgForContent(Article article) {
		return null;
	}

	public static List<Article> getRondomList(String category_id, Integer size, String param) {
		return getFirstImageList(articleService.getRondomList(category_id, size));
	}

	public static List<Article> getTodayHitList(Integer size) {
		return getFirstImageList(articleService.getTodayHitList(size));

	}

	public static List<Article> getMoreImgPositionList(String category_id, String type, Integer size, String param) {
		Integer officSize = 0;
		return getMoreImageList(articleService.getPosList(category_id, type, 0, size, officSize), true);
	}

	public static List<Article> getMoreImgPositionList(String category_id, String type, Integer index, Integer size, String param,
			Integer officSize) {
		return getMoreImageList(articleService.getPosList(category_id, type, index, size, officSize), true);
	}

	/**
	 * 获取文章的第一段文字
	 * @param article2
	 * @return
	 */
	public static String getFirstPara(Article article) {
		String content = article.getArticleData().getContent();
		String firstPara = "";
		if(StringUtils.isBlank(article.getDescription())) {
			Pattern ptn = Pattern.compile("<p.*?>(.*?[\\s]*.*?)</p>", Pattern.CASE_INSENSITIVE);
			Matcher mth = ptn.matcher(content);
			while (mth.find()) {
				log.info("找到内容");
				log.info("g=" + mth.group(1));
				firstPara = mth.group(1).trim();
				log.info(firstPara);
				if(firstPara.contains("<img")) {
					continue;
				}
				break;
			}
		}
		return firstPara;
	}
}
package com.modules.sys.copy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.modules.cms.entity.Article;
import com.modules.cms.entity.ArticleData;
import com.modules.cms.entity.Category;
import com.modules.cms.service.CategoryService;
import com.modules.copy.entity.Contextnodedefine;
import com.modules.msgsource.entity.Msgsource;
import com.modules.sys.entity.User;
import com.modules.sys.utils.UserUtils;

public class CopyUrlCall implements Callable<List<Article>> {
	private Logger log = Logger.getLogger(getClass());

	private List<String> urllist;
	private CategoryService categoryService;
	private Contextnodedefine contextnodedefine;

	CopyUrlCall(List<String> urllist, CategoryService categoryService, Contextnodedefine contextnodedefine) {
		this.urllist = urllist;
		this.categoryService = categoryService;
		this.contextnodedefine = contextnodedefine;
	}

	@Override
	public List<Article> call() throws Exception {
		// TODO Auto-generated method stub
		List<Article> articles = new ArrayList<Article>();
		for (int i = 0; i < urllist.size(); i++) {
			String url = urllist.get(i);
			log.info("url=" + url + "------index=" + i);

			Article article = new Article();

			Category category = categoryService.get(contextnodedefine.getCategoryId());
			article.setCategory(category);
			article.setCustomContentView(category.getCustomContentView());
			article.setAcquisitionSource(url);
			article.setUpdateDate(new Date());
			article.setPosid(contextnodedefine.getPosid());
			User user = UserUtils.getUser();
			// log.info("loginName:" + user.getLoginName());

			CopyUtil util = new CopyUtil(url, contextnodedefine.getContextdefine().getContentLang());
			StringBuffer context = util.getUrlContext();
			if (contextnodedefine.getIsContextSimpleMeta().equals("1")) {
				util.getMeteList(context);
				article.setKeywords(util.getMetaList().get("keywords"));
				article.setDescription(util.getMetaList().get("description"));
			} else {
				article.setKeywords(util.defaultCopy(context,
						util.appendBegin2End(contextnodedefine.getContextKeyorksRegBegin(), contextnodedefine.getContextKeyorksRegEnd()),
						""));
				article.setDescription(util.defaultCopy(context, util.appendBegin2End(contextnodedefine.getContextDescriptionRegBegin(),
						contextnodedefine.getContextDescriptionRegEnd()), ""));
			}
			if (contextnodedefine.getIsContextSimpleTitle().equals("1")) {
				article.setTitle(util.simpletitle(context));
			} else {
				article.setTitle(util.defaultCopy(context,
						util.appendBegin2End(contextnodedefine.getContextTitleBegin(), contextnodedefine.getContextTitleEnd()), ""));
				// log.info("t=" + article.getTitle());
			}
			if (contextnodedefine.getIsContextSimpleDate().equals("1")) {
				article.setCreateDate(new Date());
			} else {
				try {
					article.setCreateDate(
							util.fomatStringToDate(util.defaultCopy(context, util.replace2reg(contextnodedefine.getContextDateReg()), ""),
									contextnodedefine.getContextDateFormat()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 组合ID

			// article.setId(defaultCopy(new StringBuffer(url),
			// "[/]{1}([\\d]+)*.html", ""));
			if (contextnodedefine.getIshaspic().equals("1") && util.getMiniimglist() != null
					&& util.getMiniimglist().size() == urllist.size()) {
				if (StringUtils.isNotBlank(util.getMiniimglist().get(i))) {
					// 设备缩略图
					// log.info("id=" + article.getId() + " 缩略图="
					// + miniimglist.get(i));
					article.setImage(util.getMiniimglist().get(i));
				}
			}
			if (contextnodedefine.getIsContextSimpleShortTitle().equals("0") && util.getShorttitlelist() != null
					&& util.getShorttitlelist().size() == urllist.size()) {
				if (StringUtils.isNotBlank(util.getShorttitlelist().get(i))) {
					// log.info("id=" + article.getId() + " 短标题："
					// + shorttitlelist.get(i));
					article.setShorttile(util.getShorttitlelist().get(i));
				}
			}

			String maintext = util.getMainText(context,
					util.appendBegin2End(contextnodedefine.getMainContextRegBegin(), contextnodedefine.getMainContextRegEnd()),
					contextnodedefine.getContextdefine().getUrl(), url, contextnodedefine);
			// log.info("maintext=" + maintext);

			// log.info("finsh");
			ArticleData articleData = new ArticleData();
			maintext = maintext.trim().replaceAll("</div>$", "");
			String result = util.getNextContextContext(context, contextnodedefine, url, maintext.trim());
			articleData.setContent(result);
			// log.info(articleData.getContent());
			article.setArticleData(articleData);
			article.setDelFlag("3");
			if (contextnodedefine.getIsMsgsource().equals("1")) {
				Msgsource msgsource = util.InitMsgsource(context, contextnodedefine);
				// log.info("msgsource:" + msgsource);
				article.setMsgsource(msgsource);
			} else {
				Msgsource msgsource = new Msgsource();
				msgsource.setUrl(contextnodedefine.getCopyUrl());
				msgsource.setName(contextnodedefine.getDescription());
				article.setMsgsource(msgsource);
			}
			articles.add(article);

		}
		return articles;
	}
}

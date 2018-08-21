package com.modules.cms.utils;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.modules.cms.dao.ArticleDao;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.CmsUpdatePage;
import com.modules.cms.service.CmsUpdatePageService;
import com.modules.cms.web.StaticPageThread;
import com.modules.copy.web.RunMobileStatic;
import com.modules.copy.web.RunStaticPage;

@EnableScheduling
@Service
@Lazy(false)
public class ArticleAutoRun {
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private CmsUpdatePageService updatePageService;
	@Autowired
	private RunMobileStatic mobileStatic;
	  
	@Autowired
	private RunStaticPage runStatic;
	
	
	private Logger log = Logger.getLogger(ArticleAutoRun.class);

	/*@Scheduled(cron = "0 0/5 * * * ?")
	public void ArticleRun() {
		log.info("into ArticleRun");
		List<Article> timeList = articleDao.findTimeList();
		articleDao.UpdateByTiming();
		CmsUpdatePage updatePage = new CmsUpdatePage();
		// 首页进行静态化
		updatePage.setId("2");
		updatePage.setType("1");
		// runStatic.indexPublic(updatePage); runStatic.listPublic(updatePage);
		mobileStatic.indexPublic(updatePage);
		for (Article article : timeList) {
			// 栏目页静态化
			if (article.getCategory() != null) {
				updatePage.setId("3");
				updatePage.setCategoryId(article.getCategory().getId());
				mobileStatic.listPublic(updatePage);
			}
		}
	}*/

	@Scheduled(cron = "59 23 * * * ?")
	public void ClearTodayHits() {
		log.info("ClearTodayHits");
		articleDao.clearTodayHit();
	}
	
	
	@Scheduled(cron = "0 0 0 * * ?")
	public void statsAllArticle() {
		List<Article> list = articleDao.getAllNoStats();  
		for(Article newArticle :list) {
			Thread thread =new StaticPageThread(runStatic, mobileStatic, newArticle,updatePageService);
			thread.start();
		}
	}

}

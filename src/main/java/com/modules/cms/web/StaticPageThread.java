package com.modules.cms.web;

import java.util.ArrayList;
import java.util.List;

import com.common.utils.DateUtils;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.CmsUpdatePage;
import com.modules.cms.service.CmsUpdatePageService;
import com.modules.copy.web.RunMobileStatic;
import com.modules.copy.web.RunStaticPage;

public class StaticPageThread extends Thread {
	private RunStaticPage runStatic;
	private RunMobileStatic mobileStatic;
	private Article article;
	private CmsUpdatePageService updatePageService;
	private List<Article> list = new ArrayList<Article>();

	public StaticPageThread(RunStaticPage runStatic, RunMobileStatic mobileStatic, Article article,
			CmsUpdatePageService updatePageService) {
		this.runStatic = runStatic;
		this.mobileStatic = mobileStatic;
		this.article = article;
		this.updatePageService = updatePageService;
	}

	public StaticPageThread(RunStaticPage runStatic,RunMobileStatic mobileStatic,List<Article>list,CmsUpdatePageService updatePageService) {
		this.runStatic = runStatic;
		this.mobileStatic = mobileStatic;
		this.article = article;
		this.updatePageService = updatePageService;
		this.list = list;
	}
	
	public void run() {
		//旧版本初始化，初始化一次一个线程
		if(list.size() == 0) {
			// 对应文章页进行静态化
			runStatic.manualStatic(article.getId());
			mobileStatic.manualStatic(article.getId());
			// 首页进行静态化
			CmsUpdatePage updatePage = new CmsUpdatePage();
			updatePage.setId("2");
			updatePage.setType("1");
			runStatic.indexPublic(updatePage);
			runStatic.listPublic(updatePage);
			mobileStatic.indexPublic(updatePage);
			mobileStatic.listPublic(updatePage);
			// 对应文章栏目页进行静态化（等待定时程序扫描）
			updatePage.setId("3");
			updatePage.setIsUpdate("1");
			updatePage.setType("4");
			updatePage.setState("1");
			updatePage.setUpdateDate(DateUtils.parseDate(DateUtils.getDate()));
			updatePage.setCategoryId("0");
			updatePageService.update(updatePage);
		}
		//2018-08-23 一个线程执行所有内容
		else {
			for(int i =0 ; i<list.size(); i++) {
				runStatic.manualStatic(list.get(i).getId());
				mobileStatic.manualStatic(list.get(i).getId());
			}	
				// 首页进行静态化
				CmsUpdatePage updatePage = new CmsUpdatePage();
				updatePage.setId("2");
				updatePage.setType("1");
				runStatic.indexPublic(updatePage);
				runStatic.listPublic(updatePage);
				mobileStatic.indexPublic(updatePage);
				mobileStatic.listPublic(updatePage);
				// 对应文章栏目页进行静态化（等待定时程序扫描）
				updatePage.setId("3");
				updatePage.setIsUpdate("1");
				updatePage.setType("4");
				updatePage.setState("1");
				updatePage.setUpdateDate(DateUtils.parseDate(DateUtils.getDate()));
				updatePage.setCategoryId("0");
				updatePageService.update(updatePage);
			
		}
	}
}

package com.modules.copy.web;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.utils.DateUtils;
import com.modules.cms.dao.ArticleDao;
import com.modules.cms.dao.ArticleDataDao;
import com.modules.cms.dao.ArticleDataTempDao;
import com.modules.cms.dao.ArticleTempDao;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.ArticleDataTemp;
import com.modules.cms.entity.ArticleTemp;
import com.modules.cms.entity.CmsUpdatePage;
import com.modules.cms.service.ArticleDataTempService;
import com.modules.cms.service.ArticleService;
import com.modules.cms.service.ArticleTempService;
import com.modules.cms.service.CategoryService;
import com.modules.cms.service.CmsUpdatePageService;
import com.modules.copy.dao.ContextNodeDefineFilterDao;
import com.modules.copy.entity.ContextNodeDefineFilter;
import com.modules.copy.entity.Contextdefine;
import com.modules.copy.entity.Contextnodedefine;
import com.modules.copy.service.ContextNodeDefineFilterService;
import com.modules.copy.service.ContextdefineService;
import com.modules.copy.service.ContextnodedefineService;
import com.modules.msgsource.dao.MsgsourceDao;
import com.modules.position.dao.PositionArticleDao;
import com.modules.position.entity.PositionArticle;
import com.modules.sys.copy.CopyUtil;

@Service
public class AutoArticlePostAll {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private ArticleTempService articletempService;
	@Autowired
	private ArticleDataTempService articleDataService;
	@Autowired
	private ArticleService articleService;
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
	@Autowired
	private CmsUpdatePageService updatePageService;
	/**
	 * 
	 */
	
	public void articlepostAll() {
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
	}
}

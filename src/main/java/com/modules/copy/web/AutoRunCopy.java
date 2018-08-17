package com.modules.copy.web;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modules.cms.dao.ArticleDao;
import com.modules.cms.dao.ArticleDataDao;
import com.modules.cms.dao.ArticleDataTempDao;
import com.modules.cms.dao.ArticleTempDao;
import com.modules.cms.entity.Article;
import com.modules.cms.service.CategoryService;
import com.modules.copy.dao.ContextNodeDefineFilterDao;
import com.modules.copy.entity.ContextNodeDefineFilter;
import com.modules.copy.entity.Contextdefine;
import com.modules.copy.entity.Contextnodedefine;
import com.modules.copy.service.ContextNodeDefineFilterService;
import com.modules.copy.service.ContextdefineService;
import com.modules.copy.service.ContextnodedefineService;
import com.modules.msgsource.dao.MsgsourceDao;
import com.modules.sys.copy.CopyUtil;

@Service
public class AutoRunCopy {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	ContextNodeDefineFilterService filterService;

	@Autowired
	ContextnodedefineService contextnodedefineService;

	@Autowired
	ContextdefineService contextdefineService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	private ArticleTempDao articleTempDao;
	@Autowired
	private ArticleDataTempDao articleDataTempDao;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private ArticleDataDao articleDataDao;
	@Autowired
	private ContextNodeDefineFilterDao filterDao;

	@Autowired
	private MsgsourceDao msgsourceDao;
	@Value("${webpath}")
	protected String webpath;

	private static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

		return isSameDate;
	}

	/**
	 * 
	 */
	
	public void runCopy() {
		log.info("Scheduled");
		List<Contextnodedefine> contextnodedefines = contextnodedefineService.findAll(new Contextnodedefine());
		log.info("contextnodedefines.size=" + contextnodedefines.size());
		if (contextnodedefines != null && contextnodedefines.size() > 0) {
			for (Contextnodedefine contextnodedefine : contextnodedefines) {

				Contextdefine querycontextdefine = new Contextdefine();
				querycontextdefine.setCid(contextnodedefine.getParentid());
				List<Contextdefine> contextdefines = contextdefineService.findAll(querycontextdefine);
				if (contextdefines != null && contextdefines.size() == 1) {
					Contextdefine contextdefine = contextdefines.get(0);
					// if (contextdefine.getCopyflag() != null &&
					// contextdefine.getCopyflag().SIZE > 0) {
					if (!isSameDate(contextnodedefine.getLastcopydate(), new Date())) {
						contextnodedefine.setContextdefine(contextdefine);
						contextnodedefine.setLastcopydate(new Date());
						contextnodedefineService.update(contextnodedefine);

						CopyUtil copy = new CopyUtil(contextnodedefine, webpath, categoryService);

						List<Article> articles = copy.getArticles();
						log.info("articles.size()=" + articles.size());
						copy.full();
						
						/*List<ContextNodeDefineFilter> filterList = filterService.findListByNodeId(contextdefine.getCid());*/

						/**
						 * 执行保存到内容表
						 */
						CopyTempUtil copyTempUtil = new CopyTempUtil();
						copyTempUtil.DefineToTemp(articles, contextdefine, contextnodedefine, articleTempDao, articleDataTempDao, articleDao, articleDataDao,
								msgsourceDao, filterDao);
						// contextnodedefineService.update(contextnodedefine);

					} else {
						log.error("今天已经采集过了");
					}
					// } else {
					// log.error("找不到时间标识");
					// }
				}
			}
		}
	}
}

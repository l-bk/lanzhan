package com.modules.copy.web;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.common.config.Global;
import com.common.web.BaseController;
import com.modules.cms.dao.ArticleDao;
import com.modules.cms.dao.ArticleDataDao;
import com.modules.cms.dao.ArticleDataTempDao;
import com.modules.cms.dao.ArticleTempDao;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.ArticleData;
import com.modules.cms.entity.ArticleDataTemp;
import com.modules.cms.entity.ArticleTemp;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.Site;
import com.modules.cms.service.CategoryService;
import com.modules.cms.service.FileTplService;
import com.modules.cms.service.SiteService;
import com.modules.cms.utils.TplUtils;
import com.modules.copy.dao.ContextNodeDefineFilterDao;
import com.modules.copy.dao.ContextdefineDao;
import com.modules.copy.dao.ContextnodedefineDao;
import com.modules.copy.entity.Contextdefine;
import com.modules.copy.entity.Contextnodedefine;
import com.modules.copy.service.ContextdefineService;
import com.modules.copy.service.ContextnodedefineService;
import com.modules.msgsource.dao.MsgsourceDao;
import com.modules.sys.copy.CopyUtil;

@Controller
@RequestMapping(value = "${adminPath}/cms/TestCopy")
public class TestCopyController extends BaseController {

	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ContextNodeDefineFilterDao filterDao;

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

	@RequestMapping(value = { "copy", "" })
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

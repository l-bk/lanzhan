package com.modules.copy.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.modules.msgsource.dao.MsgsourceDao;
import com.modules.sys.copy.CopyUtil;

@Controller
@RequestMapping(value = "${adminPath}/cms/copynode")
public class ContextnodedefineController extends BaseController {
	
	@Autowired
	private ContextNodeDefineFilterDao filterDao;

	@Autowired
	private ContextnodedefineDao contextnodedefineDao;
	@Autowired
	private ArticleTempDao articleTempDao;
	@Autowired
	private ArticleDataTempDao articleDataTempDao;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private ArticleDataDao articleDataDao;

	@Autowired
	private FileTplService fileTplService;
	@Autowired
	private SiteService siteService;
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ContextdefineDao contextdefineDao;

	@Autowired
	private MsgsourceDao msgsourceDao;

	@Value("${webpath}")
	protected String webpath;

	@ModelAttribute("contextnodedefine")
	public Contextnodedefine get(@RequestParam(required = false) Integer cid) {
		if (cid != null) {
			Contextnodedefine contextnodedefine = new Contextnodedefine();
			contextnodedefine.setCid(cid);
			return contextnodedefineDao.findAll(contextnodedefine).get(0);
		} else {
			return new Contextnodedefine();
		}
	}

	@RequestMapping(value = { "list", "" })
	public String list(Contextnodedefine contextnodedefine, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("parentid==" + contextnodedefine.getParentid());
		List<Contextnodedefine> list = contextnodedefineDao.findAll(contextnodedefine);
		model.addAttribute("parentid", contextnodedefine.getParentid());
		model.addAttribute("list", list);
		System.out.println("into ContextdefineController");
		if (list != null && list.size() > 0) {
			for (Contextnodedefine out : list) {
				System.out.println(out);
			}
		} else {
			System.out.println("list null ");
		}

		return "modules/cms/copynodeList";
	}

	@RequestMapping(value = "form")
	public String form(Contextnodedefine contextnodedefine, Model model) {
		// Contextnodedefine contextnodedefine2 = new Contextnodedefine();
		// if (contextnodedefine.getCid() != null) {
		// contextnodedefine2 = this.get(contextnodedefine.getCid());
		// System.out.println("list_reg_end====="
		// + contextnodedefine2.getListRegEnd());
		// }
		if (contextnodedefine.getCid() != null) {
			contextnodedefine = contextnodedefineDao.findAll(contextnodedefine).get(0);
		}
		model.addAttribute("listViewList", getTplContent(Category.DEFAULT_TEMPLATE));
		model.addAttribute("category_DEFAULT_TEMPLATE", Category.DEFAULT_TEMPLATE);
		model.addAttribute("contentViewList", getTplContent(Article.DEFAULT_TEMPLATE));
		model.addAttribute("article_DEFAULT_TEMPLATE", Article.DEFAULT_TEMPLATE);
		model.addAttribute("contextnodedefine", contextnodedefine);
		return "modules/cms/copynodeForm";
	}

	@RequestMapping(value = "save")
	public String save(Contextnodedefine contextnodedefine, Model model, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/cms/copynode/";
		}
		if (!beanValidator(model, contextnodedefine)) {
			System.out.println("数据错误");
			return form(contextnodedefine, model);
		}
		if (contextnodedefine.getCid() != null) {
			System.out.println("Cid===" + contextnodedefine.getCid());
			contextnodedefineDao.update(contextnodedefine);
		} else {
			System.out.println("Cid===" + contextnodedefine.getCid());
			contextnodedefineDao.save(contextnodedefine);
		}
		addMessage(redirectAttributes, "保存采集规则'" + contextnodedefine.getDescription() + "'成功");
		return "redirect:" + adminPath + "/cms/copynode/list?parentid=" + contextnodedefine.getParentid();
	}

	@RequestMapping(value = "collect")
	public String collect(Contextnodedefine contextnodedefine, Model model) {
		Contextnodedefine contextnodedefine2 = contextnodedefineDao.findAll(contextnodedefine).get(0);

		Contextdefine contextdefine = new Contextdefine();
		contextdefine.setCid(contextnodedefine.getParentid());
		Contextdefine contextdefine2 = contextdefineDao.findAll(contextdefine).get(0);
		contextnodedefine2.setContextdefine(contextdefine2);
		System.out.println("lang====" + contextdefine2.getLang());
		CopyUtil copyUtil = new CopyUtil(contextnodedefine2, webpath, categoryService);

		List<Article> list = copyUtil.getArticles();

		copyUtil.full();
		System.out.println("采集完成====" + list.size());
		CopyTempUtil copyTempUtil = new CopyTempUtil();
		copyTempUtil.DefineToTemp(list, contextdefine2, contextnodedefine2, articleTempDao, articleDataTempDao, articleDao, articleDataDao, msgsourceDao, filterDao);
		return "redirect:" + adminPath + "/cms/copynode/list?parentid=" + contextnodedefine.getParentid();
	}

	private List<String> getTplContent(String prefix) {
		List<String> tplList = fileTplService.getNameListByPrefix(siteService.get(Site.getCurrentSiteId()).getSolutionPath());
		tplList = TplUtils.tplTrim(tplList, prefix, "");
		return tplList;
	}
}

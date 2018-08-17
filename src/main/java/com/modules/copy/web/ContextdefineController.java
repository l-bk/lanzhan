package com.modules.copy.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.common.config.Global;
import com.common.web.BaseController;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.Site;
import com.modules.cms.service.FileTplService;
import com.modules.cms.service.SiteService;
import com.modules.cms.utils.TplUtils;
import com.modules.copy.dao.ContextdefineDao;
import com.modules.copy.entity.Contextdefine;

@Controller
@RequestMapping(value = "${adminPath}/cms/copy")
public class ContextdefineController extends BaseController {

	@Autowired
	private ContextdefineDao contextdefineDao;
	@Autowired
	private FileTplService fileTplService;
	@Autowired
	private SiteService siteService;

	@RequestMapping(value = { "list", "" })
	public String list(Contextdefine contextdefine, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Contextdefine query = new Contextdefine();
//		query.setCategoryid(categoryid);

		List<Contextdefine> list = contextdefineDao
				.findAll(query);
		model.addAttribute("list", list);
		System.out.println("into ContextdefineController");
		if (list != null && list.size() > 0) {
			for (Contextdefine out : list) {
				System.out.println("list id====" + out.getId());
			}
		} else {
			System.out.println("list null ");
		}

		return "modules/cms/copyList";
	}

	@RequestMapping(value = "form")
	public String form(Contextdefine contextdefine, Model model) {
		// Contextdefine contextdefine2=new Contextdefine();
		// contextdefine2.setCid(contextdefine.getCid());
		// System.out.println("form id===="+contextdefine.getCid());
		if (contextdefine.getCid() != null) {
			contextdefine = contextdefineDao.findAll(contextdefine).get(0);

		}
		model.addAttribute("listViewList",
				getTplContent(Category.DEFAULT_TEMPLATE));
		model.addAttribute("category_DEFAULT_TEMPLATE",
				Category.DEFAULT_TEMPLATE);
		model.addAttribute("contentViewList",
				getTplContent(Article.DEFAULT_TEMPLATE));
		model.addAttribute("article_DEFAULT_TEMPLATE", Article.DEFAULT_TEMPLATE);

		model.addAttribute("contextdefine", contextdefine);
		return "modules/cms/copyForm";
	}

	private List<String> getTplContent(String prefix) {
		List<String> tplList = fileTplService.getNameListByPrefix(siteService
				.get(Site.getCurrentSiteId()).getSolutionPath());
		tplList = TplUtils.tplTrim(tplList, prefix, "");
		return tplList;
	}

	@RequestMapping(value = "save")
	public String save(Contextdefine contextdefine, Model model,
			RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/cms/copynode/";
		}
		if (!beanValidator(model, contextdefine)) {
			System.out.println("数据错误");
			return form(contextdefine, model);
		}
		if (contextdefine.getCid() != null) {
			System.out.println("Cid===" + contextdefine.getCid());
			contextdefineDao.update(contextdefine);
		} else {
			System.out.println("Cid===" + contextdefine.getCid());
			System.out.println("contextdefine.getIscheckflag()==="
					+ contextdefine.getIscheckflag());
			contextdefineDao.save(contextdefine);
		}
		addMessage(redirectAttributes,
				"保存采集站点'" + contextdefine.getDescription() + "'成功");
		return "redirect:" + adminPath + "/cms/copy/";
	}
}

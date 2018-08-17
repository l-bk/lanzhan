/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.common.config.Global;
import com.common.utils.StringUtils;
import com.common.web.BaseController;
import com.modules.attention.entity.Attention;
import com.modules.attention.service.AttentionService;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.Site;
import com.modules.cms.service.CategoryService;
import com.modules.cms.service.FileTplService;
import com.modules.cms.service.SiteService;
import com.modules.cms.utils.TplUtils;
import com.modules.msgsource.entity.Msgsource;
import com.modules.msgsource.service.MsgsourceService;
import com.modules.sys.utils.UserUtils;

/**
 * 栏目Controller
 * 
 * @author ThinkGem
 * @version 2013-4-21
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/category")
public class CategoryController extends BaseController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private FileTplService fileTplService;
	@Autowired
	private SiteService siteService;
	@Autowired
	private MsgsourceService msgsourceService;
	@Autowired
	private AttentionService attentionService;

	@ModelAttribute("category")
	public Category get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return categoryService.get(id);
		} else {
			return new Category();
		}
	}

	@RequiresPermissions("cms:category:view")
	@RequestMapping(value = { "list", "" })
	public String list(Model model) {
		List<Category> list = Lists.newArrayList();
		List<Category> sourcelist = categoryService.findByUser(true, null);
		Category.sortList(list, sourcelist, "1");
		model.addAttribute("list", list);
		return "modules/cms/categoryList";
	}

	@RequiresPermissions("cms:category:view")
	@RequestMapping(value = "form")
	public String form(Category category, Model model) {
		if (category.getParent() == null
				|| category.getParent().getId() == null) {
			category.setParent(new Category("1"));
		}
		Category parent = categoryService.get(category.getParent().getId());
		category.setParent(parent);
		if (category.getOffice() == null
				|| category.getOffice().getId() == null) {
			category.setOffice(parent.getOffice());
		}
		model.addAttribute("listViewList",
				getTplContent(Category.DEFAULT_TEMPLATE));
		model.addAttribute("category_DEFAULT_TEMPLATE",
				Category.DEFAULT_TEMPLATE);
		model.addAttribute("contentViewList",
				getTplContent(Article.DEFAULT_TEMPLATE));
		model.addAttribute("article_DEFAULT_TEMPLATE", Article.DEFAULT_TEMPLATE);
		model.addAttribute("office", category.getOffice());
		model.addAttribute("category", category);
		return "modules/cms/categoryForm";
	}

	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "save")
	public String save(Category category, Model model,
			RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/cms/category/";
		}
		if (!beanValidator(model, category)) {
			return form(category, model);
		}
		categoryService.save(category);
		addMessage(redirectAttributes, "保存栏目'" + category.getName() + "'成功");
		return "redirect:" + adminPath + "/cms/category/";
	}

	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "delete")
	public String delete(Category category,
			RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/cms/category/";
		}
		if (Category.isRoot(category.getId())) {
			addMessage(redirectAttributes, "删除栏目失败, 不允许删除顶级栏目或编号为空");
		} else {
			categoryService.delete(category);
			addMessage(redirectAttributes, "删除栏目成功");
		}
		return "redirect:" + adminPath + "/cms/category/";
	}

	/**
	 * 批量修改栏目排序
	 */
	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts,
			RedirectAttributes redirectAttributes) {
		
		System.out.println(ids);
		int len = ids.length;
		Category[] entitys = new Category[len];
		for (int i = 0; i < len; i++) {
			entitys[i] = categoryService.get(ids[i]);
			entitys[i].setSort(sorts[i]);
			categoryService.save(entitys[i]);
		}
		addMessage(redirectAttributes, "保存栏目排序成功!");
		return "redirect:" + adminPath + "/cms/category/";
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(String module,
			@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Category> list = categoryService.findByUser(true, module);
		for (int i = 0; i < list.size(); i++) {
			Category e = list.get(i);
			if (extId == null
					|| (extId != null && !extId.equals(e.getId()) && e
							.getParentIds().indexOf("," + extId + ",") == -1)) {
//				if (!e.getId().equals("18")) {
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", e.getId());
					map.put("pId", e.getParent() != null ? e.getParent()
							.getId() : 0);
					map.put("name", e.getName());
					map.put("module", e.getModule());
					mapList.add(map);
//				}
			}
		}

		return mapList;
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData2")
	public List<Map<String, Object>> treeData2(String module,
			@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		List<Category> list = categoryService.findByUser(true, module);
//		for (int i = 0; i < list.size(); i++) {
//			Category e = list.get(i);
//			if (e.getAttention() != null && "1".equals(e.getAttention())) {
//				if (extId == null
//						|| (extId != null && !extId.equals(e.getId()) && e
//								.getParentIds().indexOf("," + extId + ",") == -1)) {
//					Map<String, Object> map = Maps.newHashMap();
//					map.put("id", e.getId());
//					map.put("pId", e.getParent() != null ? e.getParent()
//							.getId() : 0);
//					map.put("name", e.getName());
//					map.put("module", e.getModule());
//					mapList.add(map);
//				}
//			}
//		}
		Attention attention = new Attention();
		attention.setType("0");
		attention.setCreateBy(UserUtils.getUser());
		List<Attention> attlist = attentionService.findList(attention);
		System.out.println("attlist.sise=="+attlist.size());
		if (attlist != null && attlist.size() > 0) {
			for (int k = 0; k < attlist.size(); k++) {
				Attention a=attlist.get(k);
				System.out.println("a.getCategory().getModule()=="+a.getCategory().getModule());
				
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", a.getCategory().getId());
					map.put("pId", a.getCategory().getParent() != null ? a.getCategory().getParent()
							.getId() : 0);
					map.put("name", a.getCategory().getName());
					map.put("module", a.getCategory().getModule());
					mapList.add(map);
				
			}
		}
		return mapList;
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "formTreeData")
	public List<Map<String, Object>> formTreeData(String module,
			@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Msgsource> list = msgsourceService.findList(new Msgsource());
		for (int i = 0; i < list.size(); i++) {
			Msgsource e = list.get(i);
			if (extId == null || "".equals(extId)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getSid());
				map.put("pId", 0);
				map.put("name", e.getName());
				map.put("module", "article");
				mapList.add(map);
			}
		}

		return mapList;
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "formTreeData2")
	public List<Map<String, Object>> formTreeData2(String module,
			@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Attention attention = new Attention();
		attention.setType("2");
		attention.setCreateBy(UserUtils.getUser());
		List<Attention> list = attentionService.findList(attention);
		System.out.println("关注的信息源==" + list.size());
		for (int i = 0; i < list.size(); i++) {
			Attention e = list.get(i);
			if (extId == null || "".equals(extId)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getMsgid());
				// String sid=e.getMsgid().toString();
				// System.out.println("sid ==="+sid);
				// Msgsource msgsource=msgsourceService.get(sid);
				map.put("pId", 0);
				map.put("name", e.getMsgsource().getName());
				map.put("module", "article");
				mapList.add(map);
			}
		}

		return mapList;
	}

	private List<String> getTplContent(String prefix) {
		List<String> tplList = fileTplService.getNameListByPrefix(siteService
				.get(Site.getCurrentSiteId()).getSolutionPath());
		tplList = TplUtils.tplTrim(tplList, prefix, "");
		return tplList;
	}
}

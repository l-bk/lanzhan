/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.copy.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.common.config.Global;
import com.common.utils.StringUtils;
import com.common.web.BaseController;
import com.modules.copy.entity.ContextNodeDefineFilter;
import com.modules.copy.service.ContextNodeDefineFilterService;

/**
 * 采集过滤Controller
 * 
 * @author kj
 * @version 2016-08-15
 */
@Controller
@RequestMapping(value = "${adminPath}/copy/contextNodeDefineFilter")
public class ContextNodeDefineFilterController extends BaseController {

	@Autowired
	private ContextNodeDefineFilterService contextNodeDefineFilterService;

	@ModelAttribute
	public ContextNodeDefineFilter get(@RequestParam(required = false) String cid) {
		ContextNodeDefineFilter entity = null;
		if (StringUtils.isNotBlank(cid)) {
			entity = contextNodeDefineFilterService.get(cid);
		}
		if (entity == null) {
			entity = new ContextNodeDefineFilter();
		}
		return entity;
	}

	@RequiresPermissions("copy:contextNodeDefineFilter:view")
	@RequestMapping(value = { "list", "" })
	public String list(ContextNodeDefineFilter contextNodeDefineFilter, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		/*
		 * Page<ContextNodeDefineFilter> page =
		 * contextNodeDefineFilterService.findPage(new
		 * Page<ContextNodeDefineFilter>(request, response),
		 * contextNodeDefineFilter);
		 */
		List<ContextNodeDefineFilter> list = contextNodeDefineFilterService.findAll(contextNodeDefineFilter);
		model.addAttribute("list", list);
		model.addAttribute("nodeDefineId", contextNodeDefineFilter.getNodeDefineId());
		model.addAttribute("parentid", contextNodeDefineFilter.getParentid());
		return "modules/cms/contextNodeDefineFilterList";
	}

	@RequiresPermissions("copy:contextNodeDefineFilter:view")
	@RequestMapping(value = "form")
	public String form(ContextNodeDefineFilter contextNodeDefineFilter, Model model) {
		if (contextNodeDefineFilter.getCid() != null) {
			contextNodeDefineFilter = contextNodeDefineFilterService.get(String.valueOf(contextNodeDefineFilter.getCid()));
		}
		model.addAttribute("contextNodeDefineFilter", contextNodeDefineFilter);
		model.addAttribute("nodeDedineId", contextNodeDefineFilter.getNodeDefineId());
		return "modules/cms/contextNodeDefineFilterForm";
	}

	@RequiresPermissions("copy:contextNodeDefineFilter:edit")
	@RequestMapping(value = "save")
	public String save(ContextNodeDefineFilter contextNodeDefineFilter, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, contextNodeDefineFilter)) {
			return form(contextNodeDefineFilter, model);
		}
		contextNodeDefineFilterService.save(contextNodeDefineFilter);
		addMessage(redirectAttributes, "保存采集过滤成功");
		return "redirect:" + Global.getAdminPath() + "/copy/contextNodeDefineFilter/?repage&nodeDefineId="
				+ contextNodeDefineFilter.getNodeDefineId() + "&parentid=" + contextNodeDefineFilter.getParentid();
	}

	@RequiresPermissions("copy:contextNodeDefineFilter:edit")
	@RequestMapping(value = "delete")
	public String delete(ContextNodeDefineFilter contextNodeDefineFilter, RedirectAttributes redirectAttributes) {
		contextNodeDefineFilterService.delete(contextNodeDefineFilter);
		addMessage(redirectAttributes, "删除采集过滤成功");
		return "redirect:" + Global.getAdminPath() + "/copy/contextNodeDefineFilter/?repage&nodeDefineId="
				+ contextNodeDefineFilter.getNodeDefineId() + "&parentid=" + contextNodeDefineFilter.getParentid();
	}

}
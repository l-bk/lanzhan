/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.web;

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
import com.common.persistence.Page;
import com.common.web.BaseController;
import com.common.utils.StringUtils;
import com.modules.cms.entity.CmsUpdatePage;
import com.modules.cms.service.CmsUpdatePageService;

/**
 * 更新模板Controller
 * @author mkj
 * @version 2016-09-21
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsUpdatePage")
public class CmsUpdatePageController extends BaseController {

	@Autowired
	private CmsUpdatePageService cmsUpdatePageService;
	
	@ModelAttribute
	public CmsUpdatePage get(@RequestParam(required=false) String id) {
		CmsUpdatePage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = cmsUpdatePageService.get(id);
		}
		if (entity == null){
			entity = new CmsUpdatePage();
		}
		return entity;
	}
	
	@RequiresPermissions("cms:cmsUpdatePage:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsUpdatePage cmsUpdatePage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsUpdatePage> page = cmsUpdatePageService.findPage(new Page<CmsUpdatePage>(request, response), cmsUpdatePage); 
		model.addAttribute("page", page);
		return "modules/cms/cmsUpdatePageList";
	}

	@RequiresPermissions("cms:cmsUpdatePage:view")
	@RequestMapping(value = "form")
	public String form(CmsUpdatePage cmsUpdatePage, Model model) {
		model.addAttribute("cmsUpdatePage", cmsUpdatePage);
		return "modules/cms/cmsUpdatePageForm";
	}

	@RequiresPermissions("cms:cmsUpdatePage:edit")
	@RequestMapping(value = "save")
	public String save(CmsUpdatePage cmsUpdatePage, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsUpdatePage)){
			return form(cmsUpdatePage, model);
		}
		cmsUpdatePageService.save(cmsUpdatePage);
		addMessage(redirectAttributes, "保存更新模板成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsUpdatePage/?repage";
	}
	
	@RequiresPermissions("cms:cmsUpdatePage:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsUpdatePage cmsUpdatePage, RedirectAttributes redirectAttributes) {
		cmsUpdatePageService.delete(cmsUpdatePage);
		addMessage(redirectAttributes, "删除更新模板成功");
		return "redirect:"+Global.getAdminPath()+"/cms/cmsUpdatePage/?repage";
	}

}
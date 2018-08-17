package com.modules.ads.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.common.persistence.Page;
import com.common.utils.StringUtils;
import com.common.web.BaseController;
import com.modules.ads.entity.Ads;
import com.modules.ads.service.AdsService;
import com.modules.sys.utils.DictUtils;

@Controller
@RequestMapping(value = "${adminPath}/cms/ads")
public class AdsController extends BaseController {
	
	@Autowired
	private AdsService adsService;
	
	@ModelAttribute
	public Ads get(@RequestParam(required=false) String aid) {
		if (StringUtils.isNotBlank(aid)){

			return adsService.get(aid);
			
		} else {

			return new Ads();
		}
	}
	
	@RequiresPermissions("cms:ads:view")
	@RequestMapping(value = {"list", ""})
	public String list(Ads ads, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<Ads> page = adsService.findPage(new Page<Ads>(request, response), ads); 
        model.addAttribute("page", page);
		return "modules/cms/adsList";
	}
	
	@RequiresPermissions("cms:ads:view")
	@RequestMapping(value = "form")
	public String form(Ads ads, Model model) {
		model.addAttribute("ads", ads);
		return "modules/cms/adsForm";
	}
	
	@RequestMapping(value = "save")
	public String save(Ads ads, Model model, RedirectAttributes redirectAttributes) {

		adsService.save(ads);
		addMessage(redirectAttributes, DictUtils.getDictLabel(ads.getDelFlag(), "cms_del_flag", "保存")
				+"广告'" + ads.getAdname() + "'成功");
		return "redirect:" + adminPath + "/cms/ads";
	}
	
	
	@RequestMapping(value = "delete")
	public String delete(Ads ads, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		adsService.delete(ads);
		addMessage(redirectAttributes, ("删除")+"成功");
		return "redirect:" + adminPath + "/cms/ads";
	}
	
}

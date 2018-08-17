package com.modules.sensitive.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.common.persistence.Page;
import com.common.utils.StringUtils;
import com.common.web.BaseController;
import com.modules.sensitive.entity.Sensitive;
import com.modules.sensitive.service.SensitiveService;


@Controller
@RequestMapping(value = "${adminPath}/cms/sensitive")
public class SensitiveController extends BaseController{

	@Autowired
	private SensitiveService sensitiveService; 
	@ModelAttribute
	public Sensitive get(@RequestParam(required = false) String sid) {
		Sensitive sensitive=new Sensitive();
		
		
		if (StringUtils.isNotBlank(sid)) {
			sensitive.setSid(Integer.valueOf(sid));
			return sensitiveService.get(sensitive);
		} else {
			return new Sensitive();
		}
	}
	
	@RequestMapping(value = { "list", "" })
	public String list(Sensitive sensitive, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		
		Page<Sensitive> page = sensitiveService.findPage(new Page<Sensitive>(request,
				response), sensitive, true);
		
		
		model.addAttribute("page", page);
		return "modules/cms/sensitiveList";
	}
	
	
	@RequestMapping(value = "form")
	public String form(Sensitive sensitive, Model model) {
		
		
		
		
		model.addAttribute("sensitive", sensitive);
		return "modules/cms/sensitiveForm";
	}
	
	@RequestMapping(value = "save")
	public String save(Sensitive sensitive, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sensitive)) {
			return form(sensitive, model);
		}
		
		sensitiveService.save(sensitive);
		addMessage(redirectAttributes,
				"保存敏感词成功");
		
		return "redirect:" + adminPath + "/cms/sensitive";
	}
	
	@RequestMapping(value = "delete")
	public String delete(Sensitive sensitive, Model model,
			RedirectAttributes redirectAttributes) {
		
		
		sensitiveService.delete(sensitive);
		addMessage(redirectAttributes,
				"删除敏感词成功");
		
		return "redirect:" + adminPath + "/cms/sensitive";
	}
	
	
	

	
}

package com.modules.Template.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.utils.Encodes;
import com.common.utils.FileUtils;
import com.common.web.BaseController;
import com.modules.Template.common.DefaultTemplateUtil;
import com.modules.Template.entity.DefaultTemplate;
import com.modules.Template.entity.Template;

@Controller
@RequestMapping(value = "${adminPath}/cms/defaulttemplatefile")
public class DefaultTemplateFileController extends BaseController {
	protected Logger log = Logger.getLogger(TemplateFileController.class);

	@Value("${tmplatePath}")
	protected String webpath;

	@RequestMapping(value = { "list", "" })
	public String list(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam(value = "show", required = false) String show) {
		DefaultTemplateUtil util = new DefaultTemplateUtil(request, webpath);
		List<DefaultTemplate> list = util.getTemplates();
		log.info("list.size=" + JSONArray.toJSONString(list));
		model.addAttribute("list", list);
		return "modules/cms/defaulttemplateList";
	}

	@RequestMapping(value = "form")
	public String form(DefaultTemplate defaultTemplate, Model model) {
		if (defaultTemplate.getParentDirname() != null && defaultTemplate.getTemplatefilename() != null) {
			defaultTemplate.setContext(ReadFile(defaultTemplate));
			model.addAttribute("defaultTemplate", defaultTemplate);
			return "modules/cms/defaulttemplateForm";
		} else {
			model.addAttribute("defaultTemplate", new Template());
			return "modules/cms/defaulttemplateForm";
		}
		// return "redirect:" + adminPath + "/cms/templatefile/list";

	}

	@RequestMapping(value = "save")
	public String save(@ModelAttribute DefaultTemplate defaultTemplate, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "show", required = false) String show) {

		Printfile(defaultTemplate, request);
		return "redirect:" + adminPath + "/cms/defaulttemplatefile/list";

	}

	public String ReadFile(DefaultTemplate defaultTemplate) {
		String context = "";
		try {
			String filepath = webpath + defaultTemplate.getParentDirname() + "/" + defaultTemplate.getTemplatefilename() + "."
					+ defaultTemplate.getSuffix();
			log.info("filepath:" + filepath);
			context = FileUtils.readFileToString(new File(filepath));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return context;
	}

	private void Printfile(DefaultTemplate defaultTemplate, HttpServletRequest request) {
		// TODO Auto-generated method stub

		try {
			String filepath = webpath + defaultTemplate.getParentDirname() + "/" + defaultTemplate.getTemplatefilename() + "."
					+ defaultTemplate.getSuffix();
			log.info("filepath:" + filepath);
			FileUtils.write(new File(filepath), Encodes.unescapeHtml(defaultTemplate.getContext()));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}

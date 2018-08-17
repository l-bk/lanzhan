package com.modules.Template.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.utils.DateUtils;
import com.common.utils.Encodes;
import com.common.utils.StringUtils;
import com.common.web.BaseController;
import com.modules.Template.common.TemplateUtil;
import com.modules.Template.entity.Template;
import com.modules.Template.service.TemplateService;
import com.modules.cms.dao.CmsUpdatePageDao;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.CmsUpdatePage;
import com.modules.cms.entity.Site;
import com.modules.cms.service.ArticleDataService;
import com.modules.cms.service.ArticleService;
import com.modules.cms.service.CategoryService;
import com.modules.cms.service.CmsUpdatePageService;
import com.modules.cms.service.SiteService;
import com.modules.cms.utils.CmsUtils;

@Controller
@RequestMapping(value = "${adminPath}/cms/templatefile")
public class TemplateFileController extends BaseController {

	protected Logger log = Logger.getLogger(TemplateFileController.class);

	@Value("${webpath}")
	protected String webpath;

	@Autowired
	private TemplateService templateService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private ArticleDataService articleDataService;

	@Autowired
	private SiteService siteService;
	@Autowired
	private CmsUpdatePageService updatePageService;

	@ModelAttribute("template")
	public Template get(@RequestParam(required = false) Integer id, @RequestParam(required = false) String show) {
		if (id != null) {
			Template template = new Template();
			template.setCid(id);
			Template templateform = templateService.findAll(template).get(0);
			templateform.setShow(show);
			return templateform;
		} else {
			return new Template();
		}
	}

	// @RequiresPermissions("cms:templatefile:view")
	@RequestMapping(value = { "list", "" })
	public String list(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam(value = "show", required = false) String show) {
		Template query = new Template();
		log.info("show=" + show);
		if (show.equals("pc")) {
			query.setType("0");
		} else if (show.equals("moblie")) {
			query.setType("1");
		}
		// log.info("query.type=" + query.getType());
		List<Template> list = templateService.findAll(query);
		TemplateUtil util = new TemplateUtil(request, show, webpath);
		List<Template> filelist = util.getTemplates();
		// log.info("list.size=" + list.size());

		if (list.size() == 0) {
			for (Template template : filelist) {
				if (save(template, request)) {
					log.info("save ok");
				} else {
					log.info("save fail");
				}
			}
		} else {
			for (Template t : filelist) {
				boolean checkexists = false;
				for (Template t1 : list) {
					// log.info("path=" + t1.getTemplatepath() + " type="
					// + t1.getType());
					if (t.getTemplatefilename().equals(t1.getTemplatefilename())) {
						checkexists = true;
					}

					if (checkexists == false) {
						save(t, request);
					}
				}

			}

		}
		list = templateService.findAll(query);

		log.info("list.size=" + list.size());

		model.addAttribute("list", list);

		return "modules/cms/templateList";
	}

	// @RequiresPermissions("cms:templatefile:edit")
	@RequestMapping(value = "save")
	public String save(Template template, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "show", required = false) String show) {
		if (template.getCid() != null) {
			try {
				update(template,request);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			save(template, request);
		}
		Printfile(template, request);
		return "redirect:" + adminPath + "/cms/templatefile/list?show=" + show;

	}

	@RequestMapping(value = "form")
	public String form(Template template, Model model, @RequestParam(value = "show", defaultValue = "pc") String show) {
		if (template.getCid() != null) {
			Template t = new Template();
			t.setCid(template.getCid());
			log.info("show:" + show);
			template = templateService.get(t);
			template.setShow(show);
			// System.out.println("template=" + template);
			template.setContext(ReadFile(template));
			model.addAttribute("template", template);
			return "modules/cms/templateForm";
		} else {
			model.addAttribute("template", new Template());
			return "modules/cms/templateForm";
		}
		// return "redirect:" + adminPath + "/cms/templatefile/list";

	}

	@RequestMapping(value = "delete")
	public String delete(Template template, Model model, @RequestParam(value = "show", required = false) String show) {
		deletefile(template);

		return "redirect:" + adminPath + "/cms/templatefile/list?show=" + show;
	}

	@RequestMapping(value = "preview")
	public String preview(Template template, Model model, HttpServletRequest request) {
		if (template.getCid() != null) {
			Template t = new Template();
			t.setCid(template.getCid());
			template = templateService.get(t);
			SetPreviewsDate(model);
			return getPath(template, request);
		} else {
			return "redirect:" + adminPath + "/cms/templatefile/list";
		}
	}

	public boolean deletefile(Template template) {

		log.info("cid=" + template.getCid());
		Template t = new Template();
		t.setCid(template.getCid());
		Template temp = templateService.get(t);
		log.info("temp=" + temp);
		if (temp != null) {
			log.info("temp.getTemplatepath=" + temp.getTemplatefilename());
			File file = new File(temp.getTemplatepath());
			if (file.delete()) {
				log.info("删除成功");
				templateService.delete(template.getCid());
				return false;
			} else {
				log.info("删除失败");
				return false;
			}
		}

		return false;
	}

	public boolean save(Template template, HttpServletRequest request) {
		java.util.Date date = new java.util.Date();
		template.setModifydate(date);
		Template query = new Template();
		query.setTemplatefilename(template.getTemplatefilename());
		 
		query.setType(template.getType());
		List<Template> list = templateService.findAll(query);
		if (list != null && list.size() > 0) {
			// log.info("已经存在目录");
			return true;
		} else {
			// log.info("save model=" + template.getShow());
			if (template.getShow().equals("mobile")) {
				template.setType("1");
			}
			// String webpath = "/app/www/jeesite/";
			String path =  "/WEB-INF/views/thymeleaf/cms/front/themes/basic/" + template.getTemplatefilename();
			if (template.getType().equals("1")) {
				path =  "/WEB-INF/views/mobile/thymeleaf/cms/front/themes/basic/" + template.getTemplatefilename();
			}
			// log.info("path=" + path);
			template.setTemplatepath(path);
			if("frontIndex.html".equals(template.getTemplatefilename())) {
				 query.setDescription("首页模板");
			 }else if("frontList.html".equals(template.getTemplatefilename())) {
				 query.setDescription("列表页模板");
			 }else  if("frontViewArticle.html".equals(template.getTemplatefilename())) {
				 query.setDescription("文章页模板");
			 }
			if (templateService.save(template) > 0) {
				// log.info("保存成功");
				return true;
			} else {
				// log.info("保存失败");
				return false;
			}
		}
	}

	public boolean update(Template template, HttpServletRequest request) throws IOException {
		java.util.Date date = new java.util.Date();
		template.setModifydate(date);
		Template query = new Template();
		query.setTemplatefilename(template.getTemplatefilename());
		query.setType(template.getType());
		List<Template> list = templateService.findAll(query);
		if (list != null && list.size() > 0) {
			// log.info("已经存在目录");
			for(Template newTemplate:list) {
				File file =new File(newTemplate.getTemplatepath());
				String[] str =newTemplate.getTemplatefilename().split(".");
				File oldFile = new File(str[0]+"_1"+str[1]);
				if(oldFile.exists()) {
					oldFile.delete();
				}
				file.renameTo(new File(str[0]+"_1"+str[1]));
				BufferedWriter bw =new BufferedWriter(new FileWriter(new File(template.getTemplatefilename())));
				bw.write(newTemplate.getContext());
				bw.flush();
				bw.close();
				if (templateService.update(template) != 1) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	
	public String ReadFile(Template template) {
		String context = "";
		String line = "";
		try {
			BufferedReader conf = new BufferedReader(new FileReader(webpath +template.getTemplatepath()));
			System.out.println(template.getTemplatepath());
			while ((line = conf.readLine()) != null) {
				context += line + "\n";
			}
			conf.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 
		return replaceJSPHead(context);
	}

	public String replaceJSPHead(String context) {
		return context.replaceAll("<%@.*?%>", "");

	}

	public String replaceJSPcode(String context) {
		if (context != null && context.length() > 0) {
			context = context.replaceAll("<%@.*?%>", "").replaceAll("<%.*?%>", "");
		}
		return context;
	}

	public void Printfile(Template template, HttpServletRequest request) {
		if (template != null) {
			/*
			 * String defaultInclude =
			 * "<%@ page contentType=\"text/html;charset=UTF-8\"%>\n" +
			 * "<%@ include file=\"/WEB-INF/views/modules/cms/front/include/taglib.jsp\"%>"
			 * ;
			 */
			String defaultInclude = "";
			String newcontext = defaultInclude + replaceJSPcode(Encodes.unescapeHtml(template.getContext()));
			try {
				String filepath = "";
				String webpath = "/app/www/jeesite/" + "WEB-INF/views/";

				if (template.getTemplatepath() != null && template.getTemplatepath().length() > 0) {
					filepath = template.getTemplatepath();
				} else {
					if (template.getType().equals("0")) {
						filepath = webpath + "/WEB-INF/views/modules/cms/front/themes/basic/" + template.getTemplatefilename();

					} else {
						filepath = webpath + "/WEB-INF/views/mobile/modules/cms/front/themes/basic/" + template.getTemplatefilename();

					}
				}
				log.info("filepath=" + filepath);
				File f = new File(filepath);
				if (!f.exists()) {
					log.info("不存在，创建文件");
					if (f.createNewFile()) {
						log.info("创建成功");
					} else {
						log.info("创建失败");
					}
				}

				PrintWriter writer = new PrintWriter(new File(filepath));
				log.info("开始重写文件");
				writer.write(newcontext);
				writer.close();
				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error("写文件失败");
			}
		}
	}

	public String getPath(Template template, HttpServletRequest request) {
		String parent_path = webpath + "/WEB-INF/views/";
		String path = template.getTemplatepath().replace(parent_path, "");
		path = path.substring(0, path.indexOf("."));
		log.info("path=" + path);
		return path;
	}

	public void SetPreviewsDate(Model model) {
		List<Category> categories = categoryService.findAllList();
		if (categories.size() > 0) {
			Category category = categories.get(0);
			model.addAttribute("category", category);
			model.addAttribute("categoryList", categories);
			Site site = siteService.get(category.getSite().getId());
			model.addAttribute("site", site);
		}
		List<Article> articles = articleService.findAllList();
		if (articles.size() > 0) {
			Article article = articles.get(0);
			article.setArticleData(articleDataService.get(article.getId()));
			model.addAttribute("article", article);
			List<Object[]> relationList = articleService.findByIds(articleDataService.get(article.getId()).getRelation());
			model.addAttribute("relationList", relationList);
		}
	}

	@RequestMapping(value = "updateArticle")
	public String updateArticle(Template template, Model model, @RequestParam(value = "show", required = false) String show) {
		if(template.getTemplatefilename().equals("frontViewArticle.html")) {
			articleService.updateNoneStatic();
		}
		if(template.getTemplatefilename().equals("frontList.html")) {
			CmsUpdatePage updatePage = new CmsUpdatePage();
			updatePage.setId("3");
			updatePage.setIsUpdate("1");
			updatePage.setIsMobileUpdate("1");
			updatePage.setType("4");
			updatePage.setState("1");
			updatePage.setUpdateDate(DateUtils.parseDate(DateUtils.getDate()));
			updatePage.setCategoryId("0");
			updatePageService.update(updatePage);
		}
		if(template.getTemplatefilename().equals("frontIndex.html")) {
			CmsUpdatePage updatePage = new CmsUpdatePage();
			updatePage.setId("2");
			updatePage.setIsUpdate("1");
			updatePage.setIsMobileUpdate("1");
			updatePage.setType("3");
			updatePage.setState("1");
			updatePage.setUpdateDate(DateUtils.parseDate(DateUtils.getDate()));
			updatePageService.update(updatePage);
		}
		return "redirect:" + adminPath + "/cms/templatefile/list?show=" + show;
	}
	
	
}

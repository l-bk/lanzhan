package com.modules.Template.web;

import com.common.utils.Encodes;
import com.common.web.BaseController;
import com.modules.Template.common.FileManagerUtil;
import com.modules.Template.entity.FileManager;
import com.modules.Template.service.FileManagerService;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.Site;
import com.modules.cms.service.ArticleDataService;
import com.modules.cms.service.ArticleService;
import com.modules.cms.service.CategoryService;
import com.modules.cms.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by jenwing on 16/7/17.
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/filemanager")
public class FileManagerController extends BaseController {

	private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FileManagerController.class);

//	@Value("${tmplatePath}")
	protected String webpath = "/app/www/tyjs/templets";

	@Autowired
	private FileManagerService fileManagerService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private ArticleDataService articleDataService;
	@Autowired
	private SiteService siteService;

	@ModelAttribute("filemanager")
	public FileManager get(@RequestParam(required = false) Integer id, @RequestParam(required = false) String show) {
		if (id != null) {
			FileManager template = new FileManager();
			template.setCid(id);
			FileManager templateform = fileManagerService.findAll(template).get(0);
			templateform.setShow(show);
			return templateform;
		} else {
			return new FileManager();
		}
	}

	// @RequiresPermissions("cms:templatefile:view")
	@RequestMapping(value = { "list", "" })
	public String list(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam(value = "show", required = false) String show) {
		FileManager query = new FileManager();
		log.info("show=" + show);
		if (show.equals("pc")) {
			query.setType("0");
		} else if (show.equals("mobile")) {
			query.setType("1");
		}
		// log.info("query.type=" + query.getType());
		List<FileManager> list = fileManagerService.findAll(query);
		FileManagerUtil util = new FileManagerUtil(request, show, webpath);
		List<FileManager> filelist = util.getTemplates();
		// log.info("list.size=" + list.size());

		if (list.size() == 0) {
			for (FileManager template : filelist) {
				if (save(template, request)) {
					log.info("save ok");
				} else {
					log.info("save fail");
				}
			}
		} else {
			for (FileManager t : filelist) {
				boolean checkexists = false;
				for (FileManager t1 : list) {
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
		list = fileManagerService.findAll(query);

		log.info("list.size=" + list.size());

		model.addAttribute("list", list);

		return "modules/cms/fileManagerList";
	}

	// @RequiresPermissions("cms:templatefile:edit")
	@RequestMapping(value = "save")
	public String save(FileManager template, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "show", required = false) String show) {
		// System.out.println("++++++测试++++++++"+template.getSortNumber()+"+++++CID+++++"+template.getCid());
		if (template.getCid() != null) {
			// System.out.println("++++++测试2+++++"+template.getSortNumber());
			deletefile(template);
			//fileManagerService.update(template);
			save(template, request);
		} else {
			save(template, request);
		}
		Printfile(template, request);
		return "redirect:" + adminPath + "/cms/filemanager/list?show=" + show;

	}

	@RequestMapping(value = "form")
	public String form(FileManager template, Model model, @RequestParam(value = "show", defaultValue = "pc") String show) {
		if (template.getCid() != null) {
			FileManager t = new FileManager();
			t.setCid(template.getCid());
			log.info("show:" + show);
			template = fileManagerService.get(t);
			template.setShow(show);
			// System.out.println("template=" + template);
			template.setContext(ReadFile(template));
			model.addAttribute("template", template);
			return "modules/cms/fileManagerForm";
		} else {
			model.addAttribute("template", new FileManager());
			return "modules/cms/fileManagerForm";
		}
		// return "redirect:" + adminPath + "/cms/templatefile/list";

	}

	@RequestMapping(value = "delete")
	public String delete(FileManager template, Model model, @RequestParam(value = "show", required = false) String show) {
		deletefile(template);

		return "redirect:" + adminPath + "/cms/filemanager/list?show=" + show;
	}

	@RequestMapping(value = "preview")
	public String preview(FileManager template, Model model, HttpServletRequest request,
			@RequestParam(value = "show", defaultValue = "pc") String show) {
		if (template.getCid() != null) {
			if (request.getParameter("show") != null) {
				if (request.getParameter("show").equals("pc")) {
					return "redirect:/templets/" + template.getTemplatefilename();
				} else {
					return "redirect:/templets/mobile/" + template.getTemplatefilename();
				}
			} else {
				return "redirect:" + adminPath + "/cms/filemanager/list" + show;
			}
		} else {
			return "redirect:" + adminPath + "/cms/filemanager/list" + show;
		}
	}

	public boolean deletefile(FileManager template) {

		log.info("cid=" + template.getCid());
		FileManager t = new FileManager();
		t.setCid(template.getCid());
		FileManager temp = fileManagerService.get(t);
		log.info("temp=" + temp);
		if (temp != null) {
			log.info("temp.getTemplatepath=" + temp.getTemplatefilename());
			File file = new File(temp.getTemplatepath());
			if (file.delete()) {
				log.info("删除成功");
				fileManagerService.delete(template.getCid());
				return false;
			} else {
				log.info("删除失败");
				return false;
			}
		}
		return false;
	}

	public boolean save(FileManager template, HttpServletRequest request) {
		java.util.Date date = new java.util.Date();
		template.setModifydate(date);
		FileManager query = new FileManager();
		query.setTemplatefilename(template.getTemplatefilename());
		query.setType(template.getType());
		List<FileManager> list = fileManagerService.findAll(query);
		if (list != null && list.size() > 0) {
			// log.info("已经存在目录");
			return true;
		} else {
			// log.info("save model=" + template.getShow());
			if (template.getShow().equals("mobile")) {
				template.setType("1");
			}
			// String webpath = "/app/www/jeesite/";
			String path = webpath + "/" + template.getTemplatefilename();
			if (template.getType().equals("1")) {
				path = webpath + "/mobile/" + template.getTemplatefilename();
			}
			// log.info("path=" + path);
			template.setTemplatepath(path);
			if (fileManagerService.save(template) > 0) {
				// log.info("保存成功");
				return true;
			} else {
				// log.info("保存失败");
				return false;
			}
		}
	}

	public String ReadFile(FileManager template) {
		String context = "";
		String line = "";
		try {
			BufferedReader conf = new BufferedReader(new FileReader(template.getTemplatepath()));
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

	public void Printfile(FileManager template, HttpServletRequest request) {
		if (template != null) {
			// String defaultInclude = "<%@ page
			// contentType=\"text/html;charset=UTF-8\"%>\n"
			// + "<%@ include
			// file=\"/WEB-INF/views/modules/cms/front/include/taglib.jsp\"%>";
			String defaultInclude = "";
			String newcontext = defaultInclude + replaceJSPcode(Encodes.unescapeHtml(template.getContext()));
			try {
				String filepath = "";
				//String webpath = "/app/www/jeesite/" + "templets/";

				if (template.getTemplatepath() != null && template.getTemplatepath().length() > 0) {
					filepath = template.getTemplatepath();
				} else {
					if (template.getType().equals("0")) {
						filepath = webpath + "/" + template.getTemplatefilename();

					} else {
						filepath = webpath + "/mobile/" + template.getTemplatefilename();

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

	public String getPath(FileManager template, HttpServletRequest request) {
		// String parent_path = webpath + "/WEB-INF/views/";
		String parent_path = webpath;
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

}

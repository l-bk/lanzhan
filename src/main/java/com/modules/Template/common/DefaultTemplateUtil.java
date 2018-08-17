package com.modules.Template.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.modules.Template.entity.DefaultTemplate;

public class DefaultTemplateUtil {

	public List<DefaultTemplate> getTemplates() {
		return templates;
	}

	public void setTemplates(List<DefaultTemplate> templates) {
		this.templates = templates;
	}

	protected Logger log = Logger.getLogger(DefaultTemplateUtil.class);
	private List<DefaultTemplate> templates = new ArrayList<DefaultTemplate>();

	public DefaultTemplateUtil(HttpServletRequest request, String webpath) {
		readFile(request, webpath);
	}

	private void readFile(HttpServletRequest request, String webpath) {
		templates.add(getDefaultTemplateForDir(webpath, "/templets/88ysg"));
		templates.add(getDefaultTemplateForDir(webpath, "/templets/mobile"));
	}

	private DefaultTemplate getDefaultTemplateForDir(String webpath,
			String dirname) {
		DefaultTemplate template = new DefaultTemplate();
		template.setParentDirname(dirname);
		String path = webpath + dirname;
		log.info("path=" + path);
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			template.setTemplatefilename(file.getName().replace(path, ""));
			template.setTemplatepath(file.getPath());
			log.info("找到" + dirname + "的文件夹");
			File[] filess = file.listFiles();
			for (File tmpfile : filess) {
				if (tmpfile.isFile()) {
					if (checkIsHtmlfile(tmpfile.getName())) {
						DefaultTemplate ctemplate = new DefaultTemplate();
						String suffix = getSuffix(tmpfile.getName());
						ctemplate.setTemplatefilename(tmpfile.getName()
								.replace(path, "").replace("." + suffix, ""));
						ctemplate.setTemplatepath(tmpfile.getPath());
						File parentfile = tmpfile.getParentFile();
						// log.info("parentPath:" + parentfile.getPath());
						// log.info("parentName:" + parentfile.getName());
						ctemplate
								.setModifydate(new Date(tmpfile.lastModified()));
						ctemplate.setParentDirname(parentfile.getName());
						ctemplate.setSuffix(suffix);
						template.getFiles().add(ctemplate);
					}
				}
			}
		}
		return template;
	}

	public static String getSuffix(String name) {
		return name.substring(name.lastIndexOf(".") + 1).toLowerCase();
	}

	public static boolean checkIsHtmlfile(String name) {
		String suffix = getSuffix(name);

		if (suffix.equals("html") || suffix.equals("htm")) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		System.out
				.println(checkIsHtmlfile("/Users/weifeng/work/jeesite/ytsf/src/main/webapp/mobile/article_article.htm"));
	}
}

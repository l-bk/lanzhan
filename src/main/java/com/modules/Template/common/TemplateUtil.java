package com.modules.Template.common;

import com.modules.Template.entity.Template;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class TemplateUtil
{
  protected Logger logger = Logger.getLogger(TemplateUtil.class);

  private List<Template> templates = new ArrayList();

  public TemplateUtil(HttpServletRequest request, String show, String webpath) {
    readFile(request, show, webpath);
  }

  public void readFile(HttpServletRequest request, String show, String webpath)
  {
    String path = webpath + "/WEB-INF/views/thymeleaf/cms/front/themes/basic";
    if (show.equals("moblie")) {
      path = webpath + 
        "/WEB-INF/views/mobile/thymeleaf/cms/front/themes/basic";
    }
    this.logger.info("path=" + path);
    File file = new File(path);
    if (file.exists()) {
      this.logger.info("找到文件");
      File[] filess = file.listFiles();
      for (File tmpfile : filess)
        if (checkIsfrontfile(tmpfile.getName()))
        {
          Template template = new Template();
          template.setTemplatefilename(tmpfile.getName());
          template.setTemplatepath(tmpfile.getPath());
          if (show.equals("pc"))
            template.setType("0");
          else if (show.equals("moblie")) {
            template.setType("1");
          }
          this.templates.add(template);
        }
    }
    else
    {
      System.out.println("找不到文件");
    }
  }

  public List<Template> getTemplates() {
    return this.templates;
  }

  public boolean checkIsfrontfile(String name) {
	 if("frontIndex.html".equals(name) || "frontList.html".equals(name) || "frontViewArticle.html".equals(name)) {
		 
	    Pattern pth = Pattern.compile("front[a-zA-Z0-9_]*[.]html");
	    Matcher mth = pth.matcher(name);
	    if (mth.matches()) {
	      return true;
	    }
	    return false;
	}
	 return false;
  }

  public static void main(String[] args)
  {
  }
}
package com.modules.Template.common;

import com.modules.Template.entity.FileManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jenwing on 16/7/17.
 */
public class FileManagerUtil {
    protected Logger logger = Logger.getLogger(FileManagerUtil.class);

    private List<FileManager> templates = new ArrayList<FileManager>();

    public FileManagerUtil(HttpServletRequest request, String show, String webpath) {
        readFile(request, show, webpath);
    }

    FileManagerUtil(){

    }

    public void readFile(HttpServletRequest request, String show, String webpath) {

        // logger.info("读取模板:" + show);

        String path = webpath + "/templets/88ysg/";
        if (show.equals("mobile")) {
            path = webpath
                    + "/templets/mobile/";
        }
        logger.info("path=" + path);
        File file = new File(path);
        if (file.exists()) {
            logger.info("找到文件");
            File[] filess = file.listFiles();
            for (File tmpfile : filess) {
//                if (checkIsfrontfile(tmpfile.getName())) {
                    //System.out.println("name=" + tmpfile.getName());
                    FileManager template = new FileManager();
                    template.setTemplatefilename(tmpfile.getName());
                    template.setTemplatepath(tmpfile.getPath());
                    if (show.equals("pc")) {
                        template.setType("0");
                    } else if (show.equals("mobile")) {
                        template.setType("1");
                    }
                    templates.add(template);
                //}
            }

        } else {
            System.out.println("找不到文件");
        }
    }

    public List<FileManager> getTemplates() {
        return templates;
    }

    public boolean checkIsfrontfile(String name) {
        Pattern pth = Pattern.compile("front[a-zA-Z0-9_]*[.][jsphtmlxs]*");
        Matcher mth = pth.matcher(name);
       // logger.info("name:"+name+"  check:"+mth.matches());
        if (mth.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        // new TemplateUtil().readFile();
        FileManagerUtil file=new FileManagerUtil();
        System.out.print(file.checkIsfrontfile("article_article.htm"));
    }
}

package com.modules.site.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.modules.cms.dao.ArticleDao;
import com.modules.cms.dao.ArticleDataDao;
import com.modules.cms.dao.CategoryDao;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.Site;
import com.modules.msgsource.entity.Msgsource;
import com.modules.sys.entity.Office;


@Service
public class SiteUtil {
	@Value("${siteheadPath}")
	protected String siteheadpath;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private ArticleDataDao articleDataDao;
	@Autowired
	private CategoryDao categoryDao;

	@Scheduled(cron = "0 0/30 * * * ?")
	public void SiteUtilrun() {
		String reURL = siteheadpath+"WEB-INF/views/modules/site/sitehead.html";
		String wrURL = siteheadpath+"static/site/sitedemo.html";
		try {
			System.out.println("into Init Site");
			System.out.println("siteheadpath=" + siteheadpath);
			// File file = new
			// File("/Users/linsikai/gitwork/ytsf/target/jeesite/WEB-INF/views/modules/site/sitehead.html");
			// File file2 = new File(
			// "/Users/linsikai/gitwork/ytsf/target/jeesite/site/sitedemo.html");
			System.out.println("开始生成网站地图.....");
			try {
				BufferedReader conf = new BufferedReader(new FileReader(reURL));
				String line = "";
				String str = "";
				while ((line = conf.readLine()) != null) {
					str += line;
				}
				// System.out.println("str="+str);
				conf.close();

				// str = str.replace("[内容]", getBodyContent());
				str = str + getBodyContent();
				PrintWriter writer = new PrintWriter(new File(wrURL));

				writer.write(str);
				writer.close();
				writer.flush();
				System.out.println("生成网站地图完成.....");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("有bug===");
		}
	}

	public String getBodyContent() {
		String content = "";
		StringBuffer buff = new StringBuffer();

		// 测试代码start
		/**
		 * List<Category> list=new ArrayList(); for(Integer i=0;i<10;i++){
		 * Category category=new Category(); category.setName(i.toString());
		 * list.add(category); } List<Article> articleList=new ArrayList();
		 * for(Integer i=0;i<10;i++){ Article article=new Article();
		 * article.setTitle(i.toString()); articleList.add(article); }
		 **/
		// 测试代码end
		// buff.append("<body>");
		/**
		 * if (list != null && list.size() > 0) { for (Category category : list)
		 * {
		 * 
		 * buff.append(category.getName() + " "); } for (Category category :
		 * list) { buff.append(category.getName() + " "); Article article = new
		 * Article(); article.setCategory(category); //List<Article> articleList
		 * = articleDao.findAllList(article); if (articleList != null &&
		 * articleList.size() > 0) { for (Article ar : articleList) {
		 * buff.append(ar.getTitle() + " "); } }
		 * 
		 * } }
		 **/
		// User user = UserUtils.getUser();
		Category category = new Category();
		category.setOffice(new Office());
		// category.getSqlMap().put("dsf", dataScopeFilter(user, "o", "u"));
		category.setSite(new Site());
		category.setParent(new Category());
		List<Category> list = categoryDao.findList(category);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Category category2 = list.get(i);
				buff.append("<a href=\"" + category2.getUrl() + "\">"
						+ category2.getName() + "</a>    ");
			}
			buff.append("</div>");
			for (int n = 0; n < list.size(); n++) {
				Category category3 = list.get(n);
				Article article = new Article();
				article.setCategory(category3);
				if(article.getMsgsource() == null){
					article.setMsgsource(new Msgsource());
				}
				List<Article> articlelist = articleDao.findList(article);
				buff.append("<div class=\"article\">");
				buff.append("<h2><a href=\"" + category3.getUrl() + "\">"
						+ category3.getName() + "</a></h2>\n");
				if (articlelist != null && articlelist.size() > 0) {
					for (int m = 0; m < articlelist.size(); m++) {
						Article article2 = articlelist.get(m);
						buff.append("<a href=\"" + article2.getUrl() + "\">"
								+ article2.getTitle() + "</a>    ");
					}
				}
				buff.append("</div>");
			}
		}
		buff.append("</div></body></html>");

		content = buff.toString();

		return content;

	}

	public static void main(String[] args) {
		SiteUtil uitl = new SiteUtil();
		uitl.SiteUtilrun();
	}

}

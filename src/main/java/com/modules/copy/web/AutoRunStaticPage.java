package com.modules.copy.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.common.utils.DateUtils;
import com.common.utils.Encodes;
import com.modules.cms.dao.ArticleDao;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.CmsUpdatePage;
import com.modules.cms.service.CategoryService;
import com.modules.cms.service.CmsUpdatePageService;

@Service
public class AutoRunStaticPage {
	protected Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CmsUpdatePageService updatePageService;

	@Value("${gwebpath}")
	protected String webpath;
	// protected String host = "http://www.60mil.com/";
	@Value("${ghost}")
	protected String host;

	@Value("${webhost}")
	protected String webhost = "";
	protected String badUrl = "";

	@Value("${webpath}")
	protected String indexpath = "";

	/**
	 * 
	 * 
	 */
	public static void sop(String str) {
		System.out.println(str);
	}

	public static int getSubCount_2(String str, String key) {
		int count = 0;
		int index = 0;
		while ((index = str.indexOf(key, index)) != -1) {
			sop("index=" + index);
			index = index + key.length();

			count++;
		}
		return count;
	}

	public void articlePublic(String flag) {
		Article article = new Article();
		article.setIsStatis(flag);
		List<Article> articleList = articleDao.findAllList(article);
		staticize(articleList);
	}

	public Article manualStatic(String id) {
		Article article = articleDao.get(id);
		if (article.getIsStatis().equals("0")) {
			List<Article> articleList = new ArrayList<Article>();
			articleList.add(article);
			staticize(articleList);
		}
		return articleDao.get(id);
	}

	public void staticize(List<Article> articleList) {
		for (int i = 0; i < articleList.size(); i++) {
			Article tmp = articleList.get(i);
			StringBuffer contextbuffer = getUrlContext(
					host + tmp.getCategory().getNameEn() + "/" + tmp.getId() + ".shtml", "UTF-8");
			if (StringUtils.isNotEmpty(contextbuffer)) {
				try {

					File file1 = new File(webpath + "a/" + tmp.getCategory().getNameEn());
					if (!file1.isDirectory()) {
						file1.mkdir();
					}
					File file = new File(webpath + "a/" + tmp.getCategory().getNameEn() + "/" + tmp.getId() + ".shtml");
					if (!file.exists()) {
						file.createNewFile();
					}
					FileOutputStream out = new FileOutputStream(file, false); // 如果追加方式用true
					out.write(contextbuffer.toString().getBytes("utf-8"));// 注意需要转换对应的字符集
					out.close();
				} catch (IOException ex) {
					System.out.println(ex.getStackTrace());
				}

				String newStr = Encodes.escapeHtml(contextbuffer.toString());
				String reg = "<ul class=\"pagelist\">" + "(.*?)" + "</ul>";
				reg = Encodes.escapeHtml(reg);

				Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				Matcher mth = pattern.matcher(newStr.toString());
				int count = 0;
				while (mth.find()) {
					String rpStr = mth.group(1);
					reg = "<a>共<span>" + "(.*?)" + "</span>页";
					reg = Encodes.escapeHtml(reg);
					pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
					mth = pattern.matcher(newStr.toString());
					while (mth.find()) {
						count = Integer.valueOf(mth.group(1));
					}

				}
				for (int j = 0; j < count; j++) {
					contextbuffer = getUrlContext(host + tmp.getCategory().getNameEn() + "/" + tmp.getId() + "_"
							+ String.valueOf(j + 1) + ".shtml", "UTF-8");

					try {

						File file1 = new File(webpath + "a/" + tmp.getCategory().getNameEn());
						if (!file1.isDirectory()) {
							file1.mkdir();
						}
						File file = new File(webpath + "a/" + tmp.getCategory().getNameEn() + "/" + tmp.getId() + "_"
								+ String.valueOf(j + 1) + ".shtml");
						if (!file.exists()) {
							file.createNewFile();
						}
						FileOutputStream out = new FileOutputStream(file, false); // 如果追加方式用true
						out.write(contextbuffer.toString().getBytes("utf-8"));// 注意需要转换对应的字符集
						out.close();
					} catch (IOException ex) {
						System.out.println(ex.getStackTrace());
					}

				}

				///////////////////////////////
				Article acticleData = articleDao.get(tmp.getId());
				acticleData.setIsStatis("1");
				acticleData.setStaticUrl(webhost + "a/" + tmp.getCategory().getNameEn() + "/" + tmp.getId() + ".shtml");
				articleDao.updateStaticPage(acticleData);

			}

			/*
			 * CmsUpdatePage updatePage = new CmsUpdatePage();
			 * updatePage.setCategoryId(acticleData.getCategory() != null ?
			 * acticleData.getCategory().getId() : null); updatePage.setId("3");
			 * listPublic(updatePage);
			 */
		}
	}

	public void runCopy() {
		log.info("runCopy");
		articlePublic("0");

		/*
		 * CmsUpdatePage updatePage = updatePageService.get("1");
		 * if(updatePage.getIsUpdate().equals("1") &&
		 * updatePage.getType().equals("1")) { updatePage.setState("2");
		 * updatePageService.update(updatePage);
		 * 
		 * articlePublic("1"); indexPublic(updatePage); listPublic(updatePage);
		 * 
		 * updatePage.setState("3"); updatePage.setIsUpdate("0");
		 * updatePage.setUpdateDate(DateUtils.parseDate(DateUtils.getDateTime())
		 * ); updatePageService.update(updatePage); }
		 */
	}

	public void runCopyActicle() {
		log.info("runCopyActicle");
		articlePublic("1");
	}

	/*
	 * public void runCopyIndex() { log.info("Scheduled"); List<Category>
	 * cateList = categoryService.findAllList(); for (int i = 0; i <
	 * cateList.size(); i++) { Category tmp = cateList.get(i); for (int j = 0; j
	 * < 50; j++) { StringBuffer contextbuffer = getUrlContext(host +
	 * tmp.getNameEn() + "/list" + String.valueOf(j + 1) + ".shtml", "UTF-8");
	 * 
	 * try {
	 * 
	 * File file1 = new File(webpath + "a/" + tmp.getNameEn()); if
	 * (!file1.isDirectory()) { file1.mkdir(); } File file = new File(webpath +
	 * "a/" + tmp.getNameEn() + "/list" + String.valueOf(j + 1) + ".shtml"); if
	 * (!file.exists()) { file.createNewFile(); } FileOutputStream out = new
	 * FileOutputStream(file, false); // 如果追加方式用true
	 * out.write(contextbuffer.toString().getBytes("utf-8"));// 注意需要转换对应的字符集
	 * out.close(); } catch (IOException ex) {
	 * System.out.println(ex.getStackTrace()); }
	 * 
	 * }
	 * 
	 * } // 生成首页
	 * 
	 * StringBuffer contextbuffer = getUrlContext(host + "jsindex", "UTF-8");
	 * 
	 * try {
	 * 
	 * File file = new File(indexpath + "a/" + "/index.shtml"); if
	 * (!file.exists()) { file.createNewFile(); } FileOutputStream out = new
	 * FileOutputStream(file, false); // 如果追加方式用true
	 * out.write(contextbuffer.toString().getBytes("utf-8"));// 注意需要转换对应的字符集
	 * out.close(); } catch (IOException ex) {
	 * System.out.println(ex.getStackTrace()); }
	 * 
	 * }
	 */

	// 更新首页
	public void indexPublic(CmsUpdatePage updatePage) {
		updatePage.setState("2");
		updatePageService.update(updatePage);

		commonStaticFile("jsindex", "index.shtml");

		commonStaticFile("twj/getTjw?size=4&positionId=27&view=frontList-tjw1", "frontList-tjw1.shtml");
		commonStaticFile("twj/getBangTjw?view=frontList-tjw2", "frontList-tjw2.shtml");
		commonStaticFile("twj/getTjw?size=8&positionId=30&view=frontList-tjw3", "frontList-tjw3.shtml");
		commonStaticFile("twj/getTjw?size=8&positionId=31&view=frontList-tjw4", "frontList-tjw4.shtml");

		commonStaticFile("twj/getJcTjw?view=frontViewArticle-tjw5", "frontViewArticle-tjw5.shtml");
		commonStaticFile("twj/getTjw?size=6&positionId=26&view=frontViewArticle-tjw6", "frontViewArticle-tjw6.shtml");

		commonStaticFile("twj/getTjw?size=4&positionId=27&view=frontViewArticle-tjw1", "frontViewArticle-tjw1.shtml");
		commonStaticFile("twj/getBangTjw?view=frontViewArticle-tjw2", "frontViewArticle-tjw2.shtml");
		commonStaticFile("twj/getTjw?size=8&positionId=30&view=frontViewArticle-tjw3", "frontViewArticle-tjw3.shtml");
		commonStaticFile("twj/getTjw?size=8&positionId=31&view=frontViewArticle-tjw4", "frontViewArticle-tjw4.shtml");


		updatePage.setState("3");
		updatePage.setIsMobileUpdate("0");
		;
		updatePage.setUpdateDate(DateUtils.parseDate(DateUtils.getDateTime()));
		updatePageService.update(updatePage);

		/*
		 * StringBuffer contextbuffer = getUrlContext(host + "jsindex",
		 * "UTF-8"); if (StringUtils.isNotEmpty(contextbuffer)) { try { File
		 * file = new File(indexpath + "/index.shtml"); if (!file.exists()) {
		 * file.createNewFile(); } FileOutputStream out = new
		 * FileOutputStream(file, false); // 如果追加方式用true
		 * out.write(contextbuffer.toString().getBytes("utf-8"));// 注意需要转换对应的字符集
		 * out.close(); } catch (IOException ex) {
		 * System.out.println(ex.getStackTrace()); } }
		 */
	}

	// 更新首页
	public void commonStaticFile(String srcPath, String distPath) {
		log.info("###############################" + host + srcPath);
		StringBuffer contextbuffer = getUrlContext(host + srcPath, "UTF-8");
		if (StringUtils.isNotEmpty(contextbuffer)) {
			try {
				System.out.println(indexpath + "/" + distPath);
				File file = new File(indexpath + "/" + distPath);
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream out = new FileOutputStream(file, false); // 如果追加方式用true
				out.write(contextbuffer.toString().getBytes("utf-8"));// 注意需要转换对应的字符集
				out.close();
			} catch (IOException ex) {
				System.out.println(ex.getStackTrace());
			}

		}

	}

	// 更新列表页
	public void listPublic(CmsUpdatePage updatePage) {
		updatePage.setState("2");
		updatePageService.update(updatePage);
		List<Category> cateList = new ArrayList<Category>();
		if (!StringUtils.isBlank(updatePage.getCategoryId()) && !updatePage.getCategoryId().equals("0")) {
			String categoryId = updatePage.getCategoryId();
			Category category = categoryService.get(categoryId);
			cateList.add(category);
		} else if (StringUtils.isBlank(updatePage.getCategoryId()) || updatePage.getCategoryId().equals("0")) {
			cateList = categoryService.findAllList();
		}
		for (int i = 0; i < cateList.size(); i++) {
			Category tmp = cateList.get(i);
			for (int j = 0; j < 50; j++) {
				StringBuffer contextbuffer = getUrlContext(
						host + tmp.getNameEn() + "/list" + String.valueOf(j + 1) + ".shtml", "UTF-8");
				if (StringUtils.isNotEmpty(contextbuffer)) {
					try {

						File file1 = new File(webpath + "a/" + tmp.getNameEn());
						if (!file1.isDirectory()) {
							file1.mkdir();
						}
						File file = new File(
								webpath + "a/" + tmp.getNameEn() + "/list" + String.valueOf(j + 1) + ".shtml");
						if (!file.exists()) {
							file.createNewFile();
						}
						FileOutputStream out = new FileOutputStream(file, false); // 如果追加方式用true
						out.write(contextbuffer.toString().getBytes("utf-8"));// 注意需要转换对应的字符集
						out.close();
					} catch (IOException ex) {
						System.out.println(ex.getStackTrace());
					}
				}

			}
		}
		updatePage.setIsUpdate("0");
		updatePage.setState("3");
		updatePage.setUpdateDate(DateUtils.parseDate(DateUtils.getDateTime()));
		updatePageService.update(updatePage);
	}

	public void runCopyIndex() {
		log.info("runCopyIndex");
		CmsUpdatePage updatePage = updatePageService.get("2");
		if (updatePage.getIsUpdate().equals("1")) {
			indexPublic(updatePage);
		}
	}

	public void runCopyList() {
		log.info("runCopyList");
		CmsUpdatePage updatePage = updatePageService.get("3");
		if (updatePage.getIsUpdate().equals("1")) {
			listPublic(updatePage);
		}
	}

	public void outputHtml() {

	}

	private StringBuffer getUrlContext(String url, String lang) {
		StringBuffer buffer = new StringBuffer("");
		if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(lang)) {
			if (!badUrl.contains(url)) {
				// log.info("url=" + url);
				try {
					HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
					urlConnection.setRequestProperty("User-Agent",
							"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
					if (urlConnection != null) {
						// 设置超时时间 60秒
						urlConnection.setConnectTimeout(10000);
						urlConnection.setRequestMethod("GET");
						InputStream input = urlConnection.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(input, lang));
						String line = null;
						while ((line = reader.readLine()) != null) {
							buffer.append(line).append("\r\n");
						}
						if (reader != null) {
							reader.close();
						}
						if (urlConnection != null) {
							urlConnection.disconnect();
						}

					} else {
						log.error("打开网站异常,url:" + url);
					}
				} catch (IOException e) {
					e.printStackTrace();
					log.error("打开网站异常,url:" + url);
				}
			}
		}
		// log.info("buffer=" + buffer);
		return buffer;

	}

	public static void main(String args[]) {
		StringBuffer t = new StringBuffer("");
		if (StringUtils.isNotEmpty(t)) {
			System.out.println("1");
		} else {
			System.out.println("2");
		}

	}
}

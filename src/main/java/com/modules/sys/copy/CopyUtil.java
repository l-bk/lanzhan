package com.modules.sys.copy;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.common.utils.Encodes;
import com.modules.cms.entity.Article;
import com.modules.cms.service.CategoryService;
import com.modules.copy.entity.Contextnodedefine;
import com.modules.msgsource.entity.Msgsource;

import net.coobird.thumbnailator.Thumbnails;

public class CopyUtil {

	private static Logger log = Logger.getLogger(CopyUtil.class);

	private List<String> urllist = new ArrayList<String>();

	private List<String> miniimglist = new ArrayList<String>();

	private List<String> shorttitlelist = new ArrayList<String>();

	private Map<String, String> metaList = new LinkedHashMap<String, String>();

	private StringBuffer contextbuffer = new StringBuffer();

	private String url;

	private String lang;

	private Integer count = 0;

	private Integer imgcount = 0;

	private Integer shortcount = 0;

	private List<Article> articles = new ArrayList<Article>();

	private Integer pagesize = 500;

	private Integer currpageindex = 0;

	private static String webpath;

	private static List<String> badUrl = new ArrayList<String>();

	private static Integer poolsize = 5;// 每一个线程处理10条请求

	public List<String> getUrllist() {
		return urllist;
	}

	public void setUrllist(List<String> urllist) {
		this.urllist = urllist;
	}

	public List<String> getMiniimglist() {
		return miniimglist;
	}

	public void setMiniimglist(List<String> miniimglist) {
		this.miniimglist = miniimglist;
	}

	public List<String> getShorttitlelist() {
		return shorttitlelist;
	}

	public void setShorttitlelist(List<String> shorttitlelist) {
		this.shorttitlelist = shorttitlelist;
	}

	/**
	 * 节点点的模型
	 * 
	 * @param contextnodedefine
	 * @param categoryService
	 */
	public CopyUtil() {
		badUrl.add("http://www.88ysg.com/news/565_2.html");
	}

	public CopyUtil(Contextnodedefine contextnodedefine, String webpath, CategoryService categoryService) {
		this.webpath = webpath;
		badUrl.add("http://www.88ysg.com/news/565_2.html");
		InitCpoy(contextnodedefine, categoryService);
	}

	public void InitCpoy(Contextnodedefine contextnodedefine, CategoryService categoryService) {
		if (contextnodedefine != null) {
			if (contextnodedefine.getCopyUrl() != null && contextnodedefine.getCopyUrl().length() > 0) {
				log.info("开始采集,url=" + contextnodedefine.getCopyUrl() + ",lang=" + contextnodedefine.getContextdefine().getLang());

				this.contextbuffer = getUrlContext(contextnodedefine.getCopyUrl(), contextnodedefine.getContextdefine().getLang());
				StringBuffer listbuffer = new StringBuffer(InitListUrlParent(this.contextbuffer,
						appendBegin2End(contextnodedefine.getListRegBegin(), contextnodedefine.getListRegEnd())));
				
				// 是否规定基准url
				String baseHref = "";
				if (contextnodedefine.getIsBaseHref().equals("1")) {
					baseHref = getBaseHref(this.contextbuffer, contextnodedefine);
				}
				contextnodedefine.setBaseHref(baseHref);

				InitListUrl(contextnodedefine.getContextdefine().getUrl(), listbuffer, replace2reg(contextnodedefine.getCopyUrlReg()),
						contextnodedefine.getCopyCount(), contextnodedefine);
				if (contextnodedefine.getIsSplitPage().equals("1")) {
					getNextPageListUrl(this.contextbuffer, contextnodedefine);
				}

				log.info("urllist.size=" + urllist.size());
				log.info("miniimglist.size=" + miniimglist.size());
				log.info("shorttitlelist.size=" + shorttitlelist.size());

				ExecutorService pool = Executors.newFixedThreadPool(poolsize);
				List<Callable> callables = new ArrayList<Callable>();
				// Callable cList[] = new Callable[poolsize];
				int split = urllist.size() % poolsize > 0 ? urllist.size() / poolsize + 1 : urllist.size() / poolsize;
				log.info("split:" + split);
				for (int j = 0; j <= split; j++) {
					int start = j * poolsize > urllist.size() ? urllist.size() : j * poolsize;
					int end = (j + 1) * poolsize > urllist.size() ? urllist.size() : (j + 1) * poolsize;
					log.info("start:" + start + " end:" + end);
					List<String> urlthreadList = urllist.subList(start, end);
					callables.add(new CopyUrlCall(urlthreadList, categoryService, contextnodedefine));
				}
				List<Future<List<Article>>> futures = new ArrayList<Future<List<Article>>>();
				for (int i = 0; i < callables.size(); i++) {
					log.info("cList[" + i + "]:" + callables.get(i));
					if (callables.get(i) != null) {
						futures.add(pool.submit(callables.get(i)));

					} else {
						continue;
					}
				}
				for (Future<List<Article>> future : futures) {
					try {
						articles.addAll(future.get());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				pool.shutdown();
			}
			log.info("finsh");
		}
	}
	/**
	 * 获取基准url
	 * @param contextbuffer2
	 * @param contextnodedefine
	 * @return
	 */
	private String getBaseHref(StringBuffer contextbuffer2, Contextnodedefine contextnodedefine) {
			String baseHrefReg = replace2reg(contextnodedefine.getBaseHrefReg());
			log.info("匹配的表达式：" + contextnodedefine.getBaseHrefReg());
			log.info("匹配规则：" + baseHrefReg);
			String base_href = "";
			Pattern pattern = Pattern.compile(baseHrefReg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher mth = pattern.matcher(contextbuffer2.toString());
			while (mth.find()) {
				base_href = mth.group(1);
				break;
			}
			log.info("base_href=" + base_href);
			return base_href;
	}

	public void full() {
		this.articles = new ArrayList<Article>();
		this.contextbuffer = new StringBuffer();
		// this.next_pagecontextbuffer = new StringBuffer();
		this.urllist = new ArrayList<String>();
		this.lang = "";
		this.url = "";
		this.count = 0;
		this.shortcount = 0;
		this.imgcount = 0;
		this.currpageindex = 0;
		log.warn("采集:" + url + " 时间:" + fomatDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));

	}

	public String appendBegin2End(String begin, String end) {
		String reg = begin + "(.*?)" + end;

		return replace2reg(reg);
	}

	public String replace2reg(String str) {
		// log.info("str=" + str);
		return Encodes.unescapeHtml(new CopyStringUtil(str).getReplacereg());

	}

	public void getNextPageListUrl(StringBuffer buffer, Contextnodedefine contextnodedefine) {
		log.info("this.pagesize=" + this.pagesize + "     this.currpageinde=" + this.currpageindex);
		if (this.count < contextnodedefine.getCopyCount()) {
			this.currpageindex++;
			log.info("找到下一页");

			String spiltreg = replace2reg(contextnodedefine.getSplitPageNewReg());
			log.info("匹配的表达式：" + contextnodedefine.getSplitPageNewReg());
			log.info("匹配规则：" + spiltreg);
			String next_page_url = contextnodedefine.getBaseHref() + defaultPageCopy(buffer, spiltreg);
			log.info("next_page_url=" + next_page_url);
			StringBuffer next_pagecontextbuffer = new StringBuffer();
			if (next_page_url != null && next_page_url.length() > 0 && !next_page_url.equals("#")) {
				if (!(next_page_url.indexOf("http") > -1 || next_page_url.indexOf("https") > -1 || next_page_url.indexOf("./") > -1)) {
					next_page_url = contextnodedefine.getCopyUrl() + next_page_url;
				}else if(next_page_url.indexOf("./") > -1) { // 相对路径，当前目录
					int n = contextnodedefine.getCopyUrl().lastIndexOf("/");
					next_page_url = contextnodedefine.getCopyUrl().substring(0,n) + next_page_url.substring(1);
				}
				// log.info(defaultCopy(buffer,
				// contextnodedefine.getSplitPageNewReg(), ""));
				next_pagecontextbuffer = getUrlContext(next_page_url, contextnodedefine.getContextdefine().getLang());
				InitListUrl(contextnodedefine.getContextdefine().getUrl(),
						new StringBuffer(InitListUrlParent(next_pagecontextbuffer,
								appendBegin2End(contextnodedefine.getListRegBegin(), contextnodedefine.getListRegEnd()))),
						replace2reg(contextnodedefine.getCopyUrlReg()), contextnodedefine.getCopyCount(), contextnodedefine);
				if (next_pagecontextbuffer != null && next_pagecontextbuffer.length() > 0) {
					// log.info("递归");
					if (this.currpageindex < this.pagesize) {
						getNextPageListUrl(next_pagecontextbuffer, contextnodedefine);
					}
				}

			} else {
				// log.info("非公网标识");
			}
		} else {
			// log.info("超出指定条数");
		}

	}

	public String getNextContextContext(StringBuffer buffer, Contextnodedefine contextnodedefine, String pre_url, String res) {

		if (contextnodedefine.getIscontextnextpage().equals("1")) {

			return nextPageContent(buffer, contextnodedefine, pre_url, res);
		}
		return res;
	}

	public String nextPageContent(StringBuffer buffer, Contextnodedefine contextnodedefine, String cur_url, String res) {

		String next_url = checkContextHasNextPage(buffer, contextnodedefine, cur_url);
		if (next_url != null && next_url.length() > 0 && !cur_url.equals(next_url)) {
			log.info("next_url:" + next_url);
			StringBuffer nextbuffer = getUrlContext(next_url, contextnodedefine.getContextdefine().getContentLang());

			res += "<hr style=\"page-break-after:always;\" class=\"ke-pagebreak\" />" + getMainText(nextbuffer,
					appendBegin2End(contextnodedefine.getMainContextRegBegin(), contextnodedefine.getMainContextRegEnd()),
					contextnodedefine.getContextdefine().getUrl(), next_url, contextnodedefine).trim().replaceAll("</div>$", "");

			return nextPageContent(nextbuffer, contextnodedefine, next_url, res);
		} else {
			log.info("已经没有下一页");
			return checkImg(res, contextnodedefine.getContextdefine().getUrl(), cur_url, contextnodedefine);
		}
	}

	public String checkContextHasNextPage(StringBuffer buffer, Contextnodedefine contextnodedefine, String cur_url) {
		String spiltreg = replace2reg(contextnodedefine.getContextnextreg());

		// log.info("spiltreg=" + spiltreg);
		String next_page_url = defaultPageCopy(buffer, spiltreg);
		// log.info("next_page_url=" + next_page_url);

		if (next_page_url != null && next_page_url.length() > 0 && !next_page_url.equals("#")) {
			if (next_page_url.indexOf("http") > -1 || next_page_url.indexOf("https") > -1) {
			} else {
				if (!Pattern.compile("^/").matcher(next_page_url).matches() && contextnodedefine.getBaseHref() == "") {
					// log.info(domain
					// + mth.group(1).replaceAll("^/", ""));
					String url1 = cur_url.substring(0, cur_url.lastIndexOf("/") + 1);
					next_page_url = url1 + next_page_url;
				} else if(contextnodedefine.getBaseHref() != "") {
					next_page_url = contextnodedefine.getBaseHref() + next_page_url;
				} else {
					next_page_url = contextnodedefine.getContextdefine().getUrl() + "/" + next_page_url;
				}
			}
			return next_page_url;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @return
	 */

	public List<Article> getArticles() {
		return articles;
	}

	CopyUtil(String url, String lang) {
		this.url = url;
		this.lang = lang;
	}

	public StringBuffer getUrlContext() {
		return getUrlContext(this.url, this.lang);
	}

	private StringBuffer getUrlContext(String url, String lang) {
		StringBuffer buffer = new StringBuffer("");
		if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(lang)) {
			if (!badUrl.contains(url)) {
				// log.info("url=" + url);
				try {
					HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
					urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
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

	public static String defaultCopy(StringBuffer context, String reg, String replacetext) {
		String text = "";
		Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher mth = pattern.matcher(context.toString());
		while (mth.find()) {
			text = mth.group(1);
			break;
		}
		if (!replacetext.isEmpty()) {
			String.format(replacetext, text);
		}
		return text;

	}

	private String defaultPageCopy(StringBuffer context, String reg) {
		String text = "";
		Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher mth = pattern.matcher(context.toString());
		while (mth.find()) {
			text = mth.group(1);
			break;
		}
		return text;
	}

	public String simpletitle(StringBuffer context) {
		String text = defaultCopy(context, "<title>(.*?)</title>", "");
		if (!text.isEmpty()) {
			return text;
		}
		return "";
	}

	private String simpledescription() {
		return metaList.get("keywords");

	}

	public void getMeteList(StringBuffer context) {
		Pattern pattern = Pattern.compile("<meta.*?name=[\"\'](.*?)[\"\'].*?content=[\"\'](.*?)[\"\']",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher mth = pattern.matcher(context.toString());

		while (mth.find()) {
			// log.info("find");
			// log.info(mth.groupCount());
			metaList.put(mth.group(1).toLowerCase(), mth.group(2));
			// log.info(mth.group(1));
			// log.info(mth.group(2));
		}
	}

	/**
	 *  
	 */
	private void InitListUrl(String domain, StringBuffer context, String reg, int count, Contextnodedefine contextnodedefine) {

		// log.info("初始化列表");
		// log.info("context=" + context);

		// log.info("reg=" + reg);
		Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		Matcher mth = pattern.matcher(context);
		while (mth.find()) {
			// log.info("找到匹配的列表url");
			if (this.count <= count && mth.groupCount() == 1) {
				log.info("list url=" + mth.group(1));
				if (mth.group(1).indexOf("http") > -1 || mth.group(1).indexOf("https") > -1 || contextnodedefine.getBaseHref() != "") {
					getUrllist().add(contextnodedefine.getBaseHref() + mth.group(1));
				} else {

					if (!Pattern.compile("^/").matcher(mth.group(1)).matches()) {
						// log.info(domain
						// + mth.group(1).replaceAll("^/", ""));
						if (mth.group(1).startsWith("./")) {
							getUrllist().add(domain + mth.group(1).replace("./", ""));
						} else {
							getUrllist().add(domain + mth.group(1));
						}
					} else {
						getUrllist().add(domain + "/" + mth.group(1));

					}
				}
				this.count++;
			} else {
				log.info("已经超出指定条数");
				break;
			}

		}
		log.info("this.count=" + this.count + "  count=" + count);
		if (contextnodedefine.getIshaspic().equals("1")) {
			try {
				InitMinimgListUrl(contextnodedefine.getContextdefine().getUrl(), context, replace2reg(contextnodedefine.getPicreg()),
						contextnodedefine.getCopyCount(), contextnodedefine.getBaseHref());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (contextnodedefine.getIsContextSimpleShortTitle().equals("0")) {
			InitShortTitle(context,
					appendBegin2End(contextnodedefine.getContextShortTitleRegBegin(), contextnodedefine.getContextShortTitleRegEnd()),
					count);
		}

	}

	public void InitMinimgListUrl(String domain, StringBuffer context, String reg, int count, String baseHref) throws IOException {
		// log.info("初始化缩略图列表");
		// log.info("context=" + context);

		// log.info("reg=" + reg);
		Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		Matcher mth = pattern.matcher(context);
		while (mth.find()) {

			if (this.imgcount <= count) {
				log.info("找到匹配的缩略图列表:" + mth.group(1));
				if (mth.group(1).indexOf("http") > -1 || mth.group(1).indexOf("https") > -1 || baseHref != "") {

					getMiniimglist().add(downimg(baseHref + mth.group(1), "/imgdown/min"));
				} else {
					if (!Pattern.compile("^/").matcher(mth.group(1)).matches()) {
						// log.info(domain
						// + mth.group(1).replaceAll("^/", ""));
						// log.info("down 1:" + domain + mth.group(1));
						getMiniimglist().add(downimg(domain + mth.group(1), "/imgdown/min"));
					} else {
						// log.info("down 2:" + domain + mth.group(1));
						getMiniimglist().add(downimg(domain + "/" + mth.group(1), "/imgdown/min"));

					}
				}
				this.imgcount++;
			} else {
				log.info("已经超出指定条数");
				break;
			}

		}
		log.info("this.imgcount=" + this.imgcount + "  count=" + count);
	}

	public void InitShortTitle(StringBuffer context, String reg, int count) {
		String text = "";
		Pattern pattern = Pattern.compile(reg, Pattern.DOTALL);
		// log.info("context=" + context);
		Matcher mth = pattern.matcher(context.toString());
		// log.info(mth.matches());
		while (mth.find()) {
			if (shortcount <= count) {
				text = mth.group(1);
				// System.out.println("h2:" + text);
				getShorttitlelist().add(text);
				shortcount++;
			} else {
				// log.info("已经超出指定条数");
				break;
			}
		}

	}

	public Msgsource InitMsgsource(StringBuffer context, Contextnodedefine contextnodedefine) {
		Msgsource msgsource = new Msgsource();
		String msgListText = "";
		//System.out.println(context);
		Pattern pattern = Pattern.compile(appendBegin2End(contextnodedefine.getMsgsourceBegin(), contextnodedefine.getMsgsourceEnd()),
				Pattern.DOTALL);
		Matcher mth = pattern.matcher(context.toString());
		msgsource.setUrl(contextnodedefine.getCopyUrl());
		msgsource.setName(contextnodedefine.getDescription());
		while (mth.find()) {
			msgListText = mth.group(1);
			if (StringUtils.isNotBlank(msgListText)) {
				// log.info("msgListText:" + msgListText);
				// log.info("cn_reg:" + contextnodedefine.getMsgsourceCnReg());
				// log.info("url_reg:" +
				// contextnodedefine.getMsgsourceUrlReg());
				Pattern pattern_cn = Pattern.compile(replace2reg(contextnodedefine.getMsgsourceCnReg()), Pattern.DOTALL);
				Matcher mth_cn = pattern_cn.matcher(msgListText);
				while (mth_cn.find()) {
					log.info("来源名字:" + mth_cn.group(1));
					msgsource.setName(mth_cn.group(1).trim());
				}
				if (contextnodedefine.getIsMsgsourceCn().equals("0")) {
					Pattern pattern_url = Pattern.compile(replace2reg(contextnodedefine.getMsgsourceUrlReg()), Pattern.DOTALL);
					Matcher mth_url = pattern_url.matcher(msgListText);
					int flag = 0;
					while (mth_url.find()) {
						log.info("来源url:" + mth_url.group(1));
						msgsource.setUrl(mth_url.group(1).trim());
						flag = 1;
					}
					if (flag == 0) {
						msgsource.setUrl("");
					}
				}
			}
		}
		return msgsource;
	}

	public String InitListUrlParent(StringBuffer context, String reg) {
		String text = "";
		// log.info("context=" + context);
		// log.info("list reg=" + reg);
		Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher mth = pattern.matcher(context);
		while (mth.find()) {
			text = mth.group(1);
		}
		// log.info("list context=" + text);
		return text;
	}

	public String getMainText(StringBuffer context, String reg, String url, String c_url, Contextnodedefine contextnodedefine) {
		log.info(reg);
		String text = "";
		Pattern pattern = Pattern.compile(reg, Pattern.DOTALL);
		// log.info("context=" + context);
		Matcher mth = pattern.matcher(context.toString());
		log.info(mth.matches());
		String contentBaseHref = "";
		if(contextnodedefine.getIsBaseHref().equals("1")) {
			contentBaseHref = getBaseHref(context, contextnodedefine);
		}
		contextnodedefine.setContentBaseHref(contentBaseHref);
		while (mth.find()) {
			log.info("找到内容");
			log.info("g=" + mth.group(1));
			text = mth.group(1);
			text = checkImg(text, url, c_url, contextnodedefine);
		}
		return text;
	}

	public String checkImg(String text, String url, String s_url, Contextnodedefine contextnodedefine) {
		Pattern ptn = Pattern.compile("<img.*?src=\"(.*?)\"", Pattern.CASE_INSENSITIVE);
		Matcher mth = ptn.matcher(text);

		while (mth.find()) {
			// log.info(mth.group(1));
			String src = mth.group(1);

			if (src.indexOf("http://") > -1 || src.indexOf("https://") > -1) {
				try {
					// log.info("替换为本地图片");
					text = text.replace(src, downimg(src, "/imgdown"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}  else {
				if (src.indexOf("file://") > -1) {
					continue;
				}

				if (src.startsWith("./")) {

					try {
						// log.info("替换为本地图片:path=" + url + src);
						text = text.replaceAll(src,
								downimg(s_url.substring(0, s_url.lastIndexOf("/") + 1) + src.replace("./", ""), "/imgdown"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (src.startsWith("/")) {

					try {
						// log.info("替换为本地图片:path=" + url + src);
						text = text.replaceAll(src,
								downimg(url + src, "/imgdown"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if(contextnodedefine.getContentBaseHref() != "") {
					try {
						text = text.replace(src, downimg(contextnodedefine.getContentBaseHref() + src, "/imgdown"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {

					try {
						// log.info("替换为本地图片:path=" + url + "/" + src);
						text = text.replaceAll(src, downimg(url + "/" + src.replace("./", ""), "/imgdown"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		}
		// log.info("downname=" + text);
		return text;

	}

	public static String downimg(String src, String path) throws IOException {
		// log.info("src:" + src);
		if (src.indexOf("file://") > -1) {
			return "";
		}
		String name = getImgName(src);
		if (name != null && name.length() > 0) {
			// log.info("下载图片路径=" + src);
			// log.info("name=" + name);

			String downpath = webpath + path + "/" + name;

			File locationfile = new File(downpath);
			// log.info("downpath=" + downpath + " exists:"
			// + locationfile.exists());
			if (!locationfile.exists()) {
				// log.info("parsent=" + locationfile.getParent());
				File parentpath = new File(locationfile.getParent());
				if (!parentpath.isDirectory()) {
					// log.info("不存在");
					parentpath.mkdirs();
				}
				URL url = new URL(src);
				URLConnection con = url.openConnection();
				con.setConnectTimeout(10000);
				con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
				InputStream input = con.getInputStream();
				OutputStream out = new BufferedOutputStream(new FileOutputStream(locationfile));
				int length = 1024 * 1024 * 10;
				byte[] a = new byte[length];
				while ((length = input.read(a)) > 0) {
					out.write(a, 0, length);
				}
				input.close();
				out.close();
			}
			String rstext = path + "/" + name;
			// log.info(rstext);
			return rstext;
		} else {
			return src;
		}
	}

	public static String checkImgforWord(String text, String url, String webpath) {
		Pattern ptn = Pattern.compile("<img.*?src=\"(.*?)\"");
		Matcher mth = ptn.matcher(text);

		while (mth.find()) {
			// log.info(mth.group(1));
			String src = mth.group(1);
			// log.info("src:" + src);
			if (src.indexOf("http://") > -1 || src.indexOf("https://") > -1) {
				try {
					// log.info("替换为本地图片");
					text = text.replace(src, downimgforword(src, "/imgdown", webpath));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}

		}
		// log.info("downname=" + text);
		return text;

	}

	public static String downimgforword(String src, String path, String webpath) throws IOException {
		String name = getImgName(src);
		if (name != null && name.length() > 0) {

			String downpath = webpath + path + "/" + name;
			// log.info(downpath);
			File locationfile = new File(downpath);
			// log.info(locationfile.getPath());
			if (!locationfile.exists()) {
				// log.info("parsent=" + locationfile.getParent());
				File parentpath = new File(locationfile.getParent());
				if (!parentpath.isDirectory()) {
					// log.info("不存在");
					parentpath.mkdirs();
				}
				URL url = new URL(src);
				URLConnection con = url.openConnection();
				con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
				InputStream input = con.getInputStream();
				OutputStream out = new BufferedOutputStream(new FileOutputStream(locationfile));
				int length = 1024 * 1024 * 10;
				byte[] a = new byte[length];
				while ((length = input.read(a)) > 0) {
					out.write(a, 0, length);
				}
				input.close();
				out.close();
			}
			String rstext = path + "/" + name;
			log.info(rstext);
			return rstext;
		} else {
			return src;
		}
	}

	public static String getImgName(String src) {
		String name = "";
		if (src.indexOf("file://") > -1) {
			return name;
		}
		if (src.indexOf("jpg") > -1 || src.indexOf("png") > -1 || src.indexOf("gif") > -1 || src.indexOf("jpeg") > -1) {
			if (src.indexOf("?") > -1) {
				String tmp = src.substring(0, src.lastIndexOf("?"));
				name = src.substring(src.lastIndexOf("/") + 1);
			} else {
				name = src.substring(src.lastIndexOf("/") + 1);

			}
		}
		name = name.replace("./", "");
		// log.info("name=" + name);

		return name;
	}

	public static String fomatDateToString(Date ud) {
		return fomatDateToString(ud, null);

	}

	public static String fomatDateToString(Date ud, String formatStyle) {
		if (ud == null)
			return "";
		// formatStyle :"MM-dd-yyyy"
		if (formatStyle == null || formatStyle.length() == 0) {
			formatStyle = "yyyy-MM-dd";
		}
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(formatStyle);
		String strDate = null;
		strDate = bartDateFormat.format(ud);
		return strDate;
	}

	public static boolean checkCopyDate(Date date, Integer copyflag) {
		Date now = new Date();
		if (date != null) {
			if (fomatDateToString(date).equals(fomatDateToString(now))) {
				return false;
			} else {
				// log.info("当前的时:" + now.getHours() + "--------copyflag="
				// + copyflag);
				if (now.getHours() == copyflag) {
					return true;
				} else {
					// log.error("未到采集时间");
					return false;
				}
			}
		} else {
			if (now.getHours() == copyflag) {
				return true;
			} else {
				// log.error("未到采集时间");
				return false;
			}
		}
	}

	public static Date fomatStringToDate(String strDate, String formatStyle) throws Exception {
		// formatStyle :"MM-dd-yyyy"
		if (strDate == null || strDate.length() == 0)
			return null;
		if (formatStyle == null || formatStyle.length() == 0)
			formatStyle = "yyyy-MM-dd HH:ss:mm";

		SimpleDateFormat bartDateFormat = new SimpleDateFormat(formatStyle);
		Date date = null;
		try {
			// Parse the text version of the date.
			// We have to perform the parse method in a
			// try-catch construct in case dateStringToParse
			// does not contain a date in the format we are expecting.

			date = bartDateFormat.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		// Now send the parsed date as a long value
		// to the system output.

		return date;
	}

	public static void toMobileImg(HttpServletRequest request, String img) {
		String mb_img_path = request.getSession().getServletContext().getRealPath("/") + "mobile";
		if (img.indexOf("http") > -1 || img.indexOf("https") > -1) {
			mb_img_path += "/" + getImgName(img);
		} else {
			mb_img_path += img;
		}
		// log.info("mb_img_path=" + mb_img_path);
		File file = new File(mb_img_path);
		if (!file.exists()) {
			File checkdir = new File(file.getParent());
			if (!checkdir.exists()) {
				checkdir.mkdirs();
			}
			try {
				Thumbnails.of(new File(request.getSession().getServletContext().getRealPath("/") + img)).scale(0.8F).toFile(mb_img_path);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
	}

	public static void WordtoMobileImg(String text, HttpServletRequest request) {
		// log.info("text=" + text);
		Pattern ptn = Pattern.compile("\"(/attached/image/[\\d]*/[\\d_]*[.][gif|jpg|jpeg|png|bmp]*)\"" + "",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher mth = ptn.matcher(text);
		while (mth.find()) {
			String src = mth.group(1);
			// log.info("找到图片:" + src);
			if (!Pattern.compile("[^/]").matcher(src).matches()) {
				toMobileImg(request, src);
			}

		}
		// log.info("生成内容中的缩略图完成");

	}

	public static void main(String[] args) {

		// Contextnodedefine contextnodedefine = new Contextnodedefine();
		// contextnodedefine.setCategoryId("1");
		// contextnodedefine.setIsListSimpleMeta("1");
		// contextnodedefine.setIsContextSimpleMeta("1");
		// contextnodedefine.setCopyUrl("http://www.88ysg.com/zy/");
		// contextnodedefine.setIsSplitPage("1");
		// contextnodedefine
		// .setSplitPageNewReg("<li><a href='(list_[数字].html)'>下一页</a></li>");
		// contextnodedefine.setCopyCount(100);
		// contextnodedefine
		// .setMainContextRegBegin("<div id=\"article_text\"
		// class=\"content\">");
		// contextnodedefine.setMainContextRegEnd("</div>");
		//
		// contextnodedefine
		// .setCopyUrlReg("<a class=\"clearfix\" href=\"(/[字母]/[数字].html)\"
		// target=\"_blank\">");
		//
		// contextnodedefine.setListRegBegin("<div class=\"news_main_info\">");
		// contextnodedefine.setListRegEnd("<div class=\"blank20\"></div>");
		// contextnodedefine.setIsContextSimpleTitle("0");
		// contextnodedefine.setIsContextSimpleMeta("1");
		// Contextdefine contextdefine = new Contextdefine();
		// contextdefine.setUrl("http://www.88ysg.com");
		// contextdefine.setLang("utf-8");
		// contextnodedefine.setIsContextSimpleDate("0");
		// contextnodedefine
		// .setContextDateReg("<span
		// class=\"post-time\">[通配]([数字]-[数字]-[数字][通配][数字]:[数字])</span>");
		// contextnodedefine.setContextDateFormat("yyyy-MM-dd hh:mm");
		// contextnodedefine.setContextdefine(contextdefine);
		// contextnodedefine.setIshaspic("1");
		// contextnodedefine.setPicreg("<img src=\"(.*?)\" alt=\".*?\">");
		// contextnodedefine.setIscontextnextpage("1");
		// contextnodedefine.setContextnextreg("<a
		// href='([0-9_]*.html)'>下一页</a>");
		//
		// CopyUtil util = new CopyUtil(contextnodedefine, "", null);
		// //
		// checkCopyDate(null, 2);
		// CopyUtil copyUtil = new CopyUtil();
		// for (int i = 0; i < 100000; i++) {
		// StringBuffer builder = copyUtil
		// .getUrlContext(
		// "http://183.232.32.182:8082/portal.do?wlanacname=WG-A0:0A:BF&wlanuserip=10.10.10.10&act=mobilepreview",
		// "gbk");
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// log.info(builder.toString());
		// }
		// System.out
		// .println("s="
		// + copyUtil.getMainText(
		// builder,
		// copyUtil.appendBegin2End(
		// "<div id=\"content\"><font class=\"content\"
		// id=\"zm\"><!--enpcontent-->",
		// "<!--百度分享 -->"), ""));

		// Pattern pattern = Pattern
		// .compile("http://www.qhnews.com/([a-zA-Z]*)/([a-zA-Z]*)/(.*?)/(.*?)/(.*?).html");
		// Matcher matcher = pattern
		// .matcher("http://www.qhnews.com/qiye/sx/2015/1013/42594.html");
		// while (matcher.find()) {
		// log.info(matcher.groupCount());
		// for (int i = 0; i <= matcher.groupCount(); i++) {
		// log.info("i=" + matcher.group(i));
		// }
		// }

		// CopyUtil copyUtil = new CopyUtil();
		// System.out.println(copyUtil.getUrlContext("http://www.88ysg.com/zy/",
		// "utf-8"));
		String name = getImgName("https://www.baidu.com/img/bd_logo1.png?123sdf=sdf");
		System.out.println("name" + name);

	}

	public Map<String, String> getMetaList() {
		return metaList;
	}

	public void setMetaList(Map<String, String> metaList) {
		this.metaList = metaList;
	}
}

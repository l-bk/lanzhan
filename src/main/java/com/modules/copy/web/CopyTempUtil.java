package com.modules.copy.web;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.common.utils.Encodes;
import com.modules.cms.dao.ArticleDao;
import com.modules.cms.dao.ArticleDataDao;
import com.modules.cms.dao.ArticleDataTempDao;
import com.modules.cms.dao.ArticleTempDao;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.ArticleData;
import com.modules.cms.entity.ArticleDataTemp;
import com.modules.cms.entity.ArticleTemp;
import com.modules.cms.entity.Category;
import com.modules.copy.dao.ContextNodeDefineFilterDao;
import com.modules.copy.entity.ContextNodeDefineFilter;
import com.modules.copy.entity.Contextdefine;
import com.modules.copy.entity.Contextnodedefine;
import com.modules.msgsource.dao.MsgsourceDao;
import com.modules.msgsource.entity.Msgsource;
import com.modules.sys.entity.User;
import com.modules.sys.utils.UserUtils;

@Component
public class CopyTempUtil {

	/*
	 * @Autowired private ContextNodeDefineFilterService filterService;
	 */

	private Logger log = Logger.getLogger(CopyTempUtil.class);

	// @Transactional(readOnly = false)
	public void DefineToTemp(List<Article> list, Contextdefine contextdefine, Contextnodedefine contextnodedefine,
			ArticleTempDao articleTempDao, ArticleDataTempDao articleDataTempDao, ArticleDao articleDao, ArticleDataDao articleDataDao,
			MsgsourceDao msgsourceDao, ContextNodeDefineFilterDao filterDao) {
		if (contextdefine.getIscheckflag().equals("0")) {
			log.info("入临时表");
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					ArticleDataTemp articleDataTemp = new ArticleDataTemp();
					ArticleData articleData = new ArticleData();
					Article article = list.get(i);
					ArticleTemp isUrl = new ArticleTemp();
					// 如果标题为空,就不入库 zzj
					if (StringUtils.isEmpty(article.getTitle())) {
						continue;
					}
					if (StringUtils.isEmpty(article.getArticleData().getContent())) {
						continue;
					}

					// 找出过滤规则
					List<ContextNodeDefineFilter> filterList = filterDao.findListByNodeId(contextnodedefine);

					// 内容过滤
					String content = contentFiliter(article.getArticleData().getContent(), filterList);
					article.getArticleData().setContent(content);

					isUrl.setCategory(new Category());
					isUrl.setAcquisitionSource(article.getAcquisitionSource());
					// 判断模板库已经入库,则不入库
					List<ArticleTemp> isUrlList = articleTempDao.findList(isUrl);
					if (isUrlList != null && isUrlList.size() > 0) {
						continue;
					} else {
						ArticleTemp articleTemp = new ArticleTemp();
						article.preInsert();
						User user = UserUtils.getUser();
						if (StringUtils.isNotBlank(user.getId())) {
							articleTemp.setUpdateBy(user);
							articleTemp.setCreateBy(user);
						} else {
							// 默认管理
							user = UserUtils.get("20160525231042212");
							articleTemp.setUpdateBy(user);
							articleTemp.setCreateBy(user);
						}
						articleTemp.setUpdateDate(new Date());
						articleTemp.setCreateDate(article.getCreateDate());
						articleTemp.setId(article.getId());
						articleTemp.setCategory(article.getCategory());
						// articleTemp.setc
						articleTemp.setTitle(article.getTitle());
						articleTemp.setLink(article.getLink());
						articleTemp.setColor(article.getColor());
						articleTemp.setImage(article.getImage());
						articleTemp.setKeywords(article.getKeywords());
						articleTemp.setDescription(article.getDescription());
						articleTemp.setWeight(article.getWeight());
						articleTemp.setWeightDate(article.getWeightDate());
						articleTemp.setHits(article.getHits());
						articleTemp.setPosid(article.getPosid());
						articleTemp.setCustomContentView(article.getCustomContentView());
						articleTemp.setViewConfig(article.getViewConfig());
						articleTemp.setBeginDate(article.getBeginDate());
						articleTemp.setEndDate(article.getEndDate());
						articleTemp.setUser(article.getUser());
						articleTemp.setAcquisitionSource(article.getAcquisitionSource());
						articleTemp.setDelFlag("0");
						articleTemp.setCustomContentView(article.getCustomContentView());
						articleTemp.setMsgsource(article.getMsgsource());
						articleData = article.getArticleData();

						articleDataTemp.setId(article.getId());
						articleDataTemp.setContent(articleData.getContent());
						articleDataTemp.setCopyfrom(articleData.getCopyfrom());
						articleDataTemp.setRelation(articleData.getRelation());
						articleDataTemp.setAllowComment(articleData.getAllowComment());
						articleDataTemp.setArticle(articleTemp);
						articleTemp.setArticleData(articleDataTemp);
						setMsgIdTemp(articleTemp, msgsourceDao);
						try {
							articleTempDao.insert(articleTemp);
							articleDataTempDao.insert(articleDataTemp);
						} catch (Exception e) {
							e.printStackTrace();

						}
					}
				}
			}
		} else {
			log.info("直接入库");
			if (list != null && list.size() > 0) {
				for (int k = 0; k < list.size(); k++) {
					Article article = list.get(k);
					log.info("article.getAcquisitionSource()====" + article.getAcquisitionSource());

					// Article isUrl = new Article();
					//
					// isUrl.setAcquisitionSource(article.getAcquisitionSource());
					// isUrl.setCategory(new Category());
					// isUrl.setMsgsource(new Msgsource());
					// List<Article> isUrlList = articleDao.findList(isUrl);
					Map map = new LinkedHashMap();
					map.put("acquisitionSource", article.getAcquisitionSource());
					int check = articleDao.checkAcquisitionSource(map);
					log.info("check:" + check);
					if (check > 0) {
						continue;
					} else {
						article.preInsert();
						User user = UserUtils.getUser();
						if (StringUtils.isNotBlank(user.getId())) {
							article.setUpdateBy(user);
							article.setCreateBy(user);
						} else {
							// 默认管理
							user = UserUtils.get("20160525231042212");
							article.setUpdateBy(user);
							article.setCreateBy(user);
						}
						article.setUpdateDate(new Date());
						ArticleData articleData = article.getArticleData();
						articleData.setId(article.getId());
						setMsgId(article, msgsourceDao);
						try {
							articleDao.insert(article);
							articleDataDao.insert(articleData);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

	}

	public void setMsgId(Article article, MsgsourceDao msgsourceDao) {
		if (article.getMsgsource() != null && StringUtils.isNoneBlank(article.getMsgsource().getName())) {
			Msgsource tempmsgsource = getMsg(article, msgsourceDao);
			if (tempmsgsource != null) {
				// log.info("tempmsgsource.id:" + tempmsgsource.getSid());
				log.info("msgid:" + tempmsgsource.getSid());
				article.setMsgsource(tempmsgsource);
			} else {
				Msgsource msgsource = article.getMsgsource();
				msgsource.preInsert();
				int check = msgsourceDao.insert(msgsource);
				if (check > 0) {
					Msgsource tempmsgsource2 = getMsg(article, msgsourceDao);
					if (tempmsgsource2 != null) {
						log.info("msgid:" + tempmsgsource2.getSid());
						article.setMsgsource(tempmsgsource2);
					}
				}

			}
		}

	}

	public Msgsource getMsg(Article article, MsgsourceDao msgsourceDao) {
		Msgsource querymsgsource = new Msgsource();
		querymsgsource.setUrl(article.getMsgsource().getUrl());
		log.info(":" + querymsgsource.getUrl());
		// 根据url从信息源表获取值
		List<Msgsource> msgsource = msgsourceDao.getName(querymsgsource);
		log.info("msgsource:" + msgsource + " size:" + msgsource.size());
		if (msgsource != null && msgsource.size() > 0) {
			return msgsource.get(0);
		} else {
			return null;
		}
	}

	public void setMsgIdTemp(ArticleTemp articletemp, MsgsourceDao msgsourceDao) {
		if (articletemp.getMsgsource() != null && StringUtils.isNoneBlank(articletemp.getMsgsource().getName())) {
			Msgsource tempmsgsource = getMsgTemp(articletemp, msgsourceDao);
			if (tempmsgsource != null) {
				// log.info("tempmsgsource.id:" + tempmsgsource.getSid());
				log.info("msgid:" + tempmsgsource.getSid());
				articletemp.setMsgsource(tempmsgsource);
			} else {
				Msgsource msgsource = articletemp.getMsgsource();
				msgsource.preInsert();
				int check = msgsourceDao.insert(msgsource);
				if (check > 0) {
					Msgsource tempmsgsource2 = getMsgTemp(articletemp, msgsourceDao);
					if (tempmsgsource2 != null) {
						log.info("msgid:" + tempmsgsource2.getSid());
						articletemp.setMsgsource(tempmsgsource2);
					}
				}

			}
		}
	}

	public Msgsource getMsgTemp(ArticleTemp articletemp, MsgsourceDao msgsourceDao) {
		Msgsource querymsgsource = new Msgsource();
		querymsgsource.setUrl(articletemp.getMsgsource().getUrl());
		querymsgsource.setName(articletemp.getMsgsource().getName());
		log.info(":" + querymsgsource.getUrl());
		log.info(":" + querymsgsource.getName());
		// 根据url从信息源表获取值
		List<Msgsource> tempmsgsource = msgsourceDao.getName(querymsgsource);
		log.info("tempmsgsource:" + tempmsgsource + " size:" + tempmsgsource.size());
		if (tempmsgsource != null && tempmsgsource.size() > 0) {
			return tempmsgsource.get(0);
		} else {
			return null;
		}
	}

	public String contentFiliter(String content, List<ContextNodeDefineFilter> filterList) {

		String begin = null;
		String end = null;
		String type = null;
		String newStr = content;
		if (filterList != null && (filterList.size() > 0)) {
			for (ContextNodeDefineFilter filter : filterList) {
				begin = filter.getBegin();
				end = filter.getEnd();
				type = filter.getType();
				// System.out.println(newStr);
				// System.out.println("#################");
				newStr = filterContent(begin, end, newStr, type);
				// System.out.println(newStr);
			}
		}
		/*
		 * // 文章内容字符串过来 广告过来 String begin = "<div id=adhzh name=hzh>"; String
		 * end = "</div>"; content = filterContent(begin, end, content, "1");
		 * 
		 * // 屏蔽内容的a标签 begin = "<a href=\""; end = "\" target=\"_blank\"";
		 * content = filterContent(begin, end, content, "4");
		 */

		return newStr;
	}

	public String filterContent(String begin, String end, String content, String flag) {
		String newStr = Encodes.escapeHtml(content);
		String reg = begin + "(.*?)" + end;
		// reg = Encodes.unescapeHtml(reg);

		Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher mth = pattern.matcher(newStr.toString());

		if (flag.endsWith("1")) {
			while (mth.find()) {
				String rpStr = mth.group(1);
				// rpStr = Encodes.unescapeHtml(rpStr);
				// rpStr = replaceAll(rpStr, "\\", "");
				newStr = replaceAll(newStr, rpStr, "");
			}
		} else if (flag.endsWith("2")) {
			while (mth.find()) {
				String rpStr = begin + mth.group(1) + end;
				// rpStr = Encodes.unescapeHtml(rpStr);
				// rpStr = replaceAll(rpStr, "\\", "");
				newStr = replaceAll(newStr, rpStr, "");
			}

		} else if (flag.endsWith("3")) {
			while (mth.find()) {
				String rpStr = begin + mth.group(1);
				// rpStr = Encodes.unescapeHtml(rpStr);
				// rpStr = replaceAll(rpStr, "\\", "");
				newStr = replaceAll(newStr, rpStr, "");
			}

		} else if (flag.endsWith("4")) {
			while (mth.find()) {
				String rpStr = mth.group(1) + end;
				// rpStr = Encodes.unescapeHtml(rpStr);
				// rpStr = replaceAll(rpStr, "\\", "");
				newStr = replaceAll(newStr, rpStr, "");
			}
		} else if (flag.endsWith("5")) {// 替换最后一个
			String rpStr = begin;
			newStr = replaceAll(newStr, rpStr, "");
		}
		newStr = Encodes.unescapeHtml(newStr);
		// System.out.println(newStr);
		// System.out.println("#################");
		return newStr;

	}

	public String replaceAll(String src, String fnd, String rep) {
		int pos = 0;
		int prepos = 0;
		int fndlen = fnd.length();
		String newstr = "";
		pos = src.indexOf(fnd, pos);
		while (pos >= 0) {
			newstr += src.substring(prepos, pos);
			newstr += rep;
			prepos = pos + fndlen;
			pos = src.indexOf(fnd, pos + fndlen);
		}
		newstr += src.substring(prepos);
		return newstr;
	}

	public String replaceLast(String src, String fnd, String rep) {
		int pos = 0;
		int prepos = 0;
		int fndlen = fnd.length();
		String newstr = "";
		pos = src.lastIndexOf(fnd);
		if (pos >= 0) {
			newstr = src.substring(prepos, pos) + rep + src.substring(pos + fndlen, src.length());

		}
		return newstr;
	}

}

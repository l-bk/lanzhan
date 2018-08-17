/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.modules.cms.entity;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.Length;

import com.modules.msgsource.entity.Msgsource;
import com.modules.sys.entity.User;
import com.google.common.collect.Lists;
import com.common.persistence.ArticleEntity;
import com.common.persistence.DataEntity;
import com.common.utils.Encodes;
import com.modules.cms.utils.CmsUtils;

/**
 * 文章Entity
 * 
 * @author ThinkGem
 * @version 2013-05-15
 */
public class Article extends ArticleEntity<Article> {
	private Logger log = Logger.getLogger(Article.class);
	public static final String DEFAULT_TEMPLATE = "frontViewArticle";

	private static final long serialVersionUID = 1L;
	private Category category;// 分类编号
	private String title; // 标题
	private String link; // 外部链接
	private String color; // 标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）
	private String image; // 文章图片
	private String shorttile;// 短标题
	private String keywords;// 关键字
	private String description;// 描述、摘要
	private Integer weight; // 权重，越大越靠前
	private Date weightDate;// 权重期限，超过期限，将weight设置为0
	private Integer hits; // 点击数
	private Integer today_hits;// 每天点击数
	private String posid; // 推荐位，多选（1：首页焦点图；2：栏目页文章推荐；）
	private String customContentView; // 自定义内容视图
	private String viewConfig; // 视图参数

	private ArticleData articleData; // 文章副表

	private Date beginDate; // 开始时间
	private Date endDate; // 结束时间
	private String managerid;
	private String acquisitionSource;
	private String timing;
	private String istime;
	private Integer msgid;
	private Msgsource msgsource;
	private User user;
	private String posName;
	private Integer number;
	private String isshowview;
	private String url;

	private List<String> images;

	private String staticUrl;
	private String isStatis;
	private String isMobileStatis;

	public String getIsMobileStatis() {
		return isMobileStatis;
	}

	public void setIsMobileStatis(String isMobileStatis) {
		this.isMobileStatis = isMobileStatis;
	}

	public String getStaticUrl() {
		return staticUrl;
	}

	public void setStaticUrl(String staticUrl) {
		this.staticUrl = staticUrl;
	}

	public String getIsStatis() {
		return isStatis;
	}

	public void setIsStatis(String isStatis) {
		this.isStatis = isStatis;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getIsshowview() {
		return isshowview;
	}

	public void setIsshowview(String isshowview) {
		this.isshowview = isshowview;
	}

	public String getPosName() {
		return posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}

	public Article() {
		super();
		this.weight = 0;
		this.hits = 0;
		this.posid = "";
		this.istime = "0";
		// this.delFlag="";
	}

	public Article(String id) {
		this();
		this.id = id;
	}

	public Article(Category category) {
		this();
		this.category = category;
	}

	public void prePersist() {
		// TODO 后续处理，暂不知有何用处
		// super.prePersist();
		articleData.setId(this.id);
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title != null && title.length() > 0 ? title.replace("_88养生馆", "") : title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Length(min = 0, max = 255)
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Length(min = 0, max = 50)
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Length(min = 0, max = 255)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;// CmsUtils.formatImageSrcToDb(image);
	}

	@Length(min = 0, max = 255)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Length(min = 0, max = 255)
	public String getDescription() {
		return description;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Date getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Date weightDate) {
		this.weightDate = weightDate;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public Integer getToday_hits() {
		return today_hits;
	}

	public void setToday_hits(Integer today_hits) {
		this.today_hits = today_hits;
	}

	@Length(min = 0, max = 10)
	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

	public String getCustomContentView() {
		return customContentView;
	}

	public void setCustomContentView(String customContentView) {
		this.customContentView = customContentView;
	}

	public String getViewConfig() {
		return viewConfig;
	}

	public void setViewConfig(String viewConfig) {
		this.viewConfig = viewConfig;
	}

	public ArticleData getArticleData() {
		return articleData;
	}

	public void setArticleData(ArticleData articleData) {
		this.articleData = articleData;
	}

	public List<String> getPosidList() {
		List<String> list = Lists.newArrayList();
		if (posid != null) {
			for (String s : StringUtils.split(posid, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setPosidList(List<String> list) {
		posid = "," + StringUtils.join(list, ",") + ",";
	}

	public String getUrl() {
		return CmsUtils.getUrlDynamic(this);
	}

	public String getImageSrc() {
		return CmsUtils.formatImageSrcToWeb(this.image);
	}

	public String getManagerid() {
		return managerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}

	public String getAcquisitionSource() {
		return acquisitionSource;
	}

	public void setAcquisitionSource(String acquisitionSource) {
		this.acquisitionSource = acquisitionSource;
	}

	public String getTiming() {
		return timing;
	}

	public void setTiming(String timing) {
		this.timing = timing;
	}

	public String getIstime() {
		return istime;
	}

	public void setIstime(String istime) {
		this.istime = istime;
	}

	public Integer getMsgid() {
		return msgid;
	}

	public void setMsgid(Integer msgid) {
		this.msgid = msgid;
	}

	public Msgsource getMsgsource() {
		return msgsource;
	}

	public void setMsgsource(Msgsource msgsource) {
		this.msgsource = msgsource;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getShorttile() {
		return shorttile;
	}

	public void setShorttile(String shorttile) {
		this.shorttile = shorttile;
	}

}

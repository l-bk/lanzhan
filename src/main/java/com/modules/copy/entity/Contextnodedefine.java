package com.modules.copy.entity;

import java.util.Date;

import com.common.persistence.BaseEntity;
import com.modules.cms.entity.Category;

public class Contextnodedefine extends BaseEntity<Contextnodedefine> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer cid;
	private String stat;
	private String categoryId;
	private String description;
	private String copyUrl;
	private String listRegBegin;
	private String copyUrlReg;
	private String listRegEnd;
	private Integer copyCount;
	private String isSplitPage;
	private String splitPageNewReg;
	private String isListSimpleTitle;
	private String titleRegBegin;
	private String titleRegEnd;
	private String isListSimpleMeta;
	private String listKeyorksRegBegin;
	private String listKeyorksRegEnd;
	private String listDescriptionRegBegin;
	private String listDescriptionRegEnd;
	private String isContextSimpleTitle;
	private String contextTitleBegin;
	private String contextTitleEnd;
	private String isContextSimpleMeta;
	private String contextKeyorksRegBegin;
	private String contextKeyorksRegEnd;
	private String contextDescriptionRegBegin;
	private String contextDescriptionRegEnd;
	private String mainContextRegBegin;
	private String mainContextRegEnd;
	private Contextdefine contextdefine;
	private Category category;
	private Integer parentid;
	private Date lastcopydate;
	private String isContextSimpleDate;
	private String contextDateReg;
	private String contextDateFormat;
	private String ishaspic;
	private String picreg;
	private String iscontextnextpage;
	private String contextnextreg;
	private String isContextSimpleShortTitle;
	private String contextShortTitleRegBegin;
	private String contextShortTitleRegEnd;
	private String IsMsgsource;
	private String MsgsourceBegin;
	private String MsgsourceEnd;
	private String IsMsgsourceCn;
	private String MsgsourceCnReg;
	private String MsgsourceUrlReg;
	private String isBaseHref; // 是否规定基准url
	private String baseHrefReg; // 基准url正则
	private String baseHref; // 基准url
	private String contentBaseHref; // 文章内容基准url
	private String posid; // 关联推荐位
	
	
	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

	public String getContentBaseHref() {
		return contentBaseHref;
	}

	public void setContentBaseHref(String contentBaseHref) {
		this.contentBaseHref = contentBaseHref;
	}

	public String getBaseHref() {
		return baseHref;
	}

	public void setBaseHref(String baseHref) {
		this.baseHref = baseHref;
	}

	public String getIsBaseHref() {
		return isBaseHref;
	}

	public void setIsBaseHref(String isBaseHref) {
		this.isBaseHref = isBaseHref;
	}

	public String getBaseHrefReg() {
		return baseHrefReg;
	}

	public void setBaseHrefReg(String baseHrefReg) {
		this.baseHrefReg = baseHrefReg;
	}

	public Contextnodedefine() {
		this.stat = "1";
		this.isSplitPage = "1";
		this.isListSimpleTitle = "1";
		this.isListSimpleMeta = "1";
		this.isContextSimpleTitle = "1";
		this.isContextSimpleMeta = "1";
		this.isContextSimpleDate = "1";
		this.ishaspic = "0";
		this.iscontextnextpage = "0";
		this.isContextSimpleShortTitle="1";
		this.IsMsgsource="1";//是否采集来源
		this.IsMsgsourceCn="1";//是否只采集来源文字
	}

	public String getIsMsgsource() {
		return IsMsgsource;
	}

	public void setIsMsgsource(String isMsgsource) {
		IsMsgsource = isMsgsource;
	}

	public String getMsgsourceBegin() {
		return MsgsourceBegin;
	}

	public void setMsgsourceBegin(String msgsourceBegin) {
		MsgsourceBegin = msgsourceBegin;
	}

	public String getMsgsourceEnd() {
		return MsgsourceEnd;
	}

	public void setMsgsourceEnd(String msgsourceEnd) {
		MsgsourceEnd = msgsourceEnd;
	}

	public String getIsMsgsourceCn() {
		return IsMsgsourceCn;
	}

	public void setIsMsgsourceCn(String isMsgsourceCn) {
		IsMsgsourceCn = isMsgsourceCn;
	}

	public String getMsgsourceCnReg() {
		return MsgsourceCnReg;
	}

	public void setMsgsourceCnReg(String msgsourceCnReg) {
		MsgsourceCnReg = msgsourceCnReg;
	}

	public String getMsgsourceUrlReg() {
		return MsgsourceUrlReg;
	}

	public void setMsgsourceUrlReg(String msgsourceUrlReg) {
		MsgsourceUrlReg = msgsourceUrlReg;
	}

	public String getIshaspic() {
		return ishaspic;
	}

	public void setIshaspic(String ishaspic) {
		this.ishaspic = ishaspic;
	}

	public String getPicreg() {
		return picreg;
	}

	public void setPicreg(String picreg) {
		this.picreg = picreg;
	}

	public String getIscontextnextpage() {
		return iscontextnextpage;
	}

	public void setIscontextnextpage(String iscontextnextpage) {
		this.iscontextnextpage = iscontextnextpage;
	}

	public String getContextnextreg() {
		return contextnextreg;
	}

	public void setContextnextreg(String contextnextreg) {
		this.contextnextreg = contextnextreg;
	}

	

	public Date getLastcopydate() {
		return lastcopydate;
	}

	public void setLastcopydate(Date lastcopydate) {
		this.lastcopydate = lastcopydate;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCopyUrl() {
		return copyUrl;
	}

	public void setCopyUrl(String copyUrl) {
		this.copyUrl = copyUrl;
	}

	public String getListRegBegin() {
		return listRegBegin;
	}

	public void setListRegBegin(String listRegBegin) {
		this.listRegBegin = listRegBegin;
	}

	public String getCopyUrlReg() {
		return copyUrlReg;
	}

	public void setCopyUrlReg(String copyUrlReg) {
		this.copyUrlReg = copyUrlReg;
	}

	public String getListRegEnd() {
		return listRegEnd;
	}

	public void setListRegEnd(String listRegEnd) {
		this.listRegEnd = listRegEnd;
	}

	public Integer getCopyCount() {
		return copyCount;
	}

	public void setCopyCount(Integer copyCount) {
		this.copyCount = copyCount;
	}

	public String getIsSplitPage() {
		return isSplitPage;
	}

	public void setIsSplitPage(String isSplitPage) {
		this.isSplitPage = isSplitPage;
	}

	public String getSplitPageNewReg() {
		return splitPageNewReg;
	}

	public void setSplitPageNewReg(String splitPageNewReg) {
		this.splitPageNewReg = splitPageNewReg;
	}

	public String getIsListSimpleTitle() {
		return isListSimpleTitle;
	}

	public void setIsListSimpleTitle(String isListSimpleTitle) {
		this.isListSimpleTitle = isListSimpleTitle;
	}

	public String getTitleRegBegin() {
		return titleRegBegin;
	}

	public void setTitleRegBegin(String titleRegBegin) {
		this.titleRegBegin = titleRegBegin;
	}

	public String getTitleRegEnd() {
		return titleRegEnd;
	}

	public void setTitleRegEnd(String titleRegEnd) {
		this.titleRegEnd = titleRegEnd;
	}

	public String getIsListSimpleMeta() {
		return isListSimpleMeta;
	}

	public void setIsListSimpleMeta(String isListSimpleMeta) {
		this.isListSimpleMeta = isListSimpleMeta;
	}

	public String getListKeyorksRegBegin() {
		return listKeyorksRegBegin;
	}

	public void setListKeyorksRegBegin(String listKeyorksRegBegin) {
		this.listKeyorksRegBegin = listKeyorksRegBegin;
	}

	public String getListKeyorksRegEnd() {
		return listKeyorksRegEnd;
	}

	public void setListKeyorksRegEnd(String listKeyorksRegEnd) {
		this.listKeyorksRegEnd = listKeyorksRegEnd;
	}

	public String getListDescriptionRegBegin() {
		return listDescriptionRegBegin;
	}

	public void setListDescriptionRegBegin(String listDescriptionRegBegin) {
		this.listDescriptionRegBegin = listDescriptionRegBegin;
	}

	public String getListDescriptionRegEnd() {
		return listDescriptionRegEnd;
	}

	public void setListDescriptionRegEnd(String listDescriptionRegEnd) {
		this.listDescriptionRegEnd = listDescriptionRegEnd;
	}

	public String getIsContextSimpleTitle() {
		return isContextSimpleTitle;
	}

	public void setIsContextSimpleTitle(String isContextSimpleTitle) {
		this.isContextSimpleTitle = isContextSimpleTitle;
	}

	public String getContextTitleBegin() {
		return contextTitleBegin;
	}

	public void setContextTitleBegin(String contextTitleBegin) {
		this.contextTitleBegin = contextTitleBegin;
	}

	public String getContextTitleEnd() {
		return contextTitleEnd;
	}

	public void setContextTitleEnd(String contextTitleEnd) {
		this.contextTitleEnd = contextTitleEnd;
	}

	public String getIsContextSimpleMeta() {
		return isContextSimpleMeta;
	}

	public void setIsContextSimpleMeta(String isContextSimpleMeta) {
		this.isContextSimpleMeta = isContextSimpleMeta;
	}

	public String getContextKeyorksRegBegin() {
		return contextKeyorksRegBegin;
	}

	public void setContextKeyorksRegBegin(String contextKeyorksRegBegin) {
		this.contextKeyorksRegBegin = contextKeyorksRegBegin;
	}

	public String getContextKeyorksRegEnd() {
		return contextKeyorksRegEnd;
	}

	public void setContextKeyorksRegEnd(String contextKeyorksRegEnd) {
		this.contextKeyorksRegEnd = contextKeyorksRegEnd;
	}

	public String getContextDescriptionRegBegin() {
		return contextDescriptionRegBegin;
	}

	public void setContextDescriptionRegBegin(String contextDescriptionRegBegin) {
		this.contextDescriptionRegBegin = contextDescriptionRegBegin;
	}

	public String getContextDescriptionRegEnd() {
		return contextDescriptionRegEnd;
	}

	public void setContextDescriptionRegEnd(String contextDescriptionRegEnd) {
		this.contextDescriptionRegEnd = contextDescriptionRegEnd;
	}

	public String getMainContextRegBegin() {
		return mainContextRegBegin;
	}

	public void setMainContextRegBegin(String mainContextRegBegin) {
		this.mainContextRegBegin = mainContextRegBegin;
	}

	public String getMainContextRegEnd() {
		return mainContextRegEnd;
	}

	public void setMainContextRegEnd(String mainContextRegEnd) {
		this.mainContextRegEnd = mainContextRegEnd;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Contextdefine getContextdefine() {
		return contextdefine;
	}

	public void setContextdefine(Contextdefine contextdefine) {
		this.contextdefine = contextdefine;
	}

	@Override
	public void preInsert() {
		// TODO Auto-generated method stub

	}

	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub

	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public String getIsContextSimpleDate() {
		return isContextSimpleDate;
	}

	public void setIsContextSimpleDate(String isContextSimpleDate) {
		this.isContextSimpleDate = isContextSimpleDate;
	}

	public String getContextDateReg() {
		return contextDateReg;
	}

	public void setContextDateReg(String contextDateReg) {
		this.contextDateReg = contextDateReg;
	}

	public String getContextDateFormat() {
		return contextDateFormat;
	}

	public void setContextDateFormat(String contextDateFormat) {
		this.contextDateFormat = contextDateFormat;
	}

	public String getIsContextSimpleShortTitle() {
		return isContextSimpleShortTitle;
	}

	public void setIsContextSimpleShortTitle(String isContextSimpleShortTitle) {
		this.isContextSimpleShortTitle = isContextSimpleShortTitle;
	}

	public String getContextShortTitleRegBegin() {
		return contextShortTitleRegBegin;
	}

	public void setContextShortTitleRegBegin(String contextShortTitleRegBegin) {
		this.contextShortTitleRegBegin = contextShortTitleRegBegin;
	}

	public String getContextShortTitleRegEnd() {
		return contextShortTitleRegEnd;
	}

	public void setContextShortTitleRegEnd(String contextShortTitleRegEnd) {
		this.contextShortTitleRegEnd = contextShortTitleRegEnd;
	}


}

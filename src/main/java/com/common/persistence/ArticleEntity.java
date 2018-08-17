package com.common.persistence;

import com.modules.cms.entity.Category;
import com.modules.cms.utils.CmsUtils;

public abstract class ArticleEntity<T> extends DataEntity<T> {
	public abstract String getUrl();

	public abstract String getLink();

	public abstract Category getCategory();
	
	public abstract String getCustomContentView();

}

package com.modules.ads.dao;

import java.util.List;

import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.ads.entity.Ads;

@MyBatisDao
public interface AdsDao extends CrudDao<Ads> {

	public List<Ads> qryAds(Ads ads);
	
	public int addAds(Ads ads);

	public int editAds(Ads ads);
	
	public int deleteAds(int aid);
	
	public List<Ads> findAdsbyposid(Ads ads);
}

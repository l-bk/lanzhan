package com.modules.ads.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.persistence.Page;
import com.common.service.CrudService;
import com.common.utils.StringUtils;
import com.modules.ads.dao.AdsDao;
import com.modules.ads.entity.Ads;
import com.modules.cms.dao.GuestbookDao;
import com.modules.cms.entity.Guestbook;

@Service
public class AdsService extends CrudService<AdsDao, Ads> {
	
	@Autowired
	private AdsDao adsDao;
	
	public Page<Ads> findPage(Page<Ads> page, Ads ads) {
//		DetachedCriteria dc = dao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(guestbook.getType())){
//			dc.add(Restrictions.eq("type", guestbook.getType()));
//		}
//		if (StringUtils.isNotEmpty(guestbook.getContent())){
//			dc.add(Restrictions.like("content", "%"+guestbook.getContent()+"%"));
//		}
//		dc.add(Restrictions.eq(Guestbook.FIELD_DEL_FLAG, guestbook.getDelFlag()));
//		dc.addOrder(Order.desc("createDate"));
//		return dao.find(page, dc);
//		guestbook.getSqlMap().put("dsf", dataScopeFilter(guestbook.getCurrentUser(), "o", "u"));
		
		ads.setPage(page);
		page.setList(dao.findList(ads));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(Ads ads){
		if (StringUtils.isBlank(ads.getAid())){
			ads.preInsert();
			dao.insert(ads);
		}else{
			ads.preUpdate();
			dao.update(ads);
		}
	}
	
	
	
	
	public List<Ads> qryAds(Ads ads){
		
		return adsDao.qryAds(ads);
	}
	
	public List<Ads> findAdsbyposid(String  advPosid,String typeid){
		Ads ads = new Ads();
		ads.setAdvPosid(advPosid);
		ads.setTypeid(typeid);
		return adsDao.findAdsbyposid(ads);
	}
	
	public int addAds(Ads ads){
		
		return adsDao.addAds(ads);
	}
	
	public int editAds(Ads ads){
		
		return adsDao.editAds(ads);
	}
	
	public int deleteAds(int aid){
		
		return adsDao.deleteAds(aid);
	}
}

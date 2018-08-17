/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.common.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.common.persistence.CrudDao;
import com.common.persistence.DataEntity;
import com.common.persistence.Page;
import com.common.utils.StringUtils;
import com.modules.cms.dao.CategoryDao;
import com.modules.cms.entity.Category;
import com.modules.sys.entity.User;
import com.modules.sys.utils.UserUtils;

/**
 * Service基类
 * @author ThinkGem
 * @version 2014-05-16
 */
@Transactional(readOnly = true)
public abstract class CrudService<D extends CrudDao<T>, T extends DataEntity<T>> extends BaseService {
	
	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T get(String id) {
		return dao.get(id);
	}
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public T get(T entity) {
		return dao.get(entity);
	}
	
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<T> findPage(Page<T> page, T entity) {
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}

	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 */
	@Transactional(readOnly = false)
//	public void save(T entity) {
//		if (entity.getIsNewRecord()){
//			entity.preInsert();
//			dao.insert(entity);
//		}else{
//			entity.preUpdate();
//			dao.update(entity);
//		}
	public void save(T entity) {
		if (entity.getIsNewRecord()){
			if(entity.getClass().getSimpleName().equals("Category")){
				//判断是否是从新增栏目的方法，如果是，更改ID为自增方式
				 Category t = categoryDao.maxid();
				 if (t != null) {
					 logger.info("category_maxid:" + t.getId());
					 entity.setId("" + (Long.parseLong(t.getId()) + 1));
				 } else {
					 entity.setId("" + 1);
				 }
					User user = UserUtils.getUser();
					if (StringUtils.isNotBlank(user.getId())) {
						entity.setUpdateBy(user);
						entity.setCreateBy(user);
					}
					entity.setUpdateDate(new Date());
					entity.setCreateDate(new Date());
			}else{
				entity.preInsert();
			}
//			entity.preInsert();
			dao.insert(entity);
		}else{
			entity.preUpdate();
			dao.update(entity);
		}
	}
	
	/**
	 * 删除数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void delete(T entity) {
		dao.delete(entity);
	}
	@Transactional(readOnly = false)
	public List<T> findAllList() {
		return dao.findAllList();
	}


}

package com.modules.Template.dao;

import com.common.persistence.CrudDao;
import com.common.persistence.annotation.MyBatisDao;
import com.modules.Template.entity.FileManager;
import com.modules.sys.entity.Dict;

import java.util.List;

/**
 * Created by jenwing on 16/7/17.
 */
@MyBatisDao
public interface FileManagerDao extends CrudDao<Dict> {
    //取文件列表
    public List<FileManager> findAll(FileManager t);

    public int save(FileManager fileManager);

    public int update(FileManager fileManager);

    public int delete(int cid);

    public FileManager get(FileManager t);

}

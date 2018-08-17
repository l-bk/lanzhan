package com.modules.Template.service;

import com.modules.Template.dao.FileManagerDao;
import com.modules.Template.entity.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jenwing on 16/7/17.
 */
@Service
public class FileManagerService {
    @Autowired
    private FileManagerDao fileManagerDao;

    public List<FileManager> findAll(FileManager t) {
        return fileManagerDao.findAll(t);
    }

    public int save(FileManager template) {
        return fileManagerDao.save(template);
    }

    public int update(FileManager template) {
//        System.out.println("+++++测试3+++++++"+template.getSortNumber()+template.getDescription());
        return fileManagerDao.update(template);
    }

    public int delete(int cid) {
        return fileManagerDao.delete(cid);
    }

    public FileManager get(FileManager t) {
        return fileManagerDao.get(t);
    }
}

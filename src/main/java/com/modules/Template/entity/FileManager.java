package com.modules.Template.entity;

import com.common.persistence.BaseEntity;
import com.modules.collect.entity.CollectContext;

import java.util.Date;

/**
 * Created by jenwing on 16/7/17.
 */
public class FileManager extends BaseEntity<CollectContext> {

    private Integer cid;
    private String description;
    private String templatefilename;
    private String templatepath;
    private Date modifydate;
    private String notes;
    private String context;
    private String type = "0";
    private String show = "pc";
    private Integer sortNumber;

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplatefilename() {
        return templatefilename;
    }

    public void setTemplatefilename(String templatefilename) {
        this.templatefilename = templatefilename;
    }

    public String getTemplatepath() {
        return templatepath;
    }

    public void setTemplatepath(String templatepath) {
        this.templatepath = templatepath;
    }

    public Date getModifydate() {
        return modifydate;
    }

    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    @Override
    public void preInsert() {
        // TODO Auto-generated method stub

    }

    @Override
    public void preUpdate() {
        // TODO Auto-generated method stub

    }
}
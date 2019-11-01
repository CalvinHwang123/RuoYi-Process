package com.ruoyi.process.definition.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

public class ProcessDefinition extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    @Excel(name = "流程名称")
    private String name;

    @Excel(name = "流程KEY")
    private String key;

    @Excel(name = "流程版本")
    private int version;

    @Excel(name = "所属分类")
    private String category;

    @Excel(name = "流程描述")
    private String description;

    private String deploymentId;

    @Excel(name = "部署时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deploymentTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public Date getDeploymentTime() {
        return deploymentTime;
    }

    public void setDeploymentTime(Date deploymentTime) {
        this.deploymentTime = deploymentTime;
    }
}

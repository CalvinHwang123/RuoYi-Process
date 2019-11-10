package com.ruoyi.process.todoitem.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 待办事项对象 biz_todo_item
 *
 * @author Xianlu Tech
 * @date 2019-11-08
 */
public class BizTodoItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 ID */
    private Long id;

    /** 事项标题 */
    @Excel(name = "事项标题")
    private String itemName;

    /** 事项内容 */
    @Excel(name = "事项内容")
    private String itemContent;

    /** 模块名称 (必须以 uri 一致) */
    @Excel(name = "模块名称")
    private String module;

    /** 任务 ID */
    @Excel(name = "任务 ID")
    private String taskId;

    /** 任务名称 (必须以表单页面名称一致) */
    @Excel(name = "任务名称")
    private String taskName;

    /** 节点名称 */
    @Excel(name = "节点名称")
    private String nodeName;

    /** 是否查看 default 0 (0 否 1 是) */
    @Excel(name = "是否查看")
    private String isView;

    /** 是否处理 default 0 (0 否 1 是) */
    @Excel(name = "是否处理")
    private String isHandle;

    /** 待办人 ID */
    @Excel(name = "待办人 ID")
    private String todoUserId;

    /** 待办人名称 */
    @Excel(name = "待办人名称")
    private String todoUserName;

    /** 处理人 ID */
    @Excel(name = "处理人 ID")
    private String handleUserId;

    /** 处理人名称 */
    @Excel(name = "处理人名称")
    private String handleUserName;

    /** 通知时间 */
    @Excel(name = "通知时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date todoTime;

    /** 处理时间 */
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date handleTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public String getItemContent() {
        return itemContent;
    }
    public void setModule(String module) {
        this.module = module;
    }

    public String getModule() {
        return module;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getTaskName() {
        return taskName;
    }
    public void setIsView(String isView) {
        this.isView = isView;
    }

    public String getIsView() {
        return isView;
    }
    public void setIsHandle(String isHandle) {
        this.isHandle = isHandle;
    }

    public String getIsHandle() {
        return isHandle;
    }
    public void setTodoUserId(String todoUserId) {
        this.todoUserId = todoUserId;
    }

    public String getTodoUserId() {
        return todoUserId;
    }
    public void setTodoUserName(String todoUserName) {
        this.todoUserName = todoUserName;
    }

    public String getTodoUserName() {
        return todoUserName;
    }
    public void setHandleUserId(String handleUserId) {
        this.handleUserId = handleUserId;
    }

    public String getHandleUserId() {
        return handleUserId;
    }
    public void setHandleUserName(String handleUserName) {
        this.handleUserName = handleUserName;
    }

    public String getHandleUserName() {
        return handleUserName;
    }
    public void setTodoTime(Date todoTime) {
        this.todoTime = todoTime;
    }

    public Date getTodoTime() {
        return todoTime;
    }
    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("itemName", getItemName())
            .append("itemContent", getItemContent())
            .append("module", getModule())
            .append("taskId", getTaskId())
            .append("taskName", getTaskName())
            .append("isView", getIsView())
            .append("isHandle", getIsHandle())
            .append("todoUserId", getTodoUserId())
            .append("todoUserName", getTodoUserName())
            .append("handleUserId", getHandleUserId())
            .append("handleUserName", getHandleUserName())
            .append("todoTime", getTodoTime())
            .append("handleTime", getHandleTime())
            .toString();
    }
}

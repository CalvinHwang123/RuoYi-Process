package com.ruoyi.process.todoitem.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.process.leave.domain.BizLeaveVo;
import com.ruoyi.process.leave.service.IBizLeaveService;
import com.ruoyi.process.todoitem.domain.BizTodoItem;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import com.ruoyi.system.domain.SysUser;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 待办事项Controller
 *
 * @author Xianlu Tech
 * @date 2019-11-08
 */
@Controller
@RequestMapping("/process/todoitem")
public class BizTodoItemController extends BaseController {
    private String prefix = "process/todoitem";

    @Autowired
    private IBizTodoItemService bizTodoItemService;

    @Autowired
    private IBizLeaveService bizLeaveService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @GetMapping("/todoListView")
    public String todoListView(ModelMap mmap) {
        BizTodoItem todoItem = new BizTodoItem();
        todoItem.setTodoUserId(ShiroUtils.getLoginName());
        todoItem.setIsHandle("0");
        List<BizTodoItem> todoItemList = bizTodoItemService.selectBizTodoItemList(todoItem);
        mmap.put("todoItemList", todoItemList);
        return prefix + "/todoList";
    }

    /**
     * 加载办理弹窗
     * @param taskId
     * @param mmap
     * @return
     */
    @GetMapping("/showVerifyDialog/{taskId}")
    public String showVerifyDialog(@PathVariable("taskId") String taskId, ModelMap mmap,
                                   String module, String formPageName) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        BizLeaveVo bizLeave = bizLeaveService.selectBizLeaveById(new Long(processInstance.getBusinessKey()));
        mmap.put("bizLeave", bizLeave);
        mmap.put("taskId", taskId);
//        String verifyName = task.getTaskDefinitionKey().substring(0, 1).toUpperCase() + task.getTaskDefinitionKey().substring(1);
        return "process/" + module + "/" + formPageName;
    }

    @RequiresPermissions("process:todoitem:view")
    @GetMapping()
    public String todoitem(ModelMap mmap) {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/todoitem";
    }

    /**
     * 查询待办事项列表
     */
    @RequiresPermissions("process:todoitem:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizTodoItem bizTodoItem) {
        bizTodoItem.setIsHandle("0");
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
        }
        startPage();
        List<BizTodoItem> list = bizTodoItemService.selectBizTodoItemList(bizTodoItem);
        return getDataTable(list);
    }

    @RequiresPermissions("process:todoitem:doneView")
    @GetMapping("/doneitemView")
    public String doneitem() {
        return prefix + "/doneitem";
    }

    /**
     * 查询已办事项列表
     */
    @RequiresPermissions("process:todoitem:doneList")
    @PostMapping("/doneList")
    @ResponseBody
    public TableDataInfo doneList(BizTodoItem bizTodoItem) {
        bizTodoItem.setIsHandle("1");
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
        }
        startPage();
        List<BizTodoItem> list = bizTodoItemService.selectBizTodoItemList(bizTodoItem);
        return getDataTable(list);
    }

    /**
     * 导出待办事项列表
     */
    @RequiresPermissions("process:todoitem:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizTodoItem bizTodoItem) {
        bizTodoItem.setIsHandle("0");
        List<BizTodoItem> list = bizTodoItemService.selectBizTodoItemList(bizTodoItem);
        ExcelUtil<BizTodoItem> util = new ExcelUtil<BizTodoItem>(BizTodoItem.class);
        return util.exportExcel(list, "todoitem");
    }

    /**
     * 导出已办事项列表
     */
    @RequiresPermissions("process:todoitem:doneExport")
    @PostMapping("/doneExport")
    @ResponseBody
    public AjaxResult doneExport(BizTodoItem bizTodoItem) {
        bizTodoItem.setIsHandle("1");
        List<BizTodoItem> list = bizTodoItemService.selectBizTodoItemList(bizTodoItem);
        ExcelUtil<BizTodoItem> util = new ExcelUtil<BizTodoItem>(BizTodoItem.class);
        return util.exportExcel(list, "todoitem");
    }

    /**
     * 新增待办事项
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存待办事项
     */
    @RequiresPermissions("process:todoitem:add")
    @Log(title = "待办事项", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizTodoItem bizTodoItem) {
        return toAjax(bizTodoItemService.insertBizTodoItem(bizTodoItem));
    }

    /**
     * 修改待办事项
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        BizTodoItem bizTodoItem = bizTodoItemService.selectBizTodoItemById(id);
        mmap.put("bizTodoItem", bizTodoItem);
        return prefix + "/edit";
    }

    /**
     * 修改保存待办事项
     */
    @RequiresPermissions("process:todoitem:edit")
    @Log(title = "待办事项", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizTodoItem bizTodoItem) {
        return toAjax(bizTodoItemService.updateBizTodoItem(bizTodoItem));
    }

    /**
     * 删除待办事项
     */
    @RequiresPermissions("process:todoitem:remove")
    @Log(title = "待办事项", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizTodoItemService.deleteBizTodoItemByIds(ids));
    }
}

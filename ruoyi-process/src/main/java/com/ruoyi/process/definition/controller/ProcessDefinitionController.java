package com.ruoyi.process.definition.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.process.definition.domain.ProcessDefinition;
import com.ruoyi.process.definition.service.ProcessDefinitionService;
import org.activiti.engine.task.Task;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/process/definition")
public class ProcessDefinitionController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionController.class);

    private String prefix = "process/definition";

    @Autowired
    private ProcessDefinitionService processDefinitionService;

    @RequiresPermissions("process:definition:view")
    @GetMapping
    public String processDefinition() {
        return prefix + "/definition";
    }

    @PostMapping("/list")
    @RequiresPermissions("process:definition:list")
    @ResponseBody
    public TableDataInfo list(ProcessDefinition processDefinition) {
        List<ProcessDefinition> list = processDefinitionService.listProcessDefinition(processDefinition);
        return getDataTable(list);
    }

    /**
     * 部署流程定义
     */
    @RequiresPermissions("process:definition:upload")
    @Log(title = "流程定义", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult upload(@RequestParam("processDefinition") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String extensionName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
                if ("xml".equalsIgnoreCase(extensionName)) {
                    if (!file.getOriginalFilename().contains("bpmn20.xml")) {
                        return error("流程定义文件仅支持 bpmn, bpmn20.xml, zip 和 bar 格式！");
                    }
                }
                if (!"xml".equalsIgnoreCase(extensionName)
                        && !"bpmn".equalsIgnoreCase(extensionName)
                        && !"zip".equalsIgnoreCase(extensionName)
                        && !"bar".equalsIgnoreCase(extensionName)) {
                    return error("流程定义文件仅支持 bpmn, bpmn20.xml, zip 和 bar 格式！");
                }
                // p.s. 此时 FileUploadUtils.upload() 返回字符串 fileName 前缀为 Constants.RESOURCE_PREFIX，需剔除
                // 详见: FileUploadUtils.getPathFileName(...)
                String fileName = FileUploadUtils.upload(Global.getProfile() + "/processDefiniton", file);
                if (StringUtils.isNotBlank(fileName)) {
                    String realFilePath = Global.getProfile() + fileName.substring(Constants.RESOURCE_PREFIX.length());
                    processDefinitionService.deployProcessDefinition(realFilePath);
                    return success();
                }
            }
            return error();
        }
        catch (Exception e) {
            log.error("上传流程定义文件失败！", e);
            return error(e.getMessage());
        }
    }

    @RequiresPermissions("process:definition:remove")
    @Log(title = "流程定义", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(processDefinitionService.deleteProcessDeploymentByIds(ids));
        }
        catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @Log(title = "流程定义", businessType = BusinessType.EXPORT)
    @RequiresPermissions("process:definition:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export() {
        List<ProcessDefinition> list = processDefinitionService.listProcessDefinition(new ProcessDefinition());
        ExcelUtil<ProcessDefinition> util = new ExcelUtil<>(ProcessDefinition.class);
        return util.exportExcel(list, "流程定义数据");
    }

//    @RequestMapping(value="/process", method= RequestMethod.POST)
//    public void startProcessInstance(@RequestBody StartProcessRepresentation startProcessRepresentation) {
//        myService.startProcess(startProcessRepresentation.getAssignee());
////        myService.startProcess();
//    }

    @RequestMapping(value="/process", method= RequestMethod.GET)
    public void startProcessInstance(@RequestParam String assignee) {
        processDefinitionService.startProcess(assignee);
//        myService.startProcess();
    }

    @RequestMapping(value="/tasks", method= RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public List<TaskRepresentation> getTasks(@RequestParam String assignee) {
        List<Task> tasks = processDefinitionService.getTasks(assignee);
        List<TaskRepresentation> dtos = new ArrayList<TaskRepresentation>();
        for (Task task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        return dtos;
    }

    static class StartProcessRepresentation {

        private String assignee;

        public String getAssignee() {
            return assignee;
        }

        public void setAssignee(String assignee) {
            this.assignee = assignee;
        }
    }

    static class TaskRepresentation {

        private String id;
        private String name;

        public TaskRepresentation(String id, String name) {
            this.id = id;
            this.name = name;
        }

         public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

    }

}

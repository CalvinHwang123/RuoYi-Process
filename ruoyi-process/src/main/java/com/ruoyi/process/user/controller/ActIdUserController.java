package com.ruoyi.process.user.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.process.group.service.IActIdGroupService;
import com.ruoyi.process.user.domain.ActIdUser;
import com.ruoyi.process.user.service.IActIdUserService;
import com.ruoyi.system.domain.SysUser;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程用户Controller
 *
 * @author Xianlu Tech
 * @date 2019-10-02
 */
@Controller
@RequestMapping("/process/user")
public class ActIdUserController extends BaseController {
    private String prefix = "process/user";

    @Autowired
    private IActIdUserService actIdUserService;

    @Autowired
    private IActIdGroupService actIdGroupService;

    @Autowired
    private IdentityService identityService;

    @RequiresPermissions("process:user:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/user";
    }

    /**
     * 查询流程用户列表
     */
    @RequiresPermissions("process:user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActIdUser actIdUser)
    {
        startPage();
        List<ActIdUser> list = actIdUserService.selectActIdUserList(actIdUser);
        return getDataTable(list);
    }

    /**
     * 导出流程用户列表
     */
    @RequiresPermissions("process:user:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ActIdUser actIdUser)
    {
        List<ActIdUser> list = actIdUserService.selectActIdUserList(actIdUser);
        ExcelUtil<ActIdUser> util = new ExcelUtil<ActIdUser>(ActIdUser.class);
        return util.exportExcel(list, "user");
    }

    /**
     * 新增流程用户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("groups", actIdGroupService.selectActIdGroupList(null));
        return prefix + "/add";
    }

    /**
     * 新增保存流程用户
     */
    @RequiresPermissions("process:user:add")
    @Log(title = "流程用户", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ActIdUser actIdUser)
    {
        int rows = actIdUserService.insertActIdUser(actIdUser);
        String[] groupIds = actIdUser.getGroupIds();
        for (String groupId: groupIds) {
            identityService.createMembership(actIdUser.getId(), groupId);
        }
        return toAjax(rows);
    }

    /**
     * 修改流程用户
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        ActIdUser actIdUser = actIdUserService.selectActIdUserById(id);
        mmap.put("actIdUser", actIdUser);
        mmap.put("groups", actIdUserService.selectGroupByUserId(id));
        return prefix + "/edit";
    }

    /**
     * 修改保存流程用户
     */
    @RequiresPermissions("process:user:edit")
    @Log(title = "流程用户", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ActIdUser actIdUser)
    {
        int rows = actIdUserService.updateActIdUser(actIdUser);
        String[] groupIds = actIdUser.getGroupIds();
        List<Group> groupList = identityService.createGroupQuery().groupMember(actIdUser.getId()).list();
        // 先删后增
        groupList.forEach(existGroup -> {
            identityService.deleteMembership(actIdUser.getId(), existGroup.getId());
        });
        for (String groupId: groupIds) {
            identityService.createMembership(actIdUser.getId(), groupId);
        }
        return toAjax(rows);
    }

    /**
     * 删除流程用户
     */
    @RequiresPermissions("process:user:remove")
    @Log(title = "流程用户", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(actIdUserService.deleteActIdUserByIds(ids));
    }

    /**
     * 选择系统用户
     */
    @GetMapping("/authUser/selectUser")
    public String selectUser() {
        return prefix + "/selectUser";
    }

    @RequiresPermissions("system:user:list")
    @PostMapping("/systemUserList")
    @ResponseBody
    public TableDataInfo systemUserList(SysUser user) {
        startPage();
        List<SysUser> list = actIdUserService.selectUnAssociatedSystemUserList(user);
        return getDataTable(list);
    }

}

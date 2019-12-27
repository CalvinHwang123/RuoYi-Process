package com.ruoyi.process.leave.service.impl;

import com.github.pagehelper.Page;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.process.leave.domain.BizLeaveVo;
import com.ruoyi.process.leave.mapper.BizLeaveMapper;
import com.ruoyi.process.leave.service.IBizLeaveService;
import com.ruoyi.process.todoitem.domain.BizTodoItem;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 请假业务Service业务层处理
 *
 * @author Xianlu Tech
 * @date 2019-10-11
 */
@Service
@Transactional
public class BizLeaveServiceImpl implements IBizLeaveService {
    @Autowired
    private BizLeaveMapper bizLeaveMapper;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private IBizTodoItemService bizTodoItemService;

    /**
     * 查询请假业务
     *
     * @param id 请假业务ID
     * @return 请假业务
     */
    @Override
    public BizLeaveVo selectBizLeaveById(Long id) {
        BizLeaveVo leave = bizLeaveMapper.selectBizLeaveById(id);
        SysUser sysUser = userMapper.selectUserByLoginName(leave.getApplyUser());
        if (sysUser != null) {
            leave.setApplyUserName(sysUser.getUserName());
        }
        return leave;
    }

    /**
     * 查询请假业务列表
     *
     * @param bizLeave 请假业务
     * @return 请假业务
     */
    @Override
    public List<BizLeaveVo> selectBizLeaveList(BizLeaveVo bizLeave) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        // PageHelper 仅对第一个 List 分页
        Page<BizLeaveVo> list = (Page<BizLeaveVo>) bizLeaveMapper.selectBizLeaveList(bizLeave);
        Page<BizLeaveVo> returnList = new Page<>();
        for (BizLeaveVo leave: list) {
            SysUser sysUser = userMapper.selectUserByLoginName(leave.getCreateBy());
            if (sysUser != null) {
                leave.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(leave.getApplyUser());
            if (sysUser2 != null) {
                leave.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(leave.getInstanceId())) {
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(leave.getInstanceId())
//                        .singleResult();
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    leave.setTaskId(task.getId());
                    leave.setTaskName(task.getName());
                } else {
                    leave.setTaskName("已办结");
                }
            } else {
                leave.setTaskName("未启动");
            }
            returnList.add(leave);
        }
        returnList.setTotal(CollectionUtils.isEmpty(list) ? 0 : list.getTotal());
        returnList.setPageNum(pageNum);
        returnList.setPageSize(pageSize);
        return returnList;
    }

    /**
     * 新增请假业务
     *
     * @param bizLeave 请假业务
     * @return 结果
     */
    @Override
    public int insertBizLeave(BizLeaveVo bizLeave) {
        bizLeave.setCreateBy(ShiroUtils.getLoginName());
        bizLeave.setCreateTime(DateUtils.getNowDate());
        return bizLeaveMapper.insertBizLeave(bizLeave);
    }

    /**
     * 修改请假业务
     *
     * @param bizLeave 请假业务
     * @return 结果
     */
    @Override
    public int updateBizLeave(BizLeaveVo bizLeave) {
        bizLeave.setUpdateTime(DateUtils.getNowDate());
        return bizLeaveMapper.updateBizLeave(bizLeave);
    }

    /**
     * 删除请假业务对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizLeaveByIds(String ids) {
        return bizLeaveMapper.deleteBizLeaveByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除请假业务信息
     *
     * @param id 请假业务ID
     * @return 结果
     */
    @Override
    public int deleteBizLeaveById(Long id) {
        return bizLeaveMapper.deleteBizLeaveById(id);
    }

    /**
     * 启动流程
     * @param entity
     * @param applyUserId
     * @return
     */
    @Override
    public ProcessInstance submitApply(BizLeaveVo entity, String applyUserId) {
        entity.setApplyUser(applyUserId);
        entity.setApplyTime(DateUtils.getNowDate());
        entity.setUpdateBy(applyUserId);
        bizLeaveMapper.updateBizLeave(entity);
        String businessKey = entity.getId().toString(); // 实体类 ID，作为流程的业务 key

        // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
        identityService.setAuthenticatedUserId(applyUserId);

        ProcessInstance processInstance = runtimeService // 启动流程时设置业务 key
                .startProcessInstanceByKey("leave", businessKey);
        String processInstanceId = processInstance.getId();
        entity.setInstanceId(processInstanceId); // 建立双向关系
        bizLeaveMapper.updateBizLeave(entity);

        // 下一节点处理人待办事项
        bizTodoItemService.insertTodoItem(processInstanceId, entity, "leave");

        return processInstance;
    }

    /**
     * 查询待办任务
     */
    @Transactional(readOnly = true)
    public List<BizLeaveVo> findTodoTasks(BizLeaveVo leave, String userId) {
        List<BizLeaveVo> results = new ArrayList<>();
        List<Task> tasks = new ArrayList<Task>();

        // 根据当前人的ID查询
        List<Task> todoList = taskService.createTaskQuery().processDefinitionKey("leave").taskAssignee(userId).list();

        // 根据当前人未签收的任务
        List<Task> unsignedTasks = taskService.createTaskQuery().processDefinitionKey("leave").taskCandidateUser(userId).list();

        // 合并
        tasks.addAll(todoList);
        tasks.addAll(unsignedTasks);

        // 根据流程的业务ID查询实体并关联
        for (Task task : tasks) {
            String processInstanceId = task.getProcessInstanceId();

            // 条件过滤 1
            if (StringUtils.isNotBlank(leave.getInstanceId()) && !leave.getInstanceId().equals(processInstanceId)) {
                continue;
            }

            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            String businessKey = processInstance.getBusinessKey();
            BizLeaveVo leave2 = bizLeaveMapper.selectBizLeaveById(new Long(businessKey));

            // 条件过滤 2
            if (StringUtils.isNotBlank(leave.getType()) && !leave.getType().equals(leave2.getType())) {
                continue;
            }

            leave2.setTaskId(task.getId());
            leave2.setTaskName(task.getName());

            SysUser sysUser = userMapper.selectUserByLoginName(leave2.getApplyUser());
            leave2.setApplyUserName(sysUser.getUserName());

            results.add(leave2);
        }
        return results;
    }

    /**
     * 完成任务
     * @param leave
     * @param saveEntity
     * @param taskId
     * @param variables
     */
    @Override
    public void complete(BizLeaveVo leave, Boolean saveEntity, String taskId, Map<String, Object> variables) {
        if (saveEntity) {
            bizLeaveMapper.updateBizLeave(leave);
        }
        // 只有签收任务，act_hi_taskinst 表的 assignee 字段才不为 null
        taskService.claim(taskId, ShiroUtils.getLoginName());
        taskService.complete(taskId, variables);

        // 更新待办事项状态
        BizTodoItem query = new BizTodoItem();
        query.setTaskId(taskId);
        // 考虑到候选用户组，会有多个 todoitem 办理同个 task
        List<BizTodoItem> updateList = CollectionUtils.isEmpty(bizTodoItemService.selectBizTodoItemList(query)) ? null : bizTodoItemService.selectBizTodoItemList(query);
        for (BizTodoItem update: updateList) {
            // 找到当前登录用户的 todoitem，置为已办
            if (update.getTodoUserId().equals(ShiroUtils.getLoginName())) {
                update.setIsView("1");
                update.setIsHandle("1");
                update.setHandleUserId(ShiroUtils.getLoginName());
                update.setHandleUserName(ShiroUtils.getSysUser().getUserName());
                update.setHandleTime(DateUtils.getNowDate());
                bizTodoItemService.updateBizTodoItem(update);
            } else {
                bizTodoItemService.deleteBizTodoItemById(update.getId()); // 删除候选用户组其他 todoitem
            }
        }

        // 下一节点处理人待办事项
        bizTodoItemService.insertTodoItem(leave.getInstanceId(), leave, "leave");
    }

    /**
     * 查询已办列表
     * @param bizLeave
     * @param userId
     * @return
     */
    @Override
    public List<BizLeaveVo> findDoneTasks(BizLeaveVo bizLeave, String userId) {
        List<BizLeaveVo> results = new ArrayList<>();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey("leave")
                .taskAssignee(userId)
                .finished()
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();

        // 根据流程的业务ID查询实体并关联
        for (HistoricTaskInstance instance : list) {
            String processInstanceId = instance.getProcessInstanceId();

            // 条件过滤 1
            if (StringUtils.isNotBlank(bizLeave.getInstanceId()) && !bizLeave.getInstanceId().equals(processInstanceId)) {
                continue;
            }

            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

            String businessKey = processInstance.getBusinessKey();
            BizLeaveVo leave2 = bizLeaveMapper.selectBizLeaveById(new Long(businessKey));

            // 条件过滤 2
            if (StringUtils.isNotBlank(bizLeave.getType()) && !bizLeave.getType().equals(leave2.getType())) {
                continue;
            }

            leave2.setTaskId(instance.getId());
            leave2.setTaskName(instance.getName());
            leave2.setDoneTime(instance.getEndTime());

            SysUser sysUser = userMapper.selectUserByLoginName(leave2.getApplyUser());
            leave2.setApplyUserName(sysUser.getUserName());

            results.add(leave2);
        }
        return results;
    }

}

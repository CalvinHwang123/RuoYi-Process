package com.ruoyi.process.group.service;

import com.ruoyi.process.group.domain.ActIdGroup;
import com.ruoyi.process.user.domain.ActIdUser;

import java.util.List;

/**
 * 流程用户组Service接口
 *
 * @author Xianlu Tech
 * @date 2019-10-02
 */
public interface IActIdGroupService
{
    /**
     * 查询流程用户组
     *
     * @param id 流程用户组ID
     * @return 流程用户组
     */
    public ActIdGroup selectActIdGroupById(String id);

    /**
     * 查询流程用户组列表
     *
     * @param actIdGroup 流程用户组
     * @return 流程用户组集合
     */
    public List<ActIdGroup> selectActIdGroupList(ActIdGroup actIdGroup);

    /**
     * 新增流程用户组
     *
     * @param actIdGroup 流程用户组
     * @return 结果
     */
    public int insertActIdGroup(ActIdGroup actIdGroup);

    /**
     * 修改流程用户组
     *
     * @param actIdGroup 流程用户组
     * @return 结果
     */
    public int updateActIdGroup(ActIdGroup actIdGroup);

    /**
     * 批量删除流程用户组
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteActIdGroupByIds(String ids);

    /**
     * 删除流程用户组信息
     *
     * @param id 流程用户组ID
     * @return 结果
     */
    public int deleteActIdGroupById(String id);

    /**
     * 根据用户组ID查询所在用户
     * @param groupId 流程用户组ID
     * @return 关联用户集合
     */
    List<ActIdUser> selectUserByGroupId(String groupId);
}

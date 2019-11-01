package com.ruoyi.process.group.mapper;

import com.ruoyi.process.group.domain.ActIdGroup;
import java.util.List;

/**
 * 流程用户组Mapper接口
 * 
 * @author Xianlu Tech
 * @date 2019-10-02
 */
public interface ActIdGroupMapper 
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
     * 删除流程用户组
     * 
     * @param id 流程用户组ID
     * @return 结果
     */
    public int deleteActIdGroupById(String id);

    /**
     * 批量删除流程用户组
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteActIdGroupByIds(String[] ids);
}

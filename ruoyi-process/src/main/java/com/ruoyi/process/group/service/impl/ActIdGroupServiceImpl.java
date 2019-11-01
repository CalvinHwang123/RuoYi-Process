package com.ruoyi.process.group.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.process.group.domain.ActIdGroup;
import com.ruoyi.process.group.mapper.ActIdGroupMapper;
import com.ruoyi.process.group.service.IActIdGroupService;
import com.ruoyi.process.user.domain.ActIdUser;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程用户组Service业务层处理
 *
 * @author Xianlu Tech
 * @date 2019-10-02
 */
@Service
public class ActIdGroupServiceImpl implements IActIdGroupService
{
    @Autowired
    private ActIdGroupMapper actIdGroupMapper;

    @Autowired
    private IdentityService identityService;

    /**
     * 查询流程用户组
     *
     * @param id 流程用户组ID
     * @return 流程用户组
     */
    @Override
    public ActIdGroup selectActIdGroupById(String id)
    {
        return actIdGroupMapper.selectActIdGroupById(id);
    }

    /**
     * 查询流程用户组列表
     *
     * @param actIdGroup 流程用户组
     * @return 流程用户组
     */
    @Override
    public List<ActIdGroup> selectActIdGroupList(ActIdGroup actIdGroup)
    {
        return actIdGroupMapper.selectActIdGroupList(actIdGroup);
    }

    /**
     * 新增流程用户组
     *
     * @param actIdGroup 流程用户组
     * @return 结果
     */
    @Transactional
    @Override
    public int insertActIdGroup(ActIdGroup actIdGroup)
    {
        return actIdGroupMapper.insertActIdGroup(actIdGroup);
    }

    /**
     * 修改流程用户组
     *
     * @param actIdGroup 流程用户组
     * @return 结果
     */
    @Transactional
    @Override
    public int updateActIdGroup(ActIdGroup actIdGroup)
    {
        return actIdGroupMapper.updateActIdGroup(actIdGroup);
    }

    /**
     * 删除流程用户组对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteActIdGroupByIds(String ids)
    {
        return actIdGroupMapper.deleteActIdGroupByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除流程用户组信息
     *
     * @param id 流程用户组ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteActIdGroupById(String id)
    {
        return actIdGroupMapper.deleteActIdGroupById(id);
    }

    @Override
    public List<ActIdUser> selectUserByGroupId(String groupId) {
        List<User> userList = identityService.createUserQuery().list();
        List<ActIdUser> idUserList = new ArrayList<>();
        userList.forEach(user -> {
            ActIdUser idUser= new ActIdUser();
            idUser.setId(user.getId());
            idUser.setFirst(user.getFirstName());
            idUser.setEmail(user.getEmail());
            idUserList.add(idUser);
        });
        List<User> groupUserList = identityService.createUserQuery().memberOfGroup(groupId).list();
        idUserList.forEach(idUser -> {
            groupUserList.forEach(groupUser -> {
                if (idUser.getId().equals(groupUser.getId())) {
                    idUser.setFlag(true);
                }
            });
        });
        return idUserList;
    }
}

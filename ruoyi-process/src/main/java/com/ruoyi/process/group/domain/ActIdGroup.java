package com.ruoyi.process.group.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 流程用户组对象 act_id_group
 *
 * @author Xianlu Tech
 * @date 2019-10-02
 */
public class ActIdGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "组ID")
    private String id;

    /** 版本 */
    private Long rev;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 类型 */
    private String type;

    private String[] userIds;

    /** 用户是否存在此用户组标识 默认不存在 */
    private boolean flag = false;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setRev(Long rev)
    {
        this.rev = rev;
    }

    public Long getRev()
    {
        return rev;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public String[] getUserIds() {
        return userIds;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("rev", getRev())
            .append("name", getName())
            .append("type", getType())
            .toString();
    }
}

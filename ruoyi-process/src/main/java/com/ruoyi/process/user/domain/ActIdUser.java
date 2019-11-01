package com.ruoyi.process.user.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流程用户对象 act_id_user
 *
 * @author Xianlu Tech
 * @date 2019-10-02
 */
public class ActIdUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "用户ID")
    private String id;

    /** 版本 */
    private Long rev;

    /** 名字 */
    @Excel(name = "名字")
    private String first;

    /** 姓氏 */
    private String last;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 密码 */
    private String pwd;

    /** 头像 */
    private String pictureId;

    /** 用户组 */
    private String[] groupIds;

    /** 用户组是否存在此用户标识 默认不存在 */
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
    public void setFirst(String first)
    {
        this.first = first;
    }

    public String getFirst()
    {
        return first;
    }
    public void setLast(String last)
    {
        this.last = last;
    }

    public String getLast()
    {
        return last;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }

    public String getPwd()
    {
        return pwd;
    }
    public void setPictureId(String pictureId)
    {
        this.pictureId = pictureId;
    }

    public String getPictureId()
    {
        return pictureId;
    }

    public String[] getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String[] groupIds) {
        this.groupIds = groupIds;
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
            .append("first", getFirst())
            .append("last", getLast())
            .append("email", getEmail())
            .append("pwd", getPwd())
            .append("pictureId", getPictureId())
            .toString();
    }
}

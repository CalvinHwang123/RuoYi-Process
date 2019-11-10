package com.ruoyi.process.general.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProcessMapper {

    @Select("select user_id_ from act_id_membership where group_id_ = (select group_id_ from act_ru_identitylink where task_id_ = #{taskId})")
    List<String> selectTodoUserListByTaskId(@Param(value = "taskId") String taskId);
}

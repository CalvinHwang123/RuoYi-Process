package com.ruoyi.process.todoitem.mapper;

import com.ruoyi.process.todoitem.domain.BizTodoItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 待办事项Mapper接口
 *
 * @author Xianlu Tech
 * @date 2019-11-08
 */
public interface BizTodoItemMapper {
    /**
     * 查询待办事项
     *
     * @param id 待办事项ID
     * @return 待办事项
     */
    public BizTodoItem selectBizTodoItemById(Long id);

    /**
     * 查询待办事项列表
     *
     * @param bizTodoItem 待办事项
     * @return 待办事项集合
     */
    public List<BizTodoItem> selectBizTodoItemList(BizTodoItem bizTodoItem);

    /**
     * 新增待办事项
     *
     * @param bizTodoItem 待办事项
     * @return 结果
     */
    public int insertBizTodoItem(BizTodoItem bizTodoItem);

    /**
     * 修改待办事项
     *
     * @param bizTodoItem 待办事项
     * @return 结果
     */
    public int updateBizTodoItem(BizTodoItem bizTodoItem);

    /**
     * 删除待办事项
     *
     * @param id 待办事项ID
     * @return 结果
     */
    public int deleteBizTodoItemById(Long id);

    /**
     * 批量删除待办事项
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizTodoItemByIds(String[] ids);

    @Select("select * from biz_todo_item where task_id = #{taskId}")
    BizTodoItem selectTodoItemByTaskId(@Param(value = "taskId")String taskId);
}

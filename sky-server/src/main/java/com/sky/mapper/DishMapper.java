package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 菜品数据访问层（Mapper）
 * 负责对 dish（菜品表）的增删改查操作
 */
@Mapper  // 告诉 MyBatis 这是一个 Mapper 接口，Spring 会自动生成实现类
public interface DishMapper {

    /**
     * 新增菜品
     * @param dish 菜品实体对象
     */
    @AutoFill(OperationType.INSERT)  // 自动填充公共字段（createTime, createUser, updateTime, updateUser）
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 菜品名称、分类ID、状态
     * @return  PageHelper 分页对象,total（总数）和 records（当前页数据）
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据 ID 查询菜品
     * @param id 菜品 ID
     * @return Dish 菜品实体对象
     */
    Dish getById(Long id);

    /**
     * 修改菜品
     * @param dish 菜品实体对象
     */
    @AutoFill(OperationType.UPDATE)  // 自动填充公共字段（updateTime, updateUser）
    void update(Dish dish);

    /**
     * 批量删除菜品（根据 ID 列表）
     * @param ids 菜品 ID 列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据分类 ID 查询起售中的菜品,带口味信息
     * 用于用户端展示菜品列表
     * @param categoryId 分类 ID
     * @return List<DishVO> 菜品视图对象列表,包含口味数据
     */
    List<DishVO> listWithFlavor(Long categoryId);
}
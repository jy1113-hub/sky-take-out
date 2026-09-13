package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 插入菜品（AOP自动填创建时间、创建人）
     */
    @AutoFill(OperationType.INSERT)
    @Insert("insert into dish (name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) " +
            "values (#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Dish dish);

    /**
     * 菜品分页查询（带分类名称）
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 动态更新菜品（AOP自动填更新时间、更新人）
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 批量删除菜品
     */
    void deleteByIds(List<Long> ids);

    /**
     * 用户端：根据 Dish（含分类id和状态）查询起售中的菜品
     */
    List<DishVO> listWithFlavor(Dish dish);

    /**
     * 管理端：根据分类id查询菜品（不过滤状态）
     */
    List<DishVO> listWithFlavorByCategoryId(Long categoryId);
}
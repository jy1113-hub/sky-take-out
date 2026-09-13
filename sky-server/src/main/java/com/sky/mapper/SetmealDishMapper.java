package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 批量插入套餐和菜品的关联关系
     * @param setmealDishes 套餐菜品关系列表
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id删除关联关系
     * @param setmealId 套餐id
     */
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐id列表批量删除关联关系
     * @param setmealIds 套餐id列表
     */
    void deleteBySetmealIds(List<Long> setmealIds);

    /**
     * 根据套餐id查询关联的菜品
     * @param setmealId 套餐id
     * @return 套餐菜品关系列表
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

    /**
     * 根据菜品ID列表，查询关联的套餐ID列表
     * @param dishIds 菜品ID列表
     * @return 套餐ID列表
     */
    @Select("<script>" +
            "select setmeal_id from setmeal_dish where dish_id in " +
            "<foreach collection='list' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
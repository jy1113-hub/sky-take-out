
package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface SetmealDishMapper {

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
// SetmealDishMapper.java
package com.sky.mapper;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface SetmealDishMapper {
    void insertBatch(List<SetmealDish> setmealDishes);
    void deleteBySetmealId(Long setmealId);
    void deleteBySetmealIds(List<Long> setmealIds);
}
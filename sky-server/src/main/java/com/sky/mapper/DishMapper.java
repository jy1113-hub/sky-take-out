// DishMapper.java
package com.sky.mapper;
import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface DishMapper {
    void insert(Dish dish);
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
    void deleteByIds(List<Long> ids);
    Dish getById(Long id);
    void update(Dish dish);
}
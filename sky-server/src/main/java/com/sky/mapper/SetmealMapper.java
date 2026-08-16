// SetmealMapper.java
package com.sky.mapper;
import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface SetmealMapper {
    void insert(Setmeal setmeal);
    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
    void deleteByIds(List<Long> ids);
    void update(Setmeal setmeal);
}
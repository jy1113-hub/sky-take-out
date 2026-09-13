package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 新增套餐,AOP自动填创建信息
     */
    @AutoFill(OperationType.INSERT)
    @Insert("insert into setmeal (category_id, name, price, status, description, image, create_time, update_time, create_user, update_user) " +
            "values (#{categoryId}, #{name}, #{price}, #{status}, #{description}, #{image}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Setmeal setmeal);

    /**
     * 套餐分页查询
     */
    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据id查询套餐
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 修改套餐（AOP自动填更新信息）
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 根据分类id查询起售中的套餐（用户端）
     */
    @Select("select * from setmeal where category_id = #{categoryId} and status = 1")
    List<Setmeal> list(Long categoryId);

    /**
     * 根据套餐id查询包含的菜品（用户端展示套餐详情）
     */
    List<DishItemVO> getDishItemBySetmealId(Long id);
}
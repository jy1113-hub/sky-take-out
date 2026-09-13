package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    //新增套餐和对应的菜品
    void saveWithDish(SetmealDTO setmealDTO);

    //套餐分页查询
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    // 批量删除套餐
    void deleteBatch(List<Long> ids);

    //根据id查询套餐和对应的菜品（回显）
    SetmealVO getByIdWithDish(Long id);

    //修改套餐和对应的菜品
    void update(SetmealDTO setmealDTO);

    // 起售/停售套餐
    void startOrStop(Integer status, Long id);

    //根据分类id查询套餐（用户端）
    List<Setmeal> list(Long categoryId);

    //根据套餐id查询包含的菜品（用户端）
    List<DishItemVO> getDishItemBySetmealId(Long id);
}
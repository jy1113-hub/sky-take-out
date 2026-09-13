package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * 菜品服务层接口
 */
public interface DishService {

    /**
     * 新增菜品和对应的口味
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品（需要判断起售和套餐关联）
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品和口味（用于编辑回显）
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品和对应的口味
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 用户端：根据 Dish 对象（含分类id和状态）查询起售中的菜品（带口味）
     */
    List<DishVO> listWithFlavor(Dish dish);

    /**
     * 管理端：根据分类id查询菜品（不过滤状态）
     */
    List<DishVO> listWithFlavor(Long categoryId);

    /**
     * 起售/停售菜品
     */
    void startOrStop(Integer status, Long id);
}
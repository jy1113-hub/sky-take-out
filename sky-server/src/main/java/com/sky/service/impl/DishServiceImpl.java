package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品和对应的口味
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 1. 向菜品表插入1条数据 ,数据回显，自动生成id
        dishMapper.insert(dish);
        // 2. 获取插入后生成的菜品id
        Long dishId = dish.getId();
        // 3. 向口味表插入n条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> flavor.setDishId(dishId));
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除菜品,要判断起售和套餐关联
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 1. 判断当前菜品是否起售中
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 2. 判断当前菜品是否被套餐关联
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 3. 删除菜品表数据
        dishMapper.deleteByIds(ids);
        // 4. 删除口味表数据
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 根据id查询菜品和口味（回显）
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * 修改菜品和口味
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 1. 修改菜品表
        dishMapper.update(dish);
        // 2. 删除原有的口味数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        // 3. 重新插入新的口味数据（需要赋予菜品id）
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> flavor.setDishId(dishDTO.getId()));
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 根据分类ID查询起售中的菜品列表（带口味数据）
     * 用于用户端小程序展示菜品时，同时展示菜品的基本信息和可选口味
     *
     * @param categoryId 分类ID
     * @return 菜品视图对象列表，每个DishVO中包含菜品信息和对应的口味列表
     */
    @Override
    public List<DishVO> listWithFlavor(Long categoryId) {
        // 直接调用 Mapper 层方法，Mapper 已在 XML 中写好 SQL：
        // SELECT d.*, f.* FROM dish d LEFT JOIN dish_flavor f ON d.id = f.dish_id
        // WHERE d.category_id = #{categoryId} AND d.status = 1
        // 并自动把一对多的数据（一个菜品对应多个口味）组装到 DishVO 的 flavors 字段中
        return dishMapper.listWithFlavor(categoryId);
    }
    /**
     * 起售/停售菜品（启用/禁用菜品）
     * 修改 dish 表中的 status 字段：1=起售（启用），0=停售（禁用）
     *
     * @param status 目标状态：1=起售（启用），0=停售（禁用）
     * @param id     菜品ID，用来找到要修改的菜品
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }
}
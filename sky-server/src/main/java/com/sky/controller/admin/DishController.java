package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 管理端-菜品相关接口
 * 负责菜品的增删改查、起售停售，并配合Redis清理缓存
 */
@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 清理缓存
     * @param pattern 缓存key的模式，例如 "dish_*" 表示删除所有菜品缓存
     */
    private void cleanCache(String pattern) {
        // 1. 根据模式找出所有匹配的key
        Set keys = redisTemplate.keys(pattern);
        // 2. 批量删除这些key
        redisTemplate.delete(keys);
    }

    /**
     * 新增菜品
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result<String> save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        // 新增后清理该分类下的菜品缓存
        cleanCache("dish_" + dishDTO.getCategoryId());
        return Result.success();
    }

    /**
     * 批量删除菜品
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("批量删除菜品：{}", ids);
        dishService.deleteBatch(ids);
        // 删除后清理所有菜品缓存
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 修改菜品
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result<String> update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        // 修改后清理该分类下的菜品缓存
        cleanCache("dish_" + dishDTO.getCategoryId());
        return Result.success();
    }

    /**
     * 菜品起售/停售
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        log.info("菜品起售停售：status={}, id={}", status, id);
        dishService.startOrStop(status, id);
        // 起售停售后清理所有菜品缓存
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 菜品分页查询
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询：{}", dishPageQueryDTO);
        return Result.success(dishService.pageQuery(dishPageQueryDTO));
    }

    /**
     * 根据id查询菜品（用于编辑回显）
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}", id);
        return Result.success(dishService.getByIdWithFlavor(id));
    }

    /**
     * 根据分类id查询菜品（管理端用，不过滤状态）
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        log.info("根据分类id查询菜品：{}", categoryId);
        return Result.success(dishService.listWithFlavor(categoryId));
    }
}
package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户端-菜品浏览接口
 * 用户端只展示起售中的菜品，且带Redis缓存
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "C端-菜品浏览接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品（带缓存）
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        // 1. 拼接缓存key，格式：dish_分类id
        String key = "dish_" + categoryId;

        // 2. 先查Redis缓存
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (list != null && !list.isEmpty()) {
            // 缓存命中，直接返回，不再查数据库
            log.info("从缓存中获取菜品数据：{}", key);
            return Result.success(list);
        }

        // 3. 缓存没有，查询数据库（只查起售中的）
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        list = dishService.listWithFlavor(dish);

        // 4. 将查询结果存入Redis
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }
}
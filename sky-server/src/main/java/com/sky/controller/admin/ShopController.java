package com.sky.controller.admin;

import com.sky.constant.RedisConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺操作接口
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺操作相关接口")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺的营业状态
     * @param status 状态值：1为营业，0为打烊
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result<String> setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态：{}", status == 1 ? "营业中" : "打烊中");
        // 将状态存入 Redis，key 为 SHOP_STATUS，value 为传入的 status
        redisTemplate.opsForValue().set(RedisConstant.SHOP_STATUS, status);
        return Result.success();
    }

    /**
     * 获取店铺的营业状态
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus() {
        // 从 Redis 获取状态值
        Integer status = (Integer) redisTemplate.opsForValue().get(RedisConstant.SHOP_STATUS);
        log.info("获取店铺营业状态：{}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }
}
package com.sky.service.impl;

import com.sky.mapper.*;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /** 查询今日运营数据 */
    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);

        // 1. 营业额（只统计已完成的订单金额，状态=5）
        map.put("status", 5);
        Double turnover = orderMapper.sumByMap(map);
        turnover = turnover == null ? 0.0 : turnover;

        // 2. 有效订单数（只统计已完成的订单，状态=5）
        Integer validOrderCount = orderMapper.countByMap(map);
        if (validOrderCount == null) validOrderCount = 0;

        // 3. 平均客单价
        Double unitPrice = 0.0;
        if (validOrderCount > 0) {
            unitPrice = turnover / validOrderCount;
        }

        // 4. 订单完成率（有效订单 / 全部订单）
        Integer totalOrderCount = orderMapper.countByMapTotal(map);
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != null && totalOrderCount > 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }

        // 5. 新增用户数
        map.remove("status");
        Integer newUsers = userMapper.countByMap(map);
        if (newUsers == null) newUsers = 0;

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }

    /** 查询订单管理数据（今日各个状态订单数量） */
    @Override
    public OrderOverViewVO getOrderOverView() {
        Map<String, Object> map = new HashMap<>();
        // 今日零点
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));

        // 待接单 (状态 2)
        map.put("status", 2);
        Integer waitingOrders = orderMapper.countByMap(map);

        // 待派送 (状态 3)
        map.put("status", 3);
        Integer deliveredOrders = orderMapper.countByMap(map);

        // 已完成 (状态 5)
        map.put("status", 5);
        Integer completedOrders = orderMapper.countByMap(map);

        // 已取消 (状态 6)
        map.put("status", 6);
        Integer cancelledOrders = orderMapper.countByMap(map);

        // 全部订单 (不传 status，直接查今日)
        map.remove("status");
        Integer allOrders = orderMapper.countByMap(map);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /** 查询菜品总览 */
    @Override
    public DishOverViewVO getDishOverView() {
        // 1起售 0停售
        Integer sold = dishMapper.countByStatus(1);
        Integer discontinued = dishMapper.countByStatus(0);
        return new DishOverViewVO(sold, discontinued);
    }

    /** 查询套餐总览 */
    @Override
    public SetmealOverViewVO getSetmealOverView() {
        // 1起售 0停售
        Integer sold = setmealMapper.countByStatus(1);
        Integer discontinued = setmealMapper.countByStatus(0);
        return new SetmealOverViewVO(sold, discontinued);
    }
}
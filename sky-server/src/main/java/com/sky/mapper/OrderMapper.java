package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /** 插入订单 */
    void insert(Orders orders);

    /** 根据订单号查询订单 */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /** 根据订单id查询订单 */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /** 更新订单信息 */
    void update(Orders orders);

    /** 根据状态和下单时间查询订单（定时任务用） */
    List<Orders> getByStatusAndOrderTimeLT(@Param("status") Integer status, @Param("orderTime") LocalDateTime orderTime);

    /** 统计营业额 */
    Double sumByMap(Map<String, Object> map);

    /** 根据条件统计订单数量 */
    Integer countByMap(Map<String, Object> map);

    /** 统计全部订单数量（不按状态过滤） */
    Integer countByMapTotal(Map<String, Object> map);

    /** 查询销量排名Top10 */
    List<Map<String, Object>> getSalesTop10(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);
}
package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Insert;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    @Insert("insert into orders (number, status, user_id, address_book_id, order_time, amount, pay_method, remark, phone, address, consignee) " +
            "values (#{number}, #{status}, #{userId}, #{addressBookId}, #{orderTime}, #{amount}, #{payMethod}, #{remark}, #{phone}, #{address}, #{consignee})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Orders orders);

    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    void update(Orders orders);
}
package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> list(ShoppingCart shoppingCart);
    void updateNumberById(ShoppingCart shoppingCart);
    void insert(ShoppingCart shoppingCart);
    List<ShoppingCart> listByUserId(Long userId);
    void deleteByUserId(Long userId);
}
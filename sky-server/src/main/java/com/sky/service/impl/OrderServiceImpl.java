package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务层实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 用户下单
     * 逻辑：
     * 1. 校验地址
     * 2. 校验购物车
     * 3. 插入订单
     * 4. 批量插入订单明细
     * 5. 清空购物车
     * 6. 返回订单信息
     */
    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 1. 校验地址
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 2. 校验购物车
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 3. 构造订单并插入
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(addressBook.getDetail());

        orderMapper.insert(orders);

        // 4. 构造订单明细并批量插入
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        // 5. 清空购物车
        shoppingCartMapper.deleteByUserId(userId);

        // 6. 返回结果
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }

    /**
     * 订单支付（模拟支付）
     * 逻辑：
     * 1. 根据订单号查询订单
     * 2. 更新订单状态：待接单 + 已支付
     * 3. 返回模拟支付结果
     */
    @Override
    @Transactional
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
        // 1. 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(ordersPaymentDTO.getOrderNumber());
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // 2. 修改订单状态
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        orders.setPayStatus(Orders.PAID);
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setPayMethod(ordersPaymentDTO.getPayMethod());
        orderMapper.update(orders);

        // 3. 返回模拟支付结果
        return OrderPaymentVO.builder()
                .nonceStr("mock_nonce")
                .paySign("mock_sign")
                .timeStamp(String.valueOf(System.currentTimeMillis() / 1000))
                .signType("RSA")
                .packageStr("prepay_id=mock")
                .build();
    }
}
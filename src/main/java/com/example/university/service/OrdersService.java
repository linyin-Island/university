package com.example.university.service;

import com.example.university.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author linyin
 * @since 2023-06-20
 */
public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);
}

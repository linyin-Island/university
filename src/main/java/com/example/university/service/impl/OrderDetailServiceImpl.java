package com.example.university.service.impl;

import com.example.university.entity.OrderDetail;
import com.example.university.mapper.OrderDetailMapper;
import com.example.university.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author linyin
 * @since 2023-06-23
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}

package com.example.university.mapper;

import com.example.university.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author linyin
 * @since 2023-06-20
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}

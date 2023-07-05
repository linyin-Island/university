package com.example.university.mapper;

import com.example.university.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单明细表 Mapper 接口
 * </p>
 *
 * @author linyin
 * @since 2023-06-23
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}

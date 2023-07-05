package com.example.university.Dto;


import com.example.university.entity.OrderDetail;
import com.example.university.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {
    //订单明细表 存储多个OrderDetail对象的列表，可以按照需要动态地添加、删除和修改 OrderDetail 对象
    private List<OrderDetail> orderDetails;
}

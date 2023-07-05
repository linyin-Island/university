package com.example.university.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.university.Dto.OrderDto;
import com.example.university.common.BaseContext;
import com.example.university.common.R;
import com.example.university.entity.OrderDetail;
import com.example.university.entity.Orders;
import com.example.university.entity.ShoppingCart;
import com.example.university.service.OrderDetailService;
import com.example.university.service.OrdersService;
import com.example.university.service.ShoppingCartService;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("cg");
    }

    /**
     * 历史订单
     * @param page
     * @param pageSize
     * @return
     */

    @GetMapping("/userPage")
    public R<Page> page(int page,int pageSize){
        //获取用户id 根据id查询订单表
        Long userId = BaseContext.getCurrentId();
        Page<Orders> pageInfo=new Page<>(page,pageSize);
        Page<OrderDto> ordersDtoPage=new Page<>(page,pageSize);

        //条件构建器
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        //根据当前用户id，查询rderDetail表中数据
        queryWrapper.eq(userId!=null,Orders::getUserId,userId);
        //按时间降序排列
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(pageInfo,queryWrapper);

        List<OrderDto> list=pageInfo.getRecords().stream().map(item->{
            OrderDto orderDto=new OrderDto();
            //获取orderId,然后根据这个id，去orderDetail表中查数据
            Long orderId= item.getId();
            LambdaQueryWrapper<OrderDetail> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId,orderId);
            List<OrderDetail> details = orderDetailService.list(wrapper);
            BeanUtils.copyProperties(item,orderDto);
            orderDto.setOrderDetails(details);
            return  orderDto;
        }).collect(Collectors.toList());

        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");
        ordersDtoPage.setRecords(list);

        return R.success(ordersDtoPage);
    }

    /**
     * orderId获取订单明细的数据
     * 把订单明细的数据的数据塞到购物车表中，
     * 不过在此之前要先把购物车表中的数据给清除
     * @param map
     * @return
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Map<String,String> map){
        String ids=map.get("id");
        Long id=Long.parseLong(ids);

        LambdaQueryWrapper<OrderDetail> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId,id);
        //orderDetailService的list方法执行查询操作
        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);
        //清空购物车
        shoppingCartService.clean();

        //获取用户id
        Long userId=BaseContext.getCurrentId();
        //把从order表中和order_details表中获取到的数据赋值给这个购物车对象
        List<ShoppingCart> shoppingCartList=orderDetailList.stream().map(item->{
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(userId);
            shoppingCart.setImage(item.getImage());
            Long dishId = item.getDishId();
            Long setmealId = item.getSetmealId();
            if (dishId != null) {
                //如果是菜品那就添加菜品的查询条件
                shoppingCart.setDishId(dishId);
            } else {
                //添加到购物车的是套餐
                shoppingCart.setSetmealId(setmealId);
            }
            shoppingCart.setName(item.getName());
            shoppingCart.setDishFlavor(item.getDishFlavor());
            shoppingCart.setNumber(item.getNumber());
            shoppingCart.setAmount(item.getAmount());
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());

        //把携带数据的购物车批量插入购物车表
        shoppingCartService.saveBatch(shoppingCartList);
        return R.success("cg");

    }

    /**
     * 订单明细
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number, String beginTime, String endTime) {
        //获取当前id
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrderDto> ordersDtoPage = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //按时间降序排序
        queryWrapper.orderByDesc(Orders::getOrderTime);
        //订单号 订单id
        queryWrapper.eq(number!=null,Orders::getId,number);
        //判断beginTime是否为空字符串或 null。如果不为空，则为 true
        // .gt表示要添加大于条件。
        queryWrapper.gt(!StringUtils.isEmpty(beginTime),Orders::getOrderTime,beginTime)
                .lt(!StringUtils.isEmpty(endTime),Orders::getOrderTime,endTime);
        ordersService.page(pageInfo,queryWrapper);

        List<OrderDto> list = pageInfo.getRecords().stream().map((item) -> {
            OrderDto ordersDto = new OrderDto();
            //获取orderId,然后根据这个id，去orderDetail表中查数据
            Long orderId = item.getId();
            LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId, orderId);
            List<OrderDetail> details = orderDetailService.list(wrapper);
            BeanUtils.copyProperties(item, ordersDto);
            //之后set一下属性
            ordersDto.setOrderDetails(details);
            return ordersDto;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");
        ordersDtoPage.setRecords(list);
        //日志输出看一下
        log.info("list:{}", list);
        return R.success(ordersDtoPage);
    }

    @PutMapping
    public R<String> changeStatus(@RequestBody Map<String, String> map) {
        int status=Integer.parseInt(map.get("status"));
        Long orderId= Long.parseLong( map.get("id"));
        LambdaUpdateWrapper<Orders> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(Orders::getId,orderId);
        updateWrapper.set(Orders::getStatus,status);
        ordersService.update(updateWrapper);
        return R.success("cg");
    }

}

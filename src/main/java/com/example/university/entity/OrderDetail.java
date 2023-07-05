package com.example.university.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author linyin
 * @since 2023-06-23
 */
@Data
public class OrderDetail implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  //名称
  private String name;

  //订单id
  private Long orderId;


  //菜品id
  private Long dishId;


  //套餐id
  private Long setmealId;


  //口味
  private String dishFlavor;


  //数量
  private Integer number;

  //金额
  private BigDecimal amount;

  //图片
  private String image;
}

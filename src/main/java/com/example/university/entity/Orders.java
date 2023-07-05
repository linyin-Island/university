package com.example.university.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author linyin
 * @since 2023-06-20
 */
@Getter
@Setter
  @ApiModel(value = "Orders对象", description = "订单表")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("主键")
        private Long id;

      @ApiModelProperty("订单号")
      private String number;

      @ApiModelProperty("订单状态 1待付款，2待派送，3已派送，4已完成，5已取消")
      private Integer status;

      @ApiModelProperty("下单用户")
      private Long userId;

      @ApiModelProperty("地址id")
      private Long addressBookId;

      @ApiModelProperty("下单时间")
      private LocalDateTime orderTime;

      @ApiModelProperty("结账时间")
      private LocalDateTime checkoutTime;

      @ApiModelProperty("支付方式 1微信,2支付宝")
      private Integer payMethod;

      @ApiModelProperty("实收金额")
      private BigDecimal amount;

      @ApiModelProperty("备注")
      private String remark;

    private String phone;

    private String address;

    private String userName;

    private String consignee;


}

package com.example.university.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 菜品口味关系表
 * </p>
 *
 * @author linyin
 * @since 2023-06-20
 */
@Getter
@Setter
  @TableName("dish_flavor")
@ApiModel(value = "DishFlavor对象", description = "菜品口味关系表")
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("主键")
        private Long id;

      @ApiModelProperty("菜品")
      private Long dishId;

      @ApiModelProperty("口味名称")
      private String name;

      @ApiModelProperty("口味数据list")
      private String value;

      @ApiModelProperty("创建时间")
      private LocalDateTime createTime;

      @ApiModelProperty("更新时间")
      private LocalDateTime updateTime;

      @ApiModelProperty("创建人")
      private Long createUser;

      @ApiModelProperty("修改人")
      private Long updateUser;

      @ApiModelProperty("是否删除")
      private Integer isDeleted;


}

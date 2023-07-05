package com.example.university.Dto;


import com.example.university.entity.Dish;
import com.example.university.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    //菜品对应的口味数据 Dish表你没有口味数据 扩展了flavors属性
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}

package com.example.university.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.university.Dto.DishDto;
import com.example.university.entity.Dish;


public interface DishService extends IService<Dish> {
    //新增菜品，同时插入口味数据 涉及表格 dish dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);


    void updateWithFlavor(DishDto dishDto);
}

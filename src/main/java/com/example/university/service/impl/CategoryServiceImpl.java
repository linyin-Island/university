package com.example.university.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.university.common.CustomException;
import com.example.university.entity.Category;
import com.example.university.entity.Dish;
import com.example.university.entity.Setmeal;
import com.example.university.mapper.CategoryMapper;
import com.example.university.service.CategoryService;
import com.example.university.service.DishService;
import com.example.university.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        //查询分类是否关联套餐
        //按条件查询构建器 以lambda形式
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        //查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count=dishService.count(dishLambdaQueryWrapper);

        if(count>0)
        {
            throw new CustomException("当前分类下关联了菜品");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int counts=setmealService.count(setmealLambdaQueryWrapper);
        if (counts>0){
            throw new CustomException("当前分类下关联了套餐");
        }

        super.removeById(id);
    }
}

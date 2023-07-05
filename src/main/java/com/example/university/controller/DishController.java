package com.example.university.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.university.Dto.DishDto;
import com.example.university.common.CustomException;
import com.example.university.common.R;
import com.example.university.entity.Category;
import com.example.university.entity.Dish;
import com.example.university.entity.DishFlavor;
import com.example.university.entity.Setmeal;
import com.example.university.service.CategoryService;
import com.example.university.service.DishFlavorService;
import com.example.university.service.DishService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品口味关系表 前端控制器
 * </p>
 *
 * @author linyin
 * @since 2023-06-20
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        //数据需要保存在两张表，所以要重写save
        dishService.saveWithFlavor(dishDto);
        return R.success("cg");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构建器
        Page<Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        //过滤条件
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish::getName,name);
        //排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,queryWrapper);
        //对象拷贝 pageInfo拷贝给dishDtoPage 不拷贝records
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();

       List<DishDto> list= records.stream().map((item)->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();//分类id
            Category category=categoryService.getById(categoryId);
           if (category !=null){
               String categoryName = category.getName();
               dishDto.setCategoryName(categoryName);
           }
           return  dishDto;
       }).collect(Collectors.toList());

       dishDtoPage.setRecords(list);
       return R.success(dishDtoPage);
    }

    /**
     * 根据id查信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
       DishDto dishDto= dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("成功");
    }

    //套餐管理中 显示菜品数据
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        //查询条件
//        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus,1);
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //查询条件
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        //口味数据
        List<DishDto> dishDtoList =list.stream().map((item)->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            //当前菜品id
            Long dishId = item.getId();

            LambdaQueryWrapper<DishFlavor> queryWrapper1=new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }

//    /*
//    菜品的停售
//     */
//
//    @PostMapping("/status/{status}")
//    public R<String> status(@PathVariable Integer status, Long ids) {
//        Dish dish = dishService.getById(ids);
//        if(dish!=null){
//            dish.setStatus(status);
//            dishService.updateById(dish);
//            return R.success("cg");
//        }
//        return R.error("cw");
//    }

    /**
     * 菜品批量停售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status, @RequestParam List<Long> ids) {
        log.info("status:{},ids:{}", status, ids);
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ids != null, Dish::getId, ids);
        updateWrapper.set(Dish::getStatus, status);
        dishService.update(updateWrapper);
        return R.success("批量操作成功");
    }

    /*
    批量删除
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        queryWrapper.eq(Dish::getStatus,1);
        int count =dishService.count(queryWrapper);
        if (count>0){
            throw new CustomException("删除列表中存在启售状态商品，无法删除");
        }
        dishService.removeByIds(ids);
        return R.success("cg");
    }
}


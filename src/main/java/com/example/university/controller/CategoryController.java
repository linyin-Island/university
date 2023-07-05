package com.example.university.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.university.common.R;
import com.example.university.entity.Category;
import com.example.university.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.callback.LanguageCallback;
import java.security.PublicKey;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //接受前端数据 @RequestBody
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){

        Page<Category> pageInfo =new Page<>(page,pageSize);
        //条件排序
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }
    @DeleteMapping
    public R<String> delete(Long ids){

//        categoryService.removeById(id);
        categoryService.remove(ids);
        return R.success("删除成功");
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改信息分类:{}",category);
        categoryService.updateById(category);
        return R.success("修改信息成功");
    }

    /**
     * 传菜品分类
     * @param category
     * @return
     */

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
         //创建一个条件构造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //查询条件 type type为1 在数据库查询整体信息
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //分局种类和更新时间进行排序
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}

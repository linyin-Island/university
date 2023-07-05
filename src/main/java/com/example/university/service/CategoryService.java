package com.example.university.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.university.entity.Category;


public interface CategoryService extends IService<Category> {


    void remove(Long id);
}

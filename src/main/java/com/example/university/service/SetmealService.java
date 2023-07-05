package com.example.university.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.university.Dto.SetmealDto;
import com.example.university.entity.Setmeal;


import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}

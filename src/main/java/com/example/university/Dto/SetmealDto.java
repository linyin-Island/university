package com.example.university.Dto;



import com.example.university.entity.Setmeal;
import com.example.university.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

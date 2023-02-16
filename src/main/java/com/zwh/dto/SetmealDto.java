package com.zwh.dto;


import com.zwh.domain.Setmeal;
import com.zwh.domain.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

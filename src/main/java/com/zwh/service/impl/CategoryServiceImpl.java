package com.zwh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwh.common.CustomException;
import com.zwh.domain.Category;
import com.zwh.domain.Dish;
import com.zwh.domain.Setmeal;
import com.zwh.mapper.CategoryMapper;
import com.zwh.service.CategoryService;
import com.zwh.service.DishService;
import com.zwh.service.SetmealService;
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
        LambdaQueryWrapper<Dish> queryWrapper1 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> queryWrapper2 = new LambdaQueryWrapper<>();

        queryWrapper1.eq(Dish::getCategoryId, id);
        queryWrapper2.eq(Setmeal::getCategoryId, id);

        int count1 = dishService.count(queryWrapper1);
        int count2 = setmealService.count(queryWrapper2);

        if (count1 > 0 || count2 > 0) {
            //抛出业务异常
            throw new CustomException("删除失败，当前分类下关联了菜品或套餐");
        } else {
            super.removeById(id);
        }
    }
}

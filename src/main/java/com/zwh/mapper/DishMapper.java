package com.zwh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zwh.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}

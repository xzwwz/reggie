package com.zwh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zwh.domain.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}

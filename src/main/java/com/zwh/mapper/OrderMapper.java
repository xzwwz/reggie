package com.zwh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zwh.domain.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}

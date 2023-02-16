package com.zwh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwh.domain.OrderDetail;
import com.zwh.domain.Orders;
import com.zwh.mapper.OrderDetailMapper;
import com.zwh.mapper.OrderMapper;
import com.zwh.service.OrdersDetailService;
import com.zwh.service.OrdersService;
import org.springframework.stereotype.Service;

@Service
public class OrdersDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrdersDetailService {
}

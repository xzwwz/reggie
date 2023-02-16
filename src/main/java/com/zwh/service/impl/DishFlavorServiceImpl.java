package com.zwh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwh.domain.DishFlavor;
import com.zwh.mapper.DishFlavorMapper;
import com.zwh.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}

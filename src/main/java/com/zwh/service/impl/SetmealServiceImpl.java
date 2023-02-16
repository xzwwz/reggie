package com.zwh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwh.domain.Setmeal;
import com.zwh.mapper.SetmealMapper;
import com.zwh.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}

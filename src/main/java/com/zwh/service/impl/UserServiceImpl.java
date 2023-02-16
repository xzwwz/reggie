package com.zwh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwh.domain.User;
import com.zwh.mapper.UserMapper;
import com.zwh.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}

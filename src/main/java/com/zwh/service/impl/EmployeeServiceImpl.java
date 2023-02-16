package com.zwh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwh.domain.Employee;
import com.zwh.mapper.EmployeeMapper;
import com.zwh.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

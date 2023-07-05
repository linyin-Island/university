package com.example.university.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.university.entity.Employee;
import com.example.university.mapper.EmployeeMapper;
import com.example.university.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

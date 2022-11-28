package com.code.takeaway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.code.takeaway.entity.Employee;
import com.code.takeaway.mapper.EmployeeMapper;
import com.code.takeaway.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/11/24 13:39
 * 员工逻辑实现层
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

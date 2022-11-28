package com.code.takeaway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.code.takeaway.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/11/24 13:34
 * 员工数据层
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}

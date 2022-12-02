package com.code.takeaway.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.code.takeaway.common.R;
import com.code.takeaway.entity.Employee;
import com.code.takeaway.enums.StatusEnum;
import com.code.takeaway.service.EmployeeService;
import com.sun.org.apache.regexp.internal.RE;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/11/24 13:42
 * 员工接口实现层
 */
@Slf4j
@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("登录的参数" + JSON.toJSON(employee));
        // 获取填写的密码
        String password = employee.getPassword();
        log.info("加密前的密码：" + password);
        // 将页面提交的密码 password 进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("加密后的密码：" + password);
        // 获取用户名
        String username = employee.getUsername();
        // 根据页面提交的用户名 username 查询数据库
        LambdaQueryWrapper<Employee> employeeQueryWrapper = new LambdaQueryWrapper<>();
        employeeQueryWrapper.eq(Employee::getUsername, username);
        Employee userInfo = employeeService.getOne(employeeQueryWrapper);
        // 如果没有查询到则返回登录失败结果
        if (userInfo == null) {
            return R.error("登录失败");
        }
        // 密码比对，如果不一致则返回登录失败结果
        if (!userInfo.getPassword().equals(password)) {
            return R.error("登录失败");
        }
        // 查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (userInfo.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        // 登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", employee.getId());

        return R.success(userInfo);
    }

    /**
     * 员工退出登录操作
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public R<String> logout(HttpServletRequest request) {
        // 获取session中保存的当前登录员工的id
        String id = (String) request.getSession().getAttribute("employee");
        log.info(" 获取session中保存的当前登录员工的id " + id);
        //根据 session中保存的当前登录员工的id 获取用户信息
        LambdaQueryWrapper<Employee> employeeQueryWrapper = new LambdaQueryWrapper<>();
        employeeQueryWrapper.eq(Employee::getCreateUser, id);
        Employee employee = employeeService.getOne(employeeQueryWrapper);
        if (employee == null) {
            return R.error("没有获取到该员工信息");
        }
        log.info("获取session中保存的当前登录员工信息 {}" + JSON.toJSON(employee));
        // 清除Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("" + employee.getUsername() + "退出成功！");

    }


    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}" + JSON.toJSON(employee));
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee userInfo = employeeService.getOne(queryWrapper);
        if (userInfo != null) {
            if (userInfo.getUsername().equals(employee.getUsername())) {
                return R.error("" + employee.getUsername() + "  已存在，请换个用户名注册！");
            }
        }

        // 用户注册密码 md5加密
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        // 获得当前登录用户的id
        Long employeeId = ((Long) request.getSession().getAttribute("employee"));
        Employee user = Employee.builder()
                .username(employee.getUsername())
                .name(employee.getName())
                .sex(employee.getSex())
                .password(password)
                .phone(employee.getPhone())
                .idNumber(employee.getIdNumber())
                .status(StatusEnum.Normal.getCode())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .createUser(employeeId)
                .updateUser(employeeId)
                .build();
        boolean save = employeeService.save(user);
        return save == true ? R.success("注册成功") : R.error("注册失败");
    }

    /**
     * 员工信息分页查询
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public R<Page<Employee>> page(@RequestParam(value = "currentPage") Integer currentPage, @RequestParam(value = "pageSize") Integer pageSize, @RequestParam(value = "name", required = false) String name) {
        log.info("当前页{},每页条数{},用户名{}", currentPage, pageSize, name);
        // 构造分页构造器
        Page<Employee> pageInfo = new Page<>(currentPage, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        // 执行查询
        Page<Employee> employeePage = employeeService.page(pageInfo, queryWrapper);
        return R.success(employeePage);
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R<Employee> getById(@PathVariable(name = "id") String id) {
        Employee employee = employeeService.getById(id);
        log.info("根据id查询员工信息 {}" +JSON.toJSON(employee));
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");

    }

    /**
     * 根据id修改员工信息
     *
     * @param request
     * @param employee
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("根据id修改员工信息 {}" + JSON.toJSON(employee));

        // 获取session 的id
        Long employeeId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(employeeId);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }
}

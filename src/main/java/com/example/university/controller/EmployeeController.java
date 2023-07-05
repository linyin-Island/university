package com.example.university.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.university.common.R;
import com.example.university.entity.Employee;
import com.example.university.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1、将页面提交的密码password进行md5加密处理 获取密码
        String password = employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        // 2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper();
        //Employee类的Username，与传入的Username对比
        queryWrapper.eq(Employee::getUsername,employee.getUsername());//条件
        Employee emp=employeeService.getOne(queryWrapper);//单一查询 包含Username的一条数据

        //3、如果没有查询到则返回登录失败结果
        if (emp == null){
            return R.error("登录失败");
        }
        //4、密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }
        //5.看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() ==0){
            return R.error("账号被禁用");
        }
        //6、登录成功，将员工id存入Session并返回登录成功结果 浏览器只允许session里的值进行访问
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    //新增员工
    @PostMapping("logout")
    public R<String> logout(HttpServletRequest request){
        //清除掉session的数据
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    //1、页面发送ajax请求，将新增员工页面中输入的数据以json的形式提交到服务端
    // 2、服务端Controller接收页面提交的数据并调用Service将数据进行保存
    //3、Service调用Mapper操作数据库，保存数据
    //新增员工
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());
        //设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);
        return R.success("新增成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);
        //分页构建器
        Page pageInfo=new Page(page,pageSize);
        //按条件查询 条件构建器
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper();
        //name不为空  查询条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //按修改时间 顺序排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 禁用
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee){
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 编辑功能，数据回显
     */
    @GetMapping("/{id}")//接受参数
    public R<Employee> getByID(@PathVariable Long id){
        log.info("根据id查询员工信息");
        Employee employee= employeeService.getById(id);
        if(employee !=null)
        {
            return R.success(employee);
        }
        return R.error("没有");
    }
}


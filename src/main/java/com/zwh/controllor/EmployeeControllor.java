package com.zwh.controllor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zwh.common.R;
import com.zwh.domain.Employee;
import com.zwh.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@Slf4j
@RestController
@RequestMapping("/employee")
@Transactional
public class EmployeeControllor {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 用户登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //将页面提交的密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if (emp == null) {
            return R.error("用户名不存在");
        }

        if (!password.equals(emp.getPassword())) {
            return R.error("密码错误");
        }

        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

    }

    /**
     * 退出登录并清理session中的信息
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("成功退出");
    }

    /**
     * 新增员工
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> addEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long empId = (Long)request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
//        try {
        employeeService.save(employee);
//        }catch (Exception ex){
//            return R.error("新增员工失败");
//        }
        return R.success("新增员工成功");
    }

    /**
     * 显示员工信息
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<Employee>> listEmployee(int page, int pageSize, String name) {
        log.info("page = {} ,pagesize = {} , name = {}", page, pageSize, name);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        Page<Employee> page1 = new Page<>(page, pageSize);

        log.info(queryWrapper.toString());
        if (name != null) {
//            log.info("查询"+name);
            queryWrapper.like(Employee::getName, name);
        }
//        log.info(queryWrapper.toString());

        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(page1, queryWrapper);

        return R.success(page1);
    }

    @PutMapping
    public R<String> updateEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());
        employee.setUpdateTime(LocalDateTime.now());

//        Long empId = (Long)request.getSession().getAttribute("employee");
//        Long empId = MyBaseContext.getCurrentId();

//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

  /*  @GetMapping("/*")
    public R<Employee> queryEmployeeById(HttpServletRequest request){
        log.info(request.getRequestURI());
        Long empId = Long.parseLong(request.getRequestURI().split("/")[2]);
//        log.info(empId.toString());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getId,empId);
        Employee emp = employeeService.getOne(queryWrapper);
        return R.success(emp);
    }*/

    @GetMapping("/{id}")
    public R<Employee> queryEmployeeById(@PathVariable Long id) {
        Employee emp = employeeService.getById(id);
        if (emp != null) {
            return R.success(emp);
        }
        return R.error("");
    }
}

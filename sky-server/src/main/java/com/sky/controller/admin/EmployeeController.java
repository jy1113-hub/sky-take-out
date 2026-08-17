package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理 Controller
 * 负责接收前端发来的员工相关请求
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO 前端传过来的登录参数
     * @return 返回登录结果，包含员工信息和 JWT 令牌
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        // 调用 Service 层进行登录校验
        Employee employee = employeeService.login(employeeLoginDTO);

        // 登录成功后，生成 JWT 令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        // 封装返回给前端的 VO 数据
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 员工退出登录
     *
     * @return 返回成功信息
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO 前端传过来的员工信息（姓名、手机号、账号）
     * @return 返回成功信息
     */
    @PostMapping
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO 查询条件（页码、每页条数、姓名搜索）
     * @return 返回分页结果（总记录数 + 当前页数据）
     */
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用或禁用员工账号
     *
     * @param status 状态：1 启用，0 禁用
     * @param id     员工 ID
     * @return 返回成功信息
     */
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用禁用员工账号：status={}, id={}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据 ID 查询员工信息（用于修改页面）
     *
     * @param id 员工 ID
     * @return 返回该员工的详细信息
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("根据 id 查询员工信息：{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 修改员工信息
     *
     * @param employeeDTO 修改后的员工信息
     * @return 返回成功信息
     */
    @PutMapping
    public Result<String> update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息：{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
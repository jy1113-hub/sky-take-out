package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO 登录参数
     * @return 登录成功的员工实体
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     *
     * @param employeeDTO 员工信息
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO 查询参数
     * @return 分页结果
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用/禁用员工账号
     *
     * @param status 状态 1启用 0禁用
     * @param id     员工 ID
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据 ID 查询员工
     *
     * @param id 员工 ID
     * @return 员工实体
     */
    Employee getById(Long id);

    /**
     * 修改员工信息
     *
     * @param employeeDTO 修改后的信息
     */
    void update(EmployeeDTO employeeDTO);
}
package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工（登录）
     */
    Employee getByUsername(String username);

    /**
     * 新增员工
     */
    void insert(Employee employee);

    /**
     * 分页查询员工
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 根据ID查询员工
     */
    Employee getById(Long id);

    /**
     * 动态更新员工信息
     */
    void update(Employee employee);
}
package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "C端-地址簿接口")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    @ApiOperation("新增地址")
    public Result<String> save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list() {
        return Result.success(addressBookService.list());
    }

    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault() {
        return Result.success(addressBookService.getDefault());
    }

    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result<String> update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result<String> delete(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        return Result.success(addressBookService.getById(id));
    }

    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result<String> setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }
}
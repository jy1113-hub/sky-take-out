package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface AddressBookMapper {
    void insert(AddressBook addressBook);
    List<AddressBook> list(Long userId);
    @Select("select * from address_book where user_id = #{userId} and is_default = 1")
    AddressBook getDefault(Long userId);
    void update(AddressBook addressBook);
    void deleteById(Long id);
    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);
    void updateIsDefaultByUserId(AddressBook addressBook);
}
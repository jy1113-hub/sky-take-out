package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    /** 根据openid查询用户 */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /** 插入用户 */
    @Insert("insert into user (openid, create_time) values (#{openid}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    /** 根据条件统计用户数量 */
    @Select("<script>" +
            "select count(id) from user " +
            "<where>" +
            "<if test='begin != null'> and create_time &gt;= #{begin} </if>" +
            "<if test='end != null'> and create_time &lt;= #{end} </if>" +
            "</where>" +
            "</script>")
    Integer countByMap(Map<String, Object> map);
}
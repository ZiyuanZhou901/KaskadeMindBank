package com.kaskademindbank.mapper;

import com.kaskademindbank.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author ZiyuanZhou
 * @since 2023-11-28
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {
    @Select("SELECT * FROM users WHERE userName = #{userName}")
    Users findByUsername(@Param("userName") String username);

    @Select("SELECT * FROM users WHERE email = #{email}")
    Users findByEmail(String email);
}

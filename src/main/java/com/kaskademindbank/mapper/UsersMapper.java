package com.kaskademindbank.mapper;

import com.kaskademindbank.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author ZiyuanZhou
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {
    @Select("SELECT * FROM users WHERE userName = #{userName}")
    Users findByUsername(@Param("userName") String username);

    @Select("SELECT * FROM users WHERE email = #{email}")
    Users findByEmail(String email);

    @Select("SELECT userId FROM users WHERE userName = #{userName}")
    Integer findUserIdByUsername(@Param("userName") String username);
}

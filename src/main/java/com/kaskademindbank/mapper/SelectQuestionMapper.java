package com.kaskademindbank.mapper;

import com.kaskademindbank.entity.SelectQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZiyuanZhou
 * @since 2023-11-28
 */
@Mapper
public interface SelectQuestionMapper extends BaseMapper<SelectQuestion> {
    @Select("SELECT DISTINCT subject FROM selectQuestion WHERE userId = #{userId}")
    List<String> findSubjectsByUserId(@Param("userId") Integer userId);
    @Select("SELECT * FROM selectQuestion WHERE userId = #{userId}")
    List<SelectQuestion> findSelectQuestionsByUserId(Integer userId);

    @Select("SELECT * FROM selectQuestion WHERE userId = #{userId} and voiFile is null and vidFile is null")
    List<SelectQuestion> findSelectQuestionsByUserIdWoFile(Integer userId);

    @Select("SELECT COUNT(*) FROM selectQuestion WHERE userId = #{userId}")
    Integer countByUserId(@Param("userId") Integer userId);
    @Select("SELECT * FROM selectQuestion WHERE userId = #{userId} and voiFile is null and vidFile is null and subject=#{subject}")
    List<SelectQuestion> findSelectQuestionsByUserIdWoFileSubject(Integer userId, String subject);
}

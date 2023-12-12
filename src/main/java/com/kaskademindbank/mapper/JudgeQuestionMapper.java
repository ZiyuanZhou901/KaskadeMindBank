package com.kaskademindbank.mapper;

import com.kaskademindbank.entity.JudgeQuestion;
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
public interface JudgeQuestionMapper extends BaseMapper<JudgeQuestion> {
    @Select("SELECT DISTINCT subject FROM judgeQuestion WHERE userId = #{userId}")
    List<String> findSubjectsByUserId(@Param("userId") Integer userId);
    @Select("SELECT * FROM judgeQuestion WHERE userId = #{userId}")
    List<JudgeQuestion> findJudgeQuestionsByUserId(Integer userIdByUsername);
}

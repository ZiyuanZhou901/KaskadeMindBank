package com.kaskademindbank.mapper;

import com.kaskademindbank.entity.JudgeQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 *
 * @author ZiyuanZhou
 */
@Mapper
public interface JudgeQuestionMapper extends BaseMapper<JudgeQuestion> {
    @Select("SELECT DISTINCT subject FROM judgeQuestion WHERE userId = #{userId}")
    List<String> findSubjectsByUserId(@Param("userId") Integer userId);
    @Select("SELECT * FROM judgeQuestion WHERE userId = #{userId}")
    List<JudgeQuestion> findJudgeQuestionsByUserId(Integer userId);
    @Select("SELECT * FROM judgeQuestion WHERE userId = #{userId} and voiFile is null and vidFile is null")
    List<JudgeQuestion> findJudgeQuestionsByUserIdWoFile(Integer userId);
    @Select("SELECT COUNT(*) FROM judgeQuestion WHERE userId = #{userId}")
    Integer countByUserId(Integer userId);
    @Select("SELECT * FROM judgeQuestion WHERE userId = #{userId} and voiFile is null and vidFile is null and subject=#{subject}")
    List<JudgeQuestion> findJudgeQuestionsByUserIdWoFileSubject(Integer userId, String subject);
    @Select("SELECT DATE(upTime) AS upTime, COUNT(*) AS uploadCount " +
            "FROM judgeQuestion " +
            "WHERE userId = #{userId} " +
            "AND upTime >= CURDATE() - INTERVAL 6 DAY " +
            "GROUP BY DATE(upTime)")
    List<Map<String, Integer>> countByUserIdAndDate(Integer userId);
}

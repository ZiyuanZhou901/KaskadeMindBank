package com.kaskademindbank.mapper;

import com.kaskademindbank.entity.SelectQuestion;
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
    @Select("SELECT DATE(upTime) AS upTime, COUNT(*) AS uploadCount " +
            "FROM selectQuestion " +
            "WHERE userId = #{userId} " +
            "AND upTime >= CURDATE() - INTERVAL 6 DAY " +
            "GROUP BY DATE(upTime)")
    List<Map<String, Integer>> countByUserIdAndDate(Integer userId);

    @Select({
            "SELECT subject, SUM(questionCount) AS totalQuestionCount FROM (",
            "  SELECT subject, COUNT(*) AS questionCount FROM fillQuestion WHERE userId = #{userId} GROUP BY subject",
            "  UNION ALL",
            "  SELECT subject, COUNT(*) AS questionCount FROM judgeQuestion WHERE userId = #{userId} GROUP BY subject",
            "  UNION ALL",
            "  SELECT subject, COUNT(*) AS questionCount FROM selectQuestion WHERE userId = #{userId} GROUP BY subject",
            ") AS subquery GROUP BY subject"
    })
    List<Map<String, Object>> countQuestionsBySubject(@Param("userId") Integer userId);
}

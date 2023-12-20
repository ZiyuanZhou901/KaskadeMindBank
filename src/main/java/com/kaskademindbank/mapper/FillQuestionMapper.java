package com.kaskademindbank.mapper;

import com.kaskademindbank.entity.FillQuestion;
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
public interface FillQuestionMapper extends BaseMapper<FillQuestion> {

    @Select("SELECT DISTINCT subject FROM fillQuestion WHERE userId = #{userId}")
    List<String> findSubjectsByUserId(@Param("userId") Integer userId);
    @Select("SELECT * FROM fillQuestion WHERE userId = #{userId}")
    List<FillQuestion> findFillQuestionsByUserId(Integer userId);

    @Select("SELECT * FROM fillQuestion WHERE userId = #{userId} and voiFile is null and vidFile is null")
    List<FillQuestion> findFillQuestionsByUserIdWoFile(Integer userId);
    @Select("SELECT COUNT(*) FROM fillQuestion WHERE userId = #{userId}")
    Integer countByUserId(Integer userId);
    @Select("SELECT * FROM fillQuestion WHERE userId = #{userId} and voiFile is null and vidFile is null and subject=#{subject}")
    List<FillQuestion> findFillQuestionsByUserIdWoFileSubject(Integer userId,String subject);
    @Select("SELECT DATE(upTime) AS upTime, COUNT(*) AS uploadCount " +
            "FROM fillQuestion " +
            "WHERE userId = #{userId} " +
            "AND upTime >= CURDATE() - INTERVAL 6 DAY " +
            "GROUP BY DATE(upTime)")
    List<Map<String, Integer>> countByUserIdAndDate(Integer userId);
}

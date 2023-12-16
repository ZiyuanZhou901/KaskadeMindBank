package com.kaskademindbank.mapper;

import com.kaskademindbank.entity.FillQuestion;
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
public interface FillQuestionMapper extends BaseMapper<FillQuestion> {

    @Select("SELECT DISTINCT subject FROM fillQuestion WHERE userId = #{userId}")
    List<String> findSubjectsByUserId(@Param("userId") Integer userId);
    @Select("SELECT * FROM fillQuestion WHERE userId = #{userId}")
    List<FillQuestion> findFillQuestionsByUserId(Integer userIdByUsername);

    @Select("SELECT * FROM fillQuestion WHERE userId = #{userId} and picFile is null and voiFile is null and vidFile is null")
    List<FillQuestion> findFillQuestionsByUserIdWoFile(Integer userIdByUsername);
    @Select("SELECT COUNT(*) FROM fillQuestion WHERE userId = #{userId}")
    Integer countByUserId(Integer userIdByUsername);
}

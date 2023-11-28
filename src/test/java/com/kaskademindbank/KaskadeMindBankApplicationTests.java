package com.kaskademindbank;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KaskadeMindBankApplicationTests {

    @Test
    void contextLoads() {
        FastAutoGenerator
                //首先使用create来配置数据库链接信息
                .create(new DataSourceConfig.Builder("jdbc:mysql://localhost:3306/Kaskade","root","Mba230721"))
                .globalConfig(builder -> {
                    builder.author("ZiyuanZhou");              //作者信息，一会会变成注释
                    builder.commentDate("2023-11-28");  //日期信息，一会会变成注释
                    builder.outputDir("src/main/java"); //输出目录设置为当前项目的目录
                })
                .packageConfig(builder -> builder.parent("com.kaskademindbank")) //设置父包名
                .strategyConfig(builder -> {
                    //设置为所有Mapper添加@Mapper注解
                    builder
                            .mapperBuilder()
                            .mapperAnnotation(Mapper.class)
                            .build();
                })
                .execute();

    }

}

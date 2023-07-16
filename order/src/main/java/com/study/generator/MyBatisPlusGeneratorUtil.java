package com.study.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MyBatisPlusGeneratorUtil {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://139.199.175.93:3306/study", "hc", "Hc_123456")
                //全局配置
                .globalConfig(builder -> {
                    builder.author("hc") //设置作者
                            .commentDate("yyyy-MM-dd") //注释日期格式
                            .outputDir("C:\\Users\\Administrator.DESKTOP-UIIV4N1\\Desktop\\test") //指定输出目录
                            .fileOverride(); //覆盖文件
                })
                //包配置
                .packageConfig(builder -> {
                    builder.parent("com.study") //包名的前缀
                            .entity("entity") //实体类包名
                            .mapper("mapper") //mapper接口包名
                            .service("service") //service包名
                            .controller("controller") //controller包名
                            .xml("com.study.order.mapper"); //映射文件包名
                })
                //策略配置
                .strategyConfig(builder -> {
                    builder.addInclude("customer") //设置需要生成的表名或排除的表名，支持多张表
//                            .addTablePrefix("") //设置表名前缀
//                            .addTableSuffix("") //设置表名后缀
                            .entityBuilder() //开始实体类配置
                            .enableLombok() //开启lombok模型
                            .naming(NamingStrategy.underline_to_camel) //表名下划线转驼峰
                            .columnNaming(NamingStrategy.underline_to_camel); //列名下划线转驼峰
                })
                .execute();
    }
}

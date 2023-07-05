package com.example.university.utils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.sql.Types;
import java.util.Collections;

public class codeGenerdor {
    public static void main(String[] args) {
        generator();


    }
    private static void generator(){
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/reggie?characterEncoding=utf-8&serverTimezone=GMT%2B8&userSSL=false", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("linyin") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\IdeaJava\\university\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {

                    builder.parent("com.example.university") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\IdeaJava\\university\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
                    builder.mapperBuilder().enableMapperAnnotation().build();
                    builder.controllerBuilder().enableHyphenStyle()  // 开启驼峰转连字符
                            .enableRestStyle();  // 开启生成@RestController 控制器
                    builder.addInclude("order_detail") // 设置需要生成的表名
                            .addTablePrefix("t_", "sys_"); // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}

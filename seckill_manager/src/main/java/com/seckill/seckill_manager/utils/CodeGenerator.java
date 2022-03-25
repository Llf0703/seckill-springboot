package com.seckill.seckill_manager.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @author Wky1742095859
 * @version 1.0
 * @ClassName CodeGenerator
 * @description: 代码生成器
 * @date 2022/3/18 23:57
 */
public class CodeGenerator {
    public static void main(String[] args) {
        generator();
    }

    private static void generator() {
        FastAutoGenerator.create("jdbc:mysql://124.223.199.137:3306/seckill?serverTimezone=GMT%2b8", "root", "seckill")
                .globalConfig(builder -> {
                    builder.author("wky1742095859") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("E:\\大创and服创\\codegenerator"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.seckill.seckill_manager") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "E:\\大创and服创\\codegenerator"));//OutputFile.mapperXml, "D://")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("risk_control") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}

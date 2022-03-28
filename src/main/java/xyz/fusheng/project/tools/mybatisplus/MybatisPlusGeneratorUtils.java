package xyz.fusheng.project.tools.mybatisplus;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Property;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import xyz.fusheng.project.model.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.baomidou.mybatisplus.generator.config.rules.DbColumnType.*;

/**
 * @FileName: MybatisPlusGeneratorUtils
 * @Author: code-fusheng
 * @Date: 2022/3/26 14:48
 * @Version: 1.0
 * @Description:
 * 参考文档 : https://baomidou.com/pages/981406/
 */

public class MybatisPlusGeneratorUtils {

    final static String  DIR_PATH = System.getProperty("user.dir");

    private static String DB_URL = "jdbc:mysql://47.111.158.6:3306/springboot-project-mysql";
    private static String USERNAME = "root";
    private static String PASSWORD = "Xcode-mysql?";

    public static void main(String[] args) {

        DataSourceConfig.Builder dataSourceConfig = new DataSourceConfig.Builder(DB_URL, USERNAME, PASSWORD)
                .typeConvert((globalConfig, fieldType) -> {
                    String type = fieldType.toLowerCase();
                    if (type.contains("tinyint") || type.contains("tinyint(1)") || type.contains("tinyint(2)") || type.contains("tinyint(4)")) {
                        return INTEGER;
                    }
                    if (type.contains("bit")) {
                        return INTEGER;
                    }
                    return new MySqlTypeConvert().processTypeConvert(globalConfig, fieldType);
                })
                .keyWordsHandler(new MySqlKeyWordsHandler());

        List<IFill> fillList = new ArrayList<>();
        fillList.add(new Property("creatorId", FieldFill.INSERT));
        fillList.add(new Property("updaterId", FieldFill.INSERT_UPDATE));
        fillList.add(new Property("creatorName", FieldFill.INSERT));
        fillList.add(new Property("updaterName", FieldFill.INSERT_UPDATE));

        FastAutoGenerator.create(dataSourceConfig)
                .globalConfig(builder -> builder
                        //.fileOverride()
                        .disableOpenDir()
                        .outputDir(DIR_PATH + "/src/main/java")
                        .author("code-fusheng")
                        .enableSwagger()
                        .dateType(DateType.ONLY_DATE)
                        .build())
                .packageConfig(builder -> builder
                        .parent("xyz.fusheng")
                        .moduleName("project")
                        .controller("controller")
                        .entity("model.entity")
                        .service("core.service")
                        .serviceImpl("core.service.impl")
                        .mapper("core.mapper")
                        .xml("core.mapper.xml")
                        .build())
                .strategyConfig(builder -> builder
                        .addInclude("sys_user", "sys_role", "sys_menu").addTablePrefix("sys_")
                        .entityBuilder()
                        .superClass(BaseEntity.class)
                        .enableColumnConstant()
                        .enableLombok()
                        .versionColumnName("version").versionPropertyName("version")
                        .logicDeleteColumnName("is_deleted").logicDeletePropertyName("isDeleted")
                        .naming(NamingStrategy.underline_to_camel)
                        .addTableFills(fillList)
                        .idType(IdType.AUTO)
                        .controllerBuilder()
                        .enableRestStyle().formatFileName("%sController")
                        .mapperBuilder().superClass(BaseMapper.class)
                        .enableMapperAnnotation()
                        .build())
                .templateEngine(new VelocityTemplateEngine())
                .execute();

    }

}

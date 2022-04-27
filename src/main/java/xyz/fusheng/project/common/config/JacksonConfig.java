package xyz.fusheng.project.common.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @FileName: JsonConfig
 * @Author: code-fusheng
 * @Date: 2022/3/27 20:51
 * @Version: 1.0
 * @Description:
 */

@Configuration
public class JacksonConfig {

    /**
     * Jackson全局转化long类型为String，解决jackson序列化时long类型缺失精度问题 这里解决前端Long精度问题的方案是转为""字符串
     * @return Jackson2ObjectMapperBuilderCustomizer 注入的对象
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
        };
    }

}

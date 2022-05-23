package xyz.fusheng.project.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @FileName: RedisConfig
 * @Author: code-fusheng
 * @Date: 2022/5/23 00:24
 * @Version: 1.0
 * @Description: RedisConfig
 */

@Configuration
public class RedisConfig {

    @Bean(name = "redisTemplate")
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 自定义 template 对象
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        // 配置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field get 和 set 以及修饰符范围，ANY 是都有包括 private 和 public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 使用 StringRedisSerializer 来(反)序列化 redis 的 key
        redisTemplate.setKeySerializer(RedisSerializer.string());
        // 使用 Jackson2JsonRedisSerializer 来序列化和反序列化 redis 的 value 值(默认使用 JDK 的序列化方式)
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // 设置hash key 和value序列化模式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}

package xyz.fusheng.project.errordemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import xyz.fusheng.project.common.config.RedisConfig;
import xyz.fusheng.project.model.domain.Model;

/**
 * @FileName: RedisTest
 * @Author: code-fusheng
 * @Date: 2022/5/22 22:59
 * @Version: 1.0
 * @Description:
 * 1. 再未进行任何配置(默认)的情况下，使用 RedisTemplate 就会产生乱码数据，StringRedisTemplate 不会，但是如果两者的区别不仅仅是读取 Value 是 Object 和 String
 *    那么简单，因为这两种方式存储的数据完全无法通用。
 * 2. 自定义 RedisTemplate 的序列化配置 {@link RedisConfig} 可以参考 {@link StringRedisTemplate} 对 {@link RedisTemplate<String, String>} 的重写。
 *    Key 的序列化方式使用 RedisSerializer.string() [StringRedisSerializer 方式]
 *    Value 的序列化方式使用 {@link Jackson2JsonRedisSerializer}
 *
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    private static final Logger logger = LoggerFactory.getLogger(RedisTest.class);

    public JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxTotal(50);
        config.setLifo(true);
        config.setMinIdle(5);
        config.setMaxIdle(5);
        return config;
    }

    @Test
    public void testJedis() {
        JedisPool pool =new JedisPool(this.getJedisPoolConfig(),"42.192.222.62",6390,3000,"Xcode-redis?", 7);
        Jedis redis = pool.getResource();
        redis.set("code-fusheng","testJedis");
        redis.close();
        System.out.println("ok");
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 通过使用默认的 RedisTemplate 和 StringRedisTemplate 在实际存储中的效果，引出自定义 RedisTemplate Key 和 Value 的序列化方式起源。
     */
    @Test
    public void testRedisTemplateError() throws JsonProcessingException {
        /**
         * 这里使用默认的 {@link RedisTemplate} 设置 Redis 数据，会造成 Redis 数据乱码。
         * 默认情况下 RedisTemplate 针对 Key 和 Value 使用了 JDK 序列化。  {@link RedisTemplate#afterPropertiesSet() => JdkSerializationRedisSerializer}
         */
        redisTemplate.opsForValue().set("redisTemplate:test1", new Model("1", "redis", "redisTemplate"));
        /**
         * 这里使用 StringRedisTemplate 并不会导致数据乱码。
         * StringRedisTemplate 默认情况下使用的是 String 序列化方式，Key 和 Value 只能是 String。{@link StringRedisTemplate => RedisSerializer.string() => StringRedisSerializer}
         */
        stringRedisTemplate.opsForValue().set("stringRedisTemplate:test1", objectMapper.writeValueAsString(new Model("1", "redis", "stringRedisTemplate")));
        // null
        logger.info("[redisTemplate] => value:{}", redisTemplate.opsForValue().get("stringRedisTemplate:test1"));
        // null
        logger.info("[stringRedisTemplate] => value:{}", stringRedisTemplate.opsForValue().get("redisTemplate:test1"));

        // 使用 RedisTemplate 获取 Value，无需反序列化就可以拿到实际对象，虽然方便，但是不处理的化数据不可读。
        Model modelFromRedisTemplate = (Model) this.redisTemplate.opsForValue().get("redisTemplate:test1");
        logger.info("[redisTemplate] => value:{}", modelFromRedisTemplate);

        // 使用 StringRedisTemplate 获取 Value，虽然 Key 正常，但是 Value 的存储需要手动序列化成字符串。
        Model modelFromStringRedisTemplate = objectMapper.readValue(this.stringRedisTemplate.opsForValue().get("stringRedisTemplate:test1"), Model.class);
        logger.info("[stringRedisTemplate] => value:{}", modelFromStringRedisTemplate);

    }

}

package xyz.fusheng.project.errordemo.log;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @FileName: LogTest
 * @Author: code-fusheng
 * @Date: 2022/5/12 00:05
 * @Version: 1.0
 * @Description: 日志框架技术测试
 * Spring Boot 默认依赖 logback 说明
 * spring-boot-starter
 * +- spring-boot-starter-logging
 * ｜  +- logback-classic (包含 SLF4J + Logback 日志框架以及 SLF4J 适配器)
 * ｜  ｜  +- log4j-to-slf4j (用于实现 Log4j2 API 到 SLF4J 的桥接)
 * ｜  ｜  +- jul-to-slf4j (实现 java.util,logging API 到 SLF4J 的桥接)
 */

@SpringBootTest
class LogTest {

    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

    @Test
    void testLogRepeat() {
        logger.debug("[debug]");
        logger.info("[info]");
        logger.warn("[warn]");
        logger.error("[error]");
    }

    @Test
    void testMarkLog() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Thread.sleep(1000);
        Marker timeMarker = MarkerFactory.getMarker("mark");
        logger.info(timeMarker, "took {} ms", System.currentTimeMillis() - startTime);

    }

}

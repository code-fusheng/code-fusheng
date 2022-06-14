package xyz.fusheng.project.errordemo.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @FileName: SayService
 * @Author: code-fusheng
 * @Date: 2022/6/6 22:30
 * @Version: 1.0
 * @Description: SayService 抽象类 - 测试单例 Bean 如何注入 Prototype 的 Bean
 */

// 让 Service 以代理的方式注入。这样虽然 Controller 本身是单例的，但是每次都能从代理获取 Service。这样一来，prototype 范围才能真的生效
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public abstract class SayService {

    private static final Logger logger = LoggerFactory.getLogger(SayService.class);

    /**
     * 用于保存方法处理的中间数据
     */
    List<String> data = new ArrayList<>();

    /**
     * 每次调用 say 方法都会往 data 中加入新数据，可以认为 SayService 是有状态的，如果 SayService 是单例的话必然会 OOM。
     */
    public void say() {
        data.add(IntStream.rangeClosed(1, 1000000)
                .mapToObj(__ -> "a")
                .collect(Collectors.joining("")) + UUID.randomUUID().toString());
        logger.info("I'm {} size:{}", this, data.size());
    }

}

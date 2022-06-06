package xyz.fusheng.project.errordemo.Bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @FileName: SayHello
 * @Author: code-fusheng
 * @Date: 2022/6/6 22:39
 * @Version: 1.0
 * @Description: SayHello 实现类 —— 测试单例 Bean 问题
 */

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SayHello extends SayService {

    private static final Logger logger = LoggerFactory.getLogger(SayHello.class);

    @Override
    public void say() {
        super.say();
        logger.info("[sayHello#say()] => hello");
    }
}

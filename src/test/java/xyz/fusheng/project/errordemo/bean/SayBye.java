package xyz.fusheng.project.errordemo.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @FileName: SayBye
 * @Author: code-fusheng
 * @Date: 2022/6/6 22:45
 * @Version: 1.0
 * @Description: SayBye 实现类 —— 测试单例 Bean 问题
 */

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SayBye extends SayService {

    private static final Logger logger = LoggerFactory.getLogger(SayService.class);

    @Override
    public void say() {
        super.say();
        logger.info("[sayBye#say()] => bye");
    }
}

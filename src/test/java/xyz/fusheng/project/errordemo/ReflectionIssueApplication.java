package xyz.fusheng.project.errordemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionIssueApplication {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionIssueApplication.class);

    void age(int age) {
        logger.info("[ReflectionIssueApplication#age(int age)] => age:{}", age);
    }

    void age(Integer age) {
        logger.info("[ReflectionIssueApplication#age(Integer age)] => age:{}", age);
    }

}

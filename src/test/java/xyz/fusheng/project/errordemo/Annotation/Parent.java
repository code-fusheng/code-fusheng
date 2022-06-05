package xyz.fusheng.project.errordemo.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @FileName: Parent
 * @Author: code-fusheng
 * @Date: 2022/6/5 22:07
 * @Version: 1.0
 * @Description: Parent 父类 —— 测试注解继承
 */

@MyAnnotation(value = "Class")
public class Parent {

    private static final Logger logger = LoggerFactory.getLogger(Parent.class);

    @MyAnnotation(value = "Method")
    public void foo() {
    }

}

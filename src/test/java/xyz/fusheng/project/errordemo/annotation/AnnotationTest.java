package xyz.fusheng.project.errordemo.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * @FileName: AnnotationTest
 * @Author: code-fusheng
 * @Date: 2022/6/4 22:57
 * @Version: 1.0
 * @Description: 注解测试
 * 1. 子类以及子类的方法，无法自动继承父类和父类方法上的注解
 */

public class AnnotationTest {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationTest.class);

    private static String getAnnotationValue(MyAnnotation annotation) {
        if (annotation == null) return "";
        return annotation.value();
    }

    /**
     * 测试类继承注解
     */
    private static void testClassExtendAnnotation_Error() throws NoSuchMethodException {
        // 获取父类的类和方法上的注解
        Parent parent = new Parent();
        logger.info("[Parent父类] => 类的注解:{}", getAnnotationValue(parent.getClass().getAnnotation(MyAnnotation.class)));
        logger.info("[Parent父类] => 方法的注解:{}", getAnnotationValue(parent.getClass().getMethod("foo").getAnnotation(MyAnnotation.class)));

        // 获取子类的类和方法上的注解
        // 注解不存在 —— 输出空字符串
        Child child = new Child();
        logger.info("[Child子类] => 类的注解:{}", getAnnotationValue(child.getClass().getAnnotation(MyAnnotation.class)));
        logger.info("[Child子类] => 方法的注解:{}", getAnnotationValue(child.getClass().getMethod("foo").getAnnotation(MyAnnotation.class)));
    }

    private static void testClassExtendAnnotation_Right() throws NoSuchMethodException {
        // 获取子类的类和方法上的注解
        Child child = new Child();
        logger.info("[Child子类] => 类的注解:{}", getAnnotationValue(AnnotatedElementUtils.findMergedAnnotation(child.getClass(), MyAnnotation.class)));
        logger.info("[Child子类] => 方法的注解:{}", getAnnotationValue(AnnotatedElementUtils.findMergedAnnotation(child.getClass().getMethod("foo"), MyAnnotation.class)));
    }

    public static void main(String[] args) throws NoSuchMethodException {
        // 测试类继承注解 —— 错误用法
        testClassExtendAnnotation_Error();
        // 测试类继承注解 —— 正确用法 - Spring 提供的 AnnotatedElementUtils 类
        testClassExtendAnnotation_Right();
    }

}

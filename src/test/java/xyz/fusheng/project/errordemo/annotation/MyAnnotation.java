package xyz.fusheng.project.errordemo.annotation;

import java.lang.annotation.*;

/**
 * 通过在注解上标记 @Inherited 元注解可以实现注解的继承；@Inherited 只能实现类上的注解继承。
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited // +:处理类继承注解问题时添加，添加此「元注解」可以使类继承时类本身拿到注解，但是无法获得方法上的注解
public @interface MyAnnotation {
    String value();
}

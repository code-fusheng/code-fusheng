package xyz.fusheng.project.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * @FileName: AdminRequestMapping
 * @Author: code-fusheng
 * @Date: 2021/12/11 11:02 上午
 * @Version: 1.0
 * @Description:
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
public @interface AdminRequestMapping {

    @AliasFor(annotation = RequestMapping.class)
    String name() default "";

    @AliasFor(annotation = RequestMapping.class, value = "path")
    String[] value() default {};

    @AliasFor(annotation = RequestMapping.class, value = "value")
    String[] path() default {};

}

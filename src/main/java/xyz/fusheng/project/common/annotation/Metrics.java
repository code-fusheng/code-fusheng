package xyz.fusheng.project.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @FileName: Metrics
 * @Author: code-fusheng
 * @Date: 2022/6/6 23:18
 * @Version: 1.0
 * @Description: 自定义监控注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Metrics {

    /**
     * 在方法执行成功后打点，记录方法的执行时间发送到指标系统 (默认开启)
     *
     * @return
     */
    boolean recordSuccessMetrics() default true;

    /**
     * 在方法执行失败后打点，记录方法的执行时间发送到指标系统 (默认开启)
     *
     * @return
     */
    boolean recordFailMetrics() default true;

    /**
     * 通过日志记录请求参数 (默认开启)
     *
     * @return
     */
    boolean logParameters() default true;

    /**
     * 通过日志记录方法返回值 (默认开启)
     *
     * @return
     */
    boolean logReturn() default true;

    /**
     * 出现异常后通过日志记录异常信息 (默认开启)
     *
     * @return
     */
    boolean logException() default true;

    /**
     * 出现异常后忽略异常返回默认值 (默认关闭)
     *
     * @return
     */
    boolean ignoreException() default false;

}

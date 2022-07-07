package xyz.fusheng.project.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.fusheng.project.common.annotation.Metrics;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * @FileName: MetricsAspect
 * @Author: code-fusheng
 * @Date: 2022/6/6 23:27
 * @Version: 1.0
 * @Description: 切面监控
 */

@Aspect
@Component
public class MetricsAspect {

    private static final Logger logger = LoggerFactory.getLogger(MetricsAspect.class);

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 实现一个返回 Java 基本类型默认值的工具
     * 这里通过初始化一个具有1个元素的数组，然后通过获取这个数组的值来获取基本类型的默认值
     */
    private static final Map<Class<?>, Object> DEFAULT_VALUES = Stream
            .of(boolean.class, byte.class, char.class, double.class, float.class, int.class, long.class, short.class)
            .collect(toMap(clazz -> (Class<?>) clazz, clazz -> Array.get(Array.newInstance(clazz, 1), 0)));

    private static <T> T getDefaultValue(Class<T> clazz) {
        return (T) DEFAULT_VALUES.get(clazz);
    }

    /**
     * annotation 指示器实现对标记了 Metrics 注解的方法进行匹配
     */
    @Pointcut("within(@xyz.fusheng.project.common.annotation.Metrics *)")
    public void withMetricsAnnotation() {
    }

    /**
     * within 指示器实现了匹配那些类型上标记了 @RestController 注解的方法
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerBean() {
    }

    @Around("controllerBean() || withMetricsAnnotation()")
    public Object metrics(ProceedingJoinPoint pjp) throws Throwable {
        // 通过连接点获取方法签名和方法上 Metrics 注解，并根据方法签名生成日志中要输出的方法定义描述
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Metrics metrics = signature.getMethod().getAnnotation(Metrics.class);
        String name = String.format("[%s] [%s]", signature.getDeclaringType().toString(), signature.toLongString());
        // 因为需要默认对所有 @RestController 标记的 Web 控制器实现 @Metrics 注解的功能，在这种情况下方法上必然是没有 @Metrics 注解的
        // 我们需要获取一个默认注解，可以通过手动实例化一个 @Metrics 注解的示例；也可以内部类定义 @Metrics 注解方法，然后通过反射获取注解的实例
        if (metrics == null) {
            @Metrics
            final class tempMetrics {
            }
            metrics = tempMetrics.class.getAnnotation(Metrics.class);
        }
        // 尝试从上下文获取请求的 URL
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (request != null) {
                name += String.format("[%s]", request.getRequestURL().toString());
            }
        }
        // 实现的是入参的日志输出
        if (metrics.logParameters()) {
            logger.info("[入参日志] => 调用 {} 的参数是:{}", name, objectMapper.writeValueAsString(pjp.getArgs()));
        }
        // 实现连接点方法的执行，以及成功失败的打点，出现异常的时候还需要记录日志
        Object returnValue;
        Instant start = Instant.now();
        try {
            returnValue = pjp.proceed();
            if (metrics.recordSuccessMetrics()) {
                // 在生产级代码中，我们应当考虑使用类似 Micrometer 的指标框架，把打点信息记录到时间序列数据库中，实现通过图表来查看方法的调用次数和执行时间
                logger.info("[成功打点] => 调用 {} 成功，耗时:{}ms", name, Duration.between(start, Instant.now()).toMillis());
            }
        } catch (Exception ex) {
            if (metrics.recordFailMetrics()) {
                logger.info("[失败打点] => 调用 {} 失败，耗时:{}ms，失败信息:{}", name, Duration.between(start, Instant.now()).toMillis(), ex.getMessage(), ex);
            }
            if (metrics.logException()) {
                logger.info("[异常日志] => 调用 {} 异常，异常信息:{}", name, ex.getMessage(), ex);
            }
            if (metrics.ignoreException()) {
                returnValue = getDefaultValue(signature.getReturnType());
            } else {
                throw ex;
            }
        }
        // 实现了返回值的日志输出
        if (metrics.logReturn()) {
            logger.info("[出参日志] => 调用 {} 的返回结果:{}", name, objectMapper.writeValueAsString(returnValue));
        }
        return returnValue;
    }
}

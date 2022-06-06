package xyz.fusheng.project.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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


}

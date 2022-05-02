package xyz.fusheng.project.controller.com;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import xyz.fusheng.project.common.holder.ThreadLocalContext;
import xyz.fusheng.project.model.base.BaseResult;

/**
 * @FileName: ComTestController
 * @Author: code-fusheng
 * @Date: 2022/3/27 20:20
 * @Version: 1.0
 * @Description:
 */

@RestController
@RequestMapping("/com/test")
public class ComTestController {

    private static final Logger logger = LoggerFactory.getLogger(ComTestController.class);

    @Value("${server.port:8080}")
    private String port;

    @ApiOperation("测试服务端口")
    @PostMapping("/testPort")
    public BaseResult<Object> testPort() {
        return BaseResult.success(port);
    }

    @ApiOperation("测试Jackson序列化")
    @PostMapping("/testJackson")
    public BaseResult<Object> testJackson() {
        long id = IdWorker.getId();
        logger.info("[公共测试-测试Jackson序列化问题] -> Id:{}", id);
        return BaseResult.success(id);
    }

    /**
     * TODO ThreadLocal 线程并发问题
     * 问题说明: Spring Boot 框架的请求本身是单线程的也就是支持并发，我们这里的线程变量的确是存储在本次线程中的，
     * 但是！因为 Spring Boot 的多线程依赖 web 容器 tomcat 的线程池机制，而线程池有线程复用的机制，
     * 当并发请求过大时，会导致 ThreadLocal 数据混乱，所以需要显示的做 ThreadLocal 数据清除。
     */
    @ApiOperation("测试ThreadLocal并发问题")
    @GetMapping("/testThreadLocal")
    public BaseResult<Object> testThreadLocal(@RequestParam("id") Integer id) {
        try {
            logger.info("[ThreadLocal-Before] => mark:{}", ThreadLocalContext.get().getMark());
            ThreadLocalContext.get().setMark("false");
            logger.info("[ThreadLocal-After] => mark:{}", ThreadLocalContext.get().getMark());
            return BaseResult.success(ThreadLocalContext.get().getMark());
        } finally {
            ThreadLocalContext.get().removeAll();
        }
    }

}

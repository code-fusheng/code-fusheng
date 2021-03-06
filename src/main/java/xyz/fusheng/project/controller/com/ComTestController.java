package xyz.fusheng.project.controller.com;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import xyz.fusheng.project.common.exception.BusinessException;
import xyz.fusheng.project.common.holder.ThreadLocalContext;
import xyz.fusheng.project.common.utils.ThreadPoolUtils;
import xyz.fusheng.project.core.service.ITestService;
import xyz.fusheng.project.model.base.BaseResult;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

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

    @Resource
    private ExecutorService customThreadPool;

    @ApiOperation("测试服务端口")
    @GetMapping("/testPort")
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
    @ApiOperation("测试ThreadLocal并发问题-错误")
    @GetMapping("/testThreadLocalError")
    public BaseResult<Object> testThreadLocalError(@RequestParam("id") Integer id) {
        logger.info("[ThreadLocal-Before] => mark:{}", ThreadLocalContext.get().getMark());
        ThreadLocalContext.get().setMark("false");
        logger.info("[ThreadLocal-After] => mark:{}", ThreadLocalContext.get().getMark());
        return BaseResult.success(ThreadLocalContext.get().getMark());
    }

    @ApiOperation("测试ThreadLocal并发问题-正确")
    @GetMapping("/testThreadLocalTrue")
    public BaseResult<Object> testThreadLocalTrue(@RequestParam("id") Integer id) {
        try {
            logger.info("[ThreadLocal-Before] => mark:{}", ThreadLocalContext.get().getMark());
            ThreadLocalContext.get().setMark("false");
            logger.info("[ThreadLocal-After] => mark:{}", ThreadLocalContext.get().getMark());
            return BaseResult.success(ThreadLocalContext.get().getMark());
        } finally {
            ThreadLocalContext.get().removeAll();
        }
    }

    // 辅助方法 - 用来获取一个指定元素量的模拟 ConcurrentHashMap 数据
    private ConcurrentHashMap<String, Long> getData(int count) {
        return LongStream.rangeClosed(1, count)
                .boxed()
                .collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(),
                        Function.identity(),
                        (o1, o2) -> o1, ConcurrentHashMap::new));
    }

    /**
     * TODO ConcurrentHashMap 聚合方法非线程安全问题
     * ConcurrentHashMap 虽然提供了数据结构上的线程安全技术支持，但是在 诸如 size、isEmpty、containsValue 等聚合方法上
     * 在并发情况下，可能会反映其中间状态或信息
     * 解决方案: size 部分逻辑加锁
     * PS: 这样无法充分发挥工具的特性，ConcurrentHashMap 提供了一些原子性的复合逻辑方法，例如: ComputeIfAbsent
     * ComputeIfAbsent 底层通过 Java 自带的 Unsafe 实现 CAS，在虚拟机层面确保了写入数据的原子性。
     */

    @ApiOperation("测试ConcurrentHashMap并发问题-错误")
    @GetMapping("/testConcurrentHashMapError")
    public BaseResult<Object> testConcurrentHashMapError() throws InterruptedException {
        // 初始化数据 size = 900
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(1000 - 100);
        logger.info("[ConcurrentHashMap] => initSize:{}", concurrentHashMap.size());
        // 使用线程池并发处理
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            // 查询还要补充多少元素到 1000
            int needSize = 1000 - concurrentHashMap.size();
            logger.info("[ConcurrentHashMap] => needSize:{}", needSize);
            // 根据需要的量补充元素
            concurrentHashMap.putAll(getData(needSize));
        }));
        // 等待所有任务完成
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        // 最后的元素大小
        logger.info("[ConcurrentHashMap] => finalSize:{}", concurrentHashMap.size());
        return BaseResult.success(concurrentHashMap);
    }

    @ApiOperation("测试ConcurrentHashMap并发问题-正确")
    @GetMapping("/testConcurrentHashMapTrue")
    public BaseResult<Object> testConcurrentHashMapTrue() throws InterruptedException {
        // 初始化数据 size = 900
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(1000 - 100);
        logger.info("[ConcurrentHashMap] => initSize:{}", concurrentHashMap.size());
        // 使用线程池并发处理
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            synchronized (concurrentHashMap) {
                // 查询还要补充多少元素到 1000
                int needSize = 1000 - concurrentHashMap.size();
                logger.info("[ConcurrentHashMap] => needSize:{}", needSize);
                // 根据需要的量补充元素
                concurrentHashMap.putAll(getData(needSize));
            }
        }));
        // 等待所有任务完成
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        // 最后的元素大小
        logger.info("[ConcurrentHashMap] => finalSize:{}", concurrentHashMap.size());
        return BaseResult.success(concurrentHashMap);
    }

    @ApiOperation("测试ConcurrentHashMap并发问题-优化")
    @GetMapping("/testConcurrentHashMapPro")
    public BaseResult<Object> testConcurrentHashMapPro() throws InterruptedException {
        ConcurrentHashMap<String, LongAdder> maps = new ConcurrentHashMap<>(10);
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10000000).parallel().forEach(i -> {
            String key = "item" + ThreadLocalRandom.current().nextInt(10);
            // 利用 computeIfAbsent 方法实例化 LongAdder，利用此进行线程安全记数
            maps.computeIfAbsent(key, k -> new LongAdder()).increment();
        }));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        return BaseResult.success(maps);
    }

    @ApiOperation("测试CustomThreadLocal问题")
    @GetMapping("/testCustomThreadLocal")
    public BaseResult<Object> testCustomThreadLocal() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            ThreadPoolUtils.printStats(customThreadPool);
            customThreadPool.execute(() -> {
                String payload = IntStream.rangeClosed(1, 1000)
                        .mapToObj(__ -> "a")
                        .collect(Collectors.joining("")) + UUID.randomUUID().toString();
                logger.info("ThreadPool:{}, Payload:{}", Thread.currentThread().getId(), payload);
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                }
            });
        }
        customThreadPool.shutdown();
        customThreadPool.awaitTermination(1, TimeUnit.HOURS);
        return BaseResult.success(null);
    }

    @ApiOperation("测试GlobalExceptionHandler效果")
    @PostMapping("/testGlobalExceptionHandler")
    public BaseResult<Object> testGlobalExceptionHandler() {
        if (true) {
            throw new BusinessException("测试异常");
        }
        return BaseResult.success(null);
    }

    /**
     * 可以在日志中看到，尽管在 try 代码块和 catch 代码块中都出现了异常，但是最终都被 finally 中的异常覆盖了
     * 这样可能会导致生产环境异常信息变得不稳定、不明显 怎么才算明显? 返回 Try Exception
     * @return
     */
    @ApiOperation("测试Finally代码块异常问题")
    @PostMapping("/testFinallyExceptionError")
    public BaseResult<Object> testFinallyExceptionError() {
        try {
            logger.info("[测试Finally代码块异常问题-Try]");
            throw new BusinessException("Try Exception");
        } catch (Exception e) {
            logger.info("[测试Finally代码块异常问题-Catch]");
        } finally {
            logger.info("[测试Finally代码块异常问题-Finally]");
            throw new BusinessException("Finally Exception");
        }
    }

    /**
     * 解决方案:一
     * finally 代码块自己负责异常捕获和处理，因为一个方法无法出现两个异常
     */
    @ApiOperation("测试Finally代码块异常-优化一")
    @PostMapping("/testFinallyExceptionRight1")
    public BaseResult<Object> testFinallyExceptionRight1() {
        try {
            logger.info("[测试Finally代码块异常问题-Try]");
            throw new BusinessException("Try Exception");
        } finally {
            logger.info("[测试Finally代码块异常问题-Finally]");
            try {
                throw new BusinessException("Finally Exception");
            } catch (Exception ex) {
                logger.error("[测试Finally代码块异常问题-Finally-Catch]", ex);
            }
        }
    }

    /**
     * 解决方案:二
     * 使用 addSuppressed 方法把 finally 中的异常附加到主异常上
     * 这其实是一种 try-with-resources 语法的做法，对于实现了 AutoCloseable 接口的资源，使用这种方式会合并多个异常，不会丢失!
     */
    @ApiOperation("测试Finally代码块异常-优化二")
    @PostMapping("/testFinallyExceptionRight2")
    public BaseResult<Object> testFinallyExceptionRight2() throws Exception {
        Exception e = null;
        try {
            logger.info("[测试Finally代码块异常问题-Try]");
            throw new BusinessException("Try Exception");
        } catch (Exception ex) {
            e = ex;
            logger.info("[测试Finally代码块异常问题-Catch]");
        } finally {
            logger.info("[测试Finally代码块异常问题-Finally]");
            try {
                throw new BusinessException("Finally Exception");
            } catch (Exception ex) {
                if (e != null) {
                    e.addSuppressed(ex);
                } else {
                    e = ex;
                }
            }
        }
        throw e;
    }

    @ApiOperation("测试AutoCloseable异常情况")
    @PostMapping("/testAutoCloseableException")
    public BaseResult<Object> testAutoCloseableException() throws Exception {
        try (TestResource testResource = new TestResource()) {
            testResource.read();
        }
        return BaseResult.success(null);
    }

    public class TestResource implements AutoCloseable {
        public void read() throws Exception{
            throw new Exception("read error");
        }
        @Override
        public void close() throws Exception {
            throw new Exception("close error");
        }
    }

}

package xyz.fusheng.project.common.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @FileName: ThreadPoolConfig
 * @Author: code-fusheng
 * @Date: 2022/5/2 14:15
 * @Version: 1.0
 * @Description: ThreadPool 线程池配置
 * 禁止使用 newFixedThreadPool 和 newCachedThreadPool，会因为资源耗尽问题造成 OOM
 * newFixedThreadPool 默认构造了一个长度为 Integer.MAX_VALUE 的 LinkedBlockingQueue 无界队列，虽然控制了工作线程数，但是任务队列的积压会造成内存 OOM
 * newCachedThreadPool 默认最大线程数是 Integer.MAX_VALUE，而工作对列是没有存储空间的阻塞队列，每次请求到来，都会去创建新的线程，线程创建需要分配空间作为线程栈，无限制创建必定会导致 OOM
 */

@Configuration
public class ThreadPoolConfig {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolConfig.class);
    /**
     * 获取虚拟机可用核心数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 线程池核心数量
     */
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    /**
     * 线程池最大线程容量
     */
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    /**
     * 线程空闲后的存活时长
     */
    private static final int KEEP_ALIVE_TIME = 30;

    private static final String CUSTOM_THREAD_POOL_PREFIX = "custom-thread-pool";

    @Bean("customThreadPool")
    public ExecutorService customThreadPool() {
        logger.info("[初始化自定义线程池-开始]");
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(CUSTOM_THREAD_POOL_PREFIX)
                // 未捕获异常自定义处理方式
                .setUncaughtExceptionHandler((thread, throwable) -> logger.error("[自定义线程池-发生未捕获异常] => ThreadPool:{}", thread, throwable))
                .build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                // 没有存储容量的阻塞队列
                new SynchronousQueue<>(),
                threadFactory);
        logger.info("[初始化自定义线程池-结束] => 核心线程大小:{}, 最大线程容量:{}, 工作线程数:{}, 总任务数:{}",
                threadPoolExecutor.getCorePoolSize(), threadPoolExecutor.getMaximumPoolSize(), threadPoolExecutor.getActiveCount(), threadPoolExecutor.getTaskCount());
        return threadPoolExecutor;
    }

}

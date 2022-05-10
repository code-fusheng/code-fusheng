package xyz.fusheng.project.demo;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @FileName: ThreadPoolExceptionTest
 * @Author: code-fusheng
 * @Date: 2022/5/10 23:00
 * @Version: 1.0
 * @Description: 线程池异常处理
 */

public class ThreadPoolExceptionTest {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolExceptionTest.class);

    /**
     * Case: 提交10个任务到线程池异步处理，第五个任务抛出异常，查看日志
     * 1. 我们显示的定义了线程的名字，在第五个任务发生异常后，线程池只能重新创建线程，没有起到线程重用
     * 2. 因为没有手动捕获异常，ThreadGroup 帮忙处理了未捕获的异常
     *
     * 处理步骤:
     * 1. 以 execute 方法提交到线程池的异步任务，最好在任务内部处理好异常
     * 2. 设置自定义的异常处理程序作为保底，比如在声明线程池时自定义线程池的未捕获异常处理程序
     */
    private static void testThreadPoolExceptionError() throws InterruptedException {
        String prefix = "test-thread";
        ExecutorService threadPool = Executors.newFixedThreadPool(1,
                new ThreadFactoryBuilder().setNameFormat(prefix + "%d")
                        .build());
        IntStream.rangeClosed(1, 10).forEach(i -> {
            threadPool.execute(() -> {
                if (i == 5) { throw new RuntimeException("error"); }
                logger.info("Thread Exec Done:{}", i);
            });
        });
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
    }

    // 处理方式
    private static void testThreadPoolExceptionRight() throws InterruptedException {
        String prefix = "test-thread";
        ExecutorService threadPool = Executors.newFixedThreadPool(1,
                new ThreadFactoryBuilder().setNameFormat(prefix + "%d")
                        .setUncaughtExceptionHandler((thread, throwable) -> logger.error("ThreadPool {} Exception", thread, throwable))
                        .build());
        List<Future> tasks = IntStream.rangeClosed(1, 10).mapToObj(i ->
            threadPool.submit(() -> {
                if (i == 5) { throw new RuntimeException("error"); }
                logger.info("Thread Exec Done:{}", i);
            })).collect(Collectors.toList());
        tasks.forEach(task -> {
            try {
                task.get();
            } catch (Exception e) {
                logger.error("e", e);
            }
        });
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
    }

    public static void main(String[] args) throws InterruptedException {

        testThreadPoolExceptionRight();

    }

}

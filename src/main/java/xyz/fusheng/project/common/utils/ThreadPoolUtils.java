package xyz.fusheng.project.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @FileName: ThreadPoolUtils
 * @Author: code-fusheng
 * @Date: 2022/5/2 14:46
 * @Version: 1.0
 * @Description:
 */

public class ThreadPoolUtils {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolUtils.class);

    public static void printStats(ExecutorService executorService) {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            logger.info("[线程池状态信息 - 工作线程数:{}, 完成任务数:{}, 积压任务数:{}]", threadPoolExecutor.getActiveCount(), threadPoolExecutor.getCompletedTaskCount(), threadPoolExecutor.getQueue().size());
        }, 0, 1, TimeUnit.SECONDS);
    }

}

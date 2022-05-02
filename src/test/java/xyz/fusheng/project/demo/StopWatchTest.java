package xyz.fusheng.project.demo;

import org.springframework.util.StopWatch;

/**
 * @FileName: StopWatchTest
 * @Author: code-fusheng
 * @Date: 2022/5/2 12:03
 * @Version: 1.0
 * @Description:
 */

public class StopWatchTest {

    public static void main(String[] args) throws InterruptedException {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("task1");
        Thread.sleep(1000);
        stopWatch.stop();

        stopWatch.start("task2");
        Thread.sleep(10000);
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());

    }

}

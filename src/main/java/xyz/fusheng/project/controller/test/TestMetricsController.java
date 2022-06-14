package xyz.fusheng.project.controller.test;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.fusheng.project.common.annotation.Metrics;
import xyz.fusheng.project.core.service.ITestService;
import xyz.fusheng.project.model.po.SysUser;

import javax.annotation.Resource;

/**
 * @FileName: TestMetricsController
 * @Author: code-fusheng
 * @Date: 2022/6/8 23:11
 * @Version: 1.0
 * @Description: 测试 Metrics 注解 Controller 层
 */

@RestController
@RequestMapping("/test/metrics")
@Metrics(logParameters = false, logReturn = false) // step2 改动点1
public class TestMetricsController {

    private static final Logger logger = LoggerFactory.getLogger(TestMetricsController.class);

    @Resource
    private ITestService iTestService;

    @GetMapping("transaction")
    public int transaction(@RequestParam("name") String name) {
        try {
            SysUser sysUser = new SysUser();
            sysUser.setUsername(name);
            iTestService.createUser(sysUser);
        } catch (Exception ex) {
            logger.info("[]", ex.getMessage(), ex);
        }
        return 0;
    }


}

package xyz.fusheng.project.controller.com;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fusheng.project.model.base.BaseResult;

import javax.validation.Valid;

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

}

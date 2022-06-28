package xyz.fusheng.project.controller.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fusheng.project.model.base.BaseResult;

/**
 * @FileName: WebUserController
 * @Author: code-fusheng
 * @Date: 2022/6/15 15:16
 * @Version: 1.0
 * @Description:
 */

@Api(tags = "平台用户接口")
@RestController
@RequestMapping("/web/user")
public class WebUserController {

    @ApiOperation("注册接口")
    @PostMapping("/register")
    public BaseResult<Object> register() {

        return BaseResult.success(null);
    }

}

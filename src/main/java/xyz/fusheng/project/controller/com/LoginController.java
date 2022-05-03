package xyz.fusheng.project.controller.com;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fusheng.project.core.service.IUserService;
import xyz.fusheng.project.model.base.BaseResult;
import xyz.fusheng.project.model.po.SysUser;
import xyz.fusheng.project.tools.security.entity.CustomUser;
import xyz.fusheng.project.tools.security.utils.SecurityUtils;

import javax.annotation.Resource;

/**
 * @FileName: LoginController
 * @Author: code-fusheng
 * @Date: 2022/3/29 23:14
 * @Version: 1.0
 * @Description:
 */

@Api(tags = "登录接口管理")
@RestController
public class LoginController {

    @Resource
    private IUserService iUserService;

    @ApiOperation("获取用户信息接口")
    @GetMapping("/info")
    public BaseResult<SysUser> info() {
        CustomUser userInfo = SecurityUtils.getUserInfo();
        SysUser sysUser = iUserService.selectUserByUsername(userInfo.getUsername());
        return BaseResult.success(sysUser);
    }

    @ApiOperation("注册接口")
    @PostMapping("/register")
    public BaseResult<Object> register() {

        return BaseResult.success(null);
    }

    @ApiOperation("登出接口")
    @PutMapping("/logout")
    public BaseResult<Object> logout() {

        return BaseResult.success(null);
    }

}

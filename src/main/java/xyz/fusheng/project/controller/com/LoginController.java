package xyz.fusheng.project.controller.com;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fusheng.project.core.service.IUserService;
import xyz.fusheng.project.model.base.BaseResult;
import xyz.fusheng.project.model.entity.User;
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

@RestController
public class LoginController {

    @Resource
    private IUserService iUserService;

    @GetMapping("/info")
    public BaseResult<User> info() {
        CustomUser userInfo = SecurityUtils.getUserInfo();
        User user = iUserService.selectUserByUsername(userInfo.getUsername());
        return BaseResult.success(user);
    }

}

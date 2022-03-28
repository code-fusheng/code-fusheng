package xyz.fusheng.project.tools.security.core.evaluator;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import xyz.fusheng.project.core.service.IUserService;
import xyz.fusheng.project.model.entity.Menu;
import xyz.fusheng.project.tools.security.entity.CustomUser;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @FileName: CustomUserPermissionEvaluator
 * @Author: code-fusheng
 * @Date: 2022/3/28 00:06
 * @Version: 1.0
 * @Description: 自定义用户权限注解验证
 */

@Component
public class CustomUserPermissionEvaluator implements PermissionEvaluator {

    @Resource
    private IUserService iUserService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object permission) {
        // 获取用户信息
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        // 查询用户权限（这里可以将权限放入缓存提高效率）
        Set<String> permissions = new HashSet<>();
        List<Menu> menuList = iUserService.selectMenuByUserId(customUser.getUserId());
        for (Menu menu : menuList) {
            permissions.add(menu.getPermission());
        }
        // 权限对比
        if (permissions.contains(permission.toString())){
            return true;
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}

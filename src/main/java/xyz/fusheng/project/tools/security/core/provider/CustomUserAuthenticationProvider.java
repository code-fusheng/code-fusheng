package xyz.fusheng.project.tools.security.core.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import xyz.fusheng.project.common.exception.BusinessException;
import xyz.fusheng.project.common.utils.CommonUtils;
import xyz.fusheng.project.core.service.IUserService;
import xyz.fusheng.project.model.po.SysRole;
import xyz.fusheng.project.tools.security.core.service.CustomUserDetailsService;
import xyz.fusheng.project.tools.security.entity.CustomUser;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @FileName: CustomUserAuthenticationProvider
 * @Author: code-fusheng
 * @Date: 2022/3/28 00:05
 * @Version: 1.0
 * @Description: 自定义用户登录验证
 */

@Component
public class CustomUserAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private CustomUserDetailsService customUserDetailsService;

    @Resource
    private IUserService iUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 取出表单中输入的用户名
        String username = (String) authentication.getPrincipal();
        // 获取表单中输入的密码
        String password = (String) authentication.getCredentials();
        // 查询用户是否存在
        CustomUser customUser = customUserDetailsService.loadUserByUsername(username);
        if (CommonUtils.objAndAtrIsNull(customUser)) {
            throw new BusinessException("用户名不存在!");
        }
        // 我们还要判断密码是否正确，这里我们的密码使用BCryptPasswordEncoder进行加密的
        if (!new BCryptPasswordEncoder().matches(password, customUser.getPassword())) {
            throw new BadCredentialsException("密码不正确!");
        }
        // 角色集合
        Set<GrantedAuthority> authorities = new HashSet<>();
        // 查询用户角色
        List<SysRole> sysRoleList = iUserService.selectRoleByUserId(customUser.getUserId());
        for (SysRole sysRole : sysRoleList){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + sysRole.getRoleName()));
        }
        customUser.setAuthorities(authorities);
        // 进行登录
        return new UsernamePasswordAuthenticationToken(customUser, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}

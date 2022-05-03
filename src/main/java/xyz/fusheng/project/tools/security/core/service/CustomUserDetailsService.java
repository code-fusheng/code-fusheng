package xyz.fusheng.project.tools.security.core.service;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import xyz.fusheng.project.common.utils.CommonUtils;
import xyz.fusheng.project.core.service.IUserService;
import xyz.fusheng.project.model.po.SysUser;
import xyz.fusheng.project.tools.security.entity.CustomUser;

import javax.annotation.Resource;

/**
 * @FileName: CustomUserDetailsService
 * @Author: code-fusheng
 * @Date: 2022/3/27 23:18
 * @Version: 1.0
 * @Description:
 */

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private IUserService iUserService;

    /**
     * 查询用户信息根据 username
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        SysUser sysUser = iUserService.selectUserByUsername(username);
        if (CommonUtils.objAndAtrIsNull(sysUser)) {
            return null;
        }
        // 组装参数
        CustomUser customUser = new CustomUser();
        BeanUtils.copyProperties(sysUser, customUser);
        return customUser;
    }

    /**
     * 查询用户信息根据 mobile 手机号
     * @param mobile
     * @return
     */
    public CustomUser loadUserByMobile(String mobile) {
        // 查询用户信息
        SysUser sysUser = iUserService.selectUserByMobile(mobile);
        if (CommonUtils.objAndAtrIsNull(sysUser)) {
            return null;
        }
        // 组装参数
        CustomUser customUser = new CustomUser();
        BeanUtils.copyProperties(sysUser, customUser);
        return customUser;
    }

}

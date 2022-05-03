package xyz.fusheng.project.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.fusheng.project.model.po.SysMenu;
import xyz.fusheng.project.model.po.SysRole;
import xyz.fusheng.project.model.po.SysUser;

import java.util.List;

/**
 * <p>
 * 系统表-用户表 服务类
 * </p>
 *
 * @author code-fusheng
 * @since 2022-03-27
 */
public interface IUserService extends IService<SysUser> {

    /**
     * 查询用户信息根据 username 用户名
     * @param username
     * @return
     */
    SysUser selectUserByUsername(String username);

    /**
     * 查询用户信息根据 mobile 手机号
     * @param mobile
     * @return
     */
    SysUser selectUserByMobile(String mobile);

    /**
     * 根据用户id查询用户角色集合
     * @param userId
     * @return
     */
    List<SysRole> selectRoleByUserId(Long userId);

    /**
     * 根据用户id查询权限集合
     * @param userId
     * @return
     */
    List<SysMenu> selectMenuByUserId(Long userId);
}

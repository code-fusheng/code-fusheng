package xyz.fusheng.project.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.fusheng.project.model.entity.Menu;
import xyz.fusheng.project.model.entity.Role;
import xyz.fusheng.project.model.entity.User;

import java.util.List;

/**
 * <p>
 * 系统表-用户表 服务类
 * </p>
 *
 * @author code-fusheng
 * @since 2022-03-27
 */
public interface IUserService extends IService<User> {

    /**
     * 查询用户信息根据 username 用户名
     * @param username
     * @return
     */
    User selectUserByUsername(String username);

    /**
     * 查询用户信息根据 mobile 手机号
     * @param mobile
     * @return
     */
    User selectUserByMobile(String mobile);

    /**
     * 根据用户id查询用户角色集合
     * @param userId
     * @return
     */
    List<Role> selectRoleByUserId(Long userId);

    /**
     * 根据用户id查询权限集合
     * @param userId
     * @return
     */
    List<Menu> selectMenuByUserId(Long userId);
}

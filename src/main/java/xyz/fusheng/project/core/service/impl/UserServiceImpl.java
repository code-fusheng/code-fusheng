package xyz.fusheng.project.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.fusheng.project.common.constants.GlobalConstants;
import xyz.fusheng.project.core.mapper.UserMapper;
import xyz.fusheng.project.model.entity.Menu;
import xyz.fusheng.project.model.entity.Role;
import xyz.fusheng.project.model.entity.User;
import xyz.fusheng.project.core.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 系统表-用户表 服务实现类
 * </p>
 *
 * @author code-fusheng
 * @since 2022-03-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 查询用户信息根据 username
     * @param username
     * @return
     */
    @Override
    public User selectUserByUsername(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getIsEnabled, GlobalConstants.YES));
        return user;
    }

    /**
     * 查询用户信息根据 mobile 手机号
     * @param mobile
     * @return
     */
    @Override
    public User selectUserByMobile(String mobile) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getMobile, mobile)
                .eq(User::getIsEnabled, GlobalConstants.YES));
        return user;
    }

    /**
     * 根据用户id查询用户角色集合
     * @param userId
     * @return
     */
    @Override
    public List<Role> selectRoleByUserId(Long userId) {
        return this.baseMapper.selectRoleByUserId(userId);
    }

    /**
     * 根据用户id查询用户权限集合
     * @param userId
     * @return
     */
    @Override
    public List<Menu> selectMenuByUserId(Long userId) {
        return this.baseMapper.selectMenuByUserId(userId);
    }
}

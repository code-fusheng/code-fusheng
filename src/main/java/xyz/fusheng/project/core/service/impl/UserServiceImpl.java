package xyz.fusheng.project.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.fusheng.project.common.constants.GlobalConstants;
import xyz.fusheng.project.core.mapper.UserMapper;
import xyz.fusheng.project.core.service.IUserService;
import xyz.fusheng.project.model.po.SysMenu;
import xyz.fusheng.project.model.po.SysRole;
import xyz.fusheng.project.model.po.SysUser;

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
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements IUserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 查询用户信息根据 username
     * @param username
     * @return
     */
    @Override
    public SysUser selectUserByUsername(String username) {
        SysUser sysUser = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .eq(SysUser::getIsEnabled, GlobalConstants.YES));
        return sysUser;
    }

    /**
     * 查询用户信息根据 mobile 手机号
     * @param mobile
     * @return
     */
    @Override
    public SysUser selectUserByMobile(String mobile) {
        SysUser sysUser = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getMobile, mobile)
                .eq(SysUser::getIsEnabled, GlobalConstants.YES));
        return sysUser;
    }

    /**
     * 根据用户id查询用户角色集合
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> selectRoleByUserId(Long userId) {
        return this.baseMapper.selectRoleByUserId(userId);
    }

    /**
     * 根据用户id查询用户权限集合
     * @param userId
     * @return
     */
    @Override
    public List<SysMenu> selectMenuByUserId(Long userId) {
        return this.baseMapper.selectMenuByUserId(userId);
    }
}

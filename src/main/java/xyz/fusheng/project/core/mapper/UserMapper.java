package xyz.fusheng.project.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.fusheng.project.model.po.SysMenu;
import xyz.fusheng.project.model.po.SysRole;
import xyz.fusheng.project.model.po.SysUser;

import java.util.List;

/**
 * <p>
 * 系统表-用户表 Mapper 接口
 * </p>
 *
 * @author code-fusheng
 * @since 2022-03-27
 */
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户id查询用户角色集合
     * @param id
     * @return
     */
    List<SysRole> selectRoleByUserId(Long id);

    /**
     * 根据用户id查询权限集合
     * @param id
     * @return
     */
    List<SysMenu> selectMenuByUserId(Long id);
}

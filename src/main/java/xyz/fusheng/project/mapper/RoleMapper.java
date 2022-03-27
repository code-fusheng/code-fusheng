package xyz.fusheng.project.mapper;

import xyz.fusheng.project.model.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统表-角色表 Mapper 接口
 * </p>
 *
 * @author code-fusheng
 * @since 2022-03-27
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}

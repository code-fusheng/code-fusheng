package xyz.fusheng.project.core.service.impl;

import xyz.fusheng.project.model.entity.Role;
import xyz.fusheng.project.mapper.RoleMapper;
import xyz.fusheng.project.core.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统表-角色表 服务实现类
 * </p>
 *
 * @author code-fusheng
 * @since 2022-03-27
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}

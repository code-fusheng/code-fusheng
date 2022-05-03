package xyz.fusheng.project.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.fusheng.project.core.mapper.RoleMapper;
import xyz.fusheng.project.core.service.IRoleService;
import xyz.fusheng.project.model.po.SysRole;

/**
 * <p>
 * 系统表-角色表 服务实现类
 * </p>
 *
 * @author code-fusheng
 * @since 2022-03-27
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, SysRole> implements IRoleService {

}

package xyz.fusheng.project.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.fusheng.project.core.mapper.MenuMapper;
import xyz.fusheng.project.core.service.IMenuService;
import xyz.fusheng.project.model.po.SysMenu;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author code-fusheng
 * @since 2022-03-27
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, SysMenu> implements IMenuService {

}

package xyz.fusheng.project.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.fusheng.project.core.mapper.UserMapper;
import xyz.fusheng.project.model.entity.User;
import xyz.fusheng.project.core.service.IUserService;
import org.springframework.stereotype.Service;

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

}

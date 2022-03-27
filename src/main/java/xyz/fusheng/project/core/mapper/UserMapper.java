package xyz.fusheng.project.core.mapper;

import xyz.fusheng.project.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统表-用户表 Mapper 接口
 * </p>
 *
 * @author code-fusheng
 * @since 2022-03-27
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

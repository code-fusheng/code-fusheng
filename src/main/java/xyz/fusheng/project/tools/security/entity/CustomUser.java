package xyz.fusheng.project.tools.security.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.fusheng.project.common.constants.GlobalConstants;

/**
 * @FileName: SelfUser
 * @Author: code-fusheng
 * @Date: 2022/3/27 22:37
 * @Version: 1.0
 * @Description:
 */

@Data
public class CustomUser implements Serializable, UserDetails{

    @ApiModelProperty("主键Id")
    private Long userId;

    @ApiModelProperty("全局唯一用户ID")
    private String uuid;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("电话号码")
    private String mobile;

    @ApiModelProperty("邮箱")
    private String mail;

    @ApiModelProperty("真实姓名")
    private String realname;

    @ApiModelProperty("是否启用(1:已启用/0:未启用)")
    private Integer isEnabled;

    @ApiModelProperty("用户角色")
    private Collection<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return Objects.equals(isEnabled, GlobalConstants.NO);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return Objects.equals(isEnabled, GlobalConstants.YES);
    }
}

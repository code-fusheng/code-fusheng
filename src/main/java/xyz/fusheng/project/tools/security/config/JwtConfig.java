package xyz.fusheng.project.tools.security.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @FileName: JwtConfig
 * @Author: code-fusheng
 * @Date: 2022/3/28 23:06
 * @Version: 1.0
 * @Description:
 */

@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * 密钥KEY
     */
    public static String secret;
    /**
     * TokenKey
     */
    public static String tokenHeader;
    /**
     * Token前缀字符
     */
    public static String tokenPrefix;
    /**
     * 过期时间
     */
    public static Integer expiration;
    /**
     * 不需要认证的接口
     */
    public static String antMatchers;


    public void setSecret(String secret) {
        JwtConfig.secret = secret;
    }

    public void setTokenHeader(String tokenHeader) {
        JwtConfig.tokenHeader = tokenHeader;
    }

    public void setTokenPrefix(String tokenPrefix) {
        JwtConfig.tokenPrefix = tokenPrefix;
    }

    public void setExpiration(Integer expiration) {
        JwtConfig.expiration = expiration * 1000;
    }

    public void setAntMatchers(String antMatchers) {
        JwtConfig.antMatchers = antMatchers;
    }

}

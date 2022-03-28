package xyz.fusheng.project.tools.security.utils;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.fusheng.project.tools.security.config.JwtConfig;
import xyz.fusheng.project.tools.security.entity.CustomUser;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @FileName: JwtTokenUtil
 * @Author: code-fusheng
 * @Date: 2022/3/28 23:10
 * @Version: 1.0
 * @Description:
 */

public class JwtTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    /**
     * 私有化构造器
     */
    private JwtTokenUtil() {}

    /**
     * 生成 Token
     * @param customUser 用户安全实体
     * @return Token
     */
    public static String createAccessToken(CustomUser customUser){
        // 登录成功生成 JWT
        String token = Jwts.builder()
                // 放入用户名和用户id
                .setId(customUser.getUserId() + "")
                // 主题
                .setSubject(customUser.getUsername())
                // 签发时间
                .setIssuedAt(new Date())
                // 签发者
                .setIssuer("zh")
                // 自定义属性，放入用户拥有的权限
                .claim("authorities", JSON.toJSONString(customUser.getAuthorities()))
                // 失效时间
                .setExpiration(new Date(System.currentTimeMillis() + JwtConfig.expiration))
                // 签名算法和密钥
                .signWith(SignatureAlgorithm.HS512, JwtConfig.secret)
                .compact();
        return token;
    }

}

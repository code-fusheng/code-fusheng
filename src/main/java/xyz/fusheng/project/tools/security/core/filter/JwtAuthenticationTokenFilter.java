package xyz.fusheng.project.tools.security.core.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import xyz.fusheng.project.tools.security.config.JwtConfig;
import xyz.fusheng.project.tools.security.entity.CustomUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @FileName: JwtAuthenticationTokenFilter
 * @Author: code-fusheng
 * @Date: 2022/3/29 00:08
 * @Version: 1.0
 * @Description: Jwt接口请求校验拦截器
 */

public class JwtAuthenticationTokenFilter extends BasicAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 获取请求头中JWT的Token
        String tokenHeader = request.getHeader(JwtConfig.tokenHeader);
        if (null!=tokenHeader && tokenHeader.startsWith(JwtConfig.tokenPrefix)) {
            try {
                // 截取JWT前缀
                String token = tokenHeader.replace(JwtConfig.tokenPrefix, "");
                // 解析JWT
                Claims claims = Jwts.parser()
                        .setSigningKey(JwtConfig.secret)
                        .parseClaimsJws(token)
                        .getBody();
                // 获取用户名
                String username = claims.getSubject();
                String userId = claims.getId();
                if(!StringUtils.isEmpty(username)&&!StringUtils.isEmpty(userId)) {
                    //组装参数
                    CustomUser customUser = new CustomUser();
                    customUser.setUsername(claims.getSubject());
                    customUser.setUserId(Long.parseLong(claims.getId()));
                    // selfUser.setAuthorities(authorities);
                    customUser.setAuthorities(null);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUser, userId, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e){
                logger.info("Token过期");
            } catch (Exception e) {
                logger.info("Token无效");
            }
        }
        filterChain.doFilter(request, response);
        return;
    }
}

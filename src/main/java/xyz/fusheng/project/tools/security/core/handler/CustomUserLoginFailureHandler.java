package xyz.fusheng.project.tools.security.core.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import xyz.fusheng.project.model.base.BaseResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @FileName: CustomUserLoginFailureHandler
 * @Author: code-fusheng
 * @Date: 2022/3/27 23:59
 * @Version: 1.0
 * @Description: 自定义用户登录失败处理
 */

@Component
public class CustomUserLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if (e instanceof UsernameNotFoundException) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(new BaseResult<>(500, "登录失败: 用户名不存在！")));
        }
        if (e instanceof LockedException) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(new BaseResult<>(500, "登录失败: 用户被冻结！")));
        }
        if (e instanceof BadCredentialsException) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(new BaseResult<>(500, "登录失败: 用户名密码不正确！")));
        }
        // TODO 待完善
    }
}

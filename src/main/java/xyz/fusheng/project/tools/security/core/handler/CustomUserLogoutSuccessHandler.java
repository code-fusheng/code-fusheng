package xyz.fusheng.project.tools.security.core.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import xyz.fusheng.project.model.base.BaseResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @FileName: CustomUserLogoutSuccessHandler
 * @Author: code-fusheng
 * @Date: 2022/3/28 00:02
 * @Version: 1.0
 * @Description: 自定义用户退出登录成功处理
 */

@Component
public class CustomUserLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(new BaseResult<>("登出成功!")));
    }

}

package xyz.fusheng.project.tools.security.core.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import xyz.fusheng.project.common.enums.ResultEnum;
import xyz.fusheng.project.model.base.BaseResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @FileName: CustomUserAuthenticationEntryPointHandler
 * @Author: code-fusheng
 * @Date: 2022/3/27 23:57
 * @Version: 1.0
 * @Description: 自定义用户未登录处理
 */

@Component
public class CustomUserAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(new BaseResult<>(ResultEnum.NOT_LOGIN)));
    }
}

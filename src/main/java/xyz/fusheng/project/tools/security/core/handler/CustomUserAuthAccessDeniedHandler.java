package xyz.fusheng.project.tools.security.core.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import xyz.fusheng.project.common.enums.ResultEnum;
import xyz.fusheng.project.model.base.BaseResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @FileName: CustomUserAuthAccessDeniedHandler
 * @Author: code-fusheng
 * @Date: 2022/3/27 23:56
 * @Version: 1.0
 * @Description: 自定义用户暂无权限返回结果处理
 */

@Component
public class CustomUserAuthAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(new BaseResult<>(ResultEnum.NOT_AUTHORIZED)));
    }
}

package xyz.fusheng.project.tools.security.core.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import xyz.fusheng.project.common.enums.ResultEnum;
import xyz.fusheng.project.common.utils.CommonUtils;
import xyz.fusheng.project.model.base.BaseResult;
import xyz.fusheng.project.tools.security.config.JwtConfig;
import xyz.fusheng.project.tools.security.entity.CustomUser;
import xyz.fusheng.project.tools.security.utils.JwtTokenUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @FileName: CustomUserLoginSuccessHandler
 * @Author: code-fusheng
 * @Date: 2022/3/28 00:01
 * @Version: 1.0
 * @Description: 自定义用户登录成功处理
 */

@Component
public class CustomUserLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 组装 JWT
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        if (!CommonUtils.objAndAtrIsNull(customUser)) {
            BeanUtils.copyProperties(authentication.getPrincipal(), customUser);
        }
        String token = JwtConfig.tokenPrefix + JwtTokenUtil.createAccessToken(customUser);
        // 封装返回参数
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(new BaseResult<>("登录成功!", token)));


    }
}

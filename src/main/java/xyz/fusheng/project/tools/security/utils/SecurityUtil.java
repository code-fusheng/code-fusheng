package xyz.fusheng.project.tools.security.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import xyz.fusheng.project.tools.security.core.service.CustomUserDetailsService;
import xyz.fusheng.project.tools.security.entity.CustomUser;

import javax.annotation.Resource;

/**
 * @FileName: SecurityUtil
 * @Author: code-fusheng
 * @Date: 2022/3/29 23:09
 * @Version: 1.0
 * @Description:
 */

public class SecurityUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    private SecurityUtil() {
    }

    public static CustomUser getUserInfo() {
        CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customUser;
    }

}

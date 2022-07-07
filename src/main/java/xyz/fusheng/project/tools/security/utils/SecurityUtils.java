package xyz.fusheng.project.tools.security.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import xyz.fusheng.project.common.enums.ResultEnum;
import xyz.fusheng.project.common.exception.BusinessException;
import xyz.fusheng.project.tools.security.entity.CustomUser;

/**
 * @FileName: SecurityUtil
 * @Author: code-fusheng
 * @Date: 2022/3/29 23:09
 * @Version: 1.0
 * @Description:
 */

public class SecurityUtils {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    private SecurityUtils() {
    }

    public static CustomUser getUserInfo() {
        try {
            CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return customUser;
        } catch (Exception e) {
            throw new BusinessException(ResultEnum.AUTH_FAILED);
        }
    }

}

package xyz.fusheng.project.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @FileName: CommonUtils
 * @Author: code-fusheng
 * @Date: 2022/3/14 2:39 下午
 * @Version: 1.0
 * @Description:
 */

public final class CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * 检查对象是否为空并且属性是否全为空
     * @param obj
     * @return
     * Remark : 此方法无法处理对象中包含基本类型的情况,基本类型带有默认值
     */
    public static boolean objAndAtrIsNull(Object obj) {
        if (null == obj) {
            return true;
        }
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.get(obj) != null && StringUtils.isNotBlank(field.get(obj).toString())) {
                    return false;
                }
            }
        } catch (Exception e) {
            logger.info("[CommonUtils-检查对象是否为空异常] -> 异常信息:{}", e.getMessage());
        }
        return true;
    }

}

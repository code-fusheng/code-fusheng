package xyz.fusheng.project.common.exception;

import xyz.fusheng.project.common.enums.ResultEnum;

/**
 * @FileName: Exceptions
 * @Author: code-fusheng
 * @Date: 2022/5/6 23:17
 * @Version: 1.0
 * @Description: 异常类
 */

public class Exceptions {

    /**
     * 错误的定义
     * 原因: 把异常定义为静态变量会导致异常信息固化，和异常的栈一定是需要根据当前调用来动态获取项目矛盾
     */
    // public static BusinessException ORDER_EXISTS = new BusinessException(ResultEnum.BUSINESS_ERROR.getCode(), "订单已经存在!");

    /**
     * 正确的定义 需要动态的 new
     */
    public static BusinessException orderExists() {
        return new BusinessException(ResultEnum.BUSINESS_ERROR.getCode(), "订单已经存在!");
    }

}

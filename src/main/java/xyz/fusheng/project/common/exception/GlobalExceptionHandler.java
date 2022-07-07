package xyz.fusheng.project.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.fusheng.project.common.enums.ResultEnum;
import xyz.fusheng.project.common.utils.StringUtils;
import xyz.fusheng.project.model.base.BaseResult;

/**
 * @FileName: GlobalExceptionHandler
 * @Author: code-fusheng
 * @Date: 2022/5/6 22:00
 * @Version: 1.0
 * @Description: 全局统一异常处理
 * PS: @RestControllerAdvice + @ExceptionHandler
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = BusinessException.class)
    public BaseResult<Object> businessExceptionHandler(BusinessException e) {
        //logger.error("全局统一异常处理-业务异常 => 异常信息:{}", e.getMessage(), e);
        return BaseResult.error(ResultEnum.BUSINESS_ERROR.getCode(), ResultEnum.BUSINESS_ERROR.getMsg() + ":" + e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public BaseResult<Object> exceptionHandler(Exception e) {
        //logger.error("全局统一异常处理-系统异常 => 异常信息:{}", e.getMessage(), e);
        return BaseResult.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMsg() + ":" + e.getMessage());
    }


}

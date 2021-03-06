package xyz.fusheng.project.common.exception;

import lombok.Getter;
import xyz.fusheng.project.common.enums.ResultEnum;

/**
 * @FileName: BusinessException
 * @Author: code-fusheng
 * @Date: 2021/4/12 7:54 下午
 * @Version: 1.0
 * @Description: 自定义异常类 - 业务异常
 */

@Getter
public class BusinessException extends RuntimeException {

    /**
     * RuntimeException 运行时异常：是那些可能在 Java 虚拟机正常运行期间抛出的异常的超类。
     * 可能在执行方法期间抛出但未被捕获的RuntimeException 的任何子类都无需在 throws 子句中进行声明。
     * 它是Exception的子类。
     */

    private Integer errorCode = ResultEnum.BUSINESS_ERROR.getCode();

    public BusinessException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.errorCode = resultEnum.getCode();
    }

    public BusinessException(ResultEnum resultEnum, Throwable throwable) {
        super(resultEnum.getMsg(), throwable);
        this.errorCode = resultEnum.getCode();
    }

    public BusinessException(Integer errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(Throwable throwable) {
        super(throwable);
    }

    public BusinessException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

}

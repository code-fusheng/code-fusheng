package xyz.fusheng.project.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xyz.fusheng.project.common.enums.ResultEnum;

import java.io.Serializable;

/**
 * @FileName: BaseResult
 * @Author: code-fusheng
 * @Date: 2021/4/7 11:35 上午
 * @Version: 1.0
 * @Description: 统一返回结果
 */

@ApiModel(value = "统一返回结果-视图对象")
@Data
public class BaseResult<T> implements Serializable {

    /**
     * 1. File -> setting -> plugins  安装 GenerateSerialVersionUID 插件
     * 2. Win 光标 Serializable Alt+Insert 生成序列化唯一标识
     * 3. Mac Serializable command + N
     */

    @ApiModelProperty(value = "状态码")
    private Integer code;
    @ApiModelProperty(value = "消息提示")
    private String msg;
    @ApiModelProperty(value = "数据")
    private T data;

    public static final BaseResult<String> SUCCESS = new BaseResult(ResultEnum.SUCCESS.getCode() , ResultEnum.SUCCESS.getMsg(), null);
    public static final BaseResult<String> ERROR = new BaseResult(ResultEnum.ERROR.getCode() , ResultEnum.ERROR.getMsg(), null);

    /**
     * 默认情况下返回成功结果集
     * 说明 ： 以下情况如何理解
     * 只有msg参数，说明请求未返回结果，比如保存，修改等操作，直接对应信息
     * 害。具体情况集体分析吧
     */
    public BaseResult() {
        this.code = ResultEnum.SUCCESS.getCode();
        this.msg = ResultEnum.SUCCESS.getMsg();
    }

    /**
     * 默认失败
     *
     * @param code
     * @param msg
     * @param data
     */
    public BaseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResult(String msg) {
        this.code = ResultEnum.SUCCESS.getCode();
        this.msg = msg;
    }

    public BaseResult(T data) {
        this.code = ResultEnum.SUCCESS.getCode();
        this.msg = ResultEnum.SUCCESS.getMsg();
        this.data = data;
    }

    public BaseResult(String msg, T data) {
        this.code = ResultEnum.SUCCESS.getCode();
        this.msg = msg;
        this.data = data;
    }

    public BaseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResult(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public static <T> BaseResult<T> getResult(Integer code, String msg, T data) {
        BaseResult<T> baseResult = new BaseResult();
        baseResult.setCode(code);
        baseResult.setMsg(msg);
        baseResult.setData(data);
        return baseResult;
    }

    public static <T> BaseResult<T> success() {
        return getResult(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null);
    }

    public static <T> BaseResult<T> success(T data) {
        return getResult(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data);
    }

    public static <T> BaseResult<T> error(String msg) {
        return getResult(ResultEnum.ERROR.getCode(), msg, null);
    }

    public static <T> BaseResult<T> error(Integer code, String msg) {
        return getResult(code, msg, null);
    }
}


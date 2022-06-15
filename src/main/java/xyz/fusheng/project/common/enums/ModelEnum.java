package xyz.fusheng.project.common.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

/**
 * @FileName: ModelEnum
 * @Author: code-fusheng
 * @Date: 2022/5/23 23:21
 * @Version: 1.0
 * @Description: 枚举参考模版
 */

@Getter
public enum ModelEnum implements IEnumCode<String> {

    /**
     * 枚举的 code 建议使用语意性的字符串，而尽量不要是用 int
     */
    MODEL("MODEL", "模版"),

    /**
     * 枚举添加默认值，待完善
     */
    @JsonEnumDefaultValue
    UNKNOWN(null, "");

    private final String code;
    private final String desc;

    ModelEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

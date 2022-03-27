package xyz.fusheng.project.common.enums;

import lombok.Getter;

/**
 * @FileName: WeekdayEnum
 * @Author: code-fusheng
 * @Date: 2022/3/27 19:35
 * @Version: 1.0
 * @Description: 星期枚举
 */

@Getter
public enum WeekdayEnum {

    MONDAY("MONDAY", "星期一"),
    TUESDAY("TUESDAY", "星期二"),
    WEDNESDAY("WEDNESDAY", "星期三"),
    THURSDAY("THURSDAY", "星期四"),
    FRIDAY("FRIDAY", "星期五"),
    SATURDAY("SATURDAY", "星期六"),
    SUNDAY("SUNDAY", "星期天"),
    ;

    private String code;
    private String msg;

    WeekdayEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}

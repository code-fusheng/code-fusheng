package xyz.fusheng.project.model.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

/**
 * @FileName: RegisterUserDto
 * @Author: code-fusheng
 * @Date: 2022/6/15 15:42
 * @Version: 1.0
 * @Description:
 */

@Api(tags = "注册用户DTO")
public class RegisterUserDto {

    @ApiModelProperty(value = "主键Id", required = true, example = "1")
    private Long userId;

    @ApiModelProperty(value = "UUID", hidden = true, example = "")
    private String uuid;

}

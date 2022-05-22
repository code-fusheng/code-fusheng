package xyz.fusheng.project.model.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @FileName: BaseEntity
 * @Author: code-fusheng
 * @Date: 2022/3/26 18:22
 * @Version: 1.0
 * @Description:
 */

@Data
public class BaseEntity implements Serializable {

    @ApiModelProperty("创建者编号")
    @TableField(fill = FieldFill.INSERT)
    private String creatorId;

    @ApiModelProperty("修改者编号")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updaterId;

    @ApiModelProperty("创建者姓名")
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    @ApiModelProperty("修改者姓名")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updaterName;

    @ApiModelProperty("创建时间")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}

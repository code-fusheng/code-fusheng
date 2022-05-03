package xyz.fusheng.project.model.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xyz.fusheng.project.model.base.BasePo;

import java.util.Date;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author code-fusheng
 * @since 2022-03-27
 */
@Data
@TableName("sys_menu")
@ApiModel(value = "Menu对象", description = "权限表")
public class SysMenu extends BasePo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键Id")
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("权限标识")
    private String permission;

    @ApiModelProperty("权限路由")
    private String path;

    @ApiModelProperty("父级id")
    private Integer pid;

    @ApiModelProperty("权限级别 1 2 3")
    private Integer level;

    @ApiModelProperty("状态")
    private Integer state;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("拓展")
    private String memo;

    @ApiModelProperty("乐观锁 默认1")
    @Version
    private Integer version;

    @ApiModelProperty("逻辑删除位 1：已删除 0：未删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty("状态位 1：启用 0：弃用")
    private Integer isEnabled;

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


    public static final String MENU_ID = "menuId";

    public static final String NAME = "name";

    public static final String PERMISSION = "permission";

    public static final String PATH = "path";

    public static final String PID = "pid";

    public static final String LEVEL = "level";

    public static final String STATE = "state";

    public static final String REMARK = "remark";

    public static final String MEMO = "memo";

    public static final String VERSION = "version";

    public static final String IS_DELETED = "is_deleted";

    public static final String IS_ENABLED = "is_enabled";

    public static final String CREATOR_ID = "creator_id";

    public static final String UPDATER_ID = "updater_id";

    public static final String CREATOR_NAME = "creator_name";

    public static final String UPDATER_NAME = "updater_name";

    public static final String CREATED_TIME = "created_time";

    public static final String UPDATE_TIME = "update_time";

}

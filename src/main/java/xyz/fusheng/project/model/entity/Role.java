package xyz.fusheng.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import xyz.fusheng.project.model.base.BaseEntity;

import java.util.Date;

/**
 * <p>
 * 系统表-角色表
 * </p>
 *
 * @author code-fusheng
 * @since 2022-03-27
 */
@Getter
@Setter
@TableName("sys_role")
@ApiModel(value = "Role对象", description = "系统表-角色表")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键Id")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("状态")
    private Integer state;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("拓展")
    private String memo;

    @ApiModelProperty("乐观锁 默认1")
    @Version
    private Integer version;

    @ApiModelProperty("是否启用(1:已启用/0:未启用)")
    private Integer isEnabled;

    @ApiModelProperty("是否逻辑删除(1:已删除/0:未删除)")
    @TableLogic
    private Integer isDeleted;

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

    @ApiModelProperty("修改时间")
    private Date updatedTime;


    public static final String ROLE_ID = "role_id";

    public static final String ROLE_NAME = "role_name";

    public static final String STATE = "state";

    public static final String REMARK = "remark";

    public static final String MEMO = "memo";

    public static final String VERSION = "version";

    public static final String IS_ENABLED = "is_enabled";

    public static final String IS_DELETED = "is_deleted";

    public static final String CREATOR_ID = "creator_id";

    public static final String UPDATER_ID = "updater_id";

    public static final String CREATOR_NAME = "creator_name";

    public static final String UPDATER_NAME = "updater_name";

    public static final String CREATED_TIME = "created_time";

    public static final String UPDATED_TIME = "updated_time";

}

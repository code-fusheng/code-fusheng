package xyz.fusheng.project.model.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xyz.fusheng.project.model.base.BasePo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 系统表-用户表
 * </p>
 *
 * @author code-fusheng
 * @since 2022-03-27
 */
@Data
@TableName("sys_user")
@ApiModel(value = "User对象", description = "系统表-用户表")
public class SysUser extends BasePo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键Id")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty("全局唯一用户ID")
    private String uuid;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("电话号码")
    private String mobile;

    @ApiModelProperty("邮箱")
    private String mail;

    @ApiModelProperty("签名")
    private String signature;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("真实姓名")
    private String realname;

    @ApiModelProperty("性别 0:私密 1:男 2:女")
    private Integer sex;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("经度")
    private BigDecimal lng;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

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


    public static final String USER_ID = "user_id";

    public static final String UUID = "uuid";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String AVATAR = "avatar";

    public static final String MOBILE = "mobile";

    public static final String MAIL = "mail";

    public static final String SIGNATURE = "signature";

    public static final String DESCRIPTION = "description";

    public static final String REALNAME = "realname";

    public static final String SEX = "sex";

    public static final String ADDRESS = "address";

    public static final String LNG = "lng";

    public static final String LAT = "lat";

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

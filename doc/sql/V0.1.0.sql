DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT COMMENT '主键Id',
    `uuid`         varchar(200) NOT NULL COMMENT '全局唯一用户ID',
    `username`     varchar(50)  NOT NULL COMMENT '用户名',
    `password`     varchar(200) NOT NULL COMMENT '密码',
    `avatar`       varchar(255)   DEFAULT NULL COMMENT '头像',
    `mobile`       varchar(100)   DEFAULT NULL COMMENT '电话号码',
    `mail`         varchar(100)   DEFAULT NULL COMMENT '邮箱',
    `signature`    varchar(255)   DEFAULT NULL COMMENT '签名',
    `description`  varchar(255)   DEFAULT NULL COMMENT '描述',
    `realname`     varchar(50)    DEFAULT NULL COMMENT '真实姓名',
    `sex`          tinyint(1) NOT NULL DEFAULT '0' COMMENT '性别 0:私密 1:男 2:女',
    `address`      varchar(255)   DEFAULT NULL COMMENT '地址',
    `lng`          decimal(16, 6) DEFAULT NULL COMMENT '经度',
    `lat`          decimal(16, 6) DEFAULT NULL COMMENT '纬度',
    `state`        tinyint(1) DEFAULT NULL COMMENT '状态',
    `remark`       varchar(255)   DEFAULT NULL COMMENT '备注',
    `memo`         text           DEFAULT NULL COMMENT '拓展',
    `version`      int(11) NOT NULL DEFAULT '1' COMMENT '乐观锁 默认1',
    `is_enabled`   tinyint(1) DEFAULT '1' COMMENT '是否启用(1:已启用/0:未启用)',
    `is_deleted`   tinyint(1) DEFAULT '0' COMMENT '是否逻辑删除(1:已删除/0:未删除)',
    `creator_id`   varchar(50)    DEFAULT NULL COMMENT '创建者编号',
    `updater_id`   varchar(50)    DEFAULT NULL COMMENT '修改者编号',
    `creator_name` varchar(100)   DEFAULT NULL COMMENT '创建者姓名',
    `updater_name` varchar(100)   DEFAULT NULL COMMENT '修改者姓名',
    `created_time` datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_uuid` (`uuid`) USING BTREE,
    UNIQUE KEY `uniq_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统表-用户表';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`           bigint      NOT NULL AUTO_INCREMENT COMMENT '主键Id',
    `role_name`    varchar(50) NOT NULL COMMENT '角色名称',
    `state`        tinyint(1) DEFAULT NULL COMMENT '状态',
    `remark`       varchar(255) DEFAULT NULL COMMENT '备注',
    `memo`         varchar(255) DEFAULT NULL COMMENT '拓展',
    `version`      int(11) NOT NULL DEFAULT '1' COMMENT '乐观锁 默认1',
    `is_enabled`   tinyint(1) DEFAULT '1' COMMENT '是否启用(1:已启用/0:未启用)',
    `is_deleted`   tinyint(1) DEFAULT '0' COMMENT '是否逻辑删除(1:已删除/0:未删除)',
    `creator_id`   varchar(50)  DEFAULT NULL COMMENT '创建者编号',
    `updater_id`   varchar(50)  DEFAULT NULL COMMENT '修改者编号',
    `creator_name` varchar(100) DEFAULT NULL COMMENT '创建者姓名',
    `updater_name` varchar(100) DEFAULT NULL COMMENT '修改者姓名',
    `created_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统表-角色表';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `id`           bigint      NOT NULL AUTO_INCREMENT COMMENT '主键Id',
    `name`         varchar(50) NOT NULL COMMENT '权限名称',
    `permission`   varchar(200)         DEFAULT NULL COMMENT '权限标识',
    `path`         varchar(200)         DEFAULT NULL COMMENT '权限路由',
    `pid`          int(11) NOT NULL DEFAULT '0' COMMENT '父级id',
    `level`        tinyint(4) NOT NULL DEFAULT '1' COMMENT '权限级别 1 2 3',
    `state`        tinyint(1) DEFAULT NULL COMMENT '状态',
    `remark`       varchar(255)         DEFAULT NULL COMMENT '备注',
    `memo`         varchar(255)         DEFAULT NULL COMMENT '拓展',
    `version`      int(11) NOT NULL DEFAULT '1' COMMENT '乐观锁 默认1',
    `is_deleted`   tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除位 1：已删除 0：未删除',
    `is_enabled`   tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态位 1：启用 0：弃用',
    `creator_id`   varchar(50)          DEFAULT NULL COMMENT '创建者编号',
    `updater_id`   varchar(50)          DEFAULT NULL COMMENT '修改者编号',
    `creator_name` varchar(100)         DEFAULT NULL COMMENT '创建者姓名',
    `updater_name` varchar(100)         DEFAULT NULL COMMENT '修改者姓名',
    `created_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='权限表';

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `user_id`      bigint   NOT NULL COMMENT '用户ID',
    `role_id`      bigint   NOT NULL COMMENT '角色ID',
    `creator_id`   varchar(50)       DEFAULT NULL COMMENT '创建者编号',
    `updater_id`   varchar(50)       DEFAULT NULL COMMENT '修改者编号',
    `creator_name` varchar(100)      DEFAULT NULL COMMENT '创建者姓名',
    `updater_name` varchar(100)      DEFAULT NULL COMMENT '修改者姓名',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色关系表';

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `role_id`      bigint   NOT NULL COMMENT '角色ID',
    `menu_id`      bigint   NOT NULL COMMENT '权限ID',
    `creator_id`   varchar(50)       DEFAULT NULL COMMENT '创建者编号',
    `updater_id`   varchar(50)       DEFAULT NULL COMMENT '修改者编号',
    `creator_name` varchar(100)      DEFAULT NULL COMMENT '创建者姓名',
    `updater_name` varchar(100)      DEFAULT NULL COMMENT '修改者姓名',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与权限关系表';

-- wms 系統表
create database `wms` default character set utf8mb4 collate utf8mb4_general_ci;
USE wms;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
  `role_id`     int(11) NOT NULL AUTO_INCREMENT,
  `role_name`   varchar(64)  DEFAULT NULL,
  `role_code`   varchar(64)  DEFAULT NULL,
  `role_desc`   varchar(255) DEFAULT NULL,
  `ds_type`     char(1)      DEFAULT '2',
  `ds_scope`    varchar(255) DEFAULT NULL,
  `create_time` timestamp(3) DEFAULT NULL,
  `update_time` timestamp(3) DEFAULT NULL,
  `del_flag`    char(1)      DEFAULT 'N',
  `tenant_id`   varchar(10)  DEFAULT NULL,
  PRIMARY KEY (`role_id`) USING BTREE,
  KEY           `role_idx1_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='系統角色表';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
  `menu_id`     int(11) NOT NULL AUTO_INCREMENT COMMENT '選單ID',
  `name`        varchar(32)  DEFAULT NULL,
  `permission`  varchar(32)  DEFAULT NULL,
  `path`        varchar(128) DEFAULT NULL,
  `parent_id`   int(11) DEFAULT NULL COMMENT '父選單ID',
  `icon`        varchar(32)  DEFAULT NULL,
  `sort`        int(11) DEFAULT '1' COMMENT '排序值',
  `keep_alive`  char(1)      DEFAULT 'N',
  `type`        char(1)      DEFAULT '0',
  `create_time` timestamp(3) DEFAULT NULL COMMENT '創建時間',
  `update_time` timestamp(3) DEFAULT NULL COMMENT '更新時間',
  `del_flag`    char(1)      DEFAULT 'N',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10013 DEFAULT CHARSET=utf8mb4 COMMENT='選單許可權表';

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '選單ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色選單表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
  `user_id`     int(11) NOT NULL AUTO_INCREMENT COMMENT '主鍵ID',
  `username`    varchar(64)  DEFAULT NULL,
  `first_name`  varchar(64)  DEFAULT NULL,
  `last_name`   varchar(64)  DEFAULT NULL,
  `password`    varchar(255) DEFAULT NULL,
  `salt`        varchar(255) DEFAULT NULL,
  `phone`       varchar(20)  DEFAULT NULL,
  `avatar`      varchar(255) DEFAULT NULL,
  `dept_id`     int(11) DEFAULT NULL COMMENT '部門ID',
  `create_time` timestamp(3) DEFAULT NULL COMMENT '創建時間',
  `update_time` timestamp(3) DEFAULT NULL COMMENT '修改時間',
  `lock_flag`   char(1)      DEFAULT 'N',
  `del_flag`    char(1)      DEFAULT 'N',
  `tenant_id`   varchar(10)  DEFAULT NULL COMMENT '所屬租戶',
  PRIMARY KEY (`user_id`) USING BTREE,
  KEY           `user_idx1_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用戶表';


-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
  `user_id` int(11) NOT NULL COMMENT '用戶ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用戶角色表';

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`
(
  `dept_id`     int(20) NOT NULL AUTO_INCREMENT,
  `name`        varchar(50) DEFAULT NULL,
  `sort`        int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改時間',
  `del_flag`    char(1)     DEFAULT '0',
  `parent_id`   int(11) DEFAULT NULL,
  `tenant_id`   int(11) DEFAULT NULL,
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='部門管理';

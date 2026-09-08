-- ============================================================
-- 商品管理模块 - 数据库建表脚本
-- 数据库：MySQL 8.0
-- 字符集：utf8mb4
-- 创建时间：2026-09-08
-- ============================================================

-- -----------------------------------------------------------
-- 1. 省市区字典表
-- -----------------------------------------------------------
CREATE TABLE `sys_region` (
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `region_code` VARCHAR(20) NOT NULL COMMENT '区域编码',
    `region_name` VARCHAR(50) NOT NULL COMMENT '区域名称',
    `parent_code` VARCHAR(20) DEFAULT '0' COMMENT '父级编码（0表示省级）',
    `level`       TINYINT     NOT NULL COMMENT '层级：1-省，2-市，3-区县',
    `sort_order`  INT         DEFAULT 0 COMMENT '排序',
    PRIMARY KEY (`id`),
    KEY `idx_parent_code` (`parent_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='省市区字典表';

-- -----------------------------------------------------------
-- 2. 代理商表
-- -----------------------------------------------------------
CREATE TABLE `agent` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `agent_name`  VARCHAR(100) NOT NULL COMMENT '代理商名称',
    `contact`     VARCHAR(50)  DEFAULT NULL COMMENT '联系人',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_agent_name` (`agent_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代理商表';

-- -----------------------------------------------------------
-- 3. 系统用户表（sys_user已存在）
-- -----------------------------------------------------------

-- -----------------------------------------------------------
-- 4. 商品主表
-- -----------------------------------------------------------
CREATE TABLE `product` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `province_code` VARCHAR(20)  NOT NULL COMMENT '所属省编码',
    `province_name` VARCHAR(50)  NOT NULL COMMENT '所属省名称',
    `city_code`     VARCHAR(20)  NOT NULL COMMENT '所属市编码',
    `city_name`     VARCHAR(50)  NOT NULL COMMENT '所属市名称',
    `agent_id`      BIGINT       NOT NULL COMMENT '代理商ID',
    `agent_name`    VARCHAR(100) NOT NULL COMMENT '代理商名称',
    `product_name`  VARCHAR(50)  NOT NULL COMMENT '商品名称（最多20字）',
    `amount`        DECIMAL(15,2) NOT NULL COMMENT '金额（保留2位小数）',
    `product_type`  VARCHAR(50)  DEFAULT NULL COMMENT '商品类型（类型1/类型2/类型3，单选）',
    `product_color` VARCHAR(200) DEFAULT NULL COMMENT '商品颜色（多选，逗号分隔）',
    `publish_time`  DATETIME     NOT NULL COMMENT '发布时间',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1-上架，0-下架',
    `description`   TEXT         NOT NULL COMMENT '商品描述（富文本）',
    `remark`        VARCHAR(255) DEFAULT NULL COMMENT '备注（最多255字）',
    `create_by`     BIGINT       NOT NULL COMMENT '创建人ID（操作人）',
    `create_by_name`VARCHAR(50)  NOT NULL COMMENT '创建人姓名',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`    TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_province_city` (`province_code`, `city_code`),
    KEY `idx_agent_id` (`agent_id`),
    KEY `idx_publish_time` (`publish_time`),
    KEY `idx_status` (`status`),
    KEY `idx_is_deleted` (`is_deleted`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品主表';

-- -----------------------------------------------------------
-- 5. 商品图片表
-- -----------------------------------------------------------
CREATE TABLE `product_image` (
    `id`         BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_id` BIGINT      NOT NULL COMMENT '商品ID',
    `image_url`  VARCHAR(500) NOT NULL COMMENT '图片URL',
    `sort_order` TINYINT     NOT NULL DEFAULT 1 COMMENT '排序（1/2/3 对应预览图l/m/n）',
    `create_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted` TINYINT     NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品图片表';

-- -----------------------------------------------------------
-- 6. 商品联系方式表
-- -----------------------------------------------------------
CREATE TABLE `product_contact` (
    `id`         BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_id` BIGINT      NOT NULL COMMENT '商品ID',
    `name`       VARCHAR(20) NOT NULL COMMENT '姓名（最多5个汉字）',
    `phone`      VARCHAR(20) NOT NULL COMMENT '手机号',
    `email`      VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `create_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted` TINYINT     NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品联系方式表';

-- -----------------------------------------------------------
-- 7. 商品所属人关联表
-- -----------------------------------------------------------
CREATE TABLE `product_user_rel` (
    `id`         BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_id` BIGINT    NOT NULL COMMENT '商品ID',
    `user_id`    BIGINT    NOT NULL COMMENT '用户ID（所属人）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_user` (`product_id`, `user_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品所属人关联表';

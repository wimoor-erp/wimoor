

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 db_admin 的数据库结构
CREATE DATABASE IF NOT EXISTS `db_admin` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_admin`;

-- 导出  表 db_admin.t_authority 结构
CREATE TABLE IF NOT EXISTS `t_authority` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `menuid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '定义Action',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '权限名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='Action权限控制表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_erp_serial_num 结构
CREATE TABLE IF NOT EXISTS `t_erp_serial_num` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `ftype` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `seqno` int DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `prefix_date` date DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`prefix_date`,`ftype`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_manager_limit 结构
CREATE TABLE IF NOT EXISTS `t_manager_limit` (
  `id` bigint unsigned NOT NULL COMMENT 'ID(h)',
  `shopId` bigint unsigned DEFAULT NULL,
  `maxShopCount` int DEFAULT '10' COMMENT '绑定店铺数量',
  `maxMarketCount` int DEFAULT '10' COMMENT '绑定店铺站点数量',
  `maxProductCount` int DEFAULT '50000' COMMENT '商品数量上限',
  `maxOrderCount` int DEFAULT '3600' COMMENT '处理订单上限',
  `maxMember` int DEFAULT '3' COMMENT '子用户数量上限',
  `maxProfitPlanCount` int DEFAULT '1' COMMENT '利润计算方案数量',
  `maxdayOpenAdvCount` int DEFAULT '10' COMMENT '每天开启广告组数量',
  `existShopCount` int DEFAULT '0',
  `existMarketCount` int DEFAULT '0',
  `existProductCount` int DEFAULT '0',
  `existOrderCount` int DEFAULT '0',
  `existMember` int DEFAULT '0',
  `existProfitPlanCount` int DEFAULT '0',
  `existdayOpenAdvCount` int DEFAULT '0',
  `tariffpackage` int DEFAULT '0' COMMENT '0-基础版，1-标准版，2-专业版，3-独享版,4-自定义',
  `losingEffect` date DEFAULT NULL COMMENT '失效时间',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `opratetime` datetime DEFAULT NULL COMMENT '修改时间',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `oprate` bigint unsigned DEFAULT NULL COMMENT '修改人',
  `logicVersion` bigint DEFAULT '0',
  `saleskey` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `neverNoticeShop` bit(1) DEFAULT b'0',
  `afterNnoticeTariff` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_manager_limit_ibfk_1` (`shopId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_manager_limit_append 结构
CREATE TABLE IF NOT EXISTS `t_manager_limit_append` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `tariffpackage` int unsigned NOT NULL,
  `tariffpackage_append_id` int unsigned NOT NULL,
  `ftype` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `num` int DEFAULT '0',
  `effecttime` date DEFAULT NULL,
  `losingeffect` date DEFAULT NULL,
  `isclose` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_menu 结构
CREATE TABLE IF NOT EXISTS `t_menu` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键(h)',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述信息',
  `mindex` int DEFAULT NULL COMMENT '次序',
  `mlevel` int DEFAULT NULL COMMENT '层级',
  `parentid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父菜单',
  `icon` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图标',
  `childnumber` int DEFAULT NULL COMMENT '子菜单个数',
  `action_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '页面链接',
  `groupid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '分组',
  PRIMARY KEY (`id`),
  KEY `FK_t_menu_t_menu` (`parentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='菜单';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_menu_group 结构
CREATE TABLE IF NOT EXISTS `t_menu_group` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `icon` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_picture 结构
CREATE TABLE IF NOT EXISTS `t_picture` (
  `id` bigint unsigned NOT NULL COMMENT '图片ID',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片网络位置',
  `location` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片本地位置',
  `height` decimal(10,2) DEFAULT NULL COMMENT '图片高度',
  `height_units` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '高度单位',
  `width` decimal(10,2) DEFAULT NULL COMMENT '图片宽度',
  `width_units` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '宽度单位',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_location` (`location`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用于存放Image';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_role 结构
CREATE TABLE IF NOT EXISTS `t_role` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色名称',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色类型',
  `issystem` bit(1) NOT NULL DEFAULT b'0',
  `shopid` bigint unsigned DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`shopid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='角色';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_role_adv_group 结构
CREATE TABLE IF NOT EXISTS `t_role_adv_group` (
  `roleid` bigint unsigned NOT NULL,
  `groupid` bigint unsigned NOT NULL,
  `group_name` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`roleid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_role_authority 结构
CREATE TABLE IF NOT EXISTS `t_role_authority` (
  `id` bigint unsigned NOT NULL,
  `role_id` bigint unsigned DEFAULT NULL,
  `authority_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_role_authority_t_authority` (`authority_id`),
  KEY `FK_t_role_authority_t_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='角色权限分配表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_role_group 结构
CREATE TABLE IF NOT EXISTS `t_role_group` (
  `roleid` bigint unsigned NOT NULL,
  `groupid` bigint unsigned NOT NULL,
  `group_name` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`roleid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_role_marketplace 结构
CREATE TABLE IF NOT EXISTS `t_role_marketplace` (
  `id` bigint unsigned NOT NULL,
  `roleid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`roleid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_role_menu 结构
CREATE TABLE IF NOT EXISTS `t_role_menu` (
  `id` bigint unsigned NOT NULL,
  `role_id` bigint unsigned DEFAULT NULL COMMENT '角色ID',
  `menu_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  KEY `FK_t_role_menu_t_role` (`role_id`),
  KEY `FK_t_role_menu_t_menu` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='角色菜单分配表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_shop 结构
CREATE TABLE IF NOT EXISTS `t_shop` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司名称',
  `remark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `invitecode` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邀请码',
  `fromcode` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '受邀请码',
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `boss_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `invitecode` (`invitecode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='店铺';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_app_store_company 结构
CREATE TABLE IF NOT EXISTS `t_sys_app_store_company` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `image` bigint unsigned DEFAULT NULL,
  `telphone` varchar(25) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `QQ` varchar(25) DEFAULT NULL,
  `website` varchar(100) DEFAULT NULL COMMENT '网址',
  `work` varchar(100) DEFAULT NULL COMMENT '业务范围',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `iscompany` bit(1) DEFAULT b'1' COMMENT '是否企业',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_app_store_detail 结构
CREATE TABLE IF NOT EXISTS `t_sys_app_store_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `appgroupid` bigint unsigned DEFAULT NULL,
  `appcompanyid` bigint unsigned DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL COMMENT '服务名称',
  `description` varchar(500) DEFAULT NULL COMMENT '服务描述',
  `image` bigint unsigned DEFAULT NULL,
  `price_original` decimal(15,2) DEFAULT NULL COMMENT '原价',
  `price` decimal(15,2) DEFAULT NULL COMMENT '现在售价',
  `recommend_score` float(5,2) DEFAULT NULL COMMENT '推荐指数',
  `recommend_reason` varchar(255) DEFAULT NULL COMMENT '推荐理由',
  `customer_feedback` varchar(500) DEFAULT NULL COMMENT '客户反馈',
  `customer_pageview` int DEFAULT '0' COMMENT '用户浏览量',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_g_c` (`appgroupid`,`appcompanyid`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_app_store_group 结构
CREATE TABLE IF NOT EXISTS `t_sys_app_store_group` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `image` bigint unsigned DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `createdate` timestamp NULL DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_app_store_service_detail 结构
CREATE TABLE IF NOT EXISTS `t_sys_app_store_service_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `detailid` int DEFAULT NULL,
  `content` longtext COMMENT '服务详情',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `detailid` (`detailid`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_channel_salesperson_key 结构
CREATE TABLE IF NOT EXISTS `t_sys_channel_salesperson_key` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `salesperson` char(50) NOT NULL DEFAULT '0',
  `fkey` char(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_contact 结构
CREATE TABLE IF NOT EXISTS `t_sys_contact` (
  `id` bigint unsigned NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_customer_discount 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_discount` (
  `id` bigint unsigned NOT NULL,
  `number` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '折扣编码',
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `packages` int DEFAULT NULL,
  `amount` decimal(10,2) unsigned DEFAULT NULL COMMENT '折扣金额',
  `account` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '指定应用帐户（可以不填）',
  `pkgtime` int DEFAULT NULL,
  `isused` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否应用',
  `orderid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '应用订单',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '应用公司',
  `createdate` datetime DEFAULT NULL COMMENT '创建时间',
  `lostime` datetime DEFAULT NULL COMMENT '失效时间',
  `operator` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `number` (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户折扣表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_customer_invoice 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_invoice` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `orderid` bigint unsigned DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL,
  `invoice` varchar(50) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `bank` varchar(255) DEFAULT NULL,
  `cardNo` char(50) DEFAULT NULL,
  `ftype` char(20) DEFAULT NULL,
  `sendAddress` char(255) DEFAULT NULL,
  `sendPhone` char(20) DEFAULT NULL,
  `sendName` char(10) DEFAULT NULL,
  `isSend` bit(1) DEFAULT b'0',
  `ivctype` char(50) DEFAULT 'normal',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_customer_order 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_order` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `ftype` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单类型  package:套餐  append:附加包',
  `out_trade_no` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商户订单号',
  `subject` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单名称',
  `trade_no` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付宝交易号',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '付款金额',
  `discountnumber` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `paytime` timestamp NULL DEFAULT NULL,
  `paystatus` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '付款状态',
  `ivcstatus` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开票状态',
  `months` int DEFAULT NULL,
  `tariffpackage` int DEFAULT NULL,
  `pcs` int DEFAULT '1',
  `paytype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `out_trade_no` (`out_trade_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT=' ';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_customer_order_refund 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_order_refund` (
  `id` bigint unsigned DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `orderid` bigint unsigned DEFAULT NULL,
  `out_trade_no` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商户订单号',
  `trade_no` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付宝交易号',
  `refund_amount` decimal(10,2) DEFAULT NULL COMMENT '付款金额',
  `refund_reason` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单名称',
  `out_request_no` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT=' ';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_dept 结构
CREATE TABLE IF NOT EXISTS `t_sys_dept` (
  `id` bigint unsigned NOT NULL,
  `name` char(100) DEFAULT NULL,
  `parent_id` bigint unsigned DEFAULT NULL,
  `tree_path` char(100) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  `deleted` int DEFAULT NULL,
  `gmt_create` timestamp NULL DEFAULT NULL,
  `gmt_modified` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_dict 结构
CREATE TABLE IF NOT EXISTS `t_sys_dict` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '类型名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '类型编码',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0-正常 ,1-停用）',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1712653504552034307 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='字典类型表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_dict_item 结构
CREATE TABLE IF NOT EXISTS `t_sys_dict_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '字典项名称',
  `value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '字典项值',
  `dict_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '字典编码',
  `sort` int DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0 停用 1正常）',
  `defaulted` tinyint(1) DEFAULT '0' COMMENT '是否默认（0否 1是）',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '备注',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `value_dict_code` (`dict_code`,`value`)
) ENGINE=InnoDB AUTO_INCREMENT=1712653923256819718 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='字典数据表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_feishu_auth 结构
CREATE TABLE IF NOT EXISTS `t_sys_feishu_auth` (
  `app_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `app_secret` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `encrypt_key` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `verification_token` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned NOT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`app_id`,`shopid`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_feishu_leave_calendar 结构
CREATE TABLE IF NOT EXISTS `t_sys_feishu_leave_calendar` (
  `timeoff_event_id` char(60) NOT NULL,
  `uuid` char(36) NOT NULL,
  `userid` char(36) DEFAULT NULL,
  `appid` char(36) NOT NULL,
  `event_content_type` char(20) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `isdelete` bit(1) DEFAULT NULL,
  `logs` varchar(2000) DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`timeoff_event_id`),
  KEY `appid` (`appid`),
  KEY `uuid` (`uuid`),
  KEY `userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_help_page 结构
CREATE TABLE IF NOT EXISTS `t_sys_help_page` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键(h)',
  `menuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`menuid`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='菜单';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_holiday 结构
CREATE TABLE IF NOT EXISTS `t_sys_holiday` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `country` char(3) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `marketplaceid` char(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `month` int DEFAULT NULL,
  `value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `marketplaceid` (`marketplaceid`,`month`),
  KEY `country` (`country`,`month`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_importrecord 结构
CREATE TABLE IF NOT EXISTS `t_sys_importrecord` (
  `id` bigint unsigned NOT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `issuccess` bit(1) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `importtype` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `log` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`shopid`,`importtype`),
  KEY `opttime` (`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_local_holiday 结构
CREATE TABLE IF NOT EXISTS `t_sys_local_holiday` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `shopid` bigint unsigned NOT NULL DEFAULT '0',
  `year` int DEFAULT NULL COMMENT '日期',
  `month` int DEFAULT NULL,
  `day` int DEFAULT NULL,
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '节日名称',
  `show_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `week_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `type` tinyint(1) DEFAULT NULL COMMENT '节假日类型(0 工作日、1 周末、2 节日、3 调休)',
  `type_str` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `key_date` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `cur_date` date DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `lunars` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `lunars_date` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `is_gray` bit(1) DEFAULT NULL,
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建者',
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '更新者',
  `opttime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_year_month_day` (`shopid`,`year`,`month`,`day`),
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1882361149880598583 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='节假日表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_mailsender 结构
CREATE TABLE IF NOT EXISTS `t_sys_mailsender` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mailServerHost` varchar(50) DEFAULT NULL,
  `mailServerPort` char(10) DEFAULT NULL,
  `fromAddress` varchar(50) DEFAULT NULL,
  `serverAccount` varchar(50) DEFAULT NULL,
  `userName` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `iwallHost` varchar(50) DEFAULT NULL,
  `validate` bit(1) DEFAULT NULL,
  `starttls` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_mail_template 结构
CREATE TABLE IF NOT EXISTS `t_sys_mail_template` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mail_subject` varchar(50) DEFAULT NULL,
  `ftype` tinyint unsigned DEFAULT NULL COMMENT '0、系统废弃模板，1、系统通知邮件 ，2、公司通知邮件，3、买家订单回复，4、买家邀请',
  `shopid` bigint unsigned DEFAULT NULL,
  `content` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_menu 结构
CREATE TABLE IF NOT EXISTS `t_sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '菜单名称',
  `parent_id` bigint DEFAULT NULL COMMENT '父菜单ID',
  `apppath` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '路由路径',
  `component` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '菜单图标',
  `appicon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '',
  `sort` int DEFAULT '0' COMMENT '排序',
  `visible` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用 1-开启',
  `redirect` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '跳转路径',
  `runui` bit(1) DEFAULT NULL,
  `runapp` bit(1) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  `oldid` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1012162 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='菜单管理';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_menu_favorite 结构
CREATE TABLE IF NOT EXISTS `t_sys_menu_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `shopid` bigint NOT NULL DEFAULT '0' COMMENT '公司ID',
  `userid` bigint NOT NULL DEFAULT '0' COMMENT '用户ID',
  `menuid` bigint DEFAULT NULL COMMENT '菜单ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='菜单收藏';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_message_template 结构
CREATE TABLE IF NOT EXISTS `t_sys_message_template` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '消息类型',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '消息内容',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_notify 结构
CREATE TABLE IF NOT EXISTS `t_sys_notify` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ftype` int NOT NULL COMMENT '消息的类型，1: 公告 Announce，2: 提醒 Remind，3：信息 Message',
  `target` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '目标类型',
  `action` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '提醒信息的动作类型',
  `sender` bigint unsigned DEFAULT NULL COMMENT '发送者的ID',
  `receiver` bigint unsigned DEFAULT NULL COMMENT '接受者ID',
  `createdAt` datetime DEFAULT NULL COMMENT '创建时间',
  `shopid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `createdAt` (`createdAt`),
  KEY `idx_notify_shopid` (`ftype`),
  KEY `shopid` (`shopid`),
  KEY `target` (`target`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_operationlog 结构
CREATE TABLE IF NOT EXISTS `t_sys_operationlog` (
  `id` bigint unsigned NOT NULL,
  `time` datetime DEFAULT NULL,
  `ip` char(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `userid` bigint unsigned DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `logType` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `method` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `exceptionDetail` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `param` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_method_time_userid` (`method`,`time`,`userid`),
  KEY `time` (`userid`,`method`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_permission 结构
CREATE TABLE IF NOT EXISTS `t_sys_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '权限名称',
  `menu_id` int DEFAULT NULL COMMENT '菜单模块ID\r\n',
  `url_perm` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'URL权限标识',
  `btn_perm` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '按钮权限标识',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`,`name`),
  KEY `id_2` (`id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1697438074115891206 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='权限表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_potential_customer 结构
CREATE TABLE IF NOT EXISTS `t_sys_potential_customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company` varchar(2000) DEFAULT NULL,
  `user_name` varchar(1000) DEFAULT NULL,
  `address` varchar(2000) DEFAULT NULL,
  `telphone` char(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `sendtime` timestamp NULL DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`telphone`,`email`)
) ENGINE=InnoDB AUTO_INCREMENT=38005 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_quartz_task 结构
CREATE TABLE IF NOT EXISTS `t_sys_quartz_task` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `fgroup` varchar(50) DEFAULT NULL,
  `priority` int DEFAULT '5',
  `description` varchar(50) DEFAULT NULL,
  `cron` varchar(100) DEFAULT NULL,
  `server` varchar(20) DEFAULT NULL,
  `bean` varchar(50) DEFAULT NULL,
  `method` varchar(50) DEFAULT NULL,
  `parameter` varchar(200) DEFAULT NULL,
  `path` varchar(200) DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=231 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_query_field 结构
CREATE TABLE IF NOT EXISTS `t_sys_query_field` (
  `fquery` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `ffield` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `title` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `titleTooltip` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `width` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `findex` int DEFAULT NULL,
  `formatter` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `footerFormatter` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sortable` bit(1) DEFAULT NULL,
  `valign` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `align` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`fquery`,`ffield`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_query_user_version 结构
CREATE TABLE IF NOT EXISTS `t_sys_query_user_version` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint unsigned DEFAULT NULL,
  `fquery` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isused` bit(1) DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`,`fquery`)
) ENGINE=InnoDB AUTO_INCREMENT=123457041 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_query_version_feild 结构
CREATE TABLE IF NOT EXISTS `t_sys_query_version_feild` (
  `fversionid` bigint unsigned NOT NULL,
  `ffield` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `findex` int NOT NULL,
  `isshow` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`fversionid`,`ffield`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_role_menu 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_menu` (
  `role_id` bigint unsigned NOT NULL COMMENT '角色ID',
  `menu_id` bigint unsigned NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`),
  KEY `menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='角色和菜单关联表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_role_permission 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_permission` (
  `role_id` bigint unsigned NOT NULL COMMENT '角色id',
  `permission_id` bigint unsigned NOT NULL COMMENT '资源id',
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='角色权限表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_role_tag 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_tag` (
  `id` bigint unsigned NOT NULL,
  `tag_id` char(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `roleid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`tag_id`,`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='管理员新建，用于给不同下属分配不同的产品查询权限';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_role_taggroup 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_taggroup` (
  `id` bigint unsigned NOT NULL,
  `roleid` bigint unsigned DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`roleid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_subscription 结构
CREATE TABLE IF NOT EXISTS `t_sys_subscription` (
  `target` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `userid` bigint unsigned NOT NULL,
  `action` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订阅动作',
  `disable` bit(1) NOT NULL DEFAULT b'0',
  `createdAt` datetime DEFAULT NULL,
  PRIMARY KEY (`target`,`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_tags 结构
CREATE TABLE IF NOT EXISTS `t_sys_tags` (
  `id` bigint unsigned NOT NULL COMMENT 'ID',
  `name` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '标签名称',
  `value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `color` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `taggroupid` bigint unsigned DEFAULT NULL COMMENT '分组ID',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `creator` bigint unsigned DEFAULT NULL COMMENT '创建人',
  `operator` bigint unsigned DEFAULT NULL COMMENT '修改人',
  `remark` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `gmt_create` datetime DEFAULT NULL COMMENT '修改日期',
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_taggroupid` (`name`,`taggroupid`),
  KEY `groupid` (`taggroupid`),
  KEY `FK_t_tag_t_sho` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_tags_groups 结构
CREATE TABLE IF NOT EXISTS `t_sys_tags_groups` (
  `id` bigint unsigned NOT NULL,
  `sort` int DEFAULT NULL,
  `name` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shop_id` bigint unsigned DEFAULT NULL,
  `description` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `opterator` bigint unsigned DEFAULT NULL,
  `remark` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_shop_id` (`name`,`shop_id`),
  KEY `FK_t_tag_group_t_shop` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_target 结构
CREATE TABLE IF NOT EXISTS `t_sys_target` (
  `id` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `description` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_tariff_packages 结构
CREATE TABLE IF NOT EXISTS `t_sys_tariff_packages` (
  `id` int NOT NULL COMMENT '套餐id 0-基础版，1-标准版，2-专业版，3-独享版,4-自定义',
  `name` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '套餐名字',
  `roleId` bigint unsigned NOT NULL COMMENT '角色id',
  `isdefault` bit(1) NOT NULL DEFAULT b'0',
  `maxShopCount` int DEFAULT '1' COMMENT '绑定店铺数量',
  `maxProductCount` int DEFAULT '50000' COMMENT '商品数量上限',
  `maxOrderCount` int DEFAULT '3600' COMMENT '处理订单上限',
  `maxMember` int DEFAULT '10' COMMENT '子用户数量上限',
  `maxProfitPlanCount` int DEFAULT '1' COMMENT '利润计算方案数量',
  `maxMarketCount` int DEFAULT '1' COMMENT '每个店铺绑定站点数量',
  `orderMemoryCount` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '3' COMMENT '订单存储时间 单位:月  默认基础版是3个月',
  `dayOpenAdvCount` int NOT NULL DEFAULT '10' COMMENT '每天开启广告组数量',
  `controlProductCount` int NOT NULL DEFAULT '10' COMMENT '跟卖监控产品数量',
  `anysisProductCount` int NOT NULL DEFAULT '10' COMMENT '商品分析数量',
  `yearprice` decimal(18,2) DEFAULT NULL,
  `monthprice` decimal(18,2) DEFAULT NULL,
  `lastUpdateTime` date DEFAULT NULL COMMENT '最后更新时间',
  `lastUpdateUser` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '最后更新的人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='套餐表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_tariff_packages_append 结构
CREATE TABLE IF NOT EXISTS `t_sys_tariff_packages_append` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ftype` char(20) DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `units` int DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_tariff_packages_append_discount 结构
CREATE TABLE IF NOT EXISTS `t_sys_tariff_packages_append_discount` (
  `appendid` int DEFAULT NULL,
  `packages` int DEFAULT NULL,
  `month` int DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  UNIQUE KEY `appendid_packages_month` (`appendid`,`packages`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_timetask 结构
CREATE TABLE IF NOT EXISTS `t_sys_timetask` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务名',
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务组',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `cron` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '表达式',
  `job_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '发布状态',
  `plan_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '计划状态',
  `is_concurrent` int DEFAULT NULL COMMENT '运行状态',
  `job_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '参数',
  `menthod_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '方法',
  `bean_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实例bean',
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务描述',
  `creator` bigint unsigned DEFAULT NULL COMMENT '创建人',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_timetask_log 结构
CREATE TABLE IF NOT EXISTS `t_sys_timetask_log` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `createdate` datetime DEFAULT NULL,
  `job_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_usernotify 结构
CREATE TABLE IF NOT EXISTS `t_sys_usernotify` (
  `userid` bigint unsigned NOT NULL COMMENT '用户消息所属者',
  `notify` int NOT NULL,
  `isRead` bit(1) NOT NULL DEFAULT b'0' COMMENT '0，代表未读；1，代表已读',
  `createdAt` datetime DEFAULT NULL,
  PRIMARY KEY (`userid`,`notify`),
  KEY `createdAt` (`isRead`),
  KEY `Index 2` (`notify`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_user_bind 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_bind` (
  `userid` bigint unsigned NOT NULL,
  `bindid` bigint unsigned NOT NULL,
  PRIMARY KEY (`userid`),
  KEY `bindid` (`bindid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_user_datalimit 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_datalimit` (
  `userid` bigint unsigned NOT NULL,
  `datatype` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'owner只能查看自己负责的产品（在产品管理页面配置）;operations只能查看自己运营的产品（在商品分析页面配置）',
  `islimit` bit(1) DEFAULT b'0' COMMENT 'true表示需要限制，false表示不需要限制',
  PRIMARY KEY (`userid`,`datatype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户数据权限，放在用户信息中，登录后将在所有模块生效';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_user_group 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_group` (
  `userid` bigint unsigned DEFAULT NULL COMMENT '用户ID',
  `groupid` bigint unsigned DEFAULT NULL COMMENT '店铺ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户客户对店铺的权限绑定';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_user_ip_city 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_ip_city` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `userip` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `city` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userip` (`userip`)
) ENGINE=InnoDB AUTO_INCREMENT=1385 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_video 结构
CREATE TABLE IF NOT EXISTS `t_sys_video` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `menuid` char(36) DEFAULT NULL,
  `video_url` varchar(100) DEFAULT NULL,
  `video_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_weather 结构
CREATE TABLE IF NOT EXISTS `t_sys_weather` (
  `id` bigint unsigned NOT NULL,
  `city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '城市',
  `weatype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '值:多云 晴 小雨 大雨',
  `updatedate` datetime DEFAULT NULL COMMENT '当前日期的天气',
  `nowdegree` int DEFAULT NULL COMMENT '当前温度 ''C',
  `lowdegree` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '最低温度',
  `highdegree` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '最高温度',
  `weaforce` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '风向',
  PRIMARY KEY (`id`),
  UNIQUE KEY `city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='城市天气表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_tasklock 结构
CREATE TABLE IF NOT EXISTS `t_tasklock` (
  `task` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `mylock` bit(1) DEFAULT NULL,
  PRIMARY KEY (`task`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` bigint unsigned NOT NULL COMMENT '整型自增主键',
  `account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `password` char(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL COMMENT '用户密码，采用MD5加密',
  `salt` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `leader_id` bigint unsigned DEFAULT NULL COMMENT '上层',
  `createDate` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `losingEffect` date DEFAULT NULL COMMENT '失效时间',
  `logicDelete` bit(1) DEFAULT b'0' COMMENT '逻辑删除',
  `disable` bit(1) DEFAULT b'0' COMMENT '停用',
  `isActive` bit(1) DEFAULT b'1' COMMENT '账户是否激活',
  `hasEmail` bit(1) DEFAULT b'0' COMMENT '邮箱是否激活',
  `member` int DEFAULT '5' COMMENT '拥有下属数量上限',
  `passwordkey` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `googleAuthenticatorSecret` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `lastlogintime` datetime DEFAULT NULL,
  `lastloginip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `lastloginsession` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ftype` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `deptid` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`),
  KEY `leader_id` (`leader_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_userinfo 结构
CREATE TABLE IF NOT EXISTS `t_userinfo` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `picture` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tel` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `company` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_userinfo_t_picture` (`picture`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户详细信息表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_user_oldpassword 结构
CREATE TABLE IF NOT EXISTS `t_user_oldpassword` (
  `userid` int DEFAULT NULL,
  `oldpassword` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `oldsale` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_user_role 结构
CREATE TABLE IF NOT EXISTS `t_user_role` (
  `id` bigint unsigned NOT NULL,
  `user_id` bigint unsigned DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint unsigned DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 4` (`user_id`,`role_id`),
  KEY `FK_t_user_role_t_user` (`user_id`),
  KEY `FK_t_user_role_t_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户角色分配表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_user_shop 结构
CREATE TABLE IF NOT EXISTS `t_user_shop` (
  `id` bigint unsigned NOT NULL,
  `user_id` bigint unsigned DEFAULT NULL COMMENT '用户ID',
  `shop_id` bigint unsigned DEFAULT NULL COMMENT '店铺ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shop_id` (`shop_id`),
  KEY `FK_t_user_shop_t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户店铺归属表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_user_wechat_login 结构
CREATE TABLE IF NOT EXISTS `t_user_wechat_login` (
  `openid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `userid` bigint unsigned NOT NULL,
  `unionid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `access_token` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refresh_token` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`openid`),
  UNIQUE KEY `userid` (`userid`),
  UNIQUE KEY `unionid` (`unionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_user_wechat_mp 结构
CREATE TABLE IF NOT EXISTS `t_user_wechat_mp` (
  `openid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `userid` bigint unsigned NOT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `access_token` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refresh_token` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`openid`,`ftype`,`userid`),
  KEY `key` (`userid`,`ftype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.undo_log 结构
CREATE TABLE IF NOT EXISTS `undo_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `branch_id` bigint NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

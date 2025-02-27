

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 db_erp 的数据库结构
CREATE DATABASE IF NOT EXISTS `db_erp` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_erp`;

-- 导出  表 db_erp.t_dimensions 结构
CREATE TABLE IF NOT EXISTS `t_dimensions` (
  `id` bigint unsigned NOT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `length_units` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `width_units` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `height_units` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `weight_units` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_assembly 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly` (
  `id` bigint unsigned NOT NULL COMMENT 'ID',
  `mainmid` bigint unsigned DEFAULT NULL COMMENT '主产品',
  `submid` bigint unsigned DEFAULT NULL COMMENT '子产品',
  `subnumber` int DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `mainmid` (`mainmid`,`submid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_assembly_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `planitem` bigint unsigned DEFAULT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '组装=ass, 拆分=dis',
  `warehouseid` bigint unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `mainmid` bigint unsigned DEFAULT NULL COMMENT '主sku id',
  `amount` int DEFAULT NULL,
  `amount_handle` int unsigned DEFAULT NULL,
  `auditstatus` int DEFAULT NULL COMMENT '0：未提交，1：待组装，2 组装中，3 已完成，4 已终止, 5已作废',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `check_inv` bigint unsigned DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number` (`shopid`,`number`),
  KEY `auditstatus` (`auditstatus`),
  KEY `mainmid` (`mainmid`),
  KEY `shopid_createdate` (`shopid`,`createdate`),
  KEY `createdate` (`shopid`,`opttime`),
  KEY `check_inv` (`check_inv`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_assembly_form_checkinv 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_form_checkinv` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_assembly_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT '0',
  `subnumber` int DEFAULT '0',
  `whamount` int DEFAULT '0' COMMENT '仓库调出量',
  `phamount` int DEFAULT '0' COMMENT '采购量',
  `phedamount` int DEFAULT '0' COMMENT '已经采购数量',
  `purchase_from_entry_id` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`formid`,`materialid`),
  UNIQUE KEY `indexunique` (`id`),
  KEY `purchase_from_entry_id` (`purchase_from_entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_assembly_from_instock 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_from_instock` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `shipmentid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `optformid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`),
  KEY `idx_formid_shipmentid` (`formid`,`shipmentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_changewh_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_changewh_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `auditstatus` int DEFAULT NULL COMMENT '0：未提交，1：提交未审核，2：已审核',
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`),
  KEY `warehouseid` (`warehouseid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_changewh_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_changewh_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `material_from` bigint unsigned DEFAULT NULL,
  `material_to` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_customer 结构
CREATE TABLE IF NOT EXISTS `t_erp_customer` (
  `id` bigint unsigned NOT NULL COMMENT 'ID(h)',
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客户简称',
  `number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客户编码',
  `fullname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客户全称',
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客户分类',
  `contacts` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '联系人',
  `phone_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '联系电话',
  `contact_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '其它联系信息',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址',
  `shoplink` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商品链接',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '所属店铺（公司）(h)',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_shopid` (`shopid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_dispatch_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '调拨单号',
  `shopid` bigint unsigned DEFAULT NULL,
  `from_warehouseid` bigint unsigned DEFAULT NULL,
  `to_warehouseid` bigint unsigned DEFAULT NULL,
  `ftype` tinyint unsigned DEFAULT '0' COMMENT '0:货物调度，1，报废，2，修复，3，送检，4，验收入库,5海外发货，6海外退货',
  `creator` bigint unsigned DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `auditstatus` int DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_dispatch_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_dispatch_form_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_form_record` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `auditstatus` int DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_dispatch_oversea_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_oversea_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '调拨单号',
  `shopid` bigint unsigned DEFAULT NULL,
  `from_warehouseid` bigint unsigned DEFAULT NULL,
  `to_warehouseid` bigint unsigned DEFAULT NULL,
  `country` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `auditstatus` int DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_dispatch_oversea_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_oversea_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `sellersku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fnsku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_dispatch_oversea_trans 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_oversea_trans` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `company` bigint unsigned DEFAULT NULL,
  `channel` bigint unsigned DEFAULT NULL,
  `singleprice` decimal(10,4) DEFAULT NULL,
  `transweight` decimal(10,4) DEFAULT NULL,
  `wunit` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `otherfee` decimal(10,4) DEFAULT NULL,
  `ordernum` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  `outarrtime` datetime DEFAULT NULL,
  `inarrtime` datetime DEFAULT NULL,
  `wtype` tinyint DEFAULT '0',
  `transtype` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shipmentid` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_download_report 结构
CREATE TABLE IF NOT EXISTS `t_erp_download_report` (
  `id` bigint unsigned NOT NULL,
  `ftype` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `userid` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `isrun` bit(1) NOT NULL DEFAULT b'0',
  `log` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `content` longblob,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shopid` (`shopid`),
  KEY `ftype_userid` (`ftype`,`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_fin_account 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_account` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `paymeth` int unsigned DEFAULT '1',
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '默认',
  `isdefault` bit(1) DEFAULT b'0',
  `isdelete` bit(1) DEFAULT b'0',
  `balance` decimal(18,4) DEFAULT NULL COMMENT '账户余额',
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`,`paymeth`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='账户表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_fin_journalaccount 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_journalaccount` (
  `id` bigint unsigned NOT NULL,
  `acct` bigint unsigned NOT NULL,
  `ftype` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '记账类型:out,支出；in,收入',
  `projectid` bigint unsigned DEFAULT NULL,
  `amount` decimal(18,4) DEFAULT NULL,
  `balance` decimal(18,4) DEFAULT NULL,
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creator` bigint unsigned DEFAULT NULL COMMENT '创建人',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `shopid` bigint unsigned NOT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_fin_journaldaily 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_journaldaily` (
  `id` bigint unsigned NOT NULL,
  `acct` bigint unsigned DEFAULT NULL COMMENT '账户id',
  `byday` date DEFAULT NULL,
  `rec` decimal(18,4) DEFAULT NULL COMMENT '收入',
  `pay` decimal(18,4) DEFAULT NULL COMMENT '支出',
  `balance` decimal(18,4) DEFAULT NULL COMMENT '余额',
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `byday` (`byday`,`acct`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='流水_日账单';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_fin_project 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_project` (
  `id` bigint unsigned NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `issys` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是系统项目名称',
  `shopid` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_shopid` (`name`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='流水账_类型';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_fin_type_journalmonthly 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_type_journalmonthly` (
  `id` bigint unsigned NOT NULL,
  `projectid` bigint unsigned DEFAULT NULL COMMENT '项目id',
  `acct` bigint unsigned DEFAULT NULL COMMENT '账户id',
  `year` int DEFAULT NULL,
  `month` int DEFAULT NULL,
  `rec` decimal(18,4) DEFAULT NULL,
  `pay` decimal(18,4) DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`projectid`,`acct`,`year`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='流水_月账单 类型统计';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_formtype 结构
CREATE TABLE IF NOT EXISTS `t_erp_formtype` (
  `id` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory` (
  `id` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned NOT NULL DEFAULT '0',
  `shopid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `quantity` int DEFAULT '0',
  `status` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_w_s_m_s` (`warehouseid`,`materialid`,`shopid`,`status`),
  KEY `FK_t_erp_inventory_t_erp_material` (`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory_bkp20240613 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_bkp20240613` (
  `id` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned NOT NULL DEFAULT '0',
  `shopid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `quantity` int DEFAULT '0',
  `status` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_w_s_m_s` (`warehouseid`,`materialid`,`shopid`,`status`),
  KEY `FK_t_erp_inventory_t_erp_material` (`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_his` (
  `id` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned NOT NULL DEFAULT '0',
  `shopid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `quantity` int DEFAULT '0',
  `status` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `modifyday` date NOT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_erp_inventory_t_erp_material` (`warehouseid`,`materialid`),
  KEY `modifyday` (`modifyday`),
  KEY `mykey` (`shopid`,`materialid`,`warehouseid`),
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory_his_day 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_his_day` (
  `shopid` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `byday` date NOT NULL,
  `inbound` int DEFAULT '0',
  `fulfillable` int DEFAULT '0',
  `outbound` int DEFAULT '0',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`shopid`,`warehouseid`,`byday`,`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory_month_summary 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_month_summary` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned NOT NULL,
  `month` date NOT NULL,
  `startqty` int DEFAULT NULL,
  `endqty` int DEFAULT NULL,
  `shipment` int DEFAULT NULL,
  `dispatch` int DEFAULT NULL,
  `assembly` int DEFAULT NULL,
  `purchase` int DEFAULT NULL,
  `otherout` int DEFAULT NULL,
  `otherin` int DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `period` decimal(15,4) DEFAULT NULL,
  `turndays` decimal(15,4) DEFAULT NULL,
  `diff` int DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `materialid_warehouseid` (`shopid`,`warehouseid`,`materialid`,`month`),
  KEY `month` (`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_record` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `formoptid` bigint unsigned DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `status` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `invqty` int DEFAULT NULL,
  `startinbound` int DEFAULT NULL,
  `inbound` int DEFAULT NULL,
  `endinbound` int DEFAULT NULL,
  `startfulfillable` int DEFAULT NULL,
  `fulfillable` int DEFAULT NULL,
  `endfulfillable` int DEFAULT NULL,
  `startoutbound` int DEFAULT NULL,
  `outbound` int DEFAULT NULL,
  `endoutbound` int DEFAULT NULL,
  `formtype` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operate` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'in,out,readyin,readyout,cancel,stop',
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`opttime`,`id`),
  KEY `索引 3` (`materialid`,`status`),
  KEY `number` (`shopid`,`number`),
  KEY `shopid_opttime` (`shopid`,`opttime`),
  KEY `formtype` (`shopid`,`formtype`,`opttime`),
  KEY `Index 2` (`warehouseid`,`materialid`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory_record_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_record_form` (
  `shopid` bigint unsigned NOT NULL,
  `formid` bigint unsigned NOT NULL,
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehouseid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `startinbound` int DEFAULT '0',
  `inbound` int DEFAULT '0',
  `endinbound` int DEFAULT '0',
  `startfulfillable` int DEFAULT '0',
  `fulfillable` int DEFAULT '0',
  `endfulfillable` int DEFAULT '0',
  `startoutbound` int DEFAULT '0',
  `outbound` int DEFAULT '0',
  `endoutbound` int DEFAULT '0',
  `formtype` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operate` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime NOT NULL,
  KEY `shopid_formtype` (`shopid`,`formtype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory_status 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_status` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `code` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inwh_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_inwh_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditstatus` int DEFAULT NULL COMMENT '0：未提交，1：提交未审核，2：已审核',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inwh_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_inwh_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_material` (
  `id` bigint unsigned NOT NULL COMMENT 'ID(h)',
  `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'SKU',
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID(h)',
  `upc` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '条码',
  `brand` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '品牌',
  `image` bigint unsigned DEFAULT NULL COMMENT '图片',
  `itemDimensions` bigint unsigned DEFAULT NULL COMMENT '产品尺寸',
  `pkgDimensions` bigint unsigned DEFAULT NULL COMMENT '带包装尺寸',
  `boxDimensions` bigint unsigned DEFAULT NULL,
  `boxnum` int unsigned DEFAULT NULL,
  `specification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '规格',
  `supplier` bigint unsigned DEFAULT NULL COMMENT '供应商',
  `badrate` float DEFAULT NULL COMMENT '不良率',
  `vatrate` float DEFAULT NULL COMMENT '退税率',
  `productCode` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '供应商产品代码',
  `delivery_cycle` int DEFAULT NULL COMMENT '供货周期',
  `other_cost` decimal(10,2) DEFAULT NULL,
  `MOQ` int unsigned DEFAULT '0' COMMENT '起订量：minimum order quantity',
  `purchaseUrl` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '采购链接',
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `categoryid` bigint unsigned DEFAULT NULL COMMENT '类型id',
  `issfg` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0:单独成品；1：组装成品；2：半成品',
  `color` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0',
  `owner` bigint unsigned DEFAULT '0',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `price` decimal(10,2) DEFAULT NULL,
  `price_wavg` decimal(10,2) DEFAULT NULL,
  `price_ship_wavg` decimal(10,2) DEFAULT NULL,
  `addfee` decimal(10,2) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `effectivedate` datetime DEFAULT NULL,
  `isSmlAndLight` bit(1) DEFAULT b'0' COMMENT '是否轻小产品',
  `assembly_time` int DEFAULT NULL COMMENT '组装时间',
  `isDelete` bit(1) DEFAULT NULL,
  `mtype` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `skurepeat` (`sku`,`shopid`),
  KEY `supplier` (`supplier`),
  KEY `FK_t_erp_material_t_erp_material_sku` (`sku`,`shopid`,`isDelete`),
  KEY `opttime` (`shopid`,`opttime`),
  KEY `categoryid` (`categoryid`),
  KEY `shop_delete_sku_color` (`shopid`,`isDelete`,`mtype`,`issfg`) USING BTREE,
  KEY `Index 4` (`shopid`,`owner`,`sku`,`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_brand 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_brand` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`name`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_category 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_category` (
  `id` bigint unsigned NOT NULL,
  `name` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `color` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`name`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_consumable 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_consumable` (
  `id` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `submaterialid` bigint unsigned NOT NULL,
  `amount` decimal(10,4) unsigned DEFAULT '0.0000',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `materialid_submaterialid` (`materialid`,`submaterialid`),
  KEY `submaterialid` (`submaterialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='耗材表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_consumable_inventory 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_consumable_inventory` (
  `id` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned NOT NULL DEFAULT '0',
  `shopid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `quantity` decimal(10,6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_consumable_safety_stock 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_consumable_safety_stock` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `shopid` bigint unsigned NOT NULL,
  `amount` int unsigned NOT NULL DEFAULT '0',
  `operator` bigint unsigned NOT NULL DEFAULT '0',
  `opttime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB AUTO_INCREMENT=1819635035878256642 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_customs 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_customs` (
  `materialid` bigint unsigned NOT NULL,
  `country` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `price` decimal(20,6) DEFAULT NULL,
  `code` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '海关编码',
  `rate` decimal(20,6) DEFAULT NULL,
  `material` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品材质',
  `materialcn` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `application` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用途',
  `url` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ename` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品英文名',
  `cname` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品中文名',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`materialid`,`country`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='海关表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_customs_file 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_customs_file` (
  `id` bigint unsigned NOT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `filename` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `filepath` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_customs_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_customs_item` (
  `materialid` bigint unsigned NOT NULL,
  `country` char(10) NOT NULL COMMENT 'DE UK FR',
  `code` char(10) DEFAULT NULL,
  `fee` decimal(10,2) DEFAULT NULL,
  `taxrate` decimal(10,2) DEFAULT NULL,
  `currency` char(50) DEFAULT 'CNY',
  PRIMARY KEY (`materialid`,`country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_group 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_group` (
  `materialid` bigint unsigned NOT NULL,
  `groupid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`materialid`,`groupid`),
  KEY `FK_t_erp_material_category_t_erp_category` (`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_his` (
  `id` bigint unsigned NOT NULL COMMENT 'ID(h)',
  `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'SKU',
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID(h)',
  `upc` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '条码',
  `brand` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '品牌',
  `image` bigint unsigned DEFAULT NULL COMMENT '图片',
  `itemDimensions` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品尺寸',
  `pkgDimensions` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '带包装尺寸',
  `boxDimensions` bigint unsigned DEFAULT NULL,
  `boxnum` int unsigned DEFAULT NULL,
  `specification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '规格',
  `supplier` bigint unsigned DEFAULT NULL COMMENT '供应商',
  `productCode` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '供应商产品代码',
  `delivery_cycle` int DEFAULT NULL,
  `other_cost` decimal(10,2) DEFAULT NULL,
  `MOQ` int DEFAULT '0' COMMENT '起订量：minimum order quantity',
  `purchaseUrl` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '采购链接',
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `categoryid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类型id',
  `issfg` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0:单独成品；1：组装成品；2：半成品',
  `color` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0',
  `owner` bigint unsigned DEFAULT '0',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `price` decimal(10,4) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `parentid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用于导入数据是引用的系统内的那个SKU产品',
  `effectivedate` datetime DEFAULT NULL,
  `isSmlAndLight` bit(1) DEFAULT b'0' COMMENT '是否轻小产品',
  `assembly_time` int DEFAULT NULL,
  PRIMARY KEY (`id`,`opttime`),
  UNIQUE KEY `Index 3` (`shopid`,`sku`,`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='t_erp_material_his历史表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_mark 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_mark` (
  `materialid` bigint unsigned NOT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'notice：产品出现问题时发布的公告',
  `mark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`materialid`,`ftype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_mark_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_mark_his` (
  `materialid` bigint unsigned NOT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'notice：产品出现问题时发布的公告',
  `mark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`materialid`,`ftype`,`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_supplier 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_supplier` (
  `id` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `supplierid` bigint unsigned NOT NULL,
  `purchaseUrl` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `productCode` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `specId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `offerid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `otherCost` decimal(10,2) DEFAULT NULL,
  `deliverycycle` int DEFAULT NULL,
  `isdefault` bit(1) NOT NULL DEFAULT b'0',
  `badrate` float DEFAULT '0',
  `MOQ` int NOT NULL DEFAULT '0',
  `creater` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `materialid_supplierid` (`materialid`,`supplierid`),
  KEY `specId` (`specId`,`offerid`),
  KEY `productCode` (`productCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_supplier_stepwise 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_supplier_stepwise` (
  `id` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `supplierid` bigint unsigned NOT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '',
  `price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00',
  `amount` int unsigned NOT NULL DEFAULT '0',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `materialid` (`materialid`,`supplierid`,`amount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_tags 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_tags` (
  `mid` bigint unsigned NOT NULL,
  `tagid` bigint unsigned NOT NULL,
  `operator` bigint unsigned NOT NULL,
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`mid`,`tagid`),
  KEY `tagid` (`tagid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品-标签';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_m_group 结构
CREATE TABLE IF NOT EXISTS `t_erp_m_group` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `ftype` int NOT NULL,
  `color` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `issys` bit(1) DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_order 结构
CREATE TABLE IF NOT EXISTS `t_erp_order` (
  `id` bigint unsigned NOT NULL,
  `platform_id` bigint unsigned DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `isout` bit(1) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `price` decimal(20,6) DEFAULT NULL,
  `ship_fee` decimal(20,6) DEFAULT NULL,
  `referral_fee` decimal(20,6) DEFAULT NULL,
  `referral_rate` decimal(20,6) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid` (`shopid`,`purchase_date`) USING BTREE,
  KEY `order_id` (`order_id`),
  KEY `sku` (`sku`),
  KEY `platform_id` (`platform_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_order_invoice 结构
CREATE TABLE IF NOT EXISTS `t_erp_order_invoice` (
  `id` bigint unsigned NOT NULL,
  `country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `logoUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片地址id',
  `image` bigint unsigned DEFAULT NULL,
  `company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `company_simple` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `billto` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bank` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `abn` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `rate` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `country_shopid` (`country`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_order_platform 结构
CREATE TABLE IF NOT EXISTS `t_erp_order_platform` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `disabled` bit(1) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_name` (`shopid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_order_ship_plan 结构
CREATE TABLE IF NOT EXISTS `t_erp_order_ship_plan` (
  `shopid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  PRIMARY KEY (`shopid`,`materialid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_order_ship_plan_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_order_ship_plan_form` (
  `id` bigint unsigned DEFAULT NULL,
  `number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `auditstatus` int DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_order_ship_plan_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_order_ship_plan_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `quantity` int unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `formid_materialid` (`formid`,`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_outwh_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_outwh_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ftype` tinyint DEFAULT '0' COMMENT '0:正品出库；1:废品出库，2:验收出库 ，3:海外出库',
  `auditor` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `purchaser` bigint unsigned DEFAULT NULL COMMENT '发货客户',
  `customer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `toaddress` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '发货地址',
  `express` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '物流快递',
  `expressno` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '快递编号',
  `warehouseid` bigint unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditstatus` int DEFAULT NULL,
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`),
  KEY `createdate` (`createdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_outwh_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_outwh_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_preprocessing_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_preprocessing_form` (
  `id` bigint unsigned NOT NULL COMMENT 'ID 唯一键',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `warehouseid` bigint unsigned DEFAULT NULL,
  `number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ftype` tinyint unsigned DEFAULT NULL COMMENT '类型0代表成品，1代表组装成品',
  `is_check_inv_time` datetime DEFAULT NULL COMMENT '是否下架',
  `is_out_consumable_time` datetime DEFAULT NULL COMMENT '是否耗材出库',
  `is_dispatch_time` datetime DEFAULT NULL COMMENT '是否调库',
  `is_down_time` datetime DEFAULT NULL COMMENT '是否下载配货单',
  `is_assembly_time` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL COMMENT '创建人',
  `isrun` bit(1) DEFAULT b'0' COMMENT '是否完成计划',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_auth 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_auth` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `shopid` bigint unsigned DEFAULT NULL,
  `name` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `access_token` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refresh_token` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `resource_owner` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `aliId` bigint unsigned DEFAULT NULL,
  `memberId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refresh_token_timeout` datetime DEFAULT NULL,
  `access_token_timeout` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `isDelete` bit(1) DEFAULT b'0',
  `appkey` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `appsecret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid_name` (`shopid`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1894013767986167810 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_contact 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_contact` (
  `id` char(30) NOT NULL,
  `customer` bigint unsigned NOT NULL,
  `companyName` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `imInPlatform` varchar(100) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`customer`),
  KEY `customer` (`customer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_group 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_group` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `shopid` bigint unsigned DEFAULT NULL,
  `aliId` bigint unsigned DEFAULT NULL,
  `name` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `appkey` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `appsecret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isDelete` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `aliId` (`aliId`,`shopid`),
  KEY `shopid_name` (`shopid`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1724339670451511298 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_message 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_message` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `content` longtext NOT NULL,
  `signature` varchar(50) NOT NULL DEFAULT '',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14404 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_order_baseinfo 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_order_baseinfo` (
  `id` bigint unsigned NOT NULL,
  `idOfStr` char(30) NOT NULL DEFAULT '',
  `alipayTradeId` char(50) DEFAULT NULL,
  `allDeliveredTime` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `modifyTime` datetime DEFAULT NULL,
  `payTime` datetime DEFAULT NULL,
  `businessType` char(2) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `buyerFeedback` varchar(500) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `closeOperateType` char(50) DEFAULT NULL,
  `tradeType` char(10) DEFAULT NULL,
  `discount` int DEFAULT NULL,
  `refund` int DEFAULT NULL,
  `overSeaOrder` bit(1) DEFAULT NULL,
  `refundPayment` decimal(20,6) DEFAULT NULL,
  `shippingFee` decimal(20,6) DEFAULT NULL,
  `totalAmount` decimal(20,6) DEFAULT NULL,
  `sumProductPayment` decimal(20,6) DEFAULT NULL,
  `flowTemplateCode` char(50) DEFAULT NULL,
  `buyerID` char(50) DEFAULT NULL,
  `sellerID` char(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_order_productitems 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_order_productitems` (
  `skuID` bigint unsigned NOT NULL,
  `orderid` bigint unsigned NOT NULL,
  `entryid` bigint unsigned NOT NULL,
  `itemAmount` int unsigned NOT NULL DEFAULT '0',
  `price` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `entryDiscount` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0.000000',
  `statusStr` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0.000000',
  `skuInfos` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `productID` bigint unsigned NOT NULL,
  `productCargoNumber` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `logisticsStatus` tinyint NOT NULL DEFAULT '0',
  `productSnapshotUrl` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `productImgUrl` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `unit` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `refundStatus` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `gmtCreate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `gmtModified` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`orderid`,`skuID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_order_receiverinfo 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_order_receiverinfo` (
  `orderid` bigint unsigned DEFAULT NULL,
  `toArea` varchar(200) DEFAULT NULL,
  `toDivisionCode` char(50) DEFAULT NULL,
  `toFullName` char(50) DEFAULT NULL,
  `toMobile` char(50) DEFAULT NULL,
  `toPost` char(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_order_tradeterms 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_order_tradeterms` (
  `orderid` bigint unsigned NOT NULL,
  `payStatus` char(20) NOT NULL,
  `payTime` datetime DEFAULT NULL,
  `payway` char(20) DEFAULT NULL,
  `phasAmount` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `cardPay` bit(1) NOT NULL DEFAULT b'0',
  `expressPay` bit(1) NOT NULL DEFAULT b'0',
  `payWayDesc` char(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_productitems 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_productitems` (
  `productID` char(20) NOT NULL DEFAULT '',
  `specId` char(36) NOT NULL DEFAULT '',
  `offerid` char(36) NOT NULL DEFAULT '',
  `price` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `name` varchar(500) NOT NULL DEFAULT '0',
  `productSnapshotUrl` varchar(100) NOT NULL DEFAULT '0',
  `productImgUrl` varchar(100) NOT NULL DEFAULT '0',
  `unit` varchar(5) NOT NULL DEFAULT '0',
  `skuInfos` varchar(200) NOT NULL DEFAULT '0',
  PRIMARY KEY (`productID`),
  UNIQUE KEY `specId_offerid` (`specId`,`offerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_settlement 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_settlement` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `acct` bigint unsigned DEFAULT NULL,
  `alibaba_account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` decimal(20,6) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `postdate` date DEFAULT NULL,
  `paydate` date DEFAULT NULL,
  `payamount` decimal(20,6) DEFAULT NULL,
  `paytimes` int DEFAULT NULL,
  `returnamount` decimal(20,6) DEFAULT NULL,
  `returntimes` int DEFAULT NULL,
  `payreturnamount` decimal(20,6) DEFAULT NULL,
  `loaddate` datetime DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_acct_alibaba_account_postdate` (`shopid`,`acct`,`alibaba_account`,`postdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_settlement_order 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_settlement_order` (
  `orderid` char(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `paytime` datetime DEFAULT NULL,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `payamount` decimal(20,6) DEFAULT NULL,
  `confirmamount` decimal(20,6) DEFAULT NULL,
  `confirmtime` datetime DEFAULT NULL,
  `paytype` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `returntype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `returnamount` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`orderid`),
  KEY `settlementid` (`settlementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_settlement_order_return 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_settlement_order_return` (
  `orderid` char(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `payamount` decimal(20,6) DEFAULT NULL,
  `returntime` datetime DEFAULT NULL,
  `returnamount` decimal(20,6) DEFAULT NULL,
  `returntype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `returnto` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`orderid`),
  KEY `settlementid` (`settlementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_settlement_pay 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_settlement_pay` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint DEFAULT NULL,
  `paytime` datetime DEFAULT NULL,
  `paymethod` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `paytype` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `settlementid` (`settlementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='还款明细列表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_settlement_pay_return 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_settlement_pay_return` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint DEFAULT NULL,
  `returntime` datetime DEFAULT NULL,
  `returntype` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `settlementid` (`settlementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='还款明细列表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_fin_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_fin_form` (
  `id` bigint unsigned NOT NULL,
  `entryid` bigint unsigned DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `auditstatus` int unsigned DEFAULT '0' COMMENT '0,待审核 1已审核待付款 2已完成 3.已退回',
  `payment_method` int unsigned DEFAULT NULL,
  `number` char(36) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_fin_form_payment 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_fin_form_payment` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned NOT NULL,
  `formentryid` bigint unsigned DEFAULT NULL,
  `acct` bigint unsigned DEFAULT NULL,
  `payprice` decimal(18,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `projectid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formentryid` (`formentryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `purchaser` bigint unsigned DEFAULT NULL,
  `batch` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`),
  KEY `warehouseid_shopid` (`warehouseid`,`shopid`),
  KEY `createdate` (`createdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_consumable 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_consumable` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `supplier` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `itemprice` decimal(10,2) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `auditstatus` int DEFAULT NULL COMMENT '0:草稿，退回；  1:待审核  ；2:审核通过 ；3：已完成',
  `ischange` bit(1) DEFAULT b'0',
  `paystatus` int DEFAULT NULL,
  `planitemid` bigint unsigned DEFAULT NULL,
  `inwhstatus` int DEFAULT NULL,
  `totalpay` decimal(10,2) DEFAULT '0.00',
  `totalre` int DEFAULT '0',
  `totalin` int DEFAULT '0',
  `totalch` int DEFAULT '0',
  `deliverydate` datetime DEFAULT NULL,
  `closerecdate` datetime DEFAULT NULL COMMENT '入库结束时间',
  `closepaydate` datetime DEFAULT NULL COMMENT '付款结束时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `inwhstatus` (`inwhstatus`),
  KEY `auditstatus` (`auditstatus`),
  KEY `supplier` (`supplier`),
  KEY `formid_materialid_planitemid` (`formid`,`materialid`,`planitemid`),
  KEY `paystatus` (`paystatus`),
  KEY `materialid` (`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry_alibabainfo 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_alibabainfo` (
  `entryid` bigint unsigned NOT NULL,
  `alibaba_auth` bigint unsigned DEFAULT NULL,
  `alibaba_orderid` bigint unsigned DEFAULT NULL,
  `logistics_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `logistics_trace_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `order_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `logistics_status` bit(1) DEFAULT b'0',
  `logistics_trace_status` bit(1) DEFAULT b'0',
  `order_status` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_refresh_time` datetime DEFAULT NULL,
  `logistics_refresh_time` datetime DEFAULT NULL,
  `logistics_trace_refresh_time` datetime DEFAULT NULL,
  `signinfo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`entryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry_alibabainfo_ext 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_alibabainfo_ext` (
  `entryid` bigint unsigned NOT NULL,
  `alibaba_auth` bigint unsigned DEFAULT NULL,
  `alibaba_orderid` bigint unsigned DEFAULT NULL,
  `logistics_status` bit(1) DEFAULT b'0',
  `logistics_trace_status` bit(1) DEFAULT b'0',
  `order_status` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_refresh_time` datetime DEFAULT NULL,
  `logistics_refresh_time` datetime DEFAULT NULL,
  `logistics_trace_refresh_time` datetime DEFAULT NULL,
  `signinfo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `paydate` datetime DEFAULT NULL,
  `countdate` datetime DEFAULT NULL,
  PRIMARY KEY (`entryid`),
  KEY `signinfo` (`signinfo`),
  KEY `countdate` (`countdate`),
  KEY `alibaba_orderid` (`alibaba_orderid`),
  KEY `paydate` (`paydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry_change 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_change` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `entryid` bigint unsigned DEFAULT NULL,
  `supplierid` bigint unsigned DEFAULT NULL,
  `logistics` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` int unsigned DEFAULT NULL,
  `auditstatus` int unsigned DEFAULT '1' COMMENT '1:进行中，0：已完成',
  `without_inv` bit(1) DEFAULT b'0' COMMENT '是否扣库存 0需要扣  1不需要',
  `totalin` int unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `mainid` bigint unsigned DEFAULT NULL,
  `ass_form_id` bigint unsigned DEFAULT NULL,
  `dis_form_id` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`number`,`shopid`),
  KEY `warehouseid` (`warehouseid`),
  KEY `materialid` (`materialid`),
  KEY `entryid` (`entryid`),
  KEY `ass_form_id` (`ass_form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry_change_attachment 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_change_attachment` (
  `id` bigint unsigned NOT NULL,
  `entrychangeid` bigint unsigned DEFAULT NULL,
  `image` bigint unsigned DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `entrychangeid` (`entrychangeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry_change_receive 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_change_receive` (
  `id` bigint unsigned NOT NULL,
  `entrychangeid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0' COMMENT '1撤销 0正常',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `entrychangeid` (`entrychangeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry_copy 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_copy` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `supplier` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `itemprice` decimal(10,2) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `auditstatus` int DEFAULT NULL COMMENT '0:草稿，退回；  1:待审核  ；2:审核通过 ；3：已完成',
  `paystatus` int DEFAULT NULL,
  `planitemid` bigint unsigned DEFAULT NULL,
  `inwhstatus` int DEFAULT NULL,
  `totalpay` decimal(10,2) DEFAULT '0.00',
  `totalre` int DEFAULT '0',
  `totalin` int DEFAULT '0',
  `totalch` int DEFAULT '0',
  `deliverydate` datetime DEFAULT NULL,
  `closerecdate` datetime DEFAULT NULL COMMENT '入库结束时间',
  `closepaydate` datetime DEFAULT NULL COMMENT '付款结束时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `inwhstatus` (`inwhstatus`),
  KEY `auditstatus` (`auditstatus`),
  KEY `supplier` (`supplier`),
  KEY `formid_materialid_planitemid` (`formid`,`materialid`,`planitemid`),
  KEY `paystatus` (`paystatus`),
  KEY `materialid` (`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_history` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `supplier` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `itemprice` decimal(10,2) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `ischange` bit(1) DEFAULT b'0',
  `auditstatus` int DEFAULT NULL COMMENT '0:草稿，退回；  1:待审核  ；2:审核通过 ；3：已完成,4.审核待下单',
  `paystatus` int DEFAULT NULL,
  `planitemid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `inwhstatus` int DEFAULT NULL,
  `totalpay` decimal(10,2) DEFAULT '0.00',
  `totalre` int DEFAULT '0',
  `totalin` int DEFAULT '0',
  `totalch` int DEFAULT '0',
  `deliverydate` datetime DEFAULT NULL,
  `closerecdate` datetime DEFAULT NULL COMMENT '入库结束时间',
  `closepaydate` datetime DEFAULT NULL COMMENT '付款结束时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  KEY `inwhstatus` (`inwhstatus`),
  KEY `auditstatus` (`auditstatus`),
  KEY `supplier` (`supplier`),
  KEY `formid_materialid_planitemid` (`formid`,`materialid`,`planitemid`),
  KEY `paystatus` (`paystatus`),
  KEY `id` (`id`),
  KEY `materialid` (`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry_logistics 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_logistics` (
  `entryid` bigint unsigned NOT NULL,
  `logisticsId` char(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`entryid`,`logisticsId`),
  KEY `logisticsId` (`logisticsId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_payment 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_payment` (
  `id` bigint unsigned NOT NULL,
  `formentryid` bigint unsigned DEFAULT NULL,
  `auditstatus` int DEFAULT NULL COMMENT '1 已付款，2 请款中，0 驳回',
  `payment_method` int DEFAULT NULL,
  `acct` bigint unsigned DEFAULT NULL,
  `payprice` decimal(18,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `projectid` bigint unsigned DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formentryid` (`formentryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_payment_method 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_payment_method` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_print_ip 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_print_ip` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `shopid` bigint unsigned NOT NULL,
  `addressid` bigint unsigned DEFAULT '0',
  `ftype` tinyint unsigned DEFAULT NULL,
  `paper` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ip` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_ftype` (`shopid`,`ftype`,`addressid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1852331708022587394 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_receive 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_receive` (
  `id` bigint unsigned NOT NULL,
  `formentryid` bigint unsigned DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `consumable_form_id` bigint unsigned DEFAULT NULL,
  `preprocessingid` bigint unsigned DEFAULT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'in -入库，re -退货， ch-换货,clear-撤销入库',
  `amount` int DEFAULT NULL,
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formentryid` (`formentryid`),
  KEY `opttime` (`opttime`),
  KEY `consumable_form_id` (`consumable_form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_plan 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_plan` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `number` char(36) DEFAULT NULL,
  `status` tinyint DEFAULT '1' COMMENT '0-取消，1-工作中，2-提交',
  `creator` bigint unsigned DEFAULT NULL,
  `shopid` bigint unsigned NOT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `totalnum` int DEFAULT NULL,
  `totalbuyqty` int DEFAULT NULL,
  `totalpayprice` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_planitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planitem` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `subplanid` bigint unsigned NOT NULL DEFAULT '0',
  `materialid` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned NOT NULL,
  `status` tinyint DEFAULT '1',
  `sales` int DEFAULT '0',
  `amount` int DEFAULT NULL,
  `itemprice` decimal(10,4) DEFAULT NULL,
  `orderprice` decimal(10,4) DEFAULT NULL,
  `supplier` char(36) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `isparent` bit(1) DEFAULT NULL,
  `parent` char(36) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`subplanid`,`materialid`,`warehouseid`),
  KEY `idx_materialid_status` (`materialid`,`status`),
  KEY `materialid` (`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_planitemsub 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planitemsub` (
  `id` bigint unsigned DEFAULT NULL,
  `planitemid` bigint unsigned NOT NULL DEFAULT '0',
  `groupid` bigint unsigned NOT NULL DEFAULT '0',
  `warehouseid` bigint unsigned NOT NULL DEFAULT '0',
  `planamount` int DEFAULT NULL COMMENT '实际发货量',
  PRIMARY KEY (`planitemid`,`warehouseid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='废表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_planmodel 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planmodel` (
  `planid` bigint unsigned NOT NULL DEFAULT '0',
  `modelid` bigint unsigned NOT NULL DEFAULT '0',
  `refreshtime` datetime DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  `operator` bigint unsigned DEFAULT '0',
  PRIMARY KEY (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_planmodelitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planmodelitem` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `modelid` bigint unsigned NOT NULL DEFAULT '0',
  `materialid` bigint unsigned DEFAULT NULL,
  `planamount` int DEFAULT NULL COMMENT '建议补货量',
  `itemprice` decimal(10,4) DEFAULT NULL,
  `invamount` int DEFAULT NULL,
  `orderprice` decimal(10,4) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_materialid_itemprice_planamount_invamount` (`materialid`,`itemprice`,`planamount`,`invamount`),
  KEY `modelid_materialid` (`modelid`,`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_planmodelitemsub 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planmodelitemsub` (
  `itemid` bigint unsigned NOT NULL DEFAULT '0',
  `sku` char(50) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `groupid` bigint unsigned NOT NULL COMMENT '断货时间',
  `needship` int NOT NULL,
  `salesday` int DEFAULT NULL,
  `aftersalesday` int DEFAULT NULL,
  PRIMARY KEY (`itemid`,`sku`,`marketplaceid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_plansub 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_plansub` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `planid` bigint unsigned DEFAULT NULL,
  `status` int DEFAULT NULL COMMENT '1代表在用，0代表放弃，2代表已提交',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'po代表订单，ao代表组装单',
  PRIMARY KEY (`id`),
  KEY `planid` (`planid`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_plan_warahouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_plan_warahouse` (
  `warehouseid` bigint unsigned NOT NULL,
  `planid` bigint unsigned NOT NULL DEFAULT '0',
  `shopid` bigint unsigned NOT NULL,
  PRIMARY KEY (`warehouseid`),
  KEY `planid` (`planid`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='入库仓库和补货规划的映射关系表，一个入库仓库不能在多个补货规划中出现，一个补货规划会有多个入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_warahouse_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_warahouse_material` (
  `planid` bigint unsigned NOT NULL DEFAULT '0',
  `materialid` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`planid`,`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='记录每个sku在补货规划中所默认的入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_warahouse_status 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_warahouse_status` (
  `warehouseid` bigint unsigned NOT NULL,
  `purchase_status` int DEFAULT '0' COMMENT '0表示改仓库无采购任务；1表示采购任务待处理；2表示采购任务已完成',
  `assbly_status` int DEFAULT '0' COMMENT '0表示改仓库无组装任务；1表示组装任务待处理；2表示组装任务已完成',
  `userid` bigint unsigned DEFAULT NULL,
  `opptime` datetime DEFAULT NULL,
  PRIMARY KEY (`warehouseid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='记录每个仓库补货规划的状态，操作人，日期';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_serial_num 结构
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

-- 导出  表 db_erp.t_erp_ship_address 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_address` (
  `id` bigint unsigned NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称或公司名称。',
  `groupid` bigint unsigned DEFAULT NULL COMMENT '店铺id',
  `isfrom` bit(1) DEFAULT NULL COMMENT '1 代表发货地址，0代表收货地址',
  `addressLine1` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '街道地址信息。',
  `addressLine2` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '其他街道地址信息（如果需要）。',
  `city` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '城市',
  `districtOrCounty` char(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '区或县 ',
  `stateOrProvinceCode` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '省份代码',
  `countryCode` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '国家/地区代码',
  `postalCode` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮政编码',
  `shopid` bigint unsigned DEFAULT NULL,
  `phone` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isdefault` bit(1) DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_groupid_city` (`name`,`groupid`,`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_addressto 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_addressto` (
  `id` bigint unsigned NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称或公司名称。',
  `isfrom` bit(1) DEFAULT NULL COMMENT '1 代表发货地址，0代表收货地址',
  `addressLine1` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '街道地址信息。',
  `addressLine2` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '其他街道地址信息（如果需要）。',
  `city` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '城市',
  `districtOrCounty` char(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '区或县 ',
  `stateOrProvinceCode` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '省份代码',
  `countryCode` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '国家/地区代码',
  `postalCode` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮政编码',
  `shopid` bigint unsigned DEFAULT NULL,
  `phone` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_inboundruntime 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundruntime` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `put_on_days` int DEFAULT NULL,
  `first_leg_days` int DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_marketplaceid` (`shopid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_plan 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_plan` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `shopid` bigint unsigned DEFAULT NULL,
  `warehouseid` bigint unsigned NOT NULL COMMENT '自有仓ID（parentid）',
  `amazongroupid` bigint unsigned NOT NULL,
  `totalnum` int NOT NULL COMMENT 'sku数量',
  `totalamount` int NOT NULL COMMENT '发货量= sum(单个sku发货量）',
  `goodsworth` decimal(10,4) DEFAULT NULL COMMENT '发货货值',
  `totalweight` decimal(10,4) DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint unsigned NOT NULL,
  `status` int DEFAULT NULL COMMENT '0代表放弃，1代表在用，2代表已提交',
  PRIMARY KEY (`id`),
  UNIQUE KEY `warehouseid_amazongroupid` (`warehouseid`,`amazongroupid`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_planitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planitem` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `plansubid` bigint unsigned DEFAULT NULL,
  `status` int DEFAULT NULL COMMENT '0代表已放弃，1 代表可用 2.代表已提交。如果plansub的status等于2这里的1 也是已提交',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL COMMENT '实际发货量',
  `selfamount` int DEFAULT NULL,
  `goodsworth` decimal(15,4) DEFAULT NULL COMMENT '货物货值',
  `planweight` decimal(15,4) DEFAULT NULL COMMENT 'itemweight*amount',
  `dimweight` decimal(15,4) DEFAULT NULL COMMENT '材积',
  `needship` int DEFAULT NULL COMMENT '建议发货量',
  PRIMARY KEY (`id`),
  KEY `Index 2` (`plansubid`,`sku`),
  KEY `status_materialid` (`materialid`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_planmodel 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planmodel` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `planid` bigint unsigned DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `索引 2` (`planid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_planmodelitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planmodelitem` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `modelid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `planamount` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`modelid`,`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_planmodelitemsub 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planmodelitemsub` (
  `itemid` bigint unsigned NOT NULL DEFAULT '0',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `needship` int NOT NULL,
  `short_time` datetime DEFAULT NULL COMMENT '断货时间',
  `salesday` int DEFAULT NULL,
  `aftersalesday` int DEFAULT NULL,
  PRIMARY KEY (`itemid`,`marketplaceid`,`sku`),
  KEY `索引 2` (`sku`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_plansub 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_plansub` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `planid` bigint unsigned DEFAULT NULL,
  `status` int DEFAULT NULL COMMENT '0代表放弃，1代表在用，2代表已提交',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `planid` (`planid`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_plansub_euitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_plansub_euitem` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `planid` bigint unsigned DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `plansubid` bigint unsigned NOT NULL DEFAULT '0',
  `marketplaceid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `amount` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqid` (`plansubid`,`marketplaceid`,`sku`)
) ENGINE=InnoDB AUTO_INCREMENT=20649 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_shipment_template_file 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_shipment_template_file` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `filename` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `filepath` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transchannel 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transchannel` (
  `id` bigint unsigned NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transcompany 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transcompany` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '物流公司名称',
  `simplename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `access_token` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `api` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `uploadpath` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `location` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transcompany_api 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transcompany_api` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `api` varchar(200) NOT NULL DEFAULT '0',
  `name` varchar(200) NOT NULL DEFAULT '0',
  `openaccount` varchar(200) NOT NULL DEFAULT '0',
  `openkey` varchar(200) NOT NULL DEFAULT '0',
  `appkey` varchar(200) NOT NULL DEFAULT '0',
  `appsecret` varchar(200) NOT NULL DEFAULT '0',
  `token` varchar(200) NOT NULL DEFAULT '0',
  `shopid` bigint unsigned NOT NULL DEFAULT '0',
  `operator` bigint unsigned NOT NULL DEFAULT '0',
  `opttime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `url` varchar(500) DEFAULT NULL,
  `system` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transcompany_services_zhihui 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transcompany_services_zhihui` (
  `id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `code` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `apiid` bigint unsigned NOT NULL DEFAULT '0',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`,`apiid`) USING BTREE,
  KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transdetail 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transdetail` (
  `id` bigint unsigned NOT NULL,
  `company` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `subarea` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `channel` bigint unsigned DEFAULT NULL,
  `channame` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pretime` int DEFAULT NULL COMMENT 'US预计时效',
  `price` decimal(10,4) DEFAULT NULL,
  `drate` int DEFAULT '5000',
  `opttime` datetime DEFAULT NULL,
  `transtype` bigint unsigned DEFAULT NULL,
  `priceunits` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `cbmrate` int DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `disabled` bit(1) NOT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`company`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transdetail_bkp20240802 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transdetail_bkp20240802` (
  `id` bigint unsigned NOT NULL,
  `company` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `subarea` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `channel` bigint unsigned DEFAULT NULL,
  `channame` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pretime` int DEFAULT NULL COMMENT 'US预计时效',
  `price` decimal(10,4) DEFAULT NULL,
  `drate` int DEFAULT '5000',
  `opttime` datetime DEFAULT NULL,
  `transtype` bigint unsigned DEFAULT NULL,
  `priceunits` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `cbmrate` int DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `disabled` bit(1) NOT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `Index 2` (`company`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transdetail_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transdetail_his` (
  `id` bigint unsigned NOT NULL,
  `company` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `subarea` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `channel` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `channame` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pretime` int DEFAULT NULL COMMENT 'US预计时效',
  `price` decimal(10,4) DEFAULT NULL,
  `drate` int DEFAULT '5000',
  `opttime` datetime NOT NULL,
  `transtype` bigint unsigned DEFAULT NULL,
  `priceunits` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `cbmrate` int DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `disabled` bit(1) DEFAULT b'0',
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`opttime`,`id`),
  KEY `Index 2` (`company`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transtype_day 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transtype_day` (
  `transtypeid` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `day` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`transtypeid`,`shopid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stepwise_quotn 结构
CREATE TABLE IF NOT EXISTS `t_erp_stepwise_quotn` (
  `id` bigint unsigned NOT NULL,
  `material` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `material_amount` (`material`,`amount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stockcycle 结构
CREATE TABLE IF NOT EXISTS `t_erp_stockcycle` (
  `id` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `stockingCycle` int DEFAULT NULL COMMENT '安全库存周期',
  `min_cycle` int DEFAULT NULL COMMENT '最小补货周期/最小发货周期',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `warehouseid_materialid` (`warehouseid`,`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stocktaking 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `isworking` bit(1) DEFAULT NULL,
  `ftype` tinyint DEFAULT NULL,
  `whtotalamount` int DEFAULT NULL,
  `whtotalprice` decimal(15,4) DEFAULT NULL,
  `overamount` int DEFAULT NULL,
  `lossamount` int DEFAULT NULL,
  `overprice` decimal(12,4) DEFAULT NULL,
  `lossprice` decimal(12,4) DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stocktaking_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking_item` (
  `id` bigint unsigned NOT NULL,
  `stocktakingid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `overamount` int DEFAULT '0',
  `lossamount` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `stocktaking_mate_ware` (`stocktakingid`,`warehouseid`,`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stocktaking_item_shelf 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking_item_shelf` (
  `id` bigint unsigned NOT NULL,
  `stocktakingid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `shelfid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `overamount` int DEFAULT '0',
  `lossamount` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `stocktaking_mate_ware` (`stocktakingid`,`warehouseid`,`materialid`,`shelfid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stocktaking_shelf 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking_shelf` (
  `stocktakingid` bigint unsigned NOT NULL,
  `shelfid` bigint unsigned NOT NULL,
  PRIMARY KEY (`stocktakingid`,`shelfid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stocktaking_warehouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking_warehouse` (
  `stocktakingid` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned NOT NULL,
  PRIMARY KEY (`stocktakingid`,`warehouseid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_thirdparty_api 结构
CREATE TABLE IF NOT EXISTS `t_erp_thirdparty_api` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `api` varchar(200) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `appkey` varchar(200) DEFAULT NULL,
  `appsecret` varchar(1000) DEFAULT NULL,
  `token` varchar(200) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  `system` varchar(50) DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name_shopid` (`name`,`shopid`,`system`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17392024503578653692 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_thirdparty_api_bkp20240830 结构
CREATE TABLE IF NOT EXISTS `t_erp_thirdparty_api_bkp20240830` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `api` varchar(200) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `appkey` varchar(200) DEFAULT NULL,
  `appsecret` varchar(200) DEFAULT NULL,
  `token` varchar(200) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  `system` varchar(50) DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17392024503578653692 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_thirdparty_inventory_k5 结构
CREATE TABLE IF NOT EXISTS `t_erp_thirdparty_inventory_k5` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `api` bigint unsigned NOT NULL DEFAULT '0',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `cname` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ename` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `housename` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `houseid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `locknum` int DEFAULT NULL,
  `num` int DEFAULT NULL,
  `outnum` int DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku_houseid` (`shopid`,`api`,`houseid`,`sku`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1887388682961502211 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_thirdparty_inventory_ops 结构
CREATE TABLE IF NOT EXISTS `t_erp_thirdparty_inventory_ops` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `api` bigint unsigned NOT NULL DEFAULT '0',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `cname` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ename` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `housename` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `houseid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `code` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `weight` decimal(20,6) DEFAULT NULL,
  `height` decimal(20,6) DEFAULT NULL,
  `length` decimal(20,6) DEFAULT NULL,
  `width` decimal(20,6) DEFAULT NULL,
  `vol` decimal(20,6) DEFAULT NULL,
  `packagetype` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `mark` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `sku_houseid` (`shopid`,`api`,`houseid`,`sku`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1892512979436503044 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_thirdparty_quote_buyer 结构
CREATE TABLE IF NOT EXISTS `t_erp_thirdparty_quote_buyer` (
  `id` bigint unsigned NOT NULL,
  `buyertoken` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isowner` bit(1) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `operater` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `buyertoken_shopid` (`buyertoken`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_thirdparty_system 结构
CREATE TABLE IF NOT EXISTS `t_erp_thirdparty_system` (
  `id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `cname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `support` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `apidoc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `classz` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `needkey` bit(1) DEFAULT NULL COMMENT '使用appkey和appsecret',
  `needtoken` bit(1) DEFAULT NULL COMMENT '使用token',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_thirdparty_warehouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_thirdparty_warehouse` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `code` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `api` bigint unsigned DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `country` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ext` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1890589091553300482 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_transtype 结构
CREATE TABLE IF NOT EXISTS `t_erp_transtype` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_turnover_rate 结构
CREATE TABLE IF NOT EXISTS `t_erp_turnover_rate` (
  `id` int DEFAULT NULL,
  `year` int DEFAULT NULL,
  `month` int DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `sku` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `begininv` int DEFAULT NULL,
  `endinv` int DEFAULT NULL,
  `outinv` int DEFAULT NULL,
  `wrate` decimal(10,2) DEFAULT NULL,
  `wday` decimal(10,2) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_unsalable 结构
CREATE TABLE IF NOT EXISTS `t_erp_unsalable` (
  `sku` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `invqty` int DEFAULT NULL,
  `invinqty` int DEFAULT NULL,
  `inv90` int DEFAULT NULL,
  `inv180` int DEFAULT NULL,
  `inv365` int DEFAULT NULL,
  `invout90` int DEFAULT NULL,
  `over90` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_calculate_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_calculate_record` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `ftype` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '计算类型，发货，采购，人力',
  `iswarn` bit(1) NOT NULL DEFAULT b'0',
  `operator` bigint unsigned NOT NULL COMMENT '计算操作人',
  `opttime` datetime NOT NULL COMMENT '计算时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_ftype` (`shopid`,`ftype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用于存储各个计算模块的计算时间，计算人。统一留存历史记录';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_calculate_record_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_calculate_record_history` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `ftype` char(20) DEFAULT NULL,
  `iswarn` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_ftype` (`shopid`,`ftype`,`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_man_month 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_man_month` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `month` date DEFAULT NULL COMMENT '对应月份',
  `standardtime` int DEFAULT NULL COMMENT '以标准工时计算的月份对应的总时长',
  `standardperson` int DEFAULT NULL COMMENT '以标准工时算对应所需要的人力',
  `overtime` int DEFAULT NULL COMMENT '以超出时间算的对应时长',
  `overperson` int DEFAULT NULL COMMENT '以超出时间算的对应人力',
  `multiple` float DEFAULT NULL COMMENT '当前月份计算的倍数',
  `unitsoftime` int DEFAULT NULL COMMENT '一个员工常规工作时间',
  `overoftime` int DEFAULT NULL COMMENT '超出时间（即加班） 工作时间',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_month` (`shopid`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='人力计算结果保存';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_man_month_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_man_month_history` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `month` date DEFAULT NULL,
  `standardtime` int DEFAULT NULL,
  `standardperson` int DEFAULT NULL,
  `overtime` int DEFAULT NULL,
  `overperson` int DEFAULT NULL,
  `multiple` float DEFAULT NULL,
  `unitsoftime` int DEFAULT NULL,
  `overoftime` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_month` (`shopid`,`month`,`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='人力计算结果历史';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_pickpay_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_pickpay_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT '' COMMENT '订单编码',
  `month` date DEFAULT NULL COMMENT '对应月份',
  `shopid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '公司ID',
  `operator` bigint unsigned NOT NULL COMMENT '创建人',
  `creator` bigint unsigned NOT NULL COMMENT '操作人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `month_shopid` (`month`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='采购提货与付款模块分组，采用一个月一个表单的结构，对采购付款历史与审核进行保存';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_pickpay_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_pickpay_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned NOT NULL COMMENT '订单ID',
  `materialid` bigint unsigned NOT NULL COMMENT '产品ID',
  `auditor` bigint unsigned NOT NULL COMMENT '审核人',
  `auditstatus` int unsigned NOT NULL DEFAULT '0' COMMENT '审核状态',
  `audittime` datetime NOT NULL COMMENT '审核时间',
  `supplier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '供应商',
  `inbound` int NOT NULL DEFAULT '0' COMMENT '待入库数量',
  `suggest` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '建议提货量',
  `planpick` int NOT NULL DEFAULT '0' COMMENT '建议付款金额',
  `planpay` decimal(14,2) NOT NULL DEFAULT '0.00' COMMENT '计划付款金额',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '计划提货量',
  `operator` bigint unsigned NOT NULL COMMENT '操作人',
  `creator` bigint unsigned NOT NULL COMMENT '创建人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='提货付款模块SKU从审核到通过以及历史的具体entry表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_presale_month 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_month` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `pid` bigint unsigned NOT NULL COMMENT '商品ID',
  `month` date NOT NULL COMMENT '月日期，每个月1号',
  `amount` int unsigned NOT NULL DEFAULT '0' COMMENT '月销量',
  `opttime` datetime DEFAULT NULL COMMENT '修改日期',
  `operator` bigint unsigned DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`pid`,`month`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='销售预测月度结果';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_presale_month_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_month_form` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `year` int unsigned DEFAULT NULL COMMENT '年份',
  `month` int unsigned DEFAULT NULL COMMENT '月份',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creator` bigint unsigned DEFAULT NULL COMMENT '创建人',
  `creattime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_year_month` (`shopid`,`year`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='销售预测提交的表单，以每个月一份表单的方式存储整个公司关于销售预测的提交';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_presale_month_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_month_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned NOT NULL COMMENT '本地产品ID ',
  `pid` bigint unsigned NOT NULL COMMENT '商品ID',
  `auditstatus` int unsigned DEFAULT '0' COMMENT '0未提交，1 提交待审核，2审核成功  3已驳回',
  `audittime` datetime DEFAULT NULL COMMENT '审核时间',
  `auditor` bigint unsigned DEFAULT '0' COMMENT '审核人',
  `status` int unsigned DEFAULT '0' COMMENT '维持 提升 等销售状态 默认是无',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creatime` datetime DEFAULT NULL COMMENT '创建时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `creator` bigint unsigned DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_one` (`pid`,`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='销售预测单个SKU所以的审核与历史表单对应';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_presale_month_form_entry_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_month_form_entry_item` (
  `id` bigint unsigned NOT NULL,
  `entryid` bigint unsigned NOT NULL COMMENT '订单中产品所对应记录的ID',
  `year` int unsigned NOT NULL COMMENT '年份',
  `month` int unsigned NOT NULL COMMENT '月份',
  `amount` int NOT NULL DEFAULT '0' COMMENT '对应计划数量',
  `opttime` datetime DEFAULT NULL COMMENT '操作人',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_one` (`entryid`,`year`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='销售预测每个SKU对应每个月的预测数据';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_presale_week 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_week` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `pid` bigint unsigned NOT NULL COMMENT '商品ID',
  `week` date NOT NULL COMMENT '周日期',
  `amount` int NOT NULL DEFAULT '0' COMMENT '周预估销量',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`pid`,`week`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='销售预测周结构数据存储';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT '' COMMENT '采购订单编码',
  `auditor` bigint unsigned DEFAULT NULL COMMENT '审核人',
  `auditstatus` int DEFAULT '0' COMMENT '审核状态',
  `audittime` datetime DEFAULT NULL COMMENT '审核时间',
  `skunum` int DEFAULT NULL COMMENT 'SKU数量',
  `warehouseid` bigint unsigned DEFAULT NULL COMMENT '仓库',
  `shopid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '公司ID',
  `operator` bigint unsigned NOT NULL COMMENT '操作人',
  `creator` bigint unsigned NOT NULL COMMENT '创建人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='采购模块表单保存表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned NOT NULL COMMENT '订单ID',
  `materialid` bigint unsigned NOT NULL COMMENT '本地产品ID',
  `warehouse` char(50) NOT NULL DEFAULT '' COMMENT '仓库',
  `sumreq` int NOT NULL DEFAULT '0' COMMENT '需求量',
  `salemonth` int NOT NULL DEFAULT '0' COMMENT '月销量',
  `presalemonth` varchar(500) NOT NULL DEFAULT '0' COMMENT '销售预测',
  `moreqty` char(50) NOT NULL DEFAULT '0' COMMENT '多余库存',
  `suggest` varchar(500) NOT NULL DEFAULT '0' COMMENT '月度建议采购量',
  `needqty` int NOT NULL DEFAULT '0' COMMENT '需求量',
  `sugpurchase` int NOT NULL DEFAULT '0' COMMENT '建议采购量',
  `planpurchase` int NOT NULL DEFAULT '0' COMMENT '计划采购量',
  `detail` text COMMENT '当时计算的需求详情与提货详情',
  `operator` bigint unsigned NOT NULL COMMENT '操作人',
  `creator` bigint unsigned NOT NULL COMMENT '创建人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_material` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `materialid` bigint unsigned NOT NULL COMMENT '本地产品ID',
  `purchasedaynum` int DEFAULT NULL COMMENT '采购周期天数',
  `needamount` int DEFAULT NULL COMMENT '需求量（通过采购周期内需求量汇总减去库存得出）',
  `suggestamount` int DEFAULT NULL COMMENT '建议采购量（通过对需求量合箱规得出）',
  `sumrquest` int DEFAULT NULL COMMENT '总需求量',
  `findex` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`materialid`),
  KEY `shopid` (`shopid`),
  KEY `sumrquest` (`sumrquest`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品采购计划，计算结果，存储每个SKU对应的采购周期，需求量，建议采购量，总需求量等';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_material_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_material_history` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `purchasedaynum` int NOT NULL DEFAULT '0',
  `needamount` int NOT NULL DEFAULT '0',
  `suggestamount` int NOT NULL DEFAULT '0',
  `sumrquest` int NOT NULL DEFAULT '0',
  `findex` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`materialid`,`opttime`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_selected 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_selected` (
  `materialid` bigint unsigned NOT NULL COMMENT '本地产品ID',
  `userid` bigint unsigned NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`materialid`,`userid`),
  KEY `userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='采购计算结果选中发货，记录是否选中';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_week 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_week` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `materialid` bigint unsigned NOT NULL COMMENT '本地产品ID',
  `week` date NOT NULL COMMENT '周日期',
  `requestqty` int NOT NULL DEFAULT '0' COMMENT '需求量（将商品销量通过对关系与本地产品对应）',
  `moreqty` int DEFAULT '0' COMMENT '多余库存（库存减去对应安全库存+头程周期+增长天数对应需求量的和）',
  `suggestqty` int DEFAULT '0' COMMENT '建议提货量（将需求量+剩余库存，缺少部分通过合箱规后）',
  `differentqty` int DEFAULT '0' COMMENT '差异数量-（销售预测与库存之间的差异值）',
  `isfull` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`materialid`,`week`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品采购计划，通过发货需求与本地产品对应，并进行本地SKU转化，组装子SKU转换\r\n采购周情况存储。包换需求量，多余库存等';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_week_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_week_history` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `week` date NOT NULL,
  `requestqty` int NOT NULL DEFAULT '0',
  `moreqty` int DEFAULT '0',
  `suggestqty` int DEFAULT '0',
  `differentqty` int DEFAULT '0',
  `isfull` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`materialid`,`week`,`opttime`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='采购计算周数据历史保存';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_ship_setting 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_ship_setting` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `addnum` int DEFAULT NULL,
  `startday` int DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='发货计划设置，用于保存发货计算中当达成率与增长率达到标准时增加的天数';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_ship_week 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_ship_week` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `pid` bigint unsigned NOT NULL COMMENT '商品ID',
  `week` date NOT NULL COMMENT '对应周日期',
  `amount` int unsigned NOT NULL DEFAULT '0' COMMENT '对应周需求量',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`pid`,`week`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='发货计划周情况，记录发货计算历史结果,根据销售预测得到需求量';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_ship_week_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_ship_week_history` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `pid` bigint unsigned NOT NULL,
  `week` date NOT NULL,
  `amount` int unsigned NOT NULL DEFAULT '0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`pid`,`week`,`opttime`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_productl_workhours 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_productl_workhours` (
  `mid` bigint unsigned NOT NULL COMMENT 'pid',
  `amount` int unsigned DEFAULT NULL COMMENT '一个小时内的生产量',
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'hour' COMMENT '类型: hour mins second等 默认hour',
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`mid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='人力计算配置表\r\n标准工时/H	待增加	每个小时能处理多少个该产品的组装及打包发货工作	产品信息管理	\r\n独立产品及组装产品的主SKU（在售成品）需要设置【标准工时】。用作计算发货工时及人力需求。	\r\n如有产品需要完成组装（工作较为复杂），则可能为“12”，意为每小时从拣货、组装到装箱打包可以处理12个该产品。如流程较为简单，则可能为“35”，即为每小时可处理35个。\r\n';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_ship_product_delivery_cycle 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_ship_product_delivery_cycle` (
  `pid` bigint unsigned NOT NULL COMMENT '商品ID',
  `deliverycycle` int unsigned DEFAULT NULL COMMENT '头程天数',
  `findex` int unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='商品的头程周期存储';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_shop_units_worktime 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_shop_units_worktime` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `multiple` float DEFAULT NULL COMMENT '工时倍数',
  `unitsoftime` int DEFAULT NULL COMMENT '每个人一个月的正常工作时间 h 工作时长（常规）	待增加	工作时长/人/月	增加参数设置（蔚蓝使用为204小时）	用作计算所需人力数量',
  `overoftime` int DEFAULT NULL COMMENT '工作时长（较长）	待增加	工作时长/人/月	增加参数设置（蔚蓝使用为286小时）	用作计算所需人力数量',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='人力计算配置表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v3_purchase_plan 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `min_cycle` int unsigned DEFAULT NULL,
  `stocking_cycle` int unsigned DEFAULT NULL,
  `shopid` bigint unsigned NOT NULL,
  `disable` bit(1) NOT NULL DEFAULT b'0',
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v3_purchase_plan_consumable_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_consumable_item` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `materialid` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned NOT NULL,
  `amount` int DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`materialid`,`warehouseid`),
  KEY `shopid` (`shopid`),
  KEY `materialid` (`warehouseid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v3_purchase_plan_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_item` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `materialid` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned NOT NULL,
  `planid` bigint unsigned NOT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `batchnumber` char(20) DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`planid`,`materialid`),
  KEY `shopid` (`shopid`),
  KEY `materialid` (`warehouseid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v3_purchase_plan_item_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_item_history` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned NOT NULL,
  `planid` bigint unsigned NOT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `batchnumber` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`planid`,`warehouseid`,`batchnumber`,`materialid`),
  KEY `shopid_opttime` (`shopid`,`warehouseid`,`opttime`),
  KEY `materialid` (`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v3_purchase_plan_warehouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_warehouse` (
  `warehouseid` bigint unsigned NOT NULL,
  `planid` bigint unsigned NOT NULL DEFAULT '0',
  `shopid` bigint unsigned NOT NULL,
  PRIMARY KEY (`planid`,`warehouseid`),
  KEY `shopid` (`shopid`),
  KEY `planid` (`warehouseid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='入库仓库和补货规划的映射关系表，一个入库仓库不能在多个补货规划中出现，一个补货规划会有多个入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v3_purchase_plan_warehouse_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_warehouse_material` (
  `planid` bigint unsigned NOT NULL DEFAULT '0',
  `materialid` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`planid`,`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='记录每个sku在补货规划中所默认的入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_warehouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse` (
  `id` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'ID',
  `name` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `ftype` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类型',
  `flevel` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '级别',
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '编号',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `findex` int DEFAULT NULL COMMENT '次序',
  `country` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `fbawareid` bigint unsigned DEFAULT NULL COMMENT '海外仓',
  `isdefault` bit(1) DEFAULT b'0' COMMENT '默认仓库',
  `shopid` bigint unsigned NOT NULL COMMENT '店铺',
  `parentid` bigint unsigned DEFAULT NULL COMMENT '父节点',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `addressid` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  `stocking_cycle` int DEFAULT '0',
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  `isstocktaking` bit(1) DEFAULT b'0',
  `min_cycle` int DEFAULT '0',
  `first_leg_charges` decimal(12,2) DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ishungry` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `ftype` (`ftype`),
  KEY `Index 2` (`parentid`),
  KEY `name_shopid` (`name`,`shopid`),
  KEY `shopid` (`shopid`),
  KEY `addressid` (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_warehouse_address 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_address` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `shopid` bigint unsigned NOT NULL DEFAULT '0',
  `name` char(50) NOT NULL DEFAULT '0' COMMENT '地址名称',
  `number` char(50) NOT NULL DEFAULT '0',
  `detail` varchar(500) DEFAULT '0' COMMENT '地址街道详情',
  `postcode` char(50) DEFAULT '0' COMMENT '邮编',
  `phone` char(50) DEFAULT NULL COMMENT '业主电话',
  `landlord` char(50) DEFAULT NULL COMMENT '业主（房东）',
  `lost_effect_date` datetime DEFAULT NULL COMMENT '到期时间',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `disabled` bit(1) DEFAULT b'0' COMMENT '是否失效（是否删除）',
  `operator` bigint unsigned DEFAULT '0' COMMENT '修改人',
  `opttime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `creator` bigint unsigned DEFAULT '0' COMMENT '创建人',
  `creattime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_number` (`shopid`,`number`),
  UNIQUE KEY `shopid_name` (`shopid`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_warehouse_shelf 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf` (
  `id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '货柜ID',
  `addressid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '仓库ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '货柜名称',
  `number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '编码',
  `capacity` float NOT NULL DEFAULT '0' COMMENT '容量(立方厘米)',
  `length` float unsigned NOT NULL DEFAULT '0' COMMENT '长度(cm)',
  `width` float unsigned NOT NULL DEFAULT '0' COMMENT '宽度(cm)',
  `height` float unsigned NOT NULL DEFAULT '0' COMMENT '高度(cm)',
  `parentid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '父货柜ID',
  `sort` int unsigned NOT NULL DEFAULT '0' COMMENT '排序即（柜子所在位置）',
  `treepath` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '所有付货柜编码如：A01!033!F01',
  `shopid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '公司ID',
  `iswarn` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否报警',
  `isdelete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否逻辑删除',
  `isfrozen` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否冻结',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creator` bigint unsigned DEFAULT NULL COMMENT '创建人',
  `creattime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `treepath` (`shopid`,`addressid`,`treepath`(191)),
  KEY `parentid` (`parentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='仓库货柜';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_warehouse_shelf_inventory 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf_inventory` (
  `id` bigint unsigned NOT NULL COMMENT 'ID',
  `shelfid` bigint unsigned DEFAULT NULL COMMENT '货柜ID',
  `materialid` bigint unsigned DEFAULT NULL COMMENT '产品ID',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `warehouseid` bigint unsigned DEFAULT NULL,
  `quantity` int DEFAULT NULL COMMENT '当前数量',
  `size` float unsigned DEFAULT NULL COMMENT '当前体积',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shelfid_materialid_shopid` (`shopid`,`shelfid`,`materialid`,`warehouseid`),
  UNIQUE KEY `materialid_shopid` (`shopid`,`materialid`,`shelfid`,`warehouseid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='货架产品库存';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_warehouse_shelf_inventory_opt_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf_inventory_opt_record` (
  `id` bigint unsigned NOT NULL COMMENT 'ID',
  `shelfid` bigint unsigned DEFAULT NULL COMMENT '货柜ID',
  `materialid` bigint unsigned DEFAULT NULL COMMENT '产品ID',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `warehouseid` bigint unsigned DEFAULT NULL,
  `quantity` int unsigned DEFAULT NULL COMMENT '操作数量',
  `size` float unsigned DEFAULT NULL COMMENT '操作数量对于的体积使用立方厘米cm3',
  `balance_qty` int unsigned DEFAULT NULL COMMENT '操作后结余数量',
  `balance_size` float unsigned DEFAULT NULL COMMENT '操作后结余体积',
  `opt` int unsigned DEFAULT NULL COMMENT '0：出库；1：入库;2：修正下架；3：修正上架',
  `formid` bigint unsigned DEFAULT NULL COMMENT '表单ID',
  `formtype` char(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '表单类型',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `shelfid_materialid_shopid_formid_formtype` (`shopid`,`shelfid`,`materialid`,`warehouseid`),
  KEY `formid_formtype` (`formid`,`formtype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='操作记录';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_warehouse_type 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_type` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'ID',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '店铺',
  `issystem` bit(1) DEFAULT NULL COMMENT '是否系统',
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名字',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_whse_unsalable_rpt 结构
CREATE TABLE IF NOT EXISTS `t_erp_whse_unsalable_rpt` (
  `shopid` bigint unsigned NOT NULL,
  `wid` bigint unsigned NOT NULL,
  `name` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `mtid` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `groupid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `qtysum` decimal(35,0) DEFAULT NULL,
  `qtyablesum` decimal(35,0) DEFAULT NULL,
  `qtysum30` decimal(35,0) DEFAULT NULL,
  `qtysum60` decimal(35,0) DEFAULT NULL,
  `qtysum90` decimal(35,0) DEFAULT NULL,
  `qtysum180` decimal(35,0) DEFAULT NULL,
  `qtysum365` decimal(35,0) DEFAULT NULL,
  `salesum30` decimal(35,0) DEFAULT NULL,
  `salesum60` decimal(32,0) DEFAULT NULL,
  `salesum90` decimal(32,0) DEFAULT NULL,
  `salesum180` decimal(32,0) DEFAULT NULL,
  `salesum365` decimal(32,0) DEFAULT NULL,
  `nostock30` decimal(32,0) DEFAULT NULL,
  PRIMARY KEY (`shopid`,`wid`,`mtid`,`sku`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_marketplace 结构
CREATE TABLE IF NOT EXISTS `t_marketplace` (
  `marketplaceId` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '站点编码ID',
  `market` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '站点简码',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '站点名称',
  `region_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '所属区域名称',
  `region` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '所属区域简码',
  `end_point` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '所属区域站点',
  `point_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `accessKey` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `secretKey` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `dim_units` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight_units` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `findex` int DEFAULT '0',
  `adv_end_point` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `aws_access_key` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `aws_secret_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `associate_tag` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `developer_url` varchar(1100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `dev_account_num` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bytecode` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sp_api_endpoint` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `aws_region` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `language` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`marketplaceId`),
  KEY `aws_region` (`aws_region`),
  KEY `Index 2` (`market`),
  KEY `Index 3` (`point_name`),
  KEY `currency` (`currency`),
  KEY `region` (`region`),
  KEY `marketplaceId_region` (`marketplaceId`,`region`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='站点';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_parameterconfig 结构
CREATE TABLE IF NOT EXISTS `t_parameterconfig` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ptype` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pkey` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sortindex` int DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pkey` (`pkey`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_picture 结构
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

-- 导出  表 db_erp.t_sys_operationlog 结构
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

-- 导出  表 db_erp.undo_log 结构
CREATE TABLE IF NOT EXISTS `undo_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `branch_id` bigint NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `context` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

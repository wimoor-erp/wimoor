-- --------------------------------------------------------
-- 主机:                           wimoor.rwlb.rds.aliyuncs.com
-- 服务器版本:                        8.0.36 - Source distribution
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 导出  表 db_quote.t_erp_serial_num 结构
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

-- 导出  表 db_quote.t_order 结构
CREATE TABLE IF NOT EXISTS `t_order` (
  `id` bigint unsigned NOT NULL,
  `buyerid` bigint unsigned DEFAULT NULL,
  `ftype` tinyint DEFAULT NULL COMMENT '0：单个报价，2 批量报价 ，3 地址报价',
  `isgroupbuy` bit(1) DEFAULT NULL COMMENT '是否拼团',
  `isbidding` bit(1) DEFAULT NULL,
  `weight` decimal(20,6) DEFAULT NULL,
  `volume` decimal(20,6) DEFAULT NULL,
  `number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` int unsigned DEFAULT NULL COMMENT '1,等待询价，2等待拼团 3等待报价，4已产生报价， 5结束',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `closetime` datetime DEFAULT NULL COMMENT '结束时间',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `pricetime` datetime DEFAULT NULL COMMENT '报价结束时间',
  PRIMARY KEY (`id`),
  KEY `createtime` (`createtime`),
  KEY `buyerid` (`buyerid`,`createtime`) USING BTREE,
  KEY `buyerid_number` (`buyerid`,`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_order_shipment 结构
CREATE TABLE IF NOT EXISTS `t_order_shipment` (
  `orderid` bigint unsigned NOT NULL,
  `shipmentid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`orderid`,`shipmentid`),
  KEY `shipmentid` (`shipmentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_order_supplier 结构
CREATE TABLE IF NOT EXISTS `t_order_supplier` (
  `orderid` bigint unsigned NOT NULL,
  `supplierid` bigint unsigned NOT NULL,
  `base` decimal(20,6) unsigned NOT NULL DEFAULT '0.000000',
  `status` tinyint unsigned DEFAULT NULL COMMENT '5：接受报价 4:提交报价-已出价 1：等待报价 ',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`orderid`,`supplierid`),
  KEY `supplierid` (`supplierid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_purchase_alibaba_buyer 结构
CREATE TABLE IF NOT EXISTS `t_purchase_alibaba_buyer` (
  `id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `buyerLoginId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `phone` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `mobile` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `loginname` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopName` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `companyName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_purchase_alibaba_order 结构
CREATE TABLE IF NOT EXISTS `t_purchase_alibaba_order` (
  `id` bigint unsigned DEFAULT NULL,
  `sellerID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyerID` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `buyername` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `buyercompany` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `buyerothername` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `price` decimal(20,6) DEFAULT NULL,
  `sysbuyerid` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  KEY `sellerID` (`sellerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_purchase_alibaba_order_item 结构
CREATE TABLE IF NOT EXISTS `t_purchase_alibaba_order_item` (
  `id` bigint unsigned DEFAULT NULL,
  `orderid` bigint unsigned DEFAULT NULL,
  `skuID` bigint unsigned DEFAULT NULL,
  `productID` bigint unsigned DEFAULT NULL,
  `name` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `productCargoNumber` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `image` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int unsigned DEFAULT NULL,
  `status` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `statusStr` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `price` decimal(20,6) DEFAULT NULL,
  `amount` decimal(20,6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_purchase_alibaba_receiver 结构
CREATE TABLE IF NOT EXISTS `t_purchase_alibaba_receiver` (
  `orderid` bigint unsigned NOT NULL,
  `area` varchar(500) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `division` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `mobile` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `post` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `town` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`orderid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_purchase_alibaba_seller 结构
CREATE TABLE IF NOT EXISTS `t_purchase_alibaba_seller` (
  `id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sellerLoginId` char(50) COLLATE utf8mb4_bin NOT NULL,
  `phone` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `mobile` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `loginname` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `shopName` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `companyName` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_purchase_order_entry 结构
CREATE TABLE IF NOT EXISTS `t_purchase_order_entry` (
  `id` bigint unsigned NOT NULL,
  `orderid` bigint unsigned NOT NULL,
  `number` char(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `materialid` bigint unsigned DEFAULT NULL,
  `itemprice` decimal(20,6) unsigned DEFAULT NULL,
  `orderprice` decimal(20,6) unsigned DEFAULT NULL,
  `sku` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `image` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `warehousename` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `suppliername` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `length` decimal(20,6) DEFAULT NULL,
  `width` decimal(20,6) DEFAULT NULL,
  `height` decimal(20,6) DEFAULT NULL,
  `weight` decimal(20,6) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `orderid` (`orderid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_purchase_quote_form 结构
CREATE TABLE IF NOT EXISTS `t_purchase_quote_form` (
  `id` bigint DEFAULT NULL,
  `weight` decimal(20,6) DEFAULT NULL,
  `boxnum` int DEFAULT NULL,
  `number` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_shipment 结构
CREATE TABLE IF NOT EXISTS `t_shipment` (
  `shipmentid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `status` tinyint unsigned DEFAULT '0',
  `destination` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `area` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isfar` bit(1) DEFAULT b'0',
  `buyername` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyercompany` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyerothername` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `groupname` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehousename` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `country` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(20,6) DEFAULT NULL,
  `volume` decimal(20,6) DEFAULT NULL,
  `num` int DEFAULT NULL,
  `buyerid` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '询价备注',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`shipmentid`),
  KEY `opttime` (`opttime`),
  KEY `buyerid` (`buyerid`,`opttime`,`status`) USING BTREE,
  KEY `number` (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_shipment_box 结构
CREATE TABLE IF NOT EXISTS `t_shipment_box` (
  `boxid` char(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `shipmentid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `length` decimal(15,2) DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `unit` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `wunit` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`boxid`),
  KEY `shipmentid` (`shipmentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_shipment_destination_address 结构
CREATE TABLE IF NOT EXISTS `t_shipment_destination_address` (
  `code` char(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `destinationType` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称或公司名称。',
  `addressLine1` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '街道地址信息。',
  `addressLine2` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '其他街道地址信息（如果需要）。',
  `city` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '城市',
  `companyName` char(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '区或县 ',
  `phoneNumber` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '省份代码',
  `countryCode` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '国家/地区代码',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `stateOrProvinceCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `postalCode` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮政编码',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_shipment_item 结构
CREATE TABLE IF NOT EXISTS `t_shipment_item` (
  `id` bigint unsigned NOT NULL,
  `shipmentid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ename` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `material` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `code` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `image` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`shipmentid`,`id`) USING BTREE,
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `shipmentid_sku` (`shipmentid`,`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_shipment_summary 结构
CREATE TABLE IF NOT EXISTS `t_shipment_summary` (
  `code` char(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `orderid` bigint unsigned NOT NULL,
  `destinationType` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称或公司名称。',
  `weight` decimal(20,6) DEFAULT NULL,
  `volume` decimal(20,6) DEFAULT NULL,
  `calweight` decimal(20,6) DEFAULT NULL,
  `num` int DEFAULT NULL,
  `shipnum` int DEFAULT NULL,
  `shipmentList` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`orderid`,`destinationType`,`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_shipment_supplier_transchannel 结构
CREATE TABLE IF NOT EXISTS `t_shipment_supplier_transchannel` (
  `id` bigint unsigned NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `supplierid` bigint unsigned DEFAULT NULL,
  `channelid` bigint unsigned DEFAULT NULL,
  `country` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `disable` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name_shopid` (`supplierid`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_shipment_transchannel 结构
CREATE TABLE IF NOT EXISTS `t_shipment_transchannel` (
  `id` bigint unsigned NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `buyerid` bigint unsigned DEFAULT NULL,
  `country` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `disable` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name_shopid` (`buyerid`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_supplier_quotation_price 结构
CREATE TABLE IF NOT EXISTS `t_supplier_quotation_price` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `orderid` bigint unsigned NOT NULL,
  `confirm` bit(1) NOT NULL DEFAULT b'0',
  `base` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `destination` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipmentid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `supplierid` bigint unsigned DEFAULT NULL,
  `weight` decimal(20,6) DEFAULT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `unitprice` decimal(20,6) DEFAULT NULL,
  `tax` decimal(20,6) DEFAULT NULL,
  `otherfee` decimal(20,6) DEFAULT NULL,
  `shipfee` decimal(20,6) DEFAULT NULL,
  `totalfee` decimal(20,6) DEFAULT NULL,
  `pricetime` datetime DEFAULT NULL,
  `remark` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_key` (`orderid`,`supplierid`,`shipmentid`,`destination`),
  KEY `orderid_shipmentid_supplierid` (`orderid`,`supplierid`) USING BTREE,
  KEY `shipmentid` (`shipmentid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1905544624842268491 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_supply_relationship 结构
CREATE TABLE IF NOT EXISTS `t_supply_relationship` (
  `buyerid` bigint unsigned NOT NULL,
  `supplierid` bigint unsigned NOT NULL,
  PRIMARY KEY (`buyerid`,`supplierid`),
  KEY `supplierid` (`supplierid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_user_buyer 结构
CREATE TABLE IF NOT EXISTS `t_user_buyer` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `company` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `mobile` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `token` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tokentime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_quote.t_user_supplier 结构
CREATE TABLE IF NOT EXISTS `t_user_supplier` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `company` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `mobile` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `token` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tokentime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

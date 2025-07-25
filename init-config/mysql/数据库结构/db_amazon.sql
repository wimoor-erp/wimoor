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

-- 导出  事件 db_amazon.autoRefreshInsert 结构
DELIMITER //
CREATE EVENT `autoRefreshInsert` ON SCHEDULE EVERY 1 DAY STARTS '2025-04-08 17:47:39' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
INSERT ignore into t_amz_product_refresh 
		SELECT i.id,i.amazonauthid,
		str_to_date('1949-10-01 00:00:00', '%Y-%m-%d %H:%i:%s'),
		str_to_date('1949-10-01 00:00:00', '%Y-%m-%d %H:%i:%s'),
		str_to_date('1949-10-01 00:00:00', '%Y-%m-%d %H:%i:%s'),
		i.sku,i.asin,i.marketplaceid,i.isparent,i.invalid
		FROM t_product_info i
		LEFT JOIN t_amz_product_refresh r ON r.pid=i.id
		LEFT JOIN t_amazon_auth a ON a.id=i.amazonauthid
		WHERE a.`disable`=0 
		AND i.invalid=0  
		AND a.createtime>DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY)
		AND r.pid IS NULL ;
		

delete from t_report_requestrecord WHERE reportType IN(
'GET_FBA_FULFILLMENT_LONGTERM_STORAGE_FEE_CHARGES_DATA',
'GET_FBA_FULFILLMENT_INVENTORY_HEALTH_DATA',
'GET_FBA_INVENTORY_PLANNING_DATA',
'GET_FBA_REIMBURSEMENTS_DATA',
'GET_FBA_STORAGE_FEE_CHARGES_DATA'
)
 AND  lastupdate<DATE_SUB(NOW(),INTERVAL 30 DAY);

delete from t_report_requestrecord WHERE reportType='GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE_V2' AND  lastupdate<DATE_SUB(NOW(),INTERVAL 365 DAY);

delete from t_report_requestrecord WHERE reportType NOT  IN(
'GET_FBA_FULFILLMENT_LONGTERM_STORAGE_FEE_CHARGES_DATA',
'GET_FBA_FULFILLMENT_INVENTORY_HEALTH_DATA',
'GET_FBA_INVENTORY_PLANNING_DATA',
'GET_FBA_REIMBURSEMENTS_DATA',
'GET_FBA_STORAGE_FEE_CHARGES_DATA',
'GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE_V2'
)
 AND  lastupdate<DATE_SUB(NOW(),INTERVAL 3 DAY);
 

update t_product_info  i
LEFT JOIN t_amazon_auth a ON a.id=i.amazonAuthId
LEFT JOIN t_product_in_opt o ON o.pid=i.id
LEFT JOIN db_erp.t_erp_material m ON m.sku=IFNULL(o.msku,i.sku) AND m.shopid=a.shop_id AND m.isDelete=0
SET o.`owner`=m.`owner`
WHERE o.`owner` IS NULL AND m.`owner` IS NOT NULL ;


INSERT INTO  `t_product_in_opt` (	`pid` ,	`msku`,	`owner`)
SELECT i.id,m.sku,m.`owner`
FROM t_product_info i
LEFT JOIN t_amazon_auth a ON a.id=i.amazonAuthId
LEFT JOIN t_product_in_opt o ON o.pid=i.id
LEFT JOIN db_erp.t_erp_material m ON m.sku=i.sku AND m.shopid=a.shop_id AND m.isdelete=0
WHERE o.pid IS NULL
AND m.id IS NOT NULL
AND m.owner IS NOT NULL	;


END//
DELIMITER ;

-- 导出  事件 db_amazon.opt_owner 结构
DELIMITER //
CREATE EVENT `opt_owner` ON SCHEDULE EVERY 1 DAY STARTS '2025-04-08 17:49:39' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN

		
UPDATE t_erp_ship_inbounditem t
LEFT JOIN t_erp_ship_inboundshipment s ON s.ShipmentId=t.ShipmentId
LEFT join t_erp_ship_v2_inboundshipment v ON v.shipment_confirmation_id=t.ShipmentId 
LEFT JOIN t_erp_ship_v2_inboundplan p ON p.id=v.formid
LEFT JOIN t_erp_ship_v2_inboundshipment_item i ON i.shipmentid=v.shipmentid AND i.sku=t.SellerSKU
LEFT JOIN db_erp.t_erp_material m ON m.sku=i.msku AND m.isDelete=0 AND m.shopid=p.shopid 
SET t.unittransfee=i.unittransfee,t.unitcost=i.unitcost,t.totalcost=i.totalcost,t.totaltransfee=i.totaltransfee,t.msku=i.msku,t.materialid=m.id ,
i.received=t.QuantityReceived,i.receiveddate=t.ReceivedDate,t.Quantity=i.quantity
WHERE p.opttime> date_format(CURRENT_DATE()-90,'%Y-%m-%d')  AND v.shipmentid IS NOT NULL;	
	
UPDATE  t_erp_ship_inboundshipment s  
LEFT join t_erp_ship_v2_inboundshipment v ON v.shipment_confirmation_id=s.ShipmentId 
SET  s.ShipmentStatus=v.shipmentstatus,s.`status`=case when v.shipmentstatus='CANCEL' then 0 ELSE s.`status` end 
WHERE v.shipment_confirmation_id=s.ShipmentId AND s.ShipmentStatus!=v.shipmentstatus AND v.opttime>DATE_SUB(NOW(),interval 1 DAY);

UPDATE  t_erp_ship_inboundshipment s  
LEFT join t_erp_ship_v2_inboundshipment v ON v.shipment_confirmation_id=s.ShipmentId 
LEFT JOIN t_erp_ship_inboundplan p ON p.id=s.inboundplanid
LEFT join t_erp_ship_v2_inboundplan p2 ON p2.id=v.formid
SET  p.warehouseid=p2.warehouseid
WHERE v.shipment_confirmation_id=s.ShipmentId AND  p.warehouseid IS NULL    AND v.opttime>DATE_SUB(NOW(),INTERVAL 90 DAY);

UPDATE  t_erp_ship_inboundshipment s  
LEFT join t_erp_ship_v2_inboundshipment v ON v.shipment_confirmation_id=s.ShipmentId 
LEFT JOIN t_erp_ship_inboundplan p ON p.id=s.inboundplanid
LEFT join t_erp_ship_v2_inboundplan p2 ON p2.id=v.formid
SET  v.start_receive_date=s.start_receive_date
WHERE v.shipment_confirmation_id=s.ShipmentId AND v.start_receive_date IS NULL   AND v.opttime>DATE_SUB(NOW(),INTERVAL 90 DAY);
	
INSERT ignore INTO  t_erp_ship_inboundbox
SELECT UUID_SHORT(),s.shipment_confirmation_id,b.boxnum,b.length,b.width,b.height,b.unit,b.weight,b.wunit,b.tracking_id,b.package_status,b.opttime,b.operator
from t_erp_ship_v2_inboundshipment s
LEFT JOIN t_erp_ship_v2_inboundshipment_box b ON b.shipmentid=s.shipmentid
LEFT JOIN t_erp_ship_inboundbox bx ON bx.shipmentid=s.shipment_confirmation_id
WHERE b.boxnum IS NOT NULL  AND s.opttime>DATE_SUB(NOW(),INTERVAL 3 DAY)
AND bx.id IS null
;

UPDATE  t_erp_ship_v2_inboundshipment s
LEFT JOIN t_erp_ship_inboundshipment p ON p.ShipmentId=s.shipment_confirmation_id
SET s.shipmentstatus=p.ShipmentStatus ,s.`status`=8
WHERE p.ShipmentStatus='CLOSED' AND p.ShipmentStatus!=s.shipmentstatus;


UPDATE  t_erp_ship_v2_inboundshipment s
LEFT JOIN t_erp_ship_inboundshipment p ON p.ShipmentId=s.shipment_confirmation_id
SET p.shipmentstatus=s.ShipmentStatus ,s.`status`=0 ,p.`status`=0
WHERE s.shipmentstatus='CANCELLED' AND (p.ShipmentStatus!='CANCELLED'  OR s.`status`!=0 OR p.`status`!=0)
AND s.opttime>DATE_SUB(NOW(),INTERVAL  12 MONTH) AND p.ShipmentId=s.shipment_confirmation_id;

UPDATE  t_erp_ship_inboundshipment s  
LEFT join t_erp_ship_v2_inboundshipment v ON v.shipment_confirmation_id=s.ShipmentId 
SET  s.boxnum=(SELECT COUNT(0) FROM t_erp_ship_v2_inboundshipment_box b WHERE  b.shipmentid=v.shipmentid) 
WHERE v.shipment_confirmation_id=s.ShipmentId AND s.boxnum IS NULL     AND v.opttime>DATE_SUB(NOW(),INTERVAL 30 DAY);

delete from t_holiday;
INSERT INTO t_holiday
SELECT id,shopid,	`day`,week_name, `type`,DAYOFWEEK(`day`) 	`week`,creator create_by,createtime create_time,
operator update_by,opttime update_time FROM (
SELECT id,shopid,concat(YEAR,case when MONTH>9 then '-' ELSE '-0' end, MONTH,
case when day>9 then '-' ELSE '-0' END ,DAY) AS `day`,week_name,
1 `type` ,creator,createtime,ifnull(operator,creator) operator,opttime from db_admin.t_sys_local_holiday
WHERE TYPE=1 OR TYPE=2) v;
END//
DELIMITER ;

-- 导出  表 db_amazon.t_amazonseller_market 结构
CREATE TABLE IF NOT EXISTS `t_amazonseller_market` (
  `sellerid` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '卖家Sellerid',
  `marketplace_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '站点ID',
  `country` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '国家编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '站点英文名称',
  `language` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '对应语言编码',
  `currency` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '对应币种',
  `domain` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '对应域名',
  `amazonauthid` bigint unsigned DEFAULT NULL COMMENT '授权对应ID等同于Sellerid',
  `opttime` datetime(6) DEFAULT NULL COMMENT '操作时间',
  `disable` bit(1) DEFAULT b'0' COMMENT '操作人',
  PRIMARY KEY (`sellerid`,`marketplace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amazon_auth 结构
CREATE TABLE IF NOT EXISTS `t_amazon_auth` (
  `id` bigint unsigned NOT NULL,
  `shop_id` bigint unsigned DEFAULT NULL COMMENT '用户ID',
  `sellerid` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '卖家ID',
  `groupid` bigint unsigned DEFAULT NULL,
  `region` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `MWSAuthToken` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '卖家授权码',
  `disable` bit(1) DEFAULT b'0',
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pictureId` bigint unsigned DEFAULT NULL,
  `status` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `statusupdate` datetime DEFAULT NULL,
  `productdate` datetime DEFAULT NULL,
  `refreshinvtime` datetime DEFAULT NULL,
  `refresh_token` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refresh_token_time` datetime DEFAULT NULL,
  `aws_region` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `access_key_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `secret_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `role_arn` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `client_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `client_secret` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 3` (`sellerid`),
  KEY `shop_id` (`shop_id`),
  KEY `Index_id_shopid` (`groupid`),
  KEY `disable` (`disable`),
  KEY `region` (`region`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='亚马逊账号授权';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amazon_auth_market_performance 结构
CREATE TABLE IF NOT EXISTS `t_amazon_auth_market_performance` (
  `amazonauthid` bigint unsigned NOT NULL,
  `marketplaceid` char(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `accountstatus` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sellerid` char(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `performance` varchar(6000) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `refreshtime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amazon_declare_rate 结构
CREATE TABLE IF NOT EXISTS `t_amazon_declare_rate` (
  `shopid` bigint unsigned NOT NULL,
  `rate` float unsigned DEFAULT NULL,
  PRIMARY KEY (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='申报金额比例';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amazon_group 结构
CREATE TABLE IF NOT EXISTS `t_amazon_group` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `profitcfgid` bigint unsigned DEFAULT NULL COMMENT '店铺默认利润方案',
  `findex` int unsigned DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`shopid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_adv_browsenode 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_browsenode` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `parentid` bigint unsigned DEFAULT NULL,
  `country` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `level` int DEFAULT NULL,
  `is_category_root` bit(1) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`parentid`),
  KEY `Index 3` (`country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_api_timelimit 结构
CREATE TABLE IF NOT EXISTS `t_amz_api_timelimit` (
  `id` bigint unsigned NOT NULL,
  `apiname` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `maximum` int DEFAULT NULL COMMENT '一次性最多访问次数',
  `quarter` int DEFAULT NULL COMMENT '一刻钟可以访问多少次',
  `half` int DEFAULT NULL COMMENT '半小时可以访问多少次',
  `hour` int DEFAULT NULL COMMENT '一小时可以访问多少次',
  `restore_rate` int DEFAULT NULL COMMENT '每个单位时长恢复多少个',
  `restore_unit` int DEFAULT NULL COMMENT '多少秒钟可以恢复一个单位值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `apiname` (`apiname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_api_timelimit_seller_request 结构
CREATE TABLE IF NOT EXISTS `t_amz_api_timelimit_seller_request` (
  `id` bigint unsigned NOT NULL,
  `sellerid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `apiname` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `lastuptime` timestamp NULL DEFAULT NULL,
  `nexttoken` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `pages` int DEFAULT '1',
  `restore_num` int DEFAULT NULL,
  `isnewtoken` bit(1) DEFAULT NULL,
  `log` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 3` (`nexttoken`(191)),
  KEY `sellerid_apiname` (`sellerid`,`apiname`,`lastuptime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_auth_api_timelimit 结构
CREATE TABLE IF NOT EXISTS `t_amz_auth_api_timelimit` (
  `id` bigint unsigned NOT NULL COMMENT 'ID用于单独表示一行',
  `amazonauthid` bigint unsigned NOT NULL COMMENT '授权ID，等同于SellerId',
  `apiname` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'API的名字',
  `nexttoken` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '是否有nexttoken',
  `start_time` timestamp NULL DEFAULT NULL COMMENT 'API调用的开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT 'API调用的结束时间',
  `pages` int DEFAULT '1' COMMENT '本次调用的页数',
  `restore` double DEFAULT NULL COMMENT '下一次的恢复时常',
  `lastuptime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  `log` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '异常log',
  PRIMARY KEY (`amazonauthid`,`apiname`),
  UNIQUE KEY `key` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='新版本SPI-API使用';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_coupon_asin 结构
CREATE TABLE IF NOT EXISTS `t_amz_coupon_asin` (
  `couponid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `asin` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`couponid`,`asin`),
  KEY `asin` (`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_coupon_performance_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_coupon_performance_report` (
  `couponid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `merchantid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `currency` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `websit_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `start_date_time` datetime DEFAULT NULL,
  `end_date_time` datetime DEFAULT NULL,
  `discount_type` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `discount_amount` decimal(20,6) DEFAULT NULL,
  `total_discount` decimal(20,6) DEFAULT NULL,
  `clips` int DEFAULT NULL,
  `redemptions` int DEFAULT NULL,
  `budget` decimal(20,6) DEFAULT NULL,
  `budget_spend` decimal(20,6) DEFAULT NULL,
  `budget_remaining` decimal(20,6) DEFAULT NULL,
  `budget_percentage_used` decimal(20,6) DEFAULT NULL,
  `sales` decimal(20,6) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`couponid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_dimensions 结构
CREATE TABLE IF NOT EXISTS `t_amz_dimensions` (
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

-- 导出  表 db_amazon.t_amz_epr_monthly_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_epr_monthly_report` (
  `sellerid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `report_period_start` date NOT NULL,
  `report_period_end` date NOT NULL,
  `asin` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazon_marketplace` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_to_country` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_to_country_code` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `registration_number` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `epr_category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `epr_subcategory1` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `epr_subcategory2` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `epr_subcategory3` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `epr_subcategory4` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `gl_product_group_description` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `product_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `total_units_sold` int DEFAULT NULL,
  `units_per_asin` int DEFAULT NULL,
  `battery_embedded` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_weight_without_package_kg` decimal(10,2) DEFAULT NULL,
  `item_weight_with_package_kg` decimal(10,2) DEFAULT NULL,
  `total_reported_weight_kg` decimal(10,2) DEFAULT NULL,
  `item_width_cm` decimal(10,2) DEFAULT NULL,
  `package_width_cm` decimal(10,2) DEFAULT NULL,
  `item_height_cm` decimal(10,2) DEFAULT NULL,
  `package_height_cm` decimal(10,2) DEFAULT NULL,
  `paper_kg` decimal(10,2) DEFAULT NULL,
  `glass_kg` decimal(10,2) DEFAULT NULL,
  `aluminum_kg` decimal(10,2) DEFAULT NULL,
  `steel_kg` decimal(10,2) DEFAULT NULL,
  `plastic_kg` decimal(10,2) DEFAULT NULL,
  `wood_kg` decimal(10,2) DEFAULT NULL,
  `other_kg` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`sellerid`,`report_period_start`,`report_period_end`,`asin`,`ship_to_country_code`,`epr_category`),
  KEY `sellerid_report_period_start_report_period_end_epr_category` (`sellerid`,`report_period_start`,`report_period_end`,`epr_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_fbm_returns_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_fbm_returns_report` (
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `return_date` datetime NOT NULL,
  `purchase_date` datetime NOT NULL,
  `order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sellerid` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplaceid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazon_rma_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_rma_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `label_type` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `label_cost` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `currency` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `carrier` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `trackingid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `labelpaidby` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `azclaim` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isprime` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `inpolicy` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `returntype` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `resolution` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `invoice_number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `return_delivery_date` datetime DEFAULT NULL,
  `order_amount` decimal(20,6) DEFAULT NULL,
  `order_quantity` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `amount` decimal(20,6) DEFAULT NULL,
  `asin` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `safet_action_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `safet_claim_id` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `safet_claim_state` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `safet_claim_creation_time` datetime DEFAULT NULL,
  `safet_claim_reimbursement_amount` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`sellerid`,`sku`,`order_id`),
  KEY `sku` (`sku`,`return_date`),
  KEY `order_id` (`order_id`),
  KEY `return_date` (`return_date`),
  KEY `return_date_sellerid_marketplaceid` (`sellerid`,`marketplaceid`,`return_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_fin_account 结构
CREATE TABLE IF NOT EXISTS `t_amz_fin_account` (
  `amazonAuthid` bigint unsigned NOT NULL,
  `groupid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `financial_event_group_start` datetime DEFAULT NULL,
  `financial_event_group_end` datetime DEFAULT NULL,
  `fund_transfer_date` datetime DEFAULT NULL,
  `processing_status` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `trace_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `account_tail` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `converted_total` decimal(15,2) DEFAULT NULL,
  `beginning_balance` decimal(15,2) DEFAULT NULL,
  `original_total` decimal(15,2) DEFAULT NULL,
  `fund_transfer_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `nexttoken` varchar(1800) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`groupid`,`amazonAuthid`),
  KEY `amazonAuthid` (`amazonAuthid`,`currency`,`financial_event_group_start`,`financial_event_group_end`,`original_total`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_fin_settlement_formula 结构
CREATE TABLE IF NOT EXISTS `t_amz_fin_settlement_formula` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `formula` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `field` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `pricetype` int DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_fin_user_item 结构
CREATE TABLE IF NOT EXISTS `t_amz_fin_user_item` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `name` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `isused` bit(1) DEFAULT b'0',
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_fin_user_item_data 结构
CREATE TABLE IF NOT EXISTS `t_amz_fin_user_item_data` (
  `id` bigint unsigned NOT NULL,
  `itemid` bigint unsigned DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'sku=* 号时，是指店铺费用',
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` decimal(14,2) unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `itemid_groupid_marketplaceid_byday_shopid` (`shopid`,`groupid`,`marketplaceid`,`sku`,`byday`,`itemid`,`currency`),
  KEY `byday` (`byday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='客户导入的SKU财务项费用-应用于商品营收其他费用项目导入';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_follow_offer 结构
CREATE TABLE IF NOT EXISTS `t_amz_follow_offer` (
  `sellerid` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` tinyblob,
  `positive_feedback_rating` int DEFAULT NULL,
  `feedback_count` int DEFAULT NULL,
  `refreshtime` timestamp NULL DEFAULT NULL,
  `refreshnum` int DEFAULT '0',
  PRIMARY KEY (`sellerid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_follow_offerchange 结构
CREATE TABLE IF NOT EXISTS `t_amz_follow_offerchange` (
  `id` bigint unsigned NOT NULL,
  `asin` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sellerid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `listing_price_amount` decimal(18,2) DEFAULT NULL,
  `shiping_amount` decimal(18,2) DEFAULT NULL,
  `currency` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `is_fulfilled_by_amazon` bit(1) DEFAULT NULL,
  `is_buy_box_winner` bit(1) DEFAULT NULL,
  `is_featured_merchant` bit(1) DEFAULT NULL,
  `is_prime` bit(1) DEFAULT NULL,
  `is_national_prime` bit(1) DEFAULT NULL,
  `ships_domestically` bit(1) DEFAULT NULL,
  `minimum_hours` int DEFAULT NULL,
  `maximum_hours` int DEFAULT NULL,
  `availability_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `available_date` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sub_condition` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ships_from_state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ships_from_country` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `findtime` timestamp NULL DEFAULT NULL,
  `losttime` timestamp NULL DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `asin_marketplaceid_sellerid` (`asin`,`marketplaceid`,`sellerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_fulfillment_order 结构
CREATE TABLE IF NOT EXISTS `t_amz_fulfillment_order` (
  `sellerFulfillmentOrderId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplaceId` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `displayableOrderId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `displayableOrderDate` datetime DEFAULT NULL,
  `displayableOrderComment` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shippingSpeedCategory` int DEFAULT NULL,
  `deliveryStart` datetime DEFAULT NULL,
  `deliveryEnd` datetime DEFAULT NULL,
  `destinationAddress` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillmentAction` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillmentPolicy` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `receivedDate` datetime DEFAULT NULL,
  `fulfillmentOrderStatus` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `statusUpdatedDate` datetime DEFAULT NULL,
  `notificationEmails` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `featureConstraints` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`sellerFulfillmentOrderId`),
  KEY `refreshtime` (`refreshtime`),
  KEY `amazonauthid` (`amazonauthid`),
  KEY `fulfillmentOrderStatus` (`fulfillmentOrderStatus`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_fulfillment_order_items 结构
CREATE TABLE IF NOT EXISTS `t_amz_fulfillment_order_items` (
  `sellerFulfillmentOrderId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sellerFulfillmentOrderItemId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sellerSku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `quantity` int DEFAULT NULL,
  `giftMessage` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `displayableComment` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillmentNetworkSku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `orderItemDisposition` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `cancelledQuantity` int DEFAULT NULL,
  `unfulfillableQuantity` int DEFAULT NULL,
  `estimatedShipDate` datetime DEFAULT NULL,
  `estimatedArrivalDate` datetime DEFAULT NULL,
  `perUnitPrice` decimal(20,6) DEFAULT NULL,
  `perUnitTax` decimal(20,6) DEFAULT NULL,
  `perUnitDeclaredValue` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`sellerFulfillmentOrderId`,`sellerFulfillmentOrderItemId`),
  KEY `sellerSku` (`sellerSku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_fulfillment_order_payment 结构
CREATE TABLE IF NOT EXISTS `t_amz_fulfillment_order_payment` (
  `sellerFulfillmentOrderId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `paymentTransactionId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `paymentMode` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `paymentDate` datetime DEFAULT NULL,
  PRIMARY KEY (`sellerFulfillmentOrderId`,`paymentTransactionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_fulfillment_order_return_authorization 结构
CREATE TABLE IF NOT EXISTS `t_amz_fulfillment_order_return_authorization` (
  `sellerFulfillmentOrderId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `returnAuthorizationId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fulfillmentCenterId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `returnToAddress` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonRmaId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `rmaPageURL` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`sellerFulfillmentOrderId`,`returnAuthorizationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_fulfillment_order_return_item 结构
CREATE TABLE IF NOT EXISTS `t_amz_fulfillment_order_return_item` (
  `sellerReturnItemId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sellerFulfillmentOrderItemId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazonShipmentId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sellerReturnReasonCode` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `returnComment` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonReturnReasonCode` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `statusChangedDate` datetime DEFAULT NULL,
  `returnAuthorizationId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '',
  `returnReceivedCondition` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '',
  `fulfillmentCenterId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '',
  PRIMARY KEY (`sellerReturnItemId`,`sellerFulfillmentOrderItemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_fulfillment_order_shipment 结构
CREATE TABLE IF NOT EXISTS `t_amz_fulfillment_order_shipment` (
  `sellerFulfillmentOrderId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazonShipmentId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fulfillmentCenterId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillmentShipmentStatus` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shippingDate` datetime DEFAULT NULL,
  `estimatedArrivalDate` datetime DEFAULT NULL,
  `shippingNotes` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`sellerFulfillmentOrderId`,`amazonShipmentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_inbound_fba_cycle 结构
CREATE TABLE IF NOT EXISTS `t_amz_inbound_fba_cycle` (
  `id` bigint unsigned NOT NULL COMMENT 'ID',
  `shopid` bigint unsigned NOT NULL COMMENT '店铺',
  `transtype` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `stocking_cycle` int DEFAULT '0' COMMENT '安全库存周期',
  `min_cycle` int DEFAULT '0' COMMENT '发货频率',
  `put_on_days` int DEFAULT '0' COMMENT '上架周期',
  `first_leg_days` int DEFAULT '0' COMMENT '头程周期',
  `isdefault` bit(1) NOT NULL DEFAULT b'0',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `shopid_marketplaceid` (`shopid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_inventory_health 结构
CREATE TABLE IF NOT EXISTS `t_amz_inventory_health` (
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '站点ID',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'SKU区分大小写',
  `authid` bigint unsigned NOT NULL COMMENT '授权ID等价sellerid',
  `snapshot_date` datetime DEFAULT NULL COMMENT '报表更新时间',
  `fnsku` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '仓库SKUID',
  `asin` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'ASIN产品销售ID',
  `name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `fcondition` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '是否新旧',
  `sales_rank` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '销售排名',
  `product_group` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品分组',
  `total_quantity` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '当前总库存',
  `sellable_quantity` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '可销售数量',
  `unsellable_quantity` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '不可售数量',
  `inv_age_0to90days` int DEFAULT NULL COMMENT '90天以内库龄数量',
  `inv_age_91to180days` int DEFAULT NULL COMMENT '91-180天以内库龄数量',
  `inv_age_181to270days` int DEFAULT NULL COMMENT '181-270天以内库龄数量',
  `inv_age_271to365days` int DEFAULT NULL COMMENT '271-365天以内库龄数量',
  `inv_age_365plusdays` int DEFAULT NULL COMMENT '365天以上库龄数量',
  `units_shipped_last24hrs` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '最近24小时发货',
  `units_shipped_last7days` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '最近7天发货',
  `units_shipped_last30days` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '最近30天发货',
  `units_shipped_last90days` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '最近90天发货',
  `units_shipped_last180days` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '最近180天发货',
  `units_shipped_last365days` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '最近365天发货',
  `weeks_of_cover_t7` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '7天周转',
  `weeks_of_cover_t30` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '30天周转',
  `weeks_of_cover_t90` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '90天周转',
  `weeks_of_cover_t180` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '180天周转',
  `weeks_of_cover_t365` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '360天周转',
  `num_afn_new_sellers` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '新产品的卖家数量',
  `num_afn_used_sellers` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '旧产品的卖家数量',
  `currency` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '币种',
  `your_price` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '你的售价',
  `sales_price` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '售价',
  `lowest_afn_new_price` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '新产品最低售价',
  `lowest_afn_used_price` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '旧产品最低售价',
  `lowest_mfn_new_price` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '新产品自发货最低售价',
  `lowest_mfn_used_price` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '旧产品自发货最低售价',
  `qty_to_be_charged_ltsf_12mo` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '超长期仓储费：超过365天的仓库费',
  `qty_in_long_term_storage_program` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '长期仓库数量',
  `qty_with_removals_in_progress` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '正在移除的数量',
  `projected_ltsf_12_mo` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '下期超长期仓储费',
  `per_unit_volume` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '库容',
  `is_hazmat` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '是否危险品',
  `in_bound_quantity` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '待入库数量',
  `asin_limit` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品限制',
  `inbound_recommend_quantity` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '建议待入库数量（即发货量）',
  `qty_to_be_charged_ltsf_6mo` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '长期仓储费',
  `projected_ltsf_6mo` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '下期长期仓储费',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`authid`,`marketplaceid`,`sku`),
  KEY `index1` (`shopid`,`authid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_inventory_planning 结构
CREATE TABLE IF NOT EXISTS `t_amz_inventory_planning` (
  `snapshot_date` date DEFAULT NULL COMMENT '快照时间',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '平台产品SKU',
  `amazonauthid` bigint unsigned NOT NULL COMMENT '授权ID',
  `countrycode` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '国家编码',
  `fnsku` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '仓库编码（产品条码）',
  `asin` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商品编码',
  `condition` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商品状况',
  `available` int DEFAULT NULL COMMENT '可用库存数量',
  `pending_removal_quantity` int DEFAULT NULL COMMENT '等待移除的库存',
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '币种',
  `inv_age_0_to_90_days` int DEFAULT NULL COMMENT '90天以内库龄数量',
  `inv_age_91_to_180_days` int DEFAULT NULL COMMENT '91-180天以内库龄数量',
  `inv_age_181_to_270_days` int DEFAULT NULL COMMENT '181-270天以内库龄数量',
  `inv_age_271_to_365_days` int DEFAULT NULL COMMENT '271-365天以内库龄数量',
  `inv_age_365_plus_days` int DEFAULT NULL COMMENT '365天以上库龄数量',
  `qty_to_be_charged_ltsf_11_mo` decimal(20,6) DEFAULT NULL COMMENT '长期仓储费：超过11个月的仓库费',
  `projected_ltsf_11_mo` decimal(20,6) DEFAULT NULL COMMENT '预估下期仓储费：超过11个月的',
  `qty_to_be_charged_ltsf_12_mo` decimal(20,6) DEFAULT NULL COMMENT '长期仓储费：超过12个月的仓库费',
  `estimated_ltsf_next_charge` decimal(20,6) DEFAULT NULL COMMENT '预估下期长期仓储费',
  `units_shipped_t7` int DEFAULT NULL COMMENT '最近7天发货',
  `units_shipped_t30` int DEFAULT NULL COMMENT '最近30天发货',
  `units_shipped_t60` int DEFAULT NULL COMMENT '最近60天发货',
  `units_shipped_t90` int DEFAULT NULL COMMENT '最近90天发货',
  `alert` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '低流量或低转化率提醒',
  `your_price` decimal(20,6) DEFAULT NULL COMMENT '您发布的商品价格',
  `sales_price` decimal(20,6) DEFAULT NULL COMMENT '您促销商品的价格',
  `lowest_price_new_plus_shipping` decimal(20,6) DEFAULT NULL COMMENT '此商品的新品在亚马逊商城的最低价格（含运费）',
  `lowest_price_used` decimal(20,6) DEFAULT NULL COMMENT '此商品的二手商品在亚马逊商城的最低价格（含运费）',
  `recommended_action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '建议您对库存执行的操作。建议的依据是您当前的买家需求以及采取措施是否比不采取任何措施花费更低',
  `healthy_inventory_level` int DEFAULT NULL COMMENT '根据商品需求和您的成本，被认定为不是冗余库存的库存数量。我们的建议是为了帮助您达到这一库存水平。',
  `recommended_sales_price` decimal(20,6) DEFAULT NULL COMMENT '建议售价',
  `recommended_sale_duration_days` int DEFAULT NULL COMMENT '建议售价的天数',
  `recommended_removal_quantity` int DEFAULT NULL COMMENT '建议移除数量',
  `estimated_cost_savings_of_recommended_actions` decimal(20,6) DEFAULT NULL COMMENT '可以节约成本:与不采取任何措施（需要为相应库存支付仓储费）相比，采取建议措施预计可以节约的成本',
  `sell_through` decimal(20,6) DEFAULT NULL COMMENT '售罄率：较高的售罄率可能表明仍有库存机会，而较低的数值可能意味着你已经过度投资',
  `item_volume` decimal(20,6) DEFAULT NULL COMMENT '商品的体积',
  `volume_unit_measurement` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商品体积的计量单位',
  `storage_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '仓储类型分类：有六种仓储类型：标准尺寸、大件、服装、鞋靴、易燃物或气溶胶',
  `storage_volume` decimal(20,6) DEFAULT NULL COMMENT '存储体积',
  `product_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品分类',
  `sales_rank` int DEFAULT NULL COMMENT '销售排名',
  `days_of_supply` int DEFAULT NULL COMMENT '持续供货(天）',
  `estimated_excess_quantity` int DEFAULT NULL COMMENT '预计冗余商品数量：根据您当前的销售速度和买家需求预测得出的积压商品数量。保留这些商品并支付仓储费比通过降低或移除价格来降低商品数量的成本更高',
  `weeks_of_cover_t30` int DEFAULT NULL COMMENT '30天周转',
  `weeks_of_cover_t90` int DEFAULT NULL COMMENT '90天周转',
  `featuredoffer_price` decimal(20,6) DEFAULT NULL COMMENT '价格',
  `sales_shipped_last_7_days` decimal(20,6) DEFAULT NULL COMMENT '7天销售额',
  `sales_shipped_last_30_days` decimal(20,6) DEFAULT NULL COMMENT '30天销售额',
  `sales_shipped_last_60_days` decimal(20,6) DEFAULT NULL COMMENT '60天销售额',
  `sales_shipped_last_90_days` decimal(20,6) DEFAULT NULL COMMENT '90天销售额',
  `inv_age_0_to_30_days` int DEFAULT NULL COMMENT '30天以内库龄数量',
  `inv_age_31_to_60_days` int DEFAULT NULL COMMENT '31-60天以内库龄数量',
  `inv_age_61_to_90_days` int DEFAULT NULL COMMENT '61-90天以内库龄数量',
  `inv_age_181_to_330_days` int DEFAULT NULL COMMENT '181-330天以内库龄数量',
  `inv_age_331_to_365_days` int DEFAULT NULL COMMENT '331-365天以内库龄数量',
  `estimated_storage_cost_next_month` decimal(20,6) DEFAULT NULL COMMENT '预估下个月仓储费',
  `inbound_quantity` int DEFAULT NULL COMMENT '待入库总数',
  `inbound_working` int DEFAULT NULL COMMENT '待入库-发货中',
  `inbound_shipped` int DEFAULT NULL COMMENT '待入库-已发货',
  `inbound_received` int DEFAULT NULL COMMENT '待入库-接收中',
  `no_sale_last_6_months` int DEFAULT NULL COMMENT '6个月内没有售出的数量',
  `reserved_quantity` int DEFAULT NULL COMMENT '总的预留库存',
  `unfulfillable_quantity` int DEFAULT NULL COMMENT '不可售库存',
  `afn_researching_quantity` int DEFAULT NULL COMMENT '正在找回的库存',
  `afn_reserved_future_supply` int DEFAULT NULL COMMENT '预留库存（为有意向购买者保留）',
  `afn_future_supply_buyable` int DEFAULT NULL COMMENT '未来可售（即将到货的库存）',
  `quantity_to_be_charged_ais_181_210_days` int DEFAULT NULL,
  `quantity_to_be_charged_ais_211_240_days` int DEFAULT NULL,
  `quantity_to_be_charged_ais_241_270_days` int DEFAULT NULL,
  `quantity_to_be_charged_ais_271_300_days` int DEFAULT NULL,
  `quantity_to_be_charged_ais_301_330_days` int DEFAULT NULL,
  `quantity_to_be_charged_ais_331_365_days` int DEFAULT NULL,
  `quantity_to_be_charged_ais_365_plus_days` int DEFAULT NULL,
  `estimated_ais_181_210_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_211_240_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_241_270_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_271_300_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_301_330_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_331_365_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_365_plus_days` decimal(20,6) DEFAULT NULL,
  `fba_minimum_inventory_level` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '最低水位库存量',
  `historical_days_of_supply` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '历史供货天数',
  `fba_inventory_level_health_status` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '库存健康状态',
  PRIMARY KEY (`amazonauthid`,`countrycode`,`sku`,`condition`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_merchant_shipping_group 结构
CREATE TABLE IF NOT EXISTS `t_amz_merchant_shipping_group` (
  `id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazonauthid` bigint unsigned NOT NULL DEFAULT '0',
  `marketplaceid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`amazonauthid`),
  KEY `amazonauthid_marketplaceid_name` (`amazonauthid`,`marketplaceid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='自配送买家运费模板';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_notifications 结构
CREATE TABLE IF NOT EXISTS `t_amz_notifications` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `notifications` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isrun` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_notifications_destination 结构
CREATE TABLE IF NOT EXISTS `t_amz_notifications_destination` (
  `destinationid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '接受消息对象的ID',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '接受消息对象的名称',
  `resource_sqs_arn` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '消息队列arn',
  `resource_event_bridge_region` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '本地bridge区域',
  `resource_event_bridge_accountid` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '本地bridge账号',
  `resource_event_bridge_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `awsregion` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`destinationid`),
  KEY `name_amazonauthid` (`awsregion`,`name`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='亚马逊Destination 亚马逊消息接受对象';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_notifications_subscriptions 结构
CREATE TABLE IF NOT EXISTS `t_amz_notifications_subscriptions` (
  `subscriptionId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订阅ID',
  `amazonauthid` bigint unsigned NOT NULL COMMENT '授权ID',
  `eventFilterType` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订阅类型',
  `payloadVersion` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '版本',
  `destinationId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '接受信息的目标',
  `aggregationSettings` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '聚合时间',
  `marketplaceIds` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '站点',
  `refreshtime` datetime DEFAULT NULL COMMENT '刷新时间',
  PRIMARY KEY (`subscriptionId`,`amazonauthid`,`eventFilterType`),
  KEY `eventFilterType_destinationId_amazonauthid` (`amazonauthid`,`eventFilterType`,`destinationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='订阅消息对象';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_orders_address 结构
CREATE TABLE IF NOT EXISTS `t_amz_orders_address` (
  `amazon_order_id` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL,
  `marketplaceId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `phone` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address_line1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address_line2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address_line3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `city` char(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `country_code` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `county` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `district` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `municipality` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `postal_code` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state_or_region` char(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`),
  KEY `amazonAuthId_marketplaceId_name` (`amazonAuthId`,`marketplaceId`,`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_orders_invoice_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_orders_invoice_report` (
  `order_id` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `order_item_id` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `purchase_date` datetime NOT NULL,
  `payments_date` datetime DEFAULT NULL,
  `buyer_email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_phone_number` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity_purchased` int DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `ship_service_level` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `recipient_name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_city` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_state` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_postal_code` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_phone_number` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `delivery_start_date` datetime DEFAULT NULL,
  `delivery_end_date` datetime DEFAULT NULL,
  `delivery_time_zone` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `delivery_instructions` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `is_business_order` bit(1) DEFAULT NULL,
  `price_designation` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `is_iba` bit(1) DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `refreshtime` datetime NOT NULL,
  PRIMARY KEY (`order_id`,`order_item_id`),
  KEY `purchase_date_sku` (`sku`,`purchase_date`),
  KEY `purchase_date_sales_channel_amazonAuthId` (`amazonAuthId`,`sales_channel`,`purchase_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_order_buyer_ship_address 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_buyer_ship_address` (
  `id` bigint unsigned NOT NULL,
  `amazon_order_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `city` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `county` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '区县',
  `district` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `postalcode` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `countrycode` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `phone` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `amazonAuthId` (`amazonAuthId`,`marketplaceId`,`name`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_order_feedback 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_feedback` (
  `amazon_order_id` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `feeddate` datetime DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `comments` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `response` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_order_item 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_item` (
  `id` bigint unsigned NOT NULL,
  `amazon_order_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `orderItemId` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `QuantityOrdered` int DEFAULT NULL,
  `QuantityShipped` int DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `promotion_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `CODFee` decimal(10,2) DEFAULT NULL,
  `CODFeeDiscount` decimal(10,2) DEFAULT NULL,
  `GiftMessageText` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `GiftWrapLevel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ConditionId` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ConditionSubtypeId` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ConditionNote` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ScheduledDeliveryStartDate` datetime DEFAULT NULL,
  `ScheduledDeliveryEndDate` datetime DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`orderItemId`),
  KEY `sku` (`sku`),
  KEY `amazonAuthId_shopid_groupid` (`amazonAuthId`,`marketplaceId`,`purchase_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_order_item_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_item_archive` (
  `id` bigint unsigned NOT NULL,
  `amazon_order_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `orderItemId` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `QuantityOrdered` int DEFAULT NULL,
  `QuantityShipped` int DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `promotion_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `CODFee` decimal(10,2) DEFAULT NULL,
  `CODFeeDiscount` decimal(10,2) DEFAULT NULL,
  `GiftMessageText` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `GiftWrapLevel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ConditionId` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ConditionSubtypeId` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ConditionNote` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ScheduledDeliveryStartDate` datetime DEFAULT NULL,
  `ScheduledDeliveryEndDate` datetime DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`orderItemId`),
  KEY `sku` (`sku`),
  KEY `amazonAuthId_shopid_groupid` (`amazonAuthId`,`marketplaceId`,`purchase_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_order_main 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_main` (
  `id` bigint unsigned NOT NULL,
  `amazon_order_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `seller_order_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `replaced_orderid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_status` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillment_channel` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_channel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_service_level` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '货件服务水平',
  `buyer_shipping_address_id` bigint unsigned DEFAULT NULL COMMENT '买家收货地址id',
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_total` decimal(10,2) DEFAULT NULL COMMENT '订单的总费用',
  `numberOfItemsShipped` int DEFAULT NULL COMMENT '已配送的商品数量。',
  `numberOfItemsUnshipped` int DEFAULT NULL COMMENT '未配送的商品数量。',
  `paymentMethod` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'COD 订单的次级付款方式',
  `payment_execution_detail_item` decimal(10,0) DEFAULT NULL COMMENT '使用同级PaymentMethod响应元素指明的次级付款方式支付的金额',
  `buyer_email` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_name` tinyblob,
  `shipment_serviceLevel_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单的配送服务级别分类。',
  `fulfillment_supply_sourceid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `CbaDisplayableShippingLabel` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '卖家自定义的配送方式，属于Checkout by Amazon (CBA) 所支持的四种标准配送设置中的一种',
  `orderType` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `earliestShipDate` datetime DEFAULT NULL,
  `latestShipDate` datetime DEFAULT NULL,
  `earliestDeliveryDate` datetime DEFAULT NULL,
  `latestDeliveryDate` datetime DEFAULT NULL,
  `promise_response_duedate` datetime DEFAULT NULL,
  `isBusinessOrder` bit(1) DEFAULT b'0',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `hasItem` bit(1) DEFAULT b'0',
  `marketplaceId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  PRIMARY KEY (`amazon_order_id`,`amazonAuthId`),
  KEY `amazonauth` (`amazonAuthId`,`purchase_date`,`hasItem`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_order_main_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_main_archive` (
  `id` bigint unsigned NOT NULL,
  `amazon_order_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `seller_order_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `replaced_orderid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_status` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillment_channel` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_channel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_service_level` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '货件服务水平',
  `buyer_shipping_address_id` bigint unsigned DEFAULT NULL COMMENT '买家收货地址id',
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_total` decimal(10,2) DEFAULT NULL COMMENT '订单的总费用',
  `numberOfItemsShipped` int DEFAULT NULL COMMENT '已配送的商品数量。',
  `numberOfItemsUnshipped` int DEFAULT NULL COMMENT '未配送的商品数量。',
  `paymentMethod` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'COD 订单的次级付款方式',
  `payment_execution_detail_item` decimal(10,0) DEFAULT NULL COMMENT '使用同级PaymentMethod响应元素指明的次级付款方式支付的金额',
  `buyer_email` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_name` tinyblob,
  `shipment_serviceLevel_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单的配送服务级别分类。',
  `fulfillment_supply_sourceid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `CbaDisplayableShippingLabel` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '卖家自定义的配送方式，属于Checkout by Amazon (CBA) 所支持的四种标准配送设置中的一种',
  `orderType` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `earliestShipDate` datetime DEFAULT NULL,
  `latestShipDate` datetime DEFAULT NULL,
  `earliestDeliveryDate` datetime DEFAULT NULL,
  `latestDeliveryDate` datetime DEFAULT NULL,
  `promise_response_duedate` datetime DEFAULT NULL,
  `isBusinessOrder` bit(1) DEFAULT b'0',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `hasItem` bit(1) DEFAULT b'0',
  `marketplaceId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  PRIMARY KEY (`amazon_order_id`),
  KEY `amazonauth` (`amazonAuthId`,`purchase_date`,`hasItem`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_order_remove_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_remove_report` (
  `order_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_type` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_status` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fnsku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `disposition` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `requested_quantity` int DEFAULT NULL,
  `cancelled_quantity` int DEFAULT NULL,
  `disposed_quantity` int DEFAULT NULL,
  `shipped_quantity` int DEFAULT NULL,
  `in_process_quantity` int DEFAULT NULL,
  `removal_fee` decimal(10,2) DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`order_id`,`sku`),
  KEY `purchase_date` (`purchase_date`),
  KEY `sku` (`sku`),
  KEY `amazonAuthId_last_updated_date_currency` (`amazonAuthId`,`currency`,`last_updated_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_pdt_price_opt 结构
CREATE TABLE IF NOT EXISTS `t_amz_pdt_price_opt` (
  `pid` bigint unsigned NOT NULL DEFAULT '0',
  `feed_submission_id` bigint unsigned NOT NULL DEFAULT '0',
  `standardprice` decimal(10,4) DEFAULT NULL,
  `oldstandardprice` decimal(10,4) DEFAULT NULL,
  `saleprice` decimal(10,4) DEFAULT NULL,
  `businessprice` decimal(10,4) DEFAULT NULL,
  `businesstype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `businesslist` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`pid`,`feed_submission_id`),
  KEY `opttime` (`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_po_rpt_day 结构
CREATE TABLE IF NOT EXISTS `t_amz_po_rpt_day` (
  `amazonAuthId` bigint unsigned NOT NULL,
  `purchase_date` date NOT NULL,
  `sales_channel` char(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `order_status` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `quantity` int DEFAULT NULL,
  `ordersum` int DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `price` decimal(15,2) DEFAULT NULL,
  `pricermb` decimal(15,2) DEFAULT NULL,
  `ship_country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`sku`,`sales_channel`,`purchase_date`,`order_status`,`amazonAuthId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_product_active 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_active` (
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '站点',
  `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户码sku',
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '唯一码asin',
  `openDate` datetime DEFAULT NULL COMMENT '创建日期',
  `price` decimal(14,2) DEFAULT NULL COMMENT '价格',
  PRIMARY KEY (`amazonAuthId`,`marketplaceid`,`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_product_active_daynum 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_active_daynum` (
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceid` char(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `byday` date NOT NULL,
  `num` int DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`marketplaceid`,`byday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_product_lock 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_lock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `isused` bit(1) DEFAULT NULL COMMENT '是否可用',
  `num` int DEFAULT NULL COMMENT '询问次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_product_pageviews 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_pageviews` (
  `amazonAuthid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `SKU` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `byday` date NOT NULL,
  `child_asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `parent_asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `Sessions` int DEFAULT NULL,
  `Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Page_Views` int DEFAULT NULL,
  `Page_Views_Percentage` decimal(10,2) DEFAULT NULL,
  `Buy_Box_Percentage` decimal(10,2) DEFAULT NULL,
  `Units_Ordered` int DEFAULT NULL,
  `Units_Ordered_B2B` int DEFAULT NULL,
  `Unit_Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Unit_Session_Percentage_B2B` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales_B2B` decimal(10,2) DEFAULT NULL,
  `Total_Order_Items` int DEFAULT NULL,
  `Total_Order_Items_B2B` int DEFAULT NULL,
  PRIMARY KEY (`amazonAuthid`,`marketplaceid`,`byday`,`SKU`,`child_asin`),
  KEY `amazonAuthid_marketplaceid_byday` (`amazonAuthid`,`marketplaceid`,`byday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_product_pageviews_download 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_pageviews_download` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `SKU` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `byday` date NOT NULL,
  `parent_asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `child_asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `Sessions` int DEFAULT NULL COMMENT '访问量（点击量）',
  `Session_Percentage` decimal(10,2) DEFAULT NULL COMMENT '访问比例',
  `Page_Views` int DEFAULT NULL COMMENT '浏览量',
  `Page_Views_Percentage` decimal(10,2) DEFAULT NULL,
  `Buy_Box_Percentage` decimal(10,2) DEFAULT NULL,
  `Units_Ordered` int DEFAULT NULL COMMENT '销量',
  `Units_Ordered_B2B` int DEFAULT NULL,
  `Unit_Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Unit_Session_Percentage_B2B` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales_B2B` decimal(10,2) DEFAULT NULL,
  `Total_Order_Items` int DEFAULT NULL,
  `Total_Order_Items_B2B` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`amazonAuthid`,`marketplaceid`,`byday`,`SKU`,`parent_asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='流量报表';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_product_pageviews_month 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_pageviews_month` (
  `amazonAuthid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `SKU` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `byday` date NOT NULL,
  `parent_asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `child_asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `Sessions` int DEFAULT NULL,
  `Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Page_Views` int DEFAULT NULL,
  `Page_Views_Percentage` decimal(10,2) DEFAULT NULL,
  `Buy_Box_Percentage` decimal(10,2) DEFAULT NULL,
  `Units_Ordered` int DEFAULT NULL,
  `Units_Ordered_B2B` int DEFAULT NULL,
  `Unit_Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Unit_Session_Percentage_B2B` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales_B2B` decimal(10,2) DEFAULT NULL,
  `Total_Order_Items` int DEFAULT NULL,
  `Total_Order_Items_B2B` int DEFAULT NULL,
  PRIMARY KEY (`amazonAuthid`,`marketplaceid`,`SKU`,`byday`,`child_asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_product_pageviews_week 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_pageviews_week` (
  `amazonAuthid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `SKU` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `byday` date NOT NULL,
  `parent_asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `child_asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `Sessions` int DEFAULT NULL,
  `Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Page_Views` int DEFAULT NULL,
  `Page_Views_Percentage` decimal(10,2) DEFAULT NULL,
  `Buy_Box_Percentage` decimal(10,2) DEFAULT NULL,
  `Units_Ordered` int DEFAULT NULL,
  `Units_Ordered_B2B` int DEFAULT NULL,
  `Unit_Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Unit_Session_Percentage_B2B` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales_B2B` decimal(10,2) DEFAULT NULL,
  `Total_Order_Items` int DEFAULT NULL,
  `Total_Order_Items_B2B` int DEFAULT NULL,
  PRIMARY KEY (`amazonAuthid`,`marketplaceid`,`SKU`,`byday`,`child_asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_product_price_record 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_price_record` (
  `pid` bigint unsigned NOT NULL DEFAULT '0',
  `opttime` datetime NOT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `lowestprice` decimal(10,4) DEFAULT NULL,
  `price` decimal(10,4) DEFAULT NULL,
  `refprice` decimal(10,4) DEFAULT NULL,
  `oldprice` decimal(10,4) DEFAULT NULL,
  `shipprice` decimal(10,4) DEFAULT NULL,
  `oldshipprice` decimal(10,4) DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `ftype` tinyint DEFAULT '0' COMMENT '1:永久调价，2，临时调价，3，商务调价',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`pid`,`opttime`),
  KEY `opttime` (`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用于记录调价';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_product_refresh 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_refresh` (
  `pid` bigint unsigned NOT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `detail_refresh_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `price_refresh_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `catalog_refresh_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `asin` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `isparent` bit(1) DEFAULT b'0',
  `notfound` bit(1) DEFAULT b'0',
  PRIMARY KEY (`pid`),
  UNIQUE KEY `amazonauthid` (`amazonauthid`,`marketplaceid`,`sku`),
  KEY `ftime` (`amazonauthid`,`detail_refresh_time`),
  KEY `amazonauthid_catalog_refresh_time` (`catalog_refresh_time`,`asin`),
  KEY `amazonauthid_price_refresh_time_marketplaceid_isparent` (`amazonauthid`,`isparent`,`price_refresh_time`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_product_refreshtime 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_refreshtime` (
  `pid` bigint unsigned NOT NULL,
  `item` int unsigned NOT NULL COMMENT '0:GetCompetitivePricingForSKURequest;\\r\\n1:GetLowestPricedOffersForSKU;\\r\\n2:captureProductDetail;3:captureProductCategoriesBySku',
  `ftime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`pid`,`item`),
  KEY `ftime` (`ftime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_product_sales_plan 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_sales_plan` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `msku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `shipday` int DEFAULT NULL,
  `delivery_cycle` int DEFAULT NULL,
  `avgsales` int DEFAULT NULL,
  `needship` int DEFAULT NULL,
  `needshipfba` int DEFAULT NULL,
  `needpurchase` int DEFAULT NULL,
  `ship_min_cycle_sale` int DEFAULT NULL,
  `salesday` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `short_time` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku_marketplaceid_amazonauthid` (`amazonauthid`,`marketplaceid`,`sku`),
  KEY `shipday` (`shipday`),
  KEY `shopid_groupid` (`groupid`,`marketplaceid`,`sku`),
  KEY `marketplaceid_groupid_amazonauthid` (`groupid`,`marketplaceid`,`amazonauthid`,`sku`,`opttime`),
  KEY `msku_shopid` (`shopid`,`msku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_product_sales_plan_ship_item 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_sales_plan_ship_item` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `msku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `overseaid` bigint unsigned DEFAULT NULL,
  `transtype` bigint unsigned DEFAULT NULL,
  `batchnumber` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `aftersalesday` int DEFAULT NULL,
  `isdefault` bit(1) DEFAULT (1),
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_groupid` (`shopid`,`groupid`,`warehouseid`,`amazonauthid`,`marketplaceid`,`sku`,`overseaid`,`transtype`),
  KEY `shipday` (`amount`),
  KEY `groupid_batchnumber` (`batchnumber`),
  KEY `msku_shopid` (`shopid`,`groupid`,`warehouseid`,`msku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_product_sales_plan_ship_item_history 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_sales_plan_ship_item_history` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `msku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `overseaid` bigint unsigned DEFAULT NULL,
  `transtype` bigint unsigned DEFAULT NULL,
  `batchnumber` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `aftersalesday` int DEFAULT NULL,
  `isdefault` bit(1) DEFAULT (1),
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shipday` (`amount`),
  KEY `shopid_groupid` (`shopid`,`groupid`,`amazonauthid`,`marketplaceid`,`sku`),
  KEY `groupid_transtype` (`batchnumber`),
  KEY `msku_shopid` (`shopid`,`groupid`,`warehouseid`,`msku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_promotions_asin 结构
CREATE TABLE IF NOT EXISTS `t_amz_promotions_asin` (
  `promotionid` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `asin` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `currency` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `product_glance_views` int DEFAULT NULL,
  `product_units_sold` int DEFAULT NULL,
  `product_revenue` decimal(20,6) DEFAULT NULL,
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`promotionid`,`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_promotions_performance_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_promotions_performance_report` (
  `promotionid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplaceid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `currency` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `merchantId` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `type` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `glance_views` int DEFAULT NULL,
  `units_sold` int DEFAULT NULL,
  `revenue` decimal(20,6) DEFAULT NULL,
  `start_date_time` datetime DEFAULT NULL,
  `end_date_time` datetime DEFAULT NULL,
  `created_date_time` datetime DEFAULT NULL,
  `last_updated_date_time` datetime DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`promotionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_region 结构
CREATE TABLE IF NOT EXISTS `t_amz_region` (
  `code` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `advname` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `advpoint` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `client_id` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `client_secret` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_report_request_type 结构
CREATE TABLE IF NOT EXISTS `t_amz_report_request_type` (
  `id` int NOT NULL COMMENT 'ID',
  `cname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '中文名称',
  `ename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '英文名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '报表编码',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `bean` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '报表处理类',
  `day` int NOT NULL DEFAULT '0' COMMENT '报表默认请求天数',
  `disabled` bit(1) DEFAULT b'0' COMMENT '是否可用',
  `reportOptions` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '报表默认参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='亚马逊报表类型';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_returns_detailed_disposition 结构
CREATE TABLE IF NOT EXISTS `t_amz_returns_detailed_disposition` (
  `code` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_returns_reason 结构
CREATE TABLE IF NOT EXISTS `t_amz_returns_reason` (
  `code` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `description` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `type` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_returns_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_returns_report` (
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `return_date` datetime NOT NULL,
  `order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sellerid` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplaceid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fnsku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `fulfillment_center_id` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `detailed_disposition` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `license_plate_number` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `customer_comments` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`sellerid`,`return_date`,`sku`,`order_id`,`license_plate_number`) USING BTREE,
  KEY `order_id` (`order_id`),
  KEY `return_date` (`return_date`),
  KEY `sku` (`sku`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_returns_report_summary 结构
CREATE TABLE IF NOT EXISTS `t_amz_returns_report_summary` (
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `return_date` date NOT NULL,
  `sellerid` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`sellerid`,`marketplaceid`,`sku`,`return_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_returns_status 结构
CREATE TABLE IF NOT EXISTS `t_amz_returns_status` (
  `code` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `SELLABLE` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_rpt_inventory_age 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_inventory_age` (
  `id` bigint unsigned NOT NULL,
  `authid` bigint unsigned DEFAULT NULL,
  `snapshot` datetime DEFAULT NULL,
  `marketplace` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fnsku` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fcondition` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `qty_with_removals_in_progress` int DEFAULT NULL,
  `inv_age_0_to_90_days` int DEFAULT NULL,
  `inv_age_91_to_180_days` int DEFAULT NULL,
  `inv_age_181_to_270_days` int DEFAULT NULL,
  `inv_age_271_to_365_days` int DEFAULT NULL,
  `inv_age_365_plus_days` int DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `qty_to_be_charged_ltsf_6_mo` int DEFAULT NULL,
  `projected_ltsf_6_mo` decimal(20,6) DEFAULT NULL,
  `qty_to_be_charged_ltsf_12_mo` int DEFAULT NULL,
  `projected_ltsf_12_mo` decimal(20,6) DEFAULT NULL,
  `units_shipped_last_7_days` int DEFAULT NULL,
  `units_shipped_last_30_days` int DEFAULT NULL,
  `units_shipped_last_60_days` int DEFAULT NULL,
  `units_shipped_last_90_days` int DEFAULT NULL,
  `your_price` decimal(20,6) DEFAULT NULL,
  `sales_price` decimal(20,6) DEFAULT NULL,
  `lowest_price_new` decimal(20,6) DEFAULT NULL,
  `lowest_price_used` decimal(20,6) DEFAULT NULL,
  `recommended_action` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `days` int DEFAULT NULL,
  `removal_quantity` decimal(20,6) DEFAULT NULL,
  `estimated_cost_savings_of_recommended_actions` decimal(20,6) DEFAULT NULL,
  `sell_through` decimal(20,6) DEFAULT NULL,
  `item_volume` decimal(20,6) DEFAULT NULL,
  `volume_units` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `storage_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `authid_marketplace_sku` (`authid`,`marketplace`,`sku`,`fcondition`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='GET_FBA_INVENTORY_AGED_DATA';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_rpt_inventory_country 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_inventory_country` (
  `id` bigint unsigned NOT NULL COMMENT '唯一ID',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'SKU',
  `fnsku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'FBA仓库标示码',
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商品标示',
  `fcondition` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品新旧类型',
  `country` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '国家',
  `quantity` int DEFAULT NULL COMMENT '库存数量',
  `authid` bigint unsigned DEFAULT NULL COMMENT '授权ID',
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku_country_authid` (`sku`,`country`,`authid`,`fcondition`),
  KEY `country_authid` (`authid`,`country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用于存储欧洲各个国家的库存';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_rpt_inventory_detail 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_inventory_detail` (
  `id` bigint unsigned NOT NULL,
  `authid` bigint unsigned DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `bytime` datetime DEFAULT NULL,
  `fnsku` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `msku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `country` char(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `eventType` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `referenceID` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillmentCenter` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `disposition` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `reason` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `reconciledQuantity` int DEFAULT NULL,
  `unreconciledQuantity` int DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniquesku` (`authid`,`byday`,`msku`,`country`,`eventType`,`referenceID`,`fulfillmentCenter`,`disposition`),
  KEY `authid` (`authid`,`eventType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_rpt_inventory_summary 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_inventory_summary` (
  `id` bigint unsigned NOT NULL,
  `authid` bigint unsigned DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `location` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `msku` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fnsku` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `disposition` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `startingWarehouseBalance` int DEFAULT NULL,
  `endingWarehouseBalance` int DEFAULT NULL,
  `inTransitBetweenWarehouses` int DEFAULT NULL,
  `receipts` int DEFAULT NULL,
  `customerShipments` int DEFAULT NULL,
  `customerReturns` int DEFAULT NULL,
  `vendorReturns` int DEFAULT NULL,
  `warehouseTransferInOut` int DEFAULT NULL,
  `found` int DEFAULT NULL,
  `lost` int DEFAULT NULL,
  `damaged` int DEFAULT NULL,
  `disposed` int DEFAULT NULL,
  `otherEvents` int DEFAULT NULL,
  `unknownEvents` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `byday_msku_location_authid` (`authid`,`byday`,`disposition`,`location`,`msku`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_rpt_orders_fulfilled_shipments_fee 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_orders_fulfilled_shipments_fee` (
  `amazonauthid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazon_order_id` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `shipmentid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `itemid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `shipment_date` datetime NOT NULL,
  `reporting_date` datetime DEFAULT NULL,
  `payments_date` datetime DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `unitcost` decimal(20,6) DEFAULT NULL,
  `unittransfee` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`sku`,`shipmentid`,`itemid`),
  KEY `shipment_date_sales_channel_amazonauthid` (`amazonauthid`,`marketplaceid`,`shipment_date`),
  KEY `amazon_order_id_itemid` (`shipmentid`,`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_rpt_orders_fulfilled_shipments_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_orders_fulfilled_shipments_report` (
  `amazon_order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `merchant_order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_id` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_item_id` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazon_order_item_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_order_item_id` char(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `payments_date` datetime DEFAULT NULL,
  `shipment_date` datetime DEFAULT NULL,
  `reporting_date` datetime DEFAULT NULL,
  `buyer_email` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_phone_number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity_shipped` int DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `ship_service_level` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `recipient_name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_1` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_2` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_3` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_city` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_postal_code` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_country` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_phone_number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_address_1` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_address_2` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_address_3` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_city` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_postal_code` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_country` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `carrier` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tracking_number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `estimated_arrival_date` datetime DEFAULT NULL,
  `fulfillment_center_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillment_channel` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`shipment_item_id`),
  KEY `authid_sku_sales_channel` (`amazonauthid`,`sales_channel`,`sku`,`shipment_date`),
  KEY `amazonauthid_shipment_date` (`amazonauthid`,`shipment_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_rpt_orders_fulfilled_shipments_report_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_orders_fulfilled_shipments_report_archive` (
  `amazon_order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `merchant_order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_id` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_item_id` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazon_order_item_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_order_item_id` char(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `payments_date` datetime DEFAULT NULL,
  `shipment_date` datetime DEFAULT NULL,
  `reporting_date` datetime DEFAULT NULL,
  `buyer_email` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_phone_number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity_shipped` int DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `ship_service_level` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `recipient_name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_1` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_2` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_3` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_city` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_postal_code` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_country` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_phone_number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_address_1` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_address_2` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_address_3` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_city` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_postal_code` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_country` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `carrier` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tracking_number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `estimated_arrival_date` datetime DEFAULT NULL,
  `fulfillment_center_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillment_channel` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`shipment_item_id`),
  KEY `authid_sku_sales_channel` (`amazonauthid`,`sales_channel`,`sku`,`shipment_date`),
  KEY `amazonauthid_shipment_date` (`amazonauthid`,`shipment_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_scout_asins 结构
CREATE TABLE IF NOT EXISTS `t_amz_scout_asins` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sour_date` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '抓取的日期',
  `marketplace` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '格式: Amazon.com / Amazon.co.uk 等',
  `currency` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '格式: USD / GBP ..',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '榜单类型',
  `category_top` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '一级榜单品类',
  `category` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '当前的榜单品类(抓取时)',
  `category_lev` tinyint NOT NULL COMMENT '当前的榜单品类的级别',
  `billboard_rank` tinyint NOT NULL COMMENT '当前的榜单排名',
  `ASIN` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `product_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `image_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `product_title` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `reivew_score` float DEFAULT NULL COMMENT '评分',
  `review_count` smallint DEFAULT '0' COMMENT '评价数量',
  `current_price` double DEFAULT NULL COMMENT '价格',
  `prime_icon` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '是否是PRIME,空为否',
  `rank_date` date DEFAULT NULL COMMENT '计算的日期.以下字段需要计算补充',
  `product_rank` int DEFAULT NULL,
  `Length` double DEFAULT NULL,
  `Width` double DEFAULT NULL,
  `Height` double DEFAULT NULL,
  `LWH_unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '长宽高的单位',
  `Weight` double DEFAULT NULL,
  `Weight_unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '重量单位',
  `fba_fee` double DEFAULT NULL,
  `est_profit` double DEFAULT NULL COMMENT '利润. 使用的利润方案统一为: TUPARKA-pan_EU . 采购成本计为0. ',
  `costdetail` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='测试选品思路,手动加入ASIN数据,自动更新ASIN尺寸及计算利润';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_acc_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_acc_report` (
  `settlement_id` bigint unsigned NOT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `settlement_start_date` datetime DEFAULT NULL,
  `settlement_end_date` datetime DEFAULT NULL,
  `deposit_date` datetime DEFAULT NULL,
  `total_amount` decimal(15,2) DEFAULT NULL,
  `currency` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ismoved` bit(1) DEFAULT NULL,
  `capturetime` datetime DEFAULT NULL,
  `sumtime` datetime DEFAULT NULL,
  `invalid` bit(1) DEFAULT b'0' COMMENT '无效转账',
  PRIMARY KEY (`settlement_id`),
  KEY `deposit_date` (`deposit_date`),
  KEY `index1` (`amazonauthid`,`marketplace_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_acc_report_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_acc_report_archive` (
  `settlement_id` bigint unsigned NOT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `settlement_start_date` datetime DEFAULT NULL,
  `settlement_end_date` datetime DEFAULT NULL,
  `deposit_date` datetime DEFAULT NULL,
  `total_amount` decimal(15,2) DEFAULT NULL,
  `currency` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ismoved` bit(1) DEFAULT NULL,
  `capturetime` datetime DEFAULT NULL,
  `sumtime` datetime DEFAULT NULL,
  PRIMARY KEY (`settlement_id`),
  KEY `deposit_date` (`deposit_date`),
  KEY `index1` (`amazonauthid`,`marketplace_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_acc_statement 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_acc_statement` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `startdate` date DEFAULT NULL,
  `enddate` date DEFAULT NULL,
  `datetype` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `summaryall` mediumblob,
  `listdata` longblob,
  `fielddata` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_amount_description 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_amount_description` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `cname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `ename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `cdescription` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `edescription` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ename` (`ename`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_amount_type_nonsku 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_amount_type_nonsku` (
  `transaction_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amount_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amount_description` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`transaction_type`,`amount_type`,`amount_description`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_open 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_open` (
  `id` bigint unsigned NOT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `group_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `settlement_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `posted_date` datetime NOT NULL,
  `amazon_order_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_item_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `account_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillment` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `event_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ftype` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `currency` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` decimal(20,6) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `create_time` date DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`,`posted_date`,`id`),
  KEY `order_item_id` (`order_item_id`),
  KEY `group_id` (`group_id`),
  KEY `amazon_order_id` (`amazon_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_report` (
  `id` bigint unsigned NOT NULL,
  `settlement_id` bigint unsigned NOT NULL,
  `currency` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transaction_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_id` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `merchant_order_id` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `adjustment_id` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount_description` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  `fulfillment_id` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `posted_date` date NOT NULL,
  `posted_date_time` datetime DEFAULT NULL,
  `order_item_code` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `merchant_order_item_id` char(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `merchant_adjustment_item_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity_purchased` int DEFAULT NULL,
  `promotion_id` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `index1` (`settlement_id`,`sku`),
  KEY `order_id` (`order_id`,`order_item_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='账期报表';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_report_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_report_archive` (
  `id` bigint unsigned NOT NULL,
  `settlement_id` bigint unsigned NOT NULL,
  `currency` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transaction_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_id` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `merchant_order_id` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `adjustment_id` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount_description` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  `fulfillment_id` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `posted_date` date NOT NULL,
  `posted_date_time` datetime DEFAULT NULL,
  `order_item_code` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `merchant_order_item_id` char(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `merchant_adjustment_item_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity_purchased` int DEFAULT NULL,
  `promotion_id` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `index1` (`settlement_id`,`sku`),
  KEY `order_id` (`order_id`,`order_item_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='账期报表';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_summary_day 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_day` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `sku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `posted_date` date NOT NULL,
  `transaction_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商品费用',
  `amount_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amount_description` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fulfillment_type` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `currency` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  `quantity_purchased` int DEFAULT NULL,
  `quantity_orders` int DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `sku` (`sku`),
  KEY `settlementid` (`settlementid`),
  KEY `marketplace_name` (`marketplace_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_summary_day_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_day_archive` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `sku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `posted_date` date NOT NULL,
  `transaction_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商品费用',
  `amount_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amount_description` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fulfillment_type` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `currency` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  `quantity_purchased` int DEFAULT NULL,
  `quantity_orders` int DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `sku` (`sku`),
  KEY `settlementid` (`settlementid`),
  KEY `marketplace_name` (`marketplace_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_summary_month 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_month` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `posted_date` date NOT NULL,
  `transaction_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amount_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amount_description` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fulfillment_type` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `currency` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  `quantity_purchased` int DEFAULT NULL,
  `quantity_orders` int DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `settlementid` (`settlementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_summary_month_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_month_archive` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `posted_date` date NOT NULL,
  `transaction_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amount_type` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amount_description` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fulfillment_type` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `currency` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `settlementid` (`settlementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_summary_returns 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_returns` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL DEFAULT '0',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `posted_date` date NOT NULL,
  `quantity` int DEFAULT NULL,
  `mfnqty` int DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `sku` (`sku`),
  KEY `settlementid` (`settlementid`),
  KEY `marketplace_name` (`marketplace_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_summary_returns_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_returns_archive` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL DEFAULT '0',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `posted_date` date NOT NULL,
  `quantity` int DEFAULT NULL,
  `mfnqty` int DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `sku` (`sku`),
  KEY `settlementid` (`settlementid`),
  KEY `marketplace_name` (`marketplace_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_summary_sku 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_sku` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `sku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `posted_date` date NOT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_amount` int DEFAULT NULL COMMENT '订单量',
  `sales` int DEFAULT NULL COMMENT '销量',
  `refundsales` int DEFAULT NULL,
  `refundorder` int DEFAULT NULL,
  `principal` decimal(20,8) DEFAULT NULL COMMENT '销售额',
  `commission` decimal(20,8) DEFAULT NULL COMMENT '销售佣金',
  `fbafee` decimal(20,8) DEFAULT NULL COMMENT 'FBA费用',
  `refund` decimal(20,8) DEFAULT NULL COMMENT '退款金额',
  `shipping` decimal(20,8) DEFAULT NULL,
  `promotion` decimal(20,8) DEFAULT NULL,
  `tax` decimal(20,8) DEFAULT NULL,
  `otherfee` decimal(20,8) DEFAULT NULL COMMENT '其它',
  `share_storage_fee` decimal(20,8) DEFAULT NULL,
  `share_long_storage_fee` decimal(20,8) DEFAULT NULL,
  `share_adv_spend_fee` decimal(20,8) DEFAULT NULL,
  `share_coupon_redemption_fee` decimal(20,8) DEFAULT NULL,
  `share_reserve_fee` decimal(20,8) DEFAULT NULL,
  `share_reimbursement_fee` decimal(20,8) DEFAULT NULL,
  `share_disposal_fee` decimal(20,8) DEFAULT NULL,
  `share_shop_other_fee` decimal(20,8) DEFAULT NULL,
  `local_price` decimal(20,8) DEFAULT NULL,
  `local_other_cost` decimal(20,8) DEFAULT NULL,
  `local_return_tax` decimal(20,8) DEFAULT NULL,
  `profit_local_shipmentfee` decimal(20,8) DEFAULT NULL,
  `profit_marketfee` decimal(20,8) DEFAULT NULL,
  `profit_vat` decimal(20,8) DEFAULT NULL COMMENT '不再使用计算器配置，直接使用MarketplaceFacilitatorVAT-Principal',
  `profit_companytax` decimal(20,8) DEFAULT NULL,
  `profit_customstax` decimal(20,8) DEFAULT NULL,
  `profit_exchangelost` decimal(20,8) DEFAULT NULL COMMENT 'profit_lastrate',
  `profit_lostrate` decimal(20,8) DEFAULT NULL COMMENT 'profit_costrate',
  `profit_otherfee` decimal(20,8) DEFAULT NULL,
  `owner` bigint unsigned DEFAULT NULL,
  `pid` bigint unsigned DEFAULT NULL,
  `mid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `sku` (`sku`),
  KEY `settlementid` (`settlementid`),
  KEY `marketplace_name` (`marketplace_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_summary_sku_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_sku_archive` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `sku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `posted_date` date NOT NULL,
  `order_amount` int DEFAULT NULL COMMENT '订单量',
  `sales` int DEFAULT NULL COMMENT '销量',
  `principal` decimal(20,8) DEFAULT NULL COMMENT '销售额',
  `commission` decimal(20,8) DEFAULT NULL COMMENT '销售佣金',
  `fbafee` decimal(20,8) DEFAULT NULL COMMENT 'FBA费用',
  `refund` decimal(20,8) DEFAULT NULL COMMENT '退款金额',
  `otherfee` decimal(20,8) DEFAULT NULL COMMENT '其它',
  `tax` decimal(20,8) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `sku` (`sku`),
  KEY `settlementid` (`settlementid`),
  KEY `marketplace_name` (`marketplace_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_summary_sku_month 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_sku_month` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `sku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `msku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `groupid` bigint unsigned NOT NULL,
  `asin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `parentasin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `categoryid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `posted_date` date NOT NULL,
  `owner` bigint unsigned DEFAULT NULL,
  `pid` bigint unsigned DEFAULT NULL,
  `mid` bigint unsigned DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_amount` int DEFAULT NULL COMMENT '订单量',
  `sales` int DEFAULT NULL COMMENT '销量',
  `refundsales` int DEFAULT NULL,
  `refundorder` int DEFAULT NULL,
  `principal` decimal(20,8) DEFAULT NULL COMMENT '销售额',
  `commission` decimal(20,8) DEFAULT NULL COMMENT '销售佣金',
  `fbafee` decimal(20,8) DEFAULT NULL COMMENT 'FBA费用',
  `refund` decimal(20,8) DEFAULT NULL COMMENT '退款金额',
  `shipping` decimal(20,8) DEFAULT NULL,
  `promotion` decimal(20,8) DEFAULT NULL,
  `tax` decimal(20,8) DEFAULT NULL,
  `otherfee` decimal(20,8) DEFAULT NULL COMMENT '其它',
  `share_storage_fee` decimal(20,8) DEFAULT NULL,
  `share_long_storage_fee` decimal(20,8) DEFAULT NULL,
  `share_adv_spend_fee` decimal(20,8) DEFAULT NULL,
  `share_coupon_redemption_fee` decimal(20,8) DEFAULT NULL,
  `share_reserve_fee` decimal(20,8) DEFAULT NULL,
  `share_reimbursement_fee` decimal(20,8) DEFAULT NULL,
  `share_disposal_fee` decimal(20,8) DEFAULT NULL,
  `share_shop_other_fee` decimal(20,8) DEFAULT NULL,
  `local_price` decimal(20,8) DEFAULT NULL,
  `local_unit_price` decimal(20,8) DEFAULT NULL,
  `local_other_cost` decimal(20,8) DEFAULT NULL,
  `local_return_tax` decimal(20,8) DEFAULT NULL,
  `local_shipment_item_fee` decimal(20,8) DEFAULT NULL COMMENT '来自本地货件',
  `local_fifo_shipment_fee` decimal(20,8) DEFAULT NULL,
  `local_fifo_cost` decimal(20,8) DEFAULT NULL,
  `profit_local_shipmentfee` decimal(20,8) DEFAULT NULL,
  `profit_vat` decimal(20,8) DEFAULT NULL,
  `profit_companytax` decimal(20,8) DEFAULT NULL,
  `profit_customstax` decimal(20,8) DEFAULT NULL,
  `profit_exchangelost` decimal(20,8) DEFAULT NULL COMMENT 'profit_lostrate',
  `profit_lostrate` decimal(20,8) DEFAULT NULL COMMENT 'profit_costrate',
  `profit_marketfee` decimal(20,8) DEFAULT NULL,
  `profit_otherfee` decimal(20,6) DEFAULT NULL,
  `rpt_storage_fee` decimal(20,8) DEFAULT NULL COMMENT '来自仓储费报表',
  `rpt_long_storage_fee` decimal(20,8) DEFAULT NULL,
  `rpt_adv_spend_fee` decimal(20,8) DEFAULT NULL,
  `rpt_adv_sales` decimal(20,8) DEFAULT NULL,
  `rpt_adv_units` int DEFAULT NULL,
  `rpt_disposal_fee` decimal(20,8) DEFAULT NULL,
  `rpt_disposal_units` int DEFAULT NULL,
  `rpt_reimbursements_fee` decimal(20,8) DEFAULT NULL,
  `fin_sum_fee` decimal(20,8) DEFAULT NULL,
  `profit` decimal(20,8) DEFAULT NULL,
  PRIMARY KEY (`posted_date`,`id`),
  KEY `owner` (`owner`),
  KEY `parentasin` (`parentasin`),
  KEY `msku` (`msku`),
  KEY `amazonAuthId` (`amazonAuthId`),
  KEY `groupid` (`groupid`),
  KEY `mid` (`mid`),
  KEY `asin` (`asin`),
  KEY `pid` (`pid`),
  KEY `sku` (`sku`),
  KEY `categoryid` (`categoryid`),
  KEY `marketplace_name` (`marketplace_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_summary_sku_month_cny 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_sku_month_cny` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `sku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `msku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `groupid` bigint unsigned NOT NULL,
  `asin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `parentasin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `categoryid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `posted_date` date NOT NULL,
  `owner` bigint unsigned DEFAULT NULL,
  `pid` bigint unsigned DEFAULT NULL,
  `mid` bigint unsigned DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_amount` int DEFAULT NULL COMMENT '订单量',
  `sales` int DEFAULT NULL COMMENT '销量',
  `refundsales` int DEFAULT NULL,
  `refundorder` int DEFAULT NULL,
  `principal` decimal(20,8) DEFAULT NULL COMMENT '销售额',
  `commission` decimal(20,8) DEFAULT NULL COMMENT '销售佣金',
  `fbafee` decimal(20,8) DEFAULT NULL COMMENT 'FBA费用',
  `refund` decimal(20,8) DEFAULT NULL COMMENT '退款金额',
  `shipping` decimal(20,8) DEFAULT NULL,
  `promotion` decimal(20,8) DEFAULT NULL,
  `tax` decimal(20,8) DEFAULT NULL,
  `otherfee` decimal(20,8) DEFAULT NULL COMMENT '其它',
  `share_storage_fee` decimal(20,8) DEFAULT NULL,
  `share_long_storage_fee` decimal(20,8) DEFAULT NULL,
  `share_adv_spend_fee` decimal(20,8) DEFAULT NULL,
  `share_coupon_redemption_fee` decimal(20,8) DEFAULT NULL,
  `share_reserve_fee` decimal(20,8) DEFAULT NULL,
  `share_reimbursement_fee` decimal(20,8) DEFAULT NULL,
  `share_disposal_fee` decimal(20,8) DEFAULT NULL,
  `share_shop_other_fee` decimal(20,8) DEFAULT NULL,
  `local_price` decimal(20,8) DEFAULT NULL,
  `local_unit_price` decimal(20,8) DEFAULT NULL,
  `local_other_cost` decimal(20,8) DEFAULT NULL,
  `local_return_tax` decimal(20,8) DEFAULT NULL,
  `local_shipment_item_fee` decimal(20,8) DEFAULT NULL COMMENT '来自本地货件',
  `local_fifo_shipment_fee` decimal(20,8) DEFAULT NULL,
  `local_fifo_cost` decimal(20,8) DEFAULT NULL,
  `profit_local_shipmentfee` decimal(20,8) DEFAULT NULL,
  `profit_vat` decimal(20,8) DEFAULT NULL,
  `profit_companytax` decimal(20,8) DEFAULT NULL,
  `profit_customstax` decimal(20,8) DEFAULT NULL,
  `profit_exchangelost` decimal(20,8) DEFAULT NULL,
  `profit_lostrate` decimal(20,8) DEFAULT NULL,
  `profit_marketfee` decimal(20,8) DEFAULT NULL,
  `profit_otherfee` decimal(20,6) DEFAULT NULL,
  `rpt_storage_fee` decimal(20,8) DEFAULT NULL COMMENT '来自仓储费报表',
  `rpt_long_storage_fee` decimal(20,8) DEFAULT NULL,
  `rpt_adv_spend_fee` decimal(20,8) DEFAULT NULL,
  `rpt_adv_sales` decimal(20,8) DEFAULT NULL,
  `rpt_adv_units` int DEFAULT NULL,
  `rpt_disposal_fee` decimal(20,8) DEFAULT NULL,
  `rpt_disposal_units` int DEFAULT NULL,
  `rpt_reimbursements_fee` decimal(20,8) DEFAULT NULL,
  `fin_sum_fee` decimal(20,8) DEFAULT NULL,
  `profit` decimal(20,8) DEFAULT NULL,
  PRIMARY KEY (`posted_date`,`id`),
  KEY `owner` (`owner`),
  KEY `parentasin` (`parentasin`),
  KEY `msku` (`msku`),
  KEY `amazonAuthId` (`amazonAuthId`),
  KEY `groupid` (`groupid`),
  KEY `mid` (`mid`),
  KEY `asin` (`asin`),
  KEY `pid` (`pid`),
  KEY `sku` (`sku`),
  KEY `categoryid` (`categoryid`),
  KEY `marketplace_name` (`marketplace_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_settlement_summary_sku_month_usd 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_sku_month_usd` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `sku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `msku` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `groupid` bigint unsigned NOT NULL,
  `asin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `parentasin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `categoryid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplace_name` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `posted_date` date NOT NULL,
  `owner` bigint unsigned DEFAULT NULL,
  `pid` bigint unsigned DEFAULT NULL,
  `mid` bigint unsigned DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_amount` int DEFAULT NULL COMMENT '订单量',
  `sales` int DEFAULT NULL COMMENT '销量',
  `refundsales` int DEFAULT NULL,
  `refundorder` int DEFAULT NULL,
  `principal` decimal(20,8) DEFAULT NULL COMMENT '销售额',
  `commission` decimal(20,8) DEFAULT NULL COMMENT '销售佣金',
  `fbafee` decimal(20,8) DEFAULT NULL COMMENT 'FBA费用',
  `refund` decimal(20,8) DEFAULT NULL COMMENT '退款金额',
  `shipping` decimal(20,8) DEFAULT NULL,
  `promotion` decimal(20,8) DEFAULT NULL,
  `tax` decimal(20,8) DEFAULT NULL,
  `otherfee` decimal(20,8) DEFAULT NULL COMMENT '其它',
  `share_storage_fee` decimal(20,8) DEFAULT NULL,
  `share_long_storage_fee` decimal(20,8) DEFAULT NULL,
  `share_adv_spend_fee` decimal(20,8) DEFAULT NULL,
  `share_coupon_redemption_fee` decimal(20,8) DEFAULT NULL,
  `share_reserve_fee` decimal(20,8) DEFAULT NULL,
  `share_reimbursement_fee` decimal(20,8) DEFAULT NULL,
  `share_disposal_fee` decimal(20,8) DEFAULT NULL,
  `share_shop_other_fee` decimal(20,8) DEFAULT NULL,
  `local_price` decimal(20,8) DEFAULT NULL,
  `local_unit_price` decimal(20,8) DEFAULT NULL,
  `local_other_cost` decimal(20,8) DEFAULT NULL,
  `local_return_tax` decimal(20,8) DEFAULT NULL,
  `local_shipment_item_fee` decimal(20,8) DEFAULT NULL COMMENT '来自本地货件',
  `local_fifo_shipment_fee` decimal(20,8) DEFAULT NULL,
  `local_fifo_cost` decimal(20,8) DEFAULT NULL,
  `profit_local_shipmentfee` decimal(20,8) DEFAULT NULL,
  `profit_vat` decimal(20,8) DEFAULT NULL,
  `profit_companytax` decimal(20,8) DEFAULT NULL,
  `profit_customstax` decimal(20,8) DEFAULT NULL,
  `profit_exchangelost` decimal(20,8) DEFAULT NULL,
  `profit_lostrate` decimal(20,8) DEFAULT NULL,
  `profit_marketfee` decimal(20,8) DEFAULT NULL,
  `profit_otherfee` decimal(20,6) DEFAULT NULL,
  `rpt_storage_fee` decimal(20,8) DEFAULT NULL COMMENT '来自仓储费报表',
  `rpt_long_storage_fee` decimal(20,8) DEFAULT NULL,
  `rpt_adv_spend_fee` decimal(20,8) DEFAULT NULL,
  `rpt_adv_sales` decimal(20,8) DEFAULT NULL,
  `rpt_adv_units` int DEFAULT NULL,
  `rpt_disposal_fee` decimal(20,8) DEFAULT NULL,
  `rpt_disposal_units` int DEFAULT NULL,
  `rpt_reimbursements_fee` decimal(20,8) DEFAULT NULL,
  `fin_sum_fee` decimal(20,8) DEFAULT NULL,
  `profit` decimal(20,8) DEFAULT NULL,
  PRIMARY KEY (`posted_date`,`id`),
  KEY `owner` (`owner`),
  KEY `parentasin` (`parentasin`),
  KEY `msku` (`msku`),
  KEY `amazonAuthId` (`amazonAuthId`),
  KEY `groupid` (`groupid`),
  KEY `mid` (`mid`),
  KEY `asin` (`asin`),
  KEY `pid` (`pid`),
  KEY `sku` (`sku`),
  KEY `categoryid` (`categoryid`),
  KEY `marketplace_name` (`marketplace_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_ship_fulfillment_anysis_center 结构
CREATE TABLE IF NOT EXISTS `t_amz_ship_fulfillment_anysis_center` (
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `city` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `zip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `area` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_ship_fulfillment_center 结构
CREATE TABLE IF NOT EXISTS `t_amz_ship_fulfillment_center` (
  `code` char(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `country` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `address_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `city` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `zip` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `area` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isfar` bit(1) DEFAULT b'0',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_ship_state_province_code 结构
CREATE TABLE IF NOT EXISTS `t_amz_ship_state_province_code` (
  `code` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `ename` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `capital` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `ecapital` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_submitfeed 结构
CREATE TABLE IF NOT EXISTS `t_amz_submitfeed` (
  `feed_submissionid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sellerid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `feed_type` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `submitted_date` datetime DEFAULT NULL,
  `started_processing_date` datetime DEFAULT NULL,
  `completed_processiong_date` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `feed_processing_status` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `queueid` bigint unsigned DEFAULT NULL,
  `amzprocesslog` blob,
  `documentid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`feed_submissionid`,`sellerid`,`feed_type`),
  KEY `marketplaceid_shopid` (`shopid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='\r\n';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_submitfeed_queue 结构
CREATE TABLE IF NOT EXISTS `t_amz_submitfeed_queue` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `feed_type` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `process_date` timestamp NULL DEFAULT NULL,
  `filename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `content` blob,
  `process_log` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `submitfeedid` bigint unsigned DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT NULL,
  `feedoptions` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid_sellerid_marketplaceid_feed_type_process_date` (`amazonAuthId`,`marketplaceid`,`createtime`),
  KEY `filename` (`filename`),
  KEY `feedtype` (`feed_type`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_amz_vat_transaction 结构
CREATE TABLE IF NOT EXISTS `t_amz_vat_transaction` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthid` bigint unsigned NOT NULL,
  `unique_account_identifier` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `activity_period` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `activity_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplace` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `program_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transaction_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transaction_event_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `activity_transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tax_calculation_date` date DEFAULT NULL,
  `transaction_depart_date` date DEFAULT NULL,
  `transaction_arrival_date` date DEFAULT NULL,
  `transaction_complete_date` date NOT NULL,
  `seller_sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_manufacture_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `qty` int DEFAULT NULL,
  `item_weight` decimal(10,2) DEFAULT NULL,
  `total_activity_weight` decimal(10,2) DEFAULT NULL,
  `cost_price_of_items` decimal(10,2) DEFAULT NULL,
  `price_of_items_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `promo_price_of_items_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `total_price_of_items_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `ship_charge_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `promo_ship_charge_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `total_ship_charge_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `gift_wrap_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `promo_gift_wrap_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `total_gift_wrap_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `total_activity_value_amt_vat_excl` decimal(10,2) DEFAULT NULL,
  `price_of_items_vat_rate_percent` decimal(10,2) DEFAULT NULL,
  `price_of_items_vat_amt` decimal(10,2) DEFAULT NULL,
  `promo_price_of_items_vat_amt` decimal(10,2) DEFAULT NULL,
  `total_price_of_items_vat_amt` decimal(10,2) DEFAULT NULL,
  `ship_charge_vat_rate_percent` decimal(10,2) DEFAULT NULL,
  `ship_charge_vat_amt` decimal(10,2) DEFAULT NULL,
  `promo_ship_charge_vat_amt` decimal(10,2) DEFAULT NULL,
  `total_ship_charge_vat_amt` decimal(10,2) DEFAULT NULL,
  `gift_wrap_vat_rate_percent` decimal(10,2) DEFAULT NULL,
  `gift_wrap_vat_amt` decimal(10,2) DEFAULT NULL,
  `promo_gift_wrap_vat_amt` decimal(10,2) DEFAULT NULL,
  `total_gift_wrap_vat_amt` decimal(10,2) DEFAULT NULL,
  `total_activity_value_vat_amt` decimal(10,2) DEFAULT NULL,
  `price_of_items_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `promo_price_of_items_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `total_price_of_items_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `ship_charge_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `promo_ship_charge_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `total_ship_charge_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `gift_wrap_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `promo_gift_wrap_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `total_gift_wrap_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `total_activity_value_amt_vat_incl` decimal(10,2) DEFAULT NULL,
  `transaction_currency_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `commodity_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `statistical_code_depart` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `statistical_code_arrival` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `commodity_code_supplementary_unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_qty_supplementary_unit` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `total_activity_supplementary_unit` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `product_tax_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `depature_city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `departure_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `departure_post_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `arrival_city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `arrival_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `arrival_post_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sale_depart_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sale_arrival_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transportation_mode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `delivery_conditions` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `seller_depart_vat_number_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `seller_depart_country_vat_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `seller_arrival_vat_number_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `seller_arrival_country_vat_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transaction_seller_vat_number_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transaction_seller_vat_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_vat_number_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_vat_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `vat_calculation_imputation_country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `taxable_jurisdiction` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `taxable_jurisdiction_level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `vat_inv_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `vat_inv_converted_amt` decimal(10,2) DEFAULT NULL,
  `vat_inv_currency_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `vat_inv_exchange_rate` decimal(10,2) DEFAULT NULL,
  `vat_inv_exchange_rate_date` date DEFAULT NULL,
  `export_outside_eu` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `invoice_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `arrival_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `supplier_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `supplier_vat_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tax_reporting_scheme` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tax_collection_responsibility` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`transaction_complete_date`,`id`) USING BTREE,
  UNIQUE KEY `transaction_event_id_activity_transaction_id` (`transaction_event_id`,`activity_transaction_id`,`seller_sku`),
  KEY `amazonAuthid` (`amazonAuthid`,`transaction_complete_date`,`transaction_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_authority 结构
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

-- 导出  表 db_amazon.t_data_runs_remove_time 结构
CREATE TABLE IF NOT EXISTS `t_data_runs_remove_time` (
  `id` int NOT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pages` int DEFAULT NULL,
  `runtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ftype` (`ftype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_daysales_formula 结构
CREATE TABLE IF NOT EXISTS `t_daysales_formula` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `formula` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `formula_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `month_sales_rate` decimal(10,2) DEFAULT NULL,
  `15sales_rate` decimal(10,2) DEFAULT NULL,
  `7sales_rate` decimal(10,2) DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_amazon_feedstatus 结构
CREATE TABLE IF NOT EXISTS `t_erp_amazon_feedstatus` (
  `status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remark` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_estimated_sales 结构
CREATE TABLE IF NOT EXISTS `t_erp_estimated_sales` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `presales` int DEFAULT NULL COMMENT '手动输入日均销量',
  `startTime` date DEFAULT NULL COMMENT '开始生效时间 为null则是不限制',
  `endTime` date DEFAULT NULL COMMENT '结束生效时间 为null则是不限制',
  `conditions` int DEFAULT '0' COMMENT '失效条件：0 = 不限制；1 = 指定>默认 ; 2 = 指定<默认',
  `conditionNum` decimal(10,2) DEFAULT NULL COMMENT '超过失效条件数值（百分比）',
  `isInvalid` bit(1) DEFAULT NULL COMMENT '当前输入销量是否有效 1表示有效，0表示无效',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku_marketplaceid_groupid` (`groupid`,`sku`,`marketplaceid`)
) ENGINE=InnoDB AUTO_INCREMENT=14123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户维护日均销量表';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_market_priority 结构
CREATE TABLE IF NOT EXISTS `t_erp_market_priority` (
  `marketplaceid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `groupid` bigint unsigned NOT NULL,
  `priority` int DEFAULT NULL COMMENT 'FBA站点优先级',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`marketplaceid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='主要用于发货计划中的同一个店铺下面各个国家的优先级。';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_serial_num 结构
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

-- 导出  表 db_amazon.t_erp_shipcycle 结构
CREATE TABLE IF NOT EXISTS `t_erp_shipcycle` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `sku` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `transtype` bigint unsigned DEFAULT NULL,
  `stockingCycle` int DEFAULT NULL COMMENT '安全库存周期',
  `min_cycle` int DEFAULT NULL COMMENT '最小发货周期',
  `first_leg_charges` decimal(12,2) DEFAULT NULL COMMENT '头程运输成本',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku_marketplaceid_groupid` (`sku`,`marketplaceid`,`groupid`)
) ENGINE=InnoDB AUTO_INCREMENT=17392024503393378255 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='FBA仓库配置';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_address 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_address` (
  `id` bigint unsigned NOT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称或公司名称。',
  `groupid` bigint unsigned DEFAULT NULL COMMENT '店铺id',
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
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
  `email` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `contact` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `companyName` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isdefault` bit(1) DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_groupid_city` (`name`,`groupid`,`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_addressto 结构
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

-- 导出  表 db_amazon.t_erp_ship_config_carrier 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_config_carrier` (
  `country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `transtyle` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`country`,`name`,`transtyle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_inboundbox 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundbox` (
  `id` bigint unsigned NOT NULL,
  `shipmentid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `boxnum` int DEFAULT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `unit` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `wunit` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tracking_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `package_status` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shipmentid_boxnum` (`shipmentid`,`boxnum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_inboundcase 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundcase` (
  `id` bigint unsigned NOT NULL,
  `shipmentid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchantsku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `unitspercase` int DEFAULT NULL,
  `numberofcase` int NOT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shipmentid` (`shipmentid`,`merchantsku`,`numberofcase`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_inbounditem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inbounditem` (
  `id` bigint unsigned NOT NULL,
  `ShipmentId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `inboundplanid` bigint unsigned DEFAULT NULL COMMENT 'inboundplan的id',
  `FulfillmentNetworkSKU` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `SellerSKU` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商品的卖家 SKU。',
  `QuantityShipped` int DEFAULT NULL,
  `QuantityReceived` int DEFAULT NULL,
  `QuantityInCase` int DEFAULT NULL,
  `Quantity` int DEFAULT NULL COMMENT '	要配送的商品数量。',
  `PrepInstruction` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `PrepOwner` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `msku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `unitcost` decimal(20,6) DEFAULT NULL,
  `totalcost` decimal(20,6) DEFAULT NULL,
  `unittransfee` decimal(20,6) DEFAULT NULL,
  `totaltransfee` decimal(20,6) DEFAULT NULL,
  `ReceivedDate` datetime DEFAULT NULL,
  `QuantityReceivedSub` int DEFAULT NULL,
  `QuantityReceivedBalance` int DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 3` (`SellerSKU`,`ShipmentId`),
  KEY `idx_ShipmentId_QuantityReceived_QuantityShipped` (`ShipmentId`,`QuantityReceived`,`QuantityShipped`),
  KEY `msku` (`msku`),
  KEY `SellerSKU_amazonauthid_marketplaceid_ReceivedDate` (`amazonauthid`,`marketplaceid`,`SellerSKU`,`ReceivedDate`),
  KEY `Index 4` (`ShipmentId`),
  KEY `FK_t_erp_ship_inboundplanitem_t_erp_ship_inboundplan` (`inboundplanid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_inboundplan 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundplan` (
  `id` bigint unsigned NOT NULL,
  `name` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `number` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipFromAddressID` bigint unsigned DEFAULT NULL COMMENT '	您的退货地址。(发货地址)',
  `skunum` int DEFAULT NULL,
  `labelPrepType` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '	入库货件所需的标签准备类型。',
  `AreCasesRequired` bit(1) DEFAULT NULL COMMENT '指明入库货件是否包含原厂包装发货商品。注： 货件所含的商品必须全部是原厂包装发货或者混装发货。',
  `amazongroupid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `auditstatus` tinyint DEFAULT NULL COMMENT '1 已提交（待审核）；  3,已确认货件；   2已退回货件',
  `invtype` tinyint DEFAULT '0',
  `plansubid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `warehouseid` (`warehouseid`),
  KEY `marketplaceid_warehouseid_shopid` (`shopid`,`marketplaceid`),
  KEY `Index 3` (`createdate`),
  KEY `idx_amazongroupid_marketplaceid_shopid` (`amazongroupid`,`marketplaceid`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_inboundshipment 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundshipment` (
  `ShipmentId` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '货件编号',
  `DestinationFulfillmentCenterId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '	亚马逊创建的亚马逊配送中心标识。',
  `ShipToAddressID` bigint unsigned DEFAULT NULL COMMENT '目的地',
  `LabelPrepType` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ShipmentStatus` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'ShipmentStatus 值：WORKING - 卖家已创建货件，但未发货。 SHIPPED - 承运人已取件。',
  `inboundplanid` bigint unsigned DEFAULT NULL COMMENT 'planId',
  `name` char(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'shipment_name',
  `TotalUnits` int DEFAULT NULL COMMENT '装运单位数',
  `FeePerUnit` decimal(10,2) DEFAULT NULL COMMENT '单位手工加工费',
  `status` int DEFAULT NULL COMMENT '-1,已驳回；0取消货件；1,待审核；2，配货（已确认货件）；3，装箱；4，物流信息确认；5已发货；6，已完成发货',
  `status0date` datetime DEFAULT NULL,
  `status1date` datetime DEFAULT NULL,
  `status2date` datetime DEFAULT NULL,
  `status3date` datetime DEFAULT NULL,
  `status4date` datetime DEFAULT NULL,
  `status5date` datetime DEFAULT NULL COMMENT '客户可以修改的发货日期',
  `status6date` datetime DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `TotalFee` decimal(10,2) DEFAULT NULL COMMENT '装运的总手工加工费',
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '货币',
  `carrier` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '承运人',
  `boxnum` int DEFAULT NULL,
  `oldboxnum` int DEFAULT NULL,
  `transtyle` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `shiped_date` datetime DEFAULT NULL,
  `start_receive_date` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `submissionid_excel` bigint unsigned DEFAULT NULL,
  `submissionid` bigint unsigned DEFAULT NULL,
  `feedstatus` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pro_number` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `referenceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transport_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `box_contents_source` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sync_inv` tinyint DEFAULT '0' COMMENT '1代表没有扣库存，2代表已经扣库存',
  `check_inv` bigint unsigned DEFAULT NULL COMMENT '下架ID',
  `ignorerec` bit(1) DEFAULT b'0' COMMENT '忽略收货异常',
  PRIMARY KEY (`ShipmentId`),
  KEY `idx_inboundplanid_status_refreshtime` (`inboundplanid`,`status`,`refreshtime`),
  KEY `Index 2` (`inboundplanid`),
  KEY `Index 3` (`status`),
  KEY `idx_inboundplanid_status_shipeddate_refreshtime` (`inboundplanid`,`status`,`shiped_date`,`refreshtime`),
  KEY `status5date` (`status5date`),
  KEY `DestinationFulfillmentCenterId` (`DestinationFulfillmentCenterId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_inboundshipment_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundshipment_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `shipmentid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '货件编号',
  `status` int DEFAULT NULL COMMENT '-1,已驳回；0取消货件；1,待审核；2，配货（已确认货件）；3，装箱；4，物流信息确认；5已发货；6，已完成发货',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ShipmentId` (`shipmentid`)
) ENGINE=InnoDB AUTO_INCREMENT=927977 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_inboundshipment_traceupload 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundshipment_traceupload` (
  `shipmentid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `status` tinyint DEFAULT NULL COMMENT '0待处理，1,已处理，2，处理失败',
  `errormsg` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `creator` bigint unsigned NOT NULL,
  PRIMARY KEY (`shipmentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_inboundtrans 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundtrans` (
  `id` bigint unsigned NOT NULL,
  `shipmentid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
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
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `wtype` tinyint DEFAULT '0',
  `transtype` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shipmentid` (`shipmentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_inboundtrans_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundtrans_his` (
  `opttime` datetime NOT NULL,
  `id` bigint unsigned NOT NULL,
  `shipmentid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `company` bigint unsigned DEFAULT NULL,
  `channel` bigint unsigned DEFAULT NULL,
  `singleprice` decimal(10,4) DEFAULT NULL,
  `transweight` decimal(10,4) DEFAULT NULL,
  `wunit` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `otherfee` decimal(10,4) DEFAULT NULL,
  `ordernum` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  `outarrtime` datetime DEFAULT NULL,
  `inarrtime` datetime DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `wtype` tinyint DEFAULT '0',
  `transtype` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`opttime`,`id`),
  UNIQUE KEY `shipmentid` (`shipmentid`,`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_shipment_template_file 结构
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

-- 导出  表 db_amazon.t_erp_ship_status 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_status` (
  `status` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `content` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_destination_address 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_destination_address` (
  `code` char(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `destinationType` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
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

-- 导出  表 db_amazon.t_erp_ship_v2_inboundbox 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundbox` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `packing_group_id` char(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipmentid` char(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `boxnum` int DEFAULT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `unit` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `wunit` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tracking_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `package_status` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shipmentid_boxnum` (`formid`,`boxnum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundcase 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundcase` (
  `id` bigint unsigned NOT NULL,
  `boxid` bigint unsigned DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shipmentid` (`boxid`,`sku`),
  KEY `sku` (`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inbounditem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inbounditem` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `expiration` datetime DEFAULT NULL,
  `label_owner` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `manufacturing_lot_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `prep_owner` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `msku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `confirm_quantity` int DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `appendtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `msku` (`msku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundoperation 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundoperation` (
  `operationid` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `formid` bigint unsigned NOT NULL,
  `operation_status` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operation` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operation_problem` varchar(8000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`operationid`),
  KEY `key` (`formid`,`operationid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundplan 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundplan` (
  `id` bigint unsigned NOT NULL,
  `inbound_pland_id` char(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `number` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `placement_option_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `source_address` bigint unsigned DEFAULT NULL COMMENT '	您的退货地址。(发货地址)',
  `groupid` bigint unsigned DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `auditstatus` tinyint DEFAULT NULL COMMENT '1 已提交（待审核）； 2待配货 3,待装箱；4待发货；  7已发货； 11已驳回； 12已取消',
  `invstatus` tinyint DEFAULT NULL COMMENT '0 未扣库存；1锁定待出库库存；2 已经出库',
  `auditime` datetime DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipments` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '货件ids',
  `pre_shipping_date` date DEFAULT NULL COMMENT '预计出库时间',
  `shipping_date` date DEFAULT NULL,
  `shipping_solution` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'SEND 亚马逊合作承运商:AMAZON_PARTNERED_CARRIER; 非合作承运商:USE_YOUR_OWN_CARRIER',
  `transtyle` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'SP:小包裹快递(SPD),LTL:托盘配送,NONE',
  `transtype` bigint unsigned DEFAULT NULL COMMENT '运输方式',
  `invtype` tinyint DEFAULT '0' COMMENT '0:代表正常库存流程，1：代表不扣库存，2代表同步过来的计划直接扣可用库存,5没有同步',
  `batchnumber` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transportation_token` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `check_inv` bigint unsigned DEFAULT NULL,
  `submitbox` bit(1) DEFAULT b'0',
  `areCasesRequired` bit(1) DEFAULT b'0',
  `createtime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`number`,`shopid`),
  KEY `warehouseid` (`warehouseid`),
  KEY `marketplaceid_warehouseid_shopid` (`shopid`,`marketplaceid`),
  KEY `Index 3` (`shopid`,`createtime`),
  KEY `amazonauthid` (`amazonauthid`),
  KEY `idx_amazongroupid_marketplaceid_shopid` (`groupid`,`marketplaceid`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundplan_delivery_options 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundplan_delivery_options` (
  `delivery_window_option_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `shipmentid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `availability_type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `editable_until` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `end_date` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `start_date` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`shipmentid`,`delivery_window_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundplan_packing_options 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundplan_packing_options` (
  `packing_option_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `inbound_plan_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `contents` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `opttime` datetime DEFAULT NULL,
  `operator` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`packing_option_id`,`inbound_plan_id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundplan_packing_options_group_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundplan_packing_options_group_item` (
  `packing_group_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `inbound_plan_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `formid` bigint unsigned NOT NULL,
  `packing_option_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`packing_group_id`,`inbound_plan_id`,`sku`),
  KEY `formid` (`formid`),
  KEY `packing_option_id` (`packing_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundplan_placement_options 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundplan_placement_options` (
  `placement_option_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `inbound_plan_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `contents` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `opttime` datetime DEFAULT NULL,
  `operator` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`placement_option_id`,`inbound_plan_id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundplan_transportation_options 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundplan_transportation_options` (
  `placement_option_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `shipment_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `transportation_option_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `contents` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `opttime` datetime DEFAULT NULL,
  `operator` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`shipment_id`,`transportation_option_id`,`placement_option_id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundshipment 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundshipment` (
  `shipmentid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '货件编号',
  `referenceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `destination` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '	亚马逊创建的亚马逊配送中心标识。',
  `shipment_confirmation_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `placement_option_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transportation_option_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `delivery_window_option_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `addressid` bigint unsigned DEFAULT NULL,
  `shipmentstatus` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'ShipmentStatus 值：WORKING - 卖家已创建货件，但未发货。 SHIPPED - 承运人已取件。',
  `transaction_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `formid` bigint unsigned DEFAULT NULL COMMENT 'planId',
  `totalunits` int unsigned DEFAULT NULL COMMENT '装运单位数',
  `feeunit` decimal(20,6) unsigned DEFAULT NULL COMMENT '单位手工加工费',
  `totalfee` decimal(20,6) unsigned DEFAULT NULL COMMENT '装运的总手工加工费',
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `inboundplanid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` char(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'shipment_name',
  `status` int DEFAULT NULL COMMENT '-1,已驳回；0取消货件；1,待审核；2，配货（已确认货件）；3，装箱；4，物流信息确认；5已发货；6，已完成发货',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `carrier` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '承运人',
  `transtyle` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transtype` bigint unsigned DEFAULT NULL COMMENT '运输方式',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `boxtime` datetime DEFAULT NULL,
  `shiped_date` datetime DEFAULT NULL,
  `closed_date` datetime DEFAULT NULL,
  `start_receive_date` datetime DEFAULT NULL,
  `pro_number` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transport_status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transportation_token` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sync_inv` tinyint DEFAULT '0' COMMENT '1代表没有扣库存，2代表已经扣库存',
  `check_inv` bigint unsigned DEFAULT NULL,
  `ignorerec` bit(1) DEFAULT b'0' COMMENT '忽略收货异常',
  `isquote` bit(1) DEFAULT b'0',
  PRIMARY KEY (`shipmentid`),
  KEY `idx_inboundplanid_status_refreshtime` (`formid`,`shipmentstatus`,`refreshtime`),
  KEY `shipment_confirmation_id` (`shipment_confirmation_id`),
  KEY `Index 2` (`formid`),
  KEY `Index 3` (`status`),
  KEY `idx_inboundplanid_status_shipeddate_refreshtime` (`formid`,`status`,`shiped_date`,`refreshtime`),
  KEY `DestinationFulfillmentCenterId` (`destination`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundshipment_box 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundshipment_box` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `formid` bigint unsigned DEFAULT NULL,
  `packing_group_id` char(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipmentid` char(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `placement_option_id` char(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `boxnum` int DEFAULT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `unit` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `wunit` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tracking_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `package_status` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shipmentid` (`shipmentid`),
  KEY `shipmentid_boxnum` (`formid`,`boxnum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundshipment_boxitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundshipment_boxitem` (
  `id` bigint unsigned NOT NULL,
  `boxid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `expiration` datetime DEFAULT NULL,
  `label_owner` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `manufacturing_lot_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `prep_owner` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `msku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `confirm_quantity` int DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `boxid` (`boxid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundshipment_customs 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundshipment_customs` (
  `itemid` bigint unsigned NOT NULL DEFAULT '0',
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
  PRIMARY KEY (`itemid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='海关表';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundshipment_delivery 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundshipment_delivery` (
  `delivery_window_option_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `availability_type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `editable_until` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `end_date` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `start_date` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`delivery_window_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inboundshipment_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundshipment_item` (
  `id` bigint unsigned NOT NULL,
  `shipmentid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `PrepInstruction` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `expiration` datetime DEFAULT NULL,
  `fnsku` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `label_owner` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `manufacturing_lot_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `prep_owner` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `msku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `received` int DEFAULT NULL,
  `receiveddate` datetime DEFAULT NULL,
  `totaltransfee` decimal(20,6) DEFAULT NULL,
  `unittransfee` decimal(20,6) DEFAULT NULL,
  `totalcost` decimal(20,6) DEFAULT NULL,
  `unitcost` decimal(20,6) DEFAULT NULL,
  `prepinstructions` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shipmentid_sku` (`shipmentid`,`sku`),
  KEY `shipmentid` (`shipmentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_ship_v2_inbound_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inbound_record` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `formid` bigint unsigned NOT NULL,
  `shipmentid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '#',
  `status` int NOT NULL COMMENT '-1,已驳回；0取消货件；1,待审核；2，配货（已确认货件）；3，装箱；4，物流信息确认；5已发货；6，已完成发货',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key` (`formid`,`status`) USING BTREE,
  KEY `shipmentid_status` (`shipmentid`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=159375 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_summary_data 结构
CREATE TABLE IF NOT EXISTS `t_erp_summary_data` (
  `id` bigint unsigned NOT NULL,
  `ftype` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `value` decimal(10,2) DEFAULT NULL,
  `mapdata` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned NOT NULL,
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `索引 2` (`shopid`,`ftype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='主页上的数据，每日更新';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_usersales_rank 结构
CREATE TABLE IF NOT EXISTS `t_erp_usersales_rank` (
  `userid` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `daytype` int NOT NULL,
  `quantity` int DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `oldorderprice` decimal(10,2) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`userid`,`shopid`,`daytype`),
  KEY `createdate` (`createdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_erp_warehouse_fba 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_fba` (
  `id` bigint unsigned NOT NULL COMMENT 'ID',
  `shopid` bigint unsigned NOT NULL COMMENT '店铺',
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `stocking_cycle` int DEFAULT '0' COMMENT '安全库存周期',
  `min_cycle` int DEFAULT '0' COMMENT '最小发货周期',
  `put_on_days` int DEFAULT '0' COMMENT '上架周期',
  `first_leg_days` int DEFAULT '0' COMMENT '头程周期',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_marketplaceid` (`shopid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_exchangeinfo 结构
CREATE TABLE IF NOT EXISTS `t_exchangeinfo` (
  `currency` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '币别',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`currency`),
  UNIQUE KEY `symbol` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_exchangerate 结构
CREATE TABLE IF NOT EXISTS `t_exchangerate` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '币别',
  `price` decimal(18,6) DEFAULT NULL COMMENT '汇率',
  `symbol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `utctime` datetime DEFAULT NULL COMMENT '服务器更新时间',
  `volume` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=444945 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_exchangerate_customer 结构
CREATE TABLE IF NOT EXISTS `t_exchangerate_customer` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `name` char(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT '' COMMENT '币别',
  `price` decimal(18,6) DEFAULT NULL COMMENT '汇率',
  `symbol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `type` varchar(15) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `utctime` datetime DEFAULT NULL COMMENT '服务器更新时间',
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`shopid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_exchangerate_his 结构
CREATE TABLE IF NOT EXISTS `t_exchangerate_his` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '币别',
  `price` decimal(18,6) DEFAULT NULL COMMENT '汇率',
  `symbol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `type` varchar(15) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `utctime` datetime DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `isnewest` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `index_name` (`name`,`byday`)
) ENGINE=InnoDB AUTO_INCREMENT=486207 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_fbaformat 结构
CREATE TABLE IF NOT EXISTS `t_fbaformat` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `country` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ismedia` bit(1) DEFAULT NULL,
  `producttierId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fba_format` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `month` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(10,4) DEFAULT NULL COMMENT '最大重量',
  `effective_date` date DEFAULT NULL COMMENT '生效日期',
  `expiry_date` date DEFAULT NULL COMMENT '失效日期',
  `dispatch_type` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '亚马逊配送方案',
  `isclothing` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`),
  KEY `FK_t_fbaformat_t_producttier` (`producttierId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_fbaformat_archive 结构
CREATE TABLE IF NOT EXISTS `t_fbaformat_archive` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `country` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ismedia` bit(1) DEFAULT NULL,
  `producttierId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fba_format` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `month` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(10,4) DEFAULT NULL COMMENT '最大重量',
  `effective_date` date DEFAULT NULL COMMENT '生效日期',
  `expiry_date` date DEFAULT NULL COMMENT '失效日期',
  `dispatch_type` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '亚马逊配送方案',
  `isclothing` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`),
  KEY `FK_t_fbaformat_t_producttier` (`producttierId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_fba_estimated_fees 结构
CREATE TABLE IF NOT EXISTS `t_fba_estimated_fees` (
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fnsku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `product_group` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `brand` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfilled_by` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `has_local_inventory` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `your_price` decimal(10,2) DEFAULT NULL,
  `sales_price` decimal(10,2) DEFAULT NULL,
  `longest_side` decimal(10,2) DEFAULT NULL,
  `median_side` decimal(10,2) DEFAULT NULL,
  `shortest_side` decimal(10,2) DEFAULT NULL,
  `length_and_girth` decimal(10,2) DEFAULT NULL,
  `unit_of_dimension` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_package_weight` decimal(10,2) DEFAULT NULL,
  `unit_of_weight` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `product_size_tier` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `currency` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `estimated_fee_total` decimal(10,2) DEFAULT NULL,
  `estimated_referral_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `estimated_variable_closing_fee` decimal(10,2) DEFAULT NULL COMMENT '自配送方式才有的费用',
  `estimated_fixed_closing_fee` decimal(10,2) DEFAULT NULL COMMENT '印度独有的费用',
  `estimated_order_handling_fee_per_order` decimal(10,2) DEFAULT NULL,
  `estimated_pick_pack_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `estimated_weight_handling_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `expected_fulfillment_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `estimated_future_fee` decimal(10,2) DEFAULT NULL,
  `estimated_future_order_handling_fee_per_order` decimal(10,2) DEFAULT NULL,
  `estimated_future_pick_pack_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `estimated_future_weight_handling_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `expected_future_fulfillment_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `expected_domestic_fulfilment_fee_per_unit` decimal(10,2) DEFAULT NULL,
  `expected_efn_fulfilment_fee_per_unit_uk` decimal(10,2) DEFAULT NULL,
  `expected_efn_fulfilment_fee_per_unit_de` decimal(10,2) DEFAULT NULL,
  `expected_efn_fulfilment_fee_per_unit_it` decimal(10,2) DEFAULT NULL,
  `expected_efn_fulfilment_fee_per_unit_fr` decimal(10,2) DEFAULT NULL,
  `expected_efn_fulfilment_fee_per_unit_es` decimal(10,2) DEFAULT NULL,
  `expected_efn_fulfilment_fee_per_unit_se` decimal(10,2) DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`sku`,`asin`,`amazonAuthId`,`marketplaceid`),
  KEY `index_auth` (`amazonAuthId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_fba_fbasipp 结构
CREATE TABLE IF NOT EXISTS `t_fba_fbasipp` (
  `id` bigint unsigned DEFAULT NULL,
  `country` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `producttierId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(10,4) DEFAULT NULL,
  `format` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isclothing` bit(1) DEFAULT b'0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_fba_labeling_service_fee 结构
CREATE TABLE IF NOT EXISTS `t_fba_labeling_service_fee` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `isStandard` bit(1) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `country` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`),
  KEY `producttierId` (`isStandard`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_fba_longterm_storage_fee_report 结构
CREATE TABLE IF NOT EXISTS `t_fba_longterm_storage_fee_report` (
  `id` bigint unsigned NOT NULL,
  `snapshot_date` date NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fnsku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `condition` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `qty_charged_12month` int DEFAULT NULL COMMENT '存储超过12个月的产品被收费的数量',
  `qty_charged_6month` int DEFAULT NULL,
  `qty_charged` int DEFAULT NULL,
  `amount_6` decimal(10,2) DEFAULT NULL,
  `amount_12` decimal(10,2) DEFAULT NULL,
  `amount_charged` decimal(10,2) DEFAULT NULL,
  `surcharge_age_tier` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `per_unit_volume` decimal(12,2) DEFAULT NULL,
  `rate_surcharge` decimal(12,2) DEFAULT NULL,
  `volume_unit` decimal(10,2) DEFAULT NULL,
  `country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `is_sl` bit(1) DEFAULT b'0',
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`snapshot_date`,`id`),
  UNIQUE KEY `snapshot_date_sku_surcharge_age_tier_country_amazonauthid` (`snapshot_date`,`sku`,`surcharge_age_tier`,`country`,`amazonauthid`),
  KEY `date-sku-country` (`amazonauthid`,`country`,`sku`,`snapshot_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_fba_reimbursements_fee_report 结构
CREATE TABLE IF NOT EXISTS `t_fba_reimbursements_fee_report` (
  `id` bigint unsigned NOT NULL,
  `approval_date` datetime NOT NULL,
  `reimbursement_id` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `case_id` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazon_order_id` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `reason` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '原因',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fnsku` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `conditions` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `currency_unit` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount_per_unit` decimal(16,4) DEFAULT NULL,
  `amount_total` decimal(16,4) DEFAULT NULL,
  `quantity_reimbursed_cash` int DEFAULT NULL,
  `quantity_reimbursed_inventory` int DEFAULT NULL,
  `quantity_reimbursed_total` int DEFAULT NULL,
  `original_reimbursement_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `original_reimbursement_type` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`,`approval_date`,`id`),
  UNIQUE KEY `reimbursement_id_amazon_order_id_sku` (`sku`,`amazon_order_id`,`reimbursement_id`,`reason`),
  KEY `marketplaceid` (`marketplaceid`),
  KEY `reimbursement_id` (`amazon_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_fba_storage_fee_report 结构
CREATE TABLE IF NOT EXISTS `t_fba_storage_fee_report` (
  `id` bigint unsigned NOT NULL,
  `asin` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fnsku` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fulfillment_center` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'country_code',
  `longest_side` decimal(10,2) DEFAULT NULL,
  `median_side` decimal(10,2) DEFAULT NULL,
  `shortest_side` decimal(10,2) DEFAULT NULL,
  `measurement_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(10,2) DEFAULT NULL,
  `weight_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_volume` decimal(12,4) DEFAULT NULL,
  `volume_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `product_size_tier` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `average_quantity_on_hand` decimal(10,2) DEFAULT NULL COMMENT '商品的日均体积，等于过去一个月的库存总体积除以该月包含的天数。',
  `average_quantity_pending_removal` decimal(12,4) DEFAULT NULL,
  `estimated_total_item_volume` decimal(20,6) DEFAULT NULL,
  `month` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '收费的月份，如2019-11 month_of_charge',
  `storage_utilization_ratio` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `storage_utilization_ratio_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `base_rate` decimal(10,2) DEFAULT NULL,
  `utilization_surcharge_rate` decimal(10,2) DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `monthly_storage_fee` decimal(20,6) DEFAULT NULL COMMENT 'estimated_monthly_storage_fee',
  `dangerous_goods_storage_type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `eligible_for_inventory_discount` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `qualifies_for_inventory_discount` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `total_incentive_fee_amount` decimal(20,6) DEFAULT NULL,
  `breakdown_incentive_fee_amount` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `average_quantity_customer_orders` decimal(20,6) DEFAULT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`,`country`,`month`,`asin`,`fulfillment_center`,`fnsku`),
  KEY `idkey` (`id`),
  KEY `asinkey` (`asin`,`fnsku`,`fulfillment_center`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_fba_storage_fee_report_archive 结构
CREATE TABLE IF NOT EXISTS `t_fba_storage_fee_report_archive` (
  `id` bigint unsigned NOT NULL,
  `asin` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fnsku` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fulfillment_center` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'country_code',
  `longest_side` decimal(10,2) DEFAULT NULL,
  `median_side` decimal(10,2) DEFAULT NULL,
  `shortest_side` decimal(10,2) DEFAULT NULL,
  `measurement_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(10,2) DEFAULT NULL,
  `weight_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_volume` decimal(12,4) DEFAULT NULL,
  `volume_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `product_size_tier` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `average_quantity_on_hand` decimal(10,2) DEFAULT NULL COMMENT '商品的日均体积，等于过去一个月的库存总体积除以该月包含的天数。',
  `average_quantity_pending_removal` decimal(12,4) DEFAULT NULL,
  `estimated_total_item_volume` decimal(20,6) DEFAULT NULL,
  `month` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '收费的月份，如2019-11 month_of_charge',
  `storage_utilization_ratio` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `storage_utilization_ratio_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `base_rate` decimal(10,2) DEFAULT NULL,
  `utilization_surcharge_rate` decimal(10,2) DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `monthly_storage_fee` decimal(20,6) DEFAULT NULL COMMENT 'estimated_monthly_storage_fee',
  `dangerous_goods_storage_type` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `eligible_for_inventory_discount` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `qualifies_for_inventory_discount` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `total_incentive_fee_amount` decimal(20,6) DEFAULT NULL,
  `breakdown_incentive_fee_amount` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `average_quantity_customer_orders` decimal(20,6) DEFAULT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`,`country`,`month`,`asin`,`fulfillment_center`,`fnsku`),
  KEY `idkey` (`id`),
  KEY `asinkey` (`asin`,`fnsku`,`fulfillment_center`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_fba_storage_fee_report_excel 结构
CREATE TABLE IF NOT EXISTS `t_fba_storage_fee_report_excel` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `asin` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fnsku` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillment_center` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'country_code',
  `longest_side` decimal(10,2) DEFAULT NULL,
  `median_side` decimal(10,2) DEFAULT NULL,
  `shortest_side` decimal(10,2) DEFAULT NULL,
  `measurement_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(10,2) DEFAULT NULL,
  `weight_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_volume` decimal(12,4) DEFAULT NULL,
  `volume_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `product_size_tier` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `average_quantity_on_hand` decimal(10,2) DEFAULT NULL COMMENT '商品的日均体积，等于过去一个月的库存总体积除以该月包含的天数。',
  `average_quantity_pending_removal` decimal(12,4) DEFAULT NULL,
  `estimated_total_item_volume` decimal(20,6) DEFAULT NULL,
  `month` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '收费的月份，如2019-11 month_of_charge',
  `storage_utilization_ratio` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `storage_utilization_ratio_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `base_rate` decimal(10,2) DEFAULT NULL,
  `utilization_surcharge_rate` decimal(10,2) DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `monthly_storage_fee` decimal(20,6) DEFAULT NULL COMMENT 'estimated_monthly_storage_fee',
  `dangerous_goods_storage_type` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `eligible_for_inventory_discount` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `qualifies_for_inventory_discount` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `total_incentive_fee_amount` decimal(20,6) DEFAULT NULL,
  `breakdown_incentive_fee_amount` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `average_quantity_customer_orders` decimal(20,6) DEFAULT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_fixed_closingfee 结构
CREATE TABLE IF NOT EXISTS `t_fixed_closingfee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `format` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '价格范围',
  `category` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fee` decimal(10,4) DEFAULT NULL,
  `country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sort` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_holiday 结构
CREATE TABLE IF NOT EXISTS `t_holiday` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `shopid` bigint unsigned NOT NULL DEFAULT '0',
  `day` date DEFAULT NULL COMMENT '日期',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '节日名称',
  `type` tinyint(1) DEFAULT NULL COMMENT '节假日类型(0 工作日、1 周末、2 节日、3 调休)',
  `week` tinyint(1) DEFAULT NULL COMMENT '1 - 7，分别表示 周一 至 周日',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB AUTO_INCREMENT=1945757654443974749 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='节假日表';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_individualfee 结构
CREATE TABLE IF NOT EXISTS `t_individualfee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `perItemFee` decimal(10,2) DEFAULT NULL COMMENT '个人卖家才有per-item fee',
  PRIMARY KEY (`id`),
  KEY `country` (`country`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_inplacefee 结构
CREATE TABLE IF NOT EXISTS `t_inplacefee` (
  `id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`),
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_inplacefeeformat 结构
CREATE TABLE IF NOT EXISTS `t_inplacefeeformat` (
  `id` int NOT NULL AUTO_INCREMENT,
  `inplacefeeid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `producttierId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `standard` bit(1) DEFAULT NULL,
  `format` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `country` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`),
  KEY `producttierId` (`producttierId`),
  KEY `inplacefeeid` (`inplacefeeid`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_inventorystoragefee 结构
CREATE TABLE IF NOT EXISTS `t_inventorystoragefee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `month` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `price` decimal(10,3) DEFAULT NULL,
  `country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isStandard` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_inventory_report 结构
CREATE TABLE IF NOT EXISTS `t_inventory_report` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fnsku` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pcondition` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `your_price` decimal(10,2) DEFAULT NULL,
  `mfn_listing_exists` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `mfn_fulfillable_quantity` int DEFAULT NULL,
  `afn_listing_exists` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `afn_warehouse_quantity` int DEFAULT NULL COMMENT '亚马逊库存数量 =（ 亚马逊可用库存）+（亚马逊不可用库存）+（亚马逊预留库存）',
  `afn_fulfillable_quantity` int DEFAULT NULL COMMENT '亚马逊可用库存',
  `afn_unsellable_quantity` int DEFAULT NULL COMMENT '亚马逊不可用库存',
  `afn_reserved_quantity` int DEFAULT NULL COMMENT '亚马逊预留库存',
  `afn_total_quantity` int DEFAULT NULL COMMENT '亚马逊物流总数量 =（亚马逊库存数量）+（亚马逊物流入库处理数量）+（亚马逊物流入库发货数量）（亚马逊物流入库接收数量',
  `per_unit_volume` decimal(10,2) DEFAULT NULL,
  `afn_inbound_working_quantity` int DEFAULT NULL COMMENT '亚马逊物流入库处理数量',
  `afn_inbound_shipped_quantity` int DEFAULT NULL COMMENT '亚马逊物流入库发货数量',
  `afn_inbound_receiving_quantity` int DEFAULT NULL COMMENT '亚马逊物流入库接受数量',
  `afn_researching_quantity` int DEFAULT '0' COMMENT '亚马逊异常库存数量',
  `afn_future_supply_buyable` int DEFAULT '0' COMMENT '亚马逊物流网络未来可购买供货',
  `afn_reserved_future_supply` int DEFAULT '0' COMMENT '亚马逊物流网络预留未来供货',
  `isnewest` bit(1) DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`marketplaceid`,`amazonAuthId`,`sku`),
  KEY `iv_sku_amazonauthid` (`sku`,`amazonAuthId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_inventory_report_his 结构
CREATE TABLE IF NOT EXISTS `t_inventory_report_his` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `byday` date NOT NULL,
  `marketplaceid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fnsku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `pcondition` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `your_price` decimal(10,2) DEFAULT NULL,
  `mfn_listing_exists` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `mfn_fulfillable_quantity` int DEFAULT NULL,
  `afn_listing_exists` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `afn_warehouse_quantity` int DEFAULT NULL,
  `afn_fulfillable_quantity` int DEFAULT NULL,
  `afn_unsellable_quantity` int DEFAULT NULL,
  `afn_reserved_quantity` int DEFAULT NULL,
  `afn_total_quantity` int DEFAULT NULL,
  `per_unit_volume` decimal(10,2) DEFAULT NULL,
  `afn_inbound_working_quantity` int DEFAULT NULL,
  `afn_inbound_shipped_quantity` int DEFAULT NULL,
  `afn_inbound_receiving_quantity` int DEFAULT NULL,
  `afn_researching_quantity` int DEFAULT '0',
  `afn_future_supply_buyable` int DEFAULT '0',
  `afn_reserved_future_supply` int DEFAULT '0',
  `isnewest` bit(1) DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  PRIMARY KEY (`byday`,`id`),
  UNIQUE KEY `索引 3` (`amazonAuthId`,`marketplaceid`,`sku`,`byday`),
  KEY `sku` (`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_inventory_report_his_archive 结构
CREATE TABLE IF NOT EXISTS `t_inventory_report_his_archive` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `byday` date NOT NULL,
  `marketplaceid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fnsku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `pcondition` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `your_price` decimal(10,2) DEFAULT NULL,
  `mfn_listing_exists` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `mfn_fulfillable_quantity` int DEFAULT NULL,
  `afn_listing_exists` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `afn_warehouse_quantity` int DEFAULT NULL,
  `afn_fulfillable_quantity` int DEFAULT NULL,
  `afn_unsellable_quantity` int DEFAULT NULL,
  `afn_reserved_quantity` int DEFAULT NULL,
  `afn_total_quantity` int DEFAULT NULL,
  `per_unit_volume` decimal(10,2) DEFAULT NULL,
  `afn_inbound_working_quantity` int DEFAULT NULL,
  `afn_inbound_shipped_quantity` int DEFAULT NULL,
  `afn_inbound_receiving_quantity` int DEFAULT NULL,
  `afn_researching_quantity` int DEFAULT '0',
  `afn_future_supply_buyable` int DEFAULT '0',
  `afn_reserved_future_supply` int DEFAULT '0',
  `isnewest` bit(1) DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  PRIMARY KEY (`byday`,`amazonAuthId`,`marketplaceid`,`sku`),
  KEY `marketplaceid` (`marketplaceid`),
  KEY `sku` (`sku`),
  KEY `key` (`amazonAuthId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_inventory_reserved_report 结构
CREATE TABLE IF NOT EXISTS `t_inventory_reserved_report` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `fnsku` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `reserved_qty` int DEFAULT NULL,
  `reserved_customerorders` int DEFAULT NULL,
  `reserved_fc_transfers` int DEFAULT NULL,
  `reserved_fc_processing` int DEFAULT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index1` (`sku`,`marketplaceid`,`amazonAuthId`),
  KEY `idx_amazonAuthId_marketplaceid` (`amazonAuthId`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_inventory_status 结构
CREATE TABLE IF NOT EXISTS `t_inventory_status` (
  `id` bigint unsigned NOT NULL,
  `stockStatus` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonAuthId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `Index 2` (`byday`,`asin`,`marketplaceid`,`amazonAuthId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_inventory_temp 结构
CREATE TABLE IF NOT EXISTS `t_inventory_temp` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehouse` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `instock` int DEFAULT NULL,
  `inbound` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1641 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_manual_processing_fee 结构
CREATE TABLE IF NOT EXISTS `t_manual_processing_fee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `month` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `manualProcessingFee` decimal(10,2) DEFAULT '0.00' COMMENT '手动处理费',
  `country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_marketplace 结构
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

-- 导出  表 db_amazon.t_orders_financial 结构
CREATE TABLE IF NOT EXISTS `t_orders_financial` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `amazon_order_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单ID',
  `order_item_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单Item id',
  `sku` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'SKU',
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '货币',
  `ftype` char(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '费用类型',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '费用',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `marketplaceId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '站点',
  `posted_date` datetime DEFAULT NULL COMMENT '出账时间',
  PRIMARY KEY (`id`),
  KEY `amazon_order_id` (`amazon_order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=183397 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_orders_fulfilled_shipments_fee 结构
CREATE TABLE IF NOT EXISTS `t_orders_fulfilled_shipments_fee` (
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazon_order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_id` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_item_id` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazon_order_item_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_order_item_id` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `payments_date` datetime DEFAULT NULL,
  `shipment_date` datetime DEFAULT NULL,
  `reporting_date` datetime DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`shipment_item_id`,`amazon_order_item_id`),
  KEY `amazonauthid_sku_sales_channel_payments_date` (`amazonauthid`,`sales_channel`,`sku`,`payments_date`),
  KEY `shipment_date_sales_channel_amazonauthid` (`amazonauthid`,`sales_channel`,`shipment_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_orders_fulfilled_shipments_report 结构
CREATE TABLE IF NOT EXISTS `t_orders_fulfilled_shipments_report` (
  `amazon_order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_id` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_item_id` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazon_order_item_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_order_item_id` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `payments_date` datetime DEFAULT NULL,
  `shipment_date` datetime DEFAULT NULL,
  `reporting_date` datetime DEFAULT NULL,
  `buyer_email` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_phone_number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity_shipped` int DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `ship_service_level` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `recipient_name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_1` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_2` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_3` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_city` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_postal_code` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_country` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_phone_number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_address_1` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_address_2` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_address_3` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_city` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_postal_code` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_country` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `carrier` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tracking_number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `estimated_arrival_date` datetime DEFAULT NULL,
  `fulfillment_center_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillment_channel` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`shipment_item_id`,`amazon_order_item_id`),
  KEY `shipment_date_sales_channel_amazonauthid` (`amazonauthid`,`sales_channel`,`shipment_date`),
  KEY `purchase_date` (`purchase_date`),
  KEY `sku` (`sku`,`purchase_date`),
  KEY `sales_channel_amazonauthid` (`amazonauthid`,`sales_channel`,`purchase_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_orders_fulfilled_shipments_report_archive 结构
CREATE TABLE IF NOT EXISTS `t_orders_fulfilled_shipments_report_archive` (
  `amazon_order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_id` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_item_id` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazon_order_item_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_order_item_id` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `payments_date` datetime DEFAULT NULL,
  `shipment_date` datetime DEFAULT NULL,
  `reporting_date` datetime DEFAULT NULL,
  `buyer_email` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_phone_number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity_shipped` int DEFAULT NULL,
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `ship_service_level` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `recipient_name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_1` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_2` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_address_3` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_city` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_postal_code` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_country` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_phone_number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_address_1` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_address_2` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_address_3` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_city` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_postal_code` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bill_country` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `carrier` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tracking_number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `estimated_arrival_date` datetime DEFAULT NULL,
  `fulfillment_center_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillment_channel` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`shipment_item_id`,`amazon_order_item_id`),
  KEY `purchase_date` (`purchase_date`),
  KEY `sku` (`sku`,`purchase_date`),
  KEY `sales_channel_amazonauthid` (`amazonauthid`,`sales_channel`,`purchase_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_orders_remark 结构
CREATE TABLE IF NOT EXISTS `t_orders_remark` (
  `amazon_order_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `feed_queueid` bigint unsigned DEFAULT NULL,
  `review_send_time` datetime DEFAULT NULL,
  `review_send_operator` bigint DEFAULT NULL,
  `invoice_no` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `credit_note` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`),
  KEY `amazon_order_id_remark` (`remark`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_orders_report 结构
CREATE TABLE IF NOT EXISTS `t_orders_report` (
  `id` bigint unsigned NOT NULL,
  `amazon_order_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_order_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_status` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillment_channel` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_channel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_service_level` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_status` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_state` char(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_postal_code` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `promotion_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `is_business_order` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_order_number` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `price_designation` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`purchase_date`,`id`),
  UNIQUE KEY `Index` (`amazon_order_id`,`sku`),
  KEY `Index 2` (`sales_channel`,`sku`,`purchase_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_orders_report_archive 结构
CREATE TABLE IF NOT EXISTS `t_orders_report_archive` (
  `id` bigint unsigned NOT NULL,
  `amazon_order_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_order_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_status` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillment_channel` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_channel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_service_level` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_status` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_state` char(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_postal_code` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `promotion_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `is_business_order` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_order_number` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `price_designation` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`purchase_date`,`id`),
  KEY `Index 2` (`sales_channel`,`sku`),
  KEY `Index` (`amazon_order_id`,`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_orders_report_download 结构
CREATE TABLE IF NOT EXISTS `t_orders_report_download` (
  `id` bigint unsigned NOT NULL,
  `amazon_order_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_order_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_status` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillment_channel` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_channel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_service_level` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_status` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_state` char(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_postal_code` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ship_country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `promotion_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `is_business_order` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_order_number` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `price_designation` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL,
  `marketplaceId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refreshtime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`purchase_date`,`id`),
  UNIQUE KEY `Index 2` (`amazonAuthId`,`amazon_order_id`,`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_orders_reviews_customer 结构
CREATE TABLE IF NOT EXISTS `t_orders_reviews_customer` (
  `amazon_order_id` char(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `remark` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `picture` varchar(500) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_orders_sumconfig 结构
CREATE TABLE IF NOT EXISTS `t_orders_sumconfig` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `order_status` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shop_id` bigint unsigned DEFAULT NULL,
  `sales_channel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `is_business_order` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `discountfrom` decimal(10,0) DEFAULT NULL,
  `discountTo` decimal(10,0) DEFAULT NULL,
  `amazonAuthId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_orders_summary 结构
CREATE TABLE IF NOT EXISTS `t_orders_summary` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceid` char(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `purchase_date` date NOT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `quantity` int DEFAULT NULL,
  `ordersum` int DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `quantity2b` int DEFAULT NULL,
  `ordersum2b` int DEFAULT NULL,
  `orderprice2b` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`),
  UNIQUE KEY `index_1` (`amazonAuthId`,`marketplaceid`,`sku`,`purchase_date`),
  KEY `Index 2` (`marketplaceid`,`quantity`,`orderprice`,`ordersum`),
  KEY `amazonauthid` (`sku`,`amazonAuthId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_orders_summary_month 结构
CREATE TABLE IF NOT EXISTS `t_orders_summary_month` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceid` char(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `purchase_date` date NOT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `quantity` int DEFAULT NULL,
  `ordersum` int DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `quantity2b` int DEFAULT NULL,
  `ordersum2b` int DEFAULT NULL,
  `orderprice2b` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`),
  UNIQUE KEY `index_1` (`amazonAuthId`,`marketplaceid`,`sku`,`purchase_date`),
  KEY `Index 2` (`marketplaceid`,`quantity`,`orderprice`,`ordersum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_orders_summary_week 结构
CREATE TABLE IF NOT EXISTS `t_orders_summary_week` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceid` char(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `purchase_date` date NOT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `quantity` int DEFAULT NULL,
  `ordersum` int DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `quantity2b` int DEFAULT NULL,
  `ordersum2b` int DEFAULT NULL,
  `orderprice2b` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`),
  UNIQUE KEY `index_1` (`amazonAuthId`,`marketplaceid`,`sku`,`purchase_date`),
  KEY `Index 2` (`marketplaceid`,`quantity`,`orderprice`,`ordersum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_order_invoice 结构
CREATE TABLE IF NOT EXISTS `t_order_invoice` (
  `id` bigint unsigned NOT NULL,
  `groupid` bigint unsigned NOT NULL,
  `logoUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片地址id',
  `image` bigint unsigned DEFAULT NULL,
  `company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `postalcode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sign` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `groupid` (`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_order_invoice_vat 结构
CREATE TABLE IF NOT EXISTS `t_order_invoice_vat` (
  `id` bigint unsigned NOT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `vat_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `vat_rate` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `groupid` (`groupid`,`country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_outbound_weightformat 结构
CREATE TABLE IF NOT EXISTS `t_outbound_weightformat` (
  `id` bigint unsigned NOT NULL,
  `producttierId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isMedia` bit(1) DEFAULT NULL,
  `format` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `producttierId` (`producttierId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_picture 结构
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

-- 导出  表 db_amazon.t_prepservicefee 结构
CREATE TABLE IF NOT EXISTS `t_prepservicefee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isStandard` bit(1) DEFAULT NULL,
  `prepServiceFee` decimal(10,2) DEFAULT '0.00',
  `country` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_productformat 结构
CREATE TABLE IF NOT EXISTS `t_productformat` (
  `id` char(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `producttierId` char(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `country` char(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `format` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `length_unit` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `weight_unit` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `sort` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`producttierId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_producttier 结构
CREATE TABLE IF NOT EXISTS `t_producttier` (
  `id` char(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `name` char(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `isStandard` bit(1) DEFAULT NULL,
  `country` char(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `box_weight` decimal(10,4) DEFAULT NULL COMMENT '包装箱重量（单位：kg）',
  `amz_name` char(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '对应亚马逊显示的product tier',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_category 结构
CREATE TABLE IF NOT EXISTS `t_product_category` (
  `CategoryId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `pid` bigint unsigned NOT NULL,
  `Name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `parentId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`pid`,`CategoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品分类表';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_customs_setting 结构
CREATE TABLE IF NOT EXISTS `t_product_customs_setting` (
  `shopid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `price_type` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `rate` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`shopid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_follow 结构
CREATE TABLE IF NOT EXISTS `t_product_follow` (
  `id` bigint unsigned NOT NULL,
  `asin` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `lastupdateTime` datetime DEFAULT NULL,
  `isread` bit(1) DEFAULT b'0',
  `sku` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `flownumber` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `asin_amazonAuthId_marketplaceid_endtime_isnewest` (`amazonAuthId`,`marketplaceid`,`asin`),
  KEY `flownumber` (`flownumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_info 结构
CREATE TABLE IF NOT EXISTS `t_product_info` (
  `id` bigint unsigned NOT NULL COMMENT '产品ID',
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '唯一码asin',
  `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户码sku',
  `marketplaceid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '站点',
  `name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品名称（产品标题）',
  `openDate` datetime DEFAULT NULL COMMENT '创建日期',
  `itemDimensions` bigint unsigned DEFAULT NULL COMMENT '产品尺寸',
  `pageDimensions` bigint unsigned DEFAULT NULL COMMENT '含包装尺寸',
  `fulfillChannel` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '交付渠道',
  `binding` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '装订',
  `totalOfferCount` int DEFAULT NULL COMMENT '卖家数量',
  `brand` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '品牌',
  `manufacturer` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '厂商',
  `pgroup` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '分组',
  `typename` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '分类',
  `price` decimal(14,2) DEFAULT NULL COMMENT '价格',
  `image` bigint unsigned DEFAULT NULL COMMENT '照片',
  `parentMarketplace` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父商品marketplace',
  `parentAsin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父商品asin',
  `isparent` bit(1) DEFAULT b'0' COMMENT '是否副产品（即不是变体）',
  `lastupdate` datetime DEFAULT NULL COMMENT '更新时间',
  `createdate` datetime DEFAULT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL COMMENT '授权ID',
  `invalid` bit(1) DEFAULT b'0' COMMENT '是否无效',
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `inSnl` bit(1) DEFAULT b'0' COMMENT '是否轻小',
  `fnsku` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pcondition` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 3` (`amazonAuthId`,`marketplaceid`,`sku`),
  KEY `Index 6` (`marketplaceid`,`amazonAuthId`,`isparent`,`invalid`,`disable`),
  KEY `invalid` (`invalid`,`disable`,`isparent`),
  KEY `idx_asin_amazonAuthId` (`asin`),
  KEY `marketplaceid_parentAsin_amazonAuthId` (`parentAsin`,`marketplaceid`,`amazonAuthId`),
  KEY `idx_sku_isparent_invalid` (`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_info_follow 结构
CREATE TABLE IF NOT EXISTS `t_product_info_follow` (
  `pid` bigint unsigned NOT NULL,
  `status_upload` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `orders_sum` int DEFAULT NULL,
  `last_order_time` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `quantity` int unsigned DEFAULT NULL,
  `oldprice` decimal(20,6) unsigned DEFAULT NULL,
  `price` decimal(20,6) unsigned DEFAULT NULL,
  `assumeprice` decimal(20,6) unsigned DEFAULT NULL,
  `lowprice` decimal(20,6) unsigned DEFAULT NULL,
  `shopprice` decimal(20,6) unsigned DEFAULT NULL,
  `pricetime` datetime DEFAULT NULL,
  `delivery_cycle` int DEFAULT NULL,
  `rank` int DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_info_status_define 结构
CREATE TABLE IF NOT EXISTS `t_product_info_status_define` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `shopid` bigint unsigned DEFAULT NULL,
  `name` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '状态名称',
  `issystem` bit(1) NOT NULL DEFAULT b'0',
  `color` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `remark` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_name` (`shopid`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_adv 结构
CREATE TABLE IF NOT EXISTS `t_product_in_adv` (
  `pid` bigint unsigned NOT NULL COMMENT '产品ID',
  `adv_impr7` decimal(14,4) DEFAULT NULL,
  `adv_sales7` decimal(10,4) DEFAULT NULL,
  `adv_cpc7` decimal(14,4) DEFAULT NULL,
  `adv_clicks7` decimal(14,4) DEFAULT NULL,
  `adv_spc7` decimal(14,4) DEFAULT NULL,
  `adv_spend7` decimal(14,4) DEFAULT NULL,
  `adv_ctr7` decimal(14,4) DEFAULT NULL,
  `adv_acos7` decimal(14,4) DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_aftersale 结构
CREATE TABLE IF NOT EXISTS `t_product_in_aftersale` (
  `id` bigint unsigned NOT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `date` date DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_bydate` (`sku`,`marketplaceid`,`groupid`,`date`),
  KEY `date_amazonauthid` (`amazonauthid`,`date`),
  KEY `sku_marketplaceid_amazonauthid` (`amazonauthid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_autoprice 结构
CREATE TABLE IF NOT EXISTS `t_product_in_autoprice` (
  `pid` bigint unsigned NOT NULL,
  `ftype` tinyint unsigned DEFAULT NULL COMMENT '1代表最低价，2代表购物车',
  `lowestprice` decimal(10,2) DEFAULT NULL COMMENT '0元 代表停用',
  `disable` bit(1) DEFAULT NULL COMMENT 'false代表启动，true代表停用',
  `buybox_lowest_first` bit(1) DEFAULT NULL,
  `buybox_lowest_next` bit(1) DEFAULT NULL,
  `down_step_price` decimal(10,2) DEFAULT NULL,
  `down_step_rate` decimal(10,2) DEFAULT NULL,
  `up_step_price` decimal(10,2) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_opt 结构
CREATE TABLE IF NOT EXISTS `t_product_in_opt` (
  `pid` bigint unsigned NOT NULL COMMENT '产品ID',
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `priceremark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '价格公告',
  `buyprice` decimal(10,2) DEFAULT NULL COMMENT '采购单价',
  `businessprice` decimal(10,2) DEFAULT NULL COMMENT '销售价格',
  `businesstype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '价格',
  `businesslist` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '隐藏',
  `disable` bit(1) DEFAULT b'0' COMMENT '手动输入的预估销量',
  `presales` int DEFAULT NULL COMMENT '手动输入的预估销量',
  `lastupdate` datetime DEFAULT NULL COMMENT '更新时间',
  `remark_analysis` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `msku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '本地SKU',
  `fnsku` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'FNSKU',
  `review_daily_refresh` int DEFAULT NULL COMMENT '评论刷新时间',
  `fulfillment_availability` int DEFAULT NULL COMMENT '自发货可用库存',
  `merchant_shipping_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '自发货运费模板',
  `profitid` bigint unsigned DEFAULT NULL COMMENT '对应利润计算方案',
  `status` int unsigned DEFAULT NULL COMMENT '产品状态 0备货 1维持 2提升 3促销  4停售 5清仓 6删除',
  `owner` bigint unsigned DEFAULT NULL COMMENT '运营负责人',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `lowestprice` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`pid`),
  KEY `idx_msku_disable_status` (`msku`,`disable`,`status`),
  KEY `msku` (`msku`),
  KEY `Index 5` (`disable`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_order 结构
CREATE TABLE IF NOT EXISTS `t_product_in_order` (
  `pid` bigint unsigned NOT NULL COMMENT '产品ID',
  `avgsales` int DEFAULT NULL,
  `oldavgsales` int DEFAULT NULL,
  `daynum` int DEFAULT NULL,
  `sales_week` int DEFAULT NULL COMMENT 'sales_week,往前推2天之后的7日销量',
  `price_week` decimal(10,2) DEFAULT NULL COMMENT '销售额',
  `sales_month` int DEFAULT NULL COMMENT '30日销量',
  `order_week` int DEFAULT NULL,
  `order_month` int DEFAULT NULL,
  `sales_return_month` int DEFAULT NULL,
  `order_return_month` int DEFAULT NULL,
  `changeRate` decimal(10,2) DEFAULT NULL COMMENT '销量上升或者下降比率',
  `lastupdate` datetime DEFAULT NULL,
  `sales_seven` int DEFAULT NULL COMMENT 'sales_seven,7日销量',
  `sales_fifteen` int DEFAULT NULL COMMENT 'sales_fifteen,15日销量',
  `rank` int DEFAULT NULL,
  PRIMARY KEY (`pid`),
  KEY `lastupdate` (`lastupdate`),
  KEY `sales_month` (`sales_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品信息的订单销售数据';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_presale 结构
CREATE TABLE IF NOT EXISTS `t_product_in_presale` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `groupid` bigint unsigned NOT NULL,
  `date` date NOT NULL,
  `quantity` int DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `start` date DEFAULT NULL,
  `end` date DEFAULT NULL,
  `month` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `hasdaysales` bit(1) DEFAULT b'0',
  PRIMARY KEY (`sku`,`marketplaceid`,`groupid`,`date`),
  UNIQUE KEY `idx` (`id`),
  KEY `index_date_event` (`start`,`end`),
  KEY `sku_marketplaceid_date_amazonauthid` (`amazonauthid`,`marketplaceid`,`sku`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_presale_archive 结构
CREATE TABLE IF NOT EXISTS `t_product_in_presale_archive` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `date` date DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `start` date DEFAULT NULL,
  `end` date DEFAULT NULL,
  `month` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `hasdaysales` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_bydate` (`sku`,`marketplaceid`,`groupid`,`date`),
  KEY `index_date_event` (`start`,`end`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_profit 结构
CREATE TABLE IF NOT EXISTS `t_product_in_profit` (
  `pid` bigint unsigned NOT NULL COMMENT '产品ID',
  `profit_week` decimal(14,6) DEFAULT NULL,
  `margin_week` decimal(10,2) DEFAULT NULL,
  `margin` decimal(10,2) DEFAULT NULL,
  `planName` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `dimensions` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  `shipmentfee` decimal(10,2) DEFAULT NULL,
  `othersfee` decimal(10,2) DEFAULT NULL,
  `costDetail` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '',
  PRIMARY KEY (`pid`),
  KEY `lastupdate` (`lastupdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品信息的利润信息';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_review 结构
CREATE TABLE IF NOT EXISTS `t_product_in_review` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `asin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `starofrate` float unsigned DEFAULT NULL,
  `reviewnum` int unsigned DEFAULT NULL,
  `starofrate_1` float unsigned DEFAULT NULL,
  `starofrate_2` float unsigned DEFAULT NULL,
  `starofrate_3` float unsigned DEFAULT NULL,
  `starofrate_4` float unsigned DEFAULT NULL,
  `starofrate_5` float unsigned DEFAULT NULL,
  `negative` bit(1) DEFAULT b'0',
  `positiveReview` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `criticalReview` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `refreshnum` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `asin_marketplaceid` (`asin`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_review_detail 结构
CREATE TABLE IF NOT EXISTS `t_product_in_review_detail` (
  `id` bigint unsigned NOT NULL,
  `reviewid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `profile_avatar_img` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `profile_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `review_star_rating` float DEFAULT NULL,
  `review_date` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `review_cndate` datetime DEFAULT NULL,
  `review_title` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `review_body` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `review_image_tile` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `video_img_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `video_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_size_color_num` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `verified_text` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `helpful_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `refreshorder` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `reviewid_asin_marketplaceid` (`asin`,`marketplaceid`,`reviewid`),
  KEY `review_star_rating` (`marketplaceid`,`review_star_rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_review_order 结构
CREATE TABLE IF NOT EXISTS `t_product_in_review_order` (
  `amazonauthid` bigint unsigned NOT NULL,
  `purchase_date` datetime NOT NULL,
  `orderid` char(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `pid` bigint unsigned DEFAULT NULL,
  `reviewid` char(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `asin` char(15) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `email` char(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `sku` char(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `sales_channel` char(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `review_star_rating` float DEFAULT NULL,
  `review_title` varchar(2000) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `review_date` date DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`,`purchase_date`,`orderid`),
  KEY `marketplaceid` (`marketplaceid`,`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_review_runs 结构
CREATE TABLE IF NOT EXISTS `t_product_in_review_runs` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `pid` bigint unsigned DEFAULT NULL,
  `sku` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid_asin` (`shopid`),
  KEY `asin_marketplaceid` (`amazonauthid`,`marketplaceid`,`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_ses 结构
CREATE TABLE IF NOT EXISTS `t_product_in_ses` (
  `pid` bigint unsigned NOT NULL COMMENT '产品ID',
  `session_day7` int DEFAULT NULL,
  `session_rate7` decimal(14,4) DEFAULT NULL,
  `buybox_rate7` decimal(14,4) DEFAULT NULL,
  `units_ordered7` int DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_sys 结构
CREATE TABLE IF NOT EXISTS `t_product_in_sys` (
  `pid` bigint unsigned NOT NULL COMMENT '产品ID',
  `saleDate` datetime DEFAULT NULL,
  `orderDate` datetime DEFAULT NULL,
  `avgsales` int DEFAULT NULL,
  `oldavgsales` int DEFAULT NULL,
  `daynum` int DEFAULT NULL,
  `maxsales_day_month` int DEFAULT NULL,
  `sales_week` int DEFAULT NULL COMMENT 'sales_week,往前推2天之后的7日销量',
  `price_week` decimal(10,2) DEFAULT NULL COMMENT '销售额',
  `profit_week` decimal(10,2) DEFAULT NULL,
  `margin_week` decimal(10,2) DEFAULT NULL,
  `sales_month` int DEFAULT NULL COMMENT '30日销量',
  `order_week` int DEFAULT NULL,
  `order_month` int DEFAULT NULL,
  `changeRate` decimal(10,2) DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  `sales_seven` int DEFAULT NULL COMMENT 'sales_seven,7日销量',
  `sales_fifteen` int DEFAULT NULL COMMENT 'sales_fifteen,15日销量',
  `rank` int DEFAULT NULL,
  `buyprice` decimal(10,2) DEFAULT NULL,
  `shipmentfee` decimal(10,2) DEFAULT NULL,
  `othersfee` decimal(10,2) DEFAULT NULL,
  `costDetail` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_in_tags 结构
CREATE TABLE IF NOT EXISTS `t_product_in_tags` (
  `pid` bigint unsigned NOT NULL,
  `tagid` bigint unsigned NOT NULL,
  `operator` bigint unsigned NOT NULL,
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`pid`,`tagid`),
  KEY `tagid` (`tagid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品-标签';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_price 结构
CREATE TABLE IF NOT EXISTS `t_product_price` (
  `id` bigint unsigned NOT NULL,
  `MarketplaceId` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `ptype` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `landed_amount` decimal(10,2) DEFAULT NULL,
  `landed_currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `listing_amount` decimal(10,2) DEFAULT NULL,
  `listing_currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipping_amount` decimal(10,2) DEFAULT NULL,
  `shipping_currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isnewest` bit(1) DEFAULT NULL,
  `fulfillmentChannel` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `itemCondition` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `itemSubCondition` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `SellerId` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `SellerSKU` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`SellerId`,`MarketplaceId`,`SellerSKU`,`ptype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品价格信息表';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_price_his 结构
CREATE TABLE IF NOT EXISTS `t_product_price_his` (
  `id` bigint unsigned NOT NULL,
  `MarketplaceId` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `ptype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `landed_amount` decimal(10,2) DEFAULT NULL,
  `landed_currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `listing_amount` decimal(10,2) DEFAULT NULL,
  `listing_currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipping_amount` decimal(10,2) DEFAULT NULL,
  `shipping_currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isnewest` bit(1) DEFAULT NULL,
  `fulfillmentChannel` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `itemCondition` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `itemSubCondition` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `SellerId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `SellerSKU` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`SellerId`,`MarketplaceId`,`isnewest`,`ptype`,`asin`,`byday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品价格信息表';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_price_locked 结构
CREATE TABLE IF NOT EXISTS `t_product_price_locked` (
  `pid` bigint unsigned NOT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  `disable` bit(1) NOT NULL DEFAULT b'0',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_rank 结构
CREATE TABLE IF NOT EXISTS `t_product_rank` (
  `id` bigint unsigned NOT NULL,
  `byday` datetime DEFAULT NULL,
  `categoryId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `rank` int DEFAULT NULL,
  `product_id` bigint unsigned DEFAULT NULL,
  `title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `link` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isMain` bit(1) DEFAULT NULL,
  `isNewest` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `FK_t_ranklist_t_produ` (`product_id`,`categoryId`),
  KEY `tprank_pid_byday_rank` (`product_id`,`byday`,`rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_rank_his 结构
CREATE TABLE IF NOT EXISTS `t_product_rank_his` (
  `id` bigint unsigned NOT NULL,
  `byday` date DEFAULT NULL,
  `categoryId` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `rank` int DEFAULT NULL,
  `product_id` bigint unsigned DEFAULT NULL,
  `isMain` bit(1) DEFAULT NULL,
  `isNewest` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_ranklist_t_produ` (`product_id`),
  KEY `Index 3` (`byday`),
  KEY `categoryId` (`categoryId`,`byday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_rank_sales_his 结构
CREATE TABLE IF NOT EXISTS `t_product_rank_sales_his` (
  `id` int NOT NULL AUTO_INCREMENT,
  `market` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `byday` date NOT NULL DEFAULT '0000-00-00',
  `ordersum` int NOT NULL DEFAULT '0',
  `rank` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品当天在一级大类排名对应产品当天的ordersum';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_recommended 结构
CREATE TABLE IF NOT EXISTS `t_product_recommended` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `link` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品购买链接',
  `brand` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '品牌',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '分类',
  `subcategory` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '子分类',
  `lowestprice` decimal(10,2) DEFAULT NULL COMMENT '上周最低价格',
  `fbaoffer` bit(1) DEFAULT b'0' COMMENT 'fba提供',
  `amzoffer` bit(1) DEFAULT b'0' COMMENT '亚马逊提供',
  `offers` int DEFAULT NULL COMMENT '优惠数量',
  `reviews` int DEFAULT NULL COMMENT '评论数量',
  `rank` int DEFAULT NULL COMMENT '销量排名',
  `sales_rank_growth` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '业务销售额排名增长 评级',
  `page_views` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '页面浏览量 评级',
  `manufacturer_part_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '制造商零件编号',
  `EAN` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'EAN码',
  `UPC` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'UPC码',
  `model_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '型号编号',
  `ISBN` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图书编码',
  `brandoffer` bit(1) DEFAULT NULL COMMENT '是否 自己提供的品牌',
  `categoryoffer` bit(1) DEFAULT NULL COMMENT '是否 自己提供的类别',
  `performance` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品性能',
  `istoprank` bit(1) DEFAULT b'0' COMMENT '是否最高销售排名',
  `islowprice` bit(1) DEFAULT b'0' COMMENT '是否最低价格',
  `onAmazon` bit(1) DEFAULT b'0' COMMENT '产品尚未在亚马逊上',
  `isrefresh` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `amazonAuthId_marketplaceid_asin` (`amazonAuthId`,`marketplaceid`,`asin`),
  KEY `asin` (`amazonAuthId`,`sales_rank_growth`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_recommended_ext 结构
CREATE TABLE IF NOT EXISTS `t_product_recommended_ext` (
  `rid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `imgurl` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `dim` bigint unsigned DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `category` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `margin` decimal(10,2) DEFAULT NULL,
  `profit` decimal(10,2) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`rid`),
  KEY `asin` (`asin`),
  KEY `profit` (`profit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_remark_his 结构
CREATE TABLE IF NOT EXISTS `t_product_remark_his` (
  `pid` bigint unsigned NOT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint unsigned NOT NULL,
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`pid`,`ftype`,`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品备注历史表';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_type 结构
CREATE TABLE IF NOT EXISTS `t_product_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类型',
  `country` varchar(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL COMMENT '国家',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='成本计算用，的亚马逊成本。采用父分类的方式，将所有国家的分类都放入此表。\r\n以美国的分类作为主要分类。如果美国没有此分类，则该分类依旧为美国添加，使用其他分类 类型的策略。\r\n当用户选择1 号分类，则其他子分类即改分类对应的其他国家的分类。\r\n ';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_product_usercategory 结构
CREATE TABLE IF NOT EXISTS `t_product_usercategory` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_profitcfg 结构
CREATE TABLE IF NOT EXISTS `t_profitcfg` (
  `id` bigint unsigned NOT NULL COMMENT 'ID 用于区分每一个方案',
  `shop_id` bigint unsigned DEFAULT NULL COMMENT '添加方案的人，只能当事人或其部下使用',
  `isSystem` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否系统内置方案',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `sales_channel` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '配送方案，是否亚马逊配送',
  `sellerPlan` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '销售计划',
  `shipmentStyle` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '运费计算方式',
  `isDefault` bit(1) DEFAULT b'0' COMMENT '是否为默认方案',
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='利润计算方案';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_profitcfgcountry 结构
CREATE TABLE IF NOT EXISTS `t_profitcfgcountry` (
  `id` bigint unsigned NOT NULL COMMENT 'ID 用于区分每一个方案对应不同国家的方案配置项',
  `profitid` bigint unsigned NOT NULL COMMENT '总方案',
  `country` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '国家',
  `constantw` decimal(10,2) DEFAULT '0.00' COMMENT '重量基数',
  `constantd` decimal(10,2) DEFAULT '0.00' COMMENT '材积比',
  `constantm` decimal(10,2) DEFAULT '0.00' COMMENT '材积基数',
  `fba_month` int DEFAULT NULL COMMENT 'FBA配送月份和亚马逊仓储费计算月份',
  `storagefee` decimal(10,3) DEFAULT '0.000' COMMENT '仓储费',
  `taxRate` decimal(10,2) DEFAULT '0.00' COMMENT '进口关税费率(原名：税率)',
  `lostRate` decimal(10,2) DEFAULT '0.00' COMMENT '汇率损耗比率',
  `sellerRate` decimal(10,2) DEFAULT '0.00' COMMENT '市场营销占比',
  `otherfee` decimal(10,2) DEFAULT '0.00' COMMENT '其他每单销售固定费用',
  `costRate` decimal(10,2) DEFAULT '0.00' COMMENT '其他每单销售成本比率',
  `logistics` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '物流方式',
  `subscriptionfee` decimal(10,2) DEFAULT NULL COMMENT '订阅费',
  `prepservice` bit(1) DEFAULT b'0' COMMENT '计划内的服务费',
  `labelService` bit(1) DEFAULT b'0' COMMENT '亚马逊标签服务费',
  `manualProcessing` decimal(10,2) DEFAULT '0.00' COMMENT '亚马逊手动处理费',
  `unprepservice` decimal(10,2) DEFAULT '0.00' COMMENT '计划外的服务费',
  `invplacefee` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '库存配置费',
  `promotion` decimal(10,2) DEFAULT '0.00' COMMENT 'all store promtion',
  `amonth` int DEFAULT '1' COMMENT '亚马逊仓储费，库存周期',
  `hasAddedSite` bit(1) DEFAULT b'0' COMMENT '当配送方案为Pan EU时，Germany是否添加Poland, Czech Republic仓库站点',
  `warehouse_site` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '当配送方案为EFN时，亚马逊仓库站点',
  `dispatch_type` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '亚马逊配送方案:pan_EU,EFN',
  `vat_rate` decimal(10,2) DEFAULT '0.00' COMMENT 'VAT增值税费率',
  `fba_taxes` decimal(10,2) DEFAULT '0.00' COMMENT 'FBA GST/HST taxes',
  `hasDeclaredValue` bit(1) DEFAULT b'0' COMMENT '是否单独输入申报价值',
  `sipp` bit(1) DEFAULT b'0',
  `declared_value` decimal(10,4) DEFAULT '0.0000' COMMENT '申报价值',
  `gst_rate` decimal(10,2) DEFAULT '0.00' COMMENT '进口GST税率',
  `selling_GSTRate` decimal(10,2) DEFAULT '0.00' COMMENT '销售GST税率',
  `corporate_InRate` decimal(10,2) DEFAULT '0.00' COMMENT '企业所得税率',
  PRIMARY KEY (`id`),
  KEY `country` (`country`),
  KEY `FK_t_profitcfgcountry_t_profitcfg` (`profitid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='利润各国计算方案';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_pro_rcd_dimensions 结构
CREATE TABLE IF NOT EXISTS `t_pro_rcd_dimensions` (
  `id` bigint unsigned NOT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `length_units` char(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `width_units` char(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `height_units` char(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `weight_units` char(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_referralfee 结构
CREATE TABLE IF NOT EXISTS `t_referralfee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类型',
  `isMedia` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '是否媒介',
  `name` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL COMMENT '名称',
  `loweast` decimal(18,6) DEFAULT '0.000000' COMMENT '最低值',
  `top1` decimal(18,6) DEFAULT NULL COMMENT '第一最高值',
  `top2` decimal(18,6) DEFAULT NULL COMMENT '第二最高值',
  `top3` decimal(18,6) DEFAULT NULL COMMENT '第三最高值',
  `percent1` decimal(18,6) DEFAULT NULL COMMENT '第一百分比',
  `percent2` decimal(18,6) DEFAULT NULL COMMENT '第二百分比',
  `percent3` decimal(18,6) DEFAULT NULL COMMENT '第三百分比',
  `country` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '国家',
  `parent_id` int DEFAULT NULL COMMENT '父分类',
  PRIMARY KEY (`id`),
  UNIQUE KEY `parent_id` (`country`,`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=699 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='成本计算用，的亚马逊成本。采用父分类的方式，将所有国家的分类都放入此表。\r\n以美国的分类作为主要分类。如果美国没有此分类，则该分类依旧为美国添加，使用其他分类 类型的策略。\r\n当用户选择1 号分类，则其他子分类即改分类对应的其他国家的分类。\r\n ';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_report_requestrecord 结构
CREATE TABLE IF NOT EXISTS `t_report_requestrecord` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `sellerid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketPlaceId` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `reportId` bigint unsigned DEFAULT NULL,
  `reportType` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `reportRequestId` bigint unsigned DEFAULT NULL,
  `reportDocumentId` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `isnewest` bit(1) DEFAULT NULL,
  `availableDate` datetime DEFAULT NULL,
  `getnumber` int DEFAULT '0',
  `treatnumber` int DEFAULT '0',
  `lastupdate` datetime DEFAULT NULL,
  `report_processing_status` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `log` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin,
  `reportOptions` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`reportType`,`reportId`,`sellerid`),
  KEY `index3` (`lastupdate`),
  KEY `sellerid_marketPlaceId` (`sellerid`,`marketPlaceId`)
) ENGINE=InnoDB AUTO_INCREMENT=16097968530015756373 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_summaryall 结构
CREATE TABLE IF NOT EXISTS `t_summaryall` (
  `id` bigint unsigned NOT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `purchase_date` date NOT NULL,
  `sales_channel` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_status` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fulfillChannel` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '交付渠道',
  `discount` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `is_business_order` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `quantity` decimal(32,0) DEFAULT NULL,
  `ordernumber` bigint unsigned NOT NULL DEFAULT '0',
  `discount_amount` decimal(10,2) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`),
  KEY `index_1` (`amazonauthid`,`sales_channel`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_sys_operationlog 结构
CREATE TABLE IF NOT EXISTS `t_sys_operationlog` (
  `id` bigint unsigned NOT NULL,
  `time` datetime DEFAULT NULL,
  `ip` char(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `userid` bigint unsigned DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `logType` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `method` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `exceptionDetail` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `param` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_method_time_userid` (`method`(191),`time`,`userid`),
  KEY `time` (`userid`,`method`(191),`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon.t_variable_closing_fee 结构
CREATE TABLE IF NOT EXISTS `t_variable_closing_fee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `country` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `typeid` int DEFAULT NULL,
  `isMedia` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `logisticsId` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `format` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0.00',
  PRIMARY KEY (`id`),
  KEY `country` (`country`),
  KEY `FK_t_logistics_t_referralfee` (`typeid`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='物流方式';

-- 数据导出被取消选择。

-- 导出  表 db_amazon.undo_log 结构
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

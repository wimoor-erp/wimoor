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

-- 导出  事件 db_amazon_adv.db_timeout_process_kill 结构
DELIMITER //
CREATE EVENT `db_timeout_process_kill` ON SCHEDULE EVERY 1 DAY STARTS '2024-06-14 16:44:41' ON COMPLETION PRESERVE ENABLE DO BEGIN


declare v_sql varchar(500);
declare no_more_long_running_trx integer default 0;

declare c_tid cursor for
select concat ('kill ',trx_mysql_thread_id,';')
from information_schema.innodb_trx
where timestampdiff(second,trx_started,NOW()) >= 20
AND trx_query LIKE 'select%';

declare continue handler for not found set no_more_long_running_trx=1;

open c_tid;

repeat fetch c_tid into v_sql;

if no_more_long_running_trx != 1 then
set @v_sql=v_sql;
prepare stmt from @v_sql;
execute stmt;
SELECT @v_sql;
deallocate prepare stmt;
END if;

until no_more_long_running_trx end repeat;

close c_tid;

INSERT  into t_amazon_group
SELECT g.* FROM db_plum.t_amazon_group g
LEFT JOIN t_amazon_group m ON m.id=g.id
WHERE m.id IS NULL;
end//
DELIMITER ;

-- 导出  事件 db_amazon_adv.delete_long_data 结构
DELIMITER //
CREATE EVENT `delete_long_data` ON SCHEDULE EVERY 1 DAY STARTS '2024-06-14 16:45:01' ON COMPLETION PRESERVE ENABLE DO BEGIN


delete from t_amz_adv_report_request WHERE requesttime<DATE_SUB(NOW(),INTERVAL 65 DAY) AND opttime<DATE_SUB(NOW(),INTERVAL 65 DAY);
delete from t_amz_adv_report_request WHERE requesttime<DATE_SUB(NOW(),INTERVAL 8 DAY) and campaignType='sp';

DELETE FROM t_amz_adv_snapshot WHERE requesttime<DATE_SUB(NOW(),INTERVAL 2 DAY);

DELETE FROM t_amz_adv_sumalltype WHERE byday<DATE_SUB(NOW(),INTERVAL 7 DAY);

END//
DELIMITER ;

-- 导出  事件 db_amazon_adv.groupSync 结构
DELIMITER //
CREATE EVENT `groupSync` ON SCHEDULE EVERY 1 MINUTE STARTS '2025-02-17 11:22:13' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
insert into db_amazon_adv.t_amazon_group
SELECT g.* FROM db_amazon.t_amazon_group g
LEFT JOIN db_amazon_adv.t_amazon_group g2 ON g2.id=g.id
WHERE g2.id IS null;
END//
DELIMITER ;

-- 导出  事件 db_amazon_adv.syncData 结构
DELIMITER //
CREATE EVENT `syncData` ON SCHEDULE EVERY 1 DAY STARTS '2024-06-14 16:45:40' ON COMPLETION PRESERVE ENABLE DO BEGIN

update t_amz_adv_auth a
LEFT JOIN (SELECT advauthid FROM t_amz_adv_profile GROUP BY advauthid) v ON v.advauthid=a.id
SET  a.`disable`=1 ,a.disableTime=NOW()
WHERE a.`disable`=0  AND v.advauthid IS NULL;


replace into db_amazon_adv.t_amazon_group
SELECT * FROM db_amazon.t_amazon_group ;
 
 replace into db_amazon_adv.t_exchangerate
SELECT * FROM db_amazon.t_exchangerate;
replace into db_amazon_adv.t_exchangerate_customer
SELECT * FROM db_amazon.t_exchangerate_customer;

END//
DELIMITER ;

-- 导出  表 db_amazon_adv.t_advert_warning_keywords_data 结构
CREATE TABLE IF NOT EXISTS `t_advert_warning_keywords_data` (
  `keywordid` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `groupName` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `market` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `campaignName` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `adGroupName` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `keywordText` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `click0` int DEFAULT NULL,
  `impressions0` int DEFAULT NULL,
  `cost0` decimal(12,2) DEFAULT NULL,
  `orders0` int DEFAULT NULL,
  `sales0` int DEFAULT NULL,
  `click1` int DEFAULT NULL,
  `impressions1` int DEFAULT NULL,
  `cost1` decimal(12,2) DEFAULT NULL,
  `orders1` int DEFAULT NULL,
  `sales1` int DEFAULT NULL,
  `click2` int DEFAULT NULL,
  `impressions2` int DEFAULT NULL,
  `cost2` decimal(12,2) DEFAULT NULL,
  `orders2` int DEFAULT NULL,
  `sales2` int DEFAULT NULL,
  PRIMARY KEY (`keywordid`,`ftype`),
  KEY `ftype` (`ftype`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_advert_warning_product_data 结构
CREATE TABLE IF NOT EXISTS `t_advert_warning_product_data` (
  `adid` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `groupName` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `market` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `campaignName` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `adGroupName` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `click0` int DEFAULT NULL,
  `impressions0` int DEFAULT NULL,
  `cost0` decimal(12,2) DEFAULT NULL,
  `orders0` int DEFAULT NULL,
  `sales0` int DEFAULT NULL,
  `click1` int DEFAULT NULL,
  `impressions1` int DEFAULT NULL,
  `cost1` decimal(12,2) DEFAULT NULL,
  `orders1` int DEFAULT NULL,
  `sales1` int DEFAULT NULL,
  `click2` int DEFAULT NULL,
  `impressions2` int DEFAULT NULL,
  `cost2` decimal(12,2) DEFAULT NULL,
  `orders2` int DEFAULT NULL,
  `sales2` int DEFAULT NULL,
  PRIMARY KEY (`adid`,`ftype`),
  KEY `ftype` (`ftype`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_browsenode 结构
CREATE TABLE IF NOT EXISTS `t_adv_browsenode` (
  `id` int NOT NULL AUTO_INCREMENT,
  `node_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `node_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refinement` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_dimensions 结构
CREATE TABLE IF NOT EXISTS `t_adv_dimensions` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `length_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `width_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `height_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `weight_units` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_productgroup 结构
CREATE TABLE IF NOT EXISTS `t_adv_productgroup` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `referralfeeId` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_rank 结构
CREATE TABLE IF NOT EXISTS `t_adv_rank` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `asin` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `nodeid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pricerange` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `dim` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `offerprice` int DEFAULT NULL,
  `listprice` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `lowestnprice` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `categoryrank` int DEFAULT NULL,
  `imageurl` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `reviewsURL` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `totalOfferNew` int DEFAULT NULL,
  `lastupdate` datetime NOT NULL,
  `reviews` int DEFAULT NULL,
  `reviewAverage` float(5,1) DEFAULT NULL,
  `productgroup` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `estiMargin` float DEFAULT NULL,
  `estiProfit` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`asin`),
  KEY `Index 3` (`productgroup`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_rank_his 结构
CREATE TABLE IF NOT EXISTS `t_adv_rank_his` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `asin` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `nodeid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pricerange` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `dim` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `offerprice` int DEFAULT NULL,
  `listprice` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `lowestnprice` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `categoryrank` int DEFAULT NULL,
  `imageurl` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `reviewsURL` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `totalOfferNew` int DEFAULT NULL,
  `lastupdate` datetime NOT NULL,
  `reviews` int DEFAULT NULL,
  `reviewAverage` float DEFAULT NULL,
  `productgroup` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `estiMargin` float DEFAULT NULL,
  `estiProfit` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amazon_auth_market_performance 结构
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

-- 导出  表 db_amazon_adv.t_amazon_group 结构
CREATE TABLE IF NOT EXISTS `t_amazon_group` (
  `id` bigint unsigned NOT NULL,
  `name` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
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
  KEY `name_shopid` (`shopid`,`name`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_advert_invoices 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_invoices` (
  `id` bigint unsigned NOT NULL,
  `profileid` bigint unsigned DEFAULT NULL,
  `invoice_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fromDate` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `toDate` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `invoiceDate` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `invoiceDay` date DEFAULT NULL,
  `amountDue_amount` decimal(20,4) DEFAULT NULL,
  `amountDue_code` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `taxAmountDue_amount` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `taxAmountDue_code` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remainingAmountDue_amount` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remainingAmountDue_code` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remainingTaxAmountDue_amount` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remainingTaxAmountDue_code` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fees` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remainingFees` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `downloadable_documents` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `profileid_invoice_id_fromDate` (`profileid`,`invoice_id`,`fromDate`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='广告发票数据表';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_advert_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sellerid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `adId` bigint DEFAULT NULL,
  `asin` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bydate` date NOT NULL,
  `campaign_name` char(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `ad_Group_Name` char(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `clicks` int DEFAULT NULL,
  `impressions` int NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `spend` decimal(10,2) DEFAULT NULL,
  `cpc` decimal(10,2) DEFAULT NULL COMMENT 'Cost Per Click (CPC)	',
  `acos` double DEFAULT NULL COMMENT 'Total Advertising Cost of Sales (ACoS) ',
  `RoAS` double DEFAULT NULL COMMENT 'Total Return on Advertising Spend (RoAS)',
  `orders` int DEFAULT NULL,
  `spc` double DEFAULT NULL,
  `units` int DEFAULT NULL,
  `totalsales` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `mykey` (`bydate`,`sku`,`marketplaceid`,`sellerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_advert_report_summary 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report_summary` (
  `sellerid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `id` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `ctype` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'sp',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `asin` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bydate` date NOT NULL,
  `clicks` int DEFAULT NULL,
  `impressions` int NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `spend` decimal(10,2) DEFAULT NULL,
  `cpc` decimal(10,2) DEFAULT NULL COMMENT 'Cost Per Click (CPC)	',
  `acos` double DEFAULT NULL COMMENT 'Total Advertising Cost of Sales (ACoS) ',
  `RoAS` double DEFAULT NULL COMMENT 'Total Return on Advertising Spend (RoAS)',
  `orders` int DEFAULT NULL,
  `units` int DEFAULT NULL,
  `spc` double DEFAULT NULL,
  `totalsales` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`id`),
  UNIQUE KEY `sellerskum` (`sellerid`,`marketplaceid`,`sku`,`ctype`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_advert_report_summary_month 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report_summary_month` (
  `sellerid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `id` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `ctype` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'sp',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `asin` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bydate` date NOT NULL,
  `clicks` int DEFAULT NULL,
  `impressions` int NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `spend` decimal(10,2) DEFAULT NULL,
  `cpc` decimal(10,2) DEFAULT NULL COMMENT 'Cost Per Click (CPC)	',
  `acos` double DEFAULT NULL COMMENT 'Total Advertising Cost of Sales (ACoS) ',
  `RoAS` double DEFAULT NULL COMMENT 'Total Return on Advertising Spend (RoAS)',
  `orders` int DEFAULT NULL,
  `units` int DEFAULT NULL,
  `spc` double DEFAULT NULL,
  `totalsales` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`id`),
  UNIQUE KEY `sellerskum` (`sellerid`,`marketplaceid`,`sku`,`ctype`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_advert_report_summary_week 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report_summary_week` (
  `sellerid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `id` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `ctype` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'sp',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `asin` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bydate` date NOT NULL,
  `clicks` int DEFAULT NULL,
  `impressions` int NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `spend` decimal(10,2) DEFAULT NULL,
  `cpc` decimal(10,2) DEFAULT NULL COMMENT 'Cost Per Click (CPC)	',
  `acos` double DEFAULT NULL COMMENT 'Total Advertising Cost of Sales (ACoS) ',
  `RoAS` double DEFAULT NULL COMMENT 'Total Return on Advertising Spend (RoAS)',
  `orders` int DEFAULT NULL,
  `units` int DEFAULT NULL,
  `spc` double DEFAULT NULL,
  `totalsales` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`id`),
  UNIQUE KEY `sellerskum` (`sellerid`,`marketplaceid`,`sku`,`ctype`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_adgroups 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `defaultBid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`),
  KEY `profileid_name` (`name`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_adgroups_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups_hsa` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_adgroups_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups_sd` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bidOptimization` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `creativeType` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `defaultBid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`),
  KEY `profileid_name` (`name`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_ads_asin_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_ads_asin_hsa` (
  `adid` bigint unsigned NOT NULL,
  `asin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`adid`,`asin`),
  KEY `asin` (`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_ads_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_ads_hsa` (
  `adId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `creative` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `landingPage` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_api_pages 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_api_pages` (
  `profileid` bigint unsigned NOT NULL,
  `apipath` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `pages` int DEFAULT NULL,
  `nexttoken` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `flog` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`profileid`,`apipath`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_assets 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_assets` (
  `assetId` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `brandEntityId` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `mediaType` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opptime` datetime DEFAULT NULL,
  PRIMARY KEY (`profileid`,`brandEntityId`,`assetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_auth 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_auth` (
  `id` bigint unsigned NOT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `code` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `region` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `access_token` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refresh_token` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `token_type` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `disableTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `groupid` (`groupid`),
  KEY `shopid` (`shopid`),
  KEY `region` (`region`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_bidding_rules 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_bidding_rules` (
  `campaign_optimization_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `profileid` bigint unsigned DEFAULT NULL,
  `rule_name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `rule_type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `rule_sub_category` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `rule_category` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `recurrence` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `action` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `conditions` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`campaign_optimization_id`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_brand` (
  `profileid` bigint unsigned NOT NULL,
  `brandId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `brandEntityId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `brandRegistryName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`brandId`,`brandEntityId`,`profileid`),
  KEY `profileid_brandRegistryName` (`profileid`,`brandRegistryName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_brand_tml 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_brand_tml` (
  `brand` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sellingcategory` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sellername` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sellerID` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `country` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`brand`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_budget_rules 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_budget_rules` (
  `rule_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaigntype` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `rule_state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `duration` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `recurrence` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `rule_type` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `budget_increase_by` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(355) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `performance_measure_condition` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `rule_status` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`rule_id`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campaigns 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaigns` (
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `portfolioid` bigint unsigned DEFAULT NULL,
  `name` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '广告活动名称',
  `campaignType` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'sp 和 sb（hsa）',
  `dailyBudget` decimal(12,2) DEFAULT NULL COMMENT '每日预算',
  `targetingType` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '投放类型',
  `state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `premiumBidAdjustment` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '竞价',
  `bidding` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `startDate` date DEFAULT NULL COMMENT '开始时间',
  `endDate` date DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`campaignId`),
  KEY `idx_profileid_name` (`name`(191)),
  KEY `profileid` (`profileid`),
  KEY `state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campaigns_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaigns_hsa` (
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `portfolioid` bigint unsigned DEFAULT NULL,
  `name` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `budgetType` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `budget` decimal(12,2) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bidding` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `brandEntityId` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `goal` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `productLocation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tags` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `costType` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `smartDefault` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `spendingPolicy` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `landingPage` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `adFormat` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bidOptimization` bit(1) DEFAULT NULL,
  `isMultiAdGroupsEnabled` bit(1) DEFAULT NULL,
  `bidMultiplier` decimal(12,2) DEFAULT NULL,
  `bidAdjustments` varchar(350) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bidAdjustmentsByShopperSegment` varchar(350) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bidAdjustmentsByPlacement` varchar(350) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bidOptimizationStrategy` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `creative` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `servingStatus` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`profileid`),
  KEY `profileid_name` (`profileid`,`name`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campaigns_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaigns_sd` (
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `portfolioId` bigint unsigned DEFAULT NULL,
  `name` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `tactic` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `costtype` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `budgettype` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `budget` decimal(12,2) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`profileid`),
  KEY `profileid` (`profileid`,`name`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campaign_budget_rule 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaign_budget_rule` (
  `profileid` bigint unsigned NOT NULL,
  `campaignid` bigint unsigned NOT NULL,
  `rule_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`campaignid`,`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campkeywords_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campkeywords_negativa` (
  `profileid` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `keywordId` bigint unsigned NOT NULL,
  `keywordText` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `matchType` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campproduct_targe_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campproduct_targe_negativa` (
  `targetId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `expressionType` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `profileid` (`profileid`),
  KEY `adGroupId` (`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_group 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_group` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_keywords 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `keywordId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignType` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `keywordText` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `nativeLanguageKeyword` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `matchType` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid_keywordText` (`profileid`),
  KEY `adGroupId` (`adGroupId`),
  KEY `keywordText` (`keywordText`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_keywords_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords_hsa` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `keywordId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignType` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `keywordText` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `nativeLanguageKeyword` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `matchType` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid_keywordText` (`profileid`),
  KEY `adGroupId` (`adGroupId`),
  KEY `keywordText` (`keywordText`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_keywords_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords_negativa` (
  `keywordId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `keywordText` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `matchType` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `campaignType` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `nativeLanguageKeyword` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `nativeLanguageLocale` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_keywords_negativa_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords_negativa_hsa` (
  `keywordId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `keywordText` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `matchType` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `campaignType` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_media_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_media_hsa` (
  `mediaId` char(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned DEFAULT NULL,
  `status` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `statusMetadata` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `publishedMediaUrl` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned NOT NULL DEFAULT '0',
  `opttime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`mediaId`),
  KEY `profileid` (`profileid`,`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_operate_log 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_operate_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `profileid` bigint unsigned DEFAULT NULL,
  `campaignId` bigint unsigned DEFAULT NULL,
  `adGroupId` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `beanclasz` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `beforeobject` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `afterobject` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`,`opttime`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=520353 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_portfolios 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_portfolios` (
  `id` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `name` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT ' ',
  `policy` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT ' ',
  `currencyCode` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` decimal(12,2) DEFAULT NULL COMMENT ' ',
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `inBudget` bit(1) DEFAULT NULL,
  `startDate` date DEFAULT NULL COMMENT ' ',
  `endDate` date DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_productads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_productads` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `customText` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adId`),
  KEY `profileid` (`profileid`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid_sku` (`sku`),
  KEY `state` (`state`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_productads_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_productads_sd` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `adName` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `landingPageType` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `landingPageURL` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adId`),
  KEY `profileid` (`profileid`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid_sku` (`sku`),
  KEY `state` (`state`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe` (
  `targetId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `expressionType` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid_expression` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_hsa` (
  `targetId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `expressionType` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid_expression` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_negativa` (
  `targetId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `expressionType` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe_negativa_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_negativa_hsa` (
  `targetId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `expressionType` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `profileid` (`profileid`),
  KEY `campaignId` (`campaignId`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe_negativa_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_negativa_sd` (
  `targetId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `expressionType` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `profileid` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_sd` (
  `targetId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `expressionType` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `profileid_expression` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_profile 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_profile` (
  `id` bigint unsigned NOT NULL,
  `countryCode` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `currencyCode` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceId` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sellerId` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `advauthId` bigint unsigned DEFAULT NULL,
  `timezone` char(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `dailyBudget` decimal(20,5) DEFAULT NULL,
  `errorday` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `errorlog` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `marketplaceId` (`marketplaceId`),
  KEY `sellerId` (`sellerId`),
  KEY `authid` (`advauthId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_remark 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_remark` (
  `profileid` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adgroupId` bigint unsigned NOT NULL DEFAULT '0',
  `keywordId` bigint unsigned NOT NULL DEFAULT '0',
  `adId` bigint unsigned NOT NULL DEFAULT '0',
  `targetId` bigint unsigned NOT NULL DEFAULT '0',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`profileid`,`campaignId`,`adgroupId`,`keywordId`,`adId`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='广告历史记录备注';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_remind 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_remind` (
  `profileid` bigint unsigned NOT NULL,
  `campaignid` bigint unsigned NOT NULL,
  `adgroupid` bigint unsigned NOT NULL DEFAULT '0',
  `keywordid` bigint unsigned NOT NULL DEFAULT '0',
  `adid` bigint unsigned NOT NULL DEFAULT '0',
  `targetid` bigint unsigned NOT NULL DEFAULT '0',
  `recordtype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `cycle` int DEFAULT NULL COMMENT '1当天，7（7天）',
  `quota` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'click（点击数），ctr（点击率）,cost（花费），acos,cr(转化率）',
  `fcondition` int DEFAULT NULL COMMENT '1是超过，2是低于',
  `subtrahend` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL COMMENT '(cycle.quota） condition(>) amount',
  `iswarn` bit(1) NOT NULL DEFAULT b'0',
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`profileid`,`campaignid`,`adgroupid`,`keywordid`,`adid`,`targetid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='广告提醒';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_report_request 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_report_request` (
  `reportId` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `profileId` bigint unsigned NOT NULL,
  `recordType` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `status` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `statusDetails` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `reportType` int DEFAULT NULL,
  `campaignType` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `segment` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `creativeType` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `location` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fileSize` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `requesttime` datetime DEFAULT NULL,
  `treat_number` int unsigned DEFAULT '0',
  `treat_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `log` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  PRIMARY KEY (`reportId`,`profileId`,`recordType`),
  KEY `Index1` (`requesttime`,`treat_number`,`treat_status`,`isrun`),
  KEY `Index 2` (`profileId`,`recordType`,`campaignType`,`segment`),
  KEY `byday` (`startDate`),
  KEY `opttime` (`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='广告报表请求记录表';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_report_request_type 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_report_request_type` (
  `id` int NOT NULL DEFAULT '0',
  `campaigntype` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `reporttype` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `segment` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0',
  `activeType` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0',
  `metrics` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `bean` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `reponsetype` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `nomarket` char(245) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `disablevendor` bit(1) DEFAULT b'0',
  `disabled` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `campaigntype_reporttype_segment_activeType` (`campaigntype`,`reporttype`,`segment`,`activeType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_adgroups 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_adgroups` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`),
  KEY `adGroupId_campaignId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_adgroups_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_adgroups_attributed` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_adgroups_attributed_all 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_adgroups_attributed_all` (
  `adGroupId` bigint NOT NULL COMMENT '广告组ID',
  `bydate` date NOT NULL COMMENT '日期',
  `addToCart` int DEFAULT NULL COMMENT '加入购物车次数',
  `addToCartClicks` int DEFAULT NULL COMMENT '加入购物车点击次数',
  `addToCartRate` decimal(10,4) DEFAULT NULL COMMENT '加入购物车率',
  `addToList` int DEFAULT NULL COMMENT '加入列表次数',
  `addToListFromClicks` int DEFAULT NULL COMMENT '从点击加入列表次数',
  `qualifiedBorrows` int DEFAULT NULL COMMENT '合格借阅次数',
  `qualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击合格借阅次数',
  `royaltyQualifiedBorrows` int DEFAULT NULL COMMENT '版税合格借阅次数',
  `royaltyQualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击版税合格借阅次数',
  `brandedSearches` int DEFAULT NULL COMMENT '品牌搜索次数',
  `brandedSearchesClicks` int DEFAULT NULL COMMENT '品牌搜索点击次数',
  `campaignBudgetAmount` decimal(12,2) DEFAULT NULL COMMENT '广告活动预算金额',
  `campaignBudgetCurrencyCode` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '广告活动预算货币代码',
  `detailPageViews` int DEFAULT NULL COMMENT '详情页浏览量',
  `detailPageViewsClicks` int DEFAULT NULL COMMENT '详情页浏览点击次数',
  `eCPAddToCart` decimal(10,2) DEFAULT NULL COMMENT '加入购物车平均成本',
  `newToBrandDetailPageViewRate` decimal(10,4) DEFAULT NULL COMMENT '新品牌详情页浏览率',
  `newToBrandDetailPageViews` int DEFAULT NULL COMMENT '新品牌详情页浏览量',
  `newToBrandDetailPageViewsClicks` int DEFAULT NULL COMMENT '新品牌详情页浏览点击次数',
  `newToBrandECPDetailPageView` decimal(10,2) DEFAULT NULL COMMENT '新品牌详情页浏览平均成本',
  `newToBrandPurchases` int DEFAULT NULL COMMENT '新品牌购买次数',
  `newToBrandPurchasesClicks` int DEFAULT NULL COMMENT '新品牌购买点击次数',
  `newToBrandPurchasesPercentage` decimal(10,4) DEFAULT NULL COMMENT '新品牌购买百分比',
  `newToBrandPurchasesRate` decimal(10,4) DEFAULT NULL COMMENT '新品牌购买率',
  `newToBrandSales` decimal(12,2) DEFAULT NULL COMMENT '新品牌销售额',
  `newToBrandSalesClicks` int DEFAULT NULL COMMENT '新品牌销售点击次数',
  `newToBrandSalesPercentage` decimal(10,4) DEFAULT NULL COMMENT '新品牌销售百分比',
  `newToBrandUnitsSold` int DEFAULT NULL COMMENT '新品牌销售单位数',
  `newToBrandUnitsSoldClicks` int DEFAULT NULL COMMENT '新品牌销售单位点击次数',
  `newToBrandUnitsSoldPercentage` decimal(10,4) DEFAULT NULL COMMENT '新品牌销售单位百分比',
  `purchases` int DEFAULT NULL COMMENT '购买次数',
  `purchasesClicks` int DEFAULT NULL COMMENT '购买点击次数',
  `purchasesPromoted` int DEFAULT NULL COMMENT '促销购买次数',
  `sales` decimal(12,2) DEFAULT NULL COMMENT '销售额',
  `salesClicks` int DEFAULT NULL COMMENT '销售点击次数',
  `salesPromoted` decimal(12,2) DEFAULT NULL COMMENT '促销销售额',
  `unitsSold` int DEFAULT NULL COMMENT '销售单位数',
  `unitsSoldClicks` int DEFAULT NULL COMMENT '销售单位点击次数',
  `video5SecondViewRate` decimal(10,4) DEFAULT NULL COMMENT '5秒视频观看率',
  `video5SecondViews` int DEFAULT NULL COMMENT '5秒视频观看次数',
  `videoCompleteViews` int DEFAULT NULL COMMENT '完整视频观看次数',
  `videoFirstQuartileViews` int DEFAULT NULL COMMENT '视频第一四分位观看次数',
  `videoMidpointViews` int DEFAULT NULL COMMENT '视频中点观看次数',
  `videoThirdQuartileViews` int DEFAULT NULL COMMENT '视频第三四分位观看次数',
  `videoUnmutes` int DEFAULT NULL COMMENT '视频取消静音次数',
  `viewabilityRate` decimal(10,4) DEFAULT NULL COMMENT '可视率',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`adGroupId`,`bydate`),
  KEY `idx_bydate` (`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='亚马逊广告HSA广告组归因报告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_adgroups_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_adgroups_brand` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedDetailPageViewsClicks14d` int unsigned DEFAULT '0',
  `attributedOrdersNewToBrand14d` int unsigned DEFAULT '0',
  `attributedOrdersNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedOrderRateNewToBrand14d` int unsigned DEFAULT '0',
  `attributedSalesNewToBrand14d` int unsigned DEFAULT '0',
  `attributedSalesNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedUnitsOrderedNewToBrand14d` int unsigned DEFAULT '0',
  `attributedUnitsOrderedNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `unitsSold14d` int unsigned DEFAULT '0',
  `dpv14d` int unsigned DEFAULT '0',
  `attributedBrandedSearches14d` int unsigned DEFAULT '0',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_adgroups_video 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_adgroups_video` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewableImpressions` int unsigned DEFAULT '0',
  `videoFirstQuartileViews` int unsigned DEFAULT '0',
  `videoMidpointViews` int unsigned DEFAULT '0',
  `videoThirdQuartileViews` int unsigned DEFAULT '0',
  `videoCompleteViews` int unsigned DEFAULT '0',
  `video5SecondViews` int unsigned DEFAULT '0',
  `video5SecondViewRate` decimal(10,2) unsigned DEFAULT '0.00',
  `videoUnmutes` int unsigned DEFAULT '0',
  `vtr` decimal(12,2) unsigned DEFAULT '0.00',
  `vctr` decimal(12,2) unsigned DEFAULT '0.00',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`),
  KEY `campaignId_bydate` (`adGroupId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_ads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_ads` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `keywordBid` decimal(10,2) DEFAULT '0.00',
  `impressions` int DEFAULT '0',
  `clicks` int DEFAULT '0',
  `cost` decimal(12,2) DEFAULT '0.00',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`),
  KEY `profileid` (`profileid`,`campaignId`,`adGroupId`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_ads_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_ads_attributed` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_ads_attributed_all 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_ads_attributed_all` (
  `adId` bigint unsigned NOT NULL COMMENT '广告ID',
  `bydate` date NOT NULL COMMENT '日期',
  `addToCart` int DEFAULT NULL COMMENT '加入购物车次数',
  `addToCartClicks` int DEFAULT NULL COMMENT '加入购物车点击次数',
  `addToCartRate` decimal(10,2) DEFAULT NULL COMMENT '加入购物车率',
  `addToList` int DEFAULT NULL COMMENT '加入列表次数',
  `qualifiedBorrows` int DEFAULT NULL COMMENT '合格借阅次数',
  `royaltyQualifiedBorrows` int DEFAULT NULL COMMENT '版税合格借阅次数',
  `addToListFromClicks` int DEFAULT NULL COMMENT '从点击加入列表次数',
  `qualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击合格借阅次数',
  `royaltyQualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击版税合格借阅次数',
  `brandedSearches` int DEFAULT NULL COMMENT '品牌搜索次数',
  `brandedSearchesClicks` int DEFAULT NULL COMMENT '品牌搜索点击次数',
  `campaignBudgetAmount` decimal(10,2) DEFAULT NULL COMMENT '广告活动预算金额',
  `campaignBudgetCurrencyCode` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '广告活动预算货币代码',
  `detailPageViews` int DEFAULT NULL COMMENT '详情页浏览量',
  `detailPageViewsClicks` int DEFAULT NULL COMMENT '详情页浏览点击次数',
  `eCPAddToCart` decimal(10,2) DEFAULT NULL COMMENT '加入购物车平均成本',
  `newToBrandDetailPageViewRate` decimal(10,2) DEFAULT NULL COMMENT '新品牌详情页浏览率',
  `newToBrandDetailPageViews` int DEFAULT NULL COMMENT '新品牌详情页浏览量',
  `newToBrandDetailPageViewsClicks` int DEFAULT NULL COMMENT '新品牌详情页浏览点击次数',
  `newToBrandECPDetailPageView` decimal(10,2) DEFAULT NULL COMMENT '新品牌详情页浏览平均成本',
  `newToBrandPurchases` int DEFAULT NULL COMMENT '新品牌购买次数',
  `newToBrandPurchasesClicks` int DEFAULT NULL COMMENT '新品牌购买点击次数',
  `newToBrandPurchasesPercentage` decimal(10,2) DEFAULT NULL COMMENT '新品牌购买百分比',
  `newToBrandPurchasesRate` decimal(10,2) DEFAULT NULL COMMENT '新品牌购买率',
  `newToBrandSales` decimal(10,2) DEFAULT NULL COMMENT '新品牌销售额',
  `newToBrandSalesClicks` int DEFAULT NULL COMMENT '新品牌销售点击次数',
  `newToBrandSalesPercentage` decimal(10,2) DEFAULT NULL COMMENT '新品牌销售百分比',
  `newToBrandUnitsSold` int DEFAULT NULL COMMENT '新品牌销售单位数',
  `newToBrandUnitsSoldClicks` int DEFAULT NULL COMMENT '新品牌销售单位点击次数',
  `newToBrandUnitsSoldPercentage` decimal(10,2) DEFAULT NULL COMMENT '新品牌销售单位百分比',
  `purchases` int DEFAULT NULL COMMENT '购买次数',
  `purchasesClicks` int DEFAULT NULL COMMENT '购买点击次数',
  `purchasesPromoted` int DEFAULT NULL COMMENT '促销购买次数',
  `sales` decimal(10,2) DEFAULT NULL COMMENT '销售额',
  `salesClicks` int DEFAULT NULL COMMENT '销售点击次数',
  `salesPromoted` decimal(10,2) DEFAULT NULL COMMENT '促销销售额',
  `unitsSold` int DEFAULT NULL COMMENT '销售单位数',
  `unitsSoldClicks` int DEFAULT NULL COMMENT '销售单位点击次数',
  `video5SecondViewRate` decimal(10,2) DEFAULT NULL COMMENT '5秒视频观看率',
  `video5SecondViews` int DEFAULT NULL COMMENT '5秒视频观看次数',
  `videoCompleteViews` int DEFAULT NULL COMMENT '完整视频观看次数',
  `videoFirstQuartileViews` int DEFAULT NULL COMMENT '视频第一四分位观看次数',
  `videoMidpointViews` int DEFAULT NULL COMMENT '视频中点观看次数',
  `videoThirdQuartileViews` int DEFAULT NULL COMMENT '视频第三四分位观看次数',
  `videoUnmutes` int DEFAULT NULL COMMENT '视频取消静音次数',
  `viewabilityRate` decimal(10,2) DEFAULT NULL COMMENT '可视率',
  `viewableImpressions` int DEFAULT NULL COMMENT '可视展示次数',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`adId`,`bydate`),
  KEY `idx_bydate` (`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='亚马逊广告HSA广告归因报告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_ads_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_ads_brand` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedDetailPageViewsClicks14d` int DEFAULT NULL,
  `attributedOrdersNewToBrand14d` int DEFAULT NULL,
  `attributedOrdersNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `attributedOrderRateNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int DEFAULT NULL,
  `attributedUnitsOrderedNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `unitsSold14d` int DEFAULT NULL,
  `dpv14d` int DEFAULT NULL,
  `vctr` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedBrandedSearches14d` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_ads_video 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_ads_video` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewableImpressions` int unsigned DEFAULT '0',
  `videoFirstQuartileViews` int unsigned DEFAULT '0',
  `videoMidpointViews` int unsigned DEFAULT '0',
  `videoThirdQuartileViews` int unsigned DEFAULT '0',
  `videoCompleteViews` int unsigned DEFAULT '0',
  `video5SecondViews` int unsigned DEFAULT '0',
  `video5SecondViewRate` decimal(10,2) unsigned DEFAULT '0.00',
  `videoUnmutes` int unsigned DEFAULT '0',
  `vtr` decimal(12,2) unsigned DEFAULT '0.00',
  `vctr` decimal(12,2) unsigned DEFAULT '0.00',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`adId`),
  KEY `campaignId_bydate` (`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignBudget` decimal(12,2) DEFAULT NULL,
  `impressions` int unsigned NOT NULL DEFAULT '0',
  `clicks` int unsigned NOT NULL DEFAULT '0',
  `cost` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `topOfSearchImpressionShare` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`),
  KEY `campaignId_profileid` (`profileid`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_attributed` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`bydate`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_attributed_all 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_attributed_all` (
  `campaignId` bigint unsigned NOT NULL COMMENT '广告活动ID',
  `bydate` date NOT NULL COMMENT '日期',
  `addToCart` int DEFAULT NULL COMMENT '加入购物车次数',
  `addToCartClicks` int DEFAULT NULL COMMENT '加入购物车点击次数',
  `addToCartRate` decimal(10,4) DEFAULT NULL COMMENT '加入购物车率',
  `addToList` int DEFAULT NULL COMMENT '加入列表次数',
  `addToListFromClicks` int DEFAULT NULL COMMENT '从点击加入列表次数',
  `qualifiedBorrows` int DEFAULT NULL COMMENT '合格借阅次数',
  `qualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击合格借阅次数',
  `royaltyQualifiedBorrows` int DEFAULT NULL COMMENT '版税合格借阅次数',
  `royaltyQualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击版税合格借阅次数',
  `brandedSearches` int DEFAULT NULL COMMENT '品牌搜索次数',
  `brandedSearchesClicks` int DEFAULT NULL COMMENT '品牌搜索点击次数',
  `campaignBudgetAmount` decimal(12,2) DEFAULT NULL COMMENT '广告活动预算金额',
  `campaignBudgetCurrencyCode` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '广告活动预算货币代码',
  `detailPageViews` int DEFAULT NULL COMMENT '详情页浏览量',
  `detailPageViewsClicks` int DEFAULT NULL COMMENT '详情页浏览点击次数',
  `eCPAddToCart` decimal(10,2) DEFAULT NULL COMMENT '加入购物车平均成本',
  `newToBrandDetailPageViewRate` decimal(10,4) DEFAULT NULL COMMENT '新品牌详情页浏览率',
  `newToBrandDetailPageViews` int DEFAULT NULL COMMENT '新品牌详情页浏览量',
  `newToBrandDetailPageViewsClicks` int DEFAULT NULL COMMENT '新品牌详情页浏览点击次数',
  `newToBrandECPDetailPageView` decimal(10,2) DEFAULT NULL COMMENT '新品牌详情页浏览平均成本',
  `newToBrandPurchases` int DEFAULT NULL COMMENT '新品牌购买次数',
  `newToBrandPurchasesClicks` int DEFAULT NULL COMMENT '新品牌购买点击次数',
  `newToBrandPurchasesPercentage` decimal(10,4) DEFAULT NULL COMMENT '新品牌购买百分比',
  `newToBrandPurchasesRate` decimal(10,4) DEFAULT NULL COMMENT '新品牌购买率',
  `newToBrandSales` decimal(12,2) DEFAULT NULL COMMENT '新品牌销售额',
  `newToBrandSalesClicks` int DEFAULT NULL COMMENT '新品牌销售点击次数',
  `newToBrandSalesPercentage` decimal(10,4) DEFAULT NULL COMMENT '新品牌销售百分比',
  `newToBrandUnitsSold` int DEFAULT NULL COMMENT '新品牌销售单位数',
  `newToBrandUnitsSoldClicks` int DEFAULT NULL COMMENT '新品牌销售单位点击次数',
  `newToBrandUnitsSoldPercentage` decimal(10,4) DEFAULT NULL COMMENT '新品牌销售单位百分比',
  `purchases` int DEFAULT NULL COMMENT '购买次数',
  `purchasesClicks` int DEFAULT NULL COMMENT '购买点击次数',
  `purchasesPromoted` int DEFAULT NULL COMMENT '促销购买次数',
  `sales` decimal(12,2) DEFAULT NULL COMMENT '销售额',
  `salesClicks` int DEFAULT NULL COMMENT '销售点击次数',
  `salesPromoted` decimal(12,2) DEFAULT NULL COMMENT '促销销售额',
  `topOfSearchImpressionShare` decimal(10,4) DEFAULT NULL COMMENT '搜索顶部展示份额',
  `unitsSold` int DEFAULT NULL COMMENT '销售单位数',
  `unitsSoldClicks` int DEFAULT NULL COMMENT '销售单位点击次数',
  `video5SecondViewRate` decimal(10,4) DEFAULT NULL COMMENT '5秒视频观看率',
  `video5SecondViews` int DEFAULT NULL COMMENT '5秒视频观看次数',
  `videoCompleteViews` int DEFAULT NULL COMMENT '完整视频观看次数',
  `videoFirstQuartileViews` int DEFAULT NULL COMMENT '视频第一四分位观看次数',
  `videoMidpointViews` int DEFAULT NULL COMMENT '视频中点观看次数',
  `videoThirdQuartileViews` int DEFAULT NULL COMMENT '视频第三四分位观看次数',
  `videoUnmutes` int DEFAULT NULL COMMENT '视频取消静音次数',
  `viewabilityRate` decimal(10,4) DEFAULT NULL COMMENT '可视率',
  `viewableImpressions` int DEFAULT NULL COMMENT '可视展示次数',
  `viewClickThroughRate` decimal(10,4) DEFAULT NULL COMMENT '可视点击通过率',
  `oipttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`campaignId`,`bydate`),
  KEY `idx_bydate` (`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='亚马逊广告HSA广告活动归因报告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_brand` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedDetailPageViewsClicks14d` int unsigned DEFAULT '0',
  `attributedOrdersNewToBrand14d` int unsigned DEFAULT '0',
  `attributedOrdersNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedOrderRateNewToBrand14d` int unsigned DEFAULT '0',
  `attributedSalesNewToBrand14d` int unsigned DEFAULT '0',
  `attributedSalesNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedUnitsOrderedNewToBrand14d` int unsigned DEFAULT '0',
  `attributedUnitsOrderedNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `unitsSold14d` int unsigned DEFAULT '0',
  `dpv14d` int unsigned DEFAULT '0',
  `attributedBrandedSearches14d` int unsigned DEFAULT '0',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_place 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_place` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignBudget` decimal(12,2) DEFAULT NULL,
  `impressions` int DEFAULT '0',
  `clicks` int DEFAULT '0',
  `cost` decimal(12,2) DEFAULT '0.00',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`),
  KEY `profileid` (`profileid`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_place_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_place_attributed` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int unsigned NOT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) DEFAULT '0.00',
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_place_attributed_all 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_place_attributed_all` (
  `campaignId` bigint unsigned NOT NULL COMMENT '广告活动ID',
  `bydate` date NOT NULL COMMENT '日期',
  `placementid` int unsigned NOT NULL,
  `addToCart` int DEFAULT NULL COMMENT '加入购物车次数',
  `addToCartClicks` int DEFAULT NULL COMMENT '加入购物车点击次数',
  `addToCartRate` decimal(10,2) DEFAULT NULL COMMENT '加入购物车率',
  `addToList` int DEFAULT NULL COMMENT '加入列表次数',
  `addToListFromClicks` int DEFAULT NULL COMMENT '从点击加入列表次数',
  `qualifiedBorrows` int DEFAULT NULL COMMENT '合格借阅次数',
  `qualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击合格借阅次数',
  `royaltyQualifiedBorrows` int DEFAULT NULL COMMENT '版税合格借阅次数',
  `royaltyQualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击版税合格借阅次数',
  `brandedSearches` int DEFAULT NULL COMMENT '品牌搜索次数',
  `brandedSearchesClicks` int DEFAULT NULL COMMENT '品牌搜索点击次数',
  `campaignBudgetAmount` decimal(10,2) DEFAULT NULL COMMENT '广告活动预算金额',
  `campaignBudgetCurrencyCode` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '广告活动预算货币代码',
  `detailPageViews` int DEFAULT NULL COMMENT '详情页浏览量',
  `detailPageViewsClicks` int DEFAULT NULL COMMENT '详情页浏览点击次数',
  `eCPAddToCart` decimal(10,2) DEFAULT NULL COMMENT '加入购物车平均成本',
  `newToBrandDetailPageViewRate` decimal(10,2) DEFAULT NULL COMMENT '新品牌详情页浏览率',
  `newToBrandDetailPageViews` int DEFAULT NULL COMMENT '新品牌详情页浏览量',
  `newToBrandDetailPageViewsClicks` int DEFAULT NULL COMMENT '新品牌详情页浏览点击次数',
  `newToBrandECPDetailPageView` decimal(10,2) DEFAULT NULL COMMENT '新品牌详情页浏览平均成本',
  `newToBrandPurchases` int DEFAULT NULL COMMENT '新品牌购买次数',
  `newToBrandPurchasesClicks` int DEFAULT NULL COMMENT '新品牌购买点击次数',
  `newToBrandPurchasesPercentage` decimal(10,2) DEFAULT NULL COMMENT '新品牌购买百分比',
  `newToBrandPurchasesRate` decimal(10,2) DEFAULT NULL COMMENT '新品牌购买率',
  `newToBrandSales` decimal(10,2) DEFAULT NULL COMMENT '新品牌销售额',
  `newToBrandSalesClicks` int DEFAULT NULL COMMENT '新品牌销售点击次数',
  `newToBrandSalesPercentage` decimal(10,2) DEFAULT NULL COMMENT '新品牌销售百分比',
  `newToBrandUnitsSold` int DEFAULT NULL COMMENT '新品牌销售单位数',
  `newToBrandUnitsSoldClicks` int DEFAULT NULL COMMENT '新品牌销售单位点击次数',
  `newToBrandUnitsSoldPercentage` decimal(10,2) DEFAULT NULL COMMENT '新品牌销售单位百分比',
  `purchases` int DEFAULT NULL COMMENT '购买次数',
  `purchasesClicks` int DEFAULT NULL COMMENT '购买点击次数',
  `purchasesPromoted` int DEFAULT NULL COMMENT '促销购买次数',
  `sales` decimal(10,2) DEFAULT NULL COMMENT '销售额',
  `salesClicks` int DEFAULT NULL COMMENT '销售点击次数',
  `salesPromoted` decimal(10,2) DEFAULT NULL COMMENT '促销销售额',
  `unitsSold` int DEFAULT NULL COMMENT '销售单位数',
  `unitsSoldClicks` int DEFAULT NULL COMMENT '销售单位点击次数',
  `video5SecondViewRate` decimal(10,2) DEFAULT NULL COMMENT '5秒视频观看率',
  `video5SecondViews` int DEFAULT NULL COMMENT '5秒视频观看次数',
  `videoCompleteViews` int DEFAULT NULL COMMENT '完整视频观看次数',
  `videoFirstQuartileViews` int DEFAULT NULL COMMENT '视频第一四分位观看次数',
  `videoMidpointViews` int DEFAULT NULL COMMENT '视频中点观看次数',
  `videoThirdQuartileViews` int DEFAULT NULL COMMENT '视频第三四分位观看次数',
  `videoUnmutes` int DEFAULT NULL COMMENT '视频取消静音次数',
  `viewabilityRate` decimal(10,2) DEFAULT NULL COMMENT '可视率',
  `viewableImpressions` int DEFAULT NULL COMMENT '可视展示次数',
  `viewClickThroughRate` decimal(10,2) DEFAULT NULL COMMENT '可视点击通过率',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`campaignId`,`bydate`),
  KEY `idx_bydate` (`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='亚马逊广告HSA广告活动位置归因报告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_place_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_place_brand` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int unsigned NOT NULL,
  `attributedDetailPageViewsClicks14d` int unsigned DEFAULT '0',
  `attributedOrdersNewToBrand14d` int unsigned DEFAULT '0',
  `attributedOrdersNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedOrderRateNewToBrand14d` int unsigned DEFAULT '0',
  `attributedSalesNewToBrand14d` decimal(20,6) unsigned DEFAULT NULL,
  `attributedSalesNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedUnitsOrderedNewToBrand14d` int unsigned DEFAULT '0',
  `attributedUnitsOrderedNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `unitsSold14d` int unsigned DEFAULT '0',
  `dpv14d` int unsigned DEFAULT '0',
  `attributedBrandedSearches14d` int unsigned DEFAULT '0',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_place_video 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_place_video` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int unsigned NOT NULL,
  `viewableImpressions` int unsigned DEFAULT '0',
  `videoFirstQuartileViews` int unsigned DEFAULT '0',
  `videoMidpointViews` int unsigned DEFAULT '0',
  `videoThirdQuartileViews` int unsigned DEFAULT '0',
  `videoCompleteViews` int unsigned DEFAULT '0',
  `video5SecondViews` int unsigned DEFAULT '0',
  `video5SecondViewRate` decimal(10,2) unsigned DEFAULT '0.00',
  `videoUnmutes` int unsigned DEFAULT '0',
  `vtr` decimal(12,2) unsigned DEFAULT '0.00',
  `vctr` decimal(12,2) unsigned DEFAULT '0.00',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_video 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_video` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewableImpressions` int unsigned DEFAULT '0',
  `videoFirstQuartileViews` int unsigned DEFAULT '0',
  `videoMidpointViews` int unsigned DEFAULT '0',
  `videoThirdQuartileViews` int unsigned DEFAULT '0',
  `videoCompleteViews` int unsigned DEFAULT '0',
  `video5SecondViews` int unsigned DEFAULT '0',
  `video5SecondViewRate` decimal(10,2) unsigned DEFAULT '0.00',
  `videoUnmutes` int unsigned DEFAULT '0',
  `vtr` decimal(12,2) unsigned DEFAULT '0.00',
  `vctr` decimal(12,2) unsigned DEFAULT '0.00',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`campaignId`),
  KEY `campaignId_bydate` (`campaignId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_keywords 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_keywords` (
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `keywordBid` decimal(10,2) DEFAULT '0.00',
  `impressions` int DEFAULT '0',
  `clicks` int DEFAULT '0',
  `cost` decimal(12,2) DEFAULT '0.00',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`),
  KEY `profileid` (`profileid`,`campaignId`,`adGroupId`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_keywords_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_keywords_attributed` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_keywords_attributed_all 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_keywords_attributed_all` (
  `keywordId` bigint unsigned NOT NULL COMMENT '关键词ID',
  `bydate` date NOT NULL COMMENT '日期',
  `addToCart` int DEFAULT NULL COMMENT '加入购物车次数',
  `addToCartClicks` int DEFAULT NULL COMMENT '加入购物车点击次数',
  `addToCartRate` decimal(10,2) DEFAULT NULL COMMENT '加入购物车率',
  `addToList` int DEFAULT NULL COMMENT '加入列表次数',
  `addToListFromClicks` int DEFAULT NULL COMMENT '从点击加入列表次数',
  `qualifiedBorrows` int DEFAULT NULL COMMENT '合格借阅次数',
  `qualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击合格借阅次数',
  `royaltyQualifiedBorrows` int DEFAULT NULL COMMENT '版税合格借阅次数',
  `royaltyQualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击版税合格借阅次数',
  `brandedSearches` int DEFAULT NULL COMMENT '品牌搜索次数',
  `brandedSearchesClicks` int DEFAULT NULL COMMENT '品牌搜索点击次数',
  `campaignBudgetAmount` decimal(10,2) DEFAULT NULL COMMENT '广告活动预算金额',
  `campaignBudgetCurrencyCode` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '广告活动预算货币代码',
  `detailPageViews` int DEFAULT NULL COMMENT '详情页浏览量',
  `detailPageViewsClicks` int DEFAULT NULL COMMENT '详情页浏览点击次数',
  `eCPAddToCart` decimal(10,2) DEFAULT NULL COMMENT '加入购物车平均成本',
  `keywordBid` decimal(10,2) DEFAULT NULL COMMENT '关键词出价',
  `newToBrandDetailPageViewRate` decimal(10,2) DEFAULT NULL COMMENT '新品牌详情页浏览率',
  `newToBrandDetailPageViews` int DEFAULT NULL COMMENT '新品牌详情页浏览量',
  `newToBrandDetailPageViewsClicks` int DEFAULT NULL COMMENT '新品牌详情页浏览点击次数',
  `newToBrandECPDetailPageView` decimal(10,2) DEFAULT NULL COMMENT '新品牌详情页浏览平均成本',
  `newToBrandPurchases` int DEFAULT NULL COMMENT '新品牌购买次数',
  `newToBrandPurchasesClicks` int DEFAULT NULL COMMENT '新品牌购买点击次数',
  `newToBrandPurchasesPercentage` decimal(10,2) DEFAULT NULL COMMENT '新品牌购买百分比',
  `newToBrandPurchasesRate` decimal(10,2) DEFAULT NULL COMMENT '新品牌购买率',
  `newToBrandSales` decimal(10,2) DEFAULT NULL COMMENT '新品牌销售额',
  `newToBrandSalesClicks` int DEFAULT NULL COMMENT '新品牌销售点击次数',
  `newToBrandSalesPercentage` decimal(10,2) DEFAULT NULL COMMENT '新品牌销售百分比',
  `newToBrandUnitsSold` int DEFAULT NULL COMMENT '新品牌销售单位数',
  `newToBrandUnitsSoldClicks` int DEFAULT NULL COMMENT '新品牌销售单位点击次数',
  `newToBrandUnitsSoldPercentage` decimal(10,2) DEFAULT NULL COMMENT '新品牌销售单位百分比',
  `purchases` int DEFAULT NULL COMMENT '购买次数',
  `purchasesClicks` int DEFAULT NULL COMMENT '购买点击次数',
  `purchasesPromoted` int DEFAULT NULL COMMENT '促销购买次数',
  `sales` decimal(10,2) DEFAULT NULL COMMENT '销售额',
  `salesClicks` int DEFAULT NULL COMMENT '销售点击次数',
  `salesPromoted` decimal(10,2) DEFAULT NULL COMMENT '促销销售额',
  `topOfSearchImpressionShare` decimal(10,2) DEFAULT NULL COMMENT '搜索顶部展示份额',
  `unitsSold` int DEFAULT NULL COMMENT '销售单位数',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`keywordId`,`bydate`),
  KEY `idx_bydate` (`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='亚马逊广告HSA关键词归因报告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_keywords_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_keywords_brand` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedDetailPageViewsClicks14d` int DEFAULT NULL,
  `attributedOrdersNewToBrand14d` int DEFAULT NULL,
  `attributedOrdersNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `attributedOrderRateNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int DEFAULT NULL,
  `attributedUnitsOrderedNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `unitsSold14d` int DEFAULT NULL,
  `dpv14d` int DEFAULT NULL,
  `attributedBrandedSearches14d` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_keywords_query 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_keywords_query` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `queryid` bigint unsigned NOT NULL DEFAULT '0',
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`queryid`,`keywordId`),
  KEY `adGroupId_campaignId_profileid` (`profileid`,`campaignId`,`adGroupId`),
  KEY `queryid` (`queryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_keywords_video 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_keywords_video` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewableImpressions` int DEFAULT NULL,
  `videoFirstQuartileViews` int DEFAULT NULL,
  `videoMidpointViews` int DEFAULT NULL,
  `videoThirdQuartileViews` int DEFAULT NULL,
  `videoCompleteViews` int DEFAULT NULL,
  `video5SecondViews` int DEFAULT NULL,
  `video5SecondViewRate` decimal(12,2) DEFAULT NULL,
  `videoUnmutes` int DEFAULT NULL,
  `vtr` decimal(12,2) DEFAULT NULL,
  `vctr` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_product_targets 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_product_targets` (
  `targetId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`),
  KEY `campaignId_adGroupId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_product_targets_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_product_targets_attributed` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions14d` decimal(10,2) DEFAULT NULL,
  `attributedConversions14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,0) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_product_targets_attributed_all 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_product_targets_attributed_all` (
  `targetingId` bigint unsigned NOT NULL COMMENT '商品定位ID',
  `bydate` date NOT NULL COMMENT '日期',
  `addToCart` int DEFAULT NULL COMMENT '加入购物车次数',
  `addToCartClicks` int DEFAULT NULL COMMENT '加入购物车点击次数',
  `addToCartRate` decimal(10,2) DEFAULT NULL COMMENT '加入购物车率',
  `addToList` int DEFAULT NULL COMMENT '加入列表次数',
  `addToListFromClicks` int DEFAULT NULL COMMENT '从点击加入列表次数',
  `qualifiedBorrows` int DEFAULT NULL COMMENT '合格借阅次数',
  `qualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击合格借阅次数',
  `royaltyQualifiedBorrows` int DEFAULT NULL COMMENT '版税合格借阅次数',
  `royaltyQualifiedBorrowsFromClicks` int DEFAULT NULL COMMENT '从点击版税合格借阅次数',
  `brandedSearches` int DEFAULT NULL COMMENT '品牌搜索次数',
  `brandedSearchesClicks` int DEFAULT NULL COMMENT '品牌搜索点击次数',
  `campaignBudgetAmount` decimal(10,2) DEFAULT NULL COMMENT '广告活动预算金额',
  `campaignBudgetCurrencyCode` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '广告活动预算货币代码',
  `detailPageViews` int DEFAULT NULL COMMENT '详情页浏览量',
  `detailPageViewsClicks` int DEFAULT NULL COMMENT '详情页浏览点击次数',
  `eCPAddToCart` decimal(10,2) DEFAULT NULL COMMENT '加入购物车平均成本',
  `matchType` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '匹配类型',
  `newToBrandDetailPageViewRate` decimal(10,2) DEFAULT NULL COMMENT '新品牌详情页浏览率',
  `newToBrandDetailPageViews` int DEFAULT NULL COMMENT '新品牌详情页浏览量',
  `newToBrandDetailPageViewsClicks` int DEFAULT NULL COMMENT '新品牌详情页浏览点击次数',
  `newToBrandECPDetailPageView` decimal(10,2) DEFAULT NULL COMMENT '新品牌详情页浏览平均成本',
  `newToBrandPurchases` int DEFAULT NULL COMMENT '新品牌购买次数',
  `newToBrandPurchasesClicks` int DEFAULT NULL COMMENT '新品牌购买点击次数',
  `newToBrandPurchasesPercentage` decimal(10,2) DEFAULT NULL COMMENT '新品牌购买百分比',
  `newToBrandPurchasesRate` decimal(10,2) DEFAULT NULL COMMENT '新品牌购买率',
  `newToBrandSales` decimal(10,2) DEFAULT NULL COMMENT '新品牌销售额',
  `newToBrandSalesClicks` int DEFAULT NULL COMMENT '新品牌销售点击次数',
  `newToBrandSalesPercentage` decimal(10,2) DEFAULT NULL COMMENT '新品牌销售百分比',
  `newToBrandUnitsSold` int DEFAULT NULL COMMENT '新品牌销售单位数',
  `newToBrandUnitsSoldClicks` int DEFAULT NULL COMMENT '新品牌销售单位点击次数',
  `newToBrandUnitsSoldPercentage` decimal(10,2) DEFAULT NULL COMMENT '新品牌销售单位百分比',
  `purchases` int DEFAULT NULL COMMENT '购买次数',
  `purchasesClicks` int DEFAULT NULL COMMENT '购买点击次数',
  `purchasesPromoted` int DEFAULT NULL COMMENT '促销购买次数',
  `sales` decimal(10,2) DEFAULT NULL COMMENT '销售额',
  `salesClicks` int DEFAULT NULL COMMENT '销售点击次数',
  `salesPromoted` decimal(10,2) DEFAULT NULL COMMENT '促销销售额',
  `topOfSearchImpressionShare` decimal(10,2) DEFAULT NULL COMMENT '搜索顶部展示份额',
  `unitsSold` int DEFAULT NULL COMMENT '销售单位数',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`targetingId`,`bydate`),
  KEY `idx_bydate` (`bydate`),
  KEY `idx_matchType` (`matchType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='亚马逊广告HSA商品定位归因报告(全量)';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_product_targets_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_product_targets_brand` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedDetailPageViewsClicks14d` int DEFAULT NULL,
  `attributedOrdersNewToBrand14d` int DEFAULT NULL,
  `attributedOrdersNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `attributedOrderRateNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int DEFAULT NULL,
  `attributedUnitsOrderedNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `unitsSold14d` int DEFAULT NULL,
  `dpv14d` int DEFAULT NULL,
  `attributedBrandedSearches14d` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_product_targets_video 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_product_targets_video` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewableImpressions` int DEFAULT NULL,
  `videoFirstQuartileViews` int DEFAULT NULL,
  `videoMidpointViews` int DEFAULT NULL,
  `videoThirdQuartileViews` int DEFAULT NULL,
  `videoCompleteViews` int DEFAULT NULL,
  `video5SecondViews` int DEFAULT NULL,
  `video5SecondViewRate` decimal(12,2) DEFAULT NULL,
  `videoUnmutes` int DEFAULT NULL,
  `vtr` decimal(12,2) DEFAULT NULL,
  `vctr` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_adgroups 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`),
  KEY `adGroupId_campaignId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_adgroups_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups_attributed` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_adgroups_attributed_all 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups_attributed_all` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `addToCart` int DEFAULT NULL,
  `addToCartClicks` int DEFAULT NULL,
  `addToCartRate` decimal(20,6) DEFAULT NULL,
  `addToCartViews` int DEFAULT NULL,
  `addToList` int DEFAULT NULL,
  `addToListFromClicks` int DEFAULT NULL,
  `addToListFromViews` int DEFAULT NULL,
  `qualifiedBorrows` int DEFAULT NULL,
  `qualifiedBorrowsFromClicks` int DEFAULT NULL,
  `qualifiedBorrowsFromViews` int DEFAULT NULL,
  `royaltyQualifiedBorrows` int DEFAULT NULL,
  `royaltyQualifiedBorrowsFromClicks` int DEFAULT NULL,
  `royaltyQualifiedBorrowsFromViews` int DEFAULT NULL,
  `bidOptimization` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `brandedSearches` int DEFAULT NULL,
  `brandedSearchesClicks` int DEFAULT NULL,
  `brandedSearchesViews` int DEFAULT NULL,
  `brandedSearchRate` decimal(20,6) DEFAULT NULL,
  `campaignBudgetCurrencyCode` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `detailPageViews` int DEFAULT NULL,
  `detailPageViewsClicks` int DEFAULT NULL,
  `eCPAddToCart` decimal(20,6) DEFAULT NULL,
  `eCPBrandSearch` decimal(20,6) DEFAULT NULL,
  `impressionsViews` int DEFAULT NULL,
  `newToBrandPurchases` int DEFAULT NULL,
  `newToBrandPurchasesClicks` int DEFAULT NULL,
  `newToBrandSales` decimal(20,6) DEFAULT NULL,
  `newToBrandSalesClicks` int DEFAULT NULL,
  `newToBrandUnitsSold` int DEFAULT NULL,
  `newToBrandUnitsSoldClicks` int DEFAULT NULL,
  `purchases` int DEFAULT NULL COMMENT 'attributedConversions14d',
  `purchasesClicks` int DEFAULT NULL,
  `purchasesPromotedClicks` int DEFAULT NULL,
  `sales` decimal(20,6) DEFAULT NULL COMMENT 'attributedSales14d',
  `salesClicks` decimal(20,6) DEFAULT NULL,
  `salesPromotedClicks` decimal(20,6) DEFAULT NULL,
  `unitsSold` int DEFAULT NULL COMMENT 'attributedUnitsOrdered1d',
  `unitsSoldClicks` int DEFAULT NULL,
  `videoCompleteViews` int DEFAULT NULL,
  `videoFirstQuartileViews` int DEFAULT NULL,
  `videoMidpointViews` int DEFAULT NULL,
  `videoThirdQuartileViews` int DEFAULT NULL,
  `videoUnmutes` int DEFAULT NULL,
  `viewabilityRate` decimal(20,6) DEFAULT NULL,
  `viewClickThroughRate` decimal(20,6) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`,`bydate`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_adgroups_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups_attributed_new` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_adgroups_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups_attributed_same` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_adgroups_attributed_view 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups_attributed_view` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewImpressions` int DEFAULT NULL,
  `viewAttributedConversions14d` int DEFAULT NULL,
  `viewAttributedUnitsOrdered14d` int DEFAULT NULL,
  `viewAttributedSales14d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_asins 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_asins` (
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `asin` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `otherAsin` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedUnitsOrdered1dOtherSKU` int DEFAULT NULL,
  `attributedUnitsOrdered7dOtherSKU` int DEFAULT NULL,
  `attributedUnitsOrdered14dOtherSKU` int DEFAULT NULL,
  `attributedUnitsOrdered30dOtherSKU` int DEFAULT NULL,
  `attributedSales1dOtherSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dOtherSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dOtherSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dOtherSKU` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`asin`,`otherAsin`,`adGroupId`),
  KEY `campaignId_adGroupId_profileid` (`profileid`,`campaignId`,`adGroupId`,`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`),
  KEY `campaignId_profileid` (`profileid`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_attributed` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns_attributed_all 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_attributed_all` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `addToCart` int DEFAULT NULL,
  `addToCartClicks` int DEFAULT NULL,
  `addToCartRate` decimal(20,6) DEFAULT NULL,
  `addToCartViews` int DEFAULT NULL,
  `addToList` int DEFAULT NULL,
  `addToListFromClicks` int DEFAULT NULL,
  `addToListFromViews` int DEFAULT NULL,
  `qualifiedBorrows` int DEFAULT NULL,
  `qualifiedBorrowsFromClicks` int DEFAULT NULL,
  `qualifiedBorrowsFromViews` int DEFAULT NULL,
  `royaltyQualifiedBorrows` int DEFAULT NULL,
  `royaltyQualifiedBorrowsFromClicks` int DEFAULT NULL,
  `royaltyQualifiedBorrowsFromViews` int DEFAULT NULL,
  `brandedSearches` int DEFAULT NULL,
  `brandedSearchesClicks` int DEFAULT NULL,
  `brandedSearchesViews` int DEFAULT NULL,
  `clickThroughRate` decimal(20,6) DEFAULT NULL,
  `campaignBudgetCurrencyCode` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `detailPageViews` int DEFAULT NULL,
  `detailPageViewsClicks` int DEFAULT NULL,
  `eCPAddToCart` decimal(20,6) DEFAULT NULL,
  `eCPBrandSearch` decimal(20,6) DEFAULT NULL,
  `impressionsViews` int DEFAULT NULL,
  `newToBrandPurchases` int DEFAULT NULL,
  `newToBrandPurchasesClicks` int DEFAULT NULL,
  `newToBrandSalesClicks` int DEFAULT NULL,
  `newToBrandUnitsSold` int DEFAULT NULL,
  `newToBrandUnitsSoldClicks` int DEFAULT NULL,
  `purchases` int DEFAULT NULL COMMENT 'attributedConversions14d',
  `purchasesClicks` int DEFAULT NULL,
  `purchasesPromotedClicks` int DEFAULT NULL,
  `purchasesViews` int DEFAULT NULL,
  `sales` decimal(20,6) DEFAULT NULL COMMENT 'attributedSales14d',
  `salesClicks` decimal(20,6) DEFAULT NULL,
  `salesPromotedClicks` decimal(20,6) DEFAULT NULL,
  `unitsSold` int DEFAULT NULL COMMENT 'attributedUnitsOrdered1d',
  `unitsSoldClicks` int DEFAULT NULL,
  `unitsSoldViews` int DEFAULT NULL,
  `videoCompleteViews` int DEFAULT NULL,
  `videoFirstQuartileViews` int DEFAULT NULL,
  `videoMidpointViews` int DEFAULT NULL,
  `videoThirdQuartileViews` int DEFAULT NULL,
  `videoUnmutes` int DEFAULT NULL,
  `viewabilityRate` decimal(20,6) DEFAULT NULL,
  `viewClickThroughRate` decimal(20,6) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_attributed_new` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_attributed_same` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns_attributed_view 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_attributed_view` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewImpressions` int DEFAULT NULL,
  `viewAttributedConversions14d` int DEFAULT NULL,
  `viewAttributedUnitsOrdered14d` int DEFAULT NULL,
  `viewAttributedSales14d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns_t00001 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_t00001` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `attributedDPV14d` int DEFAULT NULL,
  `attributedUnitsSold14d` int DEFAULT NULL,
  `attributedSales14d` int DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`,`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_productads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`),
  KEY `profileid_adGroupId` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_productads_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads_attributed` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(10,2) DEFAULT NULL,
  `attributedSales7d` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,2) DEFAULT NULL,
  `attributedSales30d` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_productads_attributed_all 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads_attributed_all` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `addToCart` int DEFAULT NULL,
  `addToCartClicks` int DEFAULT NULL,
  `addToCartRate` decimal(20,6) DEFAULT NULL,
  `addToCartViews` int DEFAULT NULL,
  `addToList` int DEFAULT NULL,
  `addToListFromClicks` int DEFAULT NULL,
  `qualifiedBorrows` int DEFAULT NULL,
  `qualifiedBorrowsFromClicks` int DEFAULT NULL,
  `royaltyQualifiedBorrows` int DEFAULT NULL,
  `royaltyQualifiedBorrowsFromClicks` int DEFAULT NULL,
  `bidOptimization` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `brandedSearches` int DEFAULT NULL,
  `brandedSearchesClicks` int DEFAULT NULL,
  `campaignBudgetAmount` decimal(20,6) DEFAULT NULL,
  `campaignBudgetCurrencyCode` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `detailPageViews` int DEFAULT NULL,
  `detailPageViewsClicks` int DEFAULT NULL,
  `eCPAddToCart` decimal(20,6) DEFAULT NULL,
  `newToBrandDetailPageViewRate` decimal(20,6) DEFAULT NULL,
  `newToBrandDetailPageViews` int DEFAULT NULL,
  `newToBrandDetailPageViewsClicks` int DEFAULT NULL,
  `newToBrandECPDetailPageView` int DEFAULT NULL,
  `newToBrandPurchasesPercentage` decimal(20,6) DEFAULT NULL,
  `newToBrandPurchases` int DEFAULT NULL,
  `newToBrandPurchasesClicks` int DEFAULT NULL,
  `newToBrandPurchasesRate` decimal(20,6) DEFAULT NULL,
  `newToBrandSales` decimal(20,6) DEFAULT NULL,
  `newToBrandSalesClicks` int DEFAULT NULL,
  `newToBrandSalesPercentage` decimal(20,6) DEFAULT NULL,
  `newToBrandUnitsSold` int DEFAULT NULL,
  `newToBrandUnitsSoldClicks` int DEFAULT NULL,
  `newToBrandUnitsSoldPercentage` decimal(20,6) DEFAULT NULL,
  `purchases` int DEFAULT NULL COMMENT 'attributedConversions14d',
  `purchasesClicks` int DEFAULT NULL,
  `purchasesPromoted` int DEFAULT NULL,
  `sales` decimal(20,6) DEFAULT NULL COMMENT 'attributedSales14d',
  `salesClicks` decimal(20,6) DEFAULT NULL,
  `salesPromoted` decimal(20,6) DEFAULT NULL,
  `unitsSold` int DEFAULT NULL COMMENT 'attributedUnitsOrdered1d',
  `unitsSoldClicks` int DEFAULT NULL,
  `video5SecondViewRate` decimal(20,6) DEFAULT NULL,
  `video5SecondViews` int DEFAULT NULL,
  `videoCompleteViews` int DEFAULT NULL,
  `videoFirstQuartileViews` int DEFAULT NULL,
  `videoMidpointViews` int DEFAULT NULL,
  `videoThirdQuartileViews` int DEFAULT NULL,
  `videoUnmutes` int DEFAULT NULL,
  `viewabilityRate` decimal(20,6) DEFAULT NULL,
  `viewableImpressions` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adId`,`bydate`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_productads_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads_attributed_new` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_productads_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads_attributed_same` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_productads_attributed_view 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads_attributed_view` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewImpressions` int DEFAULT NULL,
  `viewAttributedConversions14d` int DEFAULT NULL,
  `viewAttributedUnitsOrdered14d` int DEFAULT NULL,
  `viewAttributedSales14d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_product_targets 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets` (
  `targetId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`),
  KEY `campaignId_adGroupId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_product_targets_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets_attributed` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(10,2) DEFAULT NULL,
  `attributedSales7d` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,2) DEFAULT NULL,
  `attributedSales30d` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_product_targets_attributed_all 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets_attributed_all` (
  `targetingId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `addToCart` int DEFAULT NULL,
  `addToCartClicks` int DEFAULT NULL,
  `addToCartRate` decimal(20,6) DEFAULT NULL,
  `addToCartViews` int DEFAULT NULL,
  `addToList` int DEFAULT NULL,
  `addToListFromClicks` int DEFAULT NULL,
  `addToListFromViews` int DEFAULT NULL,
  `qualifiedBorrows` int DEFAULT NULL,
  `qualifiedBorrowsFromClicks` int DEFAULT NULL,
  `qualifiedBorrowsFromViews` int DEFAULT NULL,
  `royaltyQualifiedBorrows` int DEFAULT NULL,
  `royaltyQualifiedBorrowsFromClicks` int DEFAULT NULL,
  `royaltyQualifiedBorrowsFromViews` int DEFAULT NULL,
  `brandedSearches` int DEFAULT NULL,
  `brandedSearchesClicks` int DEFAULT NULL,
  `brandedSearchesViews` int DEFAULT NULL,
  `brandedSearchRate` decimal(20,6) DEFAULT NULL,
  `campaignBudgetCurrencyCode` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `detailPageViews` int DEFAULT NULL,
  `detailPageViewsClicks` int DEFAULT NULL,
  `eCPAddToCart` decimal(20,6) DEFAULT NULL,
  `eCPBrandSearch` decimal(20,6) DEFAULT NULL,
  `impressionsViews` int DEFAULT NULL,
  `newToBrandDetailPageViewRate` decimal(20,6) DEFAULT NULL,
  `newToBrandDetailPageViews` int DEFAULT NULL,
  `newToBrandDetailPageViewsClicks` int DEFAULT NULL,
  `newToBrandECPDetailPageView` int DEFAULT NULL,
  `newToBrandPurchasesPercentage` decimal(20,6) DEFAULT NULL,
  `newToBrandPurchases` int DEFAULT NULL,
  `newToBrandPurchasesClicks` int DEFAULT NULL,
  `newToBrandPurchasesRate` decimal(20,6) DEFAULT NULL,
  `newToBrandSales` decimal(20,6) DEFAULT NULL,
  `newToBrandSalesClicks` int DEFAULT NULL,
  `newToBrandSalesPercentage` decimal(20,6) DEFAULT NULL,
  `newToBrandUnitsSold` int DEFAULT NULL,
  `newToBrandUnitsSoldClicks` int DEFAULT NULL,
  `newToBrandUnitsSoldPercentage` decimal(20,6) DEFAULT NULL,
  `purchases` int DEFAULT NULL COMMENT 'attributedConversions14d',
  `purchasesClicks` int DEFAULT NULL,
  `purchasesPromoted` int DEFAULT NULL,
  `purchasesPromotedClicks` int DEFAULT NULL,
  `sales` decimal(20,6) DEFAULT NULL COMMENT 'attributedSales14d',
  `salesClicks` decimal(20,6) DEFAULT NULL,
  `salesPromoted` decimal(20,6) DEFAULT NULL,
  `salesPromotedClicks` decimal(20,6) DEFAULT NULL,
  `unitsSold` int DEFAULT NULL COMMENT 'attributedUnitsOrdered1d',
  `unitsSoldClicks` int DEFAULT NULL,
  `video5SecondViewRate` decimal(20,6) DEFAULT NULL,
  `video5SecondViews` int DEFAULT NULL,
  `videoCompleteViews` int DEFAULT NULL,
  `videoFirstQuartileViews` int DEFAULT NULL,
  `videoMidpointViews` int DEFAULT NULL,
  `videoThirdQuartileViews` int DEFAULT NULL,
  `videoUnmutes` int DEFAULT NULL,
  `viewabilityRate` decimal(20,6) DEFAULT NULL,
  `viewClickThroughRate` decimal(20,6) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetingId`,`bydate`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_product_targets_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets_attributed_new` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_product_targets_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets_attributed_same` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_product_targets_attributed_view 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets_attributed_view` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewImpressions` int DEFAULT NULL,
  `viewAttributedConversions14d` int DEFAULT NULL,
  `viewAttributedUnitsOrdered14d` int DEFAULT NULL,
  `viewAttributedSales14d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_adgroups 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_adgroups` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`),
  KEY `proadcam` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_adgroups_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_adgroups_attributed` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_adgroups_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_adgroups_attributed_same` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` int DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_asins 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_asins` (
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `advertisedAsin` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'advertisedAsin',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `purchasedAsin` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'purchasedAsin',
  `attributedSales1d` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales7d` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales14d` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales30d` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales1dSameSKU` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales7dSameSKU` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales14dSameSKU` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales30dSameSKU` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedConversions1d` int NOT NULL DEFAULT '0',
  `attributedConversions7d` int NOT NULL DEFAULT '0',
  `attributedConversions14d` int NOT NULL DEFAULT '0',
  `attributedConversions30d` int NOT NULL DEFAULT '0',
  `attributedConversions1dSameSKU` int NOT NULL DEFAULT '0',
  `attributedConversions7dSameSKU` int NOT NULL DEFAULT '0',
  `attributedConversions14dSameSKU` int NOT NULL DEFAULT '0',
  `attributedConversions30dSameSKU` int NOT NULL DEFAULT '0',
  `attributedUnitsOrdered1d` int NOT NULL DEFAULT '0',
  `attributedUnitsOrdered7d` int NOT NULL DEFAULT '0',
  `attributedUnitsOrdered14d` int NOT NULL DEFAULT '0',
  `attributedUnitsOrdered30d` int NOT NULL DEFAULT '0',
  `attributedUnitsOrdered1dSameSKU` int NOT NULL DEFAULT '0',
  `attributedUnitsOrdered7dSameSKU` int NOT NULL DEFAULT '0',
  `attributedUnitsOrdered14dSameSKU` int NOT NULL DEFAULT '0',
  `attributedUnitsOrdered30dSameSKU` int NOT NULL DEFAULT '0',
  `profileid` bigint unsigned NOT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`advertisedAsin`,`purchasedAsin`,`keywordId`),
  KEY `profileid` (`profileid`,`campaignId`,`adGroupId`,`advertisedAsin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_compaigns 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_compaigns` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignBudget` decimal(12,2) DEFAULT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`),
  KEY `campaignId_profileid` (`profileid`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_compaigns_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_compaigns_attributed` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_compaigns_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_compaigns_attributed_same` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` int DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_compaigns_place 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_compaigns_place` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignBudget` decimal(12,2) DEFAULT NULL,
  `placementid` int NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_compaigns_place_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_compaigns_place_attributed` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int NOT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_compaigns_place_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_compaigns_place_attributed_same` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int NOT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` int DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_keywords 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_keywords` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`),
  KEY `campaignId_adGroupId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='campaignId';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_keywords_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_keywords_attributed` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='campaignId';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_keywords_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_keywords_attributed_same` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='campaignId';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_keywords_query 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_keywords_query` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `queryid` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`queryid`,`keywordId`),
  KEY `adGroupId_campaignId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_keywords_query_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_keywords_query_attributed` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `queryid` bigint unsigned NOT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`queryid`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_keywords_query_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_keywords_query_attributed_same` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `queryid` bigint unsigned NOT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` int DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`queryid`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_productads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_productads` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`),
  KEY `profileid_campaignId_adGroupId` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_productads_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_productads_attributed` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(10,2) DEFAULT NULL,
  `attributedSales7d` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,2) DEFAULT NULL,
  `attributedSales30d` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_productads_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_productads_attributed_same` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_product_targets 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_product_targets` (
  `targetId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`),
  KEY `campaignId_adGroupId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_product_targets_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_product_targets_attributed` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(10,2) DEFAULT NULL,
  `attributedSales7d` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,2) DEFAULT NULL,
  `attributedSales30d` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_product_targets_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_product_targets_attributed_same` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` int DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_product_targets_query 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_product_targets_query` (
  `targetId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `queryid` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`targetId`,`queryid`),
  KEY `campaignId_adGroupId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_product_targets_query_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_product_targets_query_attributed` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `queryid` bigint unsigned NOT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(10,2) DEFAULT NULL,
  `attributedSales7d` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,2) DEFAULT NULL,
  `attributedSales30d` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`,`queryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_product_targets_query_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_product_targets_query_attributed_same` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `queryid` bigint unsigned NOT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` int DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`,`queryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt_placement 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_placement` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt_query 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_query` (
  `id` bigint unsigned NOT NULL,
  `query` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `query` (`query`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_schedule_plan 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_schedule_plan` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '计划名称',
  `status` tinyint DEFAULT NULL COMMENT '状态 enabled ： 已开启    paused ： 已禁止   disable  : 已结束',
  `startDate` date DEFAULT NULL COMMENT '开始时间',
  `endDate` date DEFAULT NULL,
  `weekdays` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `startTime` time DEFAULT NULL,
  `endTime` time DEFAULT NULL,
  `remark` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='广告定时任务';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_schedule_plandata 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_schedule_plandata` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `planid` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned zerofill DEFAULT '00000000000000000000',
  `adId` bigint(20) unsigned zerofill DEFAULT '00000000000000000000',
  `keywordId` bigint(20) unsigned zerofill DEFAULT '00000000000000000000',
  `oldbid` decimal(10,2) DEFAULT NULL,
  `oldstatus` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `profileid_campaignId_adGroupId_adId_keywordId` (`profileid`,`campaignId`,`adGroupId`,`keywordId`,`adId`,`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='广告定时任务';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_schedule_planitem 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_schedule_planitem` (
  `taskId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `planId` bigint unsigned NOT NULL,
  `type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'one单次执行，cycle周期执行',
  `startTime` time DEFAULT NULL COMMENT '开始时间',
  `status` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `bid` decimal(12,2) DEFAULT NULL,
  `weekdays` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '1 ：周日； 2：周一 ；13：周日和周二 以此类推',
  PRIMARY KEY (`taskId`,`planId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_serch_history 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_serch_history` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `userId` bigint unsigned DEFAULT NULL,
  `ftype` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fcondition` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_snapshot 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_snapshot` (
  `snapshotId` char(75) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `region` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `status` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `location` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `recordtype` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `campaignType` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fileSize` int DEFAULT NULL,
  `expires` datetime DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `requesttime` datetime DEFAULT NULL,
  `treat_number` int DEFAULT NULL,
  `treat_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `log` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`snapshotId`),
  KEY `Index 2` (`profileid`,`recordtype`,`campaignType`,`region`),
  KEY `Index 3` (`requesttime`,`treat_number`,`treat_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_stores 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_stores` (
  `profileid` bigint unsigned NOT NULL,
  `entityId` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `brandEntityId` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `storePageId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `storeName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `storePageUrl` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `storePageName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opptime` datetime DEFAULT NULL,
  PRIMARY KEY (`entityId`,`brandEntityId`,`profileid`,`storePageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_sumalltype 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_sumalltype` (
  `profileid` bigint unsigned NOT NULL,
  `byday` date NOT NULL,
  `state` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `campaignType` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `recordType` char(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `quantity` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`profileid`,`campaignType`,`state`,`recordType`,`byday`),
  KEY `byday` (`byday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='计划的 state,campagintype,recordtype 都是task';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_sumpdtads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_sumpdtads` (
  `profileid` bigint unsigned NOT NULL,
  `byday` date NOT NULL,
  `ctype` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'sp',
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(14,2) DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `attributedUnitsOrdered` int DEFAULT NULL,
  `attributedSales` decimal(14,2) DEFAULT NULL,
  `attributedConversions` int DEFAULT NULL,
  PRIMARY KEY (`profileid`,`byday`,`ctype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='转化';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_warningdate 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_warningdate` (
  `shopid` bigint NOT NULL,
  `recordType` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `ftype` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'yesterday：昨日突降；sequent：连续下降；co：同期下降',
  `num_impressions` decimal(10,2) DEFAULT NULL,
  `num_CSRT` decimal(10,2) DEFAULT NULL,
  `num_ACOS` decimal(10,2) DEFAULT NULL,
  `num_clicks` decimal(10,2) DEFAULT NULL COMMENT '比率',
  `absoluteNum_impressions` int DEFAULT NULL COMMENT '绝对值',
  `absoluteNum_clicks` int DEFAULT NULL,
  `absoluteNum_ACOS` decimal(10,2) DEFAULT NULL,
  `absoluteNum_CSRT` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`shopid`,`recordType`,`ftype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_region 结构
CREATE TABLE IF NOT EXISTS `t_amz_region` (
  `code` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `advname` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `advpoint` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `client_id` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `client_secret` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_erp_serial_num 结构
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

-- 导出  表 db_amazon_adv.t_exchangerate 结构
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

-- 导出  表 db_amazon_adv.t_exchangerate_customer 结构
CREATE TABLE IF NOT EXISTS `t_exchangerate_customer` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `name` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '币别',
  `price` decimal(18,6) DEFAULT NULL COMMENT '汇率',
  `symbol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `utctime` datetime DEFAULT NULL COMMENT '服务器更新时间',
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`shopid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_marketplace 结构
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

-- 导出  表 db_amazon_adv.undo_log 结构
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

-- --------------------------------------------------------
-- 主机:                           rm-wz903sa454i2h35ik6o.mysql.rds.aliyuncs.com
-- 服务器版本:                        5.7.28-log - Source distribution
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
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

#----------------------------------------------------------------------------
DELETE d,a,b,v FROM t_amz_adv_rpt2_hsa_adgroups d 
LEFT JOIN t_amz_adv_rpt2_hsa_adgroups_attributed a ON a.adGroupId=d.adGroupId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_hsa_adgroups_brand b ON b.adGroupId=d.adGroupId AND b.bydate=a.bydate
LEFT JOIN t_amz_adv_rpt2_hsa_adgroups_video v ON v.adGroupId=d.adGroupId AND v.bydate=d.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b,v FROM t_amz_adv_rpt2_hsa_campaigns d 
LEFT JOIN t_amz_adv_rpt2_hsa_campaigns_attributed a ON a.campaignId=d.campaignId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_hsa_campaigns_brand b ON b.campaignId=d.campaignId AND b.bydate=a.bydate
LEFT JOIN t_amz_adv_rpt2_hsa_campaigns_video v ON v.campaignId=d.campaignId AND v.bydate=d.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);


DELETE d,a,b,v FROM t_amz_adv_rpt2_hsa_campaigns_place d 
LEFT JOIN t_amz_adv_rpt2_hsa_campaigns_place_attributed a ON a.campaignId=d.campaignId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_hsa_campaigns_place_brand b ON b.campaignId=d.campaignId AND b.bydate=a.bydate
LEFT JOIN t_amz_adv_rpt2_hsa_campaigns_place_video v ON v.campaignId=d.campaignId AND v.bydate=d.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b,v,q FROM t_amz_adv_rpt2_hsa_keywords d 
LEFT JOIN t_amz_adv_rpt2_hsa_keywords_attributed a ON a.keywordId=d.keywordId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_hsa_keywords_brand b ON b.keywordId=d.keywordId AND b.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_hsa_keywords_video v ON v.keywordId=d.keywordId AND v.bydate=d.bydate
left join t_amz_adv_rpt2_hsa_keywords_query q ON q.keywordId=d.keywordId AND q.bydate=d.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b,v FROM t_amz_adv_rpt2_hsa_product_targets d 
LEFT JOIN t_amz_adv_rpt2_hsa_product_targets_attributed a ON a.targetId=d.targetId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_hsa_product_targets_brand b ON b.targetId=d.targetId AND b.bydate=a.bydate
LEFT JOIN t_amz_adv_rpt2_hsa_product_targets_video v ON v.targetId=d.targetId AND v.bydate=d.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b,s,v FROM t_amz_adv_rpt2_sd_adgroups d 
LEFT JOIN t_amz_adv_rpt2_sd_adgroups_attributed a ON a.adGroupId=d.adGroupId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sd_adgroups_attributed_new b ON b.adGroupId=d.adGroupId AND b.bydate=a.bydate
LEFT JOIN t_amz_adv_rpt2_sd_adgroups_attributed_same s ON s.adGroupId=d.adGroupId AND s.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sd_adgroups_attributed_view v ON v.adGroupId=d.adGroupId AND v.bydate=d.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b,s,v FROM t_amz_adv_rpt2_sd_campaigns d 
LEFT JOIN t_amz_adv_rpt2_sd_campaigns_attributed a ON a.campaignId=d.campaignId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sd_campaigns_attributed_new b ON b.campaignId=d.campaignId AND b.bydate=a.bydate
LEFT JOIN t_amz_adv_rpt2_sd_campaigns_attributed_same s ON s.campaignId=d.campaignId AND s.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sd_campaigns_attributed_view v ON v.campaignId=d.campaignId AND v.bydate=d.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE a from t_amz_adv_rpt2_sd_asins a WHERE a.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE t from t_amz_adv_rpt2_sd_campaigns_t00001 t WHERE t.bydate =DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b,s,v FROM t_amz_adv_rpt2_sd_productads d 
LEFT JOIN t_amz_adv_rpt2_sd_productads_attributed a ON a.adId=d.adId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sd_productads_attributed_new b ON b.adId=d.adId AND b.bydate=a.bydate
LEFT JOIN t_amz_adv_rpt2_sd_productads_attributed_same s ON s.adId=d.adId AND s.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sd_productads_attributed_view v ON v.adId=d.adId AND v.bydate=d.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b,s,v FROM t_amz_adv_rpt2_sd_product_targets d 
LEFT JOIN t_amz_adv_rpt2_sd_product_targets_attributed a ON a.targetId=d.targetId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sd_product_targets_attributed_new b ON b.targetId=d.targetId AND b.bydate=a.bydate
LEFT JOIN t_amz_adv_rpt2_sd_product_targets_attributed_same s ON s.targetId=d.targetId AND s.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sd_product_targets_attributed_view v ON v.targetId=d.targetId AND v.bydate=d.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b FROM t_amz_adv_rpt2_sp_adgroups d 
LEFT JOIN t_amz_adv_rpt2_sp_adgroups_attributed a ON a.adGroupId=d.adGroupId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sp_adgroups_attributed_same b ON b.adGroupId=d.adGroupId AND b.bydate=a.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE a from t_amz_adv_rpt2_sp_asins a WHERE a.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b FROM t_amz_adv_rpt2_sp_compaigns d 
LEFT JOIN t_amz_adv_rpt2_sp_compaigns_attributed a ON a.campaignId=d.campaignId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sp_compaigns_attributed_same b ON b.campaignId=d.campaignId AND b.bydate=a.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b FROM t_amz_adv_rpt2_sp_compaigns_place d 
LEFT JOIN t_amz_adv_rpt2_sp_compaigns_place_attributed a ON a.campaignId=d.campaignId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sp_compaigns_place_attributed_same b ON b.campaignId=d.campaignId AND b.bydate=a.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b FROM t_amz_adv_rpt2_sp_keywords d 
LEFT JOIN t_amz_adv_rpt2_sp_keywords_attributed a ON a.keywordId=d.keywordId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sp_keywords_attributed_same b ON b.keywordId=d.keywordId AND b.bydate=a.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b FROM t_amz_adv_rpt2_sp_keywords_query d 
LEFT JOIN t_amz_adv_rpt2_sp_keywords_query_attributed a ON a.keywordId=d.keywordId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sp_keywords_query_attributed_same b ON b.keywordId=d.keywordId AND b.bydate=a.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b FROM t_amz_adv_rpt2_sp_productads d 
LEFT JOIN t_amz_adv_rpt2_sp_productads_attributed a ON a.adId=d.adId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sp_productads_attributed_same b ON b.adId=d.adId AND b.bydate=a.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b FROM t_amz_adv_rpt2_sp_product_targets d 
LEFT JOIN t_amz_adv_rpt2_sp_product_targets_attributed a ON a.targetId=d.targetId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sp_product_targets_attributed_same b ON b.targetId=d.targetId AND b.bydate=a.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

DELETE d,a,b FROM t_amz_adv_rpt2_sp_product_targets_query d 
LEFT JOIN t_amz_adv_rpt2_sp_product_targets_query_attributed a ON a.targetId=d.targetId AND a.bydate=d.bydate
LEFT JOIN t_amz_adv_rpt2_sp_product_targets_query_attributed_same b ON b.targetId=d.targetId AND b.bydate=a.bydate
WHERE  d.bydate=DATE_SUB(NOW(),INTERVAL 365 DAY);

END//
DELIMITER ;

-- 导出  事件 db_amazon_adv.syncData 结构
DELIMITER //
CREATE EVENT `syncData` ON SCHEDULE EVERY 1 DAY STARTS '2024-06-14 16:45:40' ON COMPLETION PRESERVE ENABLE DO BEGIN

replace into t_amazon_group
SELECT * FROM db_amazon.t_amazon_group;

update t_amz_adv_auth a
LEFT JOIN (SELECT advauthid FROM t_amz_adv_profile GROUP BY advauthid) v ON v.advauthid=a.id
SET  a.`disable`=1 ,a.disableTime=NOW()
WHERE a.`disable`=0  AND v.advauthid IS NULL;

END//
DELIMITER ;

-- 导出  表 db_amazon_adv.t_advert_warning_keywords_data 结构
CREATE TABLE IF NOT EXISTS `t_advert_warning_keywords_data` (
  `keywordid` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `ftype` char(10) COLLATE utf8mb4_bin NOT NULL,
  `groupName` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `market` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `campaignName` char(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `adGroupName` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `keywordText` char(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `click0` int(11) DEFAULT NULL,
  `impressions0` int(11) DEFAULT NULL,
  `cost0` decimal(12,2) DEFAULT NULL,
  `orders0` int(11) DEFAULT NULL,
  `sales0` int(11) DEFAULT NULL,
  `click1` int(11) DEFAULT NULL,
  `impressions1` int(11) DEFAULT NULL,
  `cost1` decimal(12,2) DEFAULT NULL,
  `orders1` int(11) DEFAULT NULL,
  `sales1` int(11) DEFAULT NULL,
  `click2` int(11) DEFAULT NULL,
  `impressions2` int(11) DEFAULT NULL,
  `cost2` decimal(12,2) DEFAULT NULL,
  `orders2` int(11) DEFAULT NULL,
  `sales2` int(11) DEFAULT NULL,
  PRIMARY KEY (`keywordid`,`ftype`),
  KEY `ftype` (`ftype`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_advert_warning_product_data 结构
CREATE TABLE IF NOT EXISTS `t_advert_warning_product_data` (
  `adid` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `ftype` char(10) COLLATE utf8mb4_bin NOT NULL,
  `groupName` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `market` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `campaignName` char(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `adGroupName` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `click0` int(11) DEFAULT NULL,
  `impressions0` int(11) DEFAULT NULL,
  `cost0` decimal(12,2) DEFAULT NULL,
  `orders0` int(11) DEFAULT NULL,
  `sales0` int(11) DEFAULT NULL,
  `click1` int(11) DEFAULT NULL,
  `impressions1` int(11) DEFAULT NULL,
  `cost1` decimal(12,2) DEFAULT NULL,
  `orders1` int(11) DEFAULT NULL,
  `sales1` int(11) DEFAULT NULL,
  `click2` int(11) DEFAULT NULL,
  `impressions2` int(11) DEFAULT NULL,
  `cost2` decimal(12,2) DEFAULT NULL,
  `orders2` int(11) DEFAULT NULL,
  `sales2` int(11) DEFAULT NULL,
  PRIMARY KEY (`adid`,`ftype`),
  KEY `ftype` (`ftype`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_browsenode 结构
CREATE TABLE IF NOT EXISTS `t_adv_browsenode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `node_path` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `query` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `refinement` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_dimensions 结构
CREATE TABLE IF NOT EXISTS `t_adv_dimensions` (
  `id` char(36) COLLATE utf8mb4_bin NOT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `length_units` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `width_units` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `height_units` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `weight_units` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_productgroup 结构
CREATE TABLE IF NOT EXISTS `t_adv_productgroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `referralfeeId` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_rank 结构
CREATE TABLE IF NOT EXISTS `t_adv_rank` (
  `id` char(36) COLLATE utf8mb4_bin NOT NULL,
  `asin` char(10) COLLATE utf8mb4_bin NOT NULL,
  `nodeid` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `pricerange` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `dim` char(36) COLLATE utf8mb4_bin DEFAULT NULL,
  `offerprice` int(11) DEFAULT NULL,
  `listprice` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `currency` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `lowestnprice` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `categoryrank` int(11) DEFAULT NULL,
  `imageurl` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `reviewsURL` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL,
  `totalOfferNew` int(11) DEFAULT NULL,
  `lastupdate` datetime NOT NULL,
  `reviews` int(11) DEFAULT NULL,
  `reviewAverage` float(5,1) DEFAULT NULL,
  `productgroup` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(32) COLLATE utf8mb4_bin DEFAULT NULL,
  `estiMargin` float DEFAULT NULL,
  `estiProfit` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`asin`),
  KEY `Index 3` (`productgroup`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_rank_his 结构
CREATE TABLE IF NOT EXISTS `t_adv_rank_his` (
  `id` char(36) COLLATE utf8mb4_bin NOT NULL,
  `asin` char(10) COLLATE utf8mb4_bin NOT NULL,
  `nodeid` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `pricerange` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `dim` char(36) COLLATE utf8mb4_bin DEFAULT NULL,
  `offerprice` int(11) DEFAULT NULL,
  `listprice` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `currency` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `lowestnprice` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `categoryrank` int(11) DEFAULT NULL,
  `imageurl` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `reviewsURL` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL,
  `totalOfferNew` int(11) DEFAULT NULL,
  `lastupdate` datetime NOT NULL,
  `reviews` int(11) DEFAULT NULL,
  `reviewAverage` float DEFAULT NULL,
  `productgroup` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(32) COLLATE utf8mb4_bin DEFAULT NULL,
  `estiMargin` float DEFAULT NULL,
  `estiProfit` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amazon_auth_market_performance 结构
CREATE TABLE IF NOT EXISTS `t_amazon_auth_market_performance` (
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(20) CHARACTER SET latin1 NOT NULL,
  `accountstatus` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `sellerid` char(20) CHARACTER SET latin1 DEFAULT NULL,
  `performance` varchar(6000) CHARACTER SET latin1 DEFAULT NULL,
  `refreshtime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amazon_group 结构
CREATE TABLE IF NOT EXISTS `t_amazon_group` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `profitcfgid` bigint(20) unsigned DEFAULT NULL COMMENT '店铺默认利润方案',
  `findex` int(10) unsigned DEFAULT NULL,
  `oldid` char(36) COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `name_shopid` (`shopid`,`name`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_advert_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report` (
  `id` char(36) COLLATE utf8mb4_bin NOT NULL,
  `sellerid` char(15) COLLATE utf8mb4_bin NOT NULL,
  `marketplaceid` char(15) COLLATE utf8mb4_bin NOT NULL,
  `sku` char(50) COLLATE utf8mb4_bin NOT NULL,
  `adId` bigint(20) DEFAULT NULL,
  `asin` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `bydate` date NOT NULL,
  `campaign_name` char(128) COLLATE utf8mb4_bin NOT NULL,
  `ad_Group_Name` char(128) COLLATE utf8mb4_bin NOT NULL,
  `clicks` int(11) DEFAULT NULL,
  `impressions` int(11) NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `spend` decimal(10,2) DEFAULT NULL,
  `cpc` decimal(10,2) DEFAULT NULL COMMENT 'Cost Per Click (CPC)	',
  `acos` double DEFAULT NULL COMMENT 'Total Advertising Cost of Sales (ACoS) ',
  `RoAS` double DEFAULT NULL COMMENT 'Total Return on Advertising Spend (RoAS)',
  `orders` int(11) DEFAULT NULL,
  `spc` double DEFAULT NULL,
  `units` int(11) DEFAULT NULL,
  `totalsales` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `mykey` (`bydate`,`sku`,`marketplaceid`,`sellerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_advert_report_summary 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report_summary` (
  `sellerid` char(15) COLLATE utf8mb4_bin NOT NULL,
  `id` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) COLLATE utf8mb4_bin NOT NULL,
  `ctype` char(2) COLLATE utf8mb4_bin NOT NULL DEFAULT 'sp',
  `sku` char(50) COLLATE utf8mb4_bin NOT NULL,
  `asin` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `bydate` date NOT NULL,
  `clicks` int(11) DEFAULT NULL,
  `impressions` int(11) NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `spend` decimal(10,2) DEFAULT NULL,
  `cpc` decimal(10,2) DEFAULT NULL COMMENT 'Cost Per Click (CPC)	',
  `acos` double DEFAULT NULL COMMENT 'Total Advertising Cost of Sales (ACoS) ',
  `RoAS` double DEFAULT NULL COMMENT 'Total Return on Advertising Spend (RoAS)',
  `orders` int(11) DEFAULT NULL,
  `units` int(11) DEFAULT NULL,
  `spc` double DEFAULT NULL,
  `totalsales` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`id`),
  UNIQUE KEY `sellerskum` (`sellerid`,`marketplaceid`,`sku`,`ctype`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_advert_report_summary_month 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report_summary_month` (
  `sellerid` char(15) COLLATE utf8mb4_bin NOT NULL,
  `id` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) COLLATE utf8mb4_bin NOT NULL,
  `ctype` char(2) COLLATE utf8mb4_bin NOT NULL DEFAULT 'sp',
  `sku` char(50) COLLATE utf8mb4_bin NOT NULL,
  `asin` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `bydate` date NOT NULL,
  `clicks` int(11) DEFAULT NULL,
  `impressions` int(11) NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `spend` decimal(10,2) DEFAULT NULL,
  `cpc` decimal(10,2) DEFAULT NULL COMMENT 'Cost Per Click (CPC)	',
  `acos` double DEFAULT NULL COMMENT 'Total Advertising Cost of Sales (ACoS) ',
  `RoAS` double DEFAULT NULL COMMENT 'Total Return on Advertising Spend (RoAS)',
  `orders` int(11) DEFAULT NULL,
  `units` int(11) DEFAULT NULL,
  `spc` double DEFAULT NULL,
  `totalsales` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`id`),
  UNIQUE KEY `sellerskum` (`sellerid`,`marketplaceid`,`sku`,`ctype`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_advert_report_summary_week 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report_summary_week` (
  `sellerid` char(15) COLLATE utf8mb4_bin NOT NULL,
  `id` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) COLLATE utf8mb4_bin NOT NULL,
  `ctype` char(2) COLLATE utf8mb4_bin NOT NULL DEFAULT 'sp',
  `sku` char(50) COLLATE utf8mb4_bin NOT NULL,
  `asin` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `bydate` date NOT NULL,
  `clicks` int(11) DEFAULT NULL,
  `impressions` int(11) NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `spend` decimal(10,2) DEFAULT NULL,
  `cpc` decimal(10,2) DEFAULT NULL COMMENT 'Cost Per Click (CPC)	',
  `acos` double DEFAULT NULL COMMENT 'Total Advertising Cost of Sales (ACoS) ',
  `RoAS` double DEFAULT NULL COMMENT 'Total Return on Advertising Spend (RoAS)',
  `orders` int(11) DEFAULT NULL,
  `units` int(11) DEFAULT NULL,
  `spc` double DEFAULT NULL,
  `totalsales` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`id`),
  UNIQUE KEY `sellerskum` (`sellerid`,`marketplaceid`,`sku`,`ctype`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_adgroups 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `name` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
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
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `name` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_adgroups_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups_sd` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `name` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `bidOptimization` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `creativeType` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
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
  `adid` bigint(20) unsigned NOT NULL,
  `asin` char(15) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`adid`,`asin`),
  KEY `asin` (`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_ads_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_ads_hsa` (
  `adId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `name` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `creative` varchar(3000) COLLATE utf8mb4_bin DEFAULT NULL,
  `landingPage` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_api_pages 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_api_pages` (
  `profileid` bigint(20) unsigned NOT NULL,
  `apipath` char(100) COLLATE utf8mb4_bin NOT NULL,
  `pages` int(11) DEFAULT NULL,
  `nexttoken` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `flog` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`profileid`,`apipath`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_assets 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_assets` (
  `assetId` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `brandEntityId` varchar(30) COLLATE utf8mb4_bin NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `mediaType` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `url` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `opptime` datetime DEFAULT NULL,
  PRIMARY KEY (`profileid`,`brandEntityId`,`assetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_auth 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_auth` (
  `id` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `code` char(36) COLLATE utf8mb4_bin DEFAULT NULL,
  `region` char(2) COLLATE utf8mb4_bin DEFAULT NULL,
  `access_token` varchar(800) COLLATE utf8mb4_bin DEFAULT NULL,
  `refresh_token` varchar(800) COLLATE utf8mb4_bin DEFAULT NULL,
  `token_type` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
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
  `campaign_optimization_id` char(36) COLLATE utf8mb4_bin NOT NULL,
  `profileid` bigint(20) unsigned DEFAULT NULL,
  `rule_name` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `rule_type` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `rule_sub_category` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `rule_category` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `recurrence` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `action` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `conditions` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `status` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`campaign_optimization_id`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_brand` (
  `profileid` bigint(20) unsigned NOT NULL,
  `brandId` varchar(50) COLLATE utf8mb4_bin NOT NULL,
  `brandEntityId` varchar(50) COLLATE utf8mb4_bin NOT NULL,
  `brandRegistryName` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`brandId`,`brandEntityId`,`profileid`),
  KEY `profileid_brandRegistryName` (`profileid`,`brandRegistryName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_brand_tml 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_brand_tml` (
  `brand` char(100) COLLATE utf8mb4_bin NOT NULL,
  `sellingcategory` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `sellername` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `sellerID` char(15) COLLATE utf8mb4_bin DEFAULT NULL,
  `country` char(2) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`brand`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_budget_rules 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_budget_rules` (
  `rule_id` char(36) COLLATE utf8mb4_bin NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaigntype` char(5) COLLATE utf8mb4_bin DEFAULT NULL,
  `rule_state` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `duration` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `recurrence` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `rule_type` char(15) COLLATE utf8mb4_bin DEFAULT NULL,
  `budget_increase_by` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(355) COLLATE utf8mb4_bin DEFAULT NULL,
  `performance_measure_condition` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `rule_status` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`rule_id`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campaigns 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaigns` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `portfolioid` bigint(20) unsigned DEFAULT NULL,
  `name` char(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '广告活动名称',
  `campaignType` char(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'sp 和 sb（hsa）',
  `dailyBudget` decimal(12,2) DEFAULT NULL COMMENT '每日预算',
  `targetingType` char(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '投放类型',
  `state` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `premiumBidAdjustment` char(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '竞价',
  `bidding` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
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
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `portfolioid` bigint(20) unsigned DEFAULT NULL,
  `name` char(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `budgetType` char(15) COLLATE utf8mb4_bin DEFAULT NULL,
  `budget` decimal(12,2) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `bidding` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL,
  `brandEntityId` char(30) COLLATE utf8mb4_bin DEFAULT NULL,
  `goal` char(30) COLLATE utf8mb4_bin DEFAULT NULL,
  `productLocation` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `tags` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `costType` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `smartDefault` char(15) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `spendingPolicy` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `landingPage` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `adFormat` char(30) COLLATE utf8mb4_bin DEFAULT NULL,
  `bidOptimization` bit(1) DEFAULT NULL,
  `isMultiAdGroupsEnabled` bit(1) DEFAULT NULL,
  `bidMultiplier` decimal(12,2) DEFAULT NULL,
  `bidAdjustments` varchar(350) COLLATE utf8mb4_bin DEFAULT NULL,
  `bidAdjustmentsByShopperSegment` varchar(350) COLLATE utf8mb4_bin DEFAULT NULL,
  `bidAdjustmentsByPlacement` varchar(350) COLLATE utf8mb4_bin DEFAULT NULL,
  `bidOptimizationStrategy` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL,
  `creative` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL,
  `servingStatus` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`profileid`),
  KEY `profileid_name` (`profileid`,`name`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campaigns_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaigns_sd` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `portfolioId` bigint(20) unsigned DEFAULT NULL,
  `name` char(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `tactic` char(30) COLLATE utf8mb4_bin DEFAULT NULL,
  `costtype` char(5) COLLATE utf8mb4_bin DEFAULT NULL,
  `budgettype` char(15) COLLATE utf8mb4_bin DEFAULT NULL,
  `budget` decimal(12,2) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`profileid`),
  KEY `profileid` (`profileid`,`name`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campaign_budget_rule 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaign_budget_rule` (
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignid` bigint(20) unsigned NOT NULL,
  `rule_id` char(36) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`campaignid`,`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campkeywords_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campkeywords_negativa` (
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `keywordId` bigint(20) unsigned NOT NULL,
  `keywordText` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `matchType` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(15) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campproduct_targe_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campproduct_targe_negativa` (
  `targetId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `profileid` (`profileid`),
  KEY `adGroupId` (`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_group 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_group` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_keywords 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `keywordId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignType` char(5) COLLATE utf8mb4_bin NOT NULL,
  `keywordText` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `nativeLanguageKeyword` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `matchType` char(15) COLLATE utf8mb4_bin DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
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
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `keywordId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignType` char(5) COLLATE utf8mb4_bin NOT NULL,
  `keywordText` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `nativeLanguageKeyword` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `matchType` char(15) COLLATE utf8mb4_bin DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
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
  `keywordId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `keywordText` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `matchType` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `campaignType` char(4) COLLATE utf8mb4_bin DEFAULT NULL,
  `nativeLanguageKeyword` char(5) COLLATE utf8mb4_bin DEFAULT NULL,
  `nativeLanguageLocale` char(5) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_keywords_negativa_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords_negativa_hsa` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `keywordText` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `matchType` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `campaignType` char(4) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_media_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_media_hsa` (
  `mediaId` char(60) COLLATE utf8mb4_bin NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned DEFAULT NULL,
  `status` char(30) COLLATE utf8mb4_bin DEFAULT NULL,
  `statusMetadata` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `publishedMediaUrl` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint(20) unsigned NOT NULL DEFAULT '0',
  `opttime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`mediaId`),
  KEY `profileid` (`profileid`,`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_operate_log 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_operate_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `profileid` bigint(20) unsigned DEFAULT NULL,
  `campaignId` bigint(20) unsigned DEFAULT NULL,
  `adGroupId` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `beanclasz` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `beforeobject` longtext COLLATE utf8mb4_bin,
  `afterobject` longtext COLLATE utf8mb4_bin,
  `remark` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`,`opttime`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=503427 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_portfolios 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_portfolios` (
  `id` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `name` char(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT ' ',
  `policy` char(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT ' ',
  `currencyCode` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` decimal(12,2) DEFAULT NULL COMMENT ' ',
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `inBudget` bit(1) DEFAULT NULL,
  `startDate` date DEFAULT NULL COMMENT ' ',
  `endDate` date DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_productads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_productads` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `sku` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `customText` varchar(150) COLLATE utf8mb4_bin DEFAULT NULL,
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
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `sku` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `adName` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `landingPageType` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `landingPageURL` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
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
  `targetId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
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
  `targetId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
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
  `targetId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe_negativa_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_negativa_hsa` (
  `targetId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `profileid` (`profileid`),
  KEY `campaignId` (`campaignId`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe_negativa_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_negativa_sd` (
  `targetId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `profileid` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_sd` (
  `targetId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `expression` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `profileid_expression` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_profile 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_profile` (
  `id` bigint(20) unsigned NOT NULL,
  `countryCode` char(2) COLLATE utf8mb4_bin DEFAULT NULL,
  `currencyCode` char(3) COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceId` char(15) COLLATE utf8mb4_bin DEFAULT NULL,
  `sellerId` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `advauthId` bigint(20) unsigned DEFAULT NULL,
  `timezone` char(25) COLLATE utf8mb4_bin DEFAULT NULL,
  `type` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `dailyBudget` decimal(20,5) DEFAULT NULL,
  `errorday` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `errorlog` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `marketplaceId` (`marketplaceId`),
  KEY `sellerId` (`sellerId`),
  KEY `authid` (`advauthId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_remark 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_remark` (
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adgroupId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `keywordId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `adId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `targetId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `remark` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`profileid`,`campaignId`,`adgroupId`,`keywordId`,`adId`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='广告历史记录备注';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_remind 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_remind` (
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignid` bigint(20) unsigned NOT NULL,
  `adgroupid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `keywordid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `adid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `targetid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `recordtype` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `cycle` int(11) DEFAULT NULL COMMENT '1当天，7（7天）',
  `quota` char(15) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'click（点击数），ctr（点击率）,cost（花费），acos,cr(转化率）',
  `fcondition` int(11) DEFAULT NULL COMMENT '1是超过，2是低于',
  `subtrahend` char(15) COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL COMMENT '(cycle.quota） condition(>) amount',
  `iswarn` bit(1) NOT NULL DEFAULT b'0',
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`profileid`,`campaignid`,`adgroupid`,`keywordid`,`adid`,`targetid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='广告提醒';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_report_request 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_report_request` (
  `reportId` char(100) COLLATE utf8mb4_bin NOT NULL,
  `profileId` bigint(20) unsigned NOT NULL,
  `recordType` char(50) COLLATE utf8mb4_bin NOT NULL,
  `status` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `statusDetails` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `reportType` int(11) DEFAULT NULL,
  `campaignType` char(5) COLLATE utf8mb4_bin DEFAULT NULL,
  `segment` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `creativeType` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `location` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL,
  `fileSize` int(11) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `requesttime` datetime DEFAULT NULL,
  `treat_number` int(10) unsigned DEFAULT '0',
  `treat_status` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `log` varchar(5000) COLLATE utf8mb4_bin DEFAULT NULL,
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
  `id` int(11) NOT NULL DEFAULT '0',
  `campaigntype` char(3) COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `reporttype` char(30) COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `segment` char(11) COLLATE utf8mb4_bin DEFAULT '0',
  `activeType` char(10) COLLATE utf8mb4_bin DEFAULT '0',
  `metrics` varchar(2000) COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `bean` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `reponsetype` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL,
  `nomarket` char(245) COLLATE utf8mb4_bin DEFAULT NULL,
  `disablevendor` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `campaigntype_reporttype_segment_activeType` (`campaigntype`,`reporttype`,`segment`,`activeType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_adgroups 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_adgroups` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`),
  KEY `adGroupId_campaignId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_adgroups_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_adgroups_attributed` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_adgroups_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_adgroups_brand` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedDetailPageViewsClicks14d` int(10) unsigned DEFAULT '0',
  `attributedOrdersNewToBrand14d` int(10) unsigned DEFAULT '0',
  `attributedOrdersNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedOrderRateNewToBrand14d` int(10) unsigned DEFAULT '0',
  `attributedSalesNewToBrand14d` int(10) unsigned DEFAULT '0',
  `attributedSalesNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedUnitsOrderedNewToBrand14d` int(10) unsigned DEFAULT '0',
  `attributedUnitsOrderedNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `unitsSold14d` int(10) unsigned DEFAULT '0',
  `dpv14d` int(10) unsigned DEFAULT '0',
  `attributedBrandedSearches14d` int(10) unsigned DEFAULT '0',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_adgroups_video 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_adgroups_video` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewableImpressions` int(10) unsigned DEFAULT '0',
  `videoFirstQuartileViews` int(10) unsigned DEFAULT '0',
  `videoMidpointViews` int(10) unsigned DEFAULT '0',
  `videoThirdQuartileViews` int(10) unsigned DEFAULT '0',
  `videoCompleteViews` int(10) unsigned DEFAULT '0',
  `video5SecondViews` int(10) unsigned DEFAULT '0',
  `video5SecondViewRate` decimal(10,2) unsigned DEFAULT '0.00',
  `videoUnmutes` int(10) unsigned DEFAULT '0',
  `vtr` decimal(12,2) unsigned DEFAULT '0.00',
  `vctr` decimal(12,2) unsigned DEFAULT '0.00',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`),
  KEY `campaignId_bydate` (`adGroupId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_ads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_ads` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `keywordBid` decimal(10,2) DEFAULT '0.00',
  `impressions` int(11) DEFAULT '0',
  `clicks` int(11) DEFAULT '0',
  `cost` decimal(12,2) DEFAULT '0.00',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`),
  KEY `profileid` (`profileid`,`campaignId`,`adGroupId`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_ads_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_ads_attributed` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_ads_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_ads_brand` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedDetailPageViewsClicks14d` int(11) DEFAULT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedOrdersNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `attributedOrderRateNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `unitsSold14d` int(11) DEFAULT NULL,
  `dpv14d` int(11) DEFAULT NULL,
  `vctr` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedBrandedSearches14d` int(11) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_ads_video 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_ads_video` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewableImpressions` int(10) unsigned DEFAULT '0',
  `videoFirstQuartileViews` int(10) unsigned DEFAULT '0',
  `videoMidpointViews` int(10) unsigned DEFAULT '0',
  `videoThirdQuartileViews` int(10) unsigned DEFAULT '0',
  `videoCompleteViews` int(10) unsigned DEFAULT '0',
  `video5SecondViews` int(10) unsigned DEFAULT '0',
  `video5SecondViewRate` decimal(10,2) unsigned DEFAULT '0.00',
  `videoUnmutes` int(10) unsigned DEFAULT '0',
  `vtr` decimal(12,2) unsigned DEFAULT '0.00',
  `vctr` decimal(12,2) unsigned DEFAULT '0.00',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`adId`),
  KEY `campaignId_bydate` (`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignBudget` decimal(12,2) DEFAULT NULL,
  `impressions` int(10) unsigned NOT NULL DEFAULT '0',
  `clicks` int(10) unsigned NOT NULL DEFAULT '0',
  `cost` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `topOfSearchImpressionShare` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`),
  KEY `campaignId_profileid` (`profileid`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_attributed` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`bydate`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_brand` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedDetailPageViewsClicks14d` int(10) unsigned DEFAULT '0',
  `attributedOrdersNewToBrand14d` int(10) unsigned DEFAULT '0',
  `attributedOrdersNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedOrderRateNewToBrand14d` int(10) unsigned DEFAULT '0',
  `attributedSalesNewToBrand14d` int(10) unsigned DEFAULT '0',
  `attributedSalesNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedUnitsOrderedNewToBrand14d` int(10) unsigned DEFAULT '0',
  `attributedUnitsOrderedNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `unitsSold14d` int(10) unsigned DEFAULT '0',
  `dpv14d` int(10) unsigned DEFAULT '0',
  `attributedBrandedSearches14d` int(10) unsigned DEFAULT '0',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_place 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_place` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int(10) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignBudget` decimal(12,2) DEFAULT NULL,
  `impressions` int(11) DEFAULT '0',
  `clicks` int(11) DEFAULT '0',
  `cost` decimal(12,2) DEFAULT '0.00',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`),
  KEY `profileid` (`profileid`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_place_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_place_attributed` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int(10) unsigned NOT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) DEFAULT '0.00',
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_place_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_place_brand` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int(10) unsigned NOT NULL,
  `attributedDetailPageViewsClicks14d` int(10) unsigned DEFAULT '0',
  `attributedOrdersNewToBrand14d` int(10) unsigned DEFAULT '0',
  `attributedOrdersNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedOrderRateNewToBrand14d` int(10) unsigned DEFAULT '0',
  `attributedSalesNewToBrand14d` decimal(20,6) unsigned DEFAULT NULL,
  `attributedSalesNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `attributedUnitsOrderedNewToBrand14d` int(10) unsigned DEFAULT '0',
  `attributedUnitsOrderedNewToBrandPercentage14d` decimal(12,2) unsigned DEFAULT '0.00',
  `unitsSold14d` int(10) unsigned DEFAULT '0',
  `dpv14d` int(10) unsigned DEFAULT '0',
  `attributedBrandedSearches14d` int(10) unsigned DEFAULT '0',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_place_video 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_place_video` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int(10) unsigned NOT NULL,
  `viewableImpressions` int(10) unsigned DEFAULT '0',
  `videoFirstQuartileViews` int(10) unsigned DEFAULT '0',
  `videoMidpointViews` int(10) unsigned DEFAULT '0',
  `videoThirdQuartileViews` int(10) unsigned DEFAULT '0',
  `videoCompleteViews` int(10) unsigned DEFAULT '0',
  `video5SecondViews` int(10) unsigned DEFAULT '0',
  `video5SecondViewRate` decimal(10,2) unsigned DEFAULT '0.00',
  `videoUnmutes` int(10) unsigned DEFAULT '0',
  `vtr` decimal(12,2) unsigned DEFAULT '0.00',
  `vctr` decimal(12,2) unsigned DEFAULT '0.00',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_video 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_video` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewableImpressions` int(10) unsigned DEFAULT '0',
  `videoFirstQuartileViews` int(10) unsigned DEFAULT '0',
  `videoMidpointViews` int(10) unsigned DEFAULT '0',
  `videoThirdQuartileViews` int(10) unsigned DEFAULT '0',
  `videoCompleteViews` int(10) unsigned DEFAULT '0',
  `video5SecondViews` int(10) unsigned DEFAULT '0',
  `video5SecondViewRate` decimal(10,2) unsigned DEFAULT '0.00',
  `videoUnmutes` int(10) unsigned DEFAULT '0',
  `vtr` decimal(12,2) unsigned DEFAULT '0.00',
  `vctr` decimal(12,2) unsigned DEFAULT '0.00',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`campaignId`),
  KEY `campaignId_bydate` (`campaignId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_keywords 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_keywords` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `keywordBid` decimal(10,2) DEFAULT '0.00',
  `impressions` int(11) DEFAULT '0',
  `clicks` int(11) DEFAULT '0',
  `cost` decimal(12,2) DEFAULT '0.00',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`),
  KEY `profileid` (`profileid`,`campaignId`,`adGroupId`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_keywords_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_keywords_attributed` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_keywords_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_keywords_brand` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedDetailPageViewsClicks14d` int(11) DEFAULT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedOrdersNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `attributedOrderRateNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `unitsSold14d` int(11) DEFAULT NULL,
  `dpv14d` int(11) DEFAULT NULL,
  `attributedBrandedSearches14d` int(11) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_keywords_query 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_keywords_query` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `queryid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`queryid`,`keywordId`),
  KEY `adGroupId_campaignId_profileid` (`profileid`,`campaignId`,`adGroupId`),
  KEY `queryid` (`queryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_keywords_video 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_keywords_video` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewableImpressions` int(11) DEFAULT NULL,
  `videoFirstQuartileViews` int(11) DEFAULT NULL,
  `videoMidpointViews` int(11) DEFAULT NULL,
  `videoThirdQuartileViews` int(11) DEFAULT NULL,
  `videoCompleteViews` int(11) DEFAULT NULL,
  `video5SecondViews` int(11) DEFAULT NULL,
  `video5SecondViewRate` decimal(12,2) DEFAULT NULL,
  `videoUnmutes` int(11) DEFAULT NULL,
  `vtr` decimal(12,2) DEFAULT NULL,
  `vctr` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_product_targets 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_product_targets` (
  `targetId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`),
  KEY `campaignId_adGroupId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_product_targets_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_product_targets_attributed` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions14d` decimal(10,2) DEFAULT NULL,
  `attributedConversions14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,0) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_product_targets_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_product_targets_brand` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedDetailPageViewsClicks14d` int(11) DEFAULT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedOrdersNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `attributedOrderRateNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrandPercentage14d` decimal(12,2) DEFAULT NULL,
  `unitsSold14d` int(11) DEFAULT NULL,
  `dpv14d` int(11) DEFAULT NULL,
  `attributedBrandedSearches14d` int(11) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_product_targets_video 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_product_targets_video` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewableImpressions` int(11) DEFAULT NULL,
  `videoFirstQuartileViews` int(11) DEFAULT NULL,
  `videoMidpointViews` int(11) DEFAULT NULL,
  `videoThirdQuartileViews` int(11) DEFAULT NULL,
  `videoCompleteViews` int(11) DEFAULT NULL,
  `video5SecondViews` int(11) DEFAULT NULL,
  `video5SecondViewRate` decimal(12,2) DEFAULT NULL,
  `videoUnmutes` int(11) DEFAULT NULL,
  `vtr` decimal(12,2) DEFAULT NULL,
  `vctr` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_adgroups 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`),
  KEY `adGroupId_campaignId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_adgroups_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups_attributed` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int(11) DEFAULT NULL,
  `attributedConversions7d` int(11) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions30d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30d` int(11) DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_adgroups_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups_attributed_new` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_adgroups_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups_attributed_same` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions7dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions30dSameSKU` int(11) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_adgroups_attributed_view 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups_attributed_view` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewImpressions` int(11) DEFAULT NULL,
  `viewAttributedConversions14d` int(11) DEFAULT NULL,
  `viewAttributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `viewAttributedSales14d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_asins 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_asins` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `asin` char(50) COLLATE utf8mb4_bin NOT NULL,
  `sku` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `otherAsin` char(50) COLLATE utf8mb4_bin NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `attributedUnitsOrdered1d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1dOtherSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7dOtherSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14dOtherSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30dOtherSKU` int(11) DEFAULT NULL,
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
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`),
  KEY `campaignId_profileid` (`profileid`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_attributed` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int(11) DEFAULT NULL,
  `attributedConversions7d` int(11) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions30d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30d` int(11) DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_attributed_new` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_attributed_same` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions7dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions30dSameSKU` int(11) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns_attributed_view 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_attributed_view` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewImpressions` int(11) DEFAULT NULL,
  `viewAttributedConversions14d` int(11) DEFAULT NULL,
  `viewAttributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `viewAttributedSales14d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns_t00001 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_t00001` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `currency` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `attributedDPV14d` int(11) DEFAULT NULL,
  `attributedUnitsSold14d` int(11) DEFAULT NULL,
  `attributedSales14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`,`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_productads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`),
  KEY `profileid_adGroupId` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_productads_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads_attributed` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int(11) DEFAULT NULL,
  `attributedConversions7d` int(11) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions30d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30d` int(11) DEFAULT NULL,
  `attributedSales1d` decimal(10,2) DEFAULT NULL,
  `attributedSales7d` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,2) DEFAULT NULL,
  `attributedSales30d` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_productads_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads_attributed_new` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_productads_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads_attributed_same` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions7dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions30dSameSKU` int(11) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_productads_attributed_view 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads_attributed_view` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewImpressions` int(11) DEFAULT NULL,
  `viewAttributedConversions14d` int(11) DEFAULT NULL,
  `viewAttributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `viewAttributedSales14d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_product_targets 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets` (
  `targetId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`),
  KEY `campaignId_adGroupId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_product_targets_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets_attributed` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int(11) DEFAULT NULL,
  `attributedConversions7d` int(11) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions30d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30d` int(11) DEFAULT NULL,
  `attributedSales1d` decimal(10,2) DEFAULT NULL,
  `attributedSales7d` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,2) DEFAULT NULL,
  `attributedSales30d` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_product_targets_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets_attributed_new` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_product_targets_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets_attributed_same` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions7dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions30dSameSKU` int(11) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_product_targets_attributed_view 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets_attributed_view` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewImpressions` int(11) DEFAULT NULL,
  `viewAttributedConversions14d` int(11) DEFAULT NULL,
  `viewAttributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `viewAttributedSales14d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_adgroups 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_adgroups` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`),
  KEY `proadcam` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_adgroups_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_adgroups_attributed` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int(11) DEFAULT NULL,
  `attributedConversions7d` int(11) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions30d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30d` int(11) DEFAULT NULL,
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
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions7dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions30dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` int(11) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_asins 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_asins` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `advertisedAsin` char(50) COLLATE utf8mb4_bin NOT NULL COMMENT 'advertisedAsin',
  `sku` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `purchasedAsin` char(50) COLLATE utf8mb4_bin NOT NULL COMMENT 'purchasedAsin',
  `attributedSales1d` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales7d` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales14d` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales30d` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales1dSameSKU` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales7dSameSKU` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales14dSameSKU` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedSales30dSameSKU` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `attributedConversions1d` int(11) NOT NULL DEFAULT '0',
  `attributedConversions7d` int(11) NOT NULL DEFAULT '0',
  `attributedConversions14d` int(11) NOT NULL DEFAULT '0',
  `attributedConversions30d` int(11) NOT NULL DEFAULT '0',
  `attributedConversions1dSameSKU` int(11) NOT NULL DEFAULT '0',
  `attributedConversions7dSameSKU` int(11) NOT NULL DEFAULT '0',
  `attributedConversions14dSameSKU` int(11) NOT NULL DEFAULT '0',
  `attributedConversions30dSameSKU` int(11) NOT NULL DEFAULT '0',
  `attributedUnitsOrdered1d` int(11) NOT NULL DEFAULT '0',
  `attributedUnitsOrdered7d` int(11) NOT NULL DEFAULT '0',
  `attributedUnitsOrdered14d` int(11) NOT NULL DEFAULT '0',
  `attributedUnitsOrdered30d` int(11) NOT NULL DEFAULT '0',
  `attributedUnitsOrdered1dSameSKU` int(11) NOT NULL DEFAULT '0',
  `attributedUnitsOrdered7dSameSKU` int(11) NOT NULL DEFAULT '0',
  `attributedUnitsOrdered14dSameSKU` int(11) NOT NULL DEFAULT '0',
  `attributedUnitsOrdered30dSameSKU` int(11) NOT NULL DEFAULT '0',
  `profileid` bigint(20) unsigned NOT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`advertisedAsin`,`purchasedAsin`,`keywordId`),
  KEY `profileid` (`profileid`,`campaignId`,`adGroupId`,`advertisedAsin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_compaigns 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_compaigns` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignBudget` decimal(12,2) DEFAULT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`),
  KEY `campaignId_profileid` (`profileid`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_compaigns_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_compaigns_attributed` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int(11) DEFAULT NULL,
  `attributedConversions7d` int(11) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions30d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30d` int(11) DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_compaigns_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_compaigns_attributed_same` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions7dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions30dSameSKU` int(11) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_compaigns_place 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_compaigns_place` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignBudget` decimal(12,2) DEFAULT NULL,
  `placementid` int(11) NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_compaigns_place_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_compaigns_place_attributed` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int(11) NOT NULL,
  `attributedConversions1d` int(11) DEFAULT NULL,
  `attributedConversions7d` int(11) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions30d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30d` int(11) DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_compaigns_place_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_compaigns_place_attributed_same` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int(11) NOT NULL,
  `attributedConversions1dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions7dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions30dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` int(11) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_keywords 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_keywords` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`),
  KEY `campaignId_adGroupId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='campaignId';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_keywords_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_keywords_attributed` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int(11) DEFAULT NULL,
  `attributedConversions7d` int(11) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions30d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30d` int(11) DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='campaignId';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_keywords_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_keywords_attributed_same` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions7dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions30dSameSKU` int(11) DEFAULT NULL,
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
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `queryid` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`queryid`,`keywordId`),
  KEY `adGroupId_campaignId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_keywords_query_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_keywords_query_attributed` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `queryid` bigint(20) unsigned NOT NULL,
  `attributedConversions1d` int(11) DEFAULT NULL,
  `attributedConversions7d` int(11) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions30d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30d` int(11) DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`queryid`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_keywords_query_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_keywords_query_attributed_same` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `queryid` bigint(20) unsigned NOT NULL,
  `attributedConversions1dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions7dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions30dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` int(11) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`queryid`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_productads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_productads` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`),
  KEY `profileid_campaignId_adGroupId` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_productads_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_productads_attributed` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int(11) DEFAULT NULL,
  `attributedConversions7d` int(11) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions30d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30d` int(11) DEFAULT NULL,
  `attributedSales1d` decimal(10,2) DEFAULT NULL,
  `attributedSales7d` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,2) DEFAULT NULL,
  `attributedSales30d` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_productads_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_productads_attributed_same` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions7dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions30dSameSKU` int(11) DEFAULT NULL,
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
  `targetId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`),
  KEY `campaignId_adGroupId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_product_targets_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_product_targets_attributed` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1d` int(11) DEFAULT NULL,
  `attributedConversions7d` int(11) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions30d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30d` int(11) DEFAULT NULL,
  `attributedSales1d` decimal(10,2) DEFAULT NULL,
  `attributedSales7d` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,2) DEFAULT NULL,
  `attributedSales30d` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_product_targets_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_product_targets_attributed_same` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions1dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions7dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions30dSameSKU` int(11) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_product_targets_query 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_product_targets_query` (
  `targetId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `queryid` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`targetId`,`queryid`),
  KEY `campaignId_adGroupId_profileid` (`profileid`,`campaignId`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_product_targets_query_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_product_targets_query_attributed` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `queryid` bigint(20) unsigned NOT NULL,
  `attributedConversions1d` int(11) DEFAULT NULL,
  `attributedConversions7d` int(11) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions30d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered1d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14d` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30d` int(11) DEFAULT NULL,
  `attributedSales1d` decimal(10,2) DEFAULT NULL,
  `attributedSales7d` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,2) DEFAULT NULL,
  `attributedSales30d` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`,`queryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_product_targets_query_attributed_same 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_product_targets_query_attributed_same` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `queryid` bigint(20) unsigned NOT NULL,
  `attributedConversions1dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions7dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  `attributedConversions30dSameSKU` int(11) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrdered1dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered7dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered14dSameSKU` int(11) DEFAULT NULL,
  `attributedUnitsOrdered30dSameSKU` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`,`queryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt_placement 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_placement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt_query 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_query` (
  `id` bigint(20) unsigned NOT NULL,
  `query` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `query` (`query`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_schedule_plan 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_schedule_plan` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` char(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '计划名称',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态 enabled ： 已开启    paused ： 已禁止   disable  : 已结束',
  `startDate` date DEFAULT NULL COMMENT '开始时间',
  `endDate` date DEFAULT NULL,
  `weekdays` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `startTime` time DEFAULT NULL,
  `endTime` time DEFAULT NULL,
  `remark` char(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='广告定时任务';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_schedule_plandata 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_schedule_plandata` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `planid` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned zerofill DEFAULT '00000000000000000000',
  `adId` bigint(20) unsigned zerofill DEFAULT '00000000000000000000',
  `keywordId` bigint(20) unsigned zerofill DEFAULT '00000000000000000000',
  `oldbid` decimal(10,2) DEFAULT NULL,
  `oldstatus` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `profileid_campaignId_adGroupId_adId_keywordId` (`profileid`,`campaignId`,`adGroupId`,`keywordId`,`adId`,`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='广告定时任务';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_schedule_planitem 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_schedule_planitem` (
  `taskId` char(36) COLLATE utf8mb4_bin NOT NULL,
  `planId` bigint(20) unsigned NOT NULL,
  `type` char(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'one单次执行，cycle周期执行',
  `startTime` time DEFAULT NULL COMMENT '开始时间',
  `status` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `bid` decimal(12,2) DEFAULT NULL,
  `weekdays` char(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '1 ：周日； 2：周一 ；13：周日和周二 以此类推',
  PRIMARY KEY (`taskId`,`planId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_serch_history 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_serch_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) unsigned DEFAULT NULL,
  `ftype` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `fcondition` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_snapshot 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_snapshot` (
  `snapshotId` char(75) COLLATE utf8mb4_bin NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `region` char(2) COLLATE utf8mb4_bin NOT NULL,
  `status` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `location` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL,
  `recordtype` char(30) COLLATE utf8mb4_bin DEFAULT NULL,
  `campaignType` char(5) COLLATE utf8mb4_bin DEFAULT NULL,
  `fileSize` int(11) DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `requesttime` datetime DEFAULT NULL,
  `treat_number` int(11) DEFAULT NULL,
  `treat_status` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `log` varchar(5000) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`snapshotId`),
  KEY `Index 2` (`profileid`,`recordtype`,`campaignType`,`region`),
  KEY `Index 3` (`requesttime`,`treat_number`,`treat_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_stores 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_stores` (
  `profileid` bigint(20) unsigned NOT NULL,
  `entityId` varchar(30) COLLATE utf8mb4_bin NOT NULL,
  `brandEntityId` varchar(30) COLLATE utf8mb4_bin NOT NULL,
  `storePageId` varchar(50) COLLATE utf8mb4_bin NOT NULL,
  `storeName` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `storePageUrl` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `storePageName` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `opptime` datetime DEFAULT NULL,
  PRIMARY KEY (`entityId`,`brandEntityId`,`profileid`,`storePageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_sumalltype 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_sumalltype` (
  `profileid` bigint(20) unsigned NOT NULL,
  `byday` date NOT NULL,
  `state` char(10) COLLATE utf8mb4_bin NOT NULL,
  `campaignType` char(4) COLLATE utf8mb4_bin NOT NULL,
  `recordType` char(25) COLLATE utf8mb4_bin NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`profileid`,`campaignType`,`state`,`recordType`,`byday`),
  KEY `byday` (`byday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='计划的 state,campagintype,recordtype 都是task';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_sumpdtads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_sumpdtads` (
  `profileid` bigint(20) unsigned NOT NULL,
  `byday` date NOT NULL,
  `ctype` char(2) COLLATE utf8mb4_bin NOT NULL DEFAULT 'sp',
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(14,2) DEFAULT NULL,
  `currency` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `attributedUnitsOrdered` int(11) DEFAULT NULL,
  `attributedSales` decimal(14,2) DEFAULT NULL,
  `attributedConversions` int(11) DEFAULT NULL,
  PRIMARY KEY (`profileid`,`byday`,`ctype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='转化';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_warningdate 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_warningdate` (
  `shopid` bigint(20) NOT NULL,
  `recordType` char(15) COLLATE utf8mb4_bin NOT NULL,
  `ftype` char(15) COLLATE utf8mb4_bin NOT NULL COMMENT 'yesterday：昨日突降；sequent：连续下降；co：同期下降',
  `num_impressions` decimal(10,2) DEFAULT NULL,
  `num_CSRT` decimal(10,2) DEFAULT NULL,
  `num_ACOS` decimal(10,2) DEFAULT NULL,
  `num_clicks` decimal(10,2) DEFAULT NULL COMMENT '比率',
  `absoluteNum_impressions` int(11) DEFAULT NULL COMMENT '绝对值',
  `absoluteNum_clicks` int(11) DEFAULT NULL,
  `absoluteNum_ACOS` decimal(10,2) DEFAULT NULL,
  `absoluteNum_CSRT` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`shopid`,`recordType`,`ftype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_region 结构
CREATE TABLE IF NOT EXISTS `t_amz_region` (
  `code` char(2) COLLATE utf8mb4_bin NOT NULL,
  `name` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `advname` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `advpoint` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `client_id` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `client_secret` char(100) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_erp_serial_num 结构
CREATE TABLE IF NOT EXISTS `t_erp_serial_num` (
  `id` char(36) COLLATE utf8mb4_bin NOT NULL,
  `ftype` char(36) COLLATE utf8mb4_bin DEFAULT NULL,
  `seqno` int(11) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `prefix_date` date DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`prefix_date`,`ftype`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_exchangerate 结构
CREATE TABLE IF NOT EXISTS `t_exchangerate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '币别',
  `price` decimal(18,6) DEFAULT NULL COMMENT '汇率',
  `symbol` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `type` varchar(15) COLLATE utf8mb4_bin DEFAULT NULL,
  `utctime` datetime DEFAULT NULL COMMENT '服务器更新时间',
  `volume` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=444945 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_exchangerate_customer 结构
CREATE TABLE IF NOT EXISTS `t_exchangerate_customer` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `name` char(5) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '币别',
  `price` decimal(18,6) DEFAULT NULL COMMENT '汇率',
  `symbol` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `type` varchar(15) COLLATE utf8mb4_bin DEFAULT NULL,
  `utctime` datetime DEFAULT NULL COMMENT '服务器更新时间',
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`shopid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_marketplace 结构
CREATE TABLE IF NOT EXISTS `t_marketplace` (
  `marketplaceId` varchar(15) COLLATE utf8mb4_bin NOT NULL COMMENT '站点编码ID',
  `market` char(5) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '站点简码',
  `name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '站点名称',
  `region_name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '所属区域名称',
  `region` char(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '所属区域简码',
  `end_point` varchar(40) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '所属区域站点',
  `point_name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `accessKey` varchar(60) COLLATE utf8mb4_bin DEFAULT NULL,
  `secretKey` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `dim_units` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `weight_units` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `currency` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `findex` int(11) DEFAULT '0',
  `adv_end_point` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL,
  `aws_access_key` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL,
  `aws_secret_key` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `associate_tag` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL,
  `developer_url` varchar(1100) COLLATE utf8mb4_bin DEFAULT NULL,
  `dev_account_num` char(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `bytecode` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `sp_api_endpoint` char(40) COLLATE utf8mb4_bin DEFAULT NULL,
  `aws_region` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `context` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

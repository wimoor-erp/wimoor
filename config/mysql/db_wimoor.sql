-- --------------------------------------------------------
-- 主机:                            127.0.0.1
-- 服务器版本:                        8.0.20 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 db_wimoor 的数据库结构
CREATE DATABASE IF NOT EXISTS `db_wimoor` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_wimoor`;

-- 导出  事件 db_wimoor.delete_adv_request 结构
DELIMITER //
CREATE EVENT `delete_adv_request` ON SCHEDULE EVERY 1 DAY STARTS '2021-12-11 23:37:03' ON COMPLETION PRESERVE ENABLE DO BEGIN
delete from t_amz_adv_request WHERE requesttime<DATE_SUB(NOW(),INTERVAL 2 DAY) AND byday<DATE_SUB(NOW(),INTERVAL 32 DAY);
END//
DELIMITER ;


-- 导出  表 db_wimoor.oauth_client_details 结构
CREATE TABLE IF NOT EXISTS `oauth_client_details` (
  `client_id` varchar(100) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int DEFAULT NULL,
  `refresh_token_validity` int DEFAULT NULL,
  `additional_information` varchar(2000) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_advert_warning_keywords_data 结构
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
  KEY `shopid` (`shopid`) USING BTREE,
  KEY `ftype` (`ftype`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_advert_warning_product_data 结构
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
  KEY `shopid` (`shopid`) USING BTREE,
  KEY `ftype` (`ftype`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_adv_browsenode 结构
CREATE TABLE IF NOT EXISTS `t_adv_browsenode` (
  `id` int NOT NULL AUTO_INCREMENT,
  `node_id` varchar(255) DEFAULT NULL,
  `node_path` varchar(255) DEFAULT NULL,
  `query` varchar(255) DEFAULT NULL,
  `refinement` varchar(255) DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2580 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_adv_dimensions 结构
CREATE TABLE IF NOT EXISTS `t_adv_dimensions` (
  `id` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `length_units` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `width_units` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `height_units` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `weight_units` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_adv_productgroup 结构
CREATE TABLE IF NOT EXISTS `t_adv_productgroup` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `referralfeeId` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8879 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_adv_rank 结构
CREATE TABLE IF NOT EXISTS `t_adv_rank` (
  `id` char(36) NOT NULL,
  `asin` char(10) NOT NULL,
  `nodeid` char(50) DEFAULT NULL,
  `pricerange` char(100) DEFAULT NULL,
  `dim` char(36) DEFAULT NULL,
  `offerprice` int DEFAULT NULL,
  `listprice` char(10) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `lowestnprice` char(10) DEFAULT NULL,
  `categoryrank` int DEFAULT NULL,
  `imageurl` varchar(100) DEFAULT NULL,
  `reviewsURL` varchar(300) DEFAULT NULL,
  `totalOfferNew` int DEFAULT NULL,
  `lastupdate` datetime NOT NULL,
  `reviews` int DEFAULT NULL,
  `reviewAverage` float(5,1) DEFAULT NULL,
  `productgroup` varchar(50) DEFAULT NULL,
  `marketplaceid` char(32) DEFAULT NULL,
  `estiMargin` float DEFAULT NULL,
  `estiProfit` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`asin`),
  KEY `Index 3` (`productgroup`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_adv_rank_his 结构
CREATE TABLE IF NOT EXISTS `t_adv_rank_his` (
  `id` char(36) NOT NULL,
  `asin` char(10) NOT NULL,
  `nodeid` char(50) DEFAULT NULL,
  `pricerange` char(100) DEFAULT NULL,
  `dim` char(36) DEFAULT NULL,
  `offerprice` int DEFAULT NULL,
  `listprice` char(10) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `lowestnprice` char(10) DEFAULT NULL,
  `categoryrank` int DEFAULT NULL,
  `imageurl` varchar(100) DEFAULT NULL,
  `reviewsURL` varchar(300) DEFAULT NULL,
  `totalOfferNew` int DEFAULT NULL,
  `lastupdate` datetime NOT NULL,
  `reviews` int DEFAULT NULL,
  `reviewAverage` float DEFAULT NULL,
  `productgroup` varchar(50) DEFAULT NULL,
  `marketplaceid` char(32) DEFAULT NULL,
  `estiMargin` float DEFAULT NULL,
  `estiProfit` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amazonseller_market 结构
CREATE TABLE IF NOT EXISTS `t_amazonseller_market` (
  `sellerid` char(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '卖家Sellerid',
  `marketplace_id` char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '站点ID',
  `country` char(2) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '国家编码',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '站点英文名称',
  `language` char(5) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '对应语言编码',
  `currency` char(4) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '对应币种',
  `domain` char(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '对应域名',
  `amazonauthid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '授权对应ID等同于Sellerid',
  `opttime` datetime(6) DEFAULT NULL COMMENT '操作时间',
  `disable` bit(1) DEFAULT b'0' COMMENT '操作人',
  PRIMARY KEY (`sellerid`,`marketplace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='授权对应区域客户所有绑定的站点';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amazon_auth 结构
CREATE TABLE IF NOT EXISTS `t_amazon_auth` (
  `id` bigint unsigned NOT NULL,
  `shop_id` bigint unsigned DEFAULT NULL COMMENT '用户ID',
  `sellerid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卖家ID',
  `groupid` bigint unsigned DEFAULT NULL,
  `region` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `MWSAuthToken` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '卖家授权码',
  `disable` bit(1) DEFAULT b'0',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `pictureId` bigint unsigned DEFAULT NULL,
  `status` char(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `statusupdate` datetime DEFAULT NULL,
  `productdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `refreshinvtime` datetime DEFAULT NULL,
  `refresh_token` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `aws_region` char(15) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 3` (`sellerid`) USING BTREE,
  KEY `disable` (`disable`),
  KEY `shop_id` (`shop_id`),
  KEY `Index_id_shopid` (`groupid`) USING BTREE,
  KEY `region` (`region`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='亚马逊账号授权';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amazon_auth_market_performance 结构
CREATE TABLE IF NOT EXISTS `t_amazon_auth_market_performance` (
  `amazonauthid` bigint unsigned NOT NULL,
  `sellerid` char(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `marketplaceid` char(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `performance` varchar(6000) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `refreshtime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amazon_group 结构
CREATE TABLE IF NOT EXISTS `t_amazon_group` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `profitcfgid` bigint unsigned DEFAULT NULL COMMENT '店铺默认利润方案',
  `oldid` char(36) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`shopid`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_advert_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report` (
  `id` char(36) NOT NULL,
  `sellerid` char(15) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `sku` char(50) NOT NULL,
  `adId` bigint DEFAULT NULL,
  `asin` char(20) DEFAULT NULL,
  `bydate` date NOT NULL,
  `campaign_name` char(128) NOT NULL,
  `ad_Group_Name` char(128) NOT NULL,
  `clicks` int DEFAULT NULL,
  `impressions` int NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_advert_report_summary 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report_summary` (
  `sellerid` char(15) NOT NULL,
  `id` bigint unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `ctype` char(2) NOT NULL DEFAULT 'sp',
  `sku` char(50) NOT NULL,
  `asin` char(20) DEFAULT NULL,
  `bydate` date NOT NULL,
  `clicks` int DEFAULT NULL,
  `impressions` int NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_advert_report_summary_month 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report_summary_month` (
  `sellerid` char(15) NOT NULL,
  `id` bigint unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `ctype` char(2) NOT NULL DEFAULT 'sp',
  `sku` char(50) NOT NULL,
  `asin` char(20) DEFAULT NULL,
  `bydate` date NOT NULL,
  `clicks` int DEFAULT NULL,
  `impressions` int NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_advert_report_summary_week 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report_summary_week` (
  `sellerid` char(15) NOT NULL,
  `id` bigint unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `ctype` char(2) NOT NULL DEFAULT 'sp',
  `sku` char(50) NOT NULL,
  `asin` char(20) DEFAULT NULL,
  `bydate` date NOT NULL,
  `clicks` int DEFAULT NULL,
  `impressions` int NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_adgroups 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  `state` char(50) DEFAULT NULL,
  `defaultBid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`),
  KEY `profileid_name` (`name`(255)),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_adgroups_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups_hsa` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `name` char(200) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_adgroups_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups_sd` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  `state` char(50) DEFAULT NULL,
  `defaultBid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`),
  KEY `profileid_name` (`name`(255)),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_assets 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_assets` (
  `assetId` varchar(100) NOT NULL,
  `brandEntityId` varchar(30) NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `mediaType` varchar(10) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  `opptime` datetime DEFAULT NULL,
  PRIMARY KEY (`profileid`,`brandEntityId`,`assetId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_auth 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_auth` (
  `id` bigint unsigned NOT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `code` char(36) DEFAULT NULL,
  `region` char(2) DEFAULT NULL,
  `access_token` varchar(800) DEFAULT NULL,
  `refresh_token` varchar(800) DEFAULT NULL,
  `token_type` char(20) DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `disableTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid` (`shopid`),
  KEY `groupid` (`groupid`),
  KEY `region` (`region`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_brand` (
  `profileid` bigint unsigned NOT NULL,
  `brandId` varchar(50) NOT NULL,
  `brandEntityId` varchar(50) NOT NULL,
  `brandRegistryName` varchar(50) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`brandId`,`brandEntityId`,`profileid`),
  KEY `profileid_brandRegistryName` (`profileid`,`brandRegistryName`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_browsenode 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_browsenode` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `parentid` bigint unsigned DEFAULT NULL,
  `country` char(2) DEFAULT NULL,
  `level` int DEFAULT NULL,
  `is_category_root` bit(1) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`parentid`),
  KEY `Index 3` (`country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_campaigns 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaigns` (
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `portfolioid` bigint unsigned DEFAULT NULL,
  `name` char(200) DEFAULT NULL COMMENT '广告活动名称',
  `campaignType` char(20) DEFAULT NULL COMMENT 'sp 和 sb（hsa）',
  `dailyBudget` decimal(12,2) DEFAULT NULL COMMENT '每日预算',
  `targetingType` char(50) DEFAULT NULL COMMENT '投放类型',
  `state` char(50) DEFAULT NULL,
  `premiumBidAdjustment` char(10) DEFAULT NULL COMMENT '竞价',
  `bidding` varchar(200) DEFAULT NULL,
  `startDate` date DEFAULT NULL COMMENT '开始时间',
  `endDate` date DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`campaignId`),
  KEY `idx_profileid_name` (`name`),
  KEY `profileid` (`profileid`),
  KEY `state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_campaigns_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaigns_hsa` (
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `portfolioid` bigint unsigned DEFAULT NULL,
  `name` char(200) DEFAULT NULL,
  `tags` char(200) DEFAULT NULL,
  `budgetType` char(15) DEFAULT NULL,
  `budget` decimal(12,2) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `servingStatus` char(30) DEFAULT NULL,
  `brandEntityId` char(30) DEFAULT NULL,
  `landingPage` varchar(200) DEFAULT NULL,
  `adFormat` char(30) DEFAULT NULL,
  `creative` varchar(300) DEFAULT NULL,
  `spendingPolicy` char(10) DEFAULT NULL,
  `bidOptimization` char(10) DEFAULT NULL,
  `bidMultiplier` decimal(12,2) DEFAULT NULL,
  `bidAdjustments` varchar(350) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`profileid`),
  KEY `profileid_name` (`profileid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_campaigns_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaigns_sd` (
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `name` char(200) DEFAULT NULL,
  `tactic` char(30) DEFAULT NULL,
  `budgetType` char(15) DEFAULT NULL,
  `budget` decimal(12,2) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`profileid`),
  KEY `profileid` (`profileid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_campkeywords_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campkeywords_negativa` (
  `profileid` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `keywordId` bigint unsigned NOT NULL,
  `keywordText` char(100) DEFAULT NULL,
  `matchType` char(20) DEFAULT NULL,
  `state` char(15) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_group 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_group` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_keywords 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `keywordId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignType` char(5) NOT NULL,
  `keywordText` char(100) DEFAULT NULL,
  `nativeLanguageKeyword` char(100) DEFAULT NULL,
  `matchType` char(15) DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `profileid_keywordText` (`profileid`),
  KEY `campaignId` (`campaignId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `keywordText` (`keywordText`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_keywords_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords_hsa` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `keywordId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignType` char(5) NOT NULL,
  `keywordText` char(100) DEFAULT NULL,
  `nativeLanguageKeyword` char(100) DEFAULT NULL,
  `matchType` char(15) DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `profileid_keywordText` (`profileid`),
  KEY `campaignId` (`campaignId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `keywordText` (`keywordText`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_keywords_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords_negativa` (
  `keywordId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `keywordText` char(100) DEFAULT NULL,
  `matchType` char(20) DEFAULT NULL,
  `campaignType` char(4) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_media_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_media_hsa` (
  `mediaId` char(60) NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned DEFAULT NULL,
  `status` char(30) DEFAULT NULL,
  `statusMetadata` varchar(100) DEFAULT NULL,
  `publishedMediaUrl` varchar(2000) DEFAULT NULL,
  `operator` bigint unsigned NOT NULL DEFAULT '0',
  `opttime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`mediaId`),
  KEY `profileid` (`profileid`,`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_operate_log 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_operate_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `profileid` bigint unsigned DEFAULT NULL,
  `campaignId` bigint unsigned DEFAULT NULL,
  `adGroupId` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `beanclasz` char(100) DEFAULT NULL,
  `beforeobject` longtext,
  `afterobject` longtext,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `campaignId` (`campaignId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `profileid` (`profileid`,`opttime`)
) ENGINE=InnoDB AUTO_INCREMENT=319741 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_portfolios 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_portfolios` (
  `id` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `name` char(100) DEFAULT NULL COMMENT ' ',
  `policy` char(20) DEFAULT NULL COMMENT ' ',
  `currencyCode` char(10) DEFAULT NULL,
  `amount` decimal(12,2) DEFAULT NULL COMMENT ' ',
  `state` char(10) DEFAULT NULL,
  `inBudget` bit(1) DEFAULT NULL,
  `startDate` date DEFAULT NULL COMMENT ' ',
  `endDate` date DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_productads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_productads` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `asin` char(50) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adId`),
  KEY `profileid_sku` (`sku`),
  KEY `profileid` (`profileid`),
  KEY `campaignId` (`campaignId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_productads_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_productads_sd` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `asin` char(50) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adId`),
  KEY `profileid_sku` (`sku`),
  KEY `profileid` (`profileid`),
  KEY `campaignId` (`campaignId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_product_targe 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe` (
  `targetId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `expressionType` char(10) DEFAULT NULL,
  `expression` varchar(500) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `profileid_expression` (`profileid`),
  KEY `adGroupId` (`adGroupId`),
  KEY `campaignId` (`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_product_targe_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_negativa` (
  `targetId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `expressionType` char(10) DEFAULT NULL,
  `expression` varchar(500) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_product_targe_negativa_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_negativa_sd` (
  `targetId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `expressionType` char(10) DEFAULT NULL,
  `expression` varchar(500) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_product_targe_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_sd` (
  `targetId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `expressionType` char(10) DEFAULT NULL,
  `expression` varchar(500) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `profileid_expression` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_profile 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_profile` (
  `id` bigint unsigned NOT NULL,
  `countryCode` char(2) DEFAULT NULL,
  `currencyCode` char(3) DEFAULT NULL,
  `marketplaceId` char(15) DEFAULT NULL,
  `sellerId` char(20) DEFAULT NULL,
  `advauthId` bigint unsigned DEFAULT NULL,
  `timezone` char(25) DEFAULT NULL,
  `type` char(10) DEFAULT NULL,
  `dailyBudget` decimal(20,5) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `marketplaceId` (`marketplaceId`),
  KEY `authid` (`advauthId`),
  KEY `sellerId` (`sellerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_remark 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_remark` (
  `profileid` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adgroupId` bigint unsigned NOT NULL DEFAULT '0',
  `keywordId` bigint unsigned NOT NULL DEFAULT '0',
  `adId` bigint unsigned NOT NULL DEFAULT '0',
  `targetId` bigint unsigned NOT NULL DEFAULT '0',
  `remark` varchar(1000) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`profileid`,`campaignId`,`adgroupId`,`keywordId`,`adId`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='广告历史记录备注';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_remind 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_remind` (
  `profileid` bigint unsigned NOT NULL,
  `campaignid` bigint unsigned NOT NULL,
  `adgroupid` bigint unsigned NOT NULL DEFAULT '0',
  `keywordid` bigint unsigned NOT NULL DEFAULT '0',
  `adid` bigint unsigned NOT NULL DEFAULT '0',
  `targetid` bigint unsigned NOT NULL DEFAULT '0',
  `recordtype` char(10) DEFAULT NULL,
  `cycle` int DEFAULT NULL COMMENT '1当天，7（7天）',
  `quota` char(15) DEFAULT NULL COMMENT 'click（点击数），ctr（点击率）,cost（花费），acos,cr(转化率）',
  `fcondition` int DEFAULT NULL COMMENT '1是超过，2是低于',
  `subtrahend` char(15) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL COMMENT '(cycle.quota） condition(>) amount',
  `iswarn` bit(1) NOT NULL DEFAULT b'0',
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`profileid`,`campaignid`,`adgroupid`,`keywordid`,`adid`,`targetid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='广告提醒';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_report_metrics 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_report_metrics` (
  `id` int NOT NULL DEFAULT '0',
  `campaigntype` char(3) NOT NULL DEFAULT '0',
  `reporttype` char(30) NOT NULL DEFAULT '0',
  `segment` char(11) DEFAULT '0',
  `activeType` char(10) DEFAULT '0',
  `metrics` varchar(2000) NOT NULL DEFAULT '0',
  `level` int DEFAULT NULL,
  `reponsetype` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `campaigntype_reporttype_segment_activeType` (`campaigntype`,`reporttype`,`segment`,`activeType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_request 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_request` (
  `reportId` char(100) NOT NULL,
  `profileId` bigint unsigned NOT NULL,
  `recordType` char(50) DEFAULT NULL,
  `status` char(50) DEFAULT NULL,
  `statusDetails` char(100) DEFAULT NULL,
  `campaignType` char(5) DEFAULT NULL,
  `segment` char(20) DEFAULT NULL,
  `creativeType` char(20) DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `location` varchar(500) DEFAULT NULL,
  `fileSize` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `requesttime` datetime DEFAULT NULL,
  `treat_number` int unsigned DEFAULT '0',
  `treat_status` varchar(20) DEFAULT NULL,
  `log` varchar(5000) DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  PRIMARY KEY (`reportId`,`profileId`),
  KEY `Index1` (`requesttime`,`treat_number`,`treat_status`,`isrun`) USING BTREE,
  KEY `Index 2` (`profileId`,`recordType`,`campaignType`,`segment`) USING BTREE,
  KEY `byday` (`byday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='广告报表请求记录表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignBudget` decimal(12,2) DEFAULT NULL,
  `impressions` int unsigned NOT NULL DEFAULT '0',
  `clicks` int unsigned NOT NULL DEFAULT '0',
  `cost` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`),
  KEY `campaignId_profileid` (`profileid`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_attributed` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`campaignId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_brand 结构
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
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_place 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_place_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_place_attributed` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int unsigned NOT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) DEFAULT '0.00',
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_place_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_place_brand` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int unsigned NOT NULL,
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
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_place_video 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_video 结构
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
  PRIMARY KEY (`bydate`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_keywords 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_keywords_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_keywords_attributed` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_keywords_brand 结构
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
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_keywords_query 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_keywords_video 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_product_targets 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_product_targets_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_product_targets_attributed` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions14d` decimal(10,2) DEFAULT NULL,
  `attributedConversions14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,0) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_product_targets_brand 结构
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
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_product_targets_video 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_adgroups 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_adgroups_attributed 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_adgroups_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups_attributed_new` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_adgroups_attributed_same 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_adgroups_attributed_view 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups_attributed_view` (
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewImpressions` int DEFAULT NULL,
  `viewAttributedConversions14d` int DEFAULT NULL,
  `viewAttributedUnitsOrdered14d` int DEFAULT NULL,
  `viewAttributedSales14d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_asins 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_asins` (
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `asin` char(50) NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `otherAsin` char(50) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_campaigns 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_campaigns_attributed 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_campaigns_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_attributed_new` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_campaigns_attributed_same 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_campaigns_attributed_view 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_attributed_view` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewImpressions` int DEFAULT NULL,
  `viewAttributedConversions14d` int DEFAULT NULL,
  `viewAttributedUnitsOrdered14d` int DEFAULT NULL,
  `viewAttributedSales14d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_campaigns_t00001 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_t00001` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `attributedDPV14d` int DEFAULT NULL,
  `attributedUnitsSold14d` int DEFAULT NULL,
  `attributedSales14d` int DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`,`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_productads 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_productads_attributed 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_productads_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads_attributed_new` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_productads_attributed_same 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_productads_attributed_view 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads_attributed_view` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewImpressions` int DEFAULT NULL,
  `viewAttributedConversions14d` int DEFAULT NULL,
  `viewAttributedUnitsOrdered14d` int DEFAULT NULL,
  `viewAttributedSales14d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_product_targets 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_product_targets_attributed 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_product_targets_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets_attributed_new` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_product_targets_attributed_same 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_product_targets_attributed_view 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets_attributed_view` (
  `targetId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `viewImpressions` int DEFAULT NULL,
  `viewAttributedConversions14d` int DEFAULT NULL,
  `viewAttributedUnitsOrdered14d` int DEFAULT NULL,
  `viewAttributedSales14d` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_adgroups 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_adgroups_attributed 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_adgroups_attributed_same 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_asins 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_asins` (
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `targetId` bigint unsigned NOT NULL,
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `asin` char(50) NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `otherAsin` char(50) NOT NULL,
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
  PRIMARY KEY (`bydate`,`asin`,`otherAsin`,`keywordId`,`targetId`),
  KEY `profileid` (`profileid`,`campaignId`,`adGroupId`,`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_compaigns 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_compaigns_attributed 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_compaigns_attributed_same 结构
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
  PRIMARY KEY (`campaignId`,`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_compaigns_place 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_compaigns_place_attributed 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_compaigns_place_attributed_same 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_keywords 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='campaignId';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_keywords_attributed 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='campaignId';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_keywords_attributed_same 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='campaignId';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_keywords_query 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_keywords_query_attributed 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_keywords_query_attributed_same 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_productads 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_productads_attributed 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_productads_attributed_same 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_product_targets 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_product_targets_attributed 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_product_targets_attributed_same 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_product_targets_query 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_product_targets_query_attributed 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_product_targets_query_attributed_same 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt_compaignsplace_bkp20210924 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_compaignsplace_bkp20210924` (
  `campaignId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `placement` char(50) NOT NULL,
  `campaignStatus` char(10) DEFAULT NULL,
  `campaignName` char(200) DEFAULT NULL,
  `bidPlus` char(10) DEFAULT NULL,
  `campaignBudget` decimal(12,2) DEFAULT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placement`),
  KEY `profileid` (`profileid`),
  KEY `bydate` (`bydate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt_keywordsquery_bkp20210924 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_keywordsquery_bkp20210924` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `query` char(200) NOT NULL,
  `keywordText` char(100) DEFAULT NULL,
  `adGroupName` char(200) DEFAULT NULL,
  `campaignName` char(200) DEFAULT NULL,
  `matchType` char(15) DEFAULT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`,`query`),
  KEY `keywordsquery_bydate_profileid` (`bydate`),
  KEY `adGroupId` (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt_keywords_bkp20210924 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_keywords_bkp20210924` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `keywordText` char(100) DEFAULT NULL,
  `adGroupName` char(200) DEFAULT NULL,
  `campaignName` char(200) DEFAULT NULL,
  `matchType` char(50) DEFAULT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(12,2) DEFAULT NULL,
  `attributedSales7d` decimal(12,2) DEFAULT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales30d` decimal(12,2) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`),
  KEY `bydate` (`bydate`),
  KEY `campaignId` (`campaignId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='campaignId'
/*!50100 PARTITION BY RANGE (year(`bydate`))
SUBPARTITION BY HASH (quarter(`bydate`))
SUBPARTITIONS 4
(PARTITION p2017 VALUES LESS THAN (2018) ENGINE = InnoDB,
 PARTITION p2018 VALUES LESS THAN (2019) ENGINE = InnoDB,
 PARTITION p2019 VALUES LESS THAN (2020) ENGINE = InnoDB,
 PARTITION p2020 VALUES LESS THAN (2021) ENGINE = InnoDB,
 PARTITION p2021 VALUES LESS THAN (2022) ENGINE = InnoDB,
 PARTITION p2022 VALUES LESS THAN (2023) ENGINE = InnoDB,
 PARTITION p2023 VALUES LESS THAN (2024) ENGINE = InnoDB,
 PARTITION p2024 VALUES LESS THAN (2025) ENGINE = InnoDB,
 PARTITION p2025 VALUES LESS THAN (2026) ENGINE = InnoDB,
 PARTITION p2026 VALUES LESS THAN (2027) ENGINE = InnoDB,
 PARTITION p2027 VALUES LESS THAN (2028) ENGINE = InnoDB,
 PARTITION p2028 VALUES LESS THAN (2029) ENGINE = InnoDB,
 PARTITION p2029 VALUES LESS THAN (2030) ENGINE = InnoDB,
 PARTITION p2030 VALUES LESS THAN (2031) ENGINE = InnoDB,
 PARTITION p2031 VALUES LESS THAN (2032) ENGINE = InnoDB,
 PARTITION p2032 VALUES LESS THAN (2033) ENGINE = InnoDB,
 PARTITION p2033 VALUES LESS THAN (2034) ENGINE = InnoDB,
 PARTITION p2034 VALUES LESS THAN (2035) ENGINE = InnoDB,
 PARTITION p2035 VALUES LESS THAN (2036) ENGINE = InnoDB,
 PARTITION p2036 VALUES LESS THAN (2037) ENGINE = InnoDB,
 PARTITION p2037 VALUES LESS THAN (2038) ENGINE = InnoDB,
 PARTITION p2038 VALUES LESS THAN (2039) ENGINE = InnoDB,
 PARTITION p2039 VALUES LESS THAN (2040) ENGINE = InnoDB,
 PARTITION p2040 VALUES LESS THAN (2041) ENGINE = InnoDB,
 PARTITION p2041 VALUES LESS THAN (2042) ENGINE = InnoDB) */;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt_keywords_hsa_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_keywords_hsa_attributed` (
  `keywordId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) DEFAULT '0.00',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt_placement 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_placement` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt_productads_bkp20210924 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_productads_bkp20210924` (
  `adId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `campaignName` char(200) DEFAULT NULL,
  `adGroupName` char(200) DEFAULT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `asin` char(10) DEFAULT NULL,
  `sku` char(40) DEFAULT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(10,2) DEFAULT NULL,
  `attributedSales7d` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,2) DEFAULT NULL,
  `attributedSales30d` decimal(10,2) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`profileid`,`bydate`,`adId`),
  KEY `campaignId` (`campaignId`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt_product_target_query_bkp20210924 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_product_target_query_bkp20210924` (
  `targetId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `query` char(100) NOT NULL,
  `targetingType` char(50) NOT NULL,
  `targetingExpression` char(200) DEFAULT NULL,
  `campaignName` char(200) DEFAULT NULL,
  `adGroupName` char(200) DEFAULT NULL,
  `targetingText` char(200) DEFAULT NULL,
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `attributedConversions1d` int DEFAULT NULL,
  `attributedConversions7d` int DEFAULT NULL,
  `attributedConversions14d` int DEFAULT NULL,
  `attributedConversions30d` int DEFAULT NULL,
  `attributedConversions1dSameSKU` int DEFAULT NULL,
  `attributedConversions7dSameSKU` int DEFAULT NULL,
  `attributedConversions14dSameSKU` int DEFAULT NULL,
  `attributedConversions30dSameSKU` int DEFAULT NULL,
  `attributedUnitsOrdered1d` int DEFAULT NULL,
  `attributedUnitsOrdered7d` int DEFAULT NULL,
  `attributedUnitsOrdered14d` int DEFAULT NULL,
  `attributedUnitsOrdered30d` int DEFAULT NULL,
  `attributedSales1d` decimal(10,2) DEFAULT NULL,
  `attributedSales7d` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,2) DEFAULT NULL,
  `attributedSales30d` decimal(10,2) DEFAULT NULL,
  `attributedSales1dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales7dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales30dSameSKU` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`,`query`),
  KEY `campaignId` (`campaignId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `bydate_profileid` (`bydate`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt_query 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_query` (
  `id` bigint unsigned NOT NULL,
  `query` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `query` (`query`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_schedule_plan 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_schedule_plan` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `taskName` char(20) DEFAULT NULL COMMENT '计划名称',
  `status` char(10) DEFAULT NULL COMMENT '状态 enabled ： 已开启    paused ： 已禁止   disable  : 已结束',
  `conditionsExecute` varchar(2000) DEFAULT NULL,
  `executeStatus` char(10) DEFAULT NULL COMMENT 'wait ：等待执行    executing ：执行中     completed  ： 执行完成   error : 执行失败',
  `startDate` date DEFAULT NULL COMMENT '开始时间',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `remark` char(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=264 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='广告定时任务';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_schedule_plandata 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_schedule_plandata` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `planid` bigint unsigned NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned zerofill DEFAULT '00000000000000000000',
  `adId` bigint(20) unsigned zerofill DEFAULT '00000000000000000000',
  `keywordId` bigint(20) unsigned zerofill DEFAULT '00000000000000000000',
  `oldbid` decimal(10,2) DEFAULT NULL,
  `oldstatus` char(10) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `profileid_campaignId_adGroupId_adId_keywordId` (`profileid`,`campaignId`,`adGroupId`,`keywordId`,`adId`,`planid`)
) ENGINE=InnoDB AUTO_INCREMENT=733 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='广告定时任务';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_schedule_planitem 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_schedule_planitem` (
  `taskId` char(36) NOT NULL,
  `planId` bigint unsigned NOT NULL,
  `type` char(10) DEFAULT NULL COMMENT 'one单次执行，cycle周期执行',
  `startTime` time DEFAULT NULL COMMENT '开始时间',
  `status` char(10) DEFAULT NULL,
  `bid` decimal(12,2) DEFAULT NULL,
  `weekdays` char(20) DEFAULT NULL COMMENT '1 ：周日； 2：周一 ；13：周日和周二 以此类推',
  PRIMARY KEY (`taskId`,`planId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_serch_history 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_serch_history` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `userId` bigint unsigned DEFAULT NULL,
  `ftype` char(20) DEFAULT NULL,
  `fcondition` varchar(200) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_snapshot 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_snapshot` (
  `snapshotId` char(75) NOT NULL,
  `profileid` bigint unsigned NOT NULL,
  `region` char(2) NOT NULL,
  `status` char(20) DEFAULT NULL,
  `location` varchar(2000) DEFAULT NULL,
  `recordtype` char(30) DEFAULT NULL,
  `campaignType` char(5) DEFAULT NULL,
  `fileSize` int DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `requesttime` datetime DEFAULT NULL,
  `treat_number` int DEFAULT NULL,
  `treat_status` varchar(20) DEFAULT NULL,
  `log` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`snapshotId`),
  KEY `Index 2` (`profileid`,`recordtype`,`campaignType`,`region`) USING BTREE,
  KEY `Index 3` (`requesttime`,`treat_number`,`treat_status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_stores 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_stores` (
  `profileid` bigint unsigned NOT NULL,
  `entityId` varchar(30) NOT NULL,
  `brandEntityId` varchar(30) NOT NULL,
  `storePageId` varchar(50) NOT NULL,
  `storeName` varchar(50) DEFAULT NULL,
  `storePageUrl` varchar(100) DEFAULT NULL,
  `storePageName` varchar(50) DEFAULT NULL,
  `opptime` datetime DEFAULT NULL,
  PRIMARY KEY (`entityId`,`brandEntityId`,`profileid`,`storePageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_sumalltype 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_sumalltype` (
  `profileid` bigint unsigned NOT NULL,
  `byday` date NOT NULL,
  `state` char(10) NOT NULL,
  `campaignType` char(4) NOT NULL,
  `recordType` char(25) NOT NULL,
  `quantity` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`profileid`,`state`,`campaignType`,`recordType`,`byday`),
  KEY `byday` (`byday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='计划的 state,campagintype,recordtype 都是task';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_sumpdtads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_sumpdtads` (
  `profileid` bigint unsigned NOT NULL,
  `byday` date NOT NULL,
  `ctype` char(2) NOT NULL DEFAULT 'sp',
  `impressions` int DEFAULT NULL,
  `clicks` int DEFAULT NULL,
  `cost` decimal(14,2) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `attributedUnitsOrdered` int DEFAULT NULL,
  `attributedSales` decimal(14,2) DEFAULT NULL,
  `attributedConversions` int DEFAULT NULL,
  PRIMARY KEY (`profileid`,`byday`,`ctype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='转化';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_warningdate 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_warningdate` (
  `shopid` bigint NOT NULL,
  `recordType` char(15) NOT NULL,
  `ftype` char(15) NOT NULL COMMENT 'yesterday：昨日突降；sequent：连续下降；co：同期下降',
  `num_impressions` decimal(10,2) DEFAULT NULL,
  `num_CSRT` decimal(10,2) DEFAULT NULL,
  `num_ACOS` decimal(10,2) DEFAULT NULL,
  `num_clicks` decimal(10,2) DEFAULT NULL COMMENT '比率',
  `absoluteNum_impressions` int DEFAULT NULL COMMENT '绝对值',
  `absoluteNum_clicks` int DEFAULT NULL,
  `absoluteNum_ACOS` decimal(10,2) DEFAULT NULL,
  `absoluteNum_CSRT` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`shopid`,`recordType`,`ftype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_api_timelimit 结构
CREATE TABLE IF NOT EXISTS `t_amz_api_timelimit` (
  `id` bigint unsigned NOT NULL,
  `apiname` char(50) DEFAULT NULL,
  `maximum` int DEFAULT NULL COMMENT '一次性最多访问次数',
  `quarter` int DEFAULT NULL COMMENT '一刻钟可以访问多少次',
  `half` int DEFAULT NULL COMMENT '半小时可以访问多少次',
  `hour` int DEFAULT NULL COMMENT '一小时可以访问多少次',
  `restore_rate` int DEFAULT NULL COMMENT '每个单位时长恢复多少个',
  `restore_unit` int DEFAULT NULL COMMENT '多少秒钟可以恢复一个单位值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `apiname` (`apiname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_auth_api_timelimit 结构
CREATE TABLE IF NOT EXISTS `t_amz_auth_api_timelimit` (
  `id` bigint unsigned NOT NULL COMMENT 'ID用于单独表示一行',
  `amazonauthid` bigint unsigned NOT NULL COMMENT '授权ID，等同于SellerId',
  `apiname` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'API的名字',
  `nexttoken` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '是否有nexttoken',
  `start_time` timestamp NULL DEFAULT NULL COMMENT 'API调用的开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT 'API调用的结束时间',
  `pages` int DEFAULT '1' COMMENT '本次调用的页数',
  `restore` double DEFAULT NULL COMMENT '下一次的恢复时常',
  `lastuptime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  `log` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '异常log',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`amazonauthid`,`apiname`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新版本SPI-API使用';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_fin_account 结构
CREATE TABLE IF NOT EXISTS `t_amz_fin_account` (
  `amazonAuthid` bigint unsigned NOT NULL,
  `currency` char(10) NOT NULL,
  `groupid` char(50) NOT NULL,
  `financial_event_group_start` datetime DEFAULT NULL,
  `financial_event_group_end` datetime DEFAULT NULL,
  `fund_transfer_date` datetime DEFAULT NULL,
  `processing_status` char(40) NOT NULL,
  `trace_id` char(50) DEFAULT NULL,
  `account_tail` char(30) DEFAULT NULL,
  `converted_total` decimal(15,2) DEFAULT NULL,
  `beginning_balance` decimal(15,2) DEFAULT NULL,
  `original_total` decimal(15,2) DEFAULT NULL,
  `fund_transfer_status` char(50) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthid`,`currency`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_fin_settlement_formula 结构
CREATE TABLE IF NOT EXISTS `t_amz_fin_settlement_formula` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `formula` varchar(500) DEFAULT NULL,
  `field` varchar(500) DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `pricetype` int DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_fin_user_item 结构
CREATE TABLE IF NOT EXISTS `t_amz_fin_user_item` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `name` char(20) DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `isused` bit(1) DEFAULT b'0',
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_fin_user_item_data 结构
CREATE TABLE IF NOT EXISTS `t_amz_fin_user_item_data` (
  `id` bigint unsigned NOT NULL,
  `itemid` bigint unsigned DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `sku` char(50) DEFAULT NULL COMMENT 'sku=* 号时，是指店铺费用',
  `currency` char(5) DEFAULT NULL,
  `amount` decimal(14,2) unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `itemid_groupid_marketplaceid_byday_shopid` (`shopid`,`groupid`,`marketplaceid`,`sku`,`byday`,`itemid`,`currency`),
  KEY `byday` (`byday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户导入的SKU财务项费用-应用于商品营收其他费用项目导入';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_follow_offer 结构
CREATE TABLE IF NOT EXISTS `t_amz_follow_offer` (
  `sellerid` char(30) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `name` tinyblob,
  `positive_feedback_rating` int DEFAULT NULL,
  `feedback_count` int DEFAULT NULL,
  `refreshtime` timestamp NULL DEFAULT NULL,
  `refreshnum` int DEFAULT '0',
  PRIMARY KEY (`sellerid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_follow_offerchange 结构
CREATE TABLE IF NOT EXISTS `t_amz_follow_offerchange` (
  `id` bigint unsigned NOT NULL,
  `asin` char(10) DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `sellerid` char(20) DEFAULT NULL,
  `listing_price_amount` decimal(18,2) DEFAULT NULL,
  `shiping_amount` decimal(18,2) DEFAULT NULL,
  `currency` char(3) DEFAULT NULL,
  `is_fulfilled_by_amazon` bit(1) DEFAULT NULL,
  `is_buy_box_winner` bit(1) DEFAULT NULL,
  `is_featured_merchant` bit(1) DEFAULT NULL,
  `is_prime` bit(1) DEFAULT NULL,
  `is_national_prime` bit(1) DEFAULT NULL,
  `ships_domestically` bit(1) DEFAULT NULL,
  `findtime` timestamp NULL DEFAULT NULL,
  `losttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `asin_marketplaceid_sellerid` (`asin`,`marketplaceid`,`sellerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_inventory_health 结构
CREATE TABLE IF NOT EXISTS `t_amz_inventory_health` (
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `marketplaceid` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '站点ID',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'SKU区分大小写',
  `authid` bigint unsigned NOT NULL COMMENT '授权ID等价sellerid',
  `snapshot_date` datetime DEFAULT NULL COMMENT '报表更新时间',
  `fnsku` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '仓库SKUID',
  `asin` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'ASIN产品销售ID',
  `name` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '名称',
  `fcondition` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否新旧',
  `sales_rank` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '销售排名',
  `product_group` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '产品分组',
  `total_quantity` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '当前总库存',
  `sellable_quantity` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '可销售数量',
  `unsellable_quantity` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '不可售数量',
  `inv_age_0to90days` int DEFAULT NULL COMMENT '90天以内库龄数量',
  `inv_age_91to180days` int DEFAULT NULL COMMENT '91-180天以内库龄数量',
  `inv_age_181to270days` int DEFAULT NULL COMMENT '181-270天以内库龄数量',
  `inv_age_271to365days` int DEFAULT NULL COMMENT '271-365天以内库龄数量',
  `inv_age_365plusdays` int DEFAULT NULL COMMENT '365天以上库龄数量',
  `units_shipped_last24hrs` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '最近24小时发货',
  `units_shipped_last7days` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '最近7天发货',
  `units_shipped_last30days` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '最近30天发货',
  `units_shipped_last90days` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '最近90天发货',
  `units_shipped_last180days` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '最近180天发货',
  `units_shipped_last365days` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '最近365天发货',
  `weeks_of_cover_t7` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '7天周转',
  `weeks_of_cover_t30` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '30天周转',
  `weeks_of_cover_t90` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '90天周转',
  `weeks_of_cover_t180` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '180天周转',
  `weeks_of_cover_t365` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '360天周转',
  `num_afn_new_sellers` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '新产品的卖家数量',
  `num_afn_used_sellers` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '旧产品的卖家数量',
  `currency` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '币种',
  `your_price` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '你的售价',
  `sales_price` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '售价',
  `lowest_afn_new_price` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '新产品最低售价',
  `lowest_afn_used_price` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '旧产品最低售价',
  `lowest_mfn_new_price` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '新产品自发货最低售价',
  `lowest_mfn_used_price` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '旧产品自发货最低售价',
  `qty_to_be_charged_ltsf_12mo` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '6个月变化',
  `qty_in_long_term_storage_program` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '长期仓库数量',
  `qty_with_removals_in_progress` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '正在移除的数量',
  `projected_ltsf_12_mo` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '12个月被保护数量',
  `per_unit_volume` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '库容',
  `is_hazmat` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '是否危险品',
  `in_bound_quantity` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '待入库数量',
  `asin_limit` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '产品限制',
  `inbound_recommend_quantity` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '建议待入库数量（即发货量）',
  `qty_to_be_charged_ltsf_6mo` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '6个月变化',
  `projected_ltsf_6mo` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '6个月被保护数量',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`authid`,`marketplaceid`,`sku`,`shopid`,`currency`,`fcondition`),
  KEY `index1` (`shopid`,`authid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_notifications 结构
CREATE TABLE IF NOT EXISTS `t_amz_notifications` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `notifications` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `isrun` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_notifications_destination 结构
CREATE TABLE IF NOT EXISTS `t_amz_notifications_destination` (
  `destinationid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接受消息对象的ID',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '接受消息对象的名称',
  `resource_sqs_arn` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '消息队列arn',
  `resource_event_bridge_region` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '本地bridge区域',
  `resource_event_bridge_accountid` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '本地bridge账号',
  `resource_event_bridge_name` varchar(50) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`destinationid`) USING BTREE,
  KEY `name_amazonauthid` (`amazonauthid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='亚马逊Destination 亚马逊消息接受对象';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_notifications_subscriptions 结构
CREATE TABLE IF NOT EXISTS `t_amz_notifications_subscriptions` (
  `subscriptionId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订阅ID',
  `eventFilterType` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订阅类型',
  `payloadVersion` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '版本',
  `destinationId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接受信息的目标',
  `aggregationSettings` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '聚合时间',
  `marketplaceIds` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '站点',
  `refreshtime` datetime DEFAULT NULL COMMENT '刷新时间',
  `amazonauthid` bigint unsigned NOT NULL COMMENT '授权ID',
  PRIMARY KEY (`subscriptionId`) USING BTREE,
  KEY `eventFilterType_destinationId_amazonauthid` (`amazonauthid`,`eventFilterType`,`destinationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订阅消息对象';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_orders 结构
CREATE TABLE IF NOT EXISTS `t_amz_orders` (
  `amazon_orderid` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `displayable_shipping_label` char(50) DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `earliest_delivery_date` datetime DEFAULT NULL,
  `earliest_ship_date` datetime DEFAULT NULL,
  `fulfillment_channel` char(50) DEFAULT NULL,
  `fulfillment_supply_sourceid` char(50) DEFAULT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `latest_delivery_date` datetime DEFAULT NULL,
  `latest_ship_date` datetime DEFAULT NULL,
  `promise_response_duedate` datetime DEFAULT NULL,
  `number_of_items_shipped` int DEFAULT NULL,
  `number_of_items_unshipped` int DEFAULT NULL,
  `order_channel` char(20) DEFAULT NULL,
  `order_status` char(20) DEFAULT NULL,
  `order_total` char(20) DEFAULT NULL,
  `order_type` char(20) DEFAULT NULL,
  `payment_method` char(20) DEFAULT NULL,
  `replaced_orderid` char(20) DEFAULT NULL,
  `sales_channel` char(20) DEFAULT NULL,
  `seller_orderid` char(20) DEFAULT NULL,
  `ship_service_level` char(20) DEFAULT NULL,
  `shipment_service_level_category` char(50) DEFAULT NULL,
  `amazonAuthid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`amazon_orderid`) USING BTREE,
  KEY `marketplaceid` (`shopid`,`marketplaceid`,`purchase_date`) USING BTREE,
  KEY `group` (`shopid`,`groupid`,`marketplaceid`,`purchase_date`),
  KEY `shopid` (`shopid`,`purchase_date`),
  KEY `group2` (`shopid`,`groupid`,`purchase_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_orders_address 结构
CREATE TABLE IF NOT EXISTS `t_amz_orders_address` (
  `amazon_orderid` char(20) NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `amazonAuthid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `phone` char(20) DEFAULT NULL,
  `address_line1` char(50) DEFAULT NULL,
  `address_line2` char(50) DEFAULT NULL,
  `address_line3` char(50) DEFAULT NULL,
  `address_type` char(20) DEFAULT NULL,
  `city` char(15) DEFAULT NULL,
  `country_code` char(10) DEFAULT NULL,
  `county` char(15) DEFAULT NULL,
  `district` char(50) DEFAULT NULL,
  `municipality` char(20) DEFAULT NULL,
  `postal_code` char(20) DEFAULT NULL,
  `state_or_region` char(50) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`amazon_orderid`) USING BTREE,
  KEY `amazonAuthid` (`amazonAuthid`),
  KEY `shopid_groupid_marketplaceid` (`shopid`,`groupid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_orders_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_orders_archive` (
  `amazon_orderid` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `marketplaceid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `displayable_shipping_label` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `earliest_delivery_date` datetime DEFAULT NULL,
  `earliest_ship_date` datetime DEFAULT NULL,
  `fulfillment_channel` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fulfillment_supply_sourceid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `latest_delivery_date` datetime DEFAULT NULL,
  `latest_ship_date` datetime DEFAULT NULL,
  `promise_response_duedate` datetime DEFAULT NULL,
  `number_of_items_shipped` int DEFAULT NULL,
  `number_of_items_unshipped` int DEFAULT NULL,
  `order_channel` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `order_status` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `order_total` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `order_type` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `payment_method` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `replaced_orderid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `sales_channel` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `seller_orderid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ship_service_level` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `shipment_service_level_category` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `amazonAuthid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`amazon_orderid`) USING BTREE,
  KEY `marketplaceid` (`shopid`,`marketplaceid`,`purchase_date`) USING BTREE,
  KEY `group` (`shopid`,`groupid`,`marketplaceid`,`purchase_date`) USING BTREE,
  KEY `shopid` (`shopid`,`purchase_date`) USING BTREE,
  KEY `group2` (`shopid`,`groupid`,`purchase_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_order_buyer_ship_address 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_buyer_ship_address` (
  `id` bigint unsigned NOT NULL,
  `amazon_order_id` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `marketplaceid` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `amazonAuthid` bigint unsigned DEFAULT NULL,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `address1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `address2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `address3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `address_type` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `city` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `county` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '区县',
  `district` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `state` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `postalcode` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `countrycode` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `municipality` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `phone` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`),
  UNIQUE KEY `id` (`id`),
  KEY `amazonAuthId` (`amazonAuthid`,`marketplaceid`,`name`(191)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_order_item 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_item` (
  `id` bigint unsigned NOT NULL,
  `amazon_order_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `orderItemId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `asin` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `title` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `QuantityOrdered` int DEFAULT NULL,
  `QuantityShipped` int DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `promotion_ids` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `CODFee` decimal(10,2) DEFAULT NULL,
  `CODFeeDiscount` decimal(10,2) DEFAULT NULL,
  `GiftMessageText` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `GiftWrapLevel` char(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ConditionId` char(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ConditionSubtypeId` char(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ConditionNote` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ScheduledDeliveryStartDate` datetime DEFAULT NULL,
  `ScheduledDeliveryEndDate` datetime DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceId` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`orderItemId`) USING BTREE,
  KEY `amazonAuthId_shopid_groupid` (`amazonAuthId`,`marketplaceId`,`purchase_date`) USING BTREE,
  KEY `sku` (`sku`,`purchase_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_order_main 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_main` (
  `id` bigint unsigned NOT NULL,
  `amazon_order_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `seller_order_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `replaced_orderid` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_status` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `fulfillment_channel` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sales_channel` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `order_channel` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ship_service_level` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '货件服务水平',
  `buyer_shipping_address_id` bigint unsigned DEFAULT NULL COMMENT '买家收货地址id',
  `currency` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `order_total` decimal(10,2) DEFAULT NULL COMMENT '订单的总费用',
  `numberOfItemsShipped` int DEFAULT NULL COMMENT '已配送的商品数量。',
  `numberOfItemsUnshipped` int DEFAULT NULL COMMENT '未配送的商品数量。',
  `paymentMethod` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'COD 订单的次级付款方式',
  `payment_execution_detail_item` decimal(10,0) DEFAULT NULL COMMENT '使用同级PaymentMethod响应元素指明的次级付款方式支付的金额',
  `buyer_email` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `buyer_name` tinyblob,
  `shipment_serviceLevel_category` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '订单的配送服务级别分类。',
  `fulfillment_supply_sourceid` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `CbaDisplayableShippingLabel` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '卖家自定义的配送方式，属于Checkout by Amazon (CBA) 所支持的四种标准配送设置中的一种',
  `orderType` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `earliestShipDate` datetime DEFAULT NULL,
  `latestShipDate` datetime DEFAULT NULL,
  `earliestDeliveryDate` datetime DEFAULT NULL,
  `latestDeliveryDate` datetime DEFAULT NULL,
  `promise_response_duedate` datetime DEFAULT NULL,
  `isBusinessOrder` bit(1) DEFAULT b'0',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `hasItem` bit(1) DEFAULT b'0',
  `marketplaceId` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `groupid` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  PRIMARY KEY (`amazon_order_id`) USING BTREE,
  KEY `purchase_date` (`shopid`,`purchase_date`) USING BTREE,
  KEY `amazonauth` (`amazonAuthId`,`purchase_date`,`hasItem`) USING BTREE,
  KEY `groupid` (`shopid`,`groupid`,`purchase_date`) USING BTREE,
  KEY `marketplace` (`shopid`,`marketplaceId`,`purchase_date`) USING BTREE,
  KEY `group_marketplace` (`shopid`,`groupid`,`marketplaceId`,`purchase_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_order_remove_report 结构
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
  PRIMARY KEY (`order_id`,`sku`) USING BTREE,
  KEY `purchase_date` (`purchase_date`) USING BTREE,
  KEY `sku` (`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_pdt_price_opt 结构
CREATE TABLE IF NOT EXISTS `t_amz_pdt_price_opt` (
  `pid` bigint unsigned NOT NULL DEFAULT '0',
  `feed_submission_id` bigint unsigned NOT NULL DEFAULT '0',
  `standardprice` decimal(10,4) DEFAULT NULL,
  `saleprice` decimal(10,4) DEFAULT NULL,
  `businessprice` decimal(10,4) DEFAULT NULL,
  `businesstype` char(10) DEFAULT NULL,
  `businesslist` varchar(1000) DEFAULT NULL,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `ftype` char(10) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`pid`,`feed_submission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_po_rpt_day 结构
CREATE TABLE IF NOT EXISTS `t_amz_po_rpt_day` (
  `amazonAuthId` bigint unsigned NOT NULL,
  `purchase_date` date NOT NULL,
  `sales_channel` char(25) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `sku` char(40) NOT NULL,
  `order_status` char(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `asin` char(36) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `quantity` int DEFAULT NULL,
  `ordersum` int DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `price` decimal(15,2) DEFAULT NULL,
  `pricermb` decimal(15,2) DEFAULT NULL,
  `ship_country` char(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`sku`,`sales_channel`,`purchase_date`,`order_status`,`amazonAuthId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_active 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_active` (
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL COMMENT '站点',
  `sku` varchar(50) NOT NULL COMMENT '用户码sku',
  `asin` char(36) DEFAULT NULL COMMENT '唯一码asin',
  `openDate` datetime DEFAULT NULL COMMENT '创建日期',
  `price` decimal(14,2) DEFAULT NULL COMMENT '价格',
  PRIMARY KEY (`amazonAuthId`,`marketplaceid`,`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_active_daynum 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_active_daynum` (
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceid` char(14) NOT NULL,
  `byday` date NOT NULL,
  `num` int DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`marketplaceid`,`byday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_lock 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_lock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `isused` bit(1) DEFAULT NULL COMMENT '是否可用',
  `num` int DEFAULT NULL COMMENT '询问次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_pageviews 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_pageviews` (
  `amazonAuthid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `SKU` char(36) NOT NULL,
  `byday` date NOT NULL,
  `parent_asin` char(36) NOT NULL,
  `child_asin` char(36) DEFAULT NULL,
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
  PRIMARY KEY (`amazonAuthid`,`marketplaceid`,`byday`,`SKU`,`parent_asin`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='流量报表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_pageviews_download 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_pageviews_download` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `SKU` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `byday` date NOT NULL,
  `parent_asin` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `child_asin` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
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
  UNIQUE KEY `key` (`amazonAuthid`,`marketplaceid`,`byday`,`SKU`,`parent_asin`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='流量报表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_pageviews_month 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_pageviews_month` (
  `amazonAuthid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `SKU` char(36) NOT NULL,
  `byday` date NOT NULL,
  `parent_asin` char(36) NOT NULL,
  `child_asin` char(36) DEFAULT NULL,
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
  PRIMARY KEY (`amazonAuthid`,`marketplaceid`,`byday`,`SKU`,`parent_asin`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_pageviews_week 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_pageviews_week` (
  `amazonAuthid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `SKU` char(36) NOT NULL,
  `byday` date NOT NULL,
  `parent_asin` char(36) NOT NULL,
  `child_asin` char(36) DEFAULT NULL,
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
  PRIMARY KEY (`amazonAuthid`,`marketplaceid`,`byday`,`SKU`,`parent_asin`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_refresh 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_refresh` (
  `pid` bigint unsigned NOT NULL,
  `amazonauthid` bigint DEFAULT NULL,
  `detail_refresh_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `price_refresh_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `catalog_refresh_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `sku` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `asin` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE,
  UNIQUE KEY `amazonauthid` (`amazonauthid`,`marketplaceid`,`sku`) USING BTREE,
  KEY `ftime` (`amazonauthid`,`sku`,`detail_refresh_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_refreshtime 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_refreshtime` (
  `pid` bigint unsigned NOT NULL,
  `item` int unsigned NOT NULL COMMENT '0:GetCompetitivePricingForSKURequest;\\r\\n1:GetLowestPricedOffersForSKU;\\r\\n2:captureProductDetail;3:captureProductCategoriesBySku',
  `ftime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`pid`,`item`),
  KEY `ftime` (`ftime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_region 结构
CREATE TABLE IF NOT EXISTS `t_amz_region` (
  `code` char(2) NOT NULL,
  `name` char(10) DEFAULT NULL,
  `advname` char(100) DEFAULT NULL,
  `advpoint` char(100) DEFAULT NULL,
  `client_id` char(100) DEFAULT NULL,
  `client_secret` char(100) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_report_request_type 结构
CREATE TABLE IF NOT EXISTS `t_amz_report_request_type` (
  `id` int NOT NULL COMMENT 'ID',
  `cname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '中文名称',
  `ename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '英文名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '报表编码',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '描述',
  `bean` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '报表处理类',
  `day` int NOT NULL DEFAULT '0' COMMENT '报表默认请求天数',
  `disabled` bit(1) DEFAULT b'0' COMMENT '是否可用',
  `reportOptions` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '报表默认参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='亚马逊报表类型';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_returns_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_returns_report` (
  `sku` char(50) NOT NULL,
  `return_date` datetime NOT NULL,
  `order_id` char(50) NOT NULL,
  `sellerid` char(16) NOT NULL,
  `marketplaceid` char(36) DEFAULT NULL,
  `asin` char(40) DEFAULT NULL,
  `fnsku` char(40) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `fulfillment_center_id` char(20) DEFAULT NULL,
  `detailed_disposition` char(20) DEFAULT NULL,
  `reason` varchar(100) DEFAULT NULL,
  `status` char(50) DEFAULT NULL,
  `license_plate_number` varchar(1000) DEFAULT NULL,
  `customer_comments` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`sellerid`,`return_date`,`sku`,`order_id`) USING BTREE,
  KEY `return_date` (`return_date`),
  KEY `order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_returns_report_summary 结构
CREATE TABLE IF NOT EXISTS `t_amz_returns_report_summary` (
  `sku` char(50) NOT NULL,
  `return_date` date NOT NULL,
  `sellerid` char(16) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`sellerid`,`marketplaceid`,`sku`,`return_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_scout_asins 结构
CREATE TABLE IF NOT EXISTS `t_amz_scout_asins` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sour_date` varchar(50) DEFAULT NULL COMMENT '抓取的日期',
  `marketplace` varchar(50) NOT NULL COMMENT '格式: Amazon.com / Amazon.co.uk 等',
  `currency` varchar(6) NOT NULL COMMENT '格式: USD / GBP ..',
  `type` varchar(50) NOT NULL COMMENT '榜单类型',
  `category_top` varchar(50) NOT NULL COMMENT '一级榜单品类',
  `category` varchar(100) NOT NULL COMMENT '当前的榜单品类(抓取时)',
  `category_lev` tinyint NOT NULL COMMENT '当前的榜单品类的级别',
  `billboard_rank` tinyint NOT NULL COMMENT '当前的榜单排名',
  `ASIN` char(10) NOT NULL,
  `product_url` varchar(500) DEFAULT NULL,
  `image_url` varchar(300) DEFAULT NULL,
  `product_title` varchar(300) DEFAULT NULL,
  `reivew_score` float DEFAULT NULL COMMENT '评分',
  `review_count` smallint DEFAULT '0' COMMENT '评价数量',
  `current_price` double DEFAULT NULL COMMENT '价格',
  `prime_icon` char(5) DEFAULT NULL COMMENT '是否是PRIME,空为否',
  `rank_date` date DEFAULT NULL COMMENT '计算的日期.以下字段需要计算补充',
  `product_rank` int DEFAULT NULL,
  `Length` double DEFAULT NULL,
  `Width` double DEFAULT NULL,
  `Height` double DEFAULT NULL,
  `LWH_unit` varchar(50) DEFAULT NULL COMMENT '长宽高的单位',
  `Weight` double DEFAULT NULL,
  `Weight_unit` varchar(50) DEFAULT NULL COMMENT '重量单位',
  `fba_fee` double DEFAULT NULL,
  `est_profit` double DEFAULT NULL COMMENT '利润. 使用的利润方案统一为: TUPARKA-pan_EU . 采购成本计为0. ',
  `costdetail` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试选品思路,手动加入ASIN数据,自动更新ASIN尺寸及计算利润';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_acc_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_acc_report` (
  `settlement_id` bigint unsigned NOT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `settlement_start_date` datetime DEFAULT NULL,
  `settlement_end_date` datetime DEFAULT NULL,
  `deposit_date` datetime DEFAULT NULL,
  `total_amount` decimal(15,2) DEFAULT NULL,
  `currency` char(7) DEFAULT NULL,
  PRIMARY KEY (`settlement_id`),
  KEY `index1` (`amazonauthid`,`marketplace_name`) USING BTREE,
  KEY `deposit_date` (`deposit_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_acc_statement 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_acc_statement` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(20) DEFAULT NULL,
  `currency` char(5) DEFAULT NULL,
  `startdate` date DEFAULT NULL,
  `enddate` date DEFAULT NULL,
  `datetype` char(5) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `summaryall` mediumblob,
  `listdata` longblob,
  `fielddata` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_amount_description 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_amount_description` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `cname` varchar(200) NOT NULL DEFAULT '',
  `ename` varchar(200) NOT NULL DEFAULT '',
  `cdescription` varchar(1000) NOT NULL DEFAULT '',
  `edescription` varchar(1000) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ename` (`ename`)
) ENGINE=InnoDB AUTO_INCREMENT=510 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_amount_type_nonsku 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_amount_type_nonsku` (
  `transaction_type` char(40) NOT NULL,
  `amount_type` char(40) NOT NULL,
  `amount_description` char(100) NOT NULL,
  PRIMARY KEY (`transaction_type`,`amount_type`,`amount_description`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_report` (
  `id` bigint unsigned NOT NULL,
  `settlement_id` bigint unsigned NOT NULL,
  `currency` char(7) DEFAULT NULL,
  `transaction_type` char(40) DEFAULT NULL,
  `order_id` char(40) DEFAULT NULL,
  `merchant_order_id` char(40) DEFAULT NULL,
  `adjustment_id` char(40) DEFAULT NULL,
  `shipment_id` char(15) DEFAULT NULL,
  `marketplace_name` char(40) DEFAULT NULL,
  `amount_type` char(40) DEFAULT NULL,
  `amount_description` char(100) DEFAULT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  `fulfillment_id` char(5) DEFAULT NULL,
  `posted_date` date NOT NULL,
  `posted_date_time` datetime DEFAULT NULL,
  `order_item_code` char(16) DEFAULT NULL,
  `merchant_order_item_id` char(150) DEFAULT NULL,
  `merchant_adjustment_item_id` char(15) DEFAULT NULL,
  `sku` char(40) DEFAULT NULL,
  `quantity_purchased` int DEFAULT NULL,
  `promotion_id` char(100) DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  PRIMARY KEY (`posted_date`,`amazonAuthId`,`id`),
  KEY `index1` (`settlement_id`,`amazonAuthId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='账期报表'
/*!50100 PARTITION BY RANGE (year(`posted_date`))
SUBPARTITION BY HASH (quarter(`posted_date`))
SUBPARTITIONS 4
(PARTITION p0 VALUES LESS THAN (2015) ENGINE = InnoDB,
 PARTITION p2015 VALUES LESS THAN (2016) ENGINE = InnoDB,
 PARTITION p2016 VALUES LESS THAN (2017) ENGINE = InnoDB,
 PARTITION p2017 VALUES LESS THAN (2018) ENGINE = InnoDB,
 PARTITION p2018 VALUES LESS THAN (2019) ENGINE = InnoDB,
 PARTITION p2019 VALUES LESS THAN (2020) ENGINE = InnoDB,
 PARTITION p1 VALUES LESS THAN MAXVALUE ENGINE = InnoDB) */;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_day 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_day` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `sku` char(40) NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `transaction_type` char(40) NOT NULL COMMENT '商品费用',
  `amount_type` char(40) NOT NULL,
  `amount_description` char(100) NOT NULL,
  `fulfillment_type` char(5) NOT NULL,
  `currency` char(7) NOT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `marketplace_name` (`marketplace_name`),
  KEY `sku` (`sku`),
  KEY `settlementid` (`settlementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_month 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_month` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `transaction_type` char(40) NOT NULL,
  `amount_type` char(40) NOT NULL,
  `amount_description` char(100) NOT NULL,
  `fulfillment_type` char(5) NOT NULL,
  `currency` char(7) NOT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `settlementid` (`settlementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_returns 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_returns` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL DEFAULT '0',
  `sku` char(50) NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `quantity` int DEFAULT NULL,
  `mfnqty` int DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `sku` (`sku`),
  KEY `marketplace_name` (`marketplace_name`),
  KEY `settlementid` (`settlementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_sku 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_sku` (
  `id` bigint unsigned NOT NULL,
  `settlementid` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `sku` char(40) NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `order_amount` int DEFAULT NULL COMMENT '订单量',
  `sales` int DEFAULT NULL COMMENT '销量',
  `principal` decimal(20,8) DEFAULT NULL COMMENT '销售额',
  `commission` decimal(20,8) DEFAULT NULL COMMENT '销售佣金',
  `fbafee` decimal(20,8) DEFAULT NULL COMMENT 'FBA费用',
  `refund` decimal(20,8) DEFAULT NULL COMMENT '退款金额',
  `otherfee` decimal(20,8) DEFAULT NULL COMMENT '其它',
  `tax` decimal(20,8) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`) USING BTREE,
  KEY `marketplace_name` (`marketplace_name`),
  KEY `sku` (`sku`),
  KEY `settlementid` (`settlementid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_ship_fulfillment_center 结构
CREATE TABLE IF NOT EXISTS `t_amz_ship_fulfillment_center` (
  `code` char(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `country` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `address_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `city` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `state` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `zip` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_ship_state_province_code 结构
CREATE TABLE IF NOT EXISTS `t_amz_ship_state_province_code` (
  `code` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `ename` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `capital` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `ecapital` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_submitfeed 结构
CREATE TABLE IF NOT EXISTS `t_amz_submitfeed` (
  `feed_submissionid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `sellerid` char(15) NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `feed_type` char(50) DEFAULT NULL,
  `submitted_date` datetime DEFAULT NULL,
  `started_processing_date` datetime DEFAULT NULL,
  `completed_processiong_date` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `feed_processing_status` char(15) DEFAULT NULL,
  `queueid` bigint unsigned DEFAULT NULL,
  `amzprocesslog` blob,
  PRIMARY KEY (`feed_submissionid`,`marketplaceid`,`sellerid`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='\r\n';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_submitfeed_queue 结构
CREATE TABLE IF NOT EXISTS `t_amz_submitfeed_queue` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `feed_type` char(50) DEFAULT NULL,
  `process_date` timestamp NULL DEFAULT NULL,
  `filename` varchar(100) DEFAULT NULL,
  `content` blob,
  `process_log` varchar(1000) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `submitfeedid` bigint unsigned DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT NULL,
  `feedoptions` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `filename` (`filename`),
  KEY `shopid` (`shopid`),
  KEY `feedtype` (`feed_type`),
  KEY `shopid_sellerid_marketplaceid_feed_type_process_date` (`amazonAuthId`,`marketplaceid`,`createtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_authority 结构
CREATE TABLE IF NOT EXISTS `t_authority` (
  `id` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `menuid` char(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '定义Action',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限名称',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='Action权限控制表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_data_runs_remove_time 结构
CREATE TABLE IF NOT EXISTS `t_data_runs_remove_time` (
  `id` int NOT NULL,
  `ftype` char(10) DEFAULT NULL,
  `pages` int DEFAULT NULL,
  `runtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ftype` (`ftype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_daysales_formula 结构
CREATE TABLE IF NOT EXISTS `t_daysales_formula` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `formula` varchar(500) DEFAULT NULL,
  `formula_name` varchar(500) DEFAULT NULL,
  `month_sales_rate` decimal(10,2) DEFAULT NULL,
  `15sales_rate` decimal(10,2) DEFAULT NULL,
  `7sales_rate` decimal(10,2) DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_dimensions 结构
CREATE TABLE IF NOT EXISTS `t_dimensions` (
  `id` bigint unsigned NOT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `length_units` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `width_units` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `height_units` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `weight_units` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_amazon_feedstatus 结构
CREATE TABLE IF NOT EXISTS `t_erp_amazon_feedstatus` (
  `status` char(50) NOT NULL,
  `name` char(50) DEFAULT NULL,
  `remark` char(100) DEFAULT NULL,
  PRIMARY KEY (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_assembly 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly` (
  `id` bigint unsigned NOT NULL COMMENT 'ID',
  `mainmid` bigint unsigned DEFAULT NULL COMMENT '主产品',
  `submid` bigint unsigned DEFAULT NULL COMMENT '子产品',
  `subnumber` int DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `mainmid` (`mainmid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_assembly_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `planitem` char(36) DEFAULT NULL,
  `ftype` char(10) DEFAULT NULL COMMENT '组装=ass, 拆分=dis',
  `warehouseid` bigint unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `mainmid` bigint unsigned DEFAULT NULL COMMENT '主sku id',
  `amount` int DEFAULT NULL,
  `amount_handle` int unsigned DEFAULT NULL,
  `auditstatus` int DEFAULT NULL COMMENT '0：未提交，1：待组装，2 组装中，3 已完成，4 已终止',
  `remark` varchar(500) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number` (`shopid`,`number`),
  KEY `mainmid` (`mainmid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_assembly_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT '0',
  `whamount` int DEFAULT '0' COMMENT '仓库调出量',
  `phamount` int DEFAULT '0' COMMENT '采购量',
  `phedamount` int DEFAULT '0' COMMENT '已经采购数量',
  `purchase_from_entry_id` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`formid`,`materialid`),
  UNIQUE KEY `indexunique` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_assembly_from_instock 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_from_instock` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `shipmentid` char(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_changewh_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_changewh_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `auditstatus` int DEFAULT NULL COMMENT '0：未提交，1：提交未审核，2：已审核',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`),
  KEY `warehouseid` (`warehouseid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_changewh_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_changewh_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `material_from` bigint unsigned DEFAULT NULL,
  `material_to` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_customer 结构
CREATE TABLE IF NOT EXISTS `t_erp_customer` (
  `id` bigint unsigned NOT NULL COMMENT 'ID(h)',
  `name` char(50) DEFAULT NULL COMMENT '客户简称',
  `number` char(50) DEFAULT NULL COMMENT '客户编码',
  `fullname` varchar(200) DEFAULT NULL COMMENT '客户全称',
  `ftype` char(10) DEFAULT NULL COMMENT '客户分类',
  `contacts` varchar(50) DEFAULT NULL COMMENT '联系人',
  `phone_num` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `contact_info` varchar(2000) DEFAULT NULL COMMENT '其它联系信息',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `shoplink` varchar(500) DEFAULT NULL COMMENT '商品链接',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '所属店铺（公司）(h)',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_shopid` (`shopid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_dispatch_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) DEFAULT NULL COMMENT '调拨单号',
  `shopid` bigint unsigned DEFAULT NULL,
  `from_warehouseid` bigint unsigned DEFAULT NULL,
  `to_warehouseid` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `auditstatus` int DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_dispatch_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_estimated_sales 结构
CREATE TABLE IF NOT EXISTS `t_erp_estimated_sales` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `sku` char(50) DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=10588 DEFAULT CHARSET=utf8 COMMENT='用户维护日均销量表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_fin_account 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_account` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `balance` decimal(18,4) DEFAULT NULL COMMENT '账户余额',
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='账户表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_fin_journalaccount 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_journalaccount` (
  `id` bigint unsigned NOT NULL,
  `acct` bigint unsigned NOT NULL,
  `ftype` char(36) NOT NULL COMMENT '记账类型:out,支出；in,收入',
  `projectid` bigint unsigned DEFAULT NULL,
  `amount` decimal(18,4) DEFAULT NULL,
  `balance` decimal(18,4) DEFAULT NULL,
  `remark` varchar(2000) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creator` bigint unsigned DEFAULT NULL COMMENT '创建人',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `shopid` bigint unsigned NOT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_fin_journaldaily 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_journaldaily` (
  `id` bigint unsigned NOT NULL,
  `acct` bigint unsigned DEFAULT NULL COMMENT '账户id',
  `byday` date DEFAULT NULL,
  `rec` decimal(18,4) DEFAULT NULL COMMENT '收入',
  `pay` decimal(18,4) DEFAULT NULL COMMENT '支出',
  `balance` decimal(18,4) DEFAULT NULL COMMENT '余额',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `byday` (`byday`,`acct`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='流水_日账单';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_fin_project 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_project` (
  `id` bigint unsigned NOT NULL,
  `name` char(50) DEFAULT NULL,
  `issys` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是系统项目名称',
  `shopid` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_shopid` (`name`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='流水账_类型';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_fin_type_journalmonthly 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_type_journalmonthly` (
  `id` bigint unsigned NOT NULL,
  `projectid` bigint unsigned DEFAULT NULL COMMENT '项目id',
  `acct` bigint unsigned DEFAULT NULL COMMENT '账户id',
  `year` int DEFAULT NULL,
  `month` int DEFAULT NULL,
  `rec` decimal(18,4) DEFAULT NULL,
  `pay` decimal(18,4) DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`projectid`,`acct`,`year`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='流水_月账单 类型统计';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_formtype 结构
CREATE TABLE IF NOT EXISTS `t_erp_formtype` (
  `id` char(20) NOT NULL,
  `name` char(50) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory` (
  `id` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned NOT NULL DEFAULT '0',
  `shopid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `quantity` int DEFAULT '0',
  `status` char(36) NOT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_w_s_m_s` (`shopid`,`warehouseid`,`materialid`,`status`),
  KEY `FK_t_erp_inventory_t_erp_material` (`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_his` (
  `id` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned NOT NULL DEFAULT '0',
  `shopid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `quantity` int DEFAULT '0',
  `status` char(36) NOT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `modifyday` date NOT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_erp_inventory_t_erp_material` (`warehouseid`,`materialid`) USING BTREE,
  KEY `mykey` (`shopid`,`materialid`,`warehouseid`) USING BTREE,
  KEY `modifyday` (`modifyday`),
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_month_summary 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_record` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `formoptid` bigint unsigned DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `status` char(36) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `invqty` int DEFAULT NULL,
  `formtype` char(20) DEFAULT NULL,
  `operate` char(10) DEFAULT NULL COMMENT 'in,out,readyin,readyout,cancel,stop',
  `number` char(36) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`opttime`,`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`shopid`,`warehouseid`,`operate`,`materialid`,`status`,`quantity`,`number`,`opttime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_record_bkp20211216 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_record_bkp20211216` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `formoptid` bigint unsigned DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `status` char(36) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `invqty` int DEFAULT NULL,
  `formtype` char(20) DEFAULT NULL,
  `operate` char(10) DEFAULT NULL COMMENT 'in,out,readyin,readyout,cancel,stop',
  `number` char(36) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`opttime`,`id`),
  KEY `Index 2` (`shopid`,`warehouseid`,`operate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_status 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_status` (
  `id` char(36) DEFAULT NULL,
  `code` char(36) DEFAULT NULL,
  `name` char(36) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inwh_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_inwh_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditstatus` int DEFAULT NULL COMMENT '0：未提交，1：提交未审核，2：已审核',
  `remark` varchar(1000) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inwh_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_inwh_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_market_priority 结构
CREATE TABLE IF NOT EXISTS `t_erp_market_priority` (
  `marketplaceid` varchar(36) NOT NULL,
  `priority` int DEFAULT NULL COMMENT 'FBA站点优先级',
  `groupid` bigint unsigned NOT NULL,
  PRIMARY KEY (`marketplaceid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='主要用于发货计划中的同一个店铺下面各个国家的优先级。';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_material` (
  `id` bigint unsigned NOT NULL COMMENT 'ID(h)',
  `sku` varchar(50) DEFAULT NULL COMMENT 'SKU',
  `name` varchar(500) DEFAULT NULL COMMENT '名称',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID(h)',
  `upc` char(30) DEFAULT NULL COMMENT '条码',
  `brand` char(50) DEFAULT NULL COMMENT '品牌',
  `image` bigint unsigned DEFAULT NULL COMMENT '图片',
  `itemDimensions` bigint unsigned DEFAULT NULL COMMENT '产品尺寸',
  `pkgDimensions` bigint unsigned DEFAULT NULL COMMENT '带包装尺寸',
  `boxDimensions` bigint unsigned DEFAULT NULL,
  `boxnum` int unsigned DEFAULT NULL,
  `specification` char(36) DEFAULT NULL COMMENT '规格',
  `supplier` bigint unsigned DEFAULT NULL COMMENT '供应商',
  `badrate` float DEFAULT NULL COMMENT '不良率',
  `vatrate` float DEFAULT NULL COMMENT '退税率',
  `productCode` char(36) DEFAULT NULL COMMENT '供应商产品代码',
  `delivery_cycle` int DEFAULT NULL COMMENT '供货周期',
  `other_cost` decimal(10,2) DEFAULT NULL,
  `MOQ` int unsigned DEFAULT '0' COMMENT '起订量：minimum order quantity',
  `purchaseUrl` varchar(1000) DEFAULT NULL COMMENT '采购链接',
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注',
  `categoryid` char(36) DEFAULT NULL COMMENT '类型id',
  `issfg` char(10) DEFAULT '0' COMMENT '0:单独成品；1：组装成品；2：半成品',
  `color` char(10) DEFAULT '0',
  `owner` bigint unsigned DEFAULT '0',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `price` decimal(10,2) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `parentid` char(36) DEFAULT NULL COMMENT '用于导入数据是引用的系统内的那个SKU产品',
  `effectivedate` datetime DEFAULT NULL,
  `isSmlAndLight` bit(1) DEFAULT b'0' COMMENT '是否轻小产品',
  `assembly_time` int DEFAULT NULL COMMENT '组装时间',
  `isDelete` bit(1) DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 4` (`shopid`,`owner`,`color`,`sku`),
  KEY `supplier` (`supplier`),
  KEY `FK_t_erp_material_t_erp_material_sku` (`sku`,`shopid`,`isDelete`) USING BTREE,
  KEY `categoryid` (`categoryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_category 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_category` (
  `id` char(36) NOT NULL,
  `name` char(100) DEFAULT NULL,
  `number` char(50) DEFAULT NULL,
  `color` char(10) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`name`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_consumable 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_consumable` (
  `id` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `submaterialid` bigint unsigned NOT NULL,
  `amount` decimal(10,4) unsigned DEFAULT '0.0000',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `materialid_submaterialid` (`materialid`,`submaterialid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='耗材表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_customs 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_customs` (
  `matreialid` bigint unsigned NOT NULL,
  `name_en` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品英文名',
  `name_cn` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品中文名',
  `material` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品材质',
  `model` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品型号',
  `customs_code` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '海关编码',
  `material_use` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用途',
  `brand` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品品牌',
  `iselectricity` bit(1) DEFAULT b'0' COMMENT '是否带电/磁',
  `isdanger` bit(1) DEFAULT b'0' COMMENT '是否危险品',
  `unitprice` decimal(10,2) DEFAULT NULL COMMENT '申报单价',
  `addfee` decimal(10,2) DEFAULT NULL COMMENT '附加费用',
  PRIMARY KEY (`matreialid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='海关表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_customs_file 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_customs_file` (
  `id` bigint unsigned NOT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `filename` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `filepath` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_group 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_group` (
  `materialid` bigint unsigned NOT NULL,
  `groupid` char(36) NOT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`materialid`,`groupid`),
  KEY `FK_t_erp_material_category_t_erp_category` (`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_his` (
  `id` bigint unsigned NOT NULL COMMENT 'ID(h)',
  `sku` varchar(50) DEFAULT NULL COMMENT 'SKU',
  `name` varchar(500) DEFAULT NULL COMMENT '名称',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID(h)',
  `upc` char(30) DEFAULT NULL COMMENT '条码',
  `brand` char(50) DEFAULT NULL COMMENT '品牌',
  `image` bigint unsigned DEFAULT NULL COMMENT '图片',
  `itemDimensions` char(36) DEFAULT NULL COMMENT '产品尺寸',
  `pkgDimensions` char(36) DEFAULT NULL COMMENT '带包装尺寸',
  `boxDimensions` bigint unsigned DEFAULT NULL,
  `boxnum` int unsigned DEFAULT NULL,
  `specification` char(36) DEFAULT NULL COMMENT '规格',
  `supplier` bigint unsigned DEFAULT NULL COMMENT '供应商',
  `productCode` char(36) DEFAULT NULL COMMENT '供应商产品代码',
  `delivery_cycle` int DEFAULT NULL,
  `other_cost` decimal(10,2) DEFAULT NULL,
  `MOQ` int DEFAULT '0' COMMENT '起订量：minimum order quantity',
  `purchaseUrl` varchar(1000) DEFAULT NULL COMMENT '采购链接',
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注',
  `categoryid` char(36) DEFAULT NULL COMMENT '类型id',
  `issfg` char(10) DEFAULT '0' COMMENT '0:单独成品；1：组装成品；2：半成品',
  `color` char(10) DEFAULT '0',
  `owner` bigint unsigned DEFAULT '0',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `price` decimal(10,4) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `parentid` char(36) DEFAULT NULL COMMENT '用于导入数据是引用的系统内的那个SKU产品',
  `effectivedate` datetime DEFAULT NULL,
  `isSmlAndLight` bit(1) DEFAULT b'0' COMMENT '是否轻小产品',
  `assembly_time` int DEFAULT NULL,
  PRIMARY KEY (`id`,`opttime`),
  UNIQUE KEY `Index 3` (`shopid`,`sku`,`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='t_erp_material_his历史表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_mark 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_mark` (
  `materialid` bigint unsigned NOT NULL,
  `ftype` char(10) NOT NULL COMMENT 'notice：产品出现问题时发布的公告',
  `mark` varchar(100) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`materialid`,`ftype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_supplier 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_supplier` (
  `id` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `supplierid` bigint unsigned NOT NULL,
  `purchaseUrl` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `productCode` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `otherCost` decimal(10,2) DEFAULT NULL,
  `deliverycycle` int DEFAULT NULL,
  `isdefault` bit(1) NOT NULL DEFAULT b'0',
  `badrate` float DEFAULT '0',
  `MOQ` int NOT NULL DEFAULT '0',
  `creater` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `materialid_supplierid` (`materialid`,`supplierid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_supplier_stepwise 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_supplier_stepwise` (
  `id` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `supplierid` bigint unsigned NOT NULL,
  `price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00',
  `amount` int unsigned NOT NULL DEFAULT '0',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `materialid` (`materialid`,`supplierid`,`amount`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_m_group 结构
CREATE TABLE IF NOT EXISTS `t_erp_m_group` (
  `id` char(36) NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `ftype` int NOT NULL,
  `color` char(10) DEFAULT NULL,
  `issys` bit(1) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_outwh_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_outwh_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `purchaser` bigint unsigned DEFAULT NULL COMMENT '发货客户',
  `toaddress` varchar(500) DEFAULT NULL COMMENT '发货地址',
  `express` varchar(500) DEFAULT NULL COMMENT '物流快递',
  `expressno` char(50) DEFAULT NULL COMMENT '快递编号',
  `warehouseid` bigint unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditstatus` int DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_outwh_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_outwh_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_alibaba_auth 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_auth` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `shopid` bigint unsigned DEFAULT NULL,
  `name` char(36) DEFAULT NULL,
  `access_token` varchar(500) DEFAULT NULL,
  `refresh_token` varchar(500) DEFAULT NULL,
  `resource_owner` varchar(50) DEFAULT NULL,
  `aliId` bigint unsigned DEFAULT NULL,
  `memberId` char(50) DEFAULT NULL,
  `refresh_token_timeout` datetime DEFAULT NULL,
  `access_token_timeout` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `isDelete` bit(1) DEFAULT b'0',
  `appkey` varchar(255) DEFAULT NULL,
  `appsecret` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid_name` (`shopid`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1531205820504211458 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`) USING BTREE,
  KEY `warehouseid_shopid` (`warehouseid`,`shopid`),
  KEY `createdate` (`createdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_entry 结构
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
  `paystatus` int DEFAULT NULL,
  `planitemid` char(36) DEFAULT NULL,
  `inwhstatus` int DEFAULT NULL,
  `totalpay` decimal(10,2) DEFAULT '0.00',
  `totalre` int DEFAULT '0',
  `totalin` int DEFAULT '0',
  `totalch` int DEFAULT '0',
  `deliverydate` datetime DEFAULT NULL,
  `closerecdate` datetime DEFAULT NULL COMMENT '入库结束时间',
  `closepaydate` datetime DEFAULT NULL COMMENT '付款结束时间',
  `remark` varchar(500) DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid_materialid_planitemid` (`formid`,`materialid`,`planitemid`),
  KEY `materialid` (`materialid`),
  KEY `auditstatus` (`auditstatus`),
  KEY `inwhstatus` (`inwhstatus`),
  KEY `paystatus` (`paystatus`),
  KEY `supplier` (`supplier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_entry_alibabainfo 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_alibabainfo` (
  `entryid` bigint unsigned NOT NULL,
  `alibaba_auth` bigint unsigned DEFAULT NULL,
  `alibaba_orderid` bigint unsigned DEFAULT NULL,
  `logistics_info` text,
  `logistics_trace_info` text,
  `order_info` longtext,
  `logistics_status` bit(1) DEFAULT b'0',
  `logistics_trace_status` bit(1) DEFAULT b'0',
  `order_status` char(30) DEFAULT NULL,
  `order_refresh_time` datetime DEFAULT NULL,
  `logistics_refresh_time` datetime DEFAULT NULL,
  `logistics_trace_refresh_time` datetime DEFAULT NULL,
  PRIMARY KEY (`entryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_entry_logistics 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_logistics` (
  `entryid` bigint unsigned NOT NULL,
  `logisticsId` char(25) NOT NULL,
  PRIMARY KEY (`entryid`,`logisticsId`),
  KEY `logisticsId` (`logisticsId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_payment 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_payment` (
  `id` bigint unsigned NOT NULL,
  `formentryid` bigint unsigned DEFAULT NULL,
  `auditstatus` int DEFAULT NULL,
  `payment_method` int DEFAULT NULL,
  `acct` bigint unsigned DEFAULT NULL,
  `payprice` decimal(18,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(2000) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `projectid` bigint unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formentryid` (`formentryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_payment_method 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_payment_method` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_receive 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_receive` (
  `id` bigint unsigned NOT NULL,
  `formentryid` bigint unsigned DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `ftype` char(10) DEFAULT NULL COMMENT 'in -入库，re -退货， ch-换货,clear-撤销入库',
  `amount` int DEFAULT NULL,
  `remark` varchar(2000) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formentryid` (`formentryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_plan 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_plan` (
  `id` char(36) NOT NULL,
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
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_planitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planitem` (
  `id` char(36) NOT NULL,
  `subplanid` char(36) NOT NULL,
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
  KEY `materialid` (`materialid`),
  KEY `idx_materialid_status` (`materialid`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_planitemsub 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planitemsub` (
  `id` char(36) DEFAULT NULL,
  `planitemid` char(36) NOT NULL,
  `groupid` char(36) NOT NULL,
  `warehouseid` char(36) NOT NULL,
  `planamount` int DEFAULT NULL COMMENT '实际发货量',
  PRIMARY KEY (`planitemid`,`warehouseid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='废表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_planmodel 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planmodel` (
  `planid` char(36) NOT NULL,
  `modelid` char(36) NOT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  `operator` bigint unsigned DEFAULT '0',
  PRIMARY KEY (`planid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_planmodelitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planmodelitem` (
  `id` char(36) NOT NULL,
  `modelid` char(36) NOT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `planamount` int DEFAULT NULL COMMENT '建议补货量',
  `itemprice` decimal(10,4) DEFAULT NULL,
  `invamount` int DEFAULT NULL,
  `orderprice` decimal(10,4) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `modelid_materialid` (`modelid`,`materialid`),
  KEY `idx_materialid_itemprice_planamount_invamount` (`materialid`,`itemprice`,`planamount`,`invamount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_planmodelitemsub 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planmodelitemsub` (
  `itemid` char(36) NOT NULL,
  `sku` char(50) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `groupid` bigint unsigned NOT NULL COMMENT '断货时间',
  `needship` int NOT NULL,
  `salesday` int DEFAULT NULL,
  `aftersalesday` int DEFAULT NULL,
  PRIMARY KEY (`itemid`,`sku`,`marketplaceid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_plansub 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_plansub` (
  `id` char(36) NOT NULL,
  `planid` char(36) DEFAULT NULL,
  `status` int DEFAULT NULL COMMENT '1代表在用，0代表放弃，2代表已提交',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `ftype` char(10) DEFAULT NULL COMMENT 'po代表订单，ao代表组装单',
  PRIMARY KEY (`id`),
  KEY `planid` (`planid`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_plan_warahouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_plan_warahouse` (
  `warehouseid` bigint unsigned NOT NULL,
  `planid` char(36) NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  PRIMARY KEY (`warehouseid`),
  KEY `planid` (`planid`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='入库仓库和补货规划的映射关系表，一个入库仓库不能在多个补货规划中出现，一个补货规划会有多个入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_warahouse_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_warahouse_material` (
  `planid` char(36) NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`planid`,`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录每个sku在补货规划中所默认的入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_warahouse_status 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_warahouse_status` (
  `warehouseid` bigint unsigned NOT NULL,
  `purchase_status` int DEFAULT '0' COMMENT '0表示改仓库无采购任务；1表示采购任务待处理；2表示采购任务已完成',
  `assbly_status` int DEFAULT '0' COMMENT '0表示改仓库无组装任务；1表示组装任务待处理；2表示组装任务已完成',
  `userid` bigint unsigned DEFAULT NULL,
  `opptime` datetime DEFAULT NULL,
  PRIMARY KEY (`warehouseid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录每个仓库补货规划的状态，操作人，日期';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_serial_num 结构
CREATE TABLE IF NOT EXISTS `t_erp_serial_num` (
  `id` char(36) NOT NULL,
  `ftype` char(36) DEFAULT NULL,
  `seqno` int DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `prefix_date` date DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`prefix_date`,`ftype`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_shipcycle 结构
CREATE TABLE IF NOT EXISTS `t_erp_shipcycle` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `sku` char(36) DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `stockingCycle` int DEFAULT NULL COMMENT '安全库存周期',
  `min_cycle` int DEFAULT NULL COMMENT '最小发货周期',
  `first_leg_charges` decimal(12,2) DEFAULT NULL COMMENT '头程运输成本',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku_marketplaceid_groupid` (`sku`,`marketplaceid`,`groupid`)
) ENGINE=InnoDB AUTO_INCREMENT=17392024503393374543 DEFAULT CHARSET=utf8 COMMENT='FBA仓库配置';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_address 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_address` (
  `id` bigint unsigned NOT NULL,
  `name` char(50) DEFAULT NULL COMMENT '名称或公司名称。',
  `groupid` bigint unsigned DEFAULT NULL COMMENT '店铺id',
  `isfrom` bit(1) DEFAULT NULL COMMENT '1 代表发货地址，0代表收货地址',
  `addressLine1` varchar(300) DEFAULT NULL COMMENT '街道地址信息。',
  `addressLine2` varchar(300) DEFAULT NULL COMMENT '其他街道地址信息（如果需要）。',
  `city` char(30) DEFAULT NULL COMMENT '城市',
  `districtOrCounty` char(25) DEFAULT NULL COMMENT '区或县 ',
  `stateOrProvinceCode` char(20) DEFAULT NULL COMMENT '省份代码',
  `countryCode` char(2) DEFAULT NULL COMMENT '国家/地区代码',
  `postalCode` char(30) DEFAULT NULL COMMENT '邮政编码',
  `shopid` bigint unsigned DEFAULT NULL,
  `phone` char(30) DEFAULT NULL,
  `isdefault` bit(1) DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_groupid_city` (`name`,`groupid`,`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_addressto 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_addressto` (
  `id` bigint unsigned NOT NULL,
  `name` char(50) DEFAULT NULL COMMENT '名称或公司名称。',
  `isfrom` bit(1) DEFAULT NULL COMMENT '1 代表发货地址，0代表收货地址',
  `addressLine1` varchar(300) DEFAULT NULL COMMENT '街道地址信息。',
  `addressLine2` varchar(300) DEFAULT NULL COMMENT '其他街道地址信息（如果需要）。',
  `city` char(30) DEFAULT NULL COMMENT '城市',
  `districtOrCounty` char(25) DEFAULT NULL COMMENT '区或县 ',
  `stateOrProvinceCode` char(40) DEFAULT NULL COMMENT '省份代码',
  `countryCode` char(2) DEFAULT NULL COMMENT '国家/地区代码',
  `postalCode` char(30) DEFAULT NULL COMMENT '邮政编码',
  `shopid` bigint unsigned DEFAULT NULL,
  `phone` char(30) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_config_carrier 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_config_carrier` (
  `country` char(10) NOT NULL,
  `name` char(30) NOT NULL,
  `transtyle` char(5) NOT NULL,
  PRIMARY KEY (`country`,`name`,`transtyle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundbox 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundbox` (
  `id` bigint unsigned NOT NULL,
  `shipmentid` char(36) DEFAULT NULL,
  `boxnum` int DEFAULT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `unit` char(10) DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `wunit` char(10) DEFAULT NULL,
  `tracking_id` char(32) DEFAULT NULL,
  `package_status` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundcase 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundcase` (
  `id` bigint unsigned NOT NULL,
  `shipmentid` char(36) NOT NULL,
  `merchantsku` char(50) NOT NULL,
  `unitspercase` int DEFAULT NULL,
  `numberofcase` int NOT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shipmentid` (`shipmentid`,`merchantsku`,`numberofcase`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inbounditem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inbounditem` (
  `id` bigint unsigned NOT NULL,
  `ShipmentId` char(36) DEFAULT NULL,
  `inboundplanid` bigint unsigned DEFAULT NULL COMMENT 'inboundplan的id',
  `FulfillmentNetworkSKU` char(36) DEFAULT NULL,
  `SellerSKU` char(50) DEFAULT NULL COMMENT '商品的卖家 SKU。',
  `QuantityShipped` int DEFAULT NULL,
  `QuantityReceived` int DEFAULT NULL,
  `QuantityInCase` int DEFAULT NULL,
  `Quantity` int DEFAULT NULL COMMENT '	要配送的商品数量。',
  `PrepInstruction` varchar(50) DEFAULT NULL,
  `PrepOwner` varchar(50) DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `shelfsub` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 3` (`SellerSKU`,`ShipmentId`),
  KEY `FK_t_erp_ship_inboundplanitem_t_erp_ship_inboundplan` (`inboundplanid`),
  KEY `Index 4` (`ShipmentId`),
  KEY `idx_ShipmentId_QuantityReceived_QuantityShipped` (`ShipmentId`,`QuantityReceived`,`QuantityShipped`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundplan 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundplan` (
  `id` bigint unsigned NOT NULL,
  `name` char(200) DEFAULT NULL,
  `number` char(20) DEFAULT NULL,
  `shipFromAddressID` bigint unsigned DEFAULT NULL COMMENT '	您的退货地址。(发货地址)',
  `skunum` int DEFAULT NULL,
  `labelPrepType` char(50) DEFAULT NULL COMMENT '	入库货件所需的标签准备类型。',
  `AreCasesRequired` bit(1) DEFAULT NULL COMMENT '指明入库货件是否包含原厂包装发货商品。注： 货件所含的商品必须全部是原厂包装发货或者混装发货。',
  `amazongroupid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(36) DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `auditstatus` tinyint DEFAULT NULL COMMENT '1 已提交（待审核）；  3,已确认货件；   2已退回货件',
  `plansubid` char(36) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 3` (`createdate`),
  KEY `marketplaceid_warehouseid_shopid` (`shopid`,`marketplaceid`),
  KEY `warehouseid` (`warehouseid`),
  KEY `idx_amazongroupid_marketplaceid_shopid` (`amazongroupid`,`marketplaceid`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundruntime 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundruntime` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(36) DEFAULT NULL,
  `put_on_days` int DEFAULT NULL,
  `first_leg_days` int DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_marketplaceid` (`shopid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundshipment 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundshipment` (
  `ShipmentId` char(15) NOT NULL COMMENT '货件编号',
  `DestinationFulfillmentCenterId` char(50) DEFAULT NULL COMMENT '	亚马逊创建的亚马逊配送中心标识。',
  `ShipToAddressID` bigint unsigned DEFAULT NULL COMMENT '目的地',
  `LabelPrepType` char(36) DEFAULT NULL,
  `ShipmentStatus` char(36) DEFAULT NULL COMMENT 'ShipmentStatus 值：WORKING - 卖家已创建货件，但未发货。 SHIPPED - 承运人已取件。',
  `inboundplanid` bigint unsigned DEFAULT NULL COMMENT 'planId',
  `name` char(80) DEFAULT NULL COMMENT 'shipment_name',
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
  `remark` varchar(500) DEFAULT NULL,
  `TotalFee` decimal(10,2) DEFAULT NULL COMMENT '装运的总手工加工费',
  `currency` char(10) DEFAULT NULL COMMENT '货币',
  `carrier` char(30) DEFAULT NULL COMMENT '承运人',
  `boxnum` int DEFAULT NULL,
  `oldboxnum` int DEFAULT NULL,
  `transtyle` char(10) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `shiped_date` datetime DEFAULT NULL,
  `start_receive_date` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `submissionid` bigint unsigned DEFAULT NULL,
  `feedstatus` char(50) DEFAULT NULL,
  `pro_number` char(10) DEFAULT NULL,
  `transport_status` char(50) DEFAULT NULL,
  `box_contents_source` char(10) DEFAULT NULL,
  `sync_inv` tinyint DEFAULT '0' COMMENT '1代表没有扣库存，2代表已经扣库存',
  `ignorerec` bit(1) DEFAULT b'0' COMMENT '忽略收货异常',
  PRIMARY KEY (`ShipmentId`),
  KEY `Index 2` (`inboundplanid`),
  KEY `Index 3` (`status`),
  KEY `idx_inboundplanid_status_refreshtime` (`inboundplanid`,`status`,`refreshtime`),
  KEY `idx_inboundplanid_status_shipeddate_refreshtime` (`inboundplanid`,`status`,`shiped_date`,`refreshtime`),
  KEY `status5date` (`status5date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundshipment_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundshipment_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `shipmentid` char(15) NOT NULL COMMENT '货件编号',
  `status` int DEFAULT NULL COMMENT '-1,已驳回；0取消货件；1,待审核；2，配货（已确认货件）；3，装箱；4，物流信息确认；5已发货；6，已完成发货',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ShipmentId` (`shipmentid`)
) ENGINE=InnoDB AUTO_INCREMENT=405553 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundtrans 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundtrans` (
  `id` bigint unsigned NOT NULL,
  `shipmentid` char(36) DEFAULT NULL,
  `company` bigint unsigned DEFAULT NULL,
  `channel` bigint unsigned DEFAULT NULL,
  `singleprice` decimal(10,4) DEFAULT NULL,
  `transweight` decimal(10,4) DEFAULT NULL,
  `wunit` char(10) DEFAULT NULL,
  `otherfee` decimal(10,4) DEFAULT NULL,
  `ordernum` char(50) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  `outarrtime` datetime DEFAULT NULL,
  `inarrtime` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  `wtype` tinyint DEFAULT '0',
  `transtype` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shipmentid` (`shipmentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundtrans_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundtrans_his` (
  `opttime` datetime NOT NULL,
  `id` bigint unsigned NOT NULL,
  `shipmentid` char(36) NOT NULL,
  `company` bigint unsigned DEFAULT NULL,
  `channel` bigint unsigned DEFAULT NULL,
  `singleprice` decimal(10,4) DEFAULT NULL,
  `transweight` decimal(10,4) DEFAULT NULL,
  `wunit` char(10) DEFAULT NULL,
  `otherfee` decimal(10,4) DEFAULT NULL,
  `ordernum` char(50) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  `outarrtime` datetime DEFAULT NULL,
  `inarrtime` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  `wtype` tinyint DEFAULT '0',
  `transtype` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`opttime`,`id`) USING BTREE,
  UNIQUE KEY `shipmentid` (`shipmentid`,`opttime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_plan 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_plan` (
  `id` char(36) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_planitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planitem` (
  `id` char(36) NOT NULL,
  `plansubid` char(36) DEFAULT NULL,
  `status` int DEFAULT NULL COMMENT '0代表已放弃，1 代表可用 2.代表已提交。如果plansub的status等于2这里的1 也是已提交',
  `sku` char(50) DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_planmodel 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planmodel` (
  `id` char(36) NOT NULL,
  `planid` char(36) DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `索引 2` (`planid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_planmodelitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planmodelitem` (
  `id` char(36) NOT NULL,
  `modelid` char(36) DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `planamount` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`modelid`,`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_planmodelitemsub 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planmodelitemsub` (
  `itemid` char(36) NOT NULL,
  `sku` char(50) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `needship` int NOT NULL,
  `short_time` datetime DEFAULT NULL COMMENT '断货时间',
  `salesday` int DEFAULT NULL,
  `aftersalesday` int DEFAULT NULL,
  PRIMARY KEY (`itemid`,`marketplaceid`,`sku`),
  KEY `索引 2` (`sku`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_plansub 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_plansub` (
  `id` char(36) NOT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `planid` char(36) DEFAULT NULL,
  `status` int DEFAULT NULL COMMENT '0代表放弃，1代表在用，2代表已提交',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `planid` (`planid`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_plansub_euitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_plansub_euitem` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `planid` char(36) DEFAULT NULL,
  `sku` char(50) DEFAULT NULL,
  `plansubid` char(36) NOT NULL,
  `marketplaceid` char(20) NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `amount` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqid` (`plansubid`,`marketplaceid`,`sku`)
) ENGINE=InnoDB AUTO_INCREMENT=20735 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_shipment_template_file 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_shipment_template_file` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `filename` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `filepath` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_status 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_status` (
  `status` char(20) NOT NULL,
  `content` char(200) NOT NULL DEFAULT '0',
  `name` char(50) DEFAULT NULL,
  PRIMARY KEY (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_transchannel 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transchannel` (
  `id` bigint unsigned NOT NULL,
  `name` char(50) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_transcompany 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transcompany` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '物流公司名称',
  `simplename` varchar(100) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `access_token` char(100) DEFAULT NULL,
  `api` int DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `uploadpath` varchar(200) DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_transcompany_api 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transcompany_api` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `api` varchar(200) NOT NULL DEFAULT '0',
  `name` varchar(200) NOT NULL DEFAULT '0',
  `openkey` varchar(200) NOT NULL DEFAULT '0',
  `openaccount` varchar(200) NOT NULL DEFAULT '0',
  `url` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_transcompany_services_zhihui 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transcompany_services_zhihui` (
  `code` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `apiid` int NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`code`,`apiid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_transdetail 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transdetail` (
  `id` bigint unsigned NOT NULL,
  `company` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `subarea` char(20) DEFAULT NULL,
  `channel` bigint unsigned DEFAULT NULL,
  `channame` char(36) DEFAULT NULL,
  `pretime` int DEFAULT NULL COMMENT 'US预计时效',
  `price` decimal(10,4) DEFAULT NULL,
  `drate` int DEFAULT '5000',
  `opttime` datetime DEFAULT NULL,
  `transtype` bigint unsigned DEFAULT NULL,
  `priceunits` char(10) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `cbmrate` int DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`company`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_transdetail_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transdetail_his` (
  `id` bigint unsigned NOT NULL,
  `opttime` datetime NOT NULL,
  `company` bigint unsigned NOT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `subarea` char(20) DEFAULT NULL,
  `channel` char(36) DEFAULT NULL,
  `channame` char(36) DEFAULT NULL,
  `pretime` int DEFAULT NULL COMMENT 'US预计时效',
  `price` decimal(10,4) DEFAULT NULL,
  `drate` int DEFAULT '5000',
  `transtype` bigint unsigned DEFAULT NULL,
  `priceunits` char(10) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `cbmrate` int DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `disabled` bit(1) DEFAULT b'0',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`opttime`,`id`) USING BTREE,
  KEY `Index 2` (`company`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_stepwise_quotn 结构
CREATE TABLE IF NOT EXISTS `t_erp_stepwise_quotn` (
  `id` bigint unsigned NOT NULL,
  `material` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `material_amount` (`material`,`amount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_stockcycle 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_stocktaking 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `isworking` bit(1) DEFAULT NULL,
  `whtotalamount` int DEFAULT NULL,
  `whtotalprice` decimal(15,4) DEFAULT NULL,
  `overamount` int DEFAULT NULL,
  `lossamount` int DEFAULT NULL,
  `overprice` decimal(12,4) DEFAULT NULL,
  `lossprice` decimal(12,4) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_stocktaking_item 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_summary_data 结构
CREATE TABLE IF NOT EXISTS `t_erp_summary_data` (
  `id` bigint unsigned NOT NULL,
  `ftype` char(20) NOT NULL,
  `value` decimal(10,2) DEFAULT NULL,
  `mapdata` varchar(2000) DEFAULT NULL,
  `shopid` bigint unsigned NOT NULL,
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`shopid`,`ftype`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='主页上的数据，每日更新';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_transtype 结构
CREATE TABLE IF NOT EXISTS `t_erp_transtype` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_turnover_rate 结构
CREATE TABLE IF NOT EXISTS `t_erp_turnover_rate` (
  `id` int DEFAULT NULL,
  `year` int DEFAULT NULL,
  `month` int DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `sku` char(36) DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `begininv` int DEFAULT NULL,
  `endinv` int DEFAULT NULL,
  `outinv` int DEFAULT NULL,
  `wrate` decimal(10,2) DEFAULT NULL,
  `wday` decimal(10,2) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_unsalable 结构
CREATE TABLE IF NOT EXISTS `t_erp_unsalable` (
  `sku` char(30) DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `invqty` int DEFAULT NULL,
  `invinqty` int DEFAULT NULL,
  `inv90` int DEFAULT NULL,
  `inv180` int DEFAULT NULL,
  `inv365` int DEFAULT NULL,
  `invout90` int DEFAULT NULL,
  `over90` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_usersales_rank 结构
CREATE TABLE IF NOT EXISTS `t_erp_usersales_rank` (
  `userid` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `daytype` int NOT NULL,
  `quantity` int DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `oldorderprice` decimal(10,2) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`userid`,`shopid`,`daytype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_calculate_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_calculate_record` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `ftype` char(20) NOT NULL COMMENT '计算类型，发货，采购，人力',
  `iswarn` bit(1) NOT NULL DEFAULT b'0',
  `operator` bigint unsigned NOT NULL COMMENT '计算操作人',
  `opttime` datetime NOT NULL COMMENT '计算时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_ftype` (`shopid`,`ftype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用于存储各个计算模块的计算时间，计算人。统一留存历史记录';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_calculate_record_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_calculate_record_history` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `ftype` char(20) DEFAULT NULL,
  `iswarn` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_ftype` (`shopid`,`ftype`,`opttime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_man_month 结构
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_month` (`shopid`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人力计算结果保存';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_man_month_history 结构
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_month` (`shopid`,`month`,`opttime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人力计算结果历史';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_pickpay_form 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购提货与付款模块分组，采用一个月一个表单的结构，对采购付款历史与审核进行保存';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_pickpay_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_pickpay_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned NOT NULL COMMENT '订单ID',
  `materialid` bigint unsigned NOT NULL COMMENT '产品ID',
  `auditor` bigint unsigned NOT NULL COMMENT '审核人',
  `auditstatus` int unsigned NOT NULL DEFAULT '0' COMMENT '审核状态',
  `audittime` datetime NOT NULL COMMENT '审核时间',
  `supplier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '供应商',
  `inbound` int NOT NULL DEFAULT '0' COMMENT '待入库数量',
  `suggest` varchar(500) NOT NULL DEFAULT '0' COMMENT '建议提货量',
  `planpick` int NOT NULL DEFAULT '0' COMMENT '建议付款金额',
  `planpay` decimal(14,2) NOT NULL DEFAULT '0.00' COMMENT '计划付款金额',
  `remark` varchar(500) NOT NULL DEFAULT '0' COMMENT '计划提货量',
  `operator` bigint unsigned NOT NULL COMMENT '操作人',
  `creator` bigint unsigned NOT NULL COMMENT '创建人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提货付款模块SKU从审核到通过以及历史的具体entry表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_presale_month 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售预测月度结果';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_presale_month_form 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售预测提交的表单，以每个月一份表单的方式存储整个公司关于销售预测的提交';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_presale_month_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_month_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned NOT NULL COMMENT '本地产品ID ',
  `pid` bigint unsigned NOT NULL COMMENT '商品ID',
  `auditstatus` int unsigned DEFAULT '0' COMMENT '0未提交，1 提交待审核，2审核成功  3已驳回',
  `audittime` datetime DEFAULT NULL COMMENT '审核时间',
  `auditor` bigint unsigned DEFAULT '0' COMMENT '审核人',
  `status` int unsigned DEFAULT '0' COMMENT '维持 提升 等销售状态 默认是无',
  `remark` varchar(500) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creatime` datetime DEFAULT NULL COMMENT '创建时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `creator` bigint unsigned DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_one` (`pid`,`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售预测单个SKU所以的审核与历史表单对应';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_presale_month_form_entry_item 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售预测每个SKU对应每个月的预测数据';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_presale_week 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售预测周结构数据存储';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_form 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='采购模块表单保存表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_form_entry 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_material 结构
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`materialid`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE,
  KEY `sumrquest` (`sumrquest`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品采购计划，计算结果，存储每个SKU对应的采购周期，需求量，建议采购量，总需求量等';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_material_history 结构
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`materialid`,`opttime`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_selected 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_selected` (
  `materialid` bigint unsigned NOT NULL COMMENT '本地产品ID',
  `userid` bigint unsigned NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`materialid`,`userid`),
  KEY `userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='采购计算结果选中发货，记录是否选中';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_week 结构
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`materialid`,`week`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品采购计划，通过发货需求与本地产品对应，并进行本地SKU转化，组装子SKU转换\r\n采购周情况存储。包换需求量，多余库存等';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_week_history 结构
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`materialid`,`week`,`opttime`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='采购计算周数据历史保存';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_ship_setting 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_ship_setting` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `addnum` int DEFAULT NULL,
  `startday` int DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发货计划设置，用于保存发货计算中当达成率与增长率达到标准时增加的天数';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_ship_week 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_ship_week` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `pid` bigint unsigned NOT NULL COMMENT '商品ID',
  `week` date NOT NULL COMMENT '对应周日期',
  `amount` int unsigned NOT NULL DEFAULT '0' COMMENT '对应周需求量',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`pid`,`week`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发货计划周情况，记录发货计算历史结果,根据销售预测得到需求量';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_ship_week_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_ship_week_history` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `pid` bigint unsigned NOT NULL,
  `week` date NOT NULL,
  `amount` int unsigned NOT NULL DEFAULT '0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`pid`,`week`,`opttime`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_productl_workhours 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_productl_workhours` (
  `mid` bigint unsigned NOT NULL COMMENT 'pid',
  `amount` int unsigned DEFAULT NULL COMMENT '一个小时内的生产量',
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'hour' COMMENT '类型: hour mins second等 默认hour',
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`mid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人力计算配置表\r\n标准工时/H	待增加	每个小时能处理多少个该产品的组装及打包发货工作	产品信息管理	\r\n独立产品及组装产品的主SKU（在售成品）需要设置【标准工时】。用作计算发货工时及人力需求。	\r\n如有产品需要完成组装（工作较为复杂），则可能为“12”，意为每小时从拣货、组装到装箱打包可以处理12个该产品。如流程较为简单，则可能为“35”，即为每小时可处理35个。\r\n';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_ship_product_delivery_cycle 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_ship_product_delivery_cycle` (
  `pid` bigint unsigned NOT NULL COMMENT '商品ID',
  `deliverycycle` int unsigned DEFAULT NULL COMMENT '头程天数',
  `findex` int unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品的头程周期存储';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_shop_units_worktime 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人力计算配置表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse` (
  `id` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'ID',
  `name` char(36) DEFAULT NULL COMMENT '名称',
  `ftype` char(36) NOT NULL COMMENT '类型',
  `flevel` char(36) DEFAULT NULL COMMENT '级别',
  `number` char(36) NOT NULL COMMENT '编号',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `findex` int DEFAULT NULL COMMENT '次序',
  `fbawareid` bigint unsigned DEFAULT NULL COMMENT '海外仓',
  `isdefault` bit(1) DEFAULT b'0' COMMENT '默认仓库',
  `shopid` bigint unsigned NOT NULL COMMENT '店铺',
  `parentid` bigint unsigned DEFAULT NULL COMMENT '父节点',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  `stocking_cycle` int DEFAULT '0',
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  `isstocktaking` bit(1) DEFAULT b'0',
  `min_cycle` int DEFAULT '0',
  `first_leg_charges` decimal(12,2) DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  `ishungry` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `Index 2` (`parentid`),
  KEY `ftype` (`ftype`),
  KEY `shopid` (`shopid`),
  KEY `name_shopid` (`name`,`shopid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse_fba 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_fba` (
  `id` bigint unsigned NOT NULL COMMENT 'ID',
  `shopid` bigint unsigned NOT NULL COMMENT '店铺',
  `marketplaceid` char(15) NOT NULL,
  `name` char(36) DEFAULT NULL COMMENT '名称',
  `number` char(36) DEFAULT NULL,
  `stocking_cycle` int DEFAULT '0' COMMENT '安全库存周期',
  `min_cycle` int DEFAULT '0' COMMENT '最小发货周期',
  `put_on_days` int DEFAULT '0' COMMENT '上架周期',
  `first_leg_days` int DEFAULT '0' COMMENT '头程周期',
  `remark` varchar(1000) DEFAULT '0',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `shopid_marketplaceid` (`shopid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse_shelf 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf` (
  `id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '货柜ID',
  `warehouseid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '仓库ID',
  `name` varchar(200) NOT NULL DEFAULT '0' COMMENT '货柜名称',
  `number` char(50) NOT NULL DEFAULT '0' COMMENT '编码',
  `capacity` float NOT NULL DEFAULT '0' COMMENT '容量(立方厘米)',
  `length` float unsigned NOT NULL DEFAULT '0' COMMENT '长度(cm)',
  `width` float unsigned NOT NULL DEFAULT '0' COMMENT '宽度(cm)',
  `height` float unsigned NOT NULL DEFAULT '0' COMMENT '高度(cm)',
  `parentid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '父货柜ID',
  `sort` int unsigned NOT NULL DEFAULT '0' COMMENT '排序即（柜子所在位置）',
  `treepath` char(200) NOT NULL DEFAULT '0' COMMENT '所有付货柜编码如：A01!033!F01',
  `shopid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '公司ID',
  `iswarn` bit(1) DEFAULT b'0' COMMENT '是否报警',
  `isdelete` bit(1) DEFAULT b'0' COMMENT '是否逻辑删除',
  `isfrozen` bit(1) DEFAULT b'0' COMMENT '是否冻结',
  `operator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '操作人',
  `opttime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '操作时间',
  `creator` bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `creattime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `treepath` (`shopid`,`warehouseid`,`treepath`),
  KEY `parentid` (`parentid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='仓库货柜';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse_shelf_inventory 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf_inventory` (
  `id` bigint unsigned NOT NULL COMMENT 'ID',
  `shelfid` bigint unsigned DEFAULT NULL COMMENT '货柜ID',
  `materialid` bigint unsigned DEFAULT NULL COMMENT '产品ID',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `quantity` int unsigned DEFAULT NULL COMMENT '当前数量',
  `size` float unsigned DEFAULT NULL COMMENT '当前体积',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `shelfid_materialid_shopid` (`shopid`,`shelfid`,`materialid`) USING BTREE,
  KEY `materialid_shopid` (`shopid`,`materialid`,`shelfid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='货架产品库存';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse_shelf_inventory_opt_pro 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf_inventory_opt_pro` (
  `id` bigint unsigned NOT NULL COMMENT 'ID',
  `shelfid` bigint unsigned DEFAULT NULL COMMENT '货架ID',
  `materialid` bigint unsigned DEFAULT NULL COMMENT '产品ID',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `quantity` int unsigned DEFAULT NULL COMMENT '操作数量',
  `size` float unsigned DEFAULT NULL COMMENT '操作数量对于的体积',
  `balance_qty` int unsigned DEFAULT NULL COMMENT '操作前结余重量',
  `balance_size` float unsigned DEFAULT NULL COMMENT '操作前结余体积',
  `opt` int unsigned DEFAULT NULL COMMENT '0：出库；1：入库',
  `formid` bigint unsigned DEFAULT NULL COMMENT '表单ID',
  `formtype` char(50) DEFAULT NULL COMMENT '表单类型',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shelfid_materialid_shopid` (`shopid`,`shelfid`,`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预操作，此表内不存储任何记录。当预操作结束后自动删除';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse_shelf_inventory_opt_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf_inventory_opt_record` (
  `id` bigint unsigned NOT NULL COMMENT 'ID',
  `shelfid` bigint unsigned DEFAULT NULL COMMENT '货柜ID',
  `materialid` bigint unsigned DEFAULT NULL COMMENT '产品ID',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `quantity` int unsigned DEFAULT NULL COMMENT '操作数量',
  `size` float unsigned DEFAULT NULL COMMENT '操作数量对于的体积使用立方厘米cm3',
  `balance_qty` int unsigned DEFAULT NULL COMMENT '操作后结余数量',
  `balance_size` float unsigned DEFAULT NULL COMMENT '操作后结余体积',
  `opt` int unsigned DEFAULT NULL COMMENT '0：出库；1：入库;2：修正下架；3：修正上架',
  `formid` bigint unsigned DEFAULT NULL COMMENT '表单ID',
  `formtype` char(50) DEFAULT NULL COMMENT '表单类型',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shelfid_materialid_shopid_formid_formtype` (`shopid`,`shelfid`,`materialid`),
  KEY `formid_formtype` (`formid`,`formtype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作记录';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse_type 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_type` (
  `id` char(36) NOT NULL COMMENT 'ID',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '店铺',
  `issystem` bit(1) DEFAULT NULL COMMENT '是否系统',
  `name` char(50) DEFAULT NULL COMMENT '名字',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_whse_unsalable_rpt 结构
CREATE TABLE IF NOT EXISTS `t_erp_whse_unsalable_rpt` (
  `shopid` bigint unsigned NOT NULL,
  `wid` bigint unsigned NOT NULL,
  `name` char(36) DEFAULT NULL,
  `mtid` bigint unsigned NOT NULL,
  `sku` char(50) NOT NULL,
  `groupid` char(36) NOT NULL,
  `qtysum` decimal(35,0) DEFAULT NULL,
  `qtyablesum` decimal(35,0) DEFAULT NULL,
  `qtysum60` decimal(35,0) DEFAULT NULL,
  `qtysum90` decimal(35,0) DEFAULT NULL,
  `qtysum180` decimal(35,0) DEFAULT NULL,
  `qtysum365` decimal(35,0) DEFAULT NULL,
  `salesum60` decimal(32,0) DEFAULT NULL,
  `salesum90` decimal(32,0) DEFAULT NULL,
  `salesum180` decimal(32,0) DEFAULT NULL,
  `salesum365` decimal(32,0) DEFAULT NULL,
  `nostock30` decimal(32,0) DEFAULT NULL,
  `qtysum30` decimal(35,0) DEFAULT NULL,
  `salesum30` decimal(35,0) DEFAULT NULL,
  PRIMARY KEY (`shopid`,`wid`,`mtid`,`sku`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_exchangeinfo 结构
CREATE TABLE IF NOT EXISTS `t_exchangeinfo` (
  `currency` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '币别',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`currency`) USING BTREE,
  UNIQUE KEY `symbol` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_exchangerate 结构
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

-- 导出  表 db_wimoor.t_exchangerate_customer 结构
CREATE TABLE IF NOT EXISTS `t_exchangerate_customer` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL,
  `name` char(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT '' COMMENT '币别',
  `price` decimal(18,6) DEFAULT NULL COMMENT '汇率',
  `symbol` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `type` varchar(15) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `utctime` datetime DEFAULT NULL COMMENT '服务器更新时间',
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`shopid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_exchangerate_his 结构
CREATE TABLE IF NOT EXISTS `t_exchangerate_his` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '币别',
  `price` decimal(18,6) DEFAULT NULL COMMENT '汇率',
  `symbol` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `type` varchar(15) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `isnewest` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `index_name` (`name`,`byday`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=452103 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fbaformat 结构
CREATE TABLE IF NOT EXISTS `t_fbaformat` (
  `id` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `country` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ismedia` bit(1) DEFAULT NULL,
  `producttierId` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `fba_format` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `month` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `weight` decimal(10,4) DEFAULT NULL,
  `effective_date` date DEFAULT NULL COMMENT '生效日期',
  `expiry_date` date DEFAULT NULL COMMENT '失效日期',
  `dispatch_type` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '亚马逊配送方案',
  `isclothing` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_fbaformat_t_producttier` (`producttierId`),
  KEY `country` (`country`),
  CONSTRAINT `key_productid` FOREIGN KEY (`producttierId`) REFERENCES `t_producttier` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fba_estimated_fees 结构
CREATE TABLE IF NOT EXISTS `t_fba_estimated_fees` (
  `sku` char(50) NOT NULL,
  `fnsku` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `asin` char(36) NOT NULL,
  `product_name` varchar(500) DEFAULT NULL,
  `product_group` char(50) DEFAULT NULL,
  `brand` char(200) DEFAULT NULL,
  `fulfilled_by` char(30) DEFAULT NULL,
  `has_local_inventory` char(50) DEFAULT NULL,
  `your_price` decimal(10,2) DEFAULT NULL,
  `sales_price` decimal(10,2) DEFAULT NULL,
  `longest_side` decimal(10,2) DEFAULT NULL,
  `median_side` decimal(10,2) DEFAULT NULL,
  `shortest_side` decimal(10,2) DEFAULT NULL,
  `length_and_girth` decimal(10,2) DEFAULT NULL,
  `unit_of_dimension` char(20) DEFAULT NULL,
  `item_package_weight` decimal(10,2) DEFAULT NULL,
  `unit_of_weight` char(30) DEFAULT NULL,
  `product_size_tier` char(100) DEFAULT NULL,
  `currency` char(40) DEFAULT NULL,
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
  `marketplaceid` char(15) NOT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`marketplaceid`,`sku`) USING BTREE,
  KEY `index_auth` (`sku`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fba_labeling_service_fee 结构
CREATE TABLE IF NOT EXISTS `t_fba_labeling_service_fee` (
  `id` char(36) NOT NULL,
  `isStandard` bit(1) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `country` char(12) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `producttierId` (`isStandard`),
  KEY `country` (`country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fba_longterm_storage_fee_report 结构
CREATE TABLE IF NOT EXISTS `t_fba_longterm_storage_fee_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `snapshot_date` datetime DEFAULT NULL,
  `sku` char(50) DEFAULT NULL,
  `fnsku` char(50) DEFAULT NULL,
  `asin` char(255) DEFAULT NULL,
  `qty_charged_12month` int DEFAULT NULL COMMENT '存储超过12个月的产品被收费的数量',
  `qty_charged_6month` int DEFAULT NULL,
  `amount_6` decimal(10,2) DEFAULT NULL,
  `amount_12` decimal(10,2) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `per_unit_volume` decimal(12,2) DEFAULT NULL,
  `volume_unit` decimal(10,2) DEFAULT NULL,
  `country` char(10) DEFAULT NULL,
  `is_sl` bit(1) DEFAULT b'0',
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `date-sku-country` (`amazonauthid`,`country`,`sku`,`snapshot_date`)
) ENGINE=InnoDB AUTO_INCREMENT=132507 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fba_storage_fee_report 结构
CREATE TABLE IF NOT EXISTS `t_fba_storage_fee_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `asin` char(20) NOT NULL,
  `fnsku` char(20) DEFAULT NULL,
  `fulfillment_center` char(15) DEFAULT NULL,
  `country` char(10) NOT NULL,
  `longest_side` decimal(10,2) DEFAULT NULL,
  `median_side` decimal(10,2) DEFAULT NULL,
  `shortest_side` decimal(10,2) DEFAULT NULL,
  `unit_of_dimension` char(20) DEFAULT NULL,
  `weight` decimal(10,2) DEFAULT NULL,
  `unit_of_weight` char(20) DEFAULT NULL,
  `item_volume` decimal(12,4) DEFAULT NULL,
  `volume_units` char(20) DEFAULT NULL,
  `product_size_tier` char(100) DEFAULT NULL,
  `avg_quantity_on_hand` decimal(10,2) DEFAULT NULL COMMENT '商品的日均体积，等于过去一个月的库存总体积除以该月包含的天数。',
  `avg_quantity_pending_removal` decimal(12,4) DEFAULT NULL,
  `total_item_volume` decimal(10,2) DEFAULT NULL,
  `month` char(50) DEFAULT NULL COMMENT '收费的月份，如2019-11',
  `storage_rate` decimal(10,2) DEFAULT NULL,
  `currency` char(20) DEFAULT NULL,
  `monthly_storage_fee` decimal(12,4) DEFAULT NULL,
  `category` char(50) DEFAULT NULL,
  `eligible_for_inv_discount` bit(1) DEFAULT b'0',
  `qualifies_for_inv_discount` bit(1) DEFAULT b'0',
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `month` (`month`),
  KEY `index1` (`amazonauthid`,`country`,`asin`,`month`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17441468 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fixed_closingfee 结构
CREATE TABLE IF NOT EXISTS `t_fixed_closingfee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `format` char(100) DEFAULT NULL COMMENT '价格范围',
  `category` char(50) DEFAULT NULL,
  `fee` decimal(10,4) DEFAULT NULL,
  `country` char(10) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_individualfee 结构
CREATE TABLE IF NOT EXISTS `t_individualfee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `country` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `perItemFee` decimal(10,2) DEFAULT NULL COMMENT '个人卖家才有per-item fee',
  PRIMARY KEY (`id`),
  KEY `country` (`country`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inplacefee 结构
CREATE TABLE IF NOT EXISTS `t_inplacefee` (
  `id` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `description` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `country` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `country` (`country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inplacefeeformat 结构
CREATE TABLE IF NOT EXISTS `t_inplacefeeformat` (
  `id` int NOT NULL AUTO_INCREMENT,
  `inplacefeeid` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `producttierId` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `standard` bit(1) DEFAULT NULL,
  `format` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `country` char(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `inplacefeeid` (`inplacefeeid`),
  KEY `country` (`country`),
  KEY `producttierId` (`producttierId`),
  CONSTRAINT `t_inplacefeeformat_ibfk_1` FOREIGN KEY (`inplacefeeid`) REFERENCES `t_inplacefee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventorystoragefee 结构
CREATE TABLE IF NOT EXISTS `t_inventorystoragefee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `month` varchar(255) DEFAULT NULL,
  `price` decimal(10,3) DEFAULT NULL,
  `country` char(10) DEFAULT NULL,
  `isStandard` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventory_report 结构
CREATE TABLE IF NOT EXISTS `t_inventory_report` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `fnsku` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `pcondition` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `your_price` decimal(10,2) DEFAULT NULL,
  `mfn_listing_exists` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `mfn_fulfillable_quantity` int DEFAULT NULL,
  `afn_listing_exists` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
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
  `amazonAuthId` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`marketplaceid`,`amazonAuthId`,`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventory_report_his 结构
CREATE TABLE IF NOT EXISTS `t_inventory_report_his` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `byday` date NOT NULL,
  `marketplaceid` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `fnsku` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pcondition` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `your_price` decimal(10,2) DEFAULT NULL,
  `mfn_listing_exists` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `mfn_fulfillable_quantity` int DEFAULT NULL,
  `afn_listing_exists` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventory_reserved_report 结构
CREATE TABLE IF NOT EXISTS `t_inventory_reserved_report` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `fnsku` char(100) DEFAULT NULL,
  `asin` char(36) DEFAULT NULL,
  `reserved_qty` int DEFAULT NULL,
  `reserved_customerorders` int DEFAULT NULL,
  `reserved_fc_transfers` int DEFAULT NULL,
  `reserved_fc_processing` int DEFAULT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index1` (`sku`,`marketplaceid`,`amazonAuthId`),
  KEY `idx_amazonAuthId_marketplaceid` (`amazonAuthId`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventory_status 结构
CREATE TABLE IF NOT EXISTS `t_inventory_status` (
  `id` bigint unsigned NOT NULL,
  `stockStatus` char(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `sku` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `marketplaceid` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `amazonAuthId` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`byday`,`asin`,`marketplaceid`,`amazonAuthId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventory_temp 结构
CREATE TABLE IF NOT EXISTS `t_inventory_temp` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sku` char(50) DEFAULT NULL,
  `warehouse` char(100) DEFAULT NULL,
  `instock` int DEFAULT NULL,
  `inbound` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1641 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_manager_limit 结构
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
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `opratetime` datetime DEFAULT NULL COMMENT '修改时间',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `oprate` bigint unsigned DEFAULT NULL COMMENT '修改人',
  `logicVersion` bigint DEFAULT '0',
  `saleskey` char(36) DEFAULT NULL,
  `neverNoticeShop` bit(1) DEFAULT b'0',
  `afterNnoticeTariff` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `t_manager_limit_ibfk_1` (`shopId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_manager_limit_append 结构
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_manual_processing_fee 结构
CREATE TABLE IF NOT EXISTS `t_manual_processing_fee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `month` varchar(255) DEFAULT NULL,
  `manualProcessingFee` decimal(10,2) DEFAULT '0.00' COMMENT '手动处理费',
  `country` char(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_marketplace 结构
CREATE TABLE IF NOT EXISTS `t_marketplace` (
  `marketplaceId` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '站点编码ID',
  `market` char(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '站点简码',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '站点名称',
  `region_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '所属区域名称',
  `region` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '所属区域简码',
  `end_point` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '所属区域站点',
  `point_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `accessKey` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `secretKey` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `dim_units` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `weight_units` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `findex` int DEFAULT '0',
  `adv_end_point` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `aws_access_key` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `aws_secret_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `associate_tag` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `developer_url` varchar(1100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `dev_account_num` char(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `bytecode` char(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `sp_api_endpoint` char(40) COLLATE utf8_bin DEFAULT NULL,
  `aws_region` char(10) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`marketplaceId`),
  KEY `Index 2` (`market`),
  KEY `Index 3` (`point_name`),
  KEY `region` (`region`),
  KEY `marketplaceId_region` (`marketplaceId`,`region`),
  KEY `currency` (`currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='站点';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_menu 结构
CREATE TABLE IF NOT EXISTS `t_menu` (
  `id` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键(h)',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述信息',
  `mindex` int DEFAULT NULL COMMENT '次序',
  `mlevel` int DEFAULT NULL COMMENT '层级',
  `parentid` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '父菜单',
  `icon` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图标',
  `childnumber` int DEFAULT NULL COMMENT '子菜单个数',
  `action_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '页面链接',
  `groupid` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '分组',
  PRIMARY KEY (`id`),
  KEY `FK_t_menu_t_menu` (`parentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='菜单';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_menu_group 结构
CREATE TABLE IF NOT EXISTS `t_menu_group` (
  `id` char(36) NOT NULL,
  `name` varchar(60) DEFAULT NULL,
  `icon` varchar(500) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_financial 结构
CREATE TABLE IF NOT EXISTS `t_orders_financial` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `amazon_order_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单ID',
  `order_item_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单Item id',
  `sku` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'SKU',
  `currency` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '货币',
  `ftype` char(240) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '费用类型',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '费用',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `marketplaceId` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '站点',
  `posted_date` datetime DEFAULT NULL COMMENT '出账时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `amazon_order_id` (`amazon_order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33764 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_fulfilled_shipments_report 结构
CREATE TABLE IF NOT EXISTS `t_orders_fulfilled_shipments_report` (
  `amazon_order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_order_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_id` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipment_item_id` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `amazon_order_item_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `merchant_order_item_id` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
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
  PRIMARY KEY (`amazon_order_id`,`shipment_item_id`,`amazon_order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_remark 结构
CREATE TABLE IF NOT EXISTS `t_orders_remark` (
  `amazon_order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '亚马逊订单ID',
  `feed_queueid` bigint unsigned DEFAULT NULL COMMENT '上传VAT发货的Feed队列ID【系统指派】',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '客户给订单的备注',
  `operator` bigint unsigned DEFAULT NULL COMMENT '修改人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`amazon_order_id`),
  KEY `amazon_order_id_remark` (`remark`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='订单备注';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_report 结构
CREATE TABLE IF NOT EXISTS `t_orders_report` (
  `id` bigint unsigned NOT NULL,
  `amazon_order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `merchant_order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_status` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `fulfillment_channel` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sales_channel` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `order_channel` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ship_service_level` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `item_status` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_city` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ship_state` char(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ship_postal_code` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ship_country` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `promotion_ids` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `is_business_order` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `purchase_order_number` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `price_designation` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceId` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`purchase_date`,`id`),
  KEY `Index 2` (`sales_channel`,`sku`),
  KEY `Index` (`amazon_order_id`,`sku`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_report_download 结构
CREATE TABLE IF NOT EXISTS `t_orders_report_download` (
  `id` bigint unsigned NOT NULL,
  `amazon_order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `merchant_order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_status` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `fulfillment_channel` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sales_channel` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `order_channel` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ship_service_level` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `asin` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `item_status` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `currency` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ship_state` char(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ship_postal_code` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ship_country` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `promotion_ids` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `is_business_order` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `purchase_order_number` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `price_designation` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL,
  `marketplaceId` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`) USING BTREE,
  KEY `Index 2` (`amazonAuthId`,`amazon_order_id`,`sku`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_reviews_customer 结构
CREATE TABLE IF NOT EXISTS `t_orders_reviews_customer` (
  `amazon_order_id` char(50) NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `remark` varchar(50) DEFAULT NULL,
  `picture` varchar(500) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_sumconfig 结构
CREATE TABLE IF NOT EXISTS `t_orders_sumconfig` (
  `id` char(36) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `order_status` char(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `shop_id` bigint unsigned DEFAULT NULL,
  `sales_channel` char(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `is_business_order` char(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `discountfrom` decimal(10,0) DEFAULT NULL,
  `discountTo` decimal(10,0) DEFAULT NULL,
  `amazonAuthId` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_summary 结构
CREATE TABLE IF NOT EXISTS `t_orders_summary` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceid` char(25) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `purchase_date` date NOT NULL,
  `asin` char(36) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `quantity` int DEFAULT NULL,
  `ordersum` int DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `quantity2b` int DEFAULT NULL,
  `ordersum2b` int DEFAULT NULL,
  `orderprice2b` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`),
  UNIQUE KEY `index_1` (`amazonAuthId`,`marketplaceid`,`sku`,`purchase_date`),
  KEY `Index 2` (`marketplaceid`,`quantity`,`orderprice`,`ordersum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_summary_month 结构
CREATE TABLE IF NOT EXISTS `t_orders_summary_month` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceid` char(25) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `purchase_date` date NOT NULL,
  `asin` char(36) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `quantity` int DEFAULT NULL,
  `ordersum` int DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `quantity2b` int DEFAULT NULL,
  `ordersum2b` int DEFAULT NULL,
  `orderprice2b` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`),
  UNIQUE KEY `index_1` (`amazonAuthId`,`marketplaceid`,`sku`,`purchase_date`),
  KEY `Index 2` (`marketplaceid`,`quantity`,`orderprice`,`ordersum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_summary_week 结构
CREATE TABLE IF NOT EXISTS `t_orders_summary_week` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceid` char(25) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `purchase_date` date NOT NULL,
  `asin` char(36) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `quantity` int DEFAULT NULL,
  `ordersum` int DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `quantity2b` int DEFAULT NULL,
  `ordersum2b` int DEFAULT NULL,
  `orderprice2b` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`),
  UNIQUE KEY `index_1` (`amazonAuthId`,`marketplaceid`,`sku`,`purchase_date`),
  KEY `Index 2` (`marketplaceid`,`quantity`,`orderprice`,`ordersum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_order_invoice 结构
CREATE TABLE IF NOT EXISTS `t_order_invoice` (
  `id` bigint unsigned NOT NULL,
  `groupid` bigint unsigned NOT NULL,
  `image` bigint unsigned DEFAULT NULL,
  `logoUrl` varchar(255) DEFAULT NULL COMMENT '图片地址id',
  `company` varchar(255) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `postalcode` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `sign` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `groupid` (`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_order_invoice_vat 结构
CREATE TABLE IF NOT EXISTS `t_order_invoice_vat` (
  `id` bigint unsigned NOT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `vat_num` varchar(50) DEFAULT NULL,
  `vat_rate` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `groupid` (`groupid`,`country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_outbound_weightformat 结构
CREATE TABLE IF NOT EXISTS `t_outbound_weightformat` (
  `id` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `producttierId` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `isMedia` bit(1) DEFAULT NULL,
  `format` char(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `producttierId` (`producttierId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_parameterconfig 结构
CREATE TABLE IF NOT EXISTS `t_parameterconfig` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ptype` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `pkey` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sortindex` int DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pkey` (`pkey`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_picture 结构
CREATE TABLE IF NOT EXISTS `t_picture` (
  `id` bigint unsigned NOT NULL COMMENT '图片ID',
  `url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图片网络位置',
  `location` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图片本地位置',
  `height` decimal(10,2) DEFAULT NULL COMMENT '图片高度',
  `height_units` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '高度单位',
  `width` decimal(10,2) DEFAULT NULL COMMENT '图片宽度',
  `width_units` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '宽度单位',
  `oldid` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_location` (`location`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='用于存放Image';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_prepservicefee 结构
CREATE TABLE IF NOT EXISTS `t_prepservicefee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `isStandard` bit(1) DEFAULT NULL,
  `prepServiceFee` decimal(10,2) DEFAULT '0.00',
  `country` char(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_productformat 结构
CREATE TABLE IF NOT EXISTS `t_productformat` (
  `id` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `producttierId` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `country` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `format` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `length_unit` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `weight_unit` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sort` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`producttierId`),
  CONSTRAINT `key_producttierid` FOREIGN KEY (`producttierId`) REFERENCES `t_producttier` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_producttier 结构
CREATE TABLE IF NOT EXISTS `t_producttier` (
  `id` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `isStandard` bit(1) DEFAULT NULL,
  `country` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `box_weight` decimal(10,4) DEFAULT NULL COMMENT '包装箱重量（单位：kg）',
  `amz_name` char(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '对应亚马逊显示的product tier',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_category 结构
CREATE TABLE IF NOT EXISTS `t_product_category` (
  `CategoryId` char(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `pid` bigint unsigned NOT NULL,
  `Name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `parentId` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`pid`,`CategoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='产品分类表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_follow 结构
CREATE TABLE IF NOT EXISTS `t_product_follow` (
  `id` bigint unsigned NOT NULL,
  `asin` char(18) DEFAULT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `lastupdateTime` datetime DEFAULT NULL,
  `isread` bit(1) DEFAULT b'0',
  `sku` char(36) DEFAULT NULL,
  `flownumber` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `asin_amazonAuthId_marketplaceid_endtime_isnewest` (`amazonAuthId`,`marketplaceid`,`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_info 结构
CREATE TABLE IF NOT EXISTS `t_product_info` (
  `id` bigint unsigned NOT NULL COMMENT '产品ID',
  `asin` char(36) DEFAULT NULL COMMENT '唯一码asin',
  `sku` varchar(50) DEFAULT NULL COMMENT '用户码sku',
  `marketplaceid` char(36) DEFAULT NULL COMMENT '站点',
  `name` varchar(1000) DEFAULT NULL COMMENT '产品名称（产品标题）',
  `openDate` datetime DEFAULT NULL COMMENT '创建日期',
  `itemDimensions` bigint unsigned DEFAULT NULL COMMENT '产品尺寸',
  `pageDimensions` bigint unsigned DEFAULT NULL COMMENT '含包装尺寸',
  `fulfillChannel` varchar(120) DEFAULT NULL COMMENT '交付渠道',
  `binding` varchar(50) DEFAULT NULL COMMENT '装订',
  `totalOfferCount` int DEFAULT NULL COMMENT '卖家数量',
  `brand` varchar(100) DEFAULT NULL COMMENT '品牌',
  `manufacturer` varchar(250) DEFAULT NULL COMMENT '厂商',
  `pgroup` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '分组',
  `typename` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '分类',
  `price` decimal(14,2) DEFAULT NULL COMMENT '价格',
  `image` bigint unsigned DEFAULT NULL COMMENT '照片',
  `parentMarketplace` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '父商品marketplace',
  `parentAsin` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '父商品asin',
  `isparent` bit(1) DEFAULT b'0' COMMENT '是否副产品（即不是变体）',
  `lastupdate` datetime DEFAULT NULL COMMENT '更新时间',
  `createdate` datetime DEFAULT NULL,
  `amazonAuthId` bigint unsigned DEFAULT NULL COMMENT '授权ID',
  `invalid` bit(1) DEFAULT b'0' COMMENT '是否无效',
  `oldid` char(36) DEFAULT NULL,
  `inSnl` bit(1) DEFAULT b'0' COMMENT '是否轻小',
  `fnsku` char(20) DEFAULT NULL,
  `pcondition` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `status` char(20) DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 3` (`amazonAuthId`,`marketplaceid`,`sku`) USING BTREE,
  KEY `Index 6` (`marketplaceid`,`amazonAuthId`,`isparent`,`invalid`,`disable`) USING BTREE,
  KEY `invalid` (`invalid`,`disable`,`isparent`) USING BTREE,
  KEY `idx_sku_isparent_invalid` (`sku`) USING BTREE,
  KEY `idx_asin_amazonAuthId` (`asin`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_info_status_define 结构
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_name` (`shopid`,`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_adv 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_opt 结构
CREATE TABLE IF NOT EXISTS `t_product_in_opt` (
  `pid` bigint unsigned NOT NULL COMMENT '产品ID',
  `remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `priceremark` varchar(1000) DEFAULT NULL COMMENT '价格公告',
  `buyprice` decimal(10,2) DEFAULT NULL COMMENT '采购单价',
  `businessprice` decimal(10,2) DEFAULT NULL COMMENT '销售价格',
  `businesstype` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '价格类型',
  `businesslist` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '价格',
  `disable` bit(1) DEFAULT b'0' COMMENT '隐藏',
  `presales` int DEFAULT NULL COMMENT '手动输入的预估销量',
  `lastupdate` datetime DEFAULT NULL COMMENT '更新时间',
  `remark_analysis` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `msku` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '本地SKU',
  `fnsku` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'FNSKU',
  `review_daily_refresh` int DEFAULT NULL COMMENT '评论刷新时间',
  `profitid` bigint unsigned DEFAULT NULL COMMENT '对应',
  `status` int unsigned DEFAULT NULL COMMENT '产品状态 0备货 1维持 2提升 3促销  4停售 5清仓 6删除',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`pid`),
  KEY `Index 5` (`disable`),
  KEY `msku` (`msku`),
  KEY `idx_msku_disable_status` (`msku`,`disable`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_order 结构
CREATE TABLE IF NOT EXISTS `t_product_in_order` (
  `pid` bigint unsigned NOT NULL COMMENT '产品ID',
  `avgsales` int DEFAULT NULL COMMENT '平均销量',
  `oldavgsales` int DEFAULT NULL COMMENT '旧平均销量',
  `daynum` int DEFAULT NULL COMMENT '天数',
  `sales_week` int DEFAULT NULL COMMENT 'sales_week,往前推2天之后的7日销量',
  `price_week` decimal(10,2) DEFAULT NULL COMMENT '销售额',
  `sales_month` int DEFAULT NULL COMMENT '30日销量',
  `order_week` int DEFAULT NULL COMMENT '周订单量',
  `order_month` int DEFAULT NULL COMMENT '月订单量',
  `changeRate` decimal(10,2) DEFAULT NULL COMMENT '销量上升或者下降比率',
  `lastupdate` datetime DEFAULT NULL COMMENT '更新时间',
  `sales_seven` int DEFAULT NULL COMMENT 'sales_seven,7日销量',
  `sales_fifteen` int DEFAULT NULL COMMENT 'sales_fifteen,15日销量',
  `rank` int DEFAULT NULL COMMENT '排名',
  PRIMARY KEY (`pid`),
  KEY `lastupdate` (`lastupdate`),
  KEY `sales_month` (`sales_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='产品信息的订单销售数据';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_presale 结构
CREATE TABLE IF NOT EXISTS `t_product_in_presale` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `date` date DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `start` date DEFAULT NULL,
  `end` date DEFAULT NULL,
  `month` char(10) DEFAULT NULL,
  `hasdaysales` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_bydate` (`sku`,`marketplaceid`,`groupid`,`date`) USING BTREE,
  KEY `index_date_event` (`start`,`end`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_profit 结构
CREATE TABLE IF NOT EXISTS `t_product_in_profit` (
  `pid` bigint unsigned NOT NULL COMMENT '产品ID',
  `profit_week` decimal(14,6) DEFAULT NULL,
  `margin_week` decimal(10,2) DEFAULT NULL,
  `margin` decimal(10,2) DEFAULT NULL,
  `planName` char(50) DEFAULT NULL,
  `dimensions` char(100) DEFAULT NULL,
  `weight` char(50) DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  `shipmentfee` decimal(10,2) DEFAULT NULL,
  `othersfee` decimal(10,2) DEFAULT NULL,
  `costDetail` varchar(2000) DEFAULT '',
  PRIMARY KEY (`pid`),
  KEY `lastupdate` (`lastupdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='产品信息的利润信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_review 结构
CREATE TABLE IF NOT EXISTS `t_product_in_review` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `asin` char(15) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_review_detail 结构
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
  `review_body` varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `review_image_tile` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `video_img_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `video_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `buyer_size_color_num` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `verified_text` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `helpful_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `refreshorder` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `reviewid_asin_marketplaceid` (`asin`,`marketplaceid`,`reviewid`) USING BTREE,
  KEY `review_star_rating` (`marketplaceid`,`review_star_rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_review_order 结构
CREATE TABLE IF NOT EXISTS `t_product_in_review_order` (
  `amazonauthid` bigint unsigned NOT NULL,
  `purchase_date` datetime NOT NULL,
  `orderid` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `pid` bigint unsigned DEFAULT NULL,
  `reviewid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `email` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sales_channel` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `review_star_rating` float DEFAULT NULL,
  `review_title` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `review_date` date DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`,`purchase_date`,`orderid`) USING BTREE,
  KEY `marketplaceid` (`marketplaceid`,`sku`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_review_runs 结构
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
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shopid_asin` (`shopid`) USING BTREE,
  KEY `asin_marketplaceid` (`amazonauthid`,`marketplaceid`,`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_ses 结构
CREATE TABLE IF NOT EXISTS `t_product_in_ses` (
  `pid` bigint unsigned NOT NULL COMMENT '产品ID',
  `session_day7` decimal(14,4) DEFAULT NULL,
  `session_rate7` decimal(14,4) DEFAULT NULL,
  `buybox_rate7` decimal(14,4) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_sys 结构
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
  `costDetail` varchar(2000) DEFAULT '',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_price 结构
CREATE TABLE IF NOT EXISTS `t_product_price` (
  `id` bigint unsigned NOT NULL,
  `MarketplaceId` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `asin` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `ptype` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `landed_amount` decimal(10,2) DEFAULT NULL,
  `landed_currency` char(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `listing_amount` decimal(10,2) DEFAULT NULL,
  `listing_currency` char(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `shipping_amount` decimal(10,2) DEFAULT NULL,
  `shipping_currency` char(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `isnewest` bit(1) DEFAULT NULL,
  `fulfillmentChannel` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `itemCondition` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `itemSubCondition` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SellerId` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SellerSKU` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`SellerId`,`MarketplaceId`,`SellerSKU`,`ptype`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='产品价格信息表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_price_his 结构
CREATE TABLE IF NOT EXISTS `t_product_price_his` (
  `id` bigint unsigned NOT NULL,
  `MarketplaceId` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `asin` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `ptype` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `landed_amount` decimal(10,2) DEFAULT NULL,
  `landed_currency` char(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `listing_amount` decimal(10,2) DEFAULT NULL,
  `listing_currency` char(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `shipping_amount` decimal(10,2) DEFAULT NULL,
  `shipping_currency` char(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `isnewest` bit(1) DEFAULT NULL,
  `fulfillmentChannel` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `itemCondition` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `itemSubCondition` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SellerId` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SellerSKU` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`SellerId`,`MarketplaceId`,`isnewest`,`ptype`,`asin`,`byday`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='产品价格信息表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_price_locked 结构
CREATE TABLE IF NOT EXISTS `t_product_price_locked` (
  `pid` bigint unsigned NOT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  `disable` bit(1) NOT NULL DEFAULT b'0',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_rank 结构
CREATE TABLE IF NOT EXISTS `t_product_rank` (
  `id` bigint unsigned NOT NULL,
  `byday` datetime DEFAULT NULL,
  `categoryId` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `rank` int DEFAULT NULL,
  `product_id` bigint unsigned DEFAULT NULL,
  `isMain` bit(1) DEFAULT NULL,
  `isNewest` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `FK_t_ranklist_t_produ` (`product_id`,`categoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_rank_his 结构
CREATE TABLE IF NOT EXISTS `t_product_rank_his` (
  `id` bigint unsigned NOT NULL,
  `byday` date DEFAULT NULL,
  `categoryId` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `rank` int DEFAULT NULL,
  `product_id` bigint unsigned DEFAULT NULL,
  `isMain` bit(1) DEFAULT NULL,
  `isNewest` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_ranklist_t_produ` (`product_id`),
  KEY `Index 3` (`byday`),
  KEY `categoryId` (`categoryId`,`byday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_rank_sales_his 结构
CREATE TABLE IF NOT EXISTS `t_product_rank_sales_his` (
  `id` int NOT NULL AUTO_INCREMENT,
  `market` char(5) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '0',
  `byday` date NOT NULL DEFAULT '0000-00-00',
  `ordersum` int NOT NULL DEFAULT '0',
  `rank` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品当天在一级大类排名对应产品当天的ordersum';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_recommended 结构
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `amazonAuthId_marketplaceid_asin` (`amazonAuthId`,`marketplaceid`,`asin`),
  KEY `asin` (`asin`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_recommended_ext 结构
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
  PRIMARY KEY (`rid`) USING BTREE,
  KEY `asin` (`asin`),
  KEY `profit` (`profit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_remark_his 结构
CREATE TABLE IF NOT EXISTS `t_product_remark_his` (
  `pid` bigint unsigned NOT NULL,
  `ftype` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `opttime` datetime NOT NULL,
  `operator` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`pid`,`ftype`,`opttime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='产品备注历史表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_type 结构
CREATE TABLE IF NOT EXISTS `t_product_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '类型',
  `country` varchar(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL COMMENT '国家',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='成本计算用，的亚马逊成本。采用父分类的方式，将所有国家的分类都放入此表。\r\n以美国的分类作为主要分类。如果美国没有此分类，则该分类依旧为美国添加，使用其他分类 类型的策略。\r\n当用户选择1 号分类，则其他子分类即改分类对应的其他国家的分类。\r\n ';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_usercategory 结构
CREATE TABLE IF NOT EXISTS `t_product_usercategory` (
  `id` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `name` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_profitcfg 结构
CREATE TABLE IF NOT EXISTS `t_profitcfg` (
  `id` bigint unsigned NOT NULL COMMENT 'ID 用于区分每一个方案',
  `shop_id` bigint unsigned DEFAULT NULL COMMENT '添加方案的人，只能当事人或其部下使用',
  `isSystem` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否系统内置方案',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '名称',
  `sales_channel` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '配送方案，是否亚马逊配送',
  `sellerPlan` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '销售计划',
  `shipmentStyle` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '运费计算方式',
  `isDefault` bit(1) DEFAULT b'0' COMMENT '是否为默认方案',
  `opttime` datetime DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='利润计算方案';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_profitcfgcountry 结构
CREATE TABLE IF NOT EXISTS `t_profitcfgcountry` (
  `id` bigint unsigned NOT NULL COMMENT 'ID 用于区分每一个方案对应不同国家的方案配置项',
  `profitid` bigint unsigned NOT NULL COMMENT '总方案',
  `country` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '国家',
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
  `logistics` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '物流方式',
  `subscriptionfee` decimal(10,2) DEFAULT NULL COMMENT '订阅费',
  `prepservice` bit(1) DEFAULT b'0' COMMENT '计划内的服务费',
  `labelService` bit(1) DEFAULT b'0' COMMENT '亚马逊标签服务费',
  `manualProcessing` decimal(10,2) DEFAULT '0.00' COMMENT '亚马逊手动处理费',
  `unprepservice` decimal(10,2) DEFAULT '0.00' COMMENT '计划外的服务费',
  `invplacefee` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '库存配置费',
  `promotion` decimal(10,2) DEFAULT '0.00' COMMENT 'all store promtion',
  `amonth` int DEFAULT '1' COMMENT '亚马逊仓储费，库存周期',
  `hasAddedSite` bit(1) DEFAULT b'0' COMMENT '当配送方案为Pan EU时，Germany是否添加Poland, Czech Republic仓库站点',
  `warehouse_site` char(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '当配送方案为EFN时，亚马逊仓库站点',
  `dispatch_type` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '亚马逊配送方案:pan_EU,EFN',
  `vat_rate` decimal(10,2) DEFAULT '0.00' COMMENT 'VAT增值税费率',
  `fba_taxes` decimal(10,2) DEFAULT '0.00' COMMENT 'FBA GST/HST taxes',
  `hasDeclaredValue` bit(1) DEFAULT b'0' COMMENT '是否单独输入申报价值',
  `declared_value` decimal(10,4) DEFAULT '0.0000' COMMENT '申报价值',
  `gst_rate` decimal(10,2) DEFAULT '0.00' COMMENT '进口GST税率',
  `selling_GSTRate` decimal(10,2) DEFAULT '0.00' COMMENT '销售GST税率',
  `corporate_InRate` decimal(10,2) DEFAULT '0.00' COMMENT '企业所得税率',
  PRIMARY KEY (`id`),
  KEY `FK_t_profitcfgcountry_t_profitcfg` (`profitid`),
  KEY `country` (`country`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='利润各国计算方案';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_pro_rcd_dimensions 结构
CREATE TABLE IF NOT EXISTS `t_pro_rcd_dimensions` (
  `id` bigint unsigned NOT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `length_units` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `width_units` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `height_units` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `weight_units` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_referralfee 结构
CREATE TABLE IF NOT EXISTS `t_referralfee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '类型',
  `isMedia` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '是否媒介',
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
  KEY `country` (`country`),
  KEY `parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=680 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='成本计算用，的亚马逊成本。采用父分类的方式，将所有国家的分类都放入此表。\r\n以美国的分类作为主要分类。如果美国没有此分类，则该分类依旧为美国添加，使用其他分类 类型的策略。\r\n当用户选择1 号分类，则其他子分类即改分类对应的其他国家的分类。\r\n ';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_report_requestrecord 结构
CREATE TABLE IF NOT EXISTS `t_report_requestrecord` (
  `id` bigint unsigned NOT NULL,
  `sellerid` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `marketPlaceId` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `reportId` bigint unsigned DEFAULT NULL,
  `reportType` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `reportRequestId` bigint unsigned DEFAULT NULL,
  `reportDocumentId` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `isnewest` bit(1) DEFAULT NULL,
  `availableDate` datetime DEFAULT NULL,
  `getnumber` int DEFAULT '0',
  `treatnumber` int DEFAULT '0',
  `lastupdate` datetime DEFAULT NULL,
  `report_processing_status` char(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `log` longtext CHARACTER SET utf8 COLLATE utf8_bin,
  `reportOptions` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sellerid_reportId` (`sellerid`,`reportId`,`reportType`),
  KEY `Index 2` (`reportType`) USING BTREE,
  KEY `index3` (`lastupdate`) USING BTREE,
  KEY `sellerid_marketPlaceId` (`sellerid`,`marketPlaceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role 结构
CREATE TABLE IF NOT EXISTS `t_role` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `type` varchar(10) DEFAULT NULL COMMENT '角色类型',
  `issystem` bit(1) NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`shopid`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_adv_group 结构
CREATE TABLE IF NOT EXISTS `t_role_adv_group` (
  `roleid` bigint unsigned NOT NULL,
  `groupid` bigint unsigned NOT NULL,
  `group_name` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`roleid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_authority 结构
CREATE TABLE IF NOT EXISTS `t_role_authority` (
  `id` bigint unsigned NOT NULL,
  `role_id` bigint unsigned DEFAULT NULL,
  `authority_id` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_role_authority_t_role` (`role_id`),
  KEY `FK_t_role_authority_t_authority` (`authority_id`),
  CONSTRAINT `FK_t_role_authority_t_authority` FOREIGN KEY (`authority_id`) REFERENCES `t_authority` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='角色权限分配表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_group 结构
CREATE TABLE IF NOT EXISTS `t_role_group` (
  `roleid` bigint unsigned NOT NULL,
  `groupid` bigint unsigned NOT NULL,
  `group_name` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`roleid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_marketplace 结构
CREATE TABLE IF NOT EXISTS `t_role_marketplace` (
  `id` bigint unsigned NOT NULL,
  `roleid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`roleid`,`marketplaceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_menu 结构
CREATE TABLE IF NOT EXISTS `t_role_menu` (
  `id` bigint unsigned NOT NULL,
  `role_id` bigint unsigned DEFAULT NULL COMMENT '角色ID',
  `menu_id` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  KEY `FK_t_role_menu_t_role` (`role_id`),
  KEY `FK_t_role_menu_t_menu` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='角色菜单分配表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_menu_bkp20211115 结构
CREATE TABLE IF NOT EXISTS `t_role_menu_bkp20211115` (
  `id` bigint unsigned NOT NULL,
  `role_id` bigint unsigned DEFAULT NULL COMMENT '角色ID',
  `menu_id` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  KEY `FK_t_role_menu_t_role` (`role_id`),
  KEY `FK_t_role_menu_t_menu` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='角色菜单分配表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_tag 结构
CREATE TABLE IF NOT EXISTS `t_role_tag` (
  `id` bigint unsigned NOT NULL,
  `tag_id` char(36) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `roleid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`tag_id`,`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='管理员新建，用于给不同下属分配不同的产品查询权限';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_taggroup 结构
CREATE TABLE IF NOT EXISTS `t_role_taggroup` (
  `id` bigint unsigned NOT NULL,
  `roleid` bigint unsigned DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`roleid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_shop 结构
CREATE TABLE IF NOT EXISTS `t_shop` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '公司名称',
  `remark` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `invitecode` char(7) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '邀请码',
  `fromcode` char(7) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '受邀请码',
  `oldid` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `boss_email` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `invitecode` (`invitecode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='店铺';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_summaryall 结构
CREATE TABLE IF NOT EXISTS `t_summaryall` (
  `id` bigint unsigned NOT NULL,
  `amazonauthid` bigint unsigned NOT NULL,
  `purchase_date` date NOT NULL,
  `sales_channel` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `order_status` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `fulfillChannel` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '交付渠道',
  `discount` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `is_business_order` char(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `quantity` decimal(32,0) DEFAULT NULL,
  `ordernumber` bigint unsigned NOT NULL DEFAULT '0',
  `discount_amount` decimal(10,2) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`),
  KEY `index_1` (`amazonauthid`,`sales_channel`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_app_store_company 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_app_store_detail 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_app_store_group 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_app_store_service_detail 结构
CREATE TABLE IF NOT EXISTS `t_sys_app_store_service_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `detailid` int DEFAULT NULL,
  `content` longtext COMMENT '服务详情',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `detailid` (`detailid`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_channel_salesperson_key 结构
CREATE TABLE IF NOT EXISTS `t_sys_channel_salesperson_key` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `salesperson` char(50) NOT NULL DEFAULT '0',
  `fkey` char(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_contact 结构
CREATE TABLE IF NOT EXISTS `t_sys_contact` (
  `id` bigint unsigned NOT NULL,
  `name` char(50) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `message` varchar(500) DEFAULT NULL,
  `operatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_customer_discount 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_discount` (
  `id` bigint unsigned NOT NULL,
  `number` char(7) DEFAULT NULL COMMENT '折扣编码',
  `ftype` char(10) DEFAULT NULL,
  `packages` int DEFAULT NULL,
  `amount` decimal(10,2) unsigned DEFAULT NULL COMMENT '折扣金额',
  `account` char(50) DEFAULT NULL COMMENT '指定应用帐户（可以不填）',
  `pkgtime` int DEFAULT NULL,
  `isused` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否应用',
  `orderid` char(50) DEFAULT NULL COMMENT '应用订单',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '应用公司',
  `createdate` datetime DEFAULT NULL COMMENT '创建时间',
  `lostime` datetime DEFAULT NULL COMMENT '失效时间',
  `operator` varchar(36) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `number` (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户折扣表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_customer_invoice 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_customer_order 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_order` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `ftype` char(20) DEFAULT NULL COMMENT '订单类型  package:套餐  append:附加包',
  `out_trade_no` char(20) DEFAULT NULL COMMENT '商户订单号',
  `subject` char(50) DEFAULT NULL COMMENT '订单名称',
  `trade_no` char(50) DEFAULT NULL COMMENT '支付宝交易号',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '付款金额',
  `discountnumber` char(10) DEFAULT NULL,
  `paytime` timestamp NULL DEFAULT NULL,
  `paystatus` char(50) DEFAULT NULL COMMENT '付款状态',
  `ivcstatus` char(50) DEFAULT NULL COMMENT '开票状态',
  `months` int DEFAULT NULL,
  `tariffpackage` int DEFAULT NULL,
  `pcs` int DEFAULT '1',
  `paytype` char(10) DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `out_trade_no` (`out_trade_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' ';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_customer_order_refund 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_order_refund` (
  `id` bigint unsigned DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `orderid` bigint unsigned DEFAULT NULL,
  `out_trade_no` char(20) DEFAULT NULL COMMENT '商户订单号',
  `trade_no` char(50) DEFAULT NULL COMMENT '支付宝交易号',
  `refund_amount` decimal(10,2) DEFAULT NULL COMMENT '付款金额',
  `refund_reason` char(50) DEFAULT NULL COMMENT '订单名称',
  `out_request_no` char(20) DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT=' ';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_help_page 结构
CREATE TABLE IF NOT EXISTS `t_sys_help_page` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键(h)',
  `menuid` char(36) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`menuid`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='菜单';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_holiday 结构
CREATE TABLE IF NOT EXISTS `t_sys_holiday` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `country` char(3) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `marketplaceid` char(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
  `month` int DEFAULT NULL,
  `value` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`,`month`),
  KEY `marketplaceid` (`marketplaceid`,`month`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_importrecord 结构
CREATE TABLE IF NOT EXISTS `t_sys_importrecord` (
  `id` bigint unsigned NOT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `issuccess` bit(1) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `importtype` char(50) DEFAULT NULL,
  `log` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`shopid`,`importtype`),
  KEY `opttime` (`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_mailsender 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_mail_template 结构
CREATE TABLE IF NOT EXISTS `t_sys_mail_template` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mail_subject` varchar(50) DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `content` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_menu 结构
CREATE TABLE IF NOT EXISTS `t_sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '菜单名称',
  `parent_id` bigint DEFAULT NULL COMMENT '父菜单ID',
  `apppath` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '路由路径',
  `component` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '菜单图标',
  `appicon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '',
  `sort` int DEFAULT '0' COMMENT '排序',
  `visible` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用 1-开启',
  `redirect` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '跳转路径',
  `runui` bit(1) DEFAULT NULL,
  `runapp` bit(1) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  `oldid` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=315 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='菜单管理';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_menu_favorite 结构
CREATE TABLE IF NOT EXISTS `t_sys_menu_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `shopid` bigint NOT NULL DEFAULT '0' COMMENT '公司ID',
  `userid` bigint NOT NULL DEFAULT '0' COMMENT '用户ID',
  `menuid` bigint DEFAULT NULL COMMENT '菜单ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='菜单收藏';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_message_template 结构
CREATE TABLE IF NOT EXISTS `t_sys_message_template` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `ftype` char(10) NOT NULL COMMENT '消息类型',
  `content` varchar(2000) DEFAULT NULL COMMENT '消息内容',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_notify 结构
CREATE TABLE IF NOT EXISTS `t_sys_notify` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '0',
  `content` varchar(2000) DEFAULT NULL,
  `ftype` int NOT NULL COMMENT '消息的类型，1: 公告 Announce，2: 提醒 Remind，3：信息 Message',
  `target` char(100) DEFAULT NULL COMMENT '目标类型',
  `action` char(100) DEFAULT NULL COMMENT '提醒信息的动作类型',
  `sender` bigint unsigned DEFAULT NULL COMMENT '发送者的ID',
  `receiver` bigint unsigned DEFAULT NULL COMMENT '接受者ID',
  `createdAt` datetime DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_notify_shopid` (`ftype`) USING BTREE,
  KEY `shopid` (`shopid`),
  KEY `target` (`target`),
  KEY `createdAt` (`createdAt`)
) ENGINE=InnoDB AUTO_INCREMENT=483199 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_operationlog 结构
CREATE TABLE IF NOT EXISTS `t_sys_operationlog` (
  `id` bigint unsigned NOT NULL,
  `time` datetime DEFAULT NULL,
  `ip` char(35) DEFAULT NULL,
  `userid` bigint unsigned DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `logType` varchar(255) DEFAULT NULL,
  `method` char(100) DEFAULT NULL,
  `exceptionDetail` char(50) DEFAULT NULL,
  `param` varchar(4000) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `time` (`userid`,`method`,`time`) USING BTREE,
  KEY `idx_method_time_userid` (`method`,`time`,`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_permission 结构
CREATE TABLE IF NOT EXISTS `t_sys_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) DEFAULT NULL COMMENT '权限名称',
  `menu_id` int DEFAULT NULL COMMENT '菜单模块ID\r\n',
  `url_perm` varchar(128) DEFAULT NULL COMMENT 'URL权限标识',
  `btn_perm` varchar(64) DEFAULT NULL COMMENT '按钮权限标识',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id` (`id`,`name`) USING BTREE,
  KEY `id_2` (`id`,`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='权限表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_potential_customer 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=38005 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_quartz_task 结构
CREATE TABLE IF NOT EXISTS `t_sys_quartz_task` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `fgroup` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `cron` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `parameter` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `path` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_query_field 结构
CREATE TABLE IF NOT EXISTS `t_sys_query_field` (
  `fquery` char(20) NOT NULL,
  `ffield` char(30) NOT NULL,
  `title` char(50) DEFAULT NULL,
  `titleTooltip` char(50) DEFAULT NULL,
  `width` char(50) DEFAULT NULL,
  `findex` int DEFAULT NULL,
  `formatter` char(50) DEFAULT NULL,
  `footerFormatter` char(50) DEFAULT NULL,
  `sortable` bit(1) DEFAULT NULL,
  `valign` char(10) DEFAULT NULL,
  `align` char(10) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`fquery`,`ffield`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_query_user_version 结构
CREATE TABLE IF NOT EXISTS `t_sys_query_user_version` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint unsigned DEFAULT NULL,
  `fquery` char(20) DEFAULT NULL,
  `isused` bit(1) DEFAULT NULL,
  `name` char(50) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`,`fquery`)
) ENGINE=InnoDB AUTO_INCREMENT=123456944 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_query_version_feild 结构
CREATE TABLE IF NOT EXISTS `t_sys_query_version_feild` (
  `fversionid` bigint unsigned NOT NULL,
  `ffield` char(30) NOT NULL,
  `findex` int NOT NULL,
  PRIMARY KEY (`fversionid`,`ffield`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_role_menu 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色和菜单关联表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_role_permission 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_permission` (
  `role_id` bigint DEFAULT NULL COMMENT '角色id',
  `permission_id` bigint DEFAULT NULL COMMENT '资源id',
  KEY `role_id` (`role_id`) USING BTREE,
  KEY `permission_id` (`permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色权限表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_subscription 结构
CREATE TABLE IF NOT EXISTS `t_sys_subscription` (
  `target` char(2) NOT NULL,
  `userid` bigint unsigned NOT NULL,
  `action` char(100) DEFAULT NULL COMMENT '订阅动作',
  `disable` bit(1) NOT NULL DEFAULT b'0',
  `createdAt` datetime DEFAULT NULL,
  PRIMARY KEY (`target`,`userid`),
  CONSTRAINT `FK_t_sys_subscription_t_sys_target` FOREIGN KEY (`target`) REFERENCES `t_sys_target` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_target 结构
CREATE TABLE IF NOT EXISTS `t_sys_target` (
  `id` char(2) NOT NULL,
  `name` char(50) NOT NULL,
  `description` char(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_tariff_packages 结构
CREATE TABLE IF NOT EXISTS `t_sys_tariff_packages` (
  `id` int NOT NULL COMMENT '套餐id 0-基础版，1-标准版，2-专业版，3-独享版,4-自定义',
  `name` char(36) NOT NULL COMMENT '套餐名字',
  `roleId` bigint unsigned NOT NULL COMMENT '角色id',
  `maxShopCount` int DEFAULT '1' COMMENT '绑定店铺数量',
  `maxProductCount` int DEFAULT '50000' COMMENT '商品数量上限',
  `maxOrderCount` int DEFAULT '3600' COMMENT '处理订单上限',
  `maxMember` int DEFAULT '10' COMMENT '子用户数量上限',
  `maxProfitPlanCount` int DEFAULT '1' COMMENT '利润计算方案数量',
  `maxMarketCount` int DEFAULT '1' COMMENT '每个店铺绑定站点数量',
  `orderMemoryCount` char(10) NOT NULL DEFAULT '3' COMMENT '订单存储时间 单位:月  默认基础版是3个月',
  `dayOpenAdvCount` int NOT NULL DEFAULT '10' COMMENT '每天开启广告组数量',
  `controlProductCount` int NOT NULL DEFAULT '10' COMMENT '跟卖监控产品数量',
  `anysisProductCount` int NOT NULL DEFAULT '10' COMMENT '商品分析数量',
  `yearprice` decimal(18,2) DEFAULT NULL,
  `monthprice` decimal(18,2) DEFAULT NULL,
  `lastUpdateTime` date DEFAULT NULL COMMENT '最后更新时间',
  `lastUpdateUser` varchar(36) DEFAULT NULL COMMENT '最后更新的人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='套餐表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_tariff_packages_append 结构
CREATE TABLE IF NOT EXISTS `t_sys_tariff_packages_append` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ftype` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `units` int DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_tariff_packages_append_discount 结构
CREATE TABLE IF NOT EXISTS `t_sys_tariff_packages_append_discount` (
  `appendid` int DEFAULT NULL,
  `packages` int DEFAULT NULL,
  `month` int DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  UNIQUE KEY `appendid_packages_month` (`appendid`,`packages`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_timetask 结构
CREATE TABLE IF NOT EXISTS `t_sys_timetask` (
  `id` char(36) NOT NULL COMMENT 'id',
  `name` varchar(50) DEFAULT NULL COMMENT '任务名',
  `group_name` varchar(50) DEFAULT NULL COMMENT '任务组',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `cron` varchar(30) DEFAULT NULL COMMENT '表达式',
  `job_status` varchar(20) DEFAULT NULL COMMENT '发布状态',
  `plan_status` varchar(20) DEFAULT NULL COMMENT '计划状态',
  `is_concurrent` int DEFAULT NULL COMMENT '运行状态',
  `job_data` longtext COMMENT '参数',
  `menthod_name` varchar(200) DEFAULT NULL COMMENT '方法',
  `bean_name` varchar(200) DEFAULT NULL COMMENT '实例bean',
  `description` varchar(1000) DEFAULT NULL COMMENT '任务描述',
  `creator` bigint unsigned DEFAULT NULL COMMENT '创建人',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_timetask_log 结构
CREATE TABLE IF NOT EXISTS `t_sys_timetask_log` (
  `id` char(36) NOT NULL,
  `createdate` datetime DEFAULT NULL,
  `job_id` char(36) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_usernotify 结构
CREATE TABLE IF NOT EXISTS `t_sys_usernotify` (
  `userid` bigint unsigned NOT NULL COMMENT '用户消息所属者',
  `notify` int NOT NULL,
  `isRead` bit(1) NOT NULL DEFAULT b'0' COMMENT '0，代表未读；1，代表已读',
  `createdAt` datetime DEFAULT NULL,
  PRIMARY KEY (`userid`,`notify`),
  KEY `Index 2` (`notify`),
  KEY `createdAt` (`isRead`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_user_ip_city 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_ip_city` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `userip` char(50) NOT NULL DEFAULT '0',
  `city` char(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userip` (`userip`)
) ENGINE=InnoDB AUTO_INCREMENT=1385 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_video 结构
CREATE TABLE IF NOT EXISTS `t_sys_video` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `menuid` char(36) DEFAULT NULL,
  `video_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `video_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_weather 结构
CREATE TABLE IF NOT EXISTS `t_sys_weather` (
  `id` bigint unsigned NOT NULL,
  `city` varchar(20) NOT NULL COMMENT '城市',
  `weatype` varchar(20) DEFAULT NULL COMMENT '值:多云 晴 小雨 大雨',
  `updatedate` datetime DEFAULT NULL COMMENT '当前日期的天气',
  `nowdegree` int DEFAULT NULL COMMENT '当前温度 ''C',
  `lowdegree` varchar(50) DEFAULT NULL COMMENT '最低温度',
  `highdegree` varchar(50) DEFAULT NULL COMMENT '最高温度',
  `weaforce` varchar(50) DEFAULT NULL COMMENT '风向',
  PRIMARY KEY (`id`),
  UNIQUE KEY `city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='城市天气表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_tag 结构
CREATE TABLE IF NOT EXISTS `t_tag` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `groupid` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `description` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `lastone` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `lasttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_tag_t_sho` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_tag_group 结构
CREATE TABLE IF NOT EXISTS `t_tag_group` (
  `id` bigint unsigned NOT NULL,
  `grouptype` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `name` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `shop_id` bigint unsigned DEFAULT NULL,
  `description` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `lastone` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `lasttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_tag_group_t_shop` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_tasklock 结构
CREATE TABLE IF NOT EXISTS `t_tasklock` (
  `task` char(15) NOT NULL,
  `mylock` bit(1) DEFAULT NULL,
  PRIMARY KEY (`task`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_temp_purchase_form 结构
CREATE TABLE IF NOT EXISTS `t_temp_purchase_form` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` bigint unsigned NOT NULL COMMENT '整型自增主键',
  `account` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` char(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL COMMENT '用户密码，采用MD5加密',
  `salt` char(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `leader_id` bigint unsigned DEFAULT NULL COMMENT '上层',
  `createDate` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `losingEffect` date DEFAULT NULL COMMENT '失效时间',
  `logicDelete` bit(1) DEFAULT b'0' COMMENT '逻辑删除',
  `disable` bit(1) DEFAULT b'0' COMMENT '停用',
  `isActive` bit(1) DEFAULT b'1' COMMENT '账户是否激活',
  `hasEmail` bit(1) DEFAULT b'0' COMMENT '邮箱是否激活',
  `member` int DEFAULT '5' COMMENT '拥有下属数量上限',
  `passwordkey` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `lastlogintime` datetime DEFAULT NULL,
  `lastloginip` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `lastloginsession` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ftype` char(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `leader_id` (`leader_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_userinfo 结构
CREATE TABLE IF NOT EXISTS `t_userinfo` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `picture` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `tel` varchar(25) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `company` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_userinfo_t_picture` (`picture`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='用户详细信息表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_user_role 结构
CREATE TABLE IF NOT EXISTS `t_user_role` (
  `id` bigint unsigned NOT NULL,
  `user_id` bigint unsigned DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint unsigned DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 4` (`user_id`,`role_id`),
  KEY `FK_t_user_role_t_user` (`user_id`),
  KEY `FK_t_user_role_t_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='用户角色分配表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_user_shop 结构
CREATE TABLE IF NOT EXISTS `t_user_shop` (
  `id` bigint unsigned NOT NULL,
  `user_id` bigint unsigned DEFAULT NULL COMMENT '用户ID',
  `shop_id` bigint unsigned DEFAULT NULL COMMENT '店铺ID',
  PRIMARY KEY (`id`),
  KEY `FK_t_user_shop_t_user` (`user_id`),
  KEY `FK_t_user_shop_t_shop` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='用户店铺归属表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_user_wechat_login 结构
CREATE TABLE IF NOT EXISTS `t_user_wechat_login` (
  `openid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `userid` bigint unsigned NOT NULL,
  `unionid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `access_token` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refresh_token` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`openid`) USING BTREE,
  UNIQUE KEY `userid` (`userid`) USING BTREE,
  UNIQUE KEY `unionid` (`unionid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_user_wechat_mp 结构
CREATE TABLE IF NOT EXISTS `t_user_wechat_mp` (
  `openid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `userid` bigint unsigned NOT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `access_token` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `refresh_token` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`openid`,`userid`,`ftype`),
  KEY `openid_userid_ftype` (`userid`,`ftype`,`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_variable_closing_fee 结构
CREATE TABLE IF NOT EXISTS `t_variable_closing_fee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `country` char(3) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `typeid` int DEFAULT NULL,
  `isMedia` char(1) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `logisticsId` char(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '0',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `format` char(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '0.00',
  PRIMARY KEY (`id`),
  KEY `FK_t_logistics_t_referralfee` (`typeid`),
  KEY `country` (`country`),
  CONSTRAINT `FK_t_logistics_t_referralfee` FOREIGN KEY (`typeid`) REFERENCES `t_referralfee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='物流方式';

-- 数据导出被取消选择。

-- 导出  事件 db_wimoor.wimoorerp_timeout_process_kill 结构
DELIMITER //
CREATE EVENT `wimoorerp_timeout_process_kill` ON SCHEDULE EVERY 1 MINUTE STARTS '2020-12-01 10:05:39' ON COMPLETION PRESERVE ENABLE DO BEGIN
declare v_sql varchar(500);
declare no_more_long_running_trx integer default 0;

declare c_tid cursor for
select concat ('kill ',trx_mysql_thread_id,';')
from information_schema.innodb_trx
where timestampdiff(minute,trx_started,NOW()) >= 2
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

END//
DELIMITER ;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

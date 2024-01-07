
-- 导出  表 db_amazon_adv.t_advert_warning_keywords_data 结构
CREATE TABLE IF NOT EXISTS `t_advert_warning_keywords_data` (
  `keywordid` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `ftype` char(10) NOT NULL,
  `groupName` char(50) DEFAULT NULL,
  `market` char(10) DEFAULT NULL,
  `campaignName` char(200) DEFAULT NULL,
  `adGroupName` varchar(500) DEFAULT NULL,
  `keywordText` char(200) DEFAULT NULL,
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
  KEY `shopid` (`shopid`) USING BTREE,
  KEY `ftype` (`ftype`) USING BTREE
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_advert_warning_product_data 结构
CREATE TABLE IF NOT EXISTS `t_advert_warning_product_data` (
  `adid` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `ftype` char(10) NOT NULL,
  `groupName` char(50) DEFAULT NULL,
  `market` char(10) DEFAULT NULL,
  `campaignName` char(200) DEFAULT NULL,
  `adGroupName` varchar(500) DEFAULT NULL,
  `sku` char(200) DEFAULT NULL,
  `asin` char(20) DEFAULT NULL,
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
  KEY `shopid` (`shopid`) USING BTREE,
  KEY `ftype` (`ftype`) USING BTREE
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_browsenode 结构
CREATE TABLE IF NOT EXISTS `t_adv_browsenode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(255) DEFAULT NULL,
  `node_path` varchar(255) DEFAULT NULL,
  `query` varchar(255) DEFAULT NULL,
  `refinement` varchar(255) DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_dimensions 结构
CREATE TABLE IF NOT EXISTS `t_adv_dimensions` (
  `id` char(36) NOT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `length_units` char(20) DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `width_units` char(20) DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `height_units` char(20) DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `weight_units` char(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_productgroup 结构
CREATE TABLE IF NOT EXISTS `t_adv_productgroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `referralfeeId` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_rank 结构
CREATE TABLE IF NOT EXISTS `t_adv_rank` (
  `id` char(36) NOT NULL,
  `asin` char(10) NOT NULL,
  `nodeid` char(50) DEFAULT NULL,
  `pricerange` char(100) DEFAULT NULL,
  `dim` char(36) DEFAULT NULL,
  `offerprice` int(10) DEFAULT NULL,
  `listprice` char(10) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `lowestnprice` char(10) DEFAULT NULL,
  `categoryrank` int(11) DEFAULT NULL,
  `imageurl` varchar(100) DEFAULT NULL,
  `reviewsURL` varchar(300) DEFAULT NULL,
  `totalOfferNew` int(11) DEFAULT NULL,
  `lastupdate` datetime NOT NULL,
  `reviews` int(11) DEFAULT NULL,
  `reviewAverage` float(5,1) DEFAULT NULL,
  `productgroup` varchar(50) DEFAULT NULL,
  `marketplaceid` char(32) DEFAULT NULL,
  `estiMargin` float DEFAULT NULL,
  `estiProfit` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`asin`),
  KEY `Index 3` (`productgroup`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_adv_rank_his 结构
CREATE TABLE IF NOT EXISTS `t_adv_rank_his` (
  `id` char(36) NOT NULL,
  `asin` char(10) NOT NULL,
  `nodeid` char(50) DEFAULT NULL,
  `pricerange` char(100) DEFAULT NULL,
  `dim` char(36) DEFAULT NULL,
  `offerprice` int(10) DEFAULT NULL,
  `listprice` char(10) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `lowestnprice` char(10) DEFAULT NULL,
  `categoryrank` int(11) DEFAULT NULL,
  `imageurl` varchar(100) DEFAULT NULL,
  `reviewsURL` varchar(300) DEFAULT NULL,
  `totalOfferNew` int(11) DEFAULT NULL,
  `lastupdate` datetime NOT NULL,
  `reviews` int(11) DEFAULT NULL,
  `reviewAverage` float DEFAULT NULL,
  `productgroup` varchar(50) DEFAULT NULL,
  `marketplaceid` char(32) DEFAULT NULL,
  `estiMargin` float DEFAULT NULL,
  `estiProfit` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`asin`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amazon_auth_market_performance 结构
CREATE TABLE IF NOT EXISTS `t_amazon_auth_market_performance` (
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(20) CHARACTER SET latin1 NOT NULL,
  `accountstatus` varchar(20) DEFAULT NULL,
  `sellerid` char(20) CHARACTER SET latin1 DEFAULT NULL,
  `performance` varchar(6000) CHARACTER SET latin1 DEFAULT NULL,
  `refreshtime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`,`marketplaceid`) USING BTREE
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amazon_group 结构
CREATE TABLE IF NOT EXISTS `t_amazon_group` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(200) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `profitcfgid` bigint(20) unsigned DEFAULT NULL COMMENT '店铺默认利润方案',
  `findex` int(10) unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `name_shopid` (`shopid`,`name`(191)) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_advert_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report` (
  `id` char(36) NOT NULL,
  `sellerid` char(15) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `sku` char(50) NOT NULL,
  `adId` bigint(20) DEFAULT NULL,
  `asin` char(20) DEFAULT NULL,
  `bydate` date NOT NULL,
  `campaign_name` char(128) NOT NULL,
  `ad_Group_Name` char(128) NOT NULL,
  `clicks` int(11) DEFAULT NULL,
  `impressions` int(11) NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) DEFAULT NULL,
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_advert_report_summary 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report_summary` (
  `sellerid` char(15) NOT NULL,
  `id` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `ctype` char(2) NOT NULL DEFAULT 'sp',
  `sku` char(50) NOT NULL,
  `asin` char(20) DEFAULT NULL,
  `bydate` date NOT NULL,
  `clicks` int(11) DEFAULT NULL,
  `impressions` int(11) NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) DEFAULT NULL,
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_advert_report_summary_month 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report_summary_month` (
  `sellerid` char(15) NOT NULL,
  `id` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `ctype` char(2) NOT NULL DEFAULT 'sp',
  `sku` char(50) NOT NULL,
  `asin` char(20) DEFAULT NULL,
  `bydate` date NOT NULL,
  `clicks` int(11) DEFAULT NULL,
  `impressions` int(11) NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) DEFAULT NULL,
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_advert_report_summary_week 结构
CREATE TABLE IF NOT EXISTS `t_amz_advert_report_summary_week` (
  `sellerid` char(15) NOT NULL,
  `id` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `ctype` char(2) NOT NULL DEFAULT 'sp',
  `sku` char(50) NOT NULL,
  `asin` char(20) DEFAULT NULL,
  `bydate` date NOT NULL,
  `clicks` int(11) DEFAULT NULL,
  `impressions` int(11) NOT NULL,
  `ctr` double DEFAULT NULL COMMENT 'Click-Thru Rate (CTR)	',
  `currency` char(50) DEFAULT NULL,
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='商品广告';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_adgroups 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  `state` char(50) DEFAULT NULL,
  `defaultBid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`),
  KEY `profileid_name` (`name`(191)),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_adgroups_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups_hsa` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  `state` char(20) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_adgroups_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups_sd` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  `state` char(50) DEFAULT NULL,
  `bidOptimization` char(20) DEFAULT NULL,
  `creativeType` char(20) DEFAULT NULL,
  `defaultBid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`),
  KEY `profileid_name` (`name`(191)),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_ads_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_ads_hsa` (
  `adId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  `state` char(20) DEFAULT NULL,
  `creative` varchar(2000) DEFAULT NULL,
  `landingPage` varchar(2000) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adId`) USING BTREE,
  KEY `campaignId` (`campaignId`) USING BTREE,
  KEY `profileid` (`profileid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_api_pages 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_api_pages` (
  `profileid` bigint(20) unsigned NOT NULL,
  `apipath` char(100) NOT NULL,
  `pages` int(11) DEFAULT NULL,
  `nexttoken` varchar(2000) DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `flog` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`profileid`,`apipath`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_assets 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_assets` (
  `assetId` varchar(100) NOT NULL,
  `brandEntityId` varchar(30) NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `mediaType` varchar(10) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  `opptime` datetime DEFAULT NULL,
  PRIMARY KEY (`profileid`,`brandEntityId`,`assetId`) USING BTREE
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_auth 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_auth` (
  `id` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `code` char(36) DEFAULT NULL,
  `region` char(2) DEFAULT NULL,
  `access_token` varchar(800) DEFAULT NULL,
  `refresh_token` varchar(800) DEFAULT NULL,
  `token_type` char(20) DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `disableTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid` (`shopid`),
  KEY `groupid` (`groupid`),
  KEY `region` (`region`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_bidding_rules 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_bidding_rules` (
  `campaign_optimization_id` char(36) NOT NULL,
  `profileid` bigint(20) unsigned DEFAULT NULL,
  `rule_name` char(50) DEFAULT NULL,
  `rule_type` char(10) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `rule_sub_category` char(50) DEFAULT NULL,
  `rule_category` char(50) DEFAULT NULL,
  `recurrence` char(50) DEFAULT NULL,
  `action` varchar(500) DEFAULT NULL,
  `conditions` varchar(500) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  PRIMARY KEY (`campaign_optimization_id`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_brand` (
  `profileid` bigint(20) unsigned NOT NULL,
  `brandId` varchar(50) NOT NULL,
  `brandEntityId` varchar(50) NOT NULL,
  `brandRegistryName` varchar(50) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`brandId`,`brandEntityId`,`profileid`),
  KEY `profileid_brandRegistryName` (`profileid`,`brandRegistryName`) USING BTREE
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_budget_rules 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_budget_rules` (
  `rule_id` char(36) NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `rule_state` char(10) DEFAULT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `duration` varchar(500) DEFAULT NULL,
  `recurrence` varchar(500) DEFAULT NULL,
  `rule_type` char(15) DEFAULT NULL,
  `budget_increase_by` varchar(500) DEFAULT NULL,
  `name` varchar(355) DEFAULT NULL,
  `performance_measure_condition` varchar(500) DEFAULT NULL,
  `rule_status` char(10) DEFAULT NULL,
  PRIMARY KEY (`rule_id`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campaigns 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaigns` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `portfolioid` bigint(20) unsigned DEFAULT NULL,
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
  KEY `idx_profileid_name` (`name`(191)),
  KEY `profileid` (`profileid`),
  KEY `state` (`state`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campaigns_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaigns_hsa` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `portfolioid` bigint(20) unsigned DEFAULT NULL,
  `name` char(200) DEFAULT NULL,
  `budgetType` char(15) DEFAULT NULL,
  `budget` decimal(12,2) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `bidding` varchar(1000) DEFAULT NULL,
  `brandEntityId` char(30) DEFAULT NULL,
  `goal` char(30) DEFAULT NULL,
  `productLocation` varchar(50) DEFAULT NULL,
  `tags` varchar(500) DEFAULT NULL,
  `costType` char(10) DEFAULT NULL,
  `smartDefault` char(15) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `spendingPolicy` char(10) DEFAULT NULL,
  `landingPage` varchar(200) DEFAULT NULL,
  `adFormat` char(30) DEFAULT NULL,
  `bidOptimization` bit(1) DEFAULT NULL,
  `isMultiAdGroupsEnabled` bit(1) DEFAULT NULL,
  `bidMultiplier` decimal(12,2) DEFAULT NULL,
  `bidAdjustments` varchar(350) DEFAULT NULL,
  `bidAdjustmentsByShopperSegment` varchar(350) DEFAULT NULL,
  `bidAdjustmentsByPlacement` varchar(350) DEFAULT NULL,
  `bidOptimizationStrategy` varchar(30) DEFAULT NULL,
  `creative` varchar(1000) DEFAULT NULL,
  `servingStatus` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`profileid`),
  KEY `profileid_name` (`profileid`,`name`(191))
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campaigns_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaigns_sd` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `portfolioId` bigint(20) unsigned DEFAULT NULL,
  `name` char(200) DEFAULT NULL,
  `tactic` char(30) DEFAULT NULL,
  `costtype` char(5) DEFAULT NULL,
  `budgettype` char(15) DEFAULT NULL,
  `budget` decimal(12,2) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`profileid`),
  KEY `profileid` (`profileid`,`name`(191))
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campkeywords_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campkeywords_negativa` (
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `keywordId` bigint(20) unsigned NOT NULL,
  `keywordText` char(100) DEFAULT NULL,
  `matchType` char(20) DEFAULT NULL,
  `state` char(15) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_campproduct_targe_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campproduct_targe_negativa` (
  `targetId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` varchar(500) DEFAULT NULL,
  `expression` varchar(500) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`) USING BTREE,
  KEY `profileid` (`profileid`) USING BTREE,
  KEY `adGroupId` (`campaignId`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_group 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_group` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`shopid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_keywords 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `keywordId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_keywords_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords_hsa` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `keywordId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_keywords_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords_negativa` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `keywordText` char(100) DEFAULT NULL,
  `matchType` char(20) DEFAULT NULL,
  `campaignType` char(4) DEFAULT NULL,
  `nativeLanguageKeyword` char(5) DEFAULT NULL,
  `nativeLanguageLocale` char(5) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_keywords_negativa_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords_negativa_hsa` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `keywordText` char(100) DEFAULT NULL,
  `matchType` char(20) DEFAULT NULL,
  `campaignType` char(4) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`) USING BTREE,
  KEY `adGroupId` (`adGroupId`) USING BTREE,
  KEY `campaignId` (`campaignId`) USING BTREE,
  KEY `profileid` (`profileid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_media_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_media_hsa` (
  `mediaId` char(60) NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned DEFAULT NULL,
  `status` char(30) DEFAULT NULL,
  `statusMetadata` varchar(100) DEFAULT NULL,
  `publishedMediaUrl` varchar(2000) DEFAULT NULL,
  `operator` bigint(20) unsigned NOT NULL DEFAULT '0',
  `opttime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`mediaId`),
  KEY `profileid` (`profileid`,`opttime`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_operate_log 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_operate_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `profileid` bigint(20) unsigned DEFAULT NULL,
  `campaignId` bigint(20) unsigned DEFAULT NULL,
  `adGroupId` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `beanclasz` char(100) DEFAULT NULL,
  `beforeobject` longtext,
  `afterobject` longtext,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `campaignId` (`campaignId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `profileid` (`profileid`,`opttime`)
) ENGINE=InnoDB AUTO_INCREMENT=494441 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_portfolios 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_portfolios` (
  `id` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
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
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_productads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_productads` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `asin` char(50) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `customText` varchar(150) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adId`),
  KEY `profileid_sku` (`sku`),
  KEY `profileid` (`profileid`),
  KEY `campaignId` (`campaignId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `state` (`state`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_productads_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_productads_sd` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `asin` char(50) DEFAULT NULL,
  `adName` char(50) DEFAULT NULL,
  `landingPageType` varchar(200) DEFAULT NULL,
  `landingPageURL` varchar(200) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adId`),
  KEY `profileid_sku` (`sku`),
  KEY `profileid` (`profileid`),
  KEY `campaignId` (`campaignId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `state` (`state`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe` (
  `targetId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` char(10) DEFAULT NULL,
  `expression` varchar(500) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `profileid_expression` (`profileid`),
  KEY `adGroupId` (`adGroupId`),
  KEY `campaignId` (`campaignId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_hsa` (
  `targetId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` varchar(500) DEFAULT NULL,
  `expression` varchar(500) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`) USING BTREE,
  KEY `profileid_expression` (`profileid`) USING BTREE,
  KEY `adGroupId` (`adGroupId`) USING BTREE,
  KEY `campaignId` (`campaignId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_negativa` (
  `targetId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` varchar(50) DEFAULT NULL,
  `expression` varchar(500) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe_negativa_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_negativa_hsa` (
  `targetId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` varchar(500) DEFAULT NULL,
  `expression` varchar(500) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`) USING BTREE,
  KEY `adGroupId` (`adGroupId`) USING BTREE,
  KEY `profileid` (`profileid`) USING BTREE,
  KEY `campaignId` (`campaignId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe_negativa_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_negativa_sd` (
  `targetId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` char(10) DEFAULT NULL,
  `expression` varchar(500) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_product_targe_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_sd` (
  `targetId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` char(10) DEFAULT NULL,
  `expression` varchar(500) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `bid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `profileid_expression` (`profileid`),
  KEY `adGroupId` (`adGroupId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_profile 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_profile` (
  `id` bigint(20) unsigned NOT NULL,
  `countryCode` char(2) DEFAULT NULL,
  `currencyCode` char(3) DEFAULT NULL,
  `marketplaceId` char(15) DEFAULT NULL,
  `sellerId` char(20) DEFAULT NULL,
  `advauthId` bigint(20) unsigned DEFAULT NULL,
  `timezone` char(25) DEFAULT NULL,
  `type` char(10) DEFAULT NULL,
  `dailyBudget` decimal(20,5) DEFAULT NULL,
  `errorday` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `errorlog` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `marketplaceId` (`marketplaceId`),
  KEY `authid` (`advauthId`),
  KEY `sellerId` (`sellerId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_remark 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_remark` (
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adgroupId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `keywordId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `adId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `targetId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `remark` varchar(1000) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`profileid`,`campaignId`,`adgroupId`,`keywordId`,`adId`,`targetId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='广告历史记录备注';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_remind 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_remind` (
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignid` bigint(20) unsigned NOT NULL,
  `adgroupid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `keywordid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `adid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `targetid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `recordtype` char(10) DEFAULT NULL,
  `cycle` int(11) DEFAULT NULL COMMENT '1当天，7（7天）',
  `quota` char(15) DEFAULT NULL COMMENT 'click（点击数），ctr（点击率）,cost（花费），acos,cr(转化率）',
  `fcondition` int(11) DEFAULT NULL COMMENT '1是超过，2是低于',
  `subtrahend` char(15) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL COMMENT '(cycle.quota） condition(>) amount',
  `iswarn` bit(1) NOT NULL DEFAULT b'0',
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`profileid`,`campaignid`,`adgroupid`,`keywordid`,`adid`,`targetid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='广告提醒';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_report_request 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_report_request` (
  `reportId` char(100) NOT NULL,
  `profileId` bigint(20) unsigned NOT NULL,
  `recordType` char(50) NOT NULL,
  `status` char(50) DEFAULT NULL,
  `statusDetails` char(100) DEFAULT NULL,
  `reportType` int(11) DEFAULT NULL,
  `campaignType` char(5) DEFAULT NULL,
  `segment` char(20) DEFAULT NULL,
  `creativeType` char(20) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `location` varchar(2000) DEFAULT NULL,
  `fileSize` int(11) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `requesttime` datetime DEFAULT NULL,
  `treat_number` int(10) unsigned DEFAULT '0',
  `treat_status` varchar(20) DEFAULT NULL,
  `log` varchar(5000) DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  PRIMARY KEY (`reportId`,`profileId`,`recordType`) USING BTREE,
  KEY `Index1` (`requesttime`,`treat_number`,`treat_status`,`isrun`) USING BTREE,
  KEY `Index 2` (`profileId`,`recordType`,`campaignType`,`segment`) USING BTREE,
  KEY `byday` (`startDate`) USING BTREE,
  KEY `opttime` (`opttime`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='广告报表请求记录表';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_report_request_type 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_report_request_type` (
  `id` int(11) NOT NULL DEFAULT '0',
  `campaigntype` char(3) NOT NULL DEFAULT '0',
  `reporttype` char(30) NOT NULL DEFAULT '0',
  `segment` char(11) DEFAULT '0',
  `activeType` char(10) DEFAULT '0',
  `metrics` varchar(2000) NOT NULL DEFAULT '0',
  `bean` varchar(50) DEFAULT NULL,
  `reponsetype` varchar(30) DEFAULT NULL,
  `nomarket` char(245) DEFAULT NULL,
  `disablevendor` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `campaigntype_reporttype_segment_activeType` (`campaigntype`,`reporttype`,`segment`,`activeType`)
) ENGINE=InnoDB;

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
  PRIMARY KEY (`bydate`,`adGroupId`) USING BTREE,
  KEY `adGroupId_campaignId_profileid` (`profileid`,`campaignId`,`adGroupId`) USING BTREE
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_adgroups_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_adgroups_attributed` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`bydate`,`adGroupId`) USING BTREE
) ENGINE=InnoDB;

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
  PRIMARY KEY (`bydate`,`adGroupId`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
  PRIMARY KEY (`bydate`,`adGroupId`) USING BTREE,
  KEY `campaignId_bydate` (`adGroupId`,`bydate`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
  PRIMARY KEY (`bydate`,`adId`) USING BTREE,
  KEY `profileid` (`profileid`,`campaignId`,`adGroupId`,`adId`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_ads_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_ads_attributed` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT NULL,
  `attributedConversions14d` int(11) DEFAULT NULL,
  `attributedConversions14dSameSKU` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
  PRIMARY KEY (`bydate`,`adId`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
  PRIMARY KEY (`bydate`,`adId`) USING BTREE,
  KEY `campaignId_bydate` (`bydate`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignBudget` decimal(12,2) DEFAULT NULL,
  `impressions` int(11) unsigned NOT NULL DEFAULT '0',
  `clicks` int(11) unsigned NOT NULL DEFAULT '0',
  `cost` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `topOfSearchImpressionShare` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`) USING BTREE,
  KEY `campaignId_profileid` (`profileid`,`campaignId`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_hsa_campaigns_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_attributed` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`bydate`,`campaignId`) USING BTREE
) ENGINE=InnoDB;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_adgroups_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups_attributed_new` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_asins 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_asins` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `asin` char(50) NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `otherAsin` char(50) NOT NULL,
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_attributed_new` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_campaigns_t00001 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_t00001` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(12,2) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `attributedDPV14d` int(11) DEFAULT NULL,
  `attributedUnitsSold14d` int(11) DEFAULT NULL,
  `attributedSales14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`,`profileid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_productads_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads_attributed_new` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_product_targets_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets_attributed_new` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sp_asins 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_asins` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `advertisedAsin` char(50) NOT NULL COMMENT 'advertisedAsin',
  `sku` char(50) DEFAULT NULL,
  `purchasedAsin` char(50) NOT NULL COMMENT 'purchasedAsin',
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
  PRIMARY KEY (`bydate`,`advertisedAsin`,`purchasedAsin`,`keywordId`) USING BTREE,
  KEY `profileid` (`profileid`,`campaignId`,`adGroupId`,`advertisedAsin`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
  PRIMARY KEY (`bydate`,`campaignId`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='campaignId';

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='campaignId';

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='campaignId';

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt_placement 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_placement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=44;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_rpt_query 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_query` (
  `id` bigint(20) unsigned NOT NULL,
  `query` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `query` (`query`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_schedule_plan 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_schedule_plan` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` char(20) DEFAULT NULL COMMENT '计划名称',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态 enabled ： 已开启    paused ： 已禁止   disable  : 已结束',
  `startDate` date DEFAULT NULL COMMENT '开始时间',
  `endDate` date DEFAULT NULL,
  `weekdays` varchar(50) DEFAULT NULL,
  `startTime` time DEFAULT NULL,
  `endTime` time DEFAULT NULL,
  `remark` char(200) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='广告定时任务';

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
  `oldstatus` char(10) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `profileid_campaignId_adGroupId_adId_keywordId` (`profileid`,`campaignId`,`adGroupId`,`keywordId`,`adId`,`planid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='广告定时任务';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_schedule_planitem 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_schedule_planitem` (
  `taskId` char(36) NOT NULL,
  `planId` bigint(20) unsigned NOT NULL,
  `type` char(10) DEFAULT NULL COMMENT 'one单次执行，cycle周期执行',
  `startTime` time DEFAULT NULL COMMENT '开始时间',
  `status` char(10) DEFAULT NULL,
  `bid` decimal(12,2) DEFAULT NULL,
  `weekdays` char(20) DEFAULT NULL COMMENT '1 ：周日； 2：周一 ；13：周日和周二 以此类推',
  PRIMARY KEY (`taskId`,`planId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_serch_history 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_serch_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) unsigned DEFAULT NULL,
  `ftype` char(20) DEFAULT NULL,
  `fcondition` varchar(200) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=14 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_snapshot 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_snapshot` (
  `snapshotId` char(75) NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `region` char(2) NOT NULL,
  `status` char(20) DEFAULT NULL,
  `location` varchar(2000) DEFAULT NULL,
  `recordtype` char(30) DEFAULT NULL,
  `campaignType` char(5) DEFAULT NULL,
  `fileSize` int(11) DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `requesttime` datetime DEFAULT NULL,
  `treat_number` int(10) DEFAULT NULL,
  `treat_status` varchar(20) DEFAULT NULL,
  `log` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`snapshotId`),
  KEY `Index 2` (`profileid`,`recordtype`,`campaignType`,`region`) USING BTREE,
  KEY `Index 3` (`requesttime`,`treat_number`,`treat_status`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_stores 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_stores` (
  `profileid` bigint(20) unsigned NOT NULL,
  `entityId` varchar(30) NOT NULL,
  `brandEntityId` varchar(30) NOT NULL,
  `storePageId` varchar(50) NOT NULL,
  `storeName` varchar(50) DEFAULT NULL,
  `storePageUrl` varchar(100) DEFAULT NULL,
  `storePageName` varchar(50) DEFAULT NULL,
  `opptime` datetime DEFAULT NULL,
  PRIMARY KEY (`entityId`,`brandEntityId`,`profileid`,`storePageId`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_sumalltype 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_sumalltype` (
  `profileid` bigint(20) unsigned NOT NULL,
  `byday` date NOT NULL,
  `state` char(10) NOT NULL,
  `campaignType` char(4) NOT NULL,
  `recordType` char(25) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`profileid`,`campaignType`,`state`,`recordType`,`byday`) USING BTREE,
  KEY `byday` (`byday`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='计划的 state,campagintype,recordtype 都是task';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_sumpdtads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_sumpdtads` (
  `profileid` bigint(20) unsigned NOT NULL,
  `byday` date NOT NULL,
  `ctype` char(2) NOT NULL DEFAULT 'sp',
  `impressions` int(11) DEFAULT NULL,
  `clicks` int(11) DEFAULT NULL,
  `cost` decimal(14,2) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `attributedUnitsOrdered` int(11) DEFAULT NULL,
  `attributedSales` decimal(14,2) DEFAULT NULL,
  `attributedConversions` int(11) DEFAULT NULL,
  PRIMARY KEY (`profileid`,`byday`,`ctype`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='转化';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_adv_warningdate 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_warningdate` (
  `shopid` bigint(20) NOT NULL,
  `recordType` char(15) NOT NULL,
  `ftype` char(15) NOT NULL COMMENT 'yesterday：昨日突降；sequent：连续下降；co：同期下降',
  `num_impressions` decimal(10,2) DEFAULT NULL,
  `num_CSRT` decimal(10,2) DEFAULT NULL,
  `num_ACOS` decimal(10,2) DEFAULT NULL,
  `num_clicks` decimal(10,2) DEFAULT NULL COMMENT '比率',
  `absoluteNum_impressions` int(11) DEFAULT NULL COMMENT '绝对值',
  `absoluteNum_clicks` int(11) DEFAULT NULL,
  `absoluteNum_ACOS` decimal(10,2) DEFAULT NULL,
  `absoluteNum_CSRT` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`shopid`,`recordType`,`ftype`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_amz_region 结构
CREATE TABLE IF NOT EXISTS `t_amz_region` (
  `code` char(2) NOT NULL,
  `name` char(10) DEFAULT NULL,
  `advname` char(100) DEFAULT NULL,
  `advpoint` char(100) DEFAULT NULL,
  `client_id` char(100) DEFAULT NULL,
  `client_secret` char(100) DEFAULT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_erp_serial_num 结构
CREATE TABLE IF NOT EXISTS `t_erp_serial_num` (
  `id` char(36) NOT NULL,
  `ftype` char(36) DEFAULT NULL,
  `seqno` int(11) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `prefix_date` date DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`prefix_date`,`ftype`,`shopid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_exchangerate 结构
CREATE TABLE IF NOT EXISTS `t_exchangerate` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '币别',
  `price` decimal(18,6) DEFAULT NULL COMMENT '汇率',
  `symbol` varchar(50) DEFAULT NULL,
  `type` varchar(15) DEFAULT NULL,
  `utctime` datetime DEFAULT NULL COMMENT '服务器更新时间',
  `volume` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=444945 ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_exchangerate_customer 结构
CREATE TABLE IF NOT EXISTS `t_exchangerate_customer` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `name` char(5) NOT NULL DEFAULT '' COMMENT '币别',
  `price` decimal(18,6) DEFAULT NULL COMMENT '汇率',
  `symbol` varchar(50) DEFAULT NULL,
  `type` varchar(15) DEFAULT NULL,
  `utctime` datetime DEFAULT NULL COMMENT '服务器更新时间',
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`shopid`,`name`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_amazon_adv.t_marketplace 结构
CREATE TABLE IF NOT EXISTS `t_marketplace` (
  `marketplaceId` varchar(15) NOT NULL COMMENT '站点编码ID',
  `market` char(5) DEFAULT NULL COMMENT '站点简码',
  `name` varchar(20) DEFAULT NULL COMMENT '站点名称',
  `region_name` varchar(20) DEFAULT NULL COMMENT '所属区域名称',
  `region` char(10) DEFAULT NULL COMMENT '所属区域简码',
  `end_point` varchar(40) DEFAULT NULL COMMENT '所属区域站点',
  `point_name` varchar(20) DEFAULT NULL,
  `accessKey` varchar(60) DEFAULT NULL,
  `secretKey` varchar(200) DEFAULT NULL,
  `dim_units` char(10) DEFAULT NULL,
  `weight_units` char(10) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `findex` int(11) DEFAULT '0',
  `adv_end_point` varchar(30) DEFAULT NULL,
  `aws_access_key` varchar(30) DEFAULT NULL,
  `aws_secret_key` varchar(100) DEFAULT NULL,
  `associate_tag` varchar(30) DEFAULT NULL,
  `developer_url` varchar(1100) DEFAULT NULL,
  `dev_account_num` char(20) DEFAULT NULL,
  `bytecode` char(10) DEFAULT NULL,
  `sp_api_endpoint` char(40) DEFAULT NULL,
  `aws_region` char(10) DEFAULT NULL,
  PRIMARY KEY (`marketplaceId`) USING BTREE,
  KEY `Index 3` (`point_name`) USING BTREE,
  KEY `region` (`region`) USING BTREE,
  KEY `marketplaceId_region` (`marketplaceId`,`region`) USING BTREE,
  KEY `currency` (`currency`) USING BTREE,
  KEY `Index 2` (`market`) USING BTREE,
  KEY `aws_region` (`aws_region`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='站点';


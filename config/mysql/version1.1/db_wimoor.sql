
-- 导出  表 db_wimoor.oauth_client_details 结构
CREATE TABLE IF NOT EXISTS `oauth_client_details` (
  `client_id` varchar(100) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(2000) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_advert_warning_keywords_data 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_advert_warning_product_data 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_adv_browsenode 结构
CREATE TABLE IF NOT EXISTS `t_adv_browsenode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(255) DEFAULT NULL,
  `node_path` varchar(255) DEFAULT NULL,
  `query` varchar(255) DEFAULT NULL,
  `refinement` varchar(255) DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2580 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_adv_dimensions 结构
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
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_adv_productgroup 结构
CREATE TABLE IF NOT EXISTS `t_adv_productgroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `referralfeeId` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8879 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_adv_rank 结构
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

-- 导出  表 db_wimoor.t_adv_rank_his 结构
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

-- 导出  表 db_wimoor.t_amazonseller_market 结构
CREATE TABLE IF NOT EXISTS `t_amazonseller_market` (
  `sellerid` char(30)  NOT NULL COMMENT '卖家Sellerid',
  `marketplace_id` char(15)  NOT NULL COMMENT '站点ID',
  `country` char(2)  DEFAULT NULL COMMENT '国家编码',
  `name` varchar(50)  DEFAULT NULL COMMENT '站点英文名称',
  `language` char(5)  DEFAULT NULL COMMENT '对应语言编码',
  `currency` char(4)  DEFAULT NULL COMMENT '对应币种',
  `domain` varchar(50)  DEFAULT NULL COMMENT '对应域名',
  `amazonauthid` bigint(20) unsigned DEFAULT NULL COMMENT '授权对应ID等同于Sellerid',
  `opttime` datetime(6) DEFAULT NULL COMMENT '操作时间',
  `disable` bit(1) DEFAULT b'0' COMMENT '操作人',
  PRIMARY KEY (`sellerid`,`marketplace_id`)
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amazon_auth 结构
CREATE TABLE IF NOT EXISTS `t_amazon_auth` (
  `id` bigint(20) unsigned NOT NULL,
  `shop_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户ID',
  `sellerid` varchar(30)  NOT NULL COMMENT '卖家ID',
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `region` char(10) DEFAULT NULL,
  `MWSAuthToken` varchar(50) DEFAULT NULL COMMENT '卖家授权码',
  `disable` bit(1) DEFAULT b'0',
  `name` varchar(10) DEFAULT NULL,
  `pictureId` bigint(20) unsigned DEFAULT NULL,
  `status` char(20)  DEFAULT NULL,
  `statusupdate` datetime DEFAULT NULL,
  `productdate` datetime DEFAULT NULL,
  `refreshinvtime` datetime DEFAULT NULL,
  `refresh_token` varchar(500)  DEFAULT NULL,
  `refresh_token_time` datetime DEFAULT NULL,
  `aws_region` char(15)  DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  `access_key_id` varchar(50)  DEFAULT NULL,
  `secret_key` varchar(50)  DEFAULT NULL,
  `role_arn` varchar(50)  DEFAULT NULL,
  `client_id` varchar(100)  DEFAULT NULL,
  `client_secret` varchar(100)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 3` (`sellerid`) USING BTREE,
  KEY `disable` (`disable`),
  KEY `shop_id` (`shop_id`),
  KEY `Index_id_shopid` (`groupid`) USING BTREE,
  KEY `region` (`region`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='亚马逊账号授权';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amazon_auth_market_performance 结构
CREATE TABLE IF NOT EXISTS `t_amazon_auth_market_performance` (
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(20) NOT NULL,
  `accountstatus` varchar(20) DEFAULT NULL,
  `sellerid` char(20) DEFAULT NULL,
  `performance` varchar(6000) DEFAULT NULL,
  `refreshtime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`,`marketplaceid`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amazon_group 结构
CREATE TABLE IF NOT EXISTS `t_amazon_group` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(200) DEFAULT NULL,
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
  UNIQUE KEY `Index 2` (`shopid`,`name`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_advert_report 结构
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

-- 导出  表 db_wimoor.t_amz_advert_report_summary 结构
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

-- 导出  表 db_wimoor.t_amz_advert_report_summary_month 结构
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

-- 导出  表 db_wimoor.t_amz_advert_report_summary_week 结构
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

-- 导出  表 db_wimoor.t_amz_adv_adgroups 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  `state` char(50) DEFAULT NULL,
  `defaultBid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`),
  KEY `profileid_name` (`name`(255)),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_adgroups_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups_hsa` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `name` char(200) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_adgroups_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_adgroups_sd` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  `state` char(50) DEFAULT NULL,
  `defaultBid` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`adGroupId`),
  KEY `profileid_name` (`name`(255)),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_assets 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_assets` (
  `assetId` varchar(100) NOT NULL,
  `brandEntityId` varchar(30) NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `mediaType` varchar(10) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  `opptime` datetime DEFAULT NULL,
  PRIMARY KEY (`profileid`,`brandEntityId`,`assetId`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_auth 结构
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

-- 导出  表 db_wimoor.t_amz_adv_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_brand` (
  `profileid` bigint(20) unsigned NOT NULL,
  `brandId` varchar(50) NOT NULL,
  `brandEntityId` varchar(50) NOT NULL,
  `brandRegistryName` varchar(50) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`brandId`,`brandEntityId`,`profileid`),
  KEY `profileid_brandRegistryName` (`profileid`,`brandRegistryName`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_browsenode 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_browsenode` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `parentid` bigint(20) unsigned DEFAULT NULL,
  `country` char(2) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `is_category_root` bit(1) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`parentid`),
  KEY `Index 3` (`country`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_campaigns 结构
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
  KEY `idx_profileid_name` (`name`),
  KEY `profileid` (`profileid`),
  KEY `state` (`state`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_campaigns_hsa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaigns_hsa` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `portfolioid` bigint(20) unsigned DEFAULT NULL,
  `name` char(200) DEFAULT NULL,
  `tags` char(200) DEFAULT NULL,
  `budgetType` char(15) DEFAULT NULL,
  `budget` decimal(12,2) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `spendingPolicy` char(10) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `servingStatus` char(30) DEFAULT NULL,
  `brandEntityId` char(30) DEFAULT NULL,
  `landingPage` varchar(200) DEFAULT NULL,
  `adFormat` char(30) DEFAULT NULL,
  `creative` varchar(1000) DEFAULT NULL,
  `bidOptimization` char(10) DEFAULT NULL,
  `bidMultiplier` decimal(12,2) DEFAULT NULL,
  `bidAdjustments` varchar(350) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`profileid`),
  KEY `profileid_name` (`profileid`,`name`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_campaigns_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_campaigns_sd` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_campkeywords_negativa 结构
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

-- 导出  表 db_wimoor.t_amz_adv_group 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_group` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`shopid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_keywords 结构
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

-- 导出  表 db_wimoor.t_amz_adv_keywords_hsa 结构
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

-- 导出  表 db_wimoor.t_amz_adv_keywords_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_keywords_negativa` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `keywordText` char(100) DEFAULT NULL,
  `matchType` char(20) DEFAULT NULL,
  `campaignType` char(4) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`keywordId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_media_hsa 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_operate_log 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=474224 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_portfolios 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_productads 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_productads` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_productads_sd 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_productads_sd` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `adId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_product_targe 结构
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

-- 导出  表 db_wimoor.t_amz_adv_product_targe_negativa 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_product_targe_negativa` (
  `targetId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `campaignId` bigint(20) unsigned NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `expressionType` char(10) DEFAULT NULL,
  `expression` varchar(500) DEFAULT NULL,
  `state` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`targetId`),
  KEY `adGroupId` (`adGroupId`),
  KEY `campaignId` (`campaignId`),
  KEY `profileid` (`profileid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_product_targe_negativa_sd 结构
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

-- 导出  表 db_wimoor.t_amz_adv_product_targe_sd 结构
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

-- 导出  表 db_wimoor.t_amz_adv_profile 结构
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

-- 导出  表 db_wimoor.t_amz_adv_remark 结构
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

-- 导出  表 db_wimoor.t_amz_adv_remind 结构
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

-- 导出  表 db_wimoor.t_amz_adv_report_metrics 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_report_metrics` (
  `id` int(11) NOT NULL DEFAULT '0',
  `campaigntype` char(3) NOT NULL DEFAULT '0',
  `reporttype` char(30) NOT NULL DEFAULT '0',
  `segment` char(11) DEFAULT '0',
  `activeType` char(10) DEFAULT '0',
  `metrics` varchar(2000) NOT NULL DEFAULT '0',
  `level` int(11) DEFAULT NULL,
  `reponsetype` varchar(30) DEFAULT NULL,
  `nomarket` char(245) DEFAULT NULL,
  `disablevendor` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `campaigntype_reporttype_segment_activeType` (`campaigntype`,`reporttype`,`segment`,`activeType`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_request 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_request` (
  `reportId` char(100) NOT NULL,
  `profileId` bigint(20) unsigned NOT NULL,
  `recordType` char(50) DEFAULT NULL,
  `status` char(50) DEFAULT NULL,
  `statusDetails` char(100) DEFAULT NULL,
  `campaignType` char(5) DEFAULT NULL,
  `segment` char(20) DEFAULT NULL,
  `creativeType` char(20) DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `location` varchar(500) DEFAULT NULL,
  `fileSize` int(11) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `requesttime` datetime DEFAULT NULL,
  `treat_number` int(10) unsigned DEFAULT '0',
  `treat_status` varchar(20) DEFAULT NULL,
  `log` varchar(5000) DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  PRIMARY KEY (`reportId`,`profileId`),
  KEY `Index1` (`requesttime`,`treat_number`,`treat_status`,`isrun`) USING BTREE,
  KEY `Index 2` (`profileId`,`recordType`,`campaignType`,`segment`) USING BTREE,
  KEY `byday` (`byday`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='广告报表请求记录表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `profileid` bigint(20) unsigned NOT NULL,
  `campaignBudget` decimal(12,2) DEFAULT NULL,
  `impressions` int(11) unsigned NOT NULL DEFAULT '0',
  `clicks` int(11) unsigned NOT NULL DEFAULT '0',
  `cost` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`campaignId`) USING BTREE,
  KEY `campaignId_profileid` (`profileid`,`campaignId`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_attributed` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) unsigned NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`bydate`,`campaignId`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_brand 结构
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
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`campaignId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_place 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_place_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_place_attributed` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int(10) unsigned NOT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) DEFAULT '0.00',
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_place_brand 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_campaigns_place_brand` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `placementid` int(10) unsigned NOT NULL,
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
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`bydate`,`campaignId`,`placementid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_place_video 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_campaigns_video 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_keywords 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_keywords_attributed 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_keywords_brand 结构
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
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_keywords_query 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_keywords_video 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_product_targets 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_product_targets_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_hsa_product_targets_attributed` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedConversions14d` decimal(10,2) DEFAULT NULL,
  `attributedConversions14dSameSKU` decimal(10,2) DEFAULT NULL,
  `attributedSales14d` decimal(10,0) DEFAULT NULL,
  `attributedSales14dSameSKU` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_product_targets_brand 结构
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
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_hsa_product_targets_video 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_adgroups 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_adgroups_attributed 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_adgroups_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_adgroups_attributed_new` (
  `adGroupId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_adgroups_attributed_same 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_adgroups_attributed_view 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_asins 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_campaigns 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_campaigns_attributed 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_campaigns_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_campaigns_attributed_new` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(12,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`campaignId`,`bydate`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_campaigns_attributed_same 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_campaigns_attributed_view 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_campaigns_t00001 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_productads 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_productads_attributed 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_productads_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_productads_attributed_new` (
  `adId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_productads_attributed_same 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_productads_attributed_view 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_product_targets 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_product_targets_attributed 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_product_targets_attributed_new 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_product_targets_attributed_new` (
  `targetId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedOrdersNewToBrand14d` int(11) DEFAULT NULL,
  `attributedSalesNewToBrand14d` decimal(10,2) DEFAULT NULL,
  `attributedUnitsOrderedNewToBrand14d` int(11) DEFAULT NULL,
  PRIMARY KEY (`bydate`,`targetId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_product_targets_attributed_same 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sd_product_targets_attributed_view 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_adgroups 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_adgroups_attributed 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_adgroups_attributed_same 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_asins 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sp_asins` (
  `campaignId` bigint(20) unsigned NOT NULL,
  `adGroupId` bigint(20) unsigned NOT NULL,
  `targetId` bigint(20) unsigned NOT NULL,
  `keywordId` bigint(20) unsigned NOT NULL,
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
  PRIMARY KEY (`bydate`,`asin`,`otherAsin`,`keywordId`,`targetId`),
  KEY `profileid` (`profileid`,`campaignId`,`adGroupId`,`asin`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_compaigns 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_compaigns_attributed 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_compaigns_attributed_same 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_compaigns_place 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_compaigns_place_attributed 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_compaigns_place_attributed_same 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_keywords 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_keywords_attributed 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_keywords_attributed_same 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_keywords_query 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_keywords_query_attributed 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_keywords_query_attributed_same 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_productads 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_productads_attributed 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_productads_attributed_same 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_product_targets 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_product_targets_attributed 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_product_targets_attributed_same 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_product_targets_query 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_product_targets_query_attributed 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt2_sp_product_targets_query_attributed_same 结构
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

-- 导出  表 db_wimoor.t_amz_adv_rpt_keywords_hsa_attributed 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_keywords_hsa_attributed` (
  `keywordId` bigint(20) unsigned NOT NULL,
  `bydate` date NOT NULL,
  `attributedSales14d` decimal(12,2) DEFAULT '0.00',
  `attributedSales14dSameSKU` decimal(12,2) DEFAULT '0.00',
  `attributedConversions14d` decimal(12,2) DEFAULT '0.00',
  `attributedConversions14dSameSKU` decimal(12,2) DEFAULT '0.00',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`keywordId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt_placement 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_placement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=27 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_rpt_query 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt_query` (
  `id` bigint(20) unsigned NOT NULL,
  `query` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `query` (`query`(191))
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_schedule_plan 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_schedule_plan` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `taskName` char(20) DEFAULT NULL COMMENT '计划名称',
  `status` char(10) DEFAULT NULL COMMENT '状态 enabled ： 已开启    paused ： 已禁止   disable  : 已结束',
  `conditionsExecute` varchar(2000) DEFAULT NULL,
  `executeStatus` char(10) DEFAULT NULL COMMENT 'wait ：等待执行    executing ：执行中     completed  ： 执行完成   error : 执行失败',
  `startDate` date DEFAULT NULL COMMENT '开始时间',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `remark` char(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=423 ROW_FORMAT=DYNAMIC COMMENT='广告定时任务';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_schedule_plandata 结构
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
) ENGINE=InnoDB AUTO_INCREMENT=1141 ROW_FORMAT=DYNAMIC COMMENT='广告定时任务';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_schedule_planitem 结构
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

-- 导出  表 db_wimoor.t_amz_adv_serch_history 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_serch_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) unsigned DEFAULT NULL,
  `ftype` char(20) DEFAULT NULL,
  `fcondition` varchar(200) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=25 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_snapshot 结构
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

-- 导出  表 db_wimoor.t_amz_adv_stores 结构
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_sumalltype 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_sumalltype` (
  `profileid` bigint(20) unsigned NOT NULL,
  `byday` date NOT NULL,
  `state` char(10) NOT NULL,
  `campaignType` char(4) NOT NULL,
  `recordType` char(25) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`profileid`,`state`,`campaignType`,`recordType`,`byday`),
  KEY `byday` (`byday`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='计划的 state,campagintype,recordtype 都是task';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_adv_sumpdtads 结构
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

-- 导出  表 db_wimoor.t_amz_adv_warningdate 结构
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

-- 导出  表 db_wimoor.t_amz_api_timelimit 结构
CREATE TABLE IF NOT EXISTS `t_amz_api_timelimit` (
  `id` bigint(20) unsigned NOT NULL,
  `apiname` char(50) DEFAULT NULL,
  `maximum` int(11) DEFAULT NULL COMMENT '一次性最多访问次数',
  `quarter` int(11) DEFAULT NULL COMMENT '一刻钟可以访问多少次',
  `half` int(11) DEFAULT NULL COMMENT '半小时可以访问多少次',
  `hour` int(11) DEFAULT NULL COMMENT '一小时可以访问多少次',
  `restore_rate` int(11) DEFAULT NULL COMMENT '每个单位时长恢复多少个',
  `restore_unit` int(11) DEFAULT NULL COMMENT '多少秒钟可以恢复一个单位值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `apiname` (`apiname`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_api_timelimit_seller_request 结构
CREATE TABLE IF NOT EXISTS `t_amz_api_timelimit_seller_request` (
  `id` bigint(20) unsigned NOT NULL,
  `sellerid` char(15) DEFAULT NULL,
  `apiname` char(50) DEFAULT NULL,
  `lastuptime` timestamp NULL DEFAULT NULL,
  `nexttoken` varchar(1000) DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `pages` int(11) DEFAULT '1',
  `restore_num` int(11) DEFAULT NULL,
  `isnewtoken` bit(1) DEFAULT NULL,
  `log` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sellerid_apiname` (`sellerid`,`apiname`,`lastuptime`),
  KEY `Index 3` (`nexttoken`(255))
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_auth_api_timelimit 结构
CREATE TABLE IF NOT EXISTS `t_amz_auth_api_timelimit` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID用于单独表示一行',
  `amazonauthid` bigint(20) unsigned NOT NULL COMMENT '授权ID，等同于SellerId',
  `apiname` char(30) NOT NULL COMMENT 'API的名字',
  `nexttoken` varchar(2000) DEFAULT NULL COMMENT '是否有nexttoken',
  `start_time` timestamp NULL DEFAULT NULL COMMENT 'API调用的开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT 'API调用的结束时间',
  `pages` int(11) DEFAULT '1' COMMENT '本次调用的页数',
  `restore` double DEFAULT NULL COMMENT '下一次的恢复时常',
  `lastuptime` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  `log` varchar(2000) DEFAULT NULL COMMENT '异常log',
  PRIMARY KEY (`amazonauthid`,`apiname`) USING BTREE,
  UNIQUE KEY `key` (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='新版本SPI-API使用';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_fin_account 结构
CREATE TABLE IF NOT EXISTS `t_amz_fin_account` (
  `amazonAuthid` bigint(20) unsigned NOT NULL,
  `groupid` char(50) NOT NULL,
  `currency` char(10) NOT NULL,
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
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`groupid`,`amazonAuthid`) USING BTREE,
  KEY `amazonAuthid` (`amazonAuthid`,`currency`,`financial_event_group_start`,`financial_event_group_end`,`original_total`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_fin_settlement_formula 结构
CREATE TABLE IF NOT EXISTS `t_amz_fin_settlement_formula` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `formula` varchar(500) DEFAULT NULL,
  `field` varchar(500) DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `pricetype` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_fin_user_item 结构
CREATE TABLE IF NOT EXISTS `t_amz_fin_user_item` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `name` char(20) DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `isused` bit(1) DEFAULT b'0',
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_fin_user_item_data 结构
CREATE TABLE IF NOT EXISTS `t_amz_fin_user_item_data` (
  `id` bigint(20) unsigned NOT NULL,
  `itemid` bigint(20) unsigned DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `sku` char(50) DEFAULT NULL COMMENT 'sku=* 号时，是指店铺费用',
  `currency` char(5) DEFAULT NULL,
  `amount` decimal(14,2) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `itemid_groupid_marketplaceid_byday_shopid` (`shopid`,`groupid`,`marketplaceid`,`sku`,`byday`,`itemid`,`currency`),
  KEY `byday` (`byday`)
) ENGINE=InnoDB COMMENT='客户导入的SKU财务项费用-应用于商品营收其他费用项目导入';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_follow_offer 结构
CREATE TABLE IF NOT EXISTS `t_amz_follow_offer` (
  `sellerid` char(30) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `name` tinyblob,
  `positive_feedback_rating` int(11) DEFAULT NULL,
  `feedback_count` int(11) DEFAULT NULL,
  `refreshtime` timestamp NULL DEFAULT NULL,
  `refreshnum` int(11) DEFAULT '0',
  PRIMARY KEY (`sellerid`,`marketplaceid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_follow_offerchange 结构
CREATE TABLE IF NOT EXISTS `t_amz_follow_offerchange` (
  `id` bigint(20) unsigned NOT NULL,
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
  `minimum_hours` int(11) DEFAULT NULL,
  `maximum_hours` int(11) DEFAULT NULL,
  `availability_type` varchar(30) DEFAULT NULL,
  `available_date` varchar(40) DEFAULT NULL,
  `sub_condition` varchar(10) DEFAULT NULL,
  `ships_from_state` varchar(20) DEFAULT NULL,
  `ships_from_country` varchar(10) DEFAULT NULL,
  `findtime` timestamp NULL DEFAULT NULL,
  `losttime` timestamp NULL DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `asin_marketplaceid_sellerid` (`asin`,`marketplaceid`,`sellerid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_inbound_fba_cycle 结构
CREATE TABLE IF NOT EXISTS `t_amz_inbound_fba_cycle` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `shopid` bigint(20) unsigned NOT NULL COMMENT '店铺',
  `transtype` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `name` char(36) DEFAULT NULL COMMENT '名称',
  `number` char(36) DEFAULT NULL,
  `stocking_cycle` int(10) DEFAULT '0' COMMENT '安全库存周期',
  `min_cycle` int(10) DEFAULT '0' COMMENT '发货频率',
  `put_on_days` int(10) DEFAULT '0' COMMENT '上架周期',
  `first_leg_days` int(10) DEFAULT '0' COMMENT '头程周期',
  `isdefault` bit(1) NOT NULL DEFAULT b'0',
  `remark` varchar(1000) DEFAULT '0',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shopid_marketplaceid` (`shopid`,`marketplaceid`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_inventory_health 结构
CREATE TABLE IF NOT EXISTS `t_amz_inventory_health` (
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `marketplaceid` char(15) NOT NULL COMMENT '站点ID',
  `sku` char(50)  NOT NULL COMMENT 'SKU区分大小写',
  `authid` bigint(20) unsigned NOT NULL COMMENT '授权ID等价sellerid',
  `snapshot_date` datetime DEFAULT NULL COMMENT '报表更新时间',
  `fnsku` char(100) DEFAULT NULL COMMENT '仓库SKUID',
  `asin` char(100) DEFAULT NULL COMMENT 'ASIN产品销售ID',
  `name` varchar(1000) DEFAULT NULL COMMENT '名称',
  `fcondition` char(10) NOT NULL COMMENT '是否新旧',
  `sales_rank` char(100) DEFAULT NULL COMMENT '销售排名',
  `product_group` char(100) DEFAULT NULL COMMENT '产品分组',
  `total_quantity` char(100) DEFAULT NULL COMMENT '当前总库存',
  `sellable_quantity` char(100) DEFAULT NULL COMMENT '可销售数量',
  `unsellable_quantity` char(100) DEFAULT NULL COMMENT '不可售数量',
  `inv_age_0to90days` int(11) DEFAULT NULL COMMENT '90天以内库龄数量',
  `inv_age_91to180days` int(11) DEFAULT NULL COMMENT '91-180天以内库龄数量',
  `inv_age_181to270days` int(11) DEFAULT NULL COMMENT '181-270天以内库龄数量',
  `inv_age_271to365days` int(11) DEFAULT NULL COMMENT '271-365天以内库龄数量',
  `inv_age_365plusdays` int(11) DEFAULT NULL COMMENT '365天以上库龄数量',
  `units_shipped_last24hrs` char(100) DEFAULT NULL COMMENT '最近24小时发货',
  `units_shipped_last7days` char(100) DEFAULT NULL COMMENT '最近7天发货',
  `units_shipped_last30days` char(100) DEFAULT NULL COMMENT '最近30天发货',
  `units_shipped_last90days` char(100) DEFAULT NULL COMMENT '最近90天发货',
  `units_shipped_last180days` char(100) DEFAULT NULL COMMENT '最近180天发货',
  `units_shipped_last365days` char(100) DEFAULT NULL COMMENT '最近365天发货',
  `weeks_of_cover_t7` char(100) DEFAULT NULL COMMENT '7天周转',
  `weeks_of_cover_t30` char(100) DEFAULT NULL COMMENT '30天周转',
  `weeks_of_cover_t90` char(100) DEFAULT NULL COMMENT '90天周转',
  `weeks_of_cover_t180` char(100) DEFAULT NULL COMMENT '180天周转',
  `weeks_of_cover_t365` char(100) DEFAULT NULL COMMENT '360天周转',
  `num_afn_new_sellers` char(100) DEFAULT NULL COMMENT '新产品的卖家数量',
  `num_afn_used_sellers` char(100) DEFAULT NULL COMMENT '旧产品的卖家数量',
  `currency` char(100) NOT NULL COMMENT '币种',
  `your_price` char(100) DEFAULT NULL COMMENT '你的售价',
  `sales_price` char(100) DEFAULT NULL COMMENT '售价',
  `lowest_afn_new_price` char(100) DEFAULT NULL COMMENT '新产品最低售价',
  `lowest_afn_used_price` char(100) DEFAULT NULL COMMENT '旧产品最低售价',
  `lowest_mfn_new_price` char(100) DEFAULT NULL COMMENT '新产品自发货最低售价',
  `lowest_mfn_used_price` char(100) DEFAULT NULL COMMENT '旧产品自发货最低售价',
  `qty_to_be_charged_ltsf_12mo` char(100) DEFAULT NULL COMMENT '超长期仓储费：超过365天的仓库费',
  `qty_in_long_term_storage_program` char(100) DEFAULT NULL COMMENT '长期仓库数量',
  `qty_with_removals_in_progress` char(100) DEFAULT NULL COMMENT '正在移除的数量',
  `projected_ltsf_12_mo` char(100) DEFAULT NULL COMMENT '下期超长期仓储费',
  `per_unit_volume` char(100) DEFAULT NULL COMMENT '库容',
  `is_hazmat` char(100) DEFAULT NULL COMMENT '是否危险品',
  `in_bound_quantity` char(100) DEFAULT NULL COMMENT '待入库数量',
  `asin_limit` char(100) DEFAULT NULL COMMENT '产品限制',
  `inbound_recommend_quantity` char(100) DEFAULT NULL COMMENT '建议待入库数量（即发货量）',
  `qty_to_be_charged_ltsf_6mo` char(100) DEFAULT NULL COMMENT '长期仓储费',
  `projected_ltsf_6mo` char(100) DEFAULT NULL COMMENT '下期长期仓储费',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`authid`,`marketplaceid`,`sku`) USING BTREE,
  KEY `index1` (`shopid`,`authid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_inventory_planning 结构
CREATE TABLE IF NOT EXISTS `t_amz_inventory_planning` (
  `snapshot_date` date DEFAULT NULL COMMENT '快照时间',
  `sku` char(50) NOT NULL COMMENT '平台产品SKU',
  `amazonauthid` bigint(20) unsigned NOT NULL COMMENT '授权ID',
  `countrycode` char(3) NOT NULL COMMENT '国家编码',
  `fnsku` char(20) DEFAULT NULL COMMENT '仓库编码（产品条码）',
  `asin` char(20) DEFAULT NULL COMMENT '商品编码',
  `condition` char(15) NOT NULL COMMENT '商品状况',
  `available` int(11) DEFAULT NULL COMMENT '可用库存数量',
  `pending_removal_quantity` int(11) DEFAULT NULL COMMENT '等待移除的库存',
  `currency` char(5) DEFAULT NULL COMMENT '币种',
  `inv_age_0_to_90_days` int(11) DEFAULT NULL COMMENT '90天以内库龄数量',
  `inv_age_91_to_180_days` int(11) DEFAULT NULL COMMENT '91-180天以内库龄数量',
  `inv_age_181_to_270_days` int(11) DEFAULT NULL COMMENT '181-270天以内库龄数量',
  `inv_age_271_to_365_days` int(11) DEFAULT NULL COMMENT '271-365天以内库龄数量',
  `inv_age_365_plus_days` int(11) DEFAULT NULL COMMENT '365天以上库龄数量',
  `qty_to_be_charged_ltsf_11_mo` decimal(20,6) DEFAULT NULL COMMENT '长期仓储费：超过11个月的仓库费',
  `projected_ltsf_11_mo` decimal(20,6) DEFAULT NULL COMMENT '预估下期仓储费：超过11个月的',
  `qty_to_be_charged_ltsf_12_mo` decimal(20,6) DEFAULT NULL COMMENT '长期仓储费：超过12个月的仓库费',
  `estimated_ltsf_next_charge` decimal(20,6) DEFAULT NULL COMMENT '预估下期长期仓储费',
  `units_shipped_t7` int(11) DEFAULT NULL COMMENT '最近7天发货',
  `units_shipped_t30` int(11) DEFAULT NULL COMMENT '最近30天发货',
  `units_shipped_t60` int(11) DEFAULT NULL COMMENT '最近60天发货',
  `units_shipped_t90` int(11) DEFAULT NULL COMMENT '最近90天发货',
  `alert` varchar(50) DEFAULT NULL COMMENT '低流量或低转化率提醒',
  `your_price` decimal(20,6) DEFAULT NULL COMMENT '您发布的商品价格',
  `sales_price` decimal(20,6) DEFAULT NULL COMMENT '您促销商品的价格',
  `lowest_price_new_plus_shipping` decimal(20,6) DEFAULT NULL COMMENT '此商品的新品在亚马逊商城的最低价格（含运费）',
  `lowest_price_used` decimal(20,6) DEFAULT NULL COMMENT '此商品的二手商品在亚马逊商城的最低价格（含运费）',
  `recommended_action` varchar(50) DEFAULT NULL COMMENT '建议您对库存执行的操作。建议的依据是您当前的买家需求以及采取措施是否比不采取任何措施花费更低',
  `healthy_inventory_level` int(11) DEFAULT NULL COMMENT '根据商品需求和您的成本，被认定为不是冗余库存的库存数量。我们的建议是为了帮助您达到这一库存水平。',
  `recommended_sales_price` decimal(20,6) DEFAULT NULL COMMENT '建议售价',
  `recommended_sale_duration_days` int(11) DEFAULT NULL COMMENT '建议售价的天数',
  `recommended_removal_quantity` int(11) DEFAULT NULL COMMENT '建议移除数量',
  `estimated_cost_savings_of_recommended_actions` decimal(20,6) DEFAULT NULL COMMENT '可以节约成本:与不采取任何措施（需要为相应库存支付仓储费）相比，采取建议措施预计可以节约的成本',
  `sell_through` decimal(20,6) DEFAULT NULL COMMENT '售罄率：较高的售罄率可能表明仍有库存机会，而较低的数值可能意味着你已经过度投资',
  `item_volume` decimal(20,6) DEFAULT NULL COMMENT '商品的体积',
  `volume_unit_measurement` varchar(50) DEFAULT NULL COMMENT '商品体积的计量单位',
  `storage_type` varchar(50) DEFAULT NULL COMMENT '仓储类型分类：有六种仓储类型：标准尺寸、大件、服装、鞋靴、易燃物或气溶胶',
  `storage_volume` decimal(20,6) DEFAULT NULL COMMENT '存储体积',
  `product_group` varchar(50) DEFAULT NULL COMMENT '产品分类',
  `sales_rank` int(11) DEFAULT NULL COMMENT '销售排名',
  `days_of_supply` int(11) DEFAULT NULL COMMENT '持续供货(天）',
  `estimated_excess_quantity` int(11) DEFAULT NULL COMMENT '预计冗余商品数量：根据您当前的销售速度和买家需求预测得出的积压商品数量。保留这些商品并支付仓储费比通过降低或移除价格来降低商品数量的成本更高',
  `weeks_of_cover_t30` int(11) DEFAULT NULL COMMENT '30天周转',
  `weeks_of_cover_t90` int(11) DEFAULT NULL COMMENT '90天周转',
  `featuredoffer_price` decimal(20,6) DEFAULT NULL COMMENT '价格',
  `sales_shipped_last_7_days` decimal(20,6) DEFAULT NULL COMMENT '7天销售额',
  `sales_shipped_last_30_days` decimal(20,6) DEFAULT NULL COMMENT '30天销售额',
  `sales_shipped_last_60_days` decimal(20,6) DEFAULT NULL COMMENT '60天销售额',
  `sales_shipped_last_90_days` decimal(20,6) DEFAULT NULL COMMENT '90天销售额',
  `inv_age_0_to_30_days` int(11) DEFAULT NULL COMMENT '30天以内库龄数量',
  `inv_age_31_to_60_days` int(11) DEFAULT NULL COMMENT '31-60天以内库龄数量',
  `inv_age_61_to_90_days` int(11) DEFAULT NULL COMMENT '61-90天以内库龄数量',
  `inv_age_181_to_330_days` int(11) DEFAULT NULL COMMENT '181-330天以内库龄数量',
  `inv_age_331_to_365_days` int(11) DEFAULT NULL COMMENT '331-365天以内库龄数量',
  `estimated_storage_cost_next_month` decimal(20,6) DEFAULT NULL COMMENT '预估下个月仓储费',
  `inbound_quantity` int(11) DEFAULT NULL COMMENT '待入库总数',
  `inbound_working` int(11) DEFAULT NULL COMMENT '待入库-发货中',
  `inbound_shipped` int(11) DEFAULT NULL COMMENT '待入库-已发货',
  `inbound_received` int(11) DEFAULT NULL COMMENT '待入库-接收中',
  `no_sale_last_6_months` int(11) DEFAULT NULL COMMENT '6个月内没有售出的数量',
  `reserved_quantity` int(11) DEFAULT NULL COMMENT '总的预留库存',
  `unfulfillable_quantity` int(11) DEFAULT NULL COMMENT '不可售库存',
  `afn_researching_quantity` int(11) DEFAULT NULL COMMENT '正在找回的库存',
  `afn_reserved_future_supply` int(11) DEFAULT NULL COMMENT '预留库存（为有意向购买者保留）',
  `afn_future_supply_buyable` int(11) DEFAULT NULL COMMENT '未来可售（即将到货的库存）',
  `quantity_to_be_charged_ais_181_210_days` int(11) DEFAULT NULL,
  `quantity_to_be_charged_ais_211_240_days` int(11) DEFAULT NULL,
  `quantity_to_be_charged_ais_241_270_days` int(11) DEFAULT NULL,
  `quantity_to_be_charged_ais_271_300_days` int(11) DEFAULT NULL,
  `quantity_to_be_charged_ais_301_330_days` int(11) DEFAULT NULL,
  `quantity_to_be_charged_ais_331_365_days` int(11) DEFAULT NULL,
  `quantity_to_be_charged_ais_365_plus_days` int(11) DEFAULT NULL,
  `estimated_ais_181_210_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_211_240_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_241_270_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_271_300_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_301_330_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_331_365_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_365_plus_days` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`,`countrycode`,`sku`,`condition`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_merchant_shipping_group 结构
CREATE TABLE IF NOT EXISTS `t_amz_merchant_shipping_group` (
  `id` char(50) NOT NULL,
  `amazonauthid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `marketplaceid` char(50) NOT NULL DEFAULT '0',
  `name` char(50) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`amazonauthid`) USING BTREE,
  KEY `amazonauthid_marketplaceid_name` (`amazonauthid`,`marketplaceid`,`name`)
) ENGINE=InnoDB COMMENT='自配送买家运费模板';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_notifications 结构
CREATE TABLE IF NOT EXISTS `t_amz_notifications` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `notifications` char(50)  DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `isrun` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_notifications_destination 结构
CREATE TABLE IF NOT EXISTS `t_amz_notifications_destination` (
  `destinationid` char(36) NOT NULL COMMENT '接受消息对象的ID',
  `name` varchar(256) DEFAULT NULL COMMENT '接受消息对象的名称',
  `resource_sqs_arn` varchar(1000) DEFAULT NULL COMMENT '消息队列arn',
  `resource_event_bridge_region` varchar(1000) DEFAULT NULL COMMENT '本地bridge区域',
  `resource_event_bridge_accountid` varchar(1000) DEFAULT NULL COMMENT '本地bridge账号',
  `resource_event_bridge_name` varchar(50) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `awsregion` char(50) DEFAULT NULL,
  PRIMARY KEY (`destinationid`) USING BTREE,
  KEY `name_amazonauthid` (`awsregion`,`name`(191)) USING BTREE
) ENGINE=InnoDB  COMMENT='亚马逊Destination 亚马逊消息接受对象';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_notifications_subscriptions 结构
CREATE TABLE IF NOT EXISTS `t_amz_notifications_subscriptions` (
  `subscriptionId` char(36) NOT NULL COMMENT '订阅ID',
  `amazonauthid` bigint(20) unsigned NOT NULL COMMENT '授权ID',
  `eventFilterType` char(50) NOT NULL COMMENT '订阅类型',
  `payloadVersion` char(50) DEFAULT NULL COMMENT '版本',
  `destinationId` char(36) NOT NULL COMMENT '接受信息的目标',
  `aggregationSettings` char(36) DEFAULT NULL COMMENT '聚合时间',
  `marketplaceIds` varchar(500) DEFAULT NULL COMMENT '站点',
  `refreshtime` datetime DEFAULT NULL COMMENT '刷新时间',
  PRIMARY KEY (`subscriptionId`,`amazonauthid`,`eventFilterType`) USING BTREE,
  KEY `eventFilterType_destinationId_amazonauthid` (`amazonauthid`,`eventFilterType`,`destinationId`)
) ENGINE=InnoDB  COMMENT='订阅消息对象';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_orders_address 结构
CREATE TABLE IF NOT EXISTS `t_amz_orders_address` (
  `amazon_order_id` char(20) NOT NULL,
  `amazonAuthId` bigint(20) unsigned DEFAULT NULL,
  `marketplaceId` char(36) DEFAULT NULL,
  `name` varchar(500) DEFAULT NULL,
  `phone` char(20) DEFAULT NULL,
  `address_line1` varchar(255) DEFAULT NULL,
  `address_line2` varchar(255) DEFAULT NULL,
  `address_line3` varchar(255) DEFAULT NULL,
  `address_type` varchar(255) DEFAULT NULL,
  `city` char(60) DEFAULT NULL,
  `country_code` char(10) DEFAULT NULL,
  `county` char(50) DEFAULT NULL,
  `district` char(20) DEFAULT NULL,
  `municipality` char(20) DEFAULT NULL,
  `postal_code` char(20) DEFAULT NULL,
  `state_or_region` char(60) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`) USING BTREE,
  KEY `amazonAuthId_marketplaceId_name` (`amazonAuthId`,`marketplaceId`,`opttime`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_orders_invoice_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_orders_invoice_report` (
  `order_id` char(30) NOT NULL DEFAULT '',
  `order_item_id` char(30) NOT NULL DEFAULT '',
  `purchase_date` datetime NOT NULL,
  `payments_date` datetime DEFAULT NULL,
  `buyer_email` varchar(50) DEFAULT NULL,
  `sales_channel` varchar(50) DEFAULT NULL,
  `buyer_name` varchar(100) DEFAULT NULL,
  `buyer_phone_number` char(30) DEFAULT NULL,
  `sku` char(50) DEFAULT NULL,
  `quantity_purchased` int(11) DEFAULT NULL,
  `currency` char(5) DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `ship_service_level` char(20) DEFAULT NULL,
  `recipient_name` char(50) DEFAULT NULL,
  `ship_address_1` varchar(255) DEFAULT NULL,
  `ship_address_2` varchar(255) DEFAULT NULL,
  `ship_address_3` varchar(255) DEFAULT NULL,
  `ship_city` varchar(60) DEFAULT NULL,
  `ship_state` varchar(60) DEFAULT NULL,
  `ship_postal_code` char(20) DEFAULT NULL,
  `ship_country` char(10) DEFAULT NULL,
  `ship_phone_number` char(30) DEFAULT NULL,
  `delivery_start_date` datetime DEFAULT NULL,
  `delivery_end_date` datetime DEFAULT NULL,
  `delivery_time_zone` varchar(10) DEFAULT NULL,
  `delivery_instructions` varchar(500) DEFAULT NULL,
  `is_business_order` bit(1) DEFAULT NULL,
  `price_designation` char(20) DEFAULT NULL,
  `is_iba` bit(1) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `refreshtime` datetime NOT NULL,
  PRIMARY KEY (`order_id`,`order_item_id`) USING BTREE,
  KEY `purchase_date_sales_channel_amazonAuthId` (`amazonAuthId`,`sales_channel`,`purchase_date`),
  KEY `purchase_date_sku` (`sku`,`purchase_date`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_order_buyer_ship_address 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_buyer_ship_address` (
  `id` bigint(20) unsigned NOT NULL,
  `amazon_order_id` varchar(30) DEFAULT NULL,
  `marketplaceId` char(36) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned DEFAULT NULL,
  `name` varchar(500) DEFAULT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `address3` varchar(255) DEFAULT NULL,
  `city` varchar(60) DEFAULT NULL,
  `county` char(50) DEFAULT NULL COMMENT '区县',
  `district` char(20) DEFAULT NULL,
  `state` varchar(60) DEFAULT NULL,
  `postalcode` char(20) DEFAULT NULL,
  `countrycode` char(10) DEFAULT NULL,
  `phone` char(30) DEFAULT NULL,
  `email` char(50) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `amazonAuthId` (`amazonAuthId`,`marketplaceId`,`name`(191)) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_order_item 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_item` (
  `id` bigint(20) unsigned NOT NULL,
  `amazon_order_id` varchar(30) NOT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `orderItemId` varchar(30) NOT NULL,
  `asin` char(36) DEFAULT NULL,
  `sku` char(50) NOT NULL,
  `title` varchar(500) DEFAULT NULL,
  `QuantityOrdered` int(11) DEFAULT NULL,
  `QuantityShipped` int(11) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `promotion_ids` varchar(500) DEFAULT NULL,
  `CODFee` decimal(10,2) DEFAULT NULL,
  `CODFeeDiscount` decimal(10,2) DEFAULT NULL,
  `GiftMessageText` varchar(500) DEFAULT NULL,
  `GiftWrapLevel` char(30)  DEFAULT NULL,
  `ConditionId` char(20)  DEFAULT NULL,
  `ConditionSubtypeId` char(20)  DEFAULT NULL,
  `ConditionNote` varchar(255)  DEFAULT NULL,
  `ScheduledDeliveryStartDate` datetime DEFAULT NULL,
  `ScheduledDeliveryEndDate` datetime DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `marketplaceId` char(36) DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`orderItemId`) USING BTREE,
  KEY `sku` (`sku`) USING BTREE,
  KEY `amazonAuthId_shopid_groupid` (`amazonAuthId`,`marketplaceId`,`purchase_date`) USING BTREE
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_order_item_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_item_archive` (
  `id` bigint(20) unsigned NOT NULL,
  `amazon_order_id` varchar(30) NOT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `orderItemId` varchar(30) NOT NULL,
  `asin` char(36) DEFAULT NULL,
  `sku` char(50) NOT NULL,
  `title` varchar(500) DEFAULT NULL,
  `QuantityOrdered` int(11) DEFAULT NULL,
  `QuantityShipped` int(11) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `promotion_ids` varchar(500) DEFAULT NULL,
  `CODFee` decimal(10,2) DEFAULT NULL,
  `CODFeeDiscount` decimal(10,2) DEFAULT NULL,
  `GiftMessageText` varchar(500) DEFAULT NULL,
  `GiftWrapLevel` char(30)  DEFAULT NULL,
  `ConditionId` char(20)  DEFAULT NULL,
  `ConditionSubtypeId` char(20)  DEFAULT NULL,
  `ConditionNote` varchar(255)  DEFAULT NULL,
  `ScheduledDeliveryStartDate` datetime DEFAULT NULL,
  `ScheduledDeliveryEndDate` datetime DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `marketplaceId` char(36) DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`orderItemId`) USING BTREE,
  KEY `sku` (`sku`) USING BTREE,
  KEY `amazonAuthId_shopid_groupid` (`amazonAuthId`,`marketplaceId`,`purchase_date`) USING BTREE
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_order_item_bkp20220721 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_item_bkp20220721` (
  `id` bigint(20) unsigned NOT NULL,
  `amazon_order_id` varchar(30) NOT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `orderItemId` varchar(30) NOT NULL,
  `asin` char(36) DEFAULT NULL,
  `sku` char(50) NOT NULL,
  `title` varchar(500) DEFAULT NULL,
  `QuantityOrdered` int(11) DEFAULT NULL,
  `QuantityShipped` int(11) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `promotion_ids` varchar(500) DEFAULT NULL,
  `CODFee` decimal(10,2) DEFAULT NULL,
  `CODFeeDiscount` decimal(10,2) DEFAULT NULL,
  `GiftMessageText` varchar(500) DEFAULT NULL,
  `GiftWrapLevel` char(30)  DEFAULT NULL,
  `ConditionId` char(20)  DEFAULT NULL,
  `ConditionSubtypeId` char(20)  DEFAULT NULL,
  `ConditionNote` varchar(255)  DEFAULT NULL,
  `ScheduledDeliveryStartDate` datetime DEFAULT NULL,
  `ScheduledDeliveryEndDate` datetime DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `marketplaceId` char(36) DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`orderItemId`) USING BTREE,
  KEY `sku` (`sku`) USING BTREE,
  KEY `amazonAuthId_shopid_groupid` (`amazonAuthId`,`marketplaceId`,`purchase_date`) USING BTREE
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_order_main 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_main` (
  `id` bigint(20) unsigned NOT NULL,
  `amazon_order_id` varchar(20) NOT NULL,
  `seller_order_id` varchar(30) DEFAULT NULL,
  `replaced_orderid` varchar(20) DEFAULT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_status` char(20) DEFAULT NULL,
  `fulfillment_channel` char(10) DEFAULT NULL,
  `sales_channel` char(30) DEFAULT NULL,
  `order_channel` char(30) DEFAULT NULL,
  `ship_service_level` varchar(30) DEFAULT NULL COMMENT '货件服务水平',
  `buyer_shipping_address_id` bigint(20) unsigned DEFAULT NULL COMMENT '买家收货地址id',
  `currency` char(10) DEFAULT NULL,
  `order_total` decimal(10,2) DEFAULT NULL COMMENT '订单的总费用',
  `numberOfItemsShipped` int(10) DEFAULT NULL COMMENT '已配送的商品数量。',
  `numberOfItemsUnshipped` int(10) DEFAULT NULL COMMENT '未配送的商品数量。',
  `paymentMethod` varchar(20) DEFAULT NULL COMMENT 'COD 订单的次级付款方式',
  `payment_execution_detail_item` decimal(10,0) DEFAULT NULL COMMENT '使用同级PaymentMethod响应元素指明的次级付款方式支付的金额',
  `buyer_email` varchar(60) DEFAULT NULL,
  `buyer_name` tinyblob,
  `shipment_serviceLevel_category` varchar(50) DEFAULT NULL COMMENT '订单的配送服务级别分类。',
  `fulfillment_supply_sourceid` varchar(50) DEFAULT NULL,
  `CbaDisplayableShippingLabel` char(20) DEFAULT NULL COMMENT '卖家自定义的配送方式，属于Checkout by Amazon (CBA) 所支持的四种标准配送设置中的一种',
  `orderType` char(50) DEFAULT NULL,
  `earliestShipDate` datetime DEFAULT NULL,
  `latestShipDate` datetime DEFAULT NULL,
  `earliestDeliveryDate` datetime DEFAULT NULL,
  `latestDeliveryDate` datetime DEFAULT NULL,
  `promise_response_duedate` datetime DEFAULT NULL,
  `isBusinessOrder` bit(1) DEFAULT b'0',
  `remark` varchar(255) DEFAULT NULL,
  `hasItem` bit(1) DEFAULT b'0',
  `marketplaceId` char(36) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`amazon_order_id`,`amazonAuthId`) USING BTREE,
  KEY `amazonauth` (`amazonAuthId`,`purchase_date`,`hasItem`) USING BTREE
) ENGINE=InnoDB  COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_order_main_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_main_archive` (
  `id` bigint(20) unsigned NOT NULL,
  `amazon_order_id` varchar(20) NOT NULL,
  `seller_order_id` varchar(30) DEFAULT NULL,
  `replaced_orderid` varchar(20) DEFAULT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_status` char(20) DEFAULT NULL,
  `fulfillment_channel` char(10) DEFAULT NULL,
  `sales_channel` char(30) DEFAULT NULL,
  `order_channel` char(30) DEFAULT NULL,
  `ship_service_level` varchar(30) DEFAULT NULL COMMENT '货件服务水平',
  `buyer_shipping_address_id` bigint(20) unsigned DEFAULT NULL COMMENT '买家收货地址id',
  `currency` char(10) DEFAULT NULL,
  `order_total` decimal(10,2) DEFAULT NULL COMMENT '订单的总费用',
  `numberOfItemsShipped` int(10) DEFAULT NULL COMMENT '已配送的商品数量。',
  `numberOfItemsUnshipped` int(10) DEFAULT NULL COMMENT '未配送的商品数量。',
  `paymentMethod` varchar(20) DEFAULT NULL COMMENT 'COD 订单的次级付款方式',
  `payment_execution_detail_item` decimal(10,0) DEFAULT NULL COMMENT '使用同级PaymentMethod响应元素指明的次级付款方式支付的金额',
  `buyer_email` varchar(60) DEFAULT NULL,
  `buyer_name` tinyblob,
  `shipment_serviceLevel_category` varchar(50) DEFAULT NULL COMMENT '订单的配送服务级别分类。',
  `fulfillment_supply_sourceid` varchar(50) DEFAULT NULL,
  `CbaDisplayableShippingLabel` char(20) DEFAULT NULL COMMENT '卖家自定义的配送方式，属于Checkout by Amazon (CBA) 所支持的四种标准配送设置中的一种',
  `orderType` char(50) DEFAULT NULL,
  `earliestShipDate` datetime DEFAULT NULL,
  `latestShipDate` datetime DEFAULT NULL,
  `earliestDeliveryDate` datetime DEFAULT NULL,
  `latestDeliveryDate` datetime DEFAULT NULL,
  `promise_response_duedate` datetime DEFAULT NULL,
  `isBusinessOrder` bit(1) DEFAULT b'0',
  `remark` varchar(255) DEFAULT NULL,
  `hasItem` bit(1) DEFAULT b'0',
  `marketplaceId` char(36) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`amazon_order_id`) USING BTREE,
  KEY `amazonauth` (`amazonAuthId`,`purchase_date`,`hasItem`) USING BTREE
) ENGINE=InnoDB  COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_order_remove_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_order_remove_report` (
  `order_id` varchar(50) NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_type` char(20) DEFAULT NULL,
  `order_status` char(20) DEFAULT NULL,
  `sku` char(50) NOT NULL,
  `fnsku` char(50) DEFAULT NULL,
  `disposition` char(30) DEFAULT NULL,
  `requested_quantity` int(11) DEFAULT NULL,
  `cancelled_quantity` int(11) DEFAULT NULL,
  `disposed_quantity` int(11) DEFAULT NULL,
  `shipped_quantity` int(11) DEFAULT NULL,
  `in_process_quantity` int(11) DEFAULT NULL,
  `removal_fee` decimal(10,2) DEFAULT NULL,
  `currency` char(5) DEFAULT NULL,
  PRIMARY KEY (`order_id`,`sku`) USING BTREE,
  KEY `purchase_date` (`purchase_date`) USING BTREE,
  KEY `sku` (`sku`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_pdt_price_opt 结构
CREATE TABLE IF NOT EXISTS `t_amz_pdt_price_opt` (
  `pid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `feed_submission_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `standardprice` decimal(10,4) DEFAULT NULL,
  `oldstandardprice` decimal(10,4) DEFAULT NULL,
  `saleprice` decimal(10,4) DEFAULT NULL,
  `businessprice` decimal(10,4) DEFAULT NULL,
  `businesstype` char(10) DEFAULT NULL,
  `businesslist` varchar(1000) DEFAULT NULL,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `ftype` char(10) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`pid`,`feed_submission_id`),
  KEY `opttime` (`opttime`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_po_rpt_day 结构
CREATE TABLE IF NOT EXISTS `t_amz_po_rpt_day` (
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `purchase_date` date NOT NULL,
  `sales_channel` char(25) NOT NULL,
  `sku` char(40) NOT NULL,
  `order_status` char(10) NOT NULL,
  `asin` char(36) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `ordersum` int(11) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `price` decimal(15,2) DEFAULT NULL,
  `pricermb` decimal(15,2) DEFAULT NULL,
  `ship_country` char(10) DEFAULT NULL,
  PRIMARY KEY (`sku`,`sales_channel`,`purchase_date`,`order_status`,`amazonAuthId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_active 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_active` (
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL COMMENT '站点',
  `sku` varchar(50) NOT NULL COMMENT '用户码sku',
  `asin` char(36) DEFAULT NULL COMMENT '唯一码asin',
  `openDate` datetime DEFAULT NULL COMMENT '创建日期',
  `price` decimal(14,2) DEFAULT NULL COMMENT '价格',
  PRIMARY KEY (`amazonAuthId`,`marketplaceid`,`sku`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_active_daynum 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_active_daynum` (
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(14) NOT NULL,
  `byday` date NOT NULL,
  `num` int(11) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`marketplaceid`,`byday`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_lock 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_lock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `isused` bit(1) DEFAULT NULL COMMENT '是否可用',
  `num` int(11) DEFAULT NULL COMMENT '询问次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_pageviews 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_pageviews` (
  `amazonAuthid` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `SKU` char(50) NOT NULL,
  `byday` date NOT NULL,
  `child_asin` char(36) NOT NULL,
  `parent_asin` char(36) DEFAULT NULL,
  `Sessions` int(11) DEFAULT NULL,
  `Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Page_Views` int(11) DEFAULT NULL,
  `Page_Views_Percentage` decimal(10,2) DEFAULT NULL,
  `Buy_Box_Percentage` decimal(10,2) DEFAULT NULL,
  `Units_Ordered` int(11) DEFAULT NULL,
  `Units_Ordered_B2B` int(11) DEFAULT NULL,
  `Unit_Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Unit_Session_Percentage_B2B` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales_B2B` decimal(10,2) DEFAULT NULL,
  `Total_Order_Items` int(11) DEFAULT NULL,
  `Total_Order_Items_B2B` int(11) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthid`,`marketplaceid`,`byday`,`SKU`,`child_asin`) USING BTREE,
  KEY `amazonAuthid_marketplaceid_byday` (`amazonAuthid`,`marketplaceid`,`byday`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_pageviews_download 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_pageviews_download` (
  `id` bigint(20) unsigned NOT NULL,
  `amazonAuthid` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `SKU` char(50) NOT NULL,
  `byday` date NOT NULL,
  `parent_asin` char(36) NOT NULL,
  `child_asin` char(36) DEFAULT NULL,
  `Sessions` int(10) DEFAULT NULL COMMENT '访问量（点击量）',
  `Session_Percentage` decimal(10,2) DEFAULT NULL COMMENT '访问比例',
  `Page_Views` int(10) DEFAULT NULL COMMENT '浏览量',
  `Page_Views_Percentage` decimal(10,2) DEFAULT NULL,
  `Buy_Box_Percentage` decimal(10,2) DEFAULT NULL,
  `Units_Ordered` int(10) DEFAULT NULL COMMENT '销量',
  `Units_Ordered_B2B` int(10) DEFAULT NULL,
  `Unit_Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Unit_Session_Percentage_B2B` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales_B2B` decimal(10,2) DEFAULT NULL,
  `Total_Order_Items` int(10) DEFAULT NULL,
  `Total_Order_Items_B2B` int(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `key` (`amazonAuthid`,`marketplaceid`,`byday`,`SKU`,`parent_asin`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='流量报表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_pageviews_month 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_pageviews_month` (
  `amazonAuthid` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `SKU` char(36) NOT NULL,
  `byday` date NOT NULL,
  `parent_asin` char(36) DEFAULT NULL,
  `child_asin` char(36) NOT NULL,
  `Sessions` int(11) DEFAULT NULL,
  `Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Page_Views` int(11) DEFAULT NULL,
  `Page_Views_Percentage` decimal(10,2) DEFAULT NULL,
  `Buy_Box_Percentage` decimal(10,2) DEFAULT NULL,
  `Units_Ordered` int(11) DEFAULT NULL,
  `Units_Ordered_B2B` int(11) DEFAULT NULL,
  `Unit_Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Unit_Session_Percentage_B2B` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales_B2B` decimal(10,2) DEFAULT NULL,
  `Total_Order_Items` int(11) DEFAULT NULL,
  `Total_Order_Items_B2B` int(11) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthid`,`marketplaceid`,`SKU`,`byday`,`child_asin`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_pageviews_week 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_pageviews_week` (
  `amazonAuthid` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `SKU` char(36) NOT NULL,
  `byday` date NOT NULL,
  `parent_asin` char(36) DEFAULT NULL,
  `child_asin` char(36) NOT NULL,
  `Sessions` int(11) DEFAULT NULL,
  `Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Page_Views` int(11) DEFAULT NULL,
  `Page_Views_Percentage` decimal(10,2) DEFAULT NULL,
  `Buy_Box_Percentage` decimal(10,2) DEFAULT NULL,
  `Units_Ordered` int(11) DEFAULT NULL,
  `Units_Ordered_B2B` int(11) DEFAULT NULL,
  `Unit_Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Unit_Session_Percentage_B2B` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales_B2B` decimal(10,2) DEFAULT NULL,
  `Total_Order_Items` int(11) DEFAULT NULL,
  `Total_Order_Items_B2B` int(11) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthid`,`marketplaceid`,`SKU`,`byday`,`child_asin`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_price_record 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_price_record` (
  `pid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `opttime` datetime NOT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `lowestprice` decimal(10,4) DEFAULT NULL,
  `price` decimal(10,4) DEFAULT NULL,
  `refprice` decimal(10,4) DEFAULT NULL,
  `oldprice` decimal(10,4) DEFAULT NULL,
  `shipprice` decimal(10,4) DEFAULT NULL,
  `oldshipprice` decimal(10,4) DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `ftype` tinyint(10) DEFAULT '0' COMMENT '1:永久调价，2，临时调价，3，商务调价',
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`pid`,`opttime`) USING BTREE,
  KEY `opttime` (`opttime`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='用于记录调价';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_refresh 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_refresh` (
  `pid` bigint(20) unsigned NOT NULL,
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `detail_refresh_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `price_refresh_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `catalog_refresh_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `sku` char(50) NOT NULL,
  `asin` char(50) DEFAULT NULL,
  `marketplaceid` char(15) NOT NULL,
  `isparent` bit(1) DEFAULT b'0',
  `notfound` bit(1) DEFAULT b'0',
  PRIMARY KEY (`pid`) USING BTREE,
  UNIQUE KEY `amazonauthid` (`amazonauthid`,`marketplaceid`,`sku`) USING BTREE,
  KEY `amazonauthid_price_refresh_time_marketplaceid_isparent` (`amazonauthid`,`isparent`,`price_refresh_time`,`marketplaceid`),
  KEY `ftime` (`amazonauthid`,`detail_refresh_time`) USING BTREE,
  KEY `amazonauthid_catalog_refresh_time` (`catalog_refresh_time`,`asin`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_refreshtime 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_refreshtime` (
  `pid` bigint(20) unsigned NOT NULL,
  `item` int(10) unsigned NOT NULL COMMENT '0:GetCompetitivePricingForSKURequest;\\r\\n1:GetLowestPricedOffersForSKU;\\r\\n2:captureProductDetail;3:captureProductCategoriesBySku',
  `ftime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`pid`,`item`),
  KEY `ftime` (`ftime`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_sales_plan 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_sales_plan` (
  `id` bigint(19) unsigned NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `msku` char(50) DEFAULT NULL,
  `shopid` bigint(19) unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `amazonauthid` bigint(19) unsigned DEFAULT NULL,
  `shipday` int(10) DEFAULT NULL,
  `delivery_cycle` int(10) DEFAULT NULL,
  `avgsales` int(10) DEFAULT NULL,
  `needship` int(10) DEFAULT NULL,
  `needshipfba` int(10) DEFAULT NULL,
  `needpurchase` int(10) DEFAULT NULL,
  `ship_min_cycle_sale` int(10) DEFAULT NULL,
  `salesday` int(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `short_time` date DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shipday` (`shipday`) USING BTREE,
  KEY `shopid_groupid` (`groupid`,`marketplaceid`,`sku`) USING BTREE,
  KEY `msku_shopid` (`msku`,`shopid`) USING BTREE,
  KEY `marketplaceid_groupid_amazonauthid` (`groupid`,`marketplaceid`,`amazonauthid`,`sku`,`opttime`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_sales_plan_ship_item 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_sales_plan_ship_item` (
  `id` bigint(19) unsigned NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `msku` char(50) DEFAULT NULL,
  `shopid` bigint(19) unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `amazonauthid` bigint(20) unsigned DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `overseaid` bigint(20) unsigned DEFAULT NULL,
  `transtype` bigint(20) unsigned DEFAULT NULL,
  `batchnumber` char(30) DEFAULT NULL,
  `amount` int(10) DEFAULT NULL,
  `aftersalesday` int(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shipday` (`amount`) USING BTREE,
  KEY `shopid_groupid` (`shopid`,`groupid`,`amazonauthid`,`marketplaceid`,`sku`) USING BTREE,
  KEY `msku_shopid` (`shopid`,`groupid`,`warehouseid`,`msku`) USING BTREE,
  KEY `groupid_batchnumber` (`batchnumber`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_product_sales_plan_ship_item_history 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_sales_plan_ship_item_history` (
  `id` bigint(19) unsigned NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `msku` char(50) DEFAULT NULL,
  `shopid` bigint(19) unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `amazonauthid` bigint(20) unsigned DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `overseaid` bigint(20) unsigned DEFAULT NULL,
  `transtype` bigint(20) unsigned DEFAULT NULL,
  `batchnumber` char(30) DEFAULT NULL,
  `amount` int(10) DEFAULT NULL,
  `aftersalesday` int(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shipday` (`amount`) USING BTREE,
  KEY `shopid_groupid` (`shopid`,`groupid`,`amazonauthid`,`marketplaceid`,`sku`) USING BTREE,
  KEY `msku_shopid` (`shopid`,`groupid`,`warehouseid`,`msku`) USING BTREE,
  KEY `groupid_transtype` (`batchnumber`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_report_request_type 结构
CREATE TABLE IF NOT EXISTS `t_amz_report_request_type` (
  `id` int(11) NOT NULL COMMENT 'ID',
  `cname` varchar(100) NOT NULL DEFAULT '' COMMENT '中文名称',
  `ename` varchar(100) NOT NULL DEFAULT '' COMMENT '英文名称',
  `code` varchar(100) NOT NULL DEFAULT '' COMMENT '报表编码',
  `description` varchar(100) NOT NULL DEFAULT '' COMMENT '描述',
  `bean` varchar(50) NOT NULL DEFAULT '' COMMENT '报表处理类',
  `day` int(11) NOT NULL DEFAULT '0' COMMENT '报表默认请求天数',
  `disabled` bit(1) DEFAULT b'0' COMMENT '是否可用',
  `reportOptions` varchar(50) DEFAULT NULL COMMENT '报表默认参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`) USING BTREE
) ENGINE=InnoDB  COMMENT='亚马逊报表类型';

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
  `quantity` int(11) DEFAULT NULL,
  `fulfillment_center_id` char(20) DEFAULT NULL,
  `detailed_disposition` char(20) DEFAULT NULL,
  `reason` varchar(100) DEFAULT NULL,
  `status` char(50) DEFAULT NULL,
  `license_plate_number` varchar(1000) DEFAULT NULL,
  `customer_comments` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`sellerid`,`sku`,`order_id`,`return_date`) USING BTREE,
  KEY `return_date` (`return_date`) USING BTREE,
  KEY `return_date_sellerid_marketplaceid` (`sellerid`,`marketplaceid`,`return_date`) USING BTREE,
  KEY `order_id` (`order_id`) USING BTREE,
  KEY `sku` (`sku`,`return_date`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_returns_report_bkp20230814 结构
CREATE TABLE IF NOT EXISTS `t_amz_returns_report_bkp20230814` (
  `sku` char(50) NOT NULL,
  `return_date` datetime NOT NULL,
  `order_id` char(50) NOT NULL,
  `sellerid` char(16) NOT NULL,
  `marketplaceid` char(36) DEFAULT NULL,
  `asin` char(40) DEFAULT NULL,
  `fnsku` char(40) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `fulfillment_center_id` char(20) DEFAULT NULL,
  `detailed_disposition` char(20) DEFAULT NULL,
  `reason` varchar(100) DEFAULT NULL,
  `status` char(50) DEFAULT NULL,
  `license_plate_number` varchar(1000) DEFAULT NULL,
  `customer_comments` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`sellerid`,`sku`,`order_id`,`return_date`) USING BTREE,
  KEY `return_date` (`return_date`),
  KEY `return_date_sellerid_marketplaceid` (`sellerid`,`marketplaceid`,`return_date`),
  KEY `order_id` (`order_id`),
  KEY `sku` (`sku`,`return_date`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_returns_report_summary 结构
CREATE TABLE IF NOT EXISTS `t_amz_returns_report_summary` (
  `sku` char(50) NOT NULL,
  `return_date` date NOT NULL,
  `sellerid` char(16) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`sellerid`,`marketplaceid`,`sku`,`return_date`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_rpt_inventory_age 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_inventory_age` (
  `id` bigint(20) unsigned NOT NULL,
  `authid` bigint(20) unsigned DEFAULT NULL,
  `snapshot` datetime DEFAULT NULL,
  `marketplace` char(20) NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `fnsku` char(36) DEFAULT NULL,
  `asin` char(36) DEFAULT NULL,
  `fcondition` char(20) DEFAULT NULL,
  `quantity` int(10) DEFAULT NULL,
  `qty_with_removals_in_progress` int(10) DEFAULT NULL,
  `inv_age_0_to_90_days` int(10) DEFAULT NULL,
  `inv_age_91_to_180_days` int(10) DEFAULT NULL,
  `inv_age_181_to_270_days` int(10) DEFAULT NULL,
  `inv_age_271_to_365_days` int(10) DEFAULT NULL,
  `inv_age_365_plus_days` int(10) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `qty_to_be_charged_ltsf_6_mo` int(10) DEFAULT NULL,
  `projected_ltsf_6_mo` decimal(20,6) DEFAULT NULL,
  `qty_to_be_charged_ltsf_12_mo` int(10) DEFAULT NULL,
  `projected_ltsf_12_mo` decimal(20,6) DEFAULT NULL,
  `units_shipped_last_7_days` int(10) DEFAULT NULL,
  `units_shipped_last_30_days` int(10) DEFAULT NULL,
  `units_shipped_last_60_days` int(10) DEFAULT NULL,
  `units_shipped_last_90_days` int(10) DEFAULT NULL,
  `your_price` decimal(20,6) DEFAULT NULL,
  `sales_price` decimal(20,6) DEFAULT NULL,
  `lowest_price_new` decimal(20,6) DEFAULT NULL,
  `lowest_price_used` decimal(20,6) DEFAULT NULL,
  `recommended_action` varchar(20) DEFAULT NULL,
  `days` int(10) DEFAULT NULL,
  `removal_quantity` decimal(20,6) DEFAULT NULL,
  `estimated_cost_savings_of_recommended_actions` decimal(20,6) DEFAULT NULL,
  `sell_through` decimal(20,6) DEFAULT NULL,
  `item_volume` decimal(20,6) DEFAULT NULL,
  `volume_units` varchar(50) DEFAULT NULL,
  `storage_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `authid_marketplace_sku` (`authid`,`marketplace`,`sku`,`fcondition`) USING BTREE
) ENGINE=InnoDB COMMENT='GET_FBA_INVENTORY_AGED_DATA';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_rpt_inventory_country 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_inventory_country` (
  `id` bigint(20) unsigned NOT NULL COMMENT '唯一ID',
  `sku` char(50) DEFAULT NULL COMMENT 'SKU',
  `fnsku` char(50) DEFAULT NULL COMMENT 'FBA仓库标示码',
  `asin` char(36) DEFAULT NULL COMMENT '商品标示',
  `fcondition` char(30) DEFAULT NULL COMMENT '产品新旧类型',
  `country` char(5) DEFAULT NULL COMMENT '国家',
  `quantity` int(10) DEFAULT NULL COMMENT '库存数量',
  `authid` bigint(20) unsigned DEFAULT NULL COMMENT '授权ID',
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `sku_country_authid` (`sku`,`country`,`authid`,`fcondition`) USING BTREE,
  KEY `country_authid` (`authid`,`country`) USING BTREE
) ENGINE=InnoDB COMMENT='用于存储欧洲各个国家的库存';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_rpt_inventory_detail 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_inventory_detail` (
  `id` bigint(20) unsigned NOT NULL,
  `authid` bigint(20) unsigned DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `bytime` datetime DEFAULT NULL,
  `fnsku` char(11) DEFAULT NULL,
  `asin` char(11) DEFAULT NULL,
  `msku` char(50) DEFAULT NULL,
  `country` char(6) DEFAULT NULL,
  `eventType` char(20) DEFAULT NULL,
  `referenceID` char(15) DEFAULT NULL,
  `fulfillmentCenter` char(10) DEFAULT NULL,
  `disposition` char(20) DEFAULT NULL,
  `reason` char(100) DEFAULT NULL,
  `quantity` int(10) DEFAULT NULL,
  `reconciledQuantity` int(10) DEFAULT NULL,
  `unreconciledQuantity` int(10) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniquesku` (`authid`,`byday`,`msku`,`country`,`eventType`,`referenceID`,`fulfillmentCenter`,`disposition`),
  KEY `authid` (`authid`,`eventType`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_rpt_inventory_summary 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_inventory_summary` (
  `id` bigint(20) unsigned NOT NULL,
  `authid` bigint(20) unsigned DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `location` char(50) DEFAULT NULL,
  `msku` char(100) DEFAULT NULL,
  `fnsku` char(20) DEFAULT NULL,
  `asin` char(20) DEFAULT NULL,
  `disposition` char(20) DEFAULT NULL,
  `startingWarehouseBalance` int(10) DEFAULT NULL,
  `endingWarehouseBalance` int(10) DEFAULT NULL,
  `inTransitBetweenWarehouses` int(11) DEFAULT NULL,
  `receipts` int(11) DEFAULT NULL,
  `customerShipments` int(11) DEFAULT NULL,
  `customerReturns` int(11) DEFAULT NULL,
  `vendorReturns` int(11) DEFAULT NULL,
  `warehouseTransferInOut` int(11) DEFAULT NULL,
  `found` int(11) DEFAULT NULL,
  `lost` int(11) DEFAULT NULL,
  `damaged` int(11) DEFAULT NULL,
  `disposed` int(11) DEFAULT NULL,
  `otherEvents` int(11) DEFAULT NULL,
  `unknownEvents` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `byday_msku_location_authid` (`authid`,`byday`,`disposition`,`location`,`msku`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_rpt_inventory_summary_bkp20230724 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_inventory_summary_bkp20230724` (
  `id` bigint(20) unsigned NOT NULL,
  `authid` bigint(20) unsigned DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `location` char(50) DEFAULT NULL,
  `msku` char(100) DEFAULT NULL,
  `fnsku` char(20) DEFAULT NULL,
  `asin` char(20) DEFAULT NULL,
  `disposition` char(20) DEFAULT NULL,
  `startingWarehouseBalance` int(10) DEFAULT NULL,
  `endingWarehouseBalance` int(10) DEFAULT NULL,
  `inTransitBetweenWarehouses` int(11) DEFAULT NULL,
  `receipts` int(11) DEFAULT NULL,
  `customerShipments` int(11) DEFAULT NULL,
  `customerReturns` int(11) DEFAULT NULL,
  `vendorReturns` int(11) DEFAULT NULL,
  `warehouseTransferInOut` int(11) DEFAULT NULL,
  `found` int(11) DEFAULT NULL,
  `lost` int(11) DEFAULT NULL,
  `damaged` int(11) DEFAULT NULL,
  `disposed` int(11) DEFAULT NULL,
  `otherEvents` int(11) DEFAULT NULL,
  `unknownEvents` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `byday_msku_location_authid` (`authid`,`disposition`,`byday`,`location`,`msku`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_rpt_orders_fulfilled_shipments_fee 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_orders_fulfilled_shipments_fee` (
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `amazon_order_id` char(20) NOT NULL,
  `shipmentid` char(20) NOT NULL,
  `sku` char(50) NOT NULL,
  `itemid` char(20) NOT NULL,
  `shipment_date` datetime NOT NULL,
  `reporting_date` datetime DEFAULT NULL,
  `payments_date` datetime DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `quantity` int(10) DEFAULT NULL,
  `unitcost` decimal(20,6) DEFAULT NULL,
  `unittransfee` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`sku`,`shipmentid`,`itemid`) USING BTREE,
  KEY `shipment_date_sales_channel_amazonauthid` (`amazonauthid`,`marketplaceid`,`shipment_date`) USING BTREE,
  KEY `amazon_order_id_itemid` (`shipmentid`,`sku`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_rpt_orders_fulfilled_shipments_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_orders_fulfilled_shipments_report` (
  `amazon_order_id` char(50) NOT NULL,
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `sales_channel` char(50) DEFAULT NULL,
  `merchant_order_id` char(50) DEFAULT NULL,
  `shipment_id` char(11) DEFAULT NULL,
  `shipment_item_id` char(20) NOT NULL,
  `amazon_order_item_id` char(15) NOT NULL,
  `merchant_order_item_id` char(60) DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `payments_date` datetime DEFAULT NULL,
  `shipment_date` datetime DEFAULT NULL,
  `reporting_date` datetime DEFAULT NULL,
  `buyer_email` char(50) DEFAULT NULL,
  `buyer_name` char(50) DEFAULT NULL,
  `buyer_phone_number` char(50) DEFAULT NULL,
  `product_name` varchar(500) DEFAULT NULL,
  `quantity_shipped` int(10) DEFAULT NULL,
  `currency` char(5) DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `ship_service_level` char(50) DEFAULT NULL,
  `recipient_name` char(50) DEFAULT NULL,
  `ship_address_1` varchar(200) DEFAULT NULL,
  `ship_address_2` varchar(200) DEFAULT NULL,
  `ship_address_3` varchar(200) DEFAULT NULL,
  `ship_city` char(100) DEFAULT NULL,
  `ship_state` char(50) DEFAULT NULL,
  `ship_postal_code` char(20) DEFAULT NULL,
  `ship_country` char(5) DEFAULT NULL,
  `ship_phone_number` char(50) DEFAULT NULL,
  `bill_address_1` varchar(200) DEFAULT NULL,
  `bill_address_2` varchar(200) DEFAULT NULL,
  `bill_address_3` varchar(200) DEFAULT NULL,
  `bill_city` char(100) DEFAULT NULL,
  `bill_state` char(50) DEFAULT NULL,
  `bill_postal_code` char(50) DEFAULT NULL,
  `bill_country` char(5) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `carrier` char(50) DEFAULT NULL,
  `tracking_number` char(50) DEFAULT NULL,
  `estimated_arrival_date` datetime DEFAULT NULL,
  `fulfillment_center_id` char(50) DEFAULT NULL,
  `fulfillment_channel` char(50) DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`shipment_item_id`) USING BTREE,
  KEY `authid_sku_sales_channel` (`amazonauthid`,`sales_channel`,`sku`,`shipment_date`) USING BTREE,
  KEY `amazonauthid_shipment_date` (`amazonauthid`,`shipment_date`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_rpt_orders_fulfilled_shipments_report_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_orders_fulfilled_shipments_report_archive` (
  `amazon_order_id` char(50) NOT NULL,
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `sales_channel` char(50) DEFAULT NULL,
  `merchant_order_id` char(50) DEFAULT NULL,
  `shipment_id` char(11) DEFAULT NULL,
  `shipment_item_id` char(20) NOT NULL,
  `amazon_order_item_id` char(15) NOT NULL,
  `merchant_order_item_id` char(60) DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `payments_date` datetime DEFAULT NULL,
  `shipment_date` datetime DEFAULT NULL,
  `reporting_date` datetime DEFAULT NULL,
  `buyer_email` char(50) DEFAULT NULL,
  `buyer_name` char(50) DEFAULT NULL,
  `buyer_phone_number` char(50) DEFAULT NULL,
  `product_name` varchar(500) DEFAULT NULL,
  `quantity_shipped` int(10) DEFAULT NULL,
  `currency` char(5) DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `ship_service_level` char(50) DEFAULT NULL,
  `recipient_name` char(50) DEFAULT NULL,
  `ship_address_1` varchar(200) DEFAULT NULL,
  `ship_address_2` varchar(200) DEFAULT NULL,
  `ship_address_3` varchar(200) DEFAULT NULL,
  `ship_city` char(100) DEFAULT NULL,
  `ship_state` char(50) DEFAULT NULL,
  `ship_postal_code` char(20) DEFAULT NULL,
  `ship_country` char(5) DEFAULT NULL,
  `ship_phone_number` char(50) DEFAULT NULL,
  `bill_address_1` varchar(200) DEFAULT NULL,
  `bill_address_2` varchar(200) DEFAULT NULL,
  `bill_address_3` varchar(200) DEFAULT NULL,
  `bill_city` char(100) DEFAULT NULL,
  `bill_state` char(50) DEFAULT NULL,
  `bill_postal_code` char(50) DEFAULT NULL,
  `bill_country` char(5) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `carrier` char(50) DEFAULT NULL,
  `tracking_number` char(50) DEFAULT NULL,
  `estimated_arrival_date` datetime DEFAULT NULL,
  `fulfillment_center_id` char(50) DEFAULT NULL,
  `fulfillment_channel` char(50) DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`shipment_item_id`) USING BTREE,
  KEY `authid_sku_sales_channel` (`amazonauthid`,`sales_channel`,`sku`,`shipment_date`) USING BTREE,
  KEY `amazonauthid_shipment_date` (`amazonauthid`,`shipment_date`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_scout_asins 结构
CREATE TABLE IF NOT EXISTS `t_amz_scout_asins` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sour_date` varchar(50) DEFAULT NULL COMMENT '抓取的日期',
  `marketplace` varchar(50) NOT NULL COMMENT '格式: Amazon.com / Amazon.co.uk 等',
  `currency` varchar(6) NOT NULL COMMENT '格式: USD / GBP ..',
  `type` varchar(50) NOT NULL COMMENT '榜单类型',
  `category_top` varchar(50) NOT NULL COMMENT '一级榜单品类',
  `category` varchar(100) NOT NULL COMMENT '当前的榜单品类(抓取时)',
  `category_lev` tinyint(4) NOT NULL COMMENT '当前的榜单品类的级别',
  `billboard_rank` tinyint(4) NOT NULL COMMENT '当前的榜单排名',
  `ASIN` char(10) NOT NULL,
  `product_url` varchar(500) DEFAULT NULL,
  `image_url` varchar(300) DEFAULT NULL,
  `product_title` varchar(300) DEFAULT NULL,
  `reivew_score` float DEFAULT NULL COMMENT '评分',
  `review_count` smallint(6) DEFAULT '0' COMMENT '评价数量',
  `current_price` double DEFAULT NULL COMMENT '价格',
  `prime_icon` char(5) DEFAULT NULL COMMENT '是否是PRIME,空为否',
  `rank_date` date DEFAULT NULL COMMENT '计算的日期.以下字段需要计算补充',
  `product_rank` int(11) DEFAULT NULL,
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
) ENGINE=InnoDB COMMENT='测试选品思路,手动加入ASIN数据,自动更新ASIN尺寸及计算利润';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_acc_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_acc_report` (
  `settlement_id` bigint(20) unsigned NOT NULL,
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `settlement_start_date` datetime DEFAULT NULL,
  `settlement_end_date` datetime DEFAULT NULL,
  `deposit_date` datetime DEFAULT NULL,
  `total_amount` decimal(15,2) DEFAULT NULL,
  `currency` char(7) DEFAULT NULL,
  `ismoved` bit(1) DEFAULT NULL,
  `capturetime` datetime DEFAULT NULL,
  `sumtime` datetime DEFAULT NULL,
  `invalid` bit(1) DEFAULT b'0' COMMENT '无效转账',
  PRIMARY KEY (`settlement_id`),
  KEY `index1` (`amazonauthid`,`marketplace_name`) USING BTREE,
  KEY `deposit_date` (`deposit_date`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_acc_report_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_acc_report_archive` (
  `settlement_id` bigint(20) unsigned NOT NULL,
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `settlement_start_date` datetime DEFAULT NULL,
  `settlement_end_date` datetime DEFAULT NULL,
  `deposit_date` datetime DEFAULT NULL,
  `total_amount` decimal(15,2) DEFAULT NULL,
  `currency` char(7) DEFAULT NULL,
  `ismoved` bit(1) DEFAULT NULL,
  `capturetime` datetime DEFAULT NULL,
  `sumtime` datetime DEFAULT NULL,
  PRIMARY KEY (`settlement_id`) USING BTREE,
  KEY `index1` (`amazonauthid`,`marketplace_name`) USING BTREE,
  KEY `deposit_date` (`deposit_date`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_acc_statement 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_acc_statement` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(20) DEFAULT NULL,
  `currency` char(5) DEFAULT NULL,
  `startdate` date DEFAULT NULL,
  `enddate` date DEFAULT NULL,
  `datetype` char(5) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `summaryall` mediumblob,
  `listdata` longblob,
  `fielddata` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key` (`shopid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_amount_description 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_amount_description` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `cname` varchar(200) NOT NULL DEFAULT '',
  `ename` varchar(200) NOT NULL DEFAULT '',
  `cdescription` varchar(1000) NOT NULL DEFAULT '',
  `edescription` varchar(1000) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ename` (`ename`)
) ENGINE=InnoDB AUTO_INCREMENT=137 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_amount_type_nonsku 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_amount_type_nonsku` (
  `transaction_type` char(40) NOT NULL,
  `amount_type` char(40) NOT NULL,
  `amount_description` char(100) NOT NULL,
  PRIMARY KEY (`transaction_type`,`amount_type`,`amount_description`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_report` (
  `id` bigint(20) unsigned NOT NULL,
  `settlement_id` bigint(20) unsigned NOT NULL,
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
  `quantity_purchased` int(11) DEFAULT NULL,
  `promotion_id` char(100) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`) USING BTREE,
  KEY `order_id` (`order_id`,`order_item_code`),
  KEY `index1` (`settlement_id`,`sku`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='账期报表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_report_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_report_archive` (
  `id` bigint(20) unsigned NOT NULL,
  `settlement_id` bigint(20) unsigned NOT NULL,
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
  `quantity_purchased` int(11) DEFAULT NULL,
  `promotion_id` char(100) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`posted_date`,`amazonAuthId`,`id`),
  KEY `index1` (`settlement_id`,`amazonAuthId`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='账期报表'
/*!50100 PARTITION BY RANGE (year(posted_date))
SUBPARTITION BY HASH (quarter(posted_date ))
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
  `id` bigint(20) unsigned NOT NULL,
  `settlementid` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `sku` char(40) NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `transaction_type` char(40) NOT NULL COMMENT '商品费用',
  `amount_type` char(40) NOT NULL,
  `amount_description` char(100) NOT NULL,
  `fulfillment_type` char(5) NOT NULL,
  `currency` char(7) NOT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  `quantity_purchased` int(11) DEFAULT NULL,
  `quantity_orders` int(11) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`) USING BTREE,
  KEY `marketplace_name` (`marketplace_name`) USING BTREE,
  KEY `sku` (`sku`) USING BTREE,
  KEY `settlementid` (`settlementid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_day_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_day_archive` (
  `id` bigint(20) unsigned NOT NULL,
  `settlementid` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `sku` char(40) NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `transaction_type` char(40) NOT NULL COMMENT '商品费用',
  `amount_type` char(40) NOT NULL,
  `amount_description` char(100) NOT NULL,
  `fulfillment_type` char(5) NOT NULL,
  `currency` char(7) NOT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  `quantity_purchased` int(11) DEFAULT NULL,
  `quantity_orders` int(11) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`) USING BTREE,
  KEY `marketplace_name` (`marketplace_name`) USING BTREE,
  KEY `sku` (`sku`) USING BTREE,
  KEY `settlementid` (`settlementid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_month 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_month` (
  `id` bigint(20) unsigned NOT NULL,
  `settlementid` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `transaction_type` char(40) NOT NULL,
  `amount_type` char(40) NOT NULL,
  `amount_description` char(100) NOT NULL,
  `fulfillment_type` char(5) NOT NULL,
  `currency` char(7) NOT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  `quantity_purchased` int(11) DEFAULT NULL,
  `quantity_orders` int(11) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`) USING BTREE,
  KEY `settlementid` (`settlementid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_month_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_month_archive` (
  `id` bigint(20) unsigned NOT NULL,
  `settlementid` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `transaction_type` char(40) NOT NULL,
  `amount_type` char(40) NOT NULL,
  `amount_description` char(100) NOT NULL,
  `fulfillment_type` char(5) NOT NULL,
  `currency` char(7) NOT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`) USING BTREE,
  KEY `settlementid` (`settlementid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_month_bkp20230803 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_month_bkp20230803` (
  `id` bigint(20) unsigned NOT NULL,
  `settlementid` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_returns 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_returns` (
  `id` bigint(20) unsigned NOT NULL,
  `settlementid` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `sku` char(50) NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `mfnqty` int(11) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`),
  KEY `sku` (`sku`),
  KEY `marketplace_name` (`marketplace_name`),
  KEY `settlementid` (`settlementid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_returns_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_returns_archive` (
  `id` bigint(20) unsigned NOT NULL,
  `settlementid` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `sku` char(50) NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `mfnqty` int(11) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`) USING BTREE,
  KEY `sku` (`sku`) USING BTREE,
  KEY `marketplace_name` (`marketplace_name`) USING BTREE,
  KEY `settlementid` (`settlementid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_sku 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_sku` (
  `id` bigint(20) unsigned NOT NULL,
  `settlementid` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `sku` char(40) NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `currency` char(5) DEFAULT NULL,
  `order_amount` int(11) DEFAULT NULL COMMENT '订单量',
  `sales` int(11) DEFAULT NULL COMMENT '销量',
  `refundsales` int(11) DEFAULT NULL,
  `refundorder` int(11) DEFAULT NULL,
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
  `share_shop_other_fee` decimal(20,8) DEFAULT NULL,
  `local_price` decimal(20,8) DEFAULT NULL,
  `local_other_cost` decimal(20,8) DEFAULT NULL,
  `local_return_tax` decimal(20,8) DEFAULT NULL,
  `profit_local_shipmentfee` decimal(20,8) DEFAULT NULL,
  `profit_marketfee` decimal(20,8) unsigned DEFAULT NULL,
  `profit_vat` decimal(20,8) DEFAULT NULL,
  `profit_companytax` decimal(20,8) DEFAULT NULL,
  `profit_customstax` decimal(20,8) DEFAULT NULL,
  `profit_exchangelost` decimal(20,8) DEFAULT NULL,
  `profit_lostrate` decimal(20,8) DEFAULT NULL,
  `profit_otherfee` decimal(20,8) DEFAULT NULL,
  `owner` bigint(20) unsigned DEFAULT NULL,
  `pid` bigint(20) unsigned DEFAULT NULL,
  `mid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`) USING BTREE,
  KEY `marketplace_name` (`marketplace_name`) USING BTREE,
  KEY `sku` (`sku`) USING BTREE,
  KEY `settlementid` (`settlementid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_sku_archive 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_sku_archive` (
  `id` bigint(20) unsigned NOT NULL,
  `settlementid` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `sku` char(40) NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `order_amount` int(11) DEFAULT NULL COMMENT '订单量',
  `sales` int(11) DEFAULT NULL COMMENT '销量',
  `principal` decimal(20,8) DEFAULT NULL COMMENT '销售额',
  `commission` decimal(20,8) DEFAULT NULL COMMENT '销售佣金',
  `fbafee` decimal(20,8) DEFAULT NULL COMMENT 'FBA费用',
  `refund` decimal(20,8) DEFAULT NULL COMMENT '退款金额',
  `otherfee` decimal(20,8) DEFAULT NULL COMMENT '其它',
  `tax` decimal(20,8) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`posted_date`,`id`) USING BTREE,
  KEY `marketplace_name` (`marketplace_name`) USING BTREE,
  KEY `sku` (`sku`) USING BTREE,
  KEY `settlementid` (`settlementid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_sku_bkp20230803 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_sku_bkp20230803` (
  `id` bigint(20) unsigned NOT NULL,
  `settlementid` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `sku` char(40) NOT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `order_amount` int(11) DEFAULT NULL COMMENT '订单量',
  `sales` int(11) DEFAULT NULL COMMENT '销量',
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_sku_month 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_sku_month` (
  `id` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `sku` char(40) NOT NULL,
  `msku` char(40) NOT NULL,
  `groupid` bigint(20) unsigned NOT NULL,
  `asin` char(15) NOT NULL,
  `parentasin` char(15) DEFAULT NULL,
  `categoryid` char(36) DEFAULT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `owner` bigint(20) unsigned DEFAULT NULL,
  `pid` bigint(20) unsigned DEFAULT NULL,
  `mid` bigint(20) unsigned DEFAULT NULL,
  `currency` char(5) DEFAULT NULL,
  `order_amount` int(11) DEFAULT NULL COMMENT '订单量',
  `sales` int(11) DEFAULT NULL COMMENT '销量',
  `refundsales` int(11) DEFAULT NULL,
  `refundorder` int(11) DEFAULT NULL,
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
  `share_shop_other_fee` decimal(20,8) DEFAULT NULL,
  `local_price` decimal(20,8) DEFAULT NULL,
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
  `rpt_adv_units` int(11) DEFAULT NULL,
  `rpt_reimbursements_fee` decimal(20,8) DEFAULT NULL,
  `fin_sum_fee` decimal(20,8) DEFAULT NULL,
  `profit` decimal(20,8) DEFAULT NULL,
  PRIMARY KEY (`posted_date`,`id`) USING BTREE,
  KEY `sku` (`sku`) USING BTREE,
  KEY `owner` (`owner`) USING BTREE,
  KEY `marketplace_name` (`marketplace_name`) USING BTREE,
  KEY `msku` (`msku`) USING BTREE,
  KEY `groupid` (`groupid`) USING BTREE,
  KEY `asin` (`asin`) USING BTREE,
  KEY `parentasin` (`parentasin`) USING BTREE,
  KEY `categoryid` (`categoryid`) USING BTREE,
  KEY `pid` (`pid`) USING BTREE,
  KEY `mid` (`mid`) USING BTREE,
  KEY `amazonAuthId` (`amazonAuthId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_sku_month_cny 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_sku_month_cny` (
  `id` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `sku` char(40) NOT NULL,
  `msku` char(40) NOT NULL,
  `groupid` bigint(20) unsigned NOT NULL,
  `asin` char(15) NOT NULL,
  `parentasin` char(15) DEFAULT NULL,
  `categoryid` char(36) DEFAULT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `owner` bigint(20) unsigned DEFAULT NULL,
  `pid` bigint(20) unsigned DEFAULT NULL,
  `mid` bigint(20) unsigned DEFAULT NULL,
  `currency` char(5) DEFAULT NULL,
  `order_amount` int(11) DEFAULT NULL COMMENT '订单量',
  `sales` int(11) DEFAULT NULL COMMENT '销量',
  `refundsales` int(11) DEFAULT NULL,
  `refundorder` int(11) DEFAULT NULL,
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
  `share_shop_other_fee` decimal(20,8) DEFAULT NULL,
  `local_price` decimal(20,8) DEFAULT NULL,
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
  `rpt_adv_units` int(11) DEFAULT NULL,
  `rpt_reimbursements_fee` decimal(20,8) DEFAULT NULL,
  `fin_sum_fee` decimal(20,8) DEFAULT NULL,
  `profit` decimal(20,8) DEFAULT NULL,
  PRIMARY KEY (`posted_date`,`id`) USING BTREE,
  KEY `sku` (`sku`) USING BTREE,
  KEY `owner` (`owner`) USING BTREE,
  KEY `marketplace_name` (`marketplace_name`) USING BTREE,
  KEY `msku` (`msku`) USING BTREE,
  KEY `groupid` (`groupid`) USING BTREE,
  KEY `asin` (`asin`) USING BTREE,
  KEY `parentasin` (`parentasin`) USING BTREE,
  KEY `categoryid` (`categoryid`) USING BTREE,
  KEY `pid` (`pid`) USING BTREE,
  KEY `mid` (`mid`) USING BTREE,
  KEY `amazonAuthId` (`amazonAuthId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_settlement_summary_sku_month_usd 结构
CREATE TABLE IF NOT EXISTS `t_amz_settlement_summary_sku_month_usd` (
  `id` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `sku` char(40) NOT NULL,
  `msku` char(40) NOT NULL,
  `groupid` bigint(20) unsigned NOT NULL,
  `asin` char(15) NOT NULL,
  `parentasin` char(15) DEFAULT NULL,
  `categoryid` char(36) DEFAULT NULL,
  `marketplace_name` char(40) NOT NULL,
  `posted_date` date NOT NULL,
  `owner` bigint(20) unsigned DEFAULT NULL,
  `pid` bigint(20) unsigned DEFAULT NULL,
  `mid` bigint(20) unsigned DEFAULT NULL,
  `currency` char(5) DEFAULT NULL,
  `order_amount` int(11) DEFAULT NULL COMMENT '订单量',
  `sales` int(11) DEFAULT NULL COMMENT '销量',
  `refundsales` int(11) DEFAULT NULL,
  `refundorder` int(11) DEFAULT NULL,
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
  `share_shop_other_fee` decimal(20,8) DEFAULT NULL,
  `local_price` decimal(20,8) DEFAULT NULL,
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
  `rpt_adv_units` int(11) DEFAULT NULL,
  `rpt_reimbursements_fee` decimal(20,8) DEFAULT NULL,
  `fin_sum_fee` decimal(20,8) DEFAULT NULL,
  `profit` decimal(20,8) DEFAULT NULL,
  PRIMARY KEY (`posted_date`,`id`) USING BTREE,
  KEY `sku` (`sku`) USING BTREE,
  KEY `owner` (`owner`) USING BTREE,
  KEY `marketplace_name` (`marketplace_name`) USING BTREE,
  KEY `msku` (`msku`),
  KEY `groupid` (`groupid`),
  KEY `asin` (`asin`),
  KEY `parentasin` (`parentasin`),
  KEY `categoryid` (`categoryid`),
  KEY `pid` (`pid`),
  KEY `mid` (`mid`),
  KEY `amazonAuthId` (`amazonAuthId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_ship_fulfillment_center 结构
CREATE TABLE IF NOT EXISTS `t_amz_ship_fulfillment_center` (
  `code` char(13) NOT NULL,
  `country` char(2) NOT NULL,
  `address_name` varchar(500) NOT NULL,
  `city` varchar(500) DEFAULT NULL,
  `state` char(50) DEFAULT NULL,
  `zip` char(10) DEFAULT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_ship_state_province_code 结构
CREATE TABLE IF NOT EXISTS `t_amz_ship_state_province_code` (
  `code` char(5) NOT NULL,
  `name` varchar(50) NOT NULL DEFAULT '',
  `ename` varchar(50) NOT NULL DEFAULT '',
  `capital` varchar(50) NOT NULL DEFAULT '',
  `ecapital` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_submitfeed 结构
CREATE TABLE IF NOT EXISTS `t_amz_submitfeed` (
  `feed_submissionid` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `sellerid` char(15) NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `feed_type` char(50) NOT NULL,
  `submitted_date` datetime DEFAULT NULL,
  `started_processing_date` datetime DEFAULT NULL,
  `completed_processiong_date` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `feed_processing_status` char(15) DEFAULT NULL,
  `queueid` bigint(20) unsigned DEFAULT NULL,
  `amzprocesslog` blob,
  `documentid` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`feed_submissionid`,`sellerid`,`feed_type`) USING BTREE,
  KEY `marketplaceid_shopid` (`shopid`,`marketplaceid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='\r\n';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_amz_submitfeed_queue 结构
CREATE TABLE IF NOT EXISTS `t_amz_submitfeed_queue` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `feed_type` char(50) DEFAULT NULL,
  `process_date` timestamp NULL DEFAULT NULL,
  `filename` varchar(100) DEFAULT NULL,
  `content` blob,
  `process_log` varchar(1000) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `submitfeedid` bigint(20) unsigned DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT NULL,
  `feedoptions` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `filename` (`filename`),
  KEY `shopid` (`shopid`),
  KEY `feedtype` (`feed_type`),
  KEY `shopid_sellerid_marketplaceid_feed_type_process_date` (`amazonAuthId`,`marketplaceid`,`createtime`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_authority 结构
CREATE TABLE IF NOT EXISTS `t_authority` (
  `id` char(36) NOT NULL,
  `menuid` char(50)  DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL COMMENT '定义Action',
  `name` varchar(100) DEFAULT NULL COMMENT '权限名称',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`url`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='Action权限控制表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_data_runs_remove_time 结构
CREATE TABLE IF NOT EXISTS `t_data_runs_remove_time` (
  `id` int(11) NOT NULL,
  `ftype` char(10) DEFAULT NULL,
  `pages` int(11) DEFAULT NULL,
  `runtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ftype` (`ftype`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_daysales_formula 结构
CREATE TABLE IF NOT EXISTS `t_daysales_formula` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `formula` varchar(500) DEFAULT NULL,
  `formula_name` varchar(500) DEFAULT NULL,
  `month_sales_rate` decimal(10,2) DEFAULT NULL,
  `15sales_rate` decimal(10,2) DEFAULT NULL,
  `7sales_rate` decimal(10,2) DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_dimensions 结构
CREATE TABLE IF NOT EXISTS `t_dimensions` (
  `id` bigint(20) unsigned NOT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `length_units` char(15) DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `width_units` char(15) DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `height_units` char(15) DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `weight_units` char(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_dimensions_copy 结构
CREATE TABLE IF NOT EXISTS `t_dimensions_copy` (
  `id` bigint(20) unsigned NOT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `length_units` char(15) DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `width_units` char(15) DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `height_units` char(15) DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `weight_units` char(15) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_amazon_feedstatus 结构
CREATE TABLE IF NOT EXISTS `t_erp_amazon_feedstatus` (
  `status` char(50) NOT NULL,
  `name` char(50) DEFAULT NULL,
  `remark` char(100) DEFAULT NULL,
  PRIMARY KEY (`status`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_assembly 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `mainmid` bigint(20) unsigned DEFAULT NULL COMMENT '主产品',
  `submid` bigint(20) unsigned DEFAULT NULL COMMENT '子产品',
  `subnumber` int(11) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `mainmid` (`mainmid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_assembly_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `planitem` bigint(20) unsigned DEFAULT NULL,
  `ftype` char(10) DEFAULT NULL COMMENT '组装=ass, 拆分=dis',
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `mainmid` bigint(20) unsigned DEFAULT NULL COMMENT '主sku id',
  `amount` int(11) DEFAULT NULL,
  `amount_handle` int(11) unsigned DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL COMMENT '0：未提交，1：待组装，2 组装中，3 已完成，4 已终止, 5已作废',
  `remark` varchar(500) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number` (`shopid`,`number`),
  KEY `mainmid` (`mainmid`),
  KEY `createdate` (`shopid`,`opttime`) USING BTREE,
  KEY `shopid_createdate` (`shopid`,`createdate`),
  KEY `auditstatus` (`auditstatus`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_assembly_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT '0',
  `subnumber` int(11) DEFAULT '0',
  `whamount` int(11) DEFAULT '0' COMMENT '仓库调出量',
  `phamount` int(11) DEFAULT '0' COMMENT '采购量',
  `phedamount` int(11) DEFAULT '0' COMMENT '已经采购数量',
  `purchase_from_entry_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`formid`,`materialid`),
  UNIQUE KEY `indexunique` (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_assembly_from_instock 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_from_instock` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `shipmentid` char(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`),
  KEY `idx_formid_shipmentid` (`formid`,`shipmentid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_changewh_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_changewh_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL COMMENT '0：未提交，1：提交未审核，2：已审核',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`),
  KEY `warehouseid` (`warehouseid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_changewh_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_changewh_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `material_from` bigint(20) unsigned DEFAULT NULL,
  `material_to` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_customer 结构
CREATE TABLE IF NOT EXISTS `t_erp_customer` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID(h)',
  `name` char(50) DEFAULT NULL COMMENT '客户简称',
  `number` char(50) DEFAULT NULL COMMENT '客户编码',
  `fullname` varchar(200) DEFAULT NULL COMMENT '客户全称',
  `ftype` char(10) DEFAULT NULL COMMENT '客户分类',
  `contacts` varchar(50) DEFAULT NULL COMMENT '联系人',
  `phone_num` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `contact_info` varchar(2000) DEFAULT NULL COMMENT '其它联系信息',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `shoplink` varchar(500) DEFAULT NULL COMMENT '商品链接',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '所属店铺（公司）(h)',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_shopid` (`shopid`,`name`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_dispatch_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36) DEFAULT NULL COMMENT '调拨单号',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `from_warehouseid` bigint(20) unsigned DEFAULT NULL,
  `to_warehouseid` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_dispatch_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_dispatch_oversea_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_oversea_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36) DEFAULT NULL COMMENT '调拨单号',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `from_warehouseid` bigint(20) unsigned DEFAULT NULL,
  `to_warehouseid` bigint(20) unsigned DEFAULT NULL,
  `country` char(5) DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `auditstatus` int(10) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `number_shopid` (`shopid`,`number`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_dispatch_oversea_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_oversea_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `sellersku` varchar(50) DEFAULT NULL,
  `fnsku` varchar(50) DEFAULT NULL,
  `amount` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `formid` (`formid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_dispatch_oversea_trans 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_oversea_trans` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `company` bigint(20) unsigned DEFAULT NULL,
  `channel` bigint(20) unsigned DEFAULT NULL,
  `singleprice` decimal(10,4) DEFAULT NULL,
  `transweight` decimal(10,4) DEFAULT NULL,
  `wunit` char(10) DEFAULT NULL,
  `otherfee` decimal(10,4) DEFAULT NULL,
  `ordernum` char(50) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  `outarrtime` datetime DEFAULT NULL,
  `inarrtime` datetime DEFAULT NULL,
  `wtype` tinyint(3) DEFAULT '0',
  `transtype` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shipmentid` (`formid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_estimated_sales 结构
CREATE TABLE IF NOT EXISTS `t_erp_estimated_sales` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `sku` char(50) DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `presales` int(10) DEFAULT NULL COMMENT '手动输入日均销量',
  `startTime` date DEFAULT NULL COMMENT '开始生效时间 为null则是不限制',
  `endTime` date DEFAULT NULL COMMENT '结束生效时间 为null则是不限制',
  `conditions` int(11) DEFAULT '0' COMMENT '失效条件：0 = 不限制；1 = 指定>默认 ; 2 = 指定<默认',
  `conditionNum` decimal(10,2) DEFAULT NULL COMMENT '超过失效条件数值（百分比）',
  `isInvalid` bit(1) DEFAULT NULL COMMENT '当前输入销量是否有效 1表示有效，0表示无效',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku_marketplaceid_groupid` (`groupid`,`sku`,`marketplaceid`)
) ENGINE=InnoDB AUTO_INCREMENT=14066   COMMENT='用户维护日均销量表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_fin_account 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_account` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `paymeth` int(10) unsigned DEFAULT '1',
  `name` char(50) DEFAULT '默认',
  `isdefault` bit(1) DEFAULT b'0',
  `isdelete` bit(1) DEFAULT b'0',
  `balance` decimal(18,4) DEFAULT NULL COMMENT '账户余额',
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`,`paymeth`,`name`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='账户表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_fin_journalaccount 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_journalaccount` (
  `id` bigint(20) unsigned NOT NULL,
  `acct` bigint(20) unsigned NOT NULL,
  `ftype` char(36) NOT NULL COMMENT '记账类型:out,支出；in,收入',
  `projectid` bigint(20) unsigned DEFAULT NULL,
  `amount` decimal(18,4) DEFAULT NULL,
  `balance` decimal(18,4) DEFAULT NULL,
  `remark` varchar(2000) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creator` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `shopid` bigint(20) unsigned NOT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_fin_journaldaily 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_journaldaily` (
  `id` bigint(20) unsigned NOT NULL,
  `acct` bigint(20) unsigned DEFAULT NULL COMMENT '账户id',
  `byday` date DEFAULT NULL,
  `rec` decimal(18,4) DEFAULT NULL COMMENT '收入',
  `pay` decimal(18,4) DEFAULT NULL COMMENT '支出',
  `balance` decimal(18,4) DEFAULT NULL COMMENT '余额',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `byday` (`byday`,`acct`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='流水_日账单';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_fin_project 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_project` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(50) DEFAULT NULL,
  `issys` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是系统项目名称',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_shopid` (`name`,`shopid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='流水账_类型';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_fin_type_journalmonthly 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_type_journalmonthly` (
  `id` bigint(20) unsigned NOT NULL,
  `projectid` bigint(20) unsigned DEFAULT NULL COMMENT '项目id',
  `acct` bigint(20) unsigned DEFAULT NULL COMMENT '账户id',
  `year` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `rec` decimal(18,4) DEFAULT NULL,
  `pay` decimal(18,4) DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`projectid`,`acct`,`year`,`month`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='流水_月账单 类型统计';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_formtype 结构
CREATE TABLE IF NOT EXISTS `t_erp_formtype` (
  `id` char(20) NOT NULL,
  `name` char(50) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory` (
  `id` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `quantity` int(11) DEFAULT '0',
  `status` char(36) NOT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_w_s_m_s` (`shopid`,`warehouseid`,`materialid`,`status`),
  KEY `FK_t_erp_inventory_t_erp_material` (`materialid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_bkp20230527 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_bkp20230527` (
  `id` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `quantity` int(11) DEFAULT '0',
  `status` char(36) NOT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_w_s_m_s` (`shopid`,`warehouseid`,`materialid`,`status`) USING BTREE,
  KEY `FK_t_erp_inventory_t_erp_material` (`materialid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_copy_felix_opt 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_copy_felix_opt` (
  `id` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `quantity` int(11) DEFAULT '0',
  `status` char(36) NOT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_w_s_m_s` (`shopid`,`warehouseid`,`materialid`,`status`) USING BTREE,
  KEY `FK_t_erp_inventory_t_erp_material` (`materialid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_his` (
  `id` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `quantity` int(11) DEFAULT '0',
  `status` char(36) NOT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `modifyday` date NOT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_erp_inventory_t_erp_material` (`warehouseid`,`materialid`) USING BTREE,
  KEY `mykey` (`shopid`,`materialid`,`warehouseid`) USING BTREE,
  KEY `modifyday` (`modifyday`),
  KEY `status` (`status`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_his_day 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_his_day` (
  `shopid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `byday` date NOT NULL,
  `inbound` int(10) DEFAULT '0',
  `fulfillable` int(10) DEFAULT '0',
  `outbound` int(10) DEFAULT '0',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`shopid`,`warehouseid`,`byday`,`materialid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_month_summary 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_month_summary` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `month` date NOT NULL,
  `startqty` int(11) DEFAULT NULL,
  `endqty` int(11) DEFAULT NULL,
  `shipment` int(11) DEFAULT NULL,
  `dispatch` int(11) DEFAULT NULL,
  `assembly` int(11) DEFAULT NULL,
  `purchase` int(11) DEFAULT NULL,
  `otherout` int(11) DEFAULT NULL,
  `otherin` int(11) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  `period` decimal(15,4) DEFAULT NULL,
  `turndays` decimal(15,4) DEFAULT NULL,
  `diff` int(11) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `materialid_warehouseid` (`shopid`,`warehouseid`,`materialid`,`month`),
  KEY `month` (`month`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_record` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `formoptid` bigint(20) unsigned DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `status` char(36) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `invqty` int(11) DEFAULT NULL,
  `startinbound` int(11) DEFAULT NULL,
  `inbound` int(11) DEFAULT NULL,
  `endinbound` int(11) DEFAULT NULL,
  `startfulfillable` int(11) DEFAULT NULL,
  `fulfillable` int(11) DEFAULT NULL,
  `endfulfillable` int(11) DEFAULT NULL,
  `startoutbound` int(11) DEFAULT NULL,
  `outbound` int(11) DEFAULT NULL,
  `endoutbound` int(11) DEFAULT NULL,
  `formtype` char(20) DEFAULT NULL,
  `operate` char(10) DEFAULT NULL COMMENT 'in,out,readyin,readyout,cancel,stop',
  `number` char(36) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`opttime`,`id`) USING BTREE,
  KEY `Index 2` (`warehouseid`,`materialid`,`status`) USING BTREE,
  KEY `索引 3` (`materialid`,`status`),
  KEY `number` (`shopid`,`number`),
  KEY `shopid_opttime` (`shopid`,`opttime`) USING BTREE,
  KEY `formtype` (`shopid`,`formtype`,`opttime`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_record_bkp20220721 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_record_bkp20220721` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `formoptid` bigint(20) unsigned DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `status` char(36) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `invqty` int(11) DEFAULT NULL,
  `formtype` char(20) DEFAULT NULL,
  `operate` char(10) DEFAULT NULL COMMENT 'in,out,readyin,readyout,cancel,stop',
  `number` char(36) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`opttime`,`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`shopid`,`warehouseid`,`operate`,`materialid`,`status`,`quantity`,`number`,`opttime`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_record_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_record_form` (
  `shopid` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `startinbound` int(10) DEFAULT '0',
  `inbound` int(10) DEFAULT '0',
  `endinbound` int(10) DEFAULT '0',
  `startfulfillable` int(10) DEFAULT '0',
  `fulfillable` int(10) DEFAULT '0',
  `endfulfillable` int(10) DEFAULT '0',
  `startoutbound` int(10) DEFAULT '0',
  `outbound` int(10) DEFAULT '0',
  `endoutbound` int(10) DEFAULT '0',
  `formtype` char(20) DEFAULT NULL,
  `operate` char(10) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime NOT NULL,
  KEY `shopid_formtype` (`shopid`,`formtype`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inventory_status 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_status` (
  `id` char(36) DEFAULT NULL,
  `code` char(36) DEFAULT NULL,
  `name` char(36) DEFAULT NULL
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inwh_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_inwh_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL COMMENT '0：未提交，1：提交未审核，2：已审核',
  `remark` varchar(1000) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_inwh_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_inwh_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_market_priority 结构
CREATE TABLE IF NOT EXISTS `t_erp_market_priority` (
  `marketplaceid` varchar(36) NOT NULL,
  `groupid` bigint(20) unsigned NOT NULL,
  `priority` int(10) DEFAULT NULL COMMENT 'FBA站点优先级',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`marketplaceid`,`groupid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='主要用于发货计划中的同一个店铺下面各个国家的优先级。';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_material` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID(h)',
  `sku` varchar(50) DEFAULT NULL COMMENT 'SKU',
  `name` varchar(500) DEFAULT NULL COMMENT '名称',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID(h)',
  `upc` char(30) DEFAULT NULL COMMENT '条码',
  `brand` char(50) DEFAULT NULL COMMENT '品牌',
  `image` bigint(20) unsigned DEFAULT NULL COMMENT '图片',
  `itemDimensions` bigint(20) unsigned DEFAULT NULL COMMENT '产品尺寸',
  `pkgDimensions` bigint(20) unsigned DEFAULT NULL COMMENT '带包装尺寸',
  `boxDimensions` bigint(20) unsigned DEFAULT NULL,
  `boxnum` int(10) unsigned DEFAULT NULL,
  `specification` varchar(100) DEFAULT NULL COMMENT '规格',
  `supplier` bigint(20) unsigned DEFAULT NULL COMMENT '供应商',
  `badrate` float DEFAULT NULL COMMENT '不良率',
  `vatrate` float DEFAULT NULL COMMENT '退税率',
  `productCode` char(36) DEFAULT NULL COMMENT '供应商产品代码',
  `delivery_cycle` int(11) DEFAULT NULL COMMENT '供货周期',
  `other_cost` decimal(10,2) DEFAULT NULL,
  `MOQ` int(11) unsigned DEFAULT '0' COMMENT '起订量：minimum order quantity',
  `purchaseUrl` varchar(1000) DEFAULT NULL COMMENT '采购链接',
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注',
  `categoryid` char(36) DEFAULT NULL COMMENT '类型id',
  `issfg` char(1) DEFAULT '0' COMMENT '0:单独成品；1：组装成品；2：半成品',
  `color` char(10) DEFAULT '0',
  `owner` bigint(20) unsigned DEFAULT '0',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `price` decimal(10,2) DEFAULT NULL,
  `price_wavg` decimal(10,2) DEFAULT NULL,
  `price_ship_wavg` decimal(10,2) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `parentid` char(36) DEFAULT NULL COMMENT '用于导入数据是引用的系统内的那个SKU产品',
  `effectivedate` datetime DEFAULT NULL,
  `isSmlAndLight` bit(1) DEFAULT b'0' COMMENT '是否轻小产品',
  `assembly_time` int(11) DEFAULT NULL COMMENT '组装时间',
  `isDelete` bit(1) DEFAULT NULL,
  `mtype` int(11) DEFAULT '0',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 4` (`shopid`,`owner`,`color`,`sku`),
  KEY `supplier` (`supplier`),
  KEY `FK_t_erp_material_t_erp_material_sku` (`sku`,`shopid`,`isDelete`) USING BTREE,
  KEY `categoryid` (`categoryid`),
  KEY `opttime` (`shopid`,`opttime`) USING BTREE,
  KEY `shop_delete_sku_color` (`shopid`,`isDelete`,`mtype`,`sku`,`color`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_brand 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_brand` (
  `id` char(36) NOT NULL,
  `name` char(100) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`name`,`shopid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_category 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_category` (
  `id` char(36) NOT NULL,
  `name` char(100) DEFAULT NULL,
  `number` char(50) DEFAULT NULL,
  `color` char(10) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`name`,`shopid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_consumable 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_consumable` (
  `id` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `submaterialid` bigint(20) unsigned NOT NULL,
  `amount` decimal(10,4) unsigned DEFAULT '0.0000',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `materialid_submaterialid` (`materialid`,`submaterialid`) USING BTREE,
  KEY `submaterialid` (`submaterialid`)
) ENGINE=InnoDB  COMMENT='耗材表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_consumable_inventory 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_consumable_inventory` (
  `id` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `quantity` decimal(10,6) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_consumable_safety_stock 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_consumable_safety_stock` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `shopid` bigint(20) unsigned NOT NULL,
  `amount` int(10) unsigned NOT NULL DEFAULT '0',
  `operator` bigint(20) unsigned NOT NULL DEFAULT '0',
  `opttime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB AUTO_INCREMENT=1658363145072189443 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_customs 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_customs` (
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `name_en` varchar(500) DEFAULT NULL COMMENT '产品英文名',
  `name_cn` varchar(500) DEFAULT NULL COMMENT '产品中文名',
  `material` char(50) DEFAULT NULL COMMENT '产品材质',
  `model` char(50) DEFAULT NULL COMMENT '产品型号',
  `customs_code` char(50) DEFAULT NULL COMMENT '海关编码',
  `currency` char(5) DEFAULT NULL,
  `material_use` char(50) DEFAULT NULL COMMENT '用途',
  `brand` char(50) DEFAULT NULL COMMENT '产品品牌',
  `iselectricity` bit(1) DEFAULT b'0' COMMENT '是否带电/磁',
  `isdanger` bit(1) DEFAULT b'0' COMMENT '是否危险品',
  `unitprice` decimal(10,2) DEFAULT NULL COMMENT '申报单价',
  `addfee` decimal(10,2) DEFAULT NULL COMMENT '附加费用',
  `matreialid` bigint(20) unsigned DEFAULT NULL,
  KEY `materialid` (`materialid`) USING BTREE,
  KEY `matreialid` (`matreialid`)
) ENGINE=InnoDB  COMMENT='海关表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_customs_file 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_customs_file` (
  `id` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `filename` varchar(500) DEFAULT NULL,
  `filepath` varchar(1000) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_customs_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_customs_item` (
  `materialid` bigint(20) unsigned NOT NULL,
  `country` char(10) NOT NULL COMMENT 'DE UK FR',
  `code` char(10) DEFAULT NULL,
  `fee` decimal(10,2) DEFAULT NULL,
  `taxrate` decimal(10,2) DEFAULT NULL,
  `currency` char(50) DEFAULT 'CNY',
  PRIMARY KEY (`materialid`,`country`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_group 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_group` (
  `materialid` bigint(20) unsigned NOT NULL,
  `groupid` char(36) NOT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`materialid`,`groupid`),
  KEY `FK_t_erp_material_category_t_erp_category` (`groupid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_his` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID(h)',
  `sku` varchar(50) DEFAULT NULL COMMENT 'SKU',
  `name` varchar(500) DEFAULT NULL COMMENT '名称',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID(h)',
  `upc` char(30) DEFAULT NULL COMMENT '条码',
  `brand` char(50) DEFAULT NULL COMMENT '品牌',
  `image` bigint(20) unsigned DEFAULT NULL COMMENT '图片',
  `itemDimensions` char(36) DEFAULT NULL COMMENT '产品尺寸',
  `pkgDimensions` char(36) DEFAULT NULL COMMENT '带包装尺寸',
  `boxDimensions` bigint(20) unsigned DEFAULT NULL,
  `boxnum` int(10) unsigned DEFAULT NULL,
  `specification` varchar(100) DEFAULT NULL COMMENT '规格',
  `supplier` bigint(20) unsigned DEFAULT NULL COMMENT '供应商',
  `productCode` char(36) DEFAULT NULL COMMENT '供应商产品代码',
  `delivery_cycle` int(11) DEFAULT NULL,
  `other_cost` decimal(10,2) DEFAULT NULL,
  `MOQ` int(11) DEFAULT '0' COMMENT '起订量：minimum order quantity',
  `purchaseUrl` varchar(1000) DEFAULT NULL COMMENT '采购链接',
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注',
  `categoryid` char(36) DEFAULT NULL COMMENT '类型id',
  `issfg` char(10) DEFAULT '0' COMMENT '0:单独成品；1：组装成品；2：半成品',
  `color` char(10) DEFAULT '0',
  `owner` bigint(20) unsigned DEFAULT '0',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `price` decimal(10,4) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `parentid` char(36) DEFAULT NULL COMMENT '用于导入数据是引用的系统内的那个SKU产品',
  `effectivedate` datetime DEFAULT NULL,
  `isSmlAndLight` bit(1) DEFAULT b'0' COMMENT '是否轻小产品',
  `assembly_time` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`,`opttime`),
  UNIQUE KEY `Index 3` (`shopid`,`sku`,`opttime`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='t_erp_material_his历史表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_mark 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_mark` (
  `materialid` bigint(20) unsigned NOT NULL,
  `ftype` char(10) NOT NULL COMMENT 'notice：产品出现问题时发布的公告',
  `mark` varchar(500) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`materialid`,`ftype`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_mark_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_mark_his` (
  `materialid` bigint(20) unsigned NOT NULL,
  `ftype` char(10) NOT NULL COMMENT 'notice：产品出现问题时发布的公告',
  `mark` varchar(500) DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`materialid`,`ftype`,`opttime`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_supplier 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_supplier` (
  `id` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `supplierid` bigint(20) unsigned NOT NULL,
  `purchaseUrl` varchar(1000) DEFAULT NULL,
  `productCode` char(36) DEFAULT NULL,
  `specId` char(36) DEFAULT NULL,
  `otherCost` decimal(10,2) DEFAULT NULL,
  `deliverycycle` int(11) DEFAULT NULL,
  `isdefault` bit(1) NOT NULL DEFAULT b'0',
  `badrate` float DEFAULT '0',
  `MOQ` int(11) NOT NULL DEFAULT '0',
  `creater` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `materialid_supplierid` (`materialid`,`supplierid`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_supplier_stepwise 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_supplier_stepwise` (
  `id` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `supplierid` bigint(20) unsigned NOT NULL,
  `currency` char(5)   DEFAULT '',
  `price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00',
  `amount` int(10) unsigned NOT NULL DEFAULT '0',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `materialid` (`materialid`,`supplierid`,`amount`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_material_tags 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_tags` (
  `mid` bigint(20) unsigned NOT NULL,
  `tagid` bigint(20) unsigned NOT NULL,
  `operator` bigint(20) unsigned NOT NULL,
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`mid`,`tagid`) USING BTREE,
  KEY `tagid` (`tagid`)
) ENGINE=InnoDB  COMMENT='产品-标签';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_m_group 结构
CREATE TABLE IF NOT EXISTS `t_erp_m_group` (
  `id` char(36) NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `ftype` int(10) NOT NULL,
  `color` char(10) DEFAULT NULL,
  `issys` bit(1) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_outwh_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_outwh_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `purchaser` bigint(20) unsigned DEFAULT NULL COMMENT '发货客户',
  `customer` varchar(100) DEFAULT NULL,
  `toaddress` varchar(500) DEFAULT NULL COMMENT '发货地址',
  `express` varchar(500) DEFAULT NULL COMMENT '物流快递',
  `expressno` char(50) DEFAULT NULL COMMENT '快递编号',
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`),
  KEY `createdate` (`createdate`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_outwh_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_outwh_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_alibaba_auth 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `name` char(36) DEFAULT NULL,
  `access_token` varchar(500) DEFAULT NULL,
  `refresh_token` varchar(500) DEFAULT NULL,
  `resource_owner` varchar(50) DEFAULT NULL,
  `aliId` bigint(20) unsigned DEFAULT NULL,
  `memberId` char(50) DEFAULT NULL,
  `refresh_token_timeout` datetime DEFAULT NULL,
  `access_token_timeout` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `isDelete` bit(1) DEFAULT b'0',
  `appkey` varchar(255) DEFAULT NULL,
  `appsecret` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid_name` (`shopid`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1687009155260399619 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_alibaba_contact 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_contact` (
  `id` char(30) NOT NULL,
  `companyName` varchar(100) DEFAULT NULL,
  `offerid` bigint(20) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `imInPlatform` varchar(100) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_alibaba_message 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_message` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `content` longtext NOT NULL,
  `signature` varchar(50) NOT NULL DEFAULT '',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2897 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_alibaba_order_baseinfo 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_order_baseinfo` (
  `id` bigint(20) unsigned NOT NULL,
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
  `discount` int(11) DEFAULT NULL,
  `refund` int(11) DEFAULT NULL,
  `overSeaOrder` bit(1) DEFAULT NULL,
  `refundPayment` decimal(20,6) DEFAULT NULL,
  `shippingFee` decimal(20,6) DEFAULT NULL,
  `totalAmount` decimal(20,6) DEFAULT NULL,
  `sumProductPayment` decimal(20,6) DEFAULT NULL,
  `flowTemplateCode` char(50) DEFAULT NULL,
  `buyerID` char(50) DEFAULT NULL,
  `sellerID` char(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_alibaba_order_productitems 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_order_productitems` (
  `skuID` bigint(20) unsigned NOT NULL,
  `orderid` bigint(20) unsigned NOT NULL,
  `entryid` bigint(20) unsigned NOT NULL,
  `itemAmount` int(10) unsigned NOT NULL DEFAULT '0',
  `price` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `entryDiscount` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `status` char(50) NOT NULL DEFAULT '0.000000',
  `statusStr` char(50) NOT NULL DEFAULT '0.000000',
  `skuInfos` varchar(500) NOT NULL DEFAULT '',
  `productID` bigint(20) unsigned NOT NULL,
  `productCargoNumber` char(50) NOT NULL DEFAULT '',
  `name` varchar(500) NOT NULL DEFAULT '0',
  `logisticsStatus` tinyint(4) NOT NULL DEFAULT '0',
  `productSnapshotUrl` varchar(100) NOT NULL DEFAULT '0',
  `productImgUrl` varchar(100) NOT NULL DEFAULT '0',
  `unit` varchar(10) NOT NULL DEFAULT '0',
  `refundStatus` varchar(30) NOT NULL DEFAULT '0',
  `gmtCreate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `gmtModified` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`orderid`,`skuID`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_alibaba_order_receiverinfo 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_order_receiverinfo` (
  `orderid` bigint(20) unsigned DEFAULT NULL,
  `toArea` varchar(200) DEFAULT NULL,
  `toDivisionCode` char(50) DEFAULT NULL,
  `toFullName` char(50) DEFAULT NULL,
  `toMobile` char(50) DEFAULT NULL,
  `toPost` char(50) DEFAULT NULL
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_alibaba_order_tradeterms 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_order_tradeterms` (
  `orderid` bigint(20) unsigned NOT NULL,
  `payStatus` char(20) NOT NULL,
  `payTime` datetime DEFAULT NULL,
  `payway` char(20) DEFAULT NULL,
  `phasAmount` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `cardPay` bit(1) NOT NULL DEFAULT b'0',
  `expressPay` bit(1) NOT NULL DEFAULT b'0',
  `payWayDesc` char(50) DEFAULT NULL
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_alibaba_productitems 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_productitems` (
  `specId` char(36) NOT NULL DEFAULT '',
  `productID` char(20) NOT NULL DEFAULT '',
  `price` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `name` varchar(500) NOT NULL DEFAULT '0',
  `productSnapshotUrl` varchar(100) NOT NULL DEFAULT '0',
  `productImgUrl` varchar(100) NOT NULL DEFAULT '0',
  `unit` varchar(5) NOT NULL DEFAULT '0',
  `skuInfos` varchar(200) NOT NULL DEFAULT '0',
  PRIMARY KEY (`specId`,`productID`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_fin_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_fin_form` (
  `id` bigint(20) unsigned NOT NULL,
  `entryid` bigint(20) unsigned DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `auditstatus` int(10) unsigned DEFAULT '0' COMMENT '0,待审核 1已审核待付款 2已完成 3.已退回',
  `payment_method` int(10) unsigned DEFAULT NULL,
  `number` char(36) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_fin_form_payment 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_fin_form_payment` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned NOT NULL,
  `formentryid` bigint(20) unsigned DEFAULT NULL,
  `acct` bigint(20) unsigned DEFAULT NULL,
  `payprice` decimal(18,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(2000) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `projectid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `formentryid` (`formentryid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `purchaser` bigint(20) unsigned DEFAULT NULL,
  `batch` char(36) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`) USING BTREE,
  KEY `warehouseid_shopid` (`warehouseid`,`shopid`),
  KEY `createdate` (`createdate`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `supplier` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `itemprice` decimal(10,2) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL COMMENT '0:草稿，退回；  1:待审核  ；2:审核通过 ；3：已完成',
  `ischange` bit(1) DEFAULT b'0',
  `paystatus` int(11) DEFAULT NULL,
  `planitemid` bigint(20) unsigned DEFAULT NULL,
  `inwhstatus` int(11) DEFAULT NULL,
  `totalpay` decimal(10,2) DEFAULT '0.00',
  `totalre` int(11) DEFAULT '0',
  `totalin` int(11) DEFAULT '0',
  `totalch` int(11) DEFAULT '0',
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_entry_alibabainfo 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_alibabainfo` (
  `entryid` bigint(20) unsigned NOT NULL,
  `alibaba_auth` bigint(20) unsigned DEFAULT NULL,
  `alibaba_orderid` bigint(20) unsigned DEFAULT NULL,
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_entry_copy 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_copy` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `supplier` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `itemprice` decimal(10,2) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL COMMENT '0:草稿，退回；  1:待审核  ；2:审核通过 ；3：已完成',
  `paystatus` int(11) DEFAULT NULL,
  `planitemid` bigint(20) unsigned DEFAULT NULL,
  `inwhstatus` int(11) DEFAULT NULL,
  `totalpay` decimal(10,2) DEFAULT '0.00',
  `totalre` int(11) DEFAULT '0',
  `totalin` int(11) DEFAULT '0',
  `totalch` int(11) DEFAULT '0',
  `deliverydate` datetime DEFAULT NULL,
  `closerecdate` datetime DEFAULT NULL COMMENT '入库结束时间',
  `closepaydate` datetime DEFAULT NULL COMMENT '付款结束时间',
  `remark` varchar(500) DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `formid_materialid_planitemid` (`formid`,`materialid`,`planitemid`) USING BTREE,
  KEY `materialid` (`materialid`) USING BTREE,
  KEY `auditstatus` (`auditstatus`) USING BTREE,
  KEY `inwhstatus` (`inwhstatus`) USING BTREE,
  KEY `paystatus` (`paystatus`) USING BTREE,
  KEY `supplier` (`supplier`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_entry_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_history` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `supplier` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `amount` int(10) DEFAULT NULL,
  `itemprice` decimal(10,2) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `ischange` bit(1) DEFAULT b'0',
  `auditstatus` int(10) DEFAULT NULL COMMENT '0:草稿，退回；  1:待审核  ；2:审核通过 ；3：已完成,4.审核待下单',
  `paystatus` int(10) DEFAULT NULL,
  `planitemid` char(36) DEFAULT NULL,
  `inwhstatus` int(10) DEFAULT NULL,
  `totalpay` decimal(10,2) DEFAULT '0.00',
  `totalre` int(10) DEFAULT '0',
  `totalin` int(10) DEFAULT '0',
  `totalch` int(10) DEFAULT '0',
  `deliverydate` datetime DEFAULT NULL,
  `closerecdate` datetime DEFAULT NULL COMMENT '入库结束时间',
  `closepaydate` datetime DEFAULT NULL COMMENT '付款结束时间',
  `remark` varchar(500) DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  KEY `formid_materialid_planitemid` (`formid`,`materialid`,`planitemid`) USING BTREE,
  KEY `materialid` (`materialid`) USING BTREE,
  KEY `auditstatus` (`auditstatus`) USING BTREE,
  KEY `inwhstatus` (`inwhstatus`) USING BTREE,
  KEY `paystatus` (`paystatus`) USING BTREE,
  KEY `supplier` (`supplier`) USING BTREE,
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_entry_logistics 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_logistics` (
  `entryid` bigint(20) unsigned NOT NULL,
  `logisticsId` char(25) NOT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`entryid`,`logisticsId`),
  KEY `logisticsId` (`logisticsId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_payment 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_payment` (
  `id` bigint(20) unsigned NOT NULL,
  `formentryid` bigint(20) unsigned DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL COMMENT '1 已付款，2 请款中，0 驳回',
  `payment_method` int(11) DEFAULT NULL,
  `acct` bigint(20) unsigned DEFAULT NULL,
  `payprice` decimal(18,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(2000) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `projectid` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formentryid` (`formentryid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_payment_method 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_payment_method` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(100) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_form_receive 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_receive` (
  `id` bigint(20) unsigned NOT NULL,
  `formentryid` bigint(20) unsigned DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `ftype` char(10) DEFAULT NULL COMMENT 'in -入库，re -退货， ch-换货,clear-撤销入库',
  `amount` int(11) DEFAULT NULL,
  `remark` varchar(2000) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formentryid` (`formentryid`),
  KEY `opttime` (`opttime`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_plan 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_plan` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `number` char(36) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1' COMMENT '0-取消，1-工作中，2-提交',
  `creator` bigint(20) unsigned DEFAULT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `totalnum` int(11) DEFAULT NULL,
  `totalbuyqty` int(11) DEFAULT NULL,
  `totalpayprice` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_planitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planitem` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `subplanid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `status` tinyint(4) DEFAULT '1',
  `sales` int(11) DEFAULT '0',
  `amount` int(11) DEFAULT NULL,
  `itemprice` decimal(10,4) DEFAULT NULL,
  `orderprice` decimal(10,4) DEFAULT NULL,
  `supplier` char(36) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `isparent` bit(1) DEFAULT NULL,
  `parent` char(36) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`subplanid`,`materialid`,`warehouseid`),
  KEY `materialid` (`materialid`),
  KEY `idx_materialid_status` (`materialid`,`status`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_planitemsub 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planitemsub` (
  `id` bigint(20) unsigned DEFAULT NULL,
  `planitemid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `groupid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `warehouseid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `planamount` int(11) DEFAULT NULL COMMENT '实际发货量',
  PRIMARY KEY (`planitemid`,`warehouseid`,`groupid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='废表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_planmodel 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planmodel` (
  `planid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `modelid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `refreshtime` datetime DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  `operator` bigint(20) unsigned DEFAULT '0',
  PRIMARY KEY (`planid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_planmodelitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planmodelitem` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `modelid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `planamount` int(11) DEFAULT NULL COMMENT '建议补货量',
  `itemprice` decimal(10,4) DEFAULT NULL,
  `invamount` int(11) DEFAULT NULL,
  `orderprice` decimal(10,4) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `modelid_materialid` (`modelid`,`materialid`),
  KEY `idx_materialid_itemprice_planamount_invamount` (`materialid`,`itemprice`,`planamount`,`invamount`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_planmodelitemsub 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planmodelitemsub` (
  `itemid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `sku` char(50) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `groupid` bigint(20) unsigned NOT NULL COMMENT '断货时间',
  `needship` int(11) NOT NULL,
  `salesday` int(11) DEFAULT NULL,
  `aftersalesday` int(11) DEFAULT NULL,
  PRIMARY KEY (`itemid`,`sku`,`marketplaceid`,`groupid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_plansub 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_plansub` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `planid` bigint(20) unsigned DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '1代表在用，0代表放弃，2代表已提交',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `ftype` char(10) DEFAULT NULL COMMENT 'po代表订单，ao代表组装单',
  PRIMARY KEY (`id`),
  KEY `planid` (`planid`,`status`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_plan_warahouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_plan_warahouse` (
  `warehouseid` bigint(20) unsigned NOT NULL,
  `planid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`warehouseid`),
  KEY `planid` (`planid`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB COMMENT='入库仓库和补货规划的映射关系表，一个入库仓库不能在多个补货规划中出现，一个补货规划会有多个入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_warahouse_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_warahouse_material` (
  `planid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`planid`,`materialid`)
) ENGINE=InnoDB COMMENT='记录每个sku在补货规划中所默认的入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_purchase_warahouse_status 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_warahouse_status` (
  `warehouseid` bigint(20) unsigned NOT NULL,
  `purchase_status` int(5) DEFAULT '0' COMMENT '0表示改仓库无采购任务；1表示采购任务待处理；2表示采购任务已完成',
  `assbly_status` int(5) DEFAULT '0' COMMENT '0表示改仓库无组装任务；1表示组装任务待处理；2表示组装任务已完成',
  `userid` bigint(20) unsigned DEFAULT NULL,
  `opptime` datetime DEFAULT NULL,
  PRIMARY KEY (`warehouseid`)
) ENGINE=InnoDB COMMENT='记录每个仓库补货规划的状态，操作人，日期';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_serial_num 结构
CREATE TABLE IF NOT EXISTS `t_erp_serial_num` (
  `id` char(36) NOT NULL,
  `ftype` char(36) DEFAULT NULL,
  `seqno` int(11) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `prefix_date` date DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`prefix_date`,`ftype`,`shopid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_shipcycle 结构
CREATE TABLE IF NOT EXISTS `t_erp_shipcycle` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `sku` char(36) DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `transtype` bigint(20) unsigned DEFAULT NULL,
  `stockingCycle` int(10) DEFAULT NULL COMMENT '安全库存周期',
  `min_cycle` int(10) DEFAULT NULL COMMENT '最小发货周期',
  `first_leg_charges` decimal(12,2) DEFAULT NULL COMMENT '头程运输成本',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku_marketplaceid_groupid` (`sku`,`marketplaceid`,`groupid`)
) ENGINE=InnoDB AUTO_INCREMENT=17392024503393378049   COMMENT='FBA仓库配置';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_address 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_address` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(50) DEFAULT NULL COMMENT '名称或公司名称。',
  `groupid` bigint(20) unsigned DEFAULT NULL COMMENT '店铺id',
  `isfrom` bit(1) DEFAULT NULL COMMENT '1 代表发货地址，0代表收货地址',
  `addressLine1` varchar(300) DEFAULT NULL COMMENT '街道地址信息。',
  `addressLine2` varchar(300) DEFAULT NULL COMMENT '其他街道地址信息（如果需要）。',
  `city` char(50) DEFAULT NULL COMMENT '城市',
  `districtOrCounty` char(25) DEFAULT NULL COMMENT '区或县 ',
  `stateOrProvinceCode` char(20) DEFAULT NULL COMMENT '省份代码',
  `countryCode` char(2) DEFAULT NULL COMMENT '国家/地区代码',
  `postalCode` char(30) DEFAULT NULL COMMENT '邮政编码',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `phone` char(30) DEFAULT NULL,
  `isdefault` bit(1) DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_groupid_city` (`name`,`groupid`,`city`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_addressto 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_addressto` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(50) DEFAULT NULL COMMENT '名称或公司名称。',
  `isfrom` bit(1) DEFAULT NULL COMMENT '1 代表发货地址，0代表收货地址',
  `addressLine1` varchar(300) DEFAULT NULL COMMENT '街道地址信息。',
  `addressLine2` varchar(300) DEFAULT NULL COMMENT '其他街道地址信息（如果需要）。',
  `city` char(30) DEFAULT NULL COMMENT '城市',
  `districtOrCounty` char(25) DEFAULT NULL COMMENT '区或县 ',
  `stateOrProvinceCode` char(40) DEFAULT NULL COMMENT '省份代码',
  `countryCode` char(2) DEFAULT NULL COMMENT '国家/地区代码',
  `postalCode` char(30) DEFAULT NULL COMMENT '邮政编码',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `phone` char(30) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_config_carrier 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_config_carrier` (
  `country` char(10) NOT NULL,
  `name` char(30) NOT NULL,
  `transtyle` char(5) NOT NULL,
  PRIMARY KEY (`country`,`name`,`transtyle`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundbox 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundbox` (
  `id` bigint(20) unsigned NOT NULL,
  `shipmentid` char(36) DEFAULT NULL,
  `boxnum` int(11) DEFAULT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `unit` char(10) DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `wunit` char(10) DEFAULT NULL,
  `tracking_id` char(32) DEFAULT NULL,
  `package_status` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shipmentid_boxnum` (`shipmentid`,`boxnum`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundcase 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundcase` (
  `id` bigint(20) unsigned NOT NULL,
  `shipmentid` char(36) NOT NULL,
  `merchantsku` char(50) NOT NULL,
  `unitspercase` int(11) DEFAULT NULL,
  `numberofcase` int(11) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shipmentid` (`shipmentid`,`merchantsku`,`numberofcase`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inbounditem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inbounditem` (
  `id` bigint(20) unsigned NOT NULL,
  `ShipmentId` char(36) DEFAULT NULL,
  `inboundplanid` bigint(20) unsigned DEFAULT NULL COMMENT 'inboundplan的id',
  `FulfillmentNetworkSKU` char(36) DEFAULT NULL,
  `SellerSKU` char(50) DEFAULT NULL COMMENT '商品的卖家 SKU。',
  `QuantityShipped` int(11) DEFAULT NULL,
  `QuantityReceived` int(11) DEFAULT NULL,
  `QuantityInCase` int(11) DEFAULT NULL,
  `Quantity` int(11) DEFAULT NULL COMMENT '	要配送的商品数量。',
  `PrepInstruction` varchar(100) DEFAULT NULL,
  `PrepOwner` varchar(100) DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `msku` char(50) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `amazonauthid` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `unitcost` decimal(20,6) DEFAULT NULL,
  `totalcost` decimal(20,6) DEFAULT NULL,
  `unittransfee` decimal(20,6) DEFAULT NULL,
  `totaltransfee` decimal(20,6) DEFAULT NULL,
  `ReceivedDate` datetime DEFAULT NULL,
  `QuantityReceivedSub` int(11) DEFAULT NULL,
  `QuantityReceivedBalance` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 3` (`SellerSKU`,`ShipmentId`),
  KEY `FK_t_erp_ship_inboundplanitem_t_erp_ship_inboundplan` (`inboundplanid`),
  KEY `Index 4` (`ShipmentId`),
  KEY `idx_ShipmentId_QuantityReceived_QuantityShipped` (`ShipmentId`,`QuantityReceived`,`QuantityShipped`),
  KEY `SellerSKU_amazonauthid_marketplaceid_ReceivedDate` (`amazonauthid`,`marketplaceid`,`SellerSKU`,`ReceivedDate`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundplan 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundplan` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(200) DEFAULT NULL,
  `number` char(20) DEFAULT NULL,
  `shipFromAddressID` bigint(20) unsigned DEFAULT NULL COMMENT '	您的退货地址。(发货地址)',
  `skunum` int(11) DEFAULT NULL,
  `labelPrepType` char(50) DEFAULT NULL COMMENT '	入库货件所需的标签准备类型。',
  `AreCasesRequired` bit(1) DEFAULT NULL COMMENT '指明入库货件是否包含原厂包装发货商品。注： 货件所含的商品必须全部是原厂包装发货或者混装发货。',
  `amazongroupid` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(36) DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `auditstatus` tinyint(4) DEFAULT NULL COMMENT '1 已提交（待审核）；  3,已确认货件；   2已退回货件',
  `plansubid` char(36) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 3` (`createdate`),
  KEY `marketplaceid_warehouseid_shopid` (`shopid`,`marketplaceid`),
  KEY `warehouseid` (`warehouseid`),
  KEY `idx_amazongroupid_marketplaceid_shopid` (`amazongroupid`,`marketplaceid`,`shopid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundruntime 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundruntime` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(36) DEFAULT NULL,
  `put_on_days` int(11) DEFAULT NULL,
  `first_leg_days` int(11) DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_marketplaceid` (`shopid`,`marketplaceid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundshipment 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundshipment` (
  `ShipmentId` char(15) NOT NULL COMMENT '货件编号',
  `DestinationFulfillmentCenterId` char(50) DEFAULT NULL COMMENT '	亚马逊创建的亚马逊配送中心标识。',
  `ShipToAddressID` bigint(20) unsigned DEFAULT NULL COMMENT '目的地',
  `LabelPrepType` char(36) DEFAULT NULL,
  `ShipmentStatus` char(36) DEFAULT NULL COMMENT 'ShipmentStatus 值：WORKING - 卖家已创建货件，但未发货。 SHIPPED - 承运人已取件。',
  `inboundplanid` bigint(20) unsigned DEFAULT NULL COMMENT 'planId',
  `name` char(80) DEFAULT NULL COMMENT 'shipment_name',
  `TotalUnits` int(11) DEFAULT NULL COMMENT '装运单位数',
  `FeePerUnit` decimal(10,2) DEFAULT NULL COMMENT '单位手工加工费',
  `status` int(5) DEFAULT NULL COMMENT '-1,已驳回；0取消货件；1,待审核；2，配货（已确认货件）；3，装箱；4，物流信息确认；5已发货；6，已完成发货',
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
  `boxnum` int(11) DEFAULT NULL,
  `oldboxnum` int(11) DEFAULT NULL,
  `transtyle` char(10) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `shiped_date` datetime DEFAULT NULL,
  `start_receive_date` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `submissionid_excel` bigint(20) unsigned DEFAULT NULL,
  `submissionid` bigint(20) unsigned DEFAULT NULL,
  `feedstatus` char(50) DEFAULT NULL,
  `pro_number` char(10) DEFAULT NULL,
  `transport_status` char(50) DEFAULT NULL,
  `box_contents_source` char(10) DEFAULT NULL,
  `sync_inv` tinyint(4) DEFAULT '0' COMMENT '1代表没有扣库存，2代表已经扣库存',
  `ignorerec` bit(1) DEFAULT b'0' COMMENT '忽略收货异常',
  PRIMARY KEY (`ShipmentId`),
  KEY `Index 2` (`inboundplanid`),
  KEY `Index 3` (`status`),
  KEY `idx_inboundplanid_status_refreshtime` (`inboundplanid`,`status`,`refreshtime`),
  KEY `idx_inboundplanid_status_shipeddate_refreshtime` (`inboundplanid`,`status`,`shiped_date`,`refreshtime`),
  KEY `status5date` (`status5date`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundshipment_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundshipment_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shipmentid` char(15) NOT NULL COMMENT '货件编号',
  `status` int(5) DEFAULT NULL COMMENT '-1,已驳回；0取消货件；1,待审核；2，配货（已确认货件）；3，装箱；4，物流信息确认；5已发货；6，已完成发货',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ShipmentId` (`shipmentid`)
) ENGINE=InnoDB AUTO_INCREMENT=583704 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundshipment_traceupload 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundshipment_traceupload` (
  `shipmentid` char(20) NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `status` tinyint(3) DEFAULT NULL COMMENT '0待处理，1,已处理，2，处理失败',
  `errormsg` varchar(1000) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`shipmentid`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundtrans 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundtrans` (
  `id` bigint(20) unsigned NOT NULL,
  `shipmentid` char(36) DEFAULT NULL,
  `company` bigint(20) unsigned DEFAULT NULL,
  `channel` bigint(20) unsigned DEFAULT NULL,
  `singleprice` decimal(10,4) DEFAULT NULL,
  `transweight` decimal(10,4) DEFAULT NULL,
  `wunit` char(10) DEFAULT NULL,
  `otherfee` decimal(10,4) DEFAULT NULL,
  `ordernum` char(50) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  `outarrtime` datetime DEFAULT NULL,
  `inarrtime` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  `wtype` tinyint(4) DEFAULT '0',
  `transtype` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shipmentid` (`shipmentid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_inboundtrans_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundtrans_his` (
  `opttime` datetime NOT NULL,
  `id` bigint(20) unsigned NOT NULL,
  `shipmentid` char(36) NOT NULL,
  `company` bigint(20) unsigned DEFAULT NULL,
  `channel` bigint(20) unsigned DEFAULT NULL,
  `singleprice` decimal(10,4) DEFAULT NULL,
  `transweight` decimal(10,4) DEFAULT NULL,
  `wunit` char(10) DEFAULT NULL,
  `otherfee` decimal(10,4) DEFAULT NULL,
  `ordernum` char(50) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  `outarrtime` datetime DEFAULT NULL,
  `inarrtime` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  `wtype` tinyint(4) DEFAULT '0',
  `transtype` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`opttime`,`id`) USING BTREE,
  UNIQUE KEY `shipmentid` (`shipmentid`,`opttime`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_plan 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_plan` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL COMMENT '自有仓ID（parentid）',
  `amazongroupid` bigint(20) unsigned NOT NULL,
  `totalnum` int(11) NOT NULL COMMENT 'sku数量',
  `totalamount` int(11) NOT NULL COMMENT '发货量= sum(单个sku发货量）',
  `goodsworth` decimal(10,4) DEFAULT NULL COMMENT '发货货值',
  `totalweight` decimal(10,4) DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint(20) unsigned NOT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0代表放弃，1代表在用，2代表已提交',
  PRIMARY KEY (`id`),
  UNIQUE KEY `warehouseid_amazongroupid` (`warehouseid`,`amazongroupid`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_planitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planitem` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `plansubid` bigint(20) unsigned DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0代表已放弃，1 代表可用 2.代表已提交。如果plansub的status等于2这里的1 也是已提交',
  `sku` char(50) DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL COMMENT '实际发货量',
  `selfamount` int(11) DEFAULT NULL,
  `goodsworth` decimal(15,4) DEFAULT NULL COMMENT '货物货值',
  `planweight` decimal(15,4) DEFAULT NULL COMMENT 'itemweight*amount',
  `dimweight` decimal(15,4) DEFAULT NULL COMMENT '材积',
  `needship` int(11) DEFAULT NULL COMMENT '建议发货量',
  PRIMARY KEY (`id`),
  KEY `Index 2` (`plansubid`,`sku`),
  KEY `status_materialid` (`materialid`,`status`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_planmodel 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planmodel` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `planid` bigint(20) unsigned DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `索引 2` (`planid`,`groupid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_planmodelitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planmodelitem` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `modelid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `planamount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`modelid`,`materialid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_planmodelitemsub 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planmodelitemsub` (
  `itemid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `sku` char(50) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `needship` int(11) NOT NULL,
  `short_time` datetime DEFAULT NULL COMMENT '断货时间',
  `salesday` int(11) DEFAULT NULL,
  `aftersalesday` int(11) DEFAULT NULL,
  PRIMARY KEY (`itemid`,`marketplaceid`,`sku`),
  KEY `索引 2` (`sku`,`marketplaceid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_plansub 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_plansub` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `marketplaceid` char(15) DEFAULT NULL,
  `planid` bigint(20) unsigned DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0代表放弃，1代表在用，2代表已提交',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `planid` (`planid`,`status`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_plansub_euitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_plansub_euitem` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `planid` bigint(20) unsigned DEFAULT NULL,
  `sku` char(50) DEFAULT NULL,
  `plansubid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `marketplaceid` char(20) NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqid` (`plansubid`,`marketplaceid`,`sku`)
) ENGINE=InnoDB AUTO_INCREMENT=20672 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_shipment_template_file 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_shipment_template_file` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `filename` varchar(500) DEFAULT NULL,
  `filepath` varchar(1000) DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_status 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_status` (
  `status` char(20) NOT NULL,
  `content` char(200) NOT NULL DEFAULT '0',
  `name` char(50) DEFAULT NULL,
  PRIMARY KEY (`status`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_transchannel 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transchannel` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(50) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_transcompany 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transcompany` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '物流公司名称',
  `simplename` varchar(100) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `access_token` char(100) DEFAULT NULL,
  `api` int(10) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `uploadpath` varchar(200) DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  `oldid` char(36) DEFAULT NULL,
  `location` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`shopid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_transcompany_api 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transcompany_api` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `api` varchar(200) NOT NULL DEFAULT '0',
  `name` varchar(200) NOT NULL DEFAULT '0',
  `openkey` varchar(200) NOT NULL DEFAULT '0',
  `openaccount` varchar(200) NOT NULL DEFAULT '0',
  `url` varchar(500) DEFAULT NULL,
  `system` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_transcompany_services_zhihui 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transcompany_services_zhihui` (
  `code` char(50) NOT NULL,
  `apiid` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `ftype` char(10) DEFAULT NULL,
  PRIMARY KEY (`code`,`apiid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_transdetail 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transdetail` (
  `id` bigint(20) unsigned NOT NULL,
  `company` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `subarea` char(20) DEFAULT NULL,
  `channel` bigint(20) unsigned DEFAULT NULL,
  `channame` char(36) DEFAULT NULL,
  `pretime` int(11) DEFAULT NULL COMMENT 'US预计时效',
  `price` decimal(10,4) DEFAULT NULL,
  `drate` int(11) DEFAULT '5000',
  `opttime` datetime DEFAULT NULL,
  `transtype` bigint(20) unsigned DEFAULT NULL,
  `priceunits` char(10) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `cbmrate` int(11) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`company`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_ship_transdetail_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transdetail_his` (
  `id` bigint(20) unsigned NOT NULL,
  `opttime` datetime NOT NULL,
  `company` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `subarea` char(20) DEFAULT NULL,
  `channel` char(36) DEFAULT NULL,
  `channame` char(36) DEFAULT NULL,
  `pretime` int(11) DEFAULT NULL COMMENT 'US预计时效',
  `price` decimal(10,4) DEFAULT NULL,
  `drate` int(11) DEFAULT '5000',
  `transtype` bigint(20) unsigned DEFAULT NULL,
  `priceunits` char(10) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `cbmrate` int(11) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `disabled` bit(1) DEFAULT b'0',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`opttime`,`id`) USING BTREE,
  KEY `Index 2` (`company`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_stepwise_quotn 结构
CREATE TABLE IF NOT EXISTS `t_erp_stepwise_quotn` (
  `id` bigint(20) unsigned NOT NULL,
  `material` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `material_amount` (`material`,`amount`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_stockcycle 结构
CREATE TABLE IF NOT EXISTS `t_erp_stockcycle` (
  `id` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `stockingCycle` int(10) DEFAULT NULL COMMENT '安全库存周期',
  `min_cycle` int(10) DEFAULT NULL COMMENT '最小补货周期/最小发货周期',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `warehouseid_materialid` (`warehouseid`,`materialid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_stocktaking 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `isworking` bit(1) DEFAULT NULL,
  `ftype` tinyint(4) DEFAULT NULL,
  `whtotalamount` int(11) DEFAULT NULL,
  `whtotalprice` decimal(15,4) DEFAULT NULL,
  `overamount` int(11) DEFAULT NULL,
  `lossamount` int(11) DEFAULT NULL,
  `overprice` decimal(12,4) DEFAULT NULL,
  `lossprice` decimal(12,4) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_stocktaking_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking_item` (
  `id` bigint(20) unsigned NOT NULL,
  `stocktakingid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `overamount` int(11) DEFAULT '0',
  `lossamount` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `stocktaking_mate_ware` (`stocktakingid`,`warehouseid`,`materialid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_stocktaking_item_shelf 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking_item_shelf` (
  `id` bigint(20) unsigned NOT NULL,
  `stocktakingid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `shelfid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `overamount` int(11) DEFAULT '0',
  `lossamount` int(11) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `stocktaking_mate_ware` (`stocktakingid`,`warehouseid`,`materialid`,`shelfid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_stocktaking_shelf 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking_shelf` (
  `stocktakingid` bigint(20) unsigned NOT NULL,
  `shelfid` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`stocktakingid`,`shelfid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_stocktaking_warehouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking_warehouse` (
  `stocktakingid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`stocktakingid`,`warehouseid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_summary_data 结构
CREATE TABLE IF NOT EXISTS `t_erp_summary_data` (
  `id` bigint(20) unsigned NOT NULL,
  `ftype` char(20) NOT NULL,
  `value` decimal(10,2) DEFAULT NULL,
  `mapdata` varchar(4000) DEFAULT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `索引 2` (`shopid`,`ftype`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='主页上的数据，每日更新';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_transtype 结构
CREATE TABLE IF NOT EXISTS `t_erp_transtype` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `name` char(50) DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_turnover_rate 结构
CREATE TABLE IF NOT EXISTS `t_erp_turnover_rate` (
  `id` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `sku` char(36) DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `begininv` int(11) DEFAULT NULL,
  `endinv` int(11) DEFAULT NULL,
  `outinv` int(11) DEFAULT NULL,
  `wrate` decimal(10,2) DEFAULT NULL,
  `wday` decimal(10,2) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_unsalable 结构
CREATE TABLE IF NOT EXISTS `t_erp_unsalable` (
  `sku` char(30) DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `invqty` int(11) DEFAULT NULL,
  `invinqty` int(11) DEFAULT NULL,
  `inv90` int(11) DEFAULT NULL,
  `inv180` int(11) DEFAULT NULL,
  `inv365` int(11) DEFAULT NULL,
  `invout90` int(11) DEFAULT NULL,
  `over90` int(11) DEFAULT NULL
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_usersales_rank 结构
CREATE TABLE IF NOT EXISTS `t_erp_usersales_rank` (
  `userid` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `daytype` int(11) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `oldorderprice` decimal(10,2) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`userid`,`shopid`,`daytype`),
  KEY `createdate` (`createdate`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_calculate_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_calculate_record` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `ftype` char(20) NOT NULL COMMENT '计算类型，发货，采购，人力',
  `iswarn` bit(1) NOT NULL DEFAULT b'0',
  `operator` bigint(20) unsigned NOT NULL COMMENT '计算操作人',
  `opttime` datetime NOT NULL COMMENT '计算时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_ftype` (`shopid`,`ftype`)
) ENGINE=InnoDB COMMENT='用于存储各个计算模块的计算时间，计算人。统一留存历史记录';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_calculate_record_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_calculate_record_history` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `ftype` char(20) DEFAULT NULL,
  `iswarn` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_ftype` (`shopid`,`ftype`,`opttime`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_man_month 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_man_month` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `month` date DEFAULT NULL COMMENT '对应月份',
  `standardtime` int(11) DEFAULT NULL COMMENT '以标准工时计算的月份对应的总时长',
  `standardperson` int(11) DEFAULT NULL COMMENT '以标准工时算对应所需要的人力',
  `overtime` int(11) DEFAULT NULL COMMENT '以超出时间算的对应时长',
  `overperson` int(11) DEFAULT NULL COMMENT '以超出时间算的对应人力',
  `multiple` float DEFAULT NULL COMMENT '当前月份计算的倍数',
  `unitsoftime` int(11) DEFAULT NULL COMMENT '一个员工常规工作时间',
  `overoftime` int(11) DEFAULT NULL COMMENT '超出时间（即加班） 工作时间',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_month` (`shopid`,`month`)
) ENGINE=InnoDB COMMENT='人力计算结果保存';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_man_month_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_man_month_history` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `month` date DEFAULT NULL,
  `standardtime` int(11) DEFAULT NULL,
  `standardperson` int(11) DEFAULT NULL,
  `overtime` int(11) DEFAULT NULL,
  `overperson` int(11) DEFAULT NULL,
  `multiple` float DEFAULT NULL,
  `unitsoftime` int(11) DEFAULT NULL,
  `overoftime` int(11) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_month` (`shopid`,`month`,`opttime`) USING BTREE
) ENGINE=InnoDB COMMENT='人力计算结果历史';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_pickpay_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_pickpay_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(50) NOT NULL DEFAULT '' COMMENT '订单编码',
  `month` date DEFAULT NULL COMMENT '对应月份',
  `shopid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '公司ID',
  `operator` bigint(20) unsigned NOT NULL COMMENT '创建人',
  `creator` bigint(20) unsigned NOT NULL COMMENT '操作人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `month_shopid` (`month`,`shopid`)
) ENGINE=InnoDB  COMMENT='采购提货与付款模块分组，采用一个月一个表单的结构，对采购付款历史与审核进行保存';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_pickpay_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_pickpay_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned NOT NULL COMMENT '订单ID',
  `materialid` bigint(20) unsigned NOT NULL COMMENT '产品ID',
  `auditor` bigint(20) unsigned NOT NULL COMMENT '审核人',
  `auditstatus` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '审核状态',
  `audittime` datetime NOT NULL COMMENT '审核时间',
  `supplier` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '供应商',
  `inbound` int(11) NOT NULL DEFAULT '0' COMMENT '待入库数量',
  `suggest` varchar(500) NOT NULL DEFAULT '0' COMMENT '建议提货量',
  `planpick` int(11) NOT NULL DEFAULT '0' COMMENT '建议付款金额',
  `planpay` decimal(14,2) NOT NULL DEFAULT '0.00' COMMENT '计划付款金额',
  `remark` varchar(500) NOT NULL DEFAULT '0' COMMENT '计划提货量',
  `operator` bigint(20) unsigned NOT NULL COMMENT '操作人',
  `creator` bigint(20) unsigned NOT NULL COMMENT '创建人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='提货付款模块SKU从审核到通过以及历史的具体entry表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_presale_month 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_month` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `pid` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `month` date NOT NULL COMMENT '月日期，每个月1号',
  `amount` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '月销量',
  `opttime` datetime DEFAULT NULL COMMENT '修改日期',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`pid`,`month`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB COMMENT='销售预测月度结果';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_presale_month_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_month_form` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `year` int(10) unsigned DEFAULT NULL COMMENT '年份',
  `month` int(10) unsigned DEFAULT NULL COMMENT '月份',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creator` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `creattime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_year_month` (`shopid`,`year`,`month`)
) ENGINE=InnoDB COMMENT='销售预测提交的表单，以每个月一份表单的方式存储整个公司关于销售预测的提交';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_presale_month_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_month_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned NOT NULL COMMENT '本地产品ID ',
  `pid` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `auditstatus` int(10) unsigned DEFAULT '0' COMMENT '0未提交，1 提交待审核，2审核成功  3已驳回',
  `audittime` datetime DEFAULT NULL COMMENT '审核时间',
  `auditor` bigint(20) unsigned DEFAULT '0' COMMENT '审核人',
  `status` int(10) unsigned DEFAULT '0' COMMENT '维持 提升 等销售状态 默认是无',
  `remark` varchar(500) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creatime` datetime DEFAULT NULL COMMENT '创建时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `creator` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_one` (`pid`,`formid`)
) ENGINE=InnoDB COMMENT='销售预测单个SKU所以的审核与历史表单对应';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_presale_month_form_entry_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_month_form_entry_item` (
  `id` bigint(20) unsigned NOT NULL,
  `entryid` bigint(20) unsigned NOT NULL COMMENT '订单中产品所对应记录的ID',
  `year` int(10) unsigned NOT NULL COMMENT '年份',
  `month` int(10) unsigned NOT NULL COMMENT '月份',
  `amount` int(10) NOT NULL DEFAULT '0' COMMENT '对应计划数量',
  `opttime` datetime DEFAULT NULL COMMENT '操作人',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_one` (`entryid`,`year`,`month`)
) ENGINE=InnoDB COMMENT='销售预测每个SKU对应每个月的预测数据';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_presale_week 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_week` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `pid` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `week` date NOT NULL COMMENT '周日期',
  `amount` int(10) NOT NULL DEFAULT '0' COMMENT '周预估销量',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`pid`,`week`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB COMMENT='销售预测周结构数据存储';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(50) NOT NULL DEFAULT '' COMMENT '采购订单编码',
  `auditor` bigint(20) unsigned DEFAULT NULL COMMENT '审核人',
  `auditstatus` int(11) DEFAULT '0' COMMENT '审核状态',
  `audittime` datetime DEFAULT NULL COMMENT '审核时间',
  `skunum` int(11) DEFAULT NULL COMMENT 'SKU数量',
  `warehouseid` bigint(20) unsigned DEFAULT NULL COMMENT '仓库',
  `shopid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '公司ID',
  `operator` bigint(20) unsigned NOT NULL COMMENT '操作人',
  `creator` bigint(20) unsigned NOT NULL COMMENT '创建人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB  COMMENT='采购模块表单保存表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned NOT NULL COMMENT '订单ID',
  `materialid` bigint(20) unsigned NOT NULL COMMENT '本地产品ID',
  `warehouse` char(50) NOT NULL DEFAULT '' COMMENT '仓库',
  `sumreq` int(11) NOT NULL DEFAULT '0' COMMENT '需求量',
  `salemonth` int(11) NOT NULL DEFAULT '0' COMMENT '月销量',
  `presalemonth` varchar(500) NOT NULL DEFAULT '0' COMMENT '销售预测',
  `moreqty` char(50) NOT NULL DEFAULT '0' COMMENT '多余库存',
  `suggest` varchar(500) NOT NULL DEFAULT '0' COMMENT '月度建议采购量',
  `needqty` int(11) NOT NULL DEFAULT '0' COMMENT '需求量',
  `sugpurchase` int(11) NOT NULL DEFAULT '0' COMMENT '建议采购量',
  `planpurchase` int(11) NOT NULL DEFAULT '0' COMMENT '计划采购量',
  `detail` text COMMENT '当时计算的需求详情与提货详情',
  `operator` bigint(20) unsigned NOT NULL COMMENT '操作人',
  `creator` bigint(20) unsigned NOT NULL COMMENT '创建人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_material` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `materialid` bigint(20) unsigned NOT NULL COMMENT '本地产品ID',
  `purchasedaynum` int(10) DEFAULT NULL COMMENT '采购周期天数',
  `needamount` int(10) DEFAULT NULL COMMENT '需求量（通过采购周期内需求量汇总减去库存得出）',
  `suggestamount` int(10) DEFAULT NULL COMMENT '建议采购量（通过对需求量合箱规得出）',
  `sumrquest` int(10) DEFAULT NULL COMMENT '总需求量',
  `findex` int(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`materialid`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE,
  KEY `sumrquest` (`sumrquest`)
) ENGINE=InnoDB COMMENT='产品采购计划，计算结果，存储每个SKU对应的采购周期，需求量，建议采购量，总需求量等';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_material_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_material_history` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `purchasedaynum` int(10) NOT NULL DEFAULT '0',
  `needamount` int(10) NOT NULL DEFAULT '0',
  `suggestamount` int(10) NOT NULL DEFAULT '0',
  `sumrquest` int(10) NOT NULL DEFAULT '0',
  `findex` int(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`materialid`,`opttime`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_selected 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_selected` (
  `materialid` bigint(20) unsigned NOT NULL COMMENT '本地产品ID',
  `userid` bigint(20) unsigned NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`materialid`,`userid`),
  KEY `userid` (`userid`)
) ENGINE=InnoDB COMMENT='采购计算结果选中发货，记录是否选中';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_week 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_week` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `materialid` bigint(20) unsigned NOT NULL COMMENT '本地产品ID',
  `week` date NOT NULL COMMENT '周日期',
  `requestqty` int(10) NOT NULL DEFAULT '0' COMMENT '需求量（将商品销量通过对关系与本地产品对应）',
  `moreqty` int(10) DEFAULT '0' COMMENT '多余库存（库存减去对应安全库存+头程周期+增长天数对应需求量的和）',
  `suggestqty` int(10) DEFAULT '0' COMMENT '建议提货量（将需求量+剩余库存，缺少部分通过合箱规后）',
  `differentqty` int(10) DEFAULT '0' COMMENT '差异数量-（销售预测与库存之间的差异值）',
  `isfull` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`materialid`,`week`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB COMMENT='产品采购计划，通过发货需求与本地产品对应，并进行本地SKU转化，组装子SKU转换\r\n采购周情况存储。包换需求量，多余库存等';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_purchase_week_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_week_history` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `week` date NOT NULL,
  `requestqty` int(10) NOT NULL DEFAULT '0',
  `moreqty` int(10) DEFAULT '0',
  `suggestqty` int(10) DEFAULT '0',
  `differentqty` int(10) DEFAULT '0',
  `isfull` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`materialid`,`week`,`opttime`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB  COMMENT='采购计算周数据历史保存';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_ship_setting 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_ship_setting` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `addnum` int(11) DEFAULT NULL,
  `startday` int(11) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB COMMENT='发货计划设置，用于保存发货计算中当达成率与增长率达到标准时增加的天数';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_ship_week 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_ship_week` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `pid` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `week` date NOT NULL COMMENT '对应周日期',
  `amount` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '对应周需求量',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`pid`,`week`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB COMMENT='发货计划周情况，记录发货计算历史结果,根据销售预测得到需求量';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_plan_ship_week_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_ship_week_history` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `pid` bigint(20) unsigned NOT NULL,
  `week` date NOT NULL,
  `amount` int(10) unsigned NOT NULL DEFAULT '0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`pid`,`week`,`opttime`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_productl_workhours 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_productl_workhours` (
  `mid` bigint(20) unsigned NOT NULL COMMENT 'pid',
  `amount` int(10) unsigned DEFAULT NULL COMMENT '一个小时内的生产量',
  `ftype` char(10)    DEFAULT 'hour' COMMENT '类型: hour mins second等 默认hour',
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`mid`) USING BTREE
) ENGINE=InnoDB COMMENT='人力计算配置表\r\n标准工时/H	待增加	每个小时能处理多少个该产品的组装及打包发货工作	产品信息管理	\r\n独立产品及组装产品的主SKU（在售成品）需要设置【标准工时】。用作计算发货工时及人力需求。	\r\n如有产品需要完成组装（工作较为复杂），则可能为“12”，意为每小时从拣货、组装到装箱打包可以处理12个该产品。如流程较为简单，则可能为“35”，即为每小时可处理35个。\r\n';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_ship_product_delivery_cycle 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_ship_product_delivery_cycle` (
  `pid` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `deliverycycle` int(10) unsigned DEFAULT NULL COMMENT '头程天数',
  `findex` int(10) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB COMMENT='商品的头程周期存储';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v2_shop_units_worktime 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_shop_units_worktime` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `multiple` float DEFAULT NULL COMMENT '工时倍数',
  `unitsoftime` int(10) DEFAULT NULL COMMENT '每个人一个月的正常工作时间 h 工作时长（常规）	待增加	工作时长/人/月	增加参数设置（蔚蓝使用为204小时）	用作计算所需人力数量',
  `overoftime` int(10) DEFAULT NULL COMMENT '工作时长（较长）	待增加	工作时长/人/月	增加参数设置（蔚蓝使用为286小时）	用作计算所需人力数量',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB COMMENT='人力计算配置表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v3_purchase_plan 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `min_cycle` int(10) unsigned DEFAULT NULL,
  `stocking_cycle` int(10) unsigned DEFAULT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `disable` bit(1) NOT NULL DEFAULT b'0',
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v3_purchase_plan_consumable_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_consumable_item` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `amount` int(10) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`materialid`,`warehouseid`) USING BTREE,
  KEY `materialid` (`warehouseid`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v3_purchase_plan_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_item` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `planid` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `batchnumber` char(20) DEFAULT NULL,
  `amount` int(10) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`planid`,`materialid`) USING BTREE,
  KEY `materialid` (`warehouseid`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v3_purchase_plan_item_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_item_history` (
  `id` char(36) NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `planid` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `batchnumber` char(20) DEFAULT NULL,
  `amount` int(10) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`planid`,`warehouseid`,`batchnumber`,`materialid`) USING BTREE,
  KEY `materialid` (`materialid`) USING BTREE,
  KEY `shopid_opttime` (`shopid`,`warehouseid`,`opttime`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v3_purchase_plan_warehouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_warehouse` (
  `warehouseid` bigint(20) unsigned NOT NULL,
  `planid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`planid`,`warehouseid`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE,
  KEY `planid` (`warehouseid`) USING BTREE
) ENGINE=InnoDB COMMENT='入库仓库和补货规划的映射关系表，一个入库仓库不能在多个补货规划中出现，一个补货规划会有多个入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_v3_purchase_plan_warehouse_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_warehouse_material` (
  `planid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`planid`,`materialid`) USING BTREE
) ENGINE=InnoDB COMMENT='记录每个sku在补货规划中所默认的入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'ID',
  `name` char(36) DEFAULT NULL COMMENT '名称',
  `ftype` char(36) NOT NULL COMMENT '类型',
  `flevel` char(36) DEFAULT NULL COMMENT '级别',
  `number` char(36) NOT NULL COMMENT '编号',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `findex` int(11) DEFAULT NULL COMMENT '次序',
  `country` char(15) DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `fbawareid` bigint(20) unsigned DEFAULT NULL COMMENT '海外仓',
  `isdefault` bit(1) DEFAULT b'0' COMMENT '默认仓库',
  `shopid` bigint(20) unsigned NOT NULL COMMENT '店铺',
  `parentid` bigint(20) unsigned DEFAULT NULL COMMENT '父节点',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `addressid` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  `stocking_cycle` int(11) DEFAULT '0',
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  `isstocktaking` bit(1) DEFAULT b'0',
  `min_cycle` int(10) DEFAULT '0',
  `first_leg_charges` decimal(12,2) DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  `ishungry` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `Index 2` (`parentid`),
  KEY `ftype` (`ftype`),
  KEY `shopid` (`shopid`),
  KEY `name_shopid` (`name`,`shopid`) USING BTREE,
  KEY `addressid` (`addressid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse_address 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_address` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `shopid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `name` char(50) NOT NULL DEFAULT '0' COMMENT '地址名称',
  `number` char(50) NOT NULL DEFAULT '0',
  `detail` varchar(500) DEFAULT '0' COMMENT '地址街道详情',
  `postcode` char(50) DEFAULT '0' COMMENT '邮编',
  `phone` char(50) DEFAULT NULL COMMENT '业主电话',
  `landlord` char(50) DEFAULT NULL COMMENT '业主（房东）',
  `lost_effect_date` datetime DEFAULT NULL COMMENT '到期时间',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `disabled` bit(1) DEFAULT b'0' COMMENT '是否失效（是否删除）',
  `operator` bigint(20) unsigned DEFAULT '0' COMMENT '修改人',
  `opttime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `creator` bigint(20) unsigned DEFAULT '0' COMMENT '创建人',
  `creattime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_name` (`shopid`,`name`) USING BTREE,
  UNIQUE KEY `shopid_number` (`shopid`,`number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=51 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse_fba 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_fba` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `shopid` bigint(20) unsigned NOT NULL COMMENT '店铺',
  `marketplaceid` char(15) NOT NULL,
  `name` char(36) DEFAULT NULL COMMENT '名称',
  `number` char(36) DEFAULT NULL,
  `stocking_cycle` int(11) DEFAULT '0' COMMENT '安全库存周期',
  `min_cycle` int(10) DEFAULT '0' COMMENT '最小发货周期',
  `put_on_days` int(10) DEFAULT '0' COMMENT '上架周期',
  `first_leg_days` int(10) DEFAULT '0' COMMENT '头程周期',
  `remark` varchar(1000) DEFAULT '0',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_marketplaceid` (`shopid`,`marketplaceid`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse_shelf 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '货柜ID',
  `addressid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '仓库ID',
  `name` varchar(200) NOT NULL DEFAULT '0' COMMENT '货柜名称',
  `number` char(50) NOT NULL DEFAULT '0' COMMENT '编码',
  `capacity` float NOT NULL DEFAULT '0' COMMENT '容量(立方厘米)',
  `length` float unsigned NOT NULL DEFAULT '0' COMMENT '长度(cm)',
  `width` float unsigned NOT NULL DEFAULT '0' COMMENT '宽度(cm)',
  `height` float unsigned NOT NULL DEFAULT '0' COMMENT '高度(cm)',
  `parentid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '父货柜ID',
  `sort` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '排序即（柜子所在位置）',
  `treepath` char(200) NOT NULL DEFAULT '0' COMMENT '所有付货柜编码如：A01!033!F01',
  `shopid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '公司ID',
  `iswarn` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否报警',
  `isdelete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否逻辑删除',
  `isfrozen` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否冻结',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creator` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `creattime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `parentid` (`parentid`) USING BTREE,
  KEY `treepath` (`shopid`,`addressid`,`treepath`) USING BTREE
) ENGINE=InnoDB COMMENT='仓库货柜';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse_shelf_inventory 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf_inventory` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `shelfid` bigint(20) unsigned DEFAULT NULL COMMENT '货柜ID',
  `materialid` bigint(20) unsigned DEFAULT NULL COMMENT '产品ID',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `quantity` int(10) unsigned DEFAULT NULL COMMENT '当前数量',
  `size` float unsigned DEFAULT NULL COMMENT '当前体积',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shelfid_materialid_shopid` (`shopid`,`shelfid`,`materialid`) USING BTREE,
  UNIQUE KEY `materialid_shopid` (`shopid`,`materialid`,`shelfid`) USING BTREE
) ENGINE=InnoDB COMMENT='货架产品库存';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse_shelf_inventory_opt_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf_inventory_opt_record` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `shelfid` bigint(20) unsigned DEFAULT NULL COMMENT '货柜ID',
  `materialid` bigint(20) unsigned DEFAULT NULL COMMENT '产品ID',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `quantity` int(10) unsigned DEFAULT NULL COMMENT '操作数量',
  `size` float unsigned DEFAULT NULL COMMENT '操作数量对于的体积使用立方厘米cm3',
  `balance_qty` int(10) unsigned DEFAULT NULL COMMENT '操作后结余数量',
  `balance_size` float unsigned DEFAULT NULL COMMENT '操作后结余体积',
  `opt` int(10) unsigned DEFAULT NULL COMMENT '0：出库；1：入库;2：修正下架；3：修正上架',
  `formid` bigint(20) unsigned DEFAULT NULL COMMENT '表单ID',
  `formtype` char(50) DEFAULT NULL COMMENT '表单类型',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shelfid_materialid_shopid_formid_formtype` (`shopid`,`shelfid`,`materialid`),
  KEY `formid_formtype` (`formid`,`formtype`)
) ENGINE=InnoDB COMMENT='操作记录';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_warehouse_type 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_type` (
  `id` char(36) NOT NULL COMMENT 'ID',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '店铺',
  `issystem` bit(1) DEFAULT NULL COMMENT '是否系统',
  `name` char(50) DEFAULT NULL COMMENT '名字',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_erp_whse_unsalable_rpt 结构
CREATE TABLE IF NOT EXISTS `t_erp_whse_unsalable_rpt` (
  `shopid` bigint(20) unsigned NOT NULL,
  `wid` bigint(20) unsigned NOT NULL,
  `name` char(36) DEFAULT NULL,
  `mtid` bigint(20) unsigned NOT NULL,
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_exchangeinfo 结构
CREATE TABLE IF NOT EXISTS `t_exchangeinfo` (
  `currency` varchar(50) NOT NULL COMMENT '币别',
  `name` varchar(50) DEFAULT NULL,
  `type` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`currency`) USING BTREE,
  UNIQUE KEY `symbol` (`name`) USING BTREE
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_exchangerate 结构
CREATE TABLE IF NOT EXISTS `t_exchangerate` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '币别',
  `price` decimal(18,6) DEFAULT NULL COMMENT '汇率',
  `symbol` varchar(50) DEFAULT NULL,
  `type` varchar(15) DEFAULT NULL,
  `utctime` datetime DEFAULT NULL COMMENT '服务器更新时间',
  `volume` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=444945    ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_exchangerate_customer 结构
CREATE TABLE IF NOT EXISTS `t_exchangerate_customer` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `name` char(5) NOT NULL DEFAULT '' COMMENT '币别',
  `price` decimal(18,6) DEFAULT NULL COMMENT '汇率',
  `symbol` varchar(50) DEFAULT NULL,
  `type` varchar(15) DEFAULT NULL,
  `utctime` datetime DEFAULT NULL COMMENT '服务器更新时间',
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`shopid`,`name`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_exchangerate_his 结构
CREATE TABLE IF NOT EXISTS `t_exchangerate_his` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '币别',
  `price` decimal(18,6) DEFAULT NULL COMMENT '汇率',
  `symbol` varchar(50) DEFAULT NULL,
  `type` varchar(15) DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `isnewest` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `index_name` (`name`,`byday`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=467142  ROW_FORMAT=DYNAMIC COMMENT='汇率';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fbaformat 结构
CREATE TABLE IF NOT EXISTS `t_fbaformat` (
  `id` char(36) NOT NULL,
  `country` char(20) DEFAULT NULL,
  `ismedia` bit(1) DEFAULT NULL,
  `producttierId` char(36) DEFAULT NULL,
  `fba_format` varchar(500) DEFAULT NULL,
  `month` varchar(500) DEFAULT NULL,
  `weight` decimal(10,4) DEFAULT NULL COMMENT '最大重量',
  `effective_date` date DEFAULT NULL COMMENT '生效日期',
  `expiry_date` date DEFAULT NULL COMMENT '失效日期',
  `dispatch_type` char(36)  DEFAULT NULL COMMENT '亚马逊配送方案',
  `isclothing` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_fbaformat_t_producttier` (`producttierId`),
  KEY `country` (`country`)
) ENGINE=InnoDB  COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fbaformat_archive 结构
CREATE TABLE IF NOT EXISTS `t_fbaformat_archive` (
  `id` char(36) NOT NULL,
  `country` char(20) DEFAULT NULL,
  `ismedia` bit(1) DEFAULT NULL,
  `producttierId` char(36) DEFAULT NULL,
  `fba_format` varchar(500) DEFAULT NULL,
  `month` varchar(500) DEFAULT NULL,
  `weight` decimal(10,4) DEFAULT NULL COMMENT '最大重量',
  `effective_date` date DEFAULT NULL COMMENT '生效日期',
  `expiry_date` date DEFAULT NULL COMMENT '失效日期',
  `dispatch_type` char(36)  DEFAULT NULL COMMENT '亚马逊配送方案',
  `isclothing` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK_t_fbaformat_t_producttier` (`producttierId`) USING BTREE,
  KEY `country` (`country`) USING BTREE
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fba_estimated_fees 结构
CREATE TABLE IF NOT EXISTS `t_fba_estimated_fees` (
  `sku` char(50) NOT NULL,
  `fnsku` char(50) DEFAULT NULL,
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
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`sku`,`asin`,`amazonAuthId`,`marketplaceid`),
  KEY `index_auth` (`amazonAuthId`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

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
) ENGINE=InnoDB COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fba_longterm_storage_fee_report 结构
CREATE TABLE IF NOT EXISTS `t_fba_longterm_storage_fee_report` (
  `id` bigint(20) unsigned NOT NULL,
  `snapshot_date` date NOT NULL,
  `sku` char(50),
  `fnsku` char(50) DEFAULT NULL,
  `condition` char(50) DEFAULT NULL,
  `asin` char(255) DEFAULT NULL,
  `qty_charged_12month` int(11) DEFAULT NULL COMMENT '存储超过12个月的产品被收费的数量',
  `qty_charged_6month` int(11) DEFAULT NULL,
  `qty_charged` int(11) DEFAULT NULL,
  `amount_6` decimal(10,2) DEFAULT NULL,
  `amount_12` decimal(10,2) DEFAULT NULL,
  `amount_charged` decimal(10,2) DEFAULT NULL,
  `surcharge_age_tier` char(20) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `per_unit_volume` decimal(12,2) DEFAULT NULL,
  `rate_surcharge` decimal(12,2) DEFAULT NULL,
  `volume_unit` decimal(10,2) DEFAULT NULL,
  `country` char(10) DEFAULT NULL,
  `is_sl` bit(1) DEFAULT b'0',
  `amazonauthid` bigint(20) unsigned,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`snapshot_date`,`id`) USING BTREE,
  UNIQUE KEY `snapshot_date_sku_surcharge_age_tier_country_amazonauthid` (`snapshot_date`,`sku`,`surcharge_age_tier`,`country`,`amazonauthid`) USING BTREE,
  KEY `date-sku-country` (`amazonauthid`,`country`,`sku`,`snapshot_date`) USING BTREE,
  KEY `snapshot_date` (`amazonauthid`,`country`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fba_reimbursements_fee_report 结构
CREATE TABLE IF NOT EXISTS `t_fba_reimbursements_fee_report` (
  `id` bigint(20) unsigned NOT NULL,
  `approval_date` datetime NOT NULL,
  `reimbursement_id` char(20) NOT NULL,
  `case_id` char(20) DEFAULT NULL,
  `amazon_order_id` char(20) DEFAULT NULL,
  `reason` varchar(50) DEFAULT NULL COMMENT '原因',
  `sku` char(50) DEFAULT NULL,
  `fnsku` char(20) DEFAULT NULL,
  `asin` char(15) DEFAULT NULL,
  `conditions` char(20) DEFAULT NULL,
  `currency_unit` char(5) DEFAULT NULL,
  `amount_per_unit` decimal(16,4) DEFAULT NULL,
  `amount_total` decimal(16,4) DEFAULT NULL,
  `quantity_reimbursed_cash` int(10) DEFAULT NULL,
  `quantity_reimbursed_inventory` int(10) DEFAULT NULL,
  `quantity_reimbursed_total` int(10) DEFAULT NULL,
  `original_reimbursement_id` char(50) DEFAULT NULL,
  `original_reimbursement_type` char(50) DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`,`approval_date`,`id`) USING BTREE,
  KEY `marketplaceid` (`marketplaceid`) USING BTREE,
  KEY `reimbursement_id` (`sku`,`approval_date`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fba_storage_fee_report 结构
CREATE TABLE IF NOT EXISTS `t_fba_storage_fee_report` (
  `id` bigint(20) unsigned NOT NULL,
  `asin` char(20) NOT NULL,
  `fnsku` char(20) DEFAULT NULL,
  `fulfillment_center` char(15) DEFAULT NULL,
  `country` char(10) NOT NULL COMMENT 'country_code',
  `longest_side` decimal(10,2) DEFAULT NULL,
  `median_side` decimal(10,2) DEFAULT NULL,
  `shortest_side` decimal(10,2) DEFAULT NULL,
  `measurement_units` char(20) DEFAULT NULL,
  `weight` decimal(10,2) DEFAULT NULL,
  `weight_units` char(20) DEFAULT NULL,
  `item_volume` decimal(12,4) DEFAULT NULL,
  `volume_units` char(20) DEFAULT NULL,
  `product_size_tier` char(50) DEFAULT NULL,
  `average_quantity_on_hand` decimal(10,2) DEFAULT NULL COMMENT '商品的日均体积，等于过去一个月的库存总体积除以该月包含的天数。',
  `average_quantity_pending_removal` decimal(12,4) DEFAULT NULL,
  `estimated_total_item_volume` decimal(20,6) DEFAULT NULL,
  `month` char(10) NOT NULL COMMENT '收费的月份，如2019-11 month_of_charge',
  `storage_utilization_ratio` char(20) DEFAULT NULL,
  `storage_utilization_ratio_units` char(20) DEFAULT NULL,
  `base_rate` decimal(10,2) DEFAULT NULL,
  `utilization_surcharge_rate` decimal(10,2) DEFAULT NULL,
  `currency` char(5) DEFAULT NULL,
  `monthly_storage_fee` decimal(20,6) DEFAULT NULL COMMENT 'estimated_monthly_storage_fee',
  `dangerous_goods_storage_type` char(10) DEFAULT NULL,
  `eligible_for_inventory_discount` char(10) DEFAULT NULL,
  `qualifies_for_inventory_discount` char(10) DEFAULT NULL,
  `total_incentive_fee_amount` decimal(20,6) DEFAULT NULL,
  `breakdown_incentive_fee_amount` varchar(50) DEFAULT NULL,
  `average_quantity_customer_orders` decimal(20,6) DEFAULT NULL,
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`month`,`amazonauthid`,`country`,`id`) USING BTREE,
  KEY `index1` (`amazonauthid`,`country`,`asin`,`month`,`fulfillment_center`,`fnsku`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fba_storage_fee_report_archive 结构
CREATE TABLE IF NOT EXISTS `t_fba_storage_fee_report_archive` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
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
  `amazonauthid` bigint(20) unsigned DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `month` (`month`),
  KEY `index1` (`amazonauthid`,`country`,`asin`,`month`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2147483647 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fba_storage_fee_report_bkp20230721 结构
CREATE TABLE IF NOT EXISTS `t_fba_storage_fee_report_bkp20230721` (
  `id` bigint(20) unsigned NOT NULL,
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
  `month` char(50) NOT NULL COMMENT '收费的月份，如2019-11',
  `storage_rate` decimal(10,2) DEFAULT NULL,
  `currency` char(20) DEFAULT NULL,
  `monthly_storage_fee` decimal(12,4) DEFAULT NULL,
  `category` char(50) DEFAULT NULL,
  `eligible_for_inv_discount` bit(1) DEFAULT b'0',
  `qualifies_for_inv_discount` bit(1) DEFAULT b'0',
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `lastupdate` datetime DEFAULT NULL,
  PRIMARY KEY (`month`,`amazonauthid`,`country`,`id`) USING BTREE,
  KEY `index1` (`amazonauthid`,`country`,`asin`,`month`,`fulfillment_center`,`fnsku`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_fixed_closingfee 结构
CREATE TABLE IF NOT EXISTS `t_fixed_closingfee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `format` char(100) DEFAULT NULL COMMENT '价格范围',
  `category` char(50) DEFAULT NULL,
  `fee` decimal(10,4) DEFAULT NULL,
  `country` char(10) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`)
) ENGINE=InnoDB AUTO_INCREMENT=5 ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_individualfee 结构
CREATE TABLE IF NOT EXISTS `t_individualfee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country` char(10) DEFAULT NULL,
  `perItemFee` decimal(10,2) DEFAULT NULL COMMENT '个人卖家才有per-item fee',
  PRIMARY KEY (`id`),
  KEY `country` (`country`)
) ENGINE=InnoDB AUTO_INCREMENT=9  ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inplacefee 结构
CREATE TABLE IF NOT EXISTS `t_inplacefee` (
  `id` char(15) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  `country` char(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `country` (`country`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inplacefeeformat 结构
CREATE TABLE IF NOT EXISTS `t_inplacefeeformat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inplacefeeid` char(36) NOT NULL,
  `producttierId` char(36)  DEFAULT NULL,
  `standard` bit(1) DEFAULT NULL,
  `format` varchar(500) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `country` char(5) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `inplacefeeid` (`inplacefeeid`),
  KEY `country` (`country`),
  KEY `producttierId` (`producttierId`),
  CONSTRAINT `t_inplacefeeformat_ibfk_1` FOREIGN KEY (`inplacefeeid`) REFERENCES `t_inplacefee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14  ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventorystoragefee 结构
CREATE TABLE IF NOT EXISTS `t_inventorystoragefee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `month` varchar(255) DEFAULT NULL,
  `price` decimal(10,3) DEFAULT NULL,
  `country` char(10) DEFAULT NULL,
  `isStandard` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30   COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventory_report 结构
CREATE TABLE IF NOT EXISTS `t_inventory_report` (
  `id` bigint(20) unsigned NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `fnsku` char(100) DEFAULT NULL,
  `asin` char(36) DEFAULT NULL,
  `pcondition` char(20) DEFAULT NULL,
  `your_price` decimal(10,2) DEFAULT NULL,
  `mfn_listing_exists` char(10) DEFAULT NULL,
  `mfn_fulfillable_quantity` int(11) DEFAULT NULL,
  `afn_listing_exists` char(10) DEFAULT NULL,
  `afn_warehouse_quantity` int(11) DEFAULT NULL COMMENT '亚马逊库存数量 =（ 亚马逊可用库存）+（亚马逊不可用库存）+（亚马逊预留库存）',
  `afn_fulfillable_quantity` int(11) DEFAULT NULL COMMENT '亚马逊可用库存',
  `afn_unsellable_quantity` int(11) DEFAULT NULL COMMENT '亚马逊不可用库存',
  `afn_reserved_quantity` int(11) DEFAULT NULL COMMENT '亚马逊预留库存',
  `afn_total_quantity` int(11) DEFAULT NULL COMMENT '亚马逊物流总数量 =（亚马逊库存数量）+（亚马逊物流入库处理数量）+（亚马逊物流入库发货数量）（亚马逊物流入库接收数量',
  `per_unit_volume` decimal(10,2) DEFAULT NULL,
  `afn_inbound_working_quantity` int(11) DEFAULT NULL COMMENT '亚马逊物流入库处理数量',
  `afn_inbound_shipped_quantity` int(11) DEFAULT NULL COMMENT '亚马逊物流入库发货数量',
  `afn_inbound_receiving_quantity` int(11) DEFAULT NULL COMMENT '亚马逊物流入库接受数量',
  `afn_researching_quantity` int(11) DEFAULT '0' COMMENT '亚马逊异常库存数量',
  `afn_future_supply_buyable` int(11) DEFAULT '0' COMMENT '亚马逊物流网络未来可购买供货',
  `afn_reserved_future_supply` int(11) DEFAULT '0' COMMENT '亚马逊物流网络预留未来供货',
  `isnewest` bit(1) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`marketplaceid`,`amazonAuthId`,`sku`),
  KEY `iv_sku_amazonauthid` (`sku`,`amazonAuthId`)
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventory_report_his 结构
CREATE TABLE IF NOT EXISTS `t_inventory_report_his` (
  `id` bigint(20) unsigned NOT NULL,
  `sku` char(50) NOT NULL,
  `byday` date NOT NULL,
  `marketplaceid` char(20) NOT NULL,
  `fnsku` char(50) DEFAULT NULL,
  `asin` char(36) NOT NULL,
  `pcondition` char(20) DEFAULT NULL,
  `your_price` decimal(10,2) DEFAULT NULL,
  `mfn_listing_exists` char(10) DEFAULT NULL,
  `mfn_fulfillable_quantity` int(11) DEFAULT NULL,
  `afn_listing_exists` char(10) DEFAULT NULL,
  `afn_warehouse_quantity` int(11) DEFAULT NULL,
  `afn_fulfillable_quantity` int(11) DEFAULT NULL,
  `afn_unsellable_quantity` int(11) DEFAULT NULL,
  `afn_reserved_quantity` int(11) DEFAULT NULL,
  `afn_total_quantity` int(11) DEFAULT NULL,
  `per_unit_volume` decimal(10,2) DEFAULT NULL,
  `afn_inbound_working_quantity` int(11) DEFAULT NULL,
  `afn_inbound_shipped_quantity` int(11) DEFAULT NULL,
  `afn_inbound_receiving_quantity` int(11) DEFAULT NULL,
  `afn_researching_quantity` int(11) DEFAULT '0',
  `afn_future_supply_buyable` int(11) DEFAULT '0',
  `afn_reserved_future_supply` int(11) DEFAULT '0',
  `isnewest` bit(1) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`byday`,`id`) USING BTREE,
  UNIQUE KEY `索引 3` (`amazonAuthId`,`marketplaceid`,`sku`,`byday`),
  KEY `sku` (`sku`) USING BTREE
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventory_report_his_archive 结构
CREATE TABLE IF NOT EXISTS `t_inventory_report_his_archive` (
  `id` bigint(20) unsigned NOT NULL,
  `sku` char(50) NOT NULL,
  `byday` date NOT NULL,
  `marketplaceid` char(20) NOT NULL,
  `fnsku` char(50) DEFAULT NULL,
  `asin` char(36) NOT NULL,
  `pcondition` char(20) DEFAULT NULL,
  `your_price` decimal(10,2) DEFAULT NULL,
  `mfn_listing_exists` char(10) DEFAULT NULL,
  `mfn_fulfillable_quantity` int(11) DEFAULT NULL,
  `afn_listing_exists` char(10) DEFAULT NULL,
  `afn_warehouse_quantity` int(11) DEFAULT NULL,
  `afn_fulfillable_quantity` int(11) DEFAULT NULL,
  `afn_unsellable_quantity` int(11) DEFAULT NULL,
  `afn_reserved_quantity` int(11) DEFAULT NULL,
  `afn_total_quantity` int(11) DEFAULT NULL,
  `per_unit_volume` decimal(10,2) DEFAULT NULL,
  `afn_inbound_working_quantity` int(11) DEFAULT NULL,
  `afn_inbound_shipped_quantity` int(11) DEFAULT NULL,
  `afn_inbound_receiving_quantity` int(11) DEFAULT NULL,
  `afn_researching_quantity` int(11) DEFAULT '0',
  `afn_future_supply_buyable` int(11) DEFAULT '0',
  `afn_reserved_future_supply` int(11) DEFAULT '0',
  `isnewest` bit(1) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`byday`,`amazonAuthId`,`marketplaceid`,`sku`),
  KEY `marketplaceid` (`marketplaceid`),
  KEY `sku` (`sku`),
  KEY `key` (`amazonAuthId`)
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventory_report_his_bkp20220815 结构
CREATE TABLE IF NOT EXISTS `t_inventory_report_his_bkp20220815` (
  `id` bigint(20) unsigned NOT NULL,
  `sku` char(50) NOT NULL,
  `byday` date NOT NULL,
  `marketplaceid` char(20) NOT NULL,
  `fnsku` char(50) DEFAULT NULL,
  `asin` char(36) NOT NULL,
  `pcondition` char(20) DEFAULT NULL,
  `your_price` decimal(10,2) DEFAULT NULL,
  `mfn_listing_exists` char(10) DEFAULT NULL,
  `mfn_fulfillable_quantity` int(11) DEFAULT NULL,
  `afn_listing_exists` char(10) DEFAULT NULL,
  `afn_warehouse_quantity` int(11) DEFAULT NULL,
  `afn_fulfillable_quantity` int(11) DEFAULT NULL,
  `afn_unsellable_quantity` int(11) DEFAULT NULL,
  `afn_reserved_quantity` int(11) DEFAULT NULL,
  `afn_total_quantity` int(11) DEFAULT NULL,
  `per_unit_volume` decimal(10,2) DEFAULT NULL,
  `afn_inbound_working_quantity` int(11) DEFAULT NULL,
  `afn_inbound_shipped_quantity` int(11) DEFAULT NULL,
  `afn_inbound_receiving_quantity` int(11) DEFAULT NULL,
  `afn_researching_quantity` int(11) DEFAULT '0',
  `afn_future_supply_buyable` int(11) DEFAULT '0',
  `afn_reserved_future_supply` int(11) DEFAULT '0',
  `isnewest` bit(1) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`amazonAuthId`,`marketplaceid`,`byday`,`sku`) USING BTREE,
  KEY `sku` (`sku`) USING BTREE
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventory_reserved_report 结构
CREATE TABLE IF NOT EXISTS `t_inventory_reserved_report` (
  `id` bigint(20) unsigned NOT NULL,
  `sku` char(50)  DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `fnsku` char(100) DEFAULT NULL,
  `asin` char(36) DEFAULT NULL,
  `reserved_qty` int(11) DEFAULT NULL,
  `reserved_customerorders` int(11) DEFAULT NULL,
  `reserved_fc_transfers` int(11) DEFAULT NULL,
  `reserved_fc_processing` int(11) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index1` (`sku`,`marketplaceid`,`amazonAuthId`),
  KEY `idx_amazonAuthId_marketplaceid` (`amazonAuthId`,`marketplaceid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventory_status 结构
CREATE TABLE IF NOT EXISTS `t_inventory_status` (
  `id` bigint(20) unsigned NOT NULL,
  `stockStatus` char(20)  DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `asin` char(36)  DEFAULT NULL,
  `sku` char(36)  DEFAULT NULL,
  `marketplaceid` char(36)  DEFAULT NULL,
  `amazonAuthId` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`byday`,`asin`,`marketplaceid`,`amazonAuthId`) USING BTREE
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_inventory_temp 结构
CREATE TABLE IF NOT EXISTS `t_inventory_temp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sku` char(50) DEFAULT NULL,
  `warehouse` char(100) DEFAULT NULL,
  `instock` int(11) DEFAULT NULL,
  `inbound` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1641 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_manager_limit 结构
CREATE TABLE IF NOT EXISTS `t_manager_limit` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID(h)',
  `shopId` bigint(20) unsigned DEFAULT NULL,
  `maxShopCount` int(8) DEFAULT '10' COMMENT '绑定店铺数量',
  `maxMarketCount` int(8) DEFAULT '10' COMMENT '绑定店铺站点数量',
  `maxProductCount` int(8) DEFAULT '50000' COMMENT '商品数量上限',
  `maxOrderCount` int(8) DEFAULT '3600' COMMENT '处理订单上限',
  `maxMember` int(8) DEFAULT '3' COMMENT '子用户数量上限',
  `maxProfitPlanCount` int(8) DEFAULT '1' COMMENT '利润计算方案数量',
  `maxdayOpenAdvCount` int(8) DEFAULT '10' COMMENT '每天开启广告组数量',
  `existShopCount` int(8) DEFAULT '0',
  `existMarketCount` int(8) DEFAULT '0',
  `existProductCount` int(8) DEFAULT '0',
  `existOrderCount` int(8) DEFAULT '0',
  `existMember` int(8) DEFAULT '0',
  `existProfitPlanCount` int(8) DEFAULT '0',
  `existdayOpenAdvCount` int(8) DEFAULT '0',
  `tariffpackage` int(8) DEFAULT '0' COMMENT '0-基础版，1-标准版，2-专业版，3-独享版,4-自定义',
  `losingEffect` date DEFAULT NULL COMMENT '失效时间',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `opratetime` datetime DEFAULT NULL COMMENT '修改时间',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `oprate` bigint(20) unsigned DEFAULT NULL COMMENT '修改人',
  `logicVersion` bigint(20) DEFAULT '0',
  `saleskey` char(36) DEFAULT NULL,
  `neverNoticeShop` bit(1) DEFAULT b'0',
  `afterNnoticeTariff` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_manager_limit_ibfk_1` (`shopId`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_manager_limit_append 结构
CREATE TABLE IF NOT EXISTS `t_manager_limit_append` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `tariffpackage` int(10) unsigned NOT NULL,
  `tariffpackage_append_id` int(10) unsigned NOT NULL,
  `ftype` char(50) DEFAULT NULL,
  `num` int(11) DEFAULT '0',
  `effecttime` date DEFAULT NULL,
  `losingeffect` date DEFAULT NULL,
  `isclose` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_manual_processing_fee 结构
CREATE TABLE IF NOT EXISTS `t_manual_processing_fee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `month` varchar(255) DEFAULT NULL,
  `manualProcessingFee` decimal(10,2) DEFAULT '0.00' COMMENT '手动处理费',
  `country` char(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_marketplace 结构
CREATE TABLE IF NOT EXISTS `t_marketplace` (
  `marketplaceId` varchar(15)  NOT NULL COMMENT '站点编码ID',
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
  `adv_end_point` varchar(30)  DEFAULT NULL,
  `aws_access_key` varchar(30)  DEFAULT NULL,
  `aws_secret_key` varchar(100)  DEFAULT NULL,
  `associate_tag` varchar(30)  DEFAULT NULL,
  `developer_url` varchar(1100)  DEFAULT NULL,
  `dev_account_num` char(20)  DEFAULT NULL,
  `bytecode` char(10)  DEFAULT NULL,
  `sp_api_endpoint` char(40)  DEFAULT NULL,
  `aws_region` char(10)  DEFAULT NULL,
  PRIMARY KEY (`marketplaceId`),
  KEY `Index 3` (`point_name`),
  KEY `region` (`region`),
  KEY `marketplaceId_region` (`marketplaceId`,`region`),
  KEY `currency` (`currency`),
  KEY `Index 2` (`market`) USING BTREE,
  KEY `aws_region` (`aws_region`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='站点';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_menu 结构
CREATE TABLE IF NOT EXISTS `t_menu` (
  `id` char(36)  NOT NULL COMMENT '主键(h)',
  `name` varchar(100)  NOT NULL COMMENT '名称',
  `description` varchar(500) DEFAULT NULL COMMENT '描述信息',
  `mindex` int(11) DEFAULT NULL COMMENT '次序',
  `mlevel` int(11) DEFAULT NULL COMMENT '层级',
  `parentid` char(36) DEFAULT NULL COMMENT '父菜单',
  `icon` varchar(2048) DEFAULT NULL COMMENT '图标',
  `childnumber` int(11) DEFAULT NULL COMMENT '子菜单个数',
  `action_url` varchar(100) DEFAULT NULL COMMENT '页面链接',
  `groupid` char(36)  DEFAULT NULL COMMENT '分组',
  PRIMARY KEY (`id`),
  KEY `FK_t_menu_t_menu` (`parentid`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='菜单';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_menu_group 结构
CREATE TABLE IF NOT EXISTS `t_menu_group` (
  `id` char(36) NOT NULL,
  `name` varchar(60) DEFAULT NULL,
  `icon` varchar(500) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_financial 结构
CREATE TABLE IF NOT EXISTS `t_orders_financial` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `amazon_order_id` varchar(30) NOT NULL COMMENT '订单ID',
  `order_item_id` varchar(30) DEFAULT NULL COMMENT '订单Item id',
  `sku` char(36) DEFAULT NULL COMMENT 'SKU',
  `currency` char(10) DEFAULT NULL COMMENT '货币',
  `ftype` char(240) NOT NULL COMMENT '费用类型',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '费用',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `marketplaceId` char(36) DEFAULT NULL COMMENT '站点',
  `posted_date` datetime DEFAULT NULL COMMENT '出账时间',
  PRIMARY KEY (`id`),
  KEY `amazon_order_id` (`amazon_order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=71538 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_fulfilled_shipments_fee 结构
CREATE TABLE IF NOT EXISTS `t_orders_fulfilled_shipments_fee` (
  `amazonauthid` bigint(20) unsigned DEFAULT NULL,
  `sku` char(50) DEFAULT NULL,
  `sales_channel` char(50) DEFAULT NULL,
  `amazon_order_id` char(50) NOT NULL,
  `merchant_order_id` char(50) DEFAULT NULL,
  `shipment_id` char(11) DEFAULT NULL,
  `shipment_item_id` char(11) NOT NULL,
  `amazon_order_item_id` char(15) NOT NULL,
  `merchant_order_item_id` char(30) DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `payments_date` datetime DEFAULT NULL,
  `shipment_date` datetime DEFAULT NULL,
  `reporting_date` datetime DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`shipment_item_id`,`amazon_order_item_id`) USING BTREE,
  KEY `shipment_date_sales_channel_amazonauthid` (`amazonauthid`,`sales_channel`,`shipment_date`) USING BTREE,
  KEY `amazonauthid_sku_sales_channel_payments_date` (`amazonauthid`,`sales_channel`,`sku`,`payments_date`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_fulfilled_shipments_report 结构
CREATE TABLE IF NOT EXISTS `t_orders_fulfilled_shipments_report` (
  `amazon_order_id` char(50) NOT NULL,
  `merchant_order_id` char(50) DEFAULT NULL,
  `shipment_id` char(11) DEFAULT NULL,
  `shipment_item_id` char(11) NOT NULL,
  `amazon_order_item_id` char(15) NOT NULL,
  `merchant_order_item_id` char(30) DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `payments_date` datetime DEFAULT NULL,
  `shipment_date` datetime DEFAULT NULL,
  `reporting_date` datetime DEFAULT NULL,
  `buyer_email` char(50) DEFAULT NULL,
  `buyer_name` char(50) DEFAULT NULL,
  `buyer_phone_number` char(50) DEFAULT NULL,
  `sku` char(50) DEFAULT NULL,
  `product_name` varchar(500) DEFAULT NULL,
  `quantity_shipped` int(11) DEFAULT NULL,
  `currency` char(5) DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `ship_service_level` char(50) DEFAULT NULL,
  `recipient_name` char(50) DEFAULT NULL,
  `ship_address_1` varchar(200) DEFAULT NULL,
  `ship_address_2` varchar(200) DEFAULT NULL,
  `ship_address_3` varchar(200) DEFAULT NULL,
  `ship_city` char(100) DEFAULT NULL,
  `ship_state` char(50) DEFAULT NULL,
  `ship_postal_code` char(20) DEFAULT NULL,
  `ship_country` char(5) DEFAULT NULL,
  `ship_phone_number` char(50) DEFAULT NULL,
  `bill_address_1` varchar(200) DEFAULT NULL,
  `bill_address_2` varchar(200) DEFAULT NULL,
  `bill_address_3` varchar(200) DEFAULT NULL,
  `bill_city` char(100) DEFAULT NULL,
  `bill_state` char(50) DEFAULT NULL,
  `bill_postal_code` char(50) DEFAULT NULL,
  `bill_country` char(5) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `carrier` char(50) DEFAULT NULL,
  `tracking_number` char(50) DEFAULT NULL,
  `estimated_arrival_date` datetime DEFAULT NULL,
  `fulfillment_center_id` char(50) DEFAULT NULL,
  `fulfillment_channel` char(50) DEFAULT NULL,
  `sales_channel` char(50) DEFAULT NULL,
  `amazonauthid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`shipment_item_id`,`amazon_order_item_id`) USING BTREE,
  KEY `purchase_date` (`purchase_date`) USING BTREE,
  KEY `sku` (`sku`,`purchase_date`) USING BTREE,
  KEY `sales_channel_amazonauthid` (`amazonauthid`,`sales_channel`,`purchase_date`) USING BTREE,
  KEY `shipment_date_sales_channel_amazonauthid` (`amazonauthid`,`sales_channel`,`shipment_date`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_fulfilled_shipments_report_archive 结构
CREATE TABLE IF NOT EXISTS `t_orders_fulfilled_shipments_report_archive` (
  `amazon_order_id` char(50) NOT NULL,
  `merchant_order_id` char(50) DEFAULT NULL,
  `shipment_id` char(11) DEFAULT NULL,
  `shipment_item_id` char(11) NOT NULL,
  `amazon_order_item_id` char(15) NOT NULL,
  `merchant_order_item_id` char(30) DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `payments_date` datetime DEFAULT NULL,
  `shipment_date` datetime DEFAULT NULL,
  `reporting_date` datetime DEFAULT NULL,
  `buyer_email` char(50) DEFAULT NULL,
  `buyer_name` char(50) DEFAULT NULL,
  `buyer_phone_number` char(50) DEFAULT NULL,
  `sku` char(50) DEFAULT NULL,
  `product_name` varchar(500) DEFAULT NULL,
  `quantity_shipped` int(11) DEFAULT NULL,
  `currency` char(5) DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `ship_service_level` char(50) DEFAULT NULL,
  `recipient_name` char(50) DEFAULT NULL,
  `ship_address_1` varchar(200) DEFAULT NULL,
  `ship_address_2` varchar(200) DEFAULT NULL,
  `ship_address_3` varchar(200) DEFAULT NULL,
  `ship_city` char(100) DEFAULT NULL,
  `ship_state` char(50) DEFAULT NULL,
  `ship_postal_code` char(20) DEFAULT NULL,
  `ship_country` char(5) DEFAULT NULL,
  `ship_phone_number` char(50) DEFAULT NULL,
  `bill_address_1` varchar(200) DEFAULT NULL,
  `bill_address_2` varchar(200) DEFAULT NULL,
  `bill_address_3` varchar(200) DEFAULT NULL,
  `bill_city` char(100) DEFAULT NULL,
  `bill_state` char(50) DEFAULT NULL,
  `bill_postal_code` char(50) DEFAULT NULL,
  `bill_country` char(5) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `carrier` char(50) DEFAULT NULL,
  `tracking_number` char(50) DEFAULT NULL,
  `estimated_arrival_date` datetime DEFAULT NULL,
  `fulfillment_center_id` char(50) DEFAULT NULL,
  `fulfillment_channel` char(50) DEFAULT NULL,
  `sales_channel` char(50) DEFAULT NULL,
  `amazonauthid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`shipment_item_id`,`amazon_order_item_id`) USING BTREE,
  KEY `purchase_date` (`purchase_date`) USING BTREE,
  KEY `sku` (`sku`,`purchase_date`) USING BTREE,
  KEY `sales_channel_amazonauthid` (`amazonauthid`,`sales_channel`,`purchase_date`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_remark 结构
CREATE TABLE IF NOT EXISTS `t_orders_remark` (
  `amazon_order_id` varchar(50) NOT NULL,
  `feed_queueid` bigint(20) unsigned DEFAULT NULL,
  `review_send_time` datetime DEFAULT NULL,
  `review_send_operator` bigint(20) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`),
  KEY `amazon_order_id_remark` (`remark`)
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_report 结构
CREATE TABLE IF NOT EXISTS `t_orders_report` (
  `id` bigint(20) unsigned NOT NULL,
  `amazon_order_id` varchar(50) NOT NULL,
  `merchant_order_id` varchar(50) DEFAULT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_status` char(30) DEFAULT NULL,
  `fulfillment_channel` char(15) DEFAULT NULL,
  `sales_channel` char(30) DEFAULT NULL,
  `order_channel` char(30) DEFAULT NULL,
  `url` varchar(50) DEFAULT NULL,
  `ship_service_level` varchar(15) DEFAULT NULL,
  `sku` char(50) DEFAULT NULL,
  `asin` char(36) DEFAULT NULL,
  `item_status` char(30) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_city` varchar(100) DEFAULT NULL,
  `ship_state` char(60) DEFAULT NULL,
  `ship_postal_code` char(20) DEFAULT NULL,
  `ship_country` char(10) DEFAULT NULL,
  `promotion_ids` varchar(500) DEFAULT NULL,
  `is_business_order` char(10) DEFAULT NULL,
  `purchase_order_number` char(100) DEFAULT NULL,
  `price_designation` char(20) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `marketplaceId` char(36) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`purchase_date`,`id`) USING BTREE,
  UNIQUE KEY `Index` (`amazon_order_id`,`sku`) USING BTREE,
  KEY `Index 2` (`sales_channel`,`sku`,`purchase_date`) USING BTREE
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_report_archive 结构
CREATE TABLE IF NOT EXISTS `t_orders_report_archive` (
  `id` bigint(20) unsigned NOT NULL,
  `amazon_order_id` varchar(50) NOT NULL,
  `merchant_order_id` varchar(50) DEFAULT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_status` char(30) DEFAULT NULL,
  `fulfillment_channel` char(15) DEFAULT NULL,
  `sales_channel` char(30) DEFAULT NULL,
  `order_channel` char(30) DEFAULT NULL,
  `url` varchar(50) DEFAULT NULL,
  `ship_service_level` varchar(15) DEFAULT NULL,
  `sku` char(50) DEFAULT NULL,
  `asin` char(36) DEFAULT NULL,
  `item_status` char(30) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_city` varchar(100) DEFAULT NULL,
  `ship_state` char(60) DEFAULT NULL,
  `ship_postal_code` char(20) DEFAULT NULL,
  `ship_country` char(10) DEFAULT NULL,
  `promotion_ids` varchar(500) DEFAULT NULL,
  `is_business_order` char(10) DEFAULT NULL,
  `purchase_order_number` char(100) DEFAULT NULL,
  `price_designation` char(20) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `marketplaceId` char(36) DEFAULT NULL,
  PRIMARY KEY (`amazonAuthId`,`purchase_date`,`id`),
  KEY `Index 2` (`sales_channel`,`sku`),
  KEY `Index` (`amazon_order_id`,`sku`) USING BTREE
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_report_download 结构
CREATE TABLE IF NOT EXISTS `t_orders_report_download` (
  `id` bigint(20) unsigned NOT NULL,
  `amazon_order_id` varchar(50) NOT NULL,
  `merchant_order_id` varchar(50) DEFAULT NULL,
  `purchase_date` datetime NOT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `order_status` char(30) DEFAULT NULL,
  `fulfillment_channel` char(15) DEFAULT NULL,
  `sales_channel` char(30) DEFAULT NULL,
  `order_channel` char(30) DEFAULT NULL,
  `url` varchar(50) DEFAULT NULL,
  `ship_service_level` varchar(15) DEFAULT NULL,
  `sku` char(50) NOT NULL,
  `asin` char(36) DEFAULT NULL,
  `item_status` char(30) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `item_price` decimal(10,2) DEFAULT NULL,
  `item_tax` decimal(10,2) DEFAULT NULL,
  `shipping_price` decimal(10,2) DEFAULT NULL,
  `shipping_tax` decimal(10,2) DEFAULT NULL,
  `gift_wrap_price` decimal(10,2) DEFAULT NULL,
  `gift_wrap_tax` decimal(10,2) DEFAULT NULL,
  `item_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_promotion_discount` decimal(10,2) DEFAULT NULL,
  `ship_city` varchar(50) DEFAULT NULL,
  `ship_state` char(60) DEFAULT NULL,
  `ship_postal_code` char(20) DEFAULT NULL,
  `ship_country` char(10) DEFAULT NULL,
  `promotion_ids` varchar(500) DEFAULT NULL,
  `is_business_order` char(10) DEFAULT NULL,
  `purchase_order_number` char(100) DEFAULT NULL,
  `price_designation` char(20) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned DEFAULT NULL,
  `marketplaceId` char(36) DEFAULT NULL,
  `refreshtime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`purchase_date`,`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`amazonAuthId`,`amazon_order_id`,`sku`) USING BTREE
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='purchase_order_number';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_reviews_customer 结构
CREATE TABLE IF NOT EXISTS `t_orders_reviews_customer` (
  `amazon_order_id` char(50) NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(50) DEFAULT NULL,
  `picture` varchar(500) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_sumconfig 结构
CREATE TABLE IF NOT EXISTS `t_orders_sumconfig` (
  `id` char(36)  NOT NULL,
  `order_status` char(40)  DEFAULT NULL,
  `shop_id` bigint(20) unsigned DEFAULT NULL,
  `sales_channel` char(30)  DEFAULT NULL,
  `is_business_order` char(10)  DEFAULT NULL,
  `discountfrom` decimal(10,0) DEFAULT NULL,
  `discountTo` decimal(10,0) DEFAULT NULL,
  `amazonAuthId` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_summary 结构
CREATE TABLE IF NOT EXISTS `t_orders_summary` (
  `id` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(25)  NOT NULL,
  `purchase_date` date NOT NULL,
  `asin` char(36)  NOT NULL,
  `sku` char(50)  NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `ordersum` int(11) DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `quantity2b` int(11) DEFAULT NULL,
  `ordersum2b` int(11) DEFAULT NULL,
  `orderprice2b` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`),
  UNIQUE KEY `index_1` (`amazonAuthId`,`marketplaceid`,`sku`,`purchase_date`) USING BTREE,
  KEY `Index 2` (`marketplaceid`,`quantity`,`orderprice`,`ordersum`),
  KEY `amazonauthid` (`sku`,`amazonAuthId`) USING BTREE
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_summary_month 结构
CREATE TABLE IF NOT EXISTS `t_orders_summary_month` (
  `id` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(25)  NOT NULL,
  `purchase_date` date NOT NULL,
  `asin` char(36)  NOT NULL,
  `sku` char(50)  NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `ordersum` int(11) DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `quantity2b` int(11) DEFAULT NULL,
  `ordersum2b` int(11) DEFAULT NULL,
  `orderprice2b` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`),
  UNIQUE KEY `index_1` (`amazonAuthId`,`marketplaceid`,`sku`,`purchase_date`),
  KEY `Index 2` (`marketplaceid`,`quantity`,`orderprice`,`ordersum`)
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_orders_summary_week 结构
CREATE TABLE IF NOT EXISTS `t_orders_summary_week` (
  `id` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(25)  NOT NULL,
  `purchase_date` date NOT NULL,
  `asin` char(36)  NOT NULL,
  `sku` char(50)  NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `ordersum` int(11) DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `quantity2b` int(11) DEFAULT NULL,
  `ordersum2b` int(11) DEFAULT NULL,
  `orderprice2b` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`),
  UNIQUE KEY `index_1` (`amazonAuthId`,`marketplaceid`,`sku`,`purchase_date`),
  KEY `Index 2` (`marketplaceid`,`quantity`,`orderprice`,`ordersum`)
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_order_invoice 结构
CREATE TABLE IF NOT EXISTS `t_order_invoice` (
  `id` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned NOT NULL,
  `logoUrl` varchar(255) DEFAULT NULL COMMENT '图片地址id',
  `image` bigint(20) unsigned DEFAULT NULL,
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_order_invoice_vat 结构
CREATE TABLE IF NOT EXISTS `t_order_invoice_vat` (
  `id` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `vat_num` varchar(50) DEFAULT NULL,
  `vat_rate` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `groupid` (`groupid`,`country`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_outbound_weightformat 结构
CREATE TABLE IF NOT EXISTS `t_outbound_weightformat` (
  `id` char(36) NOT NULL,
  `producttierId` char(36) DEFAULT NULL,
  `isMedia` bit(1) DEFAULT NULL,
  `format` char(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `producttierId` (`producttierId`)
) ENGINE=InnoDB  COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_parameterconfig 结构
CREATE TABLE IF NOT EXISTS `t_parameterconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ptype` char(36) DEFAULT NULL,
  `pkey` char(10) DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  `sortindex` int(11) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pkey` (`pkey`)
) ENGINE=InnoDB AUTO_INCREMENT=30     ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_picture 结构
CREATE TABLE IF NOT EXISTS `t_picture` (
  `id` bigint(20) unsigned NOT NULL COMMENT '图片ID',
  `url` varchar(500) DEFAULT NULL COMMENT '图片网络位置',
  `location` varchar(500) DEFAULT NULL COMMENT '图片本地位置',
  `height` decimal(10,2) DEFAULT NULL COMMENT '图片高度',
  `height_units` char(10) DEFAULT NULL COMMENT '高度单位',
  `width` decimal(10,2) DEFAULT NULL COMMENT '图片宽度',
  `width_units` char(10) DEFAULT NULL COMMENT '宽度单位',
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_location` (`location`(255))
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='用于存放Image';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_prepservicefee 结构
CREATE TABLE IF NOT EXISTS `t_prepservicefee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `isStandard` bit(1) DEFAULT NULL,
  `prepServiceFee` decimal(10,2) DEFAULT '0.00',
  `country` char(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`)
) ENGINE=InnoDB AUTO_INCREMENT=38 ROW_FORMAT=DYNAMIC COMMENT='利润计算';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_productformat 结构
CREATE TABLE IF NOT EXISTS `t_productformat` (
  `id` char(36) NOT NULL,
  `producttierId` char(36) DEFAULT NULL,
  `country` char(50) DEFAULT NULL,
  `format` varchar(500) DEFAULT NULL,
  `length_unit` char(10) DEFAULT NULL,
  `weight_unit` char(10) DEFAULT NULL,
  `sort` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`producttierId`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_producttier 结构
CREATE TABLE IF NOT EXISTS `t_producttier` (
  `id` char(36) NOT NULL,
  `name` char(50) DEFAULT NULL,
  `isStandard` bit(1) DEFAULT NULL,
  `country` char(50) DEFAULT NULL,
  `box_weight` decimal(10,4) DEFAULT NULL COMMENT '包装箱重量（单位：kg）',
  `amz_name` char(50)  DEFAULT NULL COMMENT '对应亚马逊显示的product tier',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_category 结构
CREATE TABLE IF NOT EXISTS `t_product_category` (
  `CategoryId` char(50)  NOT NULL,
  `pid` bigint(20) unsigned NOT NULL,
  `Name` varchar(200) DEFAULT NULL,
  `parentId` char(36) DEFAULT NULL,
  PRIMARY KEY (`pid`,`CategoryId`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='产品分类表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_follow 结构
CREATE TABLE IF NOT EXISTS `t_product_follow` (
  `id` bigint(20) unsigned NOT NULL,
  `asin` char(18) DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `lastupdateTime` datetime DEFAULT NULL,
  `isread` bit(1) DEFAULT b'0',
  `sku` char(36) DEFAULT NULL,
  `flownumber` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `asin_amazonAuthId_marketplaceid_endtime_isnewest` (`amazonAuthId`,`marketplaceid`,`asin`) USING BTREE,
  KEY `flownumber` (`flownumber`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_info 结构
CREATE TABLE IF NOT EXISTS `t_product_info` (
  `id` bigint(20) unsigned NOT NULL COMMENT '产品ID',
  `asin` char(36) DEFAULT NULL COMMENT '唯一码asin',
  `sku` varchar(50) DEFAULT NULL COMMENT '用户码sku',
  `marketplaceid` char(36) DEFAULT NULL COMMENT '站点',
  `name` varchar(1000) DEFAULT NULL COMMENT '产品名称（产品标题）',
  `openDate` datetime DEFAULT NULL COMMENT '创建日期',
  `itemDimensions` bigint(20) unsigned DEFAULT NULL COMMENT '产品尺寸',
  `pageDimensions` bigint(20) unsigned DEFAULT NULL COMMENT '含包装尺寸',
  `fulfillChannel` varchar(120) DEFAULT NULL COMMENT '交付渠道',
  `binding` varchar(50) DEFAULT NULL COMMENT '装订',
  `totalOfferCount` int(10) DEFAULT NULL COMMENT '卖家数量',
  `brand` varchar(100) DEFAULT NULL COMMENT '品牌',
  `manufacturer` varchar(250) DEFAULT NULL COMMENT '厂商',
  `pgroup` varchar(50) DEFAULT NULL COMMENT '分组',
  `typename` varchar(50) DEFAULT NULL COMMENT '分类',
  `price` decimal(14,2) DEFAULT NULL COMMENT '价格',
  `image` bigint(20) unsigned DEFAULT NULL COMMENT '照片',
  `parentMarketplace` char(36) DEFAULT NULL COMMENT '父商品marketplace',
  `parentAsin` char(36) DEFAULT NULL COMMENT '父商品asin',
  `isparent` bit(1) DEFAULT b'0' COMMENT '是否副产品（即不是变体）',
  `lastupdate` datetime DEFAULT NULL COMMENT '更新时间',
  `createdate` datetime DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned DEFAULT NULL COMMENT '授权ID',
  `invalid` bit(1) DEFAULT b'0' COMMENT '是否无效',
  `oldid` char(36) DEFAULT NULL,
  `inSnl` bit(1) DEFAULT b'0' COMMENT '是否轻小',
  `fnsku` char(20) DEFAULT NULL,
  `pcondition` char(20) DEFAULT NULL,
  `status` char(20) DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Index 3` (`amazonAuthId`,`marketplaceid`,`sku`) USING BTREE,
  KEY `Index 6` (`marketplaceid`,`amazonAuthId`,`isparent`,`invalid`,`disable`) USING BTREE,
  KEY `invalid` (`invalid`,`disable`,`isparent`) USING BTREE,
  KEY `idx_sku_isparent_invalid` (`sku`) USING BTREE,
  KEY `idx_asin_amazonAuthId` (`asin`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_info_bkp20220712 结构
CREATE TABLE IF NOT EXISTS `t_product_info_bkp20220712` (
  `id` bigint(20) unsigned NOT NULL COMMENT '产品ID',
  `asin` char(36) DEFAULT NULL COMMENT '唯一码asin',
  `sku` varchar(50) DEFAULT NULL COMMENT '用户码sku',
  `marketplaceid` char(36) DEFAULT NULL COMMENT '站点',
  `name` varchar(1000) DEFAULT NULL COMMENT '产品名称（产品标题）',
  `openDate` datetime DEFAULT NULL COMMENT '创建日期',
  `itemDimensions` bigint(20) unsigned DEFAULT NULL,
  `pageDimensions` bigint(20) unsigned DEFAULT NULL,
  `fulfillChannel` varchar(120) DEFAULT NULL COMMENT '交付渠道',
  `binding` varchar(50) DEFAULT NULL COMMENT '装订',
  `totalOfferCount` int(11) DEFAULT NULL,
  `brand` varchar(100) DEFAULT NULL COMMENT '品牌',
  `manufacturer` varchar(250) DEFAULT NULL COMMENT '厂商',
  `pgroup` varchar(50) DEFAULT NULL,
  `typename` varchar(50) DEFAULT NULL,
  `price` decimal(14,2) DEFAULT NULL,
  `image` bigint(20) unsigned DEFAULT NULL COMMENT '照片',
  `parentMarketplace` char(36) DEFAULT NULL,
  `parentAsin` char(36) DEFAULT NULL,
  `isparent` bit(1) DEFAULT b'0',
  `lastupdate` datetime DEFAULT NULL,
  `amazonAuthId` bigint(20) unsigned DEFAULT NULL,
  `invalid` bit(1) DEFAULT b'0',
  `oldid` char(36) DEFAULT NULL,
  `inSnl` bit(1) DEFAULT b'0' COMMENT '是否轻小',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 3` (`amazonAuthId`,`marketplaceid`,`sku`) USING BTREE,
  KEY `sku_name` (`sku`,`id`),
  KEY `Index 6` (`marketplaceid`,`asin`,`amazonAuthId`,`isparent`),
  KEY `invalid` (`invalid`),
  KEY `idx_sku_isparent_invalid` (`sku`,`isparent`,`invalid`),
  KEY `idx_asin_amazonAuthId_pageDimensions` (`asin`,`amazonAuthId`,`pageDimensions`),
  KEY `Index 5` (`amazonAuthId`,`marketplaceid`,`invalid`,`isparent`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_info_follow 结构
CREATE TABLE IF NOT EXISTS `t_product_info_follow` (
  `pid` bigint(20) unsigned NOT NULL,
  `status_upload` char(20) DEFAULT NULL,
  `orders_sum` int(11) DEFAULT NULL,
  `last_order_time` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `quantity` int(10) unsigned DEFAULT NULL,
  `oldprice` decimal(20,6) unsigned DEFAULT NULL,
  `price` decimal(20,6) unsigned DEFAULT NULL,
  `assumeprice` decimal(20,6) unsigned DEFAULT NULL,
  `lowprice` decimal(20,6) unsigned DEFAULT NULL,
  `shopprice` decimal(20,6) unsigned DEFAULT NULL,
  `pricetime` datetime DEFAULT NULL,
  `delivery_cycle` int(11) DEFAULT NULL,
  `rank` int(11) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_info_status_define 结构
CREATE TABLE IF NOT EXISTS `t_product_info_status_define` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `name` char(20) DEFAULT NULL COMMENT '状态名称',
  `issystem` bit(1) NOT NULL DEFAULT b'0',
  `color` char(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `remark` char(100) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_name` (`shopid`,`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=54 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_adv 结构
CREATE TABLE IF NOT EXISTS `t_product_in_adv` (
  `pid` bigint(20) unsigned NOT NULL COMMENT '产品ID',
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_aftersale 结构
CREATE TABLE IF NOT EXISTS `t_product_in_aftersale` (
  `id` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `amazonauthid` bigint(19) unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `sku` char(50) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `quantity` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_bydate` (`sku`,`marketplaceid`,`groupid`,`date`) USING BTREE,
  KEY `date_amazonauthid` (`amazonauthid`,`date`) USING BTREE,
  KEY `sku_marketplaceid_amazonauthid` (`amazonauthid`,`marketplaceid`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_autoprice 结构
CREATE TABLE IF NOT EXISTS `t_product_in_autoprice` (
  `pid` bigint(20) unsigned NOT NULL,
  `ftype` tinyint(3) unsigned DEFAULT NULL COMMENT '1代表最低价，2代表购物车',
  `lowestprice` decimal(10,2) DEFAULT NULL COMMENT '0元 代表停用',
  `disable` bit(1) DEFAULT NULL COMMENT 'false代表启动，true代表停用',
  `buybox_lowest_first` bit(1) DEFAULT NULL,
  `buybox_lowest_next` bit(1) DEFAULT NULL,
  `down_step_price` decimal(10,2) DEFAULT NULL,
  `down_step_rate` decimal(10,2) DEFAULT NULL,
  `up_step_price` decimal(10,2) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_opt 结构
CREATE TABLE IF NOT EXISTS `t_product_in_opt` (
  `pid` bigint(20) unsigned NOT NULL COMMENT '产品ID',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `priceremark` varchar(1000) DEFAULT NULL COMMENT '价格公告',
  `buyprice` decimal(10,2) DEFAULT NULL COMMENT '采购单价',
  `businessprice` decimal(10,2) DEFAULT NULL COMMENT '销售价格',
  `businesstype` char(10) DEFAULT NULL COMMENT '价格',
  `businesslist` varchar(1000) DEFAULT NULL COMMENT '隐藏',
  `disable` bit(1) DEFAULT b'0' COMMENT '手动输入的预估销量',
  `presales` int(11) DEFAULT NULL COMMENT '手动输入的预估销量',
  `lastupdate` datetime DEFAULT NULL COMMENT '更新时间',
  `remark_analysis` varchar(1000) DEFAULT NULL COMMENT '备注',
  `msku` varchar(50) DEFAULT NULL COMMENT '本地SKU',
  `fnsku` varchar(100) DEFAULT NULL COMMENT 'FNSKU',
  `review_daily_refresh` int(11) DEFAULT NULL COMMENT '评论刷新时间',
  `fulfillment_availability` int(11) DEFAULT NULL COMMENT '自发货可用库存',
  `merchant_shipping_group` varchar(50) DEFAULT NULL COMMENT '自发货运费模板',
  `profitid` bigint(20) unsigned DEFAULT NULL COMMENT '对应利润计算方案',
  `status` int(10) unsigned DEFAULT NULL COMMENT '产品状态 0备货 1维持 2提升 3促销  4停售 5清仓 6删除',
  `owner` bigint(20) unsigned DEFAULT NULL COMMENT '运营负责人',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `lowestprice` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`pid`),
  KEY `Index 5` (`disable`),
  KEY `msku` (`msku`),
  KEY `idx_msku_disable_status` (`msku`,`disable`,`status`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_order 结构
CREATE TABLE IF NOT EXISTS `t_product_in_order` (
  `pid` bigint(20) unsigned NOT NULL COMMENT '产品ID',
  `avgsales` int(11) DEFAULT NULL,
  `oldavgsales` int(11) DEFAULT NULL,
  `daynum` int(11) DEFAULT NULL,
  `sales_week` int(11) DEFAULT NULL COMMENT 'sales_week,往前推2天之后的7日销量',
  `price_week` decimal(10,2) DEFAULT NULL COMMENT '销售额',
  `sales_month` int(11) DEFAULT NULL COMMENT '30日销量',
  `order_week` int(11) DEFAULT NULL,
  `order_month` int(11) DEFAULT NULL,
  `changeRate` decimal(10,2) DEFAULT NULL COMMENT '销量上升或者下降比率',
  `lastupdate` datetime DEFAULT NULL,
  `sales_seven` int(11) DEFAULT NULL COMMENT 'sales_seven,7日销量',
  `sales_fifteen` int(11) DEFAULT NULL COMMENT 'sales_fifteen,15日销量',
  `rank` int(11) DEFAULT NULL,
  PRIMARY KEY (`pid`),
  KEY `lastupdate` (`lastupdate`),
  KEY `sales_month` (`sales_month`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='产品信息的订单销售数据';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_presale 结构
CREATE TABLE IF NOT EXISTS `t_product_in_presale` (
  `id` bigint(20) unsigned NOT NULL,
  `sku` char(50) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `groupid` bigint(36) unsigned NOT NULL,
  `date` date NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `amazonauthid` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `start` date DEFAULT NULL,
  `end` date DEFAULT NULL,
  `month` char(10) DEFAULT NULL,
  `hasdaysales` bit(1) DEFAULT b'0',
  PRIMARY KEY (`sku`,`marketplaceid`,`groupid`,`date`) USING BTREE,
  UNIQUE KEY `idx` (`id`) USING BTREE,
  KEY `index_date_event` (`start`,`end`) USING BTREE,
  KEY `sku_marketplaceid_date_amazonauthid` (`amazonauthid`,`marketplaceid`,`sku`,`date`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_presale_archive 结构
CREATE TABLE IF NOT EXISTS `t_product_in_presale_archive` (
  `id` bigint(20) unsigned NOT NULL,
  `sku` char(50) DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `groupid` bigint(36) unsigned DEFAULT NULL,
  `date` date DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `amazonauthid` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `start` date DEFAULT NULL,
  `end` date DEFAULT NULL,
  `month` char(10) DEFAULT NULL,
  `hasdaysales` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_bydate` (`sku`,`marketplaceid`,`groupid`,`date`) USING BTREE,
  KEY `index_date_event` (`start`,`end`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_profit 结构
CREATE TABLE IF NOT EXISTS `t_product_in_profit` (
  `pid` bigint(20) unsigned NOT NULL COMMENT '产品ID',
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='产品信息的利润信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_review 结构
CREATE TABLE IF NOT EXISTS `t_product_in_review` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `asin` char(15) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `starofrate` float unsigned DEFAULT NULL,
  `reviewnum` int(10) unsigned DEFAULT NULL,
  `starofrate_1` float unsigned DEFAULT NULL,
  `starofrate_2` float unsigned DEFAULT NULL,
  `starofrate_3` float unsigned DEFAULT NULL,
  `starofrate_4` float unsigned DEFAULT NULL,
  `starofrate_5` float unsigned DEFAULT NULL,
  `negative` bit(1) DEFAULT b'0',
  `positiveReview` char(36)  DEFAULT NULL,
  `criticalReview` char(36)  DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `refreshnum` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `asin_marketplaceid` (`asin`,`marketplaceid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_review_detail 结构
CREATE TABLE IF NOT EXISTS `t_product_in_review_detail` (
  `id` bigint(20) unsigned NOT NULL,
  `reviewid` char(20) DEFAULT NULL,
  `asin` char(15) DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `profile_avatar_img` varchar(200) DEFAULT NULL,
  `profile_name` varchar(500) DEFAULT NULL,
  `review_star_rating` float DEFAULT NULL,
  `review_date` char(200) DEFAULT NULL,
  `review_cndate` datetime DEFAULT NULL,
  `review_title` varchar(2000) DEFAULT NULL,
  `review_body` varchar(4000) DEFAULT NULL,
  `review_image_tile` varchar(2000) DEFAULT NULL,
  `video_img_url` varchar(500) DEFAULT NULL,
  `video_url` varchar(500) DEFAULT NULL,
  `buyer_size_color_num` varchar(500) DEFAULT NULL,
  `verified_text` varchar(50) DEFAULT NULL,
  `helpful_num` varchar(50) DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `refreshorder` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `reviewid_asin_marketplaceid` (`asin`,`marketplaceid`,`reviewid`) USING BTREE,
  KEY `review_star_rating` (`marketplaceid`,`review_star_rating`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_review_order 结构
CREATE TABLE IF NOT EXISTS `t_product_in_review_order` (
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `purchase_date` datetime NOT NULL,
  `orderid` char(30)  NOT NULL,
  `pid` bigint(20) unsigned DEFAULT NULL,
  `reviewid` char(36)  DEFAULT NULL,
  `asin` char(15)  DEFAULT NULL,
  `marketplaceid` char(15)  DEFAULT NULL,
  `email` char(100)  DEFAULT NULL,
  `sku` char(50)  DEFAULT NULL,
  `sales_channel` char(50)  DEFAULT NULL,
  `review_star_rating` float DEFAULT NULL,
  `review_title` varchar(2000)  DEFAULT NULL,
  `review_date` date DEFAULT NULL,
  PRIMARY KEY (`amazonauthid`,`purchase_date`,`orderid`) USING BTREE,
  KEY `marketplaceid` (`marketplaceid`,`sku`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_review_runs 结构
CREATE TABLE IF NOT EXISTS `t_product_in_review_runs` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `amazonauthid` bigint(20) unsigned DEFAULT NULL,
  `pid` bigint(20) unsigned DEFAULT NULL,
  `sku` char(100) DEFAULT NULL,
  `asin` char(15) DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shopid_asin` (`shopid`) USING BTREE,
  KEY `asin_marketplaceid` (`amazonauthid`,`marketplaceid`,`sku`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_ses 结构
CREATE TABLE IF NOT EXISTS `t_product_in_ses` (
  `pid` bigint(20) unsigned NOT NULL COMMENT '产品ID',
  `session_day7` int(11) DEFAULT NULL,
  `session_rate7` decimal(14,4) DEFAULT NULL,
  `buybox_rate7` decimal(14,4) DEFAULT NULL,
  `units_ordered7` int(11) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_sys 结构
CREATE TABLE IF NOT EXISTS `t_product_in_sys` (
  `pid` bigint(20) unsigned NOT NULL COMMENT '产品ID',
  `saleDate` datetime DEFAULT NULL,
  `orderDate` datetime DEFAULT NULL,
  `avgsales` int(11) DEFAULT NULL,
  `oldavgsales` int(11) DEFAULT NULL,
  `daynum` int(11) DEFAULT NULL,
  `maxsales_day_month` int(11) DEFAULT NULL,
  `sales_week` int(11) DEFAULT NULL COMMENT 'sales_week,往前推2天之后的7日销量',
  `price_week` decimal(10,2) DEFAULT NULL COMMENT '销售额',
  `profit_week` decimal(10,2) DEFAULT NULL,
  `margin_week` decimal(10,2) DEFAULT NULL,
  `sales_month` int(11) DEFAULT NULL COMMENT '30日销量',
  `order_week` int(11) DEFAULT NULL,
  `order_month` int(11) DEFAULT NULL,
  `changeRate` decimal(10,2) DEFAULT NULL,
  `lastupdate` datetime DEFAULT NULL,
  `sales_seven` int(11) DEFAULT NULL COMMENT 'sales_seven,7日销量',
  `sales_fifteen` int(11) DEFAULT NULL COMMENT 'sales_fifteen,15日销量',
  `rank` int(11) DEFAULT NULL,
  `buyprice` decimal(10,2) DEFAULT NULL,
  `shipmentfee` decimal(10,2) DEFAULT NULL,
  `othersfee` decimal(10,2) DEFAULT NULL,
  `costDetail` varchar(2000) DEFAULT '',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='产品信息';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_in_tags 结构
CREATE TABLE IF NOT EXISTS `t_product_in_tags` (
  `pid` bigint(20) unsigned NOT NULL,
  `tagid` bigint(20) unsigned NOT NULL,
  `operator` bigint(20) unsigned NOT NULL,
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`pid`,`tagid`) USING BTREE,
  KEY `tagid` (`tagid`)
) ENGINE=InnoDB  COMMENT='产品-标签';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_price 结构
CREATE TABLE IF NOT EXISTS `t_product_price` (
  `id` bigint(20) unsigned NOT NULL,
  `MarketplaceId` char(15) DEFAULT NULL,
  `asin` char(10) DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `ptype` char(20) DEFAULT NULL,
  `landed_amount` decimal(10,2) DEFAULT NULL,
  `landed_currency` char(5) DEFAULT NULL,
  `listing_amount` decimal(10,2) DEFAULT NULL,
  `listing_currency` char(5) DEFAULT NULL,
  `shipping_amount` decimal(10,2) DEFAULT NULL,
  `shipping_currency` char(5) DEFAULT NULL,
  `isnewest` bit(1) DEFAULT NULL,
  `fulfillmentChannel` char(50) DEFAULT NULL,
  `itemCondition` char(50) DEFAULT NULL,
  `itemSubCondition` char(50) DEFAULT NULL,
  `SellerId` char(15) DEFAULT NULL,
  `SellerSKU` char(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`SellerId`,`MarketplaceId`,`SellerSKU`,`ptype`) USING BTREE
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='产品价格信息表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_price_his 结构
CREATE TABLE IF NOT EXISTS `t_product_price_his` (
  `id` bigint(20) unsigned NOT NULL,
  `MarketplaceId` char(15) DEFAULT NULL,
  `asin` char(10) DEFAULT NULL,
  `byday` datetime DEFAULT NULL,
  `ptype` char(10) DEFAULT NULL,
  `landed_amount` decimal(10,2) DEFAULT NULL,
  `landed_currency` char(5) DEFAULT NULL,
  `listing_amount` decimal(10,2) DEFAULT NULL,
  `listing_currency` char(5) DEFAULT NULL,
  `shipping_amount` decimal(10,2) DEFAULT NULL,
  `shipping_currency` char(5) DEFAULT NULL,
  `isnewest` bit(1) DEFAULT NULL,
  `fulfillmentChannel` char(50) DEFAULT NULL,
  `itemCondition` char(50) DEFAULT NULL,
  `itemSubCondition` char(50) DEFAULT NULL,
  `SellerId` char(50) DEFAULT NULL,
  `SellerSKU` char(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`SellerId`,`MarketplaceId`,`isnewest`,`ptype`,`asin`,`byday`) USING BTREE
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='产品价格信息表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_price_locked 结构
CREATE TABLE IF NOT EXISTS `t_product_price_locked` (
  `pid` bigint(20) unsigned NOT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  `disable` bit(1) NOT NULL DEFAULT b'0',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_rank 结构
CREATE TABLE IF NOT EXISTS `t_product_rank` (
  `id` bigint(20) unsigned NOT NULL,
  `byday` datetime DEFAULT NULL,
  `categoryId` char(50) DEFAULT NULL,
  `rank` int(11) DEFAULT NULL,
  `product_id` bigint(20) unsigned DEFAULT NULL,
  `title` varchar(500)  DEFAULT NULL,
  `link` varchar(500)  DEFAULT NULL,
  `isMain` bit(1) DEFAULT NULL,
  `isNewest` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `FK_t_ranklist_t_produ` (`product_id`,`categoryId`),
  KEY `tprank_pid_byday_rank` (`product_id`,`byday`,`rank`)
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_rank_his 结构
CREATE TABLE IF NOT EXISTS `t_product_rank_his` (
  `id` bigint(20) unsigned NOT NULL,
  `byday` date DEFAULT NULL,
  `categoryId` char(50) DEFAULT NULL,
  `rank` int(11) DEFAULT NULL,
  `product_id` bigint(20) unsigned DEFAULT NULL,
  `isMain` bit(1) DEFAULT NULL,
  `isNewest` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_ranklist_t_produ` (`product_id`),
  KEY `Index 3` (`byday`),
  KEY `categoryId` (`categoryId`,`byday`)
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_rank_sales_his 结构
CREATE TABLE IF NOT EXISTS `t_product_rank_sales_his` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `market` char(5) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '0',
  `byday` date NOT NULL DEFAULT '0000-00-00',
  `ordersum` int(11) NOT NULL DEFAULT '0',
  `rank` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='产品当天在一级大类排名对应产品当天的ordersum';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_recommended 结构
CREATE TABLE IF NOT EXISTS `t_product_recommended` (
  `id` bigint(20) unsigned NOT NULL,
  `amazonAuthId` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `asin` char(36) DEFAULT NULL,
  `name` varchar(1000) DEFAULT NULL,
  `link` varchar(100) DEFAULT NULL COMMENT '产品购买链接',
  `brand` varchar(100) DEFAULT NULL COMMENT '品牌',
  `category` varchar(50) DEFAULT NULL COMMENT '分类',
  `subcategory` varchar(50) DEFAULT NULL COMMENT '子分类',
  `lowestprice` decimal(10,2) DEFAULT NULL COMMENT '上周最低价格',
  `fbaoffer` bit(1) DEFAULT b'0' COMMENT 'fba提供',
  `amzoffer` bit(1) DEFAULT b'0' COMMENT '亚马逊提供',
  `offers` int(11) DEFAULT NULL COMMENT '优惠数量',
  `reviews` int(11) DEFAULT NULL COMMENT '评论数量',
  `rank` int(11) DEFAULT NULL COMMENT '销量排名',
  `sales_rank_growth` char(100) DEFAULT NULL COMMENT '业务销售额排名增长 评级',
  `page_views` char(100) DEFAULT NULL COMMENT '页面浏览量 评级',
  `manufacturer_part_number` varchar(100) DEFAULT NULL COMMENT '制造商零件编号',
  `EAN` varchar(100) DEFAULT NULL COMMENT 'EAN码',
  `UPC` varchar(100) DEFAULT NULL COMMENT 'UPC码',
  `model_number` varchar(100) DEFAULT NULL COMMENT '型号编号',
  `ISBN` varchar(50) DEFAULT NULL COMMENT '图书编码',
  `brandoffer` bit(1) DEFAULT NULL COMMENT '是否 自己提供的品牌',
  `categoryoffer` bit(1) DEFAULT NULL COMMENT '是否 自己提供的类别',
  `performance` char(20) DEFAULT NULL COMMENT '产品性能',
  `istoprank` bit(1) DEFAULT b'0' COMMENT '是否最高销售排名',
  `islowprice` bit(1) DEFAULT b'0' COMMENT '是否最低价格',
  `onAmazon` bit(1) DEFAULT b'0' COMMENT '产品尚未在亚马逊上',
  `isrefresh` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `amazonAuthId_marketplaceid_asin` (`amazonAuthId`,`marketplaceid`,`asin`),
  KEY `asin` (`amazonAuthId`,`sales_rank_growth`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_recommended_ext 结构
CREATE TABLE IF NOT EXISTS `t_product_recommended_ext` (
  `rid` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15) DEFAULT NULL,
  `asin` char(36) DEFAULT NULL,
  `imgurl` varchar(200) DEFAULT NULL,
  `dim` bigint(20) unsigned DEFAULT NULL,
  `currency` char(10) DEFAULT NULL,
  `category` char(50) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `margin` decimal(10,2) DEFAULT NULL,
  `profit` decimal(10,2) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`rid`) USING BTREE,
  KEY `asin` (`asin`),
  KEY `profit` (`profit`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_remark_his 结构
CREATE TABLE IF NOT EXISTS `t_product_remark_his` (
  `pid` bigint(20) unsigned NOT NULL,
  `ftype` char(10) NOT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint(20) unsigned NOT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`pid`,`ftype`,`opttime`) USING BTREE
) ENGINE=InnoDB   COMMENT='产品备注历史表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_type 结构
CREATE TABLE IF NOT EXISTS `t_product_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '类型',
  `country` varchar(5) DEFAULT NULL COMMENT '国家',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147  ROW_FORMAT=DYNAMIC COMMENT='成本计算用，的亚马逊成本。采用父分类的方式，将所有国家的分类都放入此表。\r\n以美国的分类作为主要分类。如果美国没有此分类，则该分类依旧为美国添加，使用其他分类 类型的策略。\r\n当用户选择1 号分类，则其他子分类即改分类对应的其他国家的分类。\r\n ';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_product_usercategory 结构
CREATE TABLE IF NOT EXISTS `t_product_usercategory` (
  `id` char(36)  DEFAULT NULL,
  `name` char(36)  DEFAULT NULL
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_profitcfg 结构
CREATE TABLE IF NOT EXISTS `t_profitcfg` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID 用于区分每一个方案',
  `shop_id` bigint(20) unsigned DEFAULT NULL COMMENT '添加方案的人，只能当事人或其部下使用',
  `isSystem` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否系统内置方案',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `sales_channel` char(10) DEFAULT NULL COMMENT '配送方案，是否亚马逊配送',
  `sellerPlan` char(10) DEFAULT NULL COMMENT '销售计划',
  `shipmentStyle` char(10)  NOT NULL COMMENT '运费计算方式',
  `isDefault` bit(1) DEFAULT b'0' COMMENT '是否为默认方案',
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shop_id` (`shop_id`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='利润计算方案';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_profitcfgcountry 结构
CREATE TABLE IF NOT EXISTS `t_profitcfgcountry` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID 用于区分每一个方案对应不同国家的方案配置项',
  `profitid` bigint(20) unsigned NOT NULL COMMENT '总方案',
  `country` char(36)  NOT NULL COMMENT '国家',
  `constantw` decimal(10,2) DEFAULT '0.00' COMMENT '重量基数',
  `constantd` decimal(10,2) DEFAULT '0.00' COMMENT '材积比',
  `constantm` decimal(10,2) DEFAULT '0.00' COMMENT '材积基数',
  `fba_month` int(11) DEFAULT NULL COMMENT 'FBA配送月份和亚马逊仓储费计算月份',
  `storagefee` decimal(10,3) DEFAULT '0.000' COMMENT '仓储费',
  `taxRate` decimal(10,2) DEFAULT '0.00' COMMENT '进口关税费率(原名：税率)',
  `lostRate` decimal(10,2) DEFAULT '0.00' COMMENT '汇率损耗比率',
  `sellerRate` decimal(10,2) DEFAULT '0.00' COMMENT '市场营销占比',
  `otherfee` decimal(10,2) DEFAULT '0.00' COMMENT '其他每单销售固定费用',
  `costRate` decimal(10,2) DEFAULT '0.00' COMMENT '其他每单销售成本比率',
  `logistics` char(50) DEFAULT NULL COMMENT '物流方式',
  `subscriptionfee` decimal(10,2) DEFAULT NULL COMMENT '订阅费',
  `prepservice` bit(1) DEFAULT b'0' COMMENT '计划内的服务费',
  `labelService` bit(1) DEFAULT b'0' COMMENT '亚马逊标签服务费',
  `manualProcessing` decimal(10,2) DEFAULT '0.00' COMMENT '亚马逊手动处理费',
  `unprepservice` decimal(10,2) DEFAULT '0.00' COMMENT '计划外的服务费',
  `invplacefee` char(36) DEFAULT NULL COMMENT '库存配置费',
  `promotion` decimal(10,2) DEFAULT '0.00' COMMENT 'all store promtion',
  `amonth` int(11) DEFAULT '1' COMMENT '亚马逊仓储费，库存周期',
  `hasAddedSite` bit(1) DEFAULT b'0' COMMENT '当配送方案为Pan EU时，Germany是否添加Poland, Czech Republic仓库站点',
  `warehouse_site` char(10)  DEFAULT NULL COMMENT '当配送方案为EFN时，亚马逊仓库站点',
  `dispatch_type` char(36)  DEFAULT NULL COMMENT '亚马逊配送方案:pan_EU,EFN',
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
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='利润各国计算方案';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_pro_rcd_dimensions 结构
CREATE TABLE IF NOT EXISTS `t_pro_rcd_dimensions` (
  `id` bigint(20) unsigned NOT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `length_units` char(20) DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `width_units` char(20) DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `height_units` char(20) DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `weight_units` char(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_referralfee 结构
CREATE TABLE IF NOT EXISTS `t_referralfee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL COMMENT '类型',
  `isMedia` char(1)   DEFAULT '0' COMMENT '是否媒介',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `loweast` decimal(18,6) DEFAULT '0.000000' COMMENT '最低值',
  `top1` decimal(18,6) DEFAULT NULL COMMENT '第一最高值',
  `top2` decimal(18,6) DEFAULT NULL COMMENT '第二最高值',
  `top3` decimal(18,6) DEFAULT NULL COMMENT '第三最高值',
  `percent1` decimal(18,6) DEFAULT NULL COMMENT '第一百分比',
  `percent2` decimal(18,6) DEFAULT NULL COMMENT '第二百分比',
  `percent3` decimal(18,6) DEFAULT NULL COMMENT '第三百分比',
  `country` varchar(20) NOT NULL COMMENT '国家',
  `parent_id` int(10) DEFAULT NULL COMMENT '父分类',
  PRIMARY KEY (`id`),
  UNIQUE KEY `parent_id` (`country`,`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=699     COMMENT='成本计算用，的亚马逊成本。采用父分类的方式，将所有国家的分类都放入此表。\r\n以美国的分类作为主要分类。如果美国没有此分类，则该分类依旧为美国添加，使用其他分类 类型的策略。\r\n当用户选择1 号分类，则其他子分类即改分类对应的其他国家的分类。\r\n ';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_report_requestrecord 结构
CREATE TABLE IF NOT EXISTS `t_report_requestrecord` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `sellerid` char(15) DEFAULT NULL,
  `marketPlaceId` char(15) DEFAULT NULL,
  `reportId` bigint(20) unsigned DEFAULT NULL,
  `reportType` char(100) DEFAULT NULL,
  `reportRequestId` bigint(20) unsigned DEFAULT NULL,
  `reportDocumentId` varchar(100)  DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `isnewest` bit(1) DEFAULT NULL,
  `availableDate` datetime DEFAULT NULL,
  `getnumber` int(11) DEFAULT '0',
  `treatnumber` int(11) DEFAULT '0',
  `lastupdate` datetime DEFAULT NULL,
  `report_processing_status` char(20)  DEFAULT NULL,
  `log` longtext  ,
  `reportOptions` varchar(100)  DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`reportType`,`reportId`,`sellerid`) USING BTREE,
  KEY `index3` (`lastupdate`) USING BTREE,
  KEY `sellerid_marketPlaceId` (`sellerid`,`marketPlaceId`)
) ENGINE=InnoDB AUTO_INCREMENT=11989481307372265349     ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role 结构
CREATE TABLE IF NOT EXISTS `t_role` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `type` varchar(10) DEFAULT NULL COMMENT '角色类型',
  `issystem` bit(1) NOT NULL DEFAULT b'0',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`shopid`,`name`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='角色';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_adv_group 结构
CREATE TABLE IF NOT EXISTS `t_role_adv_group` (
  `roleid` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned NOT NULL,
  `group_name` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`roleid`,`groupid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_authority 结构
CREATE TABLE IF NOT EXISTS `t_role_authority` (
  `id` bigint(20) unsigned NOT NULL,
  `role_id` bigint(20) unsigned DEFAULT NULL,
  `authority_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_role_authority_t_role` (`role_id`),
  KEY `FK_t_role_authority_t_authority` (`authority_id`),
  CONSTRAINT `FK_t_role_authority_t_authority` FOREIGN KEY (`authority_id`) REFERENCES `t_authority` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='角色权限分配表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_group 结构
CREATE TABLE IF NOT EXISTS `t_role_group` (
  `roleid` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned NOT NULL,
  `group_name` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`roleid`,`groupid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_marketplace 结构
CREATE TABLE IF NOT EXISTS `t_role_marketplace` (
  `id` bigint(20) unsigned NOT NULL,
  `roleid` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`roleid`,`marketplaceid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_menu 结构
CREATE TABLE IF NOT EXISTS `t_role_menu` (
  `id` bigint(20) unsigned NOT NULL,
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色ID',
  `menu_id` char(36) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  KEY `FK_t_role_menu_t_role` (`role_id`),
  KEY `FK_t_role_menu_t_menu` (`menu_id`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='角色菜单分配表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_role_menu_bkp20211115 结构
CREATE TABLE IF NOT EXISTS `t_role_menu_bkp20211115` (
  `id` bigint(20) unsigned NOT NULL,
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色ID',
  `menu_id` char(36) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  KEY `FK_t_role_menu_t_role` (`role_id`),
  KEY `FK_t_role_menu_t_menu` (`menu_id`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='角色菜单分配表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_shop 结构
CREATE TABLE IF NOT EXISTS `t_shop` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(50)  DEFAULT NULL COMMENT '公司名称',
  `remark` varchar(50)  DEFAULT NULL COMMENT '备注',
  `invitecode` char(7)  DEFAULT NULL COMMENT '邀请码',
  `fromcode` char(7)  DEFAULT NULL COMMENT '受邀请码',
  `oldid` char(36)  DEFAULT NULL,
  `boss_email` varchar(100)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `invitecode` (`invitecode`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='店铺';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_summaryall 结构
CREATE TABLE IF NOT EXISTS `t_summaryall` (
  `id` bigint(20) unsigned NOT NULL,
  `amazonauthid` bigint(20) unsigned NOT NULL,
  `purchase_date` date NOT NULL,
  `sales_channel` char(30) DEFAULT NULL,
  `order_status` char(30) DEFAULT NULL,
  `fulfillChannel` char(15) DEFAULT NULL COMMENT '交付渠道',
  `discount` char(1)  NOT NULL DEFAULT '',
  `is_business_order` char(5) DEFAULT NULL,
  `quantity` decimal(32,0) DEFAULT NULL,
  `ordernumber` bigint(21) unsigned NOT NULL DEFAULT '0',
  `discount_amount` decimal(10,2) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`),
  KEY `index_1` (`amazonauthid`,`sales_channel`)
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_app_store_company 结构
CREATE TABLE IF NOT EXISTS `t_sys_app_store_company` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `image` bigint(20) unsigned DEFAULT NULL,
  `telphone` varchar(25) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `QQ` varchar(25) DEFAULT NULL,
  `website` varchar(100) DEFAULT NULL COMMENT '网址',
  `work` varchar(100) DEFAULT NULL COMMENT '业务范围',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `iscompany` bit(1) DEFAULT b'1' COMMENT '是否企业',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_app_store_detail 结构
CREATE TABLE IF NOT EXISTS `t_sys_app_store_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appgroupid` bigint(20) unsigned DEFAULT NULL,
  `appcompanyid` bigint(20) unsigned DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL COMMENT '服务名称',
  `description` varchar(500) DEFAULT NULL COMMENT '服务描述',
  `image` bigint(20) unsigned DEFAULT NULL,
  `price_original` decimal(15,2) DEFAULT NULL COMMENT '原价',
  `price` decimal(15,2) DEFAULT NULL COMMENT '现在售价',
  `recommend_score` float(5,2) DEFAULT NULL COMMENT '推荐指数',
  `recommend_reason` varchar(255) DEFAULT NULL COMMENT '推荐理由',
  `customer_feedback` varchar(500) DEFAULT NULL COMMENT '客户反馈',
  `customer_pageview` int(11) DEFAULT '0' COMMENT '用户浏览量',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_g_c` (`appgroupid`,`appcompanyid`)
) ENGINE=InnoDB AUTO_INCREMENT=47 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_app_store_group 结构
CREATE TABLE IF NOT EXISTS `t_sys_app_store_group` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `image` bigint(20) unsigned DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `createdate` timestamp NULL DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_app_store_service_detail 结构
CREATE TABLE IF NOT EXISTS `t_sys_app_store_service_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `detailid` int(11) DEFAULT NULL,
  `content` longtext COMMENT '服务详情',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `detailid` (`detailid`)
) ENGINE=InnoDB AUTO_INCREMENT=30 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_channel_salesperson_key 结构
CREATE TABLE IF NOT EXISTS `t_sys_channel_salesperson_key` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `salesperson` char(50) NOT NULL DEFAULT '0',
  `fkey` char(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_contact 结构
CREATE TABLE IF NOT EXISTS `t_sys_contact` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(50) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `message` varchar(500) DEFAULT NULL,
  `operatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_customer_discount 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_discount` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(7) DEFAULT NULL COMMENT '折扣编码',
  `ftype` char(10) DEFAULT NULL,
  `packages` int(11) DEFAULT NULL,
  `amount` decimal(10,2) unsigned DEFAULT NULL COMMENT '折扣金额',
  `account` char(50) DEFAULT NULL COMMENT '指定应用帐户（可以不填）',
  `pkgtime` int(11) DEFAULT NULL,
  `isused` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否应用',
  `orderid` char(50) DEFAULT NULL COMMENT '应用订单',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '应用公司',
  `createdate` datetime DEFAULT NULL COMMENT '创建时间',
  `lostime` datetime DEFAULT NULL COMMENT '失效时间',
  `operator` varchar(36) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `number` (`number`)
) ENGINE=InnoDB COMMENT='用户折扣表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_customer_invoice 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_invoice` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `orderid` bigint(20) unsigned DEFAULT NULL,
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
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_customer_order 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_order` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `ftype` char(20) DEFAULT NULL COMMENT '订单类型  package:套餐  append:附加包',
  `out_trade_no` char(20) DEFAULT NULL COMMENT '商户订单号',
  `subject` char(50) DEFAULT NULL COMMENT '订单名称',
  `trade_no` char(50) DEFAULT NULL COMMENT '支付宝交易号',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '付款金额',
  `discountnumber` char(10) DEFAULT NULL,
  `paytime` timestamp NULL DEFAULT NULL,
  `paystatus` char(50) DEFAULT NULL COMMENT '付款状态',
  `ivcstatus` char(50) DEFAULT NULL COMMENT '开票状态',
  `months` int(11) DEFAULT NULL,
  `tariffpackage` int(11) DEFAULT NULL,
  `pcs` int(11) DEFAULT '1',
  `paytype` char(10) DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `out_trade_no` (`out_trade_no`)
) ENGINE=InnoDB COMMENT=' ';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_customer_order_refund 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_order_refund` (
  `id` bigint(20) unsigned DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `orderid` bigint(20) unsigned DEFAULT NULL,
  `out_trade_no` char(20) DEFAULT NULL COMMENT '商户订单号',
  `trade_no` char(50) DEFAULT NULL COMMENT '支付宝交易号',
  `refund_amount` decimal(10,2) DEFAULT NULL COMMENT '付款金额',
  `refund_reason` char(50) DEFAULT NULL COMMENT '订单名称',
  `out_request_no` char(20) DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT=' ';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_dept 结构
CREATE TABLE IF NOT EXISTS `t_sys_dept` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(100) DEFAULT NULL,
  `parent_id` bigint(20) unsigned DEFAULT NULL,
  `tree_path` char(100) DEFAULT NULL,
  `sort` int(10) DEFAULT NULL,
  `status` int(10) DEFAULT NULL,
  `deleted` int(10) DEFAULT NULL,
  `gmt_create` timestamp NULL DEFAULT NULL,
  `gmt_modified` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_dict 结构
CREATE TABLE IF NOT EXISTS `t_sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `name` varchar(50) DEFAULT '' COMMENT '类型名称',
  `code` varchar(50) DEFAULT '' COMMENT '类型编码',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0-正常 ,1-停用）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `type_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1593091333478154242  ROW_FORMAT=DYNAMIC COMMENT='字典类型表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_dict_item 结构
CREATE TABLE IF NOT EXISTS `t_sys_dict_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT '' COMMENT '字典项名称',
  `value` varchar(50) DEFAULT '' COMMENT '字典项值',
  `dict_code` varchar(50) DEFAULT '' COMMENT '字典编码',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0 停用 1正常）',
  `defaulted` tinyint(1) DEFAULT '0' COMMENT '是否默认（0否 1是）',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `value_dict_code` (`dict_code`,`value`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1628336451638321155  ROW_FORMAT=DYNAMIC COMMENT='字典数据表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_help_page 结构
CREATE TABLE IF NOT EXISTS `t_sys_help_page` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键(h)',
  `menuid` char(36)  NOT NULL,
  `content` text  NOT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`menuid`)
) ENGINE=InnoDB AUTO_INCREMENT=38  ROW_FORMAT=DYNAMIC COMMENT='菜单';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_holiday 结构
CREATE TABLE IF NOT EXISTS `t_sys_holiday` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `country` char(3) DEFAULT NULL,
  `marketplaceid` char(20) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`,`month`),
  KEY `marketplaceid` (`marketplaceid`,`month`)
) ENGINE=InnoDB AUTO_INCREMENT=60 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_importrecord 结构
CREATE TABLE IF NOT EXISTS `t_sys_importrecord` (
  `id` bigint(20) unsigned NOT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `issuccess` bit(1) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `importtype` char(50) DEFAULT NULL,
  `log` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`shopid`,`importtype`),
  KEY `opttime` (`opttime`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_mailsender 结构
CREATE TABLE IF NOT EXISTS `t_sys_mailsender` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
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
) ENGINE=InnoDB AUTO_INCREMENT=3 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_mail_template 结构
CREATE TABLE IF NOT EXISTS `t_sys_mail_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mail_subject` varchar(50) DEFAULT NULL,
  `ftype` tinyint(3) unsigned DEFAULT NULL COMMENT '0、系统废弃模板，1、系统通知邮件 ，2、公司通知邮件，3、买家订单回复，4、买家邀请',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `content` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_menu 结构
CREATE TABLE IF NOT EXISTS `t_sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT '' COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID',
  `apppath` varchar(50) DEFAULT NULL,
  `path` varchar(128) DEFAULT '' COMMENT '路由路径',
  `component` varchar(128) DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(64) DEFAULT '' COMMENT '菜单图标',
  `appicon` varchar(64) DEFAULT '',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `visible` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用 1-开启',
  `redirect` varchar(128) DEFAULT '' COMMENT '跳转路径',
  `runui` bit(1) DEFAULT NULL,
  `runapp` bit(1) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  `oldid` char(40) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=66606  ROW_FORMAT=DYNAMIC COMMENT='菜单管理';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_menu_favorite 结构
CREATE TABLE IF NOT EXISTS `t_sys_menu_favorite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shopid` bigint(20) NOT NULL DEFAULT '0' COMMENT '公司ID',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `menuid` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='菜单收藏';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_message_template 结构
CREATE TABLE IF NOT EXISTS `t_sys_message_template` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ftype` char(10) NOT NULL COMMENT '消息类型',
  `content` varchar(2000) DEFAULT NULL COMMENT '消息内容',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_notify 结构
CREATE TABLE IF NOT EXISTS `t_sys_notify` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '0',
  `content` varchar(2000) DEFAULT NULL,
  `ftype` int(4) NOT NULL COMMENT '消息的类型，1: 公告 Announce，2: 提醒 Remind，3：信息 Message',
  `target` char(100) DEFAULT NULL COMMENT '目标类型',
  `action` char(100) DEFAULT NULL COMMENT '提醒信息的动作类型',
  `sender` bigint(20) unsigned DEFAULT NULL COMMENT '发送者的ID',
  `receiver` bigint(20) unsigned DEFAULT NULL COMMENT '接受者ID',
  `createdAt` datetime DEFAULT NULL COMMENT '创建时间',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_notify_shopid` (`ftype`) USING BTREE,
  KEY `shopid` (`shopid`),
  KEY `target` (`target`),
  KEY `createdAt` (`createdAt`)
) ENGINE=InnoDB AUTO_INCREMENT=715814 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_operationlog 结构
CREATE TABLE IF NOT EXISTS `t_sys_operationlog` (
  `id` bigint(20) unsigned NOT NULL,
  `time` datetime DEFAULT NULL,
  `ip` char(35) DEFAULT NULL,
  `userid` bigint(20) unsigned DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `logType` varchar(255) DEFAULT NULL,
  `method` char(100) DEFAULT NULL,
  `exceptionDetail` char(50) DEFAULT NULL,
  `param` varchar(4000) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `time` (`userid`,`method`,`time`) USING BTREE,
  KEY `idx_method_time_userid` (`method`,`time`,`userid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_permission 结构
CREATE TABLE IF NOT EXISTS `t_sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) DEFAULT NULL COMMENT '权限名称',
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单模块ID\r\n',
  `url_perm` varchar(128) DEFAULT NULL COMMENT 'URL权限标识',
  `btn_perm` varchar(64) DEFAULT NULL COMMENT '按钮权限标识',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id` (`id`,`name`) USING BTREE,
  KEY `id_2` (`id`,`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1656145342726844418  ROW_FORMAT=DYNAMIC COMMENT='权限表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_potential_customer 结构
CREATE TABLE IF NOT EXISTS `t_sys_potential_customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` varchar(2000) DEFAULT NULL,
  `user_name` varchar(1000) DEFAULT NULL,
  `address` varchar(2000) DEFAULT NULL,
  `telphone` char(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `sendtime` timestamp NULL DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`telphone`,`email`)
) ENGINE=InnoDB AUTO_INCREMENT=38005 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_quartz_task 结构
CREATE TABLE IF NOT EXISTS `t_sys_quartz_task` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `fgroup` varchar(50) DEFAULT NULL,
  `priority` int(11) DEFAULT '5',
  `description` varchar(50) DEFAULT NULL,
  `cron` varchar(30) DEFAULT NULL,
  `server` varchar(20) DEFAULT NULL,
  `bean` varchar(50) DEFAULT NULL,
  `method` varchar(50) DEFAULT NULL,
  `parameter` varchar(200) DEFAULT NULL,
  `path` varchar(200) DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=136 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_query_field 结构
CREATE TABLE IF NOT EXISTS `t_sys_query_field` (
  `fquery` char(20) NOT NULL,
  `ffield` char(30) NOT NULL,
  `title` char(50) DEFAULT NULL,
  `titleTooltip` char(50) DEFAULT NULL,
  `width` char(50) DEFAULT NULL,
  `findex` int(11) DEFAULT NULL,
  `formatter` char(50) DEFAULT NULL,
  `footerFormatter` char(50) DEFAULT NULL,
  `sortable` bit(1) DEFAULT NULL,
  `valign` char(10) DEFAULT NULL,
  `align` char(10) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`fquery`,`ffield`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_query_user_version 结构
CREATE TABLE IF NOT EXISTS `t_sys_query_user_version` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) unsigned DEFAULT NULL,
  `fquery` char(20) DEFAULT NULL,
  `isused` bit(1) DEFAULT NULL,
  `name` char(50) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`,`fquery`)
) ENGINE=InnoDB AUTO_INCREMENT=123457005 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_query_version_feild 结构
CREATE TABLE IF NOT EXISTS `t_sys_query_version_feild` (
  `fversionid` bigint(20) unsigned NOT NULL,
  `ffield` char(30) NOT NULL,
  `findex` int(11) NOT NULL,
  PRIMARY KEY (`fversionid`,`ffield`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_role_menu 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_menu` (
  `role_id` bigint(20) unsigned NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) unsigned NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`),
  KEY `menu_id` (`menu_id`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='角色和菜单关联表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_role_permission 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_permission` (
  `role_id` bigint(20) unsigned NOT NULL COMMENT '角色id',
  `permission_id` bigint(20) unsigned NOT NULL COMMENT '资源id',
  PRIMARY KEY (`role_id`,`permission_id`) USING BTREE,
  KEY `permission_id` (`permission_id`) USING BTREE
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='角色权限表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_role_tag 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_tag` (
  `id` bigint(20) unsigned NOT NULL,
  `tag_id` char(36) DEFAULT NULL,
  `roleid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`tag_id`,`roleid`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='管理员新建，用于给不同下属分配不同的产品查询权限';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_role_taggroup 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_taggroup` (
  `id` bigint(20) unsigned NOT NULL,
  `roleid` bigint(20) unsigned DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`roleid`,`groupid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_subscription 结构
CREATE TABLE IF NOT EXISTS `t_sys_subscription` (
  `target` char(2) NOT NULL,
  `userid` bigint(20) unsigned NOT NULL,
  `action` char(100) DEFAULT NULL COMMENT '订阅动作',
  `disable` bit(1) NOT NULL DEFAULT b'0',
  `createdAt` datetime DEFAULT NULL,
  PRIMARY KEY (`target`,`userid`),
  CONSTRAINT `FK_t_sys_subscription_t_sys_target` FOREIGN KEY (`target`) REFERENCES `t_sys_target` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_tags 结构
CREATE TABLE IF NOT EXISTS `t_sys_tags` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `name` char(200) DEFAULT NULL COMMENT '标签名称',
  `value` varchar(200)  DEFAULT NULL,
  `sort` int(10) DEFAULT NULL,
  `color` char(50)  DEFAULT NULL,
  `taggroupid` bigint(20) unsigned DEFAULT NULL COMMENT '分组ID',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `creator` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '修改人',
  `remark` char(200)  DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `gmt_create` datetime DEFAULT NULL COMMENT '修改日期',
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name_taggroupid` (`name`,`taggroupid`) USING BTREE,
  KEY `FK_t_tag_t_sho` (`shopid`) USING BTREE,
  KEY `groupid` (`taggroupid`) USING BTREE
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_tags_groups 结构
CREATE TABLE IF NOT EXISTS `t_sys_tags_groups` (
  `id` bigint(20) unsigned NOT NULL,
  `sort` int(10) DEFAULT NULL,
  `name` char(100) DEFAULT NULL,
  `shop_id` bigint(20) unsigned DEFAULT NULL,
  `description` char(100) DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `opterator` bigint(20) unsigned DEFAULT NULL,
  `remark` char(200)  DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name_shop_id` (`name`,`shop_id`),
  KEY `FK_t_tag_group_t_shop` (`shop_id`) USING BTREE
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_target 结构
CREATE TABLE IF NOT EXISTS `t_sys_target` (
  `id` char(2) NOT NULL,
  `name` char(50) NOT NULL,
  `description` char(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_tariff_packages 结构
CREATE TABLE IF NOT EXISTS `t_sys_tariff_packages` (
  `id` int(11) NOT NULL COMMENT '套餐id 0-基础版，1-标准版，2-专业版，3-独享版,4-自定义',
  `name` char(36) NOT NULL COMMENT '套餐名字',
  `roleId` bigint(20) unsigned NOT NULL COMMENT '角色id',
  `isdefault` bit(1) NOT NULL DEFAULT b'0',
  `maxShopCount` int(11) DEFAULT '1' COMMENT '绑定店铺数量',
  `maxProductCount` int(11) DEFAULT '50000' COMMENT '商品数量上限',
  `maxOrderCount` int(11) DEFAULT '3600' COMMENT '处理订单上限',
  `maxMember` int(11) DEFAULT '10' COMMENT '子用户数量上限',
  `maxProfitPlanCount` int(11) DEFAULT '1' COMMENT '利润计算方案数量',
  `maxMarketCount` int(11) DEFAULT '1' COMMENT '每个店铺绑定站点数量',
  `orderMemoryCount` char(10) NOT NULL DEFAULT '3' COMMENT '订单存储时间 单位:月  默认基础版是3个月',
  `dayOpenAdvCount` int(11) NOT NULL DEFAULT '10' COMMENT '每天开启广告组数量',
  `controlProductCount` int(11) NOT NULL DEFAULT '10' COMMENT '跟卖监控产品数量',
  `anysisProductCount` int(11) NOT NULL DEFAULT '10' COMMENT '商品分析数量',
  `yearprice` decimal(18,2) DEFAULT NULL,
  `monthprice` decimal(18,2) DEFAULT NULL,
  `lastUpdateTime` date DEFAULT NULL COMMENT '最后更新时间',
  `lastUpdateUser` varchar(36) DEFAULT NULL COMMENT '最后更新的人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='套餐表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_tariff_packages_append 结构
CREATE TABLE IF NOT EXISTS `t_sys_tariff_packages_append` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ftype` char(20)  DEFAULT NULL,
  `name` char(50)  DEFAULT NULL,
  `units` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_tariff_packages_append_discount 结构
CREATE TABLE IF NOT EXISTS `t_sys_tariff_packages_append_discount` (
  `appendid` int(10) DEFAULT NULL,
  `packages` int(10) DEFAULT NULL,
  `month` int(10) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  UNIQUE KEY `appendid_packages_month` (`appendid`,`packages`,`month`)
) ENGINE=InnoDB ;

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
  `is_concurrent` int(4) DEFAULT NULL COMMENT '运行状态',
  `job_data` longtext COMMENT '参数',
  `menthod_name` varchar(200) DEFAULT NULL COMMENT '方法',
  `bean_name` varchar(200) DEFAULT NULL COMMENT '实例bean',
  `description` varchar(1000) DEFAULT NULL COMMENT '任务描述',
  `creator` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_timetask_log 结构
CREATE TABLE IF NOT EXISTS `t_sys_timetask_log` (
  `id` char(36) NOT NULL,
  `createdate` datetime DEFAULT NULL,
  `job_id` char(36) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_usernotify 结构
CREATE TABLE IF NOT EXISTS `t_sys_usernotify` (
  `userid` bigint(20) unsigned NOT NULL COMMENT '用户消息所属者',
  `notify` int(11) NOT NULL,
  `isRead` bit(1) NOT NULL DEFAULT b'0' COMMENT '0，代表未读；1，代表已读',
  `createdAt` datetime DEFAULT NULL,
  PRIMARY KEY (`userid`,`notify`),
  KEY `Index 2` (`notify`),
  KEY `createdAt` (`isRead`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_user_bind 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_bind` (
  `userid` bigint(20) unsigned NOT NULL,
  `bindid` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`userid`) USING BTREE,
  KEY `bindid` (`bindid`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_user_datalimit 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_datalimit` (
  `userid` bigint(20) unsigned NOT NULL,
  `datatype` char(15) NOT NULL COMMENT 'owner只能查看自己负责的产品（在产品管理页面配置）;operations只能查看自己运营的产品（在商品分析页面配置）',
  `islimit` bit(1) DEFAULT b'0' COMMENT 'true表示需要限制，false表示不需要限制',
  PRIMARY KEY (`userid`,`datatype`) USING BTREE
) ENGINE=InnoDB COMMENT='用户数据权限，放在用户信息中，登录后将在所有模块生效';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_user_group 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_group` (
  `userid` bigint(20) unsigned DEFAULT NULL COMMENT '用户ID',
  `groupid` bigint(20) unsigned DEFAULT NULL COMMENT '店铺ID'
) ENGINE=InnoDB COMMENT='用户客户对店铺的权限绑定';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_user_ip_city 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_ip_city` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userip` char(50) NOT NULL DEFAULT '0',
  `city` char(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userip` (`userip`)
) ENGINE=InnoDB AUTO_INCREMENT=1385 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_video 结构
CREATE TABLE IF NOT EXISTS `t_sys_video` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `menuid` char(36) DEFAULT NULL,
  `video_url` varchar(100) DEFAULT NULL,
  `video_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_sys_weather 结构
CREATE TABLE IF NOT EXISTS `t_sys_weather` (
  `id` bigint(20) unsigned NOT NULL,
  `city` varchar(20) NOT NULL COMMENT '城市',
  `weatype` varchar(20) DEFAULT NULL COMMENT '值:多云 晴 小雨 大雨',
  `updatedate` datetime DEFAULT NULL COMMENT '当前日期的天气',
  `nowdegree` int(11) DEFAULT NULL COMMENT '当前温度 ''C',
  `lowdegree` varchar(50) DEFAULT NULL COMMENT '最低温度',
  `highdegree` varchar(50) DEFAULT NULL COMMENT '最高温度',
  `weaforce` varchar(50) DEFAULT NULL COMMENT '风向',
  PRIMARY KEY (`id`),
  UNIQUE KEY `city` (`city`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='城市天气表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_tasklock 结构
CREATE TABLE IF NOT EXISTS `t_tasklock` (
  `task` char(15) NOT NULL,
  `mylock` bit(1) DEFAULT NULL,
  PRIMARY KEY (`task`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` bigint(20) unsigned NOT NULL COMMENT '整型自增主键',
  `account` varchar(200) NOT NULL,
  `password` char(50) DEFAULT NULL COMMENT '用户密码，采用MD5加密',
  `salt` char(50)  DEFAULT NULL,
  `leader_id` bigint(20) unsigned DEFAULT NULL COMMENT '上层',
  `createDate` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `losingEffect` date DEFAULT NULL COMMENT '失效时间',
  `logicDelete` bit(1) DEFAULT b'0' COMMENT '逻辑删除',
  `disable` bit(1) DEFAULT b'0' COMMENT '停用',
  `isActive` bit(1) DEFAULT b'1' COMMENT '账户是否激活',
  `hasEmail` bit(1) DEFAULT b'0' COMMENT '邮箱是否激活',
  `member` int(8) DEFAULT '5' COMMENT '拥有下属数量上限',
  `passwordkey` char(36)  DEFAULT NULL,
  `lastlogintime` datetime DEFAULT NULL,
  `lastloginip` varchar(100)  DEFAULT NULL,
  `lastloginsession` varchar(200)  DEFAULT NULL,
  `ftype` char(50)  DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  `deptid` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`),
  KEY `leader_id` (`leader_id`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_userinfo 结构
CREATE TABLE IF NOT EXISTS `t_userinfo` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `picture` char(36) DEFAULT NULL,
  `tel` varchar(25)  DEFAULT NULL,
  `company` varchar(100)  DEFAULT NULL,
  `email` varchar(100)  DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_userinfo_t_picture` (`picture`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='用户详细信息表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_user_role 结构
CREATE TABLE IF NOT EXISTS `t_user_role` (
  `id` bigint(20) unsigned NOT NULL,
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 4` (`user_id`,`role_id`),
  KEY `FK_t_user_role_t_user` (`user_id`),
  KEY `FK_t_user_role_t_role` (`role_id`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='用户角色分配表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_user_shop 结构
CREATE TABLE IF NOT EXISTS `t_user_shop` (
  `id` bigint(20) unsigned NOT NULL,
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户ID',
  `shop_id` bigint(20) unsigned DEFAULT NULL COMMENT '店铺ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shop_id` (`shop_id`),
  KEY `FK_t_user_shop_t_user` (`user_id`)
) ENGINE=InnoDB  ROW_FORMAT=DYNAMIC COMMENT='用户店铺归属表';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_user_wechat_login 结构
CREATE TABLE IF NOT EXISTS `t_user_wechat_login` (
  `openid` char(36) NOT NULL,
  `userid` bigint(20) unsigned NOT NULL,
  `unionid` char(36) DEFAULT NULL,
  `access_token` char(200) DEFAULT NULL,
  `refresh_token` char(200) DEFAULT NULL,
  PRIMARY KEY (`openid`) USING BTREE,
  UNIQUE KEY `userid` (`userid`) USING BTREE,
  UNIQUE KEY `unionid` (`unionid`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_user_wechat_mp 结构
CREATE TABLE IF NOT EXISTS `t_user_wechat_mp` (
  `openid` char(36) NOT NULL,
  `userid` bigint(20) unsigned NOT NULL,
  `ftype` char(10) NOT NULL,
  `access_token` char(200) DEFAULT NULL,
  `refresh_token` char(200) DEFAULT NULL,
  PRIMARY KEY (`openid`,`ftype`,`userid`) USING BTREE,
  KEY `key` (`userid`,`ftype`) USING BTREE
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.t_variable_closing_fee 结构
CREATE TABLE IF NOT EXISTS `t_variable_closing_fee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country` char(3)  DEFAULT NULL,
  `typeid` int(11) DEFAULT NULL,
  `isMedia` char(1)  DEFAULT NULL,
  `logisticsId` char(10)   DEFAULT '0',
  `name` varchar(50)  DEFAULT NULL,
  `format` char(255)   DEFAULT '0.00',
  PRIMARY KEY (`id`),
  KEY `FK_t_logistics_t_referralfee` (`typeid`),
  KEY `country` (`country`),
  CONSTRAINT `FK_t_logistics_t_referralfee` FOREIGN KEY (`typeid`) REFERENCES `t_referralfee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=109  ROW_FORMAT=DYNAMIC COMMENT='物流方式';

-- 数据导出被取消选择。

-- 导出  表 db_wimoor.undo_log 结构
CREATE TABLE IF NOT EXISTS `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB ;

-- 数据导出被取消选择。

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

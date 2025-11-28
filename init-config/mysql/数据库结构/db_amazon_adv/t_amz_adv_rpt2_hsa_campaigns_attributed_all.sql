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

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

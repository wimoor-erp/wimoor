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

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

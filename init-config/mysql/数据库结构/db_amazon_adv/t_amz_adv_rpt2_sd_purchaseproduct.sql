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

-- 导出  表 db_amazon_adv.t_amz_adv_rpt2_sd_purchaseproduct 结构
CREATE TABLE IF NOT EXISTS `t_amz_adv_rpt2_sd_purchaseproduct` (
  `adGroupId` bigint unsigned NOT NULL,
  `campaignId` bigint unsigned DEFAULT NULL,
  `bydate` date NOT NULL,
  `adGroupName` char(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `asinBrandHalo` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `addToList` decimal(10,2) DEFAULT NULL,
  `addToListFromClicks` decimal(10,2) DEFAULT NULL,
  `qualifiedBorrowsFromClicks` decimal(10,2) DEFAULT NULL,
  `royaltyQualifiedBorrowsFromClicks` decimal(10,2) DEFAULT NULL,
  `addToListFromViews` decimal(10,2) DEFAULT NULL,
  `qualifiedBorrows` decimal(10,2) DEFAULT NULL,
  `qualifiedBorrowsFromViews` decimal(10,2) DEFAULT NULL,
  `royaltyQualifiedBorrows` decimal(10,2) DEFAULT NULL,
  `royaltyQualifiedBorrowsFromViews` decimal(10,2) DEFAULT NULL,
  `campaignBudgetCurrencyCode` char(10) DEFAULT NULL,
  `campaignName` char(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `conversionsBrandHalo` decimal(10,2) DEFAULT NULL,
  `conversionsBrandHaloClicks` decimal(10,2) DEFAULT NULL,
  `promotedAsin` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `promotedSku` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `salesBrandHalo` decimal(10,2) DEFAULT NULL,
  `salesBrandHaloClicks` decimal(10,2) DEFAULT NULL,
  `unitsSoldBrandHalo` decimal(10,2) DEFAULT NULL,
  `unitsSoldBrandHaloClicks` decimal(10,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`bydate`,`adGroupId`,`promotedAsin`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Amazon Advertising SD Purchase Product Report';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

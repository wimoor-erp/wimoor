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

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

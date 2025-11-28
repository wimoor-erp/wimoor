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

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

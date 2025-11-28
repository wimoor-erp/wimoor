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

-- 导出  表 db_amazon.t_inventory_report_his_archive 结构
CREATE TABLE IF NOT EXISTS `t_inventory_report_his_archive` (
  `id` bigint unsigned NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `byday` date NOT NULL,
  `marketplaceid` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `fnsku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `pcondition` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `your_price` decimal(10,2) DEFAULT NULL,
  `mfn_listing_exists` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `mfn_fulfillable_quantity` int DEFAULT NULL,
  `afn_listing_exists` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

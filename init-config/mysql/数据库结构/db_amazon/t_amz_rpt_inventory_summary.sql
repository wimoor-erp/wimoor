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

-- 导出  表 db_amazon.t_amz_rpt_inventory_summary 结构
CREATE TABLE IF NOT EXISTS `t_amz_rpt_inventory_summary` (
  `id` bigint unsigned NOT NULL,
  `authid` bigint unsigned DEFAULT NULL,
  `byday` date DEFAULT NULL,
  `location` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `msku` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `fnsku` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `asin` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `disposition` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `startingWarehouseBalance` int DEFAULT NULL,
  `endingWarehouseBalance` int DEFAULT NULL,
  `inTransitBetweenWarehouses` int DEFAULT NULL,
  `receipts` int DEFAULT NULL,
  `customerShipments` int DEFAULT NULL,
  `customerReturns` int DEFAULT NULL,
  `vendorReturns` int DEFAULT NULL,
  `warehouseTransferInOut` int DEFAULT NULL,
  `found` int DEFAULT NULL,
  `lost` int DEFAULT NULL,
  `damaged` int DEFAULT NULL,
  `disposed` int DEFAULT NULL,
  `otherEvents` int DEFAULT NULL,
  `unknownEvents` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `byday_msku_location_authid` (`authid`,`byday`,`disposition`,`location`,`msku`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

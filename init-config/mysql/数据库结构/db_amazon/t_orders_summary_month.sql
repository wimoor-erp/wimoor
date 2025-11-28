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

-- 导出  表 db_amazon.t_orders_summary_month 结构
CREATE TABLE IF NOT EXISTS `t_orders_summary_month` (
  `id` bigint unsigned NOT NULL,
  `amazonAuthId` bigint unsigned NOT NULL,
  `marketplaceid` char(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `purchase_date` date NOT NULL,
  `asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `quantity` int DEFAULT NULL,
  `ordersum` int DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `quantity2b` int DEFAULT NULL,
  `ordersum2b` int DEFAULT NULL,
  `orderprice2b` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`purchase_date`,`id`),
  UNIQUE KEY `index_1` (`amazonAuthId`,`marketplaceid`,`sku`,`purchase_date`),
  KEY `Index 2` (`marketplaceid`,`quantity`,`orderprice`,`ordersum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

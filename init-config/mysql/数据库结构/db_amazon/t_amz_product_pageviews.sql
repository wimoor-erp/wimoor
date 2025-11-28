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

-- 导出  表 db_amazon.t_amz_product_pageviews 结构
CREATE TABLE IF NOT EXISTS `t_amz_product_pageviews` (
  `amazonAuthid` bigint unsigned NOT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `SKU` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `byday` date NOT NULL,
  `child_asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `parent_asin` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `Sessions` int DEFAULT NULL,
  `Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Page_Views` int DEFAULT NULL,
  `Page_Views_Percentage` decimal(10,2) DEFAULT NULL,
  `Buy_Box_Percentage` decimal(10,2) DEFAULT NULL,
  `Units_Ordered` int DEFAULT NULL,
  `Units_Ordered_B2B` int DEFAULT NULL,
  `Unit_Session_Percentage` decimal(10,2) DEFAULT NULL,
  `Unit_Session_Percentage_B2B` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales` decimal(10,2) DEFAULT NULL,
  `Ordered_Product_Sales_B2B` decimal(10,2) DEFAULT NULL,
  `Total_Order_Items` int DEFAULT NULL,
  `Total_Order_Items_B2B` int DEFAULT NULL,
  PRIMARY KEY (`amazonAuthid`,`marketplaceid`,`byday`,`SKU`,`child_asin`),
  KEY `amazonAuthid_marketplaceid_byday` (`amazonAuthid`,`marketplaceid`,`byday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

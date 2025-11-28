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

-- 导出  表 db_quote.t_supplier_quotation_price 结构
CREATE TABLE IF NOT EXISTS `t_supplier_quotation_price` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `orderid` bigint unsigned NOT NULL,
  `confirm` bit(1) NOT NULL DEFAULT b'0',
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  `base` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `destination` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipmentid` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `supplierid` bigint unsigned DEFAULT NULL,
  `supplier_channel` bigint unsigned DEFAULT NULL,
  `weight` decimal(20,6) DEFAULT NULL,
  `ftype` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `unitprice` decimal(20,6) DEFAULT NULL,
  `tax` decimal(20,6) DEFAULT NULL,
  `otherfee` decimal(20,6) DEFAULT NULL,
  `shipfee` decimal(20,6) DEFAULT NULL,
  `totalfee` decimal(20,6) DEFAULT NULL,
  `pricetime` datetime DEFAULT NULL,
  `remark` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_key` (`orderid`,`supplierid`,`shipmentid`,`destination`),
  KEY `orderid_shipmentid_supplierid` (`orderid`,`supplierid`) USING BTREE,
  KEY `shipmentid` (`shipmentid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1905544624842272256 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

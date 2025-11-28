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

-- 导出  表 db_amazon.t_erp_ship_inbounditem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inbounditem` (
  `id` bigint unsigned NOT NULL,
  `ShipmentId` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `inboundplanid` bigint unsigned DEFAULT NULL COMMENT 'inboundplan的id',
  `FulfillmentNetworkSKU` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `SellerSKU` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商品的卖家 SKU。',
  `QuantityShipped` int DEFAULT NULL,
  `QuantityReceived` int DEFAULT NULL,
  `QuantityInCase` int DEFAULT NULL,
  `Quantity` int DEFAULT NULL COMMENT '	要配送的商品数量。',
  `PrepInstruction` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `PrepOwner` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `msku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `unitcost` decimal(20,6) DEFAULT NULL,
  `totalcost` decimal(20,6) DEFAULT NULL,
  `unittransfee` decimal(20,6) DEFAULT NULL,
  `totaltransfee` decimal(20,6) DEFAULT NULL,
  `ReceivedDate` datetime DEFAULT NULL,
  `QuantityReceivedSub` int DEFAULT NULL,
  `QuantityReceivedBalance` int DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 3` (`SellerSKU`,`ShipmentId`),
  KEY `idx_ShipmentId_QuantityReceived_QuantityShipped` (`ShipmentId`,`QuantityReceived`,`QuantityShipped`),
  KEY `msku` (`msku`),
  KEY `SellerSKU_amazonauthid_marketplaceid_ReceivedDate` (`amazonauthid`,`marketplaceid`,`SellerSKU`,`ReceivedDate`),
  KEY `Index 4` (`ShipmentId`),
  KEY `FK_t_erp_ship_inboundplanitem_t_erp_ship_inboundplan` (`inboundplanid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

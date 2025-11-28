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

-- 导出  表 db_erp.t_erp_purchase_alibaba_order_baseinfo 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_order_baseinfo` (
  `id` bigint unsigned NOT NULL,
  `idOfStr` char(30) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `alipayTradeId` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `allDeliveredTime` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `modifyTime` datetime DEFAULT NULL,
  `payTime` datetime DEFAULT NULL,
  `businessType` char(2) COLLATE utf8mb4_bin DEFAULT NULL,
  `status` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `buyerFeedback` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `remark` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `closeOperateType` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `tradeType` char(10) COLLATE utf8mb4_bin DEFAULT NULL,
  `discount` int DEFAULT NULL,
  `refund` int DEFAULT NULL,
  `overSeaOrder` bit(1) DEFAULT NULL,
  `refundPayment` decimal(20,6) DEFAULT NULL,
  `shippingFee` decimal(20,6) DEFAULT NULL,
  `totalAmount` decimal(20,6) DEFAULT NULL,
  `sumProductPayment` decimal(20,6) DEFAULT NULL,
  `flowTemplateCode` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `buyerID` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `sellerID` char(50) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

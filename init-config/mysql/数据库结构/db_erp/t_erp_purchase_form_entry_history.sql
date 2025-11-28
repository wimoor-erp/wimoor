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

-- 导出  表 db_erp.t_erp_purchase_form_entry_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_history` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `supplier` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `itemprice` decimal(10,2) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `ischange` bit(1) DEFAULT b'0',
  `auditstatus` int DEFAULT NULL COMMENT '0:草稿，退回；  1:待审核  ；2:审核通过 ；3：已完成,4.审核待下单',
  `paystatus` int DEFAULT NULL,
  `planitemid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `inwhstatus` int DEFAULT NULL,
  `totalpay` decimal(10,2) DEFAULT '0.00',
  `totalre` int DEFAULT '0',
  `totalin` int DEFAULT '0',
  `totalch` int DEFAULT '0',
  `deliverydate` datetime DEFAULT NULL,
  `closerecdate` datetime DEFAULT NULL COMMENT '入库结束时间',
  `closepaydate` datetime DEFAULT NULL COMMENT '付款结束时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  KEY `inwhstatus` (`inwhstatus`),
  KEY `auditstatus` (`auditstatus`),
  KEY `supplier` (`supplier`),
  KEY `formid_materialid_planitemid` (`formid`,`materialid`,`planitemid`),
  KEY `paystatus` (`paystatus`),
  KEY `id` (`id`),
  KEY `materialid` (`materialid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

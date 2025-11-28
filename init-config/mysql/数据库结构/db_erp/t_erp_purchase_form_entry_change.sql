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

-- 导出  表 db_erp.t_erp_purchase_form_entry_change 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_change` (
  `id` bigint unsigned NOT NULL,
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `entryid` bigint unsigned DEFAULT NULL,
  `supplierid` bigint unsigned DEFAULT NULL,
  `logistics` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `amount` int unsigned DEFAULT NULL,
  `auditstatus` int unsigned DEFAULT '1' COMMENT '1:进行中，0：已完成',
  `without_inv` bit(1) DEFAULT b'0' COMMENT '是否扣库存 0需要扣  1不需要',
  `totalin` int unsigned DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `mainid` bigint unsigned DEFAULT NULL,
  `ass_form_id` bigint unsigned DEFAULT NULL,
  `dis_form_id` bigint unsigned DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`number`,`shopid`),
  KEY `warehouseid` (`warehouseid`),
  KEY `ass_form_id` (`ass_form_id`),
  KEY `materialid` (`materialid`),
  KEY `entryid` (`entryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

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

-- 导出  表 db_erp.t_erp_ship_planitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planitem` (
  `id` bigint unsigned NOT NULL DEFAULT '0',
  `plansubid` bigint unsigned DEFAULT NULL,
  `status` int DEFAULT NULL COMMENT '0代表已放弃，1 代表可用 2.代表已提交。如果plansub的status等于2这里的1 也是已提交',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `materialid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT NULL COMMENT '实际发货量',
  `selfamount` int DEFAULT NULL,
  `goodsworth` decimal(15,4) DEFAULT NULL COMMENT '货物货值',
  `planweight` decimal(15,4) DEFAULT NULL COMMENT 'itemweight*amount',
  `dimweight` decimal(15,4) DEFAULT NULL COMMENT '材积',
  `needship` int DEFAULT NULL COMMENT '建议发货量',
  PRIMARY KEY (`id`),
  KEY `Index 2` (`plansubid`,`sku`),
  KEY `status_materialid` (`materialid`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

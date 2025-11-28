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

-- 导出  表 db_erp.t_erp_assembly_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned NOT NULL,
  `materialid` bigint unsigned NOT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `amount` int DEFAULT '0',
  `subnumber` int DEFAULT '0',
  `whamount` int DEFAULT '0' COMMENT '仓库调出量',
  `phamount` int DEFAULT '0' COMMENT '采购量',
  `phedamount` int DEFAULT '0' COMMENT '已经采购数量',
  `purchase_from_entry_id` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`formid`,`materialid`),
  UNIQUE KEY `indexunique` (`id`),
  KEY `purchase_from_entry_id` (`purchase_from_entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

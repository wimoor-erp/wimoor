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

-- 导出  表 db_erp.t_erp_v2_plan_purchase_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_material` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `materialid` bigint unsigned NOT NULL COMMENT '本地产品ID',
  `purchasedaynum` int DEFAULT NULL COMMENT '采购周期天数',
  `needamount` int DEFAULT NULL COMMENT '需求量（通过采购周期内需求量汇总减去库存得出）',
  `suggestamount` int DEFAULT NULL COMMENT '建议采购量（通过对需求量合箱规得出）',
  `sumrquest` int DEFAULT NULL COMMENT '总需求量',
  `findex` int DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`materialid`),
  KEY `shopid` (`shopid`),
  KEY `sumrquest` (`sumrquest`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品采购计划，计算结果，存储每个SKU对应的采购周期，需求量，建议采购量，总需求量等';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

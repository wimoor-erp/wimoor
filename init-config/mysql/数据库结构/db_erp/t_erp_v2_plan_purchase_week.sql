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

-- 导出  表 db_erp.t_erp_v2_plan_purchase_week 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_week` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `materialid` bigint unsigned NOT NULL COMMENT '本地产品ID',
  `week` date NOT NULL COMMENT '周日期',
  `requestqty` int NOT NULL DEFAULT '0' COMMENT '需求量（将商品销量通过对关系与本地产品对应）',
  `moreqty` int DEFAULT '0' COMMENT '多余库存（库存减去对应安全库存+头程周期+增长天数对应需求量的和）',
  `suggestqty` int DEFAULT '0' COMMENT '建议提货量（将需求量+剩余库存，缺少部分通过合箱规后）',
  `differentqty` int DEFAULT '0' COMMENT '差异数量-（销售预测与库存之间的差异值）',
  `isfull` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`materialid`,`week`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='产品采购计划，通过发货需求与本地产品对应，并进行本地SKU转化，组装子SKU转换\r\n采购周情况存储。包换需求量，多余库存等';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

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

-- 导出  表 db_erp.t_erp_v2_plan_man_month 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_man_month` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned NOT NULL COMMENT '公司ID',
  `month` date DEFAULT NULL COMMENT '对应月份',
  `standardtime` int DEFAULT NULL COMMENT '以标准工时计算的月份对应的总时长',
  `standardperson` int DEFAULT NULL COMMENT '以标准工时算对应所需要的人力',
  `overtime` int DEFAULT NULL COMMENT '以超出时间算的对应时长',
  `overperson` int DEFAULT NULL COMMENT '以超出时间算的对应人力',
  `multiple` float DEFAULT NULL COMMENT '当前月份计算的倍数',
  `unitsoftime` int DEFAULT NULL COMMENT '一个员工常规工作时间',
  `overoftime` int DEFAULT NULL COMMENT '超出时间（即加班） 工作时间',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_month` (`shopid`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='人力计算结果保存';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

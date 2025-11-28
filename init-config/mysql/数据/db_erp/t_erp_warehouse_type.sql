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

-- 导出  表 db_erp.t_erp_warehouse_type 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_type` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'ID',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '店铺',
  `issystem` bit(1) DEFAULT NULL COMMENT '是否系统',
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名字',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 正在导出表  db_erp.t_erp_warehouse_type 的数据：~9 rows (大约)
DELETE FROM `t_erp_warehouse_type`;
INSERT INTO `t_erp_warehouse_type` (`id`, `shopid`, `issystem`, `name`, `remark`, `operator`, `opttime`) VALUES
	('fba', NULL, b'1', 'FBA', '亚马逊仓库(fulfillment by amazon)', NULL, '2017-05-06 08:45:29'),
	('oversea', NULL, b'1', '海外仓', NULL, NULL, NULL),
	('oversea_test', NULL, b'1', '测试仓', NULL, NULL, NULL),
	('oversea_unusable', NULL, b'1', '废品仓', NULL, NULL, NULL),
	('oversea_usable', NULL, b'1', '正品仓', NULL, NULL, NULL),
	('self', NULL, b'1', '自有仓', '公司自己所有仓库', NULL, NULL),
	('self_test', NULL, b'1', '测试验收仓', '', NULL, '2017-05-11 09:06:45'),
	('self_unusable', NULL, b'1', '废品仓', NULL, NULL, NULL),
	('self_usable', NULL, b'1', '正品仓', NULL, NULL, NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

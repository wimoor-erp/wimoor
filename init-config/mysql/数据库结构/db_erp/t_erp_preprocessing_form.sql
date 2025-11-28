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

-- 导出  表 db_erp.t_erp_preprocessing_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_preprocessing_form` (
  `id` bigint unsigned NOT NULL COMMENT 'ID 唯一键',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `warehouseid` bigint unsigned DEFAULT NULL,
  `number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ftype` tinyint unsigned DEFAULT NULL COMMENT '类型0代表成品，1代表组装成品',
  `is_check_inv_time` datetime DEFAULT NULL COMMENT '是否下架',
  `is_out_consumable_time` datetime DEFAULT NULL COMMENT '是否耗材出库',
  `is_dispatch_time` datetime DEFAULT NULL COMMENT '是否调库',
  `is_down_time` datetime DEFAULT NULL COMMENT '是否下载配货单',
  `is_assembly_time` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL COMMENT '创建人',
  `isrun` bit(1) DEFAULT b'0' COMMENT '是否完成计划',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

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

-- 导出  表 db_erp.t_erp_warehouse_shelf_inventory_opt_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf_inventory_opt_record` (
  `id` bigint unsigned NOT NULL COMMENT 'ID',
  `shelfid` bigint unsigned DEFAULT NULL COMMENT '货柜ID',
  `materialid` bigint unsigned DEFAULT NULL COMMENT '产品ID',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID',
  `warehouseid` bigint unsigned DEFAULT NULL,
  `quantity` int unsigned DEFAULT NULL COMMENT '操作数量',
  `size` float unsigned DEFAULT NULL COMMENT '操作数量对于的体积使用立方厘米cm3',
  `balance_qty` int unsigned DEFAULT NULL COMMENT '操作后结余数量',
  `balance_size` float unsigned DEFAULT NULL COMMENT '操作后结余体积',
  `opt` int unsigned DEFAULT NULL COMMENT '0：出库；1：入库;2：修正下架；3：修正上架',
  `formid` bigint unsigned DEFAULT NULL COMMENT '表单ID',
  `formtype` char(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '表单类型',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `shelfid_materialid_shopid_formid_formtype` (`shopid`,`shelfid`,`materialid`,`warehouseid`),
  KEY `formid_formtype` (`formid`,`formtype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='操作记录';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

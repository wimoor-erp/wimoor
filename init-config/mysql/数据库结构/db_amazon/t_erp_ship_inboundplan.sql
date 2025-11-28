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

-- 导出  表 db_amazon.t_erp_ship_inboundplan 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundplan` (
  `id` bigint unsigned NOT NULL,
  `name` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `number` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipFromAddressID` bigint unsigned DEFAULT NULL COMMENT '	您的退货地址。(发货地址)',
  `skunum` int DEFAULT NULL,
  `labelPrepType` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '	入库货件所需的标签准备类型。',
  `AreCasesRequired` bit(1) DEFAULT NULL COMMENT '指明入库货件是否包含原厂包装发货商品。注： 货件所含的商品必须全部是原厂包装发货或者混装发货。',
  `amazongroupid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `auditstatus` tinyint DEFAULT NULL COMMENT '1 已提交（待审核）；  3,已确认货件；   2已退回货件',
  `invtype` tinyint DEFAULT '0',
  `plansubid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `warehouseid` (`warehouseid`),
  KEY `marketplaceid_warehouseid_shopid` (`shopid`,`marketplaceid`),
  KEY `Index 3` (`createdate`),
  KEY `idx_amazongroupid_marketplaceid_shopid` (`amazongroupid`,`marketplaceid`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

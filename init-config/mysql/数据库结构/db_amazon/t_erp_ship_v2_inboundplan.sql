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

-- 导出  表 db_amazon.t_erp_ship_v2_inboundplan 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_v2_inboundplan` (
  `id` bigint unsigned NOT NULL,
  `inbound_pland_id` char(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `number` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `placement_option_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `source_address` bigint unsigned DEFAULT NULL COMMENT '	您的退货地址。(发货地址)',
  `groupid` bigint unsigned DEFAULT NULL,
  `amazonauthid` bigint unsigned DEFAULT NULL,
  `marketplaceid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `warehouseid` bigint unsigned DEFAULT NULL,
  `auditor` bigint unsigned DEFAULT NULL,
  `auditstatus` tinyint DEFAULT NULL COMMENT '1 已提交（待审核）； 2待配货 3,待装箱；4待发货；  7已发货； 11已驳回； 12已取消',
  `invstatus` tinyint DEFAULT NULL COMMENT '0 未扣库存；1锁定待出库库存；2 已经出库',
  `auditime` datetime DEFAULT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `shipments` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '货件ids',
  `pre_shipping_date` date DEFAULT NULL COMMENT '预计出库时间',
  `shipping_date` date DEFAULT NULL,
  `shipping_solution` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'SEND 亚马逊合作承运商:AMAZON_PARTNERED_CARRIER; 非合作承运商:USE_YOUR_OWN_CARRIER',
  `transtyle` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'SP:小包裹快递(SPD),LTL:托盘配送,NONE',
  `transtype` bigint unsigned DEFAULT NULL COMMENT '运输方式',
  `invtype` tinyint DEFAULT '0' COMMENT '0:代表正常库存流程，1：代表不扣库存，2代表同步过来的计划直接扣可用库存,5没有同步',
  `batchnumber` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `transportation_token` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `check_inv` bigint unsigned DEFAULT NULL,
  `submitbox` bit(1) DEFAULT b'0',
  `areCasesRequired` bit(1) DEFAULT b'0',
  `createtime` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`number`,`shopid`),
  KEY `warehouseid` (`warehouseid`),
  KEY `marketplaceid_warehouseid_shopid` (`shopid`,`marketplaceid`),
  KEY `Index 3` (`shopid`,`createtime`),
  KEY `amazonauthid` (`amazonauthid`),
  KEY `idx_amazongroupid_marketplaceid_shopid` (`groupid`,`marketplaceid`,`shopid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

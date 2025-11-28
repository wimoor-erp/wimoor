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

-- 导出  表 db_erp.t_erp_material_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_his` (
  `id` bigint unsigned NOT NULL COMMENT 'ID(h)',
  `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'SKU',
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '公司ID(h)',
  `upc` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '条码',
  `brand` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '品牌',
  `image` bigint unsigned DEFAULT NULL COMMENT '图片',
  `itemDimensions` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品尺寸',
  `pkgDimensions` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '带包装尺寸',
  `boxDimensions` bigint unsigned DEFAULT NULL,
  `boxnum` int unsigned DEFAULT NULL,
  `specification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '规格',
  `supplier` bigint unsigned DEFAULT NULL COMMENT '供应商',
  `productCode` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '供应商产品代码',
  `delivery_cycle` int DEFAULT NULL,
  `other_cost` decimal(10,2) DEFAULT NULL,
  `MOQ` int DEFAULT '0' COMMENT '起订量：minimum order quantity',
  `purchaseUrl` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '采购链接',
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `categoryid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类型id',
  `issfg` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0:单独成品；1：组装成品；2：半成品',
  `color` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0',
  `owner` bigint unsigned DEFAULT '0',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `price` decimal(10,4) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `creator` bigint unsigned DEFAULT NULL,
  `parentid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用于导入数据是引用的系统内的那个SKU产品',
  `effectivedate` datetime DEFAULT NULL,
  `isSmlAndLight` bit(1) DEFAULT b'0' COMMENT '是否轻小产品',
  `assembly_time` int DEFAULT NULL,
  PRIMARY KEY (`id`,`opttime`),
  UNIQUE KEY `Index 3` (`shopid`,`sku`,`opttime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='t_erp_material_his历史表';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

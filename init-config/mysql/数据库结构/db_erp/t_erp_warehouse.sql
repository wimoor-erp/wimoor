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

-- 导出  表 db_erp.t_erp_warehouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse` (
  `id` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'ID',
  `name` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `ftype` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类型',
  `flevel` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '级别',
  `number` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '编号',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `findex` int DEFAULT NULL COMMENT '次序',
  `country` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `groupid` bigint unsigned DEFAULT NULL,
  `fbawareid` bigint unsigned DEFAULT NULL COMMENT '海外仓',
  `isdefault` bit(1) DEFAULT b'0' COMMENT '默认仓库',
  `shopid` bigint unsigned NOT NULL COMMENT '店铺',
  `parentid` bigint unsigned DEFAULT NULL COMMENT '父节点',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `addressid` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  `stocking_cycle` int DEFAULT '0',
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  `isstocktaking` bit(1) DEFAULT b'0',
  `min_cycle` int DEFAULT '0',
  `first_leg_charges` decimal(12,2) DEFAULT NULL,
  `oldid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ishungry` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_ftype_shopid` (`name`,`ftype`,`shopid`) USING BTREE,
  KEY `ftype` (`ftype`),
  KEY `Index 2` (`parentid`),
  KEY `name_shopid` (`name`,`shopid`),
  KEY `shopid` (`shopid`),
  KEY `addressid` (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

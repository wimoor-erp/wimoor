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

-- 导出  表 db_erp.t_erp_warehouse_shelf 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf` (
  `id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '货柜ID',
  `addressid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '仓库ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '货柜名称',
  `number` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '编码',
  `capacity` float NOT NULL DEFAULT '0' COMMENT '容量(立方厘米)',
  `length` float unsigned NOT NULL DEFAULT '0' COMMENT '长度(cm)',
  `width` float unsigned NOT NULL DEFAULT '0' COMMENT '宽度(cm)',
  `height` float unsigned NOT NULL DEFAULT '0' COMMENT '高度(cm)',
  `parentid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '父货柜ID',
  `sort` int unsigned NOT NULL DEFAULT '0' COMMENT '排序即（柜子所在位置）',
  `treepath` char(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '所有付货柜编码如：A01!033!F01',
  `shopid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '公司ID',
  `iswarn` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否报警',
  `isdelete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否逻辑删除',
  `isfrozen` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否冻结',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creator` bigint unsigned DEFAULT NULL COMMENT '创建人',
  `creattime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `treepath` (`shopid`,`addressid`,`treepath`(191)),
  KEY `parentid` (`parentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='仓库货柜';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

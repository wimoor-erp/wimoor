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

-- 导出  表 db_erp.t_erp_v2_plan_purchase_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned NOT NULL COMMENT '订单ID',
  `materialid` bigint unsigned NOT NULL COMMENT '本地产品ID',
  `warehouse` char(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '仓库',
  `sumreq` int NOT NULL DEFAULT '0' COMMENT '需求量',
  `salemonth` int NOT NULL DEFAULT '0' COMMENT '月销量',
  `presalemonth` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '销售预测',
  `moreqty` char(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '多余库存',
  `suggest` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '月度建议采购量',
  `needqty` int NOT NULL DEFAULT '0' COMMENT '需求量',
  `sugpurchase` int NOT NULL DEFAULT '0' COMMENT '建议采购量',
  `planpurchase` int NOT NULL DEFAULT '0' COMMENT '计划采购量',
  `detail` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '当时计算的需求详情与提货详情',
  `operator` bigint unsigned NOT NULL COMMENT '操作人',
  `creator` bigint unsigned NOT NULL COMMENT '创建人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

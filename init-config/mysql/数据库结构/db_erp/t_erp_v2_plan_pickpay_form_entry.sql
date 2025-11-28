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

-- 导出  表 db_erp.t_erp_v2_plan_pickpay_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_pickpay_form_entry` (
  `id` bigint unsigned NOT NULL,
  `formid` bigint unsigned NOT NULL COMMENT '订单ID',
  `materialid` bigint unsigned NOT NULL COMMENT '产品ID',
  `auditor` bigint unsigned NOT NULL COMMENT '审核人',
  `auditstatus` int unsigned NOT NULL DEFAULT '0' COMMENT '审核状态',
  `audittime` datetime NOT NULL COMMENT '审核时间',
  `supplier` bigint unsigned NOT NULL DEFAULT '0' COMMENT '供应商',
  `inbound` int NOT NULL DEFAULT '0' COMMENT '待入库数量',
  `suggest` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '建议提货量',
  `planpick` int NOT NULL DEFAULT '0' COMMENT '建议付款金额',
  `planpay` decimal(14,2) NOT NULL DEFAULT '0.00' COMMENT '计划付款金额',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '计划提货量',
  `operator` bigint unsigned NOT NULL COMMENT '操作人',
  `creator` bigint unsigned NOT NULL COMMENT '创建人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='提货付款模块SKU从审核到通过以及历史的具体entry表';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

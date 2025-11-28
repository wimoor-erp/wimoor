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

-- 导出  表 db_finance.fin_voucher_entries 结构
CREATE TABLE IF NOT EXISTS `fin_voucher_entries` (
  `entry_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '分录主键ID',
  `groupid` bigint unsigned NOT NULL COMMENT '租户ID',
  `voucher_id` bigint unsigned NOT NULL COMMENT '关联的凭证ID',
  `entry_no` int NOT NULL COMMENT '分录序号',
  `subject_id` bigint unsigned DEFAULT NULL COMMENT '会计科目ID',
  `debit_amount` decimal(15,2) DEFAULT '0.00' COMMENT '借方金额',
  `credit_amount` decimal(15,2) DEFAULT '0.00' COMMENT '贷方金额',
  `summary` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '摘要说明',
  `auxiliary_type` tinyint DEFAULT NULL COMMENT '辅助核算类型：1-部门，2-员工，3-客户，4-供应商，5-项目',
  `auxiliary_id` bigint DEFAULT NULL COMMENT '辅助核算对象ID',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`entry_id`),
  KEY `idx_voucher_entries_tenant_subject` (`groupid`,`subject_id`) USING BTREE COMMENT '租户分录科目索引',
  KEY `tenant_id` (`groupid`,`voucher_id`) USING BTREE,
  KEY `voucher_id` (`voucher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='凭证分录明细表，存储凭证的每一笔分录信息';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

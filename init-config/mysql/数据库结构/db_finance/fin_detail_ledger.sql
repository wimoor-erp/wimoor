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

-- 导出  表 db_finance.fin_detail_ledger 结构
CREATE TABLE IF NOT EXISTS `fin_detail_ledger` (
  `detail_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '明细账记录ID',
  `groupid` bigint unsigned NOT NULL COMMENT '租户ID',
  `subject_id` bigint unsigned DEFAULT NULL COMMENT '会计科目ID',
  `voucher_id` bigint NOT NULL COMMENT '凭证ID',
  `entry_id` bigint NOT NULL COMMENT '凭证分录ID',
  `voucher_date` date NOT NULL COMMENT '凭证日期',
  `voucher_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '凭证编号',
  `summary` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '摘要',
  `debit_amount` decimal(15,2) DEFAULT '0.00' COMMENT '借方金额',
  `credit_amount` decimal(15,2) DEFAULT '0.00' COMMENT '贷方金额',
  `balance` decimal(15,2) DEFAULT '0.00' COMMENT '当前余额',
  `balance_direction` tinyint DEFAULT NULL COMMENT '余额方向：1-借，2-贷',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`detail_id`),
  KEY `tenant_id_2` (`groupid`,`entry_id`) USING BTREE,
  KEY `idx_detail_ledger_tenant_subject_date` (`groupid`,`subject_id`,`voucher_date`) USING BTREE COMMENT '租户明细账科目日期索引'
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='明细账表，记录每个科目的明细发生情况';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

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

-- 导出  表 db_finance.fin_vouchers 结构
CREATE TABLE IF NOT EXISTS `fin_vouchers` (
  `voucher_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '凭证主键ID',
  `groupid` bigint unsigned NOT NULL COMMENT '租户ID',
  `voucher_type` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `voucher_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '凭证编号，如记-001',
  `voucher_date` date NOT NULL COMMENT '凭证日期',
  `attachment_count` int DEFAULT '0' COMMENT '附件数量',
  `total_amount` decimal(15,2) NOT NULL COMMENT '凭证总金额',
  `voucher_status` tinyint DEFAULT '1' COMMENT '凭证状态：0-草稿，1-待审核，2-已审核，3-已过账，4-已作废',
  `preparer_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '制单人',
  `auditor_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '审核人',
  `post_time` datetime DEFAULT NULL COMMENT '过账时间',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`voucher_id`),
  UNIQUE KEY `uk_tenant_voucher_no` (`groupid`,`voucher_no`) USING BTREE,
  KEY `idx_vouchers_tenant_date` (`groupid`,`voucher_date`) USING BTREE COMMENT '租户凭证日期索引',
  KEY `idx_vouchers_tenant_status` (`groupid`,`voucher_status`) USING BTREE COMMENT '租户凭证状态索引'
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='记账凭证主表，存储凭证的基本信息';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

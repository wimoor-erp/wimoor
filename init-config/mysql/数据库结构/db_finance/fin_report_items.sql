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

-- 导出  表 db_finance.fin_report_items 结构
CREATE TABLE IF NOT EXISTS `fin_report_items` (
  `item_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `groupid` bigint unsigned NOT NULL COMMENT '租户ID',
  `template_id` bigint unsigned NOT NULL COMMENT '报表模板ID',
  `item_code` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '项目编码',
  `item_name` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '项目名称',
  `parent_code` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父级项目编码',
  `item_level` tinyint NOT NULL COMMENT '项目级别：1-一级项目，2-二级项目，3-三级项目，4-四级项目',
  `line_number` int DEFAULT NULL COMMENT '行次',
  `item_type` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '项目类型：ASSET-资产, LIABILITY-负债, EQUITY-权益, INCOME-收入, COST-成本, EXPENSE-费用',
  `item_category` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '项目分类',
  `calculation_rule` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '计算规则',
  `formula_type` varchar(20) COLLATE utf8mb4_bin DEFAULT 'DIRECT' COMMENT '公式类型：DIRECT-直接取值, FORMULA-公式计算, CUSTOM-自定义, CALCULATED-自动计算',
  `formula_content` text COLLATE utf8mb4_bin COMMENT '公式内容',
  `data_source` varchar(50) COLLATE utf8mb4_bin DEFAULT 'SUBJECT' COMMENT '数据来源：SUBJECT-会计科目, CUSTOM-自定义, CALCULATED-计算, CONSTANT-常量',
  `subject_codes` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '关联科目编码（多个用逗号分隔）',
  `amount_type` varchar(20) COLLATE utf8mb4_bin DEFAULT 'END_BALANCE' COMMENT '金额类型：END_BALANCE-期末余额, BEGIN_BALANCE-期初余额, DEBIT_TOTAL-借方发生额, CREDIT_TOTAL-贷方发生额',
  `direction` tinyint DEFAULT NULL COMMENT '金额方向：1-正数, -1-负数, NULL-自动判断',
  `display_format` varchar(50) COLLATE utf8mb4_bin DEFAULT 'NORMAL' COMMENT '显示格式：NORMAL-正常, BOLD-加粗, ITALIC-斜体, UNDERLINE-下划线',
  `is_show_zero` tinyint DEFAULT '1' COMMENT '是否显示零值：0-不显示, 1-显示',
  `is_show` tinyint DEFAULT '1' COMMENT '是否显示：0-不显示, 1-显示',
  `is_leaf` tinyint DEFAULT '1' COMMENT '是否末级项目：0-否, 1-是',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-停用, 1-启用',
  `description` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '项目描述',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`item_id`),
  UNIQUE KEY `uk_tenant_template_item_code` (`groupid`,`template_id`,`item_code`) USING BTREE,
  KEY `idx_template_id` (`template_id`),
  KEY `idx_parent_code` (`parent_code`),
  KEY `idx_item_level` (`item_level`),
  KEY `idx_line_number` (`line_number`),
  KEY `idx_item_type` (`item_type`),
  KEY `idx_item_category` (`item_category`),
  KEY `idx_formula_type` (`formula_type`),
  KEY `idx_data_source` (`data_source`),
  KEY `idx_sort_order` (`sort_order`),
  KEY `idx_status` (`status`),
  KEY `idx_tenant_id` (`groupid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5584 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='报表项目表';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

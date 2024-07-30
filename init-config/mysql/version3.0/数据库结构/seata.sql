-- --------------------------------------------------------
-- 主机:                           rm-wz903sa454i2h35ik6o.mysql.rds.aliyuncs.com
-- 服务器版本:                        5.7.28-log - Source distribution
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 导出  表 seata.branch_table 结构
CREATE TABLE IF NOT EXISTS `branch_table` (
  `branch_id` bigint(20) unsigned NOT NULL,
  `xid` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `transaction_id` bigint(20) unsigned DEFAULT NULL,
  `resource_group_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  `resource_id` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `branch_type` varchar(8) COLLATE utf8mb4_bin DEFAULT NULL,
  `status` tinyint(3) unsigned DEFAULT NULL,
  `client_id` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL,
  `application_data` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`branch_id`),
  KEY `idx_xid` (`xid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 seata.distributed_lock 结构
CREATE TABLE IF NOT EXISTS `distributed_lock` (
  `lock_key` char(20) COLLATE utf8mb4_bin NOT NULL,
  `lock_value` varchar(20) COLLATE utf8mb4_bin NOT NULL,
  `expire` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`lock_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 seata.global_table 结构
CREATE TABLE IF NOT EXISTS `global_table` (
  `xid` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `transaction_id` bigint(20) unsigned DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  `application_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  `transaction_service_group` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  `transaction_name` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL,
  `timeout` int(11) DEFAULT NULL,
  `begin_time` bigint(20) DEFAULT NULL,
  `application_data` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`xid`),
  KEY `idx_status_gmt_modified` (`status`,`gmt_modified`),
  KEY `idx_transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 seata.lock_table 结构
CREATE TABLE IF NOT EXISTS `lock_table` (
  `row_key` char(190) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `xid` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL,
  `transaction_id` bigint(20) unsigned DEFAULT NULL,
  `branch_id` bigint(20) unsigned NOT NULL,
  `resource_id` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `table_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `pk` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:locked ,1:rollbacking',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`row_key`),
  KEY `idx_status` (`status`),
  KEY `idx_branch_id` (`branch_id`),
  KEY `idx_xid` (`xid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

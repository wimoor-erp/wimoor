
-- 导出 db_quartz 的数据库结构
CREATE DATABASE IF NOT EXISTS `db_quartz` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */;
USE `db_quartz`;

-- 导出  表 db_quartz.qrtz_blob_triggers 结构
CREATE TABLE IF NOT EXISTS `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_NAME` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quartz.qrtz_calendars 结构
CREATE TABLE IF NOT EXISTS `qrtz_calendars` (
  `SCHED_NAME` varchar(120) COLLATE utf8mb4_bin NOT NULL,
  `CALENDAR_NAME` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quartz.qrtz_cron_triggers 结构
CREATE TABLE IF NOT EXISTS `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_NAME` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `CRON_EXPRESSION` varchar(200) COLLATE utf8mb4_bin NOT NULL,
  `TIME_ZONE_ID` varchar(80) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quartz.qrtz_fired_triggers 结构
CREATE TABLE IF NOT EXISTS `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) COLLATE utf8mb4_bin NOT NULL,
  `ENTRY_ID` varchar(95) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_NAME` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `INSTANCE_NAME` varchar(200) COLLATE utf8mb4_bin NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `JOB_NAME` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `JOB_GROUP` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quartz.qrtz_job_details 结构
CREATE TABLE IF NOT EXISTS `qrtz_job_details` (
  `SCHED_NAME` varchar(120) COLLATE utf8mb4_bin NOT NULL,
  `JOB_NAME` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `JOB_GROUP` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `DESCRIPTION` varchar(250) COLLATE utf8mb4_bin DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) COLLATE utf8mb4_bin NOT NULL,
  `IS_DURABLE` varchar(1) COLLATE utf8mb4_bin NOT NULL,
  `IS_NONCONCURRENT` varchar(1) COLLATE utf8mb4_bin NOT NULL,
  `IS_UPDATE_DATA` varchar(1) COLLATE utf8mb4_bin NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) COLLATE utf8mb4_bin NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quartz.qrtz_locks 结构
CREATE TABLE IF NOT EXISTS `qrtz_locks` (
  `SCHED_NAME` varchar(120) COLLATE utf8mb4_bin NOT NULL,
  `LOCK_NAME` varchar(40) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quartz.qrtz_paused_trigger_grps 结构
CREATE TABLE IF NOT EXISTS `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quartz.qrtz_scheduler_state 结构
CREATE TABLE IF NOT EXISTS `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) COLLATE utf8mb4_bin NOT NULL,
  `INSTANCE_NAME` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quartz.qrtz_simple_triggers 结构
CREATE TABLE IF NOT EXISTS `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_NAME` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quartz.qrtz_simprop_triggers 结构
CREATE TABLE IF NOT EXISTS `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_NAME` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `STR_PROP_1` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL,
  `STR_PROP_2` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL,
  `STR_PROP_3` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_quartz.qrtz_triggers 结构
CREATE TABLE IF NOT EXISTS `qrtz_triggers` (
  `SCHED_NAME` varchar(120) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_NAME` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `JOB_NAME` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `JOB_GROUP` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `DESCRIPTION` varchar(250) COLLATE utf8mb4_bin DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_TYPE` varchar(8) COLLATE utf8mb4_bin NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。


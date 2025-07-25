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

-- 导出  表 db_erp.t_erp_thirdparty_system 结构
CREATE TABLE IF NOT EXISTS `t_erp_thirdparty_system` (
  `id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `cname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `support` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `apidoc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `classz` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `needkey` bit(1) DEFAULT NULL COMMENT '使用appkey和appsecret',
  `needtoken` bit(1) DEFAULT NULL COMMENT '使用token',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 正在导出表  db_erp.t_erp_thirdparty_system 的数据：~6 rows (大约)
DELETE FROM `t_erp_thirdparty_system`;
INSERT INTO `t_erp_thirdparty_system` (`id`, `name`, `cname`, `support`, `url`, `apidoc`, `classz`, `needkey`, `needtoken`) VALUES
	('K5', 'K5物流软件', '易宇通科技', 'warehouse', 'https://kingtrans.net/', 'http://api.kingtrans.net/', 'warehouseK5Service', b'1', b'0'),
	('OPS', 'AU-OPS', ' 深圳市乐代网络科技有限公司 ', 'warehouse', 'https://au-ops.com/', 'https://thunder-us.ai-ops.vip/?open_in_browser=true#/docs', 'warehouseOPSService', b'1', b'0'),
	('XL', '领星-语雀', '深圳市领星网络科技有限公司 ', 'warehouse', 'https://www.lingxing.com/', 'https://www.yuque.com/u25029679/kb/oziyoaoh7r0bco3p?singleDoc=#01e44802', 'warehouseXLSService', b'1', b'0'),
	('YC', '轶仓-仓储系统', '义达跨境(上海)物流股份有限公司', 'warehouse', 'http://test.yclhwc.com', 'http://test.yclhwc.com/api_document.aspx', 'warehouseYCService', b'0', b'1'),
	('ZH', '新智慧', '深圳新智慧物流科技有限公司', 'logistics', 'http://nextsls.net/', NULL, 'shipTransCompanyZhihuiService', b'0', b'1'),
	('ZM', 'V5物流云平台', '深圳市哲盟软件开发有限公司', 'logistics', 'http://www.intelink.net/', NULL, 'shipTransCompanyZMService', b'0', b'0');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

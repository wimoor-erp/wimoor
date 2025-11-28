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

-- 导出  表 db_erp.t_erp_ship_transcompany_api 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transcompany_api` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `api` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0',
  `name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0',
  `openaccount` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0',
  `openkey` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0',
  `appkey` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0',
  `appsecret` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0',
  `token` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0',
  `shopid` bigint unsigned NOT NULL DEFAULT '0',
  `operator` bigint unsigned NOT NULL DEFAULT '0',
  `opttime` datetime DEFAULT NULL,
  `url` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `system` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 正在导出表  db_erp.t_erp_ship_transcompany_api 的数据：~7 rows (大约)
DELETE FROM `t_erp_ship_transcompany_api`;
INSERT INTO `t_erp_ship_transcompany_api` (`id`, `api`, `name`, `openaccount`, `openkey`, `appkey`, `appsecret`, `token`, `shopid`, `operator`, `opttime`, `url`, `system`) VALUES
	(1, 'http://yidakj.nextsls.com/api/v4/shipment', '深圳市普拉达国际货运代理有限公司', '0', '0', '0', '0', '0', 0, 0, '2012-12-12 01:01:01', 'https://erp.wimoor.com/page.do?oldlocation=appstore/appmain.do&oldparentid=undefined&parentid=undefined&breadcrumb=%E6%9C%8D%E5%8A%A1%E8%AF%A6%E6%83%85&location=%2Fappstore%2FviewDetail.do%3Fid%3D9', 'ZH'),
	(2, 'http://yysexp.nextsls.com/api/v4/shipment', '深圳市由由顺供应链有限公司', '0', '0', '0', '0', '0', 0, 0, '2012-12-12 01:01:01', 'https://aiqicha.baidu.com/company_detail_29349115532953?rq=ef&pd=ee&from=ps', 'ZH'),
	(3, 'http://gdhjex.nextsls.com/api/v4/shipment', '深圳市鸿捷国际货运代理有限公司', '0', '0', '0', '0', '0', 0, 0, '2012-12-12 01:01:01', 'http://www.hongjie56.cn/', 'ZH'),
	(4, 'http://119.23.174.71:8020', '深圳市环球易派供应链服务有限公司', 'TKWDZ8907_YHECN', '9538227e1d644b33a42cd01bd4389b81', '0', '0', '0', 0, 0, '2012-12-12 01:01:01', 'http://www.360ees.com/', 'ZM'),
	(5, 'http://customer2.ydhex.com/webservice/PublicService.asmx/ServiceInterfaceUTF8', '深圳市义达跨境物流有限公司', '0', '0', '0', '0', '0', 0, 0, '2012-12-12 01:01:01', 'https://www.ydhex.com/', 'YD'),
	(6, 'https://hongchen.kingtrans.cn', '深圳市鸿宸联运国际货运代理有限公司', 'A8719', 'aG9uZ2NoZW4=3a8G08g3Ro9y97KcGg8l', '0', '0', '0', 0, 0, '2012-12-12 01:01:01', 'https://www.hongchen-union.com.cn/', 'K5'),
	(7, 'https://api.bojun.net/api/TransOneOrder', '深圳市华宇海外仓', '0', '0', '0', '0', '0', 0, 0, '2012-12-12 01:01:01', 'https://www.ydhex.com/', 'HY');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

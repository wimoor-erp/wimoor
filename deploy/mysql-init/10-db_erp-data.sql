USE db_erp;
REPLACE INTO `t_erp_fin_project` (`id`, `name`, `issys`, `shopid`, `createdate`, `opttime`, `operator`, `creator`, `oldid`) VALUES (26138972997300238, '运费', b'1', NULL, '2018-03-20 16:41:59', '2018-03-20 16:42:00', 26138972975759237, 26138972975759237, '42320187-2c1a-11e8-a985-00e04c023f0e');
REPLACE INTO `t_erp_fin_project` (`id`, `name`, `issys`, `shopid`, `createdate`, `opttime`, `operator`, `creator`, `oldid`) VALUES (26138972997300240, '货物费用', b'1', NULL, '2018-03-20 16:39:31', '2018-03-20 16:39:33', 26138972975759237, 26138972975759237, 'fae28a04-2c19-11e8-a985-00e04c023f0e');
REPLACE INTO `t_erp_formtype` (`id`, `name`, `remark`) VALUES ('assembly', '加工', '组装单-含准备状态的出库入库');
REPLACE INTO `t_erp_formtype` (`id`, `name`, `remark`) VALUES ('change', '换货', '换货单-含准备状态的出库入库');
REPLACE INTO `t_erp_formtype` (`id`, `name`, `remark`) VALUES ('dispatch', '调库', '调库单-直接出库直接入库');
REPLACE INTO `t_erp_formtype` (`id`, `name`, `remark`) VALUES ('dispatch-inner', '代料', '代料单');
REPLACE INTO `t_erp_formtype` (`id`, `name`, `remark`) VALUES ('otherin', '其它入库', '其他入库单-直接入库');
REPLACE INTO `t_erp_formtype` (`id`, `name`, `remark`) VALUES ('otherout', '其它出库', '其它出库单-直接出库');
REPLACE INTO `t_erp_formtype` (`id`, `name`, `remark`) VALUES ('outstockform', '发货出库', '亚马逊发货');
REPLACE INTO `t_erp_formtype` (`id`, `name`, `remark`) VALUES ('purchase', '采购', '采购单-含准备状态的出库入库');
REPLACE INTO `t_erp_formtype` (`id`, `name`, `remark`) VALUES ('stocktaking', '盘点', '盘点');
REPLACE INTO `t_erp_purchase_form_payment_method` (`id`, `name`, `opttime`, `operator`, `createtime`, `creator`) VALUES (1, '银行卡', '2020-07-15 15:42:41', NULL, NULL, NULL);
REPLACE INTO `t_erp_purchase_form_payment_method` (`id`, `name`, `opttime`, `operator`, `createtime`, `creator`) VALUES (2, '支付宝', '2020-07-15 15:42:39', NULL, NULL, NULL);
REPLACE INTO `t_erp_purchase_form_payment_method` (`id`, `name`, `opttime`, `operator`, `createtime`, `creator`) VALUES (3, '信用卡', '2020-07-15 15:42:40', NULL, NULL, NULL);
REPLACE INTO `t_erp_purchase_form_payment_method` (`id`, `name`, `opttime`, `operator`, `createtime`, `creator`) VALUES (4, '微信支付', '2020-07-17 14:21:30', NULL, NULL, NULL);
REPLACE INTO `t_erp_purchase_form_payment_method` (`id`, `name`, `opttime`, `operator`, `createtime`, `creator`) VALUES (5, '跨境宝', '2020-07-15 15:42:41', NULL, NULL, NULL);
REPLACE INTO `t_erp_purchase_form_payment_method` (`id`, `name`, `opttime`, `operator`, `createtime`, `creator`) VALUES (6, '账期(1688)', '2020-07-15 15:42:41', NULL, NULL, NULL);
REPLACE INTO `t_erp_purchase_form_payment_method` (`id`, `name`, `opttime`, `operator`, `createtime`, `creator`) VALUES (7, '诚e赊(1688)', '2020-07-15 15:42:41', NULL, NULL, NULL);
REPLACE INTO `t_erp_purchase_form_payment_method` (`id`, `name`, `opttime`, `operator`, `createtime`, `creator`) VALUES (8, '其它', '2020-07-15 15:42:39', NULL, NULL, NULL);
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

-- 导出  表 db_erp.t_erp_transtype 结构
CREATE TABLE IF NOT EXISTS `t_erp_transtype` (
  `id` bigint unsigned NOT NULL,
  `shopid` bigint unsigned DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `operator` bigint unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 正在导出表  db_erp.t_erp_transtype 的数据：~44 rows (大约)
DELETE FROM `t_erp_transtype`;
INSERT INTO `t_erp_transtype` (`id`, `shopid`, `name`, `disable`, `operator`, `opttime`) VALUES
	(1, NULL, '海运', b'0', NULL, '2021-12-02 14:31:56'),
	(2, NULL, '空运', b'0', NULL, '2021-12-02 14:31:56'),
	(3, NULL, '铁路', b'0', NULL, '2021-12-02 14:31:56'),
	(1111111, 4638381735602516432, '海运卡派', b'0', 1, '2021-12-02 14:31:56'),
	(1111112, 17392024503496557671, '海运卡派', b'0', 1, '2021-12-02 14:31:56'),
	(1111113, 4638381735798671011, '海运卡派', b'0', 1, '2021-12-02 14:31:56'),
	(1111114, 11988530289583977004, '海运卡派', b'0', 1, '2021-12-02 14:31:56'),
	(1610890409540059138, 17392024503428119746, 'ceshi', b'1', 17392024503428119745, '2023-01-05 14:46:17'),
	(8603491516282567265, 1803969078644232193, '汽车', b'0', 1803969078379991042, '2024-06-28 13:17:23'),
	(10620937817935801867, 17392024503488201278, '红单', b'0', 1702581684146307073, '2023-12-08 17:27:54'),
	(11558423911642996330, 17392024503443470467, 't', b'1', 1805163780127105025, '2025-05-20 11:51:30'),
	(11558423911643473821, 17392024503443470467, '限时达海运', b'0', 1805163780127105025, '2025-05-20 16:01:51'),
	(11558423911662904445, 26138972975530085, '限时达海运', b'0', 26138972975759264, '2025-05-23 17:16:03'),
	(11558423911663055387, 26138972975530085, 'COSCO快线', b'0', 26138972975759264, '2025-05-23 17:56:15'),
	(11558423911668435760, 17392024503549470602, '限时达', b'0', 1701855587733606401, '2025-05-24 09:55:27'),
	(11988530289661444047, 26138972975530085, '卡航', b'0', 26138972975759264, '2021-12-15 11:58:09'),
	(11988530289663500143, 17392024503525316892, '卡航', b'0', 17392024503549511791, '2021-12-16 16:04:33'),
	(11988530289663557643, 17392024503549470602, '卡航', b'0', 17392024503576833816, '2021-12-16 17:48:11'),
	(11988530289670716846, 4638381735798671011, '快递', b'0', 8817926179639323103, '2021-12-20 09:50:26'),
	(11988530289670925477, 17392024503496557671, '汽运', b'0', 11988530289437365624, '2021-12-20 15:56:31'),
	(11988530289674023944, 26138972975530085, '陆运', b'0', 26138972975759191, '2021-12-22 11:29:48'),
	(11988530289675486510, 26138972975530071, '美森限时达', b'0', 4638381735583820738, '2022-03-23 21:14:21'),
	(11988530289675829461, 17392024503443470467, '卡航', b'0', 17392024503477878062, '2021-12-23 17:02:11'),
	(11988530289685903889, 26138972975530085, '海卡', b'0', 26138972975759191, '2021-12-30 11:41:20'),
	(11988530289703132820, 17392024503488201278, '卡航', b'0', 17392024503488758142, '2022-01-06 14:38:45'),
	(11988530289809212618, 26138972975530071, '陆运', b'0', 4638381735583820738, '2022-03-23 21:14:21'),
	(11988530289997822567, 11988530289886250444, '卡航', b'0', 11988530289893300344, '2022-08-19 14:30:32'),
	(11989481306727072510, 26138972975530085, '海运卡派', b'0', 1, '2021-12-02 14:31:56'),
	(11989481306727072511, 26138972975530071, '海运卡派', b'0', 4638381735583820738, '2021-12-02 14:31:56'),
	(11989481306727072512, 17392024503549470602, '海运卡派', b'0', 1, '2021-12-02 14:31:56'),
	(11989481306727072513, 17392024503443470467, '海运卡派', b'1', 1, '2021-12-02 14:31:56'),
	(11989481306727072514, 17392024503525316892, '海运卡派', b'0', 1, '2021-12-02 14:31:56'),
	(11989481306727072515, 11988530289886250444, '海运卡派', b'0', 1, '2021-12-02 14:31:56'),
	(11989481306727072516, 11989481306704589577, '海运卡派', b'0', 1, '2021-12-02 14:31:56'),
	(11989481306727076048, 26138972975530085, '国际快递', b'0', 1, '2021-12-02 14:31:56'),
	(11989481306727076049, 26138972975530071, '国际快递', b'0', 1, '2021-12-02 14:31:56'),
	(11989481306727076050, 17392024503549470602, '国际快递', b'0', 1, '2021-12-02 14:31:56'),
	(11989481306727076051, 17392024503443470467, '国际快递', b'0', 1, '2021-12-02 14:31:56'),
	(11989481306727076052, 17392024503525316892, '国际快递', b'0', 1, '2021-12-02 14:31:56'),
	(11989481306727076053, 11988530289886250444, '国际快递', b'0', 1, '2021-12-02 14:31:56'),
	(11989481306727076054, 11989481306704589577, '国际快递', b'0', 1, '2021-12-02 14:31:56'),
	(11989481306994910234, 11989481306994907174, '陆运', b'0', 11989481306994907173, '2023-04-09 10:21:43'),
	(11989481307225798012, 11988530290036957462, '1', b'0', 11988530290036957461, '2023-07-10 16:39:43'),
	(11989481307458014685, 11988530289947210616, '自定义测试', b'0', 11988530289947210615, '2023-09-16 11:51:41');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
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

-- 导出  表 db_erp.t_erp_warehouse_type 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_type` (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'ID',
  `shopid` bigint unsigned DEFAULT NULL COMMENT '店铺',
  `issystem` bit(1) DEFAULT NULL COMMENT '是否系统',
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名字',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `operator` bigint unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 正在导出表  db_erp.t_erp_warehouse_type 的数据：~9 rows (大约)
DELETE FROM `t_erp_warehouse_type`;
INSERT INTO `t_erp_warehouse_type` (`id`, `shopid`, `issystem`, `name`, `remark`, `operator`, `opttime`) VALUES
	('fba', NULL, b'1', 'FBA', '亚马逊仓库(fulfillment by amazon)', NULL, '2017-05-06 08:45:29'),
	('oversea', NULL, b'1', '海外仓', NULL, NULL, NULL),
	('oversea_test', NULL, b'1', '测试仓', NULL, NULL, NULL),
	('oversea_unusable', NULL, b'1', '废品仓', NULL, NULL, NULL),
	('oversea_usable', NULL, b'1', '正品仓', NULL, NULL, NULL),
	('self', NULL, b'1', '自有仓', '公司自己所有仓库', NULL, NULL),
	('self_test', NULL, b'1', '测试验收仓', '', NULL, '2017-05-11 09:06:45'),
	('self_unusable', NULL, b'1', '废品仓', NULL, NULL, NULL),
	('self_usable', NULL, b'1', '正品仓', NULL, NULL, NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
INSERT INTO `t_marketplace` (`marketplaceId`, `market`, `name`, `region_name`, `region`, `end_point`, `point_name`, `accessKey`, `secretKey`, `dim_units`, `weight_units`, `currency`, `findex`, `adv_end_point`, `aws_access_key`, `aws_secret_key`, `associate_tag`, `developer_url`, `dev_account_num`, `bytecode`, `sp_api_endpoint`, `aws_region`) VALUES
	('A13V1IB3VIYZZH', 'FR', '法国', '欧洲', 'EU', '', 'Amazon.fr', '', '', 'cm', 'kg', 'EUR', 5, '', '', '', '', '', '',  'Latin1', '', 'eu-west-1'),
	('A17E79C6D8DWNP', 'SA', '沙特', '欧洲', 'EU', '', 'Amazon.sa', '', '', 'cm', 'kg', 'SAR', 17, NULL, NULL, NULL, NULL, '', '', 'UTF-8', '', 'eu-west-1'),
	('A1805IZSGTT6HS', 'NL', '荷兰', '欧洲', 'EU', '', 'Amazon.nl', '', '', 'cm', 'kg', 'EUR', 16, NULL, NULL, NULL, NULL, '', '', 'Latin1', '', 'eu-west-1'),
	('A19VAU5U5O7RUS', 'SG', '新加坡', '远东', 'SG', '', 'Amazon.sg', '', '', NULL, NULL, 'SGD', 16, NULL, NULL, NULL, NULL, NULL, '', 'UTF-8', '', 'us-west-2'),
	('A1AM78C64UM0Y8', 'MX', '墨西哥', '北美', 'NA', '', 'Amazon.com.mx', '', '', 'cm', 'kg', 'MXN', 12, NULL, NULL, NULL, NULL, '', '', 'Latin1', '', 'us-east-1'),
	('A1C3SOZRARQ6R3', 'PL', '波兰', '欧洲', 'EU', '', 'Amazon.pl', '', '', 'cm', 'kg', 'PLN', 19, NULL, NULL, NULL, NULL, NULL, '', 'UTF-8', '', 'eu-west-1'),
	('A1F83G8C2ARO7P', 'UK', '英国', '欧洲', 'UK', '', 'Amazon.co.uk', '', '', 'cm', 'kg', 'GBP', 3, '', '', '', '', '', '', 'Latin1', '', 'eu-west-1'),
	('A1PA6795UKMFR9', 'DE', '德国', '欧洲', 'EU', '', 'Amazon.de', '', '', 'cm', 'kg', 'EUR', 4, '', '', '', '', '', '', 'Latin1', '', 'eu-west-1'),
	('A1RKKUPIHCS9HS', 'ES', '西班牙', '欧洲', 'EU', '', 'Amazon.es', '', '', 'cm', 'kg', 'EUR', 6, '', '', '', '', '', '', 'Latin1', '', 'eu-west-1'),
	('A1VC38T7YXB528', 'JP', '日本', '远东', 'JP', '', 'Amazon.co.jp', '', '', 'cm', 'kg', 'JPY', 8, '', NULL, NULL, NULL, '', '', 'Shift_JIS', '', 'us-west-2'),
	('A21TJRUUN4KGV', 'IN', '印度', '欧洲', 'IN', '', 'Amazon.in', '', '', 'cm', 'kg', 'INR', 10, '', NULL, NULL, '', '', '', 'UTF-8', '', 'eu-west-1'),
	('A2EUQ1WTGCTBG2', 'CA', '加拿大', '北美', 'NA', '', 'Amazon.ca', '', '', 'cm', 'kg', 'CAD', 2, '', NULL, NULL, NULL, '', '', 'UTF-8', '', 'us-east-1'),
	('A2NODRKZP88ZB9', 'SE', '瑞典', '欧洲', 'EU', '', 'Amazon.se', '', '', 'cm', 'kg', 'SEK', 20, NULL, NULL, NULL, NULL, NULL, '', 'UTF-8', '', 'eu-west-1'),
	('A2Q3Y263D00KWC', 'BR', '巴西', '北美', 'BR', '', 'Amazon.com.br', '', '', NULL, NULL, 'BRL', 13, NULL, NULL, NULL, NULL, NULL, '', 'Latin1', '', 'us-east-1'),
	('A2VIGQ35RCS4UG', 'AE', '阿联酋', '欧洲', 'AE', '', 'Amazon.ae', '', '', 'cm', 'kg', 'AED', 14, NULL, NULL, NULL, NULL, '', '', 'Latin1', '', 'eu-west-1'),
	('A33AVAJ2PDY3EV', 'TR', '土耳其', '欧洲', 'EU', '', 'Amazon.com.tr', '', '', NULL, NULL, 'TRY', 15, NULL, NULL, NULL, NULL, '', '', 'Latin1', '', 'eu-west-1'),
	('A39IBJ37TRP1C6', 'AU', '澳大利亚', '远东', 'AU', '	', 'Amazon.com.au', '', '', 'cm', 'kg', 'AUD', 9, '', NULL, NULL, NULL, '', '3484-6862-2072', 'UTF-8', '', 'us-west-2'),
	('AAHKV2X7AFYLW', 'CN', '中国', '远东', 'CN', '', 'Amazon.cn', NULL, '', NULL, NULL, 'CNY', 11, '', NULL, NULL, NULL, NULL, NULL, 'UTF-8', NULL, NULL),
	('APJ6JRA9NG5V4', 'IT', '意大利', '欧洲', 'EU', '', 'Amazon.it', '', '', 'cm', 'kg', 'EUR', 7, '', '', '', 'kuuqastore-21', '', '', 'Latin1', '', 'eu-west-1'),
	('ARBP9OOSHTCHU', 'EG', '埃及', '欧洲', 'EU', '', 'Egypt.souq.com', '', '', NULL, NULL, 'EUR', 18, NULL, NULL, NULL, NULL, NULL, '', 'UTF-8', '', 'eu-west-1'),
	('ATVPDKIKX0DER', 'US', '美国', '北美', 'NA', '', 'Amazon.com', '', '', 'in', 'lb', 'USD', 1, '', '', '', 'Kuuqa20-01', '', '2623-6518-2224', 'Latin1', '', 'us-east-1');-- --------------------------------------------------------
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

-- 导出  表 db_erp.t_parameterconfig 结构
CREATE TABLE IF NOT EXISTS `t_parameterconfig` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ptype` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `pkey` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sortindex` int DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pkey` (`pkey`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 正在导出表  db_erp.t_parameterconfig 的数据：~29 rows (大约)
DELETE FROM `t_parameterconfig`;
INSERT INTO `t_parameterconfig` (`id`, `ptype`, `pkey`, `value`, `sortindex`, `description`) VALUES
	(1, 'salesChannel', 'amz_fba', '亚马逊订单、亚马逊配送', 1, '销售渠道及发货方式'),
	(2, 'salesChannel', 'amz_byself', '亚马逊订单、卖家自配送', 2, NULL),
	(3, 'salesChannel', 'unamz_fba', '非亚马逊订单、亚马逊FBA配送', 3, NULL),
	(4, 'sellerplan', 'profession', '专业卖家（全球开店）', 1, '卖家销售计划'),
	(5, 'sellerplan', 'person', '个人卖家', 2, NULL),
	(6, 'shipmentstyle', 'manually', '手动输入', 1, '运费计算'),
	(7, 'shipmentstyle', 'weight', '根据产品重量计算', 2, NULL),
	(8, 'shipmentstyle', 'dim_weight', '根据产品重量与材积重量大者计算', 3, NULL),
	(9, 'logistics_us', 'us_std', 'Domestic Standard', 1, '物流方式'),
	(10, 'logistics_us', 'us_exp', 'Domestic Expedited', 2, NULL),
	(11, 'logistics_us', 'us_int', 'International', 3, NULL),
	(12, 'logistics_uk', 'uk_std', 'Standard Delivery UK', 1, NULL),
	(13, 'logistics_uk', 'uk_exp', 'Express Delivery UK', 2, NULL),
	(14, 'logistics_uk', 'uk_eur', 'European Amazon.co.uk Marketplace countries', 3, NULL),
	(15, 'logistics_uk', 'uk_us', 'US (including US protectorates)', 4, NULL),
	(16, 'logistics_uk', 'uk_rest', 'Rest of World Amazon.co.uk Marketplace countries', 5, NULL),
	(17, 'isfba', 'AMAZON', '亚马逊配送', 1, '配送方式,用于产品本身发货方式上的设定（如，使用自己的仓库则是自配送）'),
	(18, 'isfba', 'DEFAULT', '卖家自配送', 2, NULL),
	(19, 'order_status', 'Pending', 'Pending', 1, NULL),
	(20, 'order_status', 'Unshipped', 'Unshipped', 2, NULL),
	(21, 'order_status', 'Shipped', 'Shipped', 3, NULL),
	(22, 'order_status', 'Cancelled', 'Cancelled', 4, NULL),
	(23, 'order_channel', 'Amazon', '亚马逊配送', 1, '订单的配送方式，'),
	(24, 'order_channel', 'Merchant', '卖家自配送', 2, NULL),
	(25, 'is_business_order', 'true', '企业买家', 1, NULL),
	(26, 'is_business_order', 'false', '个人买家', 2, NULL),
	(27, 'customer', 'supplier', '供应商', 1, NULL),
	(28, 'customer', 'purchaser', '采购商', 2, NULL),
	(29, 'shipmentstyle', 'volume', '根据产品体积计算', 4, NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

-- --------------------------------------------------------
-- 主机:                           192.168.0.252
-- 服务器版本:                        8.0.20 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 正在导出表  db_plum.t_sys_dict 的数据：~15 rows (大约)
/*!40000 ALTER TABLE `t_sys_dict` DISABLE KEYS */;
INSERT INTO `t_sys_dict` (`id`, `name`, `code`, `status`, `remark`, `gmt_create`, `gmt_modified`) VALUES
	(1, '性别', 'gender', 1, NULL, '2019-12-06 19:03:32', '2021-02-08 14:58:01'),
	(2, '授权方式', 'grant_type', 1, NULL, '2020-10-17 08:09:50', '2021-01-31 09:48:24'),
	(3, '微服务列表', 'micro_service', 1, NULL, '2021-06-17 00:13:43', '2021-06-17 00:17:22'),
	(4, '请求方式', 'request_method', 1, NULL, '2021-06-17 00:18:07', '2021-06-17 00:18:07'),
	(1575330968132694017, '用户状态', 'user_status', 1, NULL, '2022-09-29 11:45:46', '2022-09-29 11:45:46'),
	(1580461846135635969, '数据权限', 'limit_data_type', 1, NULL, '2022-10-13 15:34:02', '2022-10-13 15:34:02'),
	(1581888127234539522, '销售渠道及发货方式', 'salesChannel', 1, NULL, '2022-10-17 14:01:34', '2022-10-17 14:01:34'),
	(1581888461944193026, '卖家销售计划', 'sellerplan', 1, NULL, '2022-10-17 14:02:54', '2022-10-17 14:02:54'),
	(1581888764215099394, '运费计算', 'shipmentstyle', 1, NULL, '2022-10-17 14:04:06', '2022-10-17 14:04:06'),
	(1581889181447684098, '配送方式', 'isfba', 1, NULL, '2022-10-17 14:05:46', '2022-10-17 14:05:46'),
	(1581889888674447362, '订单状态', 'order_status', 1, NULL, '2022-10-17 14:08:34', '2022-10-17 14:08:34'),
	(1581890253167853570, '卖家类型', 'is_business_order', 1, NULL, '2022-10-17 14:10:01', '2022-10-17 14:10:01'),
	(1581890529090142210, '客户类型', 'customer', 1, NULL, '2022-10-17 14:11:07', '2022-10-17 14:11:07'),
	(1581907412451393537, '物流方式US', 'logistics_us', 1, NULL, '2022-10-17 15:18:12', '2022-10-17 15:18:12'),
	(1581907799845699586, '物流方式UK', 'logistics_uk', 1, NULL, '2022-10-17 15:19:45', '2022-10-17 15:19:45');
/*!40000 ALTER TABLE `t_sys_dict` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

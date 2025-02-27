-- --------------------------------------------------------
-- 主机:                           rm-wz903sa454i2h35ik6o.mysql.rds.aliyuncs.com
-- 服务器版本:                        5.7.28-log - Source distribution
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  12.10.0.7000
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 正在导出表  db_admin.t_sys_permission 的数据：~14 rows (大约)
INSERT INTO `t_sys_permission` (`id`, `name`, `menu_id`, `url_perm`, `btn_perm`, `gmt_create`, `gmt_modified`) VALUES
	(1590216687451979778, '库位编辑', 7, 'POST:/erp/v1/warehouse/shelf/add', 'erp:wh:shelf:add', '2022-11-09 13:36:18', '2022-11-09 13:36:18'),
	(1627577448918474754, '删除1688授权', 26, 'GET:/erp/api/v1/purchase1688/delete', 'erp:purchase:open1688:delete', '2023-02-20 15:54:37', '2023-02-20 15:54:37'),
	(1628579022289592322, '导出', 98, 'GET:/erp/api/v1/material/downloadMaterialList', 'erp:material:download', '2023-02-23 10:14:31', '2023-02-23 10:14:31'),
	(1628580738779144193, '导出', 44, 'GET:/erp/api/v1/customer/downloadCustomerList', 'erp:customer:download', '2023-02-23 10:21:20', '2023-02-23 10:21:20'),
	(1648888310276505601, '本地已完成', 27, 'GET:/amazon/api/v1/shipForm/marketShipped', 'amz:ship:localdone', '2023-04-20 11:16:23', '2023-04-20 11:16:23'),
	(1648888789660286977, '删除货件', 27, 'GET:/erp/api/v1/shipForm/disableShipment', 'amz:ship:delete', '2023-04-20 11:18:17', '2023-04-20 11:18:17'),
	(1656144687886938114, '确认出库(含耗材)', 27, 'POST:/erp/api/v1/shipForm/saveInventoryConsumable', 'amz:ship:conship', '2023-05-10 11:50:38', '2023-05-10 11:50:38'),
	(1656145342726844417, '确认出库', 27, 'GET:/amazon/api/v1/shipForm/marketAmazonShipped', 'amz:ship:shipdone', '2023-05-10 11:53:14', '2023-05-10 11:53:14'),
	(1697432618920222722, '添加加工单', 70, 'POST:/erp/api/v1/assembly/saveData', 'erp:po:ass:add', '2023-09-01 10:14:08', '2023-09-01 10:14:08'),
	(1697433107619553281, '入库操作', 70, 'GET:/erp/api/v1/assembly/saveRecord', 'erp:po:ass:in', '2023-09-01 10:16:04', '2023-09-01 10:16:04'),
	(1697437139805315073, '修改单据数量', 70, 'GET:/erp/api/v1/assembly/changeAssAmount', 'erp:po:ass:update', '2023-09-01 10:32:06', '2023-09-01 10:32:06'),
	(1697438074115891202, '终止单据', 70, 'GET:/erp/api/v1/assembly/stopAssemblyEvent', 'erp:po:ass:stop', '2023-09-01 10:35:48', '2023-09-01 10:35:48'),
	(1697438074115891203, '查看产品信息', 1012151, 'POST:/quote/api/v1/quote/supplier/addsupplier', 'erp:pi:list:item', '2023-09-01 10:35:48', '2023-09-01 10:35:48'),
	(1697438074115891205, '查看物流连接', 1012153, 'POST:/quote/api/v1/quote/supplier/addsupplier', 'erp:pi:supplier:link', '2023-09-01 10:35:48', '2023-09-01 10:35:48');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

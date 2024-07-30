-- --------------------------------------------------------
-- 主机:                           rm-wz903sa454i2h35ik6o.mysql.rds.aliyuncs.com
-- 服务器版本:                        5.7.28-log - Source distribution
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

-- 正在导出表  db_amazon.t_erp_amazon_feedstatus 的数据：~17 rows (大约)
DELETE FROM `t_erp_amazon_feedstatus`;
INSERT INTO `t_erp_amazon_feedstatus` (`status`, `name`, `remark`) VALUES
	('AWAITING_ASYNCHRONOUS_REPLY', '正在处理该请求', '正在处理该请求，但需要等待外部信息才能完成。'),
	('CANCELLED', '请求因严重错误而中止', '请求因严重错误而中止。'),
	('DONE', '请求已处理', '请求已处理。您可以调用 GetFeedSubmissionResult 操作来接收处理报告，该报告列出了上传数据中成功处理的记录以及产生错误的记录。'),
	('Error', '请求失败', '请求失败'),
	('IN_PROGRESS', '请求正在处理', ' 	请求正在处理。'),
	('IN_QUEUE', '平台排队中', '已经提交给亚马逊，亚马逊得调价队列等待'),
	('IN_SAFETY_NET', '请求正在处理，但系统发现上传数据可能包含潜在错误', '请求正在处理，但系统发现上传数据可能包含潜在错误（例如，请求将删除卖家账户中的所有库存）。亚马逊卖家支持团队将联系卖家，以确认是否应处理该上传数据。'),
	('SUBMITTED', '已收到请求，但尚未开始处理', '已收到请求，但尚未开始处理。'),
	('UNCONFIRMED', '请求等待中', '请求等待中'),
	('_AWAITING_ASYNCHRONOUS_REPLY_', '正在处理该请求', '正在处理该请求，但需要等待外部信息才能完成。'),
	('_CANCELLED_', '请求因严重错误而中止', '请求因严重错误而中止。'),
	('_DONE_', '请求已处理', '请求已处理。您可以调用 GetFeedSubmissionResult 操作来接收处理报告，该报告列出了上传数据中成功处理的记录以及产生错误的记录。'),
	('_IN_PROGRESS_', '请求正在处理', ' 	请求正在处理。'),
	('_IN_SAFETY_NET_', '请求正在处理，但系统发现上传数据可能包含潜在错误', '请求正在处理，但系统发现上传数据可能包含潜在错误（例如，请求将删除卖家账户中的所有库存）。亚马逊卖家支持团队将联系卖家，以确认是否应处理该上传数据。'),
	('_SUBMITTED_', '已收到请求，但尚未开始处理。', '已收到请求，但尚未开始处理。'),
	('_UNCONFIRMED_', '请求等待中', '请求等待中'),
	('waiting', '系统排队中', 'wimoor还没有通过API提交给亚马逊');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

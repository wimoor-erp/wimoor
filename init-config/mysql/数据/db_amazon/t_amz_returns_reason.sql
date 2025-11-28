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

-- 正在导出表  db_amazon.t_amz_returns_reason 的数据：~45 rows (大约)
DELETE FROM `t_amz_returns_reason`;
INSERT INTO `t_amz_returns_reason` (`code`, `name`, `description`, `type`) VALUES
	('APPAREL_STYLE', '服装：不喜欢服装的款式', NULL, NULL),
	('APPAREL_TOO_LARGE', '服装太大', '尺码没有判断准确', '卖家的问题'),
	('APPAREL_TOO_SMALL', '服装太小', '尺码没有判断准确', '卖家的问题'),
	('DAMAGED_BY_CARRIER', '承运人损坏', NULL, '亚马逊的问题'),
	('DAMAGED_BY_FC', '配送中心损坏', NULL, '亚马逊的问题'),
	('DEFECTIVE', '产品缺陷', '确认产品是否有缺陷', '卖家的问题'),
	('DID_NOT_LIKE_COLOR', '不喜欢颜色', NULL, NULL),
	('DID_NOT_LIKE_FABRIC', '不喜欢布料', NULL, NULL),
	('EXCESSIVE_INSTALLATION', '过度安装', '难安装，或者装不了', '客户的问题'),
	('EXTRA_ITEM', '货件中包含其他商品', NULL, NULL),
	('FOUND_BETTER_PRICE', '有更好的价格', '看看是否有一样的产品价格比低', '卖家的问题'),
	('JEWELRY_BAD_CLASP', '珠宝首饰：破损或者挂钩损坏', NULL, NULL),
	('JEWELRY_BATTERY', '珠宝首饰：电池没电', NULL, NULL),
	('JEWELRY_LOOSE_STONE', '珠宝首饰：宝石丢失或松脱', NULL, NULL),
	('JEWELRY_NO_CERT', '珠宝首饰：缺少承诺的证书', NULL, NULL),
	('JEWELRY_NO_DOCS', '珠宝首饰：缺少使用手册/质保', NULL, NULL),
	('JEWELRY_TARNISHED', '珠宝首饰：颜色不正', NULL, NULL),
	('JEWELRY_TOO_LARGE', '珠宝首饰：太大/长', NULL, NULL),
	('JEWELRY_TOO_SMALL', '珠宝首饰：太小/短', NULL, NULL),
	('MISORDERED', '订购了错误的款式/尺码/颜色', NULL, NULL),
	('MISSED_ESTIMATED_DELIVERY', '错过了预计交货日期', NULL, '亚马逊的问题'),
	('MISSING_PARTS', '配件缺失', '发货的时候检查包装', '卖家的问题'),
	('NEVER_ARRIVED', '一直未送达', NULL, '亚马逊的问题'),
	('NOT_AS_DESCRIBED', '与描述不符', '优化页面，避免过于夸张的内容，包括图片', '卖家的问题'),
	('NOT_AS_EXPECTED', '不符合预期', NULL, NULL),
	('NOT_COMPATIBLE', '产品不兼容', '优化页面，讲清楚如何安装或者兼容情况。', '卖家的问题'),
	('NO_REASON_GIVEN', '无原因的配送失败', NULL, '客户的问题'),
	('ORDERED_WRONG_ITEM', '买错了', '这基本上和不想要是一个意思', '客户的问题'),
	('OTHER', '退货选项不可用', NULL, NULL),
	('PART_NOT_COMPATIBLE', '配件不兼容', '优化页面', '卖家的问题'),
	('PRODUCT_NOT_ITALIAN', '非本地', NULL, NULL),
	('PRODUCT_NOT_SPANISH', ' 产品或手册语种不对', NULL, '卖家的问题'),
	('QUALITY_UNACCEPTABLE', '无法接受质量', '提升产品整体质量，包括包装和说明书', '卖家的问题'),
	('SWITCHEROO', '发错了商品', NULL, '卖家的问题'),
	('UNAUTHORIZED_PURCHASE', '没有授权的购买', '也是不想要的意思，很少是因为小孩子用大人账号购买之类的', '客户的问题'),
	('UNDELIVERABLE_CARRIER_MISS_SORTED', '无法交付的承运商未分拣', NULL, NULL),
	('UNDELIVERABLE_FAILED_DELIVERY_ATTEMPTS', '无法送达：多次派送均无人收件', NULL, NULL),
	('UNDELIVERABLE_FALED_DELIVERY_ATTEMPTS', '尝试配送失败', NULL, '亚马逊的问题'),
	('UNDELIVERABLE_INSUFFICIENT_ADDRESS', '无法送达：地址不详', NULL, NULL),
	('UNDELIVERABLE_REFUSED', '拒绝配送', NULL, '亚马逊的问题'),
	('UNDELIVERABLE_UNCLAIMED', '无法送达：无人认领', NULL, NULL),
	('UNDELIVERABLE_UNKNOWN', '过度安装', NULL, '亚马逊的问题'),
	('UNWANTED_ITEM', '不想要了', '客户因为某种原因后悔了', '客户的问题'),
	('WARRANTY', '商品运送到时出现瑕疵 - 质保', NULL, NULL),
	('WRONG_SIZE', '错误尺寸', NULL, NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

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

-- 导出  表 db_amazon.t_amz_payment_report 结构
CREATE TABLE IF NOT EXISTS `t_amz_payment_report` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `report_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '报告类型',
  `date_time` datetime NOT NULL COMMENT '报告日期时间',
  `settlement_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '结算ID',
  `transaction_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易类型',
  `order_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单ID',
  `sku` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品SKU',
  `quantity` int DEFAULT '0' COMMENT '数量',
  `store` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺',
  `fulfillment` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '履约方式',
  `order_city` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单城市',
  `order_state` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单州/省',
  `order_post_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单邮政编码',
  `tax_collection_model` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '征税模型',
  `product_sales` decimal(10,2) DEFAULT '0.00' COMMENT '产品销售金额',
  `product_sales_tax` decimal(10,2) DEFAULT '0.00' COMMENT '产品销售税',
  `shipping_credits` decimal(10,2) DEFAULT '0.00' COMMENT '运费抵扣',
  `shipping_credits_tax` decimal(10,2) DEFAULT '0.00' COMMENT '运费抵扣税',
  `gift_wrap_credits` decimal(10,2) DEFAULT '0.00' COMMENT '礼品包装抵扣',
  `gift_wrap_credits_tax` decimal(10,2) DEFAULT '0.00' COMMENT '礼品包装抵扣税',
  `regulatory_fee` decimal(10,2) DEFAULT '0.00' COMMENT '监管费',
  `tax_on_regulatory_fee` decimal(10,2) DEFAULT '0.00' COMMENT '监管费税费',
  `promotional_rebate` decimal(10,2) DEFAULT '0.00' COMMENT '促销返利',
  `promotional_rebate_tax` decimal(10,2) DEFAULT '0.00' COMMENT '促销返利税',
  `marketplace_withheld_tax` decimal(10,2) DEFAULT '0.00' COMMENT '市场预扣税',
  `selling_fees` decimal(10,2) DEFAULT '0.00' COMMENT '销售费用',
  `fba_fees` decimal(10,2) DEFAULT '0.00' COMMENT 'FBA费用',
  `other_transaction_fees` decimal(10,2) DEFAULT '0.00' COMMENT '其他交易费用',
  `other_fees` decimal(10,2) DEFAULT '0.00' COMMENT '其他费用',
  `total` decimal(10,2) DEFAULT '0.00' COMMENT '总计金额',
  `tax_amount` decimal(10,2) DEFAULT '0.00' COMMENT '税费总额',
  `total_amount` decimal(10,2) DEFAULT '0.00' COMMENT '总金额',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `opttime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_settlement_id` (`settlement_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_sku` (`sku`),
  KEY `idx_report_date` (`date_time`) USING BTREE,
  KEY `idx_settlement_date` (`settlement_id`,`date_time`) USING BTREE,
  KEY `idx_store_date` (`store`,`date_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='亚马逊支付报告表';

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

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

-- 导出  表 db_amazon.t_amz_inventory_planning 结构
CREATE TABLE IF NOT EXISTS `t_amz_inventory_planning` (
  `snapshot_date` date DEFAULT NULL COMMENT '快照时间',
  `sku` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '平台产品SKU',
  `amazonauthid` bigint unsigned NOT NULL COMMENT '授权ID',
  `countrycode` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '国家编码',
  `fnsku` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '仓库编码（产品条码）',
  `asin` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商品编码',
  `condition` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '商品状况',
  `available` int DEFAULT NULL COMMENT '可用库存数量',
  `pending_removal_quantity` int DEFAULT NULL COMMENT '等待移除的库存',
  `currency` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '币种',
  `inv_age_0_to_90_days` int DEFAULT NULL COMMENT '90天以内库龄数量',
  `inv_age_91_to_180_days` int DEFAULT NULL COMMENT '91-180天以内库龄数量',
  `inv_age_181_to_270_days` int DEFAULT NULL COMMENT '181-270天以内库龄数量',
  `inv_age_271_to_365_days` int DEFAULT NULL COMMENT '271-365天以内库龄数量',
  `inv_age_365_plus_days` int DEFAULT NULL COMMENT '365天以上库龄数量',
  `qty_to_be_charged_ltsf_11_mo` decimal(20,6) DEFAULT NULL COMMENT '长期仓储费：超过11个月的仓库费',
  `projected_ltsf_11_mo` decimal(20,6) DEFAULT NULL COMMENT '预估下期仓储费：超过11个月的',
  `qty_to_be_charged_ltsf_12_mo` decimal(20,6) DEFAULT NULL COMMENT '长期仓储费：超过12个月的仓库费',
  `estimated_ltsf_next_charge` decimal(20,6) DEFAULT NULL COMMENT '预估下期长期仓储费',
  `units_shipped_t7` int DEFAULT NULL COMMENT '最近7天发货',
  `units_shipped_t30` int DEFAULT NULL COMMENT '最近30天发货',
  `units_shipped_t60` int DEFAULT NULL COMMENT '最近60天发货',
  `units_shipped_t90` int DEFAULT NULL COMMENT '最近90天发货',
  `alert` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '低流量或低转化率提醒',
  `your_price` decimal(20,6) DEFAULT NULL COMMENT '您发布的商品价格',
  `sales_price` decimal(20,6) DEFAULT NULL COMMENT '您促销商品的价格',
  `lowest_price_new_plus_shipping` decimal(20,6) DEFAULT NULL COMMENT '此商品的新品在亚马逊商城的最低价格（含运费）',
  `lowest_price_used` decimal(20,6) DEFAULT NULL COMMENT '此商品的二手商品在亚马逊商城的最低价格（含运费）',
  `recommended_action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '建议您对库存执行的操作。建议的依据是您当前的买家需求以及采取措施是否比不采取任何措施花费更低',
  `healthy_inventory_level` int DEFAULT NULL COMMENT '根据商品需求和您的成本，被认定为不是冗余库存的库存数量。我们的建议是为了帮助您达到这一库存水平。',
  `recommended_sales_price` decimal(20,6) DEFAULT NULL COMMENT '建议售价',
  `recommended_sale_duration_days` int DEFAULT NULL COMMENT '建议售价的天数',
  `recommended_removal_quantity` int DEFAULT NULL COMMENT '建议移除数量',
  `estimated_cost_savings_of_recommended_actions` decimal(20,6) DEFAULT NULL COMMENT '可以节约成本:与不采取任何措施（需要为相应库存支付仓储费）相比，采取建议措施预计可以节约的成本',
  `sell_through` decimal(20,6) DEFAULT NULL COMMENT '售罄率：较高的售罄率可能表明仍有库存机会，而较低的数值可能意味着你已经过度投资',
  `item_volume` decimal(20,6) DEFAULT NULL COMMENT '商品的体积',
  `volume_unit_measurement` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '商品体积的计量单位',
  `storage_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '仓储类型分类：有六种仓储类型：标准尺寸、大件、服装、鞋靴、易燃物或气溶胶',
  `storage_volume` decimal(20,6) DEFAULT NULL COMMENT '存储体积',
  `product_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品分类',
  `sales_rank` int DEFAULT NULL COMMENT '销售排名',
  `days_of_supply` int DEFAULT NULL COMMENT '持续供货(天）',
  `estimated_excess_quantity` int DEFAULT NULL COMMENT '预计冗余商品数量：根据您当前的销售速度和买家需求预测得出的积压商品数量。保留这些商品并支付仓储费比通过降低或移除价格来降低商品数量的成本更高',
  `weeks_of_cover_t30` int DEFAULT NULL COMMENT '30天周转',
  `weeks_of_cover_t90` int DEFAULT NULL COMMENT '90天周转',
  `featuredoffer_price` decimal(20,6) DEFAULT NULL COMMENT '价格',
  `sales_shipped_last_7_days` decimal(20,6) DEFAULT NULL COMMENT '7天销售额',
  `sales_shipped_last_30_days` decimal(20,6) DEFAULT NULL COMMENT '30天销售额',
  `sales_shipped_last_60_days` decimal(20,6) DEFAULT NULL COMMENT '60天销售额',
  `sales_shipped_last_90_days` decimal(20,6) DEFAULT NULL COMMENT '90天销售额',
  `inv_age_0_to_30_days` int DEFAULT NULL COMMENT '30天以内库龄数量',
  `inv_age_31_to_60_days` int DEFAULT NULL COMMENT '31-60天以内库龄数量',
  `inv_age_61_to_90_days` int DEFAULT NULL COMMENT '61-90天以内库龄数量',
  `inv_age_181_to_330_days` int DEFAULT NULL COMMENT '181-330天以内库龄数量',
  `inv_age_331_to_365_days` int DEFAULT NULL COMMENT '331-365天以内库龄数量',
  `estimated_storage_cost_next_month` decimal(20,6) DEFAULT NULL COMMENT '预估下个月仓储费',
  `inbound_quantity` int DEFAULT NULL COMMENT '待入库总数',
  `inbound_working` int DEFAULT NULL COMMENT '待入库-发货中',
  `inbound_shipped` int DEFAULT NULL COMMENT '待入库-已发货',
  `inbound_received` int DEFAULT NULL COMMENT '待入库-接收中',
  `no_sale_last_6_months` int DEFAULT NULL COMMENT '6个月内没有售出的数量',
  `reserved_quantity` int DEFAULT NULL COMMENT '总的预留库存',
  `unfulfillable_quantity` int DEFAULT NULL COMMENT '不可售库存',
  `afn_researching_quantity` int DEFAULT NULL COMMENT '正在找回的库存',
  `afn_reserved_future_supply` int DEFAULT NULL COMMENT '预留库存（为有意向购买者保留）',
  `afn_future_supply_buyable` int DEFAULT NULL COMMENT '未来可售（即将到货的库存）',
  `quantity_to_be_charged_ais_181_210_days` int DEFAULT NULL,
  `quantity_to_be_charged_ais_211_240_days` int DEFAULT NULL,
  `quantity_to_be_charged_ais_241_270_days` int DEFAULT NULL,
  `quantity_to_be_charged_ais_271_300_days` int DEFAULT NULL,
  `quantity_to_be_charged_ais_301_330_days` int DEFAULT NULL,
  `quantity_to_be_charged_ais_331_365_days` int DEFAULT NULL,
  `quantity_to_be_charged_ais_365_plus_days` int DEFAULT NULL,
  `estimated_ais_181_210_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_211_240_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_241_270_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_271_300_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_301_330_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_331_365_days` decimal(20,6) DEFAULT NULL,
  `estimated_ais_365_plus_days` decimal(20,6) DEFAULT NULL,
  `fba_minimum_inventory_level` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '最低水位库存量',
  `historical_days_of_supply` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '历史供货天数',
  `fba_inventory_level_health_status` char(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '库存健康状态',
  PRIMARY KEY (`amazonauthid`,`countrycode`,`sku`,`condition`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

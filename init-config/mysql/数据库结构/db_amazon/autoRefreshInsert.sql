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

-- 导出  事件 db_amazon.autoRefreshInsert 结构
DELIMITER //
CREATE EVENT `autoRefreshInsert` ON SCHEDULE EVERY 1 DAY STARTS '2025-04-08 17:47:39' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
INSERT ignore into t_amz_product_refresh 
		SELECT i.id,i.amazonauthid,
		str_to_date('1949-10-01 00:00:00', '%Y-%m-%d %H:%i:%s'),
		str_to_date('1949-10-01 00:00:00', '%Y-%m-%d %H:%i:%s'),
		str_to_date('1949-10-01 00:00:00', '%Y-%m-%d %H:%i:%s'),
		i.sku,i.asin,i.marketplaceid,i.isparent,i.invalid
		FROM t_product_info i
		LEFT JOIN t_amz_product_refresh r ON r.pid=i.id
		LEFT JOIN t_amazon_auth a ON a.id=i.amazonauthid
		WHERE a.`disable`=0 
		AND i.invalid=0  
		AND a.createtime>DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY)
		AND r.pid IS NULL ;
		

delete from t_report_requestrecord WHERE reportType IN(
'GET_FBA_FULFILLMENT_LONGTERM_STORAGE_FEE_CHARGES_DATA',
'GET_FBA_FULFILLMENT_INVENTORY_HEALTH_DATA',
'GET_FBA_INVENTORY_PLANNING_DATA',
'GET_FBA_REIMBURSEMENTS_DATA',
'GET_FBA_STORAGE_FEE_CHARGES_DATA'
)
 AND  lastupdate<DATE_SUB(NOW(),INTERVAL 30 DAY);

delete from t_report_requestrecord WHERE reportType='GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE_V2' AND  lastupdate<DATE_SUB(NOW(),INTERVAL 365 DAY);

delete from t_report_requestrecord WHERE reportType NOT  IN(
'GET_FBA_FULFILLMENT_LONGTERM_STORAGE_FEE_CHARGES_DATA',
'GET_FBA_FULFILLMENT_INVENTORY_HEALTH_DATA',
'GET_FBA_INVENTORY_PLANNING_DATA',
'GET_FBA_REIMBURSEMENTS_DATA',
'GET_FBA_STORAGE_FEE_CHARGES_DATA',
'GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE_V2'
)
 AND  lastupdate<DATE_SUB(NOW(),INTERVAL 3 DAY);
 

update t_product_info  i
LEFT JOIN t_amazon_auth a ON a.id=i.amazonAuthId
LEFT JOIN t_product_in_opt o ON o.pid=i.id
LEFT JOIN db_erp.t_erp_material m ON m.sku=IFNULL(o.msku,i.sku) AND m.shopid=a.shop_id AND m.isDelete=0
SET o.`owner`=m.`owner`
WHERE o.`owner` IS NULL AND m.`owner` IS NOT NULL ;


INSERT INTO  `t_product_in_opt` (	`pid` ,	`msku`,	`owner`)
SELECT i.id,m.sku,m.`owner`
FROM t_product_info i
LEFT JOIN t_amazon_auth a ON a.id=i.amazonAuthId
LEFT JOIN t_product_in_opt o ON o.pid=i.id
LEFT JOIN db_erp.t_erp_material m ON m.sku=i.sku AND m.shopid=a.shop_id AND m.isdelete=0
WHERE o.pid IS NULL
AND m.id IS NOT NULL
AND m.owner IS NOT NULL	;

delete FROM  t_inventory_report 
WHERE byday<DATE_SUB(NOW(),INTERVAL 30 DAY);

END//
DELIMITER ;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

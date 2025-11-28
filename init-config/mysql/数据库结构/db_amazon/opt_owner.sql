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

-- 导出  事件 db_amazon.opt_owner 结构
DELIMITER //
CREATE EVENT `opt_owner` ON SCHEDULE EVERY 1 DAY STARTS '2025-04-08 17:49:39' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN

		
UPDATE t_erp_ship_inbounditem t
LEFT JOIN t_erp_ship_inboundshipment s ON s.ShipmentId=t.ShipmentId
LEFT join t_erp_ship_v2_inboundshipment v ON v.shipment_confirmation_id=t.ShipmentId 
LEFT JOIN t_erp_ship_v2_inboundplan p ON p.id=v.formid
LEFT JOIN t_erp_ship_v2_inboundshipment_item i ON i.shipmentid=v.shipmentid AND i.sku=t.SellerSKU
LEFT JOIN db_erp.t_erp_material m ON m.sku=i.msku AND m.isDelete=0 AND m.shopid=p.shopid 
SET t.unittransfee=i.unittransfee,t.unitcost=i.unitcost,t.totalcost=i.totalcost,t.totaltransfee=i.totaltransfee,t.msku=i.msku,t.materialid=m.id ,
i.received=t.QuantityReceived,i.receiveddate=t.ReceivedDate,t.Quantity=i.quantity
WHERE p.opttime> date_format(CURRENT_DATE()-90,'%Y-%m-%d')  AND v.shipmentid IS NOT NULL;	
	
UPDATE  t_erp_ship_inboundshipment s  
LEFT join t_erp_ship_v2_inboundshipment v ON v.shipment_confirmation_id=s.ShipmentId 
SET  s.ShipmentStatus=v.shipmentstatus,s.`status`=case when v.shipmentstatus='CANCEL' then 0 ELSE s.`status` end 
WHERE v.shipment_confirmation_id=s.ShipmentId AND s.ShipmentStatus!=v.shipmentstatus AND v.opttime>DATE_SUB(NOW(),interval 1 DAY);

UPDATE  t_erp_ship_inboundshipment s  
LEFT join t_erp_ship_v2_inboundshipment v ON v.shipment_confirmation_id=s.ShipmentId 
LEFT JOIN t_erp_ship_inboundplan p ON p.id=s.inboundplanid
LEFT join t_erp_ship_v2_inboundplan p2 ON p2.id=v.formid
SET  p.warehouseid=p2.warehouseid
WHERE v.shipment_confirmation_id=s.ShipmentId AND  p.warehouseid IS NULL    AND v.opttime>DATE_SUB(NOW(),INTERVAL 90 DAY);

UPDATE  t_erp_ship_inboundshipment s  
LEFT join t_erp_ship_v2_inboundshipment v ON v.shipment_confirmation_id=s.ShipmentId 
LEFT JOIN t_erp_ship_inboundplan p ON p.id=s.inboundplanid
LEFT join t_erp_ship_v2_inboundplan p2 ON p2.id=v.formid
SET  v.start_receive_date=s.start_receive_date
WHERE v.shipment_confirmation_id=s.ShipmentId AND v.start_receive_date IS NULL   AND v.opttime>DATE_SUB(NOW(),INTERVAL 90 DAY);
	
INSERT ignore INTO  t_erp_ship_inboundbox
SELECT UUID_SHORT(),s.shipment_confirmation_id,b.boxnum,b.length,b.width,b.height,b.unit,b.weight,b.wunit,b.tracking_id,b.package_status,b.opttime,b.operator
from t_erp_ship_v2_inboundshipment s
LEFT JOIN t_erp_ship_v2_inboundshipment_box b ON b.shipmentid=s.shipmentid
LEFT JOIN t_erp_ship_inboundbox bx ON bx.shipmentid=s.shipment_confirmation_id
WHERE b.boxnum IS NOT NULL  AND s.opttime>DATE_SUB(NOW(),INTERVAL 3 DAY)
AND bx.id IS null
;

UPDATE  t_erp_ship_v2_inboundshipment s
LEFT JOIN t_erp_ship_inboundshipment p ON p.ShipmentId=s.shipment_confirmation_id
SET s.shipmentstatus=p.ShipmentStatus ,s.`status`=8
WHERE p.ShipmentStatus='CLOSED' AND p.ShipmentStatus!=s.shipmentstatus;


UPDATE  t_erp_ship_v2_inboundshipment s
LEFT JOIN t_erp_ship_inboundshipment p ON p.ShipmentId=s.shipment_confirmation_id
SET p.shipmentstatus=s.ShipmentStatus ,s.`status`=0 ,p.`status`=0
WHERE s.shipmentstatus='CANCELLED' AND (p.ShipmentStatus!='CANCELLED'  OR s.`status`!=0 OR p.`status`!=0)
AND s.opttime>DATE_SUB(NOW(),INTERVAL  12 MONTH) AND p.ShipmentId=s.shipment_confirmation_id;

UPDATE  t_erp_ship_inboundshipment s  
LEFT join t_erp_ship_v2_inboundshipment v ON v.shipment_confirmation_id=s.ShipmentId 
SET  s.boxnum=(SELECT COUNT(0) FROM t_erp_ship_v2_inboundshipment_box b WHERE  b.shipmentid=v.shipmentid) 
WHERE v.shipment_confirmation_id=s.ShipmentId AND s.boxnum IS NULL     AND v.opttime>DATE_SUB(NOW(),INTERVAL 30 DAY);

delete from t_holiday;
INSERT INTO t_holiday
SELECT id,shopid,	`day`,week_name, `type`,DAYOFWEEK(`day`) 	`week`,creator create_by,createtime create_time,
operator update_by,opttime update_time FROM (
SELECT id,shopid,concat(YEAR,case when MONTH>9 then '-' ELSE '-0' end, MONTH,
case when day>9 then '-' ELSE '-0' END ,DAY) AS `day`,week_name,
1 `type` ,creator,createtime,ifnull(operator,creator) operator,opttime from db_admin.t_sys_local_holiday
WHERE TYPE=1 OR TYPE=2) v;
END//
DELIMITER ;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

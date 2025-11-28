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

-- 导出  事件 db_amazon_adv.syncData 结构
DELIMITER //
CREATE EVENT `syncData` ON SCHEDULE EVERY 1 DAY STARTS '2024-06-14 16:45:40' ON COMPLETION PRESERVE ENABLE DO BEGIN

update t_amz_adv_auth a
LEFT JOIN (SELECT advauthid FROM t_amz_adv_profile GROUP BY advauthid) v ON v.advauthid=a.id
SET  a.`disable`=1 ,a.disableTime=NOW()
WHERE a.`disable`=0  AND v.advauthid IS NULL;


replace into db_amazon_adv.t_amazon_group
SELECT * FROM db_amazon.t_amazon_group ;
 
 replace into db_amazon_adv.t_exchangerate
SELECT * FROM db_amazon.t_exchangerate;
replace into db_amazon_adv.t_exchangerate_customer
SELECT * FROM db_amazon.t_exchangerate_customer;

END//
DELIMITER ;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

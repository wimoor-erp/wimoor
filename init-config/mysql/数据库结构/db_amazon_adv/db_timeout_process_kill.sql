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

-- 导出  事件 db_amazon_adv.db_timeout_process_kill 结构
DELIMITER //
CREATE EVENT `db_timeout_process_kill` ON SCHEDULE EVERY 1 DAY STARTS '2024-06-14 16:44:41' ON COMPLETION PRESERVE ENABLE DO BEGIN


declare v_sql varchar(500);
declare no_more_long_running_trx integer default 0;

declare c_tid cursor for
select concat ('kill ',trx_mysql_thread_id,';')
from information_schema.innodb_trx
where timestampdiff(second,trx_started,NOW()) >= 20
AND trx_query LIKE 'select%';

declare continue handler for not found set no_more_long_running_trx=1;

open c_tid;

repeat fetch c_tid into v_sql;

if no_more_long_running_trx != 1 then
set @v_sql=v_sql;
prepare stmt from @v_sql;
execute stmt;
SELECT @v_sql;
deallocate prepare stmt;
END if;

until no_more_long_running_trx end repeat;

close c_tid;

INSERT  into t_amazon_group
SELECT g.* FROM db_plum.t_amazon_group g
LEFT JOIN t_amazon_group m ON m.id=g.id
WHERE m.id IS NULL;
end//
DELIMITER ;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

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

-- 正在导出表  db_amazon.t_exchangeinfo 的数据：~30 rows (大约)
DELETE FROM `t_exchangeinfo`;
INSERT INTO `t_exchangeinfo` (`currency`, `name`, `type`) VALUES
	('AED', '阿联酋迪拉姆', 'CNY(100)/AED'),
	('AUD', '澳大利亚元', 'CNY(100)/AUD'),
	('BRL', '巴西里亚尔', 'CNY(100)/BRL'),
	('CAD', '加拿大元', 'CNY(100)/CAD'),
	('CHF', '瑞士法郎', 'CNY(100)/CHF'),
	('CNY', '人民币', 'CNY(100)/CNY'),
	('DKK', '丹麦克朗', 'CNY(100)/DKK'),
	('EUR', '欧元', 'CNY(100)/EUR'),
	('GBP', '英镑', 'CNY(100)/GBP'),
	('HKD', '港币', 'CNY(100)/HKD'),
	('IDR', '印尼卢比', 'CNY(100)/IDR'),
	('INR', '印度卢比', 'CNY(100)/INR'),
	('JPY', '日元', 'CNY(100)/JPY'),
	('KRW', '韩国元', 'CNY(100)/KRW'),
	('MOP', '澳门元', 'CNY(100)/MOP'),
	('MXN', '墨西哥比索', 'CNY(100)/MXN'),
	('MYR', '林吉特', 'CNY(100)/MYR'),
	('NOK', '挪威克朗', 'CNY(100)/NOK'),
	('NZD', '新西兰元', 'CNY(100)/NZD'),
	('PHP', '菲律宾比索', 'CNY(100)/PHP'),
	('PLN', '波兰兹罗提', 'CNY(100)/PLN'),
	('RUB', '卢布', 'CNY(100)/RUB'),
	('SAR', '沙特里亚尔', 'CNY(100)/SAR'),
	('SEK', '瑞典克朗', 'CNY(100)/SEK'),
	('SGD', '新加坡元', 'CNY(100)/SGD'),
	('THB', '泰国铢', 'CNY(100)/THB'),
	('TRY', '土耳其里拉', 'CNY(100)/TRY'),
	('TWD', '新台币', 'CNY(100)/TWD'),
	('USD', '美元', 'CNY(100)/USD'),
	('ZAR', '南非兰特', 'CNY(100)/ZAR');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

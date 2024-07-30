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

-- 正在导出表  db_amazon.t_amz_ship_state_province_code 的数据：~50 rows (大约)
DELETE FROM `t_amz_ship_state_province_code`;
INSERT INTO `t_amz_ship_state_province_code` (`code`, `name`, `ename`, `capital`, `ecapital`) VALUES
	('AK', '阿拉斯加州', 'Alaska', '朱诺', 'Juneau'),
	('AL', '阿拉巴马州', 'Alabama', '蒙哥马利', 'Montgomery'),
	('AR', '阿肯色州', 'Arkansas', '小石城', 'Little?rock'),
	('AZ', '阿利桑那州', 'Arizona', '菲尼克斯', 'Phoenix'),
	('CA', '加利福尼亚州', 'California', '萨克拉门托', 'Sacramento'),
	('CO', '科罗拉多州', 'Colorado', '丹佛', 'Denver'),
	('CT', '康涅狄格州', 'Connecticut', '哈特福德', 'Hartford'),
	('DE', '特拉华州', 'Delaware', '多佛', 'Dover'),
	('FL', '佛罗里达州', 'Florida', '塔拉哈西', 'Tallahassee'),
	('GA', '乔治亚州', 'Georgia', '亚特兰大', 'Atlanta'),
	('HI', '夏威夷州', 'Hawaii', '檀香山火努鲁鲁', 'Honolulu'),
	('IA', '爱荷华州', 'Iowa', '得梅因', 'Des?Moines'),
	('ID', '爱达荷州', 'Idaho', '博伊西', 'Boise'),
	('IL', '伊利诺斯州', 'Illinois', '斯普林菲尔德', 'Springfield'),
	('IN', '印第安纳州', 'Indiana', '印第安纳波利斯', 'Indianapolis'),
	('KS', '堪萨斯州', 'Kansas', '托皮卡', 'Topeka'),
	('KY', '肯塔基州', 'Kentucky', '法兰克福', 'Frankfort'),
	('LA', '路易斯安那州', 'Louisiana', '巴吞鲁日', 'Baton?Rouge'),
	('MA', '马萨诸塞州', 'Massachusetts', '波士顿', 'Boston'),
	('MD', '马里兰州', 'Maryland', '安纳波利斯', 'Annapolis'),
	('ME', '缅因州', 'Maine', '奥古斯塔', 'Augusta'),
	('MI', '密歇根州', 'Michigan', '兰辛', 'Lansing'),
	('MN', '明尼苏达州', 'Minnesota', '圣保罗', 'St. Paul'),
	('MO', '密苏里州', 'Missouri', '杰斐逊城', 'Jefferson City'),
	('MS', '密西西比州', 'Mississippi', '杰克逊', 'Jackson'),
	('MT', '蒙大拿州', 'Montana', '海伦娜', 'Helena'),
	('NC', '北卡罗来纳州', 'North?Carolina', '纳罗利', 'Raleigh'),
	('ND', '北达科他州', 'North?Dakota', '俾斯麦', 'Bismarck'),
	('NE', '内布拉斯加州', 'Nebraska', '林肯', 'Lincoln'),
	('NH', '新罕布什尔州', 'New?hampshire', '康科德', 'Concord'),
	('NJ', '新泽西州', 'New?jersey', '特伦顿', 'Trenton'),
	('NM', '新墨西哥州', 'New?mexico', '圣菲', 'Santa?Fe'),
	('NV', '内华达州', 'Nevada', '卡森城', 'Carson City'),
	('NY', '纽约州', 'New?York', '奥尔巴尼', 'Albany'),
	('OH', '俄亥俄州', 'Ohio', '哥伦布', 'Columbus'),
	('OK', '俄克拉荷马州', 'Oklahoma', '俄克拉何马城', 'Oklahoma City'),
	('OR', '俄勒冈州', 'Oregon', '塞勒姆', 'Salem'),
	('PA', '宾夕法尼亚州', 'Pennsylvania', '哈里斯堡', 'Harrisburg'),
	('RI', '罗得岛州', 'Rhode?island', '普罗维登斯', 'Providence'),
	('SC', '南卡罗来纳州', 'South?carolina', '哥伦比亚', 'Columbia'),
	('SD', '南达科他州', 'South?dakota', '皮尔', 'Pierre'),
	('TN', '田纳西州', 'Tennessee', '纳什维尔', 'Nashville'),
	('TX', '得克萨斯州', 'Texas', '奥斯汀', 'Austin'),
	('UT', '犹他州', 'Utah', '盐湖城', 'Salt Lake City'),
	('VA', '弗吉尼亚州', 'Virginia', '里士满', 'Richmond'),
	('VT', '佛蒙特州', 'Vermont', '蒙彼利埃', 'Montpelier'),
	('WA', '华盛顿州', 'Washington', '奥林匹亚', 'Olympia'),
	('WI', '威斯康辛州', 'Wisconsin', '麦迪逊', 'Madison'),
	('WV', '西弗吉尼亚州', 'West?Virginia', '查尔斯顿', 'Charleston'),
	('WY', '怀俄明州', 'Wyoming', '夏延', 'Cheyenne');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

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

-- 正在导出表  db_amazon.t_producttier 的数据：~91 rows (大约)
DELETE FROM `t_producttier`;
INSERT INTO `t_producttier` (`id`, `name`, `isStandard`, `country`, `box_weight`, `amz_name`) VALUES
	('1752879197345345538', '低价 Envelope', b'1', 'CA', 0.0000, NULL),
	('1752880194545315842', '低价 Standard', b'1', 'CA', 0.0000, NULL),
	('1752880269296201730', '低价 Small oversize', b'0', 'CA', 0.0000, NULL),
	('1752880335162499074', '低价 Medium oversize', b'0', 'CA', 0.0000, NULL),
	('1752880415354449922', '低价 Large oversize', b'0', 'CA', 0.0000, NULL),
	('1752880488338481153', '低价 Special oversize', b'0', 'CA', 0.0000, NULL),
	('envelope_ca', '标准 Envelope', b'1', 'CA', 0.0000, NULL),
	('envelope_mx', 'Envelope', b'1', 'MX', 0.0000, NULL),
	('extra-large150up', '标准 Extra-large 150+ lb', b'0', 'US', 0.0000, 'Special-Oversize'),
	('large_oversize_us', '标准 Extra-large 70 lb', b'0', 'US', 0.0000, 'Lg-Oversize'),
	('large_standard_us', '标准 Large Standard-Size', b'1', 'US', 0.0000, 'Lg-Std'),
	('lg_envelop_ae', 'Large Envelope', b'1', 'AE', 0.0000, 'Lg-Env'),
	('lg_envelop_tr', 'standard package', b'0', 'TR', 0.0000, 'Lg-Env'),
	('lg_envelope_au', 'Large Envelope', b'1', 'AU', 0.0000, 'Lg-Env'),
	('lg_oversize_au', 'Large Oversize', b'0', 'AU', 0.0000, 'Lg-Oversize'),
	('lg_oversize_tr', 'small envelope', b'0', 'TR', 0.0000, 'Lg-OverSize'),
	('lg_size_tr', 'small envelope', b'0', 'TR', 0.0000, 'Lg-Size'),
	('light_extra-large150up', '低价 Extra-large 150+ lb', b'0', 'US', 0.0000, 'Special-Oversize'),
	('light_large_oversize_us', '低价  Extra-large 70 lb', b'0', 'US', 0.0000, 'Lg-Oversize'),
	('light_large_standard_us', '低价 Large Standard-Size', b'1', 'US', 0.0000, 'Lg-Std'),
	('light_medium_oversize_us', '低价 Extra-large 50 lb', b'0', 'US', 0.0000, 'Medium-Oversize'),
	('light_sml_oversize_us', '低价  Large bulky', b'0', 'US', 0.0000, 'Sm-Oversize'),
	('light_sml_standard_us', '低价 Small Standard-Size', b'1', 'US', 0.0000, 'Sm-Std'),
	('light_special_oversize_us', '低价 Extra-large 150 lb', b'0', 'US', 0.0000, 'Special-Oversize'),
	('medium_oversize_us', '标准 Extra-large 50 lb', b'0', 'US', 0.0000, 'Medium-Oversize'),
	('oversize1_jp', 'Oversize-1', b'0', 'JP', 0.0000, NULL),
	('oversize2_jp', 'Oversize-2', b'0', 'JP', 0.0000, NULL),
	('oversize3_jp', 'Oversize-3', b'0', 'JP', 0.0000, NULL),
	('oversize4_jp', 'Oversize-4', b'0', 'JP', 0.0000, NULL),
	('oversize5_jp', 'Oversize-5', b'0', 'JP', 0.0000, NULL),
	('oversize6_jp', 'Oversize-6', b'0', 'JP', 0.0000, NULL),
	('oversize7_jp', 'Oversize-7', b'0', 'JP', 0.0000, NULL),
	('oversize8_jp', 'Oversize-8', b'0', 'JP', 0.0000, NULL),
	('oversize_ae', 'Oversize', b'0', 'AE', 0.0000, 'Oversize'),
	('oversize_heavy_in', 'Oversize Heavy and Bulky', b'0', 'IN', 0.0000, NULL),
	('oversize_in', 'Oversize', b'0', 'IN', 0.0000, NULL),
	('oversize_lg_ca', '标准 Large Oversize', b'0', 'CA', 0.0000, NULL),
	('oversize_lgoverpkg', '标准 Oversize Large-W', b'0', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 0.0000, 'Lg-Oversize'),
	('oversize_lgoverv', '标准 Oversize Large-V', b'0', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 0.0000, 'Lg-Oversize-V'),
	('oversize_mid_ca', '标准 Medium Oversize', b'0', 'CA', 0.0000, NULL),
	('oversize_mx', 'Oversize', b'0', 'MX', 0.0000, NULL),
	('oversize_sa', 'Oversize', b'0', 'SA', 0.0000, NULL),
	('oversize_sm_ca', '标准 Small Oversize', b'0', 'CA', 0.0000, NULL),
	('oversize_smoverpkg', '标准 Oversize Small', b'1', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 0.0000, 'Sm-Oversize'),
	('oversize_special_ca', '标准 Special Oversize', b'0', 'CA', 0.0000, NULL),
	('oversize_spoverpkg', '标准 Oversize Special', b'0', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 0.0000, 'Sp-Oversize'),
	('oversize_stdoverpkg', '标准 Oversize Standard', b'0', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 0.0000, 'Std-Oversize'),
	('oversize_stdoverpkgl', '标准 Oversize Standard-L', b'0', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 0.0000, 'Std-Oversize-L'),
	('oversize_stdoverpkgw', '标准 Oversize Standard-W', b'0', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 0.0000, 'Std-Oversize-W'),
	('small', 'Small', b'1', 'JP', 0.0000, NULL),
	('sml_envelop_ae', 'Small Envelope', b'1', 'AE', 0.0000, 'Sm-Env'),
	('sml_envelop_au', 'Small Envelope', b'1', 'AU', 0.0000, 'Sm-Env'),
	('sml_envelop_tr', 'small envelope', b'0', 'TR', 0.0000, 'Sm-Env'),
	('sml_in', 'Small Size', b'1', 'IN', 0.0000, NULL),
	('sml_light_env_ext_lg', '低价 Envelope Extra Large', b'1', 'UK,DE,FR,IT,ES,NL,PL,SE', 0.0000, 'SL-Ext-Lg-Env'),
	('sml_light_env_lg', '低价 Envelope Large', b'1', 'UK,DE,FR,IT,ES,NL,PL,SE', 0.0000, 'SL-Lg-Env'),
	('sml_light_env_sml', '低价 Envelope Small', b'1', 'UK,DE,FR,IT,ES,NL,PL,SE', 0.0000, 'SL-Env'),
	('sml_light_env_std', '低价 Envelope Standard', b'1', 'UK,DE,FR,IT,ES,NL,PL,SE', 0.0000, 'SL-Std-Env'),
	('sml_light_jp_1000', 'Small and Light 1000', b'1', 'JP', 0.0000, 'Small and Light'),
	('sml_light_jp_250', 'Small and Light 250', b'1', 'JP', 0.0000, 'Small and Light'),
	('sml_light_pl_sml', '低价 Parcel Small', b'1', 'UK,DE,FR,IT,ES,NL,PL,SE', 0.0000, 'Sm-Pl'),
	('sml_oversize_au', 'Small Oversize', b'0', 'AU', 0.0000, 'Sm-Oversize'),
	('sml_oversize_us', '标准 Large bulky', b'0', 'US', 0.0000, 'Sm-Oversize'),
	('sml_standard_us', '标准 Small Standard-Size', b'1', 'US', 0.0000, 'Sm-Std'),
	('special_oversize_us', '标准 Extra-large 150 lb', b'0', 'US', 0.0000, 'Special-Oversize'),
	('std1_jp', 'Standard-1', b'1', 'JP', 0.0000, NULL),
	('std2_jp', 'Standard-2', b'1', 'JP', 0.0000, NULL),
	('std3_jp', 'Standard-3', b'1', 'JP', 0.0000, NULL),
	('std4_jp', 'Standard-4', b'1', 'JP', 0.0000, NULL),
	('std_ca', '标准 Standard', b'1', 'CA', 0.0000, NULL),
	('std_envelop_ae', 'Standard Envelope', b'1', 'AE', 0.0000, 'Std-Env'),
	('std_envelop_tr', 'standard package', b'0', 'TR', 0.0000, 'Std-Env'),
	('std_envelope_au', 'Standard Envelope', b'1', 'AU', 0.0000, 'Std-Env'),
	('std_in', 'Standard Size', b'1', 'IN', 0.0000, NULL),
	('std_lgpkg', '标准 Envelope Large', b'1', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 0.0000, 'Lg-Env'),
	('std_mx', 'Standard Size', b'1', 'MX', 0.0000, NULL),
	('std_overlgpkg', '标准 Envelope Extra Large', b'1', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 0.0000, 'Ext-Std-Env'),
	('std_oversize_au', 'Standard Oversize', b'0', 'AU', 0.0000, 'Std-Oversize'),
	('std_oversize_tr', 'small envelope', b'0', 'TR', 0.0000, 'Std-OverSize'),
	('std_parcel_ae', 'Standard Parcel', b'1', 'AE', 0.0000, 'Std-Parcel'),
	('std_parcel_au', 'Standard parcel', b'1', 'AU', 0.0000, 'Std-Parcel'),
	('std_pkg_tr', 'standard large size', b'0', 'TR', 0.0000, 'Std_Pkg'),
	('std_sa', 'Standard Size', b'1', 'SA', 0.0000, NULL),
	('std_smpkg', '标准 Envelope Small', b'1', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 0.0000, 'Sm-Env'),
	('std_smpl', '标准 Parcel Small', b'1', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 0.0000, 'Sm-Parcel'),
	('std_stdpkg', '标准 Parcel Standard', b'1', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 0.0000, 'Std-Parcel'),
	('std_stdpkg_box', '标准 Envelope Standard', b'1', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 0.0000, 'Std-Env'),
	('supersize1_jp', 'Supersize-1', b'0', 'JP', 0.0000, NULL),
	('supersize2_jp', 'Supersize-2', b'0', 'JP', 0.0000, NULL),
	('supersize3_jp', 'Supersize-3', b'0', 'JP', 0.0000, NULL),
	('supersize4_jp', 'Supersize-4', b'0', 'JP', 0.0000, NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

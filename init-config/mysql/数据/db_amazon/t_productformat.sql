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

-- 正在导出表  db_amazon.t_productformat 的数据：~89 rows (大约)
DELETE FROM `t_productformat`;
INSERT INTO `t_productformat` (`id`, `producttierId`, `country`, `format`, `length_unit`, `weight_unit`, `sort`) VALUES
	('0d2260ef-73dc-11ea-b1ed-00e04c023f0e', 'oversize1_jp', 'JP', 'dim.length.value+dim.width.value+dim.height.value <= 60  && dim.weight.value <= 2 ', NULL, NULL, 7),
	('12235', 'sml_light_pl_sml', 'UK,DE,FR,IT,ES,NL,PL,SE', 'dim.length.value <= 35 && dim.width.value <= 25 && dim.height.value <= 12 && dim.weight.value <= 0.4', 'cm', 'kg', 107),
	('123', 'sml_light_env_ext_lg', 'UK,DE,FR,IT,ES,NL,PL,SE', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value <= 6 && dim.weight.value <=0.96', 'cm', 'kg', 105),
	('1234', 'sml_light_pl_sml', 'UK,DE,FR,IT,ES,NL,PL,SE', 'dim.length.value <= 35 && dim.width.value <= 25 && dim.height.value <= 12 && dim.weight.value <= 0.15', 'cm', 'kg', 106),
	('14709525-73cc-11ea-b1ed-00e04c023f0e', 'std1_jp', 'JP', 'dim.length.value <= 45 && dim.width.value <= 35 && dim.height.value <= 20 && dim.length.value <= 33 && dim.width.value <= 24 && dim.height.value <= 2.8 && dim.weight.value <= 1', NULL, NULL, 2),
	('1902da1b-73dc-11ea-b1ed-00e04c023f0e', 'oversize2_jp', 'JP', 'dim.length.value+dim.width.value+dim.height.value <= 80  && dim.weight.value <= 5 ', NULL, NULL, 8),
	('1a92639e-63be-11e8-a076-00e04c023f0e', 'sml_envelop_au', 'AU', 'dim.length.value <= 23 && dim.width.value <= 15.5 && dim.height.value <= 1 && dim.weight.value <= 0.1', NULL, NULL, 1),
	('2302f794-73dc-11ea-b1ed-00e04c023f0e', 'oversize3_jp', 'JP', 'dim.length.value+dim.width.value+dim.height.value <= 100  && dim.weight.value <= 10 ', NULL, NULL, 9),
	('2d8079a0-73dc-11ea-b1ed-00e04c023f0e', 'oversize4_jp', 'JP', 'dim.length.value+dim.width.value+dim.height.value <= 120  && dim.weight.value <= 15 ', NULL, NULL, 10),
	('37ee67c2-73dc-11ea-b1ed-00e04c023f0e', 'oversize5_jp', 'JP', 'dim.length.value+dim.width.value+dim.height.value <= 140  && dim.weight.value <= 20 ', NULL, NULL, 11),
	('39446c8c-73cc-11ea-b1ed-00e04c023f0e', 'std2_jp', 'JP', 'dim.length.value <= 45 && dim.width.value <= 35 && dim.height.value <= 20 && dim.length.value+dim.width.value+dim.height.value <= 60  && dim.weight.value <= 2 ', NULL, NULL, 3),
	('43310ebc-73dc-11ea-b1ed-00e04c023f0e', 'oversize6_jp', 'JP', 'dim.length.value+dim.width.value+dim.height.value <= 160  && dim.weight.value <= 25 ', NULL, NULL, 12),
	('4411035e-cc0a-11e6-bab5-00e04c023f0e', 'std_stdpkg_box', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value <= 2.5 && dim.weight.value <= 0.46', NULL, NULL, 2),
	('4d827bc7-73dc-11ea-b1ed-00e04c023f0e', 'oversize7_jp', 'JP', 'dim.length.value+dim.width.value+dim.height.value <= 180  && dim.weight.value <= 30 ', NULL, NULL, 13),
	('5016e4a7-73cc-11ea-b1ed-00e04c023f0e', 'std3_jp', 'JP', 'dim.length.value <= 45 && dim.width.value <= 35 && dim.height.value <= 20 && dim.length.value+dim.width.value+dim.height.value <= 80  && dim.weight.value <= 5 ', NULL, NULL, 4),
	('5045fb35-b246-11eb-9957-98039b06df68', 'sml_envelop_ae', 'AE', 'dim.length.value <= 20 && dim.width.value <= 15 && dim.height.value <= 1 && dim.weight.value <= 0.1', 'cm', 'kg', 1),
	('504a69ff-b246-11eb-9957-98039b06df68', 'std_envelop_ae', 'AE', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value <= 2.5 && dim.weight.value <= 0.5', 'cm', 'kg', 2),
	('504d7143-b246-11eb-9957-98039b06df68', 'lg_envelop_ae', 'AE', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value <= 5 && dim.weight.value <= 1', 'cm', 'kg', 3),
	('50503681-b246-11eb-9957-98039b06df68', 'std_parcel_ae', 'AE', 'dim.length.value <= 45 && dim.width.value <= 34 && dim.height.value <= 26 && dim.weight.value <= 12', 'cm', 'kg', 4),
	('5052f2e7-b246-11eb-9957-98039b06df68', 'oversize_ae', 'AE', 'dim.length.value > 45 || dim.width.value > 34 || dim.height.value > 26 || dim.weight.value >= 30', 'cm', 'kg', 5),
	('5932658e-b246-11eb-9957-98039b06df68', 'std_sa', 'SA', 'dim.weight.value <= 12 && dim.length.value <= 45 && dim.width.value <= 34 && dim.height.value <= 26', NULL, NULL, 1),
	('5936fb86-b246-11eb-9957-98039b06df68', 'oversize_sa', 'SA', 'dim.weight.value > 12 || dim.length.value > 45 || dim.width.value > 34 || dim.height.value > 26', NULL, NULL, 2),
	('5a485465-73dc-11ea-b1ed-00e04c023f0e', 'oversize8_jp', 'JP', 'dim.length.value+dim.width.value+dim.height.value <= 200  && dim.weight.value <= 40 ', NULL, NULL, 14),
	('6c166753-73cc-11ea-b1ed-00e04c023f0e', 'std4_jp', 'JP', 'dim.length.value <= 45 && dim.width.value <= 35 && dim.height.value <= 20 && dim.length.value+dim.width.value+dim.height.value <= 100  && dim.weight.value <= 9 ', NULL, NULL, 5),
	('73537674-63d2-11e8-a076-00e04c023f0e', 'lg_envelope_au', 'AU', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value <= 5 && dim.weight.value <= 1 && dim.weight.value > 0.5', NULL, NULL, 3),
	('780db480-a106-11e8-a23f-9cda3ec5dbc1', 'sml_light_env_sml', 'UK,DE,FR,IT,ES,NL,PL,SE', 'dim.length.value <= 20 && dim.width.value <= 15 && dim.height.value <= 1&& dim.weight.value <= 0.08', 'cm', 'kg', 100),
	('7b8ac222-63d2-11e8-a076-00e04c023f0e', 'std_envelope_au', 'AU', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value <= 2.5 && dim.weight.value <= 0.5', NULL, NULL, 2),
	('86be186e-cc09-11e6-bab5-00e04c023f0e', 'std_smpkg', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 'dim.length.value <= 20 && dim.width.value <= 15 && dim.height.value <= 1 && dim.weight.value <= 0.08', NULL, NULL, 1),
	('9172ea54-94a3-11e8-899c-00e04c023f0e', 'sml_in', 'IN', 'dim.length.value <= 30.48 && dim.width.value <= 20.32 && dim.height.value <= 10.16 && dim.weight.value <= 1', NULL, NULL, 1),
	('917700cc-94a3-11e8-899c-00e04c023f0e', 'std_in', 'IN', 'dim.length.value <= 50.80 && dim.width.value <= 40.64 && dim.height.value <= 25.40 && dim.weight.value <= 12', NULL, NULL, 2),
	('9179ac20-94a3-11e8-899c-00e04c023f0e', 'oversize_in', 'IN', 'dim.length.value >50.80 || dim.width.value >40.64 || dim.height.value >25.40 || (dim.weight.value >12&&dim.weight.value <= 30)', NULL, NULL, 3),
	('917c5b0d-94a3-11e8-899c-00e04c023f0e', 'oversize_heavy_in', 'IN', 'dim.length.value >50.80 || dim.width.value >40.64 || dim.height.value >25.40 || dim.weight.value > 30', NULL, NULL, 4),
	('946ff4e1-63d2-11e8-a076-00e04c023f0e', 'std_parcel_au', 'AU', 'dim.length.value <= 45 && dim.width.value <= 34 && dim.height.value <= 26 && dim.weight.value <= 12', NULL, NULL, 4),
	('97cbb896-f4bf-11e6-bd71-00e04c023f0e', 'small', 'JP', 'dim.length.value <= 25 && dim.width.value <= 18 && dim.height.value <= 2 && dim.weight.value <= 0.25 ', NULL, NULL, 1),
	('a7591e8c-63d2-11e8-a076-00e04c023f0e', 'sml_oversize_au', 'AU', 'dim.length.value <= 61 && dim.width.value <= 46 && dim.height.value <= 46 && dim.weight.value <= 2', NULL, NULL, 5),
	('ac3f5762-7251-11ea-b1ed-00e04c023f0e', 'envelope_mx', 'MX', 'dim.length.value <= 38 && dim.width.value <= 27 && dim.height.value <= 2 && dim.weight.value <= 0.5', NULL, NULL, 1),
	('ac40e8c5-7251-11ea-b1ed-00e04c023f0e', 'std_mx', 'MX', 'dim.length.value <= 45 && dim.width.value <= 35 && dim.height.value <= 20 && dim.weight.value <= 9', NULL, NULL, 2),
	('ac422bba-7251-11ea-b1ed-00e04c023f0e', 'oversize_mx', 'MX', 'dim.length.value > 45 || dim.width.value > 35 || dim.height.value > 20 || dim.weight.value > 9', NULL, NULL, 3),
	('aff2e9bf-ccdd-11e6-bab5-00e04c023f0e', 'oversize_lgoverpkg', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', '(dim.length.value <=175) && (dim.weight.value < 31.5 || dim.dimensionalWeight.value > 108)', NULL, NULL, 9),
	('b14fbbac-73dc-11ea-b1ed-00e04c023f0e', 'supersize1_jp', 'JP', 'dim.length.value+dim.width.value+dim.height.value <= 200  && dim.weight.value <= 50 ', NULL, NULL, 15),
	('b88cb5b0-63d2-11e8-a076-00e04c023f0e', 'std_oversize_au', 'AU', 'dim.length.value <= 105 && dim.width.value <= 60 && dim.height.value <= 60 && dim.weight.value <= 22', NULL, NULL, 6),
	('b89b83be-73dc-11ea-b1ed-00e04c023f0e', 'supersize2_jp', 'JP', 'dim.length.value+dim.width.value+dim.height.value <= 220  && dim.weight.value <= 50 ', NULL, NULL, 16),
	('c275791c-73dc-11ea-b1ed-00e04c023f0e', 'supersize3_jp', 'JP', 'dim.length.value+dim.width.value+dim.height.value <= 240  && dim.weight.value <= 50 ', NULL, NULL, 17),
	('c9722733-63d2-11e8-a076-00e04c023f0e', 'lg_oversize_au', 'AU', '(dim.length.value > 105 || dim.width.value > 60 || dim.height.value > 60) || dim.weight.value > 22', NULL, NULL, 7),
	('ca-1', 'envelope_ca', 'CA', 'dim.length.value <= 38 && dim.width.value <= 27 && dim.height.value <= 2 && dim.weight.value <= 0.5', NULL, NULL, 1),
	('ca-2', 'std_ca', 'CA', 'dim.length.value <= 45 && dim.width.value <= 35 && dim.height.value <= 20 && dim.weight.value <= 9', NULL, NULL, 2),
	('ca-3', 'oversize_sm_ca', 'CA', 'dim.length.value <152 && dim.width.value < 76 && dim.height.value < 76 && dim.weight.value < 32  && dim.length.value+dim.width.value*2+dim.height.value*2 <= 330', NULL, NULL, 3),
	('ca-4', 'oversize_mid_ca', 'CA', 'dim.length.value <270 && dim.width.value < 270 && dim.height.value < 270 && dim.weight.value < 68  && dim.length.value+dim.width.value*2+dim.height.value*2 <= 330', NULL, NULL, 4),
	('ca-5', 'oversize_lg_ca', 'CA', 'dim.length.value <270 && dim.width.value < 270 && dim.height.value < 270 && dim.weight.value < 68 && dim.length.value+dim.width.value*2+dim.height.value*2 <= 419', NULL, NULL, 5),
	('ca-6', 'oversize_special_ca', 'CA', 'dim.length.value >270 || dim.length.value+dim.width.value*2+dim.height.value*2 > 419 || dim.weight.value > 68', NULL, NULL, 6),
	('cc7264fb-73dc-11ea-b1ed-00e04c023f0e', 'supersize4_jp', 'JP', 'dim.length.value+dim.width.value+dim.height.value <= 260  && dim.weight.value <= 50 ', NULL, NULL, 18),
	('cdc527c1-a106-11e8-a23f-9cda3ec5dbc1', 'sml_light_env_std', 'UK,DE,FR,IT,ES,NL,PL,SE', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value <= 2.5 && dim.weight.value <= 0.21', 'cm', 'kg', 102),
	('d4b2f0ff-ccdc-11e6-bab5-00e04c023f0e', 'oversize_smoverpkg', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 'dim.length.value <= 61 && dim.width.value <= 46 && dim.height.value <= 46 && (dim.weight.value <= 1.76  || dim.dimensionalWeight.value < 25.82)', NULL, NULL, 7),
	('ee9829e5-3bb0-11ec-9957-98039b06df68', 'sml_light_env_std', 'UK,DE,FR,IT,ES,NL,PL,SE', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value <= 2.5 && dim.weight.value <=0.06', 'cm', 'kg', 101),
	('eea6b17b-3bac-11ec-9957-98039b06df68', 'sml_light_env_lg', 'UK,DE,FR,IT,ES,NL,PL,SE', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value <= 4 && dim.weight.value <= 0.96', 'cm', 'kg', 104),
	('extra_large_envelope_960_g_uk', 'std_overlgpkg', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value <= 6 && dim.weight.value <= 0.96', NULL, NULL, 4),
	('f6b5f04f-ccdc-11e6-bab5-00e04c023f0e', 'oversize_stdoverpkg', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', '(dim.length.value <= 120 && dim.width.value <= 60 && dim.height.value <= 60)  && (dim.weight.value <= 29.76 || dim.dimensionalWeight.value < 86.4)', NULL, NULL, 8),
	('large_envelope_960_g_uk', 'std_lgpkg', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value <= 4 && dim.weight.value <= 0.96', NULL, NULL, 3),
	('lg_envelop_tr', 'lg_envelop_tr', 'TR', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value < 5 && dim.weight.value <= 1000', 'cm', 'g', 3),
	('lg_oversize_tr', 'lg_oversize_tr', 'TR', '(dim.length.value >120 || dim.width.value > 60 || dim.height.value > 60) && dim.weight.value <= 30000', 'cm', 'g', 6),
	('lg_size_tr', 'lg_size_tr', 'TR', 'dim.length.value <=61  && dim.width.value <= 46 && dim.height.value <= 46 && dim.weight.value <= 4000', 'cm', 'g', 4),
	('light_large_oversize_us', 'light_large_oversize_us', 'US', 'dim.length.value <= 108 && (dim.girth.value + dim.length.value) <= 165 && (dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value) <= 150 && (string.contains(isMedia,\'0\') || string.contains(isMedia,\'1\'))', 'in', 'lb', 108),
	('light_large_standard_us1', 'light_large_standard_us', 'US', 'dim.length.value <= 18 && dim.width.value <= 14 && dim.height.value <= 8 && string.contains(isMedia,\'0\') && dim.weight.value  <= 1', 'in', 'lb', 103),
	('light_large_standard_us_20', 'light_large_standard_us', 'US', 'dim.length.value <= 18 && dim.width.value <= 14 && dim.height.value <= 8 && string.contains(isMedia,\'0\') && dim.weight.value > 1 && (dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value) <= 20', 'in', 'lb', 104),
	('light_large_standard_us_isMedia20', 'light_large_standard_us', 'US', 'dim.length.value <= 18 && dim.width.value <= 14 && dim.height.value <= 8 && string.contains(isMedia,\'1\') && dim.weight.value  <= 20', 'in', 'lb', 105),
	('light_medium_oversize_us', 'light_medium_oversize_us', 'US', 'dim.length.value <= 108 && (dim.girth.value + dim.length.value) <= 130 && (dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value) <= 150 && (string.contains(isMedia,\'0\') || string.contains(isMedia,\'1\'))', 'in', 'lb', 107),
	('light_sml_oversize_us60', 'light_sml_oversize_us', 'US', 'dim.length.value <= 60 && dim.width.value <= 30 && (dim.girth.value + dim.length.value) <= 130 && (dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value) <= 70 && (string.contains(isMedia,\'0\') || string.contains(isMedia,\'1\'))', 'in', 'lb', 106),
	('light_sml_standard_us', 'light_sml_standard_us', 'US', 'dim.length.value <= 15 && dim.width.value <= 12&& dim.height.value <=0.75 && string.contains(isMedia,\'0\') && dim.weight.value  <= 1', 'in', 'lb', 102),
	('light_sml_standard_us_isMedia', 'light_sml_standard_us', 'US', 'dim.length.value <= 15 && dim.width.value <= 12 && dim.height.value <= 0.75 && string.contains(isMedia,\'1\') && dim.weight.value  <= 1', 'in', 'lb', 101),
	('light_special_oversize_us', 'light_special_oversize_us', 'US', 'dim.length.value > 108 || (dim.girth.value + dim.length.value) > 165 || (dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value) > 150 || string.contains(isMedia,\'2\')', 'in', 'lb', 109),
	('oversize_spoverpkg_uk', 'oversize_spoverpkg', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 'dim.length.value > 175 ||  (dim.length.value + dim.length.value*2 + dim.width.value*2) > 360 || dim.weight.value >31.5', NULL, NULL, 10),
	('small_parcel_uk', 'std_smpl', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 'dim.length.value < 35 && dim.width.value < 25 && dim.height.value < 12 && (dim.weight.value <= 3.9 || dim.dimensionalWeight.value < 3.9)', NULL, NULL, 5),
	('sml_envelop_tr', 'sml_envelop_tr', 'TR', 'dim.length.value <= 20 && dim.width.value <= 15 && dim.height.value <= 1', 'cm', 'g', 1),
	('sml_light_env_std_3', 'sml_light_env_std', 'UK,DE,FR,IT,ES,NL,PL,SE', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value <= 2.5 && dim.weight.value <=0.46', 'cm', 'kg', 103),
	('sml_light_jp_1000', 'sml_light_jp_1000', 'JP', 'dim.length.value <= 35 && dim.width.value <= 30 && dim.height.value <= 3.3 && dim.weight.value <= 1', 'cm', 'kg', 102),
	('sml_light_jp_250', 'sml_light_jp_250', 'JP', 'dim.length.value <= 25 && dim.width.value <= 18 && dim.height.value <= 2 && dim.weight.value <= 0.25', 'cm', 'kg', 101),
	('standard_parcel_uk', 'std_stdpkg', 'UK,DE,FR,IT,ES,NL,PL,SE,BE', 'dim.length.value <= 45 && dim.width.value <= 34 && dim.height.value <= 26 && (dim.weight.value <= 11.9 || dim.dimensionalWeight.value < 11.90)', NULL, NULL, 6),
	('std_envelop_tr', 'std_envelop_tr', 'TR', 'dim.length.value <= 33 && dim.width.value <= 23 && dim.height.value < 2.5 && dim.weight.value <= 500', 'cm', 'g', 2),
	('std_large_oversize_us', 'large_oversize_us', 'US', 'dim.length.value <= 108 && (dim.girth.value + dim.length.value) <= 165 && (dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value) <= 150 && (string.contains(isMedia,\'0\') || string.contains(isMedia,\'1\'))', 'in', 'lb', 8),
	('std_large_standard_us', 'large_standard_us', 'US', 'dim.length.value <= 18 && dim.width.value <= 14 && dim.height.value <= 8 && string.contains(isMedia,\'0\') && dim.weight.value  <= 1', 'in', 'lb', 3),
	('std_large_standard_us1', 'large_standard_us', 'US', 'dim.length.value <= 18 && dim.width.value <= 14 && dim.height.value <= 8 && string.contains(isMedia,\'0\') && dim.weight.value > 1 && (dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value) <= 20', 'in', 'lb', 4),
	('std_large_standard_us_ismedia', 'large_standard_us', 'US', 'dim.length.value <= 18 && dim.width.value <= 14 && dim.height.value <= 8 && string.contains(isMedia,\'1\') && dim.weight.value  <= 20', 'in', 'lb', 5),
	('std_medium_oversize_us', 'medium_oversize_us', 'US', 'dim.length.value <= 108 && (dim.girth.value + dim.length.value) <= 130 && (dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value) <= 150 && (string.contains(isMedia,\'0\') || string.contains(isMedia,\'1\'))', 'in', 'lb', 7),
	('std_oversize_tr', 'std_oversize_tr', 'TR', 'dim.length.value <=120  && dim.width.value <= 60 && dim.height.value <= 60 && dim.weight.value <= 30000', 'cm', 'g', 5),
	('std_pkg_tr', 'std_pkg_tr', 'TR', 'dim.length.value <=45 && dim.width.value <= 34 && dim.height.value <= 26 && dim.weight.value <= 12000', 'cm', 'g', 4),
	('std_sml_oversize_us60', 'sml_oversize_us', 'US', 'dim.length.value <= 60 && dim.width.value <= 30 && (dim.girth.value + dim.length.value) <= 130 && (dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value) <= 70 && (string.contains(isMedia,\'0\') || string.contains(isMedia,\'1\'))', 'in', 'lb', 6),
	('std_sml_standard_us', 'sml_standard_us', 'US', 'dim.length.value <= 15 && dim.width.value <= 12&& dim.height.value <=0.75 && string.contains(isMedia,\'0\') && dim.weight.value  <= 1', 'in', 'lb', 2),
	('std_sml_standard_us_ismedia', 'sml_standard_us', 'US', 'dim.length.value <= 15 && dim.width.value <= 12 && dim.height.value <= 0.75 && string.contains(isMedia,\'1\') && dim.weight.value  <= 1', 'in', 'lb', 1),
	('std_special_oversize_us', 'special_oversize_us', 'US', 'dim.length.value > 108 || (dim.girth.value + dim.length.value) > 165 || (dim.weight.value > dim.dimensionalWeight.value ? dim.weight.value : dim.dimensionalWeight.value) > 150 || string.contains(isMedia,\'2\')', 'in', 'lb', 9);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

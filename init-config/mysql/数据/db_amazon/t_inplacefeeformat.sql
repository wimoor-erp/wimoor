INSERT INTO `t_inplacefeeformat` (`id`, `inplacefeeid`, `producttierId`, `standard`, `format`, `description`, `country`) VALUES
	(1, 'one', NULL, b'1', 'weight>2? "0.30+0.10*({0}-2)" :"0.30"', '是否是标准尺寸，标准尺寸：0.30 美元 + 0.10 美元/磅（超出首重 2 磅的部分）;超大尺寸：1.65 美元 + 0.20 美元/磅（超出首重 5 磅的部分）', 'US'),
	(2, 'three', NULL, b'1', '0', '免费', 'US'),
	(3, 'two', NULL, b'1', 'weight>2? "0.10+0.10*({0}-2)" :"0.10"', '是否是标准尺寸，标准尺寸：0.10 美元 + 0.10 美元/磅（超出首重 2 磅的部分）；超大尺寸：0.50 美元 + 0.20 美元/磅（超出首重 5 磅的部分）', 'US'),
	(4, 'uncalculate', NULL, NULL, '0', '不参与计算', ''),
	(5, 'one', NULL, b'0', 'weight>2? "1.65+0.20*({0}-5)" :"1.65"', NULL, 'US'),
	(6, 'two', NULL, b'0', 'weight>2? "0.50+0.20*({0}-5)" :"0.50"', NULL, 'US'),
	(7, 'three', NULL, b'0', '0', NULL, 'US'),
	(8, 'calculate', 'small', b'0', '5', NULL, 'JP'),
	(9, 'calculate', 'standard', b'0', '8', NULL, 'JP'),
	(10, 'calculate', 'oversize1', b'0', '9', NULL, 'JP'),
	(11, 'calculate', 'oversize2', b'0', '9', NULL, 'JP'),
	(12, 'calculate', 'oversize3', b'0', '9', NULL, 'JP'),
	(13, 'calculate', 'special_large', b'0', '9', NULL, 'JP');
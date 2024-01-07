

-- 导出  表 db_erp.t_dimensions 结构
CREATE TABLE IF NOT EXISTS `t_dimensions` (
  `id` bigint(20) unsigned NOT NULL,
  `length` decimal(15,2) DEFAULT NULL,
  `length_units` char(15)  DEFAULT NULL,
  `width` decimal(15,2) DEFAULT NULL,
  `width_units` char(15)  DEFAULT NULL,
  `height` decimal(15,2) DEFAULT NULL,
  `height_units` char(15)  DEFAULT NULL,
  `weight` decimal(15,2) DEFAULT NULL,
  `weight_units` char(15)  DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB   ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_assembly 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `mainmid` bigint(20) unsigned DEFAULT NULL COMMENT '主产品',
  `submid` bigint(20) unsigned DEFAULT NULL COMMENT '子产品',
  `subnumber` int(11) DEFAULT NULL,
  `remark` varchar(200)  DEFAULT NULL COMMENT '备注',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `mainmid` (`mainmid`,`submid`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_assembly_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36)  DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `planitem` bigint(20) unsigned DEFAULT NULL,
  `ftype` char(10)  DEFAULT NULL COMMENT '组装=ass, 拆分=dis',
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `mainmid` bigint(20) unsigned DEFAULT NULL COMMENT '主sku id',
  `amount` int(11) DEFAULT NULL,
  `amount_handle` int(11) unsigned DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL COMMENT '0：未提交，1：待组装，2 组装中，3 已完成，4 已终止, 5已作废',
  `remark` varchar(500)  DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number` (`shopid`,`number`),
  KEY `mainmid` (`mainmid`),
  KEY `createdate` (`shopid`,`opttime`) USING BTREE,
  KEY `shopid_createdate` (`shopid`,`createdate`),
  KEY `auditstatus` (`auditstatus`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_assembly_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT '0',
  `subnumber` int(11) DEFAULT '0',
  `whamount` int(11) DEFAULT '0' COMMENT '仓库调出量',
  `phamount` int(11) DEFAULT '0' COMMENT '采购量',
  `phedamount` int(11) DEFAULT '0' COMMENT '已经采购数量',
  `purchase_from_entry_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`formid`,`materialid`),
  UNIQUE KEY `indexunique` (`id`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_assembly_from_instock 结构
CREATE TABLE IF NOT EXISTS `t_erp_assembly_from_instock` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `shipmentid` char(15)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`),
  KEY `idx_formid_shipmentid` (`formid`,`shipmentid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_changewh_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_changewh_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36)  DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL COMMENT '0：未提交，1：提交未审核，2：已审核',
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`),
  KEY `warehouseid` (`warehouseid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_changewh_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_changewh_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `material_from` bigint(20) unsigned DEFAULT NULL,
  `material_to` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `formid` (`formid`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_customer 结构
CREATE TABLE IF NOT EXISTS `t_erp_customer` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID(h)',
  `name` char(50)  DEFAULT NULL COMMENT '客户简称',
  `number` char(50)  DEFAULT NULL COMMENT '客户编码',
  `fullname` varchar(200)  DEFAULT NULL COMMENT '客户全称',
  `ftype` char(10)  DEFAULT NULL COMMENT '客户分类',
  `contacts` varchar(50)  DEFAULT NULL COMMENT '联系人',
  `phone_num` varchar(50)  DEFAULT NULL COMMENT '联系电话',
  `contact_info` varchar(2000)  DEFAULT NULL COMMENT '其它联系信息',
  `address` varchar(500)  DEFAULT NULL COMMENT '地址',
  `shoplink` varchar(500)  DEFAULT NULL COMMENT '商品链接',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '所属店铺（公司）(h)',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_shopid` (`shopid`,`name`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_dispatch_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36)  DEFAULT NULL COMMENT '调拨单号',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `from_warehouseid` bigint(20) unsigned DEFAULT NULL,
  `to_warehouseid` bigint(20) unsigned DEFAULT NULL,
  `ftype` tinyint(3) unsigned DEFAULT '0' COMMENT '0:货物调度，1，报废，2，修复，3，送检，4，验收入库,5海外发货，6海外退货',
  `creator` bigint(20) unsigned DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_dispatch_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_dispatch_oversea_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_oversea_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36)  DEFAULT NULL COMMENT '调拨单号',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `from_warehouseid` bigint(20) unsigned DEFAULT NULL,
  `to_warehouseid` bigint(20) unsigned DEFAULT NULL,
  `country` char(5)  DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  `auditstatus` int(10) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `number_shopid` (`shopid`,`number`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_dispatch_oversea_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_oversea_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `sellersku` varchar(50)  DEFAULT NULL,
  `fnsku` varchar(50)  DEFAULT NULL,
  `amount` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `formid` (`formid`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_dispatch_oversea_trans 结构
CREATE TABLE IF NOT EXISTS `t_erp_dispatch_oversea_trans` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `company` bigint(20) unsigned DEFAULT NULL,
  `channel` bigint(20) unsigned DEFAULT NULL,
  `singleprice` decimal(10,4) DEFAULT NULL,
  `transweight` decimal(10,4) DEFAULT NULL,
  `wunit` char(10)  DEFAULT NULL,
  `otherfee` decimal(10,4) DEFAULT NULL,
  `ordernum` char(50)  DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  `outarrtime` datetime DEFAULT NULL,
  `inarrtime` datetime DEFAULT NULL,
  `wtype` tinyint(3) DEFAULT '0',
  `transtype` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shipmentid` (`formid`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_fin_account 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_account` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `paymeth` int(10) unsigned DEFAULT '1',
  `name` char(50)  DEFAULT '默认',
  `isdefault` bit(1) DEFAULT b'0',
  `isdelete` bit(1) DEFAULT b'0',
  `balance` decimal(18,4) DEFAULT NULL COMMENT '账户余额',
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`,`paymeth`,`name`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC COMMENT='账户表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_fin_journalaccount 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_journalaccount` (
  `id` bigint(20) unsigned NOT NULL,
  `acct` bigint(20) unsigned NOT NULL,
  `ftype` char(36)  NOT NULL COMMENT '记账类型:out,支出；in,收入',
  `projectid` bigint(20) unsigned DEFAULT NULL,
  `amount` decimal(18,4) DEFAULT NULL,
  `balance` decimal(18,4) DEFAULT NULL,
  `remark` varchar(2000)  DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creator` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `shopid` bigint(20) unsigned NOT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_fin_journaldaily 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_journaldaily` (
  `id` bigint(20) unsigned NOT NULL,
  `acct` bigint(20) unsigned DEFAULT NULL COMMENT '账户id',
  `byday` date DEFAULT NULL,
  `rec` decimal(18,4) DEFAULT NULL COMMENT '收入',
  `pay` decimal(18,4) DEFAULT NULL COMMENT '支出',
  `balance` decimal(18,4) DEFAULT NULL COMMENT '余额',
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `byday` (`byday`,`acct`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC COMMENT='流水_日账单';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_fin_project 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_project` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(50)  DEFAULT NULL,
  `issys` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是系统项目名称',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_shopid` (`name`,`shopid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC COMMENT='流水账_类型';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_fin_type_journalmonthly 结构
CREATE TABLE IF NOT EXISTS `t_erp_fin_type_journalmonthly` (
  `id` bigint(20) unsigned NOT NULL,
  `projectid` bigint(20) unsigned DEFAULT NULL COMMENT '项目id',
  `acct` bigint(20) unsigned DEFAULT NULL COMMENT '账户id',
  `year` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `rec` decimal(18,4) DEFAULT NULL,
  `pay` decimal(18,4) DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`projectid`,`acct`,`year`,`month`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC COMMENT='流水_月账单 类型统计';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_formtype 结构
CREATE TABLE IF NOT EXISTS `t_erp_formtype` (
  `id` char(20)  NOT NULL,
  `name` char(50)  DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory` (
  `id` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `quantity` int(11) DEFAULT '0',
  `status` char(36)  NOT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_w_s_m_s` (`shopid`,`warehouseid`,`materialid`,`status`),
  KEY `FK_t_erp_inventory_t_erp_material` (`materialid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_his` (
  `id` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `quantity` int(11) DEFAULT '0',
  `status` char(36)  NOT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `modifyday` date NOT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_erp_inventory_t_erp_material` (`warehouseid`,`materialid`) USING BTREE,
  KEY `mykey` (`shopid`,`materialid`,`warehouseid`) USING BTREE,
  KEY `modifyday` (`modifyday`),
  KEY `status` (`status`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory_his_day 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_his_day` (
  `shopid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `byday` date NOT NULL,
  `inbound` int(10) DEFAULT '0',
  `fulfillable` int(10) DEFAULT '0',
  `outbound` int(10) DEFAULT '0',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`shopid`,`warehouseid`,`byday`,`materialid`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory_month_summary 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_month_summary` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `month` date NOT NULL,
  `startqty` int(11) DEFAULT NULL,
  `endqty` int(11) DEFAULT NULL,
  `shipment` int(11) DEFAULT NULL,
  `dispatch` int(11) DEFAULT NULL,
  `assembly` int(11) DEFAULT NULL,
  `purchase` int(11) DEFAULT NULL,
  `otherout` int(11) DEFAULT NULL,
  `otherin` int(11) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  `period` decimal(15,4) DEFAULT NULL,
  `turndays` decimal(15,4) DEFAULT NULL,
  `diff` int(11) DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `materialid_warehouseid` (`shopid`,`warehouseid`,`materialid`,`month`),
  KEY `month` (`month`)
) ENGINE=InnoDB    COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_record` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `formoptid` bigint(20) unsigned DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `status` char(36)  DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `invqty` int(11) DEFAULT NULL,
  `startinbound` int(11) DEFAULT NULL,
  `inbound` int(11) DEFAULT NULL,
  `endinbound` int(11) DEFAULT NULL,
  `startfulfillable` int(11) DEFAULT NULL,
  `fulfillable` int(11) DEFAULT NULL,
  `endfulfillable` int(11) DEFAULT NULL,
  `startoutbound` int(11) DEFAULT NULL,
  `outbound` int(11) DEFAULT NULL,
  `endoutbound` int(11) DEFAULT NULL,
  `formtype` char(20)  DEFAULT NULL,
  `operate` char(10)  DEFAULT NULL COMMENT 'in,out,readyin,readyout,cancel,stop',
  `number` char(36)  DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`opttime`,`id`) USING BTREE,
  KEY `Index 2` (`warehouseid`,`materialid`,`status`) USING BTREE,
  KEY `索引 3` (`materialid`,`status`),
  KEY `number` (`shopid`,`number`),
  KEY `shopid_opttime` (`shopid`,`opttime`) USING BTREE,
  KEY `formtype` (`shopid`,`formtype`,`opttime`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory_record_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_record_form` (
  `shopid` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned NOT NULL,
  `number` char(36)  DEFAULT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `startinbound` int(10) DEFAULT '0',
  `inbound` int(10) DEFAULT '0',
  `endinbound` int(10) DEFAULT '0',
  `startfulfillable` int(10) DEFAULT '0',
  `fulfillable` int(10) DEFAULT '0',
  `endfulfillable` int(10) DEFAULT '0',
  `startoutbound` int(10) DEFAULT '0',
  `outbound` int(10) DEFAULT '0',
  `endoutbound` int(10) DEFAULT '0',
  `formtype` char(20)  DEFAULT NULL,
  `operate` char(10)  DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime NOT NULL,
  KEY `shopid_formtype` (`shopid`,`formtype`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inventory_status 结构
CREATE TABLE IF NOT EXISTS `t_erp_inventory_status` (
  `id` char(36)  DEFAULT NULL,
  `code` char(36)  DEFAULT NULL,
  `name` char(36)  DEFAULT NULL
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inwh_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_inwh_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36)  DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL COMMENT '0：未提交，1：提交未审核，2：已审核',
  `remark` varchar(1000)  DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_inwh_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_inwh_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_material` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID(h)',
  `sku` varchar(50)  DEFAULT NULL COMMENT 'SKU',
  `name` varchar(500)  DEFAULT NULL COMMENT '名称',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID(h)',
  `upc` char(30)  DEFAULT NULL COMMENT '条码',
  `brand` char(50)  DEFAULT NULL COMMENT '品牌',
  `image` bigint(20) unsigned DEFAULT NULL COMMENT '图片',
  `itemDimensions` bigint(20) unsigned DEFAULT NULL COMMENT '产品尺寸',
  `pkgDimensions` bigint(20) unsigned DEFAULT NULL COMMENT '带包装尺寸',
  `boxDimensions` bigint(20) unsigned DEFAULT NULL,
  `boxnum` int(10) unsigned DEFAULT NULL,
  `specification` varchar(100)  DEFAULT NULL COMMENT '规格',
  `supplier` bigint(20) unsigned DEFAULT NULL COMMENT '供应商',
  `badrate` float DEFAULT NULL COMMENT '不良率',
  `vatrate` float DEFAULT NULL COMMENT '退税率',
  `productCode` char(36)  DEFAULT NULL COMMENT '供应商产品代码',
  `delivery_cycle` int(11) DEFAULT NULL COMMENT '供货周期',
  `other_cost` decimal(10,2) DEFAULT NULL,
  `MOQ` int(11) unsigned DEFAULT '0' COMMENT '起订量：minimum order quantity',
  `purchaseUrl` varchar(1000)  DEFAULT NULL COMMENT '采购链接',
  `remark` varchar(2000)  DEFAULT NULL COMMENT '备注',
  `categoryid` char(36)  DEFAULT NULL COMMENT '类型id',
  `issfg` char(1)  DEFAULT '0' COMMENT '0:单独成品；1：组装成品；2：半成品',
  `color` char(10)  DEFAULT '0',
  `owner` bigint(20) unsigned DEFAULT '0',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `price` decimal(10,2) DEFAULT NULL,
  `price_wavg` decimal(10,2) DEFAULT NULL,
  `price_ship_wavg` decimal(10,2) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `parentid` char(36)  DEFAULT NULL COMMENT '用于导入数据是引用的系统内的那个SKU产品',
  `effectivedate` datetime DEFAULT NULL,
  `isSmlAndLight` bit(1) DEFAULT b'0' COMMENT '是否轻小产品',
  `assembly_time` int(11) DEFAULT NULL COMMENT '组装时间',
  `isDelete` bit(1) DEFAULT NULL,
  `mtype` int(11) DEFAULT '0',
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `skurepeat` (`sku`,`shopid`) USING BTREE,
  KEY `supplier` (`supplier`) USING BTREE,
  KEY `FK_t_erp_material_t_erp_material_sku` (`sku`,`shopid`,`isDelete`) USING BTREE,
  KEY `categoryid` (`categoryid`) USING BTREE,
  KEY `opttime` (`shopid`,`opttime`) USING BTREE,
  KEY `shop_delete_sku_color` (`shopid`,`isDelete`,`mtype`,`sku`,`color`) USING BTREE,
  KEY `Index 4` (`shopid`,`owner`,`color`,`sku`,`id`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_brand 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_brand` (
  `id` char(36)  NOT NULL,
  `name` char(100)  DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`name`,`shopid`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_category 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_category` (
  `id` char(36)  NOT NULL,
  `name` char(100)  DEFAULT NULL,
  `number` char(50)  DEFAULT NULL,
  `color` char(10)  DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`name`,`shopid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_consumable 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_consumable` (
  `id` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `submaterialid` bigint(20) unsigned NOT NULL,
  `amount` decimal(10,4) unsigned DEFAULT '0.0000',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `materialid_submaterialid` (`materialid`,`submaterialid`) USING BTREE,
  KEY `submaterialid` (`submaterialid`)
) ENGINE=InnoDB      COMMENT='耗材表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_consumable_inventory 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_consumable_inventory` (
  `id` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `quantity` decimal(10,6) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_consumable_safety_stock 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_consumable_safety_stock` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `shopid` bigint(20) unsigned NOT NULL,
  `amount` int(10) unsigned NOT NULL DEFAULT '0',
  `operator` bigint(20) unsigned NOT NULL DEFAULT '0',
  `opttime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB AUTO_INCREMENT=1738145287272177666 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_customs 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_customs` (
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `name_en` varchar(500)  DEFAULT NULL COMMENT '产品英文名',
  `name_cn` varchar(500)  DEFAULT NULL COMMENT '产品中文名',
  `material` char(50)  DEFAULT NULL COMMENT '产品材质',
  `model` char(50)  DEFAULT NULL COMMENT '产品型号',
  `customs_code` char(50)  DEFAULT NULL COMMENT '海关编码',
  `currency` char(5)  DEFAULT NULL,
  `material_use` char(50)  DEFAULT NULL COMMENT '用途',
  `brand` char(50)  DEFAULT NULL COMMENT '产品品牌',
  `iselectricity` bit(1) DEFAULT b'0' COMMENT '是否带电/磁',
  `isdanger` bit(1) DEFAULT b'0' COMMENT '是否危险品',
  `unitprice` decimal(10,2) DEFAULT NULL COMMENT '申报单价',
  `addfee` decimal(10,2) DEFAULT NULL COMMENT '附加费用',
  `matreialid` bigint(20) unsigned DEFAULT NULL,
  KEY `materialid` (`materialid`) USING BTREE,
  KEY `matreialid` (`matreialid`)
) ENGINE=InnoDB      COMMENT='海关表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_customs_file 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_customs_file` (
  `id` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `filename` varchar(500)  DEFAULT NULL,
  `filepath` varchar(1000)  DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB    COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_customs_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_customs_item` (
  `materialid` bigint(20) unsigned NOT NULL,
  `country` char(10) NOT NULL COMMENT 'DE UK FR',
  `code` char(10) DEFAULT NULL,
  `fee` decimal(10,2) DEFAULT NULL,
  `taxrate` decimal(10,2) DEFAULT NULL,
  `currency` char(50) DEFAULT 'CNY',
  PRIMARY KEY (`materialid`,`country`) USING BTREE
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_group 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_group` (
  `materialid` bigint(20) unsigned NOT NULL,
  `groupid` char(36)  NOT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`materialid`,`groupid`),
  KEY `FK_t_erp_material_category_t_erp_category` (`groupid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_his` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID(h)',
  `sku` varchar(50)  DEFAULT NULL COMMENT 'SKU',
  `name` varchar(500)  DEFAULT NULL COMMENT '名称',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID(h)',
  `upc` char(30)  DEFAULT NULL COMMENT '条码',
  `brand` char(50)  DEFAULT NULL COMMENT '品牌',
  `image` bigint(20) unsigned DEFAULT NULL COMMENT '图片',
  `itemDimensions` char(36)  DEFAULT NULL COMMENT '产品尺寸',
  `pkgDimensions` char(36)  DEFAULT NULL COMMENT '带包装尺寸',
  `boxDimensions` bigint(20) unsigned DEFAULT NULL,
  `boxnum` int(10) unsigned DEFAULT NULL,
  `specification` varchar(100)  DEFAULT NULL COMMENT '规格',
  `supplier` bigint(20) unsigned DEFAULT NULL COMMENT '供应商',
  `productCode` char(36)  DEFAULT NULL COMMENT '供应商产品代码',
  `delivery_cycle` int(11) DEFAULT NULL,
  `other_cost` decimal(10,2) DEFAULT NULL,
  `MOQ` int(11) DEFAULT '0' COMMENT '起订量：minimum order quantity',
  `purchaseUrl` varchar(1000)  DEFAULT NULL COMMENT '采购链接',
  `remark` varchar(2000)  DEFAULT NULL COMMENT '备注',
  `categoryid` char(36)  DEFAULT NULL COMMENT '类型id',
  `issfg` char(10)  DEFAULT '0' COMMENT '0:单独成品；1：组装成品；2：半成品',
  `color` char(10)  DEFAULT '0',
  `owner` bigint(20) unsigned DEFAULT '0',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `price` decimal(10,4) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `parentid` char(36)  DEFAULT NULL COMMENT '用于导入数据是引用的系统内的那个SKU产品',
  `effectivedate` datetime DEFAULT NULL,
  `isSmlAndLight` bit(1) DEFAULT b'0' COMMENT '是否轻小产品',
  `assembly_time` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`,`opttime`),
  UNIQUE KEY `Index 3` (`shopid`,`sku`,`opttime`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC COMMENT='t_erp_material_his历史表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_mark 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_mark` (
  `materialid` bigint(20) unsigned NOT NULL,
  `ftype` char(10)  NOT NULL COMMENT 'notice：产品出现问题时发布的公告',
  `mark` varchar(500)  DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`materialid`,`ftype`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_mark_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_mark_his` (
  `materialid` bigint(20) unsigned NOT NULL,
  `ftype` char(10)  NOT NULL COMMENT 'notice：产品出现问题时发布的公告',
  `mark` varchar(500)  DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`materialid`,`ftype`,`opttime`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_supplier 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_supplier` (
  `id` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `supplierid` bigint(20) unsigned NOT NULL,
  `purchaseUrl` varchar(1000)  DEFAULT NULL,
  `productCode` char(36)  DEFAULT NULL,
  `specId` char(36)  DEFAULT NULL,
  `offerid` char(36)  DEFAULT NULL,
  `otherCost` decimal(10,2) DEFAULT NULL,
  `deliverycycle` int(11) DEFAULT NULL,
  `isdefault` bit(1) NOT NULL DEFAULT b'0',
  `badrate` float DEFAULT '0',
  `MOQ` int(11) NOT NULL DEFAULT '0',
  `creater` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `materialid_supplierid` (`materialid`,`supplierid`) USING BTREE,
  KEY `productCode` (`productCode`),
  KEY `specId` (`specId`,`offerid`) USING BTREE
) ENGINE=InnoDB    COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_supplier_stepwise 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_supplier_stepwise` (
  `id` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `supplierid` bigint(20) unsigned NOT NULL,
  `currency` char(5)  DEFAULT '',
  `price` decimal(10,2) unsigned NOT NULL DEFAULT '0.00',
  `amount` int(10) unsigned NOT NULL DEFAULT '0',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `materialid` (`materialid`,`supplierid`,`amount`) USING BTREE
) ENGINE=InnoDB    COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_material_tags 结构
CREATE TABLE IF NOT EXISTS `t_erp_material_tags` (
  `mid` bigint(20) unsigned NOT NULL,
  `tagid` bigint(20) unsigned NOT NULL,
  `operator` bigint(20) unsigned NOT NULL,
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`mid`,`tagid`) USING BTREE,
  KEY `tagid` (`tagid`)
) ENGINE=InnoDB      COMMENT='产品-标签';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_m_group 结构
CREATE TABLE IF NOT EXISTS `t_erp_m_group` (
  `id` char(36)  NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `ftype` int(10) NOT NULL,
  `color` char(10)  DEFAULT NULL,
  `issys` bit(1) DEFAULT NULL,
  `name` varchar(50)  DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_outwh_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_outwh_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36)  DEFAULT NULL,
  `ftype` tinyint(4) DEFAULT '0' COMMENT '0:正品出库；1:废品出库，2:验收出库 ，3:海外出库',
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `purchaser` bigint(20) unsigned DEFAULT NULL COMMENT '发货客户',
  `customer` varchar(100)  DEFAULT NULL,
  `toaddress` varchar(500)  DEFAULT NULL COMMENT '发货地址',
  `express` varchar(500)  DEFAULT NULL COMMENT '物流快递',
  `expressno` char(50)  DEFAULT NULL COMMENT '快递编号',
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL,
  `remark` varchar(1000)  DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`),
  KEY `createdate` (`createdate`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_outwh_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_outwh_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_auth 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `name` char(36)  DEFAULT NULL,
  `access_token` varchar(500)  DEFAULT NULL,
  `refresh_token` varchar(500)  DEFAULT NULL,
  `resource_owner` varchar(50)  DEFAULT NULL,
  `aliId` bigint(20) unsigned DEFAULT NULL,
  `memberId` char(50)  DEFAULT NULL,
  `refresh_token_timeout` datetime DEFAULT NULL,
  `access_token_timeout` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `isDelete` bit(1) DEFAULT b'0',
  `appkey` varchar(255)  DEFAULT NULL,
  `appsecret` varchar(255)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shopid_name` (`shopid`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1743854621062909954    ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_contact 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_contact` (
  `id` char(30) NOT NULL,
  `customer` bigint(20) unsigned NOT NULL,
  `companyName` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `imInPlatform` varchar(100) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`customer`) USING BTREE,
  KEY `customer` (`customer`)
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_group 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `aliId` bigint(20) unsigned DEFAULT NULL,
  `name` char(36)  DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `appkey` varchar(255)  DEFAULT NULL,
  `appsecret` varchar(255)  DEFAULT NULL,
  `isDelete` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `aliId` (`aliId`,`shopid`) USING BTREE,
  KEY `shopid_name` (`shopid`,`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1724339670451511298      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_message 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_message` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `content` longtext NOT NULL,
  `signature` varchar(50) NOT NULL DEFAULT '',
  `opttime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7530   CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_order_baseinfo 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_order_baseinfo` (
  `id` bigint(20) unsigned NOT NULL,
  `idOfStr` char(30) NOT NULL DEFAULT '',
  `alipayTradeId` char(50) DEFAULT NULL,
  `allDeliveredTime` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `modifyTime` datetime DEFAULT NULL,
  `payTime` datetime DEFAULT NULL,
  `businessType` char(2) DEFAULT NULL,
  `status` char(10) DEFAULT NULL,
  `buyerFeedback` varchar(500) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `closeOperateType` char(50) DEFAULT NULL,
  `tradeType` char(10) DEFAULT NULL,
  `discount` int(11) DEFAULT NULL,
  `refund` int(11) DEFAULT NULL,
  `overSeaOrder` bit(1) DEFAULT NULL,
  `refundPayment` decimal(20,6) DEFAULT NULL,
  `shippingFee` decimal(20,6) DEFAULT NULL,
  `totalAmount` decimal(20,6) DEFAULT NULL,
  `sumProductPayment` decimal(20,6) DEFAULT NULL,
  `flowTemplateCode` char(50) DEFAULT NULL,
  `buyerID` char(50) DEFAULT NULL,
  `sellerID` char(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_order_productitems 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_order_productitems` (
  `skuID` bigint(20) unsigned NOT NULL,
  `orderid` bigint(20) unsigned NOT NULL,
  `entryid` bigint(20) unsigned NOT NULL,
  `itemAmount` int(10) unsigned NOT NULL DEFAULT '0',
  `price` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `entryDiscount` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `status` char(50)  NOT NULL DEFAULT '0.000000',
  `statusStr` char(50)  NOT NULL DEFAULT '0.000000',
  `skuInfos` varchar(500)  NOT NULL DEFAULT '',
  `productID` bigint(20) unsigned NOT NULL,
  `productCargoNumber` char(50)  NOT NULL DEFAULT '',
  `name` varchar(500)  NOT NULL DEFAULT '0',
  `logisticsStatus` tinyint(4) NOT NULL DEFAULT '0',
  `productSnapshotUrl` varchar(100)  NOT NULL DEFAULT '0',
  `productImgUrl` varchar(100)  NOT NULL DEFAULT '0',
  `unit` varchar(10)  NOT NULL DEFAULT '0',
  `refundStatus` varchar(30)  NOT NULL DEFAULT '0',
  `gmtCreate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `gmtModified` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`orderid`,`skuID`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_order_receiverinfo 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_order_receiverinfo` (
  `orderid` bigint(20) unsigned DEFAULT NULL,
  `toArea` varchar(200) DEFAULT NULL,
  `toDivisionCode` char(50) DEFAULT NULL,
  `toFullName` char(50) DEFAULT NULL,
  `toMobile` char(50) DEFAULT NULL,
  `toPost` char(50) DEFAULT NULL
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_order_tradeterms 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_order_tradeterms` (
  `orderid` bigint(20) unsigned NOT NULL,
  `payStatus` char(20) NOT NULL,
  `payTime` datetime DEFAULT NULL,
  `payway` char(20) DEFAULT NULL,
  `phasAmount` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `cardPay` bit(1) NOT NULL DEFAULT b'0',
  `expressPay` bit(1) NOT NULL DEFAULT b'0',
  `payWayDesc` char(50) DEFAULT NULL
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_productitems 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_productitems` (
  `productID` char(20) NOT NULL DEFAULT '',
  `specId` char(36) NOT NULL DEFAULT '',
  `offerid` char(36) NOT NULL DEFAULT '',
  `price` decimal(20,6) NOT NULL DEFAULT '0.000000',
  `name` varchar(500) NOT NULL DEFAULT '0',
  `productSnapshotUrl` varchar(100) NOT NULL DEFAULT '0',
  `productImgUrl` varchar(100) NOT NULL DEFAULT '0',
  `unit` varchar(5) NOT NULL DEFAULT '0',
  `skuInfos` varchar(200) NOT NULL DEFAULT '0',
  PRIMARY KEY (`productID`) USING BTREE,
  UNIQUE KEY `specId_offerid` (`specId`,`offerid`)
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_settlement 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_settlement` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `acct` bigint(20) unsigned DEFAULT NULL,
  `alibaba_account` varchar(50)  DEFAULT NULL,
  `amount` decimal(20,6) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `postdate` date DEFAULT NULL,
  `paydate` date DEFAULT NULL,
  `payamount` decimal(20,6) DEFAULT NULL,
  `paytimes` int(11) DEFAULT NULL,
  `returnamount` decimal(20,6) DEFAULT NULL,
  `returntimes` int(11) DEFAULT NULL,
  `payreturnamount` decimal(20,6) DEFAULT NULL,
  `loaddate` datetime DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_acct_alibaba_account_postdate` (`shopid`,`acct`,`alibaba_account`,`postdate`) USING BTREE
) ENGINE=InnoDB    COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_settlement_order 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_settlement_order` (
  `orderid` char(20) CHARACTER SET utf8 NOT NULL,
  `settlementid` bigint(20) unsigned NOT NULL,
  `paytime` datetime DEFAULT NULL,
  `name` varchar(500)  DEFAULT NULL,
  `payamount` decimal(20,6) DEFAULT NULL,
  `confirmamount` decimal(20,6) DEFAULT NULL,
  `confirmtime` datetime DEFAULT NULL,
  `paytype` char(30)  DEFAULT NULL,
  `returntype` char(10)  DEFAULT NULL,
  `returnamount` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`orderid`) USING BTREE,
  KEY `settlementid` (`settlementid`) USING BTREE
) ENGINE=InnoDB    COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_settlement_order_return 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_settlement_order_return` (
  `orderid` char(20) CHARACTER SET utf8 NOT NULL,
  `settlementid` bigint(20) unsigned NOT NULL,
  `name` varchar(500)  DEFAULT NULL,
  `payamount` decimal(20,6) DEFAULT NULL,
  `returntime` datetime DEFAULT NULL,
  `returnamount` decimal(20,6) DEFAULT NULL,
  `returntype` char(10)  DEFAULT NULL,
  `returnto` char(30)  DEFAULT NULL,
  PRIMARY KEY (`orderid`) USING BTREE,
  KEY `settlementid` (`settlementid`) USING BTREE
) ENGINE=InnoDB    COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_settlement_pay 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_settlement_pay` (
  `id` bigint(20) unsigned NOT NULL,
  `settlementid` bigint(20) DEFAULT NULL,
  `paytime` datetime DEFAULT NULL,
  `paymethod` varchar(50)  DEFAULT NULL,
  `paytype` varchar(50)  DEFAULT NULL,
  `amount` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `settlementid` (`settlementid`) USING BTREE
) ENGINE=InnoDB      COMMENT='还款明细列表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_alibaba_settlement_pay_return 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_alibaba_settlement_pay_return` (
  `id` bigint(20) unsigned NOT NULL,
  `settlementid` bigint(20) DEFAULT NULL,
  `returntime` datetime DEFAULT NULL,
  `returntype` varchar(50)  DEFAULT NULL,
  `number` varchar(50)  DEFAULT NULL,
  `amount` varchar(50)  DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `settlementid` (`settlementid`) USING BTREE
) ENGINE=InnoDB      COMMENT='还款明细列表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_fin_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_fin_form` (
  `id` bigint(20) unsigned NOT NULL,
  `entryid` bigint(20) unsigned DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `auditstatus` int(10) unsigned DEFAULT '0' COMMENT '0,待审核 1已审核待付款 2已完成 3.已退回',
  `payment_method` int(10) unsigned DEFAULT NULL,
  `number` char(36) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_fin_form_payment 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_fin_form_payment` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned NOT NULL,
  `formentryid` bigint(20) unsigned DEFAULT NULL,
  `acct` bigint(20) unsigned DEFAULT NULL,
  `payprice` decimal(18,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(2000)  DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `projectid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `formentryid` (`formentryid`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36)  DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `purchaser` bigint(20) unsigned DEFAULT NULL,
  `batch` char(36)  DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_shopid` (`shopid`,`number`) USING BTREE,
  KEY `warehouseid_shopid` (`warehouseid`,`shopid`),
  KEY `createdate` (`createdate`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `supplier` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `itemprice` decimal(10,2) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL COMMENT '0:草稿，退回；  1:待审核  ；2:审核通过 ；3：已完成',
  `ischange` bit(1) DEFAULT b'0',
  `paystatus` int(11) DEFAULT NULL,
  `planitemid` bigint(20) unsigned DEFAULT NULL,
  `inwhstatus` int(11) DEFAULT NULL,
  `totalpay` decimal(10,2) DEFAULT '0.00',
  `totalre` int(11) DEFAULT '0',
  `totalin` int(11) DEFAULT '0',
  `totalch` int(11) DEFAULT '0',
  `deliverydate` datetime DEFAULT NULL,
  `closerecdate` datetime DEFAULT NULL COMMENT '入库结束时间',
  `closepaydate` datetime DEFAULT NULL COMMENT '付款结束时间',
  `remark` varchar(500)  DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formid_materialid_planitemid` (`formid`,`materialid`,`planitemid`),
  KEY `materialid` (`materialid`),
  KEY `auditstatus` (`auditstatus`),
  KEY `inwhstatus` (`inwhstatus`),
  KEY `paystatus` (`paystatus`),
  KEY `supplier` (`supplier`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry_alibabainfo 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_alibabainfo` (
  `entryid` bigint(20) unsigned NOT NULL,
  `alibaba_auth` bigint(20) unsigned DEFAULT NULL,
  `alibaba_orderid` bigint(20) unsigned DEFAULT NULL,
  `logistics_info` text COLLATE utf8mb4_bin,
  `logistics_trace_info` text COLLATE utf8mb4_bin,
  `order_info` longtext COLLATE utf8mb4_bin,
  `logistics_status` bit(1) DEFAULT b'0',
  `logistics_trace_status` bit(1) DEFAULT b'0',
  `order_status` char(30)  DEFAULT NULL,
  `order_refresh_time` datetime DEFAULT NULL,
  `logistics_refresh_time` datetime DEFAULT NULL,
  `logistics_trace_refresh_time` datetime DEFAULT NULL,
  `signinfo` varchar(100)  DEFAULT NULL,
  PRIMARY KEY (`entryid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry_alibabainfo_ext 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_alibabainfo_ext` (
  `entryid` bigint(20) unsigned NOT NULL,
  `alibaba_auth` bigint(20) unsigned DEFAULT NULL,
  `alibaba_orderid` bigint(20) unsigned DEFAULT NULL,
  `logistics_status` bit(1) DEFAULT b'0',
  `logistics_trace_status` bit(1) DEFAULT b'0',
  `order_status` char(30)  DEFAULT NULL,
  `order_refresh_time` datetime DEFAULT NULL,
  `logistics_refresh_time` datetime DEFAULT NULL,
  `logistics_trace_refresh_time` datetime DEFAULT NULL,
  `signinfo` varchar(100)  DEFAULT NULL,
  `paydate` datetime DEFAULT NULL,
  `countdate` datetime DEFAULT NULL,
  PRIMARY KEY (`entryid`) USING BTREE,
  KEY `signinfo` (`signinfo`) USING BTREE,
  KEY `paydate` (`paydate`) USING BTREE,
  KEY `alibaba_orderid` (`alibaba_orderid`) USING BTREE,
  KEY `countdate` (`countdate`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry_copy 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_copy` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `supplier` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `itemprice` decimal(10,2) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL COMMENT '0:草稿，退回；  1:待审核  ；2:审核通过 ；3：已完成',
  `paystatus` int(11) DEFAULT NULL,
  `planitemid` bigint(20) unsigned DEFAULT NULL,
  `inwhstatus` int(11) DEFAULT NULL,
  `totalpay` decimal(10,2) DEFAULT '0.00',
  `totalre` int(11) DEFAULT '0',
  `totalin` int(11) DEFAULT '0',
  `totalch` int(11) DEFAULT '0',
  `deliverydate` datetime DEFAULT NULL,
  `closerecdate` datetime DEFAULT NULL COMMENT '入库结束时间',
  `closepaydate` datetime DEFAULT NULL COMMENT '付款结束时间',
  `remark` varchar(500)  DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `formid_materialid_planitemid` (`formid`,`materialid`,`planitemid`) USING BTREE,
  KEY `materialid` (`materialid`) USING BTREE,
  KEY `auditstatus` (`auditstatus`) USING BTREE,
  KEY `inwhstatus` (`inwhstatus`) USING BTREE,
  KEY `paystatus` (`paystatus`) USING BTREE,
  KEY `supplier` (`supplier`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_history` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `supplier` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `amount` int(10) DEFAULT NULL,
  `itemprice` decimal(10,2) DEFAULT NULL,
  `audittime` datetime DEFAULT NULL,
  `auditor` bigint(20) unsigned DEFAULT NULL,
  `orderprice` decimal(10,2) DEFAULT NULL,
  `ischange` bit(1) DEFAULT b'0',
  `auditstatus` int(10) DEFAULT NULL COMMENT '0:草稿，退回；  1:待审核  ；2:审核通过 ；3：已完成,4.审核待下单',
  `paystatus` int(10) DEFAULT NULL,
  `planitemid` char(36)  DEFAULT NULL,
  `inwhstatus` int(10) DEFAULT NULL,
  `totalpay` decimal(10,2) DEFAULT '0.00',
  `totalre` int(10) DEFAULT '0',
  `totalin` int(10) DEFAULT '0',
  `totalch` int(10) DEFAULT '0',
  `deliverydate` datetime DEFAULT NULL,
  `closerecdate` datetime DEFAULT NULL COMMENT '入库结束时间',
  `closepaydate` datetime DEFAULT NULL COMMENT '付款结束时间',
  `remark` varchar(500)  DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  KEY `formid_materialid_planitemid` (`formid`,`materialid`,`planitemid`) USING BTREE,
  KEY `materialid` (`materialid`) USING BTREE,
  KEY `auditstatus` (`auditstatus`) USING BTREE,
  KEY `inwhstatus` (`inwhstatus`) USING BTREE,
  KEY `paystatus` (`paystatus`) USING BTREE,
  KEY `supplier` (`supplier`) USING BTREE,
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_entry_logistics 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_entry_logistics` (
  `entryid` bigint(20) unsigned NOT NULL,
  `logisticsId` char(25)  NOT NULL,
  `refreshtime` datetime DEFAULT NULL,
  PRIMARY KEY (`entryid`,`logisticsId`),
  KEY `logisticsId` (`logisticsId`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_payment 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_payment` (
  `id` bigint(20) unsigned NOT NULL,
  `formentryid` bigint(20) unsigned DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL COMMENT '1 已付款，2 请款中，0 驳回',
  `payment_method` int(11) DEFAULT NULL,
  `acct` bigint(20) unsigned DEFAULT NULL,
  `payprice` decimal(18,2) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `remark` varchar(2000)  DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `projectid` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formentryid` (`formentryid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_payment_method 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_payment_method` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(100)  DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT   COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_form_receive 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_form_receive` (
  `id` bigint(20) unsigned NOT NULL,
  `formentryid` bigint(20) unsigned DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `ftype` char(10)  DEFAULT NULL COMMENT 'in -入库，re -退货， ch-换货,clear-撤销入库',
  `amount` int(11) DEFAULT NULL,
  `remark` varchar(2000)  DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formentryid` (`formentryid`),
  KEY `opttime` (`opttime`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_plan 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_plan` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `number` char(36) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1' COMMENT '0-取消，1-工作中，2-提交',
  `creator` bigint(20) unsigned DEFAULT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `totalnum` int(11) DEFAULT NULL,
  `totalbuyqty` int(11) DEFAULT NULL,
  `totalpayprice` decimal(18,4) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_planitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planitem` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `subplanid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `status` tinyint(4) DEFAULT '1',
  `sales` int(11) DEFAULT '0',
  `amount` int(11) DEFAULT NULL,
  `itemprice` decimal(10,4) DEFAULT NULL,
  `orderprice` decimal(10,4) DEFAULT NULL,
  `supplier` char(36) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `isparent` bit(1) DEFAULT NULL,
  `parent` char(36) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`subplanid`,`materialid`,`warehouseid`),
  KEY `materialid` (`materialid`),
  KEY `idx_materialid_status` (`materialid`,`status`)
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_planitemsub 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planitemsub` (
  `id` bigint(20) unsigned DEFAULT NULL,
  `planitemid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `groupid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `warehouseid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `planamount` int(11) DEFAULT NULL COMMENT '实际发货量',
  PRIMARY KEY (`planitemid`,`warehouseid`,`groupid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC COMMENT='废表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_planmodel 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planmodel` (
  `planid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `modelid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `refreshtime` datetime DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  `operator` bigint(20) unsigned DEFAULT '0',
  PRIMARY KEY (`planid`)
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_planmodelitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planmodelitem` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `modelid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `planamount` int(11) DEFAULT NULL COMMENT '建议补货量',
  `itemprice` decimal(10,4) DEFAULT NULL,
  `invamount` int(11) DEFAULT NULL,
  `orderprice` decimal(10,4) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `modelid_materialid` (`modelid`,`materialid`),
  KEY `idx_materialid_itemprice_planamount_invamount` (`materialid`,`itemprice`,`planamount`,`invamount`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_planmodelitemsub 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_planmodelitemsub` (
  `itemid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `sku` char(50) NOT NULL,
  `marketplaceid` char(15) NOT NULL,
  `groupid` bigint(20) unsigned NOT NULL COMMENT '断货时间',
  `needship` int(11) NOT NULL,
  `salesday` int(11) DEFAULT NULL,
  `aftersalesday` int(11) DEFAULT NULL,
  PRIMARY KEY (`itemid`,`sku`,`marketplaceid`,`groupid`)
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_plansub 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_plansub` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `planid` bigint(20) unsigned DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '1代表在用，0代表放弃，2代表已提交',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `ftype` char(10)  DEFAULT NULL COMMENT 'po代表订单，ao代表组装单',
  PRIMARY KEY (`id`),
  KEY `planid` (`planid`,`status`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_plan_warahouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_plan_warahouse` (
  `warehouseid` bigint(20) unsigned NOT NULL,
  `planid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`warehouseid`),
  KEY `planid` (`planid`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB      COMMENT='入库仓库和补货规划的映射关系表，一个入库仓库不能在多个补货规划中出现，一个补货规划会有多个入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_warahouse_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_warahouse_material` (
  `planid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`planid`,`materialid`)
) ENGINE=InnoDB      COMMENT='记录每个sku在补货规划中所默认的入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_purchase_warahouse_status 结构
CREATE TABLE IF NOT EXISTS `t_erp_purchase_warahouse_status` (
  `warehouseid` bigint(20) unsigned NOT NULL,
  `purchase_status` int(5) DEFAULT '0' COMMENT '0表示改仓库无采购任务；1表示采购任务待处理；2表示采购任务已完成',
  `assbly_status` int(5) DEFAULT '0' COMMENT '0表示改仓库无组装任务；1表示组装任务待处理；2表示组装任务已完成',
  `userid` bigint(20) unsigned DEFAULT NULL,
  `opptime` datetime DEFAULT NULL,
  PRIMARY KEY (`warehouseid`)
) ENGINE=InnoDB      COMMENT='记录每个仓库补货规划的状态，操作人，日期';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_serial_num 结构
CREATE TABLE IF NOT EXISTS `t_erp_serial_num` (
  `id` char(36)  NOT NULL,
  `ftype` char(36)  DEFAULT NULL,
  `seqno` int(11) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `prefix_date` date DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`prefix_date`,`ftype`,`shopid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_address 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_address` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(50)  DEFAULT NULL COMMENT '名称或公司名称。',
  `groupid` bigint(20) unsigned DEFAULT NULL COMMENT '店铺id',
  `isfrom` bit(1) DEFAULT NULL COMMENT '1 代表发货地址，0代表收货地址',
  `addressLine1` varchar(300)  DEFAULT NULL COMMENT '街道地址信息。',
  `addressLine2` varchar(300)  DEFAULT NULL COMMENT '其他街道地址信息（如果需要）。',
  `city` char(50)  DEFAULT NULL COMMENT '城市',
  `districtOrCounty` char(25)  DEFAULT NULL COMMENT '区或县 ',
  `stateOrProvinceCode` char(20)  DEFAULT NULL COMMENT '省份代码',
  `countryCode` char(2)  DEFAULT NULL COMMENT '国家/地区代码',
  `postalCode` char(30)  DEFAULT NULL COMMENT '邮政编码',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `phone` char(30)  DEFAULT NULL,
  `isdefault` bit(1) DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_groupid_city` (`name`,`groupid`,`city`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_addressto 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_addressto` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(50)  DEFAULT NULL COMMENT '名称或公司名称。',
  `isfrom` bit(1) DEFAULT NULL COMMENT '1 代表发货地址，0代表收货地址',
  `addressLine1` varchar(300)  DEFAULT NULL COMMENT '街道地址信息。',
  `addressLine2` varchar(300)  DEFAULT NULL COMMENT '其他街道地址信息（如果需要）。',
  `city` char(30)  DEFAULT NULL COMMENT '城市',
  `districtOrCounty` char(25)  DEFAULT NULL COMMENT '区或县 ',
  `stateOrProvinceCode` char(40)  DEFAULT NULL COMMENT '省份代码',
  `countryCode` char(2)  DEFAULT NULL COMMENT '国家/地区代码',
  `postalCode` char(30)  DEFAULT NULL COMMENT '邮政编码',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `phone` char(30)  DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_inboundruntime 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_inboundruntime` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(36)  DEFAULT NULL,
  `put_on_days` int(11) DEFAULT NULL,
  `first_leg_days` int(11) DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_marketplaceid` (`shopid`,`marketplaceid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_plan 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_plan` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL COMMENT '自有仓ID（parentid）',
  `amazongroupid` bigint(20) unsigned NOT NULL,
  `totalnum` int(11) NOT NULL COMMENT 'sku数量',
  `totalamount` int(11) NOT NULL COMMENT '发货量= sum(单个sku发货量）',
  `goodsworth` decimal(10,4) DEFAULT NULL COMMENT '发货货值',
  `totalweight` decimal(10,4) DEFAULT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint(20) unsigned NOT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0代表放弃，1代表在用，2代表已提交',
  PRIMARY KEY (`id`),
  UNIQUE KEY `warehouseid_amazongroupid` (`warehouseid`,`amazongroupid`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_planitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planitem` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `plansubid` bigint(20) unsigned DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0代表已放弃，1 代表可用 2.代表已提交。如果plansub的status等于2这里的1 也是已提交',
  `sku` char(50)  DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL COMMENT '实际发货量',
  `selfamount` int(11) DEFAULT NULL,
  `goodsworth` decimal(15,4) DEFAULT NULL COMMENT '货物货值',
  `planweight` decimal(15,4) DEFAULT NULL COMMENT 'itemweight*amount',
  `dimweight` decimal(15,4) DEFAULT NULL COMMENT '材积',
  `needship` int(11) DEFAULT NULL COMMENT '建议发货量',
  PRIMARY KEY (`id`),
  KEY `Index 2` (`plansubid`,`sku`),
  KEY `status_materialid` (`materialid`,`status`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_planmodel 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planmodel` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `planid` bigint(20) unsigned DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `refreshtime` datetime DEFAULT NULL,
  `isrun` bit(1) DEFAULT b'0',
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `索引 2` (`planid`,`groupid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_planmodelitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planmodelitem` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `modelid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `planamount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`modelid`,`materialid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_planmodelitemsub 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_planmodelitemsub` (
  `itemid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `sku` char(50)  NOT NULL,
  `marketplaceid` char(15)  NOT NULL,
  `needship` int(11) NOT NULL,
  `short_time` datetime DEFAULT NULL COMMENT '断货时间',
  `salesday` int(11) DEFAULT NULL,
  `aftersalesday` int(11) DEFAULT NULL,
  PRIMARY KEY (`itemid`,`marketplaceid`,`sku`),
  KEY `索引 2` (`sku`,`marketplaceid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_plansub 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_plansub` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `marketplaceid` char(15)  DEFAULT NULL,
  `planid` bigint(20) unsigned DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0代表放弃，1代表在用，2代表已提交',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `planid` (`planid`,`status`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_plansub_euitem 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_plansub_euitem` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `planid` bigint(20) unsigned DEFAULT NULL,
  `sku` char(50)  DEFAULT NULL,
  `plansubid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `marketplaceid` char(20)  NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqid` (`plansubid`,`marketplaceid`,`sku`)
) ENGINE=InnoDB AUTO_INCREMENT=20649      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_shipment_template_file 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_shipment_template_file` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `filename` varchar(500)  DEFAULT NULL,
  `filepath` varchar(1000)  DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB    COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transchannel 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transchannel` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(50)  DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transcompany 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transcompany` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(100)  DEFAULT NULL COMMENT '物流公司名称',
  `simplename` varchar(100)  DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `access_token` char(100)  DEFAULT NULL,
  `api` int(10) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `uploadpath` varchar(200)  DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  `oldid` char(36)  DEFAULT NULL,
  `location` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`shopid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transcompany_api 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transcompany_api` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `api` varchar(200) NOT NULL DEFAULT '0',
  `name` varchar(200) NOT NULL DEFAULT '0',
  `openkey` varchar(200) NOT NULL DEFAULT '0',
  `openaccount` varchar(200) NOT NULL DEFAULT '0',
  `url` varchar(500) DEFAULT NULL,
  `system` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transcompany_services_zhihui 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transcompany_services_zhihui` (
  `code` char(50)  NOT NULL,
  `apiid` int(11) NOT NULL,
  `name` varchar(100)  DEFAULT NULL,
  `ftype` char(10)  DEFAULT NULL,
  PRIMARY KEY (`code`,`apiid`)
) ENGINE=InnoDB    COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transdetail 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transdetail` (
  `id` bigint(20) unsigned NOT NULL,
  `company` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(15)  DEFAULT NULL,
  `subarea` char(20)  DEFAULT NULL,
  `channel` bigint(20) unsigned DEFAULT NULL,
  `channame` char(36)  DEFAULT NULL,
  `pretime` int(11) DEFAULT NULL COMMENT 'US预计时效',
  `price` decimal(10,4) DEFAULT NULL,
  `drate` int(11) DEFAULT '5000',
  `opttime` datetime DEFAULT NULL,
  `transtype` bigint(20) unsigned DEFAULT NULL,
  `priceunits` char(10)  DEFAULT NULL,
  `remark` varchar(1000)  DEFAULT NULL,
  `cbmrate` int(11) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `Index 2` (`company`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_ship_transdetail_his 结构
CREATE TABLE IF NOT EXISTS `t_erp_ship_transdetail_his` (
  `id` bigint(20) unsigned NOT NULL,
  `opttime` datetime NOT NULL,
  `company` bigint(20) unsigned NOT NULL,
  `marketplaceid` char(15)  DEFAULT NULL,
  `subarea` char(20)  DEFAULT NULL,
  `channel` char(36)  DEFAULT NULL,
  `channame` char(36)  DEFAULT NULL,
  `pretime` int(11) DEFAULT NULL COMMENT 'US预计时效',
  `price` decimal(10,4) DEFAULT NULL,
  `drate` int(11) DEFAULT '5000',
  `transtype` bigint(20) unsigned DEFAULT NULL,
  `priceunits` char(10)  DEFAULT NULL,
  `remark` varchar(1000)  DEFAULT NULL,
  `cbmrate` int(11) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `disabled` bit(1) DEFAULT b'0',
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`opttime`,`id`) USING BTREE,
  KEY `Index 2` (`company`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stepwise_quotn 结构
CREATE TABLE IF NOT EXISTS `t_erp_stepwise_quotn` (
  `id` bigint(20) unsigned NOT NULL,
  `material` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `material_amount` (`material`,`amount`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stockcycle 结构
CREATE TABLE IF NOT EXISTS `t_erp_stockcycle` (
  `id` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `stockingCycle` int(10) DEFAULT NULL COMMENT '安全库存周期',
  `min_cycle` int(10) DEFAULT NULL COMMENT '最小补货周期/最小发货周期',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `warehouseid_materialid` (`warehouseid`,`materialid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stocktaking 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36)  DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `isworking` bit(1) DEFAULT NULL,
  `ftype` tinyint(4) DEFAULT NULL,
  `whtotalamount` int(11) DEFAULT NULL,
  `whtotalprice` decimal(15,4) DEFAULT NULL,
  `overamount` int(11) DEFAULT NULL,
  `lossamount` int(11) DEFAULT NULL,
  `overprice` decimal(12,4) DEFAULT NULL,
  `lossprice` decimal(12,4) DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stocktaking_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking_item` (
  `id` bigint(20) unsigned NOT NULL,
  `stocktakingid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `overamount` int(11) DEFAULT '0',
  `lossamount` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `stocktaking_mate_ware` (`stocktakingid`,`warehouseid`,`materialid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stocktaking_item_shelf 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking_item_shelf` (
  `id` bigint(20) unsigned NOT NULL,
  `stocktakingid` bigint(20) unsigned DEFAULT NULL,
  `materialid` bigint(20) unsigned DEFAULT NULL,
  `shelfid` bigint(20) unsigned DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `overamount` int(11) DEFAULT '0',
  `lossamount` int(11) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `stocktaking_mate_ware` (`stocktakingid`,`warehouseid`,`materialid`,`shelfid`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stocktaking_shelf 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking_shelf` (
  `stocktakingid` bigint(20) unsigned NOT NULL,
  `shelfid` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`stocktakingid`,`shelfid`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_stocktaking_warehouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_stocktaking_warehouse` (
  `stocktakingid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`stocktakingid`,`warehouseid`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_transtype 结构
CREATE TABLE IF NOT EXISTS `t_erp_transtype` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `name` char(50)  DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB    COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_turnover_rate 结构
CREATE TABLE IF NOT EXISTS `t_erp_turnover_rate` (
  `id` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `sku` char(36)  DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `begininv` int(11) DEFAULT NULL,
  `endinv` int(11) DEFAULT NULL,
  `outinv` int(11) DEFAULT NULL,
  `wrate` decimal(10,2) DEFAULT NULL,
  `wday` decimal(10,2) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_unsalable 结构
CREATE TABLE IF NOT EXISTS `t_erp_unsalable` (
  `sku` char(30)  DEFAULT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `invqty` int(11) DEFAULT NULL,
  `invinqty` int(11) DEFAULT NULL,
  `inv90` int(11) DEFAULT NULL,
  `inv180` int(11) DEFAULT NULL,
  `inv365` int(11) DEFAULT NULL,
  `invout90` int(11) DEFAULT NULL,
  `over90` int(11) DEFAULT NULL
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_calculate_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_calculate_record` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `ftype` char(20)  NOT NULL COMMENT '计算类型，发货，采购，人力',
  `iswarn` bit(1) NOT NULL DEFAULT b'0',
  `operator` bigint(20) unsigned NOT NULL COMMENT '计算操作人',
  `opttime` datetime NOT NULL COMMENT '计算时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_ftype` (`shopid`,`ftype`)
) ENGINE=InnoDB      COMMENT='用于存储各个计算模块的计算时间，计算人。统一留存历史记录';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_calculate_record_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_calculate_record_history` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `ftype` char(20) DEFAULT NULL,
  `iswarn` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_ftype` (`shopid`,`ftype`,`opttime`) USING BTREE
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_man_month 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_man_month` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `month` date DEFAULT NULL COMMENT '对应月份',
  `standardtime` int(11) DEFAULT NULL COMMENT '以标准工时计算的月份对应的总时长',
  `standardperson` int(11) DEFAULT NULL COMMENT '以标准工时算对应所需要的人力',
  `overtime` int(11) DEFAULT NULL COMMENT '以超出时间算的对应时长',
  `overperson` int(11) DEFAULT NULL COMMENT '以超出时间算的对应人力',
  `multiple` float DEFAULT NULL COMMENT '当前月份计算的倍数',
  `unitsoftime` int(11) DEFAULT NULL COMMENT '一个员工常规工作时间',
  `overoftime` int(11) DEFAULT NULL COMMENT '超出时间（即加班） 工作时间',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_month` (`shopid`,`month`)
) ENGINE=InnoDB      COMMENT='人力计算结果保存';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_man_month_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_man_month_history` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `month` date DEFAULT NULL,
  `standardtime` int(11) DEFAULT NULL,
  `standardperson` int(11) DEFAULT NULL,
  `overtime` int(11) DEFAULT NULL,
  `overperson` int(11) DEFAULT NULL,
  `multiple` float DEFAULT NULL,
  `unitsoftime` int(11) DEFAULT NULL,
  `overoftime` int(11) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_month` (`shopid`,`month`,`opttime`) USING BTREE
) ENGINE=InnoDB      COMMENT='人力计算结果历史';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_pickpay_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_pickpay_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(50) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT '订单编码',
  `month` date DEFAULT NULL COMMENT '对应月份',
  `shopid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '公司ID',
  `operator` bigint(20) unsigned NOT NULL COMMENT '创建人',
  `creator` bigint(20) unsigned NOT NULL COMMENT '操作人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `month_shopid` (`month`,`shopid`)
) ENGINE=InnoDB      COMMENT='采购提货与付款模块分组，采用一个月一个表单的结构，对采购付款历史与审核进行保存';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_pickpay_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_pickpay_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned NOT NULL COMMENT '订单ID',
  `materialid` bigint(20) unsigned NOT NULL COMMENT '产品ID',
  `auditor` bigint(20) unsigned NOT NULL COMMENT '审核人',
  `auditstatus` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '审核状态',
  `audittime` datetime NOT NULL COMMENT '审核时间',
  `supplier` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '供应商',
  `inbound` int(11) NOT NULL DEFAULT '0' COMMENT '待入库数量',
  `suggest` varchar(500)  NOT NULL DEFAULT '0' COMMENT '建议提货量',
  `planpick` int(11) NOT NULL DEFAULT '0' COMMENT '建议付款金额',
  `planpay` decimal(14,2) NOT NULL DEFAULT '0.00' COMMENT '计划付款金额',
  `remark` varchar(500)  NOT NULL DEFAULT '0' COMMENT '计划提货量',
  `operator` bigint(20) unsigned NOT NULL COMMENT '操作人',
  `creator` bigint(20) unsigned NOT NULL COMMENT '创建人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB      COMMENT='提货付款模块SKU从审核到通过以及历史的具体entry表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_presale_month 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_month` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `pid` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `month` date NOT NULL COMMENT '月日期，每个月1号',
  `amount` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '月销量',
  `opttime` datetime DEFAULT NULL COMMENT '修改日期',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`pid`,`month`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB      COMMENT='销售预测月度结果';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_presale_month_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_month_form` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `year` int(10) unsigned DEFAULT NULL COMMENT '年份',
  `month` int(10) unsigned DEFAULT NULL COMMENT '月份',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creator` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `creattime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid_year_month` (`shopid`,`year`,`month`)
) ENGINE=InnoDB      COMMENT='销售预测提交的表单，以每个月一份表单的方式存储整个公司关于销售预测的提交';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_presale_month_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_month_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned NOT NULL COMMENT '本地产品ID ',
  `pid` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `auditstatus` int(10) unsigned DEFAULT '0' COMMENT '0未提交，1 提交待审核，2审核成功  3已驳回',
  `audittime` datetime DEFAULT NULL COMMENT '审核时间',
  `auditor` bigint(20) unsigned DEFAULT '0' COMMENT '审核人',
  `status` int(10) unsigned DEFAULT '0' COMMENT '维持 提升 等销售状态 默认是无',
  `remark` varchar(500)  DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creatime` datetime DEFAULT NULL COMMENT '创建时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `creator` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_one` (`pid`,`formid`)
) ENGINE=InnoDB      COMMENT='销售预测单个SKU所以的审核与历史表单对应';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_presale_month_form_entry_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_month_form_entry_item` (
  `id` bigint(20) unsigned NOT NULL,
  `entryid` bigint(20) unsigned NOT NULL COMMENT '订单中产品所对应记录的ID',
  `year` int(10) unsigned NOT NULL COMMENT '年份',
  `month` int(10) unsigned NOT NULL COMMENT '月份',
  `amount` int(10) NOT NULL DEFAULT '0' COMMENT '对应计划数量',
  `opttime` datetime DEFAULT NULL COMMENT '操作人',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_one` (`entryid`,`year`,`month`)
) ENGINE=InnoDB      COMMENT='销售预测每个SKU对应每个月的预测数据';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_presale_week 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_presale_week` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `pid` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `week` date NOT NULL COMMENT '周日期',
  `amount` int(10) NOT NULL DEFAULT '0' COMMENT '周预估销量',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unione` (`pid`,`week`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB      COMMENT='销售预测周结构数据存储';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_form 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_form` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(50) CHARACTER SET latin1 NOT NULL DEFAULT '' COMMENT '采购订单编码',
  `auditor` bigint(20) unsigned DEFAULT NULL COMMENT '审核人',
  `auditstatus` int(11) DEFAULT '0' COMMENT '审核状态',
  `audittime` datetime DEFAULT NULL COMMENT '审核时间',
  `skunum` int(11) DEFAULT NULL COMMENT 'SKU数量',
  `warehouseid` bigint(20) unsigned DEFAULT NULL COMMENT '仓库',
  `shopid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '公司ID',
  `operator` bigint(20) unsigned NOT NULL COMMENT '操作人',
  `creator` bigint(20) unsigned NOT NULL COMMENT '创建人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `shopid` (`shopid`)
) ENGINE=InnoDB      COMMENT='采购模块表单保存表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_form_entry 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_form_entry` (
  `id` bigint(20) unsigned NOT NULL,
  `formid` bigint(20) unsigned NOT NULL COMMENT '订单ID',
  `materialid` bigint(20) unsigned NOT NULL COMMENT '本地产品ID',
  `warehouse` char(50) NOT NULL DEFAULT '' COMMENT '仓库',
  `sumreq` int(11) NOT NULL DEFAULT '0' COMMENT '需求量',
  `salemonth` int(11) NOT NULL DEFAULT '0' COMMENT '月销量',
  `presalemonth` varchar(500) NOT NULL DEFAULT '0' COMMENT '销售预测',
  `moreqty` char(50) NOT NULL DEFAULT '0' COMMENT '多余库存',
  `suggest` varchar(500) NOT NULL DEFAULT '0' COMMENT '月度建议采购量',
  `needqty` int(11) NOT NULL DEFAULT '0' COMMENT '需求量',
  `sugpurchase` int(11) NOT NULL DEFAULT '0' COMMENT '建议采购量',
  `planpurchase` int(11) NOT NULL DEFAULT '0' COMMENT '计划采购量',
  `detail` text COMMENT '当时计算的需求详情与提货详情',
  `operator` bigint(20) unsigned NOT NULL COMMENT '操作人',
  `creator` bigint(20) unsigned NOT NULL COMMENT '创建人',
  `opttime` datetime NOT NULL COMMENT '操作时间',
  `creattime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `formid` (`formid`)
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_material` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `materialid` bigint(20) unsigned NOT NULL COMMENT '本地产品ID',
  `purchasedaynum` int(10) DEFAULT NULL COMMENT '采购周期天数',
  `needamount` int(10) DEFAULT NULL COMMENT '需求量（通过采购周期内需求量汇总减去库存得出）',
  `suggestamount` int(10) DEFAULT NULL COMMENT '建议采购量（通过对需求量合箱规得出）',
  `sumrquest` int(10) DEFAULT NULL COMMENT '总需求量',
  `findex` int(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`materialid`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE,
  KEY `sumrquest` (`sumrquest`)
) ENGINE=InnoDB      COMMENT='产品采购计划，计算结果，存储每个SKU对应的采购周期，需求量，建议采购量，总需求量等';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_material_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_material_history` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `purchasedaynum` int(10) NOT NULL DEFAULT '0',
  `needamount` int(10) NOT NULL DEFAULT '0',
  `suggestamount` int(10) NOT NULL DEFAULT '0',
  `sumrquest` int(10) NOT NULL DEFAULT '0',
  `findex` int(10) DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`materialid`,`opttime`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB    COLLATE=utf8mb4_bin;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_selected 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_selected` (
  `materialid` bigint(20) unsigned NOT NULL COMMENT '本地产品ID',
  `userid` bigint(20) unsigned NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`materialid`,`userid`),
  KEY `userid` (`userid`)
) ENGINE=InnoDB      COMMENT='采购计算结果选中发货，记录是否选中';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_week 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_week` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `materialid` bigint(20) unsigned NOT NULL COMMENT '本地产品ID',
  `week` date NOT NULL COMMENT '周日期',
  `requestqty` int(10) NOT NULL DEFAULT '0' COMMENT '需求量（将商品销量通过对关系与本地产品对应）',
  `moreqty` int(10) DEFAULT '0' COMMENT '多余库存（库存减去对应安全库存+头程周期+增长天数对应需求量的和）',
  `suggestqty` int(10) DEFAULT '0' COMMENT '建议提货量（将需求量+剩余库存，缺少部分通过合箱规后）',
  `differentqty` int(10) DEFAULT '0' COMMENT '差异数量-（销售预测与库存之间的差异值）',
  `isfull` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`materialid`,`week`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB      COMMENT='产品采购计划，通过发货需求与本地产品对应，并进行本地SKU转化，组装子SKU转换\r\n采购周情况存储。包换需求量，多余库存等';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_purchase_week_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_purchase_week_history` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `week` date NOT NULL,
  `requestqty` int(10) NOT NULL DEFAULT '0',
  `moreqty` int(10) DEFAULT '0',
  `suggestqty` int(10) DEFAULT '0',
  `differentqty` int(10) DEFAULT '0',
  `isfull` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`materialid`,`week`,`opttime`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB      COMMENT='采购计算周数据历史保存';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_ship_setting 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_ship_setting` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `addnum` int(11) DEFAULT NULL,
  `startday` int(11) DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB      COMMENT='发货计划设置，用于保存发货计算中当达成率与增长率达到标准时增加的天数';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_ship_week 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_ship_week` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL COMMENT '公司ID',
  `pid` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `week` date NOT NULL COMMENT '对应周日期',
  `amount` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '对应周需求量',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`pid`,`week`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB      COMMENT='发货计划周情况，记录发货计算历史结果,根据销售预测得到需求量';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_plan_ship_week_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_plan_ship_week_history` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `pid` bigint(20) unsigned NOT NULL,
  `week` date NOT NULL,
  `amount` int(10) unsigned NOT NULL DEFAULT '0',
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unione` (`pid`,`week`,`opttime`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_productl_workhours 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_productl_workhours` (
  `mid` bigint(20) unsigned NOT NULL COMMENT 'pid',
  `amount` int(10) unsigned DEFAULT NULL COMMENT '一个小时内的生产量',
  `ftype` char(10)  DEFAULT 'hour' COMMENT '类型: hour mins second等 默认hour',
  `creator` bigint(20) unsigned DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`mid`) USING BTREE
) ENGINE=InnoDB      COMMENT='人力计算配置表\r\n标准工时/H	待增加	每个小时能处理多少个该产品的组装及打包发货工作	产品信息管理	\r\n独立产品及组装产品的主SKU（在售成品）需要设置【标准工时】。用作计算发货工时及人力需求。	\r\n如有产品需要完成组装（工作较为复杂），则可能为“12”，意为每小时从拣货、组装到装箱打包可以处理12个该产品。如流程较为简单，则可能为“35”，即为每小时可处理35个。\r\n';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_ship_product_delivery_cycle 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_ship_product_delivery_cycle` (
  `pid` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `deliverycycle` int(10) unsigned DEFAULT NULL COMMENT '头程天数',
  `findex` int(10) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB      COMMENT='商品的头程周期存储';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v2_shop_units_worktime 结构
CREATE TABLE IF NOT EXISTS `t_erp_v2_shop_units_worktime` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `multiple` float DEFAULT NULL COMMENT '工时倍数',
  `unitsoftime` int(10) DEFAULT NULL COMMENT '每个人一个月的正常工作时间 h 工作时长（常规）	待增加	工作时长/人/月	增加参数设置（蔚蓝使用为204小时）	用作计算所需人力数量',
  `overoftime` int(10) DEFAULT NULL COMMENT '工作时长（较长）	待增加	工作时长/人/月	增加参数设置（蔚蓝使用为286小时）	用作计算所需人力数量',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shopid` (`shopid`)
) ENGINE=InnoDB      COMMENT='人力计算配置表';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v3_purchase_plan 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(36) DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `min_cycle` int(10) unsigned DEFAULT NULL,
  `stocking_cycle` int(10) unsigned DEFAULT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `disable` bit(1) NOT NULL DEFAULT b'0',
  `createtime` datetime DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v3_purchase_plan_consumable_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_consumable_item` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `amount` int(10) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`materialid`,`warehouseid`) USING BTREE,
  KEY `materialid` (`warehouseid`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v3_purchase_plan_item 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_item` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `planid` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `batchnumber` char(20) DEFAULT NULL,
  `amount` int(10) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`planid`,`materialid`) USING BTREE,
  KEY `materialid` (`warehouseid`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB  CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v3_purchase_plan_item_history 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_item_history` (
  `id` char(36)  NOT NULL,
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned NOT NULL,
  `planid` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `batchnumber` char(20)  DEFAULT NULL,
  `amount` int(10) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`planid`,`warehouseid`,`batchnumber`,`materialid`) USING BTREE,
  KEY `materialid` (`materialid`) USING BTREE,
  KEY `shopid_opttime` (`shopid`,`warehouseid`,`opttime`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v3_purchase_plan_warehouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_warehouse` (
  `warehouseid` bigint(20) unsigned NOT NULL,
  `planid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `shopid` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`planid`,`warehouseid`) USING BTREE,
  KEY `shopid` (`shopid`) USING BTREE,
  KEY `planid` (`warehouseid`) USING BTREE
) ENGINE=InnoDB      COMMENT='入库仓库和补货规划的映射关系表，一个入库仓库不能在多个补货规划中出现，一个补货规划会有多个入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_v3_purchase_plan_warehouse_material 结构
CREATE TABLE IF NOT EXISTS `t_erp_v3_purchase_plan_warehouse_material` (
  `planid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `materialid` bigint(20) unsigned NOT NULL,
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`planid`,`materialid`) USING BTREE
) ENGINE=InnoDB      COMMENT='记录每个sku在补货规划中所默认的入库仓库';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_warehouse 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'ID',
  `name` char(36)  DEFAULT NULL COMMENT '名称',
  `ftype` char(36)  NOT NULL COMMENT '类型',
  `flevel` char(36)  DEFAULT NULL COMMENT '级别',
  `number` char(36)  NOT NULL COMMENT '编号',
  `address` varchar(500)  DEFAULT NULL COMMENT '地址',
  `remark` varchar(1000)  DEFAULT NULL COMMENT '备注',
  `findex` int(11) DEFAULT NULL COMMENT '次序',
  `country` char(15)  DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `fbawareid` bigint(20) unsigned DEFAULT NULL COMMENT '海外仓',
  `isdefault` bit(1) DEFAULT b'0' COMMENT '默认仓库',
  `shopid` bigint(20) unsigned NOT NULL COMMENT '店铺',
  `parentid` bigint(20) unsigned DEFAULT NULL COMMENT '父节点',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `addressid` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  `stocking_cycle` int(11) DEFAULT '0',
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  `isstocktaking` bit(1) DEFAULT b'0',
  `min_cycle` int(10) DEFAULT '0',
  `first_leg_charges` decimal(12,2) DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  `ishungry` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `Index 2` (`parentid`),
  KEY `ftype` (`ftype`),
  KEY `shopid` (`shopid`),
  KEY `name_shopid` (`name`,`shopid`) USING BTREE,
  KEY `addressid` (`addressid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_warehouse_address 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_address` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `shopid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `name` char(50) NOT NULL DEFAULT '0' COMMENT '地址名称',
  `number` char(50) NOT NULL DEFAULT '0',
  `detail` varchar(500) DEFAULT '0' COMMENT '地址街道详情',
  `postcode` char(50) DEFAULT '0' COMMENT '邮编',
  `phone` char(50) DEFAULT NULL COMMENT '业主电话',
  `landlord` char(50) DEFAULT NULL COMMENT '业主（房东）',
  `lost_effect_date` datetime DEFAULT NULL COMMENT '到期时间',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `disabled` bit(1) DEFAULT b'0' COMMENT '是否失效（是否删除）',
  `operator` bigint(20) unsigned DEFAULT '0' COMMENT '修改人',
  `opttime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `creator` bigint(20) unsigned DEFAULT '0' COMMENT '创建人',
  `creattime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shopid_name` (`shopid`,`name`) USING BTREE,
  UNIQUE KEY `shopid_number` (`shopid`,`number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_warehouse_shelf 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '货柜ID',
  `addressid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '仓库ID',
  `name` varchar(200)  NOT NULL DEFAULT '0' COMMENT '货柜名称',
  `number` char(50)  NOT NULL DEFAULT '0' COMMENT '编码',
  `capacity` float NOT NULL DEFAULT '0' COMMENT '容量(立方厘米)',
  `length` float unsigned NOT NULL DEFAULT '0' COMMENT '长度(cm)',
  `width` float unsigned NOT NULL DEFAULT '0' COMMENT '宽度(cm)',
  `height` float unsigned NOT NULL DEFAULT '0' COMMENT '高度(cm)',
  `parentid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '父货柜ID',
  `sort` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '排序即（柜子所在位置）',
  `treepath` char(200)  NOT NULL DEFAULT '0' COMMENT '所有付货柜编码如：A01!033!F01',
  `shopid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '公司ID',
  `iswarn` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否报警',
  `isdelete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否逻辑删除',
  `isfrozen` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否冻结',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  `creator` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `creattime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `parentid` (`parentid`) USING BTREE,
  KEY `treepath` (`shopid`,`addressid`,`treepath`(191)) USING BTREE
) ENGINE=InnoDB      COMMENT='仓库货柜';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_warehouse_shelf_inventory 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf_inventory` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `shelfid` bigint(20) unsigned DEFAULT NULL COMMENT '货柜ID',
  `materialid` bigint(20) unsigned DEFAULT NULL COMMENT '产品ID',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `quantity` int(10) DEFAULT NULL COMMENT '当前数量',
  `size` float unsigned DEFAULT NULL COMMENT '当前体积',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shelfid_materialid_shopid` (`shopid`,`shelfid`,`materialid`,`warehouseid`) USING BTREE,
  UNIQUE KEY `materialid_shopid` (`shopid`,`materialid`,`shelfid`,`warehouseid`) USING BTREE
) ENGINE=InnoDB      COMMENT='货架产品库存';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_warehouse_shelf_inventory_opt_record 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_shelf_inventory_opt_record` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `shelfid` bigint(20) unsigned DEFAULT NULL COMMENT '货柜ID',
  `materialid` bigint(20) unsigned DEFAULT NULL COMMENT '产品ID',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `warehouseid` bigint(20) unsigned DEFAULT NULL,
  `quantity` int(10) unsigned DEFAULT NULL COMMENT '操作数量',
  `size` float unsigned DEFAULT NULL COMMENT '操作数量对于的体积使用立方厘米cm3',
  `balance_qty` int(10) unsigned DEFAULT NULL COMMENT '操作后结余数量',
  `balance_size` float unsigned DEFAULT NULL COMMENT '操作后结余体积',
  `opt` int(10) unsigned DEFAULT NULL COMMENT '0：出库；1：入库;2：修正下架；3：修正上架',
  `formid` bigint(20) unsigned DEFAULT NULL COMMENT '表单ID',
  `formtype` char(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '表单类型',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `formid_formtype` (`formid`,`formtype`) USING BTREE,
  KEY `shelfid_materialid_shopid_formid_formtype` (`shopid`,`shelfid`,`materialid`,`warehouseid`) USING BTREE
) ENGINE=InnoDB      COMMENT='操作记录';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_warehouse_type 结构
CREATE TABLE IF NOT EXISTS `t_erp_warehouse_type` (
  `id` char(36)  NOT NULL COMMENT 'ID',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '店铺',
  `issystem` bit(1) DEFAULT NULL COMMENT '是否系统',
  `name` char(50)  DEFAULT NULL COMMENT '名字',
  `remark` varchar(1000)  DEFAULT NULL COMMENT '备注',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_erp_whse_unsalable_rpt 结构
CREATE TABLE IF NOT EXISTS `t_erp_whse_unsalable_rpt` (
  `shopid` bigint(20) unsigned NOT NULL,
  `wid` bigint(20) unsigned NOT NULL,
  `name` char(36)  DEFAULT NULL,
  `mtid` bigint(20) unsigned NOT NULL,
  `sku` char(50)  NOT NULL,
  `groupid` char(36)  NOT NULL,
  `qtysum` decimal(35,0) DEFAULT NULL,
  `qtyablesum` decimal(35,0) DEFAULT NULL,
  `qtysum60` decimal(35,0) DEFAULT NULL,
  `qtysum90` decimal(35,0) DEFAULT NULL,
  `qtysum180` decimal(35,0) DEFAULT NULL,
  `qtysum365` decimal(35,0) DEFAULT NULL,
  `salesum60` decimal(32,0) DEFAULT NULL,
  `salesum90` decimal(32,0) DEFAULT NULL,
  `salesum180` decimal(32,0) DEFAULT NULL,
  `salesum365` decimal(32,0) DEFAULT NULL,
  `nostock30` decimal(32,0) DEFAULT NULL,
  `qtysum30` decimal(35,0) DEFAULT NULL,
  `salesum30` decimal(35,0) DEFAULT NULL,
  PRIMARY KEY (`shopid`,`wid`,`mtid`,`sku`,`groupid`)
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_parameterconfig 结构
CREATE TABLE IF NOT EXISTS `t_parameterconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ptype` char(36)  DEFAULT NULL,
  `pkey` char(10)  DEFAULT NULL,
  `value` varchar(100)  DEFAULT NULL,
  `sortindex` int(11) DEFAULT NULL,
  `description` varchar(100)  DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `pkey` (`pkey`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=30      ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_picture 结构
CREATE TABLE IF NOT EXISTS `t_picture` (
  `id` bigint(20) unsigned NOT NULL COMMENT '图片ID',
  `url` varchar(500)  DEFAULT NULL COMMENT '图片网络位置',
  `location` varchar(500)  DEFAULT NULL COMMENT '图片本地位置',
  `height` decimal(10,2) DEFAULT NULL COMMENT '图片高度',
  `height_units` char(10)  DEFAULT NULL COMMENT '高度单位',
  `width` decimal(10,2) DEFAULT NULL COMMENT '图片宽度',
  `width_units` char(10)  DEFAULT NULL COMMENT '宽度单位',
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_location` (`location`(191)) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC COMMENT='用于存放Image';

-- 数据导出被取消选择。

-- 导出  表 db_erp.t_sys_operationlog 结构
CREATE TABLE IF NOT EXISTS `t_sys_operationlog` (
  `id` bigint(20) unsigned NOT NULL,
  `time` datetime DEFAULT NULL,
  `ip` char(35)  DEFAULT NULL,
  `userid` bigint(20) unsigned DEFAULT NULL,
  `username` varchar(255)  DEFAULT NULL,
  `logType` varchar(255)  DEFAULT NULL,
  `method` char(100)  DEFAULT NULL,
  `exceptionDetail` char(50)  DEFAULT NULL,
  `param` varchar(4000)  DEFAULT NULL,
  `description` varchar(255)  DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `time` (`userid`,`method`,`time`) USING BTREE,
  KEY `idx_method_time_userid` (`method`,`time`,`userid`) USING BTREE
) ENGINE=InnoDB      ROW_FORMAT=DYNAMIC;



CREATE TABLE IF NOT EXISTS `t_authority` (
  `id` char(36)  NOT NULL,
  `menuid` char(50)  DEFAULT NULL,
  `url` varchar(100)  DEFAULT NULL COMMENT '定义Action',
  `name` varchar(100)  DEFAULT NULL COMMENT '权限名称',
  `description` varchar(500)  DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`url`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='Action权限控制表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_erp_serial_num 结构
CREATE TABLE IF NOT EXISTS `t_erp_serial_num` (
  `id` char(36)  NOT NULL,
  `ftype` char(36)  DEFAULT NULL,
  `seqno` int(11) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `prefix_date` date DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `Index 2` (`prefix_date`,`ftype`,`shopid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_manager_limit 结构
CREATE TABLE IF NOT EXISTS `t_manager_limit` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID(h)',
  `shopId` bigint(20) unsigned DEFAULT NULL,
  `maxShopCount` int(8) DEFAULT '10' COMMENT '绑定店铺数量',
  `maxMarketCount` int(8) DEFAULT '10' COMMENT '绑定店铺站点数量',
  `maxProductCount` int(8) DEFAULT '50000' COMMENT '商品数量上限',
  `maxOrderCount` int(8) DEFAULT '3600' COMMENT '处理订单上限',
  `maxMember` int(8) DEFAULT '3' COMMENT '子用户数量上限',
  `maxProfitPlanCount` int(8) DEFAULT '1' COMMENT '利润计算方案数量',
  `maxdayOpenAdvCount` int(8) DEFAULT '10' COMMENT '每天开启广告组数量',
  `existShopCount` int(8) DEFAULT '0',
  `existMarketCount` int(8) DEFAULT '0',
  `existProductCount` int(8) DEFAULT '0',
  `existOrderCount` int(8) DEFAULT '0',
  `existMember` int(8) DEFAULT '0',
  `existProfitPlanCount` int(8) DEFAULT '0',
  `existdayOpenAdvCount` int(8) DEFAULT '0',
  `tariffpackage` int(8) DEFAULT '0' COMMENT '0-基础版，1-标准版，2-专业版，3-独享版,4-自定义',
  `losingEffect` date DEFAULT NULL COMMENT '失效时间',
  `remark` varchar(1000)  DEFAULT NULL COMMENT '备注',
  `opratetime` datetime DEFAULT NULL COMMENT '修改时间',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `oprate` bigint(20) unsigned DEFAULT NULL COMMENT '修改人',
  `logicVersion` bigint(20) DEFAULT '0',
  `saleskey` char(36)  DEFAULT NULL,
  `neverNoticeShop` bit(1) DEFAULT b'0',
  `afterNnoticeTariff` date DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `t_manager_limit_ibfk_1` (`shopId`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_manager_limit_append 结构
CREATE TABLE IF NOT EXISTS `t_manager_limit_append` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `tariffpackage` int(10) unsigned NOT NULL,
  `tariffpackage_append_id` int(10) unsigned NOT NULL,
  `ftype` char(50)  DEFAULT NULL,
  `num` int(11) DEFAULT '0',
  `effecttime` date DEFAULT NULL,
  `losingeffect` date DEFAULT NULL,
  `isclose` bit(1) DEFAULT b'0',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_menu 结构
CREATE TABLE IF NOT EXISTS `t_menu` (
  `id` char(36)  NOT NULL COMMENT '主键(h)',
  `name` varchar(100)  NOT NULL COMMENT '名称',
  `description` varchar(500)  DEFAULT NULL COMMENT '描述信息',
  `mindex` int(11) DEFAULT NULL COMMENT '次序',
  `mlevel` int(11) DEFAULT NULL COMMENT '层级',
  `parentid` char(36)  DEFAULT NULL COMMENT '父菜单',
  `icon` varchar(2048)  DEFAULT NULL COMMENT '图标',
  `childnumber` int(11) DEFAULT NULL COMMENT '子菜单个数',
  `action_url` varchar(100)  DEFAULT NULL COMMENT '页面链接',
  `groupid` char(36)  DEFAULT NULL COMMENT '分组',
  PRIMARY KEY (`id`),
  KEY `FK_t_menu_t_menu` (`parentid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='菜单';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_menu_group 结构
CREATE TABLE IF NOT EXISTS `t_menu_group` (
  `id` char(36)  NOT NULL,
  `name` varchar(60)  DEFAULT NULL,
  `icon` varchar(500)  DEFAULT NULL,
  `remark` varchar(500)  DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_picture 结构
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
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='用于存放Image';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_role 结构
CREATE TABLE IF NOT EXISTS `t_role` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(100)  DEFAULT NULL COMMENT '角色名称',
  `type` varchar(10)  DEFAULT NULL COMMENT '角色类型',
  `issystem` bit(1) NOT NULL DEFAULT b'0',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`shopid`,`name`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='角色';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_role_adv_group 结构
CREATE TABLE IF NOT EXISTS `t_role_adv_group` (
  `roleid` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned NOT NULL,
  `group_name` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`roleid`,`groupid`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_role_authority 结构
CREATE TABLE IF NOT EXISTS `t_role_authority` (
  `id` bigint(20) unsigned NOT NULL,
  `role_id` bigint(20) unsigned DEFAULT NULL,
  `authority_id` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_role_authority_t_role` (`role_id`),
  KEY `FK_t_role_authority_t_authority` (`authority_id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='角色权限分配表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_role_group 结构
CREATE TABLE IF NOT EXISTS `t_role_group` (
  `roleid` bigint(20) unsigned NOT NULL,
  `groupid` bigint(20) unsigned NOT NULL,
  `group_name` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`roleid`,`groupid`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_role_marketplace 结构
CREATE TABLE IF NOT EXISTS `t_role_marketplace` (
  `id` bigint(20) unsigned NOT NULL,
  `roleid` bigint(20) unsigned DEFAULT NULL,
  `marketplaceid` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`roleid`,`marketplaceid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_role_menu 结构
CREATE TABLE IF NOT EXISTS `t_role_menu` (
  `id` bigint(20) unsigned NOT NULL,
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色ID',
  `menu_id` char(36)  DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  KEY `FK_t_role_menu_t_role` (`role_id`),
  KEY `FK_t_role_menu_t_menu` (`menu_id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='角色菜单分配表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_shop 结构
CREATE TABLE IF NOT EXISTS `t_shop` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(50)  DEFAULT NULL COMMENT '公司名称',
  `remark` varchar(50)  DEFAULT NULL COMMENT '备注',
  `invitecode` char(7)  DEFAULT NULL COMMENT '邀请码',
  `fromcode` char(7)  DEFAULT NULL COMMENT '受邀请码',
  `oldid` char(36)  DEFAULT NULL,
  `boss_email` varchar(100)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `invitecode` (`invitecode`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='店铺';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_app_store_company 结构
CREATE TABLE IF NOT EXISTS `t_sys_app_store_company` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `image` bigint(20) unsigned DEFAULT NULL,
  `telphone` varchar(25) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `QQ` varchar(25) DEFAULT NULL,
  `website` varchar(100) DEFAULT NULL COMMENT '网址',
  `work` varchar(100) DEFAULT NULL COMMENT '业务范围',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `iscompany` bit(1) DEFAULT b'1' COMMENT '是否企业',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_app_store_detail 结构
CREATE TABLE IF NOT EXISTS `t_sys_app_store_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appgroupid` bigint(20) unsigned DEFAULT NULL,
  `appcompanyid` bigint(20) unsigned DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL COMMENT '服务名称',
  `description` varchar(500) DEFAULT NULL COMMENT '服务描述',
  `image` bigint(20) unsigned DEFAULT NULL,
  `price_original` decimal(15,2) DEFAULT NULL COMMENT '原价',
  `price` decimal(15,2) DEFAULT NULL COMMENT '现在售价',
  `recommend_score` float(5,2) DEFAULT NULL COMMENT '推荐指数',
  `recommend_reason` varchar(255) DEFAULT NULL COMMENT '推荐理由',
  `customer_feedback` varchar(500) DEFAULT NULL COMMENT '客户反馈',
  `customer_pageview` int(11) DEFAULT '0' COMMENT '用户浏览量',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_g_c` (`appgroupid`,`appcompanyid`)
) ENGINE=InnoDB AUTO_INCREMENT=47;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_app_store_group 结构
CREATE TABLE IF NOT EXISTS `t_sys_app_store_group` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `image` bigint(20) unsigned DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `createdate` timestamp NULL DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_app_store_service_detail 结构
CREATE TABLE IF NOT EXISTS `t_sys_app_store_service_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `detailid` int(11) DEFAULT NULL,
  `content` longtext COMMENT '服务详情',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `detailid` (`detailid`)
) ENGINE=InnoDB AUTO_INCREMENT=30;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_channel_salesperson_key 结构
CREATE TABLE IF NOT EXISTS `t_sys_channel_salesperson_key` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `salesperson` char(50) NOT NULL DEFAULT '0',
  `fkey` char(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_contact 结构
CREATE TABLE IF NOT EXISTS `t_sys_contact` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(50)  DEFAULT NULL,
  `email` varchar(50)  NOT NULL,
  `phone` varchar(50)  DEFAULT NULL,
  `message` varchar(500)  DEFAULT NULL,
  `operatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_customer_discount 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_discount` (
  `id` bigint(20) unsigned NOT NULL,
  `number` char(7)  DEFAULT NULL COMMENT '折扣编码',
  `ftype` char(10)  DEFAULT NULL,
  `packages` int(11) DEFAULT NULL,
  `amount` decimal(10,2) unsigned DEFAULT NULL COMMENT '折扣金额',
  `account` char(50)  DEFAULT NULL COMMENT '指定应用帐户（可以不填）',
  `pkgtime` int(11) DEFAULT NULL,
  `isused` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否应用',
  `orderid` char(50)  DEFAULT NULL COMMENT '应用订单',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '应用公司',
  `createdate` datetime DEFAULT NULL COMMENT '创建时间',
  `lostime` datetime DEFAULT NULL COMMENT '失效时间',
  `operator` varchar(36)  DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `number` (`number`)
) ENGINE=InnoDB COMMENT='用户折扣表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_customer_invoice 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_invoice` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `orderid` bigint(20) unsigned DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL,
  `invoice` varchar(50) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `bank` varchar(255) DEFAULT NULL,
  `cardNo` char(50) DEFAULT NULL,
  `ftype` char(20) DEFAULT NULL,
  `sendAddress` char(255) DEFAULT NULL,
  `sendPhone` char(20) DEFAULT NULL,
  `sendName` char(10) DEFAULT NULL,
  `isSend` bit(1) DEFAULT b'0',
  `ivctype` char(50) DEFAULT 'normal',
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_customer_order 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_order` (
  `id` bigint(20) unsigned NOT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `ftype` char(20)  DEFAULT NULL COMMENT '订单类型  package:套餐  append:附加包',
  `out_trade_no` char(20)  DEFAULT NULL COMMENT '商户订单号',
  `subject` char(50)  DEFAULT NULL COMMENT '订单名称',
  `trade_no` char(50)  DEFAULT NULL COMMENT '支付宝交易号',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '付款金额',
  `discountnumber` char(10)  DEFAULT NULL,
  `paytime` timestamp NULL DEFAULT NULL,
  `paystatus` char(50)  DEFAULT NULL COMMENT '付款状态',
  `ivcstatus` char(50)  DEFAULT NULL COMMENT '开票状态',
  `months` int(11) DEFAULT NULL,
  `tariffpackage` int(11) DEFAULT NULL,
  `pcs` int(11) DEFAULT '1',
  `paytype` char(10)  DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `out_trade_no` (`out_trade_no`)
) ENGINE=InnoDB COMMENT=' ';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_customer_order_refund 结构
CREATE TABLE IF NOT EXISTS `t_sys_customer_order_refund` (
  `id` bigint(20) unsigned DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `orderid` bigint(20) unsigned DEFAULT NULL,
  `out_trade_no` char(20)  DEFAULT NULL COMMENT '商户订单号',
  `trade_no` char(50)  DEFAULT NULL COMMENT '支付宝交易号',
  `refund_amount` decimal(10,2) DEFAULT NULL COMMENT '付款金额',
  `refund_reason` char(50)  DEFAULT NULL COMMENT '订单名称',
  `out_request_no` char(20)  DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT=' ';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_dept 结构
CREATE TABLE IF NOT EXISTS `t_sys_dept` (
  `id` bigint(20) unsigned NOT NULL,
  `name` char(100) DEFAULT NULL,
  `parent_id` bigint(20) unsigned DEFAULT NULL,
  `tree_path` char(100) DEFAULT NULL,
  `sort` int(10) DEFAULT NULL,
  `status` int(10) DEFAULT NULL,
  `deleted` int(10) DEFAULT NULL,
  `gmt_create` timestamp NULL DEFAULT NULL,
  `gmt_modified` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_dict 结构
CREATE TABLE IF NOT EXISTS `t_sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `name` varchar(50)  DEFAULT '' COMMENT '类型名称',
  `code` varchar(50)  DEFAULT '' COMMENT '类型编码',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0-正常 ,1-停用）',
  `remark` varchar(255)  DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `type_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1712653504552034307  ROW_FORMAT=DYNAMIC COMMENT='字典类型表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_dict_item 结构
CREATE TABLE IF NOT EXISTS `t_sys_dict_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50)  DEFAULT '' COMMENT '字典项名称',
  `value` varchar(50)  DEFAULT '' COMMENT '字典项值',
  `dict_code` varchar(50)  DEFAULT '' COMMENT '字典编码',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0 停用 1正常）',
  `defaulted` tinyint(1) DEFAULT '0' COMMENT '是否默认（0否 1是）',
  `remark` varchar(255)  DEFAULT '' COMMENT '备注',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `value_dict_code` (`dict_code`,`value`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1712653923256819718  ROW_FORMAT=DYNAMIC COMMENT='字典数据表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_feishu_auth 结构
CREATE TABLE IF NOT EXISTS `t_sys_feishu_auth` (
  `app_id` char(36)  NOT NULL,
  `app_secret` char(36)  NOT NULL,
  `encrypt_key` char(36)  DEFAULT NULL,
  `verification_token` char(36)  DEFAULT NULL,
  `shopid` bigint(20) unsigned NOT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`app_id`,`shopid`) USING BTREE,
  UNIQUE KEY `shopid` (`shopid`) USING BTREE
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_feishu_leave_calendar 结构
CREATE TABLE IF NOT EXISTS `t_sys_feishu_leave_calendar` (
  `timeoff_event_id` char(60) NOT NULL,
  `uuid` char(36) NOT NULL,
  `userid` char(36) DEFAULT NULL,
  `appid` char(36) NOT NULL,
  `event_content_type` char(20) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `isdelete` bit(1) DEFAULT NULL,
  `logs` varchar(2000) DEFAULT NULL,
  `opttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`timeoff_event_id`) USING BTREE,
  KEY `uuid` (`uuid`),
  KEY `userid` (`userid`),
  KEY `appid` (`appid`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_help_page 结构
CREATE TABLE IF NOT EXISTS `t_sys_help_page` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键(h)',
  `menuid` char(36)  NOT NULL,
  `content` text  NOT NULL,
  `opttime` datetime NOT NULL,
  `operator` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`menuid`)
) ENGINE=InnoDB AUTO_INCREMENT=38 ROW_FORMAT=DYNAMIC COMMENT='菜单';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_holiday 结构
CREATE TABLE IF NOT EXISTS `t_sys_holiday` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `country` char(3) CHARACTER SET latin1 DEFAULT NULL,
  `marketplaceid` char(20) CHARACTER SET latin1 DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `value` varchar(500)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `country` (`country`,`month`),
  KEY `marketplaceid` (`marketplaceid`,`month`)
) ENGINE=InnoDB AUTO_INCREMENT=60;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_importrecord 结构
CREATE TABLE IF NOT EXISTS `t_sys_importrecord` (
  `id` bigint(20) unsigned NOT NULL,
  `opttime` datetime DEFAULT NULL,
  `operator` bigint(20) unsigned DEFAULT NULL,
  `issuccess` bit(1) DEFAULT NULL,
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `importtype` char(50)  DEFAULT NULL,
  `log` varchar(2000)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`shopid`,`importtype`),
  KEY `opttime` (`opttime`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_mailsender 结构
CREATE TABLE IF NOT EXISTS `t_sys_mailsender` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mailServerHost` varchar(50) DEFAULT NULL,
  `mailServerPort` char(10) DEFAULT NULL,
  `fromAddress` varchar(50) DEFAULT NULL,
  `serverAccount` varchar(50) DEFAULT NULL,
  `userName` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `iwallHost` varchar(50) DEFAULT NULL,
  `validate` bit(1) DEFAULT NULL,
  `starttls` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_mail_template 结构
CREATE TABLE IF NOT EXISTS `t_sys_mail_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mail_subject` varchar(50) DEFAULT NULL,
  `ftype` tinyint(3) unsigned DEFAULT NULL COMMENT '0、系统废弃模板，1、系统通知邮件 ，2、公司通知邮件，3、买家订单回复，4、买家邀请',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  `content` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_menu 结构
CREATE TABLE IF NOT EXISTS `t_sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64)  DEFAULT '' COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID',
  `apppath` varchar(50)  DEFAULT NULL,
  `path` varchar(128)  DEFAULT '' COMMENT '路由路径',
  `component` varchar(128)  DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(64)  DEFAULT '' COMMENT '菜单图标',
  `appicon` varchar(64)  DEFAULT '',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `visible` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用 1-开启',
  `redirect` varchar(128)  DEFAULT '' COMMENT '跳转路径',
  `runui` bit(1) DEFAULT NULL,
  `runapp` bit(1) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  `oldid` char(40)  DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=66602 ROW_FORMAT=DYNAMIC COMMENT='菜单管理';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_menu_favorite 结构
CREATE TABLE IF NOT EXISTS `t_sys_menu_favorite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shopid` bigint(20) NOT NULL DEFAULT '0' COMMENT '公司ID',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `menuid` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='菜单收藏';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_message_template 结构
CREATE TABLE IF NOT EXISTS `t_sys_message_template` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ftype` char(10)  NOT NULL COMMENT '消息类型',
  `content` varchar(2000)  DEFAULT NULL COMMENT '消息内容',
  `operator` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_notify 结构
CREATE TABLE IF NOT EXISTS `t_sys_notify` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100)  NOT NULL DEFAULT '0',
  `content` varchar(2000)  DEFAULT NULL,
  `ftype` int(4) NOT NULL COMMENT '消息的类型，1: 公告 Announce，2: 提醒 Remind，3：信息 Message',
  `target` char(100)  DEFAULT NULL COMMENT '目标类型',
  `action` char(100)  DEFAULT NULL COMMENT '提醒信息的动作类型',
  `sender` bigint(20) unsigned DEFAULT NULL COMMENT '发送者的ID',
  `receiver` bigint(20) unsigned DEFAULT NULL COMMENT '接受者ID',
  `createdAt` datetime DEFAULT NULL COMMENT '创建时间',
  `shopid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_notify_shopid` (`ftype`) USING BTREE,
  KEY `shopid` (`shopid`),
  KEY `target` (`target`),
  KEY `createdAt` (`createdAt`)
) ENGINE=InnoDB AUTO_INCREMENT=2107686914 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_operationlog 结构
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
  PRIMARY KEY (`id`),
  KEY `time` (`userid`,`method`,`time`) USING BTREE,
  KEY `idx_method_time_userid` (`method`,`time`,`userid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_permission 结构
CREATE TABLE IF NOT EXISTS `t_sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64)  DEFAULT NULL COMMENT '权限名称',
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单模块ID\r\n',
  `url_perm` varchar(128)  DEFAULT NULL COMMENT 'URL权限标识',
  `btn_perm` varchar(64)  DEFAULT NULL COMMENT '按钮权限标识',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id` (`id`,`name`) USING BTREE,
  KEY `id_2` (`id`,`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1697438074115891203 ROW_FORMAT=DYNAMIC COMMENT='权限表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_potential_customer 结构
CREATE TABLE IF NOT EXISTS `t_sys_potential_customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` varchar(2000) DEFAULT NULL,
  `user_name` varchar(1000) DEFAULT NULL,
  `address` varchar(2000) DEFAULT NULL,
  `telphone` char(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `sendtime` timestamp NULL DEFAULT NULL,
  `disable` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`telphone`,`email`)
) ENGINE=InnoDB AUTO_INCREMENT=38005;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_quartz_task 结构
CREATE TABLE IF NOT EXISTS `t_sys_quartz_task` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `fgroup` varchar(50) DEFAULT NULL,
  `priority` int(11) DEFAULT '5',
  `description` varchar(50) DEFAULT NULL,
  `cron` varchar(100) DEFAULT NULL,
  `server` varchar(20) DEFAULT NULL,
  `bean` varchar(50) DEFAULT NULL,
  `method` varchar(50) DEFAULT NULL,
  `parameter` varchar(200) DEFAULT NULL,
  `path` varchar(200) DEFAULT NULL,
  `isdelete` bit(1) DEFAULT b'0',
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=143;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_query_field 结构
CREATE TABLE IF NOT EXISTS `t_sys_query_field` (
  `fquery` char(20)  NOT NULL,
  `ffield` char(30)  NOT NULL,
  `title` char(50)  DEFAULT NULL,
  `titleTooltip` char(50)  DEFAULT NULL,
  `width` char(50)  DEFAULT NULL,
  `findex` int(11) DEFAULT NULL,
  `formatter` char(50)  DEFAULT NULL,
  `footerFormatter` char(50)  DEFAULT NULL,
  `sortable` bit(1) DEFAULT NULL,
  `valign` char(10)  DEFAULT NULL,
  `align` char(10)  DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`fquery`,`ffield`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_query_user_version 结构
CREATE TABLE IF NOT EXISTS `t_sys_query_user_version` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) unsigned DEFAULT NULL,
  `fquery` char(20)  DEFAULT NULL,
  `isused` bit(1) DEFAULT NULL,
  `name` char(50)  DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`,`fquery`)
) ENGINE=InnoDB AUTO_INCREMENT=123457016 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_query_version_feild 结构
CREATE TABLE IF NOT EXISTS `t_sys_query_version_feild` (
  `fversionid` bigint(20) unsigned NOT NULL,
  `ffield` char(30)  NOT NULL,
  `findex` int(11) NOT NULL,
  PRIMARY KEY (`fversionid`,`ffield`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_role_menu 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_menu` (
  `role_id` bigint(20) unsigned NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) unsigned NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`),
  KEY `menu_id` (`menu_id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='角色和菜单关联表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_role_permission 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_permission` (
  `role_id` bigint(20) unsigned NOT NULL COMMENT '角色id',
  `permission_id` bigint(20) unsigned NOT NULL COMMENT '资源id',
  PRIMARY KEY (`role_id`,`permission_id`) USING BTREE,
  KEY `permission_id` (`permission_id`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='角色权限表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_role_tag 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_tag` (
  `id` bigint(20) unsigned NOT NULL,
  `tag_id` char(36) CHARACTER SET latin1 DEFAULT NULL,
  `roleid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`tag_id`,`roleid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='管理员新建，用于给不同下属分配不同的产品查询权限';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_role_taggroup 结构
CREATE TABLE IF NOT EXISTS `t_sys_role_taggroup` (
  `id` bigint(20) unsigned NOT NULL,
  `roleid` bigint(20) unsigned DEFAULT NULL,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 2` (`roleid`,`groupid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_subscription 结构
CREATE TABLE IF NOT EXISTS `t_sys_subscription` (
  `target` char(2)  NOT NULL,
  `userid` bigint(20) unsigned NOT NULL,
  `action` char(100)  DEFAULT NULL COMMENT '订阅动作',
  `disable` bit(1) NOT NULL DEFAULT b'0',
  `createdAt` datetime DEFAULT NULL,
  PRIMARY KEY (`target`,`userid`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_tags 结构
CREATE TABLE IF NOT EXISTS `t_sys_tags` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `name` char(100)  DEFAULT NULL COMMENT '标签名称',
  `value` varchar(200)  DEFAULT NULL,
  `sort` int(10) DEFAULT NULL,
  `color` char(50)  DEFAULT NULL,
  `taggroupid` bigint(20) unsigned DEFAULT NULL COMMENT '分组ID',
  `shopid` bigint(20) unsigned DEFAULT NULL COMMENT '公司ID',
  `description` varchar(100)  DEFAULT NULL COMMENT '描述',
  `creator` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '修改人',
  `remark` char(200)  DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `gmt_create` datetime DEFAULT NULL COMMENT '修改日期',
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name_taggroupid` (`name`,`taggroupid`) USING BTREE,
  KEY `FK_t_tag_t_sho` (`shopid`) USING BTREE,
  KEY `groupid` (`taggroupid`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_tags_groups 结构
CREATE TABLE IF NOT EXISTS `t_sys_tags_groups` (
  `id` bigint(20) unsigned NOT NULL,
  `sort` int(10) DEFAULT NULL,
  `name` char(100)  DEFAULT NULL,
  `shop_id` bigint(20) unsigned DEFAULT NULL,
  `description` char(100)  DEFAULT NULL,
  `creator` bigint(20) unsigned DEFAULT NULL,
  `opterator` bigint(20) unsigned DEFAULT NULL,
  `remark` char(200)  DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name_shop_id` (`name`,`shop_id`),
  KEY `FK_t_tag_group_t_shop` (`shop_id`) USING BTREE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_target 结构
CREATE TABLE IF NOT EXISTS `t_sys_target` (
  `id` char(2)  NOT NULL,
  `name` char(50)  NOT NULL,
  `description` char(100)  NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_tariff_packages 结构
CREATE TABLE IF NOT EXISTS `t_sys_tariff_packages` (
  `id` int(11) NOT NULL COMMENT '套餐id 0-基础版，1-标准版，2-专业版，3-独享版,4-自定义',
  `name` char(36)  NOT NULL COMMENT '套餐名字',
  `roleId` bigint(20) unsigned NOT NULL COMMENT '角色id',
  `isdefault` bit(1) NOT NULL DEFAULT b'0',
  `maxShopCount` int(11) DEFAULT '1' COMMENT '绑定店铺数量',
  `maxProductCount` int(11) DEFAULT '50000' COMMENT '商品数量上限',
  `maxOrderCount` int(11) DEFAULT '3600' COMMENT '处理订单上限',
  `maxMember` int(11) DEFAULT '10' COMMENT '子用户数量上限',
  `maxProfitPlanCount` int(11) DEFAULT '1' COMMENT '利润计算方案数量',
  `maxMarketCount` int(11) DEFAULT '1' COMMENT '每个店铺绑定站点数量',
  `orderMemoryCount` char(10)  NOT NULL DEFAULT '3' COMMENT '订单存储时间 单位:月  默认基础版是3个月',
  `dayOpenAdvCount` int(11) NOT NULL DEFAULT '10' COMMENT '每天开启广告组数量',
  `controlProductCount` int(11) NOT NULL DEFAULT '10' COMMENT '跟卖监控产品数量',
  `anysisProductCount` int(11) NOT NULL DEFAULT '10' COMMENT '商品分析数量',
  `yearprice` decimal(18,2) DEFAULT NULL,
  `monthprice` decimal(18,2) DEFAULT NULL,
  `lastUpdateTime` date DEFAULT NULL COMMENT '最后更新时间',
  `lastUpdateUser` varchar(36)  DEFAULT NULL COMMENT '最后更新的人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='套餐表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_tariff_packages_append 结构
CREATE TABLE IF NOT EXISTS `t_sys_tariff_packages_append` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ftype` char(20) DEFAULT NULL,
  `name` char(50) CHARACTER SET utf8  DEFAULT NULL,
  `units` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_tariff_packages_append_discount 结构
CREATE TABLE IF NOT EXISTS `t_sys_tariff_packages_append_discount` (
  `appendid` int(10) DEFAULT NULL,
  `packages` int(10) DEFAULT NULL,
  `month` int(10) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  UNIQUE KEY `appendid_packages_month` (`appendid`,`packages`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_timetask 结构
CREATE TABLE IF NOT EXISTS `t_sys_timetask` (
  `id` char(36)  NOT NULL COMMENT 'id',
  `name` varchar(50)  DEFAULT NULL COMMENT '任务名',
  `group_name` varchar(50)  DEFAULT NULL COMMENT '任务组',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `cron` varchar(30)  DEFAULT NULL COMMENT '表达式',
  `job_status` varchar(20)  DEFAULT NULL COMMENT '发布状态',
  `plan_status` varchar(20)  DEFAULT NULL COMMENT '计划状态',
  `is_concurrent` int(4) DEFAULT NULL COMMENT '运行状态',
  `job_data` longtext  COMMENT '参数',
  `menthod_name` varchar(200)  DEFAULT NULL COMMENT '方法',
  `bean_name` varchar(200)  DEFAULT NULL COMMENT '实例bean',
  `description` varchar(1000)  DEFAULT NULL COMMENT '任务描述',
  `creator` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `operator` bigint(20) unsigned DEFAULT NULL COMMENT '操作人',
  `opttime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_timetask_log 结构
CREATE TABLE IF NOT EXISTS `t_sys_timetask_log` (
  `id` char(36)  NOT NULL,
  `createdate` datetime DEFAULT NULL,
  `job_id` char(36)  DEFAULT NULL,
  `reason` varchar(255)  DEFAULT NULL,
  `state` varchar(20)  DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_usernotify 结构
CREATE TABLE IF NOT EXISTS `t_sys_usernotify` (
  `userid` bigint(20) unsigned NOT NULL COMMENT '用户消息所属者',
  `notify` int(11) NOT NULL,
  `isRead` bit(1) NOT NULL DEFAULT b'0' COMMENT '0，代表未读；1，代表已读',
  `createdAt` datetime DEFAULT NULL,
  PRIMARY KEY (`userid`,`notify`),
  KEY `Index 2` (`notify`),
  KEY `createdAt` (`isRead`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_user_bind 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_bind` (
  `userid` bigint(20) unsigned NOT NULL,
  `bindid` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`userid`) USING BTREE,
  KEY `bindid` (`bindid`)
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_user_datalimit 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_datalimit` (
  `userid` bigint(20) unsigned NOT NULL,
  `datatype` char(15)  NOT NULL COMMENT 'owner只能查看自己负责的产品（在产品管理页面配置）;operations只能查看自己运营的产品（在商品分析页面配置）',
  `islimit` bit(1) DEFAULT b'0' COMMENT 'true表示需要限制，false表示不需要限制',
  PRIMARY KEY (`userid`,`datatype`) USING BTREE
) ENGINE=InnoDB COMMENT='用户数据权限，放在用户信息中，登录后将在所有模块生效';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_user_group 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_group` (
  `userid` bigint(20) unsigned DEFAULT NULL COMMENT '用户ID',
  `groupid` bigint(20) unsigned DEFAULT NULL COMMENT '店铺ID'
) ENGINE=InnoDB COMMENT='用户客户对店铺的权限绑定';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_user_ip_city 结构
CREATE TABLE IF NOT EXISTS `t_sys_user_ip_city` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userip` char(50)  NOT NULL DEFAULT '0',
  `city` char(20)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userip` (`userip`)
) ENGINE=InnoDB AUTO_INCREMENT=1385 ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_video 结构
CREATE TABLE IF NOT EXISTS `t_sys_video` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `menuid` char(36) DEFAULT NULL,
  `video_url` varchar(100) DEFAULT NULL,
  `video_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_sys_weather 结构
CREATE TABLE IF NOT EXISTS `t_sys_weather` (
  `id` bigint(20) unsigned NOT NULL,
  `city` varchar(20)  NOT NULL COMMENT '城市',
  `weatype` varchar(20)  DEFAULT NULL COMMENT '值:多云 晴 小雨 大雨',
  `updatedate` datetime DEFAULT NULL COMMENT '当前日期的天气',
  `nowdegree` int(11) DEFAULT NULL COMMENT '当前温度 ''C',
  `lowdegree` varchar(50)  DEFAULT NULL COMMENT '最低温度',
  `highdegree` varchar(50)  DEFAULT NULL COMMENT '最高温度',
  `weaforce` varchar(50)  DEFAULT NULL COMMENT '风向',
  PRIMARY KEY (`id`),
  UNIQUE KEY `city` (`city`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='城市天气表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_tasklock 结构
CREATE TABLE IF NOT EXISTS `t_tasklock` (
  `task` char(15)  NOT NULL,
  `mylock` bit(1) DEFAULT NULL,
  PRIMARY KEY (`task`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` bigint(20) unsigned NOT NULL COMMENT '整型自增主键',
  `account` varchar(100)  NOT NULL,
  `password` char(50) CHARACTER SET latin1 DEFAULT NULL COMMENT '用户密码，采用MD5加密',
  `salt` char(50)  DEFAULT NULL,
  `leader_id` bigint(20) unsigned DEFAULT NULL COMMENT '上层',
  `createDate` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `losingEffect` date DEFAULT NULL COMMENT '失效时间',
  `logicDelete` bit(1) DEFAULT b'0' COMMENT '逻辑删除',
  `disable` bit(1) DEFAULT b'0' COMMENT '停用',
  `isActive` bit(1) DEFAULT b'1' COMMENT '账户是否激活',
  `hasEmail` bit(1) DEFAULT b'0' COMMENT '邮箱是否激活',
  `member` int(8) DEFAULT '5' COMMENT '拥有下属数量上限',
  `passwordkey` char(36)  DEFAULT NULL,
  `lastlogintime` datetime DEFAULT NULL,
  `lastloginip` varchar(100)  DEFAULT NULL,
  `lastloginsession` varchar(200)  DEFAULT NULL,
  `ftype` char(50)  DEFAULT NULL,
  `oldid` char(36)  DEFAULT NULL,
  `deptid` bigint(20) unsigned DEFAULT NULL,
  `opttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`),
  KEY `leader_id` (`leader_id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_userinfo 结构
CREATE TABLE IF NOT EXISTS `t_userinfo` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(100)  DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `picture` char(36)  DEFAULT NULL,
  `tel` varchar(25)  DEFAULT NULL,
  `company` varchar(100)  DEFAULT NULL,
  `email` varchar(100)  DEFAULT NULL,
  `remark` varchar(255)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t_userinfo_t_picture` (`picture`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='用户详细信息表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_user_role 结构
CREATE TABLE IF NOT EXISTS `t_user_role` (
  `id` bigint(20) unsigned NOT NULL,
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index 4` (`user_id`,`role_id`),
  KEY `FK_t_user_role_t_user` (`user_id`),
  KEY `FK_t_user_role_t_role` (`role_id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='用户角色分配表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_user_shop 结构
CREATE TABLE IF NOT EXISTS `t_user_shop` (
  `id` bigint(20) unsigned NOT NULL,
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户ID',
  `shop_id` bigint(20) unsigned DEFAULT NULL COMMENT '店铺ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shop_id` (`shop_id`),
  KEY `FK_t_user_shop_t_user` (`user_id`)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC COMMENT='用户店铺归属表';

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_user_wechat_login 结构
CREATE TABLE IF NOT EXISTS `t_user_wechat_login` (
  `openid` char(36)  NOT NULL,
  `userid` bigint(20) unsigned NOT NULL,
  `unionid` char(36)  DEFAULT NULL,
  `access_token` char(200)  DEFAULT NULL,
  `refresh_token` char(200)  DEFAULT NULL,
  PRIMARY KEY (`openid`) USING BTREE,
  UNIQUE KEY `userid` (`userid`) USING BTREE,
  UNIQUE KEY `unionid` (`unionid`) USING BTREE
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_admin.t_user_wechat_mp 结构
CREATE TABLE IF NOT EXISTS `t_user_wechat_mp` (
  `openid` char(36)  NOT NULL,
  `userid` bigint(20) unsigned NOT NULL,
  `ftype` char(10)  NOT NULL,
  `access_token` char(200)  DEFAULT NULL,
  `refresh_token` char(200)  DEFAULT NULL,
  PRIMARY KEY (`openid`,`ftype`,`userid`) USING BTREE,
  KEY `key` (`userid`,`ftype`) USING BTREE
) ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_admin.undo_log 结构
CREATE TABLE IF NOT EXISTS `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB;

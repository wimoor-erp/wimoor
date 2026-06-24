-- --------------------------------------------------------
-- 报表映射规则默认数据
-- 基于会计准则的科目分类规则
-- 注意：groupid 需要根据实际租户ID进行替换
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- =============================================
-- 资产负债表映射规则
-- =============================================

-- 流动资产相关规则
INSERT INTO `fin_report_mapping_rules` (`groupid`, `template_type`, `item_code`, `rule_name`, `match_type`, `match_value`, `operator`, `subject_type`, `direction`, `is_leaf_only`, `priority`, `description`) VALUES
-- 货币资金：1001-1012
(0, 'BALANCE_SHEET', 'ASSET_CASH', '库存现金', 'PREFIX', '1001', 'ADD', 1, 1, 1, 10, '1001-库存现金'),
(0, 'BALANCE_SHEET', 'ASSET_CASH', '银行存款', 'PREFIX', '1002', 'ADD', 1, 1, 1, 10, '1002-银行存款'),
(0, 'BALANCE_SHEET', 'ASSET_CASH', '其他货币资金', 'PREFIX', '1012', 'ADD', 1, 1, 1, 10, '1012-其他货币资金'),

-- 交易性金融资产：1101
(0, 'BALANCE_SHEET', 'ASSET_FINANCIAL', '交易性金融资产', 'PREFIX', '1101', 'ADD', 1, 1, 1, 10, '1101-交易性金融资产'),

-- 应收票据：1121
(0, 'BALANCE_SHEET', 'ASSET_BILLS', '应收票据', 'PREFIX', '1121', 'ADD', 1, 1, 1, 10, '1121-应收票据'),

-- 应收账款：1122（需要减去坏账准备1231）
(0, 'BALANCE_SHEET', 'ASSET_RECEIVABLE', '应收账款', 'PREFIX', '1122', 'ADD', 1, 1, 1, 10, '1122-应收账款'),
(0, 'BALANCE_SHEET', 'ASSET_RECEIVABLE', '坏账准备', 'PREFIX', '1231', 'SUBTRACT', 1, 2, 1, 20, '1231-坏账准备（减项）'),

-- 预付款项：1123
(0, 'BALANCE_SHEET', 'ASSET_PREPAY', '预付款项', 'PREFIX', '1123', 'ADD', 1, 1, 1, 10, '1123-预付款项'),

-- 应收利息：1132
(0, 'BALANCE_SHEET', 'ASSET_INTEREST_RECEIVABLE', '应收利息', 'PREFIX', '1132', 'ADD', 1, 1, 1, 10, '1132-应收利息'),

-- 应收股利：1131
(0, 'BALANCE_SHEET', 'ASSET_DIVIDEND_RECEIVABLE', '应收股利', 'PREFIX', '1131', 'ADD', 1, 1, 1, 10, '1131-应收股利'),

-- 其他应收款：1221
(0, 'BALANCE_SHEET', 'ASSET_OTHER_RECEIVABLE', '其他应收款', 'PREFIX', '1221', 'ADD', 1, 1, 1, 10, '1221-其他应收款'),

-- 存货：1401-1411（需要减去存货跌价准备1471）
(0, 'BALANCE_SHEET', 'ASSET_INVENTORY', '材料采购', 'PREFIX', '1401', 'ADD', 1, 1, 1, 10, '1401-材料采购'),
(0, 'BALANCE_SHEET', 'ASSET_INVENTORY', '在途物资', 'PREFIX', '1402', 'ADD', 1, 1, 1, 10, '1402-在途物资'),
(0, 'BALANCE_SHEET', 'ASSET_INVENTORY', '原材料', 'PREFIX', '1403', 'ADD', 1, 1, 1, 10, '1403-原材料'),
(0, 'BALANCE_SHEET', 'ASSET_INVENTORY', '材料成本差异', 'PREFIX', '1404', 'ADD', 1, 1, 1, 10, '1404-材料成本差异'),
(0, 'BALANCE_SHEET', 'ASSET_INVENTORY', '库存商品', 'PREFIX', '1405', 'ADD', 1, 1, 1, 10, '1405-库存商品'),
(0, 'BALANCE_SHEET', 'ASSET_INVENTORY', '发出商品', 'PREFIX', '1406', 'ADD', 1, 1, 1, 10, '1406-发出商品'),
(0, 'BALANCE_SHEET', 'ASSET_INVENTORY', '商品进销差价', 'PREFIX', '1407', 'ADD', 1, 1, 1, 10, '1407-商品进销差价'),
(0, 'BALANCE_SHEET', 'ASSET_INVENTORY', '委托加工物资', 'PREFIX', '1408', 'ADD', 1, 1, 1, 10, '1408-委托加工物资'),
(0, 'BALANCE_SHEET', 'ASSET_INVENTORY', '周转材料', 'PREFIX', '1411', 'ADD', 1, 1, 1, 10, '1411-周转材料'),
(0, 'BALANCE_SHEET', 'ASSET_INVENTORY', '存货跌价准备', 'PREFIX', '1471', 'SUBTRACT', 1, 2, 1, 20, '1471-存货跌价准备（减项）'),

-- 其他流动资产：1301
(0, 'BALANCE_SHEET', 'ASSET_CURRENT_OTHER', '待摊费用', 'PREFIX', '1301', 'ADD', 1, 1, 1, 10, '1301-待摊费用'),

-- 可供出售金融资产：1503
(0, 'BALANCE_SHEET', 'ASSET_AVAILABLE_SALE', '可供出售金融资产', 'PREFIX', '1503', 'ADD', 1, 1, 1, 10, '1503-可供出售金融资产'),

-- 持有至到期投资：1501
(0, 'BALANCE_SHEET', 'ASSET_HELD_TO_MATURITY', '持有至到期投资', 'PREFIX', '1501', 'ADD', 1, 1, 1, 10, '1501-持有至到期投资'),

-- 长期应收款：1511
(0, 'BALANCE_SHEET', 'ASSET_LONG_RECEIVABLE', '长期应收款', 'PREFIX', '1511', 'ADD', 1, 1, 1, 10, '1511-长期应收款'),

-- 长期股权投资：1511
(0, 'BALANCE_SHEET', 'ASSET_LONG_INVEST', '长期股权投资', 'PREFIX', '1511', 'ADD', 1, 1, 1, 10, '1511-长期股权投资'),

-- 投资性房地产：1521
(0, 'BALANCE_SHEET', 'ASSET_INVESTMENT_PROPERTY', '投资性房地产', 'PREFIX', '1521', 'ADD', 1, 1, 1, 10, '1521-投资性房地产'),

-- 固定资产：1601（需要减去累计折旧1602和减值准备1603）
(0, 'BALANCE_SHEET', 'ASSET_FIXED', '固定资产', 'PREFIX', '1601', 'ADD', 1, 1, 1, 10, '1601-固定资产'),
(0, 'BALANCE_SHEET', 'ASSET_FIXED', '累计折旧', 'PREFIX', '1602', 'SUBTRACT', 1, 2, 1, 20, '1602-累计折旧（减项）'),
(0, 'BALANCE_SHEET', 'ASSET_FIXED', '固定资产减值准备', 'PREFIX', '1603', 'SUBTRACT', 1, 2, 1, 20, '1603-固定资产减值准备（减项）'),

-- 在建工程：1604
(0, 'BALANCE_SHEET', 'ASSET_CONSTRUCTION', '在建工程', 'PREFIX', '1604', 'ADD', 1, 1, 1, 10, '1604-在建工程'),

-- 工程物资：1605
(0, 'BALANCE_SHEET', 'ASSET_ENGINEERING_MATERIAL', '工程物资', 'PREFIX', '1605', 'ADD', 1, 1, 1, 10, '1605-工程物资'),

-- 固定资产清理：1606
(0, 'BALANCE_SHEET', 'ASSET_FIXED_CLEANUP', '固定资产清理', 'PREFIX', '1606', 'ADD', 1, 1, 1, 10, '1606-固定资产清理'),

-- 无形资产：1701（需要减去累计摊销1702和减值准备1703）
(0, 'BALANCE_SHEET', 'ASSET_INTANGIBLE', '无形资产', 'PREFIX', '1701', 'ADD', 1, 1, 1, 10, '1701-无形资产'),
(0, 'BALANCE_SHEET', 'ASSET_INTANGIBLE', '累计摊销', 'PREFIX', '1702', 'SUBTRACT', 1, 2, 1, 20, '1702-累计摊销（减项）'),
(0, 'BALANCE_SHEET', 'ASSET_INTANGIBLE', '无形资产减值准备', 'PREFIX', '1703', 'SUBTRACT', 1, 2, 1, 20, '1703-无形资产减值准备（减项）'),

-- 长期待摊费用：1801
(0, 'BALANCE_SHEET', 'ASSET_DEFERRED', '长期待摊费用', 'PREFIX', '1801', 'ADD', 1, 1, 1, 10, '1801-长期待摊费用'),

-- 递延所得税资产：1811
(0, 'BALANCE_SHEET', 'ASSET_DEFERRED_TAX', '递延所得税资产', 'PREFIX', '1811', 'ADD', 1, 1, 1, 10, '1811-递延所得税资产'),

-- 其他非流动资产：1901
(0, 'BALANCE_SHEET', 'ASSET_NON_CURRENT_OTHER', '其他非流动资产', 'PREFIX', '1901', 'ADD', 1, 1, 1, 10, '1901-其他非流动资产');

-- 流动负债相关规则
INSERT INTO `fin_report_mapping_rules` (`groupid`, `template_type`, `item_code`, `rule_name`, `match_type`, `match_value`, `operator`, `subject_type`, `direction`, `is_leaf_only`, `priority`, `description`) VALUES
-- 短期借款：2001
(0, 'BALANCE_SHEET', 'LIABILITY_SHORT_LOAN', '短期借款', 'PREFIX', '2001', 'ADD', 2, 2, 1, 10, '2001-短期借款'),

-- 应付票据：2201
(0, 'BALANCE_SHEET', 'LIABILITY_BILLS', '应付票据', 'PREFIX', '2201', 'ADD', 2, 2, 1, 10, '2201-应付票据'),

-- 应付账款：2202
(0, 'BALANCE_SHEET', 'LIABILITY_PAYABLE', '应付账款', 'PREFIX', '2202', 'ADD', 2, 2, 1, 10, '2202-应付账款'),

-- 预收款项：2203
(0, 'BALANCE_SHEET', 'LIABILITY_ADVANCE', '预收款项', 'PREFIX', '2203', 'ADD', 2, 2, 1, 10, '2203-预收款项'),

-- 应付职工薪酬：2211
(0, 'BALANCE_SHEET', 'LIABILITY_SALARY', '应付职工薪酬', 'PREFIX', '2211', 'ADD', 2, 2, 1, 10, '2211-应付职工薪酬'),

-- 应交税费：2221
(0, 'BALANCE_SHEET', 'LIABILITY_TAX', '应交税费', 'PREFIX', '2221', 'ADD', 2, 2, 1, 10, '2221-应交税费'),

-- 应付利息：2231
(0, 'BALANCE_SHEET', 'LIABILITY_INTEREST_PAYABLE', '应付利息', 'PREFIX', '2231', 'ADD', 2, 2, 1, 10, '2231-应付利息'),

-- 应付股利：2232
(0, 'BALANCE_SHEET', 'LIABILITY_DIVIDEND_PAYABLE', '应付股利', 'PREFIX', '2232', 'ADD', 2, 2, 1, 10, '2232-应付股利'),

-- 其他应付款：2241
(0, 'BALANCE_SHEET', 'LIABILITY_OTHER_PAYABLE', '其他应付款', 'PREFIX', '2241', 'ADD', 2, 2, 1, 10, '2241-其他应付款'),

-- 预提费用：2301
(0, 'BALANCE_SHEET', 'LIABILITY_ACCRUED_EXPENSE', '预提费用', 'PREFIX', '2301', 'ADD', 2, 2, 1, 10, '2301-预提费用'),

-- 其他流动负债：2311
(0, 'BALANCE_SHEET', 'LIABILITY_CURRENT_OTHER', '其他流动负债', 'PREFIX', '2311', 'ADD', 2, 2, 1, 10, '2311-其他流动负债'),

-- 长期借款：2501
(0, 'BALANCE_SHEET', 'LIABILITY_LONG_LOAN', '长期借款', 'PREFIX', '2501', 'ADD', 2, 2, 1, 10, '2501-长期借款'),

-- 应付债券：2502
(0, 'BALANCE_SHEET', 'LIABILITY_BONDS', '应付债券', 'PREFIX', '2502', 'ADD', 2, 2, 1, 10, '2502-应付债券'),

-- 长期应付款：2701
(0, 'BALANCE_SHEET', 'LIABILITY_LONG_PAYABLE', '长期应付款', 'PREFIX', '2701', 'ADD', 2, 2, 1, 10, '2701-长期应付款'),

-- 专项应付款：2702
(0, 'BALANCE_SHEET', 'LIABILITY_SPECIAL_PAYABLE', '专项应付款', 'PREFIX', '2702', 'ADD', 2, 2, 1, 10, '2702-专项应付款'),

-- 预计负债：2801
(0, 'BALANCE_SHEET', 'LIABILITY_ESTIMATED', '预计负债', 'PREFIX', '2801', 'ADD', 2, 2, 1, 10, '2801-预计负债'),

-- 递延所得税负债：2901
(0, 'BALANCE_SHEET', 'LIABILITY_DEFERRED_TAX', '递延所得税负债', 'PREFIX', '2901', 'ADD', 2, 2, 1, 10, '2901-递延所得税负债'),

-- 其他非流动负债：2911
(0, 'BALANCE_SHEET', 'LIABILITY_NON_CURRENT_OTHER', '其他非流动负债', 'PREFIX', '2911', 'ADD', 2, 2, 1, 10, '2911-其他非流动负债');

-- 所有者权益相关规则
INSERT INTO `fin_report_mapping_rules` (`groupid`, `template_type`, `item_code`, `rule_name`, `match_type`, `match_value`, `operator`, `subject_type`, `direction`, `is_leaf_only`, `priority`, `description`) VALUES
-- 实收资本：4001
(0, 'BALANCE_SHEET', 'EQUITY_CAPITAL', '实收资本', 'PREFIX', '4001', 'ADD', 3, 2, 1, 10, '4001-实收资本'),

-- 资本公积：4002
(0, 'BALANCE_SHEET', 'EQUITY_RESERVE', '资本公积', 'PREFIX', '4002', 'ADD', 3, 2, 1, 10, '4002-资本公积'),

-- 盈余公积：4101
(0, 'BALANCE_SHEET', 'EQUITY_SURPLUS', '盈余公积', 'PREFIX', '4101', 'ADD', 3, 2, 1, 10, '4101-盈余公积'),

-- 本年利润：4103
(0, 'BALANCE_SHEET', 'EQUITY_CURRENT_PROFIT', '本年利润', 'PREFIX', '4103', 'ADD', 3, 2, 1, 10, '4103-本年利润'),

-- 利润分配：4104
(0, 'BALANCE_SHEET', 'EQUITY_PROFIT分配', '利润分配', 'PREFIX', '4104', 'ADD', 3, 2, 1, 10, '4104-利润分配'),

-- 库存股：4201
(0, 'BALANCE_SHEET', 'EQUITY_TREASURY_STOCK', '库存股', 'PREFIX', '4201', 'SUBTRACT', 3, 2, 1, 10, '4201-库存股（减项）'),

-- 其他综合收益：4301
(0, 'BALANCE_SHEET', 'EQUITY_OTHER_COMPREHENSIVE', '其他综合收益', 'PREFIX', '4301', 'ADD', 3, 2, 1, 10, '4301-其他综合收益'),

-- 其他权益工具：4401
(0, 'BALANCE_SHEET', 'EQUITY_OTHER_INSTRUMENT', '其他权益工具', 'PREFIX', '4401', 'ADD', 3, 2, 1, 10, '4401-其他权益工具');

-- =============================================
-- 利润表映射规则
-- =============================================

-- 收入类科目
INSERT INTO `fin_report_mapping_rules` (`groupid`, `template_type`, `item_code`, `rule_name`, `match_type`, `match_value`, `operator`, `subject_type`, `direction`, `is_leaf_only`, `priority`, `description`) VALUES
-- 主营业务收入：6001
(0, 'INCOME_STATEMENT', 'REVENUE_MAIN', '主营业务收入', 'PREFIX', '6001', 'ADD', 5, 2, 1, 10, '6001-主营业务收入'),

-- 其他业务收入：6051
(0, 'INCOME_STATEMENT', 'REVENUE_OTHER', '其他业务收入', 'PREFIX', '6051', 'ADD', 5, 2, 1, 10, '6051-其他业务收入'),

-- 公允价值变动收益：6101
(0, 'INCOME_STATEMENT', 'INCOME_FAIR_VALUE', '公允价值变动收益', 'PREFIX', '6101', 'ADD', 5, 2, 1, 10, '6101-公允价值变动收益'),

-- 投资收益：6111
(0, 'INCOME_STATEMENT', 'INCOME_INVESTMENT', '投资收益', 'PREFIX', '6111', 'ADD', 5, 2, 1, 10, '6111-投资收益'),

-- 营业外收入：6301
(0, 'INCOME_STATEMENT', 'INCOME_NON_OPERATING', '营业外收入', 'PREFIX', '6301', 'ADD', 5, 2, 1, 10, '6301-营业外收入');

-- 成本费用类科目
INSERT INTO `fin_report_mapping_rules` (`groupid`, `template_type`, `item_code`, `rule_name`, `match_type`, `match_value`, `operator`, `subject_type`, `direction`, `is_leaf_only`, `priority`, `description`) VALUES
-- 主营业务成本：6401
(0, 'INCOME_STATEMENT', 'COST_MAIN', '主营业务成本', 'PREFIX', '6401', 'ADD', 5, 1, 1, 10, '6401-主营业务成本'),

-- 其他业务成本：6402
(0, 'INCOME_STATEMENT', 'COST_OTHER', '其他业务成本', 'PREFIX', '6402', 'ADD', 5, 1, 1, 10, '6402-其他业务成本'),

-- 营业税金及附加：6403
(0, 'INCOME_STATEMENT', 'TAX_SURCHARGE', '营业税金及附加', 'PREFIX', '6403', 'ADD', 5, 1, 1, 10, '6403-营业税金及附加'),

-- 销售费用：6601
(0, 'INCOME_STATEMENT', 'EXPENSE_SELLING', '销售费用', 'PREFIX', '6601', 'ADD', 5, 1, 1, 10, '6601-销售费用'),

-- 管理费用：6602
(0, 'INCOME_STATEMENT', 'EXPENSE_ADMIN', '管理费用', 'PREFIX', '6602', 'ADD', 5, 1, 1, 10, '6602-管理费用'),

-- 财务费用：6603
(0, 'INCOME_STATEMENT', 'EXPENSE_FINANCIAL', '财务费用', 'PREFIX', '6603', 'ADD', 5, 1, 1, 10, '6603-财务费用'),

-- 资产减值损失：6701
(0, 'INCOME_STATEMENT', 'EXPENSE_ASSET_IMPAIRMENT', '资产减值损失', 'PREFIX', '6701', 'ADD', 5, 1, 1, 10, '6701-资产减值损失'),

-- 营业外支出：6711
(0, 'INCOME_STATEMENT', 'EXPENSE_NON_OPERATING', '营业外支出', 'PREFIX', '6711', 'ADD', 5, 1, 1, 10, '6711-营业外支出'),

-- 所得税费用：6801
(0, 'INCOME_STATEMENT', 'EXPENSE_INCOME_TAX', '所得税费用', 'PREFIX', '6801', 'ADD', 5, 1, 1, 10, '6801-所得税费用');

-- =============================================
-- 现金流量表映射规则
-- =============================================

-- 经营活动现金流入
INSERT INTO `fin_report_mapping_rules` (`groupid`, `template_type`, `item_code`, `rule_name`, `match_type`, `match_value`, `operator`, `subject_type`, `direction`, `is_leaf_only`, `priority`, `description`) VALUES
-- 销售商品、提供劳务收到的现金：需要根据应收账款、预收账款等科目变动计算
(0, 'CASH_FLOW', 'CASH_IN_SALES', '销售商品提供劳务收到的现金', 'CUSTOM', 'SALES_CASH_RECEIVED', 'ADD', NULL, NULL, 1, 10, '根据应收账款、预收账款等科目变动计算'),

-- 收到的税费返还：6802
(0, 'CASH_FLOW', 'CASH_IN_TAX_REFUND', '收到的税费返还', 'PREFIX', '6802', 'ADD', 5, 1, 1, 10, '6802-收到的税费返还'),

-- 收到其他与经营活动有关的现金：6301（营业外收入中的现金部分）
(0, 'CASH_FLOW', 'CASH_IN_OTHER_OPERATING', '收到其他与经营活动有关的现金', 'PREFIX', '6301', 'ADD', 5, 1, 1, 10, '6301-营业外收入中的现金部分');

-- 经营活动现金流出
INSERT INTO `fin_report_mapping_rules` (`groupid`, `template_type`, `item_code`, `rule_name`, `match_type`, `match_value`, `operator`, `subject_type`, `direction`, `is_leaf_only`, `priority`, `description`) VALUES
-- 购买商品、接受劳务支付的现金：需要根据应付账款、存货等科目变动计算
(0, 'CASH_FLOW', 'CASH_OUT_PURCHASE', '购买商品接受劳务支付的现金', 'CUSTOM', 'PURCHASE_CASH_PAID', 'ADD', NULL, NULL, 1, 10, '根据应付账款、存货等科目变动计算'),

-- 支付给职工以及为职工支付的现金：2211（应付职工薪酬变动）
(0, 'CASH_FLOW', 'CASH_OUT_SALARY', '支付给职工以及为职工支付的现金', 'CUSTOM', 'SALARY_CASH_PAID', 'ADD', NULL, NULL, 1, 10, '根据应付职工薪酬科目变动计算'),

-- 支付的各项税费：2221（应交税费变动）
(0, 'CASH_FLOW', 'CASH_OUT_TAX', '支付的各项税费', 'CUSTOM', 'TAX_CASH_PAID', 'ADD', NULL, NULL, 1, 10, '根据应交税费科目变动计算'),

-- 支付其他与经营活动有关的现金：6601+6602（销售费用+管理费用）
(0, 'CASH_FLOW', 'CASH_OUT_OTHER_OPERATING', '支付其他与经营活动有关的现金', 'CUSTOM', 'OTHER_OPERATING_CASH_PAID', 'ADD', NULL, NULL, 1, 10, '根据销售费用、管理费用等科目计算');

-- 投资活动现金流入
INSERT INTO `fin_report_mapping_rules` (`groupid`, `template_type`, `item_code`, `rule_name`, `match_type`, `match_value`, `operator`, `subject_type`, `direction`, `is_leaf_only`, `priority`, `description`) VALUES
-- 收回投资收到的现金：1101+1503（交易性金融资产+可供出售金融资产减少）
(0, 'CASH_FLOW', 'CASH_IN_INVEST_RECOVER', '收回投资收到的现金', 'CUSTOM', 'INVEST_RECOVER_CASH', 'ADD', NULL, NULL, 1, 10, '根据交易性金融资产、可供出售金融资产减少计算'),

-- 取得投资收益收到的现金：6111（投资收益）
(0, 'CASH_FLOW', 'CASH_IN_INVEST_INCOME', '取得投资收益收到的现金', 'PREFIX', '6111', 'ADD', 5, 1, 1, 10, '6111-投资收益'),

-- 处置固定资产、无形资产和其他长期资产收回的现金净额
(0, 'CASH_FLOW', 'CASH_IN_ASSET_DISPOSE', '处置固定资产无形资产和其他长期资产收回的现金净额', 'CUSTOM', 'ASSET_DISPOSE_CASH', 'ADD', NULL, NULL, 1, 10, '根据固定资产清理等科目计算');

-- 投资活动现金流出
INSERT INTO `fin_report_mapping_rules` (`groupid`, `template_type`, `item_code`, `rule_name`, `match_type`, `match_value`, `operator`, `subject_type`, `direction`, `is_leaf_only`, `priority`, `description`) VALUES
-- 购建固定资产、无形资产和其他长期资产支付的现金：1601+1701增加
(0, 'CASH_FLOW', 'CASH_OUT_INVEST', '购建固定资产无形资产和其他长期资产支付的现金', 'CUSTOM', 'FIXED_ASSET_PURCHASE_CASH', 'ADD', NULL, NULL, 1, 10, '根据固定资产、无形资产增加计算'),

-- 投资支付的现金：1101+1503+1511增加
(0, 'CASH_FLOW', 'CASH_OUT_INVEST_PAY', '投资支付的现金', 'CUSTOM', 'INVESTMENT_CASH_PAID', 'ADD', NULL, NULL, 1, 10, '根据交易性金融资产、可供出售金融资产、长期股权投资增加计算');

-- 筹资活动现金流入
INSERT INTO `fin_report_mapping_rules` (`groupid`, `template_type`, `item_code`, `rule_name`, `match_type`, `match_value`, `operator`, `subject_type`, `direction`, `is_leaf_only`, `priority`, `description`) VALUES
-- 吸收投资收到的现金：4001+4002增加
(0, 'CASH_FLOW', 'CASH_IN_INVEST_ABSORB', '吸收投资收到的现金', 'CUSTOM', 'CAPITAL_INCREASE_CASH', 'ADD', NULL, NULL, 1, 10, '根据实收资本、资本公积增加计算'),

-- 取得借款收到的现金：2001+2501增加
(0, 'CASH_FLOW', 'CASH_IN_LOAN', '取得借款收到的现金', 'CUSTOM', 'LOAN_RECEIVED_CASH', 'ADD', NULL, NULL, 1, 10, '根据短期借款、长期借款增加计算');

-- 筹资活动现金流出
INSERT INTO `fin_report_mapping_rules` (`groupid`, `template_type`, `item_code`, `rule_name`, `match_type`, `match_value`, `operator`, `subject_type`, `direction`, `is_leaf_only`, `priority`, `description`) VALUES
-- 偿还债务支付的现金：2001+2501减少
(0, 'CASH_FLOW', 'CASH_OUT_LOAN_REPAY', '偿还债务支付的现金', 'CUSTOM', 'LOAN_REPAYMENT_CASH', 'ADD', NULL, NULL, 1, 10, '根据短期借款、长期借款减少计算'),

-- 分配股利、利润或偿付利息支付的现金：2232+2231减少
(0, 'CASH_FLOW', 'CASH_OUT_DIVIDEND', '分配股利利润或偿付利息支付的现金', 'CUSTOM', 'DIVIDEND_CASH_PAID', 'ADD', NULL, NULL, 1, 10, '根据应付股利、应付利息减少计算');

-- 现金及现金等价物
INSERT INTO `fin_report_mapping_rules` (`groupid`, `template_type`, `item_code`, `rule_name`, `match_type`, `match_value`, `operator`, `subject_type`, `direction`, `is_leaf_only`, `priority`, `description`) VALUES
-- 汇率变动对现金的影响
(0, 'CASH_FLOW', 'CASH_EFFECT_EXCHANGE', '汇率变动对现金的影响', 'CUSTOM', 'EXCHANGE_RATE_EFFECT', 'ADD', NULL, NULL, 1, 10, '根据外币现金科目汇率变动计算'),

-- 现金及现金等价物净增加额
(0, 'CASH_FLOW', 'CASH_NET_INCREASE', '现金及现金等价物净增加额', 'CALCULATED', 'SUM(OPERATING)+SUM(INVESTING)+SUM(FINANCING)+EXCHANGE', 'ADD', NULL, NULL, 1, 10, '经营活动+投资活动+筹资活动+汇率变动'),

-- 期初现金及现金等价物余额
(0, 'CASH_FLOW', 'CASH_BEGINNING', '期初现金及现金等价物余额', 'CUSTOM', 'BEGINNING_CASH_BALANCE', 'ADD', NULL, NULL, 1, 10, '上期期末的货币资金余额'),

-- 期末现金及现金等价物余额
(0, 'CASH_FLOW', 'CASH_ENDING', '期末现金及现金等价物余额', 'CALCULATED', 'BEGINNING+NET_INCREASE', 'ADD', NULL, NULL, 1, 10, '期初余额+净增加额');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, '1') */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, '1') */;

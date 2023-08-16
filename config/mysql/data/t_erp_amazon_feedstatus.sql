INSERT INTO `t_erp_amazon_feedstatus` (`status`, `name`, `remark`) VALUES ('AWAITING_ASYNCHRONOUS_REPLY', '正在处理该请求', '正在处理该请求，但需要等待外部信息才能完成。');
INSERT INTO `t_erp_amazon_feedstatus` (`status`, `name`, `remark`) VALUES ('CANCELLED', '请求因严重错误而中止', '请求因严重错误而中止。');
INSERT INTO `t_erp_amazon_feedstatus` (`status`, `name`, `remark`) VALUES ('DONE', '请求已处理', '请求已处理。您可以调用 GetFeedSubmissionResult 操作来接收处理报告，该报告列出了上传数据中成功处理的记录以及产生错误的记录。');
INSERT INTO `t_erp_amazon_feedstatus` (`status`, `name`, `remark`) VALUES ('Error', '请求失败', '请求失败');
INSERT INTO `t_erp_amazon_feedstatus` (`status`, `name`, `remark`) VALUES ('IN_PROGRESS', '请求正在处理。', ' 	请求正在处理。');
INSERT INTO `t_erp_amazon_feedstatus` (`status`, `name`, `remark`) VALUES ('IN_SAFETY_NET', '请求正在处理，但系统发现上传数据可能包含潜在错误', '请求正在处理，但系统发现上传数据可能包含潜在错误（例如，请求将删除卖家账户中的所有库存）。亚马逊卖家支持团队将联系卖家，以确认是否应处理该上传数据。');
INSERT INTO `t_erp_amazon_feedstatus` (`status`, `name`, `remark`) VALUES ('SUBMITTED', '已收到请求，但尚未开始处理。', '已收到请求，但尚未开始处理。');
INSERT INTO `t_erp_amazon_feedstatus` (`status`, `name`, `remark`) VALUES ('UNCONFIRMED', '请求等待中', '请求等待中');
INSERT INTO `t_erp_amazon_feedstatus` (`status`, `name`, `remark`) VALUES ('waiting', '排队中', '排队中');

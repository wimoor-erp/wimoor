部署系统需要：
1、mysql 数据库
2、redis 数据库
3、nacos 微服务中心
4、seata 微服务事务（用于分库之后）
5、cas-server 中央登录模块（非必要）


建议使用开发工具：eclipse
建议使用JDK1.8
每次启动需要提前开启nacos 和seata

系统支持：
1、quartz任务，使用微服务调用。
2、自动记录日志@SystemControllerLog("将我放在controller上面")，@SystemControllerLog("将我放在方法上面")
3、自动序列化日期
4、自动抓取亚马逊数据（所有支持的类容已加入在任务表：t_sys_quartz_task ）
5、广告数据抓取，需要自行修改 t_amz_region 中的授权
6、系统使用shiro管理登录，用redis记录登录的session。



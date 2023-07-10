项目总述
====
* Wimoor erp是国内首款百分百开源、支持商用的亚马逊erp系统
* 不仅现有系统免费，系统源码也都是公之于众的，可以任意安装、卸载和升级，没有用户数量限制
* 除了为跨境卖家提供一站式服务，我们也提供系统源代码搭建，成品系统部署搭建，支持功能定制开发等相关服务
* 官网地址：https://wimoor.com/ 商务或技术交流请联系客服QQ：1874049814 微信：wimoorerp
* 系统注册地址：https://erp.wimoor.com/toRegister.do 大家可免费使用
* 欢迎加入Wimoor ERP开源项目交流群（可联系客服QQ或微信拉你入群）

开源协议
====
本系统100%开源，支持商用，遵守MIT协议，采用微服务+前后端分离+中央登录的模式，可支持新旧系统无缝对接

技术栈
====
* 核心框架：SpringBoot 2.0.0
* 持久层框架：Mybatis 1.3.2,Mybatis plus
* 日志管理：Log4j 2.10.0
* JS框架：Es6，vue3, nodejs
* UI框架: element plus, uni-app
* 后台框架: spring cloud , alibaba colud, nacos, quartz,oss
* 项目管理框架: Maven 3.2.3

开发环境
====
建议开发者使用以下环境，可以避免版本带来的问题

* IDE: eclipse
* DB: Mysql5.7+
* JDK: JDK1.8
* Maven: Maven3.2.3+

部署系统需要
====
* mysql 数据库
* redis 数据库
* nacos 微服务中心
* seata 微服务事务（用于分库之后）
* cas-server 中央登录模块（非必要）
* 建议使用开发工具：eclipse
* 建议使用Java语言的软件开发工具包：JDK1.8
* 每次启动需要提前开启：nacos 和seata

系统支持
====
* quartz任务，使用微服务调用。
* 自动记录日志@SystemControllerLog("将我放在controller上面")，@SystemControllerLog("将我放在方法上面")
* 自动序列化日期
* 自动抓取亚马逊数据（所有支持的类容已加入在任务表：t_sys_quartz_task ）
* 广告数据抓取，需要自行修改 t_amz_region 中的授权
* 系统使用shiro管理登录，用redis记录登录的session。

部署说明
====
* 第一步：下载项目
* 第二步：看readme
* 第三步：打开config
* 第四步：看所有文件对应的readme
* 第五步：配置redis,nacos,seata,mysql,node,jdk1.8导入对应配置
* 第六步：导入数据到mysql
* 第七步：用eclipse打开wimoor
* 第八步：运行wimoor-admin,wimoor-gateway,wimoor-auth,wimoor-amazon,wimoor-erp,wimoor-amazon-adv
* 第九步：用前端开发工具hbuild或者VSCode 打开wimoorUI
* 第十步：修改config里面的路由改成127.0.0.1 然后npm run serve

系统界面
====
* 首页
![首页](https://user-images.githubusercontent.com/12423352/203922034-0940d34d-29d3-4126-95f1-23b0e56671d3.png)
* 【采购】-补货规划
![补货规划](https://user-images.githubusercontent.com/12423352/203922135-8b891799-5498-47e0-ad73-567e98a811ce.png)
* 【采购】-采购单
![采购单](https://user-images.githubusercontent.com/12423352/203922215-8e828d3d-35c6-44af-b4a9-282c4e9ec7fb.png)
* 【销售】-商品分析
![商品分析](https://user-images.githubusercontent.com/12423352/203922400-81d80589-8da0-4480-9e99-d70985cadc53.png)
* 【销售】-趋势分析
![趋势分析](https://user-images.githubusercontent.com/12423352/203922473-482359cf-5018-4f1f-a926-842a30df46cd.png)
* 【进销存】-补货规划
![FBA发货规划](https://user-images.githubusercontent.com/12423352/203922644-2488e391-c5e9-4345-822c-14021bd56a1a.png)
* 【广告】-广告管理
![广告管理](https://user-images.githubusercontent.com/12423352/203922777-f8058fcc-2e30-4663-a49f-58542a4c1e46.png)
第一步：解压seata-server-1.4.2.zip
第二步：将config.txt和nacos-config.sh放到根目录，将registry.conf放入conf目录
第三步：在nacos中创建名称：SEATA_GROUP 分组为：SEATA_GROUP 的命名空间。
第四步：运行nacos-config.sh将配置导入nacos
第五步：运行bin中启动seata-server
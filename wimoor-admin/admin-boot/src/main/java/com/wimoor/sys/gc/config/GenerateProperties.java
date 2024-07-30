package com.wimoor.sys.gc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings({"all"})
@Configuration
@ConfigurationProperties(prefix = "gc")
@Data
public class GenerateProperties {

    public String author;               // 作者
    public String email;                // 邮箱
    public String describe;             // 描叙信息
    public String projectName;          // 项目名/路径，如：xj-server/xj-test-server
    public String packPath;             // 包路径
    public String rootModule;           // 根模块(固定为：modules(管理端), 用户端为：client)
    public String moduleName;           // 子模块(业务分类,如用户管理,订单管理模块拆分，也可以统一一个名称放在一起)
    public String tablePrefixDefault;   // 表前缀
    public String fieldPrefixDefault;   // 字段前缀
    public Boolean entitySwagger;       // 实体类是否开启swagger注释
    public String fatherPath;           // 生成路径(为空默认当前项目跟目录)
    public Boolean filterCrud;          // 是否过滤crud方法
    public String vueFieldTypes;        // vue列表排除展示类型字段
    public String basefields;           // 数据表通用字段配置
    public String keywordArray;         // 数据库关键字配置
}


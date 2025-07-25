package com.wimoor.sys.gc.constant;

@SuppressWarnings("all")
public interface TpParamConstant {

	String AUTHOR = "author";                             // 作者
	String EMAIL = "email";                               // 邮箱
	String DESCRIBE = "describe";                         // 描叙信息
	String DATE = "date";                                 // 时间
	String PROJECT_NAME = "projectName";                  // 项目名/路径，如：xj-server/xj-test-server
	String PACK_PATH = "packPath";                        // 包路径 (如: io/github/wslxm)
	String PACK_FILE_PATH = "packFilePath";               // 包路径 (如: io.github.wslxm)
	String ROOT_MODULE = "rootModule";                    // 根模块(固定为：modules(管理端), 用户端为：client)
	String MODULE_NAME = "moduleName";                    // 子模块(业务分类,如用户管理,订单管理模块拆分，也可以统一一个名称放在一起)
	String TABLE_PREFIX_DEFAULT = "tablePrefixDefault";   // 表前缀
	String FIELD_PREFIX_DEFAULT = "fieldPrefixDefault";   // 字段前缀
	String ENTITY_SWAGGER = "entitySwagger";              // 实体类是否开启swagger注释
	String FILTER_CRUD = "filterCrud";                    // 是否过滤crud方法
	String FATHER_PATH = "fatherPath";                    // 生成路径(为空默认当前项目跟目录)
	String VUE_FIELD_TYPES_ARRAY = "vueFieldTypesArray";  // vue列表排除展示类型字段
	String BASE_FIELDS = "basefields";                    // 数据表通用字段配置
	String KEYWORD_ARRAY = "keywordArray";                // 数据库关键字配置
	// 数据库
	String TABLE_NAME = "tableName";                      // 表名
	String TABLE_COMMENT = "tableComment";                // 表注释
	String TABLE_NAME_UP = "tableNameUp";                 // 表名驼峰大写开头
	String TABLE_NAME_LOWER = "tableNameLower";           // 表名驼峰小写开头
}


//package io.github.wslxm.springbootplus2.manage.gc.config;
//
//
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.google.common.base.CaseFormat;
//import io.github.wslxm.springbootplus2.manage.gc.util.TemplateParamsReplace;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// *  代码生成常量类
// *
// * @author ws
// * @mail 1720696548@qq.com
// * @date 2020/2/11 0011 0:08
// * @return
// */
//@SuppressWarnings({"all"})
//@Component
//@Slf4j
//public class GenerateConfig {
//
//
//    //=======================================================================================================
//    //=======================================================================================================
//    //======================================= 初始化配置参数  =================================================
//    //=======================================================================================================
//    //=======================================================================================================
//
//    public static String AUTHOR;                          // 作者
//    public static String EMAIL;                           // 邮箱
//    public static String DESCRIBE;                        // 描叙信息
//    public static String PROJECT_NAME;                    // 项目名/路径，如：xj-server/xj-test-server
//    public static String PACK_PATH;                       // 包路径
//    public static String ROOT_MODULE;                     // 根模块(固定为：modules(管理端), 用户端为：client)
//    //public static String PACK_PATH_ZP;                  // 业务分类模块(sys 系统代码  yw 业务代码)
//    public static String MODULE_NAME;                     // 子模块(业务分类,如用户管理,订单管理模块拆分，也可以统一一个名称放在一起)
//    public static boolean entitySwagger = false;          // 实体类是否使用swagger注释, true=是  false=否 entity / vo / dto 是否使用swagger 注释
//    public static List<String> BASE_FIELDS = null;        // mysql 数据表通用字段
//    public static List<String> KEYWORD_ARRAY = null;      // mysql 关键字配置
//    public static String TABLE_PREFIX_DEFAULT = null;     // 表前缀：TABLE_PREFIX(生成的文件名称自动去除前缀信息）
//    public static String FIELD_PREFIX_DEFAULT = null;     // 字段前缀：FIELD_PREFIX
//    public static String TABLE_PREFIX = null;             // 表前缀：TABLE_PREFIX (实际)
//    public static String FIELD_PREFIX = null;             // 字段前缀：FIELD_PREFIX(实际)
//    public static String FATHER_PATH = "";                // 生成路径,相对路径：value=""当前项目目录下, 绝对路径: 指定磁盘绝对目录
//    //
////    // 预览的 html + java代码生成路径拼接 --> 父工程绝对路径 + 包路径 + 包的下一级路径+ 模块名
////    public static String BASE_PATH_HTML_TXT_YL = null;
////    public static String BASE_PATH_VUE_TXT_YL = null;
////    public static String BASE_PATH_JAVA_YL = null;
////    public static String BASE_PATH_XML_YL = null;
////    // 生成的 html + java代码生成路径拼接 --> 父工程绝对路径 + 包路径 + 包的下一级路径+ 模块名
////    public static String BASE_PATH_HTML = null;
////    public static String BASE_PATH_VUE = null;
////    public static String BASE_PATH_JAVA = null;
////    public static String BASE_PATH_XML = null;
//
//
//    public GenerateConfig(GenerateProperties generateProperties) {
//        AUTHOR = generateProperties.getAuthor();
//        EMAIL = generateProperties.getEmail();
//        DESCRIBE = generateProperties.getDescribe();
//        PROJECT_NAME = generateProperties.getProjectName();
//        PACK_PATH = generateProperties.getPackPath();
//        ROOT_MODULE = generateProperties.getRootModule();
//        // PACK_PATH_ZP = generateProperties.getPackPathZp();
//        MODULE_NAME = generateProperties.getModuleName();
//        entitySwagger = generateProperties.getEntitySwagger();
//        BASE_FIELDS = Arrays.asList(generateProperties.getBasefields().split(","));
//        KEYWORD_ARRAY = Arrays.asList(generateProperties.getKeywordArray().split(","));
//        TABLE_PREFIX_DEFAULT = generateProperties.getTablePrefixDefault();
//        FIELD_PREFIX_DEFAULT = generateProperties.getFieldPrefixDefault();
//        TABLE_PREFIX = TABLE_PREFIX_DEFAULT;
//        FIELD_PREFIX = TABLE_PREFIX;
//        FATHER_PATH = generateProperties.fatherPath;
//        // 代码生成位置
////        BASE_PATH_HTML_TXT_YL = "File/" + PROJECT_NAME + "/code/src/main/resources/templates/" + ROOT_MODULE + "/" + MODULE_NAME + "/txt/";
////        BASE_PATH_VUE_TXT_YL = "File/" + PROJECT_NAME + "/code/src/main/resources/templates/" + ROOT_MODULE + "/" + MODULE_NAME + "/txtVue/";
////        BASE_PATH_JAVA_YL = "File/" + PROJECT_NAME + "/code/src/main/java/" + (PACK_PATH + "." + ROOT_MODULE).replace(".", "/") + "/" + MODULE_NAME + "/";
////        BASE_PATH_XML_YL = "File/" + PROJECT_NAME + "/code/src/main/resources/mapper/" + ROOT_MODULE + "/" + MODULE_NAME + "/";
////        if (PROJECT_NAME == null || "".equals(PROJECT_NAME)) {
////            BASE_PATH_VUE = PROJECT_NAME + "src/main/resources/templates/" + ROOT_MODULE + "/" + MODULE_NAME + "/";
////            BASE_PATH_HTML = PROJECT_NAME + "src/main/resources/templates/" + ROOT_MODULE + "/" + MODULE_NAME + "/";
////            BASE_PATH_JAVA = PROJECT_NAME + "src/main/java/" + (PACK_PATH + "." + ROOT_MODULE).replace(".", "/") + "/" + MODULE_NAME.replace(".", "/") + "/";
////            BASE_PATH_XML = PROJECT_NAME + "src/main/resources/mapper/" + MODULE_NAME + "/";
////        } else {
////            BASE_PATH_VUE = PROJECT_NAME + "/src/main/resources/templates/" + ROOT_MODULE + "/" + MODULE_NAME + "/";
////            BASE_PATH_HTML = PROJECT_NAME + "/src/main/resources/templates/" + ROOT_MODULE + "/" + MODULE_NAME + "/";
////            BASE_PATH_JAVA = PROJECT_NAME + "/src/main/java/" + (PACK_PATH + "." + ROOT_MODULE).replace(".", "/") + "/" + MODULE_NAME.replace(".", "/") + "/";
////            BASE_PATH_XML = PROJECT_NAME + "/src/main/resources/mapper/" + MODULE_NAME + "/";
////        }
//        log.info("===============代码生成配置已加载=================");
//    }
//
//
////    //=======================================================================================================
////    //=======================================================================================================
////    //======================================= 下方为固定参数  =================================================
////    //=======================================================================================================
////    //=======================================================================================================
////    /**
////     * 代码生成位置 --> 子路径
////     */
////    public final static String PATH_ENTITY = "model/entity/";
////    public final static String PATH_VO = "model/vo/";
////    public final static String PATH_DTO = "model/dto/";
////    public final static String PATH_Query = "model/query/";
////    public final static String PATH_CONTROLLER = "controller/";
////    public final static String PATH_SERVICE = "service/";
////    public final static String PATH_SERVICE_IMPL = "service/impl/";
////    public final static String PATH_MAPPER = "mapper/";
////    // 这里 xml 会过滤 Xml,主要用于区分
////    public final static String PATH_MAPPER_XML = "mapperXml/";
////    /**
////     * 此处内容一般都是不会修改的，特殊情况除外
////     */
////    public final static String PATH_TEMPLATE = "/template";   // 代码模版读取位置（目录 resources/template下）
////    public static String SUFFIX_JAVA = ".java";               // 实际 java 后缀名
////    public static String SUFFIX_XML = ".xml";                 // 实际 xml 后缀名
////    public static String SUFFIX_HTML = ".html";               // 实际 html 后缀名
////    public static String SUFFIX_VUE = ".vue";                 // 实际 vue 后缀名
////    //
////    public static String SUFFIX_TXT = ".txt";                 // 预览文件后缀名
////    //
////    public static String SUFFIX_JAVA_PT = ".txt";             // 最后生成html 后缀名
////    public static String SUFFIX_HTML_PT = ".txt";             // 最后生成的html 后缀名
////    public static String SUFFIX_XML_PT = ".txt";              // 最后生成的html 后缀名
////    public static String SUFFIX_VUE_PT = ".txt";              // 最后生成的vue 后缀名
//
//
//    //=======================================================================================================
//    //=======================================================================================================
//    //================================ 下方为代码生成过程中动态生成的参数  ========================================
//    //=======================================================================================================
//    //=======================================================================================================
//    // 基础参数 (代码生成过程中第一步调用DsField方法时 获得)
//    public static String PATH_TP;             // 代码模板路径(从项目名开始,除该字段，其余所有字段为模块页面替换值)
//    public static String TABLE_NAME;          //  数据库表的实际名称
//    public static String TABLE_COMMENT;       //  数据表的注释
//    // public static String PACK_PATH_GC;     //  生成代码的包名/路径（从java 目录开始，如当前: io.github.wslxm.modules）
//    public static String TABLE_NAME_UP;       //  表名驼峰大写开头 --> java 文件名+类名
//    public static String TABLE_NAME_LOWER;    //  表名驼峰小写开头 --> ( html 文件名 ||  java 对象属性名-User user = new User() 的user)
//
//    // java参数 (代码生成过程中获得)
//    public static String FIELD_ENTITYS = "";             // entity 实体类所有字段数据
//    //public static String FIND_PAGE_PARAM = "";           // controller， findPage方法参数列表
//    //public static String SWAGGER_REMARK = "";            // controller， findPage方法查询参数，的swagger注释
//    public static String FIND_PAGE_MYBATIS_PLUS = "";    // 列表查询方法查询参数,拼接到mybatisPlus方法中参数列表
//
//    // xml 参数(代码生成过程中获得)
//    public static String RESULT_MAP = "";                // mapper xml 字段与实体类映射
//    public static String COLUMN_LIST = "";               // mapper xml 通用字段返回
//    public static String XML_INSERT = "";                // mapper xml 增加sql
//    public static String XML_UPD = "";                   // mapper xml 编辑sql
//
//    // html参数(代码生成过程中获得)
//    // public static String PRIMARY_KEY_TYPE = "";        // id主键数据类型
//    public static String LAYUI_FIELDS = "";               // 生成html主页，layui数据表格所有字段数据
//    public static String LAYUI_SEARCH_PT_STR = "";        // 生成html主页，搜索条件拼接
//    public static String LAYUI_SEARCH_PARAMS_STR = "";    // 生成html主页，搜索条件url参数
//    public static String LAYUI_SEARCH_JS_STR = "";        // 生成html主页，搜索条件js
//    public static String ADD_UPD_INTRODUCE = "";          // 生成html 添加页/编辑页，css/js引入
//    public static String ADD_UPD_HTMLS = "";              // 生成html 添加页/编辑页，表单所有字段代码生成
//    public static String ADD_UPD_JS = "";                 // 生成html 添加/编辑页，js代码生成
//    public static String ADD_UPD_SUBMIT_JS = "";          // 生成html 添加/编辑页，多选js 提交参数处理代码生成
//
//    // vue 代码
//    public static String VUE_INFO_COLUMNS = "";             // 生成vue表格页字段
//    public static String VUE_ADD_COLUMNS_DEFAULT = "";      // vue添加页字段默认值
//    public static String VUE_ADD_COLUMNS = "";              // vue添加页字段
//    public static String VUE_UPD_COLUMNS = "";              // vue编辑页字段
//
//
//    //public static String LAYUI_SEARCH_PT_STR = "";        // 生成html主页，搜索条件拼接
//    //public static String LAYUI_SEARCH_PARAMS_STR = "";    // 生成html主页，搜索条件url参数
//
//
//    /**
//     * 1、tableName;         // 数据库表的实际名称
//     * 2、tableComment;      // 数据库表的注释
//     * 3、packName;          // 生成代码的包名/路径 （从java 目录开始，如当前: io.github.wslxm.baseadmin）
//     * 4、pathTp;            // 代码模板路径
//     */
//    public static void dsField(String tableName, String tableComment, String packPath, String pathTp) {
//        PATH_TP = pathTp;
//        TABLE_NAME = tableName;
//        TABLE_COMMENT = tableComment;
//        // PACK_PATH = packPath;
//        // 如果需要，去除表前缀
//        if (StringUtils.isNotBlank(GenerateConfig.TABLE_PREFIX)) {
//            // 获取前缀
//            String prefix = tableName.substring(0, GenerateConfig.TABLE_PREFIX.length());
//            String newTableName = tableName;
//            // 去除表前缀(如果为?_  去除 ?_)
//            if (GenerateConfig.TABLE_PREFIX.equals(prefix)) {
//                //去掉数据库表的前缀，如 t_
//                newTableName = tableName.substring(GenerateConfig.TABLE_PREFIX.length());
//            }
//            // test_data --> TestData
//            TABLE_NAME_UP = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, newTableName);
//            // test_data --> testData
//            TABLE_NAME_LOWER = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, newTableName);
//        } else {
//            // test_data --> TestData
//            TABLE_NAME_UP = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
//            // test_data --> testData
//            TABLE_NAME_LOWER = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName);
//        }
//
//
//        String packFilePath = GenerateConfig.PACK_PATH.replaceAll("\\.", "\\/");
//        // 处理数据
//        TemplateParamsReplace.PARAM_REPLAC.put("{projectName}", GenerateConfig.PROJECT_NAME);
//        TemplateParamsReplace.PARAM_REPLAC.put("{packPath}", GenerateConfig.PACK_PATH);
//        TemplateParamsReplace.PARAM_REPLAC.put("{packFilePath}",packFilePath);
//        TemplateParamsReplace.PARAM_REPLAC.put("{rootModule}", GenerateConfig.ROOT_MODULE);
//        TemplateParamsReplace.PARAM_REPLAC.put("{moduleName}", GenerateConfig.MODULE_NAME);
//        TemplateParamsReplace.PARAM_REPLAC.put("{tableNameUp}", GenerateConfig.TABLE_NAME_UP);
//        TemplateParamsReplace.PARAM_REPLAC.put("{tableName}", GenerateConfig.TABLE_NAME);
//        TemplateParamsReplace.PARAM_REPLAC.put("{tableNameLower}", GenerateConfig.TABLE_NAME_LOWER);
//    }
//
//
//    // TEST
//    public static void main(String[] args) {
//        log.error(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "t_test-data"));            // testData
//        log.error(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "t_test_data"));        // testData
//        log.error(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "t_test_data_test"));   // TestData
//        log.error(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "t_test_data"));        // TestData
//        log.error(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "testdata"));           // testdata
//        log.error(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "TestData"));           // test_data
//        log.error(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "testData"));               // test-data
//    }
//}

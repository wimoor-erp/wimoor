package com.wimoor.sys.gc.model.vo;

import com.wimoor.sys.gc.model.core.BaseVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 代码生成数据源维护表
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "DatasourceVO 对象", description = "代码生成数据源维护表")
public class DatasourceVO extends BaseVo {

    private static final long serialVersionUID = 0L;

    @ApiModelProperty(value = "db -标题", position = 0)
    private String dbTitle;

    @ApiModelProperty(value = "db 库名", position = 1)
    private String dbName;

    @ApiModelProperty(value = "db 连接地址", position = 2)
    private String dbUrl;

    @ApiModelProperty(value = "db 账号", position = 3)
    private String dbUsername;

    @ApiModelProperty(value = "db 密码", position = 4)
    private String dbPassword;

    @ApiModelProperty(value = "作者", position = 5)
    private String author;

    @ApiModelProperty(value = "邮箱", position = 6)
    private String email;

    @ApiModelProperty(value = "描述信息", position = 7)
    private String describe;

    @ApiModelProperty(value = "项目名/路径，如：xj-server/xj-test-server", position = 8)
    private String projectName;

    @ApiModelProperty(value = "包路径 (如: io.github.wslxm)", position = 9)
    private String packPath;

    @ApiModelProperty(value = "根模块 (固定为：modules(管理端), 用户端为：client)", position = 10)
    private String rootModule;

    @ApiModelProperty(value = "子模块 (业务分类,如用户管理,订单管理模块拆分，也可以统一一个名称放在一起)", position = 11)
    private String modulesName;

    @ApiModelProperty(value = "db 表前缀 (生成的类名会过滤掉前缀)", position = 13)
    private String dbTablePrefix;

    @ApiModelProperty(value = "db 字段前缀 (生成的字段名会过滤掉前缀)", position = 14)
    private String dbFieldPrefix;

    @ApiModelProperty(value = "实体类是否使用swagger注释 (false情况下使用doc注释)", position = 15)
    private Boolean entitySwagger;

    @ApiModelProperty(value = "是否过滤crud方法- 默认生成 (controller/service/mapper/xml)", position = 15)
    private Boolean filterCrud;

    @ApiModelProperty(value = "生成路径(不填默认当前项目跟目录,可指定绝对路径)", position = 16)
    private String fatherPath;

    @ApiModelProperty(value = "排除vue字段类型 (字典code值，参考字典生成字段类型，如: 18=富文本 19=md编辑器 )", position = 17)
    private String vueFieldTypes;

    @ApiModelProperty(value = "数据库通用字段", position = 18)
    private String baseFields;

    @ApiModelProperty(value = "数据库关键字", position = 19)
    private String keywordArray;
}


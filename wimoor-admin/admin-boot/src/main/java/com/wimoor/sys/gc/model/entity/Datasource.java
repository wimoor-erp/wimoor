package com.wimoor.sys.gc.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 代码生成数据源维护表
 * <p>
 *  ::本代码由[兮家小二]提供的代码生成器生成,如有问题,请手动修改 ::作者CSDN:https://blog.csdn.net/qq_41463655 
 * </p>
 * @author  wangsong
 */
@Data
@TableName("t_gc_datasource")
@ApiModel(value = "Datasource 对象", description = "代码生成数据源维护表")
public class Datasource  implements Serializable{

    private static final long serialVersionUID = 0L;

    /**
     * 数据库id
     * 雪花算法=IdType.ASSIGN_ID  || 自增=IdType.AUTO
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 创建人(ID)
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;
    /**
     * 创建时间(数据库策略--添加数据自动插入时间)
     */
    private LocalDateTime createTime;
    /**
     * 更新人(ID)
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;
    /**
     * 更新时间(数据库策略--修改数据自动更新时间)
     */
    private LocalDateTime updateTime;
    /**
     * 逻辑删除字段 int 默认值为0，(0、正常，1、删除) (mybatis-plus 策略, 添加了 @TableLogic 注解自动为逻辑删除)
     */
    @TableLogic
    private Integer deleted;
    /**
     * 乐观锁(mybatis-plus 策略, 使用id修改自动带版本号修改数据，version版本号如不同修改数据失败返回 0)
     */
    @Version
    private Integer version;
    /**
     * db -标题
     */
    @TableField(value = "db_title")
    private String dbTitle;

    /**
     * db 库名
     */
    @TableField(value = "db_name")
    private String dbName;

    /**
     * db 连接地址
     */
    @TableField(value = "db_url")
    private String dbUrl;

    /**
     * db 账号
     */
    @TableField(value = "db_username")
    private String dbUsername;

    /**
     * db 密码
     */
    @TableField(value = "db_password")
    private String dbPassword;

    /**
     * 作者
     */
    @TableField(value = "author")
    private String author;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 描述信息
     */
    @TableField(value = "`describe`")
    private String describe;

    /**
     * 项目名/路径，如：xj-server/xj-test-server
     */
    @TableField(value = "project_name")
    private String projectName;

    /**
     * 包路径 (如: io.github.wslxm)
     */
    @TableField(value = "pack_path")
    private String packPath;

    /**
     * 根模块 (固定为：modules(管理端), 用户端为：client)
     */
    @TableField(value = "root_module")
    private String rootModule;

    /**
     * 子模块 (业务分类,如用户管理,订单管理模块拆分，也可以统一一个名称放在一起)
     */
    @TableField(value = "modules_name")
    private String modulesName;


    /**
     * db 表前缀 (生成的类名会过滤掉前缀)
     */
    @TableField(value = "db_table_prefix")
    private String dbTablePrefix;

    /**
     * db 字段前缀 (生成的字段名会过滤掉前缀)
     */
    @TableField(value = "db_field_prefix")
    private String dbFieldPrefix;

    /**
     * 实体类是否使用swagger注释 (false情况下使用doc注释)
     */
    @TableField(value = "entity_swagger")
    private Boolean entitySwagger;

    /**
     * 是否过滤crud方法- 默认生成 (controller/service/mapper/xml)
     */
    @TableField(value = "filter_crud")
    private Boolean filterCrud;

    /**
     * 生成路径(不填默认当前项目跟目录,可指定绝对路径)
     */
    @TableField(value = "father_path")
    private String fatherPath;

    /**
     * 排除vue字段类型 (字典code值，参考字典生成字段类型，如: 18=富文本 19=md编辑器 )
     */
    @TableField(value = "vue_field_types")
    private String vueFieldTypes;

    /**
     * 数据库通用字段
     */
    @TableField(value = "base_fields")
    private String baseFields;

    /**
     * 数据库关键字
     */
    @TableField(value = "keyword_array")
    private String keywordArray;

}


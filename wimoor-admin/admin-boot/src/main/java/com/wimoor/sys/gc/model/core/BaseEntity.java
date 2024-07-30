package com.wimoor.sys.gc.model.core;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 通用Entity
 *
 * @author wangsong
 * @WX-QQ 1720696548
 * @date 2019/10/31 21:12
 *
 * <p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseEntity extends Convert {

    /**
	 * 
	 */
	private static final long serialVersionUID = -9223299889026502416L;
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
}

package com.wimoor.sys.tool.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-04-26
 */
@Data
@TableName("t_sys_query_user_version")
@ApiModel(value="SysQueryUserVersion对象", description="")
public class SysQueryUserVersion implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private BigInteger id;

    private BigInteger userid;

    private String fquery;

    private Boolean isused;

    private String name;

    private Date opttime;

    private Date createtime;

    @TableField(exist = false)
    private String queryfield;
}

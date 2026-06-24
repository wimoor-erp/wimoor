package com.wimoor.sys.tool.pojo.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_template_file")
public class SysTemplateFile extends BaseEntity{
    /**
     *
     */
    private static final long serialVersionUID = -8041499846202535828L;

    @TableField(value="shopid")
    private BigInteger shopid;

    @TableField(value="userid")
    private BigInteger userid;

    @TableField(value="ftype")
    private String ftype;

    @TableField(value="filename")
    private String filename;

    @TableField(value="filepath")
    private String filepath;

    @TableField(value="creator")
    private BigInteger creator;

    @TableField(value="operator")
    private BigInteger operator;

    @TableField(value="opttime")
    private Date opttime;

    @TableField(value="createdate")
    private Date createdate;

}
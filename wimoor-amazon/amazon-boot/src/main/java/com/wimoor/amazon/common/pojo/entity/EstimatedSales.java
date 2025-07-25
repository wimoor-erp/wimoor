package com.wimoor.amazon.common.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
 

@Data
@TableName("t_erp_estimated_sales")
public class EstimatedSales {
     
    @TableId(type=IdType.AUTO)
    private String id;

    @TableField(value= "sku")
    private String sku;

    @TableField(value= "marketplaceid")
    private String marketplaceid;

    @TableField(value= "groupid")
    private String groupid;

    @TableField(value= "presales")
    private Integer presales;

    @TableField(value= "startTime")
    private Date starttime;

    @TableField(value= "endTime")
    private Date endtime;

    @TableField(value= "conditions")
    private int conditions;

    @TableField(value= "conditionNum")
    private BigDecimal conditionnum;
    
    @TableField(value= "isInvalid")
    private Boolean isInvalid;

    @TableField(value= "operator")
    private String operator;

    @TableField(value= "opttime")
    private Date opttime;
 
}
package com.wimoor.erp.ship.pojo.entity;

import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_ship_inboundtrans_form")
public class ShipTransCompayForm {
	
    @TableId(value= "shipmentid")
    private String shipmentid;

    @TableField(value= "formid")
    private String formid;
    
    @TableField(value= "shopid")
    private BigInteger shopid;

    @TableField(value= "content")
    private String content;

    @TableField(value= "status")
    private Integer status;
    
    @TableField(value= "result")
    private String result;

    @TableField(value= "refreshtime")
    private Date refreshtime;

    @TableField(value= "creattime")
    private Date creattime;

    @TableField(value= "operator")
    private BigInteger operator;
 
    
}
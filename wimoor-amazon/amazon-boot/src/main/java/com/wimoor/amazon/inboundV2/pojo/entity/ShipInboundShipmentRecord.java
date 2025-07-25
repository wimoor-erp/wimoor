package com.wimoor.amazon.inboundV2.pojo.entity;

import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_ship_v2_inbound_record")
public class ShipInboundShipmentRecord {
    @TableField(value="formid")
    private String formid;
    
    @TableField(value="shipmentid")
    private String shipmentid;
    
    @TableField(value="status")
    private Integer status;

    @TableField(value="opttime")
    private Date opttime;

    @TableField(value="operator")
    private BigInteger operator;
 
}
package com.wimoor.amazon.inbound.pojo.entity;

import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_ship_inboundshipment_record")
public class ShipInboundShipmentRecord {
	@TableId(value="id",type = IdType.AUTO)
    private Integer id;

    @TableField(value="shipmentid")
    private String shipmentid;

    @TableField(value="status")
    private Integer status;

    @TableField(value="opttime")
    private Date opttime;

    @TableField(value="operator")
    private BigInteger operator;
 
}
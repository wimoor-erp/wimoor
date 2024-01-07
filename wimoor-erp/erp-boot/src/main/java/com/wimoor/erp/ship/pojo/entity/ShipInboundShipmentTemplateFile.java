package com.wimoor.erp.ship.pojo.entity;

import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_ship_shipment_template_file")
public class ShipInboundShipmentTemplateFile extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8041499846202535828L;

	@TableField(value="shopid")
    private BigInteger shopid;

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
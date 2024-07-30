package com.wimoor.amazon.inbound.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.AmazonBaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipInboundTrans对象", description="物流信息")
@TableName("t_erp_ship_inboundtrans")
public class ShipInboundTrans extends AmazonBaseEntity{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6318016743665149289L;

	@TableField(value= "shipmentid")
    private String shipmentid;

    @TableField(insertStrategy = FieldStrategy.IGNORED,updateStrategy = FieldStrategy.IGNORED,value= "company")
    
    private String company;

    @TableField(value= "channel")
    private String channel;

    @TableField(value= "singleprice")
    private BigDecimal singleprice;

    @TableField(value= "transweight")
    private BigDecimal transweight;

    @Size(max=10,message="重量单位不能超过10个字符")
    @TableField(value= "wunit")
    private String wunit;

 
    @TableField(value= "wtype")
    private Integer wtype;
    
    @TableField(value= "transtype")
    private BigInteger transtype;
    
    @TableField(value= "otherfee")
    private BigDecimal otherfee;
    
    @Size(max=50,message="跟踪号不能超过50个字符")
    @TableField(value="ordernum")
    private String ordernum;

	@TableField(value="arrivalTime")
	private Date arrivalTime;
	
	@TableField(value="outarrtime")
	private Date outarrtime;
	
	@TableField(value="inarrtime")
	private Date inarrtime;
	
	@Size(max=500,message="备注不能超过500个字符")
	@TableField(value="remark")
	private String remark;
	
	 
	

}
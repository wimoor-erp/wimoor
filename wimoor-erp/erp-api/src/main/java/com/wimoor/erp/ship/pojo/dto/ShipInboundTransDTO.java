package com.wimoor.erp.ship.pojo.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BizBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipInboundTrans对象", description="物流信息")
@TableName("t_erp_ship_inboundtrans")
public class ShipInboundTransDTO extends BizBaseEntity{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6318016743665149289L;
	@ApiModelProperty(value = "货件ID【系统填写】")
	@TableField(value= "shipmentid")
    private String shipmentid;

	@ApiModelProperty(value = "物流公司ID")
    @TableField(value= "company")
    private String company;
	
	@ApiModelProperty(value = "物流公司名称")
	@TableField(exist=false)
	private String companyname;

	@ApiModelProperty(value = "物流渠道ID")
    @TableField(value= "channel")
    private String channel;
	
	@ApiModelProperty(value = "物流渠道名称")
	@TableField(exist=false)
	private String channelname;
	 
	@ApiModelProperty(value = "单价")
    @TableField(value= "singleprice")
    private BigDecimal singleprice;

	@ApiModelProperty(value = "重量")
    @TableField(value= "transweight")
    private BigDecimal transweight;

	@ApiModelProperty(value = "重量单位")
    @Size(max=10,message="重量单位不能超过10个字符")
    @TableField(value= "wunit")
    private String wunit;

	@ApiModelProperty(value = "重量单位")
    @TableField(value= "wtype")
    private Integer wtype;
    
	@ApiModelProperty(value = "发货类型")
    @TableField(value= "transtype")
    private BigInteger transtype;
    
	@ApiModelProperty(value = "其他费用")
    @TableField(value= "otherfee")
    private BigDecimal otherfee;
    
    @Size(max=50,message="跟踪号不能超过50个字符")
    @TableField(value="ordernum")
    private String ordernum;

    @ApiModelProperty(value = "预计到达时间")
	@TableField(value="arrivalTime")
	private Date arrivalTime;
	
    @ApiModelProperty(value = "出港时间")
	@TableField(value="outarrtime")
	private Date outarrtime;
	
    @ApiModelProperty(value = "到港时间")
	@TableField(value="inarrtime")
	private Date inarrtime;
	
    @ApiModelProperty(value = "备注")
	@Size(max=500,message="备注不能超过500个字符")
	@TableField(value="remark")
	private String remark;
	
	 
	

}
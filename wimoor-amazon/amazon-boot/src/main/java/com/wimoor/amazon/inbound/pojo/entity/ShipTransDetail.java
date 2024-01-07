package com.wimoor.amazon.inbound.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_ship_transdetail")
public class ShipTransDetail extends  BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7615789355969927001L;

	@TableField(value="company")
    private String company;

	@TableField(value="channel")
    private String channel;
	
	
	@TableField(value="marketplaceid")
    private String marketplaceid;
	
	@NotNull(message="渠道名称不能为空")
	@Size(max=36,message="渠道名称不能超过36个字符")
	@TableField(value="channame")
	private String channame;

	@TableField(value="pretime")
    private Integer pretime;

	@TableField(value="price")
    private BigDecimal price;
 
	@TableField(value="drate")
    private Integer drate;
	
	@TableField(value="cbmrate")
    private Integer cbmrate;
	
	@TableField(value="priceunits")
    private String priceunits;
	
	@TableField(value="transtype")
    private String transtype;
	
    @TableField(value= "subarea")
    private String subarea;
    
    @TableField(value= "remark")
    private String remark;
	
    @TableField(value= "disabled")
    private Boolean disabled;
    
    @TableField(value= "opttime")
    private Date opttime;
    
    @TableField(value= "operator")
    private String operator;
	
     
	
}
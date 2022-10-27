package com.wimoor.erp.ship.pojo.entity;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_ship_transdetail")
public class ShipTransDetail extends  ErpBaseEntity{
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
	
	
    @TableField(exist = false)
	private String mname;
    
    @TableField(exist = false)
	private String cname;
    
    @TableField(exist = false)
    private String tname;
    
    @TableField(exist = false)
    private String priceu ;
    
    @TableField(exist = false)
    private String  channeltype; 
    
    @TableField(exist = false)
    private String  market; 
    
    @TableField(exist = false)
    private String  optname; 
    
}
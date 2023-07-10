package com.wimoor.amazon.finances.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_fin_user_item_data")
@ApiModel(value="AmzFinUserItemData对象", description="")
public class AmzFinUserItemData  extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4405518606045794435L;

	@TableField(value="itemid")
    private String itemid;

	@TableField(value="groupid")
    private String groupid;

	@TableField(value="marketplaceid")
    private String marketplaceid;

	@TableField(value="byday")
    private Date byday;

	@TableField(value="shopid")
    private String shopid;

	@TableField(value="sku")
    private String sku;

	@TableField(value="currency")
    private String currency;
	
	@TableField(value="amount")
    private BigDecimal amount;
	
	@TableField(value="opttime")
    private Date opttime;
	
	@TableField(value="operator")
    private String operator;
 
    
    
}
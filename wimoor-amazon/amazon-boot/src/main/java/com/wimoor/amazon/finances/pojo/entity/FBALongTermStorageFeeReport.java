package com.wimoor.amazon.finances.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
 

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

 
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_fba_longterm_storage_fee_report")  
@ApiModel(value="FBALongTermStorageFeeReport对象", description="SKU 长期仓储费")
public class FBALongTermStorageFeeReport implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 5106379846409577790L;

	@TableId(value = "id" )
	@ApiModelProperty(value = "ID")
    String id;
	
	@TableField(value="snapshot_date")
    private Date snapshotDate;

	@TableField(value="sku")
    private String sku;

	@TableField(value="fnsku")
    private String fnsku;

	@TableField(value="`condition`")
    private String condition;
	
	@TableField(value="`asin`")
    private String asin;

	@TableField(value="per_unit_volume")
    private BigDecimal perUnitVolume;

	@TableField(value="currency")
    private String currency;
	
	@TableField(value="qty_charged_12month")
	private Integer qtyCharged12month;

	@TableField(value="qty_charged_6month")
    private Integer qtyCharged6month;
	
	@TableField(value="qty_charged")
    private Integer qtyCharged;

	@TableField(value="amount_6")
    private BigDecimal amount6;

	@TableField(value="amount_12")
    private BigDecimal amount12;

	@TableField(value="amount_charged")
    private BigDecimal amountCharged;
	
	@TableField(value="rate_surcharge")
    private BigDecimal rateSurcharge;
		 	
	@TableField(value="surcharge_age_tier")
    private String surchargeAgeTier;
	
	@TableField(value="volume_unit")
    private BigDecimal volumeUnit;

	@TableField(value="country")
    private String country;

	@TableField(value="is_sl")
    private Boolean isSl;

	@TableField(value="amazonauthid")
    private String amazonauthid;

	@TableField(value="lastupdate")
    private Date lastupdate;
 
}
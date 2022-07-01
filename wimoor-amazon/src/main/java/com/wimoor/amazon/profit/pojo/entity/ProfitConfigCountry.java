package com.wimoor.amazon.profit.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_profitcfgcountry")  
@ApiModel(value="ProfitConfigCountry对象", description="配置国家")
public class ProfitConfigCountry extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	@TableField(exist=false)
	private static final long serialVersionUID = -3285371153886204280L;

	@TableField(value= "profitid")
	private String profitid;

	@TableField(value= "country")
    private String country;

	@TableField(value= "constantw")
    private BigDecimal constantw;

	@TableField(value= "constantd")
    private BigDecimal constantd;

	@TableField(value= "constantm")
    private BigDecimal constantm;
    
	@TableField(value= "fba_month")
    private Integer fbaMonth; 

	@TableField(value= "storagefee")
    private BigDecimal storagefee;

	@TableField(value= "taxRate")
    private BigDecimal taxrate;

	@TableField(value= "lostRate")
    private BigDecimal lostrate;

	@TableField(value= "sellerRate")
    private BigDecimal sellerrate;

	@TableField(value= "otherfee")
    private BigDecimal otherfee;

	@TableField(value= "costRate")
    private BigDecimal costrate;

	@TableField(value= "logistics")
    private String logistics;

	@TableField(value= "subscriptionfee")
    private BigDecimal subscriptionfee;

	@TableField(value= "prepservice")
    private Boolean prepservice;
	
	@TableField(value= "labelService")
    private Boolean labelService;

	@TableField(value= "manualProcessing")
    private BigDecimal manualProcessing;
	
	@TableField(value= "unprepservice")
    private BigDecimal unprepservice;

	@TableField(value= "invplacefee")
    private String invplacefee;

	@TableField(value= "promotion")
    private BigDecimal promotion;

	@TableField(value= "amonth")
    private Integer amonth; 
	
	@TableField(value= "dispatch_type")
    private String fenpeiType;
	
	@TableField(value= "warehouse_site")
    private String warehousesite;
	
	@TableField(value= "hasAddedSite")
    private boolean hasAddedSite;
	
	@TableField(value= "vat_rate")
    private BigDecimal vatRate; 
	
	@TableField(value= "fba_taxes")
    private BigDecimal fbaTaxes; //亚马逊收费GST 税率
	
	@TableField(value= "hasDeclaredValue")
    private boolean hasDeclaredValue; 
	
	@TableField(value= "declared_value")
    private BigDecimal declaredValue; 
    
	@TableField(value= "gst_rate")
	private BigDecimal gstrate; //进口GST税率
	
	@TableField(value= "selling_GSTRate")
	private BigDecimal sellingGSTRate; //销售GST税率
	
	@TableField(value= "corporate_InRate")
	private BigDecimal corporateInRate; //企业所得税率
	
	public BigDecimal getAmonthBigDecimal() {
		if (amonth == null) {
			return null;
		}
		return new BigDecimal(amonth.toString());
	} 

}
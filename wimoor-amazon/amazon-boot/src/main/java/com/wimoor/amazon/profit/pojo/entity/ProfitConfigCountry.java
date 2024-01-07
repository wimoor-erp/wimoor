package com.wimoor.amazon.profit.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
 

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
	
	@TableField(exist=false)
	private BigDecimal amonthBigDecimal; //企业所得税率
	
	public BigDecimal getAmonthBigDecimal() {
		if (amonth == null) {
			return null;
		}
		return new BigDecimal(amonth.toString());
	}

	public String getProfitid() {
		return profitid;
	}

	public void setProfitid(String profitid) {
		this.profitid = profitid;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public BigDecimal getConstantw() {
		return constantw==null?new BigDecimal("0"):constantw;
	}

	public void setConstantw(BigDecimal constantw) {
		this.constantw = constantw;
	}

	public BigDecimal getConstantd() {
		return constantd==null?new BigDecimal("0"):constantd;
	}

	public void setConstantd(BigDecimal constantd) {
		this.constantd = constantd;
	}

	public BigDecimal getConstantm() {
		return constantm==null?new BigDecimal("0"):constantm;
	}

	public void setConstantm(BigDecimal constantm) {
		this.constantm = constantm;
	}

	public Integer getFbaMonth() {
		return fbaMonth;
	}

	public void setFbaMonth(Integer fbaMonth) {
		this.fbaMonth = fbaMonth;
	}

	public BigDecimal getStoragefee() {
		return storagefee==null?new BigDecimal("0"):storagefee;
	}

	public void setStoragefee(BigDecimal storagefee) {
		this.storagefee = storagefee;
	}

	public BigDecimal getTaxrate() {
		return taxrate==null?new BigDecimal("0"):taxrate;
	}

	public void setTaxrate(BigDecimal taxrate) {
		this.taxrate = taxrate;
	}

	public BigDecimal getLostrate() {
		return lostrate==null?new BigDecimal("0"):lostrate;
	}

	public void setLostrate(BigDecimal lostrate) {
		this.lostrate = lostrate;
	}

	public BigDecimal getSellerrate() {
		 return sellerrate==null?new BigDecimal("0"):sellerrate;
	}

	public void setSellerrate(BigDecimal sellerrate) {
		this.sellerrate = sellerrate;
	}

	public BigDecimal getOtherfee() {
		 return otherfee==null?new BigDecimal("0"):otherfee;
	}

	public void setOtherfee(BigDecimal otherfee) {
		this.otherfee = otherfee;
	}

	public BigDecimal getCostrate() {
		 return costrate==null?new BigDecimal("0"):costrate;
	}

	public void setCostrate(BigDecimal costrate) {
		this.costrate = costrate;
	}

	public String getLogistics() {
		return logistics;
	}

	public void setLogistics(String logistics) {
		this.logistics = logistics;
	}

	public BigDecimal getSubscriptionfee() {
		 return subscriptionfee==null?new BigDecimal("0"):subscriptionfee;
	}

	public void setSubscriptionfee(BigDecimal subscriptionfee) {
		this.subscriptionfee = subscriptionfee;
	}

	public Boolean getPrepservice() {
		return prepservice;
	}

	public void setPrepservice(Boolean prepservice) {
		this.prepservice = prepservice;
	}

	public Boolean getLabelService() {
		return labelService;
	}

	public void setLabelService(Boolean labelService) {
		this.labelService = labelService;
	}

	public BigDecimal getManualProcessing() {
		 return manualProcessing==null?new BigDecimal("0"):manualProcessing;
	}

	public void setManualProcessing(BigDecimal manualProcessing) {
		this.manualProcessing = manualProcessing;
	}

	public BigDecimal getUnprepservice() {
		 return unprepservice==null?new BigDecimal("0"):unprepservice;
	}

	public void setUnprepservice(BigDecimal unprepservice) {
		this.unprepservice = unprepservice;
	}

	public String getInvplacefee() {
		return invplacefee;
	}

	public void setInvplacefee(String invplacefee) {
		this.invplacefee = invplacefee;
	}

	public BigDecimal getPromotion() {
		 return promotion==null?new BigDecimal("0"):promotion;
	}

	public void setPromotion(BigDecimal promotion) {
		this.promotion = promotion;
	}

	public Integer getAmonth() {
		return amonth;
	}

	public void setAmonth(Integer amonth) {
		this.amonth = amonth;
	}

	public String getFenpeiType() {
		return fenpeiType;
	}

	public void setFenpeiType(String fenpeiType) {
		this.fenpeiType = fenpeiType;
	}

	public String getWarehousesite() {
		return warehousesite;
	}

	public void setWarehousesite(String warehousesite) {
		this.warehousesite = warehousesite;
	}

	public boolean isHasAddedSite() {
		return hasAddedSite;
	}

	public void setHasAddedSite(boolean hasAddedSite) {
		this.hasAddedSite = hasAddedSite;
	}

	public BigDecimal getVatRate() {
		 return vatRate==null?new BigDecimal("0"):vatRate;
	}

	public void setVatRate(BigDecimal vatRate) {
		this.vatRate = vatRate;
	}

	public BigDecimal getFbaTaxes() {
		 return fbaTaxes==null?new BigDecimal("0"):fbaTaxes;
	}

	public void setFbaTaxes(BigDecimal fbaTaxes) {
		this.fbaTaxes = fbaTaxes;
	}

	public boolean isHasDeclaredValue() {
		return hasDeclaredValue;
	}

	public void setHasDeclaredValue(boolean hasDeclaredValue) {
		this.hasDeclaredValue = hasDeclaredValue;
	}

	public BigDecimal getDeclaredValue() {
		  return declaredValue==null?new BigDecimal("0"):declaredValue;
	}

	public void setDeclaredValue(BigDecimal declaredValue) {
		this.declaredValue = declaredValue;
	}

	public BigDecimal getGstrate() {
		  return gstrate==null?new BigDecimal("0"):gstrate;
	}

	public void setGstrate(BigDecimal gstrate) {
		this.gstrate = gstrate;
	}

	public BigDecimal getSellingGSTRate() {
		  return sellingGSTRate==null?new BigDecimal("0"):sellingGSTRate;
	}

	public void setSellingGSTRate(BigDecimal sellingGSTRate) {
		this.sellingGSTRate = sellingGSTRate;
	}

	public BigDecimal getCorporateInRate() {
	    return corporateInRate==null?new BigDecimal("0"):corporateInRate;
	}

	public void setCorporateInRate(BigDecimal corporateInRate) {
		this.corporateInRate = corporateInRate;
	}

	public void setAmonthBigDecimal(BigDecimal amonthBigDecimal) {
		this.amonthBigDecimal = amonthBigDecimal;
	} 

}
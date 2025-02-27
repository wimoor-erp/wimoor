package com.wimoor.amazon.adv.sp.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wimoor.amazon.adv.common.pojo.JsonBigIntergeSerializer;
 

@Entity
@Table(name="t_amz_adv_rpt2_sp_asins")
public class AmzAdvReportAsins {

	@Column(name="campaignId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger campaignid;

	@Column(name="adGroupId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adgroupid;

	@Id
	@Column(name="keywordId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger keywordid;
	
	@Id
	@Column(name="bydate")
    private Date bydate;
	
	@Column(name="sku")
    private String sku;

	@Id
	@Column(name="advertisedAsin")
    private String advertisedAsin;
	
	@Id
	@Column(name="purchasedAsin")
    private String purchasedAsin;
	
	@Column(name="opttime")
    private Date opttime;
	
	@Column(name="profileid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger profileid;
	
	@Column(name="attributedConversions1d")
    private Integer attributedconversions1d;

	@Column(name="attributedConversions7d")
    private Integer attributedconversions7d;

	@Column(name="attributedConversions14d")
    private Integer attributedconversions14d;

	@Column(name="attributedConversions30d")
    private Integer attributedconversions30d;

	@Column(name="attributedUnitsOrdered1d")
    private Integer attributedunitsordered1d;

	@Column(name="attributedUnitsOrdered7d")
    private Integer attributedunitsordered7d;

	@Column(name="attributedUnitsOrdered14d")
    private Integer attributedunitsordered14d;

	@Column(name="attributedUnitsOrdered30d")
    private Integer attributedunitsordered30d;

	@Column(name="attributedSales1d")
    private BigDecimal attributedsales1d;

	@Column(name="attributedSales7d")
    private BigDecimal attributedsales7d;

	@Column(name="attributedSales14d")
    private BigDecimal attributedsales14d;

	@Column(name="attributedSales30d")
    private BigDecimal attributedsales30d;


	@Column(name="attributedConversions1dSameSKU")
    private Integer attributedconversions1dsamesku;

	@Column(name="attributedConversions7dSameSKU")
    private Integer attributedconversions7dsamesku;

	@Column(name="attributedConversions14dSameSKU")
    private Integer attributedconversions14dsamesku;

	@Column(name="attributedConversions30dSameSKU")
    private Integer attributedconversions30dsamesku;

 
 

	@Column(name="attributedSales1dSameSKU")
    private BigDecimal attributedsales1dsamesku;

	@Column(name="attributedSales7dSameSKU")
    private BigDecimal attributedsales7dsamesku;

	@Column(name="attributedSales14dSameSKU")
    private BigDecimal attributedsales14dsamesku;

	@Column(name="attributedSales30dSameSKU")
    private BigDecimal attributedsales30dsamesku;
	

	@Column(name="attributedUnitsOrdered1dSameSKU")
    private Integer attributedUnitsOrdered1dSameSKU;

	@Column(name="attributedUnitsOrdered7dSameSKU")
    private Integer attributedUnitsOrdered7dSameSKU;

	@Column(name="attributedUnitsOrdered14dSameSKU")
    private Integer attributedUnitsOrdered14dSameSKU;

	@Column(name="attributedUnitsOrdered30dSameSKU")
    private Integer attributedUnitsOrdered30dSameSKU;
	

	public BigInteger getCampaignid() {
		return campaignid;
	}


	public void setCampaignid(BigInteger campaignid) {
		this.campaignid = campaignid;
	}


	public BigInteger getAdgroupid() {
		return adgroupid;
	}


	public void setAdgroupid(BigInteger adgroupid) {
		this.adgroupid = adgroupid;
	}


	public BigInteger getKeywordid() {
		return keywordid;
	}


	public void setKeywordid(BigInteger keywordid) {
		this.keywordid = keywordid;
	}


	public Date getBydate() {
		return bydate;
	}


	public void setBydate(Date bydate) {
		this.bydate = bydate;
	}


	public String getSku() {
		return sku;
	}


	public void setSku(String sku) {
		this.sku = sku;
	}


	public String getAdvertisedAsin() {
		return advertisedAsin;
	}


	public void setAdvertisedAsin(String advertisedAsin) {
		this.advertisedAsin = advertisedAsin;
	}


	public String getPurchasedAsin() {
		return purchasedAsin;
	}


	public void setPurchasedAsin(String purchasedAsin) {
		this.purchasedAsin = purchasedAsin;
	}


	public Date getOpttime() {
		return opttime;
	}


	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}


	public BigInteger getProfileid() {
		return profileid;
	}


	public void setProfileid(BigInteger profileid) {
		this.profileid = profileid;
	}


	public Integer getAttributedconversions1d() {
		return attributedconversions1d;
	}


	public void setAttributedconversions1d(Integer attributedconversions1d) {
		this.attributedconversions1d = attributedconversions1d;
	}


	public Integer getAttributedconversions7d() {
		return attributedconversions7d;
	}


	public void setAttributedconversions7d(Integer attributedconversions7d) {
		this.attributedconversions7d = attributedconversions7d;
	}


	public Integer getAttributedconversions14d() {
		return attributedconversions14d;
	}


	public void setAttributedconversions14d(Integer attributedconversions14d) {
		this.attributedconversions14d = attributedconversions14d;
	}


	public Integer getAttributedconversions30d() {
		return attributedconversions30d;
	}


	public void setAttributedconversions30d(Integer attributedconversions30d) {
		this.attributedconversions30d = attributedconversions30d;
	}


	public Integer getAttributedunitsordered1d() {
		return attributedunitsordered1d;
	}


	public void setAttributedunitsordered1d(Integer attributedunitsordered1d) {
		this.attributedunitsordered1d = attributedunitsordered1d;
	}


	public Integer getAttributedunitsordered7d() {
		return attributedunitsordered7d;
	}


	public void setAttributedunitsordered7d(Integer attributedunitsordered7d) {
		this.attributedunitsordered7d = attributedunitsordered7d;
	}


	public Integer getAttributedunitsordered14d() {
		return attributedunitsordered14d;
	}


	public void setAttributedunitsordered14d(Integer attributedunitsordered14d) {
		this.attributedunitsordered14d = attributedunitsordered14d;
	}


	public Integer getAttributedunitsordered30d() {
		return attributedunitsordered30d;
	}


	public void setAttributedunitsordered30d(Integer attributedunitsordered30d) {
		this.attributedunitsordered30d = attributedunitsordered30d;
	}


	public BigDecimal getAttributedsales1d() {
		return attributedsales1d;
	}


	public void setAttributedsales1d(BigDecimal attributedsales1d) {
		this.attributedsales1d = attributedsales1d;
	}


	public BigDecimal getAttributedsales7d() {
		return attributedsales7d;
	}


	public void setAttributedsales7d(BigDecimal attributedsales7d) {
		this.attributedsales7d = attributedsales7d;
	}


	public BigDecimal getAttributedsales14d() {
		return attributedsales14d;
	}


	public void setAttributedsales14d(BigDecimal attributedsales14d) {
		this.attributedsales14d = attributedsales14d;
	}


	public BigDecimal getAttributedsales30d() {
		return attributedsales30d;
	}


	public void setAttributedsales30d(BigDecimal attributedsales30d) {
		this.attributedsales30d = attributedsales30d;
	}


	public Integer getAttributedconversions1dsamesku() {
		return attributedconversions1dsamesku;
	}


	public void setAttributedconversions1dsamesku(Integer attributedconversions1dsamesku) {
		this.attributedconversions1dsamesku = attributedconversions1dsamesku;
	}


	public Integer getAttributedconversions7dsamesku() {
		return attributedconversions7dsamesku;
	}


	public void setAttributedconversions7dsamesku(Integer attributedconversions7dsamesku) {
		this.attributedconversions7dsamesku = attributedconversions7dsamesku;
	}


	public Integer getAttributedconversions14dsamesku() {
		return attributedconversions14dsamesku;
	}


	public void setAttributedconversions14dsamesku(Integer attributedconversions14dsamesku) {
		this.attributedconversions14dsamesku = attributedconversions14dsamesku;
	}


	public Integer getAttributedconversions30dsamesku() {
		return attributedconversions30dsamesku;
	}


	public void setAttributedconversions30dsamesku(Integer attributedconversions30dsamesku) {
		this.attributedconversions30dsamesku = attributedconversions30dsamesku;
	}


	public BigDecimal getAttributedsales1dsamesku() {
		return attributedsales1dsamesku;
	}


	public void setAttributedsales1dsamesku(BigDecimal attributedsales1dsamesku) {
		this.attributedsales1dsamesku = attributedsales1dsamesku;
	}


	public BigDecimal getAttributedsales7dsamesku() {
		return attributedsales7dsamesku;
	}


	public void setAttributedsales7dsamesku(BigDecimal attributedsales7dsamesku) {
		this.attributedsales7dsamesku = attributedsales7dsamesku;
	}


	public BigDecimal getAttributedsales14dsamesku() {
		return attributedsales14dsamesku;
	}


	public void setAttributedsales14dsamesku(BigDecimal attributedsales14dsamesku) {
		this.attributedsales14dsamesku = attributedsales14dsamesku;
	}


	public BigDecimal getAttributedsales30dsamesku() {
		return attributedsales30dsamesku;
	}


	public void setAttributedsales30dsamesku(BigDecimal attributedsales30dsamesku) {
		this.attributedsales30dsamesku = attributedsales30dsamesku;
	}


	public Integer getAttributedUnitsOrdered1dSameSKU() {
		return attributedUnitsOrdered1dSameSKU;
	}


	public void setAttributedUnitsOrdered1dSameSKU(Integer attributedUnitsOrdered1dSameSKU) {
		this.attributedUnitsOrdered1dSameSKU = attributedUnitsOrdered1dSameSKU;
	}


	public Integer getAttributedUnitsOrdered7dSameSKU() {
		return attributedUnitsOrdered7dSameSKU;
	}


	public void setAttributedUnitsOrdered7dSameSKU(Integer attributedUnitsOrdered7dSameSKU) {
		this.attributedUnitsOrdered7dSameSKU = attributedUnitsOrdered7dSameSKU;
	}


	public Integer getAttributedUnitsOrdered14dSameSKU() {
		return attributedUnitsOrdered14dSameSKU;
	}


	public void setAttributedUnitsOrdered14dSameSKU(Integer attributedUnitsOrdered14dSameSKU) {
		this.attributedUnitsOrdered14dSameSKU = attributedUnitsOrdered14dSameSKU;
	}


	public Integer getAttributedUnitsOrdered30dSameSKU() {
		return attributedUnitsOrdered30dSameSKU;
	}


	public void setAttributedUnitsOrdered30dSameSKU(Integer attributedUnitsOrdered30dSameSKU) {
		this.attributedUnitsOrdered30dSameSKU = attributedUnitsOrdered30dSameSKU;
	}


	public boolean isZero() {
    	if(attributedsales1dsamesku!=null&&attributedsales1dsamesku.floatValue()>0.001)return false;
    	if(attributedsales7dsamesku!=null&&attributedsales7dsamesku.floatValue()>0.001)return false;
    	if(attributedsales14dsamesku!=null&&attributedsales14dsamesku.floatValue()>0.001)return false;
    	if(attributedsales30dsamesku!=null&&attributedsales30dsamesku.floatValue()>0.001)return false;
    	
    	if(attributedsales1d!=null&&attributedsales1d.floatValue()>0.001)return false;
    	if(attributedsales7d!=null&&attributedsales7d.floatValue()>0.001)return false;
    	if(attributedsales14d!=null&&attributedsales14d.floatValue()>0.001)return false;
    	if(attributedsales30d!=null&&attributedsales30d.floatValue()>0.001)return false;
    	
    	if(attributedconversions1d!=null&&attributedconversions1d!=0)return false;
    	if(attributedconversions7d!=null&&attributedconversions7d!=0)return false;
     	if(attributedconversions14d!=null&&attributedconversions14d!=0)return false;
     	if(attributedconversions30d!=null&&attributedconversions30d!=0)return false;
     	
    	if(attributedconversions1dsamesku!=null&&attributedconversions1dsamesku!=0)return false;
    	if(attributedconversions7dsamesku!=null&&attributedconversions7dsamesku!=0)return false;
     	if(attributedconversions14dsamesku!=null&&attributedconversions14dsamesku!=0)return false;
     	if(attributedconversions30dsamesku!=null&&attributedconversions30dsamesku!=0)return false;
     	
    	if(attributedunitsordered1d!=null&&attributedunitsordered1d!=0)return false;
    	if(attributedunitsordered7d!=null&&attributedunitsordered7d!=0)return false;
     	if(attributedunitsordered14d!=null&&attributedunitsordered14d!=0)return false;
     	if(attributedunitsordered30d!=null&&attributedunitsordered30d!=0)return false;
     	
    	if(attributedUnitsOrdered1dSameSKU!=null&&attributedUnitsOrdered1dSameSKU!=0)return false;
    	if(attributedUnitsOrdered7dSameSKU!=null&&attributedUnitsOrdered7dSameSKU!=0)return false;
     	if(attributedUnitsOrdered14dSameSKU!=null&&attributedUnitsOrdered14dSameSKU!=0)return false;
     	if(attributedUnitsOrdered30dSameSKU!=null&&attributedUnitsOrdered30dSameSKU!=0)return false;
        return true;
    }
 
}
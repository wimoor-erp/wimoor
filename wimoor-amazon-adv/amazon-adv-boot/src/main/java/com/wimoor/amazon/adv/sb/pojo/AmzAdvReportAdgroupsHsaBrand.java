package com.wimoor.amazon.adv.sb.pojo;

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
@Table(name="t_amz_adv_rpt2_hsa_campaigns_brand")
public class AmzAdvReportAdgroupsHsaBrand {
	
	@Id
	@Column(name="adGroupId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adgroupid;
	
	@Id
	@Column(name="bydate")
    private Date bydate;

	@Column(name="opttime")
    private Date opttime;
	
	@Column(name="attributedDetailPageViewsClicks14d")
	private Integer attributedDetailPageViewsClicks14d;
	
	@Column(name="attributedOrdersNewToBrand14d")
	private Integer attributedOrdersNewToBrand14d;
	
	@Column(name="attributedOrdersNewToBrandPercentage14d")
	private BigDecimal attributedOrdersNewToBrandPercentage14d;
	
	@Column(name="attributedOrderRateNewToBrand14d")
	private Integer attributedOrderRateNewToBrand14d;
	
	@Column(name="attributedSalesNewToBrand14d")
	private Integer attributedSalesNewToBrand14d;
	
	@Column(name="attributedSalesNewToBrandPercentage14d")
	private BigDecimal attributedSalesNewToBrandPercentage14d;
	
	@Column(name="attributedUnitsOrderedNewToBrand14d")
	private Integer attributedUnitsOrderedNewToBrand14d;
	
	@Column(name="attributedUnitsOrderedNewToBrandPercentage14d")
	private BigDecimal attributedUnitsOrderedNewToBrandPercentage14d;
	
	@Column(name="unitsSold14d")
	private Integer unitsSold14d;
	
	@Column(name="dpv14d")
	private Integer dpv14d;

	public BigInteger getAdgroupid() {
		return adgroupid;
	}

	public void setAdgroupid(BigInteger adgroupid) {
		this.adgroupid = adgroupid;
	}

	public Date getBydate() {
		return bydate;
	}

	public void setBydate(Date bydate) {
		this.bydate = bydate;
	}

	public Date getOpttime() {
		return opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}

	public Integer getAttributedDetailPageViewsClicks14d() {
		return attributedDetailPageViewsClicks14d;
	}

	public void setAttributedDetailPageViewsClicks14d(Integer attributedDetailPageViewsClicks14d) {
		this.attributedDetailPageViewsClicks14d = attributedDetailPageViewsClicks14d;
	}

	public Integer getAttributedOrdersNewToBrand14d() {
		return attributedOrdersNewToBrand14d;
	}

	public void setAttributedOrdersNewToBrand14d(Integer attributedOrdersNewToBrand14d) {
		this.attributedOrdersNewToBrand14d = attributedOrdersNewToBrand14d;
	}

	public BigDecimal getAttributedOrdersNewToBrandPercentage14d() {
		return attributedOrdersNewToBrandPercentage14d;
	}

	public void setAttributedOrdersNewToBrandPercentage14d(BigDecimal attributedOrdersNewToBrandPercentage14d) {
		this.attributedOrdersNewToBrandPercentage14d = attributedOrdersNewToBrandPercentage14d;
	}

	public Integer getAttributedOrderRateNewToBrand14d() {
		return attributedOrderRateNewToBrand14d;
	}

	public void setAttributedOrderRateNewToBrand14d(Integer attributedOrderRateNewToBrand14d) {
		this.attributedOrderRateNewToBrand14d = attributedOrderRateNewToBrand14d;
	}

	public Integer getAttributedSalesNewToBrand14d() {
		return attributedSalesNewToBrand14d;
	}

	public void setAttributedSalesNewToBrand14d(Integer attributedSalesNewToBrand14d) {
		this.attributedSalesNewToBrand14d = attributedSalesNewToBrand14d;
	}

	public BigDecimal getAttributedSalesNewToBrandPercentage14d() {
		return attributedSalesNewToBrandPercentage14d;
	}

	public void setAttributedSalesNewToBrandPercentage14d(BigDecimal attributedSalesNewToBrandPercentage14d) {
		this.attributedSalesNewToBrandPercentage14d = attributedSalesNewToBrandPercentage14d;
	}

	public Integer getAttributedUnitsOrderedNewToBrand14d() {
		return attributedUnitsOrderedNewToBrand14d;
	}

	public void setAttributedUnitsOrderedNewToBrand14d(Integer attributedUnitsOrderedNewToBrand14d) {
		this.attributedUnitsOrderedNewToBrand14d = attributedUnitsOrderedNewToBrand14d;
	}

	public BigDecimal getAttributedUnitsOrderedNewToBrandPercentage14d() {
		return attributedUnitsOrderedNewToBrandPercentage14d;
	}

	public void setAttributedUnitsOrderedNewToBrandPercentage14d(BigDecimal attributedUnitsOrderedNewToBrandPercentage14d) {
		this.attributedUnitsOrderedNewToBrandPercentage14d = attributedUnitsOrderedNewToBrandPercentage14d;
	}

	public Integer getUnitsSold14d() {
		return unitsSold14d;
	}

	public void setUnitsSold14d(Integer unitsSold14d) {
		this.unitsSold14d = unitsSold14d;
	}

	public Integer getDpv14d() {
		return dpv14d;
	}

	public void setDpv14d(Integer dpv14d) {
		this.dpv14d = dpv14d;
	}
     
	
	  public boolean iszero() {
		  if(dpv14d!=null&&dpv14d!=0)return false; 
		  if(unitsSold14d!=null&&unitsSold14d!=0)return false; 	
		  if(attributedUnitsOrderedNewToBrand14d!=null&&attributedUnitsOrderedNewToBrand14d!=0)return false; 	
		  if(attributedSalesNewToBrand14d!=null&&attributedSalesNewToBrand14d!=0)return false; 	
		  if(attributedOrderRateNewToBrand14d!=null&&attributedOrderRateNewToBrand14d!=0)return false; 	
		  if(attributedOrdersNewToBrand14d!=null&&attributedOrdersNewToBrand14d!=0)return false; 	
		  if(attributedDetailPageViewsClicks14d!=null&&attributedDetailPageViewsClicks14d!=0)return false; 	
		  if(attributedOrdersNewToBrandPercentage14d!=null&&attributedOrdersNewToBrandPercentage14d.floatValue()>0.001)return false; 	
		  if(attributedSalesNewToBrandPercentage14d!=null&&attributedSalesNewToBrandPercentage14d.floatValue()>0.001)return false; 	
		  if(attributedUnitsOrderedNewToBrandPercentage14d!=null&&attributedUnitsOrderedNewToBrandPercentage14d.floatValue()>0.001)return false; 	
		  return true;
	  }
}
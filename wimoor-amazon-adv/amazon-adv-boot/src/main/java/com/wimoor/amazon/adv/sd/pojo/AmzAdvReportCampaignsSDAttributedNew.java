package com.wimoor.amazon.adv.sd.pojo;

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
@Table(name="t_amz_adv_rpt2_sd_campaigns_attributed_new")
public class AmzAdvReportCampaignsSDAttributedNew {
	
	@Id
	@Column(name="campaignId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger campaignid;

	@Id
	@Column(name="bydate")
	private Date bydate;

	@Column(name="attributedOrdersNewToBrand14d")
    private Integer attributedOrdersNewToBrand14d;
	 
	@Column(name="attributedSalesNewToBrand14d")
    private BigDecimal attributedSalesNewToBrand14d;
	 
	@Column(name="attributedUnitsOrderedNewToBrand14d")
    private Integer attributedUnitsOrderedNewToBrand14d;
	
    public BigInteger getCampaignid() {
        return campaignid;
    }

    public void setCampaignid(BigInteger campaignid) {
        this.campaignid = campaignid;
    }

    public Date getBydate() {
        return bydate;
    }

    public void setBydate(Date bydate) {
        this.bydate = bydate;
    }

 
    public Integer getAttributedOrdersNewToBrand14d() {
		return attributedOrdersNewToBrand14d;
	}

	public void setAttributedOrdersNewToBrand14d(Integer attributedOrdersNewToBrand14d) {
		this.attributedOrdersNewToBrand14d = attributedOrdersNewToBrand14d;
	}

	public BigDecimal getAttributedSalesNewToBrand14d() {
		return attributedSalesNewToBrand14d;
	}

	public void setAttributedSalesNewToBrand14d(BigDecimal attributedSalesNewToBrand14d) {
		this.attributedSalesNewToBrand14d = attributedSalesNewToBrand14d;
	}

	public Integer getAttributedUnitsOrderedNewToBrand14d() {
		return attributedUnitsOrderedNewToBrand14d;
	}

	public void setAttributedUnitsOrderedNewToBrand14d(Integer attributedUnitsOrderedNewToBrand14d) {
		this.attributedUnitsOrderedNewToBrand14d = attributedUnitsOrderedNewToBrand14d;
	}

	
	public boolean isZero() {
 
	  
	  if(attributedOrdersNewToBrand14d!=null&&attributedOrdersNewToBrand14d!=0)return false; 	
	  if(attributedUnitsOrderedNewToBrand14d!=null&&attributedUnitsOrderedNewToBrand14d!=0)return false; 
	  if(attributedSalesNewToBrand14d!=null&&attributedSalesNewToBrand14d.floatValue()>0.001)return false; 	
	  
	  return true;
  
	  }
}
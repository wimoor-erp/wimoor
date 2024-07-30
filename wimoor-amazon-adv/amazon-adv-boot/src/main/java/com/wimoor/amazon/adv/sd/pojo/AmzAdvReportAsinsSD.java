package com.wimoor.amazon.adv.sd.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="t_amz_adv_rpt2_sd_asins")
public class AmzAdvReportAsinsSD {
	@Id
	@Column(name="bydate")
    private Date bydate;

	@Id
	@Column(name="asin")
    private String asin;

	@Id
	@Column(name="otherAsin")
    private String otherasin;

    
	@Column(name="campaignId")
    private BigInteger campaignid;

	@Id
	@Column(name="adGroupId")
    private BigInteger adgroupid;

	@Column(name="sku")
    private String sku;

	@Column(name="profileid")
    private BigInteger profileid;
 
	@Column(name="opttime")
    private Date opttime;

	@Column(name="attributedUnitsOrdered1dOtherSKU ")
    private Integer attributedUnitsOrdered1dOtherSKU;

	@Column(name="attributedUnitsOrdered7dOtherSKU ")
    private Integer attributedUnitsOrdered7dOtherSKU;

	@Column(name="attributedUnitsOrdered14dOtherSKU ")
    private Integer attributedUnitsOrdered14dOtherSKU;

	@Column(name="attributedUnitsOrdered30dOtherSKU")
    private Integer attributedUnitsOrdered30dOtherSKU;

	@Column(name="attributedSales1dOtherSKU ")
    private BigDecimal attributedSales1dOtherSKU ;

	@Column(name="attributedSales7dOtherSKU ")
    private BigDecimal attributedSales7dOtherSKU ;

	@Column(name="attributedSales14dOtherSKU ")
    private BigDecimal attributedSales14dOtherSKU ;

	@Column(name="attributedSales30dOtherSKU ")
    private BigDecimal attributedSales30dOtherSKU ;

 

	
    public Date getOpttime() {
		return opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}

	public Date getBydate() {
        return bydate;
    }

    public void setBydate(Date bydate) {
        this.bydate = bydate;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin == null ? null : asin.trim();
    }

    public String getOtherasin() {
        return otherasin;
    }

    public void setOtherasin(String otherasin) {
        this.otherasin = otherasin == null ? null : otherasin.trim();
    }

    
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public BigInteger getProfileid() {
        return profileid;
    }

    public void setProfileid(BigInteger profileid) {
        this.profileid = profileid;
    }

	public Integer getAttributedUnitsOrdered1dOtherSKU() {
		return attributedUnitsOrdered1dOtherSKU;
	}

	public void setAttributedUnitsOrdered1dOtherSKU(Integer attributedUnitsOrdered1dOtherSKU) {
		this.attributedUnitsOrdered1dOtherSKU = attributedUnitsOrdered1dOtherSKU;
	}

	public Integer getAttributedUnitsOrdered7dOtherSKU() {
		return attributedUnitsOrdered7dOtherSKU;
	}

	public void setAttributedUnitsOrdered7dOtherSKU(Integer attributedUnitsOrdered7dOtherSKU) {
		this.attributedUnitsOrdered7dOtherSKU = attributedUnitsOrdered7dOtherSKU;
	}

	public Integer getAttributedUnitsOrdered14dOtherSKU() {
		return attributedUnitsOrdered14dOtherSKU;
	}

	public void setAttributedUnitsOrdered14dOtherSKU(Integer attributedUnitsOrdered14dOtherSKU) {
		this.attributedUnitsOrdered14dOtherSKU = attributedUnitsOrdered14dOtherSKU;
	}

	public Integer getAttributedUnitsOrdered30dOtherSKU() {
		return attributedUnitsOrdered30dOtherSKU;
	}

	public void setAttributedUnitsOrdered30dOtherSKU(Integer attributedUnitsOrdered30dOtherSKU) {
		this.attributedUnitsOrdered30dOtherSKU = attributedUnitsOrdered30dOtherSKU;
	}

	public BigDecimal getAttributedSales1dOtherSKU() {
		return attributedSales1dOtherSKU;
	}

	public void setAttributedSales1dOtherSKU(BigDecimal attributedSales1dOtherSKU) {
		this.attributedSales1dOtherSKU = attributedSales1dOtherSKU;
	}

	public BigDecimal getAttributedSales7dOtherSKU() {
		return attributedSales7dOtherSKU;
	}

	public void setAttributedSales7dOtherSKU(BigDecimal attributedSales7dOtherSKU) {
		this.attributedSales7dOtherSKU = attributedSales7dOtherSKU;
	}

	public BigDecimal getAttributedSales14dOtherSKU() {
		return attributedSales14dOtherSKU;
	}

	public void setAttributedSales14dOtherSKU(BigDecimal attributedSales14dOtherSKU) {
		this.attributedSales14dOtherSKU = attributedSales14dOtherSKU;
	}

	public BigDecimal getAttributedSales30dOtherSKU() {
		return attributedSales30dOtherSKU;
	}

	public void setAttributedSales30dOtherSKU(BigDecimal attributedSales30dOtherSKU) {
		this.attributedSales30dOtherSKU = attributedSales30dOtherSKU;
	}

    public boolean isZero() {
    	if(attributedSales30dOtherSKU!=null&&attributedSales30dOtherSKU.floatValue()>0.001)return false;
    	if(attributedSales14dOtherSKU!=null&&attributedSales14dOtherSKU.floatValue()>0.001)return false;
    	if(attributedSales7dOtherSKU!=null&&attributedSales7dOtherSKU.floatValue()>0.001)return false;
    	if(attributedSales1dOtherSKU!=null&&attributedSales1dOtherSKU.floatValue()>0.001)return false;
    	
    	if(attributedUnitsOrdered30dOtherSKU!=null&&attributedUnitsOrdered30dOtherSKU!=0)return false;
    	if(attributedUnitsOrdered14dOtherSKU!=null&&attributedUnitsOrdered14dOtherSKU!=0)return false;
     	if(attributedUnitsOrdered7dOtherSKU!=null&&attributedUnitsOrdered7dOtherSKU!=0)return false;
     	if(attributedUnitsOrdered1dOtherSKU!=null&&attributedUnitsOrdered1dOtherSKU!=0)return false;
     	
    	if(attributedUnitsOrdered30dOtherSKU!=null&&attributedUnitsOrdered30dOtherSKU!=0)return false;
    	if(attributedUnitsOrdered14dOtherSKU!=null&&attributedUnitsOrdered14dOtherSKU!=0)return false;
     	if(attributedUnitsOrdered7dOtherSKU!=null&&attributedUnitsOrdered7dOtherSKU!=0)return false;
     	if(attributedUnitsOrdered1dOtherSKU!=null&&attributedUnitsOrdered1dOtherSKU!=0)return false;
        return true;
    }
   
}
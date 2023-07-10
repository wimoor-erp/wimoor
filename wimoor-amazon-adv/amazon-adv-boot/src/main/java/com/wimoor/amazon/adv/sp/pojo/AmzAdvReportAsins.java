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
	@Column(name="targetId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger targetid;
	
	@Id
	@Column(name="bydate")
    private Date bydate;
	
	@Column(name="sku")
    private String sku;

	@Id
	@Column(name="asin")
    private String asin;
	
	@Id
	@Column(name="otherAsin")
    private String otherasin;
	
	@Column(name="opttime")
    private Date opttime;
	
	@Column(name="profileid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger profileid;
	
	@Column(name="attributedUnitsOrdered1d")
    private Integer attributedUnitsOrdered1d;
	
	@Column(name="attributedUnitsOrdered7d")
    private Integer attributedUnitsOrdered7d;
	
	@Column(name="attributedUnitsOrdered14d")
    private Integer attributedUnitsOrdered14d;
	
	@Column(name="attributedUnitsOrdered30d")
    private Integer attributedUnitsOrdered30d;
	
	@Column(name="attributedUnitsOrdered1dOtherSKU")
    private Integer attributedunitsordered1dothersku;

	@Column(name="attributedUnitsOrdered7dOtherSKU")
    private Integer attributedunitsordered7dothersku;

	@Column(name="attributedUnitsOrdered14dOtherSKU")
    private Integer attributedunitsordered14dothersku;

	@Column(name="attributedUnitsOrdered30dOtherSKU")
    private Integer attributedunitsordered30dothersku;

	@Column(name="attributedSales1dOtherSKU")
    private BigDecimal attributedsales1dothersku;

	@Column(name="attributedSales7dOtherSKU")
    private BigDecimal attributedsales7dothersku;

	@Column(name="attributedSales14dOtherSKU")
    private BigDecimal attributedsales14dothersku;

	@Column(name="attributedSales30dOtherSKU")
    private BigDecimal attributedsales30dothersku;

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

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getOtherasin() {
        return otherasin;
    }

    public void setOtherasin(String otherasin) {
        this.otherasin = otherasin == null ? null : otherasin.trim();
    }

    public BigInteger getProfileid() {
        return profileid;
    }

    public void setProfileid(BigInteger profileid) {
        this.profileid = profileid  ;
    }

 
    public BigDecimal getAttributedsales1dothersku() {
        return attributedsales1dothersku;
    }

    public void setAttributedsales1dothersku(BigDecimal attributedsales1dothersku) {
        this.attributedsales1dothersku = attributedsales1dothersku;
    }

    public BigDecimal getAttributedsales7dothersku() {
        return attributedsales7dothersku;
    }

    public void setAttributedsales7dothersku(BigDecimal attributedsales7dothersku) {
        this.attributedsales7dothersku = attributedsales7dothersku;
    }

    public BigDecimal getAttributedsales14dothersku() {
        return attributedsales14dothersku;
    }

    public void setAttributedsales14dothersku(BigDecimal attributedsales14dothersku) {
        this.attributedsales14dothersku = attributedsales14dothersku;
    }

    public BigDecimal getAttributedsales30dothersku() {
        return attributedsales30dothersku;
    }

    public void setAttributedsales30dothersku(BigDecimal attributedsales30dothersku) {
        this.attributedsales30dothersku = attributedsales30dothersku;
    }

	public Integer getAttributedUnitsOrdered1d() {
		return attributedUnitsOrdered1d;
	}

	public void setAttributedUnitsOrdered1d(Integer attributedUnitsOrdered1d) {
		this.attributedUnitsOrdered1d = attributedUnitsOrdered1d;
	}

	public Integer getAttributedUnitsOrdered7d() {
		return attributedUnitsOrdered7d;
	}

	public void setAttributedUnitsOrdered7d(Integer attributedUnitsOrdered7d) {
		this.attributedUnitsOrdered7d = attributedUnitsOrdered7d;
	}

	public Integer getAttributedUnitsOrdered14d() {
		return attributedUnitsOrdered14d;
	}

	public void setAttributedUnitsOrdered14d(Integer attributedUnitsOrdered14d) {
		this.attributedUnitsOrdered14d = attributedUnitsOrdered14d;
	}

	public Integer getAttributedUnitsOrdered30d() {
		return attributedUnitsOrdered30d;
	}

	public void setAttributedUnitsOrdered30d(Integer attributedUnitsOrdered30d) {
		this.attributedUnitsOrdered30d = attributedUnitsOrdered30d;
	}

	public Integer getAttributedunitsordered1dothersku() {
		return attributedunitsordered1dothersku;
	}

	public void setAttributedunitsordered1dothersku(Integer attributedunitsordered1dothersku) {
		this.attributedunitsordered1dothersku = attributedunitsordered1dothersku;
	}

	public Integer getAttributedunitsordered7dothersku() {
		return attributedunitsordered7dothersku;
	}

	public void setAttributedunitsordered7dothersku(Integer attributedunitsordered7dothersku) {
		this.attributedunitsordered7dothersku = attributedunitsordered7dothersku;
	}

	public Integer getAttributedunitsordered14dothersku() {
		return attributedunitsordered14dothersku;
	}

	public void setAttributedunitsordered14dothersku(Integer attributedunitsordered14dothersku) {
		this.attributedunitsordered14dothersku = attributedunitsordered14dothersku;
	}

	public Integer getAttributedunitsordered30dothersku() {
		return attributedunitsordered30dothersku;
	}

	public void setAttributedunitsordered30dothersku(Integer attributedunitsordered30dothersku) {
		this.attributedunitsordered30dothersku = attributedunitsordered30dothersku;
	}

	public Date getOpttime() {
		return opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}
    
    public BigInteger getTargetid() {
    	if(targetid==null) {
    		return new BigInteger("0");
    	}
		return targetid;
	}

	public void setTargetid(BigInteger targetid) {
		this.targetid = targetid;
	}

	public boolean isZero() {
    	if(attributedsales30dothersku!=null&&attributedsales30dothersku.floatValue()>0.001)return false;
    	if(attributedsales14dothersku!=null&&attributedsales14dothersku.floatValue()>0.001)return false;
    	if(attributedsales7dothersku!=null&&attributedsales7dothersku.floatValue()>0.001)return false;
    	if(attributedsales1dothersku!=null&&attributedsales1dothersku.floatValue()>0.001)return false;
    	
    	if(attributedunitsordered30dothersku!=null&&attributedunitsordered30dothersku!=0)return false;
    	if(attributedunitsordered14dothersku!=null&&attributedunitsordered14dothersku!=0)return false;
     	if(attributedunitsordered7dothersku!=null&&attributedunitsordered7dothersku!=0)return false;
     	if(attributedunitsordered1dothersku!=null&&attributedunitsordered1dothersku!=0)return false;
     	
    	if(attributedUnitsOrdered30d!=null&&attributedUnitsOrdered30d!=0)return false;
    	if(attributedUnitsOrdered14d!=null&&attributedUnitsOrdered14d!=0)return false;
     	if(attributedUnitsOrdered7d!=null&&attributedUnitsOrdered7d!=0)return false;
     	if(attributedUnitsOrdered1d!=null&&attributedUnitsOrdered1d!=0)return false;
        return true;
    }
 
}
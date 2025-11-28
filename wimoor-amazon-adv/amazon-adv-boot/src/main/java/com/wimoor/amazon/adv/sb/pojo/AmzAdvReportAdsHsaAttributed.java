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
@Table(name="t_amz_adv_rpt2_hsa_ads_attributed")
public class AmzAdvReportAdsHsaAttributed  {
	
	@Id
	@Column(name="adId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adid;
	
	@Id
	@Column(name="bydate")
    private Date bydate;
 
	@Column(name="attributedSales14d")
    private BigDecimal attributedsales14d;

	@Column(name="attributedSales14dSameSKU")
    private BigDecimal attributedsales14dsamesku;

	@Column(name="attributedConversions14d")
    private Integer attributedconversions14d;

	@Column(name="attributedconversions14dsamesku")
    private Integer attributedconversions14dsamesku;

	
    public BigInteger getAdid() {
		return adid;
	}

	public void setAdid(BigInteger adid) {
		this.adid = adid;
	}

	public Date getBydate() {
		return bydate;
	}

	public void setBydate(Date bydate) {
		this.bydate = bydate;
	}
 
    public BigDecimal getAttributedsales14d() {
        return attributedsales14d;
    }

    public void setAttributedsales14d(BigDecimal attributedsales14d) {
        this.attributedsales14d = attributedsales14d;
    }

    public BigDecimal getAttributedsales14dsamesku() {
        return attributedsales14dsamesku;
    }

    public void setAttributedsales14dsamesku(BigDecimal attributedsales14dsamesku) {
        this.attributedsales14dsamesku = attributedsales14dsamesku;
    }

    public Integer getAttributedconversions14d() {
        return attributedconversions14d;
    }

    public void setAttributedconversions14d(Integer attributedconversions14d) {
        this.attributedconversions14d = attributedconversions14d;
    }

    public Integer getAttributedconversions14dsamesku() {
        return attributedconversions14dsamesku;
    }

    public void setAttributedconversions14dsamesku(Integer attributedconversions14dsamesku) {
        this.attributedconversions14dsamesku = attributedconversions14dsamesku;
    }

    public boolean isZero() {
    	if(attributedsales14d!=null&&attributedsales14d.floatValue()>0.001)return false;
    	if(attributedsales14dsamesku!=null&&attributedsales14dsamesku.floatValue()>0.001)return false;
    	if(attributedconversions14d!=null&&attributedconversions14d!=0)return false;
    	if(attributedconversions14dsamesku!=null&&attributedconversions14dsamesku!=0)return false;
        return true;
    }
}
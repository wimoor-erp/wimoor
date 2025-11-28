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
@Table(name="t_amz_adv_rpt2_adgroups_sd_attributed")
public class AmzAdvReportAdGroupsSDAttributed   {
	
	@Id
	@Column(name="bydate")
    private Date bydate;

	@Id
	@Column(name="adGroupId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adgroupid;


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

 
	
	
    public Date getBydate() {
        return bydate;
    }

    public void setBydate(Date bydate) {
        this.bydate = bydate;
    }



    public BigInteger getAdgroupid() {
		return adgroupid;
	}

	public void setAdgroupid(BigInteger adgroupid) {
		this.adgroupid = adgroupid;
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

	public boolean isZero() {
	  if(attributedconversions1d!=null&&attributedconversions1d!=0)return false; 
	  if(attributedconversions7d!=null&&attributedconversions7d!=0)return false; 	
	  if(attributedconversions14d!=null&&attributedconversions14d!=0)return false; 	
	  if(attributedconversions30d!=null&&attributedconversions30d!=0)return false; 	
	  
	  
	  
	  if(attributedunitsordered1d!=null&&attributedunitsordered1d!=0)return false; 	
	  if(attributedunitsordered7d!=null&&attributedunitsordered7d!=0)return false; 	
	  if(attributedunitsordered14d!=null&&attributedunitsordered14d!=0)return false; 	
	  if(attributedunitsordered30d!=null&&attributedunitsordered30d!=0)return false; 
	  
	  if(attributedsales1d!=null&&attributedsales1d.floatValue()>0.001)return false; 	
	  if(attributedsales7d!=null&&attributedsales7d.floatValue()>0.001)return false; 	
	  if(attributedsales14d!=null&&attributedsales14d.floatValue()>0.001)return false; 	
	  if(attributedsales30d!=null&&attributedsales30d.floatValue()>0.001)return false; 
	  
	   
	  return true;
  
	  }
}
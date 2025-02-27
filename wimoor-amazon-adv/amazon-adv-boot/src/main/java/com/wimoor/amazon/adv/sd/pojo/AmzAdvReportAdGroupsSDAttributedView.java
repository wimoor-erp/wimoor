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
@Table(name="t_amz_adv_rpt2_adgroups_sd_attributed_view")
public class AmzAdvReportAdGroupsSDAttributedView   {
	
	@Id
	@Column(name="bydate")
    private Date bydate;

	@Id
	@Column(name="adGroupId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adgroupid;


	@Column(name="viewImpressions")
    private Integer viewImpressions;

	@Column(name="viewAttributedConversions14d")
    private Integer viewAttributedConversions14d ;

	@Column(name="viewAttributedSales14d")
    private BigDecimal viewAttributedSales14d ;

 
	@Column(name="viewAttributedUnitsOrdered14d")
    private Integer viewAttributedUnitsOrdered14d ;
	
	
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

 
	public Integer getViewImpressions() {
		return viewImpressions;
	}

	public void setViewImpressions(Integer viewImpressions) {
		this.viewImpressions = viewImpressions;
	}

	public Integer getViewAttributedConversions14d() {
		return viewAttributedConversions14d;
	}

	public void setViewAttributedConversions14d(Integer viewAttributedConversions14d) {
		this.viewAttributedConversions14d = viewAttributedConversions14d;
	}

	public BigDecimal getViewAttributedSales14d() {
		return viewAttributedSales14d;
	}

	public void setViewAttributedSales14d(BigDecimal viewAttributedSales14d) {
		this.viewAttributedSales14d = viewAttributedSales14d;
	}

	public Integer getViewAttributedUnitsOrdered14d() {
		return viewAttributedUnitsOrdered14d;
	}

	public void setViewAttributedUnitsOrdered14d(Integer viewAttributedUnitsOrdered14d) {
		this.viewAttributedUnitsOrdered14d = viewAttributedUnitsOrdered14d;
	}

	public boolean isZero() {
	  if(viewImpressions!=null&&viewImpressions!=0)return false; 
	  if(viewAttributedConversions14d!=null&&viewAttributedConversions14d!=0)return false; 	
	  if(viewAttributedUnitsOrdered14d!=null&&viewAttributedUnitsOrdered14d!=0)return false; 	
	  if(viewAttributedSales14d!=null&&viewAttributedSales14d.floatValue()>0.001)return false; 	
	  return true;
  
	  }
}
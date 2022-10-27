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
@Table(name="t_amz_adv_rpt2_sd_product_targets_attributed_new")
public class AmzAdvReportProductTargetsSDAttributedNew {
	@Id
	@Column(name="bydate")
	private Date bydate;

	@Id
	@Column(name="targetId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
	private BigInteger targetid;

	@Column(name="attributedUnitsOrderedNewToBrand14d")
    private Integer attributedUnitsOrderedNewToBrand14d;
	
	@Column(name="attributedOrdersNewToBrand14d")
    private Integer attributedOrdersNewToBrand14d;
	
	@Column(name="attributedSalesNewToBrand14d")
    private BigDecimal attributedSalesNewToBrand14d;
 
    /**
	 * @return the bydate
	 */
	public Date getBydate() {
		return bydate;
	}

	/**
	 * @param bydate the bydate to set
	 */
	public void setBydate(Date bydate) {
		this.bydate = bydate;
	}

	/**
	 * @return the targetid
	 */
	public BigInteger getTargetid() {
		return targetid;
	}

	/**
	 * @param targetid the targetid to set
	 */
	public void setTargetid(BigInteger targetid) {
		this.targetid = targetid;
	}

 
    public Integer getAttributedUnitsOrderedNewToBrand14d() {
		return attributedUnitsOrderedNewToBrand14d;
	}

	public void setAttributedUnitsOrderedNewToBrand14d(Integer attributedUnitsOrderedNewToBrand14d) {
		this.attributedUnitsOrderedNewToBrand14d = attributedUnitsOrderedNewToBrand14d;
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

	public boolean isZero() {
    	  if(attributedOrdersNewToBrand14d!=null&&attributedOrdersNewToBrand14d!=0)return false; 	
    	  if(attributedUnitsOrderedNewToBrand14d!=null&&attributedUnitsOrderedNewToBrand14d!=0)return false; 
    	  if(attributedSalesNewToBrand14d!=null&&attributedSalesNewToBrand14d.floatValue()>0.001)return false; 	
    	  
    	  return true;
      
    	  }
}
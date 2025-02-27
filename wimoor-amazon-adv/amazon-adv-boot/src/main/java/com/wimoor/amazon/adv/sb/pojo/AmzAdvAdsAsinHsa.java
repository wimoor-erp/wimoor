package com.wimoor.amazon.adv.sb.pojo;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name="t_amz_adv_ads_asin_hsa")
public class AmzAdvAdsAsinHsa  {
	
	@Id
	@Column(name="adid")
    private BigInteger adid;
	
	
	@Id
	@Column(name="asin")
    private String asin;


	public BigInteger getAdid() {
		return adid;
	}


	public void setAdid(BigInteger adid) {
		this.adid = adid;
	}


	public String getAsin() {
		return asin;
	}


	public void setAsin(String asin) {
		this.asin = asin;
	}
 
    
    
}
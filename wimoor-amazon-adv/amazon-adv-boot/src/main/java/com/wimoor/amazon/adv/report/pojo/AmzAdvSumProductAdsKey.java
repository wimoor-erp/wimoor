package com.wimoor.amazon.adv.report.pojo;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

public class AmzAdvSumProductAdsKey {
	@Id
	@Column(name="profileid")
    private BigInteger profileid;

	@Id
	@Column(name="byday")
    private Date byday;

	@Id
	@Column(name="ctype")
    private String ctype;
	
    public BigInteger getProfileid() {
        return profileid;
    }

    public void setProfileid(BigInteger profileid) {
        this.profileid = profileid;
    }

    public Date getByday() {
        return byday;
    }

    public void setByday(Date byday) {
        this.byday = byday;
    }

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}
    
    
}
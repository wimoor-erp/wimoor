package com.wimoor.amazon.adv.report.pojo;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Id;

public class AmzAdvRequestKey {
	@Id
	@Column(name="reportId")
    private String reportid;
	
	@Id
	@Column(name="profileId")
    private BigInteger profileid;

    public String getReportid() {
        return reportid;
    }

    public void setReportid(String reportid) {
        this.reportid = reportid == null ? null : reportid.trim();
    }

	public BigInteger getProfileid() {
		return profileid;
	}

	public void setProfileid(BigInteger profileid) {
		this.profileid = profileid;
	}

 
}
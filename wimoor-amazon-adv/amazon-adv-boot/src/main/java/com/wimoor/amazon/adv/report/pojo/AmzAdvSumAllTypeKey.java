package com.wimoor.amazon.adv.report.pojo;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

public class AmzAdvSumAllTypeKey {
	@Id
	@Column(name="profileid")
    private BigInteger profileid;

	@Id
	@Column(name="state")
    private String state;

	@Id
	@Column(name="campaignType")
    private String campaigntype;

	@Id
	@Column(name="recordType")
    private String recordtype;

	@Id
	@Column(name="byday")
    private Date byday;

    public BigInteger getProfileid() {
        return profileid;
    }

    public void setProfileid(BigInteger profileid) {
        this.profileid = profileid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getCampaigntype() {
        return campaigntype;
    }

    public void setCampaigntype(String campaigntype) {
        this.campaigntype = campaigntype == null ? null : campaigntype.trim();
    }

    public String getRecordtype() {
        return recordtype;
    }

    public void setRecordtype(String recordtype) {
        this.recordtype = recordtype == null ? null : recordtype.trim();
    }

    public Date getByday() {
        return byday;
    }

    public void setByday(Date byday) {
        this.byday = byday;
    }
}